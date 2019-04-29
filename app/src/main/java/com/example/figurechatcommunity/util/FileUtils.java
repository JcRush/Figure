package com.example.figurechatcommunity.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class FileUtils {

    /**
     * 判断sd卡是否存在
     *
     * @return
     */
    public static boolean isSdcardExist() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    public static boolean isFileExist(String path){
        if(path == null || path.isEmpty()){
            return false;
        }else{
            File file = new File(path);
            return file.exists();
        }

    }


    /**
     * 创建目录
     *
     * @param path 目录路径
     */
    public static boolean createDirFile(String path) {
        File dir = new File(path);

        if (!dir.exists()) {
            return dir.mkdirs();
        } else {
            return true;
        }
    }

    public static File createNewFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return null;
            }
        }
        return file;
    }

    public static Uri getUriFromFile(String path) throws Exception {

        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists())
                return Uri.fromFile(file);
            else
                return null;
        } else {
            return null;
        }
    }

    public static void renameFile(String path, String oldname, String newname) {

        if (!oldname.equals(newname)) {

            File oldfile = new File(path + File.separator + oldname);
            File newfile = new File(path + File.separator + newname);

            if (!oldfile.exists()) {
                return;
            }

            if (newfile.exists()) {
                System.out.println(newname + "已经存在");
            } else {
                oldfile.renameTo(newfile);
            }
        }

    }


    /**
     * 压缩保存文件
     * @param path 图片的路径
     * @param file 图片的file对象
     */
    public static void compressFile(String path, File file) {

        //头像加载到内存中
        Bitmap bitmap = PhotoUtils.createBitmap(path, DensityUtil.dip2px(80), DensityUtil.dip2px(80));
        //空则返回
        if (null == bitmap)
            return;

        FileOutputStream fileOutputStream = null;
        int quality = 100;
        try {

            //文件输出流
            fileOutputStream = new FileOutputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);

            if (baos.toByteArray().length > 1024 * 1024) {

                while (quality > 10 && baos.toByteArray().length > 1024 * 1024) {

                    quality -= 10;
                    baos.reset();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                }
            }
            baos.writeTo(fileOutputStream);
            baos.flush();
            baos.close();

            if (!bitmap.isRecycled()) bitmap.recycle();

        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {

        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                return;
            }
        }
    }


    /**
     * 保存InputSream到 mdocotr/Images/name为文件名的文件
     * @param name
     * @param in
     * @return
     */
    public static boolean write2SDFromInput(String name, InputStream in) {

        boolean isSave = false;
        name = name.endsWith(PhotoUtils.PHOTO_TYPE) ? name : name + PhotoUtils.PHOTO_TYPE;
        String path = PhotoUtils.getPicDirPath() + name;
        if (isSdcardExist()) {

            File file = null;
            OutputStream output = null;
            try {
                if (createDirFile(PhotoUtils.getPicDirPath())) {
                    file = createNewFile(path);
                    output = new FileOutputStream(file);
                    int total = in.available();
                    isSave = copyStream(in, output);
                    //保存成功，文件大于1mb，进行压缩文件
                    if (isSave && total > 1024 * 1024) {
                        compressFile(path, file);
                    }
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            } finally {
                try {
                    output.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return isSave;
        }
        return isSave;
    }

    /**
     * INputStram转为OutPutStream
     * @param in
     * @param out
     * @return 是否转换成功
     */
    public static boolean copyStream(InputStream in, OutputStream out){
        int c;
        try {
            while((c=in.read())!=-1)
            {
                out.write(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * @param filePath
     * @return
     */
    public static boolean checkFile(String filePath) {
        if (TextUtils.isEmpty(filePath) || !(new File(filePath).exists())) {
            return false;
        }
        return true;
    }

    /**
     * decode file length
     *
     * @param filePath
     * @return
     */
    public static int decodeFileLength(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return 0;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return 0;
        }
        return (int) file.length();
    }

    public static int copyFile(File src, String filename, byte[] buffer) {
        if (!src.exists()) {
            return -1;
        }
        return copyFile(src.getAbsolutePath(), filename, buffer);
    }

    /**
     * 拷贝文件
     *
     * @param fileDir
     * @param fileName
     * @param buffer
     * @return
     */
    public static int copyFile(String fileDir, String fileName, byte[] buffer) {
        if (buffer == null) {
            return -2;
        }

        try {
            File file = new File(fileDir);
            if (!file.exists()) {
                file.mkdirs();
            }
            File resultFile = new File(file, fileName);
            if (!resultFile.exists()) {
                resultFile.createNewFile();
            }
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                    new FileOutputStream(resultFile, true));
            bufferedOutputStream.write(buffer);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            return 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 根据文件名和后缀 拷贝文件
     *
     * @param fileDir
     * @param fileName
     * @param ext
     * @param buffer
     * @return
     */
    public static int copyFile(String fileDir, String fileName, String ext,
                               byte[] buffer) {
        return copyFile(fileDir, fileName + ext, buffer);
    }

    /**
     * @param filePath
     * @param seek
     * @param length
     * @return
     */
    public static byte[] readFlieToByte(String filePath, int seek, int length) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        if (length == -1) {
            length = (int) file.length();
        }

        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            byte[] bs = new byte[length];
            randomAccessFile.seek(seek);
            randomAccessFile.readFully(bs);
            randomAccessFile.close();
            return bs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

 

    /**
     * Gets the extension of a file name, like ".png" or ".jpg".
     *
     * @param uri
     * @return Extension including the dot("."); "" if there is no extension;
     * null if uri was null.
     */
    public static String getExtension(String uri) {
        if (uri == null) {
            return null;
        }

        int dot = uri.lastIndexOf(".");
        if (dot >= 0) {
            return uri.substring(dot);
        } else {
            // No extension.
            return "";
        }
    }

    /**
     * 创建视频的一张缩略图,卡顿 已弃用
     * @param filePath 视频路径
     * @return 返回bitmap
     */
    @SuppressLint("NewApi")
    public static Bitmap createVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            // retriever.setMode(MediaMetadataRetriever.);
            retriever.setDataSource(filePath);

            bitmap = retriever.getFrameAtTime(1000); //获取第一秒时候的图片

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                ex.printStackTrace();
            }

        }
        return bitmap;

    }

    public static String getFileExt(String fileName) {

        if (TextUtils.isEmpty(fileName)) {

            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1,
                fileName.length());
    }

    


 
 
 

    /**
     * 转换成Mb单位
     *
     * @param length
     * @return
     */
    public static String formatSizeMb(long length) {
        float mbSize = Math.round(10.0F * (float) length / 1048576.0F) / 10.0F;
        return mbSize + " MB";
    }

    /**
     * 转换成单位
     *
     * @param length
     * @return
     */
    public static String formatFileLength(long length) {
        if (length >> 30 > 0L) {
            float sizeGb = Math.round(10.0F * (float) length / 1.073742E+009F) / 10.0F;
            return sizeGb + " GB";
        }
        if (length >> 20 > 0L) {
            return formatSizeMb(length);
        }
        if (length >> 9 > 0L) {
            float sizekb = Math.round(10.0F * (float) length / 1024.0F) / 10.0F;
            return sizekb + " KB";
        }
        return length + " B";
    }


    /**
     * 检查SDCARD是否可写
     *
     * @return
     */
    public static boolean checkExternalStorageCanWrite() {
        try {
            boolean mouted = Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED);
            if (mouted) {
                boolean canWrite = new File(Environment
                        .getExternalStorageDirectory().getAbsolutePath())
                        .canWrite();
                if (canWrite) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }




}
