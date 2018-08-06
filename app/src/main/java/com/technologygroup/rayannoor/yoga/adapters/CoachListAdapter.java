package com.technologygroup.rayannoor.yoga.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.technologygroup.rayannoor.yoga.Classes.App;
import com.technologygroup.rayannoor.yoga.Coaches.CoachDetailsActivity;
import com.technologygroup.rayannoor.yoga.Coaches.CoachListActivity;
import com.technologygroup.rayannoor.yoga.Models.CoachModel;
import com.technologygroup.rayannoor.yoga.R;
import com.technologygroup.rayannoor.yoga.RoundedImageView;

/**
 * Created by Mohamad Hasan on 2/7/2018.
 */

public class CoachListAdapter extends RecyclerView.Adapter<CoachListAdapter.myViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private CoachModel[] models;



//    public CoachListAdapter(Context context) {
//        this.context = context;
//        mInflater = LayoutInflater.from(context);
//    }

    public CoachListAdapter(Context context, CoachModel[] models) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.models = models;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_coach_list, parent, false);
        myViewHolder holder = new myViewHolder(view);
        holder.txtCoachName.setText("me");
        return holder;
    }

    @Override
    public void onBindViewHolder(final myViewHolder holder, int position) {

        //if (models[position].idState == stateNumber && models[position].idCity == cityNumber)


        if (models[position].Img != null)
            if (!models[position].Img.equals("") && !models[position].Img.equals("null"))
                Glide.with(context).load(App.imgAddr + models[position].Img).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.imgCoach);


        holder.lName = models[position].lName;
        holder.Email = models[position].Email;
        holder.fName = models[position].fName;
        holder.id = models[position].id;
        holder.idCity = models[position].idCity;
        holder.Img = models[position].Img;
        holder.Instagram = models[position].Instagram;
        holder.Mobile = models[position].Mobile;
        holder.idCurrentPlan = models[position].idCurrentPlan;
        holder.lastUpdate = models[position].lastUpdate;
        holder.like = models[position].like;
        holder.natCode = models[position].natCode;
        holder.Telegram = models[position].Telegram;
        holder.Rate = models[position].Rate;
        holder.Gender = models[position].Gender;
        holder.City = models[position].City;
        holder.State = models[position].State;


        holder.txtCoachName.setText(models[position].fName + " " + models[position].lName);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CoachListActivity activity = (CoachListActivity) context;
                Intent intent = new Intent(activity, CoachDetailsActivity.class);
//                Toast.makeText(view.getContext(), holder.lName+"", Toast.LENGTH_LONG).show();
                intent.putExtra("fName", holder.fName);
                intent.putExtra("Email", holder.Email);
                intent.putExtra("Instagram", holder.Instagram);
                intent.putExtra("lName", holder.lName);
                intent.putExtra("Telegram", holder.Telegram);
                intent.putExtra("Img", holder.Img);
                intent.putExtra("id", holder.id);
                intent.putExtra("idCity", holder.idCity);
                intent.putExtra("idCurrentPlan", holder.idCurrentPlan);
                intent.putExtra("like", holder.like);
                intent.putExtra("lastUpdate", holder.lastUpdate);
                intent.putExtra("Mobile", holder.Mobile);
                intent.putExtra("natCode", holder.natCode);
                intent.putExtra("Rate", holder.Rate);
                intent.putExtra("Gender", holder.Gender);
                intent.putExtra("Rate", holder.Rate);
                intent.putExtra("Gender", holder.Gender);
                intent.putExtra("City", holder.City);
                intent.putExtra("State", holder.State);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.length;
    }


    class myViewHolder extends RecyclerView.ViewHolder {

        private String Email;
        private String fName;
        private String Instagram;
        private String lName;
        private String Telegram;
        private String Img;
        private int id;
        private int idCity;
        private int idCurrentPlan;
        private int like;
        private String lastUpdate;
        private String Mobile;
        private String natCode;
        private double Rate;
        private boolean Gender;
        private String City;
        private String State;

        private TextView txtStatus;
        private TextView txtLikeCount;
        private TextView txtViewCount;
        private TextView txtCoachName;
        private RoundedImageView imgCoach;


        myViewHolder(View itemView) {
            super(itemView);

            txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);
            txtLikeCount = (TextView) itemView.findViewById(R.id.txtLikeCount);
            txtViewCount = (TextView) itemView.findViewById(R.id.txtViewCount);
            txtCoachName = (TextView) itemView.findViewById(R.id.txtCoachName);
            imgCoach = (RoundedImageView) itemView.findViewById(R.id.imgCoach);
        }
    }
}