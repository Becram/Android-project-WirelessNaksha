package com.becram.wirelessnakshav1;

import android.os.Bundle;

public class Visitsite_Activity extends DashBoardActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_site);
		setHeader(getString(R.string.VisitActivityTitle), true, true);
	}

}
