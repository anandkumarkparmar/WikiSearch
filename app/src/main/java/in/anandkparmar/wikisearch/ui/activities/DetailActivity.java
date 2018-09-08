package in.anandkparmar.wikisearch.ui.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.anandkparmar.wikisearch.R;
import in.anandkparmar.wikisearch.utils.AppConstants;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.web_view)
    WebView detailWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra(AppConstants.INTENT_EXTRA_TITLE));

        initWebView(getIntent().getStringExtra(AppConstants.INTENT_EXTRA_LINK));
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void initWebView(String link) {
        if (link != null && !"".equals(link)) {
            detailWebView.getSettings().setJavaScriptEnabled(true);
            detailWebView.setWebViewClient(new WebViewClient());
            detailWebView.loadUrl(link);
        } else {
            onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
