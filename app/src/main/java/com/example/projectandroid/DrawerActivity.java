package com.example.projectandroid;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.projectandroid.databinding.ActivityDrawerBinding;
import com.google.android.material.navigation.NavigationView;

import model.fragment.CategoryFragment;

public class DrawerActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDrawerBinding binding;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarDrawer.toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUserPhone = (TextView) headerView.findViewById(R.id.myPhone);
        navUserPhone.setText(viewPreferences("profile_phone"));
        TextView navUsername = (TextView) headerView.findViewById(R.id.myName);
        SQLiteDatabase db = openOrCreateDatabase("projectAndroid", MODE_PRIVATE, null);
        String sql = "select * from profile where " +
                " phone = '" + viewPreferences("profile_phone") + "'" +
                " LIMIT 1";
        Cursor rs = db.rawQuery(sql, null);
        rs.moveToFirst();
        navUsername.setText(rs.getString(1));
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_myProfile, R.id.nav_category)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                switch (destination.getId()) {
                    case R.id.nav_home:
                        binding.appBarDrawer.fab.setVisibility(View.VISIBLE);
                        binding.appBarDrawer.fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(DrawerActivity.this, CreateHistoryActivity.class));
                            }
                        });
                        break;
                    case R.id.nav_category:
                        binding.appBarDrawer.fab.setVisibility(View.VISIBLE);
                        binding.appBarDrawer.fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final EditText input = new EditText(DrawerActivity.this);
                                input.setHint("Tên danh mục");
                                input.setPadding(100, 50,100,50);
                                input.setInputType(InputType.TYPE_CLASS_TEXT);
                                AlertDialog.Builder alert = new AlertDialog.Builder(DrawerActivity.this);
                                alert.setView(input);
                                alert.setMessage("Vui lòng nhập danh mục ");
                                alert.setNegativeButton("Trở lại", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                alert.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (input.getText().toString().isEmpty()) {
                                            Toast.makeText(DrawerActivity.this, "Vui lòng điền tên danh mục", Toast.LENGTH_SHORT).show();
                                        } else {
                                            createCategory(input.getText().toString());
                                           NavGraph gr =  navController.getGraph();
                                           gr.setStartDestination(R.id.nav_category);
                                            navController.setGraph(gr);
                                        }
                                    }
                                });
                                alert.show();
                            }
                        });
                        break;
                    default:
                        binding.appBarDrawer.fab.setVisibility(View.GONE);
                        break;

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_drawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(DrawerActivity.this, ExecuteSQLActivity.class));
                return true;
            case R.id.logOut:
                AlertDialog.Builder alert = new AlertDialog.Builder(DrawerActivity.this);
                alert.setTitle("Thông báo! ");
                alert.setMessage("Bạn có muốn đăng xuất ? ");
                alert.setNegativeButton("Ở lại", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        logOut();
                        startActivity(new Intent(DrawerActivity.this, LoginActivity.class));
                    }
                });
                alert.show();
                return true;
        }
        return false;
    }

    private Void createCategory(String categoryName) {
        SQLiteDatabase db = openOrCreateDatabase("projectAndroid", MODE_PRIVATE, null);
        String executeQuery = "INSERT INTO category (name)  \n" +
                "VALUES ('" + categoryName + "')";
        db.execSQL(executeQuery);
        return null;
    }

    private String viewPreferences(String key) {
        SharedPreferences viewPreferences = getSharedPreferences("ProjectAndroidSetting", MODE_PRIVATE);
        return viewPreferences.getString(key, null);
    }

    private Void logOut() {
        SharedPreferences viewPreferences = getSharedPreferences("ProjectAndroidSetting", MODE_PRIVATE);
        viewPreferences.edit().remove("profile_phone").commit();
        viewPreferences.edit().remove("profile_pass").commit();
        return null;
    }

}