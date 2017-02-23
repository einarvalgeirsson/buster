package com.einarvalgeir.bussrapport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.einarvalgeir.bussrapport.model.Report;
import com.einarvalgeir.bussrapport.util.SharedPrefsUtil;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.functions.Func2;


public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.name_edit_text)
    EditText reporterName;

    @BindView(R.id.email_edit_text)
    EditText assigneeEmail;

    @BindView(R.id.save_settings_button)
    Button saveButton;

    SharedPrefsUtil prefsUtil;

    Subscription buttonClickSub;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity_layout);
        ButterKnife.bind(this);


        prefsUtil = new SharedPrefsUtil(getApplicationContext());

        reporterName.setText(prefsUtil.getReporterName());
        assigneeEmail.setText(prefsUtil.getAssigneeEmail());

        RxView.clicks(saveButton)
                .subscribe(aVoid -> storeSettings());

        rx.Observable<Boolean> reporterTextObservable =
                RxTextView.textChangeEvents(reporterName)
                .map(aVoid -> isUserNameValid());

        rx.Observable<Boolean> emailTextObservable =
                RxTextView.textChangeEvents(assigneeEmail)
                        .map(aVoid -> isEmailValid());

        Observable.combineLatest(reporterTextObservable, emailTextObservable, new Func2<Boolean, Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean reporterNameIsValid, Boolean emailIsValid) {
                return reporterNameIsValid && emailIsValid;
            }
        }).subscribe(enableSaveButton -> setSaveButtonState(enableSaveButton));
    }

    private void setSaveButtonState(boolean isEnabled) {
        int backgroundColor = isEnabled ? getResources().getColor(R.color.colorPrimary) :
                getResources().getColor(R.color.disabledButtonGrey);
        saveButton.setBackgroundColor(backgroundColor);
        saveButton.setEnabled(isEnabled);
    }


    private boolean isUserNameValid() {
        String s = reporterName.getText().toString();
        return (!s.isEmpty() && s.contains(" "));
    }

    private boolean isEmailValid() {
        String s = assigneeEmail.getText().toString();
        return (!s.isEmpty() && s.contains("@"));
    }

    private void storeSettings() {
        prefsUtil
                .getPrefs()
                .edit()
                .putString(Report.USER_NAME, reporterName.getText().toString())
                .putString(Report.ASSIGNEE_EMAIL, assigneeEmail.getText().toString())
                .apply();

        Snackbar.make(findViewById(R.id.settings_root), "Ändringar sparade!", Snackbar.LENGTH_SHORT);
        finish();
    }


}
