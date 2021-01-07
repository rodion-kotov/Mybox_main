package com.example.mybox;

import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class ViewOperationActivity extends AppCompatActivity  implements View.OnClickListener,DocumentAdapter.ClickOnDoc {

private   Operation operation;
private TextView money,officer,comment,title_date;
private RecyclerView view_document;
private Button audio;
private ImageButton close;
private DocumentAdapter documentAdapter;
MediaPlayer mediaPlayer;
    private boolean isAudio=true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewoperation);
        money=findViewById(R.id.somemoney2);
        officer=findViewById(R.id.view_man);
        comment=findViewById(R.id.view_comment);
        title_date=findViewById(R.id.dialog_text_title);
        view_document=findViewById(R.id.viewfiles);
        audio= findViewById(R.id.button_audio);
        close=findViewById(R.id.fullscreen_dialog_close);
       view_document.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));




        audio.setOnClickListener(this::onClick);
        close.setOnClickListener(this::onClick);

        Bundle arguments = getIntent().getExtras();

        if(arguments!=null){


            operation = (Operation) new Gson().fromJson(arguments.getString("Operation"),new TypeToken<Operation>(){}.getType());
            money.setText(String.valueOf(operation.getAmount()));
            officer.setText(operation.getIdUser());
            comment.setText(operation.getComment());
            if (operation.audio!=null)
            {
mediaPlayer=new MediaPlayer();
                try {

mediaPlayer.setDataSource(this, Uri.parse(operation.getAudio().getUrl()));
mediaPlayer.prepare();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            isAudio=true;
                            audio.setBackground(getResources().getDrawable(R.drawable.custombutton_ok));
                            audio.setText("Воспроизвести аудио");
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                audio.setVisibility(View.INVISIBLE);
            }

            if (operation.getAmount()<0){
                money.setTextColor(Color.rgb(255,0,38));
            }else {
                money.setTextColor(Color.rgb(1,159,64));
            }


if (operation.docs!=null){
            documentAdapter=new DocumentAdapter(operation.getArrayListDocument(),ViewOperationActivity.this    , ViewOperationActivity.this);
            documentAdapter.notifyDataSetChanged();
            view_document.setAdapter(documentAdapter);}
else{
    view_document.setVisibility(View.INVISIBLE);
   TextView textView=findViewById(R.id.textViefw4);
   textView.setVisibility(View.INVISIBLE);
}


        }else {
            MaterialAlertDialogBuilder dialogBuilder= new MaterialAlertDialogBuilder(ViewOperationActivity.this);
            dialogBuilder.setTitle("Ошибка приложения!").setBackground(getResources().getDrawable(R.drawable.alertdialoglogin) )
                    .setMessage("Сбой программы...Обратитесь к разработчику)")
                    .setCancelable(false).setPositiveButton("Понятно", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            dialogBuilder.show();



        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fullscreen_dialog_close:
finish();
                break;

            case R.id.button_audio:
                if (isAudio){
                    audio.setBackground(getResources().getDrawable(R.drawable.custombutton_cancel));
                    audio.setText("Остановить");
                    mediaPlayer.start();
                }

                else{
                    mediaPlayer.stop();
                    audio.setBackground(getResources().getDrawable(R.drawable.custombutton_ok));
                    audio.setText("Воспроизвести аудио");

                }


                break;
        }
    }

    @Override
    public void clickDoc(Operation.Document document) {
       ViewDocumentActivity documentActivity=new ViewDocumentActivity("Name",document.getUrl(),this);
       documentActivity.show(getSupportFragmentManager(),"TAG");
    }
}
