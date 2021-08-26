package com.senjapagi.sibimamobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.senjapagi.sibimamobile.Constant.ConstantMentee;
import com.senjapagi.sibimamobile.Constant.ConstantMentor;
import com.senjapagi.sibimamobile.Services.CLIENT_API;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton loginButton;
    EditText txtNIM,txtPassword;
    SweetAlertDialog pDialog;
    Button btnMentee,btnMentor;
    String nim ;
    String password;

    EditText etKomplain, etNama, etJurusan, etNim;
    String initNama, initNim, initFakultas;
    ArrayList<String> kontak = new ArrayList<>();
    Spinner daftarKontak;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        getAgenda();
        MediaPlayer.create(MainActivity.this, R.raw.intro).start();

        findViewById(R.id.tv_cred).setVisibility(View.GONE);
        findViewById(R.id.logobm).setVisibility(View.GONE);
        findViewById(R.id.tv_fakultas).setVisibility(View.GONE);
        findViewById(R.id.tv_ead).setVisibility(View.GONE);

        findViewById(R.id.fab_login).setVisibility(View.INVISIBLE);
        findViewById(R.id.rl_input).setVisibility(View.INVISIBLE);

        findViewById(R.id.tv_cred).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_transition_animation_slowly));
        findViewById(R.id.logobm).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.item_animation_falldown_slowly));
        findViewById(R.id.tv_fakultas).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_transition_animation_slowly));
        findViewById(R.id.tv_ead).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_transition_animation_slowly));
        findViewById(R.id.tv_cred).setVisibility(View.VISIBLE);
        findViewById(R.id.logobm).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_fakultas).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_ead).setVisibility(View.VISIBLE);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#00B0FF"));
        pDialog.setTitleText("Loading");
        pDialog.setContentText("Please Wait\nMohon Tunggu");
        pDialog.hide();
        pDialog.setCancelable(false);

        Handler z = new Handler();
        z.postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.fab_login).setVisibility(View.VISIBLE);
                findViewById(R.id.rl_input).setVisibility(View.VISIBLE);
                findViewById(R.id.rl_input).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation_slowly));
                findViewById(R.id.fab_login).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation_slowly));
            }
        },1250);




        btnMentee=findViewById(R.id.btn_login_as_mentee);
        btnMentor=findViewById(R.id.btn_login_as_mentor);

        findViewById(R.id.btn_forgot_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.lyt_chat_pengurus).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.item_animation_falldown));
                findViewById(R.id.lyt_chat_pengurus).setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.btn_kirim_pesan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickWhatsApp();
            }
        });
        findViewById(R.id.btn_batal_kirim_pesan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.lyt_chat_pengurus).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.item_animation_fallup));
                findViewById(R.id.lyt_chat_pengurus).setVisibility(View.GONE);
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nim = txtNIM.getText().toString();
                password = txtPassword.getText().toString();
                if (nim.equals(""))
                    txtNIM.setError("Isi Kolom Ini Terlebih Dahulu");
                else if(password.equals(""))
                    txtPassword.setError("Isi Kolom Ini Terlebih Dahulu");
                //IF PRE REQ ALL GREEN
                else{
                    txtNIM.setEnabled(false);
                    txtPassword.setEnabled(false);
                findViewById(R.id.lyt_choose_login).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.item_animation_falldown));
                findViewById(R.id.lyt_choose_login).setVisibility(View.VISIBLE);
                findViewById(R.id.btn_login_as_mentee).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loginMentee(nim,password);
                    }
                });

                findViewById(R.id.btn_login_as_mentor).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       loginMentor();
                    }
                });

                findViewById(R.id.btn_login_as_admin).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          loginAdmin();
                        }
                    });

                findViewById(R.id.btn_cancel_login).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        findViewById(R.id.lyt_choose_login).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.item_animation_fallup));
                        findViewById(R.id.lyt_choose_login).setVisibility(View.GONE);
                        txtNIM.setEnabled(true);
                        txtPassword.setEnabled(true);
                    }
                });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
       logoutConfirm();
    }

    private void init(){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        loginButton=findViewById(R.id.fab_login);
        txtNIM=findViewById(R.id.et_username);
        txtPassword=findViewById(R.id.et_password);


        daftarKontak = findViewById(R.id.spinner_kontak);
        etKomplain = findViewById(R.id.et_komplain);
        etNim = findViewById(R.id.et_nim);
        etNama = findViewById(R.id.et_nama);
        etJurusan = findViewById(R.id.et_fakultas);

        etKomplain.setVisibility(View.GONE);

        initFakultas = getIntent().getStringExtra(ConstantMentee.jurusan);
        initNim = getIntent().getStringExtra(ConstantMentee.nim);
        initNama = getIntent().getStringExtra(ConstantMentee.nama);

        etJurusan.setText(initFakultas);
        etNim.setText(initNim);
        etNama.setText(initNama);

    }

    private void loginMentor(){
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.show();
        AndroidNetworking.post(CLIENT_API.loginMentor)
                .addBodyParameter("username", txtNIM.getText().toString())
                .addBodyParameter("password", txtPassword.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        Log.d("response", String.valueOf(response));
                        findViewById(R.id.lyt_choose_login).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.item_animation_fallup));
                        findViewById(R.id.lyt_choose_login).setVisibility(View.GONE);
                        txtNIM.setEnabled(true);
                        txtPassword.setEnabled(true);
                        try {
                            String message = response.getJSONArray("data_mentor").getJSONObject(0).getString("status_login");
                            if(message.contains("success")){
                                Handler a = new Handler();
                                animLogin();
                                pDialog.hide();
                                a.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent login  = new Intent(MainActivity.this,dashboard_mentor.class);
                                        Toast.makeText(MainActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                        try {
                                            String nama_mentor = response.getJSONArray("data_mentor").getJSONObject(0).getString("nama_mentor");
                                            String nim_mentor = response.getJSONArray("data_mentor").getJSONObject(0).getString("nim_mentor");
                                            String kontak = response.getJSONArray("data_mentor").getJSONObject(0).getString("no_telp");
                                            String line_id = response.getJSONArray("data_mentor").getJSONObject(0).getString("line_id");
                                            String id_mentor = response.getJSONArray("data_mentor").getJSONObject(0).getString("id_mentor");
                                            String fakultas_mentor = response.getJSONArray("data_mentor").getJSONObject(0).getString("fakultas");

                                            login.putExtra(ConstantMentor.nama,nama_mentor);
                                            login.putExtra(ConstantMentor.nim,nim_mentor);
                                            login.putExtra(ConstantMentor.mentor_telp,kontak);
                                            login.putExtra(ConstantMentor.mentor_line,line_id);
                                            login.putExtra(ConstantMentor.mentor_id,id_mentor);
                                            login.putExtra(ConstantMentor.fakultas,fakultas_mentor);
                                            login.putExtra(ConstantMentor.password,txtPassword.getText().toString());
                                            startActivity(login);
                                            finish();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                },1300);


                            }else{
                                pDialog.hide();
                                Toast.makeText(MainActivity.this, "Username atau Password Salah", Toast.LENGTH_SHORT).show();
                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setContentText("Username Atau Password Tidak Ditemukan")
                                        .setConfirmText("Coba Lagi")
                                        .show();
                            }
                        } catch (Exception e) {
                            pDialog.hide();
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Gagal Terhubung Dengan Server", Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setContentText("Gagal Terhubung")
                                    .setConfirmText("Coba Lagi")
                                    .show();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        findViewById(R.id.lyt_choose_login).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.item_animation_fallup));
                        findViewById(R.id.lyt_choose_login).setVisibility(View.GONE);
                        txtNIM.setEnabled(true);
                        txtPassword.setEnabled(true);
                        pDialog.hide();
                        pDialog.dismissWithAnimation();
                        pDialog.setCancelable(true);
                        // handle error jhjh
                        error();
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void loginMentee(String nim, String password){
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.show();
        AndroidNetworking.post(CLIENT_API.login_api)
                .addBodyParameter("username", txtNIM.getText().toString())
                .addBodyParameter("password", txtPassword.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        findViewById(R.id.lyt_choose_login).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.item_animation_fallup));
                        findViewById(R.id.lyt_choose_login).setVisibility(View.GONE);
                        txtNIM.setEnabled(true);
                        txtPassword.setEnabled(true);
                        try {
                            String message = response.getString("status_login");
                            if(message.contains("success")){
                                Handler a = new Handler();
                                pDialog.hide();
                                animLogin();
                                a.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent login  = new Intent(MainActivity.this,dashboard_mentee.class);
                                        Toast.makeText(MainActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                        try {
                                            login.putExtra(ConstantMentee.mentee_id,response.getString("id"));
                                            login.putExtra(ConstantMentee.nama,response.getString("nama"));
                                            login.putExtra(ConstantMentee.nim,response.getString("nim"));
                                            login.putExtra(ConstantMentee.jurusan,response.getString("jurusan"));
                                            login.putExtra(ConstantMentee.fakultas,response.getString("fakultas"));
                                            login.putExtra(ConstantMentee.kelompok,response.getString("kode_kelompok"));
                                            login.putExtra(ConstantMentee.mentor_name,response.getString("mentor"));
                                            login.putExtra(ConstantMentee.mentor_line,response.getString("line_mentor"));
                                            login.putExtra(ConstantMentee.mentor_telp,response.getString("telp_mentor"));
                                            login.putExtra(ConstantMentee.password,txtPassword.getText().toString());
                                            login.putExtra(ConstantMentee.mentee_line,response.getString("line_mentee"));
                                            login.putExtra(ConstantMentee.mentee_telp,response.getString("telp_mentee"));
                                            startActivity(login);
                                            finish();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                },1300);


                            }else{
                                pDialog.hide();
                                Toast.makeText(MainActivity.this, "Username atau Password Salah", Toast.LENGTH_SHORT).show();
                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setContentText("Username Atau Password Tidak Ditemukan")
                                        .setConfirmText("Coba Lagi")
                                        .show();
                            }
                        } catch (Exception e) {
                            pDialog.hide();
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Gagal Terhubung Dengan Server", Toast.LENGTH_SHORT).show();
                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setContentText("Gagal Terhubung")
                                    .setConfirmText("Coba Lagi")
                                    .show();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        findViewById(R.id.lyt_choose_login).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.item_animation_fallup));
                        findViewById(R.id.lyt_choose_login).setVisibility(View.GONE);
                        txtNIM.setEnabled(true);
                        txtPassword.setEnabled(true);
                        pDialog.hide();
                        pDialog.dismissWithAnimation();
                        pDialog.setCancelable(true);
                        error();
                        // handle error jhjh
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loginAdmin(){
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.show();
        AndroidNetworking.post(CLIENT_API.loginAdmin)
                .addBodyParameter("username", txtNIM.getText().toString())
                .addBodyParameter("password", txtPassword.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        findViewById(R.id.lyt_choose_login).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.item_animation_fallup));
                        findViewById(R.id.lyt_choose_login).setVisibility(View.GONE);
                        txtNIM.setEnabled(true);
                        txtPassword.setEnabled(true);
                        try {
                            String message = response.getJSONArray("data_admin").getJSONObject(0).getString("status_login");
                            if(message.contains("success")){
                                Handler a = new Handler();
                                pDialog.hide();
                                animLogin();
                                a.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        Intent login  = new Intent(MainActivity.this,dashboard_admin.class);
                                        Toast.makeText(MainActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                        startActivity(login);
                                        finish();
                                    }
                                },1300);


                            }else{
                                pDialog.hide();
                                Toast.makeText(MainActivity.this, "Username atau Password Salah", Toast.LENGTH_SHORT).show();
                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setContentText("Username Atau Password Tidak Ditemukan")
                                        .setConfirmText("Coba Lagi")
                                        .show();
                            }
                        } catch (Exception e) {
                            pDialog.hide();
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Gagal Terhubung Dengan Server", Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setContentText("Gagal Terhubung")
                                    .setConfirmText("Coba Lagi")
                                    .show();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        findViewById(R.id.lyt_choose_login).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.item_animation_fallup));
                        findViewById(R.id.lyt_choose_login).setVisibility(View.GONE);
                        error();
                    }
                });
    }


    private void ind_loading(){

    }
    private void getAgenda() {
        findViewById(R.id.btn_forgot_password).setVisibility(View.GONE);
        findViewById(R.id.animation_lootie_loading).setVisibility(View.GONE);
        AndroidNetworking.post(CLIENT_API.getKontak)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        findViewById(R.id.btn_forgot_password).setVisibility(View.VISIBLE);
                        findViewById(R.id.btn_forgot_password).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));

