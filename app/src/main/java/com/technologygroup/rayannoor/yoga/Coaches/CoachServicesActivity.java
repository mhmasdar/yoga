package com.technologygroup.rayannoor.yoga.Coaches;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.technologygroup.rayannoor.yoga.Classes.ClassDate;
import com.technologygroup.rayannoor.yoga.FadePageTransformer;
import com.technologygroup.rayannoor.yoga.R;
import com.technologygroup.rayannoor.yoga.Services.FilePath;
import com.technologygroup.rayannoor.yoga.adapters.CoachCertificateAdapter;
import com.technologygroup.rayannoor.yoga.adapters.CoachEducationAdapter;
import com.technologygroup.rayannoor.yoga.adapters.CoachServicesPager;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class CoachServicesActivity extends AppCompatActivity {

    private RelativeLayout btnBack;
    private TabLayout tabLayout;
    private ViewPager CoachPager;
    private Typeface typeface;

    CoachEducationAdapter adapterEdu;
    CoachCertificateAdapter adapterHonor;

    private int selectedTabIndex;
    private boolean calledFromPanel = false;
    private int idCoach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_services);
        initView();
        typeface = Typeface.createFromAsset(getAssets(), "font.ttf");

        calledFromPanel = getIntent().getBooleanExtra("calledFromPanel", false);
        selectedTabIndex = getIntent().getIntExtra("SelectedTabIndex", 0);
        idCoach = getIntent().getIntExtra("idCoach", -1);

        tabLayout.addTab(tabLayout.newTab().setText("رزومه"));
        tabLayout.addTab(tabLayout.newTab().setText("بیوگرافی"));
        tabLayout.addTab(tabLayout.newTab().setText("مدارک"));
        tabLayout.addTab(tabLayout.newTab().setText("تحصیلات"));
        tabLayout.addTab(tabLayout.newTab().setText("دوره ها"));
        tabLayout.addTab(tabLayout.newTab().setText("باشگاه ها"));
        tabLayout.addTab(tabLayout.newTab().setText("آموزش ها"));
        tabLayout.addTab(tabLayout.newTab().setText("کاریابی"));

        CoachServicesPager adapter = new CoachServicesPager(getSupportFragmentManager(), calledFromPanel, idCoach);
        CoachPager.setAdapter(adapter);
        CoachPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        CoachPager.setCurrentItem(selectedTabIndex);

//        TabLayout.Tab tab = tabLayout.getTabAt(selectedTabIndex);
//        tab.select();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                CoachPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        changeTabsFont();
    }

    private void initView() {
        btnBack = (RelativeLayout) findViewById(R.id.btnBack);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        CoachPager = (ViewPager) findViewById(R.id.CoachServicesPager);
    }

    private void changeTabsFont() {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {

                    ((TextView) tabViewChild).setTypeface(typeface);
                }
            }
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {

            adapterEdu = new CoachEducationAdapter(CoachServicesActivity.this);

            adapterEdu.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == 3) {

            adapterHonor = new CoachCertificateAdapter(CoachServicesActivity.this);

            adapterHonor.onActivityResult(requestCode, resultCode, data);
        }

    }


}
