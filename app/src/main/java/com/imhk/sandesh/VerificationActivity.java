package com.imhk.sandesh;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VerificationActivity extends AppCompatActivity {

    EditText number_text;
    Button next_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        number_text=findViewById(R.id.numberText);
        next_btn=findViewById(R.id.nextbutton);


        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String number = number_text.getText().toString();

                if(number.length()<10) {
                    number_text.setError("enter number correctly");
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
                //builder.setTitle("Notice");
                builder.setMessage("We will verify the phone number:\n\n"+"+91 "+number+"\n\nIs this OK, or would you edit the number?");
                builder.setCancelable(false);

                // add the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(VerificationActivity.this, "verifying....", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(VerificationActivity.this,OtpActivity.class);
                        intent.putExtra("mobile",number);
                        startActivity(intent);
                    }
                });
                builder.setNeutralButton("EDIT", null);

                // create and show the alert dialog
                builder.create();
                builder.show();
            }
        });

    }
}
