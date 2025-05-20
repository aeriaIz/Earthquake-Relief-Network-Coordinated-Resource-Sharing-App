package com.example.ceng106_oop;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
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
    //Adapter içinde kullanılacak context (UI için) ve gösterilecek Talep nesnelerinin listesi.
    private Context context;
    private List<Talep> talepList;

    public TaleplerAdapter(Context context, List<Talep> talepList) {
        this.context = context;
        this.talepList = talepList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvItem, tvProvince, tvDistrict, tvNeighborhood, tvStreet, tvBuildingInfo, tvStatus;
        Button btnTeslimAldim;

        //Görünümler XML'den bağlanıyor
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

    //Görünüm oluşturuluyor, xml dosyasındaki tasarım java nesnesine dönüştürülüyor
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_talep, parent, false);
        return new ViewHolder(view);
    }
    // ViewHolder oluşturulan kartın içindeki tüm TextView, Button gibi öğelere erişmeyi sağlar

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Liste içindeki Talep verisi alınıyor
        Talep talep = talepList.get(position);

        //Tüm TextView'lara Talep içeriği yazılıyor
        holder.tvCategory.setText("Kategori: " + talep.getCategory());
        holder.tvItem.setText("İhtiyaç: " + talep.getItem());
        holder.tvProvince.setText("İl: " + talep.getProvince());
        holder.tvDistrict.setText("İlçe: " + talep.getDistrict());
        holder.tvNeighborhood.setText("Mahalle: " + talep.getNeighborhood());
        holder.tvStreet.setText("Sokak: " + talep.getStreet());
        holder.tvBuildingInfo.setText("Bina: " + talep.getBuilding_info());
        holder.tvStatus.setText("Durum: " + talep.getStatus());

        // Teslim Edildi kontrolü
        //Talep daha önce teslim alınmışsa buton pasif hale getiriliyor
        if ("yolda".equalsIgnoreCase(talep.getStatus())) {
            holder.btnTeslimAldim.setEnabled(true);
            holder.btnTeslimAldim.setText("Teslim Aldım");
        } else if ("teslim alındı".equalsIgnoreCase(talep.getStatus())) {
            holder.btnTeslimAldim.setEnabled(false);
            holder.btnTeslimAldim.setText("Teslim Alındı");
        } else { // beklemede durumu
            holder.btnTeslimAldim.setEnabled(false);
            holder.btnTeslimAldim.setText("Teslim Aldım");
            holder.btnTeslimAldim.setBackgroundColor(ContextCompat.getColor(context, R.color.button_waiting));
        }

        //Buton tıklanma olayı
        holder.btnTeslimAldim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uuid = talep.getId();  // Supabase'de güncellenecek kaydı seçmek için ID alınıyr
                String queryParam  = "eq." + uuid;
                Log.d("TaleplerAdapter", "Güncellenecek Talep UUID: " + uuid);

                Map<String, Object> updateMap = new HashMap<>();
                updateMap.put("status", "Teslim Alındı");

                //Supabase'e Retrofit servisi üzerinden bağlanılıyor
                SupabaseServiceforTakip service = SupabaseClient
                        .getClient()
                        .create(SupabaseServiceforTakip.class);

                //Veri güncelleme çağrısı yapılıyor
                Call<Void> call = service.updateTalepDurum(queryParam , updateMap);
                call.enqueue(new Callback<Void>() {
                    //Güncelleme başarılıysa durumu güncelleniyor
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
