package com.example.ebtapp.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ebtapp.MainActivity;
import com.example.ebtapp.R;
import com.example.ebtapp.model.UsuariosModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.ebtapp.service.usuariosService;
import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private String line;
    private int responseCode;
    private JSONObject jsonObject;
    private InputStream inputStream;
    private StringBuilder builderResult;
    private HttpURLConnection connection;
    private BufferedReader bufferedReader;

    private Button btnLogin;
    private TextInputEditText txtUsuario;
    private TextInputEditText txtPassword;
    private TextView txtMensaje;
    private Intent homeIntent;
    private UsuariosModel usuariosModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        usuariosModel = new UsuariosModel();
        homeIntent = new Intent(LoginActivity.this, MainActivity.class);
        txtUsuario = (TextInputEditText) findViewById(R.id.txtUsuario);
        txtPassword = (TextInputEditText) findViewById(R.id.txtPassword);
        txtMensaje = (TextView) findViewById(R.id.txtMensaje);
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
                    //mensaje(usuariosModel.getUsuario() + " " + usuariosModel.getClave());
                    txtMensaje.setTextColor(Color.GREEN);
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

        private ProgressDialog progressDialog;
        usuariosService usuariosService = new usuariosService();

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
                connection = usuariosService.login(params);
                System.out.println("*******MENSAJE RESPUESTA SERVIDOR: " + connection.getResponseMessage());
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
                            mensaje(jsonObject.getString("Mensaje"));
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
                            txtMensaje.setTextColor(Color.GREEN);
                            JSONArray jsonArray = jsonObject.optJSONArray("Usuarios");
                            /*SharedPreferences sharedPreferences = getSharedPreferences("Usuario", LoginActivity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();*/
                            for (int i= 0; i < jsonArray.length(); i++){
                                JSONObject jsonObjectUs = jsonArray.getJSONObject(i);
                                /*editor.putString("Nombre", nombreCompleto);
                                editor.putString("Usuario", jsonObjectUs.getString("usuario"));
                                editor.putString("Foto", jsonObjectUs.getString("foto_perfil"));*/
                                usuariosModel.setUsuario(jsonObjectUs.getString("usuario"));
                                usuariosModel.fullName(jsonObjectUs.getString("nombre"), jsonObjectUs.getString("paterno"), jsonObjectUs.getString("materno"));
                                usuariosModel.setUrl_foto(jsonObjectUs.getString("foto_perfil"));
                                mensaje(usuariosModel.getFullName());
                            }
                            //editor.commit();
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
                homeIntent.putExtra("usuario", usuariosModel.getUsuario());
                homeIntent.putExtra("nombre", usuariosModel.getFullName());
                homeIntent.putExtra("foto", usuariosModel.getUrl_foto());
                startActivity(homeIntent);
            }
        }
    }
}