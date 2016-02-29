package io.liamju.comm.util.crop;

import android.os.Environment;

import java.io.File;

/**
 * @author LiamJu
 * @version 1.0
 * @since 16/2/27
 */
public class BaseAlbumDirFactory extends AlbumStorageDirFactory {
    private static final String CREATE_DIR = "/dcim/";

    @Override
    public File getAlbumStorageDir(String albumName) {
        return new File(Environment.getExternalStorageDirectory()
                + CREATE_DIR
                + albumName);
    }
}
