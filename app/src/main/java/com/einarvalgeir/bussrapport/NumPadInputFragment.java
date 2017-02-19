package com.einarvalgeir.bussrapport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NumPadInputFragment extends BaseFragment {

    private static final String KEY_TEXT_RES_ID = "textResId";

    private int textResId;

    @BindView(R.id.written_bus_number)
    TextView numpadResult;

    @BindView(R.id.numpad)
    GridLayout numpad;

    @BindView(R.id.bus_number_desc_text)
    TextView descriptionText;

    public static NumPadInputFragment newInstance(int textResId) {
        NumPadInputFragment fragment = new NumPadInputFragment();

        Bundle args = new Bundle();
        args.putInt(KEY_TEXT_RES_ID, textResId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if (args != null) {
            textResId = args.getInt(KEY_TEXT_RES_ID, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_select_bus_number, container, false);

        ButterKnife.bind(this, rootView);

        getCallback().changeNextButtonStatus(false);

        descriptionText.setText(getResources().getText(textResId));

        bindNumberButton(rootView, R.id.numpad_button_1);
        bindNumberButton(rootView, R.id.numpad_button_2);
        bindNumberButton(rootView, R.id.numpad_button_3);
        bindNumberButton(rootView, R.id.numpad_button_4);
        bindNumberButton(rootView, R.id.numpad_button_5);
        bindNumberButton(rootView, R.id.numpad_button_6);
        bindNumberButton(rootView, R.id.numpad_button_7);
        bindNumberButton(rootView, R.id.numpad_button_8);
        bindNumberButton(rootView, R.id.numpad_button_9);
        bindNumberButton(rootView, R.id.numpad_button_0);

        bindBackSpaceButton(rootView, R.id.numpad_button_backspace);


        // Allow only 4 digits to enable "next"-button
        RxTextView.textChangeEvents(numpadResult)
                .map(e -> e.text().length() == 4)
                .subscribe(lengthIs4 -> getCallback().changeNextButtonStatus(lengthIs4));

        return rootView;
    }

    private void bindBackSpaceButton(View v, int id) {
        ImageButton button = (ImageButton) v.findViewById(id);
        RxView.clicks(button).subscribe(aVoid -> deleteLastChar());
    }

    private void deleteLastChar() {
        CharSequence currentText = numpadResult.getText();
        int lengthOfText = currentText.length();
        numpadResult.setText(lengthOfText > 0 ? currentText.subSequence(0, lengthOfText - 1) : "");
    }


    private void bindNumberButton(View v, int id) {
        Button button = (Button) v.findViewById(id);
        RxView.clicks(button).subscribe(aVoid -> numpadResult.append(button.getText()));
    }

    @Override
    public void nextButtonClicked() {
        // Store bus number in model via presenter
        if (textResId == R.string.write_bus_number_hint_text) {
            getMainActivity()
                    .getPresenter()
                    .addBusNumber(Integer.valueOf(numpadResult.getText().toString()));
        } else if (textResId == R.string.write_service_number_hint_text) {
            getMainActivity()
                    .getPresenter()
                    .addServiceNumber(Integer.valueOf(numpadResult.getText().toString()));
        }
    }
}
