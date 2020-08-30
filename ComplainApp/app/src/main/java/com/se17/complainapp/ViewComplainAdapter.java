package com.se17.complainapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.se17.complainapp.Complain;
import com.se17.complainapp.R;

import java.util.ArrayList;

public class ViewComplainAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Complain> complainList;

    public ViewComplainAdapter(Context context, int layout, ArrayList<Complain> complainList) {
        this.context = context;
        this.layout = layout;
        this.complainList = complainList;
    }

    @Override
    public int getCount() {
        return complainList.size();
    }

    @Override
    public Object getItem(int i) {
        return complainList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView categoryTextV, severityTextV;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.categoryTextV = row.findViewById(R.id.categoryTextV);
            holder.severityTextV = row.findViewById(R.id.severityTextV);
            holder.imageView = row.findViewById(R.id.complainImageV);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder)row.getTag();
        }

        Complain complain = complainList.get(i);

        holder.categoryTextV.setText(complain.getCategory());
        holder.severityTextV.setText(complain.getSeverity());

        byte[] recordImage = complain.getComplainImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(recordImage, 0, recordImage.length);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }
}
