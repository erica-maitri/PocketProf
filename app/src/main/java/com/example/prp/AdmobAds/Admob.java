package com.example.prp.AdmobAds;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class Admob {

    public static void loadBannerAd(LinearLayout banner, Context context) {
        if (AdsUnit.isAds) {
            AdView adView = new AdView(context);
            adView.setAdUnitId(AdsUnit.BANNER);
            adView.setAdSize(AdSize.BANNER);
            banner.addView(adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
    }

    public static void loadInterstitial(Context context) {
        if (AdsUnit.isAds) {
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(context, AdsUnit.INTERSTITIAL, adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    AdsUnit.mInterstitialAd = interstitialAd;
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    AdsUnit.mInterstitialAd = null;
                }
            });
        }
    }

    public static void showInterstitial(Activity activity, OnDismiss onDismiss, boolean isReload) {
        if (AdsUnit.mInterstitialAd != null) {
            AdsUnit.mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    if (onDismiss != null) {
                        onDismiss.onDismiss();
                    }
                    if (isReload) {
                        AdsUnit.mInterstitialAd = null;
                        loadInterstitial(activity);
                    }
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    if (onDismiss != null) {
                        onDismiss.onDismiss();
                    }
                }
            });
            AdsUnit.mInterstitialAd.show(activity);
        } else {
            if (onDismiss != null) {
                onDismiss.onDismiss();
            }
        }
    }
}
