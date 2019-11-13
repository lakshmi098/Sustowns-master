package com.sustown.sustownsapp.Activities;

import android.animation.Animator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sustownsapp.R;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.sustown.sustownsapp.Adapters.ExistingAddressAdapter;
import com.sustown.sustownsapp.Adapters.GetReviewsAdapter;
import com.sustown.sustownsapp.Api.CartApi;
import com.sustown.sustownsapp.Api.DZ_URL;
import com.sustown.sustownsapp.Api.ProductsApi;
import com.sustown.sustownsapp.Api.TransportApi;
import com.sustown.sustownsapp.Api.UserApi;
import com.sustown.sustownsapp.Models.AddToCartModel;
import com.sustown.sustownsapp.Models.GetAddressModel;
import com.sustown.sustownsapp.Models.GetReviewsModel;
import com.sustown.sustownsapp.helpers.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductDetailsActivity extends AppCompatActivity {
    public static String Product_Detail_Address_Map = "";
    Button reviews, descriptions, addtocart, make_offer, buy_sample, submit_review_btn;
    LinearLayout ll_description, ll_reviews, ll_product_details,ll_shipping_services;
    ImageView backarrow, image_product_details, image_full;
    EditText name_review, edit_quantity;
    String pro_id, user_id, image, quantity, pr_title, pr_price, pr_currency, makeoffer, pr_weight, weight_unit, review, sample_pro_id;
    Intent intent;
    Spinner spinner_shipping_services,spinner_thirdparty_shipping,spinner_country;
    PreferenceUtils preferenceUtils;
    ProgressDialog progressDialog;
    TextView product_name, prod_price, prod_details_unit, packing_type, min_quantity, availability_stock, vendor_product_details;
    TextView delivery_time, reviews_text, reviews_count, product_size, vendor_discount;
    public static TextView drop_location;
    ArrayList<AddToCartModel> addToCartModels = new ArrayList<>();
    Realm realm;
    ImageView close_buy_sample, cart_img;
    Button close_btn, addtocart_sample, addtocart_btn;
    TextView buysample_title, sample_name, sample_unit, sample_size, sample_pack_type, sample_price, product_price, pick_up_location;
    EditText quantity_et;
    String ratingStr, shippingStr, discount, transportStr,shipping,addressDrop;
    String weight_sample, weight_unit_sample, pack_type_sample, pr_title_sample, pr_price_sample, pr_currency_sample, pr_min_sample;
    RecyclerView review_recyclerview;
    ArrayList<GetReviewsModel> getReviewsModels;
    ArrayList<GetAddressModel> getAddressModels;
    GetReviewsAdapter getReviewsAdapter;
    private Animator currentAnimator;
    private int shortAnimationDuration;
    Button change_address_btn;
    LinearLayout ll_vendor_link, ll_shipping_details;
    String[] transport = {"Select Shipping", "Vendor Shipping", "Third Party Shipping"};
    String[] transport1 = {"Select Shipping","Third Party Shipping"};
    String[] shippingServices = {"Select Vendor Shipping Services"};
    RatingBar ratingBarProduct, ratingBarSubmit;
    LinearLayout ll_select_shipping_services, ll_existing_address;
    RadioButton existing_radiobtn, new_radiobtn;
    Button close_drop_dialog, save_address_btn,save_address;
    RadioGroup radioGroup;
    RecyclerView recyclerview_saved_addresses;
    ExistingAddressAdapter existingAddressAdapter;
    String[] address = {"Sustowns,jntu,india,Andhra Pradesh,Guntur,500072", "way web,saurashtra,university road,rajkot,india,Telangana,500018"};
    String[] name = {"Address1", "Address2"};
    EditText name_address,company_address, email_address, first_name_address,last_name_address, address1_address, address2_address, address_state, address_town, mobile_address, pincode_address, fax_address;
    String nameAddress,companyAddress, emailAddress, firstnameAddress, lastnameAddress, address1Address, address2Address, addressState, addressTown, mobileAddress, pincodeAddress, faxAddress;
    TextView address_txt_map_dialog,saved_address_text,shipping_charge;
    Helper helper;
    String product_image_path,ServiceStr = "",vendorServiceIdStr = "",countryAddress,selected_address;
    LinearLayout ll_vendor_shipping1;
    ArrayList<String> vendor_service_ids;
    ArrayList<String> vendor_service_names;
    Dialog customdialog;
    String ProdPriceStr, ShippingValue = "2";
    RelativeLayout relativelayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product_details1);
        try {
            initializeValues();
            initializeUI();
           /* pro_id = intent.getStringExtra("Pro_Id");
            image = intent.getStringExtra("Image");*/
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    private void initializeValues() {
        realm = Realm.getDefaultInstance();
        preferenceUtils = new PreferenceUtils(ProductDetailsActivity.this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
        intent = getIntent();
        helper = new Helper(ProductDetailsActivity.this);
        pro_id = intent.getStringExtra("Pro_Id");
        image = intent.getStringExtra("Image");
       /* if(image.isEmpty() || image != null){
            Glide.with(ProductDetailsActivity.this)
                    .load(image)
                    .into(image_product_details);
        }else {
            Glide.with(ProductDetailsActivity.this)
                    .load(image)
                    .into(image_product_details);
        }*/
        // getProductDetails();

    }

    private void initializeUI() {
        review_recyclerview = (RecyclerView) findViewById(R.id.review_recyclerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
        review_recyclerview.setLayoutManager(layoutManager);
        ll_vendor_shipping1 = (LinearLayout) findViewById(R.id.ll_vendor_shipping1);
        make_offer = (Button) findViewById(R.id.make_offer);
        buy_sample = (Button) findViewById(R.id.buy_sample);
        reviews_text = (TextView) findViewById(R.id.reviews);
        ll_description = (LinearLayout) findViewById(R.id.ll_product_description);
        ll_reviews = (LinearLayout) findViewById(R.id.ll_product_reviews);
        reviews = (Button) findViewById(R.id.reviews_btn);
        descriptions = (Button) findViewById(R.id.descriptions_btn);
        ll_product_details = (LinearLayout) findViewById(R.id.ll_product_details);
        ll_vendor_link = (LinearLayout) findViewById(R.id.ll_vendor_link);
        ll_shipping_services = (LinearLayout) findViewById(R.id.ll_shipping_services);
        ratingBarProduct = (RatingBar) findViewById(R.id.ratingBarProduct);
        reviews_count = (TextView) findViewById(R.id.reviews_count);
        ratingBarSubmit = (RatingBar) findViewById(R.id.ratingBarSubmit);
        product_size = (TextView) findViewById(R.id.product_size);
        vendor_discount = (TextView) findViewById(R.id.vendor_discount);
        pick_up_location = (TextView) findViewById(R.id.pick_up_location);
        drop_location = (TextView) findViewById(R.id.drop_location);
        product_price = (TextView) findViewById(R.id.product_price);
        change_address_btn = (Button) findViewById(R.id.change_address_btn);
        shipping_charge = (TextView) findViewById(R.id.shipping_charge);
        ll_select_shipping_services = (LinearLayout) findViewById(R.id.ll_select_shipping_services);
        spinner_thirdparty_shipping = (Spinner) findViewById(R.id.spinner_thirdparty_shipping);
        String name= null;
        if(spinner_thirdparty_shipping != null && spinner_thirdparty_shipping.getSelectedItem() !=null ) {
            name = (String)spinner_thirdparty_shipping.getSelectedItem();
            if(name.equalsIgnoreCase("Vendor Shipping")){
                ShippingValue = "1";
                ll_select_shipping_services.setVisibility(View.VISIBLE);
                ll_shipping_services.setVisibility(View.VISIBLE);
            }else{
                ShippingValue = "2";
                ll_select_shipping_services.setVisibility(View.GONE);
                ll_shipping_services.setVisibility(View.GONE);
            }
        } else  {

        }
      /*  ArrayAdapter thirdparty = new ArrayAdapter(ProductDetailsActivity.this, android.R.layout.simple_spinner_item, transport1);
        thirdparty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAd
        // adapter data on the Spinner
        spinner_thirdparty_shipping.setAdapter(thirdparty);*/
        spinner_thirdparty_shipping.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                transportStr = parent.getItemAtPosition(position).toString();
                if (transportStr.equalsIgnoreCase("Vendor Shipping")) {
                    ll_select_shipping_services.setVisibility(View.VISIBLE);
                    ll_shipping_services.setVisibility(View.VISIBLE);
                    shipping = "1";
                }else {
                    ll_select_shipping_services.setVisibility(View.GONE);
                    ll_shipping_services.setVisibility(View.GONE);
                    shipping = "2";
                }
                // Toast.makeText(ProductDetailsActivity.this,shippingStr,Toast.LENGTH_SHORT).show();
                // preferenceUtils.saveString(PreferenceUtils.SAMPLE_UNIT_WEIGHT,shippingStr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_shipping_services = (Spinner) findViewById(R.id.spinner_shipping_services);
       /* ArrayAdapter aa1 = new ArrayAdapter(ProductDetailsActivity.this, android.R.layout.simple_spinner_item, shippingServices);
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAd
        // adapter data on the Spinner
        spinner_shipping_services.setAdapter(aa1);
        spinner_shipping_services.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shippingStr = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/
        name_review = (EditText) findViewById(R.id.name_review);
        edit_quantity = (EditText) findViewById(R.id.edit_quantity);
        image_product_details = (ImageView) findViewById(R.id.image_product_details);
        product_name = (TextView) findViewById(R.id.product_name);
        prod_price = (TextView) findViewById(R.id.prod_price);
        prod_details_unit = (TextView) findViewById(R.id.prod_details_unit);
        packing_type = (TextView) findViewById(R.id.packing_type);
        min_quantity = (TextView) findViewById(R.id.min_quantity);
        availability_stock = (TextView) findViewById(R.id.availability_stock);
        vendor_product_details = (TextView) findViewById(R.id.vendor_product_details);
        delivery_time = (TextView) findViewById(R.id.delivery_time);
        relativelayout = (RelativeLayout) findViewById(R.id.relativelayout);
        addtocart = (Button) findViewById(R.id.addtocart);
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(edit_quantity.getText().toString());
                int number1 = Integer.parseInt(quantity);
                if(transportStr.equalsIgnoreCase("Select Shipping")){
                    Toast.makeText(ProductDetailsActivity.this, "Please Select Transport Option", Toast.LENGTH_SHORT).show();
                }else if(transportStr.equalsIgnoreCase("Vendor Shipping")){
                    if(ServiceStr.equalsIgnoreCase("null") || ServiceStr.equalsIgnoreCase(""))
                    {
                        Toast.makeText(ProductDetailsActivity.this, "Services not avaialble for shipping", Toast.LENGTH_SHORT).show();
                    }else if(number >= number1){
                        addToCart();
                    }else{
                        Toast.makeText(ProductDetailsActivity.this, "Minimum Quantity Required", Toast.LENGTH_SHORT).show();
                    }
                }else if(number >= number1){
                    addToCart();
                }else{
                    Toast.makeText(ProductDetailsActivity.this, "Minimum Quantity Required", Toast.LENGTH_SHORT).show();
                }
                //addToCartDatabase();
            }
        });
        submit_review_btn = (Button) findViewById(R.id.submit_review_btn);
        image_full = (ImageView) findViewById(R.id.image_full);
        cart_img = (ImageView) findViewById(R.id.cart_img);
        change_address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customdialog = new Dialog(ProductDetailsActivity.this);
                customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customdialog.setContentView(R.layout.drop_location_dialog);
                customdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);
                ll_shipping_details = (LinearLayout) customdialog.findViewById(R.id.ll_shipping_details);
                ll_existing_address = (LinearLayout) customdialog.findViewById(R.id.ll_existing_address);
                recyclerview_saved_addresses = (RecyclerView) customdialog.findViewById(R.id.recyclerview_saved_addresses);
                LinearLayoutManager layoutManager1 = new LinearLayoutManager(ProductDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
                recyclerview_saved_addresses.setLayoutManager(layoutManager1);
                saved_address_text = (TextView) customdialog.findViewById(R.id.saved_address_text);
                save_address_btn = (Button) customdialog.findViewById(R.id.save_address_btn);
                name_address = (EditText) customdialog.findViewById(R.id.name_address);
                company_address = (EditText) customdialog.findViewById(R.id.company_address);
                email_address = (EditText) customdialog.findViewById(R.id.email_address);
                first_name_address = (EditText) customdialog.findViewById(R.id.first_name_address);
                spinner_country = (Spinner) customdialog.findViewById(R.id.spinner_countrydialog);
                spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        countryAddress = parent.getItemAtPosition(position).toString();
                        // Toast.makeText(ProductDetailsActivity.this,shippingStr,Toast.LENGTH_SHORT).show();
                        // preferenceUtils.saveString(PreferenceUtils.SAMPLE_UNIT_WEIGHT,shippingStr);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                last_name_address = (EditText) customdialog.findViewById(R.id.last_name_address);
                address_txt_map_dialog = customdialog.findViewById(R.id.address_txt_map_dialog);
                address_txt_map_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mapIntent = new Intent(ProductDetailsActivity.this, MapsActivity.class);
                        mapIntent.putExtra("activity", "product");
                        mapIntent.putExtra("type", "none");
                        startActivity(mapIntent);
                    }
                });
                address2_address = (EditText) customdialog.findViewById(R.id.address2_address);
                address_state = (EditText) customdialog.findViewById(R.id.address_state);
                address_town = (EditText) customdialog.findViewById(R.id.address_town);
                mobile_address = (EditText) customdialog.findViewById(R.id.mobile_address);
                pincode_address = (EditText) customdialog.findViewById(R.id.pincode_address);
                fax_address = (EditText) customdialog.findViewById(R.id.fax_address);
                radioGroup = (RadioGroup) customdialog.findViewById(R.id.radioGroup);
                existing_radiobtn = (RadioButton) customdialog.findViewById(R.id.existing_radiobtn);
                new_radiobtn = (RadioButton) customdialog.findViewById(R.id.new_radiobtn);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.existing_radiobtn:
                                ll_existing_address.setVisibility(View.VISIBLE);
                                ll_shipping_details.setVisibility(View.GONE);
                                getExistingAddresses();
                                // do operations specific to this selection
                                break;
                            case R.id.new_radiobtn:
                                ll_shipping_details.setVisibility(View.VISIBLE);
                                ll_existing_address.setVisibility(View.GONE);
                                // do operations specific to this selection
                                break;
                        }
                    }
                });
                save_address = (Button) customdialog.findViewById(R.id.save_address);
                save_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveExistingAddress();
                    }
                });
                save_address_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nameAddress = name_address.getText().toString().trim();
                        companyAddress = company_address.getText().toString().trim();
                        emailAddress = email_address.getText().toString().trim();
                        firstnameAddress = first_name_address.getText().toString().trim();
                        lastnameAddress = last_name_address.getText().toString().trim();
                        address2Address = address2_address.getText().toString().trim();
                        addressState = address_state.getText().toString().trim();
                        addressTown = address_town.getText().toString().trim();
                        mobileAddress = mobile_address.getText().toString().trim();
                        pincodeAddress = pincode_address.getText().toString().trim();
                        faxAddress = fax_address.getText().toString().trim();
                        saveNewAddress();
                    }
                });

                close_drop_dialog = (Button) customdialog.findViewById(R.id.close_drop_dialog);
                close_drop_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customdialog.dismiss();
                    }
                });
                customdialog.show();
            }
        });
        image_product_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.showImageAlert(ProductDetailsActivity.this, product_image_path);
            }
        });
        image_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_product_details.setVisibility(View.VISIBLE);
                image_full.setVisibility(View.GONE);
                image_product_details.setVisibility(View.VISIBLE);
                Glide.with(ProductDetailsActivity.this)
                        .load(image)
                        .into(image_product_details);
            }
        });
        ll_vendor_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductDetailsActivity.this, VendorLinkActivity.class);
                startActivity(i);
            }
        });
        make_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductDetailsActivity.this, MakeOffer.class);
                i.putExtra("MinQuantity", quantity);
                i.putExtra("Title", pr_title);
                i.putExtra("Unit", pr_weight + weight_unit);
                i.putExtra("Price", pr_currency + " " + pr_price);
                i.putExtra("ProID", pro_id);
                startActivity(i);
            }
        });
        buy_sample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create dialog for buy sample
                final Dialog customdialog = new Dialog(ProductDetailsActivity.this);
                customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customdialog.setContentView(R.layout.buy_sample_dialog);
                customdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);

                buysample_title = (TextView) customdialog.findViewById(R.id.buysample_title);
                sample_name = (TextView) customdialog.findViewById(R.id.sample_name);
                sample_unit = (TextView) customdialog.findViewById(R.id.sample_unit);
                sample_size = (TextView) customdialog.findViewById(R.id.sample_size);
                sample_pack_type = (TextView) customdialog.findViewById(R.id.sample_pack_type);
                sample_price = (TextView) customdialog.findViewById(R.id.sample_price);
                quantity_et = (EditText) customdialog.findViewById(R.id.quantity_et);
              /*  quantity_et.setFocusableInTouchMode(false);
                quantity_et.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        quantity_et.setFocusableInTouchMode(true);
                        quantity_et.requestFocus();
                        return false;
                    }
                });*/
                addtocart_sample = (Button) customdialog.findViewById(R.id.addtocart_btn);
                close_btn = (Button) customdialog.findViewById(R.id.close_btn);
                buy_sample.setVisibility(View.VISIBLE);
                buysample_title.setText(pr_title_sample);
                sample_name.setText(pr_title_sample);
                sample_unit.setText(weight_sample + " " + weight_unit_sample);
                sample_size.setText("***");
                sample_pack_type.setText(pack_type_sample);
                sample_price.setText(pr_currency_sample + " " + pr_price_sample);
                quantity_et.setText(pr_min_sample);
                getBuySampleDetails();

                close_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customdialog.dismiss();
                    }
                });
                addtocart_sample.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //customdialog.dismiss();
                    }
                });
                customdialog.show();
            }
        });
        cart_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductDetailsActivity.this, CartActivity.class);
                startActivity(i);
            }
        });

        reviews_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_reviews.setVisibility(View.VISIBLE);
            }
        });
        submit_review_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingStr = String.valueOf(ratingBarSubmit.getRating());
                // Toast.makeText(ProductDetailsActivity.this, ratingStr, Toast.LENGTH_LONG).show();
                review = name_review.getText().toString().trim();
                if (ratingStr.equalsIgnoreCase("0.0")) {
                    Toast.makeText(ProductDetailsActivity.this, "please give rating", Toast.LENGTH_SHORT).show();
                } else if (review.equalsIgnoreCase("")) {
                    Toast.makeText(ProductDetailsActivity.this, "please write a review", Toast.LENGTH_SHORT).show();
                } else {
                    addProductReview();
                }
            }
        });
        edit_quantity.setFocusableInTouchMode(false);
        edit_quantity.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                edit_quantity.setFocusableInTouchMode(true);
                edit_quantity.requestFocus();
                return false;
            }
        });
        name_review.setFocusableInTouchMode(false);
        name_review.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                name_review.setFocusableInTouchMode(true);
                name_review.requestFocus();
                return false;
            }
        });
        backarrow = (ImageView) findViewById(R.id.backarrow);
        //  ll_description.setVisibility(View.VISIBLE);
        // ll_reviews.setVisibility(View.GONE);
      /*  reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_reviews.setVisibility(View.VISIBLE);
                ll_description.setVisibility(View.GONE);
            }
        });
        descriptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_description.setVisibility(View.VISIBLE);
                ll_reviews.setVisibility(View.GONE);

            }
        });*/

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // edittextFocus();
        getProductDetails();
    }

    private void edittextFocus() {
        name_address.setFocusableInTouchMode(false);
        name_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                name_address.setFocusableInTouchMode(true);
                name_address.requestFocus();
                return false;
            }
        });
        company_address.setFocusableInTouchMode(false);
        company_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                company_address.setFocusableInTouchMode(true);
                company_address.requestFocus();
                return false;
            }
        });
        email_address.setFocusableInTouchMode(false);
        email_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                email_address.setFocusableInTouchMode(true);
                email_address.requestFocus();
                return false;
            }
        });
        first_name_address.setFocusableInTouchMode(false);
        first_name_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                first_name_address.setFocusableInTouchMode(true);
                first_name_address.requestFocus();
                return false;
            }
        });
        last_name_address.setFocusableInTouchMode(false);
        last_name_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                last_name_address.setFocusableInTouchMode(true);
                last_name_address.requestFocus();
                return false;
            }
        });
        address2_address.setFocusableInTouchMode(false);
        address2_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                address2_address.setFocusableInTouchMode(true);
                address2_address.requestFocus();
                return false;
            }
        });
        mobile_address.setFocusableInTouchMode(false);
        mobile_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mobile_address.setFocusableInTouchMode(true);
                mobile_address.requestFocus();
                return false;
            }
        });
        address_state.setFocusableInTouchMode(false);
        address_state.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                address_state.setFocusableInTouchMode(true);
                address_state.requestFocus();
                return false;
            }
        });
        address_town.setFocusableInTouchMode(false);
        address_town.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                address_town.setFocusableInTouchMode(true);
                address_town.requestFocus();
                return false;
            }
        });
        pincode_address.setFocusableInTouchMode(false);
        pincode_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pincode_address.setFocusableInTouchMode(true);
                pincode_address.requestFocus();
                return false;
            }
        });
        fax_address.setFocusableInTouchMode(false);
        fax_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                fax_address.setFocusableInTouchMode(true);
                fax_address.requestFocus();
                return false;
            }
        });

    }

