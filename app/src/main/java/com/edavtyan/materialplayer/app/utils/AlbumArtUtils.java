package com.edavtyan.materialplayer.app.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.edavtyan.materialplayer.app.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;

public class AlbumArtUtils {
    public static File getArtFileFromId(int id, Context context) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    new String[] { MediaStore.Audio.Albums.ALBUM_ART },
                    MediaStore.Audio.Albums._ID + "=" + id,
                    null, null);
            cursor.moveToFirst();

            String artPath = cursor.getString(0);
            return getArtFileFromPath(artPath);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static File getArtFileFromPath(String artPath) {
        File artFile = null;
        if (artPath != null) {
            artFile = new File(artPath);
        }

        return artFile;
    }

    public static RequestCreator getFullArtRequest(Context context, File artFile) {
        return Picasso
                .with(context)
                .load(artFile)
                .placeholder(R.drawable.fallback_cover)
                .error(R.drawable.fallback_cover);
    }

    public static RequestCreator getFullArtistArtRequest(Context context) {
        return Picasso
                .with(context)
                .load(R.drawable.fallback_artist);
    }
    public static RequestCreator getSmallArtistArtRequest(Context context) {
        return Picasso
                .with(context)
                .load(R.drawable.fallback_artist_listitem);
    }


    public static RequestCreator getSmallArtRequest(Context context, File artFile) {
        return Picasso
                .with(context)
                .load(artFile)
                .placeholder(R.drawable.fallback_cover_listitem)
                .error(R.drawable.fallback_cover_listitem);
    }
}