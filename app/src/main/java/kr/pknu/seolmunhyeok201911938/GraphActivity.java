package kr.pknu.seolmunhyeok201911938;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class GraphActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        String targetCurrency = getIntent().getStringExtra("targetCurrency");

        String graphUrl = "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_1"
                + "&symbol=" + targetCurrency + "KRW"
                + "&interval=D"
                + "&hidesidetoolbar=1"
                + "&saveimage=0"
                + "&symboledit=1";

        webView.loadUrl(graphUrl);
    }
}
