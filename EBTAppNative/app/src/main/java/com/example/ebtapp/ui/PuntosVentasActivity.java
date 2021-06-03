package com.example.ebtapp.ui;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ebtapp.MainActivity;
import com.example.ebtapp.R;
import com.example.ebtapp.adapters.PVentasRutasAdapter;
import com.example.ebtapp.database.DataBaseBack;
import com.example.ebtapp.model.PVentasModel;
import com.example.ebtapp.model.Rutas;
import com.example.ebtapp.model.UsuariosModel;
import com.example.ebtapp.service.APIService;

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

public class PuntosVentasActivity extends AppCompatActivity {

    private String line;
    private int responseCode;
    private JSONObject jsonObject;
    private InputStream inputStream;
    private StringBuilder builderResult;
    private HttpURLConnection connection;
    private BufferedReader bufferedReader;

    private Rutas rutas;
    private PVentasModel pVentasModel;
    private TextView txtMensajePV;
    private ListView listViewPVRutas;
    private ArrayList<HashMap<String, String>> listPVRutas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntos_ventas);

        rutas = new Rutas();
        pVentasModel = new PVentasModel();
        rutas.setId(Integer.parseInt(getIntent().getStringExtra("idRuta")));
        rutas.setNombre(getIntent().getStringExtra("punto"));

        getSupportActionBar().setTitle(rutas.getNombre());

        txtMensajePV = (TextView) findViewById(R.id.txtMensajePV);
        listViewPVRutas = (ListView) findViewById(R.id.listViewPV);
        listPVRutas = new ArrayList<HashMap<String, String>>();
        listViewPVRutas.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (MotionEvent.ACTION_DOWN == 0) {
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            break;
                    }
                } else if (MotionEvent.ACTION_UP == 1) {
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    switch (action) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            break;
                    }
                }
                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        new getPVAsync().execute();
    }

    private class getPVAsync extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private APIService apiService = new APIService();
        private static final String urlComplement = "/listar/puntos-rutas";
        private static final String method = "POST";
        private static final String style = "normal";
        private static final String jsonMsj = "Mensaje";
        private static final String jsonResponse = "Puntos";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PuntosVentasActivity.this);
            progressDialog.setMessage("Cargando lista de puntos de venta");
            progressDialog.setTitle("Espere por favor");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {

                HashMap<String, String> params = new HashMap<>();
                params.put("idRuta", String.valueOf(rutas.getId()));
                connection = apiService.ServiceSF(params, urlComplement, method, style);

                try {

                    responseCode = connection.getResponseCode();

                    if (responseCode == 404) {
                        inputStream = new BufferedInputStream(connection.getErrorStream());
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        builderResult = new StringBuilder();

                        while ((line = bufferedReader.readLine()) != null) {
                            builderResult.append(line);
                        }

                        try {
                            jsonObject = new JSONObject(builderResult.toString());

                            if (jsonObject != null){
                                listViewPVRutas.setVisibility(View.INVISIBLE);
                                txtMensajePV.setText(jsonObject.getString(jsonMsj));
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

                            if (jsonObject != null) {
                                JSONArray jsonArray = jsonObject.getJSONArray(jsonResponse);
                                System.out.println(jsonArray);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonPVRutas = jsonArray.getJSONObject(i);
                                    rutas.setId(Integer.parseInt(jsonPVRutas.getString("IDRuta")));
                                    rutas.setNombre(jsonPVRutas.getString("Ruta"));
                                    pVentasModel.setId(Integer.parseInt(jsonPVRutas.getString("IDPUnto")));
                                    pVentasModel.setNombre(jsonPVRutas.getString("Punto"));
                                    pVentasModel.setFoto(jsonPVRutas.getString("foto"));
                                    HashMap<String, String> mapPVRutas = new HashMap<>();
                                    mapPVRutas.put("idruta", String.valueOf(rutas.getId()));
                                    mapPVRutas.put("ruta", rutas.getNombre());
                                    mapPVRutas.put("idpunto", String.valueOf(pVentasModel.getId()));
                                    mapPVRutas.put("punto", pVentasModel.getNombre());
                                    mapPVRutas.put("foto", pVentasModel.getFoto());
                                    listPVRutas.add(mapPVRutas);
                                }
                                return jsonObject;
                            }

                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
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

            if (progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            PVentasRutasAdapter pVentasRutasAdapter = new PVentasRutasAdapter(PuntosVentasActivity.this, listPVRutas, R.layout.item_pv_list, new String[]{}, new int[]{});
            listViewPVRutas.setAdapter(pVentasRutasAdapter);
        }
    }
}