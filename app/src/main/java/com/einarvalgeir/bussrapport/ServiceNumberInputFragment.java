package com.einarvalgeir.bussrapport;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceNumberInputFragment extends BaseFragment {

    @BindView(R.id.write_service_number_edit_text)
    EditText text;


    public static ServiceNumberInputFragment newInstance() {
        return new ServiceNumberInputFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_select_service_number, container, false);
        ButterKnife.bind(this, rootView);

        RxTextView.textChanges(text)
                .subscribe(aVoid -> getMainActivity().changeNextButtonStatus(true));

        return rootView;
    }

    @Override
    public void nextButtonClicked() {
        getMainActivity().getPresenter().addServiceNumber((text.getText().toString()));

        InputMethodManager imm = (InputMethodManager) getMainActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }


}
