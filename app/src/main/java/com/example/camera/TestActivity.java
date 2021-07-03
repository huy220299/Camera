package com.example.camera;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestActivity extends AppCompatActivity implements ListTextStyleAdapter.ItemClickListener {
    EditText editText;
    ImageView btnDone, btnCancel;
    RecyclerView recyclerView;
    ListTextStyleAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_sheet_layout);
        editText = findViewById(R.id.edtAddedText);
//        btnDone = findViewById(R.id.btnDone);
//        btnCancel = findViewById(R.id.btnCancel);

        btnDone.setOnClickListener(v -> {
            Log.e("~~~", editText.getText().toString());
        });
        btnCancel.setOnClickListener(v -> {
           editText.setGravity(Gravity.CENTER_HORIZONTAL);
            editText.setTypeface(null, Typeface.BOLD);
        });
        List<String > listFonts = getFontFromAssets();
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new ListTextStyleAdapter(listFonts,this);
        recyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);


    }
    public List<String> getFontFromAssets(){
        List<String > list = new ArrayList<>();
        AssetManager assetManager;
        InputStream inputStream;
        try {
            assetManager = getAssets();
            list = Arrays.asList(assetManager.list("font"));

            Log.e("~~~", list.get(0));

        } catch (IOException e) {

        }
        return list;

    }

    @Override
    public void onClick(View view, int position, String type) {

    }
}