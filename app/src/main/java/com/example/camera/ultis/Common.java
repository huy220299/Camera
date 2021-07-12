package com.example.camera.ultis;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Common {
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    public int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static Drawable getFirstInPack(Context context, String namePack) throws IOException {
        InputStream inputStream = context.getAssets().open("sticker/" + namePack + "/0.png");
        return Drawable.createFromStream(inputStream, null);
    }

    public static Drawable getStickerInAssets(Context context, String nameSticker ,String namePack ) throws IOException {
        InputStream inputStream = context.getAssets().open("sticker/" + namePack + "/"+ nameSticker);
        return Drawable.createFromStream(inputStream, null);
    }

    public static List<String> getStickerFromAssets(Context context) {
        List<String> list = new ArrayList<>();
        AssetManager assetManager;
        try {
            assetManager = context.getAssets();
            list = Arrays.asList(assetManager.list("sticker"));
        } catch (IOException e) {

        }
        return list;
    }
    public static List<String> getListOverlayFromAssets(Context context) {
        List<String> list = new ArrayList<>();
        AssetManager assetManager;
        try {
            assetManager = context.getAssets();
            list = Arrays.asList(assetManager.list("overley"));
        } catch (IOException e) {

        }
        return list;
    }
    public static List<String> getNameStickerFromPack(Context context, String namePack) {
        List<String> list = new ArrayList<>();
        AssetManager assetManager;
        try {
            assetManager = context.getAssets();
            list = Arrays.asList(assetManager.list("sticker/"+namePack));
        } catch (IOException e) {

        }
        return list;
    }

    public static List<String> getNameFilter(Context context) {
        List<String> list = new ArrayList<>();
        AssetManager assetManager;
        try {
            assetManager = context.getAssets();
            list = Arrays.asList(assetManager.list("filter"));
        } catch (IOException e) {

        }
        return list;
    }
}
