package com.sustown.sustownsapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sustownsapp.R;
import com.sustown.sustownsapp.Activities.AddPaymentActivity;
import com.sustown.sustownsapp.Activities.AddTransportActivity;
import com.sustown.sustownsapp.Activities.LocationDialogActivity;
import com.sustown.sustownsapp.Activities.OrderDetailsActivity;
import com.sustown.sustownsapp.Activities.PreferenceUtils;
import com.sustown.sustownsapp.Activities.ProductDetailsActivity;
import com.sustown.sustownsapp.Activities.StoreReceivedOrdersActivity;
import com.sustown.sustownsapp.Models.OrderModel;

import java.util.ArrayList;

public class StoreReceivedOrdersAdapter extends RecyclerView.Adapter<StoreReceivedOrdersAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email,pro_id,user_id,user_role,order_status,order_id,pay_method,complete_amount_status,paymentStatus;
    PreferenceUtils preferenceUtils;
    String[] order;
    ArrayList<OrderModel> orderModels;
    ProgressDialog progressDialog;

    public StoreReceivedOrdersAdapter(StoreReceivedOrdersActivity context, ArrayList<OrderModel> orderModels) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.orderModels = orderModels;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_contract_orders_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        order_status = orderModels.get(position).getOrder_status();
        pay_method = orderModels.get(position).getPay_method();
        complete_amount_status = orderModels.get(position).getComplete_amount_status();
        paymentStatus = orderModels.get(position).getPayment_status();
        if(orderModels.get(position) != null){
            viewHolder.orderName.setText(orderModels.get(position).getPr_title());
            viewHolder.order_no.setText(orderModels.get(position).getInvoice_no());
            viewHolder.orderDate.setText(orderModels.get(position).getOrder_date());
            viewHolder.order_price.setText(orderModels.get(position).getTotalprice());
            if(order_status.equalsIgnoreCase("0")){
                viewHolder.orderStatus.setText("Pending");
               // viewHolder.add_payment_btn.setVisibility(View.GONE);
               // viewHolder.add_transport_btn.setVisibility(View.GONE);
               // viewHolder.ll_paymentstatus.setVisibility(View.GONE);
            }else if(order_status.equalsIgnoreCase("1")){
               // viewHolder.add_payment_btn.setVisibility(View.GONE);
                if(orderModels.get(position).getShipamount().equalsIgnoreCase("")||orderModels.get(position).getShipamount().equalsIgnoreCase("0")) {
                    viewHolder.add_transport_btn.setVisibility(View.VISIBLE);
                }
               // viewHolder.ll_paymentstatus.setVisibility(View.GONE);
                viewHolder.orderStatus.setText("Complete");
            }else if(order_status.equalsIgnoreCase("2")){
              //  viewHolder.add_payment_btn.setVisibility(View.GONE);
               // viewHolder.add_transport_btn.setVisibility(View.GONE);
              //  viewHolder.ll_paymentstatus.setVisibility(View.GONE);
                viewHolder.orderStatus.setText("Rejected");
            }else if(order_status.equalsIgnoreCase("3")){
                viewHolder.orderStatus.setText("Pending");
            }
            if(complete_amount_status.equalsIgnoreCase("0") && !orderModels.get(position).getBank_thr_ran_id().equalsIgnoreCase("null")) {
                viewHolder.add_payment_btn.setVisibility(View.VISIBLE);
               // viewHolder.ll_paymentstatus.setVisibility(View.GONE);
               // viewHolder.add_transport_btn.setVisibility(View.GONE);
            }else if(paymentStatus.equalsIgnoreCase("2")){
                viewHolder.ll_paymentstatus.setVisibility(View.VISIBLE);
                viewHolder.payment_status.setVisibility(View.VISIBLE);
                viewHolder.payment_status.setText("Payment Received Successfully");
            }else if(paymentStatus.equalsIgnoreCase("1")){
                viewHolder.ll_paymentstatus.setVisibility(View.VISIBLE);
                viewHolder.payment_status.setVisibility(View.VISIBLE);
                viewHolder.payment_status.setText("payment in process");
            }else if(paymentStatus.equalsIgnoreCase("0")){
                viewHolder.ll_paymentstatus.setVisibility(View.VISIBLE);
                viewHolder.payment_status.setVisibility(View.VISIBLE);
                viewHolder.payment_status.setText("Payment Process was failed");
            }else{

            }
       /*     if(order_status.equalsIgnoreCase("0")){
                viewHolder.orderStatus.setText("Pending");
                viewHolder.add_payment_btn.setVisibility(View.GONE);
                viewHolder.add_transport_btn.setVisibility(View.GONE);
                if(complete_amount_status.equalsIgnoreCase("0") && !orderModels.get(position).getBank_thr_ran_id().isEmpty()){
                    viewHolder.add_payment_btn.setVisibility(View.VISIBLE);
                    viewHolder.add_transport_btn.setVisibility(View.GONE);
                }  else if(paymentStatus.equalsIgnoreCase("2")){
                    viewHolder.orderStatus.setText("Payment Received Successfully");
                    viewHolder.add_payment_btn.setVisibility(View.GONE);
                    viewHolder.add_transport_btn.setVisibility(View.GONE);
                }else if(paymentStatus.equalsIgnoreCase("1")){
                    viewHolder.orderStatus.setText("payment in process");
                    viewHolder.add_payment_btn.setVisibility(View.GONE);
                    viewHolder.add_transport_btn.setVisibility(View.GONE);
                }else if(paymentStatus.equalsIgnoreCase("0")){
                    viewHolder.orderStatus.setText("Payment Process was failed");
                    viewHolder.add_payment_btn.setVisibility(View.GONE);
                    viewHolder.add_transport_btn.setVisibility(View.GONE);
                }
            }else if(order_status.equalsIgnoreCase("1")){
                viewHolder.add_payment_btn.setVisibility(View.GONE);
                viewHolder.add_transport_btn.setVisibility(View.VISIBLE);
                viewHolder.orderStatus.setText("Complete");
            }else if(order_status.equalsIgnoreCase("2")){
                viewHolder.add_payment_btn.setVisibility(View.GONE);
                viewHolder.add_transport_btn.setVisibility(View.GONE);
                viewHolder.orderStatus.setText("Cancelled");
            }*//*else if(order_status.equalsIgnoreCase("3")){
                viewHolder.orderStatus.setText("Payment Processing");
                viewHolder.add_payment_btn.setVisibility(View.GONE);
                viewHolder.add_transport_btn.setVisibility(View.GONE);
            }*/
        }
        viewHolder.add_transport_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderModels.get(position).getZipcode().equalsIgnoreCase("") || orderModels.get(position).getZipcode().equalsIgnoreCase("null"))
                {
                    Intent i = new Intent(context, LocationDialogActivity.class);
                    i.putExtra("ContractLocation", "2");
                    i.putExtra("OrderId", orderModels.get(position).getId());
                    i.putExtra("InvoiceNo", orderModels.get(position).getInvoice_no());
                    i.putExtra("RandId", orderModels.get(position).getProduct_order_id());
               /* i.putExtra("OrderId",orderModels.get(position).getProduct_order_id());
                i.putExtra("InvoiceNo",orderModels.get(position).getInvoice_no());
                i.putExtra("ContractTransport","0");
                */
                    context.startActivity(i);
                }else{
                    Intent i = new Intent(context, AddTransportActivity.class);
                    i.putExtra("OrderId",orderModels.get(position).getProduct_order_id());
                    i.putExtra("InvoiceNo",orderModels.get(position).getInvoice_no());
                   // i.putExtra("RandId", orderModels.get(position).getProduct_order_id());
                    i.putExtra("ContractTransport","0");
                    context.startActivity(i);
                }
            }
        });
        viewHolder.view_invoice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_id = orderModels.get(position).getId();
                Intent i = new Intent(context, OrderDetailsActivity.class);
                i.putExtra("OrderId",order_id);
                context.startActivity(i);
            }
        });
        viewHolder.add_payment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AddPaymentActivity.class);
                i.putExtra("OrderId",orderModels.get(position).getId());
                i.putExtra("BankRandId",orderModels.get(position).getBank_thr_ran_id());
                i.putExtra("RandId",orderModels.get(position).getProduct_order_id());
                i.putExtra("ContractOrders","1");
                context.startActivity(i);
            }
        });
       /* viewHolder.remove_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        // viewHolder.orderName.setText(order[position]);
     /*   viewHolder.orderQuantity.setText(order[position]);
        viewHolder.orderDate.setText(order[position]);
        viewHolder.orderStatus.setText(order[position]);*/

    }
    public void removeAt(int position) {
        //  notifyDataSetChanged();

    }
    @Override
    public int getItemCount() {
        return orderModels.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,remove_product,increase;
        TextView orderName,order_no,orderDate,orderStatus,order_price,payment_status;
        Button add_payment_btn,add_transport_btn,view_invoice_btn;
        LinearLayout ll_paymentstatus;
        public ViewHolder(View view) {
            super(view);
            orderName = (TextView) view.findViewById(R.id.order_name);
            order_no = (TextView) view.findViewById(R.id.order_no);
            orderDate = (TextView) view.findViewById(R.id.order_date);
            orderStatus = (TextView) view.findViewById(R.id.order_status);
            add_payment_btn = (Button) view.findViewById(R.id.add_payment_btn);
            add_transport_btn = (Button) view.findViewById(R.id.add_transport_btn);
            order_price = (TextView) view.findViewById(R.id.order_price);
            view_invoice_btn = (Button) view.findViewById(R.id.view_invoice_btn);
            payment_status = (TextView) view.findViewById(R.id.payment_status);
            ll_paymentstatus = (LinearLayout) view.findViewById(R.id.ll_paymentstatus);
        }
    }
}