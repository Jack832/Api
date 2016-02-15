package com.example.test4.api;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EditText e1;
    private ProgressBar progressBar;
    private LinearLayout linearLayout=null;
    private String[] listofimages;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        e1=(EditText)findViewById(R.id.edit1);
        linearLayout=(LinearLayout)findViewById(R.id.linear);

        progressBar=(ProgressBar)findViewById(R.id.progress1);
        listView=(ListView)findViewById(R.id.list1);
        listView.setOnItemClickListener(this);
        listofimages=getResources().getStringArray(R.array.urls1);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public void downloadimage(View view){
        String url=e1.getText().toString();
     Thread one=new Thread(new Downloadimg(url));
        one.start();
//        File file1=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        String s=listofimages[0];
//        Uri uri=Uri.parse(s);
//       Toast.makeText(MainActivity.this,"h"+uri.getLastPathSegment(),Toast.LENGTH_LONG).show();


    }
    public boolean downloadImageusingThread(String url){
        boolean successful=false;
        URL down=null;
        HttpURLConnection connection=null;
        InputStream inputStream=null;
        FileOutputStream fileOutputStream=null;
        File file=null;

        try {
            down=new URL(url);
            connection= (HttpURLConnection) down.openConnection();
            inputStream=connection.getInputStream();
            file=new File(Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_PICTURES)
                    .getAbsolutePath()+"/"+Uri.parse(url).getLastPathSegment());
            fileOutputStream=new FileOutputStream(file);
            //Toast.makeText(getApplication(),""+file,Toast.LENGTH_LONG).show();
            int read=-1;
            byte[] b= new byte[1024];

            while((read=inputStream.read(b)) != -1){
               fileOutputStream.write(b,0,read);
            }
            successful=true;
        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    linearLayout.setVisibility(View.GONE);
                }
            });
            if(connection != null){
            connection.disconnect();
            }
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fileOutputStream!= null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return successful;
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        e1.setText(listofimages[position]);
    }
    private class Downloadimg implements Runnable{
        private String url;
        public Downloadimg(String url){
            this.url=url;
        }

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    linearLayout.setVisibility(View.VISIBLE);

                }
            });
         downloadImageusingThread(url);

        }
    }
}
