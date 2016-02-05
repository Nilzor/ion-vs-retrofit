package no.vg.android.ionvsretrofit;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.koushikdutta.ion.Ion;
import com.squareup.otto.Bus;

public class App extends Application {
    public static Bus EventBus;
    private Context AppContext;
    public static String SERVER_HOST =  "10.200.204.239";
    public static int APP_PORT = 8000;
    public static int PROXY_PORT = 8899;

    @Override
    public void onCreate() {
        super.onCreate();
        init(getApplicationContext());
    }

    private void init(Context context) {
        AppContext = context;
        EventBus = new Bus();
        // Set up proxy.
        Ion.Config ionConf = Ion.getDefault(AppContext).configure();
        ionConf.setLogging("ION", Log.DEBUG);
        ionConf.getResponseCache().clear();
        //ionConf.proxy(SERVER_HOST, PROXY_PORT);
    }
}
