package com.sustown.sustownsapp.Adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.sustownsapp.R;
import com.sustown.sustownsapp.Activities.PreferenceUtils;
import com.sustown.sustownsapp.Activities.ServiceManagementActivity;
import com.sustown.sustownsapp.Models.TransportGetService;

import java.util.ArrayList;

public class MyServicesAdapter extends RecyclerView.Adapter<MyServicesAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email, user_id;
    PreferenceUtils preferenceUtils;
    String[] service;
    ArrayList<TransportGetService> transportGetServices;
    ProgressDialog progressDialog;
    TextView transport_service_name, category_name, transport_type, vehicle_type, load_type, from_date, to_date,
            crate_min_charge, box_charge_km, box_text, box_min_charge;
    Button close_dialog;

    public MyServicesAdapter(Context context, ArrayList<TransportGetService> transportGetServices) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.transportGetServices = transportGetServices;
    }

    @Override
    public MyServicesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.myservices_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new MyServicesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyServicesAdapter.ViewHolder viewHolder, final int position) {

        if (transportGetServices.get(position) != null) {
            viewHolder.ser_prod_name.setText(transportGetServices.get(position).getPr_title());
            viewHolder.ser_category.setText(transportGetServices.get(position).getCategory());
            viewHolder.ser_transport_type.setText(transportGetServices.get(position).getTransport_type());
            viewHolder.ser_prod_price.setText(transportGetServices.get(position).getVehicle_type());

        }
        viewHolder.update_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ((ServiceManagementActivity) context).editProduct(position);
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create dialog for buy sample
                final Dialog customdialog = new Dialog(context);
                customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customdialog.setContentView(R.layout.get_service_details);
                customdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                customdialog.getWindow().setBackgroundDrawableResource(R.drawable.squre_corner_shape);

                transport_service_name = (TextView) customdialog.findViewById(R.id.transport_service_name);
                category_name = (TextView) customdialog.findViewById(R.id.category_name);
                transport_type = (TextView) customdialog.findViewById(R.id.transport_type);
                vehicle_type = (TextView) customdialog.findViewById(R.id.vehicle_type);
                load_type = (TextView) customdialog.findViewById(R.id.load_type);
                from_date = (TextView) customdialog.findViewById(R.id.from_date);
                to_date = (TextView) customdialog.findViewById(R.id.to_date);

                transport_service_name.setText(transportGetServices.get(position).getPr_title());
                category_name.setText(transportGetServices.get(position).getCategory());
                transport_type.setText(transportGetServices.get(position).getTransport_type());
                vehicle_type.setText(transportGetServices.get(position).getVehicle_type());
                load_type.setText(transportGetServices.get(position).getLoad_type());
                from_date.setText(transportGetServices.get(position).getFromdate());
                to_date.setText(transportGetServices.get(position).getTodate());
                close_dialog = (Button) customdialog.findViewById(R.id.close_dialog);
                close_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customdialog.dismiss();
                    }
                });
                customdialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return transportGetServices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ser_prod_name, ser_prod_price, ser_category, ser_transport_type,update_service;

        public ViewHolder(View view) {
            super(view);
            ser_prod_name = (TextView) view.findViewById(R.id.ser_prod_name);
            ser_prod_price = (TextView) view.findViewById(R.id.ser_prod_price);
            ser_category = (TextView) view.findViewById(R.id.ser_category);
            ser_transport_type = (TextView) view.findViewById(R.id.ser_transport_type);
            update_service = (TextView) view.findViewById(R.id.update_service);
        }
    }
}

