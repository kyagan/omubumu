package com.kaanyagan.omubumu;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.kaanyagan.omubumu.adapters.QuestionAdapter;
import com.kaanyagan.omubumu.models.QuestionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Question extends AppCompatActivity implements QuestionAdapter.OnItemClickListener{
    ImageView img_back,img_share;
    TextView question_tv,votes_a_tv,votes_b_tv;
    ProgressBar progressBar;
    Integer total_votes,votes_a_count,votes_b_count;
    Float votes_a_percentage,votes_b_percentage;
    JsonArrayRequest jsonArrayRequest;
    RequestQueue queue;
    RecyclerView question_rv;
    List<QuestionModel> questionModel;
    private static String JSON_URL;
    QuestionAdapter adapter;
    Button sonraki_btn;
    RelativeLayout votes_a_rl,votes_b_rl;
    Float votes_a_percentage_saf,votes_b_percentage_saf;
    Integer gelen_question_row_id_int, gelen_sub_category_question_count_int;
    String gelen_sub_category_name, questions, gelen_main_category_id;
    String gelen_question_row_id;
    String gelen_sub_category_id, gelen_main_category_name, gelen_sub_category_question_count;
    String tmpStr10;
    QuestionModel questionModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Intent i = getIntent();
        gelen_main_category_id = i.getStringExtra("main_category_id");
        gelen_main_category_name = i.getStringExtra("main_category_name");
        gelen_sub_category_id = i.getStringExtra("sub_category_id");
        gelen_sub_category_name = i.getStringExtra("sub_category_name");
        gelen_question_row_id = i.getStringExtra("question_row_id");
        gelen_sub_category_question_count = i.getStringExtra("sub_category_question_count");
        gelen_sub_category_question_count_int = Integer.parseInt(gelen_sub_category_question_count);
        gelen_sub_category_question_count_int++;
        gelen_question_row_id_int = Integer.parseInt(gelen_question_row_id);

        img_back = findViewById(R.id.img_back);
        img_share = findViewById(R.id.img_share);
        progressBar = findViewById(R.id.progress_bar);
        question_tv = findViewById(R.id.question_tv);
        question_tv.setText(gelen_sub_category_name);
        sonraki_btn = findViewById(R.id.sonraki_btn);
        img_share = findViewById(R.id.img_share);
        votes_a_rl = findViewById(R.id.votes_a_rl);
        votes_b_rl = findViewById(R.id.votes_b_rl);
        votes_a_tv = findViewById(R.id.votes_a_tv);
        votes_b_tv = findViewById(R.id.votes_b_tv);

        JSON_URL = "https://kaanyagan.com/and/omubumu/question.php?question_sub_category_id="+gelen_sub_category_id+"&question_row_id="+gelen_question_row_id;
        question_rv = findViewById(R.id.question_rv);
        questionModel = new ArrayList<>();
        Question();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    onBackPressed();

            }
        });
        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "O mu Bu mu? " + questions;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        sonraki_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Question.this, R.style.AlertDialog)
                        //.setTitle("Sorular bitti!")
                        .setMessage("Lütfen, bir sonraki soruya geçmek için önce bu soruyu yanıtlayın.")
                        .setPositiveButton("Tamam", null)
                        .show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if((gelen_question_row_id_int)==1) {
            Intent i = new Intent(getApplicationContext(), SubCategory.class);
            i.putExtra("main_category_id",gelen_main_category_id);
            i.putExtra("main_category_name",gelen_main_category_name);
            startActivity(i);
        }
        else {
            new AlertDialog.Builder(Question.this, R.style.AlertDialog)
                    .setTitle("Çıkmak istediğinizden emin misin?")
                    .setMessage("Eğer çıkmak istiyorsanız en baştan başlamanız gerekecek.")
                    .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(getApplicationContext(), SubCategory.class);
                            i.putExtra("main_category_id", gelen_main_category_id);
                            i.putExtra("main_category_name", gelen_main_category_name);
                            startActivity(i);
                            finish();
                        }
                    })
                    .setNegativeButton("Hayır", null)
                    .show();
        }
    }

    public void Question() {
        queue = Volley.newRequestQueue(this);
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressBar.setVisibility(View.GONE);
                sonraki_btn.setVisibility(View.VISIBLE);
                jsonArrayRequest.setShouldCache(false);
                queue.getCache().clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject songObject = response.getJSONObject(i);
                        questionModels = new QuestionModel();
                        questionModels.setQuestionId(songObject.getString("questionId"));
                        questionModels.setQuestionSubCategoryId(songObject.getString("questionSubCategoryId"));
                        questionModels.setQuestionRowId(songObject.getString("questionRowId"));
                        questionModels.setQuestionChooseId(songObject.getString("questionChooseId"));
                        questionModels.setQuestionA(songObject.getString("questionA"));
                        questionModels.setQuestionACount(songObject.getInt("questionACount"));
                        if (i==0) {
                            votes_a_count = Integer.parseInt(songObject.getString("questionACount"));
                            questions = questionModels.getQuestionA();
                        }
                        if (i==1) {
                            votes_b_count = Integer.parseInt(songObject.getString("questionACount"));
                            questions = questions + " ya da " + questionModels.getQuestionA() + " https://play.google.com/store/apps/details?id=com.kaanyagan.omubumu";
                        }
                        questionModel.add(questionModels);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                question_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new QuestionAdapter(getApplicationContext(),questionModel);
                question_rv.addItemDecoration(new MyItemDecoration(getApplicationContext()));
                question_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                question_rv.setAdapter(adapter);
                adapter.setOnItemClickListener(Question.this);
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
        gelen_question_row_id_int++;
        tmpStr10 = String.valueOf(gelen_question_row_id_int);
            if(gelen_sub_category_question_count_int.equals(gelen_question_row_id_int)){
                new AlertDialog.Builder(Question.this, R.style.AlertDialog)
                        .setMessage("Tebrikler! Bu kategorideki tüm soruları cevapladınız.")
                        .setPositiveButton("Tekrar Oyna", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(getApplicationContext(), Question.class);
                                i.putExtra("main_category_id",gelen_main_category_id);
                                i.putExtra("main_category_name",gelen_main_category_name);
                                i.putExtra("sub_category_id",gelen_sub_category_id);
                                i.putExtra("sub_category_name",gelen_sub_category_name);
                                i.putExtra("question_row_id","1");
                                i.putExtra("sub_category_question_count",gelen_sub_category_question_count);
                                startActivity(i);
                                finish();
                            }
                        })
                        .setNegativeButton("Geri Dön", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(getApplicationContext(), SubCategory.class);
                                i.putExtra("main_category_id", gelen_main_category_id);
                                i.putExtra("main_category_name", gelen_main_category_name);
                                startActivity(i);
                                finish();
                            }
                        })
                        .show();
            }

        toVote(position);
        View v = question_rv.findViewHolderForAdapterPosition(position).itemView;
        ImageView img_checked = v.findViewById(R.id.img_checked);
        img_checked.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
        sonraki_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gelen_sub_category_question_count_int.equals(gelen_question_row_id_int)){
                    new AlertDialog.Builder(Question.this, R.style.AlertDialog)
                            .setMessage("Tebrikler! Bu kategorideki tüm soruları cevapladınız.")
                            .setPositiveButton("Tekrar Oyna", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(getApplicationContext(), Question.class);
                                    i.putExtra("main_category_id",gelen_main_category_id);
                                    i.putExtra("main_category_name",gelen_main_category_name);
                                    i.putExtra("sub_category_id",gelen_sub_category_id);
                                    i.putExtra("sub_category_name",gelen_sub_category_name);
                                    i.putExtra("question_row_id","1");
                                    i.putExtra("sub_category_question_count",gelen_sub_category_question_count);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .setNegativeButton("Geri Dön", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(getApplicationContext(), SubCategory.class);
                                    i.putExtra("main_category_id", gelen_main_category_id);
                                    i.putExtra("main_category_name", gelen_main_category_name);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .show();
                }
                else{
                Intent i = new Intent(getApplicationContext(), Question.class);
                i.putExtra("main_category_id",gelen_main_category_id);
                i.putExtra("main_category_name",gelen_main_category_name);
                i.putExtra("sub_category_id",gelen_sub_category_id);
                i.putExtra("sub_category_name",gelen_sub_category_name);
                i.putExtra("question_row_id",tmpStr10);
                i.putExtra("sub_category_question_count",gelen_sub_category_question_count);
                startActivity(i);
                }
            }
        });
    }

    public void toVote(int position){
        getVote(position);
        final QuestionModel clickedItem = questionModel.get(position);
        findViewById(R.id.overlay).setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, "https://kaanyagan.com/and/omubumu/question_save.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                jsonArrayRequest.setShouldCache(false);
                queue.getCache().clear();
                if(response.equalsIgnoreCase("Data inserted")){
                    //Toast.makeText(Question.this,gelen_sub_category_question_count,Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Question.this,response,Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Question.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String,String>();
                params.put("question_id",clickedItem.getQuestionId());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Question.this);
        requestQueue.add(request);
    }

    private void getVote(int position) {
        if (position==0) {votes_a_count++;}
        if (position==1) {votes_b_count++;}
        total_votes = votes_a_count + votes_b_count;
        votes_a_percentage = 1- (float) votes_a_count/total_votes;
        votes_a_percentage_saf = (float) votes_a_count/total_votes;
        votes_b_percentage = 1- (float) votes_b_count/total_votes;
        votes_b_percentage_saf = (float) votes_b_count/total_votes;
        DecimalFormat df = new DecimalFormat("0.00f");
        df.format(votes_a_percentage);
        df.format(votes_b_percentage);
        if(votes_a_percentage>0.0 && votes_a_percentage<0.1){
            votes_a_percentage=0.15f;
            votes_b_percentage=0.85f;
        }
        else if(votes_a_percentage>=0.1 && votes_a_percentage<0.2){
            votes_a_percentage=0.15f;
            votes_b_percentage=0.85f;
        }
        else if (votes_a_percentage>=0.9 && votes_a_percentage<=1) {
            votes_a_percentage=0.85f;
            votes_b_percentage=0.15f;
        }
        votes_a_tv.setText("%"+String.format("%.0f",votes_a_percentage_saf*100));
        votes_b_tv.setText("%"+String.format("%.0f",votes_b_percentage_saf*100));

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT,
                votes_a_percentage
        );
        votes_a_rl.setLayoutParams(param);

        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT,
                votes_b_percentage
        );
        votes_b_rl.setLayoutParams(param2);
    }
}
