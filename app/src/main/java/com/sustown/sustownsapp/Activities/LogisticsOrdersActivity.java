package com.sustown.sustownsapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sustownsapp.R;
import com.google.gson.JsonElement;
import com.sustown.sustownsapp.Adapters.MyContractPurchasesAdapter;
import com.sustown.sustownsapp.Adapters.TransportReceivedOrdersAdapter;
import com.sustown.sustownsapp.Api.DZ_URL;
import com.sustown.sustownsapp.Api.PostContractsApi;
import com.sustown.sustownsapp.Api.TransportApi;
import com.sustown.sustownsapp.Models.ContractPurchasesModel;
import com.sustown.sustownsapp.Models.TransportDetailsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogisticsOrdersActivity extends AppCompatActivity {

    RecyclerView recycler_view_logistics;
    ImageView backarrow;
    LogisticsOrdersAdapter logisticsOrdersAdapter;
    ProgressDialog progressDialog;
    TextView available_text;
    String user_id,serviceId,orderId,messageStr = "";
    PreferenceUtils preferenceUtils;
    ArrayList<TransportDetailsModel> transportDetailsList;
    LinearLayout ll_transport_details,ll_view_contact_details;
    AlertDialog alertDialog;
    TextView order_prod_name,order_weight,order_quantity,invoice_no, order_packing_type,order_shipped_to,order_seller_address;
    TextView shipping_name,shipping_number,shipping_address,shipping_postalcode,seller_name_text,seller_number_text,seller_address_text,seller_postalcode_text;
    TextView vehicle_number,driver_name,driver_number, text_message,order_vehicle_type,order_load_type,order_quote,order_charge_km,order_min_charge;
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_logistics_orders);

        preferenceUtils = new PreferenceUtils(LogisticsOrdersActivity.this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
        recycler_view_logistics = (RecyclerView) findViewById(R.id.recycler_view_logistics);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_view_logistics.setLayoutManager(linearLayoutManager);
        messageStr = getIntent().getStringExtra("Message");
        text_message = (TextView) findViewById(R.id.text_message);
        if(messageStr.equalsIgnoreCase("") || messageStr.isEmpty() || messageStr.equalsIgnoreCase("null")){
            text_message.setVisibility(View.GONE);
        }else{
            text_message.setVisibility(View.VISIBLE);
            text_message.setText(messageStr);
        }
        available_text = (TextView) findViewById(R.id.available_text);
        backarrow = (ImageView) findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.appcolor);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTransportOrdersList();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        getTransportOrdersList();
    }

    public void progressdialog() {
        progressDialog = new ProgressDialog(LogisticsOrdersActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    public void getTransportOrdersList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TransportApi service = retrofit.create(TransportApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getLogisticsOrders("https://www.sustowns.com/freight/transportBuyerTransportList/"+user_id);// user_id
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        System.out.println("----------------------------------------------------");
                        Log.d("Call request", call.request().toString());
                        Log.d("Call request header", call.request().headers().toString());
                        Log.d("Response raw header", response.headers().toString());
                        Log.d("Response raw", String.valueOf(response.raw().body()));
                        Log.d("Response code", String.valueOf(response.code()));

                        System.out.println("----------------------------------------------------");

                        if (response.body().toString() != null) {

                            if (response != null) {
                                String searchResponse = response.body().toString();
                                Log.d("Categeries", "response  >>" + searchResponse.toString());

                                if (searchResponse != null) {
                                    JSONObject root = null;
                                    try {
                                        root = new JSONObject(searchResponse);
                                        String success = root.getString("success");
                                        if (success.equalsIgnoreCase("1")) {
                                            JSONArray logistic_ordersArray = root.getJSONArray("logistic orders");
                                            transportDetailsList = new ArrayList<>();
                                            for (int i = 0; i < logistic_ordersArray.length(); i++) {
                                                JSONObject jsonObject = logistic_ordersArray.getJSONObject(i);

                                                String quantity = jsonObject.getString("quantity");
                                                String invoice_no = jsonObject.getString("invoice_no");
                                                String order_date = jsonObject.getString("order_date");
                                                String pr_sku = jsonObject.getString("pr_sku");
                                                String pr_title = jsonObject.getString("pr_title");
                                                String pr_packtype = jsonObject.getString("pr_packtype");
                                                String transport_orderid = jsonObject.getString("transport_orderid");
                                                String order_id = jsonObject.getString("order_id");
                                                String service_id = jsonObject.getString("service_id");
                                                String order_ranid = jsonObject.getString("order_ranid");
                                                String driver_name = jsonObject.getString("driver_name");
                                                String driver_number = jsonObject.getString("driver_number");
                                                String vehicle_number = jsonObject.getString("vehicle_number");
                                                String ntotal_charge = jsonObject.getString("ntotal_charge");
                                                String skm_range = jsonObject.getString("skm_range");
                                                String quote_charge_per_km = jsonObject.getString("quote_charge_per_km");
                                                String stotal_charge = jsonObject.getString("stotal_charge");
                                                String trans_status = jsonObject.getString("trans_status");
                                                String manual_automatic = jsonObject.getString("manual_automatic");
                                                String paytype = jsonObject.getString("paytype");
                                                String bank_thr_ran_id = jsonObject.getString("bank_thr_ran_id");
                                                String payment_status = jsonObject.getString("payment_status");
                                                String min_charge = jsonObject.getString("min_charge");
                                                String service_name = jsonObject.getString("service_name");

                                                TransportDetailsModel transportDetailsModel = new TransportDetailsModel();
                                                transportDetailsModel.setQuantity(quantity);
                                                transportDetailsModel.setInvoice_no(invoice_no);
                                                transportDetailsModel.setOrder_date(order_date);
                                                transportDetailsModel.setPr_sku(pr_sku);
                                                transportDetailsModel.setPr_title(pr_title);
                                                transportDetailsModel.setPr_packtype(pr_packtype);
                                                transportDetailsModel.setTransport_orderid(transport_orderid);
                                                transportDetailsModel.setOrder_id(order_id);
                                                transportDetailsModel.setService_id(service_id);
                                                transportDetailsModel.setOrder_ranid(order_ranid);
                                                transportDetailsModel.setDriver_name(driver_name);
                                                transportDetailsModel.setDriver_number(driver_number);
                                                transportDetailsModel.setVehicle_number(vehicle_number);
                                                transportDetailsModel.setNtotal_charge(ntotal_charge);
                                                transportDetailsModel.setSkm_range(skm_range);
                                                transportDetailsModel.setQuote_charge_per_km(quote_charge_per_km);
                                                transportDetailsModel.setStotal_charge(stotal_charge);
                                                transportDetailsModel.setTrans_status(trans_status);
                                                transportDetailsModel.setManual_automatic(manual_automatic);
                                                transportDetailsModel.setPaytype(paytype);
                                                transportDetailsModel.setBank_thr_ran_id(bank_thr_ran_id);
                                                transportDetailsModel.setPayment_status(payment_status);
                                                transportDetailsModel.setMin_charge(min_charge);
                                                transportDetailsModel.setService_name(service_name);
                                                transportDetailsList.add(transportDetailsModel);
                                            }
                                            if (transportDetailsList.isEmpty()) {
                                                recycler_view_logistics.setVisibility(View.GONE);
                                                available_text.setVisibility(View.VISIBLE);
                                            } else {
                                                recycler_view_logistics.setVisibility(View.VISIBLE);
                                                available_text.setVisibility(View.GONE);
                                                logisticsOrdersAdapter = new LogisticsOrdersAdapter(LogisticsOrdersActivity.this, transportDetailsList);
                                                recycler_view_logistics.setAdapter(logisticsOrdersAdapter);
                                                logisticsOrdersAdapter.notifyDataSetChanged();

                                            }
                                            // Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                }

                            }
                        }
                    } else {
                        //  Toast.makeText(CartActivity.this, "Cart is not added", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
//                Toast.makeText(ProductDetailsActivity.this, "Server not responding", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    public void viewDetails(int position) {
        serviceId = transportDetailsList.get(position).getService_id();
        orderId = transportDetailsList.get(position).getOrder_ranid();
        try {
            final android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(LogisticsOrdersActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alert_transport_view_details1, null);
            dialogBuilder.setView(dialogView);
            invoice_no = dialogView.findViewById(R.id.invoice_no);
            ll_transport_details = dialogView.findViewById(R.id.ll_transport_details);
            ll_transport_details.setVisibility(View.VISIBLE);
            ll_view_contact_details = dialogView.findViewById(R.id.ll_view_contact_details);
            ll_view_contact_details.setVisibility(View.GONE);
            order_prod_name = dialogView.findViewById(R.id.order_prod_name);
            order_weight = dialogView.findViewById(R.id.order_weight);
            order_quantity = dialogView.findViewById(R.id.order_quantity);
            order_packing_type = dialogView.findViewById(R.id.order_packing_type);
            order_vehicle_type = dialogView.findViewById(R.id.order_vehicle_type);
            order_load_type = dialogView.findViewById(R.id.order_load_type);
            order_charge_km = dialogView.findViewById(R.id.order_charge_km);
            order_quote = dialogView.findViewById(R.id.order_quote);
            order_min_charge = dialogView.findViewById(R.id.order_min_charge);
            shipping_name = dialogView.findViewById(R.id.shipping_name);
            shipping_number = dialogView.findViewById(R.id.shipping_number);
            shipping_address = dialogView.findViewById(R.id.shipping_address);
            shipping_postalcode = dialogView.findViewById(R.id.shipping_postalcode);
            seller_name_text = dialogView.findViewById(R.id.seller_name);
            seller_number_text = dialogView.findViewById(R.id.seller_number);
            seller_address_text = dialogView.findViewById(R.id.seller_address);
            seller_postalcode_text = dialogView.findViewById(R.id.seller_postalcode);
            Button order_close = dialogView.findViewById(R.id.order_close);
            order_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            getTransportDetailsList();
            alertDialog = dialogBuilder.create();
            try {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = alertDialog.getWindow();
                lp.copyFrom(window.getAttributes());
                // This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void viewContactDetails(int position) {
        try {
            final android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(LogisticsOrdersActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.view_transport_contact_details_dialog, null);
            dialogBuilder.setView(dialogView);
            driver_name = dialogView.findViewById(R.id.driver_name);
            driver_number = dialogView.findViewById(R.id.driver_number);
            vehicle_number = dialogView.findViewById(R.id.vehicle_number);
            driver_name.setText(transportDetailsList.get(position).getDriver_name());
            driver_number.setText(transportDetailsList.get(position).getDriver_number());
            vehicle_number.setText(transportDetailsList.get(position).getVehicle_number());
            Button order_close = dialogView.findViewById(R.id.close_btn);
            order_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            getTransportDetailsList();
            alertDialog = dialogBuilder.create();
            try {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = alertDialog.getWindow();
                lp.copyFrom(window.getAttributes());
                // This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*
    public void QuoteDetails(int position) {
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LogisticsOrdersActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.quote_received_details, null);
            dialogBuilder.setView(dialogView);

            ImageView close_send_quote = dialogView.findViewById(R.id.close_send_quote);
            TextView km_range = dialogView.findViewById(R.id.km_range);
            TextView charge_per_km = dialogView.findViewById(R.id.charge_per_km);
            TextView total_charge = dialogView.findViewById(R.id.total_charge);
            TextView amount_tobe_paid = dialogView.findViewById(R.id.amount_tobe_paid);
            Button submit_sendquote = dialogView.findViewById(R.id.submit_sendquote);

            km_range.setText(transportDetailsList.get(position).getSkm_range());
            charge_per_km.setText(transportDetailsList.get(position).getQuote_charge_per_km());
            if(transportDetailsList.get(position).getStotal_charge().equalsIgnoreCase("null")) {
                total_charge.setText(transportDetailsList.get(position).getNtotal_charge());
            }else{
                total_charge.setText(transportDetailsList.get(position).getStotal_charge());
            }
            if(transportDetailsList.get(position).getStotal_charge().equalsIgnoreCase("null")) {
                amount_tobe_paid.setText(transportDetailsList.get(position).getNtotal_charge());
            }else{
                amount_tobe_paid.setText(transportDetailsList.get(position).getStotal_charge());
            }
            close_send_quote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog = dialogBuilder.create();
            try {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = alertDialog.getWindow();
                lp.copyFrom(window.getAttributes());
                // This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    public void getTransportDetailsList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TransportApi service = retrofit.create(TransportApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getTransportOrdersDetails(orderId, serviceId);
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        System.out.println("----------------------------------------------------");
                        Log.d("Call request", call.request().toString());
                        Log.d("Call request header", call.request().headers().toString());
                        Log.d("Response raw header", response.headers().toString());
                        Log.d("Response raw", String.valueOf(response.raw().body()));
                        Log.d("Response code", String.valueOf(response.code()));

                        System.out.println("----------------------------------------------------");

                        if (response.body().toString() != null) {
                            if (response != null) {
                                String searchResponse = response.body().toString();
                                Log.d("Categeries", "response  >>" + searchResponse.toString());
                                if (searchResponse != null) {
                                    JSONObject root = null;
                                    try {
                                        root = new JSONObject(searchResponse);
                                        String success = root.getString("success");
                                        String message = root.getString("message");
                                        if (success.equalsIgnoreCase("1")) {
                                            JSONObject orderitemObj = root.getJSONObject("orderitem");
                                            String product_order_id = orderitemObj.getString("product_order_id");
                                            String seller_name = orderitemObj.getString("seller_name");
                                            String seller_number = orderitemObj.getString("seller_number");
                                            String seller_address = orderitemObj.getString("seller_address");
                                            String seller_zipcode = orderitemObj.getString("seller_zipcode");
                                            String display_name = orderitemObj.getString("display_name");
                                            String bill_phone = orderitemObj.getString("bill_phone");
                                            String bill_address1 = orderitemObj.getString("bill_address1");
                                            String bill_zipcode = orderitemObj.getString("bill_zipcode");
                                            String service_id = orderitemObj.getString("service_id");
                                            String pr_title = orderitemObj.getString("pr_title");
                                            String pr_min = orderitemObj.getString("pr_min");
                                            String pr_packtype = orderitemObj.getString("pr_packtype");
                                            String category = orderitemObj.getString("category");
                                            String vehicle_type = orderitemObj.getString("vehicle_type");
                                            String unit_code = orderitemObj.getString("unit_code");

                                            JSONObject vehicleObj = root.getJSONObject("vehicle");
                                            String service_id1 = vehicleObj.getString("service_id");
                                            String qchrg_km = vehicleObj.getString("qchrg_km");
                                            String qminchrg_km = vehicleObj.getString("qminchrg_km");
                                            String vehicle_type1 = vehicleObj.getString("vehicle_type");
                                            String load = root.getString("load");

                                            invoice_no.setText(product_order_id);
                                            order_prod_name.setText(pr_title);
                                            order_quantity.setText(pr_min);
                                            order_packing_type.setText(pr_packtype);
                                            order_vehicle_type.setText(vehicle_type1);
                                            order_load_type.setText(load);
                                            order_charge_km.setText(qchrg_km);
                                            order_min_charge.setText(qminchrg_km);
                                            shipping_name.setText(display_name);
                                            shipping_number.setText(bill_phone);
                                            shipping_address.setText(bill_address1);
                                            shipping_postalcode.setText(bill_zipcode);
                                            seller_name_text.setText(seller_name);
                                            seller_number_text.setText(seller_number);
                                            seller_address_text.setText(seller_address);
                                            seller_postalcode_text.setText(seller_zipcode);
                                            progressDialog.dismiss();
                                        } else {
                                            Toast.makeText(LogisticsOrdersActivity.this, message, Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                }

                            }
                        }
                    } else {
                        //  Toast.makeText(CartActivity.this, "Cart is not added", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
//                Toast.makeText(ProductDetailsActivity.this, "Server not responding", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
// Logistics Adapter
    public class LogisticsOrdersAdapter extends RecyclerView.Adapter<LogisticsOrdersAdapter.ViewHolder> {
        Context context;
        LayoutInflater inflater;
        String user_email,pro_id,user_id,user_role,complete_amount_status,order_status;
        PreferenceUtils preferenceUtils;
        String[] order;
        ArrayList<TransportDetailsModel> transportDetailsModels;
        ProgressDialog progressDialog;

        public LogisticsOrdersAdapter(Context context, ArrayList<TransportDetailsModel> transportDetailsModels) {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.context = context;
            this.transportDetailsModels = transportDetailsModels;
        }
        @Override
        public LogisticsOrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_contract_orders_item, viewGroup, false);
            //  product_sale_activity.onItemClick(i);
            return new LogisticsOrdersAdapter.ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final LogisticsOrdersAdapter.ViewHolder viewHolder, final int position) {
            viewHolder.view_invoice_btn.setText("View Transport Details");
            viewHolder.ll_paymentstatus.setVisibility(View.GONE);
            if(transportDetailsModels.get(position) != null) {
                viewHolder.orderName.setText(transportDetailsModels.get(position).getPr_title());
                viewHolder.order_no.setText(transportDetailsModels.get(position).getInvoice_no());
                viewHolder.orderDate.setText(transportDetailsModels.get(position).getOrder_date());
                if(transportDetailsModels.get(position).getStotal_charge().equalsIgnoreCase("null")) {
                    viewHolder.order_price.setText(transportDetailsModels.get(position).getNtotal_charge());
                }else{
                    viewHolder.order_price.setText(transportDetailsModels.get(position).getStotal_charge());
                }
                order_status = transportDetailsModels.get(position).getTrans_status();
                if(order_status.equalsIgnoreCase("0") && transportDetailsModels.get(position).getManual_automatic().equalsIgnoreCase("automatic")){
                    viewHolder.ll_status.setVisibility(View.VISIBLE);
                    viewHolder.orderStatus.setText("Service Request Sent");
                }else if(order_status.equalsIgnoreCase("0") && transportDetailsModels.get(position).getManual_automatic().equalsIgnoreCase("manual")){
                    viewHolder.ll_status.setVisibility(View.VISIBLE);
                    viewHolder.orderStatus.setText("Quote Request Sent");
                }
                else if(order_status.equalsIgnoreCase("1")){
                    viewHolder.add_payment_btn.setVisibility(View.VISIBLE);
                    viewHolder.add_payment_btn.setText("Pay & Approve");
                    viewHolder.add_payment_btn.setBackgroundColor(getResources().getColor(R.color.btn_background_color));
                    viewHolder.add_transport_btn.setVisibility(View.VISIBLE);
                    viewHolder.add_transport_btn.setText("View Contact Details");
                    viewHolder.add_transport_btn.setBackgroundColor(getResources().getColor(R.color.yellow));
                    viewHolder.ll_status.setVisibility(View.GONE);
                    viewHolder.add_payment_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context, PaymentContractsActvity.class);
                            i.putExtra("Logistics","2");
                            i.putExtra("OrderRandId",transportDetailsModels.get(position).getOrder_ranid());
                            i.putExtra("ServiceId",transportDetailsModels.get(position).getService_id());
                            i.putExtra("TransportId",transportDetailsModels.get(position).getTransport_orderid());
                            if(transportDetailsModels.get(position).getStotal_charge().equalsIgnoreCase("null")) {
                                i.putExtra("Amount",transportDetailsModels.get(position).getNtotal_charge());
                            }else{
                                i.putExtra("Amount",transportDetailsModels.get(position).getStotal_charge());
                            }
                           context.startActivity(i);
                        }
                    });
                    viewHolder.add_transport_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((LogisticsOrdersActivity)context).viewContactDetails(position);
                        }
                    });
                }else if(order_status.equalsIgnoreCase("2")){
                    viewHolder.add_payment_btn.setVisibility(View.GONE);
                    viewHolder.add_transport_btn.setVisibility(View.GONE);
                    viewHolder.ll_status.setVisibility(View.VISIBLE);
                    viewHolder.orderStatus.setText("Rejected");
                }else if(order_status.equalsIgnoreCase("3") && transportDetailsModels.get(position).getManual_automatic().equalsIgnoreCase("manual")){
                    viewHolder.add_payment_btn.setVisibility(View.VISIBLE);
                    viewHolder.add_payment_btn.setText("Quote Received");
                    viewHolder.add_transport_btn.setVisibility(View.GONE);
                    viewHolder.ll_status.setVisibility(View.GONE);
                    viewHolder.add_payment_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context,PaymentContractsActvity.class);
                            i.putExtra("Logistics","1");
                            i.putExtra("OrderRandId",transportDetailsModels.get(position).getOrder_ranid());
                            i.putExtra("ServiceId",transportDetailsModels.get(position).getService_id());
                            i.putExtra("TransportId",transportDetailsModels.get(position).getTransport_orderid());
                            if(transportDetailsModels.get(position).getStotal_charge().equalsIgnoreCase("null")) {
                                i.putExtra("Amount",transportDetailsModels.get(position).getNtotal_charge());
                            }else{
                                i.putExtra("Amount",transportDetailsModels.get(position).getStotal_charge());
                            }
                            i.putExtra("KmRange",transportDetailsList.get(position).getSkm_range());
                            i.putExtra("ChargePerKm",transportDetailsList.get(position).getQuote_charge_per_km());
                           // i.putExtra("TotalCharge",transportDetailsList.get(position).getC)
                            context.startActivity(i);
                            //((LogisticsOrdersActivity)context).QuoteDetails(position);
                        }
                    });
                }else if(order_status.equalsIgnoreCase("4")){
                    viewHolder.add_payment_btn.setVisibility(View.GONE);
                    viewHolder.add_transport_btn.setVisibility(View.GONE);
                    viewHolder.ll_status.setVisibility(View.VISIBLE);
                    viewHolder.orderStatus.setText("Quote Confirmed");
                }else if(order_status.equalsIgnoreCase("5") && !transportDetailsModels.get(position).getBank_thr_ran_id().isEmpty()){
                    viewHolder.add_payment_btn.setVisibility(View.VISIBLE);
                    viewHolder.add_transport_btn.setVisibility(View.GONE);
                    viewHolder.ll_status.setVisibility(View.GONE);
                    viewHolder.add_payment_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context, AddPaymentActivity.class);
                            i.putExtra("OrderId",transportDetailsModels.get(position).getTransport_orderid());
                            i.putExtra("BankRandId",transportDetailsModels.get(position).getBank_thr_ran_id());
                            i.putExtra("RandId",transportDetailsModels.get(position).getOrder_ranid());
                            i.putExtra("Status",transportDetailsModels.get(position).getPayment_status());
                            i.putExtra("ContractName",transportDetailsModels.get(position).getService_name());
                            if(transportDetailsModels.get(position).getStotal_charge().equalsIgnoreCase("null")) {
                                i.putExtra("Amount",transportDetailsModels.get(position).getNtotal_charge());
                            }else{
                                i.putExtra("Amount",transportDetailsModels.get(position).getStotal_charge());
                            }
                            i.putExtra("Date",transportDetailsModels.get(position).getOrder_date());
                            i.putExtra("ContractOrders","0");
                            context.startActivity(i);
                        }
                    });
                }
                else if(order_status.equalsIgnoreCase("7") && transportDetailsModels.get(position).getManual_automatic().equalsIgnoreCase("automatic")){
                    viewHolder.ll_status.setVisibility(View.VISIBLE);
                    viewHolder.orderStatus.setText("Payment Paid");
                }
                else{
                    viewHolder.ll_status.setVisibility(View.VISIBLE);
                    viewHolder.orderStatus.setText("Pending");
                }
            }
            viewHolder.view_invoice_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((LogisticsOrdersActivity) context).viewDetails(position);
                }
            });
        }
        @Override
        public int getItemCount() {
            return transportDetailsModels.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView,remove_product,increase;
            TextView orderName,order_no,orderDate,orderStatus,order_price;
            Button add_payment_btn,add_transport_btn,view_invoice_btn;
            LinearLayout ll_status,ll_paymentstatus;
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
                ll_status = (LinearLayout) view.findViewById(R.id.ll_status);
                ll_paymentstatus = (LinearLayout) view.findViewById(R.id.ll_paymentstatus);
            }
        }
    }

}
