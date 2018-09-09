package comw.example.user.testbigdigappb;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import comw.example.user.testbigdigappb.AlarmNotification.AlarmNotification;
import comw.example.user.testbigdigappb.Common.LinkStatus;
import comw.example.user.testbigdigappb.DataBase.DataBaseManager;

public class ChangeImageStatusActivity extends AppCompatActivity {
    private final static String EXTRA_ID = "id";

    private ImageView imageLink;
    private Intent intent;
    private String link;

    private int id;
    private int status;
    private DataBaseManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_image_status);

        manager = new DataBaseManager(getApplicationContext());
        intent = getIntent();

        imageLink = (ImageView) findViewById(R.id.imageLink2);

        id = Integer.parseInt(new Integer(intent.getStringExtra(EXTRA_ID)).toString());

        link = manager.getLinkById(id);
        status = manager.getStatus(id);

        if(status == LinkStatus.STATUS_DOWNLOADED){
            showImage(getLink());

            //test alarm
            AlarmNotification alarmNotification = new AlarmNotification(this);
            alarmNotification.scheduleNotification(alarmNotification.getNotification(),15000,id);

        } else {
            if(checkConnection()){
                showImageAgain(id); ////
            } else {
                manager.editValueDataBase(id,LinkStatus.STATUS_UNKNOWN,System.currentTimeMillis());
            }
        }
    }

    private boolean checkConnection() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showImageAgain(final int id) {
        Glide.with(getApplicationContext())
                .asBitmap()
                .load(link)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        imageLink.setImageBitmap(resource);
                        manager.editValueDataBase(id,LinkStatus.STATUS_DOWNLOADED,System.currentTimeMillis());
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        manager.editValueDataBase(id,LinkStatus.STATUS_ERROR,System.currentTimeMillis());
                    }
                });
    }

    private void showImage(String link) {
        Glide.with(getApplicationContext())
                .asBitmap()
                .load(link)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        imageLink.setImageBitmap(resource);
                        saveImage(resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                    }
                });
    }

    public String getLink() {
        return link;
    }

    private void saveImage(Bitmap image) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this,"SD карта не доступна:",Toast.LENGTH_SHORT).show();
            return;
        }

        String savedImagePath = null;

        String imageFileName = "JPEG_" + System.currentTimeMillis() + ".jpg";

        File storageDir = new File(Environment.getExternalStorageDirectory() + File.separator +"BIGDIG/test/B");

        boolean success = true;

        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }

        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            galleryAddPic(savedImagePath);
        }
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }
}
