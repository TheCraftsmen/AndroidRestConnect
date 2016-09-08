package com.example.blas.jsontext;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class newMatch extends AppCompatActivity {

    ArrayList<String> listado = new ArrayList<String>();
    ArrayList<String> detalle = new ArrayList<String>();
    String url1 = "https://todaymatch.herokuapp.com/_all_user";
    JSONObject jsonObject;
    ListView listView2;
    String match_refer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_match, menu);
        new AsyncTaskExample().execute(url1);
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

    public void salir(View v) {
        finish();
    }

    public void newMatch(View view){
        String phone_n;
        String n_match = "https://todaymatch.herokuapp.com/_new_match/";
        EditText match_name = (EditText) findViewById(R.id.match_name);
        SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
        phone_n = prefe.getString("phone", "");
        String match_string = match_name.getText().toString();
        n_match = n_match + phone_n + "/1/" + match_string + "/" + match_refer;
        System.out.println(n_match);
        new AsyncTaskGet().execute(n_match);
        Toast.makeText(newMatch.this, "Partido creado",
                Toast.LENGTH_LONG).show();
        finish();

    }

    public class AsyncTaskExample extends AsyncTask<String, String, ArrayAdapter<String>> {

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected ArrayAdapter<String> doInBackground(String... url) {
            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(newMatch.this, android.R.layout.simple_list_item_1, listado);
            try {
                jsonObject = JsonParser.readJsonFromUrl(url[0]);
                System.out.println("entra a buscar json");
                System.out.println(jsonObject.getJSONArray("data"));
                JSONArray respuesta = jsonObject.getJSONArray("data");
                String texto;
                String texto2;
                for(int i = 0; i < respuesta.length(); i++){
                    texto = (String) respuesta.getJSONObject(i).getString("user_alias");
                    texto2 = respuesta.getJSONObject(i).getString("phone");
                    System.out.println(texto);
                    listado.add("texto " + texto);
                    detalle.add(texto2);
                    adaptador = new ArrayAdapter<String>(newMatch.this, android.R.layout.simple_list_item_1, listado);


                }
            }catch (IOException | JSONException e){
                e.printStackTrace();
                System.out.println("entra en la excpecion");
            }
            return adaptador;
        }

        @Override
        protected void onPostExecute(ArrayAdapter<String> result) {
            listView2 = (ListView) findViewById(R.id.listView2);
            //textViewName =(TextView)findViewById(R.id.textViewName);
            listView2.setAdapter(result);
            listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int position, long id) {
                    System.out.println(detalle.get(position));
                    //textViewName.setText(detalle.get(position));
                    match_refer = detalle.get(position);
                }
            });
        }

    }

    public class AsyncTaskGet extends AsyncTask<String, String, String[]> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String[] doInBackground(String... url) {
            String[] objetos = new String[3];
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
        }

    }
}
