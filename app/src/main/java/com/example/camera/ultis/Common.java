package com.example.camera.ultis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Common {
    public static String getDate(){
        Date currentTime = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat spf=new SimpleDateFormat("dd MM yyyy");
        Log.e("~~~",spf.format(currentTime));
        return spf.format(currentTime);

    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static List<String> getNameOverlayFromPack(Context context, String namePack) {
        List<String> list = new ArrayList<>();
        AssetManager assetManager;
        try {
            assetManager = context.getAssets();
            list = Arrays.asList(assetManager.list("overlay/"+namePack));
        } catch (IOException e) {

        }
        return list;
    }
    public static Drawable[] getDrawableFromAssets(Context context, String nameFolder , String namePack){
        AssetManager assetManager;
        try {
            // to reach asset
            assetManager = context.getAssets();
            // to get all item in dogs folder.
            String[] images = assetManager.list(nameFolder+"/"+namePack);
            // to keep all image
            Drawable[] drawables = new Drawable[images.length];
            // the loop read all image in dogs folder and  aa
            for (int i = 0; i < images.length; i++) {
                InputStream inputStream = context.getAssets().open(nameFolder+"/"+namePack+"/" + images[i]);
                Drawable drawable = Drawable.createFromStream(inputStream, null);
                drawables[i] = drawable;
            }
            return drawables;
        } catch (IOException e) {
            // you can print error or log.
        }
        return null;
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

    public static Drawable getDrawableInAssets(Context context, String folder, String namePack , String nameImage ) throws IOException {
        InputStream inputStream = context.getAssets().open(folder+"/" + namePack + "/"+ nameImage);
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
            list = Arrays.asList(assetManager.list("overlay"));
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
