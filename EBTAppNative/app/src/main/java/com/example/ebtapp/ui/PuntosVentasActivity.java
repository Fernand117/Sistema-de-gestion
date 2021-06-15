package com.example.ebtapp.ui;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ebtapp.MainActivity;
import com.example.ebtapp.R;
import com.example.ebtapp.adapters.PVentasRutasAdapter;
import com.example.ebtapp.database.DataBaseBack;
import com.example.ebtapp.model.PVentasModel;
import com.example.ebtapp.model.Rutas;
import com.example.ebtapp.model.UsuariosModel;
import com.example.ebtapp.service.APIService;
import com.example.ebtapp.ui.gallery.GalleryFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private String mensaje;
    private TextView txtMensajePV;
    private ListView listViewPVRutas;
    private PVentasModel pVentasModel;
    private FloatingActionButton fabNuevoPV;
    private FloatingActionButton fabEditarPV;
    private FloatingActionButton fabEliminarPV;
    private FloatingActionButton fabCancelarPV;
    private ArrayList<HashMap<String, String>> listPVRutas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntos_ventas);

        rutas = new Rutas();
        pVentasModel = new PVentasModel();
        listPVRutas = new ArrayList<HashMap<String, String>>();

        rutas.setNombre(getIntent().getStringExtra("punto"));
        rutas.setId(Integer.parseInt(getIntent().getStringExtra("idRuta")));
        getSupportActionBar().setTitle(rutas.getNombre());

        txtMensajePV = (TextView) findViewById(R.id.txtMensajePV);
        listViewPVRutas = (ListView) findViewById(R.id.listViewPV);

        fabNuevoPV = (FloatingActionButton) findViewById(R.id.fabNuevoPV);
        fabEditarPV = (FloatingActionButton) findViewById(R.id.fabEditarPV);
        fabCancelarPV = (FloatingActionButton) findViewById(R.id.fabCancelarPV);
        fabEliminarPV = (FloatingActionButton) findViewById(R.id.fabEliminarPV);

        new listaPuntosVentasAsync().execute();

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

        listViewPVRutas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String punto = ((TextView) view.findViewById(R.id.txtNPV)).getText().toString();
                String idPv = ((TextView) view.findViewById(R.id.txtIdPV)).getText().toString();

                pVentasModel.setId(Integer.parseInt(idPv));
                pVentasModel.setNombre(punto);

                fabCancelarPV.setVisibility(View.VISIBLE);
                fabEditarPV.setVisibility(View.VISIBLE);
                fabEliminarPV.setVisibility(View.VISIBLE);
                fabNuevoPV.setVisibility(View.GONE);
                return false;
            }
        });

        fabNuevoPV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fabEditarPV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fabEliminarPV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(PuntosVentasActivity.this);
                dialog.setTitle("Importante");
                dialog.setMessage("¿Desea eliminar este punto de venta " + pVentasModel.getNombre() + pVentasModel.getId() + "?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fabButtons();
                        new eliminarPuntoVentaAsync().execute();
                    }
                });
                dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fabButtons();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        fabCancelarPV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabButtons();
            }
        });
    }

    private void fabButtons() {
        fabEditarPV.setVisibility(View.GONE);
        fabEliminarPV.setVisibility(View.GONE);
        fabCancelarPV.setVisibility(View.GONE);
        fabNuevoPV.setVisibility(View.VISIBLE);
    }

    private class listaPuntosVentasAsync extends AsyncTask<String, String, JSONObject> {

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
            pVentasRutasAdapter.notifyDataSetChanged();
            listViewPVRutas.invalidateViews();
            listViewPVRutas.refreshDrawableState();
            listViewPVRutas.setAdapter(pVentasRutasAdapter);
        }
    }

    private class eliminarPuntoVentaAsync extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private APIService apiService = new APIService();
        private static final String urlComplement = "/eliminar/puntos-ventas";
        private static final String method = "POST";
        private static final String style = "normal";
        private static final String jsonMsj = "Mensaje";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PuntosVentasActivity.this);
            progressDialog.setTitle("Espere por favor");
            progressDialog.setMessage("¿Desea eliminar el punto de venta " + pVentasModel.getNombre() + " ?");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(pVentasModel.getId()));
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
                            if (jsonObject != null) {
                                mensaje = jsonObject.getString(jsonMsj);
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
                                mensaje = jsonObject.getString(jsonMsj);
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
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                Toast.makeText(PuntosVentasActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                listPVRutas.clear();
                fabButtons();
                new listaPuntosVentasAsync().execute();
            }
        }
    }
}