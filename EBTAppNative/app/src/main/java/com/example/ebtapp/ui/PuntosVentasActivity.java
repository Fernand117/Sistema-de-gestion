package com.example.ebtapp.ui;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ebtapp.MainActivity;
import com.example.ebtapp.R;
import com.example.ebtapp.adapters.PVentasRutasAdapter;
import com.example.ebtapp.database.DataBaseBack;
import com.example.ebtapp.model.DireccionesModel;
import com.example.ebtapp.model.PVentasModel;
import com.example.ebtapp.model.Rutas;
import com.example.ebtapp.model.UsuariosModel;
import com.example.ebtapp.service.APIService;
import com.example.ebtapp.ui.gallery.GalleryFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PuntosVentasActivity extends AppCompatActivity {

    private String line;
    private Dialog dialog;
    private int responseCode;
    private JSONObject jsonObject;
    private InputStream inputStream;
    private String charset = "ISO-8859-1";
    private StringBuilder builderResult;
    private HttpURLConnection connection;
    private BufferedReader bufferedReader;
    private DataOutputStream dataOutputStream;

    private Rutas rutas;
    private String mensaje;
    private TextView txtMensajePV;
    private ListView listViewPVRutas;
    private PVentasModel pVentasModel;
    private FloatingActionButton fabNuevoPV;
    private FloatingActionButton fabEditarPV;
    private DireccionesModel direccionesModel;
    private FloatingActionButton fabEliminarPV;
    private FloatingActionButton fabCancelarPV;
    private PVentasRutasAdapter pVentasRutasAdapter;
    private ArrayList<HashMap<String, String>> listPVRutas;

    private Button btnGuardarPV;
    private Button btnCancelarPV;
    private ImageView imgPuntoVenta;
    private TextInputEditText txtNombrePV;
    private TextInputEditText txtMunicipio;
    private TextInputEditText txtDireccion;
    private TextInputEditText txtLocalidad;

    private Uri imgUri;
    private File imgFile;
    private Bitmap imgBitmap;
    private ContentValues values;
    private String result, imgUrl;
    private static final int PICTURE_RESULT = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntos_ventas);

        rutas = new Rutas();
        pVentasModel = new PVentasModel();
        direccionesModel = new DireccionesModel();
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

        /*listViewPVRutas.setOnTouchListener(new ListView.OnTouchListener() {
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

        listViewPVRutas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int viewID = view.getId();
                switch (viewID) {
                    case R.id.btnVerPVentas:
                        HashMap<String, Object> data = (HashMap<String, Object>)pVentasRutasAdapter.getItem(position);
                        String pventanombre = (String) data.get("punto");
                        Toast.makeText(PuntosVentasActivity.this, pventanombre, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PuntosVentasActivity.this, TirosActivity.class);
                        intent.putExtra("idPVenta", (String) data.get("idpunto"));
                        intent.putExtra("punto", pventanombre);
                        startActivity(intent);
                        break;
                }
            }
        });

        listViewPVRutas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    HashMap<String, Object> param = (HashMap<String, Object>) pVentasRutasAdapter.getItem(position);

                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primary1));
                    String idPunto = (String) param.get("idpunto");
                    pVentasModel.setId((Integer) Integer.parseInt(idPunto));
                    pVentasModel.setNombre((String) param.get("punto"));
                    pVentasModel.setFoto((String) param.get("foto"));
                    direccionesModel.setDireccion((String) param.get("direccion"));
                    direccionesModel.setLocalidad((String) param.get("localidad"));
                    direccionesModel.setMunicipio((String) param.get("municipio"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
                dialog = new Dialog(PuntosVentasActivity.this);
                dialog.setContentView(R.layout.add_puntoventas);

                imgPuntoVenta = (ImageView) dialog.findViewById(R.id.imgPuntoVenta);
                txtDireccion = (TextInputEditText) dialog.findViewById(R.id.txtDireccion);
                txtLocalidad = (TextInputEditText) dialog.findViewById(R.id.txtLocalidad);
                txtMunicipio = (TextInputEditText) dialog.findViewById(R.id.txtMunicipio);
                txtNombrePV = (TextInputEditText) dialog.findViewById(R.id.txtNombrePVenta);

                btnGuardarPV = (Button) dialog.findViewById(R.id.btnSPV);
                btnCancelarPV = (Button) dialog.findViewById(R.id.btnCPV);

                imgPuntoVenta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tomarFoto(v);
                    }
                });

                btnGuardarPV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pVentasModel.setNombre(txtNombrePV.getText().toString());
                        direccionesModel.setDireccion(txtDireccion.getText().toString());
                        direccionesModel.setLocalidad(txtLocalidad.getText().toString());
                        direccionesModel.setMunicipio(txtMunicipio.getText().toString());
                        new crearPuntoVentaAsync().execute();
                        dialog.dismiss();
                    }
                });

                btnCancelarPV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        fabEditarPV.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                dialog = new Dialog(PuntosVentasActivity.this);
                dialog.setContentView(R.layout.edit_puntosventas);

                imgPuntoVenta = (ImageView) dialog.findViewById(R.id.imgPuntoVentaE);
                txtDireccion = (TextInputEditText) dialog.findViewById(R.id.txtDireccionE);
                txtLocalidad = (TextInputEditText) dialog.findViewById(R.id.txtLocalidadE);
                txtMunicipio = (TextInputEditText) dialog.findViewById(R.id.txtMunicipioE);
                txtNombrePV = (TextInputEditText) dialog.findViewById(R.id.txtNombrePVentaE);

                Picasso.with(PuntosVentasActivity.this).load(pVentasModel.getFoto()).resize(300, 180).into(imgPuntoVenta);
                txtNombrePV.setHint(pVentasModel.getNombre());
                txtDireccion.setHint(direccionesModel.getDireccion());
                txtLocalidad.setHint(direccionesModel.getLocalidad());
                txtMunicipio.setHint(direccionesModel.getMunicipio());

                btnGuardarPV = (Button) dialog.findViewById(R.id.btnSPVE);
                btnCancelarPV = (Button) dialog.findViewById(R.id.btnCPVE);

                imgPuntoVenta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tomarFoto(v);
                    }
                });

                btnGuardarPV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pVentasModel.setNombre(txtNombrePV.getText().toString());
                        direccionesModel.setDireccion(txtDireccion.getText().toString());
                        direccionesModel.setLocalidad(txtLocalidad.getText().toString());
                        direccionesModel.setMunicipio(txtMunicipio.getText().toString());
                        new editarPuntoVentaAsync().execute();
                        dialog.dismiss();
                    }
                });

                btnCancelarPV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fabButtons();
                        dialog.dismiss();
                    }
                });

                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_dark));
                dialog.show();
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
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_dark));
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

    private void tomarFoto(View view) {
        values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "foto");
        values.put(MediaStore.Images.Media.DESCRIPTION, "foto en" + System.currentTimeMillis());
        imgUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent foto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        foto.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(foto, PICTURE_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICTURE_RESULT:
                if (requestCode == PICTURE_RESULT) {
                    if (resultCode == PuntosVentasActivity.RESULT_OK) {
                        try {
                            imgBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                            imgPuntoVenta.setImageBitmap(imgBitmap);
                            imgUrl = getRealPathFromUri(imgUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        }
    }

    private String getRealPathFromUri(Uri imagenUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(imagenUri, proj, null, null,null);
        int colum_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(colum_index);
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
                                listViewPVRutas.setVisibility(View.GONE);
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
                                    pVentasModel.setId(Integer.parseInt(jsonPVRutas.optString("IDPunto")));
                                    pVentasModel.setNombre(jsonPVRutas.getString("Punto"));
                                    pVentasModel.setFoto(jsonPVRutas.getString("foto"));
                                    rutas.setId(Integer.parseInt(jsonPVRutas.getString("IDRuta")));
                                    rutas.setNombre(jsonPVRutas.getString("Ruta"));
                                    direccionesModel.setDireccion(jsonPVRutas.getString("direccion"));
                                    direccionesModel.setLocalidad(jsonPVRutas.getString("localidad"));
                                    direccionesModel.setMunicipio(jsonPVRutas.getString("municipio"));
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
                txtMensajePV.setVisibility(View.GONE);
                progressDialog.dismiss();
            }
            pVentasRutasAdapter = new PVentasRutasAdapter(PuntosVentasActivity.this, listPVRutas, R.layout.item_pv_list, new String[]{}, new int[]{});
            pVentasRutasAdapter.notifyDataSetChanged();
            listViewPVRutas.invalidateViews();
            listViewPVRutas.refreshDrawableState();
            listViewPVRutas.setAdapter(pVentasRutasAdapter);
        }
    }

    private class crearPuntoVentaAsync extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private APIService apiService = new APIService();
        private static final String urlComplement = "/registrar/puntos-ventas";
        private static final String method = "POST";
        private static final String style = "formData";
        private static final String jsonMsj = "Mensaje";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PuntosVentasActivity.this);
            progressDialog.setMessage("Guardando nuevo punto de venta");
            progressDialog.setTitle("Espere por favor");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                imgFile = new File(getApplicationContext().getCacheDir(), "foto.jpg");
                imgFile.createNewFile();

                Bitmap fotoBitmap = imgBitmap;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                fotoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] bitmapdata = byteArrayOutputStream.toByteArray();

                FileOutputStream fileOutputStream = new FileOutputStream(imgFile);
                fileOutputStream.write(bitmapdata);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                HashMap<String, String> params = new HashMap<>();
                connection = apiService.ServiceSF(params, urlComplement, method, style);
                try {
                    String boundary = UUID.randomUUID().toString();
                    connection.setRequestProperty("Accept", "");
                    connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
                    connection.setRequestProperty("Accept-Charset", charset);
                    connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    dataOutputStream =  new DataOutputStream(connection.getOutputStream());
                    dataOutputStream.writeBytes("--" + boundary + "\r\n");
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"nombre\"\r\n");
                    dataOutputStream.writeUTF("\r\n" + pVentasModel.getNombre() + "\r\n");
                    dataOutputStream.writeBytes("--" + boundary + "\r\n");

                    dataOutputStream.writeBytes("--" + boundary + "\r\n");
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"idRuta\"\r\n");
                    dataOutputStream.writeUTF("\r\n" + rutas.getId() + "\r\n");
                    dataOutputStream.writeBytes("--" + boundary + "\r\n");

                    dataOutputStream.writeBytes("--" + boundary + "\r\n");
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"direccion\"\r\n");
                    dataOutputStream.writeUTF("\r\n" + direccionesModel.getDireccion() + "\r\n");
                    dataOutputStream.writeBytes("--" + boundary + "\r\n");

                    dataOutputStream.writeBytes("--" + boundary + "\r\n");
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"localidad\"\r\n");
                    dataOutputStream.writeUTF("\r\n" + direccionesModel.getLocalidad() + "\r\n");
                    dataOutputStream.writeBytes("--" + boundary + "\r\n");

                    dataOutputStream.writeBytes("--" + boundary + "\r\n");
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"municipio\"\r\n");
                    dataOutputStream.writeUTF("\r\n" + direccionesModel.getMunicipio() + "\r\n");
                    dataOutputStream.writeBytes("--" + boundary + "\r\n");

                    dataOutputStream.writeBytes("--" + boundary + "\r\n");
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"foto\"; filename=\"" + imgFile + "\"\r\n\r\n");
                    dataOutputStream.write(FileUtils.readFileToByteArray(imgFile));
                    dataOutputStream.writeBytes("\r\n");
                    dataOutputStream.writeBytes("--" + boundary + "--\r\n");
                    dataOutputStream.flush();

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
                                System.out.println("RESPUESTA DEL SERVIDOR ====> " + mensaje);
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
                    } else if (responseCode == 500) {
                        inputStream = new BufferedInputStream(connection.getErrorStream());
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        builderResult = new StringBuilder();

                        while ((line = bufferedReader.readLine()) != null) {
                            builderResult.append(line);
                        }
                        mensaje = builderResult.toString();
                        System.out.println("RESPUESTA DEL SERVIDOR 500 ====> " + mensaje);
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
                if (responseCode == 500 || responseCode == 404) {
                    txtMensajePV.setVisibility(View.VISIBLE);
                    txtMensajePV.setText(mensaje);
                } else {
                    Toast.makeText(PuntosVentasActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                    listPVRutas.clear();
                    fabButtons();
                    new listaPuntosVentasAsync().execute();
                }
            }
        }
    }

    private class editarPuntoVentaAsync extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;
        private APIService apiService = new APIService();
        private static final String urlComplement = "/editar/puntos-ventas";
        private static final String method = "POST";
        private static final String style = "formData";
        private static final String jsonMsj = "Mensaje";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PuntosVentasActivity.this);
            progressDialog.setTitle("Espere por favor");
            progressDialog.setMessage("Actualizando información");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                if (imgBitmap != null) {
                    imgFile = new File(getApplicationContext().getCacheDir(), "foto.jpg");
                    imgFile.createNewFile();

                    Bitmap fotoBitmap = imgBitmap;
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    fotoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] bitmapdata = byteArrayOutputStream.toByteArray();

                    FileOutputStream fileOutputStream = new FileOutputStream(imgFile);
                    fileOutputStream.write(bitmapdata);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                HashMap<String, String> params = new HashMap<>();
                connection = apiService.ServiceSF(params, urlComplement, method, style);
                try {
                    String boundary = UUID.randomUUID().toString();
                    connection.setRequestProperty("Accept", "");
                    connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
                    connection.setRequestProperty("Accept-Charset", charset);
                    connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    dataOutputStream =  new DataOutputStream(connection.getOutputStream());

                    dataOutputStream.writeBytes("--" + boundary + "\r\n");
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"id\"\r\n");
                    dataOutputStream.writeUTF("\r\n" + pVentasModel.getId() + "\r\n");
                    dataOutputStream.writeBytes("--" + boundary + "\r\n");

                    dataOutputStream.writeBytes("--" + boundary + "\r\n");
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"nombre\"\r\n");
                    dataOutputStream.writeUTF("\r\n" + pVentasModel.getNombre() + "\r\n");
                    dataOutputStream.writeBytes("--" + boundary + "\r\n");

                    dataOutputStream.writeBytes("--" + boundary + "\r\n");
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"idRuta\"\r\n");
                    dataOutputStream.writeUTF("\r\n" + rutas.getId() + "\r\n");
                    dataOutputStream.writeBytes("--" + boundary + "\r\n");

                    dataOutputStream.writeBytes("--" + boundary + "\r\n");
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"direccion\"\r\n");
                    dataOutputStream.writeUTF("\r\n" + direccionesModel.getDireccion() + "\r\n");
                    dataOutputStream.writeBytes("--" + boundary + "\r\n");

                    dataOutputStream.writeBytes("--" + boundary + "\r\n");
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"localidad\"\r\n");
                    dataOutputStream.writeUTF("\r\n" + direccionesModel.getLocalidad() + "\r\n");
                    dataOutputStream.writeBytes("--" + boundary + "\r\n");

                    dataOutputStream.writeBytes("--" + boundary + "\r\n");
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"municipio\"\r\n");
                    dataOutputStream.writeUTF("\r\n" + direccionesModel.getMunicipio() + "\r\n");
                    dataOutputStream.writeBytes("--" + boundary + "\r\n");

                    if (imgFile != null) {
                        dataOutputStream.writeBytes("--" + boundary + "\r\n");
                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"foto\"; filename=\"" + imgFile + "\"\r\n\r\n");
                        dataOutputStream.write(FileUtils.readFileToByteArray(imgFile));
                        dataOutputStream.writeBytes("\r\n");
                        dataOutputStream.writeBytes("--" + boundary + "--\r\n");
                    }

                    dataOutputStream.flush();

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
                    } else if (responseCode == 500) {
                        inputStream = new BufferedInputStream(connection.getErrorStream());
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        builderResult = new StringBuilder();

                        while ((line = bufferedReader.readLine()) != null) {
                            builderResult.append(line);
                        }
                        mensaje = builderResult.toString();
                        System.out.println(mensaje);
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