package com.sak.musicplayer;

import com.sak.musicplayer.spotifysearchapi.SearchResponse;

public interface SelectListnerSearch {
    void onItemClick(SearchResponse.Track track);
}
