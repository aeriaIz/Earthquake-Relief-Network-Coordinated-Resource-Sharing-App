package com.example.ceng106_oop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ceng106_oop.R;
import com.example.ceng106_oop.Gonderi;
import com.example.ceng106_oop.SupabaseClient;
import com.example.ceng106_oop.SupabaseServiceforTakip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GonderilerAdapter extends RecyclerView.Adapter<GonderilerAdapter.ViewHolder> {

    public interface OnGonderiActionListener {
        void onGonderiTeslimEttim(Gonderi gonderi);
    }

    private List<Gonderi> gonderiList;

    private OnGonderiActionListener listener;

    public GonderilerAdapter(List<Gonderi> gonderiList, OnGonderiActionListener listener) {
        this.gonderiList = gonderiList;
        this.listener = listener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvItem, tvProvince, tvDistrict, tvNeighborhood, tvStreet, tvBuildingInfo, tvStatus;
        Button btnTeslimEt;

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
            btnTeslimEt = itemView.findViewById(R.id.buttonTeslimEt);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gonderi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Gonderi gonderi = gonderiList.get(position);
        holder.btnTeslimEt.setVisibility(View.GONE);

        holder.tvCategory.setText("Kategori: " + gonderi.getCategory());
        holder.tvItem.setText("İçerik: " + gonderi.getItem());
        holder.tvProvince.setText("İl: " + gonderi.getProvince());
        holder.tvDistrict.setText("İlçe: " + gonderi.getDistrict());
        holder.tvNeighborhood.setText("Mahalle: " + gonderi.getNeighborhood());
        holder.tvStreet.setText("Sokak: " + gonderi.getStreet());
        holder.tvBuildingInfo.setText("Bina: " + gonderi.getBuilding_info());
        holder.tvStatus.setText("Durum: " + gonderi.getStatus());

        if ("Teslim Edildi".equalsIgnoreCase(gonderi.getStatus())) {
            holder.btnTeslimEt.setEnabled(false);
            holder.btnTeslimEt.setText("Teslim Edildi");
        } else {
            holder.btnTeslimEt.setEnabled(true);
            holder.btnTeslimEt.setText("Teslim Et");
        }

        holder.btnTeslimEt.setOnClickListener(v -> {
            SupabaseServiceforTakip service = SupabaseClient.getClient().create(SupabaseServiceforTakip.class);
            Map<String, Object> updateMap = new HashMap<>();
            updateMap.put("status", "Teslim Edildi");

            Call<Void> call = service.updateGonderiDurum("eq." + gonderi.getId(), updateMap);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        holder.tvStatus.setText("Durum: Edildi");
                        holder.btnTeslimEt.setEnabled(false);
                        holder.btnTeslimEt.setText("Teslim Edildi");
                        Toast.makeText(holder.itemView.getContext(), "Durum güncellendi", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(holder.itemView.getContext(), "Güncelleme başarısız", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(holder.itemView.getContext(), "Ağ hatası", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return gonderiList.size();
    }
}
