package comw.example.user.testbigdigappb;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import comw.example.user.testbigdigappb.Common.LinkStatus;
import comw.example.user.testbigdigappb.DataBase.DataBaseManager;

public class ImageActivity extends Activity {
    private ImageView imageLink;

    private Intent intent;
    private String link;

    private DataBaseManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        isStoragePermissionGranted();

        manager = new DataBaseManager(getApplicationContext());

        intent = getIntent();
        link = intent.getStringExtra("link");

        imageLink = (ImageView) findViewById(R.id.imageLink);

        if (checkConnection()) {
            showImage(link);
        } else {
            manager.addToDataBase(getLink(), LinkStatus.STATUS_UNKNOWN,System.currentTimeMillis());
        }
    }
    private boolean checkConnection() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showImage(String link) {
        Glide.with(getApplicationContext())
                .asBitmap()
                .load(link)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        imageLink.setImageBitmap(resource);
                        manager.addToDataBase(getLink(),LinkStatus.STATUS_DOWNLOADED,System.currentTimeMillis());
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        manager.addToDataBase(getLink(),LinkStatus.STATUS_ERROR,System.currentTimeMillis());
                    }
                });
    }

    // поменять
    @Override
    protected void onStop() {
        super.onStop();
        finish();
        System.exit(0);
    }

    public String getLink() {
        return link;
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission. WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission. WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("tag","Permission: "+permissions[0]+ "was "+grantResults[0]);
        }
    }
}
