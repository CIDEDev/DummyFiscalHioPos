package com.example.dummyfiscalhiopos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class BehaviorActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setBehaviorResult();
	}

	protected void setBehaviorResult()
	{
		Intent resultIntent = new Intent(getIntent().getAction());

		resultIntent.putExtra("canPrintSale", true);
		resultIntent.putExtra("CanRegisterCopy", true);
		resultIntent.putExtra("canPrintCopy", true);
		resultIntent.putExtra("canOpenCashdrawer", false);
		resultIntent.putExtra("canRegisterSubtotal", false);
		resultIntent.putExtra("canPrintSubtotal", false);
		resultIntent.putExtra("canPrintXReport", false);
		resultIntent.putExtra("canPrintZReport", false);
		resultIntent.putExtra("canRegisterCashin", false);
		resultIntent.putExtra("canRegisterCashout", false);
		resultIntent.putExtra("canRegisterUserLogin", true);
		resultIntent.putExtra("canRegisterUserLogout", false);
		resultIntent.putExtra("canReadCustomerData", false);
		resultIntent.putExtra("CanAudit", false);
		resultIntent.putExtra("CanRegisterDailyCashCount", false);
		resultIntent.putExtra("CanRegisterMonthlyCashCount", false);

		setResult(RESULT_OK, resultIntent);
		finish();
	}

}
