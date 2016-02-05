package no.vg.android.ionvsretrofit;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.future.ResponseFuture;

import java.util.ArrayList;

import no.vg.android.ionvsretrofit.core.Action;
import no.vg.android.ionvsretrofit.entities.PodcastEpisodeJsonListProxy;
import no.vg.android.ionvsretrofit.viewmodels.VolleyRequestActivityViewModel;

public class MainActivity extends Activity {
    private static final int SPEW_COUNT = 40;
    //private final String Url = "http://httpbin.org/get";
    //private final String Url = "http://httpbin.org/delay/1";
    private final String UrlSmall = "http://" + App.SERVER_HOST + ":" + App.APP_PORT + "/jsonSmall";
    private final String UrlLarge = "http://" + App.SERVER_HOST + ":" + App.APP_PORT + "/jsonLarge";
    private VolleyRequestActivityViewModel _model;
    private static final String TAG = "OVDR";
    private boolean mDoLog = true;
    private int mCacheBreaker = 1;
    private long mStartTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);
        _model = new VolleyRequestActivityViewModel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
        App.EventBus.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        App.EventBus.register(this);
        bindUi();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("Model", _model);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState()");
        _model = (VolleyRequestActivityViewModel) savedInstanceState.getSerializable("Model");
    }

    private void bindUi() {
        ((TextView) findViewById(R.id.statusText)).setText(_model.status);
        ((TextView) findViewById(R.id.prevResultText)).setText(_model.prevResult);
    }

    /*******************************************************/

    private ArrayList<RequestToken> mRequests;

    private void spewRequests(Action action) {
        mRequests = new ArrayList<RequestToken>(SPEW_COUNT);
        RequestToken.mIdCounter = 0;
        mStartTime = System.currentTimeMillis();
        for (int i = 0; i < SPEW_COUNT; i++) {
            action.act();
        }
    }

    private void onRequestStart(RequestToken req) {
        if (mDoLog) {
            long time = System.currentTimeMillis() - mStartTime;
            Log.i(TAG + "Z", String.format("S:%02d:%04d", req.id, time));
        }
    }

    private void onRequestDone(RequestToken req) {
        req.onDone();
        mRequests.add(req);
        if (mRequests.size() == SPEW_COUNT) {
            onAllRequestsDone();
        }

        if (mDoLog){
            long time = System.currentTimeMillis() - mStartTime;
            Log.i(TAG + "Z", String.format("E:%02d:%04d", req.id, time));
        }
    }

    private void onAllRequestsDone() {
        long totalTime = getMaxRequestTime();
        Log.i(TAG, "Spew of " + SPEW_COUNT + " requests done.");
        Log.i(TAG, String.format("Total time: %s", totalTime));
    }

    private long getMaxRequestTime() {
        long max = 0;
        for (RequestToken token : mRequests) {
            max = Math.max(token.getDuration(), max);
        }
        return max;
    }

    private void performIonGsonHttpAsync(String urlBase) {
        final RequestToken req = new RequestToken();
        String url = urlBase + "?"  + mCacheBreaker++;
        ResponseFuture<PodcastEpisodeJsonListProxy> future = Ion.with(this).load(url).as(PodcastEpisodeJsonListProxy.class);
        req.onStart();
        onRequestStart(req);
        future.setCallback((e, res) -> {
            onRequestDone(req);
            if (e != null) Log.w(TAG, e.getMessage(), e);
            else {
                //Log.i(TAG+"Q", String.format("%s,%s,%s,%s", res.description, res.iTunesLink, res.lastModified, res.episodes.get(0).title));
            }
        });
    }

    private void performIonStringAsync(String urlBase) {
        final RequestToken req = new RequestToken();
        String url = urlBase + "?"  + mCacheBreaker++;
        ResponseFuture<String> future = Ion.with(this).load(url).setTimeout(15000).noCache().asString();
        req.onStart();
        future.setCallback((e, testServiceResponse) -> {
            onRequestDone(req);
            if (e != null) Log.w(TAG, e.getMessage(), e);
        });
        onRequestStart(req);
    }

    public void onIonClicked(final View view) {
        spewRequests(() -> performIonStringAsync(UrlSmall));
    }

    public void onIonLargeStringClicked(View view) {
        spewRequests(() -> performIonStringAsync(UrlLarge));
    }

    public void onIonLargeGsonClicked(View view) {
        spewRequests(() -> performIonGsonHttpAsync(UrlLarge));
    }

    public void onRetrofitClicked(final View view) {

    }

    public void onRetrofitLargeStringClicked(View view) {
    }

    public void onRetrofitLargeGsonClicked(View view) {
    }

    private static class RequestToken {
        private static int mIdCounter = 0;
        public int id = ++mIdCounter;
        private long startTime;
        private long endTime;

        public long getDuration() {
            return endTime - startTime;
        }

        public void onStart() {
            startTime = System.currentTimeMillis();
        }

        public void onDone() {
            endTime = System.currentTimeMillis();
        }
    }
}
