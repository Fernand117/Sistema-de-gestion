package com.example.ebtapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.ebtapp.R;
import com.example.ebtapp.model.PVentasModel;
import com.example.ebtapp.model.Rutas;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PVentasRutasAdapter  extends SimpleAdapter {
    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */

    public LayoutInflater inflater = null;

    public PVentasRutasAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Rutas rutas = new Rutas();
        PVentasModel pVentasModel = new PVentasModel();

        if (convertView == null) {
            view = inflater.inflate(R.layout.item_pv_list, null);
        }

        HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);

        TextView txtNRuta = view.findViewById(R.id.txtNRuta);
        rutas.setNombre((String) data.get("ruta"));
        txtNRuta.setText(rutas.getNombre());

        TextView txtNPVenta = view.findViewById(R.id.txtNPV);
        pVentasModel.setNombre((String) data.get("punto"));
        txtNPVenta.setText(pVentasModel.getNombre());

        ImageView imgPVenta = view.findViewById(R.id.imgPV);
        pVentasModel.setFoto((String) data.get("foto"));
        Picasso.with(view.getContext()).load(pVentasModel.getFoto()).resize(300, 180).into(imgPVenta);

        return  view;
    }
}
