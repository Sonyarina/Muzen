package com.example.android.muzen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Shared Preferences
    SharedPreferences sharedPreferences;
    TextView nowPlayingSongNameTextView;
    TextView nowPlayingArtistNameTextView;
    ImageView nowPlayingAlbumArtImageView;
    //ImageView that holds the Play-Pause Button
    ImageView playButton;
    //boolean which tracks whether music is playing or not
    boolean isPlaying;
    //Declare ArrayList containing Songs objects
    private ArrayList<Songs> randomSongs = new ArrayList<Songs>();
    //Default Now Playing Song Name
    private String nowPlayingSongName;
    //Default Now Playing Artist Name
    private String nowPlayingArtistName;
    //Default Now Playing Album Art ImageID
    private int nowPlayingAlbumArt = R.drawable.namaste;
    //Default Now Playing Category
    private String nowPlayingCategory;
    //Position of currentSong in array
    private int randomSongsCurrentPosition = 6;
    private int categoryArrayCurrentPosition = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //todo remove
        //clearSharedPreferences();

        //Find the views for Now Playing Area
        nowPlayingSongNameTextView = findViewById(R.id.song_name);
        nowPlayingArtistNameTextView = findViewById(R.id.artist_name);
        nowPlayingAlbumArtImageView = findViewById(R.id.album_art_icon);
        playButton = findViewById(R.id.now_playing_play_button);

        //Retrieve intent data
        //Intent Key names and data types:

                /*  String	NowPlayingSongName
                    String	NowPlayingArtistName
                    String	NowPlayingCategory
                    boolean	isPlaying
                    boolean	isCategoryArray (will be false when Intent is passed from MainActivity)
                    int	NowPlayingAlbumArt
                    int	shuffledArrayNowPlayingSongPosition (Applicable to MainActivity)
                    int	CategoryArrayNowPlayingSongPosition (Applicable to Songs Activities ) */

        Intent intent = getIntent();

        //Check if intent is null. If so, retrieve data from SharedPreferences instead
        String intentInfo = intent.getStringExtra("NowPlayingSongName");

        if (intentInfo == null) {
            retrieveSharedPreferences();

            //todo remove log
            Log.v("MainActivity", "intentInfo is null, calling retrieveSharedPreferences");

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

        //Update View components with information retrieved from Intent or SharedPreferences
        updateViews();

        //Update SharedPreferences
        updateSharedPreferences();

        //Attach Click Listeners to the Icons on the top of the screen, below the app title; and to
        //bottom CardView that has Play/Pause button and song info
        attachClickListeners();

        // Create an ArrayList of songs
        createSongsArray();
    }

    /**
     * Method clears SharedPreferences
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
    public void clearSharedPreferences() {
        sharedPreferences = getSharedPreferences("MY_PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.clear();
        sharedPrefEditor.apply();
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
        Log.v("MainActivity", "SharedPreferences Song Name: " + nowPlayingSongName);

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
        Log.v("MainActivity", "Retrieved SharedPreferences");
    }

    /**
     * Method creates an ArrayList for Songs
     */
    public void createSongsArray() {

        //Add Songs objects to the global ArrayList<Songs> named randomSongs
        randomSongs.add(new Songs(getString(R.string.song_name_8), getString(R.string.artist_name_8), R.drawable.above_the_sky, getString(R.string.song_category_4)));
        randomSongs.add(new Songs(getString(R.string.song_name_14), getString(R.string.artist_name_14), R.drawable.feel_the_energy, getString(R.string.song_category_3)));
        randomSongs.add(new Songs(getString(R.string.song_name_15), getString(R.string.artist_name_15), R.drawable.happy_music, getString(R.string.song_category_3)));
        randomSongs.add(new Songs(getString(R.string.song_name_3), getString(R.string.artist_name_3), R.drawable.harmony, getString(R.string.song_category_1)));
        randomSongs.add(new Songs(getString(R.string.song_name_6), getString(R.string.artist_name_6), R.drawable.healing_sleep, getString(R.string.song_category_4)));
        randomSongs.add(new Songs(getString(R.string.song_name_2), getString(R.string.artist_name_2), R.drawable.meeting_of_the_mind, getString(R.string.song_category_1)));
        randomSongs.add(new Songs(getString(R.string.song_name_5), getString(R.string.artist_name_5), R.drawable.namaste, getString(R.string.song_category_1)));
        randomSongs.add(new Songs(getString(R.string.song_name_11), getString(R.string.artist_name_11), R.drawable.mind_soul, getString(R.string.song_category_2)));
        randomSongs.add(new Songs(getString(R.string.song_name_1), getString(R.string.artist_name_1), R.drawable.peace_love_zen, getString(R.string.song_category_1)));
        randomSongs.add(new Songs(getString(R.string.song_name_7), getString(R.string.artist_name_7), R.drawable.peaceful_slumber, getString(R.string.song_category_4)));
        randomSongs.add(new Songs(getString(R.string.song_name_17), getString(R.string.artist_name_17), R.drawable.positive_energy, getString(R.string.song_category_3)));
        randomSongs.add(new Songs(getString(R.string.song_name_12), getString(R.string.artist_name_12), R.drawable.reach_balance, getString(R.string.song_category_2)));
        randomSongs.add(new Songs(getString(R.string.song_name_9), getString(R.string.artist_name_9), R.drawable.sleeping_abstract, getString(R.string.song_category_4)));
        randomSongs.add(new Songs(getString(R.string.song_name_4), getString(R.string.artist_name_4), R.drawable.success, getString(R.string.song_category_1)));
        randomSongs.add(new Songs(getString(R.string.song_name_16), getString(R.string.artist_name_16), R.drawable.unlimited_energy, getString(R.string.song_category_3)));
        randomSongs.add(new Songs(getString(R.string.song_name_13), getString(R.string.artist_name_13), R.drawable.chakra, getString(R.string.song_category_2)));
        randomSongs.add(new Songs(getString(R.string.song_name_10), getString(R.string.artist_name_10), R.drawable.yoga_1, getString(R.string.song_category_2)));

        // Create a {@link SongsAdapter}
        SongsAdapter adapter = new SongsAdapter(this, R.layout.narrow_list_item, randomSongs);

        // Find the {@link GridView} object
        GridView gridView = (GridView) findViewById(R.id.list_view);

        // Make the {@link GridView} use the {@link SongsAdapter} by calling the setAdapter method on the {@link GridView} object
        // pass in 1 argument, which is the {@link SongsAdapter} with the variable name adapter.
        gridView.setAdapter(adapter);

        // Set a click listener to change the song when grid item is clicked on
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Songs song = randomSongs.get(position);
                nowPlayingSongName = song.getSongName();
                nowPlayingArtistName = song.getArtistName();
                nowPlayingAlbumArt = song.getImageID();
                nowPlayingCategory = song.getCategory();
                randomSongsCurrentPosition = position;
                isPlaying = true;
                updateViews();
                updateSharedPreferences();

                Intent nowPlayingDetailsScreen = new Intent(MainActivity.this, NowPlaying.class);
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
                nowPlayingDetailsScreen.putExtra("isCategoryArray", false);
                nowPlayingDetailsScreen.putExtra("NowPlayingAlbumArt", nowPlayingAlbumArt);
                nowPlayingDetailsScreen.putExtra("shuffledArrayNowPlayingSongPosition", randomSongsCurrentPosition);
                nowPlayingDetailsScreen.putExtra("CategoryArrayNowPlayingSongPosition", categoryArrayCurrentPosition);

                //Remove the jolt like animation when switching activity
                nowPlayingDetailsScreen.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(nowPlayingDetailsScreen);
            }
        });

        //todo remove logs
        Log.v("MainActivity", "Created Songs Array");
    }

    /**
     * Method attaches click listeners to all the category icons
     * When clicked, an intent will be sent to the Activity that is called
     * This method also attaches listeners to the Now Playing Song Information and Play/Pause Button
     */
    public void attachClickListeners() {

        // Find the View that shows the meditation songs category
        // Set a click listener on that View and pass intents to the next activity

        TextView meditationButton = (TextView) findViewById(R.id.meditation_button);

        meditationButton.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the meditation View is clicked on.
            @Override
            public void onClick(View view) {
                Intent meditationIntent = new Intent(MainActivity.this, MeditationSongs.class);

                //Pass intents with song information
                //Intent Key names and data types

                /*  String	NowPlayingSongName
                    String	NowPlayingArtistName
                    String	NowPlayingCategory
                    boolean	isPlaying
                    boolean	isCategoryArray
                    int	NowPlayingAlbumArt
                    int	shuffledArrayNowPlayingSongPosition
                    int	CategoryArrayNowPlayingSongPosition  */

                meditationIntent.putExtra("NowPlayingSongName", nowPlayingSongName);
                meditationIntent.putExtra("NowPlayingArtistName", nowPlayingArtistName);
                meditationIntent.putExtra("NowPlayingCategory", nowPlayingCategory);
                meditationIntent.putExtra("isPlaying", isPlaying);
                meditationIntent.putExtra("isCategoryArray", true);
                meditationIntent.putExtra("NowPlayingAlbumArt", nowPlayingAlbumArt);
                meditationIntent.putExtra("shuffledArrayNowPlayingSongPosition", randomSongsCurrentPosition);
                meditationIntent.putExtra("CategoryArrayNowPlayingSongPosition", categoryArrayCurrentPosition);

                //Remove the jolt like animation when switching activity, update preferences, then start activity
                meditationIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(meditationIntent);
            }
        });

        // Find the View that shows the yoga songs category
        // Set a click listener on that View and pass intents to the next activity

        TextView yogaButton = (TextView) findViewById(R.id.yoga_button);

        yogaButton.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the yoga category View is clicked on.
            @Override
            public void onClick(View view) {
                Intent yogaIntent = new Intent(MainActivity.this, YogaSongs.class);

                //Pass intents with song information
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
        // Set a click listener on that View and pass intents to the next activity
        TextView energyButton = (TextView) findViewById(R.id.energy_button);

        energyButton.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the energy category View is clicked on.
            @Override
            public void onClick(View view) {
                Intent energyIntent = new Intent(MainActivity.this, EnergySongs.class);

                //Pass intents with song information
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
        // Set a click listener on that View and pass intents to the next activity
        TextView sleepButton = (TextView) findViewById(R.id.sleep_button);

        sleepButton.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the sleep category View is clicked on.
            @Override
            public void onClick(View view) {
                Intent sleepIntent = new Intent(MainActivity.this, SleepSongs.class);

                //Pass intents with song information
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
            // The code in this method will be executed when the play button ImageView is clicked on.
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
                Intent songDetail = new Intent(MainActivity.this, NowPlaying.class);
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
                songDetail.putExtra("isCategoryArray", false);
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
                Intent songDetail = new Intent(MainActivity.this, NowPlaying.class);
                updateSharedPreferences();
                songDetail.putExtra("NowPlayingSongName", nowPlayingSongName);
                songDetail.putExtra("NowPlayingArtistName", nowPlayingArtistName);
                songDetail.putExtra("NowPlayingCategory", nowPlayingCategory);
                songDetail.putExtra("isPlaying", isPlaying);
                songDetail.putExtra("isCategoryArray", false);
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
                Intent songDetail = new Intent(MainActivity.this, NowPlaying.class);
                updateSharedPreferences();
                songDetail.putExtra("NowPlayingSongName", nowPlayingSongName);
                songDetail.putExtra("NowPlayingArtistName", nowPlayingArtistName);
                songDetail.putExtra("NowPlayingCategory", nowPlayingCategory);
                songDetail.putExtra("isPlaying", isPlaying);
                songDetail.putExtra("isCategoryArray", false);
                songDetail.putExtra("NowPlayingAlbumArt", nowPlayingAlbumArt);
                songDetail.putExtra("shuffledArrayNowPlayingSongPosition", randomSongsCurrentPosition);
                songDetail.putExtra("CategoryArrayNowPlayingSongPosition", categoryArrayCurrentPosition);
                songDetail.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(songDetail);
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
        sharedPrefEditor.putBoolean("isCategoryArray", false);
        sharedPrefEditor.putInt("NowPlayingAlbumArt", nowPlayingAlbumArt);
        sharedPrefEditor.putInt("shuffledArrayNowPlayingSongPosition", randomSongsCurrentPosition);
        sharedPrefEditor.putInt("CategoryArrayNowPlayingSongPosition", categoryArrayCurrentPosition);
        sharedPrefEditor.apply();

        //todo remove logs
        Log.v("MainActivity", "Shuffled Songs Array was clicked. MainActivity is updating shared preferences");
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
            Log.v("MainActivity", "Music was PAUSED by user. SharedPreferences updated");

        } else {
            //False means User Pressed PLAY. Change Play Icon to Pause Icon
            playButton.setImageResource(R.drawable.ic_pause_circle_white_48dp);

            //Update boolean to reflect that the music is playing
            isPlaying = true;

            //todo remove logs
            Log.v("MainActivity", "User pressed PLAY. SharedPreferences updated");
        }
        //Update shared preferences with new boolean value
        updateSharedPreferences();
    }
}
