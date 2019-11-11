package com.sustown.sustownsapp.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
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
import android.widget.RadioButton;
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
import com.google.gson.JsonElement;
import com.sustown.sustownsapp.Adapters.ImagesAdapter;
import com.sustown.sustownsapp.Adapters.MyServicesAdapter;
import com.sustown.sustownsapp.Adapters.ServiceUnitListAdapter;
import com.sustown.sustownsapp.Api.DZ_URL;
import com.sustown.sustownsapp.Api.TransportApi;
import com.sustown.sustownsapp.Models.Data_Model;
import com.sustown.sustownsapp.Models.ServiceUnitModel;
import com.sustown.sustownsapp.Models.TransportGetService;
import com.sustown.sustownsapp.helpers.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
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
import static com.sustown.sustownsapp.Activities.FileUtils.getPath;

public class ServiceManagementActivity extends AppCompatActivity {
    public static final int RESULT_OK = -1;
    private static final int PICKFILE_RESULT_CODE = 3;
    final int CAMERA_CAPTURE = 2;
    final int PICK_IMAGE = 1;
    public String PickedImgPath = null;
    ImageView backarrow, three_dots_icon;
    CircleImageView licence_image, permit_image;
    Button my_Services_btn, customizations_btn, addservice_btn, save_service, liceneceImg, choose_vehicle_docs, choose_permit_img;
    PreferenceUtils preferenceUtils;
    String user_id, transportStr, vehicleStr, profileString, SelectedButton;
    LinearLayout ll_buyer_network, ll_custom_invoice, ll_customizations, ll_my_products, ll_shipping_type;
    RecyclerView recycler_view_myservices, multi_licence_img_recylerview, multi_permit_img_recylerview;
    MyServicesAdapter myServicesAdapter;
    String[] service = {"Service1", "Service2", "Service3", "Service4"};
    LinearLayout ll_crate_box, ll_add_service, ll_full_load, ll_prod_list, ll_choose_subcategory, ll_choose_category, ll_contracts;
    CheckBox crate_checkbox, box_checkbox;
    RadioGroup radioGroup, mapRadioGroup;
    TextView current_location;
    ProgressDialog progressDialog;
    int position;
    String[] transport = {"Choose Transport Type", "Road"};
    String[] vehicleType = {"Choose Vehicle Type", "Auto", "Pickup/Ace", "Truck/DCM"};
    RadioButton partial_radiobtn, fullload_radiobtn, both_radiobtn;
    ArrayList<TransportGetService> transportGetServices;
    TextView spinner_transport_type, spinner_vehical_type, title_store, choose_category, choose_sub_category, document_name, from_date_text, to_date_text;
    RelativeLayout rl_capture, rl_gallery;
    List<String> transportList = new ArrayList<>();
    List<String> vehicleList = new ArrayList<>();
    List<String> categoriesList = new ArrayList<>(), categorySelectedList = new ArrayList<>(), loadArrayList = new ArrayList<>();
    List<String> subcategoriesList = new ArrayList<>(), subCategorySelectedList = new ArrayList<>(), actionArrayList = new ArrayList<>();
    List<Integer> categorySelectedPosition = new ArrayList<>(), subCategorySelectedPosition = new ArrayList<>();
    List<ServiceUnitModel> unitList = new ArrayList<>(), unitDisplayList = new ArrayList<>(), unitFullDisplayList = new ArrayList<>();
    boolean isUpdate, isTransport = false;
    AlertDialog alertDialog;
    Helper helper;
    String transportId = "", vehicleId = "", ServiceName = "", VehicleId = "", MinLoad = "", MaxLoad = "", categoryId = "", subCateId = "";
    EditText service_name_edit, service_vehicle_id, min_load_service, max_load_service, length_service, width_service, height_service;
    EditText tax_service, discount_service;
    Spinner spinner_body_type, spinner_choose_category;
    RecyclerView spinner_choose_subcategory, recyclerview_category;
    String loadValue = "", actionValue = "", imagePath, name_service_st, serviceId, transport_st, vehicle_st, bodyTypeStr, Lendth, Width;
    Button from_date_ser, to_date_ser, close_category;
    SparseBooleanArray sparseBooleanArray;
    String[] BodyType = {"Choose Type", "Open", "Closed"};
    Button add_partial, add_full_unit;
    RecyclerView partial_recyclerview, full_recyclerview;
    ServiceUnitListAdapter serviceUnitListAdapter;
    LinearLayout ll_partial, ll_full;
    CheckBox service_partial_checkbox, service_full_checkbox, radius_radiobtn, p2p_radiobtn;
    String[] permissions = new String[]{
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    Uri uri,returnUri;
    byte[] fileBytes;
    StringBuilder total = new StringBuilder();
    byte[] bytes;
    ByteArrayOutputStream output;
    String ret, uriString, fileString, finalString, fromDateStr, toDateStr, TitleStr="",filename;
    ArrayList<Data_Model> arrayList = new ArrayList<>();
    RecyclerView images_recyclerView, permit_images_recyclerView;
    ImageButton confirm_add_icon, confirm_permit_add_icon;
    int count = 0;
    List<String> imagesList = new ArrayList<>();
    List<String> fileList = new ArrayList<>();
    List<String> permitimagesList = new ArrayList<>();
    List<String> permitfileList = new ArrayList<>();
    List<String> documentArrayList = new ArrayList<>();
    Bitmap bitmap;
    List<String> imagesEncodedList = new ArrayList<>();
    private Calendar cal;
    private int day;
    private int month;
    private int year;

    public static String getEncodedImage(Bitmap bitmapImage) {
        ByteArrayOutputStream baos;
        baos = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        String encodedImagePatientImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImagePatientImage;
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        Log.d("MIME_TYPE_EXT", extension);
        if (extension != null && extension != "") {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            //  Log.d("MIME_TYPE", type);
        } else {
            FileNameMap fileNameMap = URLConnection.getFileNameMap();
            type = fileNameMap.getContentTypeFor(url);
        }
        return type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_service_management);

        try {
            initializeValues();
            initializeUI();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeValues() {
        isUpdate = false;
        helper = new Helper(this);
        preferenceUtils = new PreferenceUtils(ServiceManagementActivity.this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
        getServices();
        checkPermissions();
    }

    private void initializeUI() {
        ll_contracts = (LinearLayout) findViewById(R.id.ll_contracts);
        my_Services_btn = (Button) findViewById(R.id.my_Services_btn);
        customizations_btn = (Button) findViewById(R.id.customizations_btn);
        title_store = (TextView) findViewById(R.id.title_store);
        addservice_btn = (Button) findViewById(R.id.addservice_btn);
        addservice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_contracts.setVisibility(View.GONE);
                ll_add_service.setVisibility(View.VISIBLE);
                recycler_view_myservices.setVisibility(View.GONE);
                addservice_btn.setVisibility(View.GONE);
                TitleStr = "Add Service";
                title_store.setText(TitleStr);
            }
        });

        ll_my_products = (LinearLayout) findViewById(R.id.ll_my_products);
        ll_customizations = (LinearLayout) findViewById(R.id.ll_customizations);
        ll_prod_list = (LinearLayout) findViewById(R.id.ll_prod_list);
        ll_buyer_network = findViewById(R.id.ll_buyer_network);
        ll_add_service = (LinearLayout) findViewById(R.id.ll_add_service);
        backarrow = (ImageView) findViewById(R.id.backarrow);
        three_dots_icon = findViewById(R.id.three_dots_icon);
        recycler_view_myservices = (RecyclerView) findViewById(R.id.recycler_view_myservices);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ServiceManagementActivity.this, LinearLayoutManager.VERTICAL, false);
        recycler_view_myservices.setLayoutManager(layoutManager);

