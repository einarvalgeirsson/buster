package com.einarvalgeir.bussrapport;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.einarvalgeir.bussrapport.events.ViewPdfEvent;
import com.einarvalgeir.bussrapport.model.Report;
import com.einarvalgeir.bussrapport.presenter.MainPresenter;
import com.jakewharton.rxbinding.view.RxView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IMainCallback {

    @BindView(R.id.fab)
    protected FloatingActionButton nextButton;

    INextButton nextButtonCallback;
    ViewPager viewPager;
    SectionsPagerAdapter adapter;
    Toolbar toolbar;
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        turnOffToolbarScrolling();
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        adapter =
                new SectionsPagerAdapter(getSupportFragmentManager());

        presenter = new MainPresenter(new Report());

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        // Initially the next button is greyed out and disabled
        // Fragment notifies when 4 characters are inputted in edit text
        changeNextButtonStatus(false);

        RxView.clicks(nextButton)
                .subscribe(aVoid -> onNextButtonClicked());
    }

    public void onNextButtonClicked() {
        nextButtonCallback = (BaseFragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
        nextButtonCallback.nextButtonClicked();

        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
    }

    private void modifyNextButton(Boolean isEnabled) {
        nextButton.setEnabled(isEnabled);
        nextButton.setBackgroundTintList(
                ColorStateList.valueOf(
                        getResources().getColor(
                                isEnabled ? R.color.colorAccent : R.color.disabledNextButtonGrey)));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void turnOffToolbarScrolling() {
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);

        //turn off scrolling
        AppBarLayout.LayoutParams toolbarLayoutParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        toolbarLayoutParams.setScrollFlags(0);
        toolbar.setLayoutParams(toolbarLayoutParams);

        CoordinatorLayout.LayoutParams appBarLayoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        appBarLayoutParams.setBehavior(null);
        appBarLayout.setLayoutParams(appBarLayoutParams);
    }

    @Override
    public void changeNextButtonStatus(boolean isEnabled) {
        modifyNextButton(isEnabled);
    }

    public MainPresenter getPresenter() {
        return presenter;
    }


    private class SectionsPagerAdapter extends FragmentPagerAdapter {


        private final String TAG = SectionsPagerAdapter.class.getSimpleName();

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = null;

            switch(position) {
                case 0:
                    fragment = NumPadInputFragment.newInstance(R.string.write_bus_number_hint_text);
                    break;
                case 1:
                    fragment = NumPadInputFragment.newInstance(R.string.write_service_number_hint_text);
                    break;
                case 2:
                    fragment = SelectProblemAreaFragment.newInstance();
                    break;
                case 3:
                    fragment = SetDateFragment.newInstance();
                    break;
                case 4:
                    fragment = SaveReportFragment.newInstance();
                    break;
                default:
                    Log.e(TAG, "Non-supported Fragment at position: " + position);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
            }
            return null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ViewPdfEvent e) {
        viewPdf(e.getFile(), e.getDirectory());
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void viewPdf(String file, String directory) {

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
        Uri path = FileProvider.getUriForFile(getApplicationContext(), "com.einarvalgeir.bussrapport.provider" ,pdfFile);

        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        List<ResolveInfo> resInfoList = getApplicationContext().getPackageManager().queryIntentActivities(pdfIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            getApplicationContext().grantUriPermission(packageName, path, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainActivity.this, "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }
}
