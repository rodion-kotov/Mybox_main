package com.example.mybox;

import android.content.Context;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.EventListener;

public class FireBaseService {
    public static final String TYPEFOTO="docs";
    public static final String TYPEAUDIO="audio";
    public static final String Make="make";
    public static final String Remove="remove";
    FirebaseDatabase database;
    StorageReference mStorageRef;
     LoadDay view=null;
     String date=null;
    ValueEventListener valueEventListener=null;

    public FireBaseService(LoadDay view, String date) {
        this.view = view;
        this.date = date;
    }

    public FireBaseService( ) {

        database = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }


    void updateMoney(final String path, final int cash,final String day){
// Write a message to the database

        final DatabaseReference myRef = database.getReference("moneyBox");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int value = dataSnapshot.getValue(Integer.class)+cash;
                database.getReference("moneyBox").setValue(value );
                database.getReference("days/"+day+"/"+path).setValue(value );

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });


    }




   void read(String user, final Login login){
       login.start();
        // Read from the database
        database.getReference("Users/"+user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
try {
    login.result(true,dataSnapshot.getValue(User.class));
}catch (Exception e){
    login.error();
}



            }

            @Override
            public void onCancelled(DatabaseError error) {
            login.error();
            }
        });
    }
interface Loading {
        void start();
        void pause();
        void stop();
    }
    public void newDay(final String date  ) {
        final DatabaseReference myRef = database.getReference("days/"+date);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                DayBox dayBox = dataSnapshot.getValue(DayBox.class) ;
                if (dayBox==null){

                    final DatabaseReference myRef2 = database.getReference("moneyBox");

                    myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            int value = dataSnapshot.getValue(Integer.class);
                            database.getReference("days/"+date+"/money").setValue(value);


                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value

                        }
                    });

                    database.getReference("days/"+date+"/min").setValue(0 );
                    database.getReference("days/"+date+"/plus").setValue(0 );


                }



            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }

    interface Login{
        void start();
        void result(Boolean logout,User user);
        void error();

    }

    void putNewOperation( Operation operation,String path){
        // Write a message to the database

        DatabaseReference myRef = database.getReference(path);
       myRef.setValue(operation);

        // Read from the database
      /*  database.getReference("message2" ).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    dataSnapshot.getValue(Operation.class);
                }catch (Exception e){

                }



            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });*/




    }
    void putOperation(Operation operation){
        final DatabaseReference myRef = database.getReference("days/"+operation.date+"/operations/"+operation.id);
        myRef.setValue(operation);
    }
    void uploadFile(final UploadFile view, final String recordFile, final String recordPath, final String day, final String type){

        Uri file = Uri.fromFile(new File(recordFile));
        
        StorageReference riversRef = mStorageRef.child(day+"/"+type+"/"+recordFile) ;

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                       mStorageRef.child(day+"/"+type+"/"+recordFile).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                           @Override
                           public void onSuccess(Uri uri) {
                               /*switch (type){
                                   case AUDIOTYPE : view.resultUrlAudio(new Operation.Document(type,uri.toString(),recordFile,day));break;
                                   case DOCUMENTTYPE:view.resultUrlDocument(new Operation.Document(type,uri.toString(),recordFile,day));break;
                                   default:
                                       throw new IllegalStateException("Unexpected value: " + type);
                               }*/
                           }
                       });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...

                    }
                });
    }
    void setUrlFiles(String path,String url){
        // Write a message to the database

        DatabaseReference myRef = database.getReference(path);

        myRef.setValue(url);
    }
    void uploadFileAudio(final Operation.Document document, final String time){






        final StorageReference riversRef = mStorageRef.child(document.getDate()).child(document.getType()).child(document.getName()) ;

        riversRef.putFile(Uri.fromFile(new File(document.getUri())))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                if (TYPEAUDIO.equals(document.getType())){
                                    setUrlFiles("days/"+document.getDate()+"/operations/"+time+"/audio/"+"/url",uri.toString());

                                }else {
                                    setUrlFiles("days/"+document.getDate()+"/operations/"+time+"/docs/"+document.getName()+"/url",uri.toString());
                                }

                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...

                    }
                });
    }


    void uploadFilePhoto(final Operation.Document document, final String time,final Uri uri){






        final StorageReference riversRef = mStorageRef.child(document.getDate()).child(document.getType()).child(document.getName()) ;

        riversRef.putFile( uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                if (TYPEAUDIO.equals(document.getType())){
                                    setUrlFiles("days/"+document.getDate()+"/operations/"+time+"/audio/"+"/url",uri.toString());

                                }else {
                                    setUrlFiles("days/"+document.getDate()+"/operations/"+time+"/docs/"+document.getName()+"/url",uri.toString());
                                }

                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...

                    }
                });
    }



    void uploadFileOffline(final String recordFile, final String recordPath, final String day, final String type){

        Uri file = Uri.fromFile(new File(recordFile));

        StorageReference riversRef = mStorageRef.child(day+"/"+type+"/"+recordFile) ;

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        mStorageRef.child(day+"/"+type+"/"+recordFile).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...

                    }
                });
    }
    interface UploadFile{
        void start();
        void resultUrlAudio(Operation.Document file);
        void resultUrlDocument(Operation.Document file);
        void error();

    }
     class MyResult {
          DatabaseReference first;

        public MyResult(DatabaseReference first, ValueEventListener second) {
            this.first = first;
            this.second = second;
        }

        ValueEventListener second;




    }
    MyResult loadDay(final LoadDay view, final String date){
        // Read from the database
        DatabaseReference myRef = database.getReference("days/"+date);
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DayBox value = dataSnapshot.getValue(DayBox.class);
                view.resultDay(value,date);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        myRef.addValueEventListener(valueEventListener);
        return new MyResult(myRef,valueEventListener);

    }
    void removeEventListener(MyResult myResult){
        myResult.first.removeEventListener(myResult.second);
    }

    interface LoadDay{
        void resultDay(DayBox dayBox,String date);
    }
}
