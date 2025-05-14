package com.example.calculiverse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class KategoriAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Kategori> kategoris = new ArrayList<>();

    public void setKategoris(ArrayList<Kategori> kategoris) {
        this.kategoris = kategoris;
    }
    public KategoriAdapter(Context context) {
        this.context = context;
    }



    @Override
    public int getCount() {
        return kategoris.size();
    }

    @Override
    public Object getItem(int position) {
        return kategoris.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_list_subkategori, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(itemView);
        Kategori kategori = (Kategori) getItem(position);

        viewHolder.bind(kategori);
        return itemView;
    }
    private class ViewHolder {
        private TextView txtSubkategori;
        private TextView txtKategori;
        ViewHolder(View view){
            txtSubkategori = view.findViewById(R.id.txt_subkategori);
            txtKategori = view.findViewById(R.id.txt_kategori);
        }
        void bind(Kategori kategori){
            txtSubkategori.setText(kategori.getSubkategori());
            txtKategori.setText(kategori.getKategori());
        }
    }
}
