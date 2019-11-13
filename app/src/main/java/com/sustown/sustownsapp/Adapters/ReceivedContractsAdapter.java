package com.sustown.sustownsapp.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.sustownsapp.R;
import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.payu.india.Extras.PayUChecksum;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.Payu;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.payuui.Activity.PayUBaseActivity;
import com.squareup.picasso.Picasso;
import com.sustown.sustownsapp.Activities.MyContractOrdersActivity;
import com.sustown.sustownsapp.Activities.MyProductContractActivity;
import com.sustown.sustownsapp.Activities.PaymentContractsActvity;
import com.sustown.sustownsapp.Activities.PreferenceUtils;
import com.sustown.sustownsapp.Activities.ShippingAddressActivity;
import com.sustown.sustownsapp.Activities.StoreReceivedOrdersActivity;
import com.sustown.sustownsapp.Api.DZ_URL;
import com.sustown.sustownsapp.Api.PostContractsApi;
import com.sustown.sustownsapp.Api.UserApi;
import com.sustown.sustownsapp.Models.ReceivedContractModel;
import com.sustown.sustownsapp.helpers.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class ReceivedContractsAdapter extends RecyclerView.Adapter<ReceivedContractsAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email,user_id,user_role,job_id,contractor_id,bid_id,status,paymentType,Instantamount,job_quote_id,jodId,quoted_id;
    PreferenceUtils preferenceUtils;
    ArrayList<ReceivedContractModel> receivedContractModels;
    ProgressDialog progressDialog;
    Helper helper;
    Button pay_now_btn;
    LinearLayout ll_bank_details;
    RadioButton paybank_radiobtn, payu_radiobutton,radioButton;
    ImageView close_payment_dialog;
    RadioGroup radioGroup;
    Integer selectedId;
    private String merchantKey = "swpahz", salt = "h7dXPGlF", transactionId, userCredentials;
    // These will hold all the payment parameters
    private PaymentParams mPaymentParams;
    // This sets the configuration
    private PayuConfig payuConfig;
    // Used when generating hash from SDK
    private PayUChecksum checksum;
    String bankCode,mihpayid,mode,txnid ,amount,email,net_amount_debit,firstname,phone,hash,payment_source,PG_TYPE,bank_ref_num;
    CardView received_orders_cardview;
    LinearLayout ll_order_placed_text;

    public ReceivedContractsAdapter(Context context, ArrayList<ReceivedContractModel> receivedContractModels) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.receivedContractModels = receivedContractModels;
        preferenceUtils = new PreferenceUtils(context);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,"");
        helper = new Helper(context);
        //TODO Must write below code in your activity to set up initial context for PayU
        Payu.setInstance(context);
        int environment = PayuConstants.PRODUCTION_ENV;
        payuConfig = new PayuConfig();
        payuConfig.setEnvironment(environment);
        mPaymentParams = new PaymentParams();
    }

    @Override
    public ReceivedContractsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.receivedorder_details_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ReceivedContractsAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ReceivedContractsAdapter.ViewHolder viewHolder, final int position) {
        if(receivedContractModels.get(position) != null){
            if(receivedContractModels.get(position).getImage().isEmpty() || receivedContractModels.get(position).getImage() == null){
                Picasso.get()
                        .load(R.drawable.no_image_available)
                        .placeholder(R.drawable.no_image_available)
                        .error(R.drawable.no_image_available)
                        .into(viewHolder.receivedorder_image);
            }else {
                Picasso.get()
                        .load(receivedContractModels.get(position).getImage())
                        .placeholder(R.drawable.no_image_available)
                        .error(R.drawable.no_image_available)
                        .into(viewHolder.receivedorder_image);
            }
            viewHolder.name_receivedcontract.setText(receivedContractModels.get(position).getBus_name());
            viewHolder.received_reviews.setText(receivedContractModels.get(position).getCount()+" Reviews");
            viewHolder.received_contract_description.setText(receivedContractModels.get(position).getDescription());
            status = receivedContractModels.get(position).getStatus();
            if(status.equalsIgnoreCase("5")) {
                viewHolder.payment_btn.setVisibility(View.VISIBLE);
                viewHolder.approve_btn.setVisibility(View.GONE);
                viewHolder.confirm_contract.setVisibility(View.GONE);
                viewHolder.received_status.setVisibility(View.GONE);
            }else if(status.equalsIgnoreCase("0")){
                viewHolder.payment_btn.setVisibility(View.GONE);
                viewHolder.approve_btn.setVisibility(View.VISIBLE);
                viewHolder.confirm_contract.setVisibility(View.GONE);
                viewHolder.received_status.setVisibility(View.GONE);
            }else if(status.equalsIgnoreCase("2")){
                viewHolder.payment_btn.setVisibility(View.GONE);
                viewHolder.approve_btn.setVisibility(View.GONE);
                viewHolder.confirm_contract.setVisibility(View.VISIBLE);
                viewHolder.received_status.setVisibility(View.GONE);
            }else if(status.equalsIgnoreCase("3")){
                viewHolder.payment_btn.setVisibility(View.GONE);
                viewHolder.approve_btn.setVisibility(View.GONE);
                viewHolder.confirm_contract.setVisibility(View.GONE);
                viewHolder.received_status.setVisibility(View.VISIBLE);
            }else{
                viewHolder.payment_btn.setVisibility(View.GONE);
                viewHolder.approve_btn.setVisibility(View.GONE);
                viewHolder.confirm_contract.setVisibility(View.GONE);
                viewHolder.received_status.setVisibility(View.GONE);
            }
            viewHolder.received_orders_payment.setText(receivedContractModels.get(position).getPayment());
            viewHolder.received_contract_amount.setText(receivedContractModels.get(position).getCurrency()+" "+receivedContractModels.get(position).getInstant_amount());
            viewHolder.received_contract_fullname.setText(receivedContractModels.get(position).getFullname());
            viewHolder.received_contract_email.setText(receivedContractModels.get(position).getEmail());
            viewHolder.received_contract_address.setText(receivedContractModels.get(position).getLocation());
            viewHolder.uploaded_document.setText(receivedContractModels.get(position).getAppattachment());
            viewHolder.received_contract_city.setText(receivedContractModels.get(position).getCity());
            viewHolder.received_contract_phone.setText(receivedContractModels.get(position).getPhone());
            viewHolder.received_contract_country.setText(receivedContractModels.get(position).getCountry());
            viewHolder.ratingBar.setRating(Float.parseFloat(receivedContractModels.get(position).getAvg()));
        }
        viewHolder.payment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Instantamount = receivedContractModels.get(position).getInstant_amount();
                job_quote_id = receivedContractModels.get(position).getId();
                quoted_id = receivedContractModels.get(position).getUser_id();
                jodId = receivedContractModels.get(position).getJob_id();
                Intent i = new Intent(context, PaymentContractsActvity.class);
                i.putExtra("Amount",Instantamount);
                i.putExtra("JobQuoteId",job_quote_id);
                i.putExtra("QuotedId",quoted_id);
                i.putExtra("JobId",jodId);

                context.startActivity(i);
            }
        });
