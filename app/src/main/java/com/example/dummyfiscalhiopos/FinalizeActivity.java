package com.example.dummyfiscalhiopos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class FinalizeActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Intent resultIntent = new Intent(getIntent().getAction());
		setResult(RESULT_OK, resultIntent);
		finish();
	}

}
