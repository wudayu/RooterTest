package com.wudayu.rootertest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String TAG = "RooterTest MainActivity";

	Button btnRootIt;
	ProgressDialog progressDialog;

	boolean canRunRootCommands = false;

	@SuppressWarnings("rawtypes")
	AsyncTask currentTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnRootIt = (Button) findViewById(R.id.btn_root_it);

		btnRootIt.setOnClickListener(new BtnRootItOnClickListener());
	}

	class BtnRootItOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {

			currentTask = new AsyncTask<Void, Integer, Boolean>() {
				
				@Override
				protected void onPreExecute() {
					progressDialog = ProgressDialog.show(MainActivity.this, "Title",
							"Content", true, true, new DialogInterface.OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
								currentTask.cancel(true);
						}
					});

					super.onPreExecute();
				}

				@Override
				protected Boolean doInBackground(Void... params) {
					publishProgress(50);
					return ExecuteAsRootBase.canRunRootCommands();
				}

				@Override
				protected void onProgressUpdate(Integer... progress) {
					Log.i(TAG, "The progress now is " + progress + "%");

					super.onProgressUpdate(progress);
				}

				@Override
				protected void onCancelled(Boolean result) {
					Log.i(TAG, "Cancelled, result: " + result);

					super.onCancelled();
				}

				@Override
				protected void onPostExecute(Boolean result) {
					progressDialog.dismiss();
					canRunRootCommands = result;

					if (canRunRootCommands)
						Toast.makeText(MainActivity.this, "Root getted",
								Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(MainActivity.this, "Get permission failed",
								Toast.LENGTH_SHORT).show();
				}

			}.execute();
		}
	}

	@Override
	protected void onPause() {
		if (progressDialog != null)
			progressDialog.cancel();
		if (currentTask != null)
			currentTask.cancel(true);

		super.onPause();
	}

}
