package id.ac.uny.unyofficial;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by groovyle on 2018-01-14.
 */

// Refer to https://stackoverflow.com/questions/9570237/android-check-internet-connection
public class NetworkInfoHelper {
    public void doIfConnected(OnConnectionCallback callback) {
        Log.d("qwerty", "doIfConnected");
        CheckNetworkConnection cnc = new CheckNetworkConnection(callback);
        cnc.execute();
    }

    protected class CheckNetworkConnection extends AsyncTask<Void, Void, Boolean> {
        protected OnConnectionCallback callback;
        protected String message;

        public CheckNetworkConnection(OnConnectionCallback callback) {
            super();
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Log.d("qwerty", "beginning to connect");
                NetworkInfoUtility niu = new NetworkInfoUtility();
                return niu.checkConnectionToHost("uny.ac.id");
            } catch (UnknownHostException e) {
                // Log error
                message = e.getMessage();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.d("qwerty", "connection result is: "+ result);
            if(result) {
                callback.onConnectionSuccess();
            } else {
                String msg = "No internet connection";
                if(!message.equals("")) {
                    msg += ": " + message;
                }
                callback.onConnectionFailed(msg);
            }
            callback.onConnectionFinished(result, message);
        }
    }

    public interface OnConnectionCallback {
        void onConnectionSuccess();
        void onConnectionFailed(String errorMessage);
        void onConnectionFinished(Boolean result, String errorMessage);
    }
}
