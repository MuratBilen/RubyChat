package com.bilen.murat.mycoolchatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity
{
	private Toolbar mToolbar;
	private TextInputLayout mLoginEmail;
	private TextInputLayout mLoginPassword;
	private Button mLogin_btn;
	private ProgressDialog mLoginProgress;
	private FirebaseAuth mAuth;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mToolbar = (Toolbar) findViewById(R.id.login_toolbar);
		setSupportActionBar(mToolbar);


		Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Login");
		mLoginProgress = new ProgressDialog(this);

		mLoginEmail = (TextInputLayout) findViewById(R.id.login_email);
		mLoginPassword = (TextInputLayout) findViewById(R.id.login_password);
		mLogin_btn = (Button) findViewById(R.id.login_btn);
		mAuth = FirebaseAuth.getInstance();
		mLogin_btn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				String email = mLoginEmail.getEditText().getText().toString();
				String password = mLoginPassword.getEditText().getText().toString();
				if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
					Log.d("MyCoolChatApp", "Checking whether parameters are empty or not");
					mLoginProgress.setTitle("Logging In");
					mLoginProgress.setMessage("Please wait while we check your credentials.");
					mLoginProgress.setCanceledOnTouchOutside(false);
					mLoginProgress.show();
					loginUser(email, password);

				} else if (TextUtils.isEmpty(email)) {
					Toast.makeText(LoginActivity.this, "Please fill the email field", Toast.LENGTH_LONG).show();
				} else if (TextUtils.isEmpty(password)) {
					Toast.makeText(LoginActivity.this, "Please fill the email field", Toast.LENGTH_LONG).show();
				}

			}

		});


	}

	private void loginUser(String email, String password)
	{
		mAuth.signInWithEmailAndPassword(email, password)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
				{
					@Override
					public void onComplete(@NonNull Task<AuthResult> task)
					{
						if (task.isSuccessful()) {
							// Sign in success, update UI with the signed-in user's information
							mLoginProgress.dismiss();
							Log.d("MyCoolChatApp", "createUserWithEmail:success");
							Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
							mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
							startActivity(mainIntent);
							finish();
						} else {
							mLoginProgress.dismiss();
							// If sign in fails, display a message to the user.
							Log.d("MyCoolChatApp", "There is some sort of problem");
							String error = "";
							try {
								throw task.getException();
							} catch (FirebaseAuthInvalidUserException e) {
								error = "Invalid Email!";
							} catch (FirebaseAuthInvalidCredentialsException e) {
								error = "Invalid Password!";
							} catch (FirebaseNetworkException e) {
								error = "error_message_failed_sign_in_no_network";
							} catch (Exception e) {
								error = "Default error!";
								e.printStackTrace();
							}
							Log.d("MyCoolChatApp", error);
							Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
						}

					}
				});


	}
}
