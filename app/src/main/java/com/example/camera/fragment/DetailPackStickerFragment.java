package com.example.camera.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.camera.R;
import com.example.camera.adapter.StickerAdapter;
import com.example.camera.ultis.Common;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DetailPackStickerFragment extends Fragment  {
    RecyclerView recyclerView;
    private StickerAdapter adapter;
    private String namePack = "1";
    private Drawable[] listSticker;
    private List<String> listStringSticker;
    String currentSticker ;
    private CallbackSticker callback;


    public DetailPackStickerFragment(String namePack){
        this.namePack =namePack;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_detail_pack, container, false);
        listStringSticker = Common.getNameStickerFromPack(getContext(),namePack);
    recyclerView = viewGroup.findViewById(R.id.recyclerView);
    listSticker=getDrawableFromAssets(getContext(), namePack);
    adapter =new StickerAdapter(listSticker,3, (view, position) ->
    {
        if (callback != null) {
            Bundle bundle = new Bundle();
            currentSticker = listStringSticker.get(position);
            bundle.putString("nameSticker",currentSticker);
            bundle.putString("namePack",namePack);
            callback.onStickerClicked(bundle);
        }
    }
    );
    recyclerView.setAdapter(adapter);

    recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
    recyclerView.requestFocus();
        return viewGroup;
    }
    public Drawable[] getDrawableFromAssets(Context context, String namePack){
        AssetManager assetManager;
        try {

            // to reach asset
            assetManager = context.getAssets();
            // to get all item in dogs folder.
            String[] images = assetManager.list("sticker/"+namePack);
            // to keep all image
            Drawable[] drawables = new Drawable[images.length];
            // the loop read all image in dogs folder and  aa
            for (int i = 0; i < images.length; i++) {
                InputStream inputStream = context.getAssets().open("sticker/"+namePack+"/" + images[i]);
                Drawable drawable = Drawable.createFromStream(inputStream, null);
                drawables[i] = drawable;
            }
            return drawables;
        } catch (IOException e) {
            // you can print error or log.
        }
        return null;
    }
    public interface CallbackSticker {
        public void onStickerClicked(Bundle bundle);
    }
    @Override
    public void onAttach(Activity ac) {
        super.onAttach(ac);
        callback = (CallbackSticker) ac;
    }

    @Override
    public void onDetach() {
        callback = null;
        super.onDetach();
    }
}
