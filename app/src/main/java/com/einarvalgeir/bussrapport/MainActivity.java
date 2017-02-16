package com.einarvalgeir.bussrapport;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IMainCallback {

    @BindView(R.id.fab)
    protected FloatingActionButton nextButton;

    INextButton nextButtonCallback;
    ViewPager viewPager;
    SectionsPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        adapter =
                new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        // Initially the next button is greyed out and disabled
        // Fragment notifies when 4 characters are inputted in edit text
        changeNextButtonStatus(false);

        RxView.clicks(nextButton)
                .subscribe(aVoid -> onNextButtonClicked());
    }

    private void onNextButtonClicked() {
        nextButtonCallback = (INextButton) adapter.getItem(viewPager.getCurrentItem());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void changeNextButtonStatus(boolean isEnabled) {
        modifyNextButton(isEnabled);
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
                default:
                    Log.e(TAG, "Non-supported Fragment at position: " + position);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
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
}
