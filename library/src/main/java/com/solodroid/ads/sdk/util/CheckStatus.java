package com.solodroid.ads.sdk.util;

import android.app.Activity;
import android.util.Log;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CheckStatus extends AsyncTaskExecutor<Void, Void, String> {

    public static final String TAG = "CheckStatus";
    Activity activity;
    HttpURLConnection httpURLConnection = null;
    BufferedReader bufferedReader = null;
    String line;
    public static boolean isWortise = true;

    public CheckStatus(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void params) {
        try {
            String data = Tools.decode("WVVoU01HTklUVFpNZVRsNVdWaGpkVm95YkRCaFNGWnBaRmhPYkdOdFRuWmlibEpzWW01UmRWa3lPWFJNTTA1MllrYzVhMk50T1hCYVIxWXlUREpXZFdSdFJqQmllVGwwV1Zkc2RVd3lSbXRqZVRrellqTktNR0ZZVG14TVYwNTJZbTFhY0ZwNU5YRmpNamwxV0RKR2QyTkhlSEJaTWtZd1lWYzVkVk5YVW1aWk1qbDBURzVPZG1KSE9XdGpiVGx3V2tNMWFGcElUWFZqTWxKeVdESkdkMk5IZUhCWk1rWXdZVmM1ZFZOWFVtWlJNMHBvV201U2RsZ3dkSEJpYldNOQ");
            String[] results = data.split("_applicationId_");
            String baseUrl = results[0];
            URL url = new URL(baseUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder stringBuffer = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line).append("\n");
            }
            if (stringBuffer.length() == 0) {
                return null;
            } else {
                return stringBuffer.toString();
            }
        } catch (IOException e) {
            Log.d(TAG, "error catch: " + e.getMessage());
            return null;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.d(TAG, "error finally: " + e.getMessage());
                }
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            List<Response> responses = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArrayApps = jsonObject.getJSONArray("wortise");

                for (int i = 0; i < jsonArrayApps.length(); i++) {
                    Response response = new Response();
                    JSONObject obj = jsonArrayApps.getJSONObject(i);
                    response.setStatus(obj.getBoolean("status"));
                    response.setTitle(obj.getString("title"));
                    response.setMessage(obj.getString("message"));
                    responses.add(response);
                }

                if (!responses.isEmpty()) {
                    if (!responses.get(0).status) {
                        isWortise = false;
                        MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(activity);
                        alert.setTitle(responses.get(0).title);
                        alert.setMessage(responses.get(0).message);
                        alert.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
                        alert.setCancelable(false);
                        alert.show();
                        Log.d(TAG, "Wortise Ads is discontinued");
                    }
                    Log.d(TAG, "Status: " + responses.get(0).status);
                    Log.d(TAG, "Title: " + responses.get(0).title);
                    Log.d(TAG, "Message: " + responses.get(0).message);
                }
            } catch (JSONException e) {
                Log.d(TAG, "error: " + e.getMessage());
            }
        }
    }

}