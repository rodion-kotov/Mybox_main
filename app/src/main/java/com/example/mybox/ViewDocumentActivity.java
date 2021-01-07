package com.example.mybox;

import android.Manifest;

import android.content.Context;
import android.content.pm.PackageManager;

import android.os.Bundle;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Nullable;

public class ViewDocumentActivity extends DialogFragment {
    TextView name;
    ImageView imageView;
    String nam;
    String path;
    Context context;
    ImageButton close,reston;
    int k=1;

    public ViewDocumentActivity(String nam, String path, Context context) {
        this.nam = nam;
        this.path = path;
        this.context = context;
    }

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE,R.style.FullDialog );


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.document_dialog,container,false);
        imageView=view.findViewById(R.id.imagee);
        name=view.findViewById(R.id.fullscreen_dialog_text);
        close=view.findViewById(R.id.fullscreen_dialog_close);
        reston=view.findViewById(R.id.fullscreen_dialog_re);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
reston.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        k++;
        Picasso.with(context).load(path).rotate(90*k).into(imageView);
    }
});
        Picasso.with(context).load(path).rotate(90).into(imageView);



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












}
