package com.core.lib_core.utils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.format.DateFormat;

import com.core.lib_core.constants.CoreConstant;
import com.core.lib_core.utils.file.FileUtil;

import java.io.File;
import java.util.Date;
import java.util.Random;

/**
 * @author 李澄锋<br>
 * 2018/11/19
 */
public class CameraUtils {

    private Activity mActivity;
    /**
     * 拍照头像路径
     */
    private String photoPath;
    /**
     * 上传头像路径
     */
    private String uploadPhotoPath;

    private Uri uri;

    private ITakePhotoUrl photoUrl;

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getUploadPhotoPath() {
        return uploadPhotoPath;
    }

    public void setUploadPhotoPath(String uploadPhotoPath) {
        this.uploadPhotoPath = uploadPhotoPath;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public ITakePhotoUrl getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(ITakePhotoUrl photoUrl) {
        this.photoUrl = photoUrl;
    }

    public CameraUtils(Activity mActivity) {
        this.mActivity = mActivity;
    }

    // 打开相机 拍照 默认裁剪为身份证长宽比
    public void takeCardPhoto() {
        takeCardPhoto(true);
    }

    public void takeCardPhoto(boolean isCrop) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        updateURIAndPath();
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        if (isCrop) {
            mActivity.startActivityForResult(cameraIntent, CoreConstant.UPLOAD_CARD_TAKEPHOTO);
        } else {
            mActivity.startActivityForResult(cameraIntent, CoreConstant.UPLOAD_PICTURE_TAKE);
        }
    }

    /**
     * 打开相册 默认裁剪 身份证尺寸比例
     */
    public void getCardPhoto() {
        getCardPhoto(true);
    }

    //从相册中选择
    public void getCardPhoto(boolean isCrop) {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        if (isCrop) {
            mActivity.startActivityForResult(intent, CoreConstant.UPLOAD_CARD_GALLERY);
        } else {
            mActivity.startActivityForResult(intent, CoreConstant.UPLOAD_PICTURE_GALLERY);
        }
    }

    private void updateURIAndPath() {
        String fileDir = FileUtil.getSDCardPath() + "DCIM/Camera/";
        FileUtil.checkDir(fileDir);
        File imageFile = new File(fileDir, DateFormat.format("yyyy-MM-dd-hh-mm-ss", new Date()) + "_" + new Random().nextInt(1000) + ".jpg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(mActivity, mActivity.getPackageName() + ".fileprovider", imageFile);
        } else {
            uri = Uri.fromFile(imageFile);
        }
    }

    public void getUriToPath(Intent data) {
        if (data == null) {
            return;
        }
        Uri uri = data.getData();
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = mActivity.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                try {
                    photoPath = cursor.getString(column_index);
                    uploadPhotoPath = ImageUtils.compressionImage(photoPath);
                    if (photoUrl != null) {
                        photoUrl.getUploadPhotoPath(uploadPhotoPath);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }
    }

    public void getPath() {
        try {
            uploadPhotoPath = ImageUtils.compressionImage(photoPath);
            NiceLogUtil.d("------->uploadPhotoPath=" + uploadPhotoPath);
            if (photoUrl != null) {
                photoUrl.getUploadPhotoPath(uploadPhotoPath);
            }
        } catch (Exception e) {
            NiceLogUtil.i("---------->exception=" + e.toString());
        }
    }

    /**
     * 获取上传图片的回调函数
     */
    public interface ITakePhotoUrl {
        void getUploadPhotoPath(String uploadPhotoPath);
    }

    /**
     * 打开相机 拍照 默认裁剪为500， 500
     */
    public void takePhoto() {
        takePhoto(true);
    }

    public void takePhoto(boolean isCrop) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        updateURIAndPath();
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        if (isCrop) {
            mActivity.startActivityForResult(cameraIntent, CoreConstant.UPLOAD_PICTURE_TAKE_HEAD);
        } else {
            mActivity.startActivityForResult(cameraIntent, CoreConstant.UPLOAD_PICTURE_CROP);
        }
    }

    /**
     * 打开相册 默认裁剪500， 500
     */
    public void getPhoto() {
        getPhoto(true);
    }

    public void getPhoto(boolean isCrop) {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        if (isCrop) {
            mActivity.startActivityForResult(intent, CoreConstant.UPLOAD_PICTURE_HEAD);
        } else {
            mActivity.startActivityForResult(intent, CoreConstant.UPLOAD_PICTURE_GALLERY);
        }
    }

    public void cropImageUri(Uri uri, int aspectX, int aspectY, int outputX, int outputY, int requestCode) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        intent.putExtra("crop", "true");

        intent.putExtra("aspectX", aspectX);

        intent.putExtra("aspectY", aspectY);

        intent.putExtra("outputX", outputX);

        intent.putExtra("outputY", outputY);

        intent.putExtra("scale", true);

        String fileDir = FileUtil.getSDCardPath() + "com.goxueche/";
        FileUtil.checkDir(fileDir);
        File imageFile = new File(fileDir, DateFormat.format("yyyy-MM-dd-hh-mm-ss", new Date()) + "_" + new Random().nextInt(1000) + ".jpg");
        this.uri = Uri.fromFile(imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, this.uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        intent.putExtra("return-data", false);

        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        intent.putExtra("noFaceDetection", true); // no face detection
        mActivity.startActivityForResult(intent, requestCode);
    }

}