        service_name_edit = (EditText) findViewById(R.id.service_name_edit);
        service_vehicle_id = (EditText) findViewById(R.id.service_vehicle_id);
        choose_vehicle_docs = (Button) findViewById(R.id.choose_vehicle_docs);
        choose_vehicle_docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getFile = new Intent(Intent.ACTION_GET_CONTENT);
                getFile.setType("*/*");
                startActivityForResult(getFile, PICKFILE_RESULT_CODE);
             /*   Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                startActivityForResult(Intent.createChooser(i, "Choose directory"), 9999);*/
            }
        });
        spinner_transport_type = (TextView) findViewById(R.id.spinner_transport_type);
        spinner_transport_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call Transport API
                getTransportTypeList();
                // Delete when data comes
                  /*  transportList = new ArrayList<>();
                    List<String> idList = new ArrayList<>();
*/
                //In response data
                  /*  transportList.add("Road");
                    idList.add("1");

                    showAlertDialog(true, transportList, idList);*/
            }
        });

        spinner_vehical_type = (TextView) findViewById(R.id.spinner_vehical_type);
        spinner_vehical_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call Vehicle API
                getVehicleTypeList();
            }
        });
        min_load_service = (EditText) findViewById(R.id.min_load_service);
        max_load_service = (EditText) findViewById(R.id.max_load_service);
        spinner_body_type = (Spinner) findViewById(R.id.spinner_body_type);
        ArrayAdapter bodyType = new ArrayAdapter(ServiceManagementActivity.this, android.R.layout.simple_spinner_item, BodyType);
        bodyType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_body_type.setAdapter(bodyType);
        spinner_body_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bodyTypeStr = parent.getItemAtPosition(position).toString();
                // preferenceUtils.saveString(PreferenceUtils.EGGS_TYPE,eggs_type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        length_service = (EditText) findViewById(R.id.length_service);
        width_service = (EditText) findViewById(R.id.width_service);
        height_service = (EditText) findViewById(R.id.height_service);
        tax_service = (EditText) findViewById(R.id.tax_service);
        discount_service = (EditText) findViewById(R.id.discount_service);
        from_date_text = (TextView) findViewById(R.id.from_date_text);
        to_date_text = (TextView) findViewById(R.id.to_date_text);
        from_date_ser = (Button) findViewById(R.id.from_date_ser);
        from_date_ser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal = Calendar.getInstance();
                day = cal.get(Calendar.DAY_OF_MONTH);
                month = cal.get(Calendar.MONTH);
                year = cal.get(Calendar.YEAR);
                DateDialog();

            }
        });
        to_date_ser = (Button) findViewById(R.id.to_date_ser);
        to_date_ser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal = Calendar.getInstance();
                day = cal.get(Calendar.DAY_OF_MONTH);
                month = cal.get(Calendar.MONTH);
                year = cal.get(Calendar.YEAR);
                DateDialog1();

            }
        });
        choose_category = (TextView) findViewById(R.id.choose_category);
        choose_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCategoriesList();
            }
        });
        ll_choose_subcategory = (LinearLayout) findViewById(R.id.ll_choose_subcategory);
        document_name = (TextView) findViewById(R.id.document_name);
        choose_sub_category = (TextView) findViewById(R.id.choose_sub_category);
        choose_sub_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                  getSubCategoriesList();
                setJsonObject(true);
            }
        });

        images_recyclerView = findViewById(R.id.images_recyclerView);
        images_recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayout.HORIZONTAL, false);
        images_recyclerView.setLayoutManager(linearLayoutManager);
        images_recyclerView.setItemAnimator(new DefaultItemAnimator());
        confirm_add_icon = findViewById(R.id.confirm_add_icon);
        confirm_add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedButton = "Licence";
                count = count + 1;
                final Dialog customdialog = new Dialog(ServiceManagementActivity.this);
                customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customdialog.setContentView(R.layout.camera_options);
                customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);

                rl_capture = (RelativeLayout) customdialog.findViewById(R.id.rl_capture);
                rl_gallery = (RelativeLayout) customdialog.findViewById(R.id.rl_gallery);

                rl_capture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imagePath = FileUtils.launchServiceCamera(ServiceManagementActivity.this, count, true);
                        customdialog.dismiss();
                    }
                });
                rl_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imagePath = FileUtils.launchLicenceGallery(ServiceManagementActivity.this, count, true);
                        customdialog.dismiss();
                    }
                });

                customdialog.show();
