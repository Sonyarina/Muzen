package com.example.android.muzen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MeditationSongs extends AppCompatActivity {

    TextView nowPlayingSongNameTextView;
    TextView nowPlayingArtistNameTextView;
    ImageView nowPlayingAlbumArtImageView;
    //ImageView that holds the Play-Pause Button
    ImageView playButton;
    //Shared Preferences
    SharedPreferences sharedPreferences;
    // Now Playing Song Name and corresponding TextView
    private String nowPlayingSongName;
    //Now Playing Artist Name and corresponding TextView
    private String nowPlayingArtistName;
    //String keeps track of category
    private String nowPlayingCategory;
    //Now Playing Album Art ImageID and corresponding ImageView
    private int nowPlayingAlbumArt = R.drawable.namaste;
    //Position of currentSong in array
    private int randomSongsCurrentPosition = 6;
    private int categoryArrayCurrentPosition = 3;
    //Boolean keeps track of whether music is playing or not
    private boolean isPlaying;
    //Boolean keeps track of the playlist type
    private boolean isCategoryArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songs_list);

        //Find the views for Now Playing Area
        nowPlayingSongNameTextView = findViewById(R.id.song_name);
        nowPlayingArtistNameTextView = findViewById(R.id.artist_name);
        nowPlayingAlbumArtImageView = findViewById(R.id.album_art_icon);
        playButton = findViewById(R.id.now_playing_play_button);

        //Initialize category
        nowPlayingCategory = getString(R.string.song_category_1);

        //Retrieve intent data
        //Intent Key names and data types:

                /*  String	NowPlayingSongName
                    String	NowPlayingArtistName
                    String	NowPlayingCategory
                    boolean	isPlaying
                    boolean	isCategoryArray (will be false when Intent is passed from MainActivity)
                    int	NowPlayingAlbumArt
                    int	shuffledArrayNowPlayingSongPosition
                    int	CategoryArrayNowPlayingSongPosition  */

        Intent intent = getIntent();

        //Check if intent is null. If so, retrieve data from SharedPreferences instead
        String intentInfo = intent.getStringExtra("NowPlayingSongName");

        if (intentInfo == null) {
            retrieveSharedPreferences();

            //todo remove log
            Log.v("Meditation", "intentInfo is null, calling retrieveSharedPreferences");

        } else {
            nowPlayingSongName = intent.getStringExtra("NowPlayingSongName");
            nowPlayingArtistName = intent.getStringExtra("NowPlayingArtistName");
            nowPlayingCategory = intent.getStringExtra("NowPlayingCategory");
            isPlaying = intent.getBooleanExtra("isPlaying", true);
            nowPlayingAlbumArt = intent.getIntExtra("NowPlayingAlbumArt", R.drawable.namaste);
            categoryArrayCurrentPosition = intent.getIntExtra("CategoryArrayNowPlayingSongPosition", 3);
            randomSongsCurrentPosition = intent.getIntExtra("shuffledArrayNowPlayingSongPosition", 6);

            //todo remove log
            Log.v("MainActivity", "intentInfo is NOT null, populating variables");
        }
        //Adjust and/or Populate Views
        updateViews();

        //Attach Listeners
        attachListeners();

        // Create an ArrayList of songs
        final ArrayList<Songs> songs = new ArrayList<Songs>();

        songs.add(new Songs(getString(R.string.song_name_1), getString(R.string.artist_name_1), R.drawable.peace_love_zen, getString(R.string.song_category_1)));
        songs.add(new Songs(getString(R.string.song_name_2), getString(R.string.artist_name_2), R.drawable.meeting_of_the_mind, getString(R.string.song_category_1)));
        songs.add(new Songs(getString(R.string.song_name_3), getString(R.string.artist_name_3), R.drawable.harmony, getString(R.string.song_category_1)));
        songs.add(new Songs(getString(R.string.song_name_4), getString(R.string.artist_name_4), R.drawable.success, getString(R.string.song_category_1)));
        songs.add(new Songs(getString(R.string.song_name_5), getString(R.string.artist_name_5), R.drawable.namaste, getString(R.string.song_category_1)));

        // Create a {@link SongsAdapter}, whose data source is a list of Songs.
        SongsAdapter adapter = new SongsAdapter(this, R.layout.wide_list_item, songs);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        ListView listView = (ListView) findViewById(R.id.list_view);

        // Make the {@link ListView} use the {@link SongsAdapter}
        listView.setAdapter(adapter);

        // Set a click listener to change the song when grid item is clicked on
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Songs song = songs.get(position);

                nowPlayingSongName = song.getSongName();
                nowPlayingArtistName = song.getArtistName();
                nowPlayingAlbumArt = song.getImageID();
                nowPlayingCategory = song.getCategory();
                categoryArrayCurrentPosition = position;
                isPlaying = true;

                //Update Views
                updateViews();

                Intent nowPlayingDetailsScreen = new Intent(MeditationSongs.this, NowPlaying.class);

                //Update SharedPreferences
                updateSharedPreferences();

                //Pass intents with song information
                //Intent Key names and data types

                /*  String	NowPlayingSongName
                    String	NowPlayingArtistName
                    String	NowPlayingCategory
                    boolean	isPlaying
                    boolean	isCategoryArray (will be false when Intent is passed from MainActivity)
                    int	NowPlayingAlbumArt
                    int	shuffledArrayNowPlayingSongPosition
                    int	CategoryArrayNowPlayingSongPosition  */

                nowPlayingDetailsScreen.putExtra("NowPlayingSongName", nowPlayingSongName);
                nowPlayingDetailsScreen.putExtra("NowPlayingArtistName", nowPlayingArtistName);
                nowPlayingDetailsScreen.putExtra("NowPlayingCategory", nowPlayingCategory);
                nowPlayingDetailsScreen.putExtra("isPlaying", isPlaying);
                nowPlayingDetailsScreen.putExtra("isCategoryArray", true);
                nowPlayingDetailsScreen.putExtra("NowPlayingAlbumArt", nowPlayingAlbumArt);
                nowPlayingDetailsScreen.putExtra("shuffledArrayNowPlayingSongPosition", randomSongsCurrentPosition);
                nowPlayingDetailsScreen.putExtra("CategoryArrayNowPlayingSongPosition", categoryArrayCurrentPosition);

                //Remove the jolt like animation when switching activity
                nowPlayingDetailsScreen.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(nowPlayingDetailsScreen);
            }
        });
    }

    /**
     * Method updates the views
     * Views are inside CardView at bottom of the screen and show Song Name, Artist, Play Button, and Album Art
     */
    public void updateViews() {
        //Update the Now Playing Information views with new values
        //Update Song Name
        nowPlayingSongNameTextView.setText(nowPlayingSongName);

        //Update Artist Name
        nowPlayingArtistNameTextView.setText(nowPlayingArtistName);

        //Update Album Art
        nowPlayingAlbumArtImageView.setImageResource(nowPlayingAlbumArt);

        //Determine if music is playing or paused
        //Find view that shows Play/Pause button. If Player is playing, show Pause button
        if (isPlaying) {
            playButton.setImageResource(R.drawable.ic_pause_circle_white_48dp);
        } else {
            playButton.setImageResource(R.drawable.baseline_play_circle_filled_white_white_48);
        }

        //Update the remaining views

        //Find TextView with drawableTop which shows Meditation Section Icon
        TextView meditationButton = findViewById(R.id.meditation_button);

        //Find TextView that holds category description text
        TextView categoryDescription = findViewById(R.id.category_description);

        //Find Horizontal LinearLayout which contains NowPlaying information for the current song
        LinearLayout linearLayout = findViewById(R.id.linear_layout_now_playing_info);

        //Find Root LinearLayout view in layout
        LinearLayout songsListLayout = findViewById(R.id.songs_list_linear_layout_view);

        //On icon row, change background color of meditate button to indicate that it's the active activity
        meditationButton.setBackgroundColor(getResources().getColor(R.color.tertiaryDarkColor));

        //Change Color of category description background
        categoryDescription.setBackgroundColor(getResources().getColor(R.color.tertiaryDarkColor));

        //Set category description text
        categoryDescription.setText(getString(R.string.song_category_1_descrip));

        //Change Color of Now Playing Area
        linearLayout.setBackgroundColor(getResources().getColor(R.color.tertiaryDarkColor));

        //Change background color of app title TextView
        songsListLayout.setBackgroundColor(getResources().getColor(R.color.tertiaryColor));
    }

    /**
     * Method retrieves SharedPreferences
     * <p>
     * Key Names match Intent keys
     * name:	MY_PREFERENCES
     * String	NowPlayingSongName
     * String	NowPlayingArtistName
     * String	NowPlayingCategory
     * boolean	isPlaying
     * boolean	isCategoryArray
     * int	NowPlayingAlbumArt
     * int	shuffledArrayNowPlayingSongPosition
     * int	CategoryArrayNowPlayingSongPosition
     */
    public void retrieveSharedPreferences() {
        sharedPreferences = getSharedPreferences("MY_PREFERENCES", MODE_PRIVATE);

        //Retrieve shared values or pass in default value for the Now Playing Song, Artist Name, Category, and Album Art
        nowPlayingSongName = sharedPreferences.getString("NowPlayingSongName", getString(R.string.default_song_name_key));

        //todo remove log
        Log.v("Meditation", "SharedPreferences Song Name: " + nowPlayingSongName);

        //Retrieve Artist Name
        nowPlayingArtistName = sharedPreferences.getString("NowPlayingArtistName", getString(R.string.default_artist_name_key));

        //Retrieve Album Art
        nowPlayingAlbumArt = sharedPreferences.getInt("NowPlayingAlbumArt", R.drawable.namaste);

        //Retrieve Category
        nowPlayingCategory = sharedPreferences.getString("NowPlayingCategory", getString(R.string.default_category));

        //Determine if music is playing or paused
        isPlaying = sharedPreferences.getBoolean("isPlaying", false);

        //Retrieve positions of Songs elements (NowPlaying needs this info to fast forward and rewind)
        randomSongsCurrentPosition = sharedPreferences.getInt("shuffledArrayNowPlayingSongPosition", 6);
        categoryArrayCurrentPosition = sharedPreferences.getInt("CategoryArrayNowPlayingSongPosition", 3);

        //todo remove log
        Log.v("Meditation", "Retrieved SharedPreferences");
    }

    /**
     * Method updates Shared Preferences with new information
     * Key Names match Intent keys
     * name:	MY_PREFERENCES
     * String	NowPlayingSongName
     * String	NowPlayingArtistName
     * String	NowPlayingCategory
     * boolean	isPlaying
     * boolean	isCategoryArray
     * int	NowPlayingAlbumArt
     * int	shuffledArrayNowPlayingSongPosition
     * int	CategoryArrayNowPlayingSongPosition
     */
    public void updateSharedPreferences() {

        //Save details related to the layout of the app in Shared Preferences
        sharedPreferences = this.getSharedPreferences("MY_PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString("NowPlayingSongName", nowPlayingSongName);
        sharedPrefEditor.putString("NowPlayingArtistName", nowPlayingArtistName);
        sharedPrefEditor.putString("NowPlayingCategory", nowPlayingCategory);
        sharedPrefEditor.putBoolean("isPlaying", isPlaying);
        sharedPrefEditor.putBoolean("isCategoryArray", true);
        sharedPrefEditor.putInt("NowPlayingAlbumArt", nowPlayingAlbumArt);
        sharedPrefEditor.putInt("shuffledArrayNowPlayingSongPosition", randomSongsCurrentPosition);
        sharedPrefEditor.putInt("CategoryArrayNowPlayingSongPosition", categoryArrayCurrentPosition);
        sharedPrefEditor.apply();

        //todo remove log
        Log.v("MeditationSongs", "The category is now " + nowPlayingCategory + "\nMeditationSongs is passing the variables");
    }

    /**
     * Method attaches click listeners to all the category icons
     * When clicked, an intent will be sent to the Activity that is called
     */
    public void attachListeners() {

        //Attach Listeners to the Icon Row Which Allows Users To Switch Sections
        // Find the View that shows the home button
        TextView homeButton = (TextView) findViewById(R.id.home_button);

        // Set a click listener on that View and pass intents
        homeButton.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the home icon is clicked on.
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(MeditationSongs.this, MainActivity.class);

                //Send Intent Data
                // Intent Key names and data types:

                /*  String	NowPlayingSongName
                    String	NowPlayingArtistName
                    String	NowPlayingCategory
                    boolean	isPlaying
                    boolean	isCategoryArray
                    int	NowPlayingAlbumArt
                    int	shuffledArrayNowPlayingSongPosition
                    int	CategoryArrayNowPlayingSongPosition  */

                homeIntent.putExtra("NowPlayingSongName", nowPlayingSongName);
                homeIntent.putExtra("NowPlayingArtistName", nowPlayingArtistName);
                homeIntent.putExtra("NowPlayingCategory", nowPlayingCategory);
                homeIntent.putExtra("isPlaying", isPlaying);
                homeIntent.putExtra("isCategoryArray", false);
                homeIntent.putExtra("NowPlayingAlbumArt", nowPlayingAlbumArt);
                homeIntent.putExtra("shuffledArrayNowPlayingSongPosition", randomSongsCurrentPosition);
                homeIntent.putExtra("CategoryArrayNowPlayingSongPosition", categoryArrayCurrentPosition);

                //Remove the jolt like animation when switching activity then start activity
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(homeIntent);
            }
        });

        // Find the View that shows the yoga songs category
        TextView yogaButton = (TextView) findViewById(R.id.yoga_button);

        // Set a click listener on that View and pass intents
        yogaButton.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the yoga category View is clicked on.
            @Override
            public void onClick(View view) {
                Intent yogaIntent = new Intent(MeditationSongs.this, YogaSongs.class);
                yogaIntent.putExtra("NowPlayingSongName", nowPlayingSongName);
                yogaIntent.putExtra("NowPlayingArtistName", nowPlayingArtistName);
                yogaIntent.putExtra("NowPlayingCategory", nowPlayingCategory);
                yogaIntent.putExtra("isPlaying", isPlaying);
                yogaIntent.putExtra("isCategoryArray", true);
                yogaIntent.putExtra("NowPlayingAlbumArt", nowPlayingAlbumArt);
                yogaIntent.putExtra("shuffledArrayNowPlayingSongPosition", randomSongsCurrentPosition);
                yogaIntent.putExtra("CategoryArrayNowPlayingSongPosition", categoryArrayCurrentPosition);
                yogaIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(yogaIntent);
            }
        });

        // Find the View that shows the energy songs category
        TextView energyButton = (TextView) findViewById(R.id.energy_button);

        // Set a click listener on that View and pass intents
        energyButton.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the energy category View is clicked on.
            @Override
            public void onClick(View view) {
                Intent energyIntent = new Intent(MeditationSongs.this, EnergySongs.class);
                energyIntent.putExtra("NowPlayingSongName", nowPlayingSongName);
                energyIntent.putExtra("NowPlayingArtistName", nowPlayingArtistName);
                energyIntent.putExtra("NowPlayingCategory", nowPlayingCategory);
                energyIntent.putExtra("isPlaying", isPlaying);
                energyIntent.putExtra("isCategoryArray", true);
                energyIntent.putExtra("NowPlayingAlbumArt", nowPlayingAlbumArt);
                energyIntent.putExtra("shuffledArrayNowPlayingSongPosition", randomSongsCurrentPosition);
                energyIntent.putExtra("CategoryArrayNowPlayingSongPosition", categoryArrayCurrentPosition);
                energyIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(energyIntent);
            }
        });

        // Find the View that shows the sleep songs category
        TextView sleepButton = (TextView) findViewById(R.id.sleep_button);

        // Set a click listener on that View and pass intents
        sleepButton.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the sleep category View is clicked on.
            @Override
            public void onClick(View view) {
                Intent sleepIntent = new Intent(MeditationSongs.this, SleepSongs.class);
                sleepIntent.putExtra("NowPlayingSongName", nowPlayingSongName);
                sleepIntent.putExtra("NowPlayingArtistName", nowPlayingArtistName);
                sleepIntent.putExtra("NowPlayingCategory", nowPlayingCategory);
                sleepIntent.putExtra("isPlaying", isPlaying);
                sleepIntent.putExtra("isCategoryArray", true);
                sleepIntent.putExtra("NowPlayingAlbumArt", nowPlayingAlbumArt);
                sleepIntent.putExtra("shuffledArrayNowPlayingSongPosition", randomSongsCurrentPosition);
                sleepIntent.putExtra("CategoryArrayNowPlayingSongPosition", categoryArrayCurrentPosition);
                sleepIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(sleepIntent);
            }
        });

        // Now set click listeners on the Bottom CardView that shows the Song Name, Artist Name, Album
        // Art, and Play/Pause button.

        // Set a click listener on the ImageView containing the play/pause button
        // A tap on the button will call the toggleMusic method
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //The toggleMusic method will be called when a user clicks on the play or pause button
                toggleMusic();
            }
        });

        // Set a click listener on the view that shows the Song Name
        // A tap on this area will call the NowPlaying activity
        // and pass Intent data
        nowPlayingSongNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent songDetail = new Intent(MeditationSongs.this, NowPlaying.class);
                updateSharedPreferences();

                //Pass intents with song information
                //Intent Key names and data types

                /*  String	NowPlayingSongName
                    String	NowPlayingArtistName
                    String	NowPlayingCategory
                    boolean	isPlaying
                    boolean	isCategoryArray (will be false when Intent is passed from MainActivity)
                    int	NowPlayingAlbumArt
                    int	shuffledArrayNowPlayingSongPosition
                    int	CategoryArrayNowPlayingSongPosition */

                songDetail.putExtra("NowPlayingSongName", nowPlayingSongName);
                songDetail.putExtra("NowPlayingArtistName", nowPlayingArtistName);
                songDetail.putExtra("NowPlayingCategory", nowPlayingCategory);
                songDetail.putExtra("isPlaying", isPlaying);
                songDetail.putExtra("isCategoryArray", true);
                songDetail.putExtra("NowPlayingAlbumArt", nowPlayingAlbumArt);
                songDetail.putExtra("shuffledArrayNowPlayingSongPosition", randomSongsCurrentPosition);
                songDetail.putExtra("CategoryArrayNowPlayingSongPosition", categoryArrayCurrentPosition);

                //Remove the jolt like animation when switching activity
                songDetail.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(songDetail);
            }
        });

        //Set a click listener on the view that shows the Artist Name
        nowPlayingArtistNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent songDetail = new Intent(MeditationSongs.this, NowPlaying.class);
                updateSharedPreferences();
                songDetail.putExtra("NowPlayingSongName", nowPlayingSongName);
                songDetail.putExtra("NowPlayingArtistName", nowPlayingArtistName);
                songDetail.putExtra("NowPlayingCategory", nowPlayingCategory);
                songDetail.putExtra("isPlaying", isPlaying);
                songDetail.putExtra("isCategoryArray", true);
                songDetail.putExtra("NowPlayingAlbumArt", nowPlayingAlbumArt);
                songDetail.putExtra("shuffledArrayNowPlayingSongPosition", randomSongsCurrentPosition);
                songDetail.putExtra("CategoryArrayNowPlayingSongPosition", categoryArrayCurrentPosition);
                songDetail.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(songDetail);
            }
        });

        //Set a click listener on the view shows the Album Art Icon
        nowPlayingAlbumArtImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent songDetail = new Intent(MeditationSongs.this, NowPlaying.class);
                updateSharedPreferences();
                songDetail.putExtra("NowPlayingSongName", nowPlayingSongName);
                songDetail.putExtra("NowPlayingArtistName", nowPlayingArtistName);
                songDetail.putExtra("NowPlayingCategory", nowPlayingCategory);
                songDetail.putExtra("isPlaying", isPlaying);
                songDetail.putExtra("isCategoryArray", true);
                songDetail.putExtra("NowPlayingAlbumArt", nowPlayingAlbumArt);
                songDetail.putExtra("shuffledArrayNowPlayingSongPosition", randomSongsCurrentPosition);
                songDetail.putExtra("CategoryArrayNowPlayingSongPosition", categoryArrayCurrentPosition);
                songDetail.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(songDetail);
            }
        });
    }

    /**
     * This method is called when the user clicks on the Play / Pause button
     * If the music was paused, then this method will Play the music
     * If the music was playing, then this method will Pause the music
     */
    public void toggleMusic() {
        //Check if music was playing when the button was clicked
        if (isPlaying) {
            //True means User pressed PAUSE, Change Pause Icon to Play Icon
            playButton.setImageResource(R.drawable.baseline_play_circle_filled_white_white_48);

            //Update boolean to reflect that music is not playing
            isPlaying = false;

            //todo remove logs
            Log.v("Meditation", "Music was PAUSED by user. SharedPreferences updated");

        } else {
            //False means User Pressed PLAY. Change Play Icon to Pause Icon
            playButton.setImageResource(R.drawable.ic_pause_circle_white_48dp);

            //Update boolean to reflect that the music is playing
            isPlaying = true;

            //todo remove logs
            Log.v("Meditation", "User pressed PLAY. SharedPreferences updated");
        }
        //Update shared preferences with new boolean value
        updateSharedPreferences();
    }
}
