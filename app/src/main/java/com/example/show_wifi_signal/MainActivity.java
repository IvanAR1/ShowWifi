package com.example.show_wifi_signal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private TextView textViewWifi;
    private WifiManager wifiManager;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            getWifiNow();
        }
    }

    private void getWifiNow() {
        textViewWifi = findViewById(R.id.textViewWifi);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiManager.isWifiEnabled()) {
            String ssid = wifiInfo.getSSID();
            int rssi = wifiInfo.getRssi();
            String estimatedSpeed = estimateSpeedFromSignal(rssi);
            textViewWifi.setText("SSID: " + ssid + ", Nivel: " + rssi + " dBm, Velocidad estimada: " + estimatedSpeed);
        } else {
            textViewWifi.setText("El Wi-Fi está desactivado");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getWifiNow();
            } else {
                Toast.makeText(this, "Permiso de ubicación necesario para acceder a la información de Wi-Fi", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWifiNow();
    }

    private String estimateSpeedFromSignal(int level) {
        if (level >= -50) {
            return "Hasta 1000 Mbps";
        } else if (level >= -60) {
            return "Hasta 500 Mbps";
        } else if (level >= -70) {
            return "Hasta 100 Mbps";
        } else if (level >= -80) {
            return "Menos de 50 Mbps";
        } else {
            return "Velocidad muy baja o inestable";
        }
    }
}