/*
        viewHolder.payment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Instantamount = receivedContractModels.get(position).getInstant_amount();
                job_quote_id = receivedContractModels.get(position).getId();
                quoted_id = receivedContractModels.get(position).getUser_id();
                jodId = receivedContractModels.get(position).getJob_id();
                final Dialog customdialog = new Dialog(context);
                customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customdialog.setContentView(R.layout.payment_dialog);
                customdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);

                pay_now_btn = (Button) customdialog.findViewById(R.id.pay_now_btn);
                ll_bank_details = (LinearLayout) customdialog.findViewById(R.id.ll_bank_details);
                close_payment_dialog = (ImageView) customdialog.findViewById(R.id.close_payment_dialog);
                radioGroup = (RadioGroup) customdialog.findViewById(R.id.radioGroup);
                payu_radiobutton = (RadioButton) customdialog.findViewById(R.id.pay_u_radiobtn);
                paybank_radiobtn = (RadioButton) customdialog.findViewById(R.id.pay_by_bank_radiobtn);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.pay_u_radiobtn:
                                ll_bank_details.setVisibility(View.GONE);
                                // do operations specific to this selection
                                paymentType = "online";
                                break;
                            case R.id.pay_by_bank_radiobtn:
                                ll_bank_details.setVisibility(View.VISIBLE);
                                paymentType = "bank";
                                // do operations specific to this selection
                                break;
                        }
                    }
                });
                pay_now_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedId = radioGroup.getCheckedRadioButtonId();
                        radioButton = (RadioButton) customdialog.findViewById(selectedId);
                        // selectedRadioBtn = radioButton.getText().toString();
                        if (selectedId < 0) {
                            Toast.makeText(context, "Please Select Payment Method", Toast.LENGTH_SHORT).show();
                        } else if (paymentType.equalsIgnoreCase("online")) {
                            helper.showDialog((Activity) context, SweetAlertDialog.WARNING_TYPE, "", "Do you want to continue with online payment?",
                                    new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                            // PayuBiz
                                            makePaymentBank();
                                            Intent i = new Intent(context, ContractsPaymentActivity.class);
                                            context.startActivity(i);

                                        }
                                    }, new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    });
                        } else {
                            //setJsonObject();
                            helper.showDialog((Activity) context, SweetAlertDialog.WARNING_TYPE, "", "Do you want to continue with pay by bank?",
                                    new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                            makePaymentBank();
                                        }
                                    }, new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    });
                        }
                    }
                });
                close_payment_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customdialog.dismiss();
                    }
                });
                customdialog.show();
            }
        });
*/

        viewHolder.approve_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                job_id = receivedContractModels.get(position).getJob_id();
                bid_id = receivedContractModels.get(position).getId();
                contractor_id = receivedContractModels.get(position).getUser_id();
                helper.showDialog((Activity) context, SweetAlertDialog.WARNING_TYPE, "", "Are you sure you want to Approve the contract..?",
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
//                                        getHashFromSustownServer();
                                approveContract();
                            }
                        }, new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });

            }
        });
        viewHolder.confirm_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                job_id = receivedContractModels.get(position).getJob_id();
                contractor_id = receivedContractModels.get(position).getUser_id();
                bid_id = receivedContractModels.get(position).getId();
                helper.showDialog((Activity) context, SweetAlertDialog.WARNING_TYPE, "", "Are you sure you want to Confirm the contract..?",
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
//                                        getHashFromSustownServer();
                                confirmContract();
                            }
                        }, new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });

            }
        });