//                imagePath = FileUtils.launchServiceCamera(ServiceManagementActivity.this, count, true);
            }
        });

        permit_images_recyclerView = findViewById(R.id.permit_images_recyclerView);
        permit_images_recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutPermit = new LinearLayoutManager(getApplicationContext(), LinearLayout.HORIZONTAL, false);
        permit_images_recyclerView.setLayoutManager(linearLayoutPermit);
        permit_images_recyclerView.setItemAnimator(new DefaultItemAnimator());
        confirm_permit_add_icon = findViewById(R.id.confirm_permit_add_icon);
        confirm_permit_add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedButton = "Permit";
                count = count + 1;
                final Dialog customdialog = new Dialog(ServiceManagementActivity.this);
                customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customdialog.setContentView(R.layout.camera_options);
                customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);

                rl_capture = (RelativeLayout) customdialog.findViewById(R.id.rl_capture);
                rl_gallery = (RelativeLayout) customdialog.findViewById(R.id.rl_gallery);

                rl_capture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imagePath = FileUtils.launchPermitCamera(ServiceManagementActivity.this, count, true);
                        customdialog.dismiss();
                    }
                });
                rl_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imagePath = FileUtils.launchPermitGallery(ServiceManagementActivity.this, count, true);
                        customdialog.dismiss();
                    }
                });

                customdialog.show();
            }
        });
        radius_radiobtn = (CheckBox) findViewById(R.id.radius_radiobtn);
        p2p_radiobtn = (CheckBox) findViewById(R.id.p2p_radiobtn);
        radius_radiobtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    actionValue = "Radius";
                    actionArrayList.add(actionValue);
                    Intent i = new Intent(ServiceManagementActivity.this, MapsActivity.class);
                    i.putExtra("activity", "service");
                    i.putExtra("type", "radius");
                    startActivity(i);
                }
            }
        });
        p2p_radiobtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    actionValue = "Point to Point";
                    actionArrayList.add(actionValue);
                    Intent intent = new Intent(ServiceManagementActivity.this, MapsActivity.class);
                    intent.putExtra("activity", "service");
                    intent.putExtra("type", "p2p");
                    startActivity(intent);
                }
            }
        });
        save_service = (Button) findViewById(R.id.save_service);
        save_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ServiceName = service_name_edit.getText().toString().trim();
                    VehicleId = service_vehicle_id.getText().toString().trim();
                    MinLoad = min_load_service.getText().toString().trim();
                    MaxLoad = max_load_service.getText().toString().trim();
                    Lendth = length_service.getText().toString().trim();
                    Width = width_service.getText().toString().trim();

                    if (service_name_edit.getText().toString().isEmpty() || loadArrayList.isEmpty() || actionValue.isEmpty() || MinLoad.isEmpty() || MaxLoad.isEmpty() || Lendth.isEmpty()
                    || Width.isEmpty() || height_service.getText().toString().isEmpty() || bodyTypeStr.equalsIgnoreCase("Choose Type")) {
                        Toast.makeText(ServiceManagementActivity.this, "All fields are mandatory.", Toast.LENGTH_SHORT).show();
                    }
                    else if (actionValue.equalsIgnoreCase("radius") && MapsActivity.radius_edit.getText().toString().trim().isEmpty()) {
                        Toast.makeText(ServiceManagementActivity.this, "Please select to address", Toast.LENGTH_SHORT).show();
                    } else if (!actionValue.isEmpty() && actionValue.equalsIgnoreCase("Point to Point")
                            && MapsActivity.fromPincode.isEmpty()&& MapsActivity.toPincode.isEmpty()) {
                        Toast.makeText(ServiceManagementActivity.this, "Please select from address", Toast.LENGTH_SHORT).show();
                    }/*else if(unitDisplayList.get(position).getCharge().isEmpty() || unitDisplayList.get(position).getMin_charge().isEmpty()||
                            unitDisplayList.get(position).getMax_load().isEmpty() ||unitDisplayList.get(position).getMin_load().isEmpty()||
                            unitFullDisplayList.get(position).getCharge().isEmpty() ||unitFullDisplayList.get(position).getMax_load().isEmpty() ||unitDisplayList.get(position).getMin_charge().isEmpty()){
                        Toast.makeText(ServiceManagementActivity.this, "All fields are mandatory.", Toast.LENGTH_SHORT).show();
                    }*/
                    else if (!isUpdate) {
                        if( transportId.isEmpty() || vehicleId.isEmpty()){
                            Toast.makeText(ServiceManagementActivity.this, "All fields are mandatory.", Toast.LENGTH_SHORT).show();
                        }else {
                            setAddServiceJsonObject();
                        }
                    }else{
                        ll_contracts.setVisibility(View.GONE);
                        ll_add_service.setVisibility(View.VISIBLE);
                        recycler_view_myservices.setVisibility(View.GONE);
                        addservice_btn.setVisibility(View.GONE);
                       // Toast.makeText(ServiceManagementActivity.this, "editttttttttt", Toast.LENGTH_SHORT).show();
                        editServiceJsonObject();
                }

            }
        });

        add_partial = findViewById(R.id.add_partial);
        add_full_unit = findViewById(R.id.add_full_unit);

        partial_recyclerview = findViewById(R.id.partial_recyclerview);
        LinearLayoutManager linearLayoutManagerq1 = new LinearLayoutManager(ServiceManagementActivity.this, LinearLayoutManager.VERTICAL, false);
        partial_recyclerview.setLayoutManager(linearLayoutManagerq1);
        partial_recyclerview.setHasFixedSize(true);
        partial_recyclerview.setItemAnimator(new DefaultItemAnimator());

        full_recyclerview = findViewById(R.id.full_recyclerview);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(ServiceManagementActivity.this, LinearLayoutManager.VERTICAL, false);
        full_recyclerview.setLayoutManager(linearLayoutManager1);
        full_recyclerview.setHasFixedSize(true);
        full_recyclerview.setItemAnimator(new DefaultItemAnimator());
        ll_partial = findViewById(R.id.ll_partial);
        ll_full = findViewById(R.id.ll_full);
        service_partial_checkbox = findViewById(R.id.service_partial_checkbox);
        service_full_checkbox = findViewById(R.id.service_full_checkbox);

        add_partial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = unitDisplayList.size();
                if (size < unitList.size()) {
                    for (int i = 0; i < unitList.size(); i++) {
                        if (unitDisplayList.contains(unitList.get(i))) {
                            // Do nothing
                        } else {
                            unitDisplayList.add(unitList.get(i));
                            serviceUnitListAdapter.notifyItemInserted(unitFullDisplayList.size() - 1);
                            partial_recyclerview.scrollToPosition(unitFullDisplayList.size() - 1);
                            partial_recyclerview.setVisibility(View.VISIBLE);
                            if (serviceUnitListAdapter != null) {
                                serviceUnitListAdapter.notifyDataSetChanged();
                            }
                            break;
                        }
                    }
                }
            }
        });

        add_full_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = unitFullDisplayList.size();
                if (size < unitList.size()) {
                    for (int i = 0; i < unitList.size(); i++) {
                        if (unitFullDisplayList.contains(unitList.get(i))) {
                            // Do nothing
                        } else {
                            unitFullDisplayList.add(unitList.get(i));
                            full_recyclerview.setVisibility(View.VISIBLE);
                            if (serviceUnitListAdapter != null) {
                                serviceUnitListAdapter.notifyDataSetChanged();
                            }
                            break;
                        }
                    }
                }
            }
        });

        service_partial_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    loadValue = "Partial";
                    loadArrayList.add(loadValue);
                    showPartialRecycelrview(true);
                } else {
                    ll_partial.setVisibility(View.GONE);
                }
            }
        });

        service_full_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    loadValue = "Full_load";
                    loadArrayList.add(loadValue);
                    showPartialRecycelrview(false);
                } else {
                    ll_full.setVisibility(View.GONE);
                }
            }
        });
        my_Services_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_my_products.setVisibility(View.VISIBLE);
                ll_customizations.setVisibility(View.GONE);
                my_Services_btn.setTextColor(getResources().getColor(R.color.white));
                customizations_btn.setTextColor(getResources().getColor(R.color.black));
                my_Services_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                customizations_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
            }
        });
        customizations_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                three_dots_icon.setVisibility(View.VISIBLE);
                ll_buyer_network.setVisibility(View.VISIBLE);
                ll_customizations.setVisibility(View.VISIBLE);
                ll_my_products.setVisibility(View.GONE);
                customizations_btn.setTextColor(getResources().getColor(R.color.white));
                my_Services_btn.setTextColor(getResources().getColor(R.color.black));
                customizations_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundapp_transparent));
                my_Services_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_square_edges));
            }
        });
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TitleStr.equalsIgnoreCase("Add Service") || TitleStr.equalsIgnoreCase("Edit Service")) {
                    Intent i = new Intent(ServiceManagementActivity.this, ServiceManagementActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(ServiceManagementActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        three_dots_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(ServiceManagementActivity.this, v);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.service_customizations_list, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.buyer_network) {
                            ll_buyer_network.setVisibility(View.VISIBLE);
                            ll_custom_invoice.setVisibility(View.GONE);
                        } else if (id == R.id.custom_invoice) {
                            ll_buyer_network.setVisibility(View.GONE);
                            ll_custom_invoice.setVisibility(View.VISIBLE);
                        }
                        return true;
                    }
                });
                popup.show(); //showing popup menu
            }
        });
        service_vehicle_id.setFocusableInTouchMode(false);
        service_vehicle_id.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                service_vehicle_id.setFocusableInTouchMode(true);
                service_vehicle_id.requestFocus();
                return false;
            }
        });
        service_name_edit.setFocusableInTouchMode(false);
        service_name_edit.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                service_name_edit.setFocusableInTouchMode(true);
                service_name_edit.requestFocus();
                return false;
            }
        });


    }

    private void showPartialRecycelrview(boolean isPartial) {
        if (unitList.size() > 0) {
            if (isPartial) {
                ll_partial.setVisibility(View.VISIBLE);
                unitDisplayList.add(unitList.get(0));
                partial_recyclerview.setVisibility(View.VISIBLE);
                serviceUnitListAdapter = new ServiceUnitListAdapter(ServiceManagementActivity.this, unitDisplayList, unitList, isPartial);
                partial_recyclerview.setAdapter(serviceUnitListAdapter);
            } else {
                ll_full.setVisibility(View.VISIBLE);
                unitFullDisplayList.add(unitList.get(0));
                full_recyclerview.setVisibility(View.VISIBLE);
                serviceUnitListAdapter = new ServiceUnitListAdapter(ServiceManagementActivity.this, unitFullDisplayList, unitList, isPartial);
                full_recyclerview.setAdapter(serviceUnitListAdapter);
            }

        }
    }

    private void browse() {
        try {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICK_IMAGE);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void capture() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, CAMERA_CAPTURE);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE) {
            if (resultCode == RESULT_OK) {
                if (new File(imagePath).exists()) {
                    helper.showLoader(ServiceManagementActivity.this, "Loading image", "Please wait...");
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
                                        if (SelectedButton.equalsIgnoreCase("Licence")) {
                                            imagesList.add(profileString);
                                            // fileList.add(imagePath);
                                            setUpRecyclerView(imagePath);
                                        } else {
                                            permitimagesList.add(profileString);
                                            // permitfileList.add(imagePath);
                                            setUpRecyclerView(imagePath);
                                        }
                                    }
                                });
                            }
                        }
                    }).start();
                } else {
                    String errorMessage = "Image not found!";
                    helper.showToast(ServiceManagementActivity.this, errorMessage);
                }
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
                            String path = getRealPathFromURI(ServiceManagementActivity.this, uri);
                        }
                    }
                }
              /*  imagePath = null;

                Uri picUri = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(picUri, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                imagePath = c.getString(columnIndex);

                Bitmap bitmapImage = null;
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), picUri);
                    profileString = getEncodedImage(bitmapImage);
//                    profile_img.setImageURI(picUri);
                    if(SelectedButton.equalsIgnoreCase("Licence")) {
                        imagesList.add(profileString);
                        fileList.add(imagePath);
                        setUpRecyclerView(imagePath);
                    }else{
                        permitimagesList.add(profileString);
                        permitfileList.add(imagePath);
                        setUpRecyclerView(imagePath);
                    }
                    // licence_image.setImageBitmap(bitmapImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                profile_img.setImageURI(Uri.parse(imagePath));
                Log.d("Selected Image path: ", imagePath);

                c.close();
*/
            }
        }
        else {
            if (requestCode == PICKFILE_RESULT_CODE) {
                if (resultCode == RESULT_OK) {

                    try {
                        returnUri = data.getData();

                   /*     if (filesize >= FILE_SIZE_LIMIT) {
                            Toast.makeText(this,"The selected file is too large. Selet a new file with size less than 2mb",Toast.LENGTH_LONG).show();
                        } else {*/
                        String mimeType = getContentResolver().getType(returnUri);
                        if (mimeType == null) {
                            String path = getPath(this, returnUri);
                            if (path == null) {
                                //   filename = FileUtils.getName(uri.toString());
                            } else {
                                File file = new File(path);
                                filename = file.getName();
                            }
                        } else {
                            returnUri = data.getData();
                            Cursor returnCursor = getContentResolver().query(returnUri, null, null, null, null);
                            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                            returnCursor.moveToFirst();
                            filename = returnCursor.getString(nameIndex);
                            document_name.setText(filename);
                            String size = Long.toString(returnCursor.getLong(sizeIndex));
                        }
                        File fileSave = getExternalFilesDir(null);
                        String sourcePath = getExternalFilesDir(null).toString();
                        // convert file to base64 string
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        String path1 = ret;
                        InputStream in = getContentResolver().openInputStream(returnUri);

                        bytes = getBytes(in);
                        Log.d("data", "onActivityResult: bytes size=" + bytes.length);

                        Log.d("data", "onActivityResult: Base64string=" + Base64.encodeToString(bytes, Base64.DEFAULT));
                        fileString = Base64.encodeToString(bytes, Base64.DEFAULT);
                        documentArrayList.add(fileString);

                        try {
                            copyFileStream(new File(sourcePath + "/" + filename), returnUri,this);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //}
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        /*  else {
            if (requestCode == PICKFILE_RESULT_CODE) {
                if (resultCode == RESULT_OK) {
                    uri = data.getData();
                    uriString = uri.toString();
                    Log.d("data", "onActivityResult: uri" + uriString);
                    File myFile = new File(uriString);
                    String path = myFile.getAbsolutePath();
                    String displayName = null;
                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        cursor = ServiceManagementActivity.this.getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            // set the filename to textview
                            document_name.setText(displayName);
                        }
                    }
                    try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        String path1 = ret;
                        InputStream in = getContentResolver().openInputStream(uri);

                        bytes = getBytes(in);
                        Log.d("data", "onActivityResult: bytes size=" + bytes.length);

                        Log.d("data", "onActivityResult: Base64string=" + Base64.encodeToString(bytes, Base64.DEFAULT));
                        fileString = Base64.encodeToString(bytes, Base64.DEFAULT);
                        documentArrayList.add(fileString);
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                        Log.d("error", "onActivityResult: " + e.toString());
                    }
                }
            }
        }
*/
    }
    private void copyFileStream(File dest, Uri uri, Context context)
            throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;

            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            is.close();
            os.close();
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
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
//                    profile_img.setImageURI(picUri);
            if (SelectedButton.equalsIgnoreCase("Licence")) {
                imagesList.add(profileString);
                setUpRecyclerView(imagePath);
            } else {
                permitimagesList.add(profileString);
                setUpRecyclerView(imagePath);
            }
            // licence_image.setImageBitmap(bitmapImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
//                profile_img.setImageURI(Uri.parse(imagePath));
        Log.d("Selected Image path: ", imagePath);
        c.close();
        return "";
    }
    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {

            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
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
    public void DateDialog() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                fromDateStr = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                from_date_text.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
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
                toDateStr = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                to_date_text.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dpDialog.show();
    }
    public void progressdialog() {
        progressDialog = new ProgressDialog(ServiceManagementActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    private void getServices() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TransportApi service = retrofit.create(TransportApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getTransportServices(user_id);

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
                                                String pr_title = jsonObject.getString("pr_title");
                                                String pr_userid = jsonObject.getString("pr_userid");
                                                String category = jsonObject.getString("category");
                                                String transport_type = jsonObject.getString("transport_type");
                                                String vehicle_type = jsonObject.getString("vehicle_type");
                                                String fromdate = jsonObject.getString("fromdate");
                                                String todate = jsonObject.getString("todate");
                                                String load_type = jsonObject.getString("load_type");
                                                String pr_status = jsonObject.getString("pr_status");

                                                TransportGetService transportGetService = new TransportGetService();
                                                transportGetService.setId(id);
                                                transportGetService.setPr_title(pr_title);
                                                transportGetService.setPr_userid(pr_userid);
                                                transportGetService.setCategory(category);
                                                transportGetService.setTransport_type(transport_type);
                                                transportGetService.setVehicle_type(vehicle_type);
                                                transportGetService.setFromdate(fromdate);
                                                transportGetService.setTodate(todate);
                                                transportGetService.setLoad_type(load_type);
                                                transportGetService.setPr_status(pr_status);
                                                transportGetServices.add(transportGetService);
                                                progressDialog.dismiss();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (transportGetServices != null) {
                                        progressDialog.dismiss();
                                        myServicesAdapter = new MyServicesAdapter(ServiceManagementActivity.this, transportGetServices);
                                        recycler_view_myservices.setAdapter(myServicesAdapter);
                                        myServicesAdapter.notifyDataSetChanged();
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
// edit
    public void editProduct(int positionValue) {
        TitleStr = "Edit Service";
        title_store.setText(TitleStr);
        ll_add_service.setVisibility(View.VISIBLE);
        ll_prod_list.setVisibility(View.GONE);
        isUpdate = true;
        position = positionValue;
        serviceId = transportGetServices.get(position).getId();
        name_service_st = transportGetServices.get(position).getPr_title();
        transport_st = transportGetServices.get(position).getTransport_type();
        vehicle_st = transportGetServices.get(position).getVehicle_type();
        // discount_st = transportGetServices.get(position).getPr_discount();

        service_name_edit.setText(name_service_st);
        spinner_transport_type.setText(transport_st);
        spinner_vehical_type.setText(vehicle_st);
        // discount_et.setText(discount_st);
    }
    private void getTransportTypeList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TransportApi service = retrofit.create(TransportApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getTransportType();

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
                            JSONObject root = null;
                            try {
                                root = new JSONObject(response.body().toString());
                                String success = root.getString("success");
                                if (success.equalsIgnoreCase("1")) {
                                    JSONArray jsonArray = root.getJSONArray("transport");
                                    transportList = new ArrayList<>();
                                    List<String> idList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        transportList.add(jsonObject.getString("transport_type"));
                                        idList.add(jsonObject.getString("id"));
                                    }
                                    //In response data
                                    progressDialog.dismiss();
                                    showAlertDialog(true, transportList, idList);
                                } else {

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
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
    private void getVehicleTypeList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TransportApi service = retrofit.create(TransportApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getVehicleType(transportId);

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
                            JSONObject root = null;
                            try {
                                root = new JSONObject(response.body().toString());
                                String success = root.getString("success");
                                if (success.equalsIgnoreCase("1")) {
                                    JSONArray jsonArray = root.getJSONArray("vehicletype");
                                    vehicleList = new ArrayList<>();
                                    List<String> idList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        idList.add(jsonObject.getString("id"));
                                        vehicleList.add(jsonObject.getString("vehicle_type"));
                                    }
                                    //In response data
                                    progressDialog.dismiss();
                                    showAlertDialog(false, vehicleList, idList);
                                } else {

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

/*
                        if (response.body().toString() != null) {
                            vehicleList = new ArrayList<>();
                            List<String> idList = new ArrayList<>();

                            //In response data
                            vehicleList.add("Auto");
                            idList.add("1");
                            vehicleList.add("Pickup/Ace");
                            idList.add("2");
                            vehicleList.add("Truck/DCM");
                            idList.add("3");

                            progressDialog.dismiss();
                            showAlertDialog(false, vehicleList, idList);
                        }
*/
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
    private void getCategoriesList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TransportApi service = retrofit.create(TransportApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getCategoriesList();

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
                            JSONObject root = null;
                            try {
                                root = new JSONObject(response.body().toString());
                                String status = root.getString("status");
                                String error = root.getString("error");
                                JSONObject responseObj = root.getJSONObject("response");
                                if (error.equalsIgnoreCase("false")) {
                                    JSONArray jsonArray = responseObj.getJSONArray("data");
                                    categoriesList = new ArrayList<>();
                                    List<String> categoryIds = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject catObj = jsonArray.getJSONObject(i);

                                        categoryIds.add(catObj.getString("id"));
                                        categoriesList.add(catObj.getString("title"));
                                    }
                                    showAlertDialog1(true, categoriesList, categoryIds);
                                } else {
                                    Toast.makeText(ServiceManagementActivity.this, "No Categories ", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
    public void setJsonObject(boolean isAlert) {
        try {
            JSONObject jsonObj = new JSONObject();
            JSONArray Product_ids = new JSONArray();
            for (int i = 0; i < categorySelectedList.size(); i++) {
                JSONObject CatidObj = new JSONObject();
                CatidObj.put("Cat_id", categorySelectedList.get(i));
                Product_ids.put(CatidObj);
            }
            jsonObj.put("Product_ids", Product_ids);
            Log.e("Product_ids", "" + jsonObj);

            androidNetworking(jsonObj, isAlert);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void androidNetworking(JSONObject jsonObject, final boolean isAlert) {
        // progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Sustownsservice/get_subcategory")
                .addJSONObjectBody(jsonObject) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "JSON : " + response);
                        try {
                            String status = response.getString("status");
                            String error = response.getString("error");
                            if (error.equalsIgnoreCase("false")) {
                                // progressDialog.dismiss();
                                JSONObject responseObj = response.getJSONObject("response");
                                JSONObject dataObj = responseObj.getJSONObject("data");
                                JSONArray getunitArray = dataObj.getJSONArray("getunit");
                                JSONArray subcategorytypeArray = dataObj.getJSONArray("subcategorytype");
                                subcategoriesList = new ArrayList<>();
                                unitList = new ArrayList<>();
                                List<String> subcategoryIds = new ArrayList<>();
                                for (int i = 0; i < subcategorytypeArray.length(); i++) {
                                    JSONObject catObj = subcategorytypeArray.getJSONObject(i);

                                    subcategoryIds.add(catObj.getString("id"));
                                    subcategoriesList.add(catObj.getString("title"));
                                }
                                for (int j = 0; j < getunitArray.length(); j++) {
                                    JSONObject unitObj = getunitArray.getJSONObject(j);

                                    ServiceUnitModel serviceUnitModel = new ServiceUnitModel(
                                            unitObj.getString("um_id"),
                                            unitObj.getString("unit_name"),
                                            "", "", "", ""
                                    );

                                    unitList.add(serviceUnitModel);
                                }
                                if (isAlert)
                                    showAlertDialog1(false, subcategoriesList, subcategoryIds);
                            } else {
                                // progressDialog.dismiss();
                                Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // progressDialog.dismiss();
                            Toast.makeText(ServiceManagementActivity.this, "No Subcategories Available.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d("Error", "ANError : " + error);
                        // progressDialog.dismiss();
                    }
                });

    }
    //  Add Service Api
    public void setAddServiceJsonObject() {
        try {
            JSONObject jsonObj = new JSONObject();
                jsonObj.put("service", service_name_edit.getText().toString());
                jsonObj.put("prd_userid", user_id);
                jsonObj.put("prd_bussid", "");
                jsonObj.put("vehicle_id", service_vehicle_id.getText().toString());
                jsonObj.put("trans", transportId);
                jsonObj.put("vehicle", vehicleId);
                jsonObj.put("minimumload", min_load_service.getText().toString());
                jsonObj.put("maximumload", max_load_service.getText().toString());
                jsonObj.put("containertype", bodyTypeStr);
                jsonObj.put("vehicle_length", length_service.getText().toString());
                jsonObj.put("vehicle_width", width_service.getText().toString());
                jsonObj.put("vehicle_height", height_service.getText().toString());
                jsonObj.put("taxfield", tax_service.getText().toString());
                jsonObj.put("discountfild", discount_service.getText().toString());
                jsonObj.put("fromdate", fromDateStr);
                jsonObj.put("todate", toDateStr);
// category Array
                JSONArray product_categoryArray = new JSONArray();
                for (int i = 0; i < categorySelectedList.size(); i++) {
                    JSONObject CatidObj = new JSONObject();
                    CatidObj.put("cat_name", categorySelectedList.get(i));
                    product_categoryArray.put(CatidObj);
                }
                jsonObj.put("product_category", product_categoryArray);
                Log.e("product_category", "" + jsonObj);
// subcategory Array
                JSONArray subcategoryArray = new JSONArray();
                for (int i = 0; i < subCategorySelectedList.size(); i++) {
                    JSONObject SubCatidObj = new JSONObject();
                    SubCatidObj.put("subcat_name", subCategorySelectedList.get(i));
                    subcategoryArray.put(SubCatidObj);
                }
                jsonObj.put("subcategory", subcategoryArray);
                Log.e("subcategory", "" + jsonObj);
// load Array
                JSONArray loadArray = new JSONArray();
                for (int i1 = 0; i1 < loadArrayList.size(); i1++) {
                    JSONObject loadObj = new JSONObject();
                    loadObj.put("load_type", loadArrayList.get(i1));
                    loadArray.put(loadObj);
                }
                jsonObj.put("load", loadArray);
                Log.e("load", "" + jsonObj);
//  unit_partial Array
                JSONArray unit_partialArray = new JSONArray();
                for (int j = 0; j < unitDisplayList.size(); j++) {
                    JSONObject unitPartialObj = new JSONObject();
                    unitPartialObj.put("unit_id", unitDisplayList.get(j).getUnit_id());
                    unitPartialObj.put("charge", unitDisplayList.get(j).getCharge());
                    unitPartialObj.put("min_charge", unitDisplayList.get(j).getMin_charge());
                    unitPartialObj.put("min_load", unitDisplayList.get(j).getMin_load());
                    unitPartialObj.put("max_load", unitDisplayList.get(j).getMax_load());
                    unit_partialArray.put(unitPartialObj);
                }
                jsonObj.put("unit_partial", unit_partialArray);
                Log.e("unit_partial", "" + jsonObj);
                // unit_full Array
                JSONArray unit_fullArray = new JSONArray();
                for (int j1 = 0; j1 < unitFullDisplayList.size(); j1++) {
                    JSONObject unitFullObj = new JSONObject();
                    unitFullObj.put("unit_id", unitFullDisplayList.get(j1).getUnit_id());
                    unitFullObj.put("charge", unitFullDisplayList.get(j1).getCharge());
                    unitFullObj.put("min_charge", unitFullDisplayList.get(j1).getMin_charge());
                    unitFullObj.put("max_load", unitFullDisplayList.get(j1).getMax_load());
                    unit_fullArray.put(unitFullObj);
                }
                jsonObj.put("unit_full", unit_fullArray);
                Log.e("unit_full", "" + jsonObj);
// action Array
                JSONArray actionArray = new JSONArray();
                for (int k = 0; k < actionArrayList.size(); k++) {
                    JSONObject actionObj = new JSONObject();
                    actionObj.put("action_type", actionArrayList.get(k));
                    actionArray.put(actionObj);
                }
                jsonObj.put("action", actionArray);
                Log.e("action", "" + jsonObj);
                jsonObj.put("country", "");
                jsonObj.put("state", "");
                jsonObj.put("city", "");
                jsonObj.put("startingpoint", "");
                jsonObj.put("endingpoint", "");
                jsonObj.put("serv", "");
                jsonObj.put("radi_perkm", MapsActivity.radius_edit.getText().toString());
                jsonObj.put("radi_exp", MapsActivity.extradius_edit.getText().toString());
                jsonObj.put("service_pincode", MapsActivity.fromPincode);
                jsonObj.put("service_pincode_test", MapsActivity.toPincode);
                jsonObj.put("trans_ser_latitude", String.valueOf(MapsActivity.lat));
                jsonObj.put("trans_ser_langitude", String.valueOf(MapsActivity.lon));
                jsonObj.put("transport_address", MapsActivity.address);
                jsonObj.put("legal_address", MapsActivity.map_address_edit.getText().toString().trim());
                jsonObj.put("sourcelocation", MapsActivity.from_address_txt.getText().toString().trim());
                jsonObj.put("deslocation", MapsActivity.to_address_edit.getText().toString());
                jsonObj.put("type", "on");
                jsonObj.put("trans_ser_org_latitude", String.valueOf(MapsActivity.fromLatLon.latitude));
                jsonObj.put("trans_ser_org_langitude", String.valueOf(MapsActivity.fromLatLon.longitude));
                jsonObj.put("trans_ser_des_latitude", String.valueOf(MapsActivity.fromLatLon.latitude));
                jsonObj.put("trans_ser_des_langitude", String.valueOf(MapsActivity.toLatLon.longitude));
                jsonObj.put("originPlaceId", "");
                jsonObj.put("destinationPlaceId", "");
                // image Array
                JSONArray permit_imgArray = new JSONArray();
                for (int k = 0; k < permitimagesList.size(); k++) {
                    JSONObject permitObj = new JSONObject();
                    String image = permitimagesList.get(k);
                    permitObj.put("image", image.replaceAll("\\\\",""));
                    permit_imgArray.put(permitObj);
                }
                jsonObj.put("permit_img", permit_imgArray);
                Log.e("permit_img", "" + jsonObj);

                // licence_img Array
                JSONArray licence_imgArray = new JSONArray();
                for (int k = 0; k < imagesList.size(); k++) {
                    JSONObject licenceObj = new JSONObject();
                    String licimage = imagesList.get(k);
                    licenceObj.put("image", licimage.replaceAll("\\\\",""));
                    licence_imgArray.put(licenceObj);
                }
                jsonObj.put("licence_img", licence_imgArray);
                Log.e("licence_img", "" + jsonObj);

                // vehicle_documents
                JSONArray vehicle_documentsArray = new JSONArray();
                for (int k = 0; k < documentArrayList.size(); k++) {
                    JSONObject documentObj = new JSONObject();
                    String document = documentArrayList.get(k);
                    documentObj.put("file",document.replaceAll("\\\\",""));
                    actionArray.put(documentObj);
                }
                jsonObj.put("vehicle_documents", vehicle_documentsArray);
                Log.e("vehicle_documents", "" + jsonObj);

                androidNetworkingAdd(jsonObj);

            } catch(Exception e){
                e.printStackTrace();
            }
        }
        // edit service
        public void editServiceJsonObject () {
            try {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("old_id", serviceId);
                jsonObj.put("service", service_name_edit.getText().toString());
                jsonObj.put("prd_userid", user_id);
                jsonObj.put("prd_bussid", " ");
                jsonObj.put("vehicle_id", service_vehicle_id.getText().toString());
                jsonObj.put("trans", transportId);
                jsonObj.put("vehicle", vehicleId);
                jsonObj.put("minimumload", min_load_service.getText().toString());
                jsonObj.put("maximumload", max_load_service.getText().toString());
                jsonObj.put("containertype", bodyTypeStr);
                jsonObj.put("vehicle_length", length_service.getText().toString());
                jsonObj.put("vehicle_width", width_service.getText().toString());
                jsonObj.put("vehicle_height", height_service.getText().toString());
                jsonObj.put("taxfield", tax_service.getText().toString());
                jsonObj.put("discountfild", discount_service.getText().toString());
                jsonObj.put("fromdate", fromDateStr);
                jsonObj.put("todate", toDateStr);
// category Array
                JSONArray product_categoryArray = new JSONArray();
                for (int i = 0; i < categorySelectedList.size(); i++) {
                    JSONObject CatidObj = new JSONObject();
                    CatidObj.put("cat_name", categorySelectedList.get(i));
                    product_categoryArray.put(CatidObj);
                }
                jsonObj.put("product_category", product_categoryArray);
                Log.e("product_category", "" + jsonObj);
// subcategory Array
                JSONArray subcategoryArray = new JSONArray();
                for (int i = 0; i < subCategorySelectedList.size(); i++) {
                    JSONObject SubCatidObj = new JSONObject();
                    SubCatidObj.put("subcat_name", subCategorySelectedList.get(i));
                    subcategoryArray.put(SubCatidObj);
                }
                jsonObj.put("subcategory", subcategoryArray);
                Log.e("subcategory", "" + jsonObj);
// load Array
                JSONArray loadArray = new JSONArray();
                for (int i1 = 0; i1 < loadArrayList.size(); i1++) {
                    JSONObject loadObj = new JSONObject();
                    loadObj.put("load_type", loadArrayList.get(i1));
                    loadArray.put(loadObj);
                }
                jsonObj.put("load", loadArray);
                Log.e("load", "" + jsonObj);
//  unit_partial Array
                JSONArray unit_partialArray = new JSONArray();
                for (int j = 0; j < unitDisplayList.size(); j++) {
                    JSONObject unitPartialObj = new JSONObject();
                    unitPartialObj.put("unit_id", unitDisplayList.get(j).getUnit_id());
                    unitPartialObj.put("charge", unitDisplayList.get(j).getCharge());
                    unitPartialObj.put("min_charge", unitDisplayList.get(j).getMin_charge());
                    unitPartialObj.put("min_load", unitDisplayList.get(j).getMin_load());
                    unitPartialObj.put("max_load", unitDisplayList.get(j).getMax_load());
                    unit_partialArray.put(unitPartialObj);
                }
                jsonObj.put("unit_partial", unit_partialArray);
                Log.e("unit_partial", "" + jsonObj);
                // unit_full Array
                JSONArray unit_fullArray = new JSONArray();
                for (int j1 = 0; j1 < unitFullDisplayList.size(); j1++) {
                    JSONObject unitFullObj = new JSONObject();
                    unitFullObj.put("unit_id", unitFullDisplayList.get(j1).getUnit_id());
                    unitFullObj.put("charge", unitFullDisplayList.get(j1).getCharge());
                    unitFullObj.put("min_charge", unitFullDisplayList.get(j1).getMin_charge());
                    unitFullObj.put("max_load", unitFullDisplayList.get(j1).getMax_load());
                    unit_fullArray.put(unitFullObj);
                }
                jsonObj.put("unit_full", unit_fullArray);
                Log.e("unit_full", "" + jsonObj);
// action Array
                JSONArray actionArray = new JSONArray();
                for (int k = 0; k < actionArrayList.size(); k++) {
                    JSONObject actionObj = new JSONObject();
                    actionObj.put("action_type", actionArrayList.get(k));
                    actionArray.put(actionObj);
                }
                jsonObj.put("action", actionArray);
                Log.e("action", "" + jsonObj);

                jsonObj.put("country", "");
                jsonObj.put("state", "");
                jsonObj.put("city", "");
                jsonObj.put("startingpoint", "");
                jsonObj.put("endingpoint", "");
                jsonObj.put("serv", "");
                jsonObj.put("radi_perkm", "50");
                jsonObj.put("radi_exp", "100");
                jsonObj.put("service_pincode", MapsActivity.fromPincode);
                jsonObj.put("service_pincode_test", MapsActivity.toPincode);
                jsonObj.put("trans_ser_latitude", String.valueOf(MapsActivity.lat));
                jsonObj.put("trans_ser_langitude", String.valueOf(MapsActivity.lon));
                jsonObj.put("transport_address", "10");
                jsonObj.put("legal_address", "10");
                jsonObj.put("sourcelocation", MapsActivity.from_address_txt.getText().toString().trim());
                jsonObj.put("deslocation", MapsActivity.to_address_edit.getText().toString());
                jsonObj.put("type", "105");
                jsonObj.put("trans_ser_org_latitude", String.valueOf(MapsActivity.fromLatLon.latitude));
                jsonObj.put("trans_ser_org_langitude", String.valueOf(MapsActivity.fromLatLon.longitude));
                jsonObj.put("trans_ser_des_latitude", String.valueOf(MapsActivity.fromLatLon.latitude));
                jsonObj.put("trans_ser_des_langitude", String.valueOf(MapsActivity.toLatLon.longitude));
                jsonObj.put("originPlaceId", "");
                jsonObj.put("destinationPlaceId", "");

                // image Array
                JSONArray permit_imgArray = new JSONArray();
                for (int k = 0; k < permitimagesList.size(); k++) {
                    JSONObject permitObj = new JSONObject();
                    String image = permitimagesList.get(k);
                    permitObj.put("image", image.replaceAll("\\\\",""));
                    permit_imgArray.put(permitObj);
                }
                jsonObj.put("permit_img", permit_imgArray);
                Log.e("permit_img", "" + jsonObj);

                // licence_img Array
                JSONArray licence_imgArray = new JSONArray();
                for (int k = 0; k < imagesList.size(); k++) {
                    JSONObject licenceObj = new JSONObject();
                    String licimage = imagesList.get(k);
                    licenceObj.put("image", licimage.replaceAll("\\\\",""));
                    licence_imgArray.put(licenceObj);
                }
                jsonObj.put("licence_img", licence_imgArray);
                Log.e("licence_img", "" + jsonObj);

                // vehicle_documents
                JSONArray vehicle_documentsArray = new JSONArray();
                for (int k = 0; k < documentArrayList.size(); k++) {
                    JSONObject documentObj = new JSONObject();
                    String document = documentArrayList.get(k);
                    documentObj.put("file",document.replaceAll("\\\\",""));
                    actionArray.put(documentObj);
                }
                jsonObj.put("vehicle_documents", vehicle_documentsArray);
                Log.e("vehicle_documents", "" + jsonObj);

                androidNetworkingEdit(jsonObj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    public void androidNetworkingEdit(JSONObject jsonObject){
        progressdialog();
        AndroidNetworking.post("https://www.sustowns.com/Sustownsservice/add_transport_service")
                .addJSONObjectBody(jsonObject) // posting java object
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "JSON : " + response);
                        try {
                            String status = response.getString("status");
                            String error = response.getString("error");
                            if (error.equalsIgnoreCase("false")) {
                                progressDialog.dismiss();
                                JSONObject responseObj = response.getJSONObject("response");
                                String message = responseObj.getString("message");
                                Toast.makeText(ServiceManagementActivity.this, "Service Updated Successfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ServiceManagementActivity.this, ServiceManagementActivity.class);
                                startActivity(i);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ServiceManagementActivity.this, "Service not Updated", Toast.LENGTH_SHORT).show();
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
    public void androidNetworkingAdd (JSONObject jsonObject){
            progressdialog();
            AndroidNetworking.post("https://www.sustowns.com/Sustownsservice/add_transport_service")
                    .addJSONObjectBody(jsonObject) // posting java object
                    .setTag("test")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Response", "JSON : " + response);
                            try {
                                String status = response.getString("status");
                                String error = response.getString("error");
                                if (error.equalsIgnoreCase("false")) {
                                    progressDialog.dismiss();
                                    JSONObject responseObj = response.getJSONObject("response");
                                    String message = responseObj.getString("message");
                                    Toast.makeText(ServiceManagementActivity.this, "service added successfully", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(ServiceManagementActivity.this, ServiceManagementActivity.class);
                                    startActivity(i);
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(ServiceManagementActivity.this, "Service not added", Toast.LENGTH_SHORT).show();
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
        private void showAlertDialog ( final boolean isTransport, final List<String> transportList,
        final List<String> idList){
            try {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ServiceManagementActivity.this);
                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_list_layout, null);
                dialogBuilder.setView(dialogView);

                TextView title = (TextView) dialogView.findViewById(R.id.customDialogTitle);
                if (isTransport)
                    title.setText("Choose Transport Type");
                else
                    title.setText("Choose Vehicle Type");

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
                if (transportList.size() == 0) {
                    if (alertDialog != null)
                        alertDialog.dismiss();
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        R.layout.simple_list_item, R.id.list_item_txt, transportList);
                // Assign adapter to ListView
                categoryListView.setAdapter(adapter);

                // ListView Item Click Listener
                categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        String itemValue = transportList.get(position);
                        if (isTransport) {
                            spinner_transport_type.setText(itemValue);
                            spinner_vehical_type.setText("");
                            spinner_vehical_type.setHint("Choose Vehicle Type");
                            transportId = idList.get(position);
                            alertDialog.dismiss();
                        } else {
                            spinner_vehical_type.setText(itemValue);
                            vehicleId = idList.get(position);
                            alertDialog.dismiss();
                        }
                    }
                });
                alertDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Alert Dialog for Categories List and subcategories List
        private void showAlertDialog1 ( final boolean isCategory, final List<String> categoryList,
        final List<String> categoryIdList){
            try {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ServiceManagementActivity.this);
                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_list_category, null);
                dialogBuilder.setView(dialogView);

                TextView title = (TextView) dialogView.findViewById(R.id.customDialogTitle);
                Button submitCat_btn = (Button) dialogView.findViewById(R.id.submitCat_btn);
                if (isCategory)
                    title.setText("Choose Category");
                else
                    title.setText("Choose SubCategory");

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
                if (categoryList.size() == 0) {
                    if (alertDialog != null)
                        alertDialog.dismiss();
                    Toast.makeText(ServiceManagementActivity.this, "No Items Available", Toast.LENGTH_SHORT).show();
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
                        android.R.layout.simple_list_item_multiple_choice, categoryList);
                // Assign adapter to ListView
                categoryListView.setAdapter(adapter);
                categoryListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                if (isCategory) {
                    if (categorySelectedPosition.size() > 0) {
                        for (int pos = 0; pos < categorySelectedPosition.size(); pos++) {
                            categoryListView.setItemChecked(categorySelectedPosition.get(pos), true);
                        }
                    }
                } else {
                }
                submitCat_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ll_choose_subcategory.setVisibility(View.VISIBLE);
                        alertDialog.dismiss();
                        String itemValue = categoryList.get(position);
                        if (isCategory) {
                            SparseBooleanArray checked = categoryListView.getCheckedItemPositions();
                            ArrayList<String> selectedItems = new ArrayList<String>();
                            categorySelectedPosition = new ArrayList<>();
                            categorySelectedList = new ArrayList<>();
                            for (int i = 0; i < checked.size(); i++) {
                                // Item position in adapter
                                int position = checked.keyAt(i);
                                // Add sport if it is checked i.e.) == TRUE!
                                if (checked.valueAt(i)) {
                                    selectedItems.add(adapter.getItem(position));
                                    categorySelectedList.add(categoryIdList.get(position));
                                    categorySelectedPosition.add(position);
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
                                choose_category.setText(stringBuilder.toString());
                                choose_sub_category.setText("");
                                choose_sub_category.setHint("Choose Sub Category");
                                alertDialog.dismiss();
                                setJsonObject(false);
                            } else {
                                Toast.makeText(ServiceManagementActivity.this, "Please select one category!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            SparseBooleanArray checked = categoryListView.getCheckedItemPositions();
                            ArrayList<String> selectedItems = new ArrayList<String>();
                            subCategorySelectedPosition = new ArrayList<>();
                            subCategorySelectedList = new ArrayList<>();
                            for (int i = 0; i < checked.size(); i++) {
                                // Item position in adapter
                                int position = checked.keyAt(i);
                                // Add sport if it is checked i.e.) == TRUE!
                                if (checked.valueAt(i)) {
                                    selectedItems.add(adapter.getItem(position));
                                    subCategorySelectedList.add(categoryIdList.get(position));
                                    subCategorySelectedPosition.add(position);
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
                                choose_sub_category.setText(stringBuilder.toString());
                                alertDialog.dismiss();
                            } else {
                                Toast.makeText(ServiceManagementActivity.this, "Please select atleast one subcategory!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                alertDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public void hidePartialRecyclerview () {
            partial_recyclerview.setVisibility(View.GONE);
        }
        public void hideFullRecyclerview () {
            full_recyclerview.setVisibility(View.GONE);
        }
        private void setUpRecyclerView (String image){
            if (imagesList.size() > 0) {
                if (SelectedButton.equalsIgnoreCase("Licence")) {
                    fileList.add(image);
                    ImagesAdapter imagesAdapter = new ImagesAdapter(ServiceManagementActivity.this, fileList,false);
                    images_recyclerView.setAdapter(imagesAdapter);
                    imagesAdapter.notifyDataSetChanged();
                } else if (SelectedButton.equalsIgnoreCase("Permit")) {
                    permitfileList.add(image);
                    ImagesAdapter imagesAdapter = new ImagesAdapter(ServiceManagementActivity.this, permitfileList,false);
                    permit_images_recyclerView.setAdapter(imagesAdapter);
                    imagesAdapter.notifyDataSetChanged();
                }
            } else {
                images_recyclerView.setVisibility(View.GONE);
            }
        }
    }
