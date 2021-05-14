package com.example.ebtapp.ui.gallery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ebtapp.R;
import com.example.ebtapp.database.DataBaseBack;
import com.example.ebtapp.databinding.FragmentGalleryBinding;
import com.example.ebtapp.model.Rutas;
import com.example.ebtapp.service.APIService;
import com.example.ebtapp.ui.PuntosVentasActivity;

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

public class GalleryFragment extends Fragment {

        private String line;
        private int responseCode;
        private JSONObject jsonObject;
        private InputStream inputStream;
        private StringBuilder builderResult;
        private HttpURLConnection connection;
        private BufferedReader bufferedReader;

    private DataBaseBack dataBaseBack;

    private ListView listViewRutas;
    private Rutas rutas;
    private TextView txtMensaje;
    private ArrayList<HashMap<String, String>> listRutas;
    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        dataBaseBack = new DataBaseBack(getContext());
        rutas = new Rutas();
        txtMensaje = (TextView) view.findViewById(R.id.txtMensajelist);
        listViewRutas = (ListView) view.findViewById(R.id.listRutas);
        listRutas = new ArrayList<HashMap<String, String>>();

        listViewRutas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idRuta = ((TextView) view.findViewById(R.id.txtIDRuta)).getText().toString();
                Intent pvIntent = new Intent(getContext(), PuntosVentasActivity.class);
                pvIntent.putExtra("idRuta", idRuta);
                startActivity(pvIntent);
            }
        });
        new listRutasAsync().execute();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class listRutasAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private APIService apiService = new APIService();
        private static final String urlComplement = "/lista/rutas/usuario";
        private static final String method = "POST";
        private static final String style = "normal";
        private static final String jsonMsj = "Mensaje";
        private static final String jsonResponse = "Rutas";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Cargando rutas");
            progressDialog.setTitle("Espere por favor");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                HashMap<String, String> params = new HashMap<>();
                try {
                    SQLiteDatabase database = dataBaseBack.getWritableDatabase();
                    if (database != null){
                        database.acquireReference();
                        Cursor cursor = database.rawQuery("SELECT * FROM usuario", null);
                        if (cursor != null && cursor.getCount() > 0){
                            cursor.moveToFirst();
                            do {
                                params.put("id", cursor.getString(cursor.getColumnIndex("idUsuario")));
                            } while (cursor.moveToNext());
                        }
                        cursor.close();
                        database.close();
                    }
                } catch (SQLException e){
                    e.printStackTrace();
                }

                connection = apiService.ServiceSF(params, urlComplement, method, style);

                try {
                    responseCode = connection.getResponseCode();

                    if (responseCode == 404){
                        inputStream = new BufferedInputStream(connection.getErrorStream());
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        builderResult = new StringBuilder();

                        while ((line = bufferedReader.readLine()) != null){
                            builderResult.append(line);
                        }

                        try {
                            jsonObject = new JSONObject(builderResult.toString());

                            if (jsonObject != null){
                                listViewRutas.setEnabled(false);
                                txtMensaje.setText(jsonObject.getString(jsonMsj));
                            }

                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    } else if (responseCode == 200) {
                        inputStream = new BufferedInputStream(connection.getInputStream());
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        builderResult = new StringBuilder();

                        while ((line = bufferedReader.readLine()) != null) {
                            builderResult.append(line);
                        }

                        try {
                            jsonObject = new JSONObject(builderResult.toString());

                            if (jsonObject != null){
                                JSONArray jsonArray = jsonObject.getJSONArray(jsonResponse);

                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsonRutas = jsonArray.getJSONObject(i);

                                    rutas.setId(Integer.parseInt(jsonRutas.getString("id")));
                                    rutas.setNombre(jsonRutas.getString("nombre"));
                                    HashMap<String, String> mapRutas = new HashMap<>();
                                    mapRutas.put("Nombre", rutas.getNombre());
                                    mapRutas.put("IDRuta", String.valueOf(rutas.getId()));
                                    listRutas.add(mapRutas);
                                }
                            }

                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }

                } catch (IOException e){
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
            //super.onPostExecute(jsonObject);
            if (progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            if (responseCode == 200){
                SimpleAdapter simpleAdapter = new SimpleAdapter(
                        getContext(), listRutas,
                        R.layout.item_list_rutas,
                        new String[]{"Nombre", "IDRuta"}, new int[]{R.id.txtNombreRuta, R.id.txtIDRuta}
                );
                listViewRutas.setAdapter(simpleAdapter);
            }
        }
    }
}