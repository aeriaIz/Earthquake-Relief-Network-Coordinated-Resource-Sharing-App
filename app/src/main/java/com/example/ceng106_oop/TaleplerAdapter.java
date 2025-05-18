package com.example.ceng106_oop;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ceng106_oop.R;
import com.example.ceng106_oop.Talep;
import com.example.ceng106_oop.SupabaseClient;
import com.example.ceng106_oop.SupabaseServiceforTakip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaleplerAdapter extends RecyclerView.Adapter<TaleplerAdapter.ViewHolder> {
    private Context context;
    private List<Talep> talepList;

    public TaleplerAdapter(Context context, List<Talep> talepList) {
        this.context = context;
        this.talepList = talepList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvItem, tvProvince, tvDistrict, tvNeighborhood, tvStreet, tvBuildingInfo, tvStatus;
        Button btnTeslimAldim;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.textViewCategory);
            tvItem = itemView.findViewById(R.id.textViewItem);
            tvProvince = itemView.findViewById(R.id.textViewProvince);
            tvDistrict = itemView.findViewById(R.id.textViewDistrict);
            tvNeighborhood = itemView.findViewById(R.id.textViewNeighborhood);
            tvStreet = itemView.findViewById(R.id.textViewStreet);
            tvBuildingInfo = itemView.findViewById(R.id.textViewBuildingInfo);
            tvStatus = itemView.findViewById(R.id.textViewStatus);
            btnTeslimAldim = itemView.findViewById(R.id.buttonTeslimAldim);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_talep, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Talep talep = talepList.get(position);

        holder.tvCategory.setText("Kategori: " + talep.getCategory());
        holder.tvItem.setText("İhtiyaç: " + talep.getItem());
        holder.tvProvince.setText("İl: " + talep.getProvince());
        holder.tvDistrict.setText("İlçe: " + talep.getDistrict());
        holder.tvNeighborhood.setText("Mahalle: " + talep.getNeighborhood());
        holder.tvStreet.setText("Sokak: " + talep.getStreet());
        holder.tvBuildingInfo.setText("Bina: " + talep.getBuilding_info());
        holder.tvStatus.setText("Durum: " + talep.getStatus());

        // Teslim Edildi kontrolü
        if ("Teslim Alındı".equalsIgnoreCase(talep.getStatus())) {
            holder.btnTeslimAldim.setEnabled(false);
            holder.btnTeslimAldim.setText("Teslim Alındı");
        } else {
            holder.btnTeslimAldim.setEnabled(true);
            holder.btnTeslimAldim.setText("Teslim Aldım");
        }
        holder.btnTeslimAldim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uuid = talep.getId();  // UUID burada hazır
                String queryParam  = "eq." + uuid;
                Log.d("TaleplerAdapter", "Güncellenecek Talep UUID: " + uuid);

                Map<String, Object> updateMap = new HashMap<>();
                updateMap.put("status", "Teslim Alındı");

                SupabaseServiceforTakip service = SupabaseClient
                        .getClient()
                        .create(SupabaseServiceforTakip.class);

                Call<Void> call = service.updateTalepDurum(queryParam , updateMap);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d("TaleplerAdapter", "Response Code: " + response.code());
                        if (response.isSuccessful()) {
                            Log.d("TaleplerAdapter", "Durum güncelleme başarılı.");
                            Toast.makeText(context, "Durum güncellendi", Toast.LENGTH_SHORT).show();
                            talep.setStatus("Teslim Alındı");
                            notifyItemChanged(holder.getAdapterPosition());
                        } else {
                            Log.e("TaleplerAdapter", "Güncelleme başarısız, body: " + response.errorBody());
                            Toast.makeText(context, "Güncelleme başarısız", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("TaleplerAdapter", "Güncelleme isteği başarısız: " + t.getMessage());
                        Toast.makeText(context, "Hata: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return talepList.size();
    }

}
