package com.imhk.sandesh.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imhk.sandesh.Model.User;
import com.imhk.sandesh.R;

public class ProfileFragment extends Fragment {

    TextView name;
    TextView phone;
    ImageView profile;

    DatabaseReference myRef;
    FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_profile, container, false);

        name=view.findViewById(R.id.name2);
        phone=view.findViewById(R.id.nmbr2);
        profile=view.findViewById(R.id.image_profile);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        myRef= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getPhoneNumber());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                name.setText(user.getName());
                phone.setText(user.getMobile());
                if (user.getImageURL().equals("null")) {

                }
                else
                    Glide.with(getContext()).load(user.getImageURL()).into(profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}
