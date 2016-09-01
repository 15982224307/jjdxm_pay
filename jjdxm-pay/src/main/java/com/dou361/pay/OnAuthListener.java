package com.dou361.pay;


public abstract class OnAuthListener {

    private static final String TAG = OnAuthListener.class.getName();

    public void onStartAuth() {
    }

    public abstract void onAuthSuccess();

    public abstract void onAuthFail(String code, String msg);

}
