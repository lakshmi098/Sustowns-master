package com.sustown.sustownsapp.Adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.sustownsapp.R;
import com.google.gson.JsonElement;
import com.sustown.sustownsapp.Activities.AddPaymentActivity;
import com.sustown.sustownsapp.Activities.ContractOrdersInvoice;
import com.sustown.sustownsapp.Activities.MyContractOrdersActivity;
import com.sustown.sustownsapp.Activities.PreferenceUtils;
import com.sustown.sustownsapp.Activities.StoreReceivedOrdersActivity;
import com.sustown.sustownsapp.Api.DZ_URL;
import com.sustown.sustownsapp.Api.OrdersApi;
import com.sustown.sustownsapp.Models.ContractPurchasesModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyContractOrdersAdapter extends RecyclerView.Adapter<MyContractOrdersAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email,pro_id,user_id,user_role,order_status,pay_method,orderid;
    PreferenceUtils preferenceUtils;
    String orderId,invoiceNo,complete_amount_status;
    ArrayList<ContractPurchasesModel> contractOrderModels;
    ProgressDialog progressDialog;
    Dialog customdialog;
    Button cancel_btn, submit_btn;
    String sellernameStr, sellernoStr, sellerAddress, sellerCountry, sellerZipcode;
    EditText seller_name, seller_number, product_address, product_country, product_zipcode;
    AlertDialog alertDialog;
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
        complete_amount_status = contractOrderModels.get(position).getComplete_amount_status();
        pay_method = contractOrderModels.get(position).getPay_type();
        if (contractOrderModels.get(position) != null) {
            viewHolder.orderName.setText(contractOrderModels.get(position).getJob_name());
            viewHolder.order_no.setText(contractOrderModels.get(position).getInvoice_no());
            viewHolder.orderDate.setText(contractOrderModels.get(position).getOrder_date());
            viewHolder.orderPrice.setText(contractOrderModels.get(position).getTotalprice());
            if (order_status.equalsIgnoreCase("0")) {
                viewHolder.confirm_order_btn.setVisibility(View.VISIBLE);
                viewHolder.cancel_orderbtn.setVisibility(View.VISIBLE);
                viewHolder.orderStatus.setText("Pending");
            } else if (order_status.equalsIgnoreCase("1")) {
                viewHolder.confirm_order_btn.setVisibility(View.GONE);
                viewHolder.cancel_orderbtn.setVisibility(View.GONE);
                viewHolder.orderStatus.setText("Completed");
            } else if (order_status.equalsIgnoreCase("2")) {
                viewHolder.orderStatus.setText("Cancelled");
            }
          /*  if(complete_amount_status.equalsIgnoreCase("0") && !contractOrderModels.get(position).getBank_thr_ran_id().isEmpty()){
                viewHolder.add_payment_btn.setVisibility(View.VISIBLE);
            }
            // latest
            if(order_status.equalsIgnoreCase("0")){
                viewHolder.orderStatus.setText("Pending");
            }else if(order_status.equalsIgnoreCase("1")){
                viewHolder.add_payment_btn.setVisibility(View.GONE);
                viewHolder.add_transport_btn.setVisibility(View.VISIBLE);
                viewHolder.orderStatus.setText("Complete");
            }else if(order_status.equalsIgnoreCase("2")){
                viewHolder.add_payment_btn.setVisibility(View.GONE);
                viewHolder.add_transport_btn.setVisibility(View.GONE);
                viewHolder.orderStatus.setText("Cancelled");
            }else if(order_status.equalsIgnoreCase("3")){
                viewHolder.orderStatus.setText("Payment Processing");
                viewHolder.add_payment_btn.setVisibility(View.GONE);
                viewHolder.add_transport_btn.setVisibility(View.GONE);
            }*/
        }
        viewHolder.confirm_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderid = contractOrderModels.get(position).getOrder_id();
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure you want Confirm...?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                submitConfirmOrder();
                                //customdialog.show();
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        viewHolder.cancel_orderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderid = contractOrderModels.get(position).getOrder_id();
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure you want Cancel...?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                submitCancelOrder();
                              //  customdialog.show();
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        viewHolder.add_payment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AddPaymentActivity.class);
                i.putExtra("OrderId",contractOrderModels.get(position).getOrder_id());
                i.putExtra("BankRandId",contractOrderModels.get(position).getBank_thr_ran_id());
                i.putExtra("RandId",contractOrderModels.get(position).getInvoice_no());
                i.putExtra("ContractOrders","2");
                context.startActivity(i);
            }
        });
        viewHolder.view_invoice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderId = contractOrderModels.get(position).getOrder_id();
                invoiceNo = contractOrderModels.get(position).getInvoice_no();
                Intent intent = new Intent(context, ContractOrdersInvoice.class);
                intent.putExtra("OrderId",orderId);
                intent.putExtra("InvoiceNo",invoiceNo);
                intent.putExtra("Status",contractOrderModels.get(position).getOrder_status());
                context.startActivity(intent);

            }
        });
    }
    public void submitConfirmOrder() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("orderid", orderid);
            androidNetworkingConfirm(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingConfirm(JSONObject jsonObject){
        //progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Postcontractservice/confirmorder")
                .addJSONObjectBody(jsonObject) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "JSON : " + response);
                        try {
                            JSONObject responseObj = response.getJSONObject("response");
                            String message = responseObj.getString("message");
                            String success = responseObj.getString("success");
                            if (success.equalsIgnoreCase("1")) {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                ((MyContractOrdersActivity)context).getContractOrdersList();
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                    }
                });
    }
    public void submitCancelOrder() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("orderid", orderid);
            androidNetworkingCancel(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingCancel(JSONObject jsonObject){
        //progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Postcontractservice/cancelorder")
                .addJSONObjectBody(jsonObject) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "JSON : " + response);
                        try {
                            JSONObject responseObj = response.getJSONObject("response");
                            String message = responseObj.getString("message");
                            String success = responseObj.getString("success");
                            if (success.equalsIgnoreCase("1")) {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                ((MyContractOrdersActivity)context).getContractOrdersList();
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
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
        TextView orderName, order_no, orderDate, orderStatus,orderPrice;
        Button add_payment_btn, confirm_order_btn;
        Button cancel_orderbtn,add_transport_btn,view_invoice_btn;
        LinearLayout ll_status;

        public ViewHolder(View view) {
            super(view);
            orderName = (TextView) view.findViewById(R.id.order_name);
            order_no = (TextView) view.findViewById(R.id.order_no);
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
