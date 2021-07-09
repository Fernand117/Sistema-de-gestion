package com.example.ebtapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ebtapp.R;
import com.example.ebtapp.database.DataBaseBack;
import com.example.ebtapp.model.DireccionesModel;
import com.example.ebtapp.model.PVentasModel;
import com.example.ebtapp.model.Rutas;
import com.example.ebtapp.model.TirosModel;
import com.example.ebtapp.model.UsuariosModel;
import com.example.ebtapp.service.APIService;
import com.google.android.material.textfield.TextInputEditText;
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
import java.util.HashMap;

public class TirosActivity extends AppCompatActivity {

    private Rutas rutas;
    private TirosModel tirosModel;
    private PVentasModel pVentasModel;
    private UsuariosModel usuariosModel;
    private DireccionesModel direccionesModel;

    private TextView txtNombreRuta;
    private TextView txtNombrePVenta;
    private TextView txtNombreVendedor;

    private TextView txtMunicipio;
    private TextView txtLocalidad;
    private TextView txtDireccion;

    private TextView txtTiro;
    private TextView txtTotal;
    private TextView txtSalida;
    private TextView txtFechaTiro;
    private TextView txtDevolucion;
    private TextInputEditText txtVenta;

    private Button btnGuardar;
    private ImageView imgPunto;

    private String line;
    private int responseCode;
    private JSONObject jsonObject;
    private InputStream inputStream;
    private DataBaseBack dataBaseBack;
    private StringBuilder builderResult;
    private HttpURLConnection connection;
    private BufferedReader bufferedReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiros);

        rutas = new Rutas();
        tirosModel = new TirosModel();
        pVentasModel = new PVentasModel();
        usuariosModel = new UsuariosModel();
        direccionesModel = new DireccionesModel();

        pVentasModel.setId(Integer.parseInt(getIntent().getStringExtra("idPVenta")));
        pVentasModel.setNombre(getIntent().getStringExtra("punto"));
        getSupportActionBar().setTitle(pVentasModel.getNombre() + " - Detalles del tiro");

        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        imgPunto = (ImageView) findViewById(R.id.fagImage);

        txtNombreRuta = (TextView) findViewById(R.id.txtNombreRuta);
        txtNombrePVenta = (TextView) findViewById(R.id.txtNombrePVenta);
        txtNombreVendedor = (TextView) findViewById(R.id.txtVendedor);

        txtMunicipio = (TextView) findViewById(R.id.txtMunicipio);
        txtLocalidad = (TextView) findViewById(R.id.txtLocalidad);
        txtDireccion = (TextView) findViewById(R.id.txtDireccion);

        txtTiro = (TextView) findViewById(R.id.txtIdTiro);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtSalida = (TextView) findViewById(R.id.txtSalida);
        txtFechaTiro = (TextView) findViewById(R.id.txtFechaTiro);
        txtVenta = (TextInputEditText) findViewById(R.id.txtVenta);
        txtDevolucion = (TextView) findViewById(R.id.txtDevolucion);

        new detallesTiro().execute();
    }

    private class detallesTiro extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private APIService apiService = new APIService();
        private static final String method = "POST";
        private static final String style = "normal";
        private static final String jsonMsj = "Detalles";
        private static final String urlComplement = "/detalles/tiros";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(TirosActivity.this);
            progressDialog.setMessage("Cargando detalles del punto de venta");
            progressDialog.setTitle("Espere por favor");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("idPVenta", String.valueOf(pVentasModel.getId()));

                connection = apiService.ServiceSF(params, urlComplement, method, style);

                try {
                    responseCode = connection.getResponseCode();

                    if (responseCode == 404) {
                        inputStream = new BufferedInputStream(connection.getErrorStream());
                        inputStream = new BufferedInputStream(connection.getErrorStream());
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
                                JSONArray jsonArray = jsonObject.getJSONArray(jsonMsj);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectDetalles = jsonArray.getJSONObject(i);

                                    pVentasModel.setId(Integer.parseInt(jsonObjectDetalles.getString("IDPunto")));
                                    pVentasModel.setNombre(jsonObjectDetalles.getString("Punto"));
                                    pVentasModel.setFoto(jsonObjectDetalles.getString("foto"));

                                    rutas.setId(Integer.parseInt(jsonObjectDetalles.getString("IDRuta")));
                                    rutas.setNombre(jsonObjectDetalles.getString("Ruta"));

                                    direccionesModel.setId(Integer.parseInt(jsonObjectDetalles.getString("IDDireccion")));
                                    direccionesModel.setDireccion(jsonObjectDetalles.getString("direccion"));
                                    direccionesModel.setLocalidad(jsonObjectDetalles.getString("localidad"));
                                    direccionesModel.setMunicipio(jsonObjectDetalles.getString("municipio"));

                                    usuariosModel.setId(Integer.parseInt(jsonObjectDetalles.getString("IDUsuario")));
                                    usuariosModel.setUsuario(jsonObjectDetalles.getString("usuario"));
                                    usuariosModel.setNombre(jsonObjectDetalles.getString("nombre"));
                                    usuariosModel.setPaterno(jsonObjectDetalles.getString("paterno"));

                                    tirosModel.setId(Integer.parseInt(jsonObjectDetalles.getString("IDTiro")));
                                    tirosModel.setFecha(jsonObjectDetalles.getString("fecha"));
                                    tirosModel.setSalida(Integer.parseInt(jsonObjectDetalles.getString("salida")));
                                    tirosModel.setDevolucion(Integer.parseInt(jsonObjectDetalles.getString("devolucion")));
                                    tirosModel.setVenta(Integer.parseInt(jsonObjectDetalles.getString("venta")));
                                    tirosModel.setTotal(Integer.parseInt(jsonObjectDetalles.getString("total")));
                                    tirosModel.setIdPVenta(pVentasModel.getId());
                                    tirosModel.setIdUsuario(usuariosModel.getId());
                                }
                            }
                        } catch (JSONException jsonException){
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

            if (responseCode == 200) {
                Picasso.with(TirosActivity.this).load(pVentasModel.getFoto()).into(imgPunto);

                txtNombrePVenta.setText(pVentasModel.getNombre());
                txtNombreRuta.setText(rutas.getNombre());
                txtNombreVendedor.setText(usuariosModel.getNombre());

                txtDireccion.setText(direccionesModel.getDireccion());
                txtLocalidad.setText(direccionesModel.getLocalidad());
                txtMunicipio.setText(direccionesModel.getMunicipio());

                txtTiro.setText(String.valueOf(tirosModel.getId()));
                txtTotal.setText(String.valueOf(tirosModel.getTotal()));
                txtSalida.setText(String.valueOf(tirosModel.getSalida()));
                txtFechaTiro.setText(String.valueOf(tirosModel.getFecha()));
                txtDevolucion.setText(String.valueOf(tirosModel.getDevolucion()));
            }
        }
    }
}
