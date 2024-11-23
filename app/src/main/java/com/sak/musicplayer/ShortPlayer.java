package com.sak.musicplayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ShortPlayer extends Fragment {

    ViewPager2 viewPager2;
    List<ShortVideo> shortPlayers;
    ShortVideoAdapter adapter;

    public ShortPlayer() {

    }



    public static ShortPlayer newInstance(String param1, String param2) {
        ShortPlayer fragment = new ShortPlayer();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_short_player, container, false);

        shortPlayers = new ArrayList<>();
        viewPager2 = view.findViewById(R.id.videopager);

        // Populate the list with a sample video or real data
        shortPlayers.add(new ShortVideo("Feel the Beat 🔥", "Turn up the volume and get lost in the rhythm. This song is bound to get you moving! #DanceVibes #FeelTheBeat", "android.resource://" + getActivity().getPackageName() + "/" + R.raw.e));
        shortPlayers.add(new ShortVideo("Late Night Drive 🎧", "Hit the road and vibe to this perfect soundtrack for your midnight drive. #NightDrive #MusicJourney", "android.resource://" + getActivity().getPackageName() + "/" + R.raw.d));
        shortPlayers.add(new ShortVideo("On Repeat ➿", "This song’s so catchy, you’ll want to play it again and again. Let the music take over. #OnRepeat #CatchyBeats", "android.resource://" + getActivity().getPackageName() + "/" + R.raw.c));
        shortPlayers.add(new ShortVideo("Heartfelt Moments ❤️", "A song that speaks to the heart. Perfect for those emotional moments when the music says it all. #EmotionalVibes #HeartfeltMusic", "android.resource://" + getActivity().getPackageName() + "/" + R.raw.b));
        shortPlayers.add(new ShortVideo("Escape Into The Music 🎶️", "Let the melody take you to a new world. Press play and drift away into the music. #MusicEscape #VibeWithTheBeat", "android.resource://" + getActivity().getPackageName() + "/" + R.raw.a));

        adapter = new ShortVideoAdapter(shortPlayers);
        viewPager2.setAdapter(adapter);

        return view;  // Return the inflated view
    }

}