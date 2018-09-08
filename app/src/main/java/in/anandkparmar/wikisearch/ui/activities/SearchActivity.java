package in.anandkparmar.wikisearch.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.anandkparmar.wikisearch.R;
import in.anandkparmar.wikisearch.db.LastSearches;
import in.anandkparmar.wikisearch.network.core.APIService;
import in.anandkparmar.wikisearch.network.responses.SearchListResponse;
import in.anandkparmar.wikisearch.ui.adapters.LastSearchesAdapter;
import in.anandkparmar.wikisearch.ui.adapters.SearchResultAdapter;
import in.anandkparmar.wikisearch.utils.AppConstants;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SearchActivity extends AppCompatActivity {
    @BindView(R.id.search_edit_text)
    AppCompatEditText searchEditText;

    @BindView(R.id.close_button)
    ImageView closeButton;

    @BindView(R.id.last_searches)
    TextView lastSearches;

    @BindView(R.id.background)
    ImageView background;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private SearchResultAdapter searchResultAdapter;
    private LastSearchesAdapter lastSearchesAdapter;
    private SearchListResponse searchListResponse;
    private String searchQuery;
    private List<LastSearches> lastSearchesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard(searchEditText);
                    callGetSearchList();
                    return true;
                }
                return false;
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    closeButton.setVisibility(View.GONE);
                    showBackGround();
                } else {
                    closeButton.setVisibility(View.VISIBLE);
                    SearchActivity.this.searchQuery = s.toString();
                    callGetSearchList();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(recyclerView.getAdapter() == lastSearchesAdapter) {
                    swipeRefreshLayout.setRefreshing(true);
                    showBackGround();
                } else {
                    callGetSearchList();
                }
            }
        });
        searchResultAdapter = new SearchResultAdapter(this, new ArrayList<SearchListResponse.SearchListItem>());
        lastSearchesAdapter = new LastSearchesAdapter(this, new ArrayList<LastSearches>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        showBackGround();
    }

    public void callGetSearchList() {
        swipeRefreshLayout.setRefreshing(true);
        APIService.getSearchList(this.searchQuery, new Observer<SearchListResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(SearchListResponse searchListResponse) {
                if(swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                showRecyclerView();
                SearchActivity.this.searchListResponse = searchListResponse;
                recyclerView.setAdapter(searchResultAdapter);
                searchResultAdapter.updateList(searchListResponse.getQuery().getPages());
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(SearchActivity.this, "No response found.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void startDetailsActivity(String title, String link) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(AppConstants.INTENT_EXTRA_TITLE, title);
        intent.putExtra(AppConstants.INTENT_EXTRA_LINK, link);
        startActivity(intent);
    }

    @OnClick(R.id.close_button)
    protected void onClickOfCloseButton() {
        searchEditText.setText("");
        showKeyboard(searchEditText);
        showBackGround();
    }

    private void showBackGround() {
        if(swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        this.lastSearchesList = LastSearches.getLastSearches();
        if (lastSearchesList != null && lastSearchesList.size() > 0) {
            showRecyclerView();
            lastSearches.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(lastSearchesAdapter);
            lastSearchesAdapter.updateList(lastSearchesList);
        } else {
            lastSearches.setVisibility(View.GONE);
            background.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        }
    }

    private void showRecyclerView() {
        lastSearches.setVisibility(View.GONE);
        background.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
    }

    public static void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public static void showKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }

    public void onItemClickListenerForSearch(int position) {
        hideKeyboard(searchEditText);
        LastSearches.save(searchListResponse.getQuery().getPages().get(position));
        startDetailsActivity(searchListResponse.getQuery().getPages().get(position).getTitle(), searchListResponse.getQuery().getPages().get(position).getFullurl());
    }

    public void onItemClickListenerForPreviousSearch(int position) {
        hideKeyboard(searchEditText);
        startDetailsActivity(lastSearchesList.get(position).getTitle(), lastSearchesList.get(position).getFullurl());
    }

    @Override
    public void onBackPressed() {
        if (searchEditText.getText() != null && !searchEditText.getText().toString().equals("")) {
            onClickOfCloseButton();
        } else {
            finish();
        }
    }
}
