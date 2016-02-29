package io.liamju.comm.util.takephoto;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.liamju.comm.BuildConfig;
import io.liamju.comm.util.L;
import io.liamju.comm.util.SDCardUtils;
import io.liamju.comm.util.Utils;
import io.liamju.comm.util.crop.AlbumStorageDirFactory;
import io.liamju.comm.util.crop.BaseAlbumDirFactory;
import io.liamju.comm.util.crop.FroyoAlbumDirFactory;
import io.liamju.comm.util.imageloader.ImageResizer;

/**
 * @author LiamJu
 * @version 1.0
 * @since 16/2/27
 */
public class TakePhoto {

    public static final int ACTION_TAKE_PHOTO_B = 1;
    public static final int ACTION_TAKE_PHOTO_S = 2;
    private int mAction;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private AlbumStorageDirFactory mAlbumStorageDirFactory;
    private String mCurrentPhotoPath;
    private String mAlbumName = "CameraSample";

    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private Bitmap mImageBitmap;

    private Callback mCallback;

    public TakePhoto() {
        if (Utils.hasFroyo()) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }
        mCurrentPhotoPath = null;
        mImageBitmap = null;
    }

    public void dispatchTakeBigPicIntent(Fragment fragment) {
        dispatchTakePicIntent(fragment, ACTION_TAKE_PHOTO_B);
    }

    public void dispatchTakeSmallPicIntent(Fragment fragment) {
        dispatchTakePicIntent(fragment, ACTION_TAKE_PHOTO_S);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB) // API 11
    private void dispatchTakePicIntent(Fragment fragment, int requestCode) {
        if (SDCardUtils.isSDCardEnable()) {
            mAction = requestCode;
        } else {
            mAction = requestCode = ACTION_TAKE_PHOTO_S;
        }
        Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        switch (requestCode) {
            case ACTION_TAKE_PHOTO_B:
                File file = null;

                try {
                    file = setupPhotoFile();
                    mCurrentPhotoPath = file.getAbsolutePath();
                    takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                } catch (IOException e) {
                    e.printStackTrace();
                    file = null;
                    mCurrentPhotoPath = null;
                }
                break;

            default:
                break;
        }
        fragment.startActivityForResult(takePicIntent, requestCode);
    }

    public void dispatchTakeBigPicIntent(android.support.v4.app.Fragment fragment) {
        dispatchTakePicIntent(fragment, ACTION_TAKE_PHOTO_B);
    }

    public void dispatchTakeSmallPicIntent(android.support.v4.app.Fragment fragment) {
        dispatchTakePicIntent(fragment, ACTION_TAKE_PHOTO_S);
    }

    public void dispatchTakeBigPicIntent(Activity activity) {
        dispatchTakePicIntent(activity, ACTION_TAKE_PHOTO_B);
    }

    public void dispatchTakeSmallPicIntent(Activity activity) {
        dispatchTakePicIntent(activity, ACTION_TAKE_PHOTO_S);
    }

    private void dispatchTakePicIntent(Activity activity, int action) {
        if (SDCardUtils.isSDCardEnable()) {
            mAction = action;
        } else {
            mAction = action = ACTION_TAKE_PHOTO_S;
        }
        Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        switch (action) {
            case ACTION_TAKE_PHOTO_B:
                File file = null;

                try {
                    file = setupPhotoFile();
                    mCurrentPhotoPath = file.getAbsolutePath();
                    takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                } catch (IOException e) {
                    e.printStackTrace();
                    file = null;
                    mCurrentPhotoPath = null;
                }
                break;

            default:
                break;
        }

        activity.startActivityForResult(takePicIntent, action);
    }

    /**
     * 只有 mAction 为 {@link #ACTION_TAKE_PHOTO_B} 执行此方法才有效
     *
     * @param view
     */
    public void handleBigCameraPhoto(ImageView view) {

        if (BuildConfig.DEBUG) {
            L.d("handleBigCameraPhoto");
        }
        if (mCurrentPhotoPath != null) {
            setPic(view);
            mCurrentPhotoPath = null;
        }
    }

    public void handleSmallCameraPhoto(Intent intent, ImageView view) {

        if (BuildConfig.DEBUG) {
            L.d("handleSmallCameraPhoto");
        }

        Bundle extras = intent.getExtras();
        mImageBitmap = (Bitmap) extras.get("data");
        view.setImageBitmap(mImageBitmap);
    }

    // Some lifecycle callbacks so that the image can survive orientation change
    public void saveBitmap(Bundle outState) {
        outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
    }


    public void restoreBitmap(Bundle saveInstanceState, ImageView imageView) {
        mImageBitmap = saveInstanceState.getParcelable(BITMAP_STORAGE_KEY);
        imageView.setImageBitmap(mImageBitmap);
    }

    private void dispatchTakePicIntent(android.support.v4.app.Fragment fragment, int requestCode) {
        if (SDCardUtils.isSDCardEnable()) {
            mAction = requestCode;
        } else {
            mAction = requestCode = ACTION_TAKE_PHOTO_S;
        }
        Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        switch (requestCode) {
            case ACTION_TAKE_PHOTO_B:
                File file = null;

                try {
                    file = setupPhotoFile();
                    mCurrentPhotoPath = file.getAbsolutePath();
                    takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                } catch (IOException e) {
                    e.printStackTrace();
                    file = null;
                    mCurrentPhotoPath = null;
                }
                break;

            default:
                break;
        }
        fragment.startActivityForResult(takePicIntent, requestCode);
    }

    private void setPic(ImageView view) {
        int targetW = view.getWidth();
        int targetH = view.getHeight();

        // 得到图片的尺寸
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, options);

        // 获得采样率
        int inSampleSize = ImageResizer.calculateInSampleSize(options, targetW, targetH);

        // set bitmap options, 缩放图片解码目标对象
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        options.inPurgeable = true;

        // 通过 bitmap options 解码
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, options);

        // bitmap 关联到 ImageView
        view.setImageBitmap(mImageBitmap = bitmap);
    }

    private File setupPhotoFile() throws IOException {
        File f = createImageFile();
        return f;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    public File getAlbumDir() {
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(mAlbumName);
            if (storageDir != null) {
                if (!storageDir.exists()) {
                    if (!storageDir.mkdirs()) {
                        if (mCallback != null) {
                            mCallback.createDirFailed();
                        }
                    }
                }
            }
        } else {
            if (mCallback != null) {
                mCallback.externalNotMounted();
            }
        }
        return storageDir;
    }

    public TakePhoto albumName(String albumName) {
        this.mAlbumName = albumName;
        return this;
    }

    public TakePhoto callback(Callback callback) {
        mCallback = callback;
        return this;
    }

    public interface Callback {

        void createDirFailed();

        void externalNotMounted();
    }

}
