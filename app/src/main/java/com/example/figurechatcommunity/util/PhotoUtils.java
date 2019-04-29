package com.example.figurechatcommunity.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class PhotoUtils {

    // 图片在SD卡中的缓存路径
    public static final String IMAGE_PATH = Environment
            .getExternalStorageDirectory().toString() + File.separator
            + "Bmob" + File.separator + "Images" + File.separator;

    public static String IMAGE_NAME = "personal_avatar";

    public static String WORKABLUM = "workablum";

    public static String CERTIFICATE_NAME = "certificate";

    public static String APPOINTMENT_NAME = "appointment";

    public static final String PHOTO_TYPE = ".jpg";

    //RequestCode
    public static final int INTENT_REQUEST_CODE_ALBUM = 0;

    public static final int INTENT_REQUEST_CODE_CROP = 2;

    public static final int INTENT_REQUEST_CODE_CAMERA = 1;

 
 

    public static final int INTENT_REQUEST_CODE_CAMERA_PATIENT_PICTRUE = 12;  //相机拍照返回
    public static final int INTENT_REQUEST_CODE_PICK_PATIENT_PICTRUE = 13;    //图库选择返回


    /**
     * 判断SD卡
     */
    public static boolean isSDCard() {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;

    }


    /**
     * ͨ通过手机相册获取图片
     *
     * @param activity
     */
    public static void selectPhoto(Activity activity, int requestCode) {

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        //打开文件
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intent, requestCode);
    }

    public static String getUserPicPath() {

        return IMAGE_PATH + IMAGE_NAME + PHOTO_TYPE;

    }

    public static String getPicDirPath() {

        return IMAGE_PATH;

    }

    public static String getPicPath(String account, int requestCode) {

        StringBuffer Path = new StringBuffer(IMAGE_PATH);
        if (android.text.TextUtils.isEmpty(account))
            account = UUID.randomUUID().toString().replaceAll("-", "");

        Path.append(account);
        switch (requestCode) {
            case INTENT_REQUEST_CODE_ALBUM:
            case INTENT_REQUEST_CODE_CAMERA:
                Path.append(IMAGE_NAME);
                break;
            
        }

        return Path.append(PHOTO_TYPE).toString();

    }

    public static String takePicture(Activity activity) {

        return takePicture(activity, INTENT_REQUEST_CODE_CAMERA);
    }

    /**
     * 调用相机拍照
     *
     * @param activity
     * @param requestCode
     * @return
     */
    public static String takePicture(Activity activity, int requestCode) {

        //判断目录是否存在，不存在创建
        if (FileUtils.createDirFile(IMAGE_PATH)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //保存的文件名
            String path = IMAGE_PATH + UUID.randomUUID().toString().replace("-", "") + PHOTO_TYPE;
            //创建给文件
            File file = FileUtils.createNewFile(path);
            //失败不设置输出源
            if (file != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            }
            //调用系统相机
            activity.startActivityForResult(intent, requestCode);
            //返回路径
            return path;
        } else {
            //失败返回空
            return null;
        }
    }

    public static String savePhotoToSDCard(Bitmap bitmap, String account, int requestCode) {
        // TODO 裁剪 压缩 保存
        if (!FileUtils.isSdcardExist()) {
            return null;
        }


        FileUtils.createDirFile(IMAGE_PATH);

        String newFilePath = getPicPath(account, requestCode);
        File file = FileUtils.createNewFile(newFilePath);

        if (file == null) {
            return null;
        }

        FileOutputStream fileOutputStream = null;
        int quality = 100;
        try {
            fileOutputStream = new FileOutputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            while (quality > 10 && baos.toByteArray().length > 1024 * 1024) {
                quality -= 10;
                baos.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            }
            baos.writeTo(fileOutputStream);
            baos.flush();
            baos.close();
        } catch (FileNotFoundException e) {

            return null;
        } catch (IOException e) {

        } finally {
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                return null;
            }
        }

        return newFilePath;
    }

    
    public static String saveBitmap(Bitmap bitmap,String path){
        
        File file = FileUtils.createNewFile(path);

        if (file == null) {
            return null;
        }

        FileOutputStream fileOutputStream = null;
        int quality = 100;
        try {
            fileOutputStream = new FileOutputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            while (quality > 10 && baos.toByteArray().length > 1024 * 1024) {
                quality -= 10;
                baos.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            }
            baos.writeTo(fileOutputStream);
            baos.flush();
            baos.close();
        } catch (FileNotFoundException e) {

            return null;
        } catch (IOException e) {

        } finally {
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                return null;
            }
        }

        return path;
    }
    
    
    public static BitmapFactory.Options decodeResourceToOpt(Resources res, int resId) {

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, opts);

        return opts;
    }

    public static BitmapFactory.Options decodeBitmapToOpt(String path) {

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);
        return opts;

    }


    public static Bitmap readBitmap(String path) {

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        return BitmapFactory.decodeFile(path, opt);
    }


    public static Bitmap createBitmap(String path, int w, int h) {

        try {

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opts);

            int srcWidth = opts.outWidth;
            int srcHeight = opts.outHeight;

            int destWidth = 0;
            int destHeight = 0;

            double ratio = 0.0;

            if (srcWidth < w || srcHeight < h) { //任意一边小于屏幕 不压缩比例

                ratio = 0.0;
                destWidth = srcWidth;
                destHeight = srcHeight;

            } else if (srcWidth > srcHeight) {

                ratio = (double) srcWidth / w;
                destWidth = w;
                destHeight = (int) (srcHeight / ratio);

            } else {

                ratio = (double) srcHeight / h;
                destHeight = h;
                destWidth = (int) (srcWidth / ratio);

            }


            BitmapFactory.Options newOpts = new BitmapFactory.Options();

            newOpts.inSampleSize = (int) ratio + 1;
            newOpts.inJustDecodeBounds = false;
            newOpts.outHeight = destHeight;
            newOpts.outWidth = destWidth;

            return BitmapFactory.decodeFile(path, newOpts);

        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 判断图片高度和宽度是否过大
     *
     * @param bitmap 图片bitmap对象
     * @return
     */
    public static boolean bitmapIsLarge(Bitmap bitmap) {
        final int MAX_WIDTH = 100;
        final int MAX_HEIGHT = 100;
        Bundle bundle = getBitmapWidthAndHeight(bitmap);
        if (bundle != null) {
            int width = bundle.getInt("width");
            int height = bundle.getInt("height");
            if (width > MAX_WIDTH && height > MAX_HEIGHT) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取图片的长度和宽度
     *
     * @param bitmap 图片bitmap对象
     * @return
     */
    public static Bundle getBitmapWidthAndHeight(Bitmap bitmap) {
        Bundle bundle = null;
        if (bitmap != null) {
            bundle = new Bundle();
            bundle.putInt("width", bitmap.getWidth());
            bundle.putInt("height", bitmap.getHeight());
            return bundle;
        }
        return null;
    }

    /**
     * @param context
     * @param height
     * @return
     */
    public static int getMetricsDensity(Context context, float height) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(localDisplayMetrics);
        return Math.round(height * localDisplayMetrics.densityDpi / 160.0F);
    }

    public static int getImageWeidth(Context context, float pixels) {
        return context.getResources().getDisplayMetrics().widthPixels - 66 - DensityUtil.dip2px(pixels);
    }





    /**
     * <br>功能简述:4.4及以上获取图片的方法
     * <br>功能详细描述:
     * <br>注意:
     * @param context
     * @param uri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}
