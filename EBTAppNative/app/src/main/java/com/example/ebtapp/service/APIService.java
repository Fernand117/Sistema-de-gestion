package com.example.ebtapp.service;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.UUID;

public class APIService {

    private URL url;
    private String paramsString;
    private String charset = "UTF-8";
    private StringBuilder sbparams;
    private HttpURLConnection connection;
    private DataOutputStream dataOutputStream;
    //private String urlBase = "http://192.168.1.67/sistemaAPI/api";
    private String urlBase = "https://ebtapi.herokuapp.com/api";

    public HttpURLConnection ServiceSF(HashMap<String, String> parametros, String urlComplement, String method, String style){
        urlBase = urlBase + urlComplement;
        sbparams = new StringBuilder();
        int i = 0;
        for (String key : parametros.keySet()){
            try{
                if (i != 0){
                    sbparams.append("&");
                }
                sbparams.append(key).append("=").append(URLEncoder.encode(parametros.get(key), charset));
            } catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
            i++;
        }
        if (method.equals("POST")){
            try {
                url = new URL(urlBase);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                if (style.equals("normal")){
                    connection.setRequestProperty("Accept-Charset", charset);
                    connection.setReadTimeout(100000);
                    connection.setConnectTimeout(150000);
                    connection.connect();
                    paramsString = sbparams.toString();
                    dataOutputStream = new DataOutputStream(connection.getOutputStream());
                    dataOutputStream.writeBytes(paramsString);
                    dataOutputStream.flush();
                    dataOutputStream.close();
                } else if (style.equals("formData")){
                    return connection;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (method.equals("GET")){
            if (sbparams.length() != 0) {
                urlBase += "?" + sbparams.toString();
            }
            try {
                url = new URL(urlBase);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.setRequestProperty("Accept-Charset", charset);
                connection.setConnectTimeout(150000);
                connection.connect();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return connection;
    }
}