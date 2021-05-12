package com.example.ebtapp.service;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class APIService {

    private URL url;
    private String line;
    private File fileImage;
    private String paramsString;
    private JSONObject jsonObject;
    private String charset = "UTF-8";
    private StringBuilder sbparams;
    private StringBuilder builderResult;
    private HttpURLConnection connection;
    private DataOutputStream dataOutputStream;
    private InputStream inputStream;
    private BufferedReader bufferedReader;
    private String urlBase = "http://192.168.1.69/centinelaApi/api";

    //MÉTODO PARA INICIAR SESIÓN
    public HttpURLConnection login(HashMap<String, String> parametros){
        sbparams = new StringBuilder();
        int i = 0;

        for (String key : parametros.keySet()){
            try{
                if (i != 0){
                    sbparams.append("&");
                }
                sbparams.append(key).append("=").append(URLEncoder.encode(parametros.get(key), "UTF-8"));
            } catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
            i++;
        }

        try {
            urlBase = urlBase + "/login/cliente";
            url = new URL(urlBase);
            //String boundary = UUID.randomUUID().toString();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setReadTimeout(100000);
            connection.setConnectTimeout(150000);
            connection.connect();
            paramsString = sbparams.toString();
            dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(paramsString);
            dataOutputStream.flush();
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    // MÉTODO QUE OBTIENE LA LISTA DE SENSORES
    public JSONObject getListaSensores() throws Exception {
        url = new URL(urlBase + "lista/general-sensores");

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setConnectTimeout(150000);
            connection.connect();

            inputStream = new BufferedInputStream(connection.getInputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            builderResult = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null){
                builderResult.append(line);
            }

            connection.disconnect();

            jsonObject = new JSONObject(builderResult.toString());
        } catch (IOException ex){
            ex.printStackTrace();
        }

        return jsonObject;
    }
}
