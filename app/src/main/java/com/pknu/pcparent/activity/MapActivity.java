package com.pknu.pcparent.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pknu.pcparent.R;

/**
 * Created by Hoon on 2017-06-22.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Double lat;
    private Double lng;
    private String address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onCreate()", "진입");
        setContentView(R.layout.activity_map);

        // 인텐트 파라미터 받기
        Intent intent = getIntent();
        this.lat = intent.getDoubleExtra(MainActivity.EXTRA_LAT, 0.0);
        this.lng = intent.getDoubleExtra(MainActivity.EXTRA_LNG, 0.0);
        this.address = intent.getStringExtra(MainActivity.EXTRA_ADDRESS);
        Log.d("onCreate()", "EXTRA_LAT: " + this.lat + ", EXTRA_LNG: " + this.lng + ", EXTRA_ADDRESS: " + this.address);

        // 맵 fragment 생성
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.frag_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        LatLng latLng = new LatLng(this.lat, this.lng);

        // 마커 옵션 설정
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("위험 감지 장소");
        markerOptions.snippet(address);

        // 마커 추가
        Marker marker = map.addMarker(markerOptions);
        marker.showInfoWindow();

        // 맵 설정
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(14));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }
}
