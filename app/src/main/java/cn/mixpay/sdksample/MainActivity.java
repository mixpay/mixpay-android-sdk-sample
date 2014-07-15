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
    //public static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKzgKcAVdV938q/fK3NJDpThSvaXJQPooipTqVDFPHK+uhDUWIu0frwBy96lFWPQCLcXvOKhGi/WDmSaYVY8jIp53qm8r5cReAp1TqctV4lrHBxfWc6INeHj1Qk0jBwGhENQk/ySvEnk3/zTKOzWJh8blXLXIYrwD9mVqMBpDyDDAgMBAAECgYAhoTXd/Q6pNL9MQUDFm4evpKgdkkeMHBw32bCNWuEofTva/EQBYWpqDntnY3vmv9iCLab7+1UJyz1firy2tu6ry4+dlu/LdbEHqMwBPfcKHfwT2EG6Q41mA7KMlf6def+fBHDSRvWGFW82oiMVAUSvchr7lELGbarZuyHL1pFOiQJBAN7KHfNhmPoFhvFT6si5PXpl68a2k7HqjLIU23SiIQVK6RgqvNJolqzTKoE8r8AdGx9QDzDZAxuvTsb1JnmGEA0CQQDGpUeah9XIAEBfGWW1UHHe9QDmRoeynm678zdlsMNZy4O0U/1ucdbcnq5uFKglSDuxCvFURPuP1qUSipQVyPAPAkEAgFGjrJGzHZZfYdI2sYPYAA6CHYL9UIVKoitXNzdGk5jQ5sV+2iW5WzOJEMTWaW2aOI/RIA5uNtzjH78FeQsZZQJBAJ7/G9jscIis6tkzkt0Vjo9Ou3GVcnfdp/R4MBcM7M+qvbhQocENDVV5DVS+4/czYdPLVm6E6HWw8F9u3CiztLECQEh+qsa6qkGys2sJeNX0yOzgiqw3Iha1HMpLFIgx+Bo7hmZBtEZGtL1MpdgoYAv7bwHm3oi+FMYR8NmdNC9HrSs=";
    public static final String PRIVATE_KEY = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAKHZQ33yZG1KU/E4\n" +
            "FB1cAOhiBdd4wLR5dEzrbUhhJXGhQ0liOAwTDD24L08K3D4YRVr0f/geCLSdwon2\n" +
            "UjSKOsjvunXZPgEmiUKi6u7y9DtdJSe3dNE+Lh+yrFZtNw8vQcSTpBpuTd7PHoxN\n" +
            "VOMQM2aKt6ikXpB+qqiv5byVh4onAgMBAAECgYEAj2S+t0eaUnoYBQ96QsjQxRKT\n" +
            "umPx1/QDZmLvX8EyO4p04v9ySMBXiBc3UwB8QUefqwYUmu+BJwTe5vyluIb/rL44\n" +
            "BzGlyJ4LTB5l62gg1T5QUOyPrToELBtTiRtqg7/+/ze2IsV4lxpsZy7Qs5W04Dqx\n" +
            "nX7Gczh4goqHsz4S2AECQQDUOzSms9bji8GIqB6Zm4UV0/SfYPnBzGM9U6vjk9wq\n" +
            "exahHhkJ9I9nK3gOPag6SEDH4bqs5OXxAIlGIvnve8dBAkEAwzoZTE0Zqs/jiaOg\n" +
            "P1UyS/LRDaJvrLKNIFgDAMS1+GtCaRUXNYdjx5cViYyaQHizabtejSZYqGtKgFwh\n" +
            "XLmfZwJBAKi/Lp0Bz3H7tHVBH5uJUadKcW2sQMn8BkhD2B9KeLt6RUL0WHpW7xLc\n" +
            "OEgydClrO77yV2iTU9VtVLjkpI/rfkECQQCBwdDktO4VaP1cE0REM/UtI6OtQ0Sk\n" +
            "bTx9veGpFhW4mlnWjC0T5/v6wcGP3cL8jPz6jo3GlTEiJ8Wp0RJflgSLAkEAnAx0\n" +
            "RfCvjw98xU4wO2Xpru6jBWqwzTG9vrzf/3h8xnksgNWUBidjZu+XNbpGsuM8tk6b\n" +
            "msIUwSk2b35IETs/xQ==";
    public static final String MD5_SECRET_KEY = "019fafebfebf4a9d28c989254660f46c";


    //qatang product
    /*
    public static final String APP_KEY = "6018371981903597";
    public static final String PRIVATE_KEY = "MIICXAIBAAKBgQC1kkBDvAcZHpd6dsy7rbiKsdl+9rUGjvsK0qe7+sDk3MCnexKs0vukEBPZiC4ZS0tB6uhqob0BsYiQqZTkBu4rhBvwrOi44rJKbBUY0IsSEN8JjltYFBDPuF5RSYJitJVSkrh5F3SvNnC3BpDcmdzyePDLbke4IMyq21uxMp/5mwIDAQABAoGBAJ1czoOlz0UPBVum4JN74SebMhPef0/a1Wow3hyGG21+gR3mBW5xQJSVNo7efo1/ew77J0lbObLhCanOv3LNKtZoSEc+Sm8sGD+tUBBmlC2MvIupZztIUOUYBf/D6PB0Zj50ugOtS6xmCBq004LanRindYe3AfmRvp+b65QX/5MpAkEA3TthV/NaWoiRPg7xIGbYDYybRiqARtkVlRv5twl8zNXFbBGKaxu959VIj+w4p1EhX02p89hv8a9eNNh1awu5DwJBANIbPYCkmFVVkv0IZXgB2IB1EPQIx9SXLVTD1AAsI+Bs771pxftGSfxq4u/SFAHoGnk8qRvQRvIjAPuAqO4zvrUCQHQLttcUnpuIsW81FUSizcflrnlSx/Dh5FFP2GAryNwFckZquQnQBoB6P1LHXTxe8Tt1mKWBLc8/5xGfTZA2GP8CQHyQ5+LIMwSMyqu3+aivt3NZdKaqOgeBZb/Wpm8/vDmHfI+ZEWcLYjwEBu3WaERFHsT4QO6biiRuultiCUXIae0CQGWnMDpafODRuP0tvrePOKRTPqHiYcxGqNUGcM/f+pfxfpfatpkJXvuk8HoLZctbTPAyJ8tgDDnStiAoBY8TV/M=";
    public static final String MD5_SECRET_KEY = "98ed9a1f17a7ebccf23e3f89c9cbac3d";
    */
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
