package com.ksp.subitesv.actividades.cliente;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.ksp.subitesv.R;
import com.ksp.subitesv.modulos.FCMCuerpo;
import com.ksp.subitesv.modulos.FCMRespuesta;
import com.ksp.subitesv.proveedores.NotificacionProveedor;
import com.ksp.subitesv.proveedores.ProveedorGeoFire;
import com.ksp.subitesv.proveedores.TokenProveedor;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SolicitarConductorActivity extends AppCompatActivity {

    private LottieAnimationView mAnimation;
    private TextView mTextviewLookingFor;
    private Button mButtonCalcelRequest;
    private ProveedorGeoFire mGeofireProvider;

    private  double mExtraOriginLat;
    private double mExtraOriginLng;
    private LatLng mOriginLanLng;
    private double mRadius = 0.1;
    private boolean mDriverFound = false;
    private String mIdDriverFound = "";
    private LatLng mDriverFoundLatLng;
    private NotificacionProveedor mNotificacionProveedor;
    private TokenProveedor mTokenProveedor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_conductor);

        mAnimation = findViewById(R.id.animacion);
        mTextviewLookingFor = findViewById(R.id.textViewLookingFor);
        mButtonCalcelRequest = findViewById(R.id.btnCancelRequest);

        mAnimation.playAnimation();
        mExtraOriginLat = getIntent().getDoubleExtra("origin_lat",0);
        mExtraOriginLng = getIntent().getDoubleExtra("origin_lng",0);
        mOriginLanLng = new LatLng(mExtraOriginLat,mExtraOriginLng);
        mGeofireProvider = new ProveedorGeoFire();
        mNotificacionProveedor = new NotificacionProveedor();
        mTokenProveedor = new TokenProveedor();
        getClosestDriver();
    }
    private void getClosestDriver(){
        mGeofireProvider.obtenerConductoresActivos(mOriginLanLng, mRadius).addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if(!mDriverFound){
                    mDriverFound = true;
                    mIdDriverFound = key;
                    mDriverFoundLatLng = new LatLng(location.latitude, location.longitude);
                    mTextviewLookingFor.setText("CONDUCTOR ENCONTRADO\nESPERANDO RESPUESTA");
                    enviarNotificacion();
                    Log.d("DRIVER","ID:"+ mIdDriverFound);

                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                //iNGRESA CUANDO TERMINA LA BUSQUEDA DEL CONDUCTOR EN UN RADIO DE 0.1 KM
                if(!mDriverFound){
                    mRadius = mRadius + 0.1;

                    //No encontro ningun conductor
                    if(mRadius>5){
                        mTextviewLookingFor.setText("NO SE ENCONTRO UN CONDUCTOR");
                        Toast.makeText(SolicitarConductorActivity.this, "NO SE ENCONTRO UN CONDUCTOR", Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        getClosestDriver();
                    }
                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    private void enviarNotificacion() {
        mTokenProveedor.obtenerToken(mIdDriverFound).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {//contiene la informacion que esta dentro del nodo del id del usuario
                String token = snapshot.child("token").getValue().toString();
                Map<String, String> map = new HashMap<>();
                map.put("title","SOLICITUD DE SERVICIO");
                map.put("body","Un cliente esta solicitando un servicio");
                FCMCuerpo fcmCuerpo = new FCMCuerpo(token, "high",map);
                mNotificacionProveedor.enviarNotificacion(fcmCuerpo).enqueue(new Callback<FCMRespuesta>() {
                    @Override
                    public void onResponse(Call<FCMRespuesta> call, Response<FCMRespuesta> response) {
                        if(response.body() != null){
                            if(response.body().getSuccess() == 1){
                                Toast.makeText(SolicitarConductorActivity.this, R.string.notificacionEnviada, Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(SolicitarConductorActivity.this, R.string.notificacionNOEnviada, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FCMRespuesta> call, Throwable t) {
                        Log.d("Error","Error" + t.getMessage());

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}