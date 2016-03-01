package net.lbh.pay.uppay;

import net.lbh.pay.OnPayListener;
import net.lbh.pay.PayInfo;
import net.lbh.pay.PaymentActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;


/**
 * 
 * 银联支付 helper
 * 
 * @author BaoHong.Li
 * @date 2015-7-21 上午11:38:59
 * @update (date)
 * @version V1.0
 */
public class UpPayHelper {

	private static final String TAG = UpPayHelper.class.getName();

	private static OnPayListener mListener;
	
	// 00 - 启动银联正式环境
	// 01 - 连接银联测试环境
	public static String payMode ="01";

	
	public void pay(final Activity activity, PayInfo info,
			OnPayListener listener) {
		pay(activity, info, listener, payMode);
	}
	
	
	public void pay(final Activity activity, PayInfo info,
			OnPayListener listener , String mode) {

		
		mListener = listener;
		 
		PayUrlGenerator payUrlGenerator = new PayUrlGenerator(info);
		String orderInfo = payUrlGenerator.genPayOrder();
		
		if (null != mListener) {
			mListener.onStartPay();
		}
		
		//pay
		Intent intent = new Intent(activity,PaymentActivity.class);
		intent.putExtra("orderInfo", orderInfo);
		activity.startActivity(intent);
	}


	/**
	 * 接收 银联支付回调结果 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 * @return void
	 * @autour BaoHong.Li
	 * @date 2015-7-21 上午11:41:19
	 * @update (date)
	 */
	public static void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		
        /*************************************************
         * 步骤3：处理银联手机支付控件返回的支付结果
         ************************************************/
        if (data == null) {
            return;
        }

        String msg = "";
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
        	msg = "支付成功！";
        	if (null != mListener) {
//    			mListener.onPaySuccess(String.valueOf(0),msg);
    			mListener.onPaySuccess();
    		}
        
        } else if (str.equalsIgnoreCase("fail")) {
        	  msg = "支付失败！";
        	if (null != mListener) {
    			mListener.onPayFail(String.valueOf(0),msg);
    		}
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
            if (null != mListener) {
            	mListener.onPayFail(String.valueOf(0),msg);
            }
        }
	

	}

	public static void handleIntent(Intent intent, Context context) {

	}

}
