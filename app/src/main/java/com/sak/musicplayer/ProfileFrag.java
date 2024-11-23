package com.sak.musicplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sak.musicplayer.data.Userdata;
import com.sak.musicplayer.profile.MyMusicPage;
import com.sak.musicplayer.profile.Settings;
import com.sak.musicplayer.profile.UserProfile;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFrag extends Fragment {
    ImageView logout;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    String userid,imgurl;
    CircleImageView circleImageView;
    TextView verify,nameofuser;
    public ProfileFrag() {
        // Required empty public constructor
    }

    public static ProfileFrag newInstance(String param1, String param2) {
        ProfileFrag fragment = new ProfileFrag();
        Bundle args = new Bundle();
        // You can put parameters into the bundle here if needed
        // args.putString("param1", param1);
        // args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Handle any initialization here if needed
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ConstraintLayout profile = view.findViewById(R.id.profile);
        ConstraintLayout setting = view.findViewById(R.id.setting);
        ConstraintLayout help = view.findViewById(R.id.helpsupport);
        ConstraintLayout logout= view.findViewById(R.id.logout);
        nameofuser=view.findViewById(R.id.profilename);
        verify=view.findViewById(R.id.verification);
        circleImageView=view.findViewById(R.id.profileimg);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser !=null){
            String userId = firebaseUser.getUid(); // Get the UID of the current user
            reference= FirebaseDatabase.getInstance().getReference("Users").child(userId);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Userdata userdata=snapshot.getValue(Userdata.class);
                        if(userdata!=null){

                            String username = userdata.getUsername();
                            nameofuser.setText("Hi "+username);
                            String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                            nameofuser.setText("Hi " + username);

                            Glide.with(getActivity()).load(imageUrl).into(circleImageView);



                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

        firebaseAuth=FirebaseAuth.getInstance();
        userid=firebaseAuth.getCurrentUser().getUid();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(!firebaseUser.isEmailVerified()){
            verify.setText("User Not Verified Please Verify");
        }


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
               Intent intent=new Intent(getActivity(),LoginPage.class);
               startActivity(intent);
                Toast.makeText(getActivity(), "Logout Succesfully", Toast.LENGTH_SHORT).show();

            }
        });

        ConstraintLayout mymusic = view.findViewById(R.id.music);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toprofile();
            }
        });
        mymusic.setClickable(false);
        mymusic.setEnabled(false);
        mymusic.setAlpha(0.5f);

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setting();

            }
        });
        return view;
    }
    public void call() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:9289067090"));
        startActivity(callIntent);
    }
    public void setting(){
        Intent intent = new Intent(getActivity(), Settings.class);
        startActivity(intent);
    }
    public void musicpage(){
        Intent intent=new Intent(getActivity(), MyMusicPage.class);
        startActivity(intent);
    }
    public void toprofile(){
        Intent intent=new Intent(getActivity(), UserProfile.class);
        startActivity(intent);
    }

}
