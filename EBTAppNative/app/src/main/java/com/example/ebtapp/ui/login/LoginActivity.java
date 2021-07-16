package com.example.ebtapp.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ebtapp.MainActivity;
import com.example.ebtapp.R;
import com.example.ebtapp.database.DataBaseBack;
import com.example.ebtapp.model.UsuariosModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.ebtapp.service.APIService;
import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private TextInputEditText txtUsuario;
    private TextInputEditText txtPassword;

    private String line;
    private int responseCode;
    private JSONObject jsonObject;
    private InputStream inputStream;
    private StringBuilder builderResult;
    private HttpURLConnection connection;
    private BufferedReader bufferedReader;

    private DataBaseBack dataBaseBack;
    private TextView txtMensaje;
    private Intent homeIntent;
    private UsuariosModel usuariosModel;
    private SQLiteDatabase database;
    private ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        txtUsuario = (TextInputEditText) findViewById(R.id.txtUsuario);
        txtPassword = (TextInputEditText) findViewById(R.id.txtPassword);
        txtMensaje = (TextView) findViewById(R.id.txtMensaje);

        dataBaseBack = new DataBaseBack(this);
        usuariosModel = new UsuariosModel();

        database = dataBaseBack.getWritableDatabase();
        contentValues = new ContentValues();

        homeIntent = new Intent(LoginActivity.this, MainActivity.class);


        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtUsuario.getText().toString().isEmpty()){
                    mensaje("Por favor ingrese su nombre de usuario");
                    txtUsuario.isFocusable();
                } else if (txtPassword.getText().toString().isEmpty()){
                    mensaje("Por favor ingrese su contraseña");
                    txtPassword.isFocusable();
                } else if (txtUsuario.getText().toString().isEmpty() && txtPassword.getText().toString().isEmpty()){
                    mensaje("Por favor, ingrese los datos solicitados");
                    txtUsuario.isFocusable();
                } else {
                    usuariosModel.setUsuario(txtUsuario.getText().toString());
                    usuariosModel.setClave(txtPassword.getText().toString());
                    txtMensaje.setTextColor(Color.BLACK);
                    new loginAsync().execute();
                }
            }
        });
    }

    private void mensaje(String mensaje){
        txtMensaje.setText(mensaje);
        txtMensaje.setTextColor(Color.RED);
        txtUsuario.setText("");
        txtPassword.setText("");
    }

    private class loginAsync extends AsyncTask<String, String, JSONObject> {

        private static final String urlComplement = "/login/usuarios";
        private static final String method = "POST";
        private static final String style = "normal";
        private static final String jsonMsj = "Mensaje";
        private static final String jsonResponse = "Usuarios";
        private ProgressDialog progressDialog;
        APIService apiService = new APIService();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Iniciando sesión");
            progressDialog.setTitle("Espere por favor.");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("usuario", usuariosModel.getUsuario());
                params.put("clave", usuariosModel.getClave());
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

                        try{
                            jsonObject = new JSONObject(builderResult.toString());
                        } catch (JSONException jsonException){
                            jsonException.printStackTrace();
                        }

                        if (jsonObject != null){
                            mensaje(jsonObject.getString(jsonMsj));
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
                            JSONArray jsonArray = jsonObject.optJSONArray(jsonResponse);
                            System.out.print(jsonArray);
                            System.out.println("JSON RESPUESTA: " + jsonArray);
                            for (int i= 0; i < jsonArray.length(); i++){
                                JSONObject jsonObjectUs = jsonArray.getJSONObject(i);

                                usuariosModel.setId(Integer.parseInt(jsonObjectUs.getString("IDUsuario")));
                                usuariosModel.setUsuario(jsonObjectUs.getString("usuario"));
                                usuariosModel.fullName(jsonObjectUs.getString("nombre"), jsonObjectUs.getString("paterno"), jsonObjectUs.getString("materno"));
                                usuariosModel.setUrl_foto(jsonObjectUs.getString("foto_perfil"));
                                usuariosModel.setIdTipo(Integer.parseInt(jsonObjectUs.getString("IDTipo")));
                            }
                        }
                    } else {
                        mensaje("Ocurrió un error en el servidor.");
                    }

                } catch (IOException e){
                    e.printStackTrace();
                }

                return jsonObject;
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            if (responseCode == 200){
                try {
                    if (database != null){
                        database.acquireReference();
                        String[] args = new String[] {usuariosModel.getUsuario()};
                        Cursor consultaUsuario = database.rawQuery("SELECT * FROM usuario WHERE usuario = ?", args);
                        if (consultaUsuario != null && consultaUsuario.getCount() > 0) {
                            Toast.makeText(LoginActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                        } else {
                            Cursor consutlaUsuarios = database.rawQuery("SELECT * FROM usuario", null);
                            if (consutlaUsuarios != null && consutlaUsuarios.getCount() > 0) {
                                database.delete("usuario",null,null);
                                saveData();
                            } else {
                                saveData();
                            }
                            consutlaUsuarios.close();
                            Toast.makeText(LoginActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                        }
                        consultaUsuario.close();
                        database.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                startActivity(homeIntent);
            }
        }

        private void saveData(){
            contentValues.put("idUsuario", usuariosModel.getId());
            contentValues.put("fullname", usuariosModel.getFullName());
            contentValues.put("usuario", usuariosModel.getUsuario());
            contentValues.put("foto", usuariosModel.getUrl_foto());
            contentValues.put("idTipo", usuariosModel.getIdTipo());
            database.insert("usuario", null, contentValues);
        }
    }
}