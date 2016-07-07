# jjdxmthirdpay
自定义jjdxmthirdpay第三方支付

## 支付组件 简要说明
该组件为封装了 微信，支付宝，银联支付， 一键快速集成。

# //配置 AndroidManifest

 <!-- 微信支付 begin -->
        <activity
            android:name="com.dou361.pay.PaymentActivity"
            android:launchMode="singleTop"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" />

        <activity-alias
            android:name=".wxapi.WXPayEntryActivity"
            android:exported="true"
            android:targetActivity="com.dou361.pay.PaymentActivity" />
        <!-- 微信支付 end -->


        <!-- 支付宝 begin -->
        <activity
            android:name="com.alipay.sdk.app.H5PayActivity"
            android:configChanges="orientation|keyboardHidden|navigation"
            android:exported="false"
            android:screenOrientation="behind"
            android:windowSoftInputMode="adjustResize|stateHidden" />
        <!-- 支付宝 end -->


        <!-- 银联支付 begin -->
        <activity
            android:name="com.unionpay.uppay.PayActivity"
            android:configChanges="orientation|keyboardHidden"
            android:excludeFromRecents="true"
            android:screenOrientation="portrait"
            android:windowSoftInputMode="adjustResize" />

        <!-- 银联支付 end -->


        <!-- 微信 广播 start -->
        <receiver android:name="com.dou361.pay.wxpay.AppRegister" >
            <intent-filter>
                <action android:name="com.tencent.mm.plugin.openapi.Intent.ACTION_REFRESH_WXAPP" />
            </intent-filter>
        </receiver>
        <!-- 微信 广播 end -->


        <!-- 微信支付 参数 appid， 需要替换成你自己的 -->
        <meta-data
            android:name="WXPAY_APP_ID"
            android:value="替换成自己的 app id" >
        </meta-data>
        <meta-data
            android:name="WXPAY_MCH_ID"
            android:value="替换成自己的   MCH_ID" >
        </meta-data>
        <meta-data
            android:name="WXPAY_API_KEY"
            android:value="替换成自己的 api key" >
        </meta-data>
        <!-- 微信支付 参数 end  需要替换成你自己的 -->


        <!-- 支付宝 参数 appid， 需要替换成你自己的 -->  //如果是 超过10位数字，要在前边加 ,Eg: \0223987667567887653
        <meta-data
            android:name="ALIPAY_PARTNER_ID"
            android:value="替换成自己的 partenr id" >
        </meta-data>
        <meta-data
            android:name="ALIPAY_SELLER_ID"
            android:value="替换成自己的 seller id" >
        </meta-data>

        <meta-data
            android:name="ALIPAY_PRIVATE_KEY"
            android:value="替换成自己的 private key" >
        </meta-data>

        <meta-data
            android:name="ALIPAY_PUBLIC_KEY"
            android:value="替换成自己的 public key" >
        </meta-data>
        <!-- 支付宝 参数 end  需要替换成你自己的 -->

# // 初始化支付组件
		PayAgent payAgent = PayAgent.getInstance();
		payAgent.setDebug(true);
		payAgent.initPay(this);


# // 调起支付 
        PayAgent.getInstance().onPay(payType, this, payInfo,
				new OnPayListener() {

					@Override
					public void onStartPay() {
						
						progressDialog.setTitle("加载中。。。");
						progressDialog.show();
					}

					@Override
					public void onPaySuccess() {
						
						Toast.makeText(MainActivity.this,"支付成功！", 1).show();
						
						if (null != progressDialog) {
							progressDialog.dismiss();
						}

					}

					@Override
					public void onPayFail(String code, String msg) {
						Toast.makeText(MainActivity.this,
								"code:" + code + "msg:" + msg, 1).show();
						Log.e(getClass().getName(), "code:" + code + "msg:" + msg);
						
						if (null != progressDialog) {
							progressDialog.dismiss();
						}
					}
				});