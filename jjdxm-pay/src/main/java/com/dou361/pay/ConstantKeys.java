package com.dou361.pay;

/**
 * Security information for Pay
 * <p/>
 * alipay , wxpay
 *
 * @author BaoHong.Li
 * @version V1.0
 * @date 2015-7-16 上午11:35:03
 * @update (date)
 */
public class ConstantKeys {

    private static boolean isInitAliPayKeys;
    private static boolean isInitWxPayKeys;

    private static final String TAG = ConstantKeys.class.getName();

    private ConstantKeys() {
    }

    public static boolean initKeys(String appId, String partnerId, String privateKey, String appwxId, String mchId,
                                   String appKey) {
        initAliPayKeys(appId, partnerId, privateKey);
        initWxPayKeys(appwxId, mchId, appKey);
        return true;
    }

    /**
     * 初始化 支付宝 配置参数
     */
    public static boolean initAliPayKeys(String appId, String partnerId, String privateKey) {
        AliPay.APP_ID = appId;
        AliPay.PARTNER_ID = partnerId;
        AliPay.PRIVATE_KEY = privateKey;
        isInitAliPayKeys = true;
        return true;

    }

    /**
     * 初始化 微信支付 配置参数
     */
    public static boolean initWxPayKeys(String appId, String mchId,
                                        String appKey) {
        WxPay.APP_ID = appId;
        WxPay.MCH_ID = mchId;
        WxPay.API_KEY = appKey;
        isInitWxPayKeys = true;
        return true;

    }

    /**
     * alipay keys
     */
    public static class AliPay {

        /**
         * 合作者身份创建的开发者APP ID parameter "partner" in pay interface
         */
        public static String APP_ID = "";
        /**
         * 合作者身份 ID parameter "partner" in pay interface
         */
        public static String PARTNER_ID = "";

        /**
         * 参数编码字符集，目前只支持 utf-8 parameter "_input_charset" in pay interface
         */
        public static String INPUT_CHARSET = "utf-8";

        /**
         * 签名方式，目前只支持 RSA parameter "sign_type" in pay interface
         */
        public static String SIGN_TYPE = "RSA";

        /**
         * 商户私钥 parameter "sign" in pay interface
         */
        public static String PRIVATE_KEY = "";

    }

    /**
     * wx pay keys
     */
    public static class WxPay {
        /**
         * appid 请同时修改 androidmanifest.xml里面，.PayActivityd里的属性
         * <p/>
         * <b><i> data android:scheme="wxb4ba3c02aa476ea1"<i><b> 为新设置的appid
         */
        public static String APP_ID = "";

        /***
         * 商户号
         */
        public static String MCH_ID = "";

        /***
         * API密钥，在商户平台设置
         */
        public static String API_KEY = "";

    }

}
