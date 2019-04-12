package com.core.lib_core.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.core.lib_core.CoreApplication;
import com.core.lib_core.utils.file.FileUtil;
import com.core.lib_core.utils.file.StreamUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * @author miaoxin.ye
 * @createdate 2013-12-16 下午7:56:03
 * @Description: 图片工具类
 */
public class ImageUtils {

    /**
     * @param context
     * @param resId
     * @return
     * @Description: 读取本地图像
     */
    public static Bitmap readBitmap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * @param bitmap
     * @return
     * @Description: Bitmap转Drawable
     */
    public static Drawable BitmapToDrawable(Bitmap bitmap) {
        if (bitmap != null) {
            return new BitmapDrawable(bitmap);
        }
        return null;
    }

    /**
     * @param drawable
     * @return
     * @Description: Drawable转Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * @param bitmap
     * @param pixels
     * @return
     * @Description: 圆角图片
     */
    public static Bitmap getfilletBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.BLACK);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        bitmap.recycle();
        return output;
    }

    //生成圆角图片
    public static Bitmap getfilletBitmap(Bitmap bitmap) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight()));
            final float roundPx = 15;
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

            final Rect src = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());

            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        } catch (Exception e) {
            return bitmap;
        }
    }

    /**
     * @param imageUrl
     * @param reqWidth
     * @param reqHeight
     * @return
     * @throws IOException
     * @throws MalformedURLException
     * @Description: 从网络获取图片缩略图
     */
    public static Bitmap getThumbnailFromUrl(String imageUrl, int reqWidth, int reqHeight) {
        InputStream is = StreamUtil.getInputStreamFromUrl(imageUrl);
        byte[] data = StreamUtil.convertToByteArray(is);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        opts.inSampleSize = computeSampleSize(opts, reqWidth, reqHeight);
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, opts);
    }

    /**
     * @param filePath
     * @param reqWidth
     * @param reqHeight
     * @return
     * @throws IOException
     * @throws MalformedURLException
     * @Description: 从文件获取图片缩略图
     */
    public static Bitmap getThumbnailFromFile(String filePath, int reqWidth, int reqHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);
        opts.inSampleSize = computeSampleSize(opts, reqWidth, reqHeight);
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, opts);
    }

    /**
     * @param source
     * @param targetWidth
     * @param targetHeight
     * @return
     * @Description: 指定宽高缩放图像(推荐方法)
     */
    public static Bitmap extractBitmap(Bitmap source, int targetWidth, int targetHeight) {
        return extractThumbnail(source, targetWidth, targetHeight, Option.NONE);
    }

    /**
     * @param source 图像
     * @param width  宽
     * @param height 高
     * @return
     * @Description: 指定宽高缩放图像
     */
    public static Bitmap zoomBitmap(Bitmap source, int width, int height) {
        return Bitmap.createScaledBitmap(source, width, height, true);
    }

    /**
     * @param source
     * @param targetWidth
     * @param targetHeight
     * @return
     * @Description: 按比例缩放图像(推荐方法)
     */
    public static Bitmap prorateBitmap(Bitmap source, int targetWidth, int targetHeight) {
        if (source == null) {
            return null;
        }
        int srcWidth = source.getWidth();
        int srcHeight = source.getHeight();
        if (srcWidth < srcHeight) {
            targetHeight = srcHeight * targetWidth / srcWidth;
        } else {
            targetWidth = srcWidth * targetHeight / srcHeight;
        }
        return extractThumbnail(source, targetWidth, targetHeight, Option.NONE);
    }

    /**
     * @param bitmap    图像
     * @param maxWidth  最大宽
     * @param maxHeight 最大高
     * @return
     * @Description: 按比例缩放图像
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();
        if (originWidth < maxWidth && originHeight < maxHeight) {
            return bitmap;
        }
        int newWidth = originWidth;
        int newHeight = originHeight;
        // 若图片过宽, 则保持长宽比缩放图片
        if (originWidth > maxWidth) {
            newWidth = maxWidth;
            double i = originWidth * 1.0 / maxWidth;
            newHeight = (int) Math.floor(originHeight / i);
            bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        }
        // 若图片过高, 则从中部截取
        if (newHeight > maxHeight) {
            newHeight = maxHeight;
            int half_diff = (int) ((originHeight - maxHeight) / 2.0);
            bitmap = Bitmap.createBitmap(bitmap, 0, half_diff, newWidth, newHeight);
        }
        return bitmap;
    }

    /**
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     * @Description: 计算缩放比例
     */
    private static int computeSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    /**
     * @param context
     * @param path
     * @Description: 添加到图库
     */
    public static void galleryAddPic(Context context, String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * scale source Bitmap to targeted width and height
     */
    private static Bitmap extractThumbnail(Bitmap source, int targetWidth, int targetHeight, int options) {
        if (source == null) {
            return null;
        }
        float scale;
        if (source.getWidth() < source.getHeight()) {
            scale = targetWidth / (float) source.getWidth();
        } else {
            scale = targetHeight / (float) source.getHeight();
        }
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        return transform(matrix, source, targetWidth, targetHeight, Option.SCALE_UP | options);
    }

    /**
     * Transform source Bitmap to targeted width and height
     */
    private static Bitmap transform(Matrix scaler, Bitmap source, int targetWidth, int targetHeight, int options) {
        boolean scaleUp = (options & Option.SCALE_UP) != 0;
        boolean recycle = (options & Option.RECYCLE_INPUT) != 0;
        int deltaX = source.getWidth() - targetWidth;
        int deltaY = source.getHeight() - targetHeight;
        if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
            Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b2);
            int deltaXHalf = Math.max(0, deltaX / 2);
            int deltaYHalf = Math.max(0, deltaY / 2);
            Rect src = new Rect(deltaXHalf, deltaYHalf, deltaXHalf + Math.min(targetWidth, source.getWidth()), deltaYHalf
                    + Math.min(targetHeight, source.getHeight()));
            int dstX = (targetWidth - src.width()) / 2;
            int dstY = (targetHeight - src.height()) / 2;
            Rect dst = new Rect(dstX, dstY, targetWidth - dstX, targetHeight - dstY);
            c.drawBitmap(source, src, dst, null);
            if (recycle) {
                source.recycle();
            }
            return b2;
        }
        float bitmapWidthF = source.getWidth();
        float bitmapHeightF = source.getHeight();
        float bitmapAspect = bitmapWidthF / bitmapHeightF;
        float viewAspect = (float) targetWidth / targetHeight;
        if (bitmapAspect > viewAspect) {
            float scale = targetHeight / bitmapHeightF;
            if (scale < .9F || scale > 1F) {
                scaler.setScale(scale, scale);
            } else {
                scaler = null;
            }
        } else {
            float scale = targetWidth / bitmapWidthF;
            if (scale < .9F || scale > 1F) {
                scaler.setScale(scale, scale);
            } else {
                scaler = null;
            }
        }
        Bitmap b1;
        if (scaler != null) {
            b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), scaler, true);
        } else {
            b1 = source;
        }
        if (recycle && b1 != source) {
            source.recycle();
        }
        int dx1 = Math.max(0, b1.getWidth() - targetWidth);
        int dy1 = Math.max(0, b1.getHeight() - targetHeight);
        Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth, targetHeight);
        if (b2 != b1) {
            if (recycle || b1 != source) {
                b1.recycle();
            }
        }
        return b2;
    }

    private interface Option {
        int NONE = 0x0;
        int SCALE_UP = 0x1;
        int RECYCLE_INPUT = 0x2;
    }

    /**
     * @param bitmap
     * @param _file
     * @throws IOException
     * @author miaoxin.ye
     * @createdate 2014-1-20 下午3:26:14
     * @Description: 保存图片到文件
     */
    public static void saveBitmapToFile(Bitmap bitmap, String _file) {
        BufferedOutputStream os = null;
        try {
            File file = new File(_file);
            int end = _file.lastIndexOf(File.separator);
            String _filePath = _file.substring(0, end);
            File filePath = new File(_filePath);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            file.createNewFile();
            os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param path
     * @return
     * @throws IOException
     * @author miaoxin.ye
     * @createdate 2014-1-23 上午11:01:53
     * @Description:
     */
    public static String compressionImage(String path) {
        File f = new File(path);
        if (f.exists()) {
            String extensionName = FileUtil.getExtensionName(f.getName());
            String fileName = String.valueOf(System.currentTimeMillis());
            String newName = fileName + "." + extensionName;
            String newPath = CoreApplication.IMAGE_UPLOAD_TEMP + newName;
            FileInputStream fis;
            try {
                fis = new FileInputStream(f);
                long size = fis.available();
                fis.close();
                fis = null;
                if (size > 204800L) {   //200KB以内不压缩
                    Bitmap bm = getSmallBitmap(path);
                    File newFile = new File(newPath);
                    //先创建这个文件夹，不然会找不到这个文件的
                    File folder = new File(CoreApplication.IMAGE_UPLOAD_TEMP);
                    if (!folder.exists()) {
                        folder.mkdir();
                    }

                    if (!newFile.exists()) {
                        newFile.createNewFile();
                    } else {
                    }
                    FileOutputStream fos = new FileOutputStream(newFile);
                    bm.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                    if (fos != null) {
                        fos.close();
                        fos = null;
                    }
                    bm.recycle();
                    bm = null;
                    fis = new FileInputStream(new File(path));
                    fis.close();
                    fis = null;
                    return newPath;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    /**
     * 转换图片成圆形
     *
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width,
                height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     *
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
}
