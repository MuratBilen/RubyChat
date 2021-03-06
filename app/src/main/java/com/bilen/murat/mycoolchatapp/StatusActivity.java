package com.bilen.murat.mycoolchatapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class StatusActivity extends AppCompatActivity
{
	private Toolbar mToolbar;
	private TextInputLayout mStatus;
	private Button mSaveButton;
	//Firebase
	private DatabaseReference mStatusDatabase;
	private FirebaseUser mCurrentUser;

	private ProgressDialog mProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);

		//Firebase part
		mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
		String currentUserId = mCurrentUser.getUid();
		mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
		mToolbar = (Toolbar) findViewById(R.id.status_app_bar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle("Account Status");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		//Getting statusValue from the settings activity via bundle
		String statusValue = getIntent().getStringExtra("statusValue");

		mProgress = new ProgressDialog(this);
		mStatus = (TextInputLayout) findViewById(R.id.status_input);
		mStatus.getEditText().setText(statusValue);
		mSaveButton = (Button) findViewById(R.id.status_save_btn);

		mSaveButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				mProgress.setTitle("Saving Changes");
				mProgress.setMessage("Please wait while we save the changes");
				mProgress.show();
				String status = mStatus.getEditText().getText().toString();
				mStatusDatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>()
				{
					@Override
					public void onComplete(@NonNull Task<Void> task)
					{
						if (task.isSuccessful()) {
							mProgress.dismiss();
						} else {
							Toast.makeText(getApplicationContext(), "There was some error while saving", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});
	}
}
