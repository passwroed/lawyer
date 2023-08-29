package com.ruoyi.common.wxPat;

/**
 * @ClassName : WXPayConstants
 * @Description :
 * @Author : WANGKE
 * @Date: 2023-08-25 22:12
 */
public class WXPayConstants {
    public static final String DOMAIN_API = "https://api.mch.weixin.qq.com/v3";

    //app下单
    public static final String PAY_TRANSACTIONS_APP = "/pay/transactions/app";


    //微信支付回调
    public static final String WECHAT_PAY_NOTIFY_URL =
            "https://xxx.xxxx.com/deal/api/appPayment/weChatPayNotify";


    //申请退款
    public static final String REFUND_DOMESTIC_REFUNDS      = "/refund/domestic/refunds";

    //微信退款回调
    public static final String WECHAT_REFUNDS_NOTIFY_URL = "https://xxx.xxxx.com/api/appPayment/weChatPayRefundsNotify";

    //关闭订单
    public static final String PAY_TRANSACTIONS_OUT_TRADE_NO   = "/pay/transactions/out-trade-no/{}/close";

}
