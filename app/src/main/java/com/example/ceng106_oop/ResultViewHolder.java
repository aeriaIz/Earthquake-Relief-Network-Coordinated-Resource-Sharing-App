package com.example.ceng106_oop;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class ResultViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;

    public ResultViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(android.R.id.text1);
    }
}
