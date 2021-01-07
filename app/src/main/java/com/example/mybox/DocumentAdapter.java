package com.example.mybox;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> {
    private ArrayList<Operation.Document> mDoc = new ArrayList<>();
     private ClickOnDoc clickOnDoc;
    private Context mContext;

    public DocumentAdapter(ArrayList<Operation.Document> mDoc, ClickOnDoc clickOnDoc, Context mContext) {
        this.mDoc = mDoc;
        this.clickOnDoc = clickOnDoc;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Glide.with(mContext)
                .asBitmap()
                .load(mDoc.get(position).getUrl())
                .into(holder.image);

        holder.name.setText(mDoc.get(position).getName());

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOnDoc.clickDoc(mDoc.get(position));

                Toast.makeText(mContext, mDoc.get(position).getName()   , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDoc.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name);
        }
    }
    interface ClickOnDoc{
        void clickDoc(Operation.Document document);
    }
}
