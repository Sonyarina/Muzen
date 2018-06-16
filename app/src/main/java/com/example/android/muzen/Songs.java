package com.example.android.muzen;

/**
 * {@link Songs}
 * 4fields: Song Name, Artist Name, and image resource ID, and category.
 */
public class Songs {

    // Song Name
    private String mSongName;

    // Artist Name
    private String mArtistName;

    // Album Art (Drawable resource ID)
    private int mImageId;

    //Song Category
    private String mCategory;

    /*
     * Create a new Songs object using 3 parameters.
     *
     * @param vSongName is the Song Name
     * @param vArtistName is the Artist Name
     * @param vImageID is drawable reference ID
     * */
    public Songs(String vSongName, String vArtistName, int vImageID) {
        mSongName = vSongName;
        mArtistName = vArtistName;
        mImageId = vImageID;
    }

    /**
     * Create a new Songs object using 4 parameters.
     *
     * @param vSongName     is the String with the Song Name
     * @param vArtistName   is the String containing Artist Name
     * @param vImageID      is an int drawable reference ID
     * @param vCategoryName is a String with Category Name
     */
    public Songs(String vSongName, String vArtistName, int vImageID, String vCategoryName) {
        mSongName = vSongName;
        mArtistName = vArtistName;
        mImageId = vImageID;
        mCategory = vCategoryName;

    }

    /**
     * Get the Song Name
     */
    public String getSongName() {
        return mSongName;
    }

    /**
     * Get the Artist name
     */
    public String getArtistName() {
        return mArtistName;
    }

    /**
     * Get the image resource ID
     */
    public int getImageID() {
        return mImageId;
    }

    /**
     * Get the Category name
     */
    public String getCategory() {
        return mCategory;
    }
}
