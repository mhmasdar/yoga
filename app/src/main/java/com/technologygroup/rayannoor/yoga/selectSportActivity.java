package com.technologygroup.rayannoor.yoga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.technologygroup.rayannoor.yoga.Classes.App;
import com.technologygroup.rayannoor.yoga.Models.StateCityFieldModel;
import com.technologygroup.rayannoor.yoga.Services.WebService;

import java.util.ArrayList;
import java.util.List;

public class selectSportActivity extends AppCompatActivity {

    private TextView txtWelcome;
    private LinearLayout lytMain;
    private TextView txt1;
    private TextView txt2;
    private LinearLayout lyt1;
    private Spinner stateSpinner;
    private LinearLayout lyt2;
    private Spinner citySpinner;
    private LinearLayout lyt3;
    private Spinner sportSpinner;
    private Button btn;

    private List<StateCityFieldModel> stateList;
    private List<String> spinnerListState = new ArrayList<>();
    private List<StateCityFieldModel> cityList;
    private List<String> spinnerListCity = new ArrayList<>();
    private List<StateCityFieldModel> FieldList;
    private List<String> spinnerListFiled = new ArrayList<>();

    private int selectedState = 0, selectedCity = 0, selectedField = 0;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sport);
        initView();

        final Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.welcome_text);
        txtWelcome.startAnimation(anim);


        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.welcome_text2);
                txtWelcome.startAnimation(anim2);
            }
        }, 2200);

        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtWelcome.setVisibility(View.GONE);
                lytMain.setVisibility(View.VISIBLE);
                Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.select1);
                Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.select2);
                Animation anim3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.select3);
                Animation anim4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.select4);
                Animation anim5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.select5);
                txt1.startAnimation(anim1);
                txt2.startAnimation(anim1);
                lyt1.startAnimation(anim2);
                lyt2.startAnimation(anim3);
                lyt3.startAnimation(anim4);
                btn.startAnimation(anim5);
            }
        }, 3200);


        WebServiceCallState callState = new WebServiceCallState();
        callState.execute();

        WebServiceCallField callFiled = new WebServiceCallField();
        callFiled.execute();

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                //yearName = parent.getItemAtPosition(position).toString();
                selectedState = stateList.get(position).idState;

                WebServiceCallCity callCity = new WebServiceCallCity();
                callCity.execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                //yearName = parent.getItemAtPosition(position).toString();
                selectedCity = cityList.get(position).idCity;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs = getSharedPreferences("MyPrefs", 0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("cityUser", selectedCity);
                editor.putInt("fieldUser", selectedField);
                editor.putBoolean("isFirstRun", false);
                editor.apply();

                Intent intent = new Intent(selectSportActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    private void initView() {
        txtWelcome = (TextView) findViewById(R.id.txtWelcome);
        lytMain = (LinearLayout) findViewById(R.id.lytMain);
        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        lyt1 = (LinearLayout) findViewById(R.id.lyt1);
        stateSpinner = (Spinner) findViewById(R.id.stateSpinner);
        lyt2 = (LinearLayout) findViewById(R.id.lyt2);
        citySpinner = (Spinner) findViewById(R.id.citySpinner);
        lyt3 = (LinearLayout) findViewById(R.id.lyt3);
        sportSpinner = (Spinner) findViewById(R.id.sportSpinner);
        btn = (Button) findViewById(R.id.btn);
    }


    private class WebServiceCallState extends AsyncTask<Object, Void, Void> {

        private WebService webService;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            stateList = new ArrayList<>();
            webService = new WebService();

        }

        @Override
        protected Void doInBackground(Object... params) {

            stateList = webService.getStates(App.isInternetOn());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (stateList != null) {

                if (stateList.size() == 0) {
                    Toast.makeText(selectSportActivity.this, "هیچ استانی وجود ندارد", Toast.LENGTH_LONG).show();

                } else {

                    for (int i = 0; i < stateList.size(); i++) {

                        // Spinner Drop down elements
                        spinnerListState.add(stateList.get(i).stateName);

                    }

                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapterCourse = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, spinnerListState);
                    // Drop down layout style - list view with radio button
                    dataAdapterCourse.setDropDownViewResource(R.layout.spinner_text);
                    // attaching data adapter to spinner
                    stateSpinner.setAdapter(dataAdapterCourse);

                }


            } else {
                Toast.makeText(selectSportActivity.this, "ارتباط با سرور برقرار نشد", Toast.LENGTH_LONG).show();
            }

        }

    }

    private class WebServiceCallCity extends AsyncTask<Object, Void, Void> {

        private WebService webService;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cityList = new ArrayList<>();
            webService = new WebService();

        }

        @Override
        protected Void doInBackground(Object... params) {

            cityList = webService.getCiteies(App.isInternetOn(), selectedState);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (cityList != null) {

                if (cityList.size() == 0) {
                    Toast.makeText(selectSportActivity.this, "هیچ شهری وجود ندارد", Toast.LENGTH_LONG).show();

                } else {

                    for (int i = 0; i < cityList.size(); i++) {

                        // Spinner Drop down elements
                        spinnerListCity.add(cityList.get(i).cityName);

                    }

                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapterCourse = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, spinnerListCity);
                    // Drop down layout style - list view with radio button
                    dataAdapterCourse.setDropDownViewResource(R.layout.spinner_text);
                    // attaching data adapter to spinner
                    citySpinner.setAdapter(dataAdapterCourse);

                }


            } else {
                Toast.makeText(selectSportActivity.this, "ارتباط با سرور برقرار نشد", Toast.LENGTH_LONG).show();
            }

        }

    }

    private class WebServiceCallField extends AsyncTask<Object, Void, Void> {

        private WebService webService;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            FieldList = new ArrayList<>();
            webService = new WebService();

        }

        @Override
        protected Void doInBackground(Object... params) {

            FieldList = webService.getFields(App.isInternetOn());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (FieldList != null) {

                if (FieldList.size() == 0) {
                    Toast.makeText(selectSportActivity.this, "هیچ رشته ای وجود ندارد", Toast.LENGTH_LONG).show();

                } else {

                    for (int i = 0; i < FieldList.size(); i++) {

                        // Spinner Drop down elements
                        spinnerListFiled.add(FieldList.get(i).fieldName);

                    }

                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapterCourse = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, spinnerListFiled);
                    // Drop down layout style - list view with radio button
                    dataAdapterCourse.setDropDownViewResource(R.layout.spinner_text);
                    // attaching data adapter to spinner
                    sportSpinner.setAdapter(dataAdapterCourse);

                }


            } else {
                Toast.makeText(selectSportActivity.this, "ارتباط با سرور برقرار نشد", Toast.LENGTH_LONG).show();
            }

        }

    }

}
