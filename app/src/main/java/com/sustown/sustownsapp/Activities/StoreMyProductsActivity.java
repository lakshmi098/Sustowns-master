package com.sustown.sustownsapp.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.sustownsapp.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.sustown.sustownsapp.Adapters.AddVendorServicesAdapter;
import com.sustown.sustownsapp.Adapters.ImagesAdapter;
import com.sustown.sustownsapp.Adapters.MyServicesAdapter;
import com.sustown.sustownsapp.Adapters.StoreMyProductsAdapter;
import com.sustown.sustownsapp.Api.CustomizationsApi;
import com.sustown.sustownsapp.Api.DZ_URL;
import com.sustown.sustownsapp.Api.ProductsApi;
import com.sustown.sustownsapp.Api.UserApi;
import com.sustown.sustownsapp.Models.AddProductVendorServices;
import com.sustown.sustownsapp.Models.GetCurrencyModel;
import com.sustown.sustownsapp.Models.ImageModel;
import com.sustown.sustownsapp.Models.MyProductsModel;
import com.sustown.sustownsapp.Models.TransportGetService;
import com.sustown.sustownsapp.helpers.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class StoreMyProductsActivity extends AppCompatActivity {
    public static RecyclerView recycler_view_products_store;
    public static String Product_Address_Map = "";
    ImageView backarrow, three_dots_icon;
    CircleImageView profile_img,imageview;
    boolean isUpdate;
    String[] order = {"order 1", "order 2"};
    StoreMyProductsAdapter storeMyProductsAdapter;
    Button add_product_store, save_product, choosefile_btn;
    RelativeLayout rl_capture, rl_gallery;
    public static List<MyProductsModel> myProductsModelList;
    TextView title_store;
    private Uri picUri;
    public String PickedImgPath = null;
    String currency_st, TitleStr = "", shippingId = "";
    LinearLayout ll_shipping_type, ll_buyer_network,linearlayout,ll_custom_invoice, ll_customizations, ll_my_products, ll_contracts;
    ArrayList<String> currencyCodes;
    ArrayList<String> countries;
    public static LinearLayout ll_add_products_store, ll_prod_list, ll_addproduct_general;
    Spinner spinner_eggs_types, spinner_quality, spinner_prod_category, spinner_sector, spinner_list_type, spinner_sample_gross_weight;
    Spinner spinner_unit, spinner_price, spinner_sample_pack_type, spinner_shipping, sp_country, sp_state, sp_city;
    EditText title_add_prod, unit_edit, price_edittext, min_order_et, stock_et, discount_et, sample_unit_edit, pincode_et, gross_weight_et;
    String[] eggsType = {"Regular/Layer Eggs", "Organic Eggs", "Duck Eggs", "Quail Eggs"};
    String[] quality = {"select quality", "AA", "A", "B"};
    String[] prod_category = {"Egg", "Poultry"};
    String[] sector = {"B2B(Business to Business)", "Buyer network"};
    String[] listingtype = {"Product", "Service"};
    String[] unit = {"Crate", "Box"};
    String[] price = {"INR"};
    String[] samplepacktype = {"sample pack type", "12 Pack Crate", "20 Pack Crate", "30 Pack Crate", "210 Pack Box"};
    //String[] samplepacktype = {"Sample Packing Type","Bulk","Can","Drum","Gift Packing","Mason Jar","Tank","Vaccum Pack","Carton","Bags","Pallet","Other"};
    // String[] shipping = {"choose delivery type","EXW","FOB","CIF","Door to Door","Door to Port","Port to Port","Port to Door"};
    String[] country = {"India", "Indonesia", "Iceland", "Australia", "Algeria", "Malaysia", "Saudi Arabia", "Singapore", "USA", "UK", "Uganda"};
    String[] city = {"Hyderabad", "Mumbai", "Chennai", "Kolkata", "Pune"};
    String[] state = {"Telangana", "AP", "Punjab", "UP", "Kerala", "Delhi"};
    String[] shipping = {"No","Yes"};
    String[] gross_weight_unit = {"gross weight unit", "Crate", "Box"};
    String[] receivedOffers = {"Select Recevied Offers", "Yes", "No"};
    String[] packingSize = {"mm", "cm", "in", "ft"};
    String[] DeliveryType = {"EXW", "FOB", "CIF", "Door to Door", "Door to Port", "Port to Door", "Port to Port"};
    String add_prod_st, unit_st, price_st, min_order_st,stock_st, discount_st,eggsType_st,sample_unit_st, user_id, user_role, prod_id, buss_id, pincode_st;
    PreferenceUtils preferenceUtils;
    String address_st, sample_gross_weight_sp, gross_weight_st, quantity_st, sample_price_st, receivedOffersStr;
    EditText max_quantity_et, sample_price_edittext, delivery_lead_time;
    ProgressDialog progressDialog;
    Spinner spinner_sample_unit, spinner_pack_type, spinner_received_offers;
    String sample_weight_unit_sp, packTypeStr, delivery_time, profileString, currency, country_string, received_offers_st, pr_bussid, productId;
    String eggs_type, quality_st, prodCategory, sector_st, listingtype_st, unit_sp_st, price_sp_st, sample_packtype, shipping_st, country_st, state_st, city_st;
    ArrayList<GetCurrencyModel> getCurrencyModels;
    ArrayList<TransportGetService> transportGetServices;
    String currency_str, sample_price_str;
    EditText sample_gross_weight_et;
    Spinner spinner_delivery_type;
    Button dis_start_date, dis_end_date;
    TextView dis_start_date_text, dis_end_date_text, address_txt_map, spinner_shipping_type;
    Spinner spinner_sample_price, spinner_packing_size_general, spinnersample_packingsize_general, spinner_sample_gross_unit, spinner_country_origin;
    RecyclerView spinner_choose_shipping_types, shipping_list_recyclerview, recyclerview_shipping_sizes, images_recyclerView;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    Button my_products_btn, customizations_btn;
    RadioGroup mapRadioGroup;
    LinearLayout ll_my_Product_contracts, ll_choose_shipping_types, ll_vendor_services, ll_shipping_types_list, ll_add_vendor_areas;
    int position;
    String imagePath, serviceNameStr, serviceIdStr;
    protected static final int CAMERA_CAPTURE = 2;
    protected static final int PICK_IMAGE = 1;
    ArrayList<AddProductVendorServices> addProductVendorServices;
    MyServicesAdapter myServicesAdapter;
    AddVendorServicesAdapter addVendorServicesAdapter;
    SparseBooleanArray mSelectedItemsListIds = new SparseBooleanArray();
    EditText title_general, summary_general, description_general, general_length, general_width, general_height, tax_general, sample_general_min_order,
            sample_general_length, sample_general_width, sample_general_height, sample_gross_unit_weight;
    String packingSizeStr, sampackingSizeStr, sampleGrossStr, countyOriginStr, deliveryTypeStr;
    CheckBox checkbox_international, checkbox_domestic;
    LinearLayout ll_international_freight, ll_domestiic_freight;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 7;
    ImageButton confirm_add_icon;
    int count = 0;
    List<ImageModel> imagesList = new ArrayList<>();
    List<String> fileList = new ArrayList<>();
    List<String> shippingList = new ArrayList<>();
    AlertDialog alertDialog;
    List<Integer> shippingSelectedPosition = new ArrayList<>();
    List<String> shippingSelectedList = new ArrayList<>();
    Helper helper;
    String UnitId,CategoryId,SectorId;

    public static String getEncodedImage(Bitmap bitmapImage) {
        ByteArrayOutputStream baos;
        baos = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        String encodedImagePatientImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImagePatientImage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_store_my_products);
        preferenceUtils = new PreferenceUtils(StoreMyProductsActivity.this);
        try {
            // checkAndroidVersion();
            helper = new Helper(this);
            isUpdate = false;
            user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
            user_role = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ROLE, "");
            recycler_view_products_store = (RecyclerView) findViewById(R.id.recycler_view_products_store);
            // recycler_view_products_store.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(StoreMyProductsActivity.this, LinearLayoutManager.VERTICAL, false);
            recycler_view_products_store.setLayoutManager(layoutManager);

            spinner_choose_shipping_types = (RecyclerView) findViewById(R.id.spinner_choose_shipping_types);
            LinearLayoutManager layoutManager1 = new LinearLayoutManager(StoreMyProductsActivity.this, LinearLayoutManager.VERTICAL, false);
            spinner_choose_shipping_types.setLayoutManager(layoutManager1);

            shipping_list_recyclerview = (RecyclerView) findViewById(R.id.shipping_list_recyclerview);
            LinearLayoutManager layoutManager2 = new LinearLayoutManager(StoreMyProductsActivity.this, LinearLayoutManager.VERTICAL, false);
            shipping_list_recyclerview.setLayoutManager(layoutManager2);

            recyclerview_shipping_sizes = (RecyclerView) findViewById(R.id.recyclerview_shipping_sizes);
            LinearLayoutManager layoutManager3 = new LinearLayoutManager(StoreMyProductsActivity.this, LinearLayoutManager.VERTICAL, false);
            recyclerview_shipping_sizes.setLayoutManager(layoutManager3);

            ll_choose_shipping_types = (LinearLayout) findViewById(R.id.ll_choose_shipping_types);
            ll_vendor_services = (LinearLayout) findViewById(R.id.ll_vendor_services);
            ll_add_products_store = (LinearLayout) findViewById(R.id.ll_add_products_store);
            ll_prod_list = (LinearLayout) findViewById(R.id.ll_prod_list);
            ll_addproduct_general = (LinearLayout) findViewById(R.id.ll_addproduct_general);
            ll_shipping_types_list = (LinearLayout) findViewById(R.id.ll_shipping_types_list);
            ll_add_vendor_areas = (LinearLayout) findViewById(R.id.ll_add_vendor_areas);
            ll_add_products_store.setVisibility(View.GONE);
            ll_prod_list.setVisibility(View.VISIBLE);
            add_product_store = (Button) findViewById(R.id.add_product_store);
            backarrow = (ImageView) findViewById(R.id.backarrow);
            title_store = (TextView) findViewById(R.id.title_store);
            save_product = (Button) findViewById(R.id.save_product);
            linearlayout = (LinearLayout) findViewById(R.id.linearlayout);
            profile_img = (CircleImageView) findViewById(R.id.profile_img);
            dis_start_date = (Button) findViewById(R.id.dis_start_date);
            dis_end_date = (Button) findViewById(R.id.dis_end_date);
            dis_start_date_text = (TextView) findViewById(R.id.dis_start_date_text);
            dis_end_date_text = (TextView) findViewById(R.id.dis_end_date_text);
            dis_start_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cal = Calendar.getInstance();
                    day = cal.get(Calendar.DAY_OF_MONTH);
                    month = cal.get(Calendar.MONTH);
                    year = cal.get(Calendar.YEAR);
                    DateDialog();

                }
            });
            dis_end_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cal = Calendar.getInstance();
                    day = cal.get(Calendar.DAY_OF_MONTH);
                    month = cal.get(Calendar.MONTH);
                    year = cal.get(Calendar.YEAR);
                    DateDialog1();
                }
            });
            // general
            title_general = (EditText) findViewById(R.id.title_general);
            summary_general = (EditText) findViewById(R.id.summary_general);
            description_general = (EditText) findViewById(R.id.description_general);
            general_length = (EditText) findViewById(R.id.general_length);
            general_width = (EditText) findViewById(R.id.general_width);
            general_height = (EditText) findViewById(R.id.general_height);
            tax_general = (EditText) findViewById(R.id.tax_general);
            sample_general_min_order = (EditText) findViewById(R.id.sample_general_min_order);
            sample_general_length = (EditText) findViewById(R.id.sample_general_length);
            sample_general_width = (EditText) findViewById(R.id.sample_general_width);
            sample_general_height = (EditText) findViewById(R.id.sample_general_height);
            sample_gross_unit_weight = (EditText) findViewById(R.id.sample_gross_unit_weight);
            ll_international_freight = (LinearLayout) findViewById(R.id.ll_international_freight);
            ll_domestiic_freight = (LinearLayout) findViewById(R.id.ll_domestiic_freight);
            checkbox_international = (CheckBox) findViewById(R.id.checkbox_international);
            checkbox_domestic = (CheckBox) findViewById(R.id.checkbox_domestic);
            checkbox_international.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ll_international_freight.setVisibility(View.VISIBLE);
                    } else {
                        ll_international_freight.setVisibility(View.GONE);
                    }
                }
            });
            checkbox_domestic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ll_domestiic_freight.setVisibility(View.VISIBLE);
                    } else {
                        ll_domestiic_freight.setVisibility(View.GONE);
                    }
                }
            });
            sample_gross_weight_et = (EditText) findViewById(R.id.sample_gross_weight_et);
            three_dots_icon = (ImageView) findViewById(R.id.three_dots_icon);
            my_products_btn = (Button) findViewById(R.id.my_products_btn);
            customizations_btn = (Button) findViewById(R.id.customizations_btn);
            title_add_prod = (EditText) findViewById(R.id.title_add_prod);
            unit_edit = (EditText) findViewById(R.id.unit_edit);
            price_edittext = (EditText) findViewById(R.id.price_edittext);
            min_order_et = (EditText) findViewById(R.id.min_order_et);
            stock_et = (EditText) findViewById(R.id.stock_et);
            discount_et = (EditText) findViewById(R.id.discount_et);
            sample_unit_edit = (EditText) findViewById(R.id.sample_unit_edit);
            pincode_et = (EditText) findViewById(R.id.pincode_et);
            gross_weight_et = (EditText) findViewById(R.id.sample_gross_weight_et);
            max_quantity_et = (EditText) findViewById(R.id.max_quantity_et);
            sample_price_edittext = (EditText) findViewById(R.id.sample_price_edittext);
            delivery_lead_time = (EditText) findViewById(R.id.delivery_lead_time);
            choosefile_btn = (Button) findViewById(R.id.choosefile_btn);
            ll_customizations = (LinearLayout) findViewById(R.id.ll_customizations);
            ll_my_products = (LinearLayout) findViewById(R.id.ll_my_products);
            ll_contracts = (LinearLayout) findViewById(R.id.ll_contracts);
            ll_shipping_type = (LinearLayout) findViewById(R.id.ll_shipping_type);
            ll_buyer_network = (LinearLayout) findViewById(R.id.ll_buyer_network);
            ll_custom_invoice = (LinearLayout) findViewById(R.id.ll_custom_invoice);
            images_recyclerView = findViewById(R.id.images_recyclerView);
            images_recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayout.HORIZONTAL, false);
            images_recyclerView.setLayoutManager(linearLayoutManager);
            images_recyclerView.setItemAnimator(new DefaultItemAnimator());
            confirm_add_icon = findViewById(R.id.confirm_add_icon);
            confirm_add_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count = count + 1;
                    final Dialog customdialog = new Dialog(StoreMyProductsActivity.this);
                    customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    customdialog.setContentView(R.layout.camera_options);
                    customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);

                    rl_capture = (RelativeLayout) customdialog.findViewById(R.id.rl_capture);
                    rl_gallery = (RelativeLayout) customdialog.findViewById(R.id.rl_gallery);

                    rl_capture.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imagePath = FileUtils.launchServiceCamera(StoreMyProductsActivity.this, count, true);
                            customdialog.dismiss();
                        }
                    });
                    rl_gallery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imagePath = FileUtils.launchLicenceGallery(StoreMyProductsActivity.this, count, true);
                            customdialog.dismiss();
                        }
                    });
                    customdialog.show();
