package com.example.camera;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
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

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BottomSheetAddText extends BottomSheetDialogFragment implements ListTextStyleAdapter.ItemClickListener {

    private Callback callback;
    EditText editText;
    private RelativeLayout btnDone, btnCancel, alignLeft, alignCenter, alignRight, btnBold;
    RecyclerView recyclerViewTextStyle, recyclerViewTextColor;
    ListTextStyleAdapter adapter;
    ListColorAdapter adapterColor;
    Context context;
    private String currentStyle = "regular.ttf";
    private String currentColor = "#000000";
    private boolean isBold=false;
    private int align;



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
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        context = getContext();
        editText = v.findViewById(R.id.edtAddedText);
        btnDone = v.findViewById(R.id.btnDone);
        btnCancel = v.findViewById(R.id.btnCancel);
        alignLeft = v.findViewById(R.id.alignLeft);
        alignCenter = v.findViewById(R.id.alignCenter);
        alignRight = v.findViewById(R.id.alignRight);
        btnBold = v.findViewById(R.id.btnBold);


        btnDone.setOnClickListener(v1 -> {
            if (callback != null) {
                Bundle bundle = new Bundle();
                bundle.putString("text", editText.getText().toString());
                bundle.putString("textStyle",currentStyle);
                bundle.putString("textColor",currentColor);
                bundle.putInt("align", align);
                callback.onButtonClicked(v1, bundle);
            }
        });
        btnCancel.setOnClickListener(v1 -> {
            if (callback != null) {
                Bundle bundle = new Bundle();
                callback.onButtonClicked(v1, bundle);
            }
        });
        List<String> listFonts = getFontFromAssets(context);
        recyclerViewTextStyle = v.findViewById(R.id.recyclerView);
        adapter = new ListTextStyleAdapter(listFonts, (view, position, type) -> {
            Typeface font = Typeface.createFromAsset(context.getAssets(), "font/" + getFontFromAssets(context).get(position));
            editText.setTypeface(font);
            currentStyle = getFontFromAssets(context).get(position);
        });
        recyclerViewTextStyle.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 4);
        recyclerViewTextStyle.setLayoutManager(layoutManager);

        String[]  listColor = getResources().getStringArray(R.array.listColor);
        recyclerViewTextColor = v.findViewById(R.id.recyclerViewTextColor);
        adapterColor = new ListColorAdapter(listColor, (view, position, type) -> {
            currentColor = listColor[position];
            editText.setTextColor(Color.parseColor(currentColor));
        });
        recyclerViewTextColor.setAdapter(adapterColor);
        recyclerViewTextColor.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL,false));

        alignRight.setOnClickListener(v1 -> {
            editText.setGravity(Gravity.RIGHT);
            align=2;
        });
        alignLeft.setOnClickListener(v1 -> {
            editText.setGravity(Gravity.LEFT);
            align=0;
        });
        alignCenter.setOnClickListener(v1 -> {
            editText.setGravity(Gravity.CENTER_HORIZONTAL);
            align=1;
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

    @Override
    public void onClick(View view, int position, String type) {
        if (type.equals("textStyle")){
            Typeface font = Typeface.createFromAsset(context.getAssets(), "font/" + getFontFromAssets(context).get(position));
            editText.setTypeface(font);
            currentStyle = getFontFromAssets(context).get(position);
        }else if (type.equals("textColor")){

        }

    }


    public interface Callback {
        public void onButtonClicked(View view, Bundle bundle);
    }

    @Override
    public void onAttach(Activity ac) {
        super.onAttach(ac);
        callback = (Callback) ac;
    }

    @Override
    public void onDetach() {
        callback = null;
        super.onDetach();
    }
}
