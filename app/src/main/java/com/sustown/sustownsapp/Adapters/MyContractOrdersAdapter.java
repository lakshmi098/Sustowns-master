package com.sustown.sustownsapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sustownsapp.R;
import com.sustown.sustownsapp.Activities.ContractOrdersInvoice;
import com.sustown.sustownsapp.Activities.PreferenceUtils;
import com.sustown.sustownsapp.Models.ContractPurchasesModel;

import java.util.ArrayList;

public class MyContractOrdersAdapter extends RecyclerView.Adapter<MyContractOrdersAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email,pro_id,user_id,user_role,order_status,pay_method;
    PreferenceUtils preferenceUtils;
    String orderId,invoiceNo;
    ArrayList<ContractPurchasesModel> contractOrderModels;
    ProgressDialog progressDialog;

    public MyContractOrdersAdapter(Context context, ArrayList<ContractPurchasesModel> contractOrderModels) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.contractOrderModels = contractOrderModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_contract_orders_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.add_payment_btn.setVisibility(View.GONE);
        viewHolder.add_transport_btn.setVisibility(View.GONE);
        viewHolder.view_invoice_btn.setVisibility(View.VISIBLE);
        order_status = contractOrderModels.get(position).getOrder_status();
        pay_method = contractOrderModels.get(position).getPay_type();
        if (contractOrderModels.get(position) != null) {
            viewHolder.orderName.setText(contractOrderModels.get(position).getJob_name());
            viewHolder.orderQuantity.setText(contractOrderModels.get(position).getQuantity());
            viewHolder.orderDate.setText(contractOrderModels.get(position).getOrder_date());
            viewHolder.orderPrice.setText(contractOrderModels.get(position).getTotalprice());
            if (order_status.equalsIgnoreCase("1")) {
                viewHolder.orderStatus.setText("Complete");
            } else if (order_status.equalsIgnoreCase("2")) {
                viewHolder.orderStatus.setText("Cancel");
            } else if(order_status.equalsIgnoreCase("0")) {
                viewHolder.ll_status.setVisibility(View.GONE);
                viewHolder.orderStatus.setVisibility(View.GONE);
                viewHolder.confirm_order_btn.setVisibility(View.VISIBLE);
                viewHolder.cancel_orderbtn.setVisibility(View.VISIBLE);
            }
        }
        viewHolder.view_invoice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderId = contractOrderModels.get(position).getOrder_id();
                invoiceNo = contractOrderModels.get(position).getInvoice_no();
                Intent intent = new Intent(context, ContractOrdersInvoice.class);
                intent.putExtra("OrderId",orderId);
                intent.putExtra("InvoiceNo",invoiceNo);
                context.startActivity(intent);

            }
        });

    }
    public void removeAt(int position) {
        //  notifyDataSetChanged();

    }
    @Override
    public int getItemCount() {
        return contractOrderModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderName, orderQuantity, orderDate, orderStatus,orderPrice;
        Button add_payment_btn, confirm_order_btn;
        Button cancel_orderbtn,add_transport_btn,view_invoice_btn;
        LinearLayout ll_status;

        public ViewHolder(View view) {
            super(view);
            orderName = (TextView) view.findViewById(R.id.order_name);
            orderQuantity = (TextView) view.findViewById(R.id.order_quantity);
            orderDate = (TextView) view.findViewById(R.id.order_date);
            orderStatus = (TextView) view.findViewById(R.id.order_status);
            add_payment_btn = (Button) view.findViewById(R.id.add_payment_btn);
            confirm_order_btn = (Button) view.findViewById(R.id.confirm_order_btn);
            add_transport_btn = (Button) view.findViewById(R.id.add_transport_btn);
            cancel_orderbtn = (Button) view.findViewById(R.id.cancel_orderbtn);
            orderPrice = (TextView) view.findViewById(R.id.order_price);
            ll_status = (LinearLayout) view.findViewById(R.id.ll_status);
            view_invoice_btn = (Button) view.findViewById(R.id.view_invoice_btn);

        }
    }
}
