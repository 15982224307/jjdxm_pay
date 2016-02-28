package net.lbh.pay;


public abstract class OnPayListener {
	
	private static final String TAG = OnPayListener.class.getName();
	
	public void onStartPay(){}

	public abstract void onPaySuccess();
//	public abstract void onPaySuccess(String code, String msg);

	public abstract void onPayFail(String code, String msg);

}
