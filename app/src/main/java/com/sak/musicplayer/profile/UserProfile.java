package com.sak.musicplayer.profile;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sak.musicplayer.R;
import com.sak.musicplayer.data.Userdata;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    Button editButton, saveButton;
    EditText nameEditText, emailEditText, phoneEditText;
    TextView finalNameTextView, finalPhoneTextView, finalEmailTextView;
    ImageView backbtn2;
    String username, userPhone, userEmail;
    CircleImageView circleImageView, profilephoto;
    private static final int REQUEST_CODE_PERMISSIONS = 100;
    private Uri selectedImageUri;
    StorageReference storageReference;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        finalNameTextView = findViewById(R.id.fullname);
        finalEmailTextView = findViewById(R.id.editemailid);
        finalPhoneTextView = findViewById(R.id.editphoneno);
        editButton = findViewById(R.id.editbtn);
        profilephoto = findViewById(R.id.imageView7);
        backbtn2 = findViewById(R.id.imageView13);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Userdata userdata = snapshot.getValue(Userdata.class);
                        if (userdata != null) {
                            String name = userdata.getName();
                            String email = userdata.getEmail();
                            String phone = userdata.getUsername();

                            finalNameTextView.setText(name);
                            finalEmailTextView.setText(email);
                            finalPhoneTextView.setText(phone);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(UserProfile.this, "Error fetching data!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        editButton.setOnClickListener(view -> showEditDialog());
    }

    private void showEditDialog() {
        final Dialog dialog = new Dialog(UserProfile.this);
        dialog.setContentView(R.layout.inputdialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        ImageView closeButton = dialog.findViewById(R.id.close);
        saveButton = dialog.findViewById(R.id.button2);
        nameEditText = dialog.findViewById(R.id.editTextText);
        phoneEditText = dialog.findViewById(R.id.editTextText2);
        emailEditText = dialog.findViewById(R.id.editTextText3);
        circleImageView = dialog.findViewById(R.id.userimg);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        }

        circleImageView.setOnClickListener(view -> openFilePicker());

        closeButton.setOnClickListener(view -> dialog.dismiss());

        saveButton.setOnClickListener(view -> {
            username = nameEditText.getText().toString();
            userPhone = phoneEditText.getText().toString();
            userEmail = emailEditText.getText().toString();

            if (username.isEmpty() || userPhone.isEmpty() || userEmail.isEmpty()) {
                Toast.makeText(UserProfile.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedImageUri != null) {
                storageReference = FirebaseStorage.getInstance()
                        .getReference("profile_images/" + firebaseUser.getUid() + ".jpg");

                storageReference.putFile(selectedImageUri)
                        .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    imageUrl = uri.toString();
                                    updateUserData(username, userPhone, userEmail, imageUrl);
                                }));
            } else {
                imageUrl = ""; // If no image is selected, set an empty string
                updateUserData(username, userPhone, userEmail, imageUrl);
            }

            dialog.dismiss();
        });

        dialog.show();
    }

    private void updateUserData(String username, String userPhone, String userEmail, String imageUrl) {
        HashMap<String, Object> userdata = new HashMap<>();
        userdata.put("name", username);
        userdata.put("username", userPhone);
        userdata.put("email", userEmail);
        userdata.put("imageUrl", imageUrl);

        reference.updateChildren(userdata).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(UserProfile.this, "Data Updated successfully", Toast.LENGTH_SHORT).show();
                finalNameTextView.setText(username.toUpperCase());
                finalPhoneTextView.setText(userPhone);
                finalEmailTextView.setText(userEmail);

                // If image is updated, load it
                if (!imageUrl.isEmpty()) {
                    Glide.with(UserProfile.this)
                            .load(imageUrl)
                            .into(profilephoto);
                }
            } else {
                Toast.makeText(UserProfile.this, "Error updating data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data != null) {
                selectedImageUri = data.getData();
                Glide.with(this).load(selectedImageUri).into(circleImageView);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFilePicker();
            } else {
                Toast.makeText(this, "Permission denied. Cannot access images.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
