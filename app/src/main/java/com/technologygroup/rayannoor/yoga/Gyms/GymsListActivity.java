package com.technologygroup.rayannoor.yoga.Gyms;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.technologygroup.rayannoor.yoga.Classes.App;
import com.technologygroup.rayannoor.yoga.Coaches.CoachListActivity;
import com.technologygroup.rayannoor.yoga.Models.CoachGymsModel;
import com.technologygroup.rayannoor.yoga.Models.GymModel;
import com.technologygroup.rayannoor.yoga.R;
import com.technologygroup.rayannoor.yoga.Services.WebService;
import com.technologygroup.rayannoor.yoga.adapters.CoachListAdapter;
import com.technologygroup.rayannoor.yoga.adapters.GymListAdapter;

import java.util.List;

public class GymsListActivity extends AppCompatActivity {

    private RelativeLayout btnBack;
    LinearLayout lytMain, lytDisconnect, lytEmpty;
    private ShimmerRecyclerView RecyclerGyms;
    private GymModel[] gymModel;
    private int stateNumber;
    private int cityNumber;
    private SharedPreferences prefs;
    fetchDataGymsList fetchDataGymsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyms_list);
        initView();

        prefs = getSharedPreferences("MyPrefs", 0);
        stateNumber = prefs.getInt("stateNumber", 1);
        cityNumber = prefs.getInt("cityNumber", 1);

        fetchDataGymsList = new fetchDataGymsList();
        fetchDataGymsList.execute();


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnTryAgain = findViewById(R.id.btnTryAgain);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    fetchDataGymsList = new fetchDataGymsList();
                    fetchDataGymsList.execute();

            }
        });

    }

    private void setUpRecyclerView() {
        GymListAdapter adapter = new GymListAdapter(GymsListActivity.this, gymModel);
        RecyclerGyms.setAdapter(adapter);
    }

    private void initView() {
        btnBack = (RelativeLayout) findViewById(R.id.btnBack);
        RecyclerGyms = (ShimmerRecyclerView) findViewById(R.id.RecyclerGyms);
        lytEmpty = findViewById(R.id.lytEmpty);
        lytMain = findViewById(R.id.lytMain);
        lytDisconnect = findViewById(R.id.lytDisconnect);
    }


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public class fetchDataGymsList extends AsyncTask<Object, Void, Void> {
        private WebService webService;

        @Override
        protected void onPreExecute() {
            webService = new WebService();
            super.onPreExecute();
            RecyclerGyms.showShimmerAdapter();
        }

        @Override
        protected Void doInBackground(Object... params) {
//            gymModel = webService.getGyms(App.isInternetOn());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            RecyclerGyms.hideShimmerAdapter();

            if (gymModel != null) {

                if (gymModel.length > 0) {
                    RecyclerGyms = findViewById(R.id.RecyclerGyms);
                    LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getApplicationContext());
                    mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
                    RecyclerGyms.setLayoutManager(mLinearLayoutManagerVertical);

                    setUpRecyclerView();

                    lytDisconnect.setVisibility(View.GONE);
                    lytEmpty.setVisibility(View.GONE);
                    lytMain.setVisibility(View.VISIBLE);

//                if (coachModel.Img != null)
//                    if (!coachModel.Img.equals("") && !coachModel.Img.equals("null"))
//                        //Glide.with(CoachProfileActivity.this).load(App.imgAddr + coachModel.Img).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imgCoach);
////                txtCoachName.setText(coachModel.fName + " " + coachModel.lName);
////                ClassLevels classLevels = new ClassLevels();
////                txtCoachLevel.setText(classLevels.getCoachLevelName(coachModel.idCurrentPlan));
////                txtCoachRate.setText(coachModel.Rate + "");
////                txtLikeCount.setText(coachModel.like + "");
////                rating.setRating((float) coachModel.Rate);
//
                } else {
                    lytDisconnect.setVisibility(View.GONE);
                    lytMain.setVisibility(View.GONE);
                    lytEmpty.setVisibility(View.VISIBLE);
                }
            } else {

                lytMain.setVisibility(View.GONE);
                lytEmpty.setVisibility(View.GONE);
                lytDisconnect.setVisibility(View.VISIBLE);

            }

        }

    }

    @Override
    public void onStop() {
        super.onStop();

        if (fetchDataGymsList != null)
            if (fetchDataGymsList.getStatus() == AsyncTask.Status.RUNNING)
                fetchDataGymsList.cancel(true);
    }

}
