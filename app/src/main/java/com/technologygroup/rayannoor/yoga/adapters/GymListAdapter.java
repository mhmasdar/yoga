package com.technologygroup.rayannoor.yoga.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.technologygroup.rayannoor.yoga.Classes.App;
import com.technologygroup.rayannoor.yoga.Gyms.GymProfileActivity;
import com.technologygroup.rayannoor.yoga.Gyms.GymsListActivity;
import com.technologygroup.rayannoor.yoga.Models.GymModel;
import com.technologygroup.rayannoor.yoga.R;

//import android.widget.Toast;

/**
 * Created by Mohamad Hasan on 2/7/2018.
 */

public class GymListAdapter extends RecyclerView.Adapter<GymListAdapter.myViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private GymModel models[];
    private Dialog dialog;



    public GymListAdapter(Context context, GymModel[] models) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.models = models;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_gym_list, parent, false);
        myViewHolder holder = new myViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final myViewHolder holder, int position) {

        holder.txtGymName.setText(models[position].Name + "");
        holder.txtGymAddress.setText(models[position].Address);

        if (models[position].Img != null)
            if (!models[position].Img.equals("") && !models[position].Img.equals("null"))
                Glide.with(context).load(App.imgAddr + models[position].Img).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.imgGym);


//        if (models[position].idCurrentPlan == 1){
//            holder.imgStar1.setVisibility(View.VISIBLE);
//        } else if (models[position].idCurrentPlan == 2){
//            holder.imgStar1.setVisibility(View.VISIBLE);
//            holder.imgStar2.setVisibility(View.VISIBLE);
//        } else if (models[position].idCurrentPlan == 3){
//            holder.imgStar1.setVisibility(View.VISIBLE);
//            holder.imgStar2.setVisibility(View.VISIBLE);
//            holder.imgStar3.setVisibility(View.VISIBLE);
//        }

        holder.id = models[position].id;
        holder.Img = models[position].Img;
        holder.like = models[position].like;
        holder.Rate = models[position].Rate;
        holder.Des = models[position].Des;
        holder.Lat = models[position].Lat;
        holder.Lon = models[position].Lon;
        holder.Name = models[position].Name;
        holder.Tell = models[position].Tell;
        holder.workTime = models[position].workTime;
        holder.Email = models[position].Email;
        holder.fname = models[position].fname;
        holder.idCurrentPlan = models[position].idCurrentPlan;
        holder.Instagram = models[position].Instagram;
        holder.lName = models[position].lName;
        holder.Address = models[position].Address;
        holder.Telegram = models[position].Telegram;
        holder.City = models[position].City;
        holder.State = models[position].State;


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GymsListActivity activity = (GymsListActivity) context;
                Intent intent = new Intent(activity, GymProfileActivity.class);
                //intent.putExtra("idCity", holder.idCity);
                intent.putExtra("Des;", holder.Des);
                //intent.putExtra("idCurrentSMSPlan", holder.idCurrentSMSPlan);
                intent.putExtra("Lat", holder.Lat);
                intent.putExtra("Lon", holder.Lon);
                intent.putExtra("Name", holder.Name);
                //intent.putExtra("notifCount", holder.notifCount);
                intent.putExtra("Tell", holder.Tell);
                intent.putExtra("workTime", holder.workTime);
                intent.putExtra("Email", holder.Email);
                intent.putExtra("fname", holder.fname);
                intent.putExtra("id", holder.id);
                intent.putExtra("idCurrentPlan", holder.idCurrentPlan);
                intent.putExtra("Instagram", holder.Instagram);
                //intent.putExtra("lastUpdate", holder.lastUpdate);
                intent.putExtra("like", holder.like);
                intent.putExtra("lName", holder.lName);
                intent.putExtra("Address", holder.Address);
                intent.putExtra("Telegram", holder.Telegram);
                intent.putExtra("City", holder.City);
                intent.putExtra("Rate", holder.Rate);
                intent.putExtra("State", holder.State);
                intent.putExtra("Img", holder.Img);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.length;
    }


    class myViewHolder extends RecyclerView.ViewHolder {


        public int idCity;
        public String Des;
        public int idCurrentSMSPlan;
        public double Lat;
        public double Lon;
        public String Name;
        public int notifCount;
        public String Tell;
        public String workTime;
        public String Email;
        public String fname;
        public int id;
        public int idCurrentPlan;
        public String Instagram;
        public String lastUpdate;
        public int like;
        public String lName;
        public String Address;
        public double Rate;
        public String Telegram;
        public String City;
        public String State;
        public String Img;


        private TextView txtGymName;
        private TextView txtGymAddress;
        private TextView txtLikeCount;
        private TextView txtViewCount;
        private ImageView imgGym;

        myViewHolder(View itemView) {
            super(itemView);

            txtGymName = (TextView) itemView.findViewById(R.id.txtGymName);
            txtGymAddress = (TextView) itemView.findViewById(R.id.txtGymAddress);
            txtLikeCount = (TextView) itemView.findViewById(R.id.txtLikeCount);
            txtViewCount = (TextView) itemView.findViewById(R.id.txtViewCount);
            imgGym = (ImageView) itemView.findViewById(R.id.imgGym);
        }
    }
}