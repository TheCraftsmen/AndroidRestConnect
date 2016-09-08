package com.example.blas.jsontext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ArrayList<String> listado = new ArrayList<String>();
    ArrayList<String> detalle = new ArrayList<String>();
    ProgressBar progressBar;
    TextView textViewName;
    String[] objetos = new String[3];
    String url1 = "https://todaymatch.herokuapp.com/_pending_match/";
    JSONObject jsonObject;
    JSONObject pseudoPost;
    ListView listView;
    String match_refer;
    String phone_n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
        phone_n = prefe.getString("phone", "");
        url1 = url1 + phone_n;
        System.out.println(url1);
        new AsyncTaskExample().execute(url1);
        System.out.println("pasa  por aca");
        System.out.println(listado);
        //listView =(ListView)findViewById(R.id.listView);
        //ArrayAdapter<String> adapter;
        //adapter = new ArrayAdapter<String>(
        //        MainActivity.this,
        //        android.R.layout.simple_list_item_1, listado);
        //listView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean enviar(View view){
        System.out.println("envia los datos");
        // Build the JSON object to pass parameters
        makeConnection();
        return true;
    }

    public boolean rechazar(View view){
        System.out.println("envia los datos");
        // Build the JSON object to pass parameters
        makeConnectionTwo();
        return true;
    }

    public boolean configuration(View view){
        Intent i = new Intent(this, Main2Activity.class );
        startActivity(i);
        return true;
    }

    public boolean newMatch(View view){
        Intent i = new Intent(this, newMatch.class );
        startActivity(i);
        return true;
    }

    private boolean makeConnection(){
        System.out.println("entra al conector");
        TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String url2 = "https://todaymatch.herokuapp.com/_confirm_match/" + match_refer + "/" + phone_n + "/1";
        new AsyncTaskGet().execute(url2);
        return true;
    }

    private boolean makeConnectionTwo(){
        System.out.println("entra al conector");
        TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String url3 = "https://todaymatch.herokuapp.com/_confirm_match/" + match_refer + "/" + phone_n + "/0";
        new AsyncTaskGet().execute(url3);
        return true;
    }


    public class AsyncTaskExample extends AsyncTask<String, String, ArrayAdapter<String>> {

        @Override
        protected void onPreExecute() {
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected ArrayAdapter<String> doInBackground(String... url) {
            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, listado);
            try {
                jsonObject = JsonParser.readJsonFromUrl(url[0]);
                System.out.println("entra a buscar json");
                System.out.println(jsonObject.getJSONArray("data"));
                JSONArray respuesta = jsonObject.getJSONArray("data");
                String texto;
                String texto2;
                for(int i = 0; i < respuesta.length(); i++){
                    texto = (String) respuesta.getJSONObject(i).getString("match_name");
                    texto2 = respuesta.getJSONObject(i).getString("match_id");
                    System.out.println(texto);
                    listado.add("texto " + texto);
                    detalle.add(texto2);
                    adaptador = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, listado);


                }
            }catch (IOException | JSONException e){
                e.printStackTrace();
                System.out.println("entra en la excpecion");
            }
            return adaptador;
        }

        @Override
        protected void onPostExecute(ArrayAdapter<String> result) {
            listView =(ListView)findViewById(R.id.listView);
            textViewName =(TextView)findViewById(R.id.textViewName);
            listView.setAdapter(result);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int position, long id) {
                    System.out.println(detalle.get(position));
                    textViewName.setText(detalle.get(position));
                    match_refer = detalle.get(position);
                }
            });
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
        }

    }

    public class AsyncTaskGet extends AsyncTask<String, String, String[]> {

        @Override
        protected void onPreExecute() {
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... url) {

            try {
                jsonObject = JsonParser.readJsonFromUrl(url[0]);
                System.out.println("entra a buscar json");
            }catch (IOException | JSONException e){
                e.printStackTrace();
                System.out.println("entra en la excpecion");
            }
            return objetos;
        }

        @Override
        protected void onPostExecute(String[] stringFromDoInBackground) {
            textViewName = (TextView) findViewById(R.id.textViewName);
            textViewName.setText(stringFromDoInBackground[0]);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
        }

    }

}

