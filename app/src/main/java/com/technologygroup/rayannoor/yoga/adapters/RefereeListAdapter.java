package com.technologygroup.rayannoor.yoga.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.technologygroup.rayannoor.yoga.R;
import com.technologygroup.rayannoor.yoga.RoundedImageView;
import com.technologygroup.rayannoor.yoga.referees.RefereeDetailsActivity;
import com.technologygroup.rayannoor.yoga.referees.RefereeListActivity;

public class RefereeListAdapter extends RecyclerView.Adapter<RefereeListAdapter.myViewHolder> {

    private Context context;
    private LayoutInflater mInflater;


    public RefereeListAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_referee_list, parent, false);
        myViewHolder holder = new myViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final myViewHolder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RefereeListActivity activity = (RefereeListActivity) context;
                Intent intent = new Intent(activity, RefereeDetailsActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }


    class myViewHolder extends RecyclerView.ViewHolder {

        private TextView txtStatus;
        private TextView txtLikeCount;
        private TextView txtViewCount;
        private TextView txtRefereeName;
        private RoundedImageView imgReferee;


        myViewHolder(View itemView) {
            super(itemView);

            txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);
            txtLikeCount = (TextView) itemView.findViewById(R.id.txtLikeCount);
            txtViewCount = (TextView) itemView.findViewById(R.id.txtViewCount);
            txtRefereeName = (TextView) itemView.findViewById(R.id.txtRefereeName);
            imgReferee = (RoundedImageView) itemView.findViewById(R.id.imgReferee);
        }
    }
}
