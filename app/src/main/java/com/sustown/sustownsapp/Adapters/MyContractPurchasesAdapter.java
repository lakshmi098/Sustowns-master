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
import android.widget.TextView;

import com.example.sustownsapp.R;
import com.sustown.sustownsapp.Activities.ContractOrdersInvoice;
import com.sustown.sustownsapp.Activities.MyContractPurchases;
import com.sustown.sustownsapp.Activities.PreferenceUtils;
import com.sustown.sustownsapp.Models.ContractPurchasesModel;

import java.util.ArrayList;

public class MyContractPurchasesAdapter extends RecyclerView.Adapter<MyContractPurchasesAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email,pro_id,user_id,user_role;
    PreferenceUtils preferenceUtils;
    String[] order;
    ArrayList<ContractPurchasesModel> contractPurchasesModels;
    ProgressDialog progressDialog;

    public MyContractPurchasesAdapter(MyContractPurchases context, ArrayList<ContractPurchasesModel> contractPurchasesModels) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.contractPurchasesModels = contractPurchasesModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_contract_purchases_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        if(contractPurchasesModels.get(position) != null) {
            viewHolder.purchaseName.setText(contractPurchasesModels.get(position).getJob_name());
            viewHolder.purchase_price.setText(contractPurchasesModels.get(position).getTotalprice());
            viewHolder.purchaseQuantity.setText(contractPurchasesModels.get(position).getQuantity());
            viewHolder.purchaseDate.setText(contractPurchasesModels.get(position).getOrder_date());
            String status = contractPurchasesModels.get(position).getOrder_status();
            if(status.equalsIgnoreCase("0")) {
                viewHolder.purchaseStatus.setText("Pending");
            }else if(status.equalsIgnoreCase("1")){
                viewHolder.purchaseStatus.setText("Complete");
            }else{
                viewHolder.purchaseStatus.setText("Pending");
            }
        }
        viewHolder.view_con_purchase_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String order_id = contractPurchasesModels.get(position).getOrder_id();
                String invoice_no = contractPurchasesModels.get(position).getInvoice_no();
                Intent i = new Intent(context, ContractOrdersInvoice.class);
                i.putExtra("OrderId",order_id);
                i.putExtra("InvoiceNo",invoice_no);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contractPurchasesModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,decrease,increase;
        TextView purchaseName,purchaseQuantity,purchaseDate,purchaseStatus,purchase_price;
        Button view_con_purchase_invoice;
        public ViewHolder(View view) {
            super(view);
            purchaseName = (TextView) view.findViewById(R.id.purchase_name);
            purchase_price = (TextView) view.findViewById(R.id.purchase_price);
            purchaseQuantity = (TextView) view.findViewById(R.id.purchase_quantity);
            purchaseDate = (TextView) view.findViewById(R.id.purchase_date);
            purchaseStatus = (TextView) view.findViewById(R.id.purchase_status);
            view_con_purchase_invoice = (Button) view.findViewById(R.id.view_con_purchase_invoice);

        }
    }
}
