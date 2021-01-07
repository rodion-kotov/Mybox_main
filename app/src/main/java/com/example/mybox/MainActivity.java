package com.example.mybox;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.scwang.wave.MultiWaveHeader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,FireBaseService.LoadDay,OperationAdapter.OnItemList{

    private MultiWaveHeader top,footer;
    private OperationAdapter operationAdapter;
    private FireBaseService fireBaseService;
    private ServiceInfo serviceInfo;
    private FireBaseService.MyResult myResultEvent=null;

    public String OPERATIONMINUS="Снятие";
    public String OPERATIONPLUS="Внос";

private Button minus,plus,myaction,allaction;
private  DatePickerDialog datePickerDialog;
private DayBox dayBox;
private ArrayList<Operation>  operations=new ArrayList<>();
private Boolean isAll=false;

private ImageButton lasttime,nexttime;
private TextView money,dateView;
 private    Calendar dateAndTime=Calendar.getInstance();
 private    Calendar dateAndTimeToday=Calendar.getInstance();
 private RecyclerView recyclerView;

    @Override
    protected void onResume() {
        super.onResume();
        setInitialDateTime();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        top=findViewById(R.id.waveHeaderTop);
        footer=findViewById(R.id.waveHeaderFooter);
        lasttime=findViewById(R.id.lasttime);
        nexttime=findViewById(R.id.nexttime);
        money=findViewById(R.id.moneyform);
        dateView=findViewById(R.id.textdate);
        myaction=findViewById(R.id.buttonmyaction);
        allaction=findViewById(R.id.buttonallaction);
        recyclerView=findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

plus= findViewById(R.id.plus);
minus=findViewById(R.id.minus);


plus.setOnClickListener(this);
minus.setOnClickListener(this);
lasttime.setOnClickListener(this);
nexttime.setOnClickListener(this);
dateView.setOnClickListener(this);
myaction.setOnClickListener(this);
allaction.setOnClickListener(this);

fireBaseService=new FireBaseService();
fireBaseService.newDay(new SimpleDateFormat("yyyy_MM_dd").format(dateAndTime.getTime()));
serviceInfo=new ServiceInfo(MainActivity.this);



newCalendar();


        top.setVelocity(1);
        top.setProgress(1);
        top.isRunning();
        top.setGradientAngle(45);
        top.setWaveHeight(40);
        top.setStartColor(Color.rgb(13,71,161));
        top.setCloseColor(Color.rgb(126,87, 194));

        footer.setVelocity(1);
        footer.setProgress(1);
        footer.isRunning();
        footer.setGradientAngle(45);
        footer.setWaveHeight(40);
        footer.setStartColor(Color.rgb(49,27,146));
        footer.setCloseColor(Color.rgb(30,136, 229));


    }

    private void newCalendar() {
        datePickerDialog=new DatePickerDialog(MainActivity.this,
                d,dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH)
        );
    }

    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };
    private void setInitialDateTime() {

        dateView.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                     ));
        repeatService();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.minus:
                if (!today__())
                {
                    dateAndTime=Calendar.getInstance();
                    newCalendar();
                   setInitialDateTime();
                }
                OperateActivity f = new OperateActivity(OPERATIONMINUS);
                f.show(getSupportFragmentManager(),"TAG");

                break;
            case R.id.plus:
                if (!today__())
                {
                    dateAndTime=Calendar.getInstance();
                    newCalendar();
                   setInitialDateTime();
                }
                OperateActivity d = new OperateActivity(OPERATIONPLUS);
                d.show(getSupportFragmentManager(),"TAG");

                break;
            case R.id.buttonallaction:
                isAll=true;
refreshInfo();
                break;
            case R.id.buttonmyaction:
                isAll=false;
                refreshInfo();

                break;
            case R.id.lasttime:
                break;
            case R.id.nexttime:
                break;
            case R.id.textdate:
                datePickerDialog.show();
                break;
        }

    }





    @Override
    protected void onStart() {
        super.onStart();
        setInitialDateTime();
    }


    void refreshInfo(){
        ArrayList<Operation> pop= new ArrayList<>();

        Collections.sort(operations, new Comparator<Operation>() {
            @Override
            public int compare(Operation o1, Operation o2) {
                Date date1= null;
                try {
                    date1 = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss" ).parse(o1.date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date date2= null;
                try {
                    date2 = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss" ).parse(o2.date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return  date1.compareTo(date2);
            }
        });

money.setText(String.valueOf(dayBox.getMoney()));

        operationAdapter=new OperationAdapter(operations,MainActivity.this    , MainActivity.this);
        operationAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(operationAdapter);

    }

    @Override
    public void resultDay(DayBox dayBox, String date) {


        if (dayBox==null ){

            operations=new ArrayList<>();
            operationAdapter=new OperationAdapter(operations,MainActivity.this    , MainActivity.this);
            operationAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(operationAdapter);
            money.setText("");


            MaterialAlertDialogBuilder dialogBuilder= new MaterialAlertDialogBuilder(MainActivity.this);
            dialogBuilder.setTitle("Нет данных по "+ date).setBackground(getResources().getDrawable(R.drawable.alertdialoglogin) )
                    .setMessage("При запросе к базе данных произошел сбой.")
                    .setCancelable(true).setPositiveButton("Повторить запрос", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    repeatService();
                }
            }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

        }
        else {
            this.dayBox=dayBox;
            operations.clear();
            if (dayBox.operations!=null){
            for(Map.Entry<String, Operation> entry : dayBox.operations.entrySet()) {
                Operation value=entry.getValue();
                value.setDate(entry.getKey());
                operations.add(value  );
            }
            refreshInfo();
        } else {
Toasty.warning(MainActivity.this,"В этот день не было операций!",Toasty.LENGTH_LONG,true).show();
                refreshInfo();
            }


        }




    }
    boolean today__(){
        return new SimpleDateFormat("yyyy_MM_dd").format(dateAndTime.getTime()).equals(new SimpleDateFormat("yyyy_MM_dd").format(dateAndTimeToday.getTime()));
    }

    private void repeatService() {
        if (myResultEvent!=null)
            fireBaseService.removeEventListener(myResultEvent);
        myResultEvent=fireBaseService.loadDay(MainActivity.this,new SimpleDateFormat("yyyy_MM_dd").format(dateAndTime.getTime()));
        if (today__() )
        {
            plus.setBackground(getDrawable(R.drawable.custombutton_ok));
            minus.setBackground(getDrawable(R.drawable.custombutton_cancel));
        }else {
            plus.setBackground(getDrawable(R.drawable.custombutton_soso));
            minus.setBackground(getDrawable(R.drawable.custombutton_soso));

        }
    }


    @Override
    public void onItemClick(Operation operation) {
        Intent intent = new Intent(this, ViewOperationActivity.class);
        intent.putExtra("Operation", new Gson().toJson(operation));
        startActivity(intent);
    }
}
