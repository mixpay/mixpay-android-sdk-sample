package cn.mixpay.sdksample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.mixpay.android.core.OrderStatus;
import cn.mixpay.sdk.Order;

/**
 * Created by xyn0563 on 4/7/14.
 */
public class OrderArrayAdapter extends ArrayAdapter<Order> {

    private final Context context;
    private final ArrayList<Order> orders;

    private static final String TAG = "cn.mixpay.apk.widget.PayTypeArrayAdapter";

    public OrderArrayAdapter(Context context, int viewResourceId, ArrayList<Order> orders) {
        super(context, viewResourceId, orders);
        this.context = context;
        this.orders = orders;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Order order = orders.get(position);
        View orderView = convertView;
        if (orderView == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            orderView = vi.inflate(R.layout.order_list_item, parent, false);
        }
        TextView orderTitleTV = (TextView) orderView.findViewById(R.id.order_title);
        orderTitleTV.setText(order.getOrderTitle());

        //TextView orderDescTV = (TextView) orderView.findViewById(R.id.order_desc);
        //orderDescTV.setText(order.getOrderDesc());

        TextView orderIdTV = (TextView) orderView.findViewById(R.id.order_id);
        orderIdTV.setText("商户订单:" + order.getAppOrderId() + "");

        TextView orderInfoTV = (TextView) orderView.findViewById(R.id.order_info);
        String orderStatus = "未知";
        switch (order.getPaystatus()) {
            case OrderStatus.CANCELED:
                orderStatus = "取消";
                break;
            case OrderStatus.DUPLICATE:
                orderStatus = "订单重复";
                break;
            case OrderStatus.FAILURE:
                orderStatus = "支付失败";
                break;
            case OrderStatus.PAYING:
                orderStatus = "支付中";
                break;
            case OrderStatus.UNPAY:
                orderStatus = "未支付";
                break;
            case OrderStatus.SUCCESS:
                orderStatus = "支付成功";
                break;
        }
        if (order.getMixpayOrderId() == null) {
            orderInfoTV.setText("状态未知");
        } else {
            orderInfoTV.setText("mixpay订单:" + order.getMixpayOrderId() + "[" + orderStatus + "]");
        }

        TextView amountTV = (TextView) orderView.findViewById(R.id.order_amount);
        amountTV.setText((order.getAmount() / 100.00) + "");

        orderView.setId(position);

        orderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).pay(view);
            }
        });

        return orderView;
    }
}
