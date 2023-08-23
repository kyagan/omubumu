package com.kaanyagan.omubumu;

import static android.view.View.GONE;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.kaanyagan.omubumu.adapters.MainCategoryAdapter;
import com.kaanyagan.omubumu.models.MainCategoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

public class MainActivity extends AppCompatActivity implements MainCategoryAdapter.OnItemClickListener {
    ProgressBar progressBar;
    RecyclerView recyclerView;
    List<MainCategoryModel> mainCategoryModel;
    private static String JSON_URL = "https://kaanyagan.com/and/omubumu/main_category.php";
    MainCategoryAdapter adapter;
    ImageView imgSettings;
    ImageView imgNet;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            ProviderInstaller.installIfNeeded(getApplicationContext());
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            sslContext.createSSLEngine();
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException
                | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

        imgNet = findViewById(R.id.imgNet);
        imgNet.setOnClickListener(v ->{
            getMainCategory();
        });

        NetConnection netConnection = new NetConnection();
        if (netConnection.isConnected(getApplicationContext())){
            //Toast.makeText(getApplicationContext(),"internet var",Toast.LENGTH_SHORT).show();
            imgNet.setVisibility(GONE);
        }
        else{
            //Toast.makeText(getApplicationContext(),"internet yok",Toast.LENGTH_SHORT).show();
            imgNet.setVisibility(View.VISIBLE);
        }

        imgSettings = findViewById(R.id.imgSettings);
        imgSettings.setOnClickListener(v -> {
            Toast.makeText(this,"Ayarlar hazır değil!",Toast.LENGTH_SHORT).show();
        });



        recyclerView = findViewById(R.id.main_category_rv);
        progressBar = findViewById(R.id.progress_bar);
        mainCategoryModel = new ArrayList<>();
        getMainCategory();
    }

    private void getMainCategory() {
        queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                queue.getCache().clear();
                progressBar.setVisibility(GONE);
                imgNet.setVisibility(GONE);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject mainCategoryObject = response.getJSONObject(i);
                        MainCategoryModel mainCategoryModels = new MainCategoryModel();
                        mainCategoryModels.setMainCategoryId(mainCategoryObject.getString("mainCategoryId"));
                        mainCategoryModels.setMainCategoryName(mainCategoryObject.getString("mainCategoryName"));
                        mainCategoryModels.setMainCategoryImg(mainCategoryObject.getString("mainCategoryImg"));
                        mainCategoryModel.add(mainCategoryModels);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new MainCategoryAdapter(getApplicationContext(), mainCategoryModel);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(MainActivity.this);
            }
        }, error -> {
            progressBar.setVisibility(GONE);
            Log.d("tag", "onErrorResponse: " + error.getMessage());
        });
        queue.add(jsonArrayRequest);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(int position) {
            Intent i = new Intent(this, SubCategory.class);
            MainCategoryModel clickedItem = mainCategoryModel.get(position);
            i.putExtra("main_category_id",clickedItem.getMainCategoryId());
            i.putExtra("main_category_name",clickedItem.getMainCategoryName());
            startActivity(i);
    }

}