//                        findViewById(R.id.animation_lootie_loading).setVisibility(View.INVISIBLE);
                        try {
                            for (int b = 0; b <= response.getJSONArray("kontak").length(); b++) {
                                String whatsapp = response.getJSONArray("kontak").getJSONObject(b).getString("whatsapp");
                                kontak.add(b, whatsapp);
                                ArrayAdapter<String> adp1 = new ArrayAdapter<String>
                                        (MainActivity.this, android.R.layout.simple_list_item_1, kontak);
                                daftarKontak.setAdapter(adp1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        findViewById(R.id.btn_forgot_password).setEnabled(false);
                        findViewById(R.id.animation_lootie_loading).setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, anError.getMessage(), Toast.LENGTH_LONG).show();
                        error();
                    }
                });

    }

    public void onClickWhatsApp() {
        PackageManager pm = getPackageManager();
        try {
            PackageManager packageManager = getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String message = "Assalamualaikum, Nama Saya " + etNama.getText() + " " + etNim.getText() + " dari Fakultas " + etJurusan.getText() + " \n" +
                    "Mohon Untuk mereset Password Akun Mentee/Mentor (Hapus Salah Satu) saya, Terima Kasih";
            String phone = daftarKontak.getSelectedItem().toString();
            try {
                String url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + URLEncoder.encode(message, "UTF-8");
                i.setPackage("com.whatsapp");
                i.setData(Uri.parse(url));
                if (i.resolveActivity(packageManager) != null) {
                    startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
                error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            error();
        }
    }

    private void error(){
        findViewById(R.id.animation_lootie_loading).setVisibility(View.INVISIBLE);
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        pDialog.setTitleText("Gagal Terhubung Dengan Server");
        pDialog.setContentText("Periksa Koneksi Internet Anda atau Coba Lagi Nanti");
        pDialog.setCancelable(true);
        pDialog.setConfirmText("OK");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                pDialog.dismissWithAnimation();
            }
        });
        pDialog.show();
    }

    private void error2(){
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        pDialog.setTitleText("Terjadi Kesalahan");
        pDialog.setContentText("Tutup Aplikasi dan Buka Kembali");
        pDialog.setCancelable(true);
        pDialog.setConfirmText("OK");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                pDialog.dismissWithAnimation();
            }
        });
        pDialog.show();
    }

    private void animLogin(){
        findViewById(R.id.btn_forgot_password).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation_go));
        findViewById(R.id.btn_forgot_password).setVisibility(View.GONE);
        findViewById(R.id.rl_input).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation_go));
        findViewById(R.id.rl_input).setVisibility(View.GONE);
        findViewById(R.id.fab_login).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation_go));
        findViewById(R.id.fab_login).setVisibility(View.GONE);
        findViewById(R.id.logobm).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation_go));
        findViewById(R.id.logobm).setVisibility(View.GONE);
        findViewById(R.id.tv_cred).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation_go));
        findViewById(R.id.tv_cred).setVisibility(View.GONE);
        findViewById(R.id.tv_fakultas).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation_go));
        findViewById(R.id.tv_fakultas).setVisibility(View.GONE);
        findViewById(R.id.tv_ead).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation_go));
        findViewById(R.id.tv_ead).setVisibility(View.GONE);
    }
    public void logoutConfirm(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Anda Yakin Ingin Keluar ???");
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
                        finish();
                        System.exit(0);
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
