package io.sodaoud.heretest.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.sodaoud.heretest.app.HereTestApplication;
import io.sodaoud.heretest.app.R;
import io.sodaoud.heretest.app.model.Route;
import io.sodaoud.heretest.app.presenter.RoutePresenter;
import io.sodaoud.heretest.app.ui.adapter.RouteAdapter;
import io.sodaoud.heretest.app.view.RouteView;

import static io.sodaoud.heretest.app.ui.SearchActivity.PLACE;

public class RouteActivity extends AppCompatActivity implements RouteView {

    private static final String TAG = RouteActivity.class.getName();
    private static final int FROM_REQ_CODE = 98;
    private static final int TO_REQ_CODE = 99;
    public static final String ROUTE = "ROUTE";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progress)
    View progress;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.from_tv)
    TextView fromTv;
    @BindView(R.id.to_tv)
    TextView toTv;

    private RouteAdapter adapter;
    private RoutePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new RoutePresenter(this);
        presenter.init(((HereTestApplication) getApplicationContext()).getComponent());
        initRecyclerView();
    }

    private void initRecyclerView() {
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
        presenter.swapPlaces();
    }

    private void launchSearchActivity(int reqCode) {
        Intent i = new Intent(this, SearchActivity.class);
        i.putExtras(getIntent());
        startActivityForResult(i, reqCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == FROM_REQ_CODE) {
                presenter.setFrom(data.getParcelableExtra(PLACE));
            }

            if (requestCode == TO_REQ_CODE) {
                presenter.setTo(data.getParcelableExtra(PLACE));
            }
        }
    }

    @Override
    public void showProgress(boolean show) {
        progress.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }


    @Override
    public void setItems(Route[] items) {
        adapter.setRoutes(items);
    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void setFromText(String from) {
        fromTv.setText(from);
    }

    @Override
    public void setToText(String to) {
        toTv.setText(to);
    }
}
