package com.example.mybox;

import android.net.Uri;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Keep
public class Operation implements Serializable {
    String id;
    String date;
    String idUser;
    int amount;
    String comment;
    Document audio;
    HashMap<String,Document> docs;

    public Operation() {
    }

    public Operation(String id, String date, String idUser, int amount, String comment) {
        this.id = id;
        this.date = date;
        this.idUser = idUser;
        this.amount = amount;
        this.comment = comment;
    }

    public Operation(String id, String date, String idUser, int amount, String comment, Document audio, HashMap<String, Document> docs) {
        this.id = id;
        this.date = date;
        this.idUser = idUser;
        this.amount = amount;
        this.comment = comment;
        this.audio = audio;
        this.docs = docs;
    }

    public Document getAudio() {
        return audio;
    }

    public void setAudio(Document audio) {
        this.audio = audio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public ArrayList<Document> getArrayListDocument(){
        ArrayList<Document> f= new ArrayList<>();
        if (docs!=null)
        for(Map.Entry<String, Document> entry : docs.entrySet()) {
         f.add(entry.getValue());
        }


        return f;

    }



    public HashMap<String, Document> getDocs() {
        return docs;
    }

    public void setDocs(HashMap<String, Document> docs) {
        this.docs = docs;
    }

    @Keep
   public static class Document{



        private String type;
        private String url;
      private String uri;
        private String path;
        private String name;
        private String date;

        public Document() {
        }

        public Document(String type, String uri,  String name, String date) {
            this.type = type;
            this.uri = uri;

            this.name = name;
            this.date = date;
        }

        public Document(String type, String url, String uri, String path, String name, String date) {
            this.type = type;
            this.url = url;
            this.uri = uri;
            this.path = path;
            this.name = name;
            this.date = date;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }



        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getUrl() {
            return url;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
