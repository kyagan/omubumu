package com.kaanyagan.omubumu;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.kaanyagan.omubumu.adapters.SubCategoryAdapter;
import com.kaanyagan.omubumu.models.SubCategoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubCategory extends AppCompatActivity implements SubCategoryAdapter.OnItemClickListener{
    ImageView img_back,img_share;
    TextView sub_category_tv;
    ProgressBar progressBar;

    JsonArrayRequest jsonArrayRequest;
    RequestQueue queue;
    RecyclerView recyclerView;
    List<SubCategoryModel> subCategoryModel;
    private static String JSON_URL;
    SubCategoryAdapter adapter;
    String gelen_main_category_id, gelen_main_category_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        Intent i = getIntent();
        gelen_main_category_id = i.getStringExtra("main_category_id");
        gelen_main_category_name = i.getStringExtra("main_category_name");

        img_back = findViewById(R.id.img_back);
        img_share = findViewById(R.id.img_share);
        progressBar = findViewById(R.id.progress_bar);
        sub_category_tv = findViewById(R.id.sub_category_tv);
        sub_category_tv.setText(gelen_main_category_name);

        JSON_URL = "https://kaanyagan.com/and/omubumu/sub_category.php?id="+gelen_main_category_id;
        recyclerView = findViewById(R.id.sub_category_rv);
        subCategoryModel = new ArrayList<>();
        SubCategory();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"fwef",Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(SubCategory.this, MainActivity.class);
        startActivity(i);

        super.onBackPressed();
    }

    private void SubCategory() {
        queue = Volley.newRequestQueue(this);
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressBar.setVisibility(View.GONE);
                jsonArrayRequest.setShouldCache(false);
                queue.getCache().clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject songObject = response.getJSONObject(i);
                        SubCategoryModel subCategoryModels = new SubCategoryModel();
                        subCategoryModels.setSubCategoryId(songObject.getString("subCategoryId"));
                        subCategoryModels.setSubCategoryName(songObject.getString("subCategoryName"));
                        subCategoryModels.setSubCategoryImg(songObject.getString("subCategoryImg"));
                        subCategoryModels.setSubCategoryQuestionCount((songObject.getString("subCategoryQuestionCount")));
                        subCategoryModel.add(subCategoryModels);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new SubCategoryAdapter(getApplicationContext(),subCategoryModel);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(SubCategory.this);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });
        queue.add(jsonArrayRequest);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(this, Question.class);
        SubCategoryModel clickedItem = subCategoryModel.get(position);
        i.putExtra("main_category_id",gelen_main_category_id);
        i.putExtra("main_category_name",gelen_main_category_name);
        i.putExtra("sub_category_id",clickedItem.getSubCategoryId());
        i.putExtra("sub_category_name",clickedItem.getSubCategoryName());
        i.putExtra("sub_category_question_count",clickedItem.getSubCategoryQuestionCount());
        i.putExtra("question_row_id","1");
        startActivity(i);
    }
}
