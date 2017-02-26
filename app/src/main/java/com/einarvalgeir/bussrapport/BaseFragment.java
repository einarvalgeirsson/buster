package com.einarvalgeir.bussrapport;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment implements INextButton, IImageCallback {

    protected IMainCallback callback;

    protected IMainCallback getCallback() {
        return callback;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof Activity)) {
            return;
        }
        if (context instanceof IMainCallback) {
            callback = (IMainCallback) context;
        }
    }

    protected MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    @Override
    public void nextButtonClicked() {}

    @Override
    public void setNextButtonVisible(int visibility) {
//        getMainActivity().setNextButtonVisible(visibility);
    }

    @Override
    public void setImage(String path) {}
}
