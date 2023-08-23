package com.kaanyagan.omubumu;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class VeriEkle extends AppCompatActivity {

    EditText txtName,txtEmail, txtContact, txtAddress;
    Button btn_insert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veri_ekle);

        txtName = findViewById(R.id.edtName);
        txtEmail = findViewById(R.id.edtEmail);
        txtContact = findViewById(R.id.edtContact);
        txtAddress = findViewById(R.id.edtAddress);
        btn_insert = findViewById(R.id.btnInsert);

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });
    }

    private void insertData() {
        final String name = txtName.getText().toString().trim();
        final String email = txtEmail.getText().toString().trim();
        final String contact = txtContact.getText().toString().trim();
        final String address = txtAddress.getText().toString().trim();

        if (name.isEmpty()){
            Toast.makeText(this,"enter name",Toast.LENGTH_SHORT).show();
        }
        else if (email.isEmpty()){
            Toast.makeText(this,"enter email",Toast.LENGTH_SHORT).show();
        }
        else if (contact.isEmpty()){
            Toast.makeText(this,"enter contact",Toast.LENGTH_SHORT).show();
        }
        else if (address.isEmpty()){
            Toast.makeText(this,"enter address",Toast.LENGTH_SHORT).show();
        }
        else{
            StringRequest request = new StringRequest(Request.Method.POST, "http://kaanyagan.com/and/omubumu/ekle.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equalsIgnoreCase("Data inserted")){
                        Toast.makeText(VeriEkle.this,"Data inserted",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(VeriEkle.this,response,Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(VeriEkle.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String,String> params = new HashMap<String,String>();
                    params.put("name",name);
                    params.put("email",email);
                    params.put("contact",contact);
                    params.put("address",address);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(VeriEkle.this);
            requestQueue.add(request);
        }
    }
}
