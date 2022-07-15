package com.aplication.latihanapi_10119219;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aplication.latihanapi_10119219.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText cityId;
    TextView resultTv;
    Button btnFecth;
    String cityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityId = findViewById(R.id.city_et);
        resultTv = findViewById(R.id.result_tv);
        btnFecth = findViewById(R.id.btn_fet);

        btnFecth.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_fet){
            cityName = cityId.getText().toString();
            try {
                getData();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

    }
    public void getData( ) throws MalformedURLException {
        Uri uri = Uri.parse("https://katanime.vercel.app/api/getbyanime?anime=naruto&page=1")
                .buildUpon().build();
        URL url = new URL(uri.toString());
        new DOTask().execute(url);


    }
    class DOTask extends AsyncTask<URL, Void,String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls [0];
            String data = null;
            try {
                data = NetworkUtils.makeHTTPRequest(url);
            }catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                parseJson(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        public void parseJson(String data) throws JSONException {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(data);
            }catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray cityArray = jsonObject.getJSONArray("result");

            for (int i=0; i<cityArray.length();i++){
                JSONObject cityo = cityArray.getJSONObject(i);
                String cityn = cityo.get("character").toString();
                if (cityn.equals(cityName)){
                    String population = cityo.get("indo").toString();
                    resultTv.setText(population);
                    break;
                }
                else {
                    resultTv.setText("notfound");
                }


            }
        }
    }

}