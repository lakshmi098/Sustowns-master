package com.sustown.sustownsapp.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sustownsapp.R;
import com.google.gson.JsonElement;
import com.sustown.sustownsapp.Adapters.MyContractOrdersAdapter;
import com.sustown.sustownsapp.Api.DZ_URL;
import com.sustown.sustownsapp.Api.PostContractsApi;
import com.sustown.sustownsapp.Models.ContractPurchasesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyContractOrdersActivity extends AppCompatActivity {
    RecyclerView recycler_view_orders;
    MyContractOrdersAdapter myContractOrdersAdapter;
    String[] order = {"Order 1","Order 2","Order 3"};
    ImageView backarrow;
    ProgressDialog progressDialog;
    ArrayList<ContractPurchasesModel> contractOrderModels;
    TextView available_text;
    PreferenceUtils preferenceUtils;
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_contract_orders);
        preferenceUtils = new PreferenceUtils(MyContractOrdersActivity.this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");

        backarrow = (ImageView) findViewById(R.id.backarrow);
        recycler_view_orders = (RecyclerView) findViewById(R.id.recycler_view_orders);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recycler_view_orders.setLayoutManager(linearLayoutManager);
        available_text = (TextView) findViewById(R.id.available_text);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getContractOrdersList();

    }


    public void progressdialog() {
        progressDialog = new ProgressDialog(MyContractOrdersActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    public void getContractOrdersList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostContractsApi service = retrofit.create(PostContractsApi.class);

        Call<JsonElement> callRetrofit = null;
       // callRetrofit = service.getContractsOrders("445");// user_id
        callRetrofit = service.getContractsOrders(user_id);// user_id
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
                                        String message = root.getString("message");
                                        String success = root.getString("success");
                                        if (success.equalsIgnoreCase("1")) {
                                            JSONArray ordersArray = root.getJSONArray("joborderitem");
                                            contractOrderModels = new ArrayList<>();
                                            for (int i = 0; i < ordersArray.length(); i++) {
                                                JSONObject jsonObject = ordersArray.getJSONObject(i);

                                                String id = jsonObject.getString("id");
                                                String user_id = jsonObject.getString("user_id");
                                                String order_id = jsonObject.getString("order_id");
                                                String job_id = jsonObject.getString("job_id");
                                                String pay_type = jsonObject.getString("pay_type");
                                                String quantity = jsonObject.getString("quantity");
                                                String totalprice = jsonObject.getString("totalprice");
                                                String invoice_no = jsonObject.getString("invoice_no");
                                                String order_status = jsonObject.getString("order_status");
                                                String order_date = jsonObject.getString("order_date");
                                                String job_name = jsonObject.getString("job_name");

                                                ContractPurchasesModel contractOrderModel = new ContractPurchasesModel();
                                                contractOrderModel.setId(id);
                                                contractOrderModel.setUser_id(user_id);
                                                contractOrderModel.setOrder_id(order_id);
                                                contractOrderModel.setJob_id(job_id);
                                                contractOrderModel.setPay_type(pay_type);
                                                contractOrderModel.setQuantity(quantity);
                                                contractOrderModel.setTotalprice(totalprice);
                                                contractOrderModel.setInvoice_no(invoice_no);
                                                contractOrderModel.setOrder_status(order_status);
                                                contractOrderModel.setOrder_date(order_date);
                                                contractOrderModel.setJob_name(job_name);
                                                contractOrderModels.add(contractOrderModel);
                                            }
                                            if (contractOrderModels.isEmpty()) {
                                                recycler_view_orders.setVisibility(View.GONE);
                                                available_text.setVisibility(View.VISIBLE);
                                            } else {
                                                recycler_view_orders.setVisibility(View.VISIBLE);
                                                available_text.setVisibility(View.GONE);
                                                myContractOrdersAdapter = new MyContractOrdersAdapter(MyContractOrdersActivity.this,contractOrderModels);
                                                recycler_view_orders.setAdapter(myContractOrdersAdapter);
                                                myContractOrdersAdapter.notifyDataSetChanged();

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
}
