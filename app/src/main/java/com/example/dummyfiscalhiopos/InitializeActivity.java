package com.example.dummyfiscalhiopos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.dummyfiscalhiopos.utils.APIUtils;
import com.example.dummyfiscalhiopos.utils.Cide;

import java.util.Properties;


public class InitializeActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Properties properties = APIUtils.parseInitializeParameters(getIntent().getStringExtra("Parameters"));
		onInitialize(properties);
	}

	protected void onInitialize(Properties cloudLicenseProperties) {
		Cide cide = new Cide(this, this);

		for (Object objKey : cloudLicenseProperties.keySet()) {
			String key = (String) objKey;
			System.out.println("CLOUD LICENSE PROPERTY " + key + ": VALUE = " + cloudLicenseProperties.getProperty(key));

			switch (key){
				case "1":
					cide.cacheGuardaString("URLAPI", cloudLicenseProperties.getProperty(key));
					break;
			}
		}

		finishInitializeOK();
	}

	protected void finishInitializeOK() {
		Intent resultItent = new Intent(getIntent().getAction());
		setResult(RESULT_OK, resultItent);
		finish();
	}
}
