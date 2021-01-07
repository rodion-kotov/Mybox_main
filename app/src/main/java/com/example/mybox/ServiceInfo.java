package com.example.mybox;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ServiceInfo {

    private SharedPreferences preferences;
    private Context context;

    //Info
    private String CODEPASSDEVICE="passCodeDevice";
    private String CODEPREFERENCES="AppBoxCash";
    private String ISLOGIN="AppBoxCash";

    public ServiceInfo(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(CODEPREFERENCES,Context.MODE_PRIVATE);
    }
public String getPassCodeDevice(){
        return preferences.getString(CODEPASSDEVICE,"0000");
}

    public boolean isLogin() {
        return preferences.getBoolean(ISLOGIN,false);
    }
    public User getUserLocal(){
        Gson gson=new Gson();
        String json=preferences.getString(CODEPASSDEVICE,null);
        Type type=new TypeToken<User>(){}.getType();
         gson.fromJson(json,type);
        return  gson.fromJson(json,type);}
    public void putUserLocal(User localUser){
        Gson gson=new Gson();
        String json=gson.toJson(localUser);
        preferences.edit().putBoolean(ISLOGIN,true).apply();
        preferences.edit().putString(CODEPASSDEVICE,json).apply();
    }
}
