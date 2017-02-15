package com.einarvalgeir.bussrapport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;

public class SelectBusNumberFragment extends BaseFragment {

    public static SelectBusNumberFragment newInstance() {
        return new SelectBusNumberFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_select_bus_number, container, false);

        EditText busNbrEditText = (EditText) rootView.findViewById(R.id.write_bus_number_edit_text);

        getCallback().changeNextButtonStatus(false);

        RxTextView.textChangeEvents(busNbrEditText)
                .map(e -> e.text().length() == 4)
                .subscribe(lengthIs4 -> getCallback().changeNextButtonStatus(lengthIs4));

        return rootView;
    }
}
