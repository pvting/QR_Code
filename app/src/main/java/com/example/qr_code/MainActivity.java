package com.example.qr_code;


import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener{
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(MainActivity.this,BarcodeActivity.class);
		startActivity(intent);
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		findViewById(R.id.button).setOnClickListener(this);
    }

}
