package com.example.camera.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.camera.R;
import com.example.camera.adapter.StickerAdapter;
import com.example.camera.adapter.ViewPagerPackStickerAdapter;
import com.example.camera.ultis.Common;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BottomSheetAddSticker extends BottomSheetDialogFragment {

    private Callback callback;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RecyclerView recyclerView;
    private StickerAdapter adapter;
    private Drawable[] listFirstSticker;
    private int percentOfWidth = 8; // width screen/8


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
            bottomSheet.getLayoutParams().height = Common.getScreenHeight();


        }
        View view = getView();
        view.post(() -> {
            View parent = (View) view.getParent();
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) (parent).getLayoutParams();
            CoordinatorLayout.Behavior behavior = params.getBehavior();
            BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) behavior;
            bottomSheetBehavior.setPeekHeight(Common.getScreenWidth());


        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout_add_sticker, container, false);


        viewPager = v.findViewById(R.id.viewPager);
        tabLayout = v.findViewById(R.id.tabLayout);
        addTabs(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        List<String> list = getStickerFromAssets(getContext());
        listFirstSticker = new Drawable[list.size()];
        for (int i = 0; i < list.size(); i++) {
//            tabLayout.getTabAt(i).setText(list.get(i));
            try {
                tabLayout.getTabAt(i).setIcon(getFirstInPack(getContext(), list.get(i)));
                listFirstSticker[i] = getFirstInPack(getContext(), list.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.getLayoutParams().height = Common.getScreenWidth() / percentOfWidth + 10;
        adapter = new StickerAdapter(listFirstSticker, percentOfWidth, (view, position) ->
        {
            viewPager.setCurrentItem(position);
        });
        adapter.setItemSelected(0);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                adapter.setItemSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        return v;
    }

    public Drawable getFirstInPack(Context context, String namePack) throws IOException {
        InputStream inputStream = context.getAssets().open("sticker/" + namePack + "/0.png");
        return Drawable.createFromStream(inputStream, null);
    }

    public List<String> getStickerFromAssets(Context context) {
        List<String> list = new ArrayList<>();
        AssetManager assetManager;
        try {
            assetManager = context.getAssets();
            list = Arrays.asList(assetManager.list("sticker"));
        } catch (IOException e) {

        }


        Log.e("~~~", new Gson().toJson(list));
        return list;

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

    private void addTabs(ViewPager viewPager) {
        ViewPagerPackStickerAdapter adapter = new ViewPagerPackStickerAdapter(getChildFragmentManager());
        List<String> list = new ArrayList<>();
        list = getStickerFromAssets(getContext());
        for (int i = 0; i < list.size(); i++) {
            adapter.addFrag(new DetailPackStickerFragment(list.get(i)), list.get(i));
        }
        viewPager.setAdapter(adapter);
    }

}
