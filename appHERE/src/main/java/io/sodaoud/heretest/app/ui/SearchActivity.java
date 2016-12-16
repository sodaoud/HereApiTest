package io.sodaoud.heretest.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.sodaoud.heretest.app.HereTestApplication;
import io.sodaoud.heretest.app.R;
import io.sodaoud.heretest.app.model.Place;
import io.sodaoud.heretest.app.model.PlaceResult;
import io.sodaoud.heretest.app.presenter.SearchPresenter;
import io.sodaoud.heretest.app.ui.adapter.SearchAdapter;
import io.sodaoud.heretest.app.view.SearchPlaceView;

public class SearchActivity extends AppCompatActivity implements SearchPlaceView {

    private static final String TAG = SearchActivity.class.getName();
    public static final String PLACE = "PLACE";
    public static final String BBOX = "BBOX";


    @BindView(R.id.floating_search_view)
    FloatingSearchView mSearchView;

    @BindView(R.id.search_results_list)
    RecyclerView mSearchResultsList;

    private SearchAdapter mSearchResultsAdapter;

    private SearchPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        presenter = new SearchPresenter(this);
        presenter.init(((HereTestApplication) getApplication()).getComponent());
        setExtras(getIntent().getExtras());
        setupFloatingSearch();
        setupResultsList();
    }

    private void setupFloatingSearch() {
        mSearchView.setSearchFocused(true);
        mSearchView.setOnQueryChangeListener((oldQuery, newQuery) -> {
                    if (!oldQuery.equals("") && newQuery.equals("")) {
                        mSearchView.clearSuggestions();
                    } else {
                        presenter.autoSuggest(newQuery);
                    }
                }
        );

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                presenter.onPlaceClicked((Place) searchSuggestion);
            }

            @Override
            public void onSearchAction(String query) {
                presenter.search(query);

            }
        });

        mSearchView.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_location) {
                presenter.onLocationClicked();
            }
        });
        mSearchView.setOnHomeActionClickListener(this::finish);

        mSearchView.setOnSuggestionsListHeightChanged(newHeight ->
                mSearchResultsList.setTranslationY(newHeight));
    }


    private void showError(Throwable error) {
    }

    private void setupResultsList() {
        mSearchResultsAdapter = new SearchAdapter();
        mSearchResultsAdapter.getPositionClicks().subscribe(presenter::onPlaceClicked);
        mSearchResultsList.setAdapter(mSearchResultsAdapter);
        mSearchResultsList.setLayoutManager(new LinearLayoutManager(this));
    }

    public void setExtras(Bundle extras) {
        presenter.setBbox(extras.getString(MainActivity.BBOX));
    }

    @Override
    public void showProgress(boolean b) {

    }

    @Override
    public void setItems(PlaceResult[] items) {
        mSearchResultsAdapter.setPlaces(items);
    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void showSuggestionProgress(boolean show) {
        if (show)
            mSearchView.showProgress();
        else
            mSearchView.hideProgress();
    }

    @Override
    public void showSuggestions(Place[] places) {
        mSearchView.swapSuggestions(Arrays.asList(places));
    }

    @Override
    public void retrunPlace(Place place) {
        Intent intent = this.getIntent();
        intent.putExtra(PLACE, place);
        this.setResult(RESULT_OK, intent);
        finish();
    }
}
