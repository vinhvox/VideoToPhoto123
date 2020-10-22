package com.example.videotophoto123;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_CAPTURE = 200 ;
    private  int PERMISSION_CODE = 110;
    private  int  PERMISSION_GALLERY_CODE = 100;
    CardView cardViewGallery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        cardViewGallery = findViewById(R.id.cardViewGallery);
        setupViews();




    }
    private  void  setupViews(){
      CardView  cardViewSelectVideo = findViewById(R.id.cardViewSelectVideo);
      CardView cardViewSetting =  findViewById(R.id.cardViewSetting);
      CardView cardViewSlideshowMaker = findViewById(R.id.cardViewSlideshowMaker);
      CardView cardViewAboutApp = findViewById(R.id.cardviewAboutApp);
      CardView cardViewShareApp = findViewById(R.id.cardviewShareApp);
      CardView cardViewRateApp = findViewById(R.id.cardviewRateApp);

      cardViewGallery.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              openGallery();
          }
      });
        cardViewSelectVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFolder();
            }
        });
        cardViewSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(MainActivity.this, Setting.class));
            }
        });
        cardViewSlideshowMaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(MainActivity.this, SlideShows.class));
            }
        });
        cardViewShareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/developer?id=AppsPelago&hl=vi";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });
        cardViewAboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAbout();

            }
        });
        cardViewRateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateMy();
            }
        });

    }
    public void rateMy(){
        try {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=AppsPelago&hl=vi")));
        }
        catch (ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=AppsPelago&hl=vi")));
        }
    }
//    private  void  checkPermissionGallery(){
//        if (Build.VERSION.SDK_INT> Build.VERSION_CODES.M){
//            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
//             checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)  == PackageManager.PERMISSION_DENIED){
//                String[] permission = { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                requestPermissions(permission, PERMISSION_GALLERY_CODE );
//            }
//            else {
//                openGallery();
//            }
//        }
//        else {
//            openGallery();
//        }
//    }

    private  void  openGallery(){
      startActivity(new Intent(MainActivity.this, MainGallery.class));
    }
    private  void checkPermission(){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
            || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_CODE );
            }
            else {
            }
        }
        else {
        }
    }
    private  void openFolder(){
        startActivity( new Intent(MainActivity.this, SelectVideo.class));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE){
            if (grantResults.length> 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this, "Permission Granted...", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(MainActivity.this, "Permission denied...", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(MainActivity.this, "Permission denied...", Toast.LENGTH_LONG).show();
        }
//        if (requestCode == PERMISSION_GALLERY_CODE){
//            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                openGallery();
//            }
//            else {
//                Toast.makeText(MainActivity.this, "Permission denied...", Toast.LENGTH_LONG).show();
//            }
//        }
    }
    private  void  dialogAbout(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.about_us);
        TextView txtAuthorName = dialog.findViewById(R.id.txtAuthorName);
        TextView txtPrivatePolicy = dialog.findViewById(R.id.txtPrivatePolicy);
        txtAuthorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WebViewAbout.class);
                intent.putExtra("LINK", getString(R.string.link));
                startActivity(intent);
            }
        });
        txtPrivatePolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WebViewAbout.class);
                intent.putExtra("LINK", getString(R.string.link));
                startActivity(intent);
            }
        });
        dialog.show();

    }


}