/*
        my_orders_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,MyContractOrdersActivity.class);
                context.startActivity(i);
            }
        });
*/

    }

    public void progressdialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    public void approveContract() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostContractsApi service = retrofit.create(PostContractsApi.class);

        Call<JsonElement> callRetrofit = null;

        callRetrofit = service.approveReceivedContract(job_id,contractor_id,user_id,bid_id);
        callRetrofit.enqueue(new Callback<JsonElement>() {

            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
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
                                    if (success.equals("1")) {
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(context, MyProductContractActivity.class);
                                        context.startActivity(i);
                                        progressDialog.dismiss();
                                    } else {
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
                    // Toast.makeText(context, "Service not responding", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
//                Toast.makeText(context, "Service not responding", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }
    public void confirmContract() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostContractsApi service = retrofit.create(PostContractsApi.class);

        Call<JsonElement> callRetrofit = null;

        callRetrofit = service.confirmQuoteReceivedContract(bid_id,job_id,contractor_id,user_id);
        callRetrofit.enqueue(new Callback<JsonElement>() {

            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
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
                                Integer status_cart_remove;
                                String message_cart_remove;
                                try {
                                    root = new JSONObject(searchResponse);
                                    String message = root.getString("message");
                                    String success = root.getString("success");
                                    if (success.equals("1")) {
                                        Intent i = new Intent(context, MyProductContractActivity.class);
                                        context.startActivity(i);
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    } else {
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
                    // Toast.makeText(context, "Service not responding", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
//                Toast.makeText(context, "Service not responding", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }

    @Override
    public int getItemCount() {
        return receivedContractModels.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView receivedorder_image;
        TextView name_receivedcontract,received_reviews,received_prod_name,received_contract_description,received_orders_payment,
                received_contract_amount,received_status,received_contract_fullname,received_contract_email,received_contract_address,
                received_contract_city,received_contract_phone,received_contract_country,uploaded_document;
        RatingBar ratingBar;
        Button approve_btn,payment_btn,confirm_contract;
        LinearLayout ll_busi_address;
        public ViewHolder(View view) {
            super(view);
            receivedorder_image = (ImageView) view.findViewById(R.id.receivedorder_image);
            name_receivedcontract = (TextView) view.findViewById(R.id.name_receivedcontract);
            received_reviews = (TextView) view.findViewById(R.id.received_reviews);
            received_prod_name = (TextView) view.findViewById(R.id.received_prod_name);
            received_contract_description = (TextView) view.findViewById(R.id.received_contract_description);
            received_orders_payment = (TextView) view.findViewById(R.id.received_orders_payment);
            received_contract_amount = (TextView) view.findViewById(R.id.received_contract_amount);
            received_status = (TextView) view.findViewById(R.id.received_status);
            received_contract_fullname = (TextView) view.findViewById(R.id.received_contract_fullname);
            received_contract_email = (TextView) view.findViewById(R.id.received_contract_email);
            received_contract_address = (TextView) view.findViewById(R.id.received_contract_address);
            received_contract_city = (TextView) view.findViewById(R.id.received_contract_city);
            received_contract_phone = (TextView) view.findViewById(R.id.received_contract_phone);
            received_contract_country = (TextView) view.findViewById(R.id.received_contract_country);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            approve_btn = (Button) view.findViewById(R.id.approve_btn);
            confirm_contract = (Button) view.findViewById(R.id.confirm_contract);
            payment_btn = (Button) view.findViewById(R.id.payment_btn);
            uploaded_document = (TextView) view.findViewById(R.id.uploaded_document);
            ll_busi_address = (LinearLayout) view.findViewById(R.id.ll_busi_address);
            ll_order_placed_text = (LinearLayout) view.findViewById(R.id.ll_order_placed_text);
            received_orders_cardview = (CardView) view.findViewById(R.id.received_orders_cardview);
        }
    }
}
