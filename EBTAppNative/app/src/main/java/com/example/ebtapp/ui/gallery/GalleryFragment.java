package com.example.ebtapp.ui.gallery;

import android.app.AlertDialog;
import android.app.AsyncNotedAppOp;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SyncStats;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.loader.content.AsyncTaskLoader;

import com.example.ebtapp.R;
import com.example.ebtapp.adapters.RutasAdapter;
import com.example.ebtapp.database.DataBaseBack;
import com.example.ebtapp.databinding.FragmentGalleryBinding;
import com.example.ebtapp.model.Rutas;
import com.example.ebtapp.service.APIService;
import com.example.ebtapp.ui.PuntosVentasActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    private DataBaseBack dataBaseBack;
    private StringBuilder builderResult;
    private HttpURLConnection connection;
    private BufferedReader bufferedReader;

    private Rutas rutas;
    private RutasAdapter rutasAdapter;
    private TextView txtMensaje;
    private ListView listViewRutas;

    private GalleryViewModel galleryViewModel;

    private FloatingActionButton btnNRuta;
    private FloatingActionButton fabCancelar;
    private FloatingActionButton fabEditarRuta;
    private FloatingActionButton fabEliminarRuta;

    private ArrayList<HashMap<String, String>> listRutas;

    private FragmentGalleryBinding binding;

    private String mensajeRes;
    private Dialog dialog;

    private Button btnGuardarRuta;
    private Button btnCancelarRuta;
    private TextInputEditText txtNruta;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        rutas = new Rutas();
        dataBaseBack = new DataBaseBack(getContext());

        listRutas = new ArrayList<HashMap<String, String>>();
        listViewRutas = (ListView) view.findViewById(R.id.listRutas);

        txtMensaje = (TextView) view.findViewById(R.id.txtMensajelist);

        btnNRuta = (FloatingActionButton) view.findViewById(R.id.btnNRuta);
        fabCancelar = (FloatingActionButton) view.findViewById(R.id.fabCancelar);
        fabEditarRuta = (FloatingActionButton) view.findViewById(R.id.fabEditarRuta);
        fabEliminarRuta = (FloatingActionButton) view.findViewById(R.id.fabEliminarRuta);

        new listRutasAsync().execute();
        /*listViewRutas.setOnTouchListener(new ListView.OnTouchListener() {
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
        });*/
        listViewRutas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int viewId = view.getId();
                switch (viewId) {
                    case R.id.btnVerRuta:
                        HashMap<String, Object> param = (HashMap<String, Object>) rutasAdapter.getItem(position);
                        String idRuta = (String) param.get("id");
                        String ruta = (String) param.get("nombre");
                        Intent pvIntent = new Intent(getContext(), PuntosVentasActivity.class);
                        pvIntent.putExtra("idRuta", idRuta);
                        pvIntent.putExtra("punto", ruta);
                        startActivity(pvIntent);
                        break;
                }
            }
        });

        listViewRutas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String ruta = ((TextView) view.findViewById(R.id.txtNombreRuta)).getText().toString();
                String idRuta = ((TextView) view.findViewById(R.id.txtIDRuta)).getText().toString();

                /*getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.primary1));*/

                rutas.setNombre(ruta);
                rutas.setId(Integer.parseInt(idRuta));

                fabCancelar.setVisibility(View.VISIBLE);
                fabEditarRuta.setVisibility(View.VISIBLE);
                fabEliminarRuta.setVisibility(View.VISIBLE);

                btnNRuta.setVisibility(View.GONE);

                return false;
            }
        });

        btnNRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.add_new_rutes);

                btnGuardarRuta = (Button) dialog.findViewById(R.id.btnSRuta);
                btnCancelarRuta = (Button) dialog.findViewById(R.id.btnCancelarRtua);
                txtNruta = (TextInputEditText) dialog.findViewById(R.id.txtANRuta);

                btnGuardarRuta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rutas.setNombre(txtNruta.getText().toString());
                        new crearRuta().execute();
                        dialog.dismiss();
                    }
                });

                btnCancelarRuta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        fabEditarRuta.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.edit_routes);

                btnGuardarRuta = (Button) dialog.findViewById(R.id.btnSERuta);
                btnCancelarRuta = (Button) dialog.findViewById(R.id.btnCERtua);
                txtNruta = (TextInputEditText) dialog.findViewById(R.id.txtENRuta);
                txtNruta.setHint(rutas.getNombre());

                btnGuardarRuta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rutas.setNombre(txtNruta.getText().toString());
                        new editarRuta().execute();
                        dialog.dismiss();
                    }
                });

                btnCancelarRuta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fabButtons();
                        dialog.dismiss();
                    }
                });
                /*Window window = getActivity().getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.primary_dark));*/
                dialog.show();
            }
        });

        fabEliminarRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("Importante");
                dialog.setMessage("¿Desea eliminar la ruta " + rutas.getNombre() + "?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fabButtons();
                        new eliminarRuta().execute();
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

        fabCancelar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                /*Window window = getActivity().getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.primary_dark));*/
                fabButtons();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void fabButtons() {
        fabEditarRuta.setVisibility(View.GONE);
        fabEliminarRuta.setVisibility(View.GONE);
        fabCancelar.setVisibility(View.GONE);
        btnNRuta.setVisibility(View.VISIBLE);
    }

    private class listRutasAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private APIService apiService = new APIService();
        private static final String method = "POST";
        private static final String style = "normal";
        private static final String jsonMsj = "Mensaje";
        private static final String jsonResponse = "Rutas";
        private static final String urlComplement = "/lista/rutas/usuario";

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
                    System.out.println("CÓDIGO DE RESPUESTA DEL SERVIDOR ==> " + responseCode);
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
                                    mapRutas.put("nombre", rutas.getNombre());
                                    mapRutas.put("id", String.valueOf(rutas.getId()));
                                    listRutas.add(mapRutas);
                                }
                            }
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    } else if (responseCode == 500) {
                        inputStream = new BufferedInputStream(connection.getErrorStream());
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        builderResult = new StringBuilder();

                        while ((line = bufferedReader.readLine()) != null){
                            builderResult.append(line);
                        }

                        System.out.println("RESPUESTA DEL SERVIDOR ==> " + builderResult.toString());
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
            super.onPostExecute(jsonObject);
            if (progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            if (responseCode == 200){
                rutasAdapter = new RutasAdapter(getContext(), listRutas, R.layout.item_list_rutas, new String[]{}, new int[]{});
                rutasAdapter.notifyDataSetChanged();
                listViewRutas.invalidateViews();
                listViewRutas.refreshDrawableState();
                listViewRutas.setAdapter(rutasAdapter);
            }
        }
    }

    private class crearRuta extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        private APIService apiService = new APIService();
        private static final String method = "POST";
        private static final String style = "normal";
        private static final String jsonMsj = "Mensaje";
        private static final String urlComplement = "/registrar/ruta";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Registrando ruta");
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
                                params.put("idUsuario", cursor.getString(cursor.getColumnIndex("idUsuario")));
                            } while (cursor.moveToNext());
                        }
                        cursor.close();
                        database.close();
                    }
                } catch (SQLException e){
                    e.printStackTrace();
                }
                params.put("nombre", rutas.getNombre());
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
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                        if (jsonObject != null){
                            mensajeRes = jsonObject.getString(jsonMsj);
                        }
                    } else if (responseCode == 200) {
                        inputStream = new BufferedInputStream(connection.getInputStream());
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        builderResult = new StringBuilder();
                        while ((line = bufferedReader.readLine()) != null){
                            builderResult.append(line);
                        }
                        try {
                            jsonObject = new JSONObject(builderResult.toString());
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                        if (jsonObject != null){
                            mensajeRes = jsonObject.getString(jsonMsj);
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
            super.onPostExecute(jsonObject);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                dialog.dismiss();
                listRutas.clear();
                new listRutasAsync().execute();
            }
            Toast.makeText(getContext(), mensajeRes ,Toast.LENGTH_SHORT).show();
        }
    }

    private class editarRuta extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private APIService apiService = new APIService();
        private static final String method = "POST";
        private static final String style = "normal";
        private static final String jsonMsj = "Mensaje";
        private static final String urlComplement = "/editar/ruta";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Espere por favor");
            progressDialog.setMessage("Actualizando nombre de la ruta " + rutas.getNombre());
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(rutas.getId()));
                params.put("nombre", rutas.getNombre());
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
                                mensajeRes = jsonObject.getString(jsonMsj);
                            }
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    } else if (responseCode == 500) {
                        mensajeRes = "No puede eliminar esta ruta porque tiene puntos de ventas asignados";
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
                                mensajeRes = jsonObject.getString(jsonMsj);
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
                Toast.makeText(getContext(), mensajeRes, Toast.LENGTH_SHORT).show();
                listRutas.clear();
                fabButtons();
                new listRutasAsync().execute();
            }
        }
    }

    private class eliminarRuta extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private APIService apiService = new APIService();
        private static final String method = "POST";
        private static final String style = "normal";
        private static final String jsonMsj = "Mensaje";
        private static final String urlComplement = "/eliminar/ruta";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Espere por favor");
            progressDialog.setMessage("Eliminando la ruta " + rutas.getNombre());
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(rutas.getId()));
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
                                mensajeRes = jsonObject.getString(jsonMsj);
                            }
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    } else if (responseCode == 500) {
                        mensajeRes = "No puede eliminar esta ruta porque tiene puntos de ventas asignados";
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
                                mensajeRes = jsonObject.getString(jsonMsj);
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
                Toast.makeText(getContext(), mensajeRes, Toast.LENGTH_SHORT).show();
                listRutas.clear();
                fabButtons();
                new listRutasAsync().execute();
            }
        }
    }
}