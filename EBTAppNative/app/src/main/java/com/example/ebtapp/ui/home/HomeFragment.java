package com.example.ebtapp.ui.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ebtapp.R;
import com.example.ebtapp.databinding.FragmentHomeBinding;
import com.example.ebtapp.model.PVentasModel;
import com.example.ebtapp.model.Rutas;
import com.example.ebtapp.model.UsuariosModel;
import com.example.ebtapp.service.APIService;
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
import java.util.HashMap;

public class HomeFragment extends Fragment {

    private Rutas rutas;
    private APIService apiService;
    private PVentasModel pVentasModel;
    private ProgressDialog progressDialog;

    private String line;
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

        rutas = new Rutas();
        apiService = new APIService();
        pVentasModel = new PVentasModel();
        progressDialog = new ProgressDialog(getContext());

        new getRutasAsync().execute();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class getRutasAsync extends AsyncTask<String, String, JSONObject> {

        private static final String method = "GET";
        private static final String style = "nomral";
        private static final String jsonError = "Mensaje";
        private static final String jsonResponse = "Rutas";
        private static final String urlComplement = "/lista/rutas";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Cargando lista.");
            progressDialog.setMessage("Espere por favor.");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                params = new HashMap<>();
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
                        if (jsonObject != null) {
                            System.out.println(jsonObject.getString(jsonResponse));
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
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                        if (jsonObject != null) {
                            JSONArray jsonArray = jsonObject.getJSONArray(jsonResponse);
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
        }
    }
}