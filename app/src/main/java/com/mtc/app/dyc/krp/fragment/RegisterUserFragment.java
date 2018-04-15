package com.mtc.app.dyc.krp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.mtc.app.dyc.krp.R;
import com.mtc.app.dyc.krp.RegisterUserDetailActivity;
import com.mtc.app.dyc.krp.SignUpActivity;
import com.mtc.app.dyc.krp.Utils;
import com.mtc.app.dyc.krp.models.RegisterUser;
import com.mtc.app.dyc.krp.viewholder.RegisterUserViewHolder;

import java.util.Locale;
import java.util.Objects;

public class RegisterUserFragment extends Fragment {

    private static final String TAG = "RegisterUserFragment";

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<RegisterUser, RegisterUserViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private LinearLayout mButtonLayout;
    private TextView mCountRegister;

    public RegisterUserFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_users, container, false);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        mButtonLayout = rootView.findViewById(R.id.button_Register_layout);
        mButtonLayout.setOnClickListener(v -> {
            // Launch SignUpActivity
            startActivity(new Intent(getActivity(), SignUpActivity.class));
        });
        mRecycler = rootView.findViewById(R.id.users_messages_list);
        mRecycler.setHasFixedSize(true);
        mCountRegister = rootView.findViewById(R.id.count_of_members);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query registeredUsersQuery = getQuery(mDatabase);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<RegisterUser>()
                .setQuery(registeredUsersQuery, RegisterUser.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<RegisterUser, RegisterUserViewHolder>(options) {

            @NonNull
            @Override
            public RegisterUserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new RegisterUserViewHolder(inflater.inflate(R.layout.item_register_user, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull RegisterUserViewHolder viewHolder, int position, @NonNull final RegisterUser model) {
                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                Log.d(TAG, "postKey::" + postKey);
                viewHolder.bindToPost(model);
                viewHolder.itemView.setOnClickListener(v -> {
                    // Launch RegisterUserDetailActivity
                    Intent intent = new Intent(getActivity(), RegisterUserDetailActivity.class);
                    intent.putExtra(RegisterUserDetailActivity.EXTRA_POST_KEY, postKey);
                    startActivity(intent);
                });

                viewHolder.itemView.setOnDragListener((view, dragEvent) -> {
                    switch (dragEvent.getAction()) {
                        case DragEvent.ACTION_DRAG_ENDED:
                            mRecycler.removeView(view);
                            break;
                        default:

                    }
                    return false;
                });
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if (mCountRegister != null) {
                    mCountRegister.setText(String.format(Locale.US, "Member Count: %d", mAdapter.getItemCount()));
                }
            }
        };


        mRecycler.setAdapter(mAdapter);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    public Query getQuery(DatabaseReference databaseReference) {

        if (Utils.isAdmin(Objects.requireNonNull(getActivity()).getApplicationContext())) {
            return databaseReference
                    .child("adminRegister")
                    .orderByValue();
        } else {
            return databaseReference
                    .child("register")
                    .child(Utils.getUid())
                    .limitToFirst(100);
        }
    }

}
