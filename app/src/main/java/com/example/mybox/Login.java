package com.example.mybox;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hanks.passcodeview.PasscodeView;

import es.dmoral.toasty.Toasty;

import static android.widget.Toast.LENGTH_LONG;

public class Login extends AppCompatActivity implements FireBaseService.Login {
private PasscodeView passcodeView;
private ServiceInfo info;
private  AlertDialog.Builder builder;
private FireBaseService fireBaseService;
private Loading loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info=new ServiceInfo(Login.this);
         passcodeView=findViewById(R.id.passcodeView);
         fireBaseService=new FireBaseService( );
loading=new Loading(Login.this);

repeatService();





    }
    private void repeatService(){
        if (info.getUserLocal()==null){

            final EditText input = new EditText(this);
            //input.setTextColor(Color.WHITE);
            input.setInputType(InputType.TYPE_CLASS_TEXT  );

            builder = new AlertDialog.Builder(this,R.style.YDialog);
            builder.setCancelable(false);
            builder.setTitle("Введите Логин");
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    fireBaseService.read(input.getText().toString(), Login.this);
                }
            });
            builder.show();

        } else {

            fireBaseService.read(info.getUserLocal().getLogin(), Login.this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void start() {
loading.start();
    }

    @Override
    public void result(Boolean logout, User user) {
if (logout){
    info.putUserLocal(user);
    login();
}else {
    repeatService();
}
        loading.stop();
    }

    @Override
    public void error() {
        loading.stop();
        MaterialAlertDialogBuilder dialogBuilder= new MaterialAlertDialogBuilder(Login.this);
        dialogBuilder.setTitle("Ошибка!Перезайти в систему?").setBackground(getResources().getDrawable(R.drawable.alertdialoglogin) ).setMessage("При запросе к системе произошел сбой.Возможная проблема связана с верерными данными пользователя.").setCancelable(false).setPositiveButton("Повтор", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                repeatService();
            }
        });
        dialogBuilder.show();

    }

    void login(){
        passcodeView.setPasscodeLength(4).setLocalPasscode(info.getUserLocal().pass).setListener(new PasscodeView.PasscodeViewListener() {
            @Override
            public void onFail() {
                Toast.makeText(Login.this,passcodeView.getLocalPasscode(), LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(String number) {
                Intent intent= new Intent(Login.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}