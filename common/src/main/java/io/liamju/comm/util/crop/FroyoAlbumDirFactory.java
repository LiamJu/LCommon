package io.liamju.comm.util.crop;

import android.os.Environment;

import java.io.File;

/**
 * @author LiamJu
 * @version 1.0
 * @since 16/2/27
 */
public class FroyoAlbumDirFactory extends AlbumStorageDirFactory {
    @Override
    public File getAlbumStorageDir(String albumName) {
        return new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                albumName
        );
    }
}
