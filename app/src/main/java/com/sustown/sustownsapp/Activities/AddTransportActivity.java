package com.sustown.sustownsapp.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sustownsapp.R;
import com.google.gson.JsonElement;
import com.sustown.sustownsapp.Adapters.AddTransportAdapter;
import com.sustown.sustownsapp.Api.DZ_URL;
import com.sustown.sustownsapp.Api.TransportApi;
import com.sustown.sustownsapp.Models.TransportServicesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddTransportActivity extends AppCompatActivity {

    Spinner spinner_freight_type;
    EditText category_transport, product_name_transport, weight_transport, packingtype_transport, seller_country_transport, drop_country_transport,
            seller_location_transport, drop_location_transport;
    Button pickupfrom_date, pickupto_date, request_transport_btn;
    String[] type = {"Road"};
    String[] name = {"Oganic Eggs", "Agro Eggs", "White Eggs"};
    String freight_type_st, orderId, invoiceNo;
    PreferenceUtils preferenceUtils;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    TextView transport_services_text;
    RecyclerView transport_recyclerview;
    LinearLayout ll_transport_services;
    AddTransportAdapter addTransportAdapter;
    List<TransportServicesModel> transportServicesList;
    ImageView backarrow;
    Intent intent;
    ProgressDialog progressDialog;
    String pickupDateFrom = "", pickupDateto = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_transport);
        try {
            initializeValues();
            initializeUI();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void initializeValues() {
        preferenceUtils = new PreferenceUtils(AddTransportActivity.this);
        intent = getIntent();
        orderId = intent.getStringExtra("OrderId");
        invoiceNo = intent.getStringExtra("InvoiceNo");

       // invoiceNo = "1515192782050819";
       // orderId = "2200113";
    }

    private void initializeUI() {
        try {
            transport_services_text = (TextView) findViewById(R.id.transport_services_text);
            backarrow = (ImageView) findViewById(R.id.backarrow);
            ll_transport_services = (LinearLayout) findViewById(R.id.ll_transport_services);
            spinner_freight_type = (Spinner) findViewById(R.id.spinner_freight_type);
            category_transport = (EditText) findViewById(R.id.category_transport);
            product_name_transport = (EditText) findViewById(R.id.product_name_transport);
            weight_transport = (EditText) findViewById(R.id.weight_transport);
            packingtype_transport = (EditText) findViewById(R.id.packingtype_transport);
            seller_country_transport = (EditText) findViewById(R.id.seller_country_transport);
            drop_country_transport = (EditText) findViewById(R.id.drop_country_transport);
            seller_location_transport = (EditText) findViewById(R.id.seller_location_transport);
            drop_location_transport = (EditText) findViewById(R.id.drop_location_transport);
            pickupfrom_date = (Button) findViewById(R.id.pickupfrom_date);
            pickupto_date = (Button) findViewById(R.id.pickupto_date);
            request_transport_btn = (Button) findViewById(R.id.request_transport_btn);
            transport_recyclerview = (RecyclerView) findViewById(R.id.transport_recyclerview);
            LinearLayoutManager layoutManager = new LinearLayoutManager(AddTransportActivity.this, LinearLayoutManager.VERTICAL, false);
            transport_recyclerview.setLayoutManager(layoutManager);
            pickupfrom_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cal = Calendar.getInstance();
                    day = cal.get(Calendar.DAY_OF_MONTH);
                    month = cal.get(Calendar.MONTH);
                    year = cal.get(Calendar.YEAR);
                    DateDialog();

                }
            });
            pickupto_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cal = Calendar.getInstance();
                    day = cal.get(Calendar.DAY_OF_MONTH);
                    month = cal.get(Calendar.MONTH);
                    year = cal.get(Calendar.YEAR);
                    DateDialog1();

                }
            });

            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, type);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_freight_type.setAdapter(aa);
            spinner_freight_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    freight_type_st = parent.getItemAtPosition(position).toString();
                    preferenceUtils.saveString(PreferenceUtils.Freight_Type, freight_type_st);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            request_transport_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ll_transport_services.setVisibility(View.VISIBLE);
                    request_transport_btn.setVisibility(View.GONE);
                    getAddTransportServices();
                }
            });
            backarrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            getAddTransportServices();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
       /* Intent intent = new Intent(AddTransportActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
    }

    public void DateDialog() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String month_string = "";
                int monthValue = monthOfYear + 1;
                // Month is 0 based so add 1
                if (String.valueOf(monthValue).length() == 1) {
                    month_string = "0" + (monthValue);
                } else {
                    month_string = String.valueOf(monthValue);
                }

                String day_string = "";
                // Month is 0 based so add 1
                if (String.valueOf(dayOfMonth).length() == 1) {
                    day_string = "0" + (dayOfMonth);
                } else {
                    day_string = String.valueOf(dayOfMonth);
                }

                pickupfrom_date.setText(month_string + "/" +day_string + "/" + year);
                pickupDateFrom = month_string + "/" +day_string + "/" + year;

                request_transport_btn.setVisibility(View.VISIBLE);
                ll_transport_services.setVisibility(View.GONE);
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dpDialog.show();

    }

    public void DateDialog1() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String month_string = "";
                int monthValue = monthOfYear + 1;
                // Month is 0 based so add 1
                if (String.valueOf(monthValue).length() == 1) {
                    month_string = "0" + (monthValue);
                } else {
                    month_string = String.valueOf(monthValue);
                }

                String day_string = "";
                // Month is 0 based so add 1
                if (String.valueOf(dayOfMonth).length() == 1) {
                    day_string = "0" + (dayOfMonth);
                } else {
                    day_string = String.valueOf(dayOfMonth);
                }

                pickupto_date.setText(month_string + "/" + (day_string) + "/" + year);
                pickupDateto = month_string + "/" + (day_string) + "/" + year;

                request_transport_btn.setVisibility(View.VISIBLE);
                ll_transport_services.setVisibility(View.GONE);
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dpDialog.show();

    }

    public void progressdialog() {
        progressDialog = new ProgressDialog(AddTransportActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    public void getAddTransportServices() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final TransportApi service = retrofit.create(TransportApi.class);

        Call<JsonElement> callRetrofit = null;
//        callRetrofit = service.addTransportService(invoiceNo, orderId, pickupDateFrom, pickupDateto);
          callRetrofit = service.addTransportService("978905357041119", "22001186", "2019-11-08", "2019-11-09");
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
                                        //   String message = root.getString("message");
                                        String status = root.getString("status");
                                        if (status.equalsIgnoreCase("success")) {
                                            JSONObject jsonObject = root.getJSONObject("order_transport_info");
                                            String product_category = jsonObject.getString("product_category");
                                            String product_name = jsonObject.getString("product_name");
                                            String weight = jsonObject.getString("weight");
                                            String packing_type = jsonObject.getString("packing_type");
                                            String seller_country = jsonObject.getString("seller_country");
                                            String seller_pin = jsonObject.getString("seller_pin");
                                            String buyer_pin = jsonObject.getString("buyer_pin");
                                            String pickupfrom = jsonObject.getString("pickupfromdate");
                                            String pickuptodate = jsonObject.getString("pickuptodate");
                                            if (pickupfrom.isEmpty()) {
                                                request_transport_btn.setVisibility(View.VISIBLE);
                                                ll_transport_services.setVisibility(View.GONE);
                                            } else {
                                                request_transport_btn.setVisibility(View.GONE);
                                                ll_transport_services.setVisibility(View.VISIBLE);
                                            }

                                            category_transport.setText(product_category);
                                            product_name_transport.setText(product_name);
                                            weight_transport.setText(weight);
                                            packingtype_transport.setText(packing_type);
                                            seller_country_transport.setText(seller_country);
                                            //drop_country_transport.setText();
                                            seller_location_transport.setText(seller_pin);
                                            drop_location_transport.setText(buyer_pin);
                                            pickupfrom_date.setText(pickupfrom);
                                            pickupto_date.setText(pickuptodate);
                                            String getkms = root.getString("getkms");
                                            transportServicesList = new ArrayList<>();
                                            JSONArray servicesArray = root.getJSONArray("transport_services");
                                            if (servicesArray.length() > 0) {
                                                for (int i = 0; i < servicesArray.length(); i++) {
                                                    JSONObject serviceObject = servicesArray.getJSONObject(i);

                                                    TransportServicesModel transportServicesModel = new TransportServicesModel(
                                                            serviceObject.getString("transport_vendor_name"),
                                                            serviceObject.getString("service_name"),
                                                            serviceObject.getString("service_pincode"),
                                                            serviceObject.getString("distance"),
                                                            serviceObject.getString("service_area_radius"),
                                                            serviceObject.getString("radius_extended"),
                                                            serviceObject.getString("type_of_radius"),
                                                            serviceObject.getString("category"),
                                                            serviceObject.getString("rating"),
                                                            serviceObject.getString("transport_type"),
                                                            serviceObject.getString("vehicle_type"),
                                                            serviceObject.getString("load_type"),
                                                            serviceObject.getString("distance_inkms"),
                                                            serviceObject.getString("partial_charge_perkm"),
                                                            serviceObject.getString("partial_minimum_charge"),
                                                            serviceObject.getString("partial_total_price"),
                                                            serviceObject.getString("full_charge_perkm"),
                                                            serviceObject.getString("full_minimum_charge"),
                                                            serviceObject.getString("full_total_price"),
                                                            serviceObject.getString("docs"),
                                                            serviceObject.getString("service_id"),
                                                            serviceObject.getString("transport_user"),
                                                            serviceObject.getString("transport_booking_status"),
                                                            serviceObject.getString("buyer_uid")
                                                    );
                                                    transportServicesList.add(transportServicesModel);
                                                }
                                                setUpRecyclerView();
                                            } else {
                                                transport_services_text.setText("Transport Services are not available");
                                                Toast.makeText(AddTransportActivity.this, "No transport services available for the selected date.", Toast.LENGTH_SHORT).show();
                                            }

                                            progressDialog.dismiss();
                                        } else {
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

    public void cancelBooking(int position) {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TransportApi service = retrofit.create(TransportApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.cancelBooking(transportServicesList.get(position).getBuyer_uid(),orderId,transportServicesList.get(position).getService_id());
      //  callRetrofit = service.cancelBooking("524","220011414","4");
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
                                        //   String message = root.getString("message");
                                        String status = root.getString("status");
                                        if (status.equalsIgnoreCase("success")) {
                                            Toast.makeText(AddTransportActivity.this, "success", Toast.LENGTH_SHORT).show();
                                        } else {
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


    private void setUpRecyclerView() {
        addTransportAdapter = new AddTransportAdapter(AddTransportActivity.this, transportServicesList);
        transport_recyclerview.setAdapter(addTransportAdapter);
        transport_recyclerview.setVisibility(View.VISIBLE);
        addTransportAdapter.notifyDataSetChanged();
    }

    public void bookNow(final int position, final AddTransportAdapter.ViewHolder viewHolder) {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TransportApi service = retrofit.create(TransportApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getTransportBookService(invoiceNo, orderId, transportServicesList.get(position).getTransport_user(),
               transportServicesList.get(position).getTransport_user(), transportServicesList.get(position).getService_id(), transportServicesList.get(position).getFull_charge_perkm(),
                transportServicesList.get(position).getFull_minimum_charge(), transportServicesList.get(position).getFull_total_price());
        callRetrofit.enqueue(new Callback<JsonElement>() {

            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
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
                                            progressDialog.dismiss();
                                            Toast.makeText(AddTransportActivity.this, "Booking Exist", Toast.LENGTH_SHORT).show();
                                            try {
                                                addTransportAdapter.updateBookSuccess(position, viewHolder);
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        } else {
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
