package com.technologygroup.rayannoor.yoga.referees;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.like.LikeButton;
import com.technologygroup.rayannoor.yoga.Coaches.CoachDetailsActivity;
import com.technologygroup.rayannoor.yoga.Coaches.CoachServicesActivity;
import com.technologygroup.rayannoor.yoga.R;
import com.technologygroup.rayannoor.yoga.RoundedImageView;

public class RefereeDetailsActivity extends AppCompatActivity {

    private LinearLayout lytParent;
    private ImageView lytBack;
    private RoundedImageView imgReferee;
    private TextView txtRefereeName;
    private TextView txtRefereeCity;
    private ImageView imgTelegram;
    private ImageView imgInstagram;
    private ImageView imgSorush;
    private ImageView imgEmail;
    private ImageView imgCall;
    private LinearLayout lytRefereeRating;
    private RatingBar RatingBarReferee;
    private TextView txtRefereeRate;
    private LikeButton btnLike;
    private TextView txtLikeCount;
    private ImageView imgLockResume;
    private LinearLayout lytResume;
    private ImageView imgLockBio;
    private LinearLayout lytBio;
    private ImageView imgLockCertificates;
    private LinearLayout lytCertificates;
    private ImageView imgLockEducation;
    private LinearLayout lytEducation;
    private ImageView imgLockCourse;
    private LinearLayout lytCourse;
    private FloatingActionButton floatAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referee_details);
        initView();


        lytBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RefereeDetailsActivity.this, RefereeServicesActivity.class);
                startActivity(intent);
            }
        });

        lytCertificates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RefereeDetailsActivity.this, RefereeServicesActivity.class);
                startActivity(intent);
            }
        });

        lytCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RefereeDetailsActivity.this, RefereeServicesActivity.class);
                startActivity(intent);
            }
        });

        lytEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RefereeDetailsActivity.this, RefereeServicesActivity.class);
                startActivity(intent);
            }
        });


        lytResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RefereeDetailsActivity.this, RefereeServicesActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initView() {
        lytParent = (LinearLayout) findViewById(R.id.lytParent);
        lytBack = (ImageView) findViewById(R.id.lytBack);
        imgReferee = (RoundedImageView) findViewById(R.id.imgReferee);
        txtRefereeName = (TextView) findViewById(R.id.txtRefereeName);
        txtRefereeCity = (TextView) findViewById(R.id.txtRefereeCity);
        imgTelegram = (ImageView) findViewById(R.id.imgTelegram);
        imgInstagram = (ImageView) findViewById(R.id.imgInstagram);
        imgSorush = (ImageView) findViewById(R.id.imgSorush);
        imgEmail = (ImageView) findViewById(R.id.imgEmail);
        imgCall = (ImageView) findViewById(R.id.imgCall);
        lytRefereeRating = (LinearLayout) findViewById(R.id.lytRefereeRating);
        RatingBarReferee = (RatingBar) findViewById(R.id.RatingBarReferee);
        txtRefereeRate = (TextView) findViewById(R.id.txtRefereeRate);
        btnLike = (LikeButton) findViewById(R.id.btnLike);
        txtLikeCount = (TextView) findViewById(R.id.txtLikeCount);
        imgLockResume = (ImageView) findViewById(R.id.imgLockResume);
        lytResume = (LinearLayout) findViewById(R.id.lytResume);
        imgLockBio = (ImageView) findViewById(R.id.imgLockBio);
        lytBio = (LinearLayout) findViewById(R.id.lytBio);
        imgLockCertificates = (ImageView) findViewById(R.id.imgLockCertificates);
        lytCertificates = (LinearLayout) findViewById(R.id.lytCertificates);
        imgLockEducation = (ImageView) findViewById(R.id.imgLockEducation);
        lytEducation = (LinearLayout) findViewById(R.id.lytEducation);
        imgLockCourse = (ImageView) findViewById(R.id.imgLockCourse);
        lytCourse = (LinearLayout) findViewById(R.id.lytCourse);
        floatAction = (FloatingActionButton) findViewById(R.id.floatAction);
    }
}
