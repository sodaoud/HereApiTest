package io.sodaoud.heretest.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.sodaoud.heretest.app.HereTestApplication;
import io.sodaoud.heretest.app.R;
import io.sodaoud.heretest.app.location.LocationProvider;
import io.sodaoud.heretest.app.model.Place;
import io.sodaoud.heretest.app.network.PlacesService;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getName();
    public static final String PLACE = "PLACE";
    public static final String BBOX = "BBOX";

    @Inject
    PlacesService placesService;

    @Inject
    LocationProvider provider;

    @BindView(R.id.floating_search_view)
    FloatingSearchView mSearchView;

    @BindView(R.id.search_results_list)
    RecyclerView mSearchResultsList;

    private SearchAdapter mSearchResultsAdapter;
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setExtras(getIntent().getExtras());
        ((HereTestApplication) getApplication()).getComponent().inject(this);
        ButterKnife.bind(this);
        setupFloatingSearch();
        setupResultsList();
    }

    private void setupFloatingSearch() {
        mSearchView.setOnQueryChangeListener((oldQuery, newQuery) -> {

                    if (!oldQuery.equals("") && newQuery.equals("")) {
                        mSearchView.clearSuggestions();
                    } else {
                        mSearchView.showProgress();

                        if (subscription != null && subscription.isUnsubscribed())
                            subscription.unsubscribe();

                        subscription = placesService.autoSuggest("36.7323,3.1454", newQuery, "plain", 4)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(autoSuggestResult -> {
                                            mSearchView.swapSuggestions(Arrays.asList(autoSuggestResult.getResults()));
                                            mSearchView.hideProgress();
                                        },
                                        error -> showError(error));

                    }
                }
        );

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

                finishAndSendResult((Place) searchSuggestion);
            }

            @Override
            public void onSearchAction(String query) {

                if (subscription != null && subscription.isUnsubscribed())
                    subscription.unsubscribe();

                subscription = placesService.searchPlace("52.5310,13.3848", query, "plain")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(placesResult -> mSearchResultsAdapter.setPlaces(placesResult.getPlaces()),
                                error -> showError(error));
            }
        });

        mSearchView.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.action_location) {
                if (provider.getLocation() != null) {
                    Place p = new Place("Your Position", provider.getLocation());
                    finishAndSendResult(p);
                }
            }
        });

        mSearchView.setOnHomeActionClickListener(this::finish);
//

        mSearchView.setOnSuggestionsListHeightChanged(newHeight ->
                mSearchResultsList.setTranslationY(newHeight));
    }

    private void finishAndSendResult(Place place) {
        Intent intent = this.getIntent();
        intent.putExtra(PLACE, place);
        this.setResult(RESULT_OK, intent);
        finish();
    }

    private void showError(Throwable error) {
        error.printStackTrace();
    }

    private void setupResultsList() {
        mSearchResultsAdapter = new SearchAdapter();
        mSearchResultsAdapter.getPositionClicks().subscribe(this::finishAndSendResult);
        mSearchResultsList.setAdapter(mSearchResultsAdapter);
        mSearchResultsList.setLayoutManager(new LinearLayoutManager(this));
    }

    public void setExtras(Bundle extras) {
    }
}
