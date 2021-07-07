package com.example.camera.ultis;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.io.ByteArrayOutputStream;

public class BitmapUlti {
    public static Bitmap fitScreen(Bitmap mBitmap) {

        double paramHeight = 0.73;
        float ratioImage = (float) mBitmap.getWidth() / (mBitmap.getHeight());                                     // width/height of bitmap
        float ratioScreen = (float) ((float) Common.getScreenWidth() / (Common.getScreenHeight() * paramHeight));   // width/height of screen
        int newWidth = (int) (mBitmap.getWidth() * Common.getScreenHeight() * paramHeight / mBitmap.getHeight());
        int newHeight = mBitmap.getHeight() * Common.getScreenWidth() / mBitmap.getWidth();

        if (ratioImage < ratioScreen) { //scale bitmap by Height
            mBitmap = BitmapUlti.getResizedBitmap(mBitmap, newWidth, (int) (Common.getScreenHeight() * paramHeight));
        } else {                     //scale bitmap by width
            mBitmap = BitmapUlti.getResizedBitmap(mBitmap, Common.getScreenWidth(), newHeight);
        }
        return mBitmap;
    }

    public static byte[] convertToArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
    public static Bitmap rotateBitmap(Bitmap source)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(15);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    //    _____________________
    //param max -255 255
    public static Bitmap adjustSaturation(Bitmap bm, float value) {
        value = cleanValue(value, 100);
        if (value == 0) {
            return bm;
        }

        float x = 1 + ((value > 0) ? 3 * value / 100 : value / 100);
        float lumR = 0.3086f;
        float lumG = 0.6094f;
        float lumB = 0.0820f;

        float[] mat = new float[]
                {
                        lumR * (1 - x) + x, lumG * (1 - x), lumB * (1 - x), 0, 0,
                        lumR * (1 - x), lumG * (1 - x) + x, lumB * (1 - x), 0, 0,
                        lumR * (1 - x), lumG * (1 - x), lumB * (1 - x) + x, 0, 0,
                        0, 0, 0, 1, 0,
                        0, 0, 0, 0, 1
                };
        ColorMatrix cm = new ColorMatrix(mat);
        Bitmap sat = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), bm.getConfig());

        Canvas canvas = new Canvas(sat);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bm, 0, 0, paint);

        return sat;
    }
//param -150 150
    public static Bitmap adjustBrightness(Bitmap bm, float brightness) {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        1, 0, 0, 0, brightness,
                        0, 1, 0, 0, brightness,
                        0, 0, 1, 0, brightness,
                        0, 0, 0, 1, 0,
                });
        Bitmap bri = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), bm.getConfig());

        Canvas canvas = new Canvas(bri);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bm, 0, 0, paint);

        return bri;
    }
//param 0 - 10
    public static Bitmap adjustedContrast(Bitmap bm, float contrast) {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, 0,
                        0, contrast, 0, 0, 0,
                        0, 0, contrast, 0, 0,
                        0, 0, 0, 1, 0,
                });
        Bitmap con = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), bm.getConfig());

        Canvas canvas = new Canvas(con);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bm, 0, 0, paint);

        return con;
    }

    public static Bitmap adjustedBitmap(Bitmap bm, float brightness, float contrast, float saturation) {
       float b=brightness;
       float c = contrast;
       float s =saturation;
       float t = (float) ((1.0 - c) / 2.0);
       float lumR = 0.3086f;
       float lumG = 0.6094f;
       float lumB = 0.0820f;
       float sr = (1 - s) * lumR;
       float sg = (1 - s) * lumG;
       float sb = (1 - s) * lumB;


        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        c*(sr+s), c*sr, c*sr, 0, 0,
                        c*sg, c*(sg+s), c*sg, 0, 0,
                        c*sb, c*sb, c*(sb+s), 0, 0,
                        0, 0, 0, 1, 0,
                        t+b,t+b,t+b,0,1,
                });
        Bitmap con = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), bm.getConfig());

        Canvas canvas = new Canvas(con);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bm, 0, 0, paint);
        return con;
    }


    public static Bitmap blur(Bitmap bmp, int radius) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int[] pix = new int[w * h];
        bmp.getPixels(pix, 0, w, 0, 0, w, h);

        for (int r = radius; r >= 1; r /= 2) {
            for (int i = r; i < h - r; i++) {
                for (int j = r; j < w - r; j++) {
                    int tl = pix[(i - r) * w + j - r];
                    int tr = pix[(i - r) * w + j + r];
                    int tc = pix[(i - r) * w + j];
                    int bl = pix[(i + r) * w + j - r];
                    int br = pix[(i + r) * w + j + r];
                    int bc = pix[(i + r) * w + j];
                    int cl = pix[i * w + j - r];
                    int cr = pix[i * w + j + r];

                    pix[(i * w) + j] = 0xFF000000 |
                            (((tl & 0xFF) + (tr & 0xFF) + (tc & 0xFF) + (bl & 0xFF) +
                                    (br & 0xFF) + (bc & 0xFF) + (cl & 0xFF) + (cr & 0xFF)) >> 3) & 0xFF |
                            (((tl & 0xFF00) + (tr & 0xFF00) + (tc & 0xFF00) + (bl & 0xFF00)
                                    + (br & 0xFF00) + (bc & 0xFF00) + (cl & 0xFF00) + (cr & 0xFF00)) >> 3) & 0xFF00 |
                            (((tl & 0xFF0000) + (tr & 0xFF0000) + (tc & 0xFF0000) +
                                    (bl & 0xFF0000) + (br & 0xFF0000) + (bc & 0xFF0000) + (cl & 0xFF0000) +
                                    (cr & 0xFF0000)) >> 3) & 0xFF0000;
                }
            }
        }
        Bitmap blurred = Bitmap.createBitmap(w, h, bmp.getConfig());
        blurred.setPixels(pix, 0, w, 0, 0, w, h);
        return blurred;
    }

    protected static float cleanValue(float p_val, float p_limit) {
        return Math.min(p_limit, Math.max(-p_limit, p_val));
    }
}
