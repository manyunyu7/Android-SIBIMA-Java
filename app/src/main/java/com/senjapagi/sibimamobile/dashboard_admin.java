package com.senjapagi.sibimamobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.senjapagi.sibimamobile.ActivityMentee.mentee_buku_panduan;
import com.senjapagi.sibimamobile.Admin.admin_manage_contact;
import com.senjapagi.sibimamobile.Admin.admin_scanQR;
import com.senjapagi.sibimamobile.Admin.admin_scanQR_talaqqi;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;

public class dashboard_admin extends AppCompatActivity {
    TextView shubuh,dhuhur,ashar,maghrib,isya,buka,masehi,lunar;
    ShimmerFrameLayout shimmerJadwal;
    String dn;
    SweetAlertDialog pDialog,changeDialog;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);
        shimmerJadwal= (ShimmerFrameLayout) findViewById(R.id.shimmer_view_jadwal);
        shubuh=findViewById(R.id.txtTimeSubuh);
        dhuhur=findViewById(R.id.txtTimeDhuhur);
        ashar=findViewById(R.id.txtTimeAshar);
        maghrib=findViewById(R.id.txtTimeMagrib);
        isya=findViewById(R.id.txtTimeIsya);
        findViewById(R.id.containerMenuAdmin).setVisibility(View.INVISIBLE);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");//date format for APIut

        Date date = new Date();
        dn=formatter.format(date);
        dialogCheckPermission();

        findViewById(R.id.btn_presensi_general).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(dashboard_admin.this, admin_scanQR.class);
                startActivity(a);
            }
        });
        findViewById(R.id.btn_presensi_talaqqi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(dashboard_admin.this, admin_scanQR_talaqqi.class);
                startActivity(a);
            }
        });
        findViewById(R.id.btn_admin_helpdesk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(dashboard_admin.this, admin_manage_contact.class);
                startActivity(a);
            }
        });
        findViewById(R.id.btn_panduan_admin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(dashboard_admin.this, mentee_buku_panduan.class);
                startActivity(a);
            }
        });

        getJadwal();
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText("Login Berhasil");
        pDialog.setContentText("Anda Login Sebagai Admin");
        pDialog.hide();
        pDialog.setCancelable(false);
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                pDialog.dismissWithAnimation();
                findViewById(R.id.containerMenuAdmin).setVisibility(View.VISIBLE);
                findViewById(R.id.containerMenuAdmin).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.item_animation_falldown));
                findViewById(R.id.temp_loading).setVisibility(View.GONE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 1000);
            }
        });


        findViewById(R.id.containerMenuAdmin).setVisibility(View.VISIBLE);
        findViewById(R.id.containerMenuAdmin).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.item_animation_falldown));
        pDialog.show();

        findViewById(R.id.btn_website_sibima).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(dashboard_admin.this,website_sibima.class);
                startActivity(a);
            }
        });

    }

    @Override
    public void onBackPressed() {
       logoutConfirm();
    }

    private void dialogCheckPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(dashboard_admin.this, new String[]{android.Manifest.permission.CAMERA}, 50);
            Toast.makeText(this, "Aktifkan Permission Camera", Toast.LENGTH_SHORT).show();
        } else {
            //Do Nothing
        }
    }


    public void getJadwal(){
        shimmerJadwal.startShimmer();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                . writeTimeout(12, TimeUnit.SECONDS)
                .build();

        String URL_API_SHOLAT ="https://api.banghasan.com/sholat/format/json/jadwal/kota/679/tanggal/"+dn;
        System.out.println(dn);
        AndroidNetworking.post(URL_API_SHOLAT)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            shimmerJadwal.stopShimmer();
                            shimmerJadwal.hideShimmer();
                            String result = response.optJSONObject("query").getString("tanggal");
                            shubuh.setText(response.optJSONObject("jadwal").getJSONObject("data").getString("subuh"));
                            dhuhur.setText(response.optJSONObject("jadwal").getJSONObject("data").getString("dzuhur"));
                            ashar.setText(response.optJSONObject("jadwal").getJSONObject("data").getString("ashar"));
                            maghrib.setText(response.optJSONObject("jadwal").getJSONObject("data").getString("maghrib"));
                            isya.setText(response.optJSONObject("jadwal").getJSONObject("data").getString("isya"));

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(dashboard_admin.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            SnackBarInternet();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        shimmerJadwal.stopShimmer();
                        anError.printStackTrace();
                        SnackBarInternet();
                        Toast.makeText(dashboard_admin.this, "Gagal Terhubung Dengan Server", Toast.LENGTH_SHORT).show();

                    }
                });
    }
    public void SnackBarInternet(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Gagal Terhubung Dengan Server");
        builder1.setCancelable(true);

        builder1.setNegativeButton(
                "Refresh",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getJadwal();
                        dialog.cancel();
                    }
                });

        builder1.setPositiveButton(
                "Keluar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAndRemoveTask();
                            System.exit(1);
                        }else{
                            finishAffinity();
                            System.exit(1);
                        }
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void logoutConfirm(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Anda Yakin Ingin Logout ???");
        builder1.setCancelable(true);

        builder1.setNegativeButton(
                "Tidak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.setPositiveButton(
                "Ya",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent a = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(a);
                        finish();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


}