package model.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectpam.DetailPlaceActivity;
import com.example.finalprojectpam.R;

import java.util.ArrayList;
import java.util.List;

import model.PlaceModel;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    private Context context;
    private List<PlaceModel> placeList;
    private static ClickListener clickListener;

    public PlaceAdapter(Context context, ArrayList<PlaceModel> placeList){
        this.context = context;
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        final PlaceModel place = placeList.get(position);
        holder.tvName.setText(place.getName());
        holder.tvDesc.setText(place.getDescription());
        //buat set image

        //buat holder untuk tiap action

        holder.placeLayout.setOnClickListener(v -> {
            String dataName = holder.tvName.getText().toString();
            String dataDesc = holder.tvDesc.getText().toString();
            String dataImage = place.getImage(); //need validate
            String dataMap = place.getMaps(); //to url

            //Buat page detail place
            Intent intent = new Intent(context, DetailPlaceActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("pname", dataName);
            bundle.putString("pdesc", dataDesc);
            bundle.putString("pmap", dataMap);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout placeLayout;
        TextView tvName, tvDesc;
        ImageView ivImage, ivUpdate, ivDelete;
        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);

            placeLayout = itemView.findViewById(R.id.place_layout);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            ivImage = itemView.findViewById(R.id.imageView);
            ivUpdate = itemView.findViewById(R.id.update);
            ivDelete = itemView.findViewById(R.id.delete);

            ivDelete.setOnClickListener(this);
            ivUpdate.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), itemView);
        }
    }

    public void setOnItemClickListener(PlaceAdapter.ClickListener clickListener) {
        PlaceAdapter.clickListener = clickListener;
    }
    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}
