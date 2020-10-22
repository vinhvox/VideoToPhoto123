package com.example.videotophoto123;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.VerifiedInputEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.Set;

public class Setting extends AppCompatActivity   {
    TextView txtFileFormat, txtQuality, txtSize;
    String typeFileFormat, valueQuality, valueSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        readStatusFile();
        setupViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setupViews() {
         txtFileFormat =  findViewById(R.id.txtFileFormat);
         txtQuality = findViewById(R.id.txtQuality);
         txtSize= findViewById(R.id.txtSize);
       setValueFileFormat();
       setValueQuality();
       setValueSize();
    }

    private void setValueSize() {
        if (valueSize == null){
            txtSize.setText(getString(R.string.size_1x));
        }
        else {
            txtSize.setText(valueSize);
        }
        txtSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSize();
            }
        });
    }
    private  void  dialogSize(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_size);
        RadioButton radioSize05 = dialog.findViewById(R.id.radioSize05);
        RadioButton radioSize1 = dialog.findViewById(R.id.radioSize1);
        RadioButton radioSize15 = dialog.findViewById(R.id.radioSize15);
        RadioButton radioSize2 = dialog.findViewById(R.id.radioSize2);
        RadioButton radioSize3 = dialog.findViewById(R.id.radioSize3);
        ImageView imageDeleteDialog =dialog.findViewById(R.id.imageDeleteDialog);
        radioSize05.setOnCheckedChangeListener(listenerSize);
        radioSize1.setOnCheckedChangeListener(listenerSize);
        radioSize15.setOnCheckedChangeListener(listenerSize);
        radioSize2.setOnCheckedChangeListener(listenerSize);
        radioSize3.setOnCheckedChangeListener(listenerSize);
        imageDeleteDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private  void  setValueQuality(){
        if (valueQuality.equals("")){
            txtQuality.setText(getString(R.string.high));
        }
        else {
            txtQuality.setText(valueQuality);
        }
        txtQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogQuality();
            }
        });

    }
    private  void  setValueFileFormat(){
        if (typeFileFormat.equals("")){
            txtFileFormat.setText(getString(R.string.jpg));
        }
        else {
            txtFileFormat.setText(typeFileFormat);
        }
        txtFileFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFileFormat();
            }
        });
    }
    private  void dialogQuality(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_quality);
        RadioButton radioBest = dialog.findViewById(R.id.radioBest);
        RadioButton radioVeryHigh = dialog.findViewById(R.id.radioVeryHigh);
        RadioButton radioHigh = dialog.findViewById(R.id.radioHigh);
        RadioButton radioMedium = dialog.findViewById(R.id.radioMedium);
        RadioButton radioLow = dialog.findViewById(R.id.radioLow);
        ImageView imageView =dialog.findViewById(R.id.imageDeleteDialog);
        if (valueQuality== null){
            radioHigh.setChecked(true);
        }
        else {
           if (radioBest.getText().equals(valueQuality)){
               radioBest.setChecked(true);
           } else if (radioVeryHigh.getText().equals(valueQuality)){
               radioVeryHigh.setChecked(true);
           }else if (radioHigh.getText().equals(valueQuality)){
               radioHigh.setChecked(true);
           }else if (radioMedium.getText().equals(valueQuality)){
               radioMedium.setChecked(true);
           }else if (radioLow.getText().equals(valueQuality)){
               radioLow.setChecked(true);
           }
        }
        radioBest.setOnCheckedChangeListener(listenerQuality);
        radioVeryHigh.setOnCheckedChangeListener(listenerQuality);
        radioHigh.setOnCheckedChangeListener(listenerQuality);
        radioMedium.setOnCheckedChangeListener(listenerQuality);
        radioLow.setOnCheckedChangeListener(listenerQuality);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private  void  dialogFileFormat(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_file_format);

        RadioButton radioJPG = dialog.findViewById(R.id.radioJPG);
        RadioButton radioPNG = dialog.findViewById(R.id.radioPNG);
        ImageView imgDismiss = dialog.findViewById(R.id.imageDeleteDialog);
        if (typeFileFormat.equals("")){
            radioJPG.setChecked(true);
        }
        else {
           if (radioJPG.getText().equals(typeFileFormat)){
               radioJPG.setChecked(true);
           } else  if (radioPNG.getText().equals(typeFileFormat)){
               radioPNG.setChecked(true);
           }
        }
        imgDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        radioJPG.setOnCheckedChangeListener(listenerFileFormat);
        radioPNG.setOnCheckedChangeListener(listenerFileFormat);
        dialog.show();
    }
    private  void  saveStatusFile( String key, String type){
        SharedPreferences sharedPreferences = getSharedPreferences("myData", MODE_PRIVATE);
        SharedPreferences.Editor editor  = sharedPreferences.edit();
        editor.putString(key, type);
        editor.commit();
    }
    private  void readStatusFile(){
        SharedPreferences sharedPreferences = getSharedPreferences("myData", MODE_PRIVATE);
        typeFileFormat=  sharedPreferences.getString("typeFile", getString(R.string.jpg));
        valueQuality = sharedPreferences.getString("valueQuality", getString(R.string.high));
        valueSize = sharedPreferences.getString("valueSize", getString(R.string.x));

    }
    CompoundButton.OnCheckedChangeListener listenerFileFormat
            = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                txtFileFormat.setText(compoundButton.getText());
                saveStatusFile("typeFile", compoundButton.getText().toString());
            }
        }
    };
    CompoundButton.OnCheckedChangeListener listenerQuality
            = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                txtQuality.setText(compoundButton.getText());
                saveStatusFile("valueQuality", compoundButton.getText().toString());
            }
        }
    };
    CompoundButton.OnCheckedChangeListener listenerSize
            = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                txtSize.setText(compoundButton.getText());
                saveStatusFile("valueSize", compoundButton.getText().toString());
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}