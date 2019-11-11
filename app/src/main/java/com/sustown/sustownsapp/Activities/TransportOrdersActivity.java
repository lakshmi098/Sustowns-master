package com.sustown.sustownsapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.sustownsapp.R;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sustown.sustownsapp.Adapters.TransportReceivedOrdersAdapter;
import com.sustown.sustownsapp.Api.DZ_URL;
import com.sustown.sustownsapp.Api.TransportApi;
import com.sustown.sustownsapp.Api.WebServices;
import com.sustown.sustownsapp.Models.TransportOrdersModel;
import com.sustown.sustownsapp.Models.TransportOrdersModelObj;
import com.sustown.sustownsapp.helpers.Helper;
import com.sustown.sustownsapp.listeners.DataListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransportOrdersActivity extends AppCompatActivity implements DataListener {

    RecyclerView recycler_view_transportorders;
    ImageView backarrow, cart_img;
    PreferenceUtils preferenceUtils;
    String user_id,ordersValue,s_zipcode,b_zipcode,chargePerKm,getkms,getpricekms;
    ProgressDialog progressDialog;
    ArrayList<TransportOrdersModel> transportOrdersModels;
    TransportReceivedOrdersAdapter transportReceivedOrdersAdapter;
    TextView available_text,total_price,kilometers_text;
    Helper helper;
    AlertDialog alertDialog;
    WebServices webServices;
    Button radius_btn,extended_radius_btn;
    EditText charge_per_km;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_transport_orders);
        preferenceUtils = new PreferenceUtils(TransportOrdersActivity.this);
        helper = new Helper(this);
        webServices = new WebServices(this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
        ordersValue = "Radius";

        available_text = (TextView) findViewById(R.id.available_text);
        cart_img = (ImageView) findViewById(R.id.cart_img);
        backarrow = (ImageView) findViewById(R.id.backarrow);
        recycler_view_transportorders = (RecyclerView) findViewById(R.id.recycler_view_transportorders);
        LinearLayoutManager layoutManager = new LinearLayoutManager(TransportOrdersActivity.this, LinearLayoutManager.VERTICAL, false);
        recycler_view_transportorders.setLayoutManager(layoutManager);
        cart_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransportOrdersActivity.this, CartActivity.class);
                startActivity(i);
            }
        });
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransportOrdersActivity.this, TradeManagementActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        radius_btn = (Button) findViewById(R.id.radius_btn);
        extended_radius_btn = (Button) findViewById(R.id.extended_radius_btn);
        radius_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordersValue = "Radius";
                radius_btn.setTextColor(getResources().getColor(R.color.white));
                extended_radius_btn.setTextColor(getResources().getColor(R.color.black));
                radius_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                extended_radius_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                getTransportOrdersList();
            }
        });
        extended_radius_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordersValue = "Extended Radius";
                extended_radius_btn.setTextColor(getResources().getColor(R.color.white));
                radius_btn.setTextColor(getResources().getColor(R.color.black));
                extended_radius_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                radius_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                getTransportOrdersList();
            }
        });
        getTransportOrdersList();
    }
    public void progressdialog() {
        progressDialog = new ProgressDialog(TransportOrdersActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
//  Recent get Transport Received Orders
    public void getTransportOrdersList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TransportApi service = retrofit.create(TransportApi.class);
        TransportOrdersModelObj transportOrdersModel1 = new TransportOrdersModelObj();
        if(ordersValue.equalsIgnoreCase("Radius")) {
            transportOrdersModel1.setTransport_userid(user_id);
            transportOrdersModel1.setManual_automatic("automatic"); // Radius
        }else if(ordersValue.equalsIgnoreCase("Extended Radius")){
            transportOrdersModel1.setTransport_userid(user_id);
            transportOrdersModel1.setManual_automatic("manual");// Extended Radius
        }
        Call<JsonObject> callRetrofit = null;
        callRetrofit = service.getTransportReceivedOrders(transportOrdersModel1);
        callRetrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
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
                                        String status = root.getString("status");
                                        String error = root.getString("error");
                                        if (error.equalsIgnoreCase("false")) {
                                            JSONObject responseObj = root.getJSONObject("response");
                                            JSONArray dataArray = responseObj.getJSONArray("data");
                                            transportOrdersModels = new ArrayList<>();
                                            for (int i = 0; i < dataArray.length(); i++) {
                                                JSONObject jsonObject = dataArray.getJSONObject(i);
                                                String id = jsonObject.getString("id");
                                                String user_id = jsonObject.getString("user_id");
                                                String order_id = jsonObject.getString("order_id");
                                                String service_name = jsonObject.getString("service_name");
                                                String pr_sku = jsonObject.getString("pr_sku");
                                                String pr_title = jsonObject.getString("pr_title");
                                                String pr_weight = jsonObject.getString("pr_weight");
                                                String pr_type = jsonObject.getString("pr_type");
                                                String invoice_no = jsonObject.getString("invoice_no");
                                                String order_date = jsonObject.getString("order_date");
                                                String quantity = jsonObject.getString("quantity");
                                                String trans_status = jsonObject.getString("trans_status");
                                                String trans_status_text = jsonObject.getString("trans_status_text");
                                                String manual_automatic = jsonObject.getString("manual_automatic");
                                                String confirm_allow = jsonObject.getString("confirm_allow");
                                                String quote_allow = jsonObject.getString("quote_allow");
                                                String seller_zipcode = jsonObject.getString("seller_zipcode");
                                                String buyer_zipcode = jsonObject.getString("buyer_zipcode");
                                                String chargeperkm = jsonObject.getString("chargeperkm");

                                                TransportOrdersModel transportOrderModel = new TransportOrdersModel();
                                                transportOrderModel.setId(id);
                                                transportOrderModel.setUser_id(user_id);
                                                transportOrderModel.setOrder_id(order_id);
                                                transportOrderModel.setService_name(service_name);
                                                transportOrderModel.setPr_sku(pr_sku);
                                                transportOrderModel.setPr_title(pr_title);
                                                transportOrderModel.setPr_weight(pr_weight);
                                                transportOrderModel.setPr_type(pr_type);
                                                transportOrderModel.setInvoice_no(invoice_no);
                                                transportOrderModel.setOrder_date(order_date);
                                                transportOrderModel.setQuantity(quantity);
                                                transportOrderModel.setTrans_status(trans_status);
                                                transportOrderModel.setTrans_status_text(trans_status_text);
                                                transportOrderModel.setManual_automatic(manual_automatic);
                                                transportOrderModel.setConfirm_allow(confirm_allow);
                                                transportOrderModel.setQuote_allow(quote_allow);
                                                transportOrderModel.setSeller_zipcode(seller_zipcode);
                                                transportOrderModel.setBuyer_zipcode(buyer_zipcode);
                                                transportOrderModel.setChargeperkm(chargeperkm);
                                                transportOrdersModels.add(transportOrderModel);
                                            }
                                            if (transportOrdersModels != null) {
                                                transportReceivedOrdersAdapter = new TransportReceivedOrdersAdapter(TransportOrdersActivity.this, transportOrdersModels, null);
                                                recycler_view_transportorders.setAdapter(transportReceivedOrdersAdapter);
                                                transportReceivedOrdersAdapter.notifyDataSetChanged();
                                                available_text.setVisibility(View.GONE);
                                            } else {
                                                recycler_view_transportorders.setVisibility(View.GONE);
                                                available_text.setVisibility(View.VISIBLE);
                                                available_text.setText("Orders Are Not Available");
                                            }
                                            // Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        } else {
                                            //    Toast.makeText(CareerOppurtunitiesActivity.this, message, Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Toast.makeText(ProductDetailsActivity.this, "Server not responding", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    // Old Transport Received Orders
/*
    public void getOldTransportOrdersList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TransportApi service = retrofit.create(TransportApi.class);

        Call<JsonObject> callRetrofit = null;
        callRetrofit = service.getTransportReceivedOrders("https://www.sustowns.com/freight/transportvendorordersList/"+user_id);
        //callRetrofit = service.getTransportReceivedOrders("https://www.sustowns.com/freight/transportvendorordersList/"+user_id);
        callRetrofit.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
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
                                        //   String message = root.getString("message");
                                        String status = root.getString("status");
                                        if (status.equalsIgnoreCase("success")) {
                                            JSONArray jsonArray = root.getJSONArray("data");
                                            transportOrdersModels = new ArrayList<>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                TransportOrdersModel transportOrderModel = new TransportOrdersModel(
                                                        jsonObject.getString("productId"),
                                                        jsonObject.getString("productCode"),
                                                        jsonObject.getString("productName"),
                                                        jsonObject.getString("orderWeight"),
                                                        jsonObject.getString("packtype"),
                                                        jsonObject.getString("orderType"),
                                                        jsonObject.getString("invoiceNo"),
                                                        jsonObject.getString("orderDate"),
                                                        jsonObject.getString("quantity"),
                                                        jsonObject.getString("trans_status"),
                                                        jsonObject.getString("orderRandomId"),
                                                        jsonObject.getString("serviceId")
                                                );
                                                transportOrdersModels.add(transportOrderModel);
                                            }
                                            if (transportOrdersModels != null) {
                                                transportReceivedOrdersAdapter = new TransportReceivedOrdersAdapter(TransportOrdersActivity.this, transportOrdersModels, null);
                                                recycler_view_transportorders.setAdapter(transportReceivedOrdersAdapter);
                                                transportReceivedOrdersAdapter.notifyDataSetChanged();
                                                available_text.setVisibility(View.GONE);
                                            } else {
                                                recycler_view_transportorders.setVisibility(View.GONE);
                                                available_text.setVisibility(View.VISIBLE);
                                                available_text.setText("Orders Are Not Available");
                                            }
                                            // Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        } else {
                                            //    Toast.makeText(CareerOppurtunitiesActivity.this, message, Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Toast.makeText(ProductDetailsActivity.this, "Server not responding", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
*/

    public void confirmOrder(final int position) {
        helper.showDialog(TransportOrdersActivity.this, SweetAlertDialog.WARNING_TYPE, "Are you sure?", "Do you want to confirm?",
                new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        showConfirmRejectDialog(true, position);
                    }
                }, new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
    }

    public void rejectOrder(final int position) {
        helper.showDialog(TransportOrdersActivity.this, SweetAlertDialog.WARNING_TYPE, "Are you sure?", "Do you want to reject?",
                new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        showConfirmRejectDialog(false, position);
                    }
                }, new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
    }
    public void viewDetails(int position) {
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TransportOrdersActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alert_transport_view_details, null);
            dialogBuilder.setView(dialogView);

            TextView invoice_no = dialogView.findViewById(R.id.invoice_no);
            TextView order_prod_name = dialogView.findViewById(R.id.order_prod_name);
            TextView order_weight = dialogView.findViewById(R.id.order_weight);
            TextView order_quantity = dialogView.findViewById(R.id.order_quantity);
            TextView order_packing_type = dialogView.findViewById(R.id.order_packing_type);
            TextView order_shipped_to = dialogView.findViewById(R.id.order_shipped_to);
            TextView order_seller_address = dialogView.findViewById(R.id.order_seller_address);
            Button order_close = dialogView.findViewById(R.id.order_close);

          /*  invoice_no.setText(transportOrdersModels.get(position).getInvoice_no());
            order_prod_name.setText(transportOrdersModels.get(position).getProductName());
            order_weight.setText(transportOrdersModels.get(position).getOrderWeight());
            order_quantity.setText(transportOrdersModels.get(position).getQuantity());
            order_packing_type.setText(transportOrdersModels.get(position).getPacktype());*/
            order_shipped_to.setText("test"+"\n"+"9987456321"+"\n"+"Hyderabad"+"\n"+"500045");
            order_seller_address.setText("test"+"\n"+"9987456321"+"\n"+"Hyderabad"+"\n"+"500045");
            /* order_shipped_to.setText(transportDetailsList.get(position).getDisplay_name()+"\n"+
                    transportDetailsList.get(position).getPay_phone()+"\n"+
                    transportDetailsList.get(position).getPay_address1()+"\n"+
                    transportDetailsList.get(position).getPay_zipcode());

            order_seller_address.setText(transportDetailsList.get(position).getSeller_name()+"\n"+
                    transportDetailsList.get(position).getSeller_number()+"\n"+
                    transportDetailsList.get(position).getSeller_address()+"\n"+
                    transportDetailsList.get(position).getSeller_zipcode());*/
           // invoice_no.setText(transportDetailsList.get(position).getInvoice_number());
            order_close.setOnClickListener(new View.OnClickListener() {
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

    public void sendQuoteDetails(int position) {
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TransportOrdersActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.send_quote_dialog, null);
            dialogBuilder.setView(dialogView);

            ImageView close_send_quote = dialogView.findViewById(R.id.close_send_quote);
            TextView shipping_pincode = dialogView.findViewById(R.id.shipping_pincode);
            TextView delivery_pincode = dialogView.findViewById(R.id.delivery_pincode);
            kilometers_text = dialogView.findViewById(R.id.kilometers_text);
            total_price = dialogView.findViewById(R.id.total_price);
            charge_per_km = dialogView.findViewById(R.id.charge_per_km);
            charge_per_km.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    getKmsBasedPincodes();
                }
            });
            Button submit_sendquote = dialogView.findViewById(R.id.submit_sendquote);

            s_zipcode = transportOrdersModels.get(position).getSeller_zipcode();
            b_zipcode = transportOrdersModels.get(position).getBuyer_zipcode();
            chargePerKm = transportOrdersModels.get(position).getChargeperkm();
            shipping_pincode.setText(s_zipcode);
            delivery_pincode.setText(b_zipcode);
            charge_per_km.setText(chargePerKm);
           // kilometers_text.setText(transportOrdersModels.get(position).getQuantity());
           // total_price.setText(transportOrdersModels.get(position).getPacktype());
            getKmsBasedPincodes();
            submit_sendquote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitSendQuoteJsonObject();
                }
            });
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

    public void getKmsBasedPincodes() {
        //progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TransportApi service = retrofit.create(TransportApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getKmsBasePincodes("534427","534425",charge_per_km.getText().toString());
       // callRetrofit = service.getKmsBasePincodes(s_zipcode,b_zipcode,charge_per_km.getText().toString());
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                       // progressDialog.dismiss();
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
                                        if(success.equalsIgnoreCase("1")) {
                                             getkms = root.getString("getkms");
                                             getpricekms = root.getString("getpricekms");
                                             kilometers_text.setText(getkms);
                                             total_price.setText(getpricekms);
                                        }
                                        else {
                                            //    Toast.makeText(CareerOppurtunitiesActivity.this, message, Toast.LENGTH_SHORT).show();
                                           // progressDialog.dismiss();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                   // progressDialog.dismiss();
                                }

                            }
                        }
                    } else {
                        //  Toast.makeText(CartActivity.this, "Cart is not added", Toast.LENGTH_SHORT).show();
                       // progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
//                Toast.makeText(ProductDetailsActivity.this, "Server not responding", Toast.LENGTH_SHORT).show();
                //progressDialog.dismiss();
            }
        });
    }
    public void submitSendQuoteJsonObject() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("ccost",charge_per_km.getText().toString());
            jsonObj.put("km",getkms);
            jsonObj.put("total",getpricekms);
            jsonObj.put("oid", "22001187");
            jsonObj.put("sid", "69");
            jsonObj.put("userid", "502");

            androidNetworkingSubmitQuote(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingSubmitQuote(JSONObject jsonObject){
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Transportservices/sendquotesubmit/")
                .addJSONObjectBody(jsonObject) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "JSON : " + response);
                        try {
                            String message = response.getString("message");
                            if (message.equalsIgnoreCase("Quote Submitted successfully")) {
                                progressDialog.dismiss();
                                Toast.makeText(TransportOrdersActivity.this, message, Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            } else {
                                Toast.makeText(TransportOrdersActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                        progressDialog.dismiss();
                    }
                });
    }

    private void showConfirmRejectDialog(final boolean isConfirm, final int position) {
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TransportOrdersActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.confirm_trans_order_dialog, null);
            dialogBuilder.setView(dialogView);

            final EditText driver_name = dialogView.findViewById(R.id.driver_name);
            final EditText mobile_number = dialogView.findViewById(R.id.mobile_number);
            final EditText product_address = dialogView.findViewById(R.id.product_address);

            LinearLayout ll_reason_rejection = dialogView.findViewById(R.id.ll_reason_rejection);
            LinearLayout ll_confirm = dialogView.findViewById(R.id.ll_confirm);
            final EditText reason_rejection = dialogView.findViewById(R.id.reason_rejection);

            Button submit_button = dialogView.findViewById(R.id.submit_button);
            Button cancel_btn = dialogView.findViewById(R.id.cancel_btn);

            if (isConfirm) {
                ll_confirm.setVisibility(View.VISIBLE);
                ll_reason_rejection.setVisibility(View.GONE);
            } else {
                ll_confirm.setVisibility(View.GONE);
                ll_reason_rejection.setVisibility(View.VISIBLE);
            }

            submit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConfirm) {
                        if (driver_name.getText().toString().trim().isEmpty() || mobile_number.getText().toString().trim().isEmpty() ||
                                product_address.getText().toString().trim().isEmpty()) {

                            helper.singleClickAlert(TransportOrdersActivity.this, SweetAlertDialog.WARNING_TYPE, "", "All fields are mandatoryr",
                                    new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    });
                        } else {
                            helper.showLoader(TransportOrdersActivity.this, "Loading", "Please wait for a while");

                            try {
                                JSONObject confirmObject = new JSONObject();
                                confirmObject.put("userId", user_id);
                               /* confirmObject.put("orderRandomId", transportOrdersModels.get(position).getOrderRandomId());
                                confirmObject.put("serviceId", transportOrdersModels.get(position).getServiceId());*/
                                confirmObject.put("orderStatus", "confirm");
                                confirmObject.put("driverName", driver_name.getText().toString().trim());
                                confirmObject.put("conactNumber", mobile_number.getText().toString().trim());
                                confirmObject.put("vehicleNumber", product_address.getText().toString().trim());

                                webServices.postJsonBodyAndGetJsonObject(DZ_URL.POST_TRANSPORT_VENDOR_CONFIRM, confirmObject);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        if (reason_rejection.getText().toString().trim().isEmpty()) {
                            helper.singleClickAlert(TransportOrdersActivity.this, SweetAlertDialog.WARNING_TYPE, "", "All fields are mandatoryr",
                                    new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    });
                        } else {
                            helper.showLoader(TransportOrdersActivity.this, "Loading", "Please wait for a while");
                            try {
                                JSONObject rejectOrder = new JSONObject();
                                rejectOrder.put("userId", user_id);
                               /* rejectOrder.put("orderRandomId", transportOrdersModels.get(position).getOrderRandomId());
                                rejectOrder.put("serviceId", transportOrdersModels.get(position).getServiceId());*/
                                rejectOrder.put("orderStatus", "reject");
                                rejectOrder.put("comment", reason_rejection.getText().toString().trim());

                                webServices.postJsonBodyAndGetJsonObject(DZ_URL.POST_TRANSPORT_VENDOR_CONFIRM, rejectOrder);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
            cancel_btn.setOnClickListener(new View.OnClickListener() {
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
    @Override
    public void onDataRetrieved(Object data, String whichUrl) {
        helper.hideLoader();
        try {
            JSONObject response = (JSONObject) data;
            if (response.getString("status").equalsIgnoreCase("success")) {
                alertDialog.dismiss();
                helper.singleClickAlert(TransportOrdersActivity.this, SweetAlertDialog.SUCCESS_TYPE, "Success!", "Your request has submitted successfully.",
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });
            } else {
                alertDialog.dismiss();
            }
        } catch (Exception e) {
            alertDialog.dismiss();
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Object data) {
        helper.hideLoader();
        helper.singleClickAlert(TransportOrdersActivity.this, SweetAlertDialog.ERROR_TYPE, "Error!", "Something went wrong, Please try again later.",
                new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
    }
}
