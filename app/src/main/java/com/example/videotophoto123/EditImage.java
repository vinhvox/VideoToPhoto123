package com.example.videotophoto123;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.videotophoto123.Adapter.EmojisAdapter;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.ViewType;
import yuku.ambilwarna.AmbilWarnaDialog;


public class EditImage extends AppCompatActivity implements EmojisAdapter.Callback {
    String path;
    String text;
    Integer colorText;
    LinearLayout layoutCut, frameAddText;
    RelativeLayout framePaint, layoutCrop;
    PhotoEditorView idImageEdit;
    ImageView imageView;
    String type, endWiths, quality;
    int imageHeight, imageWidth;
    CropImageView cropImageView;
    int aspectRatioX, aspectRatioY, defaultColor;
    long size;
    PhotoEditor photoEditor;
    boolean paint = false;
    SeekBar seekBarOpacity;
    Dialog dialog;
    boolean edit  = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.edit_image));

    }

    @Override
    protected void onResume() {
        super.onResume();
        path = getIntent().getStringExtra("PATH_IMAGE");
        setupViews();

        setupCutImage();
        addText();
        setupPaint();
        setupSticker();
        photoEditor.setOnPhotoEditorListener(new OnPhotoEditorListener() {
            @Override
            public void onEditTextChangeListener(View rootView, String text, int colorCode) {
                edit = true;
        }

            @Override
            public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {
                edit = true;
            }

            @Override
            public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {
                edit = true;
            }

            @Override
            public void onStartViewChangeListener(ViewType viewType) {
                edit = true;
            }

            @Override
            public void onStopViewChangeListener(ViewType viewType) {
                edit = true;
            }
        });
    }

    private void setupViews() {
        imageView = findViewById(R.id.imageViewEdt);
        idImageEdit = findViewById(R.id.idImageEdit);
        layoutCut = findViewById(R.id.layoutCut);
        frameAddText = findViewById(R.id.frameAddText);
        framePaint = findViewById(R.id.framePaint);
        layoutCrop = findViewById(R.id.layoutCropImage);
        File file = new File(path);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        imageView.setImageBitmap(bitmap);
        idImageEdit.getSource().setImageBitmap(bitmap);
        cropImageView = findViewById(R.id.cropImageView);
//        cropImageView.setAspectRatio(1,3);

        cropImageView.setImageBitmap(bitmap);
        photoEditor = new PhotoEditor.Builder(this, idImageEdit).build();
        photoEditor.setBrushDrawingMode(false);
        defaultColor = ContextCompat.getColor(this, R.color.black);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_image, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (edit){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setIcon(R.drawable.icon_warning);
                    builder.setTitle(R.string.notify);
                    builder.setMessage("Bạn muốn lưu thay đổi");
                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            saveFile();
                            onBackPressed();
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            onBackPressed();
                        }
                    });
                    builder.show();
                }
                else {
                    onBackPressed();
                }

                break;
            case R.id.imageDetails:
                dialogDetail();
                break;
            case R.id.menuShare:
                eventShare();
                break;
            case R.id.menuDelete:
                eventDelete();
                break;
            case R.id.menuSaveImage:

                saveFile();
                startActivity(new Intent(EditImage.this, MainGallery.class));
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    private void eventDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.delete));
        builder.setIcon(R.drawable.icon_warning);
        builder.setMessage(getString(R.string.message_delete));
        builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                File file = new File(path);
                if (file.delete()) {
                    dialogInterface.dismiss();
                    startActivity(new Intent(EditImage.this, MainGallery.class));
                    finish();
                } else {
                    Toast.makeText(EditImage.this, "Fail", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void dialogDetail() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_image_details);
        getInformationImage();
        TextView txtLocation = dialog.findViewById(R.id.txtLocation);
        TextView txtResolution = dialog.findViewById(R.id.txtResolution);
        TextView txtSize = dialog.findViewById(R.id.txtSize);
        ImageView imageDismissDialog = dialog.findViewById(R.id.imageDismissDialog);
        txtResolution.setText(imageHeight + "x" + imageWidth);
        txtSize.setText(size + "Kb");
        txtLocation.setText(path);
        imageDismissDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getInformationImage() {
        File file = new File(path);
        size = file.length() / 1000;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(path).getAbsolutePath(), options);
        imageHeight = options.outHeight;
        imageWidth = options.outWidth;
//         size = options.outMimeType;
    }

    private void eventShare() {
        final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        Log.e("PATH", path);
        File file = new File(path);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);
        intent.putExtra(Intent.EXTRA_STREAM, photoURI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivity(Intent.createChooser(intent, "Share image"));
    }

    //Cut image
    private void setupCutImage() {
        CardView idCardViewCutImage = findViewById(R.id.idCardViewCutImage);
        Button btnCrop  = findViewById(R.id.btnCrop);

        idCardViewCutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameAddText.setVisibility(View.GONE);
                layoutCut.setVisibility(View.VISIBLE);
                framePaint.setVisibility(View.GONE);
                cropImageView.setVisibility(View.VISIBLE);
                cropImageView.setAutoZoomEnabled(true);
                idImageEdit.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                layoutCrop.setVisibility(View.VISIBLE);
                if (edit){
                    Log.e("EDIT", edit+"");
                    photoEditor.saveAsBitmap(new OnSaveBitmap() {
                        @Override
                        public void onBitmapReady(Bitmap saveBitmap) {
                            imageView.setImageBitmap(saveBitmap);
                            idImageEdit.getSource().setImageBitmap(saveBitmap);
                            cropImageView.setImageBitmap(saveBitmap);
                        }
                        @Override
                        public void onFailure(Exception e) {

                        }
                    });
                }
                else {
                    Log.e("EDIT", edit+" dsds");
                }

            }
        });
        btnCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = cropImageView.getCroppedImage();
                cropImageView.setVisibility(View.GONE);
                layoutCut.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitmap);
                idImageEdit.getSource().setImageBitmap(bitmap);
                cropImageView.setImageBitmap(bitmap);
            }
        });
        croppingWindowAspectRatio();

    }

    private void croppingWindowAspectRatio() {
        RadioButton radioWindow11 = findViewById(R.id.radioWindow11);
        RadioButton radioWindow23 = findViewById(R.id.radioWindow23);
        RadioButton radioWindow46 = findViewById(R.id.radioWindow46);
        RadioButton radioWindow43 = findViewById(R.id.radioWindow43);
        RadioButton radioWindow34 = findViewById(R.id.radioWindow34);
        RadioButton radioWindow31 = findViewById(R.id.radioWindow31);
        radioWindow11.setOnCheckedChangeListener(listener);
        radioWindow23.setOnCheckedChangeListener(listener);
        radioWindow46.setOnCheckedChangeListener(listener);
        radioWindow43.setOnCheckedChangeListener(listener);
        radioWindow34.setOnCheckedChangeListener(listener);
        radioWindow31.setOnCheckedChangeListener(listener);
    }

    CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                switch (compoundButton.getText().toString()) {
                    case "1:1":
                        aspectRatioX = 1;
                        aspectRatioY = 1;
                        break;
                    case "2:3":
                        aspectRatioX = 2;
                        aspectRatioY = 3;
                        break;
                    case "4:6":
                        aspectRatioX = 4;
                        aspectRatioY = 6;
                        break;
                    case "4:3":
                        aspectRatioX = 4;
                        aspectRatioY = 3;
                        break;
                    case "3:4":
                        aspectRatioX = 3;
                        aspectRatioY = 4;
                        break;
                    case "3:1":
                        aspectRatioX = 3;
                        aspectRatioY = 1;
                        break;
                }
                cropImageView.setAspectRatio(aspectRatioX, aspectRatioY);
            }
        }
    };
    // add text
    private void addText() {
        CardView cardView = findViewById(R.id.idCardViewText);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutCrop.setVisibility(View.GONE);
                idImageEdit.setVisibility(View.VISIBLE);
                layoutCut.setVisibility(View.GONE);
                frameAddText.setVisibility(View.VISIBLE);
                framePaint.setVisibility(View.GONE);
                photoEditor.setBrushDrawingMode(false);
            }
        });
        ChangeText();
    }
    private void ChangeText() {
        //Use custom font using latest support library
//loading font from assest
        photoEditor = new PhotoEditor.Builder(this, idImageEdit)
                .setPinchTextScalable(true)
                .build();
        ImageView selectColorText = findViewById(R.id.selectColorText);
        EditText editText = findViewById(R.id.edtTextEdit);
        Button button = findViewById(R.id.btnAddText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                if (!text.isEmpty()) {
                    photoEditor.addText(text, defaultColor);
                    editText.setText("");
                }
            }
        });
        selectColorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setTextColor(defaultColor);
                selectColor();
            }
        });
    }
    // paint
    private void setupPaint() {
        CardView cardViewPaint = findViewById(R.id.cardViewPaint);
        SeekBar seekBarBrush = findViewById(R.id.seekBarBrush);
        seekBarOpacity = findViewById(R.id.seekBarOpacity);
        SeekBar seekBarBrushEraser = findViewById(R.id.seekBarBrushEraser);
        ImageView imageEraser = findViewById(R.id.imageEraser);
        ImageView imageSelectColor = findViewById(R.id.imageSelectColor);
        seekBarBrushEraser.setProgress(50);
        seekBarBrush.setProgress(50);
        seekBarOpacity.setProgress(50);
        cardViewPaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutCrop.setVisibility(View.GONE);
                idImageEdit.setVisibility(View.VISIBLE);
                layoutCut.setVisibility(View.GONE);
                frameAddText.setVisibility(View.GONE);
                framePaint.setVisibility(View.VISIBLE);
                photoEditor.setBrushDrawingMode(true);
            }
        });
        seekBarBrush.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    photoEditor.setBrushSize(i);
                } else {
                    photoEditor.setBrushSize(50);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarBrushEraser.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    photoEditor.setBrushEraserSize(i);
                    photoEditor.brushEraser();
                } else {
                    photoEditor.setBrushEraserSize(50);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    photoEditor.setOpacity(i);
                } else {
                    photoEditor.setOpacity(50);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        imageEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!paint) {
                    imageEraser.setImageResource(R.drawable.icons_paint);
                    photoEditor.setBrushDrawingMode(false);
                    photoEditor.brushEraser();
                    seekBarBrushEraser.setVisibility(View.VISIBLE);
                    seekBarBrush.setVisibility(View.GONE);
                    paint = true;

                } else {
                    imageEraser.setImageResource(R.drawable.icons_eraser);
                    photoEditor.setBrushDrawingMode(true);
                    seekBarBrushEraser.setVisibility(View.GONE);
                    seekBarBrush.setVisibility(View.VISIBLE);
                    paint = false;
                }
            }
        });
        imageSelectColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectColor();
                seekBarOpacity.setBackgroundColor(defaultColor);
                photoEditor.setBrushColor(defaultColor);
            }
        });


    }

    private void selectColor() {

        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color;
                seekBarOpacity.setBackgroundColor(defaultColor);
                photoEditor.setBrushColor(defaultColor);
            }
        });
        ambilWarnaDialog.show();
    }

    // add sticker
    private void setupSticker() {
        CardView addSticker = findViewById(R.id.addSticker);
        addSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               layoutCrop.setVisibility(View.GONE);
                idImageEdit.setVisibility(View.VISIBLE);
                layoutCut.setVisibility(View.GONE);
                frameAddText.setVisibility(View.GONE);
                framePaint.setVisibility(View.GONE);
                photoEditor.setBrushDrawingMode(false);
                ArrayList<String> arrayList = PhotoEditor.getEmojis(EditImage.this);
                dialogSticker(arrayList);
            }
        });
    }

    private void dialogSticker(ArrayList<String> arrayList) {
        dialog = new Dialog(EditImage.this);
        dialog.setContentView(R.layout.recyclerview_emojis);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerViewItem);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(EditImage.this, 5);
        recyclerView.setLayoutManager(gridLayoutManager);
        EmojisAdapter adapter = new EmojisAdapter(arrayList, EditImage.this, this);
        recyclerView.setAdapter(adapter);
        dialog.show();
    }

    private void saveFile() {
        photoEditor.saveAsBitmap(new OnSaveBitmap() {
            @Override
            public void onBitmapReady(Bitmap saveBitmap) {
                Log.e("BITMAP", saveBitmap + "");
                saveFrameToFile(saveBitmap);
                imageView.setImageBitmap(saveBitmap);
                cropImageView.setImageBitmap(saveBitmap);
               idImageEdit.getSource().setImageBitmap(saveBitmap);
               idImageEdit.setVisibility(View.GONE);
               imageView.setVisibility(View.VISIBLE);
               framePaint.setVisibility(View.GONE);
               frameAddText.setVisibility(View.GONE);


            }
            @Override
            public void onFailure(Exception e) {

            }
        });

    }
    private void  saveFrameToFile(Bitmap imageBitmap){
        readDataShareRe();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/VideoToPhoto/Images");
        if(!file.exists()){
            file.mkdir();
        }
        DateTimeFormatter formatter = null;
        String fileName = "";
        Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
        if(type.equals(getString(R.string.jpg))){
            endWiths = ".jpg";
            compressFormat = Bitmap.CompressFormat.JPEG;
        }
        if(type.equals(getString(R.string.png))){
            endWiths = ".png";
            compressFormat = Bitmap.CompressFormat.PNG;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            fileName = String.format(formatter.format(now)+endWiths);
        }
        else fileName = getTimeSys()+endWiths;
        path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ScreenShots/VideoToPhoto/"+fileName;
        File outFile = new File(file,fileName);
        try {
            FileOutputStream fos = new FileOutputStream(outFile);
            imageBitmap.compress(compressFormat,getValueQuality(quality),fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.w("Error",e);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        try {
//            MediaScannerConnection.scanFile(getContext(), new String[]{outFile.getAbsolutePath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
//                @Override
//                public void onMediaScannerConnected() {
//
//                }
//
//                @Override
//                public void onScanCompleted(String s, Uri uri) {
//
//                }
//            });
//        }catch (Exception e){
//
//        }
    }
    private  int  getValueQuality(String quality){
        if(quality.equals(getResources().getString(R.string.best))){
            return 100;
        }
        else if(quality.equals(getResources().getString(R.string.very_high))){
            return 85;
        }
        else if(quality.equals(getResources().getString(R.string.high))){
            return 75;
        }
        else if(quality.equals(getResources().getString(R.string.medium))){
            return 65;
        }
        else if(quality.equals(getResources().getString(R.string.low))){
            return 50;
        }
        else return 75;
    }
    private String getTimeSys(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        return simpleDateFormat.format(calendar.getTime());
    }
    private  void  readDataShareRe(){
        SharedPreferences sharedPreferences = getSharedPreferences("myData", Context.MODE_PRIVATE);
        type  =  sharedPreferences.getString("typeFile", getString(R.string.jpg));
        quality = sharedPreferences.getString("valueQuality", getString(R.string.high));
    }
    @Override
    public void choseSticker(String path) {
        Log.e("STICKER", path);
        photoEditor.addEmoji(path);
        dialog.dismiss();
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}


