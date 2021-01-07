package com.example.mybox;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;


public class OperateActivity extends DialogFragment implements FireBaseService.UploadFile, View.OnClickListener
{
    private static final int RESULT_LOAD_IMAGE =65 ;

    private TextView  title;
    private ImageButton close;
            private ImageButton play;
    private Button camera,audio,action;
    private EditText comment,cash;
    private Boolean isPressed=false;
    private  String day=new SimpleDateFormat("yyyy_MM_dd" ).format(new Date());
    private Operation.Document audioDoc=new Operation.Document();

    public static final String OPERATIONMINUS="Снятие";
    public static final String OPERATIONPLUS="Внос";

    public static final String TYPEFOTO="docs";
    public static final String TYPEAUDIO="audio";

    public String operation;
    int min=1;



    private MediaRecorder mediaRecorder;
    private String recordFile;
    String recordPath;

    HashMap<String, Operation.Document> documents=new HashMap<>();
    HashMap<String, Uri> documentsUri=new HashMap<>();
    private boolean isAudio= false;

    public OperateActivity(String operation) {
        this.operation = operation;
    }

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE,R.style.FullDialog );
        checkPermissions();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.operate_dialog,container,false);

          close = view.findViewById(R.id.fullscreen_dialog_close);
          action = view.findViewById(R.id.fullscreen_dialog_action);
          camera=view.findViewById(R.id.document);
          audio=view.findViewById(R.id.audio);
          comment=view.findViewById(R.id.comment);
          cash=view.findViewById(R.id.somemoney);
          title=view.findViewById(R.id.fullscreen_dialog_text);
          play=view.findViewById(R.id.play);


          switch (operation){
              case OPERATIONMINUS:
                  min=-1;
                  cash.setTextColor( Color.rgb(255,0,38));
                  break;

              case OPERATIONPLUS:
                  cash.setTextColor( Color.rgb(1,159,64));


                  break;

              default:
                  throw new IllegalStateException("Unexpected value: " + operation);
          }

          title.setText(operation);


play.setOnClickListener(this);
camera.setOnClickListener(this);
action.setOnClickListener(this);
close.setOnClickListener(this);

audio.setOnTouchListener(new View.OnTouchListener() {
               @Override
               public boolean onTouch(View v, MotionEvent event) {
                   int action = event.getAction();
                   if(action == MotionEvent.ACTION_DOWN){
                       audio.setBackground(getResources().getDrawable(R.drawable.custombutton_cancel));
                       audio.setText("Остановить запись");
                      // play.setVisibility(View.GONE);

                        startRecording();

                       isPressed = false;
                   } else if(action == MotionEvent.ACTION_UP){
                       audio.setBackground(getResources().getDrawable(R.drawable.custombutton_ok));
                       audio.setText("Записать аудио");

                     //  play.setVisibility(View.INVISIBLE);
                       isPressed = true;
                       stopRecording();
                   }
                   return true;
               }
           });




        return view;
    }

    @Override
    public void dismiss() {

        super.dismiss();
    }
    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }


    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 0);

        }
    }

    private void stopRecording() {




     //   filenameText.setText("Recording Stopped, File Saved : " + recordFile);

        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder=null;

    }
    private void startRecording() {


          recordPath = getActivity().getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss" );
        Date now = new Date();

        recordFile = "Recording_" + formatter.format(now) + ".3gp";
        //day= formatter.format(now);
        audioDoc.setDate(day);
        audioDoc.setType(TYPEAUDIO);
        audioDoc.setName(recordFile);
        audioDoc.setUri(recordPath + "/" + recordFile);



        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recordPath + "/" + recordFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaRecorder.start();
    }
    private void playAudio(File fileToPlay) {

        MediaPlayer mediaPlayer = new MediaPlayer();
        isAudio=true;
        play.setImageResource(R.drawable.ic_baseline_stop_24);


        try {
            mediaPlayer.setDataSource(fileToPlay.getAbsolutePath());
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {

                    isAudio=false;
                    play.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                }
            });
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }






    }

    @Override
    public void start() {

    }
    @Override
    public void resultUrlAudio(Operation.Document file) {

    }
    @Override
    public void resultUrlDocument(Operation.Document file) {

    }
    @Override
    public void error() {

    }


    private void dialogForChooseDocument(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Выберете файлы"),RESULT_LOAD_IMAGE);

    }
    public String getFileName(Uri uri) {
        String result = null;

        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result.replaceAll("\\.","");
    }
    private void uploadFiles(){
if (cash.getText()!=null){
    String datetime= new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss"  ).format(new Date());

    FireBaseService fireBaseService = new FireBaseService();
   // Operation operation = new Operation("",new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA).format(new Date()),new ServiceInfo(getContext()).getUserLocal().name,Integer.parseInt(cash.getText()))
    Operation operation=new
            Operation(datetime,
            day,
            new ServiceInfo(getContext()).getUserLocal().getName(),
           Integer.parseInt(cash.getText().toString())*min,
            comment.getText().toString()
           );
 if (audioDoc.getName()!=null)
     operation.setAudio(audioDoc);
 if (documents.size()!=0)
     operation.setDocs(documents);

fireBaseService.updateMoney("money",operation.amount,day);
fireBaseService.putOperation(operation );
if (min<0){
    fireBaseService.updateMoney("min",operation.amount*-1,day);
}else {
    fireBaseService.updateMoney("plus",operation.amount,day);

}
    if (audioDoc.getName()!=null)
        fireBaseService.uploadFileAudio(audioDoc,datetime);
    if (documents.size()!=0)
        for(Map.Entry<String, Operation.Document> entry : documents.entrySet()) {
            fireBaseService.uploadFilePhoto(entry.getValue(),datetime,documentsUri.get(entry.getKey().toString()));

        }




}
        Toasty.success(getContext(),"Операция выполнена",Toasty.LENGTH_SHORT).show();
dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_LOAD_IMAGE && resultCode==RESULT_OK){
            documents.clear();
            documentsUri.clear();
            if(data.getClipData() != null){

                int totalItemsSelected = data.getClipData().getItemCount();

                for(int i = 0; i < totalItemsSelected; i++){

                    Uri fileUri = data.getClipData().getItemAt(i).getUri();

                    String fileName = getFileName(fileUri);


                    documents.put(fileName,new Operation.Document(TYPEFOTO,fileUri.toString(),fileName,day));
                    documentsUri.put(fileName,fileUri);
                    Toast.makeText(getContext(),fileUri.getPath(),Toast.LENGTH_LONG).show();







                }



            } else if (data.getData() != null){

                documents.put(getFileName(data.getData())  ,new Operation.Document(TYPEFOTO,data.getData().getPath(),getFileName(data.getData()),day));
                documentsUri.put(getFileName(data.getData()) ,data.getData());


            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.play:
                if (isPressed)
                if (   isAudio){
                    isAudio=false;
                    play.setImageResource(R.drawable.ic_baseline_play_arrow_24);

                }
                else {



                playAudio(new File(recordPath+"/"+recordFile));

                }

                break;
            case R.id.document:
                dialogForChooseDocument();
                break;
            case  R.id.fullscreen_dialog_action:

uploadFiles();



                break;
            case R.id.fullscreen_dialog_close:dismiss();break;

        }
    }
}