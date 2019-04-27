package im.craig.locateio;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static MySingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    //declare vars context and request queue
    private MySingleton(Context context) {

        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    //create instance of singleton from external
    public static synchronized MySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }

    //create request with volley and run on the url of php page
    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }
    //add to queue when called
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}