//                imagePath = FileUtils.launchServiceCamera(ServiceManagementActivity.this, count, true);
                }
            });
            imageview = (CircleImageView) findViewById(R.id.imageview);
            spinner_sample_price = (Spinner) findViewById(R.id.spinner_sample_price);
            ArrayAdapter sampleprice = new ArrayAdapter(this, android.R.layout.simple_spinner_item, price);
            sampleprice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_sample_price.setAdapter(sampleprice);
            spinner_sample_price.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sample_price_str = parent.getItemAtPosition(position).toString();
                    // preferenceUtils.saveString(PreferenceUtils.SAMPLE_CURRENCY,sample_price_str);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            // spinner general packing size
            spinner_packing_size_general = (Spinner) findViewById(R.id.spinner_packing_size_general);
            ArrayAdapter size = new ArrayAdapter(this, android.R.layout.simple_spinner_item, packingSize);
            size.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_packing_size_general.setAdapter(size);
            spinner_packing_size_general.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    packingSizeStr = parent.getItemAtPosition(position).toString();
                    // preferenceUtils.saveString(PreferenceUtils.EGGS_TYPE,eggs_type);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            spinnersample_packingsize_general = (Spinner) findViewById(R.id.spinnersample_packingsize_general);
            ArrayAdapter samplesize = new ArrayAdapter(this, android.R.layout.simple_spinner_item, packingSize);
            samplesize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinnersample_packingsize_general.setAdapter(samplesize);
            spinnersample_packingsize_general.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sampackingSizeStr = parent.getItemAtPosition(position).toString();
                    // preferenceUtils.saveString(PreferenceUtils.EGGS_TYPE,eggs_type);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            spinner_sample_gross_unit = (Spinner) findViewById(R.id.spinner_sample_gross_unit);
            ArrayAdapter samplegross = new ArrayAdapter(this, android.R.layout.simple_spinner_item, gross_weight_unit);
            samplegross.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_sample_gross_unit.setAdapter(samplegross);
            spinner_sample_gross_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sampleGrossStr = parent.getItemAtPosition(position).toString();
                    // preferenceUtils.saveString(PreferenceUtils.EGGS_TYPE,eggs_type);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            spinner_country_origin = (Spinner) findViewById(R.id.spinner_country_origin);
            ArrayAdapter country_origin = new ArrayAdapter(this, android.R.layout.simple_spinner_item, country);
            country_origin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_country_origin.setAdapter(country_origin);
            spinner_country_origin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    countyOriginStr = parent.getItemAtPosition(position).toString();
                    // preferenceUtils.saveString(PreferenceUtils.EGGS_TYPE,eggs_type);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            spinner_delivery_type = (Spinner) findViewById(R.id.spinner_delivery_type);
            ArrayAdapter deliveryType = new ArrayAdapter(this, android.R.layout.simple_spinner_item, DeliveryType);
            deliveryType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_delivery_type.setAdapter(deliveryType);
            spinner_delivery_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    deliveryTypeStr = parent.getItemAtPosition(position).toString();
                    // preferenceUtils.saveString(PreferenceUtils.EGGS_TYPE,eggs_type);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
          /*  ArrayAdapter sampleprice = new ArrayAdapter(this, android.R.layout.simple_spinner_item, price);
            sampleprice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_sample_price.setAdapter(sampleprice);
            spinner_sample_price.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sample_price_str = parent.getItemAtPosition(position).toString();
                    // preferenceUtils.saveString(PreferenceUtils.SAMPLE_CURRENCY,sample_price_str);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });*/

            spinner_eggs_types = (Spinner) findViewById(R.id.spinner_eggs_types);
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, eggsType);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_eggs_types.setAdapter(aa);
            spinner_eggs_types.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    eggs_type = parent.getItemAtPosition(position).toString();
                    preferenceUtils.saveString(PreferenceUtils.EGGS_TYPE,eggs_type);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
