package cn.mixpay.sdksample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import cn.mixpay.android.core.OrderStatus;
import cn.mixpay.sdk.MixpayAPI;
import cn.mixpay.sdk.Order;
import cn.mixpay.server.MD5SignatureTool;
import cn.mixpay.server.RSASignatureTool;

public class MainActivity extends Activity {

    public static final String TAG = "cn.mixpay.sample.MainActivity";
    public static final String APP_KEY = "6018842380091782";
    // RSA private key 必须是pkcs8格式的
    public static final String PRIVATE_KEY =
            "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANkwXDLB0zy+KxA1\n" +
            "2Ayj0dmdEnlzw7sg93x9yEDiIC190MH2qPYwXLqKb8UxTKT2RbGPzR7fdgnHatqm\n" +
            "T9M8VAcSFyNErHSj05yWqQmHtfSz070KpvjrCV0y3j+R0vWOZKjCeLcWV2EJQSyN\n" +
            "HaJQ85zURVZVmQ3EQTrC/b3XUfTBAgMBAAECgYEAgagRImv7gwO0CZVbSRBzMopG\n" +
            "gSLO8mOX20XrynAz7XRyr5hk/bOZZ/NFDl+D9ebfbEUAmzGj8eqOq8ElXmFhd02B\n" +
            "fvBTbNk62+XFTOcOmUwnAHihlZvIWNyry6XF0oCUOhSTr88Qj6dVBO6bOdMaMy61\n" +
            "4uZrkhsBJdT1gRr95VECQQDx1LXt4mF9TbojhIBsBcJVRtanPQVqs0QMq+4qjb9+\n" +
            "VQQiKqWWJBRcW4d5uB+8YrGUoRQ+imh2pdbKTt+YUl9PAkEA5eoJjg06P7rMpLT3\n" +
            "ytD9TFuqAlmoI8Zt6uJAiaBKxrS55lU2C/Z7PPnf/nutC1BO/Ro10BXkaoHQV0BM\n" +
            "kBbm7wJBANiDjW8yY/5//MuX8jLE5Tzcc4rAA32IisCdnlrTFkPRciX08NP/8YrP\n" +
            "F/hOdXuBEWT04t8rpGO2DxyKiX4jIdkCQQCxY9qNbG1F4JWmey44oPjeYlcQyyYs\n" +
            "vBvwvzJ2PWOUXrqUGajCoCFD0bNegQPpl8PqCr/8VffPqWsa9rRb9B+nAkAemh3v\n" +
            "sUTxfxAucCq6F4VbT2UNDFi8keYqlM+5+rqKzbkXqKr/DJwsN0hqZhWdZvjTzjFl\n" +
            "oLb+Blhz/ugm1D/u";
    public static final String MD5_SECRET_KEY = "019fafebfebf4a9d28c989254660f46c";
    private ArrayList<Order> orders = null;
    private MixpayAPI mixpay;
    private Order currentOrder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mixpay = new MixpayAPI(this, APP_KEY);
    }

    public void pay(View view) {
        final int orderPosition = view.getId();
        Order order = orders.get(orderPosition);
        int payResult = mixpay.startPay(this, order);
        currentOrder = order;
        switch (payResult) {
            case MixpayAPI.START_PAY_FAILED:
                Log.e(TAG, "启动聚易付支付服务出错");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MixpayAPI.REQUEST_CODE_START_PAY) {
            Long mixpayOrderId = null;
            try {
                mixpayOrderId = data.getLongExtra(MixpayAPI.GET_MIXPAY_ORDER_ID, 0);
            }catch (Exception e) {
                //mixpayOrderId = new Long(0);
                Log.e(TAG, "no order is returned");
            }
            switch (resultCode) {
                case MixpayAPI.ORDER_STATUS_PAY_SUCCESS:
                    //TODO:支付成功，继续程序逻辑
                    Toast.makeText(this, "支付成功:" + (mixpayOrderId == null ?  "" : mixpayOrderId.toString()), Toast.LENGTH_LONG).show();
                    break;
                case MixpayAPI.ORDER_STATUS_UNPAY:
                    //TODO:未支付
                    Toast.makeText(this, "订单未支付:" + (mixpayOrderId == null ?  "" : mixpayOrderId.toString()), Toast.LENGTH_LONG).show();
                    queryMixpayOrder(mixpayOrderId);
                    break;
                case MixpayAPI.ORDER_STATUS_PAYING:
                    //TODO:支付中，可以等待Mixpay异步通知，也可以主动查询订单状态
                    Toast.makeText(this, "支付中:" + (mixpayOrderId == null ?  "" : mixpayOrderId.toString()), Toast.LENGTH_LONG).show();
                    break;
                case MixpayAPI.ORDER_STATUS_CANCELED:
                    Toast.makeText(this, "支付取消:" + (mixpayOrderId == null ?  "" : mixpayOrderId.toString()), Toast.LENGTH_LONG).show();
                    break;
                case MixpayAPI.ORDER_STATUS_PAY_FAILURE:
                    Toast.makeText(this, "支付失败:" + mixpayOrderId == null ?  "" : mixpayOrderId.toString(), Toast.LENGTH_LONG).show();
                    break;
                case MixpayAPI.ORDER_STATUS_UNKNOWN:
                    //TODO:未知状态，可以等待Mixpay异步通知，也可以主动查询订单状态
                    Toast.makeText(this, "未知状态" + (mixpayOrderId == null ?  "" : mixpayOrderId.toString()), Toast.LENGTH_LONG).show();
                    break;
            }
            if (mixpayOrderId != null && currentOrder != null) {
               currentOrder.setMixpayOrderId(mixpayOrderId);
            }
        }

    }


    // 刷新订单列表，为了方便测试，将随机生成订单编码
    public void renewOrderList(View view) {
        // 生成三个测试订单
        orders = new ArrayList<Order>();
        Order wand = new Order("order_id", "U001", 1, "魔法杖", "魔法杖", "P001");
        Order cloak = new Order("order_id", "U001", 1, "斗篷", "Harry Potter 今天下单", "P002");
        Order broomstick = new Order("order_id", "U001", 1, "飞行扫帚", "光速2000最新型号，速度超快", "P003");
        orders.add(wand);
        orders.add(cloak);
        orders.add(broomstick);

        RadioGroup radioGroup =  (RadioGroup) findViewById(R.id.rg_sign_type);
        int signTypeId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(signTypeId);
        String signType = radioButton.getText().toString();

        for (int i = 0, iMax = orders.size(); i < iMax; i ++) {
            Order order = orders.get(i);
            Random random = new Random();
            long time = (new Date()).getTime() + i + 1000;
            String id = "O_" + random.nextInt(1000) + "_" +  time;
            order.setAppOrderId(id);
            order.setMixpayOrderId(null);
            order.setPaystatus(OrderStatus.UNKNOWN);
            String sign = "";
            if (signType.equalsIgnoreCase("rsa")) {
                sign = signOrderByRSA(order, APP_KEY);
            } else {
                sign = signOrderByMD5(order, APP_KEY);
            }
            order.setSign(sign);
        }
        ListView listView = (ListView) findViewById(R.id.order_list);
        OrderArrayAdapter orderArrayAdapter = new OrderArrayAdapter(this, R.layout.order_list_item, orders);
        listView.setAdapter(orderArrayAdapter);
    }

    // 查询订单状态
    public void refreshOrderList(View view) {
        final ListView listView = (ListView) findViewById(R.id.order_list);
        final Activity context = this;
        for (int i = 0, iMax = orders.size(); i < iMax; i ++) {
            final Order order = orders.get(i);
            final Long mixpayOrderId = order.getMixpayOrderId();
            if (mixpayOrderId == null) {
                continue;
            }
            order.setMixpayOrderId(mixpayOrderId);

            new Thread() {
                @Override
                public void run() {
                    JSONObject _mixpayOrder = queryMixpayOrder(mixpayOrderId);
                    if (_mixpayOrder == null) {
                        Log.e(TAG, "查询订单" + mixpayOrderId + "失败");
                    } else {
                        try {
                            int payStatus = _mixpayOrder.getInt("order_pay_status");
                            order.setPaystatus(payStatus);
                            Log.e("查询成功:" , order.getMixpayOrderId().toString() + order.getPaystatus());
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            OrderArrayAdapter orderArrayAdapter = new OrderArrayAdapter(context, R.layout.order_list_item, orders);
                            listView.setAdapter(orderArrayAdapter);
                        }
                    });

                }
            }.start();
        }


    }

    /**
     * 对订单进行RSA签名
     * 为了提高安全性，强烈建议签名在商户服务端进行。具体实现可参考本方法
     * @param order
     * @param appKey
     * @return
     */
    private String signOrderByRSA(Order order, String appKey) {
        if (appKey == null
                || order.getAppOrderId() == null
                || order.getOrderTitle() == null
                || order.getOrderDesc() == null
                || order.getAppUserId() == null
                || order.getAmount() == 0) {
            return null;
        }
        String sign = "";
        Log.e(TAG, "==============================");
        try {
            sign = RSASignatureTool.signOrderInfo(order.getAmount(),
                    appKey,
                    order.getExtData(),
                    order.getAppOrderId(),
                    order.getNotifyUrl(),
                    order.getOrderDesc(),
                    order.getOrderTitle(),
                    order.getProductDesc(),
                    order.getProductId(),
                    order.getProductName(),
                    order.getAppUserId(), PRIVATE_KEY);
            sign = URLEncoder.encode(sign, "utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.toString());
        }
        Log.e(TAG, "==============================");
        return sign;
    }

    /**
     * 对订单进行MD5签名
     * 为了提高安全性，强烈建议签名在商户服务端进行。具体实现可参考本方法
     * @param order
     * @param appKey
     * @return
     */
    private String signOrderByMD5(Order order, String appKey) {
        if (appKey == null
                || order.getAppOrderId() == null
                || order.getOrderTitle() == null
                || order.getOrderDesc() == null
                || order.getAppUserId() == null
                || order.getAmount() == 0) {
            return null;
        }
        String sign = "";
        Log.e(TAG, "==============================");
        try {
            sign = MD5SignatureTool.signOrderInfo(order.getAmount(),
                    appKey,
                    order.getExtData(),
                    order.getAppOrderId(),
                    order.getNotifyUrl(),
                    order.getOrderDesc(),
                    order.getOrderTitle(),
                    order.getProductDesc(),
                    order.getProductId(),
                    order.getProductName(),
                    order.getAppUserId(), MD5_SECRET_KEY);
            sign = URLEncoder.encode(sign, "utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.toString());
        }
        Log.e(TAG, "==============================");
        return sign;
    }

    /**
     * 查询订单支付状态示例
     * @param mixpayOrderId
     */
    private JSONObject queryMixpayOrder(Long mixpayOrderId) {
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("app_key", APP_KEY);
        queryParams.put("order_id", mixpayOrderId.toString());
        RadioGroup radioGroup =  (RadioGroup) findViewById(R.id.rg_sign_type);
        int signTypeId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(signTypeId);
        String signType = radioButton.getText().toString();
        String sign = "";
        if (signType.equalsIgnoreCase("rsa")) {
            sign = RSASignatureTool.sign(queryParams, PRIVATE_KEY);
        } else {
            sign = MD5SignatureTool.sign(queryParams, MD5_SECRET_KEY);
        }
        try {
            String encodeSign = URLEncoder.encode(sign, "utf-8");
            return mixpay.queryOrderJSON(APP_KEY, mixpayOrderId, encodeSign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
