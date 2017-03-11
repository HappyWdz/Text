package com.example.administrator.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anroidlib.Utils;

public class TextActivity extends AppCompatActivity {
    private static final String TAG = "TextActivity";

    private static int sInt;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.tv_text);
       // mTextView.setVisibility(View.GONE);
        Log.d(TAG, "onCreate: text");

        tost();

        hello();
    }

    private void tost() {
        Toast.makeText(this, "ahhah", Toast.LENGTH_SHORT).show();
        Utils.text();
//        MyClass.textJavaLib();
    }

    private void hello() {
        HelloAS helloAS =new HelloASIpl();
        helloAS.haha();
    }

    public void text(){

    }
}
