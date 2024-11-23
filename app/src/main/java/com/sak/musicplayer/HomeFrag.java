package com.sak.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sak.musicplayer.adapter.ArtistAdapter;
import com.sak.musicplayer.adapter.FlavourAdapter;
import com.sak.musicplayer.adapter.GoldenEraAdapter;
import com.sak.musicplayer.adapter.PodCastAdapter;
import com.sak.musicplayer.adapter.TopchartAdapter;
import com.sak.musicplayer.data.Artist;
import com.sak.musicplayer.data.Flavours;
import com.sak.musicplayer.data.GoldenEra;
import com.sak.musicplayer.data.Podcast;
import com.sak.musicplayer.data.Topcharts;
import com.sak.musicplayer.data.Userdata;
import com.sak.musicplayer.listner.SelectListener;
import com.sak.musicplayer.listner.SelectListnerArtrist;
import com.sak.musicplayer.listner.SelectListnerGolden;
import com.sak.musicplayer.listner.SelectListnerPodcast;
import com.sak.musicplayer.listner.SelectListnterFlavour;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeFrag extends Fragment implements SelectListener, SelectListnerArtrist, SelectListnterFlavour, SelectListnerGolden,SelectListnerPodcast {

    private RecyclerView recyclerView, recyclerView2, recyclerView3,recyclerView4,recyclerView5;
    private FirebaseFirestore firestore;
    private TopchartAdapter adapter;
    private FlavourAdapter flavourAdapter;
    private ArrayList<Topcharts> topchartsList;
    private ArrayList<Flavours> flavoursArrayList;
    private ArrayList<Artist> artists;
    private ArtistAdapter artistAdapter;
    private PodCastAdapter podCastAdapter;
    private ArrayList<Podcast> podcastArrayList;
    private GoldenEraAdapter goldenEraAdapter;
    private ArrayList<GoldenEra> goldenEraArrayList;
    private ScrollView scrollView;
    private ImageView searchbutton;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    RelativeLayout musicnav;
    int check;
    TextView songname,usernameof;
    ImageView songimg;

    public HomeFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        topchartsList = new ArrayList<>();
        flavoursArrayList = new ArrayList<>();
        podcastArrayList = new ArrayList<>();
        goldenEraArrayList= new ArrayList<>();
        artists = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        scrollView = view.findViewById(R.id.scroll); // Initialize ScrollView here
        searchbutton=view.findViewById(R.id.searchpage);
        musicnav=view.findViewById(R.id.musicnav);
        songname=view.findViewById(R.id.songname);
        songimg=view.findViewById(R.id.songimg);
        usernameof=view.findViewById(R.id.UsertextView);
        musicnav.setVisibility(View.GONE);
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
                      usernameof.setText(username+"👋👋");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        }
        Bundle args = getArguments();

        if(args!=null){
            check= args.getInt("play");
        if(check==1){
        musicnav.setVisibility(View.VISIBLE);
        songname.setText(args.getString("Name"));
            Toast.makeText(getActivity(), args.getString("Url"), Toast.LENGTH_SHORT).show();
            Picasso.get().load(args.getString("Url")).into(songimg);
        }}

        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getActivity(),SearchPage.class);
                startActivity(i);

            }
        });
        // Initialize RecyclerViews
        setupRecyclerViews(view);

        // Load data from Firebase
        loadTopCharts();
        loadFlavour();
       loadArtist();
        loadPodcast();  // Load podcasts as well
        loadGolden();
        return view;
    }

    private void setupRecyclerViews(View view) {
        // Setup Topchart RecyclerView
        recyclerView = view.findViewById(R.id.topchartrecycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        adapter = new TopchartAdapter(getContext(), topchartsList, this);
        recyclerView.setAdapter(adapter);

        // Setup Flavour RecyclerView
        recyclerView2 = view.findViewById(R.id.flavourrecycleview);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setHasFixedSize(true);
        flavourAdapter = new FlavourAdapter(getContext(), flavoursArrayList, (SelectListnterFlavour) this);
        recyclerView2.setAdapter(flavourAdapter);

        // Setup Artist RecyclerView
        recyclerView3 = view.findViewById(R.id.artist_recycle_view);
        recyclerView3.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView3.setHasFixedSize(true);
        artistAdapter = new ArtistAdapter(getContext(), artists,this);
        recyclerView3.setAdapter(artistAdapter);

        recyclerView4=view.findViewById(R.id.podcast);
        recyclerView4.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView4.setHasFixedSize(true);
        podCastAdapter=new PodCastAdapter(getContext(),podcastArrayList,this);
        recyclerView4.setAdapter(podCastAdapter);

        recyclerView5=view.findViewById(R.id.golden);
        recyclerView5.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView5.setHasFixedSize(true);
        goldenEraAdapter=new GoldenEraAdapter(getContext(),goldenEraArrayList,this);
        recyclerView5.setAdapter(goldenEraAdapter);
    }

    private void loadTopCharts() {
        CollectionReference topChartsRef = firestore.collection("topchart");
        topChartsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    topchartsList.clear(); // Clear the previous data
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Topcharts topchart = document.toObject(Topcharts.class);
                        topchartsList.add(topchart);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("HomeFrag", "Error getting top charts: ", task.getException());
                    Toast.makeText(getContext(), "Failed to load top charts.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void loadPodcast() {
        CollectionReference podcastRef = firestore.collection("podcast");
        podcastRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Clear any previous podcast data
                    podcastArrayList.clear();

                    // Add the new podcasts to the array
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Podcast podcast = document.toObject(Podcast.class);
                        podcastArrayList.add(podcast);
                    }

                    // Notify the adapter to refresh the RecyclerView
                    podCastAdapter.notifyDataSetChanged();
                } else {
                    Log.e("HomeFrag", "Error getting podcast: ", task.getException());
                    Toast.makeText(getContext(), "Failed to load podcasts", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void loadGolden() {
        CollectionReference goldenRef = firestore.collection("golden");
        goldenRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Clear previous golden era data
                    goldenEraArrayList.clear();

                    // Loop through the documents and convert them into GoldenEra objects
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        GoldenEra goldenEra = document.toObject(GoldenEra.class);
                        goldenEraArrayList.add(goldenEra);
                    }

                    // Notify the adapter to refresh the RecyclerView
                    goldenEraAdapter.notifyDataSetChanged();
                } else {
                    Log.e("HomeFrag", "Error getting golden era data: ", task.getException());
                    Toast.makeText(getContext(), "Failed to load golden era data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void loadFlavour() {
        CollectionReference flavourRef = firestore.collection("flavour");
        flavourRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    flavoursArrayList.clear(); // Clear previous data
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Flavours flavours = document.toObject(Flavours.class);
                        flavoursArrayList.add(flavours);
                    }
                    flavourAdapter.notifyDataSetChanged();
                } else {
                    Log.e("HomeFrag", "Error getting flavours: ", task.getException());
                    Toast.makeText(getContext(), "Failed to load flavours.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadArtist() {
        CollectionReference artistRef = firestore.collection("artist");
        artistRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    artists.clear(); // Clear previous data
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Artist artist = document.toObject(Artist.class);
                        artists.add(artist);
                    }
                    artistAdapter.notifyDataSetChanged();
                } else {
                    Log.e("HomeFrag", "Error getting artists: ", task.getException());
                    Toast.makeText(getContext(), "Failed to load artists.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemClicked(Topcharts topcharts) {
       /* topchartoption(topcharts.getName(),topcharts.getCoverUrl());*/
        Bundle  bundle = new Bundle();
            bundle.putString("Name",topcharts.getName());
            bundle.putString("topimg",topcharts.getCoverUrl());
        MusicPlaylist musicPlaylist= new MusicPlaylist();
        musicPlaylist.setArguments(bundle);
        FragmentTransaction transaction= getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.main, musicPlaylist);
        transaction.addToBackStack(null); // Add transaction to back stack
        transaction.commit();

        /*if (scrollView != null) {
            scrollView.setVisibility(View.GONE); // Hide ScrollView
        }*/
    }

    @Override
    public void onItemClicked(Artist artist) {
        Bundle  bundle = new Bundle();
        bundle.putString("Name",artist.getName());
        bundle.putString("topimg",artist.getCoverUrl());
        MusicPlaylist musicPlaylist= new MusicPlaylist();
        musicPlaylist.setArguments(bundle);
        FragmentTransaction transaction= getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.main, musicPlaylist);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onItemClicked(Flavours flavours) {
        Bundle  bundle = new Bundle();
        bundle.putString("Name",flavours.getName());
        bundle.putString("topimg",flavours.getCoverUrl());
        MusicPlaylist musicPlaylist= new MusicPlaylist();
        musicPlaylist.setArguments(bundle);
        FragmentTransaction transaction= getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.main, musicPlaylist);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onItemClicked(Podcast podcast) {
        Bundle  bundle = new Bundle();
        bundle.putString("Name",podcast.getName());
        bundle.putString("topimg",podcast.getCoverUrl());
        MusicPlaylist musicPlaylist= new MusicPlaylist();
        musicPlaylist.setArguments(bundle);
        FragmentTransaction transaction= getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.main, musicPlaylist);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onItemClick(GoldenEra goldenEra) {
        Bundle  bundle = new Bundle();
        bundle.putString("Name",goldenEra.getName());
        bundle.putString("topimg",goldenEra.getCoverUrl());
        MusicPlaylist musicPlaylist= new MusicPlaylist();
        musicPlaylist.setArguments(bundle);
        FragmentTransaction transaction= getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.main, musicPlaylist);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
