package com.example.guessthecelebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Button button1,button2,button3,button4;
    int i=0,c=0,flag=0,btnnum,in,inimage=0,inname=0,crrctimg;
    int[] ari;
    Bitmap myImage;
    URL url;
    HttpURLConnection urlConnection = null;
    String[] urll = new String[25];
    String[] name = new String[25];
    String result="";
    List<Integer> randimage = new ArrayList<>();
    List<Integer> randname = new ArrayList<>();
    Random random=new Random();

    public void doit()
    {

        ari = new int[25];

        i=0;

        DownloadImage task1 = new DownloadImage();

        Button[] arr = {button1, button2, button3, button4};
        btnnum=random.nextInt(4);

        try {
            myImage = task1.execute(urll[randimage.get(inimage)]).get();
            imageView.setImageBitmap(myImage);
            arr[btnnum].setText(name[randimage.get(inimage)]);
            crrctimg=randimage.get(inimage);
            inimage++;

            for(i=0;i<4;++i)
            {
                if(i!=btnnum) {
                    arr[i].setText(name[randname.get(inname)]);
                    inname++;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void check(View view)
    {
        String nam="";
        int k=0;
        if(view.getId()==R.id.op1)
            nam = button1.getText().toString();
        else if(view.getId()==R.id.op2)
            nam = button2.getText().toString();
        else if(view.getId()==R.id.op3)
            nam = button3.getText().toString();
        else if(view.getId()==R.id.op4)
            nam = button4.getText().toString();

        for(k=0;k<25;++k)
        {
            if(nam.equals(name[k]))
                break;
        }

        if(k==crrctimg)
            Toast.makeText(this,"Correct!",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"Wrong!",Toast.LENGTH_SHORT).show();



        doit();






    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {


            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;

                    result += current;

                    data = reader.read();
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();

                return "Failed";
            }
        }
        }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);
                URLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream in = urlConnection.getInputStream();
                Bitmap bitmap= BitmapFactory.decodeStream(in);

                return bitmap;

            } catch (Exception e) {
                e.printStackTrace();

                return null;
            }
        }
    }



        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            imageView = (ImageView) findViewById(R.id.imageView);
            button1 = (Button) findViewById(R.id.op1);
            button2 = (Button) findViewById(R.id.op2);
            button3 = (Button) findViewById(R.id.op3);
            button4 = (Button) findViewById(R.id.op4);


            DownloadTask task = new DownloadTask();
            String result = null;
            try {
                result = task.execute("http://www.posh24.se/kandisar").get();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Pattern p = Pattern.compile("img src=\"(.*?)\" alt=\"");
            Matcher m = p.matcher(result);

            while(m.find())
            {
                urll[i] = m.group(1);
                ++i;
            }

            i=0;

            Pattern p1 = Pattern.compile("\" alt=\"(.*?)\"/>");
            Matcher m1 = p1.matcher(result);

            while(m1.find())
            {
                name[i] = m1.group(1);
                ++i;
            }


            for (int i = 0; i < 25; i++)
            {
                while(true)
                {
                    Integer next = random.nextInt(25);
                    if (!randimage.contains(next))
                    {
                        randimage.add(next);
                        break;
                    }
                }
            }


            for (int i = 0; i < 25; i++)
            {
                while(true)
                {
                    Integer next = random.nextInt(25);
                    if (!randname.contains(next))
                    {
                        randname.add(next);
                        break;
                    }
                }
            }

            doit();
        }
}
