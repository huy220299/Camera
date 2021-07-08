package com.example.camera.ultis;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by snowbean on 16-8-5.
 */
public class FileUtil {
  private static final String TAG = "FileUtil";

  public static String getFolderName(String name) {
    File mediaStorageDir =
        new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            name);

    if (!mediaStorageDir.exists()) {
      if (!mediaStorageDir.mkdirs()) {
        return "";
      }
    }
    return mediaStorageDir.getAbsolutePath();
  }

  /**
   * 判断sd卡是否可以用
   */
  private static boolean isSDAvailable() {
    return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
  }

  public static File getNewFile(Context context, String folderName) {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);

    String timeStamp = simpleDateFormat.format(new Date());

    String path;
    if (isSDAvailable()) {
      path = getFolderName(folderName) + File.separator + timeStamp + ".jpg";
    } else {
      path = context.getFilesDir().getPath() + File.separator + timeStamp + ".jpg";
    }

    if (TextUtils.isEmpty(path)) {
      return null;
    }

    return new File(path);
  }

  public static void saveImage(Context context, Bitmap bitmap, @NonNull String name) throws IOException {
    OutputStream fos;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      ContentResolver resolver = context.getContentResolver();
      ContentValues contentValues = new ContentValues();
      contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name + ".jpg");
      contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
      contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
      Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
      fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
    } else {
      String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
      File image = new File(imagesDir, name + ".jpg");
      fos = new FileOutputStream(image);
    }
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
    Objects.requireNonNull(fos).close();
  }
}
