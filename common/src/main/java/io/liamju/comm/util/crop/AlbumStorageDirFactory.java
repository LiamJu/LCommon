package io.liamju.comm.util.crop;

import java.io.File;

/**
 * @author LiamJu
 * @version 1.0
 * @since 16/2/27
 */
public abstract class AlbumStorageDirFactory {
    public abstract File getAlbumStorageDir(String albumName);
}
