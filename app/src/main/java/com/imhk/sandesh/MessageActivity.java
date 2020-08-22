package com.imhk.sandesh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imhk.sandesh.Adapter.MessageAdapter;
import com.imhk.sandesh.Model.Chat;
import com.imhk.sandesh.Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    TextView name;
    Toolbar toolbar;
    EditText sendText;
    ImageButton sendButton;
    ImageView pImage;

    FrameLayout arrowBack;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;

    DatabaseReference myRef;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        name=findViewById(R.id.username);
        arrowBack=findViewById(R.id.arror_back);
        sendButton=findViewById(R.id.btn_send);
        sendText=findViewById(R.id.text_send);
        pImage=findViewById(R.id.profileimageview);

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent=getIntent();
        final String number=intent.getStringExtra("number");

        myRef = FirebaseDatabase.getInstance().getReference("Users").child(number);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=sendText.getText().toString().trim();
                if (!msg.isEmpty()) {
                    sendMessage(firebaseUser.getPhoneNumber(),number,msg);
                }
                else
                    Toast.makeText(MessageActivity.this, "Can't send empty message", Toast.LENGTH_SHORT).show();

                sendText.setText("");
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                name.setText(user.getName());
                if (user.getImageURL().equals("null")) {

                }
                else
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(pImage);

                readMessages(firebaseUser.getPhoneNumber(),number);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender,String receiver,String message) {
        myRef=FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object>hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        myRef.child("Chats").push().setValue(hashMap);
    }

    private void readMessages(final String myNumber,final String userNumber) {

        mChat=new ArrayList<>();

        myRef=FirebaseDatabase.getInstance().getReference("Chats");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);

                    if (chat.getReceiver().equals(myNumber) && chat.getSender().equals(userNumber) ||
                            chat.getReceiver().equals(userNumber) && chat.getSender().equals(myNumber)) {
                        mChat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mChat);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        myRef.child(userNumber+myNumber).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mChat.clear();
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Chat chat = snapshot.getValue(Chat.class);
//                    mChat.add(chat);
//
//                    messageAdapter = new MessageAdapter(MessageActivity.this, mChat);
//                    recyclerView.setAdapter(messageAdapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    private void status (String status) {
        myRef = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getPhoneNumber());

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        myRef.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}
