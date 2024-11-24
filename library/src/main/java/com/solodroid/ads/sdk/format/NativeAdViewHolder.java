package com.solodroid.ads.sdk.format;

import static com.solodroid.ads.sdk.util.Constant.ADMOB;
import static com.solodroid.ads.sdk.util.Constant.AD_STATUS_ON;
import static com.solodroid.ads.sdk.util.Constant.FAN_BIDDING_ADMOB;
import static com.solodroid.ads.sdk.util.Constant.FAN_BIDDING_AD_MANAGER;
import static com.solodroid.ads.sdk.util.Constant.GOOGLE_AD_MANAGER;
import static com.solodroid.ads.sdk.util.Constant.WORTISE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.solodroid.ads.sdk.R;
import com.solodroid.ads.sdk.util.AdManagerTemplateView;
import com.solodroid.ads.sdk.util.CheckStatus;
import com.solodroid.ads.sdk.util.Constant;
import com.solodroid.ads.sdk.util.NativeTemplateStyle;
import com.solodroid.ads.sdk.util.TemplateView;
import com.solodroid.ads.sdk.util.Tools;
import com.wortise.ads.AdError;
import com.wortise.ads.natives.GoogleNativeAd;

public class NativeAdViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "AdNetwork";
    LinearLayout nativeAdViewContainer;

    //AdMob
    MediaView mediaView;
    TemplateView admobNativeAd;
    LinearLayout admobNativeBackground;

    //Ad Manager
    MediaView adManagerMediaView;
    AdManagerTemplateView adManagerNativeAd;
    LinearLayout adManagerNativeBackground;

    //StartApp
    View startappNativeAd;
    ImageView startappNativeImage;
    ImageView startappNativeIcon;
    TextView startappNativeTitle;
    TextView startappNativeDescription;
    Button startappNativeButton;
    LinearLayout startappNativeBackground;

    //AppLovin
    FrameLayout applovinNativeAd;
    LinearLayout appLovinDiscoveryMrecAd;

    //Wortise
    private GoogleNativeAd mGoogleNativeAd;
    FrameLayout wortiseNativeAd;

    public NativeAdViewHolder(View view) {
        super(view);

        nativeAdViewContainer = view.findViewById(R.id.native_ad_view_container);

        //AdMob
        admobNativeAd = view.findViewById(R.id.admob_native_ad_container);
        mediaView = view.findViewById(R.id.media_view);
        admobNativeBackground = view.findViewById(R.id.background);

        //Ad Manager
        adManagerNativeAd = view.findViewById(R.id.google_ad_manager_native_ad_container);
        adManagerMediaView = view.findViewById(R.id.ad_manager_media_view);
        adManagerNativeBackground = view.findViewById(R.id.ad_manager_background);

        //StartApp
        startappNativeAd = view.findViewById(R.id.startapp_native_ad_container);
        startappNativeImage = view.findViewById(R.id.startapp_native_image);
        startappNativeIcon = view.findViewById(R.id.startapp_native_icon);
        startappNativeTitle = view.findViewById(R.id.startapp_native_title);
        startappNativeDescription = view.findViewById(R.id.startapp_native_description);
        startappNativeButton = view.findViewById(R.id.startapp_native_button);
        startappNativeButton.setOnClickListener(v1 -> itemView.performClick());
        startappNativeBackground = view.findViewById(R.id.startapp_native_background);

        //AppLovin
        applovinNativeAd = view.findViewById(R.id.applovin_native_ad_container);
        appLovinDiscoveryMrecAd = view.findViewById(R.id.applovin_discovery_mrec_ad_container);

        wortiseNativeAd = view.findViewById(R.id.wortise_native_ad_container);

    }

    public void loadNativeAd(Context context, String adStatus, int placementStatus, String adNetwork, String backupAdNetwork, String adMobNativeId, String adManagerNativeId, String fanNativeId, String appLovinNativeId, String appLovinDiscMrecZoneId, String wortiseNativeId, boolean darkTheme, boolean legacyGDPR, String nativeAdStyle, int nativeBackgroundLight, int nativeBackgroundDark) {
        if (adStatus.equals(AD_STATUS_ON)) {
            if (placementStatus != 0) {
                switch (adNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        if (admobNativeAd.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(context, adMobNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admobNativeAd.setStyles(styles);
                                            admobNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admobNativeAd.setStyles(styles);
                                            admobNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                        }
                                        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        admobNativeAd.setNativeAd(NativeAd);
                                        admobNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            loadBackupNativeAd(context, adStatus, placementStatus, backupAdNetwork, adMobNativeId, adManagerNativeId, fanNativeId, appLovinNativeId, appLovinDiscMrecZoneId, wortiseNativeId, darkTheme, legacyGDPR, nativeAdStyle, nativeBackgroundLight, nativeBackgroundDark);
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getAdRequest((Activity) context, legacyGDPR));
                        } else {
                            Log.d(TAG, "AdMob native ads has been loaded");
                        }
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        if (adManagerNativeAd.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(context, adManagerNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            adManagerNativeAd.setStyles(styles);
                                            adManagerNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            adManagerNativeAd.setStyles(styles);
                                            adManagerNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                        }
                                        adManagerMediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        adManagerNativeAd.setNativeAd(NativeAd);
                                        adManagerNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            loadBackupNativeAd(context, adStatus, placementStatus, backupAdNetwork, adMobNativeId, adManagerNativeId, fanNativeId, appLovinNativeId, appLovinDiscMrecZoneId, wortiseNativeId, darkTheme, legacyGDPR, nativeAdStyle, nativeBackgroundLight, nativeBackgroundDark);
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getGoogleAdManagerRequest());
                        } else {
                            Log.d(TAG, "Ad Manager Native Ad has been loaded");
                        }
                        break;

                    case WORTISE:
                        if (CheckStatus.isWortise) {
                            if (wortiseNativeAd.getVisibility() != View.VISIBLE) {
                                mGoogleNativeAd = new GoogleNativeAd(context, wortiseNativeId, new GoogleNativeAd.Listener() {
                                    @Override
                                    public void onNativeFailedToLoad(@NonNull GoogleNativeAd googleNativeAd, @NonNull AdError adError) {
                                        loadBackupNativeAd(context, adStatus, placementStatus, backupAdNetwork, adMobNativeId, adManagerNativeId, fanNativeId, appLovinNativeId, appLovinDiscMrecZoneId, wortiseNativeId, darkTheme, legacyGDPR, nativeAdStyle, nativeBackgroundLight, nativeBackgroundDark);
                                        Log.d(TAG, "Wortise Native Ad failed loaded");
                                    }

                                    @Override
                                    public void onNativeClicked(@NonNull GoogleNativeAd googleNativeAd) {

                                    }

                                    @Override
                                    public void onNativeImpression(@NonNull GoogleNativeAd googleNativeAd) {

                                    }

                                    @SuppressLint("InflateParams")
                                    @Override
                                    public void onNativeLoaded(@NonNull GoogleNativeAd googleNativeAd, @NonNull com.google.android.gms.ads.nativead.NativeAd nativeAd) {
                                        LayoutInflater inflater = LayoutInflater.from(context);
                                        NativeAdView adView;
                                        switch (nativeAdStyle) {
                                            case Constant.STYLE_NEWS:
                                            case Constant.STYLE_MEDIUM:
                                                adView = (NativeAdView) inflater.inflate(R.layout.gnt_wortise_news_template_view, null);
                                                break;
                                            case Constant.STYLE_VIDEO_SMALL:
                                                adView = (NativeAdView) inflater.inflate(R.layout.gnt_wortise_video_small_template_view, null);
                                                break;
                                            case Constant.STYLE_VIDEO_LARGE:
                                                adView = (NativeAdView) inflater.inflate(R.layout.gnt_wortise_video_large_template_view, null);
                                                break;
                                            case Constant.STYLE_RADIO:
                                            case Constant.STYLE_SMALL:
                                                adView = (NativeAdView) inflater.inflate(R.layout.gnt_wortise_radio_template_view, null);
                                                break;
                                            default:
                                                adView = (NativeAdView) inflater.inflate(R.layout.gnt_wortise_medium_template_view, null);
                                                break;
                                        }
                                        populateNativeAdView(context, nativeAd, adView, darkTheme, nativeBackgroundDark, nativeBackgroundLight);
                                        wortiseNativeAd.removeAllViews();
                                        wortiseNativeAd.addView(adView);
                                        wortiseNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                        Log.d(TAG, "Wortise Native Ad loaded");
                                    }
                                });
                                mGoogleNativeAd.load();
                            } else {
                                Log.d(TAG, "Wortise Native Ad has been loaded");
                            }
                        }
                        break;

                    default:
                        break;

                }
            }
        }
    }

    public void loadBackupNativeAd(Context context, String adStatus, int placementStatus, String backupAdNetwork, String adMobNativeId, String adManagerNativeId, String fanNativeId, String appLovinNativeId, String appLovinDiscMrecZoneId, String wortiseNativeId, boolean darkTheme, boolean legacyGDPR, String nativeAdStyle, int nativeBackgroundLight, int nativeBackgroundDark) {
        if (adStatus.equals(AD_STATUS_ON)) {
            if (placementStatus != 0) {
                switch (backupAdNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        if (admobNativeAd.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(context, adMobNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admobNativeAd.setStyles(styles);
                                            admobNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admobNativeAd.setStyles(styles);
                                            admobNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                        }
                                        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        admobNativeAd.setNativeAd(NativeAd);
                                        admobNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            admobNativeAd.setVisibility(View.GONE);
                                            nativeAdViewContainer.setVisibility(View.GONE);
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getAdRequest((Activity) context, legacyGDPR));
                        } else {
                            Log.d(TAG, "AdMob native ads has been loaded");
                        }
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        if (adManagerNativeAd.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(context, adManagerNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            adManagerNativeAd.setStyles(styles);
                                            adManagerNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            adManagerNativeAd.setStyles(styles);
                                            adManagerNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                        }
                                        adManagerMediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        adManagerNativeAd.setNativeAd(NativeAd);
                                        adManagerNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            adManagerNativeAd.setVisibility(View.GONE);
                                            nativeAdViewContainer.setVisibility(View.GONE);
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getGoogleAdManagerRequest());
                        } else {
                            Log.d(TAG, "Ad Manager Native Ad has been loaded");
                        }
                        break;

                    case WORTISE:
                        if (CheckStatus.isWortise) {
                            if (wortiseNativeAd.getVisibility() != View.VISIBLE) {
                                mGoogleNativeAd = new GoogleNativeAd(context, wortiseNativeId, new GoogleNativeAd.Listener() {
                                    @Override
                                    public void onNativeFailedToLoad(@NonNull GoogleNativeAd googleNativeAd, @NonNull AdError adError) {
                                        Log.d(TAG, "[Backup] Wortise Native Ad failed loaded");
                                    }

                                    @Override
                                    public void onNativeClicked(@NonNull GoogleNativeAd googleNativeAd) {

                                    }

                                    @Override
                                    public void onNativeImpression(@NonNull GoogleNativeAd googleNativeAd) {

                                    }

                                    @SuppressLint("InflateParams")
                                    @Override
                                    public void onNativeLoaded(@NonNull GoogleNativeAd googleNativeAd, @NonNull com.google.android.gms.ads.nativead.NativeAd nativeAd) {
                                        LayoutInflater inflater = LayoutInflater.from(context);
                                        NativeAdView adView;
                                        switch (nativeAdStyle) {
                                            case Constant.STYLE_NEWS:
                                            case Constant.STYLE_MEDIUM:
                                                adView = (NativeAdView) inflater.inflate(R.layout.gnt_wortise_news_template_view, null);
                                                break;
                                            case Constant.STYLE_VIDEO_SMALL:
                                                adView = (NativeAdView) inflater.inflate(R.layout.gnt_wortise_video_small_template_view, null);
                                                break;
                                            case Constant.STYLE_VIDEO_LARGE:
                                                adView = (NativeAdView) inflater.inflate(R.layout.gnt_wortise_video_large_template_view, null);
                                                break;
                                            case Constant.STYLE_RADIO:
                                            case Constant.STYLE_SMALL:
                                                adView = (NativeAdView) inflater.inflate(R.layout.gnt_wortise_radio_template_view, null);
                                                break;
                                            default:
                                                adView = (NativeAdView) inflater.inflate(R.layout.gnt_wortise_medium_template_view, null);
                                                break;
                                        }
                                        populateNativeAdView(context, nativeAd, adView, darkTheme, nativeBackgroundDark, nativeBackgroundLight);
                                        wortiseNativeAd.removeAllViews();
                                        wortiseNativeAd.addView(adView);
                                        wortiseNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                        Log.d(TAG, "[Backup] Wortise Native Ad loaded");
                                    }
                                });
                                mGoogleNativeAd.load();
                            } else {
                                Log.d(TAG, "[Backup] Wortise Native Ad has been loaded");
                            }
                        }
                        break;

                    default:
                        nativeAdViewContainer.setVisibility(View.GONE);
                        break;

                }
            }
        }
    }

    public void loadNativeAd(Context context, String adStatus, int placementStatus, String adNetwork, String backupAdNetwork, String adMobNativeId, String adManagerNativeId, String fanNativeId, String appLovinNativeId, String appLovinDiscMrecZoneId, boolean darkTheme, boolean legacyGDPR, String nativeAdStyle, int nativeBackgroundLight, int nativeBackgroundDark) {
        if (adStatus.equals(AD_STATUS_ON)) {
            if (placementStatus != 0) {
                switch (adNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        if (admobNativeAd.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(context, adMobNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admobNativeAd.setStyles(styles);
                                            admobNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admobNativeAd.setStyles(styles);
                                            admobNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                        }
                                        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        admobNativeAd.setNativeAd(NativeAd);
                                        admobNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            loadBackupNativeAd(context, adStatus, placementStatus, backupAdNetwork, adMobNativeId, adManagerNativeId, fanNativeId, appLovinNativeId, appLovinDiscMrecZoneId, darkTheme, legacyGDPR, nativeAdStyle, nativeBackgroundLight, nativeBackgroundDark);
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getAdRequest((Activity) context, legacyGDPR));
                        } else {
                            Log.d(TAG, "AdMob native ads has been loaded");
                        }
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        if (adManagerNativeAd.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(context, adManagerNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            adManagerNativeAd.setStyles(styles);
                                            adManagerNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            adManagerNativeAd.setStyles(styles);
                                            adManagerNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                        }
                                        adManagerMediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        adManagerNativeAd.setNativeAd(NativeAd);
                                        adManagerNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            loadBackupNativeAd(context, adStatus, placementStatus, backupAdNetwork, adMobNativeId, adManagerNativeId, fanNativeId, appLovinNativeId, appLovinDiscMrecZoneId, darkTheme, legacyGDPR, nativeAdStyle, nativeBackgroundLight, nativeBackgroundDark);
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getGoogleAdManagerRequest());
                        } else {
                            Log.d(TAG, "Ad Manager Native Ad has been loaded");
                        }
                        break;

                    default:
                        break;

                }
            }
        }
    }

    public void loadBackupNativeAd(Context context, String adStatus, int placementStatus, String backupAdNetwork, String adMobNativeId, String adManagerNativeId, String fanNativeId, String appLovinNativeId, String appLovinDiscMrecZoneId, boolean darkTheme, boolean legacyGDPR, String nativeAdStyle, int nativeBackgroundLight, int nativeBackgroundDark) {
        if (adStatus.equals(AD_STATUS_ON)) {
            if (placementStatus != 0) {
                switch (backupAdNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        if (admobNativeAd.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(context, adMobNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admobNativeAd.setStyles(styles);
                                            admobNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admobNativeAd.setStyles(styles);
                                            admobNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                        }
                                        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        admobNativeAd.setNativeAd(NativeAd);
                                        admobNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            admobNativeAd.setVisibility(View.GONE);
                                            nativeAdViewContainer.setVisibility(View.GONE);
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getAdRequest((Activity) context, legacyGDPR));
                        } else {
                            Log.d(TAG, "AdMob native ads has been loaded");
                        }
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        if (adManagerNativeAd.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(context, adManagerNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            adManagerNativeAd.setStyles(styles);
                                            adManagerNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            adManagerNativeAd.setStyles(styles);
                                            adManagerNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                        }
                                        adManagerMediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        adManagerNativeAd.setNativeAd(NativeAd);
                                        adManagerNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            adManagerNativeAd.setVisibility(View.GONE);
                                            nativeAdViewContainer.setVisibility(View.GONE);
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getGoogleAdManagerRequest());
                        } else {
                            Log.d(TAG, "Ad Manager Native Ad has been loaded");
                        }
                        break;

                    default:
                        nativeAdViewContainer.setVisibility(View.GONE);
                        break;

                }
            }
        }
    }

    public void loadNativeAd(Context context, String adStatus, int placementStatus, String adNetwork, String backupAdNetwork, String adMobNativeId, String adManagerNativeId, String fanNativeId, String appLovinNativeId, boolean darkTheme, boolean legacyGDPR, String nativeAdStyle, int nativeBackgroundLight, int nativeBackgroundDark) {
        if (adStatus.equals(AD_STATUS_ON)) {
            if (placementStatus != 0) {
                switch (adNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        if (admobNativeAd.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(context, adMobNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admobNativeAd.setStyles(styles);
                                            admobNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admobNativeAd.setStyles(styles);
                                            admobNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                        }
                                        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        admobNativeAd.setNativeAd(NativeAd);
                                        admobNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            loadBackupNativeAd(context, adStatus, placementStatus, backupAdNetwork, adMobNativeId, adManagerNativeId, fanNativeId, appLovinNativeId, darkTheme, legacyGDPR, nativeAdStyle, nativeBackgroundLight, nativeBackgroundDark);
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getAdRequest((Activity) context, legacyGDPR));
                        } else {
                            Log.d(TAG, "AdMob native ads has been loaded");
                        }
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        if (adManagerNativeAd.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(context, adManagerNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            adManagerNativeAd.setStyles(styles);
                                            adManagerNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            adManagerNativeAd.setStyles(styles);
                                            adManagerNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                        }
                                        adManagerMediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        adManagerNativeAd.setNativeAd(NativeAd);
                                        adManagerNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            loadBackupNativeAd(context, adStatus, placementStatus, backupAdNetwork, adMobNativeId, adManagerNativeId, fanNativeId, appLovinNativeId, darkTheme, legacyGDPR, nativeAdStyle, nativeBackgroundLight, nativeBackgroundDark);
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getGoogleAdManagerRequest());
                        } else {
                            Log.d(TAG, "Ad Manager Native Ad has been loaded");
                        }
                        break;

                    default:
                        break;
                }
            }
        }
    }

    public void loadBackupNativeAd(Context context, String adStatus, int placementStatus, String backupAdNetwork, String adMobNativeId, String adManagerNativeId, String fanNativeId, String appLovinNativeId, boolean darkTheme, boolean legacyGDPR, String nativeAdStyle, int nativeBackgroundLight, int nativeBackgroundDark) {
        if (adStatus.equals(AD_STATUS_ON)) {
            if (placementStatus != 0) {
                switch (backupAdNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        if (admobNativeAd.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(context, adMobNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admobNativeAd.setStyles(styles);
                                            admobNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admobNativeAd.setStyles(styles);
                                            admobNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                        }
                                        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        admobNativeAd.setNativeAd(NativeAd);
                                        admobNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            admobNativeAd.setVisibility(View.GONE);
                                            nativeAdViewContainer.setVisibility(View.GONE);
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getAdRequest((Activity) context, legacyGDPR));
                        } else {
                            Log.d(TAG, "AdMob native ads has been loaded");
                        }
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        if (adManagerNativeAd.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(context, adManagerNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            adManagerNativeAd.setStyles(styles);
                                            adManagerNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            adManagerNativeAd.setStyles(styles);
                                            adManagerNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                        }
                                        adManagerMediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        adManagerNativeAd.setNativeAd(NativeAd);
                                        adManagerNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            adManagerNativeAd.setVisibility(View.GONE);
                                            nativeAdViewContainer.setVisibility(View.GONE);
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getGoogleAdManagerRequest());
                        } else {
                            Log.d(TAG, "Ad Manager Native Ad has been loaded");
                        }
                        break;

                    default:
                        nativeAdViewContainer.setVisibility(View.GONE);
                        break;

                }
            }
        }
    }

    public void loadNativeAd(Context context, String adStatus, int placementStatus, String adNetwork, String backupAdNetwork, String adMobNativeId, String adManagerNativeId, String fanNativeId, String appLovinNativeId, boolean darkTheme, boolean legacyGDPR, String nativeAdStyle) {
        if (adStatus.equals(AD_STATUS_ON)) {
            if (placementStatus != 0) {
                switch (adNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        if (admobNativeAd.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(context, adMobNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, R.color.color_native_background_dark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admobNativeAd.setStyles(styles);
                                            admobNativeBackground.setBackgroundResource(R.color.color_native_background_dark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, R.color.color_native_background_light));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admobNativeAd.setStyles(styles);
                                            admobNativeBackground.setBackgroundResource(R.color.color_native_background_light);
                                        }
                                        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        admobNativeAd.setNativeAd(NativeAd);
                                        admobNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            loadBackupNativeAd(context, adStatus, placementStatus, backupAdNetwork, adMobNativeId, adManagerNativeId, fanNativeId, appLovinNativeId, darkTheme, legacyGDPR, nativeAdStyle);
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getAdRequest((Activity) context, legacyGDPR));
                        } else {
                            Log.d(TAG, "AdMob native ads has been loaded");
                        }
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        if (adManagerNativeAd.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(context, adManagerNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, R.color.color_native_background_dark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            adManagerNativeAd.setStyles(styles);
                                            adManagerNativeBackground.setBackgroundResource(R.color.color_native_background_dark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, R.color.color_native_background_light));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            adManagerNativeAd.setStyles(styles);
                                            adManagerNativeBackground.setBackgroundResource(R.color.color_native_background_light);
                                        }
                                        adManagerMediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        adManagerNativeAd.setNativeAd(NativeAd);
                                        adManagerNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            loadBackupNativeAd(context, adStatus, placementStatus, backupAdNetwork, adMobNativeId, adManagerNativeId, fanNativeId, appLovinNativeId, darkTheme, legacyGDPR, nativeAdStyle);
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getGoogleAdManagerRequest());
                        } else {
                            Log.d(TAG, "Ad Manager Native Ad has been loaded");
                        }
                        break;

                    default:
                        break;

                }
            }
        }
    }

    public void loadBackupNativeAd(Context context, String adStatus, int placementStatus, String backupAdNetwork, String adMobNativeId, String adManagerNativeId, String fanNativeId, String appLovinNativeId, boolean darkTheme, boolean legacyGDPR, String nativeAdStyle) {
        if (adStatus.equals(AD_STATUS_ON)) {
            if (placementStatus != 0) {
                switch (backupAdNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        if (admobNativeAd.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(context, adMobNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, R.color.color_native_background_dark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admobNativeAd.setStyles(styles);
                                            admobNativeBackground.setBackgroundResource(R.color.color_native_background_dark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, R.color.color_native_background_light));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admobNativeAd.setStyles(styles);
                                            admobNativeBackground.setBackgroundResource(R.color.color_native_background_light);
                                        }
                                        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        admobNativeAd.setNativeAd(NativeAd);
                                        admobNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            admobNativeAd.setVisibility(View.GONE);
                                            nativeAdViewContainer.setVisibility(View.GONE);
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getAdRequest((Activity) context, legacyGDPR));
                        } else {
                            Log.d(TAG, "AdMob native ads has been loaded");
                        }
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        if (adManagerNativeAd.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(context, adManagerNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, R.color.color_native_background_dark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            adManagerNativeAd.setStyles(styles);
                                            adManagerNativeBackground.setBackgroundResource(R.color.color_native_background_dark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, R.color.color_native_background_light));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            adManagerNativeAd.setStyles(styles);
                                            adManagerNativeBackground.setBackgroundResource(R.color.color_native_background_light);
                                        }
                                        adManagerMediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        adManagerNativeAd.setNativeAd(NativeAd);
                                        adManagerNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            adManagerNativeAd.setVisibility(View.GONE);
                                            nativeAdViewContainer.setVisibility(View.GONE);
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getGoogleAdManagerRequest());
                        } else {
                            Log.d(TAG, "Ad Manager Native Ad has been loaded");
                        }
                        break;

                    default:
                        nativeAdViewContainer.setVisibility(View.GONE);
                        break;

                }
            }
        }
    }

    public void setNativeAdPadding(int left, int top, int right, int bottom) {
        nativeAdViewContainer.setPadding(left, top, right, bottom);
    }

    public void setNativeAdMargin(int left, int top, int right, int bottom) {
        setMargins(nativeAdViewContainer, left, top, right, bottom);
    }

    public void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public void setNativeAdBackgroundResource(int drawableBackground) {
        nativeAdViewContainer.setBackgroundResource(drawableBackground);
    }

    public void setNativeAdBackgroundColor(Context context, boolean darkTheme, int nativeBackgroundLight, int nativeBackgroundDark) {
        if (darkTheme) {
            nativeAdViewContainer.setBackgroundColor(ContextCompat.getColor(context, nativeBackgroundDark));
        } else {
            nativeAdViewContainer.setBackgroundColor(ContextCompat.getColor(context, nativeBackgroundLight));
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void populateNativeAdView(Context context, com.google.android.gms.ads.nativead.NativeAd nativeAd, NativeAdView nativeAdView, boolean darkTheme, int nativeBackgroundDark, int nativeBackgroundLight) {

        if (darkTheme) {
            nativeAdViewContainer.setBackgroundColor(ContextCompat.getColor(context, nativeBackgroundDark));
            nativeAdView.findViewById(R.id.background).setBackgroundResource(nativeBackgroundDark);
        } else {
            nativeAdViewContainer.setBackgroundColor(ContextCompat.getColor(context, nativeBackgroundLight));
            nativeAdView.findViewById(R.id.background).setBackgroundResource(nativeBackgroundLight);
        }

        nativeAdView.setMediaView(nativeAdView.findViewById(R.id.media_view));
        nativeAdView.setHeadlineView(nativeAdView.findViewById(R.id.primary));
        nativeAdView.setBodyView(nativeAdView.findViewById(R.id.body));
        nativeAdView.setCallToActionView(nativeAdView.findViewById(R.id.cta));
        nativeAdView.setIconView(nativeAdView.findViewById(R.id.icon));

        ((TextView) nativeAdView.getHeadlineView()).setText(nativeAd.getHeadline());
        nativeAdView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        if (nativeAd.getBody() == null) {
            nativeAdView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            nativeAdView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) nativeAdView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            nativeAdView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            nativeAdView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) nativeAdView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            nativeAdView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) nativeAdView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            nativeAdView.getIconView().setVisibility(View.VISIBLE);
        }

        nativeAdView.setNativeAd(nativeAd);
    }

}
