package com.dou361.pay.alipay;

import android.text.TextUtils;

import com.dou361.pay.ConstantKeys;
import com.dou361.pay.L;
import com.dou361.pay.PayInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderInfoUtil2_0 {

    private static final String TAG = OrderInfoUtil2_0.class.getName();

    private PayInfo payInfo;

    public OrderInfoUtil2_0(PayInfo payInfo) {
        this.payInfo = payInfo;
    }

    /**
     * 构造授权参数列表
     *
     * @param target_id
     * @return
     */
    public Map<String, String> buildAuthInfoMap(String target_id) {
        Map<String, String> keyValues = new HashMap<String, String>();

        // 商户签约拿到的app_id，如：2013081700024223
        keyValues.put("app_id", ConstantKeys.AliPay.APP_ID);

        // 商户签约拿到的pid，如：2088102123816631
        keyValues.put("pid", ConstantKeys.AliPay.PARTNER_ID);

        // 服务接口名称， 固定值
        keyValues.put("apiname", "com.alipay.account.auth");

        // 商户类型标识， 固定值
        keyValues.put("app_name", "mc");

        // 业务类型， 固定值
        keyValues.put("biz_type", "openservice");

        // 产品码， 固定值
        keyValues.put("product_id", "APP_FAST_LOGIN");

        // 授权范围， 固定值
        keyValues.put("scope", "kuaijie");

        // 商户唯一标识，如：kkkkk091125
        keyValues.put("target_id", target_id);

        // 授权类型， 固定值
        keyValues.put("auth_type", "AUTHACCOUNT");

        // 签名类型
        keyValues.put("sign_type", ConstantKeys.AliPay.SIGN_TYPE);

        return keyValues;
    }

    /**
     * 获取签名的字符串
     *
     * @return
     */
    public String generatePayInfo() {
        Map<String, String> params = buildOrderParamMap();
        String orderParam = buildOrderParam(params);
        String sign = getSign(params);
        return orderParam + "&" + sign;
    }

    /**
     * 获取签名的字符串
     *
     * @return
     */
    public String generateAuthInfo(String targetId) {
        Map<String, String> authInfoMap = buildAuthInfoMap(targetId);
        String info = buildOrderParam(authInfoMap);
        String sign = getSign(authInfoMap);
        return info + "&" + sign;
    }

    /**
     * 构造支付订单参数列表
     *
     * @return
     */
    private Map<String, String> buildOrderParamMap() {


        Map<String, String> keyValues = new HashMap<String, String>();
        if (this.payInfo == null) {
            L.e(TAG, " +++++ orderInfo is null");
            return null;
        }

        validatePayInfo(payInfo);

        keyValues.put("app_id", ConstantKeys.AliPay.APP_ID);

        keyValues.put("biz_content", "{\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"total_amount\":\"" + payInfo.getPrice() +
                "\",\"subject\":\"" + payInfo.getSubject() +
                "\",\"body\":\"" + payInfo.getBody() +
                "\",\"out_trade_no\":\"" + payInfo.getOrderNo() + "\"}");

        keyValues.put("charset", "utf-8");

        keyValues.put("method", "alipay.trade.app.pay");

        keyValues.put("sign_type", ConstantKeys.AliPay.SIGN_TYPE);

        keyValues.put("timestamp", "2016-07-29 16:55:53");

        keyValues.put("version", "1.0");

        return keyValues;
    }

    /**
     * 构造支付订单参数信息
     *
     * @param map 支付订单参数
     * @return
     */
    private String buildOrderParam(Map<String, String> map) {
        List<String> keys = new ArrayList<String>(map.keySet());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, true));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true));

        return sb.toString();
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    /**
     * 对支付参数信息进行签名
     *
     * @param map 待签名授权信息
     * @return
     */
    private String getSign(Map<String, String> map) {
        List<String> keys = new ArrayList<String>(map.keySet());
        // key排序
        Collections.sort(keys);

        StringBuilder authInfo = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            authInfo.append(buildKeyValue(key, value, false));
            authInfo.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        authInfo.append(buildKeyValue(tailKey, tailValue, false));

        String oriSign = SignUtils.sign(authInfo.toString());
        String encodedSign = "";

        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return "sign=" + encodedSign;
    }

    /**
     * 验证 支付信息的有效性
     *
     * @return void
     * @autour BaoHong.Li
     * @date 2015-7-16 下午5:56:21
     * @update (date)
     */
    private void validatePayInfo(PayInfo payInfo) {

        if (TextUtils.isEmpty(payInfo.getOrderNo())) {
            throw new IllegalArgumentException(" payInfo.orderNo is  null !");
        }

        if (TextUtils.isEmpty(payInfo.getBody())) {
            throw new IllegalArgumentException(" payInfo.body is  null !");
        }

        if (TextUtils.isEmpty(payInfo.getPrice())) {
            throw new IllegalArgumentException(" payInfo.price is  null !");
        }

        if (TextUtils.isEmpty(payInfo.getSubject())) {
            throw new IllegalArgumentException(" payInfo.subject is  null !");
        }

        if (TextUtils.isEmpty(payInfo.getNotifyUrl())) {
            throw new IllegalArgumentException(" payInfo.notifyUrl is  null !");
        }

    }

}
