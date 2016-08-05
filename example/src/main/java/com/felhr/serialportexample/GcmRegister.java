package com.felhr.serialportexample;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taha on 30/05/2016.
 */
public class GcmRegister {

    SharedPreferences mSharedPreferences;
    GoogleCloudMessaging gcm;
    String gcmId = "";
    String sender_id = "354045635290";
    String strGcmId = "";
    String possibleEmail = "";
    public static Context context;

    public GcmRegister(Context context)
    {
        this.context = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public void Register(){
        try {
            Account[] accounts = AccountManager.get(context).getAccountsByType("com.google");

            for (Account account : accounts) {
                possibleEmail = account.name;
            }
        } catch (Exception e) {
            Log.i("Exception", "Exception:" + e);
        }

        if (checkPreferences() == true) {


            if (gcmId.length() == 0) {
                new asyncTask_RegisterGCM().execute();
            }
            new asyncTask_RegisterWeb().execute();
            Toast.makeText(context, "now registerd", Toast.LENGTH_SHORT).show();

        } else {
//            Toast.makeText(MainActivity.this, strGcmId, Toast.LENGTH_SHORT).show();

        }
    }


    private Boolean checkPreferences() {
        strGcmId = mSharedPreferences.getString("key_gcmId", "");

        if (strGcmId.length() == 0) {
            return true;
        }
        return false;
    }

    private class asyncTask_RegisterGCM extends AsyncTask<Void, Void, String> {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        @Override
        protected String doInBackground(Void... params) {
            try {

                gcm = GoogleCloudMessaging.getInstance(context);
                gcmId = gcm.register(sender_id);
                editor.putString("key_gcmId", gcmId.toString());
                editor.commit();

            } catch (IOException ex) {
                return "Error:" + ex.getMessage();
            }
            return gcmId;
        }

    }

    private class asyncTask_RegisterWeb extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String msg = "";
            try {
                if (gcmId.length() > -0) {
                    msg = registerDeviceToWebServer(gcmId, possibleEmail);

                }
            } catch (Exception ex) {
                msg = "Error :" + ex.getMessage();
            }
            return msg;
        }

    }

    public String registerDeviceToWebServer(String gcmId, String possibleEmail) {
        String url = "http://friendsfashion.net/android/secureme/register.php";
        String strResponse = "No response";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("device_gcm_id", gcmId));
            //nameValuePairs.add(new BasicNameValuePair("device_type", "1"));
            nameValuePairs.add(new BasicNameValuePair("device_email_address", possibleEmail));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            strResponse = EntityUtils.toString(response.getEntity());
        } catch (ClientProtocolException e) {
            strResponse = e.getMessage();
        } catch (IOException e) {
            Log.e("IOException:", e.getMessage());
            strResponse = e.getMessage();
        }
        return strResponse;
    }
}
