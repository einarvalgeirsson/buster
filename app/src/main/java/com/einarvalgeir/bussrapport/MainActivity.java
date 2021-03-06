package com.einarvalgeir.bussrapport;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.einarvalgeir.bussrapport.model.Report;
import com.einarvalgeir.bussrapport.presenter.MainPresenter;
import com.einarvalgeir.bussrapport.util.SharedPrefsUtil;
import com.jakewharton.rxbinding.support.v4.view.RxViewPager;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements IMainCallback {

    private static final int PICK_IMAGE = 0;

    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 2;


    @BindView(R.id.fab)
    protected FloatingActionButton nextButton;

    INextButton nextButtonCallback;
    IImageCallback imageCallback;
    ViewPager viewPager;
    SectionsPagerAdapter adapter;
    Toolbar toolbar;
    MainPresenter presenter;
    SharedPrefsUtil prefsUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        turnOffToolbarScrolling();
        setSupportActionBar(toolbar);
        prefsUtil = new SharedPrefsUtil(getApplicationContext());
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        adapter =
                new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        RxViewPager.pageSelections(viewPager).subscribe(page -> {
            if(page == adapter.getCount()-1) {
                hideNextButton();
            }
        });

        // Initially the next button is greyed out and disabled
        // Fragment notifies when 4 characters are inputted in edit text
        changeNextButtonStatus(false);

        RxView.clicks(nextButton)
                .subscribe(aVoid -> onNextButtonClicked());

        if (!isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        }

        presenter = new MainPresenter(new Report(), prefsUtil);
    }


    public void onNextButtonClicked() {
        nextButtonCallback = (BaseFragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
        nextButtonCallback.nextButtonClicked();

        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
    }

    @Override
    public void onBackPressed() {
        int currentItem = viewPager.getCurrentItem();

        if (currentItem > 0) {
            viewPager.setCurrentItem(currentItem -1);
        } else {
            super.onBackPressed();
        }
    }

    private void modifyNextButton(Boolean isEnabled) {
        nextButton.setEnabled(isEnabled);
        nextButton.setBackgroundTintList(
                ColorStateList.valueOf(
                        getResources().getColor(
                                isEnabled ? R.color.colorAccent : R.color.disabledButtonGrey)));
    }

    private void hideNextButton() {
        nextButton.setVisibility(GONE);
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_new_report) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
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

    public void openImagePicker() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            String s = getRealPathFromURI(selectedImageUri);
            imageCallback = (BaseFragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
            imageCallback.setImage(s);
        }
    }


    public String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
                    fragment = ServiceNumberInputFragment.newInstance();
                    break;
                case 2:
                    fragment = SelectProblemAreaFragment.newInstance();
                    break;
                case 3:
                    fragment = SetDateFragment.newInstance();
                    break;
                case 4:
                    fragment = SelectImageFragment.newInstance();
                    break;
                case 5:
                    fragment = SaveReportFragment.newInstance();
                    break;
                default:
                    Log.e(TAG, "Non-supported Fragment at position: " + position);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 6;
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

    public boolean isPermissionGranted(String permission) {
        int isGranted = ContextCompat.checkSelfPermission(this, permission);

        return isGranted == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(String permission, int requestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permission},
                requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImagePicker();
                }
                break;
            }

            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // continue
                } else {
                    finish();
                }
                break;
            }
            default:
                // Do nothing
                break;
        }
    }

}
