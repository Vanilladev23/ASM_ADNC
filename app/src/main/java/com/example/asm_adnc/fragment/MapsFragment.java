package com.example.asm_adnc.fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.asm_adnc.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment {
    String address;
    private GoogleMap mMap;

    ArrayList<LatLng> arrayList = new ArrayList<>();

    LatLng HCM = new LatLng(10.852952582559103, 106.62954645531292);

    ArrayList<String> title = new ArrayList<>();

    String hcm = "Hồ Chí Minh";
    // Truyền từ ngoài vào trong fragment
    public static MapsFragment newInstance(String address) {
        MapsFragment mapsFragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putString("address", address);
        mapsFragment.setArguments(args);
        return mapsFragment;
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            try {
                // Chuyển từ địa chỉ sang kinh độ, vĩ độ
                Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
                List<Address> list = geocoder.getFromLocationName(address, 3);
                if (list == null) {
                    return;
                }
                Address location = list.get(0);
                // Hiển thị kinh độ, vĩ độ lên map
                LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(sydney).title(address));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            } catch (Exception e) {
                e.printStackTrace();
            }

            mMap = googleMap;

            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            mMap.setMinZoomPreference(5.0f);

            try {

                for (int i=0; i<arrayList.size(); i++){

                    for (int j=0; j<title.size(); j++) {
                        mMap.addMarker(new MarkerOptions().position(arrayList.get(j)).title("FPT Polytechnic " + String.valueOf(title.get(j))));

                    }

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(arrayList.get(i)));

                }

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {

                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 18);
                        marker.showInfoWindow();
                        mMap.animateCamera(cameraUpdate, 500, null);

                        return true;
                    }
                });

            } catch (Exception e){
                Log.d(">>>>>>TAG", "onMapReady: " + e.getMessage());
            }


        }
    };

    // Đọc dữ liệu từ ngoài vào trong fragment
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            address = getArguments().getString("address");
        }
    }

    // Load layout
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    // Xử lý logic
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        arrayList.add(HCM);
        title.add(hcm);
    }
}