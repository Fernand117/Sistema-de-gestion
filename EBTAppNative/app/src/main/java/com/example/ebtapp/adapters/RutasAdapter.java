package com.example.ebtapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.ebtapp.R;
import com.example.ebtapp.model.Rutas;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RutasAdapter extends SimpleAdapter {
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

    public RutasAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Rutas rutas = new Rutas();

        if (convertView == null) {
            view = inflater.inflate(R.layout.item_list_rutas, null);
        }

        HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);

        TextView txtIdRuta = view.findViewById(R.id.txtIDRuta);
        txtIdRuta.setText((String) data.get("id"));

        TextView txtNombre = view.findViewById(R.id.txtNombreRuta);
        rutas.setNombre((String) data.get("nombre"));
        txtNombre.setText(rutas.getNombre());

        Button btnVerRuta = view.findViewById(R.id.btnVerRuta);
        btnVerRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);
            }
        });

        return view;
    }
}
