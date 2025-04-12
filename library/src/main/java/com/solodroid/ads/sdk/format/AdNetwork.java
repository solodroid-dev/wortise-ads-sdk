package com.solodroid.ads.sdk.format;

import static com.solodroid.ads.sdk.util.Constant.ADMOB;
import static com.solodroid.ads.sdk.util.Constant.AD_STATUS_ON;
import static com.solodroid.ads.sdk.util.Constant.GOOGLE_AD_MANAGER;
import static com.solodroid.ads.sdk.util.Constant.WORTISE;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.solodroid.ads.sdk.R;
import com.solodroid.ads.sdk.util.AsyncTaskExecutor;
import com.solodroid.ads.sdk.util.Response;
import com.solodroid.ads.sdk.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class AdNetwork {

    public static class Initialize {

        private static final String TAG = "AdNetwork";
        Activity activity;
        private String adStatus = "";
        private String adNetwork = "";
        private String backupAdNetwork = "";
        private String adMobAppId = "";
        private String startappAppId = "0";
        private String unityGameId = "";
        private String appLovinSdkKey = "";
        private String mopubBannerId = "";
        private String ironSourceAppKey = "";
        private String wortiseAppId = "";
        private boolean debug = true;

        public Initialize(Activity activity) {
            this.activity = activity;
        }

        public Initialize build() {
            initAds();
            initBackupAds();
            return this;
        }

        public Initialize setAdStatus(String adStatus) {
            this.adStatus = adStatus;
            return this;
        }

        public Initialize setAdNetwork(String adNetwork) {
            this.adNetwork = adNetwork;
            return this;
        }

        public Initialize setBackupAdNetwork(String backupAdNetwork) {
            this.backupAdNetwork = backupAdNetwork;
            return this;
        }

        public Initialize setAdMobAppId(String adMobAppId) {
            this.adMobAppId = adMobAppId;
            return this;
        }

        public Initialize setStartappAppId(String startappAppId) {
            this.startappAppId = startappAppId;
            return this;
        }

        public Initialize setUnityGameId(String unityGameId) {
            this.unityGameId = unityGameId;
            return this;
        }

        public Initialize setAppLovinSdkKey(String appLovinSdkKey) {
            this.appLovinSdkKey = appLovinSdkKey;
            return this;
        }

        public Initialize setMopubBannerId(String mopubBannerId) {
            this.mopubBannerId = mopubBannerId;
            return this;
        }

        public Initialize setIronSourceAppKey(String ironSourceAppKey) {
            this.ironSourceAppKey = ironSourceAppKey;
            return this;
        }

        public Initialize setWortiseAppId(String wortiseAppId) {
            this.wortiseAppId = wortiseAppId;
            return this;
        }

        public Initialize setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public void initAds() {
            if (adStatus.equals(AD_STATUS_ON)) {
                switch (adNetwork) {
                    case ADMOB:
                    case GOOGLE_AD_MANAGER:
                        MobileAds.initialize(activity, initializationStatus -> {
                            Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                            for (String adapterClass : statusMap.keySet()) {
                                AdapterStatus adapterStatus = statusMap.get(adapterClass);
                                assert adapterStatus != null;
                                Log.d(TAG, String.format("Adapter name: %s, Description: %s, Latency: %d", adapterClass, adapterStatus.getDescription(), adapterStatus.getLatency()));
                            }
                        });
                        break;

                    case WORTISE:
                        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(activity);
                        alertDialogBuilder.setTitle("Wortise Ads Removed!");
                        alertDialogBuilder.setMessage(Html.fromHtml(activity.getString(R.string.wortise_ads_announcement)));
                        alertDialogBuilder.setPositiveButton("OK", null);
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.show();
//                        WortiseSdk.initialize(activity, wortiseAppId, () -> {
//                            // This listener will be invoked when the initialization finishes
//                            Log.d(TAG, "[" + adNetwork + "] Init Wortise: " + wortiseAppId);
//                            return Unit.INSTANCE;
//                        });
//                        AdSettings.setTestEnabled(debug);
                        break;

                    default:
                        break;
                }
                Log.d(TAG, "[" + adNetwork + "] is selected as Primary Ads");
            }
        }

        public void initBackupAds() {
            if (adStatus.equals(AD_STATUS_ON)) {
                switch (backupAdNetwork) {
                    case ADMOB:
                    case GOOGLE_AD_MANAGER:
                        MobileAds.initialize(activity, initializationStatus -> {
                            Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                            for (String adapterClass : statusMap.keySet()) {
                                AdapterStatus adapterStatus = statusMap.get(adapterClass);
                                assert adapterStatus != null;
                                Log.d(TAG, String.format("Adapter name: %s, Description: %s, Latency: %d", adapterClass, adapterStatus.getDescription(), adapterStatus.getLatency()));
                            }
                        });
                        break;

//                    case WORTISE:
//                        WortiseSdk.initialize(activity, wortiseAppId, () -> {
//                            // This listener will be invoked when the initialization finishes
//                            Log.d(TAG, "[" + backupAdNetwork + "] Init Wortise backup : " + wortiseAppId);
//                            return Unit.INSTANCE;
//                        });
//                        AdSettings.setTestEnabled(debug);
//                        break;

                    default:
                        break;
                }
                Log.d(TAG, "[" + backupAdNetwork + "] is selected as Backup Ads");
            }
        }

    }

}
