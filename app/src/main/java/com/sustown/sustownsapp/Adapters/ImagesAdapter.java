package com.sustown.sustownsapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.example.sustownsapp.R;
import com.sustown.sustownsapp.Models.ImageModel;
import com.sustown.sustownsapp.helpers.Helper;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {
    Context context;
    List<String> imagesList;
    List<ImageModel> imageJSONList;
    Helper basicUtilities;
    boolean isProduct;
    String selectedPrimary = "";
    private int lastSelectedPosition = -1;

    public ImagesAdapter(Context context, List<String> imagesList, final boolean isProduct) {
        this.context = context;
        this.imagesList = imagesList;
        this.isProduct = isProduct;
        basicUtilities = new Helper(context);
    }

    public ImagesAdapter(Context context, List<String> imagesList, List<ImageModel> imageJSONList, final boolean isProduct) {
        this.context = context;
        this.imagesList = imagesList;
        this.imageJSONList = imageJSONList;
        this.isProduct = isProduct;
        basicUtilities = new Helper(context);
    }

    @NonNull
    @Override
    public ImagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.imageview_item, parent, false);
        return new ImagesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImagesAdapter.ViewHolder holder, final int position) {
        if (isProduct) {
            holder.primary_radiobtn.setVisibility(View.VISIBLE);
        } else {
            holder.primary_radiobtn.setVisibility(View.GONE);
        }
        if (imagesList.get(position).contains("http")) {
            basicUtilities.getImage(context, imagesList.get(position), holder.imageView);
        } else {
            try {
                Glide.with(context).load(imagesList.get(position)).into(holder.imageView);
            } catch (Exception e) {
                Glide.with(context).load("file://" + imagesList.get(position)).into(holder.imageView);
                e.printStackTrace();
            }
        }
        holder.primary_radiobtn.setChecked(lastSelectedPosition == position);
        holder.primary_radiobtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (int i = 0; i < imagesList.size(); i++) {
                        if(i == position){
                            imageJSONList.get(i).setIsPrimary("yes");
                        } else {
                            imageJSONList.get(i).setIsPrimary("no");
                        }
                    }
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basicUtilities.showImageAlert(context, imagesList.get(position));
            }
        });
        holder.remove_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItemByPosition(position);
            }
        });
    }

    private boolean removeItemByPosition(int position) {
        try {
            imagesList.remove(position);
            this.notifyDataSetChanged();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView, remove_image;
        RadioButton primary_radiobtn;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.phone_image);
            remove_image = itemView.findViewById(R.id.remove_image);
            primary_radiobtn = itemView.findViewById(R.id.primary_radiobtn);
            primary_radiobtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
}