//  spinner quality
            spinner_quality = (Spinner) findViewById(R.id.spinner_quality);
            ArrayAdapter aa1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, quality);
            aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_quality.setAdapter(aa1);
            spinner_quality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    quality_st = parent.getItemAtPosition(position).toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            // spinner prod category
            spinner_prod_category = (Spinner) findViewById(R.id.spinner_prod_category);
            ArrayAdapter aa2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, prod_category);
            aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_prod_category.setAdapter(aa2);
            spinner_prod_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    prodCategory = parent.getItemAtPosition(position).toString();
                    if(prodCategory.equalsIgnoreCase("Egg")){
                        CategoryId = "146";
                    }else if(prodCategory.equalsIgnoreCase("Poultry")){
                        CategoryId = "145";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            // spinner sector
            spinner_sector = (Spinner) findViewById(R.id.spinner_sector);
            ArrayAdapter aa3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sector);
            aa3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_sector.setAdapter(aa3);
            spinner_sector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sector_st = parent.getItemAtPosition(position).toString();
                    if(sector_st.equalsIgnoreCase("B2B(Business to Business)")){
                        SectorId = "1";
                    }else if(sector_st.equalsIgnoreCase("Buyer network")){
                        SectorId = "2";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            // listing type
            spinner_list_type = (Spinner) findViewById(R.id.spinner_list_type);
            ArrayAdapter aa4 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listingtype);
            aa4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_list_type.setAdapter(aa4);
            spinner_list_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    listingtype_st = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            // unit
            spinner_unit = (Spinner) findViewById(R.id.spinner_unit);
            ArrayAdapter aa5 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, unit);
            aa5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_unit.setAdapter(aa5);
            spinner_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    unit_sp_st = parent.getItemAtPosition(position).toString();
                    if(unit_sp_st.equalsIgnoreCase("Crate")){
                        UnitId = "17";
                    }else  if(unit_sp_st.equalsIgnoreCase("Box")){
                        UnitId = "18";
                    }else{
                        UnitId = "";
                    }
                    // preferenceUtils.saveString(PreferenceUtils.UNIT, unit_sp_st);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
