package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    public static final int HEADER_SIZE = 10;
    public static final int PORT = 1234;
    public static final String HOST = "192.168.0.102";


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String get_message(String text) {
        StringBuilder header = new StringBuilder("" + text.length());
        while (header.length() <= HEADER_SIZE) {
            header.append(' ');
        }
        header.append(text);
        return header.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static String getData(Bitmap image) {
        StringBuilder builder = new StringBuilder();
        int w = image.getWidth();
        int h = image.getHeight();
        builder.append(w + "").append(" ").append(h + "").append("\n");
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int cur = image.getPixel(i, j) & 0xffffff;
                builder.append(cur).append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private static final int Select = 100;
    ImageView image;
    Button buttonGetImage;
    Button buttonParse;
    TextView textView;

    @SuppressLint("WrongThread")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = findViewById(R.id.image);
        buttonGetImage = findViewById(R.id.buttonGetImage);
        buttonParse = findViewById(R.id.buttonParse);
        textView = findViewById(R.id.textView);

        buttonGetImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*"); // change the image to the file you want.
            startActivityForResult(intent, Select);
        });

        buttonParse.setOnClickListener(v -> {
            textView.setText("");
            Main main = new Main();

            System.out.println("Here");
            Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
            String data = getData(bitmap);
            String message = get_message(data);
            System.out.println("Main started");
            try {
                String result = main.sendMessage(message);
                System.out.println(result);
                if (result.length() > 10) {
                    result = result.substring(25);
                }
                textView.setText(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Select) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                Uri select = data.getData();
                InputStream inputStream = null;

                try {
                    assert select != null;
                    inputStream = getContentResolver().openInputStream(select);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                BitmapFactory.decodeStream(inputStream);
                image.setImageURI(select);
            }
        }
    }


}
