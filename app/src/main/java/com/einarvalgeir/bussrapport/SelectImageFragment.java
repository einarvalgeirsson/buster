package com.einarvalgeir.bussrapport;


import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectImageFragment extends BaseFragment {

    @BindView(R.id.select_image_button)
    Button selectImageButton;

    @BindView(R.id.imageView)
    ImageView imageView;

    public static SelectImageFragment newInstance() {
        return new SelectImageFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_select_image, container, false);
        ButterKnife.bind(this, rootView);

        selectImageButton.setOnClickListener(view -> {
            if (!getMainActivity().isPermissionGranted(Manifest.permission.CAMERA)) {
                getMainActivity().requestPermission(Manifest.permission.CAMERA, getMainActivity().MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
                getMainActivity().openImagePicker();
            }
        });


        return rootView;
    }

    @Override
    public void setImage(String path) {
        imageView.setImageURI(Uri.parse(path));
        getMainActivity().getPresenter().setImage(path);
    }
}
