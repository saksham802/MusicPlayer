package com.sak.musicplayer;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {
    TextView registerpage,rest;
    TextInputEditText email,password;
    FirebaseAuth firebaseAuth;
    String emailid,passwordtxt,restemail;
    Button login,restbtn;
    EditText emailofrest;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        registerpage=findViewById(R.id.gotoregister);
        email=findViewById(R.id.emaillogin);
        password=findViewById(R.id.passwordlogin);
        rest=findViewById(R.id.resetpassword);
        firebaseAuth=FirebaseAuth.getInstance();
        login=findViewById(R.id.loginbtn);
        Dialog dialog=new Dialog(LoginPage.this);
        if (firebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginPage.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setContentView(R.layout.dialogboxrest);
                dialog.setCancelable(false);
                emailofrest=dialog.findViewById(R.id.restpass);
                restbtn=dialog.findViewById(R.id.reststart);
                imageView=dialog.findViewById(R.id.imageView3);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                restbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        restemail=emailofrest.getText().toString().trim();
                        if(restemail.isEmpty()){ emailofrest.setError("Enter Email Id");
                        return ;
                        }
                        firebaseAuth.sendPasswordResetEmail(restemail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(LoginPage.this, "Email Sent", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginPage.this, "Error email not send", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dialog.show();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailid=email.getText().toString().trim();
                passwordtxt=password.getText().toString().trim();
                if(emailid.isEmpty()||passwordtxt.isEmpty()){
                    if(emailid.isEmpty()) email.setError("Enter Email ID");
                    if(passwordtxt.isEmpty()) password.setError("Enter Password");
                }
                if(passwordtxt.length()<6) password.setError("Enter correct password");
                firebaseAuth.signInWithEmailAndPassword(emailid,passwordtxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(LoginPage.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginPage.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(LoginPage.this, "Failed login Make sure your information is correct", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        registerpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginPage.this,RegisterNow.class);
                startActivity(intent);
                finish();
            }
        });
    }
}