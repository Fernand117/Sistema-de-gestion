package com.example.ebtapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ebtapp.model.UsuariosModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ebtapp.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView txtNUsuario;
    private TextView txtNFull;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    UsuariosModel usuariosModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usuariosModel = new UsuariosModel();
        usuariosModel.setUrl_foto(getIntent().getStringExtra("foto"));
        usuariosModel.setNombre(getIntent().getStringExtra("nombre"));
        usuariosModel.setUsuario(getIntent().getStringExtra("usuario"));

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        imageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageViewProfile);
        txtNUsuario = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtNUsuario);
        txtNFull = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtNFull);

        /*SharedPreferences sharedPreferences = getSharedPreferences("Usuario", Activity.MODE_PRIVATE);
        String nombreCompleto = sharedPreferences.getString("Nombre", "");
        String usuario = sharedPreferences.getString("Usuario", "");
        String foto_url = sharedPreferences.getString("Foto", "");*/

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
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}