/*
    private void addToCartDatabase() {
        if (realm.isInTransaction())
            realm.cancelTransaction();

        realm.beginTransaction();

            if (edit_quantity.getText().toString().trim().length() > 0) {
                Double totalAmount = Double.parseDouble(edit_quantity.getText().toString()) * Double.parseDouble(pr_price);
                AddToCartModel addToCartModel = new AddToCartModel(pro_id, user_id, image, pr_title, pr_price, quantity, pr_currency, String.valueOf(totalAmount), false);
                Toast.makeText(ProductDetailsActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                realm.copyToRealmOrUpdate(addToCartModel);
            }
            realm.commitTransaction();
            Toast.makeText(ProductDetailsActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();

    }
*/


    public void progressdialog() {
        progressDialog = new ProgressDialog(ProductDetailsActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    private void getProductDetails() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi service = retrofit.create(UserApi.class);

        Call<JsonElement> callRetrofit = null;
        // callRetrofit = service.productDetails(user_id, pro_id);
        callRetrofit = service.productDetails(user_id, pro_id);

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Log.d("Success Call", ">>>>" + call);
                        Log.d("Success Call ", ">>>>" + response.body().toString());

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
                                Log.d("Reg", "Response  >>" + searchResponse.toString());

                                if (searchResponse != null) {
                                    JSONObject root = null;
                                    try {
                                        root = new JSONObject(searchResponse);
                                        String message;
                                        Integer success;
                                        success = root.getInt("success");
                                        String reviewcount = root.getString("reviewcount");
                                        String rattingcount = root.getString("rattingcount");
                                        //   message = root.getString("message");
                                        if (success == 1) {
                                            if (progressDialog.isShowing())
                                                progressDialog.dismiss();
                                            JSONArray jsonArray = root.getJSONArray("getproduct");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                pro_id = jsonObject.getString("id");
                                                pr_title = jsonObject.getString("pr_title");
                                                String pr_image = jsonObject.getString("pr_image");
                                                pr_price = jsonObject.getString("pr_price");
                                                pr_currency = jsonObject.getString("pr_currency");
                                                discount = jsonObject.getString("discount");
                                                if (discount.equalsIgnoreCase("null")) {
                                                    vendor_discount.setText("No Discount");
                                                } else {
                                                    vendor_discount.setText(discount + "%");
                                                }
                                                quantity = jsonObject.getString("pr_min");
                                                pr_weight = jsonObject.getString("pr_weight");
                                                String pr_packtype = jsonObject.getString("pr_packtype");
                                                String days = jsonObject.getString("days");
                                                String stock_status_name = jsonObject.getString("stock_status_name");
                                                weight_unit = jsonObject.getString("weight_unit");
                                                String bus_name = jsonObject.getString("bus_name");
                                                String imagepath = jsonObject.getString("imagepath");
                                                String shipingprovide = jsonObject.getString("shipingprovide");
                                                if(shipingprovide.equalsIgnoreCase("yes")){
                                                    ArrayAdapter thirdparty = new ArrayAdapter(ProductDetailsActivity.this, android.R.layout.simple_spinner_item, transport);
                                                    thirdparty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                    spinner_thirdparty_shipping.setAdapter(thirdparty);
                                                    if (transportStr != null) {
                                                        int spinnerPosition = thirdparty.getPosition(transportStr);
                                                        spinner_thirdparty_shipping.setSelection(spinnerPosition);
                                                    }
                                                    // spinner_thirdparty_shipping.setSelection(1);
                                                }else{
                                                    ArrayAdapter thirdparty = new ArrayAdapter(ProductDetailsActivity.this, android.R.layout.simple_spinner_item, transport1);
                                                    thirdparty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                    spinner_thirdparty_shipping.setAdapter(thirdparty);
                                                    if (transportStr != null) {
                                                        int spinnerPosition = thirdparty.getPosition(transportStr);
                                                        spinner_thirdparty_shipping.setSelection(spinnerPosition);
                                                    }
                                                }
                                                String city = jsonObject.getString("city");
                                                String state = jsonObject.getString("state");
                                                String country = jsonObject.getString("country");
                                                String zipcode = jsonObject.getString("zipcode");
                                                makeoffer = jsonObject.getString("makeoffer");
                                                String prod_image = imagepath + pr_image;
                                                product_image_path = prod_image;
                                                if (makeoffer.equalsIgnoreCase("1")) {
                                                    make_offer.setVisibility(View.VISIBLE);
                                                } else {
                                                    make_offer.setVisibility(View.GONE);
                                                }

                                                if (prod_image != null && !prod_image.isEmpty()) {
                                                    Picasso.get()
                                                            .load(prod_image)
                                                            .placeholder(R.drawable.no_image_available)
                                                            .error(R.drawable.no_image_available)
                                                            .into(image_product_details);
                                                } else {
                                                    Picasso.get()
                                                            .load(R.drawable.no_image_available)
                                                            .placeholder(R.drawable.no_image_available)
                                                            .error(R.drawable.no_image_available)
                                                            .into(image_product_details);
                                                }
                                              /*  if(image.isEmpty() || image != null){
                                                    Glide.with(ProductDetailsActivity.this)
                                                            .load(image)
                                                            .into(image_product_details);
                                                }else {
                                                    Glide.with(ProductDetailsActivity.this)
                                                            .load(R.drawable.no_image_available)
                                                            .into(image_product_details);
                                                }*/
                                                product_name.setText(pr_title);
                                                prod_price.setText(pr_currency + pr_price);
                                                prod_details_unit.setText(pr_weight + weight_unit);
                                                packing_type.setText(pr_packtype);
                                                min_quantity.setText(quantity);
                                                availability_stock.setText(stock_status_name);
                                                vendor_product_details.setText(bus_name);
                                                delivery_time.setText(days + " Days");
                                                edit_quantity.setText(quantity);
                                                reviews_count.setText("Reviews" + "(" + reviewcount + ")");
                                                int quantityInt = Integer.parseInt(quantity);
                                                int priceStr = Integer.parseInt(pr_price);
                                                pick_up_location.setText(city+","+state+","+country+","+zipcode);
                                                ProdPriceStr = String.valueOf(quantityInt * priceStr);
                                                product_price.setText(ProdPriceStr);
                                                // ratingBar.setRating(Float.parseFloat(rattingcount));

                                            }
                                            JSONArray jsonArray1 = root.getJSONArray("getproductsamdel");
                                            for (int j = 0; j < jsonArray1.length(); j++) {
                                                JSONObject object = jsonArray1.getJSONObject(j);
                                                String weight = object.getString("weight");
                                                String weight_unit = object.getString("weight_unit");
                                                String pack_type = object.getString("pack_type");
                                                String price = object.getString("price");
                                                String maxquan = object.getString("maxquan");
                                                String scurrency = object.getString("scurrency");
                                            }
                                            vendor_service_ids = new ArrayList<String>();
                                            vendor_service_names = new ArrayList<String>();
                                            JSONArray vendor = root.getJSONArray("vendor");
                                            for (int k = 0; k < vendor.length(); k++) {
                                                if (vendor.length() > 0) {
                                                    JSONObject jsonObjectVendor = vendor.getJSONObject(k);
                                                    String id = jsonObjectVendor.getString("id");
                                                    String service_id = jsonObjectVendor.getString("service_id");
                                                    String pr_title = jsonObjectVendor.getString("pr_title");
                                                    vendor_service_ids.add(service_id);
                                                    vendor_service_names.add(pr_title);
                                                    // preferenceUtils.saveString(PreferenceUtils.ORDER_ID,id);
                                                } else {
                                                    // Toast.makeText(CartActivity.this, "User", Toast.LENGTH_SHORT).show();
                                                }
                                                ArrayAdapter<String> adapter1;
//                                spinner_dealer = (Spinner) findViewById(R.id.spinner_dealer);
                                                if (vendor_service_names != null) {
                                                    adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, vendor_service_names);
                                                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                    spinner_shipping_services.setAdapter(adapter1);
                                                    //  progressDialog.dismiss();
                                                } else {
                                                   /* adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, shippingServices);
                                                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                    spinner_shipping_services.setAdapter(adapter1);*/
                                                    // Toast.makeText(ProductDetailsActivity.this, "Users not available", Toast.LENGTH_SHORT).show();
                                                    //  progressDialog.dismiss();
                                                }
                                                spinner_shipping_services.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                                                        ServiceStr = spinner_shipping_services.getSelectedItem().toString();
                                                        //user_id_cust = customer_dealers.get(pos);
                                                        vendorServiceIdStr = vendor_service_ids.get(pos);
                                                        // preferenceUtils.saveString(PreferenceUtils.CUST_USER_ID,cust_id);
                                                        //   customer_users_st = customerUserListModels.get(pos).getCommon_id();
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                                        //getCustomerUserList();
                                                    }
                                                });
                                            }
                                            JSONObject delidetails = root.getJSONObject("delidetails");
                                            String address1 = delidetails.getString("address1");
                                            String city_drop = delidetails.getString("city");
                                            String state_drop = delidetails.getString("state");
                                            String country_drop = delidetails.getString("country");
                                            String zipcode_drop = delidetails.getString("zipcode");
                                            if(address1 != null || !address1.isEmpty() ||  city_drop != null || !city_drop.isEmpty() ||  state_drop != null || !state_drop.isEmpty()
                                                    || country_drop != null || !country_drop.isEmpty() || zipcode_drop != null || !zipcode_drop.isEmpty()){
                                                change_address_btn.setText("Add New Address");
                                            }else{
                                                drop_location.setText(address1+","+city_drop+","+state_drop+","+country_drop+","+zipcode_drop);
                                            }
                                            JSONArray jsonArray2 = root.getJSONArray("ratting");
                                            for (int j1 = 0; j1 < jsonArray2.length(); j1++) {
                                                JSONObject object = jsonArray2.getJSONObject(j1);
                                                String prid = object.getString("prid");
                                                String product_id = object.getString("product_id");
                                                String user_id = object.getString("user_id");
                                                String review = object.getString("review");
                                                String com_reply = object.getString("com_reply");
                                                String ratting = object.getString("ratting");
                                                String ondate = object.getString("ondate");
                                                String status = object.getString("status");
                                                String avg = object.getString("avg");
                                                String fullname = object.getString("fullname");
                                                String city1 = object.getString("city");
                                                String country1 = object.getString("country");

                                                ratingBarProduct.setRating(Float.parseFloat(ratting));

                                            }
                                            JSONArray jsonArray3 = root.getJSONArray("review");
                                            getReviewsModels = new ArrayList<>();
                                            for (int j2 = 0; j2 < jsonArray3.length(); j2++) {
                                                JSONObject object = jsonArray3.getJSONObject(j2);
                                                String prid = object.getString("prid");
                                                String product_id = object.getString("product_id");
                                                String user_id = object.getString("user_id");
                                                String review = object.getString("review");
                                                String com_reply = object.getString("com_reply");
                                                String ratting = object.getString("ratting");
                                                String ondate = object.getString("ondate");
                                                String status = object.getString("status");
                                                String fullname = object.getString("fullname");
                                                String city1 = object.getString("city");
                                                String country1 = object.getString("country");

                                                GetReviewsModel getReviewsModel = new GetReviewsModel();
                                                getReviewsModel.setOndate(ondate);
                                                getReviewsModel.setRatting(ratting);
                                                getReviewsModel.setReview(review);
                                                getReviewsModel.setFullname(fullname);
                                                getReviewsModel.setCity(city1);
                                                getReviewsModel.setCountry(country1);
                                                getReviewsModels.add(getReviewsModel);

                                            }
                                            if (getReviewsModels != null) {
                                                getReviewsAdapter = new GetReviewsAdapter(ProductDetailsActivity.this, getReviewsModels);
                                                review_recyclerview.setAdapter(getReviewsAdapter);
                                                getReviewsAdapter.notifyDataSetChanged();
                                            }
                                            JSONObject object = root.getJSONObject("sampledel");
                                            String sid = object.getString("sid");
                                            String productid = object.getString("productid");
                                            String user_id = object.getString("user_id");
                                            String sampleproduct = object.getString("sampleproduct");
                                            weight_sample = object.getString("weight");
                                            weight_unit_sample = object.getString("weight_unit");
                                            pack_type_sample = object.getString("pack_type");
                                            pr_title_sample = object.getString("pr_title");
                                            pr_price_sample = object.getString("pr_price");
                                            pr_currency_sample = object.getString("pr_currency");
                                            pr_min_sample = object.getString("pr_min");
                                            preferenceUtils.getStringFromPreference(PreferenceUtils.SampleProdId, sid);
                                            if (object != null || !object.equals("")) {
                                                buy_sample.setVisibility(View.VISIBLE);
                                            } else {
                                                buy_sample.setVisibility(View.GONE);
                                            }
                                        } else {
                                            ArrayAdapter thirdparty = new ArrayAdapter(ProductDetailsActivity.this, android.R.layout.simple_spinner_item, transport1);
                                            thirdparty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            //Setting the ArrayAd
                                            // adapter data on the Spinner
                                            spinner_thirdparty_shipping.setAdapter(thirdparty);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                //  Toast.makeText(ProductsActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    public void addToCart() {
        user_id = preferenceUtils.getStringFromPreference(preferenceUtils.USER_ID,"");
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CartApi service = retrofit.create(CartApi.class);

        Call<JsonElement> callRetrofit = null;

        callRetrofit = service.addToCart(user_id,pro_id,quantity,shipping,ProdPriceStr,vendorServiceIdStr);
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
                                          /*  Intent i = new Intent(context, Cart.class);
                                            context.startActivity(i);*/
                                            // editor.putBoolean("loginstatus", true);
                                            // editor.putString("cart_id", cart_id);
                                            // editor.commit();
                                            Snackbar snackbar = Snackbar
                                                    .make(relativelayout,message, Snackbar.LENGTH_LONG)
                                                    .setAction("GO TO CART", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            Intent i = new Intent(ProductDetailsActivity.this, CartActivity.class);
                                                            startActivity(i);
                                                        }
                                                    });
                                            snackbar.show();
                                           // Toast.makeText(ProductDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        } else {
                                            Toast.makeText(ProductDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                }

                            }
                        }
                    }
                    else {
                        Toast.makeText(ProductDetailsActivity.this, "Product Not Added To Cart", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
                catch (Exception e){
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
    private void addProductReview() {

        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ProductsApi service = retrofit.create(ProductsApi.class);
        review = name_review.getText().toString().trim();
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.addReview(user_id, pro_id, review, ratingStr);

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        Log.d("Success Call", ">>>>" + call);
                        Log.d("Success Call ", ">>>>" + response.body().toString());

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
                                Log.d("Reg", "Response  >>" + searchResponse.toString());

                                if (searchResponse != null) {
                                    JSONObject root = null;
                                    try {
                                        root = new JSONObject(searchResponse);
                                        String message;
                                        Integer success;
                                        success = root.getInt("success");
                                        message = root.getString("message");
                                        if (success == 1) {
                                            ll_reviews.setVisibility(View.GONE);
                                            Toast.makeText(ProductDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ProductDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                //  Toast.makeText(ProductsActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    private void getBuySampleDetails() {
        sample_pro_id = preferenceUtils.getStringFromPreference(PreferenceUtils.SampleProdId, "");
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ProductsApi service = retrofit.create(ProductsApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.buySampleData(quantity, sample_pro_id);

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        Log.d("Success Call", ">>>>" + call);
                        Log.d("Success Call ", ">>>>" + response.body().toString());

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
                                Log.d("Reg", "Response  >>" + searchResponse.toString());

                                if (searchResponse != null) {
                                    JSONObject root = null;
                                    try {
                                        root = new JSONObject(searchResponse);
                                        String message;
                                        Integer success;
                                        message = root.getString("message");
                                        success = root.getInt("success");
                                        if (success == 1) {
                                            // Toast.makeText(ProductDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();

                                        } else {
                                            message = root.getString("message");
                                            // Toast.makeText(ProductDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
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
                        // Toast.makeText(SignInActivity.this, "Service not responding", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                //    Toast.makeText(MakeOffer.this, "Please login again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void getExistingAddresses() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TransportApi service = retrofit.create(TransportApi.class);

        Call<JsonElement> callRetrofit = null;
        // callRetrofit = service.productDetails(user_id, pro_id);
        callRetrofit = service.getExistingAddress(user_id);

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Log.d("Success Call", ">>>>" + call);
                        Log.d("Success Call ", ">>>>" + response.body().toString());

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
                                Log.d("Reg", "Response  >>" + searchResponse.toString());

                                if (searchResponse != null) {
                                    JSONObject root = null;
                                    try {
                                        root = new JSONObject(searchResponse);
                                        String message;
                                        Integer success;
                                        message = root.getString("message");
                                        success = root.getInt("success");
                                        if (success == 1) {
                                            if (progressDialog.isShowing())
                                                progressDialog.dismiss();
                                            JSONArray jsonArray = root.getJSONArray("existing");
                                            getAddressModels = new ArrayList<>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String id = jsonObject.getString("id");
                                                String user_id = jsonObject.getString("user_id");
                                                String name = jsonObject.getString("name");
                                                String address1 = jsonObject.getString("address1");
                                                String zipcode = jsonObject.getString("zipcode");
                                                String country_name = jsonObject.getString("country_name");
                                                String state = jsonObject.getString("state");
                                                String city_name = jsonObject.getString("city_name");

                                                GetAddressModel getAddressModel = new GetAddressModel();
                                                getAddressModel.setId(id);
                                                getAddressModel.setUser_id(user_id);
                                                getAddressModel.setName(name);
                                                getAddressModel.setAddress1(address1);
                                                getAddressModel.setZipcode(zipcode);
                                                getAddressModel.setCountry_name(country_name);
                                                getAddressModel.setState(state);
                                                getAddressModel.setCity_name(city_name);
                                                getAddressModels.add(getAddressModel);

                                            }
                                            if (getAddressModels != null) {
                                                saved_address_text.setVisibility(View.GONE);
                                                existingAddressAdapter = new ExistingAddressAdapter(ProductDetailsActivity.this,getAddressModels);
                                                recyclerview_saved_addresses.setAdapter(existingAddressAdapter);
                                                existingAddressAdapter.notifyDataSetChanged();
                                            }else{
                                                saved_address_text.setVisibility(View.VISIBLE);
                                            }

                                        } else {
                                            saved_address_text.setVisibility(View.VISIBLE);

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                //  Toast.makeText(ProductsActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    private void saveNewAddress() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TransportApi service = retrofit.create(TransportApi.class);
        nameAddress = name_address.getText().toString().trim();
        companyAddress = company_address.getText().toString().trim();
        emailAddress = email_address.getText().toString().trim();
        firstnameAddress = first_name_address.getText().toString().trim();
        lastnameAddress = last_name_address.getText().toString().trim();
        address2Address = address2_address.getText().toString().trim();
        addressState = address_state.getText().toString().trim();
        addressTown = address_town.getText().toString().trim();
        mobileAddress = mobile_address.getText().toString().trim();
        pincodeAddress = pincode_address.getText().toString().trim();
        faxAddress = fax_address.getText().toString().trim();
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.addNewAddress(user_id,nameAddress,companyAddress,firstnameAddress, lastnameAddress,
                emailAddress,"",address2Address,pincodeAddress,countryAddress,addressState,addressTown,mobileAddress,faxAddress,"","");

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        Log.d("Success Call", ">>>>" + call);
                        Log.d("Success Call ", ">>>>" + response.body().toString());

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
                                Log.d("Reg", "Response  >>" + searchResponse.toString());

                                if (searchResponse != null) {
                                    JSONObject root = null;
                                    try {
                                        root = new JSONObject(searchResponse);
                                        String message;
                                        Integer success;
                                        message = root.getString("message");
                                        success = root.getInt("success");
                                        if (success == 1) {
                                            progressDialog.dismiss();
                                            customdialog.dismiss();
                                            Toast.makeText(ProductDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(ProductDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                //  Toast.makeText(ProductsActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    public void saveExistingAddress() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TransportApi service = retrofit.create(TransportApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.sentAddress(user_id,user_id,"408","500038","5.76765656","8.765645","14");

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        Log.d("Success Call", ">>>>" + call);
                        Log.d("Success Call ", ">>>>" + response.body().toString());

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
                                Log.d("Reg", "Response  >>" + searchResponse.toString());

                                if (searchResponse != null) {
                                    JSONObject root = null;
                                    try {
                                        root = new JSONObject(searchResponse);
                                        String message;
                                        Integer success;
                                        message = root.getString("message");
                                        success = root.getInt("success");
                                        if (success == 1) {
                                            progressDialog.dismiss();
                                            customdialog.dismiss();
                                            /*Intent i = new Intent(ProductDetailsActivity.this,ProductDetailsActivity.class);
                                            startActivity(i);*/
                                            //drop_location.setText(radioText);
                                            Toast.makeText(ProductDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(ProductDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                //  Toast.makeText(ProductsActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //   drop_location.setText(radioText);

/*
        if(drop_location != null){
           drop_location.setText(radioText);
        }*/
        if(address_txt_map_dialog != null){
            address_txt_map_dialog.setText(Product_Detail_Address_Map);
        } /*if ( spinner_thirdparty_shipping.getCount() > 0 )
        {
            spinner_thirdparty_shipping.getOnItemSelectedListener()
                    .onItemSelected( spinner_thirdparty_shipping, null, spinner_thirdparty_shipping.getSelectedItemPosition(), 0 );
        }*/
      /*  Spinner spinner = (Spinner)findViewById(R.id.spinner_thirdparty_shipping);

        SharedPreferences prefs = getSharedPreferences("prefs_name", Context.MODE_PRIVATE);
        int spinnerIndx = prefs.getInt("spinner_indx", 0);
        spinner.setSelection(spinnerIndx);*/
    }
  /*  @Override
    public void onPause() {
        super.onPause();

        Spinner spinner = (Spinner)findViewById(R.id.spinner_thirdparty_shipping);

        SharedPreferences prefs = getSharedPreferences("prefs_name", Context.MODE_PRIVATE);
        prefs.edit().putInt("spinner_indx", spinner.getSelectedItemPosition()).apply();
    }
*/
}
