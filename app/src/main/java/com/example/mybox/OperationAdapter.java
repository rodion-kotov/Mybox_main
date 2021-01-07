package com.example.mybox;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OperationAdapter extends RecyclerView.Adapter<OperationAdapter.RecyclerViewAdapter> {
    ArrayList<Operation> operations;
    Context context;
    OnItemList onItemList;
int pos;
    public OperationAdapter(ArrayList<Operation> operations, Context context, OnItemList onItemList) {
        this.operations = operations;
        this.context = context;
        this.onItemList = onItemList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.operation_item,parent,false);

        return new  RecyclerViewAdapter(view,onItemList);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter holder, int position) {
Operation operation=operations.get(position);
holder.money.setText(String.valueOf(operation.amount) );
holder.nameUser.setText(operation.idUser);
if(operation.docs==null) holder.isDoc.setImageResource(R.color.transparent);
        if(operation.audio==null) holder.isAudio.setImageResource(R.color.transparent);
        if(operation.comment.equals("")) holder.isComment.setImageResource(R.color.transparent);



        Date result = null;
        try {

            result = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss" ).parse(operation.date);
              holder.time.setText(new SimpleDateFormat("HH_mm").format(result));
        } catch (ParseException e) {
            e.printStackTrace();
        }


if (operation.getAmount()<0){
holder.typeOperation.setImageResource(R.drawable.ic_baseline_remove_24);
holder.money.setTextColor(Color.rgb(255,0,38));

}else {
    holder.typeOperation.setImageResource(R.drawable.ic_baseline_add_24);
    holder.money.setTextColor(Color.rgb(1,159,64));


}

    }

    @Override
    public int getItemCount() {
        return operations.size();
    }

    class RecyclerViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameUser,money,time;
        ImageView typeOperation,isComment,isAudio,isDoc;
        CardView cardView;


        RecyclerViewAdapter(@NonNull View itemView, OnItemList onItemList) {

            super(itemView);
            cardView=itemView.findViewById(R.id.card_operation);
            nameUser=itemView.findViewById(R.id.nameview);
            money=itemView.findViewById(R.id.moneyview);
            time=itemView.findViewById(R.id.timeview);
            typeOperation=itemView.findViewById(R.id.type);
            isComment=itemView.findViewById(R.id.iscomment);
            isAudio=itemView.findViewById(R.id.isaudio);
            isDoc=itemView.findViewById(R.id.isdoc);

            cardView.setOnClickListener(this);



        }

        @Override
        public void onClick(View view) {

onItemList.onItemClick(operations.get(getAdapterPosition()) );
        }
    }

        public  interface OnItemList{
        void onItemClick( Operation operation);
    }
}
