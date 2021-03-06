package com.technologygroup.rayannoor.yoga.Teaches;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.technologygroup.rayannoor.yoga.MainActivity;
import com.technologygroup.rayannoor.yoga.R;
import com.technologygroup.rayannoor.yoga.adapters.TeachListAdapter;

public class teachsListActivity extends AppCompatActivity {

    private RelativeLayout btnBack;
    private RecyclerView Recycler;
    LinearLayoutManager mLinearLayoutManagerVertical;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachs_list);
        initView();

        setUpRecyclerView();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initView() {
        btnBack = (RelativeLayout) findViewById(R.id.btnBack);
        Recycler = (RecyclerView) findViewById(R.id.Recycler);
    }

    private void setUpRecyclerView(){

        TeachListAdapter adapter = new TeachListAdapter(this);
        Recycler.setAdapter(adapter);

        mLinearLayoutManagerVertical = new LinearLayoutManager(this);
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        Recycler.setLayoutManager(mLinearLayoutManagerVertical);

    }

}