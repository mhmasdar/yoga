package com.technologygroup.rayannoor.yoga.Coaches;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.technologygroup.rayannoor.yoga.Classes.App;
import com.technologygroup.rayannoor.yoga.Classes.ClassDate;
import com.technologygroup.rayannoor.yoga.Models.CoachResumeModel;
import com.technologygroup.rayannoor.yoga.R;
import com.technologygroup.rayannoor.yoga.Services.WebService;
import com.technologygroup.rayannoor.yoga.adapters.CoachCertificateAdapter;
import com.technologygroup.rayannoor.yoga.adapters.CoachResumeAdapter;

import java.util.ArrayList;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class resumeFragment extends Fragment implements
        DatePickerDialog.OnDateSetListener{

    LinearLayout lytMain, lytDisconnect, lytEmpty;
    private ShimmerRecyclerView Recycler;
    private FloatingActionButton floatAction;
    private Dialog dialog;


    private SharedPreferences prefs;
    private int idCoach;
    List<CoachResumeModel> list;
    public boolean flagPermission = false;
    private static final int PICK_FILE_REQUEST = 1;
    private String selectedFilePath, selectedImgName = "";


    String resultAdd;

    // dialog add content
    EditText edtTitle, edtStartDate, edtEndDate;
    ImageView imgClose;
    CircularProgressButton btnOk;


    //relates to date and time picker
    private static final String TIMEPICKER = "TimePickerDialog",
            DATEPICKER = "DatePickerDialog", MULTIDATEPICKER = "MultiDatePickerDialog";
    String date;
    boolean startDateFlag = false, finishDateFlag = false;

    private boolean calledFromPanel = false;
    WebServiceAdd callBackFileDetails;
    WebServiceList webServiceCoachInfo;

    public resumeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resume, container, false);

        calledFromPanel = getArguments().getBoolean("calledFromPanel", false);
        idCoach = getArguments().getInt("idCoach", -1);


        lytEmpty = view.findViewById(R.id.lytEmpty);
        lytMain = view.findViewById(R.id.lytMain);
        lytDisconnect = view.findViewById(R.id.lytDisconnect);
        Recycler = view.findViewById(R.id.Recycler);
        floatAction = view.findViewById(R.id.floatAction);


        if (!calledFromPanel) {
            floatAction.setVisibility(View.GONE);
        }

        if (idCoach > 0) {

             webServiceCoachInfo = new WebServiceList();
            webServiceCoachInfo.execute();
        } else {
            Toast.makeText(getContext(), "مربی مورد نظر یافت نشد", Toast.LENGTH_LONG).show();
        }

        floatAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        Button btnTryAgain = view.findViewById(R.id.btnTryAgain);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idCoach > 0) {

                    webServiceCoachInfo = new WebServiceList();
                    webServiceCoachInfo.execute();
                } else {
                    Toast.makeText(getContext(), "مربی مورد نظر یافت نشد", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
            }
        });

        return view;
    }

    private void setUpRecyclerView(List<CoachResumeModel> list){
        CoachResumeAdapter adapter = new CoachResumeAdapter(getActivity(), list, idCoach, calledFromPanel);
        Recycler.setAdapter(adapter);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getContext());
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        Recycler.setLayoutManager(mLinearLayoutManagerVertical);
    }

    private void showDialog() {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_resume);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        edtTitle = dialog.findViewById(R.id.edtTitle);
        edtStartDate = dialog.findViewById(R.id.edtStartDate);
        edtEndDate = dialog.findViewById(R.id.edtEndDate);
        btnOk = dialog.findViewById(R.id.btnOk);
        imgClose = dialog.findViewById(R.id.imgClose);


        //to open date picker dialog
        edtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PersianCalendar now = new PersianCalendar();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        resumeFragment.this,
                        now.getPersianYear(),
                        now.getPersianMonth(),
                        now.getPersianDay()
                );
                startDateFlag = true;
                dpd.show(getActivity().getFragmentManager(), DATEPICKER);
                dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                        startDateFlag = false;
                    }
                });


            }
        });

        edtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PersianCalendar now = new PersianCalendar();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        resumeFragment.this,
                        now.getPersianYear(),
                        now.getPersianMonth(),
                        now.getPersianDay()
                );
                finishDateFlag = true;
                dpd.show(getActivity().getFragmentManager(), DATEPICKER);
                dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                        finishDateFlag = false;
                    }
                });


            }
        });


        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!edtTitle.getText().toString().equals("") && !edtStartDate.getText().toString().equals("")) {

                         callBackFileDetails = new WebServiceAdd();
                        callBackFileDetails.execute();


                } else {
                    Toast.makeText(getContext(), "لطفا فیلد ها را کامل کنید", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        // Note: monthOfYear is 0-indexed
        boolean flagMonth = false, flagDay = false;

        if (dayOfMonth / 10 < 1)
            flagDay = true;
        if ((monthOfYear + 1) / 10 < 1)
            flagMonth = true;

        date = year + "";
        if (flagMonth)
            date += "/0" + (monthOfYear + 1);
        else
            date += "/" + (monthOfYear + 1);
        if (flagDay)
            date += "/0" + dayOfMonth;
        else
            date += "/" + dayOfMonth;


        if (startDateFlag) {
            edtStartDate.setText(date);
            startDateFlag = false;
        }
        if (finishDateFlag) {
            edtEndDate.setText(date);
            finishDateFlag = false;
        }
    }


    private class WebServiceList extends AsyncTask<Object, Void, Void> {

        private WebService webService;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            webService = new WebService();
            list = new ArrayList<>();
            Recycler.showShimmerAdapter();
        }

        @Override
        protected Void doInBackground(Object... params) {

            list = webService.getCoachResume(App.isInternetOn(), idCoach);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Recycler.clearAnimation();

            if (list != null) {

                if (list.size() > 0) {

                    lytDisconnect.setVisibility(View.GONE);
                    lytEmpty.setVisibility(View.GONE);
                    lytMain.setVisibility(View.VISIBLE);

                    setUpRecyclerView(list);

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

    private class WebServiceAdd extends AsyncTask<Object, Void, Void> {

        private WebService webService;
        CoachResumeModel model;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            webService = new WebService();
            btnOk.startAnimation();


            model = new CoachResumeModel();
            model.id = -1;
            model.idCoach = idCoach;
            model.Title = edtTitle.getText().toString();
            model.startDate = edtStartDate.getText().toString();
            model.endDate = edtEndDate.getText().toString();
        }

        @Override
        protected Void doInBackground(Object... params) {

            resultAdd = webService.AddCoachResume(App.isInternetOn(), model);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            if (resultAdd != null) {

                if (Integer.parseInt(resultAdd) > 0) {

                    model.id = Integer.parseInt(resultAdd);

                    // بعد از اتمام عملیات کدهای زیر اجرا شوند
                    Bitmap icon = BitmapFactory.decodeResource(getResources(),
                            R.drawable.ic_ok);
                    btnOk.doneLoadingAnimation(R.color.green, icon); // finish loading

                    // بستن دیالوگ حتما با تاخیر انجام شود
                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    }, 1000);


                    list.add(model);
                    setUpRecyclerView(list);

                } else if (Integer.parseInt(resultAdd) == 0) {

                    btnOk.revertAnimation();
                    Toast.makeText(getContext(), "ارسال اطلاعات ناموفق است", Toast.LENGTH_LONG).show();

                } else {

                    btnOk.revertAnimation();
                    Toast.makeText(getContext(), "خطا در برقراری ارتباط", Toast.LENGTH_LONG).show();

                }
            } else {

                btnOk.revertAnimation();
                Toast.makeText(getContext(), "خطا در برقراری ارتباط", Toast.LENGTH_LONG).show();

            }
        }
    }


    @Override
    public void onStop() {
        super.onStop();

        if (webServiceCoachInfo != null)
            if (webServiceCoachInfo.getStatus() == AsyncTask.Status.RUNNING)
                webServiceCoachInfo.cancel(true);

        if (callBackFileDetails != null)
            if (callBackFileDetails.getStatus() == AsyncTask.Status.RUNNING)
                callBackFileDetails.cancel(true);
    }

}
