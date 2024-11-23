package com.sak.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sak.musicplayer.data.Userdata;

public class RegisterNow extends AppCompatActivity {

    TextView loginpage;
    TextInputEditText name, email, password, repassword, username;
    FirebaseAuth firebaseAuth;
    Button register;
    String emailid, username_text, password_text, repassword_text, fullname_text;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_now);

        // Initializing Views
        loginpage = findViewById(R.id.gotologin);
        name = findViewById(R.id.fullnametext);
        email = findViewById(R.id.emailtext);
        password = findViewById(R.id.passwordtext);
        repassword = findViewById(R.id.repasswordtext);
        username = findViewById(R.id.usernametext);
        firebaseAuth = FirebaseAuth.getInstance();
        register = findViewById(R.id.registerbtn);

        // Check if the user is already logged in


        // Register Button Click Listener
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the user input values
                emailid = email.getText().toString().trim();
                username_text = username.getText().toString().trim();
                password_text = password.getText().toString().trim();
                repassword_text = repassword.getText().toString().trim();
                fullname_text = name.getText().toString().trim();
                reference = FirebaseDatabase.getInstance().getReference("Users");
                // Input validation
                if (emailid.isEmpty() || fullname_text.isEmpty() || repassword_text.isEmpty() || password_text.isEmpty() || username_text.isEmpty()) {
                    // Display errors for empty fields
                    if (emailid.isEmpty()) email.setError("Enter email");
                    if (username_text.isEmpty()) username.setError("Enter Username");
                    if (repassword_text.isEmpty()) repassword.setError("Password does not match");
                    if (password_text.isEmpty()) password.setError("Enter Password");
                    if (fullname_text.isEmpty()) name.setError("Enter Name");
                } else if (password_text.length() < 6) {
                    password.setError("Password must have length of at least 6 characters");
                } else if (!password_text.equals(repassword_text)) {
                    repassword.setError("Password Does Not Match");
                } else {
                    // If all inputs are valid, proceed with registration
                    firebaseAuth.createUserWithEmailAndPassword(emailid, password_text)
                            .addOnCompleteListener(RegisterNow.this, new OnCompleteListener<AuthResult>() {
                                @Override


                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Registration success
                                        Userdata user = new Userdata(emailid,fullname_text, username_text);
                                        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                                        reference.child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                                        firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(RegisterNow.this, "Verification email is sent", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegisterNow.this, "Error Email not sent", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        Toast.makeText(RegisterNow.this, "Account Created", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterNow.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // Registration failed
                                        Toast.makeText(RegisterNow.this, "Failed to Create. Try Again Later", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        // Navigate to Login Page
        loginpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterNow.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
