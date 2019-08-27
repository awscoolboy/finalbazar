package ir.cafebazar.et.ui.fragments;

import androidx.fragment.app.Fragment;

import ir.cafebazar.et.network.BaseApiController;

public class Proxy extends Fragment implements BaseApiController.ApiCallBack{


    @Override
    public void didReceiveData(int type, Object... object) {

    }

    @Override
    public void onError(String error_message) {

    }
}
