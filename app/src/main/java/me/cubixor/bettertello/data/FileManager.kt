package me.cubixor.bettertello.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Environment;

import androidx.exifinterface.media.ExifInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import me.cubixor.bettertello.App;
import me.cubixor.telloapi.api.listeners.FileReceiver;


public class FileManager implements FileReceiver {

    public static File getDirectoryPath() {
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) +
                File.separator + "BetterTello");
        dir.mkdirs();

        return dir;
    }

    @Override
    public void onPhotoReceived(byte[] data) {
        System.out.println("PHOTO RECEIVED ");

        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        File file = new File(getDirectoryPath(), System.currentTimeMillis() + ".jpg");

        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ExifInterface exif = new ExifInterface(file.getCanonicalPath());
            exif.setAttribute(ExifInterface.TAG_DATETIME_ORIGINAL, LocalDateTime.now().toString());
            exif.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MediaScannerConnection.scanFile(App.Companion.getInstance().getApplicationContext(), new String[]{file.getAbsolutePath()}, null, (path, uri) -> {
        });


    }

}
