package com.example.android.muzen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class NowPlaying extends AppCompatActivity {

    //SharedPreferences
    SharedPreferences sharedPreferences;
    TextView nowPlayingSongNameTextView;
    TextView nowPlayingArtistNameTextView;
    //The ImageView that displays a the large album art background
    ImageView nowPlayingAlbumArtImageView;
    //The ImageView that displays a small album art icon
    ImageView nowPlayingAlbumArtIconView;
    //The ImageView that displays the Play/Pause button
    ImageView nowPlayingPlayButton;
    //ImageView that holds the "back" icon button
    ImageView backButton;
    private ArrayList<Songs> randomSongs = new ArrayList<Songs>();
    private ArrayList<Songs> categoryOneSongs = new ArrayList<Songs>();
    private ArrayList<Songs> categoryTwoSongs = new ArrayList<Songs>();
    private ArrayList<Songs> categoryThreeSongs = new ArrayList<Songs>();
    private ArrayList<Songs> categoryFourSongs = new ArrayList<Songs>();
    //Position of currentSong in array
    private int randomSongsCurrentPosition;
    private int categoryArrayCurrentPosition;
    //Default Now Playing Song Name
    private String nowPlayingSongName;
    //Default Now Playing Artist Name
    private String nowPlayingArtistName;
    //Default Now Playing Album Art ImageID
    private int nowPlayingAlbumArt = R.drawable.namaste;
    //Boolean keeps track of whether music is playing or not
    private boolean isPlaying;

    //Boolean keeps track of the playlist type
    private boolean isCategoryArray;

    //String keeps track of category
    private String nowPlayingCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        //Find Views for Now Playing Detail Screen
        nowPlayingSongNameTextView = findViewById(R.id.song_name);
        nowPlayingArtistNameTextView = findViewById(R.id.artist_name);
        nowPlayingAlbumArtImageView = findViewById(R.id.album_art_icon);
        nowPlayingAlbumArtIconView = findViewById(R.id.top_album_art_icon);
        nowPlayingPlayButton = findViewById(R.id.now_playing_play_button);
        backButton = findViewById(R.id.now_playing_back_button);

        //Populate ArrayLists with Songs
        fillArrays();

        //Retrieve intent data
        //Intent Key names and data types:

                /*  String	NowPlayingSongName
                    String	NowPlayingArtistName
                    String	NowPlayingCategory
                    boolean	isPlaying
                    boolean	isCategoryArray (will be false when Intent is passed from MainActivity)
                    int	NowPlayingAlbumArt
                    int	shuffledArrayNowPlayingSongPosition
                    int	CategoryArrayNowPlayingSongPosition */

        Intent intent = getIntent();

        //Check if intent is null. If so, retrieve data from SharedPreferences instead
        String intentInfo = intent.getStringExtra("NowPlayingSongName");

        if (intentInfo == null) {
            retrieveSharedPreferences();

            //todo remove log
            Log.v("NowPlaying", "intentInfo is null, calling retrieveSharedPreferences");


        } else {
            nowPlayingSongName = intent.getStringExtra("NowPlayingSongName");
            nowPlayingArtistName = intent.getStringExtra("NowPlayingArtistName");
            nowPlayingCategory = intent.getStringExtra("NowPlayingCategory");
            isPlaying = intent.getBooleanExtra("isPlaying", true);
            isCategoryArray = intent.getBooleanExtra("isCategoryArray", false);
            nowPlayingAlbumArt = intent.getIntExtra("NowPlayingAlbumArt", R.drawable.namaste);
            randomSongsCurrentPosition = intent.getIntExtra("shuffledArrayNowPlayingSongPosition", 6);
            categoryArrayCurrentPosition = intent.getIntExtra("CategoryArrayNowPlayingSongPosition", 3);
        }

        //Update View components with information retrieved from Intent or SharedPreferences
        updateViews();

        //Setup media controller views and attach listeners
        attachListeners();
    }

    /**
     * Method populates arrays with Songs
     */
    public void fillArrays() {

        Log.v("NowPlaying", "Running fillArrays()");
        //Fill Shuffled Array
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

        //Fill Category 1 Array
        categoryOneSongs.add(new Songs(getString(R.string.song_name_1), getString(R.string.artist_name_1), R.drawable.peace_love_zen, getString(R.string.song_category_1)));
        categoryOneSongs.add(new Songs(getString(R.string.song_name_2), getString(R.string.artist_name_2), R.drawable.meeting_of_the_mind, getString(R.string.song_category_1)));
        categoryOneSongs.add(new Songs(getString(R.string.song_name_3), getString(R.string.artist_name_3), R.drawable.harmony, getString(R.string.song_category_1)));
        categoryOneSongs.add(new Songs(getString(R.string.song_name_4), getString(R.string.artist_name_4), R.drawable.success, getString(R.string.song_category_1)));
        categoryOneSongs.add(new Songs(getString(R.string.song_name_5), getString(R.string.artist_name_5), R.drawable.namaste, getString(R.string.song_category_1)));

        //Fill Category 2 Array
        categoryTwoSongs.add(new Songs(getString(R.string.song_name_10), getString(R.string.artist_name_10), R.drawable.yoga_1, getString(R.string.song_category_2)));
        categoryTwoSongs.add(new Songs(getString(R.string.song_name_11), getString(R.string.artist_name_11), R.drawable.mind_soul, getString(R.string.song_category_2)));
        categoryTwoSongs.add(new Songs(getString(R.string.song_name_12), getString(R.string.artist_name_12), R.drawable.reach_balance, getString(R.string.song_category_2)));
        categoryTwoSongs.add(new Songs(getString(R.string.song_name_13), getString(R.string.artist_name_13), R.drawable.chakra, getString(R.string.song_category_2)));

        //Fill Category 3 Array
        categoryThreeSongs.add(new Songs(getString(R.string.song_name_14), getString(R.string.artist_name_14), R.drawable.feel_the_energy, getString(R.string.song_category_3)));
        categoryThreeSongs.add(new Songs(getString(R.string.song_name_15), getString(R.string.artist_name_15), R.drawable.happy_music, getString(R.string.song_category_3)));
        categoryThreeSongs.add(new Songs(getString(R.string.song_name_16), getString(R.string.artist_name_16), R.drawable.unlimited_energy, getString(R.string.song_category_3)));
        categoryThreeSongs.add(new Songs(getString(R.string.song_name_17), getString(R.string.artist_name_17), R.drawable.positive_energy, getString(R.string.song_category_3)));

        //Fill Category 4 Array
        categoryFourSongs.add(new Songs(getString(R.string.song_name_6), getString(R.string.artist_name_6), R.drawable.healing_sleep, getString(R.string.song_category_4)));
        categoryFourSongs.add(new Songs(getString(R.string.song_name_7), getString(R.string.artist_name_7), R.drawable.peaceful_slumber, getString(R.string.song_category_4)));
        categoryFourSongs.add(new Songs(getString(R.string.song_name_8), getString(R.string.artist_name_8), R.drawable.above_the_sky, getString(R.string.song_category_4)));
        categoryFourSongs.add(new Songs(getString(R.string.song_name_9), getString(R.string.artist_name_9), R.drawable.sleeping_abstract, getString(R.string.song_category_4)));
    }

    /**
     * Method takes user back to main screen or the category screen
     * depending on the value of the isCategoryArray boolean
     */
    public void backToListView() {
        if (isCategoryArray) {

            //If it is a category based Songs arraylist, figure out which category it is in order
            //to return user to the right section
            if (nowPlayingCategory.equals(getString(R.string.song_category_1))) {
                //Go to Category 1 activity
                Intent meditationIntent = new Intent(NowPlaying.this, MeditationSongs.class);

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

                //Remove the jolt like animation when switching activity
                meditationIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(meditationIntent);

            } else if (nowPlayingCategory.equals(getString(R.string.song_category_2))) {
                //Go to Category 2 activity
                Intent yogaIntent = new Intent(NowPlaying.this, YogaSongs.class);
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

            } else if (nowPlayingCategory.equals(getString(R.string.song_category_3))) {
                //Go to Category 3 activity
                Intent energyIntent = new Intent(NowPlaying.this, EnergySongs.class);
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

            } else {
                //Go to Category 4 activity
                Intent sleepIntent = new Intent(NowPlaying.this, SleepSongs.class);
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
        } else {
            //It's not a category based Songs array, so return user to MainActivity
            Intent homeIntent = new Intent(NowPlaying.this, MainActivity.class);
            homeIntent.putExtra("NowPlayingSongName", nowPlayingSongName);
            homeIntent.putExtra("NowPlayingArtistName", nowPlayingArtistName);
            homeIntent.putExtra("NowPlayingCategory", nowPlayingCategory);
            homeIntent.putExtra("isPlaying", isPlaying);
            homeIntent.putExtra("isCategoryArray", false);
            homeIntent.putExtra("NowPlayingAlbumArt", nowPlayingAlbumArt);
            homeIntent.putExtra("shuffledArrayNowPlayingSongPosition", randomSongsCurrentPosition);
            homeIntent.putExtra("CategoryArrayNowPlayingSongPosition", categoryArrayCurrentPosition);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(homeIntent);
        }
    }

    /**
     * This method is called when the user clicks on the Play / Pause button
     * If the music was paused, then this method will Play the music
     * If the music was playing, then this method will Pause the music
     */
    public void toggleMusic() {

        //Find view that shows Play/Pause button.
        nowPlayingPlayButton = findViewById(R.id.now_playing_play_button);

        if (isPlaying) {
            //User pressed PAUSE, Change Pause Icon to Play Icon
            nowPlayingPlayButton.setImageResource(R.drawable.baseline_play_circle_filled_white_white_48);

            //Update boolean to reflect that music is not playing
            isPlaying = false;

            //todo remove log
            Log.v("MainActivity", "Music was PAUSED by user. SharedPreferences updated");

        } else {
            //User Pressed PLAY. Change Play Icon to Pause Icon
            nowPlayingPlayButton.setImageResource(R.drawable.ic_pause_circle_white_48dp);

            //Update boolean to reflect that the music is playing
            isPlaying = true;

            //todo remove log
            Log.v("MainActivity", "User pressed PLAY. SharedPreferences updated");
        }

        //Update shared preferences
        sharedPreferences = this.getSharedPreferences("MY_PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putBoolean("isPlaying", isPlaying);
        sharedPrefEditor.apply();
    }

    /**
     * This method is called to update the views with the correct Song information
     */
    public void updateViews() {

        //Populate views with Now Playing Song, Artist Name, Category, and Album Art
        nowPlayingSongNameTextView.setText(nowPlayingSongName);

        nowPlayingArtistNameTextView.setText(nowPlayingArtistName);

        //todo remove log
        Log.v("NowPlaying", "Song Name is " + nowPlayingSongName + " by " + nowPlayingArtistName);

        //Retrieve Album Art (Big Picture)
        nowPlayingAlbumArtImageView.setImageResource(nowPlayingAlbumArt);

        //Retrieve Album Art (Icon Picture)
        nowPlayingAlbumArtIconView.setImageResource(nowPlayingAlbumArt);

        if (isPlaying) {
            nowPlayingPlayButton.setImageResource(R.drawable.ic_pause_circle_white_48dp);
        } else {
            nowPlayingPlayButton.setImageResource(R.drawable.baseline_play_circle_filled_white_white_48);
        }

        //Change background colors based on category
        updateColors();
    }

    /**
     * This method is called to update the layout colors
     * Each category of songs has a corresponding color
     */
    public void updateColors() {
        //Change background colors based on category
        //Find Views, then change color based on category
        RelativeLayout nowPlayingLayout = findViewById(R.id.now_playing_detail_screen_rl);
        LinearLayout mediaController = findViewById(R.id.now_playing_media_button_row);

        if (nowPlayingCategory.equals(getString(R.string.song_category_1))) {
            //Set background color
            nowPlayingLayout.setBackgroundColor(getResources().getColor(R.color.tertiaryColor));
            mediaController.setBackgroundColor(getResources().getColor(R.color.tertiaryDarkColor));
            DrawableCompat.setTint(backButton.getDrawable(), ContextCompat.getColor(this, R.color.tertiaryLightColor));

        } else if (nowPlayingCategory.equals(getString(R.string.song_category_2))) {
            //Set background color
            nowPlayingLayout.setBackgroundColor(getResources().getColor(R.color.orange));
            mediaController.setBackgroundColor(getResources().getColor(R.color.orangeDark));
            DrawableCompat.setTint(backButton.getDrawable(), ContextCompat.getColor(this, R.color.orangeLight));

        } else if (nowPlayingCategory.equals(getString(R.string.song_category_3))) {
            //Set background color
            nowPlayingLayout.setBackgroundColor(getResources().getColor(R.color.pink));
            mediaController.setBackgroundColor(getResources().getColor(R.color.pinkDark));
            DrawableCompat.setTint(backButton.getDrawable(), ContextCompat.getColor(this, R.color.pinkLight));

        } else {
            //Song is Category 4, change background color
            nowPlayingLayout.setBackgroundColor(getResources().getColor(R.color.blue));
            mediaController.setBackgroundColor(getResources().getColor(R.color.blueDark));
            DrawableCompat.setTint(backButton.getDrawable(), ContextCompat.getColor(this, R.color.blueLight));
        }
    }

    /**
     * This method is called when Intent data is null or missing
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

        nowPlayingArtistName = sharedPreferences.getString("NowPlayingArtistName", getString(R.string.default_artist_name_key));

        //todo remove logs
        Log.v("NowPlaying", "Song Name is " + nowPlayingSongName + " by " + nowPlayingArtistName);

        //Get boolean that tells if music is playing or paused,
        isPlaying = sharedPreferences.getBoolean("isPlaying", false);

        //Get playlist type. False means it's a shuffled list of all the songs. True means it's a small, category based songs array
        isCategoryArray = sharedPreferences.getBoolean("isCategoryArray", false);

        //Retrieve Album Art (Big Picture)
        nowPlayingAlbumArt = sharedPreferences.getInt("NowPlayingAlbumArt", R.drawable.namaste);

        //Retrieve Album Art (Icon Picture)
        nowPlayingCategory = sharedPreferences.getString("NowPlayingCategory", getString(R.string.default_category));

        //Get song positions
        randomSongsCurrentPosition = sharedPreferences.getInt("shuffledArrayNowPlayingSongPosition", 6);
        categoryArrayCurrentPosition = sharedPreferences.getInt("CategoryArrayNowPlayingSongPosition", 3);
    }

    /**
     * Method updates the media controller views and adds listeners
     */
    public void attachListeners() {

        // Attach listener to Play/Pause button. If the music is playing, it will pause and vice versa
        nowPlayingPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Call toggleMusic method
                toggleMusic();
            }
        });

        //Find the View containing Fast Forward Button, then attach listener
        ImageView fastForwardView = findViewById(R.id.now_playing_ff_button);
        fastForwardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Call method to handle skipping forward
                skipForward();
            }
        });

        //Find the View containing Rewind Button, then attach listener
        ImageView rewindView = findViewById(R.id.now_playing_rw_button);
        rewindView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Call method to handle skipping forward
                skipPrevious();
            }
        });

        // Attach listener to the "back" button (icon with arrow pointed left)
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Call method to handle when user clicks on the back button
                backToListView();
            }
        });
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

        // Save details related to the layout of the app in Shared Preferences
        sharedPreferences = this.getSharedPreferences("MY_PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString("NowPlayingSongName", nowPlayingSongName);
        sharedPrefEditor.putString("NowPlayingArtistName", nowPlayingArtistName);
        sharedPrefEditor.putString("NowPlayingCategory", nowPlayingCategory);
        sharedPrefEditor.putBoolean("isPlaying", isPlaying);
        sharedPrefEditor.putBoolean("isCategoryArray", isCategoryArray);
        sharedPrefEditor.putInt("NowPlayingAlbumArt", nowPlayingAlbumArt);
        sharedPrefEditor.putInt("shuffledArrayNowPlayingSongPosition", randomSongsCurrentPosition);
        sharedPrefEditor.putInt("CategoryArrayNowPlayingSongPosition", categoryArrayCurrentPosition);
        sharedPrefEditor.apply();
        Log.v("NowPlaying", "NowPlaying is updating shared preferences");
    }

    /**
     * Method skips forward to the next song. If current index is at the end of the array,
     * then it starts at index 0
     */
    public void skipForwardCategoryList() {
        int lastIndex = 0;
        if (nowPlayingCategory.equals(getString(R.string.song_category_1))) {
            lastIndex = categoryOneSongs.size() - 1;

            Log.v("NowPlaying", "Category: " + nowPlayingCategory + " at index " + categoryArrayCurrentPosition);
            if (categoryArrayCurrentPosition == lastIndex) {
                categoryArrayCurrentPosition = 0;
                Log.v("NowPlaying", "Index changed to 0");

            } else {
                //If the categoryArrayCurrentPosition was not at the last index position, then add 1 to get new index position
                categoryArrayCurrentPosition++;
                Log.v("NowPlaying", "Index changed to " + categoryArrayCurrentPosition);
            }
            nowPlayingSongName = categoryOneSongs.get(categoryArrayCurrentPosition).getSongName();
            nowPlayingArtistName = categoryOneSongs.get(categoryArrayCurrentPosition).getArtistName();
            nowPlayingAlbumArt = categoryOneSongs.get(categoryArrayCurrentPosition).getImageID();
            nowPlayingCategory = categoryOneSongs.get(categoryArrayCurrentPosition).getCategory();

        } else if (nowPlayingCategory.equals(getString(R.string.song_category_2))) {
            lastIndex = categoryTwoSongs.size() - 1;
            if (categoryArrayCurrentPosition == lastIndex) {
                categoryArrayCurrentPosition = 0;

            } else {
                //If the categoryArrayCurrentPosition was not at the last index position, then add 1 to get new index position
                categoryArrayCurrentPosition++;
            }
            nowPlayingSongName = categoryTwoSongs.get(categoryArrayCurrentPosition).getSongName();
            nowPlayingArtistName = categoryTwoSongs.get(categoryArrayCurrentPosition).getArtistName();
            nowPlayingAlbumArt = categoryTwoSongs.get(categoryArrayCurrentPosition).getImageID();
            nowPlayingCategory = categoryTwoSongs.get(categoryArrayCurrentPosition).getCategory();

        } else if (nowPlayingCategory.equals(getString(R.string.song_category_3))) {
            lastIndex = categoryThreeSongs.size() - 1;
            if (categoryArrayCurrentPosition == lastIndex) {
                categoryArrayCurrentPosition = 0;

            } else {
                //If the categoryArrayCurrentPosition was not at the last index position, then add 1 to get new index position
                categoryArrayCurrentPosition++;
            }
            nowPlayingSongName = categoryThreeSongs.get(categoryArrayCurrentPosition).getSongName();
            nowPlayingArtistName = categoryThreeSongs.get(categoryArrayCurrentPosition).getArtistName();
            nowPlayingAlbumArt = categoryThreeSongs.get(categoryArrayCurrentPosition).getImageID();
            nowPlayingCategory = categoryThreeSongs.get(categoryArrayCurrentPosition).getCategory();

        } else {
            //Category 4
            lastIndex = categoryFourSongs.size() - 1;
            if (categoryArrayCurrentPosition == lastIndex) {
                categoryArrayCurrentPosition = 0;

            } else {
                //If the categoryArrayCurrentPosition was not at the last index position, then add 1 to get new index position
                categoryArrayCurrentPosition++;
            }
            nowPlayingSongName = categoryFourSongs.get(categoryArrayCurrentPosition).getSongName();
            nowPlayingArtistName = categoryFourSongs.get(categoryArrayCurrentPosition).getArtistName();
            nowPlayingAlbumArt = categoryFourSongs.get(categoryArrayCurrentPosition).getImageID();
            nowPlayingCategory = categoryFourSongs.get(categoryArrayCurrentPosition).getCategory();
        }
        //Update views and shared preferences
        updateViews();
        updateSharedPreferences();
    }

    /**
     * Method skips to the previous song
     */
    public void skipPreviousCategoryList() {
        //Figure out new Song Position
        //Skip Previous means the song in the index before the current one will be played
        //Nested condition checks to see if the index is 0. If so, it goes back to the last song on the list
        //If the categoryArrayCurrentPosition is not 0, then subtract 1 to get new index position

        if (nowPlayingCategory.equals(getString(R.string.song_category_1))) {
            if (categoryArrayCurrentPosition == 0) {
                categoryArrayCurrentPosition = categoryOneSongs.size() - 1;

            } else {
                //If the categoryArrayCurrentPosition was not 0, then subtract 1 to get new index position
                categoryArrayCurrentPosition--;
            }
            nowPlayingSongName = categoryOneSongs.get(categoryArrayCurrentPosition).getSongName();
            nowPlayingArtistName = categoryOneSongs.get(categoryArrayCurrentPosition).getArtistName();
            nowPlayingAlbumArt = categoryOneSongs.get(categoryArrayCurrentPosition).getImageID();
            nowPlayingCategory = categoryOneSongs.get(categoryArrayCurrentPosition).getCategory();

        } else if (nowPlayingCategory.equals(getString(R.string.song_category_2))) {
            if (categoryArrayCurrentPosition == 0) {
                categoryArrayCurrentPosition = categoryTwoSongs.size() - 1;

            } else {
                //If the categoryArrayCurrentPosition was not 0, then subtract 1 to get new index position
                categoryArrayCurrentPosition--;
            }
            nowPlayingSongName = categoryTwoSongs.get(categoryArrayCurrentPosition).getSongName();
            nowPlayingArtistName = categoryTwoSongs.get(categoryArrayCurrentPosition).getArtistName();
            nowPlayingAlbumArt = categoryTwoSongs.get(categoryArrayCurrentPosition).getImageID();
            nowPlayingCategory = categoryTwoSongs.get(categoryArrayCurrentPosition).getCategory();

        } else if (nowPlayingCategory.equals(getString(R.string.song_category_3))) {
            if (categoryArrayCurrentPosition == 0) {
                categoryArrayCurrentPosition = categoryThreeSongs.size() - 1;

            } else {
                //If the categoryArrayCurrentPosition was not 0, then subtract 1 to get new index position
                categoryArrayCurrentPosition--;
            }
            nowPlayingSongName = categoryThreeSongs.get(categoryArrayCurrentPosition).getSongName();
            nowPlayingArtistName = categoryThreeSongs.get(categoryArrayCurrentPosition).getArtistName();
            nowPlayingAlbumArt = categoryThreeSongs.get(categoryArrayCurrentPosition).getImageID();
            nowPlayingCategory = categoryThreeSongs.get(categoryArrayCurrentPosition).getCategory();

        } else {
            //Category 4
            if (categoryArrayCurrentPosition == 0) {
                categoryArrayCurrentPosition = categoryFourSongs.size() - 1;

            } else {
                //If the categoryArrayCurrentPosition was not 0, then subtract 1 to get new index position
                categoryArrayCurrentPosition--;
            }
            nowPlayingSongName = categoryFourSongs.get(categoryArrayCurrentPosition).getSongName();
            nowPlayingArtistName = categoryFourSongs.get(categoryArrayCurrentPosition).getArtistName();
            nowPlayingAlbumArt = categoryFourSongs.get(categoryArrayCurrentPosition).getImageID();
            nowPlayingCategory = categoryFourSongs.get(categoryArrayCurrentPosition).getCategory();
        }
        //Update views and shared preferences
        updateViews();
        updateSharedPreferences();
    }

    /**
     * Method skips forward to the next song. If current index is at the end of the array,
     * then it starts at index 0
     */
    public void skipForwardShuffledList() {
        int lastIndex = randomSongs.size() - 1;

        //todo remove
        Log.v("NowPlaying", "Category: " + nowPlayingCategory + " at index " + randomSongsCurrentPosition);

        if (randomSongsCurrentPosition == lastIndex) {
            randomSongsCurrentPosition = 0;
            Log.v("NowPlaying", "Index changed to 0");

        } else {
            //If the randomSongsCurrentPosition was not at the last index position, then add 1 to get new index position
            randomSongsCurrentPosition++;
            Log.v("NowPlaying", "Index changed to " + randomSongsCurrentPosition);
        }

        nowPlayingSongName = randomSongs.get(randomSongsCurrentPosition).getSongName();
        nowPlayingArtistName = randomSongs.get(randomSongsCurrentPosition).getArtistName();
        nowPlayingAlbumArt = randomSongs.get(randomSongsCurrentPosition).getImageID();
        nowPlayingCategory = randomSongs.get(randomSongsCurrentPosition).getCategory();

        //Update views and shared preferences
        updateViews();
        updateSharedPreferences();
    }

    /**
     * Method skips to the previous song
     */
    public void skipPreviousShuffledList() {
        //Figure out new Song Position
        //Skip Previous means the song in the index before the current one will be played
        //Nested condition checks to see if the index is 0. If so, it goes back to the last song on the list
        //If the randomSongsCurrentPosition is not 0, then subtract 1 to get new index position

        Log.v("NowPlaying", "Category: " + nowPlayingCategory + " at index " + randomSongsCurrentPosition);

        if (randomSongsCurrentPosition == 0) {
            randomSongsCurrentPosition = randomSongs.size() - 1;

        } else {
            //If the randomSongsCurrentPosition was not 0, then subtract 1 to get new index position
            randomSongsCurrentPosition--;
        }

        nowPlayingSongName = randomSongs.get(randomSongsCurrentPosition).getSongName();
        nowPlayingArtistName = randomSongs.get(randomSongsCurrentPosition).getArtistName();
        nowPlayingAlbumArt = randomSongs.get(randomSongsCurrentPosition).getImageID();
        nowPlayingCategory = randomSongs.get(randomSongsCurrentPosition).getCategory();

        //Update views and shared preferences
        updateViews();
        updateSharedPreferences();
    }

    /**
     * Method handles a skip previous click. Calls separate methods based on whether
     * the arraylist is a category based Songs list
     */
    public void skipPrevious() {
        //Method to use if the playlist type is the small, category list
        if (isCategoryArray) {
            skipPreviousCategoryList();

        } else {
            //Method to use for the shuffled array from MainActivity
            skipPreviousShuffledList();
        }
    }

    /**
     * Method handles a fast forward click. Calls separate methods based on whether
     * the arraylist is a category based Songs list
     */
    public void skipForward() {
        //Method to use if the playlist type is the small, category list
        if (isCategoryArray) {
            skipForwardCategoryList();
        } else {
            //Method to use if the playlist type is the random, shuffled list from the MainActivity
            skipForwardShuffledList();
        }
    }
}