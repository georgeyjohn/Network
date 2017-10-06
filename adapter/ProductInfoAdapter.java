package com.ip.barcodescanner.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ip.barcodescanner.R;
import com.ip.barcodescanner.entity.ResponseProductInformation;

/**
 * Created by deepak on 6/8/15.
 */
public class ProductInfoAdapter extends ArrayAdapter<ResponseProductInformation> {

    private Context context;
    private ResponseProductInformation[] array;

    public ProductInfoAdapter(Context context, ResponseProductInformation[] objects) {
        super(context, R.layout.list_menu, objects);
        this.context = context;
        this.array = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        UI ui;

        if (null == convertView) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_menu, null);
            ui = new UI();
            ui.key = (TextView) rowView.findViewById(R.id.key);
            ui.value = (TextView) rowView.findViewById(R.id.value);
            rowView.setTag(ui);
        } else {
            ui = (UI) rowView.getTag();
        }

        ui.key.setText(array[position].getKey());
        ui.value.setText(array[position].getValue());

        return rowView;
    }

    static class UI {
        TextView key, value;
    }

}
