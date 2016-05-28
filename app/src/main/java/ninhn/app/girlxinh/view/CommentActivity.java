package ninhn.app.girlxinh.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import ninhn.app.girlxinh.R;

public class CommentActivity extends AppCompatActivity {

    public static final String COMMENT_URL = "commentUrl";

    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_comment);

        mapComponent();

        String url = getIntent().getStringExtra(COMMENT_URL);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadDataWithBaseURL(url,
                "<html><head></head><body><div id=\"fb-root\"></div><script>(function(d, s, id) {var js, fjs = d.getElementsByTagName(s)[0];if (d.getElementById(id)) return;js = d.createElement(s); js.id = id;js.src = \"http://connect.facebook.net/en_US/all.js#xfbml=1&appId=" + getString(R.string.facebook_app_id) + "\";fjs.parentNode.insertBefore(js, fjs);}(document, 'script', 'facebook-jssdk'));</script><div class=\"fb-comments\" data-href=\""
                        + url + "\" data-mobile=\"true\" data-order-by=\"reverse_time\"></div></body></html>", "text/html", null, null);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void mapComponent() {
        webView = (WebView) findViewById(R.id.comment_webview);
        progressBar = (ProgressBar) findViewById(R.id.comment_progress);
    }
}
