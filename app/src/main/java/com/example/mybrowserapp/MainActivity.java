package com.example.mybrowserapp;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Patterns;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mybrowserapp.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private WebView myWebView;

    //  検索するEditTextにurlを表示してあげるためのフィールド
    private EditText urlText;

    private static final String INITIAL_WEBSITE = "https://dotinstall.com";

    //  アクティビティーを終了させる際の後処理(詳しくは触れていなかったがメモリ管理らしい)
//  アクティビティーが終了する際のonDestroyメソッドをオーバーライド
    @Override
    protected void onDestroy(){
        super.onDestroy();
//      myWebViewがnullでなければ、色々終了させる
        if(myWebView != null){
            myWebView.stopLoading(); //読み込みを停止
            myWebView.setWebViewClient(null);
            myWebView.destroy();
        }
        myWebView = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myWebView = (WebView) findViewById(R.id.myWebView);
        urlText = (EditText) findViewById(R.id.urlText);

//      getSettingsでWebViewの設定、その中のJavaScriptを有効にする(true)
        myWebView.getSettings().setJavaScriptEnabled(true);

//      myWebViewで開いたブラウザをmyWebViewで表示してあげるように設定(これをしなければデフォルトのブラウザアプリが起動してしまう)
        myWebView.setWebViewClient(new WebViewClient() {
            //         ページを読み込んだ後に使用するメソッドをオーバーライド
            @Override
//         ページを読み込んだ後、アクションバーのサブタイトルに、このページのタイトルを表示ためのメソッド
            public void onPageFinished(WebView view, String url){
//            getSupportActionBarでアクションバーを取得、setSubtitle(view.getTitle())で
//            表示しているviewページのタイトルを引っ張ってきてセットする
                getSupportActionBar().setSubtitle(view.getTitle());
                urlText.setText(url);

            }
        });
        myWebView.loadUrl(INITIAL_WEBSITE);
    }

    //  EditTextに入力したurlを検索して表示するメソッド
    public void showWebsite(View view) {
        String url = urlText.getText().toString().trim();

//      urlの妥当性チェック(変な文字列が入った時にエラーを発生させる)
        if (!Patterns.WEB_URL.matcher(url).matches()){  //urlにマッチしなければ下記メッセージを返す
            urlText.setError("Invalid URL");
        } else {
//          http、httpsで始まらなければhttpsを付け加える
            if(!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }
//          マッチすれば、myWebViewにurlページを表示する
            myWebView.loadUrl(url);
        }
        myWebView.loadUrl(url);
    }

    //  url検索フォームをクリックした際に、入っている文字を削除するメソッド
    public void clearUrl(View view){
        urlText.setText("");
    }

    //  バックボタンを押した時に使用するメソッドをオーバーライド
    @Override
//  バックボタンを押した時にアプリが終了されないようにするメソッド
    public void onBackPressed(){
//      myWebViewに履歴があれば、その履歴を辿るようにする(canGoBackで履歴の有無を確認)
        if (myWebView.canGoBack()) {
            myWebView.goBack();  //goBackで履歴のページへ戻る
            return;
        }
//      戻るボタンでアプリを終了させる?
        super.onBackPressed();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}