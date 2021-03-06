package com.example.ebtapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ebtapp.database.DataBaseBack;
import com.example.ebtapp.model.UsuariosModel;
import com.example.ebtapp.ui.PuntosVentasActivity;
import com.example.ebtapp.ui.login.LoginActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ebtapp.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView txtNUsuario;
    private TextView txtNFull;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private UsuariosModel usuariosModel;
    private DataBaseBack dataBaseBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBaseBack = new DataBaseBack(this);
        usuariosModel = new UsuariosModel();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        imageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageViewProfile);
        txtNUsuario = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtNUsuario);
        txtNFull = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtNFull);

        try {
            SQLiteDatabase database = dataBaseBack.getWritableDatabase();
            if (database != null){
                //String[] args = new String[] {usuariosModel.getUsuario()};
                Cursor cursor = database.rawQuery("SELECT * FROM usuario", null);
                if (cursor != null && cursor.getCount() > 0){
                    cursor.moveToFirst();
                    do {
                        usuariosModel.setNombre(cursor.getString(cursor.getColumnIndex("fullname")));
                        usuariosModel.setUsuario(cursor.getString(cursor.getColumnIndex("usuario")));
                        usuariosModel.setUrl_foto(cursor.getString(cursor.getColumnIndex("foto")));
                    } while (cursor.moveToNext());
                }
                cursor.close();
                database.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        Picasso.with(getApplicationContext()).load(usuariosModel.getUrl_foto()).into(imageView);
        txtNUsuario.setText(usuariosModel.getUsuario());
        txtNFull.setText(usuariosModel.getNombre());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnSalir:
                cerrarSesion();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cerrarSesion() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Importante");
        dialog.setMessage("??Desea cerrar sesi??n?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast toast = Toast.makeText(getApplicationContext(), "Cerrando sesi??n", Toast.LENGTH_SHORT);
                toast.show();
                try {
                    SQLiteDatabase database = dataBaseBack.getWritableDatabase();
                    if (database != null){
                        database.acquireReference();
                        database.delete("usuario",null,null);
                        database.close();
                    }
                    finish();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}