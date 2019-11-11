package com.sustown.sustownsapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sustownsapp.R;
import com.sustown.sustownsapp.Activities.PreferenceUtils;
import com.sustown.sustownsapp.Activities.ProductDetailsActivity;
import com.sustown.sustownsapp.Models.StoreSentOffersModel;

import java.util.ArrayList;

public class StoreSentOffersAdapter extends RecyclerView.Adapter<StoreSentOffersAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    String user_email,pro_id,user_id,user_role,status,image;
    PreferenceUtils preferenceUtils;
    String[] order;
    ArrayList<StoreSentOffersModel> storeSentOffersModels;
    ProgressDialog progressDialog;

    public StoreSentOffersAdapter(Context context, ArrayList<StoreSentOffersModel> storeSentOffersModels) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.storeSentOffersModels = storeSentOffersModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sent_offers_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        status = storeSentOffersModels.get(position).getStatus();
        if(storeSentOffersModels.get(position) != null){
            viewHolder.prod_name.setText(storeSentOffersModels.get(position).getPr_title());
            viewHolder.offer_price.setText(storeSentOffersModels.get(position).getMakepeice());
            viewHolder.offer_quantity.setText(storeSentOffersModels.get(position).getMakeqty());
            viewHolder.offered_by.setText(storeSentOffersModels.get(position).getFullname());
            if(status.equalsIgnoreCase("1")){
                viewHolder.offer_btn.setText("Offer Accepted and Pay");
            }else{
                viewHolder.offer_btn.setText("Sent Offer");
            }
        }
        viewHolder.ll_offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pro_id = storeSentOffersModels.get(position).getProd_id();
                Intent i = new Intent(context, ProductDetailsActivity.class);
                i.putExtra("Pro_Id",pro_id);
                context.startActivity(i);
            }
        });
       /* viewHolder.accept_offer_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewHolder.remove_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

    }
    public void removeAt(int position) {
        //  notifyDataSetChanged();

    }
    @Override
    public int getItemCount() {
        return storeSentOffersModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView remove_offer,accept_offer_img;
        TextView prod_name,offer_prod_price,offer_quantity,offer_price,offered_by;
        Button offer_btn;
        LinearLayout ll_offers;
        public ViewHolder(View view) {
            super(view);
            prod_name = (TextView) view.findViewById(R.id.prod_name);
            offer_prod_price = (TextView) view.findViewById(R.id.offer_prod_price);
            offer_quantity = (TextView) view.findViewById(R.id.offer_quantity);
            offer_price = (TextView) view.findViewById(R.id.offer_price);
            offered_by = (TextView) view.findViewById(R.id.offered_by);
            remove_offer = (ImageView) view.findViewById(R.id.remove_offer);
            accept_offer_img = (ImageView) view.findViewById(R.id.accept_offer_img);
            offer_btn = (Button) view.findViewById(R.id.offer_btn);
            ll_offers = (LinearLayout) view.findViewById(R.id.ll_offers);

        }
    }
}

