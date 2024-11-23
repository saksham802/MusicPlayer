package com.sak.musicplayer.spotifyapi;

import com.sak.musicplayer.data.Artist;

public interface SelectListnerSpotify {


    void onItemClicked(SpotifyPlaylistResponse.Item item);
}
