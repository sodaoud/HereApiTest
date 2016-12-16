package io.sodaoud.heretest.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.sodaoud.heretest.app.BuildConfig;
import io.sodaoud.heretest.app.HereTestApplication;
import io.sodaoud.heretest.app.R;
import io.sodaoud.heretest.app.model.Place;
import io.sodaoud.heretest.app.model.Route;
import io.sodaoud.heretest.app.model.RouteResult;
import io.sodaoud.heretest.app.network.RouteService;
import io.sodaoud.heretest.app.util.Util;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static io.sodaoud.heretest.app.ui.SearchActivity.PLACE;

public class RouteActivity extends AppCompatActivity {

    private static final String TAG = RouteActivity.class.getName();
    private static final int FROM_REQ_CODE = 98;
    private static final int TO_REQ_CODE = 99;
    public static final String ROUTE = "ROUTE";

    @Inject
    RouteService routeService;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.from_tv)
    TextView fromTv;
    @BindView(R.id.to_tv)
    TextView toTv;

    private RouteAdapter adapter;

    private Place from;
    private Place to;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        ((HereTestApplication) getApplication()).getComponent().inject(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter = new RouteAdapter();
        adapter.getPositionClicks().subscribe(route -> showRouteOnMap(route));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void showRouteOnMap(Route route) {
        Intent intent = this.getIntent();
        intent.putExtra(ROUTE, route);
        this.setResult(RESULT_OK, intent);
        finish();
    }

    private void calculateRoute() {
        if (from != null && to != null)
            routeService.calculateRoute(
                    BuildConfig.appId,
                    BuildConfig.appCode,
                    Util.getPlace(from),
                    Util.getPlace(to),
                    "fastest;car",
                    5,
                    "text",
                    "shape,labels,bb").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(route -> showRoute(route),
                            error -> showError(error));
    }

    @OnClick(R.id.from_tv)
    public void fromClick(View v) {
        launchSearchActivity(FROM_REQ_CODE);
    }

    @OnClick(R.id.to_tv)
    public void toClick(View v) {
        launchSearchActivity(TO_REQ_CODE);
    }


    @OnClick(R.id.swap)
    public void swap(View v) {
        Place tmp = from;
        from = to;
        to = tmp;
        updateTextViews();
    }

    private void launchSearchActivity(int reqCode) {
        Intent i = new Intent(this, SearchActivity.class);
        startActivityForResult(i, reqCode);
    }

    private void showRoute(RouteResult route) {
        adapter.setRoutes(route.getRoutes());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void showError(Throwable error) {
        Log.e(TAG, "Error occurred", error);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == FROM_REQ_CODE) {
                from = data.getParcelableExtra(PLACE);
            }

            if (requestCode == TO_REQ_CODE) {
                to = data.getParcelableExtra(PLACE);
            }
            updateTextViews();
            calculateRoute();
        }
    }

    void updateTextViews() {
        toTv.setText(to != null ? to.getTitle() : "");
        fromTv.setText(from != null ? from.getTitle() : "");
    }
}
