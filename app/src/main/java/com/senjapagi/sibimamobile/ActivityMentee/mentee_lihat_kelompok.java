package com.senjapagi.sibimamobile.ActivityMentee;


import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.senjapagi.sibimamobile.Constant.ConstantMentee;
import com.senjapagi.sibimamobile.R;
import com.senjapagi.sibimamobile.Services.CLIENT_API;

import static com.senjapagi.sibimamobile.Constant.ConstantMentee.kelompok;

public class mentee_lihat_kelompok extends AppCompatActivity {

    FloatingActionButton btnHome;
    TextView tvKodeKelompok,tvNamaMentor,tvKontakMentor,tvLineMentor;
    WebView websiteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentee_lihat_kelompok);
        init();
        findViewById(R.id.animation_lootie_loading).setVisibility(View.VISIBLE);
        String webUrl = CLIENT_API.lihat_kelompok+"?kode_kelompok="+getIntent().getStringExtra("kelompok");
        websiteView.loadUrl(webUrl);

        tvLineMentor.setText(getIntent().getStringExtra(ConstantMentee.mentor_line));
        tvKontakMentor.setText(getIntent().getStringExtra(ConstantMentee.mentor_telp));
        tvKodeKelompok.setText(getIntent().getStringExtra(kelompok));
        tvNamaMentor.setText(getIntent().getStringExtra(ConstantMentee.mentor_name));
        findViewById(R.id.btn_refresh_lyt_error_internet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String webUrl = CLIENT_API.lihat_kelompok+"?kode_kelompok="+getIntent().getStringExtra("kelompok");
                websiteView.loadUrl(webUrl);
            }
        });


        websiteView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                findViewById(R.id.webview).setVisibility(View.VISIBLE);
                findViewById(R.id.animation_lootie_loading).setVisibility(View.GONE);

            }
            public void onReceivedError(WebView view, int errorCod,String description, String failingUrl) {
                Toast.makeText(mentee_lihat_kelompok.this, "Your Internet Connection May not be active Or " + description , Toast.LENGTH_LONG).show();
                findViewById(R.id.layout_error_internet).setVisibility(View.VISIBLE);
            }

        });


        btnHome=findViewById(R.id.btn_home);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mentee_lihat_kelompok.super.onBackPressed();
            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        WebView webview = (WebView) findViewById(R.id.webview);

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webview.canGoBack()) {
                        webview.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState )
    {
        WebView webview = (WebView) findViewById(R.id.webview);

        super.onSaveInstanceState(outState);
        webview.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        WebView webview = (WebView) findViewById(R.id.webview);
        super.onRestoreInstanceState(savedInstanceState);
        webview.restoreState(savedInstanceState);
    }

    private void init(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);}


        tvNamaMentor=findViewById(R.id.tv_nama_mentorx);
        tvKodeKelompok=findViewById(R.id.tv_kelompok_menteex);
        tvKontakMentor=findViewById(R.id.tv_kontak_mentorx);
        tvLineMentor=findViewById(R.id.tv_link_linex);

        websiteView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = websiteView.getSettings();
        websiteView.getSettings().setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        websiteView.setWebChromeClient(new WebChromeClient());
    }

}

