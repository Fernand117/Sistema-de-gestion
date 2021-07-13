package com.example.ebtapp.ui.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ebtapp.R;
import com.example.ebtapp.adapters.PVentasRutasAdapter;
import com.example.ebtapp.database.DataBaseBack;
import com.example.ebtapp.databinding.FragmentHomeBinding;
import com.example.ebtapp.model.DireccionesModel;
import com.example.ebtapp.model.PVentasModel;
import com.example.ebtapp.model.Rutas;
import com.example.ebtapp.model.UsuariosModel;
import com.example.ebtapp.service.APIService;
import com.example.ebtapp.ui.PuntosVentasActivity;
import com.example.ebtapp.ui.TirosActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {

    private Rutas rutas;
    private APIService apiService;
    private DireccionesModel direccionesModel;
    private UsuariosModel usuariosModel;
    private PVentasModel pVentasModel;
    private ProgressDialog progressDialog;
    private DataBaseBack dataBaseBack;
    private ListView viewPuntos;
    private ArrayList<HashMap<String, String>> listPVRutas;
    private PVentasRutasAdapter pVentasRutasAdapter;

    private TextView txtNumeroPuntoVentas;
    private TextView txtNumeroRutas;

    private String line;
    private String nPVentas;
    private String nRutas;
    private String mensajeErrorServidor;
    private int responseCode;
    private JSONObject jsonObject;
    private InputStream inputStream;
    private StringBuilder builderResult;
    private HttpURLConnection connection;
    private BufferedReader bufferedReader;
    private HashMap<String, String> params;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dataBaseBack = new DataBaseBack(getContext());
        usuariosModel = new UsuariosModel();
        rutas = new Rutas();
        apiService = new APIService();
        pVentasModel = new PVentasModel();
        direccionesModel = new DireccionesModel();
        progressDialog = new ProgressDialog(getContext());

        txtNumeroPuntoVentas = (TextView) view.findViewById(R.id.txtNumeroPVentas);
        txtNumeroRutas = (TextView) view.findViewById(R.id.txtNumeroRutas);
        viewPuntos = (ListView) view.findViewById(R.id.listEntregas);
        listPVRutas = new ArrayList<HashMap<String, String>>();

        new getTotalPuntosVentas().execute();

        viewPuntos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int viewID = view.getId();
                switch (viewID) {
                    case R.id.btnVerPVentas:
                        HashMap<String, Object> data = (HashMap<String, Object>)pVentasRutasAdapter.getItem(position);
                        String pventanombre = (String) data.get("punto");
                        Toast.makeText(getContext(), pventanombre, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), TirosActivity.class);
                        intent.putExtra("idPVenta", (String) data.get("idpunto"));
                        intent.putExtra("punto", pventanombre);
                        startActivity(intent);
                        break;
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class getTotalPuntosVentas extends AsyncTask<String, String, JSONObject> {

        private static final String method = "POST";
        private static final String style = "normal";
        private static final String responseRutas = "Rutas";
        private static final String responsePuntos = "Puntos";
        private static final String responseLista = "Lista";
        private static final String urlComplement = "/total/puntos-ventas";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                params = new HashMap<>();
                try {
                    SQLiteDatabase database = dataBaseBack.getWritableDatabase();
                    if (database != null){
                        Cursor cursor = database.rawQuery("SELECT * FROM usuario", null);
                        if (cursor != null && cursor.getCount() > 0){
                            cursor.moveToFirst();
                            do {
                                usuariosModel.setNombre(cursor.getString(cursor.getColumnIndex("usuario")));
                                usuariosModel.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("idUsuario"))));
                            } while (cursor.moveToNext());
                        }
                        cursor.close();
                        database.close();
                    }
                } catch (SQLException e){
                    e.printStackTrace();
                }

                params.put("usuario", usuariosModel.getNombre());
                params.put("idUsuario", String.valueOf(usuariosModel.getId()));
                connection = apiService.ServiceSF(params, urlComplement, method, style);
                try {
                    responseCode = connection.getResponseCode();
                    if (responseCode == 500) {
                        mensajeErrorServidor = "Ocurrió un error, servidor no disponible por el momento";
                    } else if (responseCode == 200) {
                        inputStream = new BufferedInputStream(connection.getInputStream());
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        builderResult = new StringBuilder();
                        while ((line = bufferedReader.readLine()) != null) {
                            builderResult.append(line);
                        }
                        try {
                            jsonObject = new JSONObject(builderResult.toString());
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                        if (jsonObject != null) {
                            JSONArray jsonArrayPuntos = jsonObject.getJSONArray(responsePuntos);
                            for (int i = 0; i < jsonArrayPuntos.length(); i++) {
                                JSONObject jsonObjectPuntos = jsonArrayPuntos.getJSONObject(i);
                                nPVentas = jsonObjectPuntos.getString("Total");
                            }

                            JSONArray jsonArrayRutas = jsonObject.getJSONArray(responseRutas);
                            for (int i = 0; i < jsonArrayRutas.length(); i++) {
                                JSONObject jsonObjectRutas = jsonArrayRutas.getJSONObject(i);
                                nRutas = jsonObjectRutas.getString("Total");
                            }

                            JSONArray jsonArrayLista = jsonObject.getJSONArray(responseLista);
                            for (int i = 0; i < jsonArrayLista.length(); i++) {
                                JSONObject jsonObjectPuntos = jsonArrayLista.getJSONObject(i);
                                pVentasModel.setId(Integer.parseInt(jsonObjectPuntos.optString("IDPunto")));
                                pVentasModel.setNombre(jsonObjectPuntos.getString("Punto"));
                                pVentasModel.setFoto(jsonObjectPuntos.getString("foto"));
                                rutas.setId(Integer.parseInt(jsonObjectPuntos.getString("IDRuta")));
                                rutas.setNombre(jsonObjectPuntos.getString("Ruta"));
                                direccionesModel.setDireccion(jsonObjectPuntos.getString("direccion"));
                                direccionesModel.setLocalidad(jsonObjectPuntos.getString("localidad"));
                                direccionesModel.setMunicipio(jsonObjectPuntos.getString("municipio"));
                                HashMap<String, String> mapPVRutas = new HashMap<>();
                                mapPVRutas.put("idruta", String.valueOf(rutas.getId()));
                                mapPVRutas.put("ruta", rutas.getNombre());
                                mapPVRutas.put("idpunto", String.valueOf(pVentasModel.getId()));
                                mapPVRutas.put("punto", pVentasModel.getNombre());
                                mapPVRutas.put("foto", pVentasModel.getFoto());
                                mapPVRutas.put("direccion", direccionesModel.getDireccion());
                                mapPVRutas.put("localidad", direccionesModel.getLocalidad());
                                mapPVRutas.put("municipio", direccionesModel.getMunicipio());
                                listPVRutas.add(mapPVRutas);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return jsonObject;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            txtNumeroPuntoVentas.setText(nPVentas);
            txtNumeroRutas.setText(nRutas);
            pVentasRutasAdapter = new PVentasRutasAdapter(getContext(), listPVRutas, R.layout.item_pv_list, new String[]{}, new int[]{});
            pVentasRutasAdapter.notifyDataSetChanged();
            viewPuntos.invalidateViews();
            viewPuntos.refreshDrawableState();
            viewPuntos.setAdapter(pVentasRutasAdapter);
        }
    }
}