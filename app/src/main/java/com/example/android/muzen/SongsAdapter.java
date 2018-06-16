/*
 * Copyright (C) 2018
 */
package com.example.android.muzen;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/*
 * {@link SongsAdapter} is an {@link ArrayAdapter} that can provide the layout for each list
 * based on a data source, which is a list of {@link Songs} objects.
 * */
public class SongsAdapter extends ArrayAdapter<Songs> {

    private int resourceID;

    /**
     * Custom constructor
     *
     * @param context The current context. Used to inflate the layout file.
     * @param songs   A List of Songs objects to display in a list
     */
    public SongsAdapter(Activity context, int resource, ArrayList<Songs> songs) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        super(context, resource, songs);

        //Grab the layout id and set it equal to the global variable resourceID
        resourceID = resource;
    }

    /**
     * Provides a view for an AdapterView
     *
     * @param position    The position in the list of data that should be displayed in the
     *                    list item view.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    resourceID, parent, false);

        }

        //todo add category view to narrow and wide layouts, then add category instructions here
        // Get the {@link Songs} object located at this position in the list
        Songs currentSongs = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID song_name
        TextView songNameTextView = (TextView) listItemView.findViewById(R.id.song_name);

        // Find the TextView in the list_item.xml layout with the ID artist_name
        TextView artistNameTextView = (TextView) listItemView.findViewById(R.id.artist_name);

        // Find the ImageView in the list_item.xml layout with the ID album_art_icon
        ImageView albumArtView = (ImageView) listItemView.findViewById(R.id.album_art_icon);

        // Check whether the currentSongs object created above is null before preceding with the following methods
        if (currentSongs != null) {
            // Get the song name from the current Songs object and
            // set this text on the name TextView
            songNameTextView.setText(currentSongs.getSongName());

            // Get the artist name from the current Songs object and
            // set this text on the number TextView
            artistNameTextView.setText(currentSongs.getArtistName());

            // Get the image resource ID from the current Songs object and
            // set the image to iconView
            albumArtView.setImageResource(currentSongs.getImageID());
        }

        // Return the list item layout with the 3 views (2 TextViews, 1 ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }
}
