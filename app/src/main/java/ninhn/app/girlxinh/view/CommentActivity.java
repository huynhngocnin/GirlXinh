package ninhn.app.girlxinh.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ninhn.app.girlxinh.R;

public class CommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_comment);

        String url = getIntent().getStringExtra("url");
        String APP_KEY = "1131735883527080";
        final WebView webView = (WebView) findViewById(R.id.comment_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadDataWithBaseURL(url,
                "<html><head></head><body><div id=\"fb-root\"></div><div id=\"fb-root\"></div><script>(function(d, s, id) {var js, fjs = d.getElementsByTagName(s)[0];if (d.getElementById(id)) return;js = d.createElement(s); js.id = id;js.src = \"http://connect.facebook.net/en_US/all.js#xfbml=1&appId=" + APP_KEY + "\";fjs.parentNode.insertBefore(js, fjs);}(document, 'script', 'facebook-jssdk'));</script><div class=\"fb-comments\" data-href=\""
                        + url + "\" data-width=\"470\"></div> </body></html>", "text/html", null, null);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.setVisibility(View.VISIBLE);

            }
        });
    }
}
