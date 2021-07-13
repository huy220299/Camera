package com.example.camera.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.camera.R;
import com.example.camera.adapter.ListColorAdapter;
import com.example.camera.adapter.ListTextStyleAdapter;
import com.example.camera.callback.EditTextCallback;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BottomSheetAddText extends BottomSheetDialogFragment  {


    EditText editText;
    private RelativeLayout btnDone, btnCancel, alignLeft, alignCenter, alignRight, btnBold;
    RecyclerView recyclerViewTextStyle, recyclerViewTextColor;
    ListTextStyleAdapter adapter;
    ListColorAdapter adapterColor;
    Context context;
    private Typeface currentStyle  ;
    private int currentColor = Color.BLACK;
    private Paint.Align Align = Paint.Align.LEFT;

    private EditTextCallback editTextCallback;
    String textEdit="";
    TextPaint textPaint;
    int[] listColor = {
            Color.BLACK,
            Color.RED,
            Color.YELLOW,
            Color.GREEN,
            Color.BLUE,
            Color.CYAN,
            Color.MAGENTA

    };

    public BottomSheetAddText(String textEdit, TextPaint textPaint, EditTextCallback editTextCallback) {

        this.editTextCallback = editTextCallback;
        this.textEdit = textEdit;
        this.textPaint = textPaint;
    }
    public BottomSheetAddText( EditTextCallback editTextCallback) {
        this.editTextCallback = editTextCallback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);


    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();

        if (dialog != null) {
            View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

        }
        View view = getView();
        view.post(() -> {
            View parent = (View) view.getParent();
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) (parent).getLayoutParams();
            CoordinatorLayout.Behavior behavior = params.getBehavior();
            BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) behavior;
            bottomSheetBehavior.setPeekHeight((int) (view.getMeasuredHeight() * 0.7));

        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout_add_text, container, false);


        context = getContext();
        editText = v.findViewById(R.id.edtAddedText);
        btnDone = v.findViewById(R.id.btnDone);
        btnCancel = v.findViewById(R.id.btnCancel);
        alignLeft = v.findViewById(R.id.alignLeft);
        alignCenter = v.findViewById(R.id.alignCenter);
        alignRight = v.findViewById(R.id.alignRight);
        btnBold = v.findViewById(R.id.btnBold);

        currentStyle = Typeface.createFromAsset(context.getAssets(), "font/" + "regular.ttf");

        if (textPaint!=null){
            currentStyle = textPaint.getTypeface();
            currentColor =  textPaint.getColor();
            editText.setText(textEdit);
            editText.setTextColor(currentColor);
            editText.setTypeface(currentStyle);
            switch (textPaint.getTextAlign()){
                case CENTER:
                    editText.setGravity(Gravity.CENTER);
                    break;
                case RIGHT:
                    editText.setGravity(Gravity.RIGHT);
                    break;
                case LEFT:
                    editText.setGravity(Gravity.LEFT);
                    break;
            }

        }else {
            editText.setTextColor(currentColor);
        }

        btnDone.setOnClickListener(v1 -> {

            if (editTextCallback!=null){
                    TextPaint textPaint = new TextPaint();
                    textPaint.setTextAlign(Align);
                    textPaint.setColor(currentColor);
                    textPaint.setTypeface(currentStyle);
                    editTextCallback.edit_callback(editText.getText().toString(),textPaint);
            }

        });
        btnCancel.setOnClickListener(v1 -> {
            if (editTextCallback!=null){
                editTextCallback.edit_callback(textEdit,textPaint);
            }
        });
        List<String> listFonts = getFontFromAssets(context);
        List<Typeface> typefaceList = new ArrayList<>();
        for (int i = 0; i < listFonts.size(); i++) {
            typefaceList.add(Typeface.createFromAsset(context.getAssets(), "font/" + listFonts.get(i)) );
        }
        recyclerViewTextStyle = v.findViewById(R.id.recyclerView);
        adapter = new ListTextStyleAdapter(typefaceList, (view, position, type) -> {
            currentStyle = typefaceList.get(position);
            editText.setTypeface(currentStyle);
        });
        recyclerViewTextStyle.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 4);
        recyclerViewTextStyle.setLayoutManager(layoutManager);

        recyclerViewTextColor = v.findViewById(R.id.recyclerViewTextColor);
        adapterColor = new ListColorAdapter(listColor, (view, position, type) -> {
            currentColor = listColor[position];
            editText.setTextColor(currentColor);
        });
        recyclerViewTextColor.setAdapter(adapterColor);
        recyclerViewTextColor.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL,false));

        alignRight.setOnClickListener(v1 -> {
            editText.setGravity(Gravity.RIGHT);
            Align =Paint.Align.RIGHT;
        });
        alignLeft.setOnClickListener(v1 -> {
            editText.setGravity(Gravity.LEFT);
            Align =Paint.Align.LEFT;
        });
        alignCenter.setOnClickListener(v1 -> {
            editText.setGravity(Gravity.CENTER_HORIZONTAL);
            Align =Paint.Align.CENTER;
        });
        btnBold.setOnClickListener(v1 -> {
            editText.setTypeface(null, Typeface.BOLD);
        });
        return v;
    }

    public List<String> getFontFromAssets(Context context) {
        List<String> list = new ArrayList<>();
        AssetManager assetManager;
                try {
            assetManager = context.getAssets();
            list = Arrays.asList(assetManager.list("font"));
        } catch (IOException e) {
        }
        return list;

    }

}
