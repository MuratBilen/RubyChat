package com.bilen.murat.mycoolchatapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity
{
	private Toolbar mToolbar;
	private RecyclerView mUsersList;
	private DatabaseReference allUserReference;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_users);
		mToolbar = (Toolbar) findViewById(R.id.users_appBar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle("All Users");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mUsersList = (RecyclerView) findViewById(R.id.users_list);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		linearLayoutManager.setReverseLayout(true);
		linearLayoutManager.setStackFromEnd(true);
		mUsersList.setLayoutManager(linearLayoutManager);
		// Query query = FirebaseDatabase.getInstance().getReference().child("Users").limitToLast(10);
		allUserReference = FirebaseDatabase.getInstance().getReference().child("Users");
		FirebaseRecyclerOptions<Users> options = new FirebaseRecyclerOptions.Builder<Users>().setQuery(allUserReference, Users.class).build();
		FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(options)
		{

			@NonNull
			@Override
			public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
			{
				Log.d("myCoolChatApp", "On create wiewholder started");
				View view = LayoutInflater.from(parent.getContext())
						.inflate(R.layout.users_single_layout, parent, false);
				Log.d("myCoolChatApp", "On create wiewholder return");
				return new UsersViewHolder(view);
			}

			@Override
			protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Users model)
			{
				Log.d("myCoolChatApp", model.getName());
				holder.name.setText(model.getName());
				holder.status.setText(model.getStatus());
				Log.d("myCoolChatApp", "On BindViewHolder wiew returned and texts were setted");
			}
		};
		mUsersList.setAdapter(firebaseRecyclerAdapter);
		firebaseRecyclerAdapter.startListening();

	}

	public static class UsersViewHolder extends RecyclerView.ViewHolder
	{
		TextView name;
		//CircleImageView image;
		TextView status;

		public UsersViewHolder(@NonNull View itemView)
		{
			super(itemView);
			name = itemView.findViewById(R.id.user_single_name);
			//image=itemView.findViewById(R.id.user_single_image);
			status = itemView.findViewById(R.id.user_single_status);
			Log.d("myCoolChatApp", "UserViewHolder name and status were matched with res id");

		}
	}
}
