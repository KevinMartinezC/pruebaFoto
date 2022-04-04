package com.ksp.subitesv.actividades.cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.ksp.subitesv.R;
import com.ksp.subitesv.includes.AppToolBar;
import com.ksp.subitesv.proveedores.GoogleApiProveedor;
import com.ksp.subitesv.utils.DecodificadorPuntos;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallesSolicitudActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMapa;
    private SupportMapFragment mMapFragment;

    private double mExtraOrigenLat;
    private double mExtraOrigenLng;
    private double mExtraDestinoLat;
    private double mExtraDestinoLng;


    private LatLng mOrigenLatLng;
    private LatLng mDestinoLatLng;

    private GoogleApiProveedor mGoogleApiProveedor;
    private List<LatLng> mPolylineLista;
    private PolylineOptions mPolylineOpciones;

    private TextView mTextViewOrigen;
    private TextView mTextViewDestino;
    private TextView mTextViewTiempo;
    private TextView mTextViewDistancia;

    private String mExtraOrigen;
    private String mExtraDestino;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_solicitud);
        AppToolBar.mostrar(this, "TUS DATOS", true);

        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mMapFragment.getMapAsync(this);

        mExtraOrigenLat = getIntent().getDoubleExtra("origin_lat", 0);
        mExtraOrigenLng = getIntent().getDoubleExtra("origin_lng", 0);
        mExtraDestinoLat = getIntent().getDoubleExtra("destination_lat", 0);
        mExtraDestinoLng = getIntent().getDoubleExtra("destination_lng", 0);
        mExtraOrigen = getIntent().getStringExtra("origin");
        mExtraDestino = getIntent().getStringExtra("destination");

        mOrigenLatLng = new LatLng(mExtraOrigenLat, mExtraOrigenLng);
        mDestinoLatLng = new LatLng(mExtraDestinoLat, mExtraDestinoLng);


        mGoogleApiProveedor = new GoogleApiProveedor(DetallesSolicitudActivity.this);

        mTextViewOrigen = findViewById(R.id.textViewOrigen);
        mTextViewDestino = findViewById(R.id.textViewDestino);
        mTextViewTiempo = findViewById(R.id.textViewTiempo);
        mTextViewDistancia = findViewById(R.id.textViewDistancia);

        mTextViewOrigen.setText(mExtraOrigen);
        mTextViewDestino.setText(mExtraDestino);
    }

    private void DibujarRuta() {
        mGoogleApiProveedor.getDirections(mOrigenLatLng, mDestinoLatLng).enqueue(new Callback<String>() {


            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {

                    JSONObject jsonObject = new JSONObject(response.body());
                    JSONArray jsonArray = jsonObject.getJSONArray("routes");
                    JSONObject route = jsonArray.getJSONObject(0);
                    JSONObject polylines = route.getJSONObject("overview_polyline");
                    String points = polylines.getString("points");
                    mPolylineLista = DecodificadorPuntos.decodePoly(points);
                    mPolylineOpciones = new PolylineOptions();
                    mPolylineOpciones.color(Color.DKGRAY);
                    mPolylineOpciones.width(8f);
                    mPolylineOpciones.startCap(new SquareCap());
                    mPolylineOpciones.jointType(JointType.ROUND);
                    mPolylineOpciones.addAll(mPolylineLista);
                    mMapa.addPolyline(mPolylineOpciones);

                    JSONArray legs =  route.getJSONArray("legs");
                    JSONObject leg = legs.getJSONObject(0);
                    JSONObject distance = leg.getJSONObject("distance");
                    JSONObject duration = leg.getJSONObject("duration");
                    String distanceText = distance.getString("text");
                    String durationText = duration.getString("text");
                    mTextViewTiempo.setText(durationText);
                    mTextViewDistancia.setText(distanceText);

                } catch(Exception e) {
                    Log.d("Error", "Error encontrado " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapa = googleMap;
        mMapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMapa.getUiSettings().setZoomControlsEnabled(true);

        mMapa.addMarker(new MarkerOptions().position(mOrigenLatLng).title("Origen").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_pin_red)));
        mMapa.addMarker(new MarkerOptions().position(mDestinoLatLng).title("Destino").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_pin_blue)));

        mMapa.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(mOrigenLatLng)
                        .zoom(14f)
                        .build()
        ));

        DibujarRuta();
    }


}