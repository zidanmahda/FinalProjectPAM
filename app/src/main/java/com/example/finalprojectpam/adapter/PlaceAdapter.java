package com.example.finalprojectpam.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalprojectpam.R;
import com.example.finalprojectpam.UpdateDataActivity;
import com.example.finalprojectpam.model.PlaceModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {
    private List<PlaceModel> itemList;

    public PlaceAdapter(List<PlaceModel> itemLIst) {
        this.itemList = itemLIst;
    }

    public void setItemList(List<PlaceModel> itemList) {
        this.itemList = itemList;
    }
    DatabaseReference db;
    FirebaseDatabase data;
    FirebaseUser current;


    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View daftar = inflater.inflate(R.layout.item_place,parent,false);
        PlaceViewHolder isi = new PlaceViewHolder(daftar);
        return isi;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        holder._namaTempat.setText(itemList.get(position).getName());
        holder._deskirpsiTempat.setText(itemList.get(position).getDescription());
        Glide.with(holder.itemView.getContext())
                .load(itemList.get(position).getImage())
                .override(100,100)
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        if(itemList == null){
            return 0;
        }
        return itemList.size();
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView _namaTempat,_deskirpsiTempat;
        ImageView _delete, _update;
        ImageView img;
        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            data = FirebaseDatabase.getInstance();
            _namaTempat = itemView.findViewById(R.id.nama_tempat);
            _deskirpsiTempat = itemView.findViewById(R.id.deskripsi_tempat);
            _delete = itemView.findViewById(R.id.delete);
            _delete.setOnClickListener(this);
            _update= itemView.findViewById(R.id.update);
            _update.setOnClickListener(this);
            current = FirebaseAuth.getInstance().getCurrentUser();
            db = data.getReference().child(PlaceModel.class.getSimpleName()).child(current.getUid());
            img = itemView.findViewById(R.id.img);
        }

        @Override
        public void onClick(View view) {
            if(_delete.getId()==view.getId()){
                int a = getAdapterPosition();
                db.child(itemList.get(a).getKey()).removeValue();
//        db.push().setValue(new Note(itemList.get(a).getKey(),itemList.get(a).getIsi()));
                itemList.remove(a);
                notifyItemRemoved(a);
                notifyItemRangeChanged(a,itemList.size());
                notifyDataSetChanged();
            } else if(_update.getId()==view.getId()){
                int a = getAdapterPosition();
                Intent upd = new Intent(view.getContext(), UpdateDataActivity.class);
                upd.putExtra("key",itemList.get(a).getKey());
                upd.putExtra("nama",itemList.get(a).getName());
                upd.putExtra("deskripsi",itemList.get(a).getDescription());
                upd.putExtra("link", itemList.get(a).getMaps());
                view.getContext().startActivity(upd);
            }
        }
    }
}