//  Static INR price(Iy
            spinner_price = (Spinner) findViewById(R.id.spinner_price);
        ArrayAdapter aa6 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,price);
        aa6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_price.setAdapter(aa6);
        spinner_price.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                price_sp_st = parent.getItemAtPosition(position).toString();
                preferenceUtils.saveString(PreferenceUtils.PRICE,price_sp_st);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

            //spinner_received_offers
            spinner_received_offers = (Spinner) findViewById(R.id.spinner_received_offers);
            ArrayAdapter received_offer_sp = new ArrayAdapter(this, android.R.layout.simple_spinner_item, receivedOffers);
            received_offer_sp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_received_offers.setAdapter(received_offer_sp);
            spinner_received_offers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    received_offers_st = parent.getItemAtPosition(position).toString();
                    preferenceUtils.saveString(PreferenceUtils.ReceivedOffersList, received_offers_st);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });


            // sample pack type
            spinner_sample_pack_type = (Spinner) findViewById(R.id.spinner_sample_pack_type);
            ArrayAdapter aa7 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, samplepacktype);
            aa7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_sample_pack_type.setAdapter(aa7);
            spinner_sample_pack_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sample_packtype = parent.getItemAtPosition(position).toString();
                    //preferenceUtils.saveString(PreferenceUtils.Sample_PackType, sample_packtype);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            spinner_shipping_type = (TextView) findViewById(R.id.spinner_shipping_type);
            spinner_shipping_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call Vehicle API
                    getVendorServicesList();
                }
            });

            spinner_shipping = (Spinner) findViewById(R.id.spinner_shipping);
            ArrayAdapter aa8 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, shipping);
            aa8.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_shipping.setAdapter(aa8);
            spinner_shipping.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    shipping_st = parent.getItemAtPosition(position).toString();
                    if (shipping_st.equalsIgnoreCase("Yes")) {
                        ll_choose_shipping_types.setVisibility(View.VISIBLE);
                        //  getVendorServicesList();
/*
                        ll_choose_shipping_types.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getVendorServicesList();
                            }
                        });
*/
                    } else {
                        ll_choose_shipping_types.setVisibility(View.GONE);
                    }
                    // preferenceUtils.saveString(PreferenceUtils.Shipping,shipping_st);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            sp_country = (Spinner) findViewById(R.id.spinner_country);
            ArrayAdapter aa9 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, country);
            aa9.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            sp_country.setAdapter(aa9);
            sp_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    country_st = parent.getItemAtPosition(position).toString();
                    preferenceUtils.saveString(PreferenceUtils.COUNTRY, country_st);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            sp_state = (Spinner) findViewById(R.id.spinner_state);
            ArrayAdapter aa_state = new ArrayAdapter(this, android.R.layout.simple_spinner_item, state);
            aa_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            sp_state.setAdapter(aa_state);
            sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    state_st = parent.getItemAtPosition(position).toString();
                    preferenceUtils.saveString(PreferenceUtils.STATE, state_st);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            sp_city = (Spinner) findViewById(R.id.spinner_city);
            ArrayAdapter aa_city = new ArrayAdapter(this, android.R.layout.simple_spinner_item, city);
            aa_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAd
            // adapter data on the Spinner
            sp_city.setAdapter(aa_city);
            sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    city_st = parent.getItemAtPosition(position).toString();
                    preferenceUtils.saveString(PreferenceUtils.CITY, city_st);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            spinner_sample_gross_weight = (Spinner) findViewById(R.id.spinner_sample_gross_weight);
            ArrayAdapter aa_sam_gross_wt = new ArrayAdapter(this, android.R.layout.simple_spinner_item, gross_weight_unit);
            aa_sam_gross_wt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAd
            // adapter data on the Spinner
            spinner_sample_gross_weight.setAdapter(aa_sam_gross_wt);
            spinner_sample_gross_weight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sample_gross_weight_sp = parent.getItemAtPosition(position).toString();
                    //  preferenceUtils.saveString(PreferenceUtils.SAMPLE_GROSS_WEIGHT, sample_gross_weight_sp);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            spinner_sample_unit = (Spinner) findViewById(R.id.spinner_sample_unit);
            ArrayAdapter sample_unit_weight = new ArrayAdapter(this, android.R.layout.simple_spinner_item, gross_weight_unit);
            sample_unit_weight.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAd
            // adapter data on the Spinner
            spinner_sample_unit.setAdapter(sample_unit_weight);
            spinner_sample_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sample_weight_unit_sp = parent.getItemAtPosition(position).toString();
                    if(sample_weight_unit_sp.equalsIgnoreCase("Crate")){
                        UnitId = "17";
                    }else  if(sample_weight_unit_sp.equalsIgnoreCase("Box")){
                        UnitId = "18";
                    }else{
                        UnitId = "";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            spinner_pack_type = (Spinner) findViewById(R.id.spinner_pack_type);
            ArrayAdapter pack_type = new ArrayAdapter(this, android.R.layout.simple_spinner_item, samplepacktype);
            pack_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAd
            // adapter data on the Spinner
            spinner_pack_type.setAdapter(pack_type);
            spinner_pack_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    packTypeStr = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        address_txt_map = findViewById(R.id.address_txt_map);
        address_txt_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(StoreMyProductsActivity.this, MapsActivity.class);
                mapIntent.putExtra("activity", "store");
                mapIntent.putExtra("type", "none");
                startActivity(mapIntent);
            }
        });

        // on click events
        add_product_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_role.equalsIgnoreCase("")) {
                    ll_add_products_store.setVisibility(View.GONE);
                    ll_addproduct_general.setVisibility(View.VISIBLE);
                    ll_contracts.setVisibility(View.GONE);
                    add_product_store.setVisibility(View.GONE);
                    //getCurrencyList();
                    TitleStr = "Add Product";
                    title_store.setText(TitleStr);
                    ll_prod_list.setVisibility(View.GONE);

                } else if (user_role.equalsIgnoreCase("poultry")) {
                    ll_add_products_store.setVisibility(View.VISIBLE);
                    ll_addproduct_general.setVisibility(View.GONE);
                    ll_contracts.setVisibility(View.GONE);
                    add_product_store.setVisibility(View.GONE);
                    address_txt_map.setText("Choose From Map");
                    address_txt_map.setTextColor(getResources().getColor(R.color.appcolor));
                   // getCurrencyList();
                    TitleStr = "Add Product";
                    title_store.setText(TitleStr);
                    ll_prod_list.setVisibility(View.GONE);

                }
            }
        });
        save_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isUpdate) {
                    if (title_add_prod.getText().toString().trim().isEmpty() || min_order_et.getText().toString().trim().isEmpty() ||
                            eggs_type.equalsIgnoreCase("select eggs type")||unit_edit.getText().toString().isEmpty() ||
                            unit_sp_st.equalsIgnoreCase("select unit") || price_edittext.getText().toString().isEmpty()) {
                        Toast.makeText(StoreMyProductsActivity.this, "Please fill Mandatory fields", Toast.LENGTH_SHORT).show();
                    }else if(imagesList.size() == 0 || imagesList.get(position).getIsPrimary().equalsIgnoreCase("no")){
                        Toast.makeText(StoreMyProductsActivity.this, "Required! Please Select Atleast One Image", Toast.LENGTH_SHORT).show();
                    }
                    else if(Product_Address_Map.isEmpty()) {
                        Toast.makeText(StoreMyProductsActivity.this, "Address is required", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        setAddProductJsonObject();
                    }
                }else{
                    setEditProductJsonObject();
                }
            }
        });
        my_products_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_my_products.setVisibility(View.VISIBLE);
                ll_customizations.setVisibility(View.GONE);
                my_products_btn.setTextColor(getResources().getColor(R.color.white));
                customizations_btn.setTextColor(getResources().getColor(R.color.black));
                my_products_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                customizations_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
            }
        });
        customizations_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                three_dots_icon.setVisibility(View.VISIBLE);
                ll_shipping_types_list.setVisibility(View.VISIBLE);
                ll_shipping_type.setVisibility(View.GONE);
                ll_buyer_network.setVisibility(View.GONE);
                ll_custom_invoice.setVisibility(View.GONE);
                ll_add_vendor_areas.setVisibility(View.GONE);
                ll_customizations.setVisibility(View.VISIBLE);
                ll_my_products.setVisibility(View.GONE);
                customizations_btn.setTextColor(getResources().getColor(R.color.white));
                my_products_btn.setTextColor(getResources().getColor(R.color.black));
                customizations_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                my_products_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
                getCustomizationList();
            }
        });
        three_dots_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(StoreMyProductsActivity.this, v);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.customization_list, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.shipping_type) {
                            ll_shipping_type.setVisibility(View.VISIBLE);
                            ll_buyer_network.setVisibility(View.GONE);
                            ll_custom_invoice.setVisibility(View.GONE);
                            ll_shipping_types_list.setVisibility(View.GONE);
                            ll_add_vendor_areas.setVisibility(View.GONE);
                        } else if (id == R.id.shipping_types_list) {
                            ll_shipping_types_list.setVisibility(View.VISIBLE);
                            ll_shipping_type.setVisibility(View.GONE);
                            ll_buyer_network.setVisibility(View.GONE);
                            ll_custom_invoice.setVisibility(View.GONE);
                            ll_add_vendor_areas.setVisibility(View.GONE);
                            getCustomizationList();
                        } else if (id == R.id.add_vendor_area) {
                            ll_add_vendor_areas.setVisibility(View.VISIBLE);
                            ll_shipping_type.setVisibility(View.GONE);
                            ll_buyer_network.setVisibility(View.GONE);
                            ll_custom_invoice.setVisibility(View.GONE);
                            ll_shipping_types_list.setVisibility(View.GONE);
                        } else if (id == R.id.buyer_network) {
                            ll_shipping_type.setVisibility(View.GONE);
                            ll_buyer_network.setVisibility(View.VISIBLE);
                            ll_custom_invoice.setVisibility(View.GONE);
                            ll_shipping_types_list.setVisibility(View.GONE);
                            ll_add_vendor_areas.setVisibility(View.GONE);
                        } else if (id == R.id.custom_invoice) {
                            ll_shipping_type.setVisibility(View.GONE);
                            ll_buyer_network.setVisibility(View.GONE);
                            ll_custom_invoice.setVisibility(View.VISIBLE);
                            ll_shipping_types_list.setVisibility(View.GONE);
                            ll_add_vendor_areas.setVisibility(View.GONE);
                        }
                        return true;
                    }
                });
                popup.show(); //showing popup menu
            }
        });

        mapRadioGroup = (RadioGroup) findViewById(R.id.mapRadioGroup);
        mapRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radius_radiobtn:
                        Intent i = new Intent(StoreMyProductsActivity.this, MapsActivity.class);
                        i.putExtra("activity", "store");
                        i.putExtra("type", "radius");
                        startActivity(i);
                        break;
                    case R.id.p2p_radiobtn:
                        Intent intent = new Intent(StoreMyProductsActivity.this, MapsActivity.class);
                        intent.putExtra("activity", "store");
                        intent.putExtra("type", "p2p");
                        startActivity(intent);
                        break;
                }
            }
        });
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TitleStr.equalsIgnoreCase("Add Product") || TitleStr.equalsIgnoreCase("Edit Product")) {
                    Intent i = new Intent(StoreMyProductsActivity.this, StoreMyProductsActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(StoreMyProductsActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        getMyProductList();
        // getVendorServicesList();
    }
    @Override
    public void onBackPressed() {
        if (TitleStr.equalsIgnoreCase("Add Product") || TitleStr.equalsIgnoreCase("Edit Product")) {
            Intent i = new Intent(StoreMyProductsActivity.this, StoreMyProductsActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(StoreMyProductsActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
    public void DateDialog() {

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                dis_start_date_text.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

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

                dis_end_date_text.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

            }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dpDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE) {
            if (resultCode == RESULT_OK) {
                if (new File(imagePath).exists()) {
                    helper.showLoader(StoreMyProductsActivity.this, "Loading image", "Please wait...");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                imagePath = FileUtils.processImage(imagePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        helper.hideLoader();
                                        Bitmap bitmapImage = null;
                                        try {
                                            bitmapImage = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(new File(imagePath)));
                                            profileString = getEncodedImage(bitmapImage);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        ImageModel imageModel = new ImageModel(
                                                profileString,
                                                "no"
                                        );
                                        imagesList.add(imageModel);
                                        setUpRecyclerView(imagePath);

                                    }
                                });
                            }
                        }
                    }).start();
                }
               // onCaptureImageResult(data);
            }
        } else if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    ClipData clipData = data.getClipData();
                    if (clipData != null) {
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            Uri uri = item.getUri();

                            //In case you need image's absolute path
                            String path = getRealPathFromURI(StoreMyProductsActivity.this, uri);
                        }
                    }
                }
            }
        }
    }

    private File getOutputImageFile(int mediaTypeImage) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraImage");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraVideo", "Failed to create directory MyCameraImage.");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (mediaTypeImage == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }
    private void onCaptureImageResult(Intent data) {
        if (data != null) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            File destination = new File(getApplicationContext().getFilesDir().getPath(),
                    System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
                PickedImgPath = destination.getAbsolutePath();
                Log.e("Camera Path", destination.getAbsolutePath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            profileString = getEncodedImage(bmp);

            profile_img.setImageBitmap(bmp);
        } else {

        }
    }
    public String getRealPathFromURI(Context context, Uri contentUri) {
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor c = getContentResolver().query(contentUri, filePath, null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePath[0]);
        imagePath = c.getString(columnIndex);
        Bitmap bitmapImage = null;
        try {
            bitmapImage = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), contentUri);
            profileString = getEncodedImage(bitmapImage);
            ImageModel imageModel = new ImageModel(
                    profileString,
                    "no"
            );
            imagesList.add(imageModel);
            setUpRecyclerView(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Selected Image path: ", imagePath);
        c.close();
        return "";
    }
    public void editProduct(int positionValue) {
        TitleStr = "Edit Product";
        title_store.setText(TitleStr);
        ll_add_products_store.setVisibility(View.VISIBLE);
        imageview.setVisibility(View.VISIBLE);
        ll_prod_list.setVisibility(View.GONE);
        isUpdate = true;
        position = positionValue;
        add_prod_st = myProductsModelList.get(position).getPr_title();
        price_st = myProductsModelList.get(position).getPr_price();
        min_order_st = myProductsModelList.get(position).getPr_min();
        discount_st = myProductsModelList.get(position).getPr_discount();
        unit_st = myProductsModelList.get(position).getPr_weight();
        if(myProductsModelList.get(position) != null) {
            Picasso.get()
                    .load(myProductsModelList.get(position).getPr_image())
                    .placeholder(R.drawable.no_image_available)
                    .error(R.drawable.no_image_available)
                    .into(imageview);
        }else{
            Picasso.get()
                    .load(R.drawable.no_image_available)
                    .placeholder(R.drawable.no_image_available)
                    .error(R.drawable.no_image_available)
                    .into(imageview);

        }
        title_add_prod.setText(add_prod_st);
        price_edittext.setText(price_st);
        min_order_et.setText(min_order_st);
        discount_et.setText(discount_st);
        unit_edit.setText(unit_st);
        address_txt_map.setText(myProductsModelList.get(position).getAddress());
    }
    public void progressdialog() {
        progressDialog = new ProgressDialog(StoreMyProductsActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    // get list of products
    public void getMyProductList() {
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ProductsApi service = retrofit.create(ProductsApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getMyProducts(user_id);// user_id : 453

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
                                        //   message = root.getString("message");
                                        if (success == 1) {
                                            JSONArray jsonArray = root.getJSONArray("getmyproduct");
                                            myProductsModelList = new ArrayList<MyProductsModel>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                productId = jsonObject.getString("id");
                                                String pr_userid = jsonObject.getString("pr_userid");
                                                pr_bussid = jsonObject.getString("pr_bussid");
                                                String pr_title = jsonObject.getString("pr_title");
                                                String pr_image = jsonObject.getString("pr_image");
                                                String pr_sku = jsonObject.getString("pr_sku");
                                                String pr_weight = jsonObject.getString("pr_weight");
                                                String pr_price = jsonObject.getString("pr_price");
                                                String pr_min = jsonObject.getString("pr_min");
                                                String pr_discount = jsonObject.getString("pr_discount");
                                                String imagepath = jsonObject.getString("imagepath");
                                                String pr_type = jsonObject.getString("pr_type");
                                                String makeoffer = jsonObject.getString("makeoffer");
                                                String prod_image = imagepath + pr_image;
                                                String status = jsonObject.getString("status");
                                                String address = jsonObject.getString("address");

                                                preferenceUtils.saveString(PreferenceUtils.STORE_PRO_ID, productId);
                                                preferenceUtils.saveString(PreferenceUtils.USER_ID, user_id);
                                                preferenceUtils.saveString(PreferenceUtils.Buss_ID, pr_bussid);
                                                preferenceUtils.saveString(PreferenceUtils.STATUS, status);

                                                MyProductsModel myProductsModel = new MyProductsModel();
                                                myProductsModel.setId(productId);
                                                myProductsModel.setPr_userid(pr_userid);
                                                myProductsModel.setPr_bussid(pr_bussid);
                                                myProductsModel.setPr_title(pr_title);
                                                myProductsModel.setPr_sku(pr_sku);
                                                myProductsModel.setPr_weight(pr_weight);
                                                myProductsModel.setPr_price(pr_price);
                                                myProductsModel.setPr_min(pr_min);
                                                myProductsModel.setPr_discount(pr_discount);
                                                myProductsModel.setPr_type(pr_type);
                                                myProductsModel.setMakeoffer(makeoffer);
                                                myProductsModel.setPr_image(prod_image);
                                                myProductsModel.setStatus(status);
                                                myProductsModel.setAddress(address);
                                                myProductsModelList.add(myProductsModel);
                                                progressDialog.dismiss();
                                            }
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (myProductsModelList != null) {
                                        progressDialog.dismiss();
                                        storeMyProductsAdapter = new StoreMyProductsAdapter(StoreMyProductsActivity.this, myProductsModelList);
                                        recycler_view_products_store.setAdapter(storeMyProductsAdapter);
                                        storeMyProductsAdapter.notifyDataSetChanged();
                                    } else {
                                        progressDialog.dismiss();
                                        // Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
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
    public void getVendorServicesList() {
        // progresDailog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductsApi service = retrofit.create(ProductsApi.class);

        Call<JsonElement> callRetrofit = null;
//        callRetrofit = service.getVendorServices(user_id);
        callRetrofit = service.getVendorServices(user_id);
        callRetrofit.enqueue(new Callback<JsonElement>() {

            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                System.out.println("----------------------------------------------------");
                Log.d("Call request", call.request().toString());
                Log.d("Call request header", call.request().headers().toString());
                Log.d("Response raw header", response.headers().toString());
                Log.d("Response raw", String.valueOf(response.raw().body()));
                Log.d("Response code", String.valueOf(response.code()));

                System.out.println("----------------------------------------------------");

                if (response.isSuccessful()) {
                    //  progressDialog.dismiss();
                    if (response.body().toString() != null) {

                        if (response != null) {
                            String searchResponse = response.body().toString();
                            Log.d("Reg", "Response  >>" + searchResponse.toString());

                            if (searchResponse != null) {
                                JSONObject root = null;
                                try {
                                    root = new JSONObject(searchResponse);
                                    String success = root.getString("success");
                                    if (success.equalsIgnoreCase("1")) {
                                        JSONArray jsonArray = root.getJSONArray("shippingtypes");
                                        shippingList = new ArrayList<>();
                                        List<String> idList = new ArrayList<>();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            idList.add(jsonObject.getString("id"));
                                            shippingList.add(jsonObject.getString("pr_title"));
                                        }
                                        //In response data
                                        progressDialog.dismiss();
                                        showAlertDialog(shippingList, idList);
                                    } else {

                                    }
/*
                                    if(success.equalsIgnoreCase("1")) {
                                        JSONArray msg = root.getJSONArray("shippingtypes");
                                        addProductVendorServices = new ArrayList<>();
                                        for (int i = 0; i < msg.length(); i++) {
                                            JSONObject Obj = msg.getJSONObject(i);

                                            String id = Obj.getString("id");
                                            String pr_title = Obj.getString("pr_title");

                                            AddProductVendorServices addProductVendorService = new AddProductVendorServices();
                                            addProductVendorService.setId(id);
                                            addProductVendorService.setPr_title(pr_title);
                                            addProductVendorServices.add(addProductVendorService);
                                            //   progressDialog.dismiss();
                                        }
                                        if(addProductVendorServices != null){
                                            addVendorServicesAdapter = new AddVendorServicesAdapter(StoreMyProductsActivity.this,addProductVendorServices,mSelectedItemsListIds);
                                            spinner_choose_shipping_types.setAdapter(addVendorServicesAdapter);
                                            addVendorServicesAdapter.notifyDataSetChanged();

                                        }
                                    }
*/
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                // progressDialog.dismiss();
//                Toast.makeText(ProductCategories.this,"Server not responding", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showAlertDialog(final List<String> shippingList, final List<String> shippingIdList) {
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(StoreMyProductsActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_list_category, null);
            dialogBuilder.setView(dialogView);

            Button submitCat_btn = (Button) dialogView.findViewById(R.id.submitCat_btn);
            final ListView categoryListView = (ListView) dialogView.findViewById(R.id.categoryList);
            final ShimmerFrameLayout shimmerFrameLayout = dialogView.findViewById(R.id.shimmer_list_item);
            shimmerFrameLayout.startShimmerAnimation();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper.stopShimmer(shimmerFrameLayout);
                }
            }, 3000);
            alertDialog = dialogBuilder.create();
            if (shippingList.size() == 0) {
                if (alertDialog != null)
                    alertDialog.dismiss();
                Toast.makeText(StoreMyProductsActivity.this, "No Items Available", Toast.LENGTH_SHORT).show();
            }
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
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_multiple_choice, shippingList);
            // Assign adapter to ListView
            categoryListView.setAdapter(adapter);
            categoryListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            if (shippingSelectedPosition.size() > 0) {
                for (int pos = 0; pos < shippingSelectedPosition.size(); pos++) {
                    categoryListView.setItemChecked(shippingSelectedPosition.get(pos), true);
                }
            }
            submitCat_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    String itemValue = shippingList.get(position);
                    SparseBooleanArray checked = categoryListView.getCheckedItemPositions();
                    ArrayList<String> selectedItems = new ArrayList<String>();
                    shippingSelectedPosition = new ArrayList<>();
                    shippingSelectedList = new ArrayList<>();
                    for (int i = 0; i < checked.size(); i++) {
                        // Item position in adapter
                        int position = checked.keyAt(i);
                        // Add sport if it is checked i.e.) == TRUE!
                        if (checked.valueAt(i)) {
                            selectedItems.add(adapter.getItem(position));
                            shippingSelectedList.add(shippingIdList.get(position));
                            shippingSelectedPosition.add(position);
                        }
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    if (selectedItems.size() > 0) {
                        for (int i = 0; i < selectedItems.size(); i++) {
                            if (i == selectedItems.size() - 1) {
                                stringBuilder.append(selectedItems.get(i));
                            } else {
                                stringBuilder.append(selectedItems.get(i) + ", ");
                            }
                        }
                        spinner_shipping_type.setText(stringBuilder.toString());
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(StoreMyProductsActivity.this, "Please select one shipping type!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setAddProductJsonObject() {
        String imageBase64;
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("pr_title", title_add_prod.getText().toString());
            jsonObj.put("pr_catid", CategoryId);
            jsonObj.put("pr_eggtype", eggs_type);
            jsonObj.put("shipingprovide", shipping_st);
            // shipping type array
            JSONArray shippingTypeArray = new JSONArray();
            for (int j = 0; j < shippingSelectedList.size(); j++) {
                JSONObject shippingObj = new JSONObject();
                shippingObj.put("shipping_type", shippingSelectedList.get(j));
                shippingTypeArray.put(shippingObj);
            }
            jsonObj.put("shippingTypeArray", shippingTypeArray);
            Log.e("shippingTypeArray", "" + jsonObj);

            jsonObj.put("pr_userid", user_id);
            jsonObj.put("pr_bussid", "");
            jsonObj.put("pr_price", price_edittext.getText().toString());
            jsonObj.put("pr_currency", "INR");
            jsonObj.put("pr_stocks", stock_et.getText().toString());
            jsonObj.put("pr_min", min_order_et.getText().toString());
            // Image Array
        //    JsonParser jsonParser = new JsonParser();
            JSONArray imageArray = new JSONArray();
            for (int j = 0; j < imagesList.size(); j++) {
                JSONObject imageObj = new JSONObject();
                imageBase64 = imagesList.get(j).getImage_base64();
                imageObj.put("img","data:image/jpeg;base64,"+imageBase64.replace("\\", ""));
//                 imageObj.put("img", URLEncoder.encode(imageBase64, "UTF-8"));
                imageObj.put("isPrimary", imagesList.get(j).getIsPrimary());
                imageArray.put(imageObj);
            }
            jsonObj.put("image", imageArray);
            Log.e("image_Array", "" + jsonObj);

            jsonObj.put("pr_type", SectorId);
            jsonObj.put("pr_quality", quality_st);
            jsonObj.put("packaging", packTypeStr);
            jsonObj.put("job_location", Product_Address_Map);
            jsonObj.put("city", city_st);
            jsonObj.put("state", state_st);
            jsonObj.put("country", country_st);
            jsonObj.put("weight", unit_edit.getText().toString());
            jsonObj.put("weight_unit", UnitId);
            jsonObj.put("makeoffer", received_offers_st);
            jsonObj.put("zipcode", pincode_et.getText().toString());
            jsonObj.put("sampleweightunit", sample_unit_edit.getText().toString());
            jsonObj.put("sgweight", sample_gross_weight_et.getText().toString());
            jsonObj.put("sgweight_unit", sample_gross_weight_sp);
            jsonObj.put("saprd_pack", sample_packtype);
            jsonObj.put("squantity", max_quantity_et.getText().toString());
            jsonObj.put("samplecost", sample_price_edittext.getText().toString());
            jsonObj.put("scurrency", "INR");
            jsonObj.put("sampleweight_unit", sample_weight_unit_sp);
            jsonObj.put("day", delivery_lead_time.getText().toString());

            androidNetworkingAdd(jsonObj);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingAdd(JSONObject jsonObject) {
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Sustownsservice/addproduct")
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
                            String success = response.getString("success");
                            if (success.equalsIgnoreCase("1")) {
                                progressDialog.dismiss();
                                Snackbar snackbar = Snackbar
                                        .make(linearlayout,message, Snackbar.LENGTH_LONG);
                                snackbar.show();
                                Intent i = new Intent(StoreMyProductsActivity.this, StoreMyProductsActivity.class);
                                startActivity(i);
                                //Toast.makeText(StoreMyProductsActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(StoreMyProductsActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
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
    public void setEditProductJsonObject() {
        String imageBase64;
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("productid",myProductsModelList.get(position).getId());
            jsonObj.put("pr_title", title_add_prod.getText().toString());
            jsonObj.put("pr_catid", CategoryId);
            jsonObj.put("pr_eggtype", eggs_type);
            jsonObj.put("shipingprovide", shipping_st);
            // shipping type array
            JSONArray shippingTypeArray = new JSONArray();
            for (int j = 0; j < shippingSelectedList.size(); j++) {
                JSONObject shippingObj = new JSONObject();
                shippingObj.put("shipping_type", shippingSelectedList.get(j));
                shippingTypeArray.put(shippingObj);
            }
            jsonObj.put("shippingTypeArray", shippingTypeArray);
            Log.e("shippingTypeArray", "" + jsonObj);

            jsonObj.put("pr_userid", user_id);
            jsonObj.put("pr_bussid", "");
            jsonObj.put("pr_price", price_edittext.getText().toString());
            jsonObj.put("pr_currency", "INR");
            jsonObj.put("pr_stocks", stock_et.getText().toString());
            jsonObj.put("pr_min", min_order_et.getText().toString());
            // Image Array
            JSONArray imageArray = new JSONArray();
            for (int j = 0; j < imagesList.size(); j++) {
                JSONObject imageObj = new JSONObject();
                imageBase64 = imagesList.get(j).getImage_base64();
                imageObj.put("img","data:image/jpeg;base64,"+imageBase64.replace("\\", ""));
//                 imageObj.put("img", URLEncoder.encode(imageBase64, "UTF-8"));
                imageObj.put("isPrimary", imagesList.get(j).getIsPrimary());
                imageArray.put(imageObj);
            }
            jsonObj.put("image", imageArray);
            Log.e("image_Array", "" + jsonObj);

            jsonObj.put("pr_type", SectorId);
            jsonObj.put("pr_quality", quality_st);
            jsonObj.put("packaging", packTypeStr);
            jsonObj.put("job_location", Product_Address_Map);
            jsonObj.put("city", city_st);
            jsonObj.put("state", state_st);
            jsonObj.put("country", country_st);
            jsonObj.put("weight", unit_edit.getText().toString());
            jsonObj.put("weight_unit", UnitId);
            jsonObj.put("makeoffer", received_offers_st);
            jsonObj.put("zipcode", pincode_et.getText().toString());
            jsonObj.put("sampleweightunit", sample_unit_edit.getText().toString());
            jsonObj.put("sgweight", sample_gross_weight_et.getText().toString());
            jsonObj.put("sgweight_unit", sample_gross_weight_sp);
            jsonObj.put("saprd_pack", sample_packtype);
            jsonObj.put("squantity", max_quantity_et.getText().toString());
            jsonObj.put("samplecost", sample_price_edittext.getText().toString());
            jsonObj.put("scurrency", "INR");
            jsonObj.put("sampleweight_unit", sample_weight_unit_sp);
            jsonObj.put("day", delivery_lead_time.getText().toString());
            androidNetworkingEdit(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworkingEdit(JSONObject jsonObject) {
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Storemanagementser/edittoproduct")
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
                            String success = response.getString("success");
                            if (success.equalsIgnoreCase("1")) {
                                Toast.makeText(StoreMyProductsActivity.this, "Product Updated Successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent i = new Intent(StoreMyProductsActivity.this, StoreMyProductsActivity.class);
                                startActivity(i);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(StoreMyProductsActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
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
    public void getCurrencyList() {
        // progresDailog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApi service = retrofit.create(UserApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getCurrencyCodes();
        callRetrofit.enqueue(new Callback<JsonElement>() {

            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                System.out.println("----------------------------------------------------");
                Log.d("Call request", call.request().toString());
                Log.d("Call request header", call.request().headers().toString());
                Log.d("Response raw header", response.headers().toString());
                Log.d("Response raw", String.valueOf(response.raw().body()));
                Log.d("Response code", String.valueOf(response.code()));

                System.out.println("----------------------------------------------------");

                if (response.isSuccessful()) {
                    //  progressDialog.dismiss();
                    if (response.body().toString() != null) {

                        if (response != null) {
                            String searchResponse = response.body().toString();
                            Log.d("Reg", "Response  >>" + searchResponse.toString());

                            if (searchResponse != null) {
                                JSONObject root = null;
                                try {
                                    root = new JSONObject(searchResponse);
                                    String success = root.getString("success");
                                    currencyCodes = new ArrayList<String>();
                                    if (success.equalsIgnoreCase("1")) {
                                        JSONArray msg = root.getJSONArray("currency");
                                        for (int i = 0; i < msg.length(); i++) {
                                            JSONObject Obj = msg.getJSONObject(i);

                                            String CurrencyCode = Obj.getString("CurrencyCode");
                                            currencyCodes.add(CurrencyCode);
                                            //   progressDialog.dismiss();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                spinner_sample_price = (Spinner) findViewById(R.id.spinner_sample_price);
                                ArrayAdapter<String> adapter;
                                spinner_price = (Spinner) findViewById(R.id.spinner_price);
                                if (currencyCodes != null) {
                                    adapter = new ArrayAdapter<String>(StoreMyProductsActivity.this, android.R.layout.simple_spinner_item, currencyCodes);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner_price.setAdapter(adapter);
                                    //  progressDialog.dismiss();

                                    spinner_price.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                                            currency_st = spinner_price.getSelectedItem().toString();
                                            preferenceUtils.saveString(PreferenceUtils.CURRENCY_CODE, currency_st);
                                            //categories = getCategoriesListModels.get(pos).getId();
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                } else {
                                    //Toast.makeText(ShippingAddressActivity.this, "No Categories available", Toast.LENGTH_SHORT).show();
                                    //  progressDialog.dismiss();
                                }

                                spinner_sample_price = (Spinner) findViewById(R.id.spinner_sample_price);
                                ArrayAdapter<String> adapter1;
                                if (currencyCodes != null) {
                                    adapter1 = new ArrayAdapter<String>(StoreMyProductsActivity.this, android.R.layout.simple_spinner_item, currencyCodes);
                                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner_sample_price.setAdapter(adapter1);
                                    //  progressDialog.dismiss();

                                    spinner_sample_price.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                                            currency_st = spinner_sample_price.getSelectedItem().toString();
                                            preferenceUtils.saveString(PreferenceUtils.CURRENCY_CODE, currency_st);
                                            //categories = getCategoriesListModels.get(pos).getId();
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                } else {
                                    //Toast.makeText(ShippingAddressActivity.this, "No Categories available", Toast.LENGTH_SHORT).show();
                                    //  progressDialog.dismiss();
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                // progressDialog.dismiss();
//                Toast.makeText(ProductCategories.this,"Server not responding", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getCountryList() {
        // progresDailog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApi service = retrofit.create(UserApi.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getCountries();
        callRetrofit.enqueue(new Callback<JsonElement>() {

            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                System.out.println("----------------------------------------------------");
                Log.d("Call request", call.request().toString());
                Log.d("Call request header", call.request().headers().toString());
                Log.d("Response raw header", response.headers().toString());
                Log.d("Response raw", String.valueOf(response.raw().body()));
                Log.d("Response code", String.valueOf(response.code()));

                System.out.println("----------------------------------------------------");

                if (response.isSuccessful()) {
                    //  progressDialog.dismiss();
                    if (response.body().toString() != null) {

                        if (response != null) {
                            String searchResponse = response.body().toString();
                            Log.d("Reg", "Response  >>" + searchResponse.toString());

                            if (searchResponse != null) {
                                JSONObject root = null;
                                try {
                                    root = new JSONObject(searchResponse);
                                    String success = root.getString("success");
                                    countries = new ArrayList<String>();
                                    if (success.equalsIgnoreCase("1")) {
                                        JSONArray msg = root.getJSONArray("currency");
                                        for (int i = 0; i < msg.length(); i++) {
                                            JSONObject Obj = msg.getJSONObject(i);

                                            String country_name = Obj.getString("country_name");
                                            String city_name = Obj.getString("city_name");
                                            countries.add(country_name);
                                            //   progressDialog.dismiss();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ArrayAdapter<String> adapter;
                                sp_country = (Spinner) findViewById(R.id.spinner_country);
                                if (currencyCodes != null) {
                                    adapter = new ArrayAdapter<String>(StoreMyProductsActivity.this, android.R.layout.simple_spinner_item, countries);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    sp_country.setAdapter(adapter);
                                    //  progressDialog.dismiss();

                                    sp_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                                            country_string = sp_country.getSelectedItem().toString();
                                            preferenceUtils.saveString(PreferenceUtils.COUNTRY, country_string);
                                            //categories = getCategoriesListModels.get(pos).getId();
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                } else {
                                    //Toast.makeText(ShippingAddressActivity.this, "No Categories available", Toast.LENGTH_SHORT).show();
                                    //  progressDialog.dismiss();
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                // progressDialog.dismiss();
//                Toast.makeText(ProductCategories.this,"Server not responding", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getCustomizationList() {
        progressdialog();
        //OkHttpClient client = new OkHttpClient.Builder().build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        CustomizationsApi service = retrofit.create(CustomizationsApi.class);

        Call<JsonElement> callRetrofit = null;
        //   callRetrofit = service.getCustmizationTypesList("502");
        callRetrofit = service.getCustmizationTypesList("502");
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
                                        //   message = root.getString("message");
                                        if (success == 1) {
                                            JSONArray jsonArray = root.getJSONArray("getmyservice");
                                            transportGetServices = new ArrayList<TransportGetService>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String id = jsonObject.getString("id");
                                                String pr_userid = jsonObject.getString("pr_userid");
                                                String pr_bussid = jsonObject.getString("pr_bussid");
                                                String pr_title = jsonObject.getString("pr_title");
                                                String transport_type = jsonObject.getString("transport_type");
                                                String vehicle_type = jsonObject.getString("vehicle_type");
                                                String load_type = jsonObject.getString("load_type");
                                                String cname = jsonObject.getString("cname");
                                                String cunit = jsonObject.getString("cunit");
                                                String ccost = jsonObject.getString("ccost");
                                                String cmincharge = jsonObject.getString("cmincharge");

                                                TransportGetService transportGetService = new TransportGetService();
                                                transportGetService.setId(id);
                                                transportGetService.setPr_userid(pr_userid);
                                                transportGetService.setPr_title(pr_title);
                                                transportGetService.setTransport_type(transport_type);
                                                transportGetService.setVehicle_type(vehicle_type);
                                                transportGetService.setLoad_type(load_type);
                                                transportGetService.setCname(cname);
                                                transportGetService.setCunit(cunit);
                                                transportGetService.setCcost(ccost);
                                                transportGetService.setCmincharge(cmincharge);
                                                transportGetServices.add(transportGetService);
                                                progressDialog.dismiss();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (transportGetServices != null) {
                                        myServicesAdapter = new MyServicesAdapter(StoreMyProductsActivity.this, transportGetServices);
                                        shipping_list_recyclerview.setAdapter(myServicesAdapter);
                                        myServicesAdapter.notifyDataSetChanged();
                                        progressDialog.dismiss();
                                    } else {
                                        progressDialog.dismiss();
                                        // Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
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

        address_txt_map.setText(Product_Address_Map);
    }
    private void setUpRecyclerView(String image) {
        if (imagesList.size() > 0) {
            fileList.add(image);
            ImagesAdapter imagesAdapter = new ImagesAdapter(StoreMyProductsActivity.this, fileList,imagesList, true);
            images_recyclerView.setAdapter(imagesAdapter);
            imagesAdapter.notifyDataSetChanged();
        } else {
            images_recyclerView.setVisibility(View.GONE);
        }
    }
}
