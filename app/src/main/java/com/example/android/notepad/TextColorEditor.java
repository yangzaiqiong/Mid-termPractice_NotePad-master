package com.example.android.notepad;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

public class TextColorEditor extends Activity {
    /**
     * This is a special intent action that means "edit the background of a note".
     */
    public static final String EDIT_TITLE_ACTION = "com.android.notepad.action.EDIT_TEXT_COLOR";

    // Creates a projection that returns the note ID and the note background.
    private static final String[] PROJECTION = new String[] {
            NotePad.Notes._ID,               // Projection position 0, the note's id
            NotePad.Notes.COLUMN_NAME_TEXT_COLOR, //当前背景
            NotePad.Notes.COLUMN_NAME_TEXT_SIZE, //当前字体大小
    };

    // The position of the title column in a Cursor returned by the provider.
    private static final int COLUMN_INDEX_TITLE = 6;

    // A Cursor object that will contain the results of querying the provider for a note.
    private Cursor mCursor;

    // get the text color
    private String colorText;

    // get the text size
    private int textSize;

    // A URI object for the note whose title is being edited.
    private Uri mUri;

    /**
     * This method is called by Android when the Activity is first started. From the incoming
     * Intent, it determines what kind of editing is desired, and then does it.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_text_color_editor);

        mUri = getIntent().getData();
        mCursor = managedQuery(
                mUri,        // The URI for the note that is to be retrieved.
                PROJECTION,  // The columns to retrieve
                null,        // No selection criteria are used, so no where columns are needed.
                null,        // No where columns are used, so no where values are needed.
                null         // No sort order is needed.
        );


        final int[] sizes = {8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36};
        Spinner spinner = (Spinner)findViewById(R.id.sizes);
        spinner.setSelection(8);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textSize = sizes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if(mCursor.moveToFirst() == true){
                    int textSizeIndex = mCursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_TEXT_SIZE);

                    Log.e("nothing_selected_index", String.valueOf(textSizeIndex));

                    textSize = mCursor.getInt(textSizeIndex);
                }
                else {
                    textSize = 24;
                }
            }
        });

    }

    /**
     * This method is called when the Activity loses focus.
     *
     * For Activity objects that edit information, onPause() may be the one place where changes are
     * saved. The Android application model is predicated on the idea that "save" and "exit" aren't
     * required actions. When users navigate away from an Activity, they shouldn't have to go back
     * to it to complete their work. The act of going away should save everything and leave the
     * Activity in a state where Android can destroy it if necessary.
     *
     * Updates the note with the text currently in the text box.
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Verifies that the query made in onCreate() actually worked. If it worked, then the
        // Cursor object is not null. If it is *empty*, then mCursor.getCount() == 0.

        if (mCursor != null) {


            // Creates a values map for updating the provider.
            ContentValues values = new ContentValues();

            // In the values map, sets the title to the current contents of the edit box.
            Log.e("textSize", String.valueOf(textSize));

            if(colorText == null){
                if(mCursor.moveToFirst() == true){
                    int textColorIndex = mCursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_TEXT_COLOR);

                    Log.e("index", String.valueOf(textColorIndex));

                    colorText = mCursor.getString(textColorIndex);
                }
                else {
                    colorText = "#000000";
                }

            }
            Log.e("textColor", colorText);
            Log.e("count", String.valueOf(mCursor.getCount()));
            Log.e("columnCount", String.valueOf(mCursor.getColumnCount()));

            values.put(NotePad.Notes.COLUMN_NAME_TEXT_COLOR, colorText);
            values.put(NotePad.Notes.COLUMN_NAME_TEXT_SIZE, textSize);

            /*
             * Updates the provider with the note's new title.
             *
             * Note: This is being done on the UI thread. It will block the thread until the
             * update completes. In a sample app, going against a simple provider based on a
             * local database, the block will be momentary, but in a real app you should use
             * android.content.AsyncQueryHandler or android.os.AsyncTask.
             */
            getContentResolver().update(
                    mUri,    // The URI for the note to update.
                    values,  // The values map containing the columns to update and the values to use.
                    null,    // No selection criteria is used, so no "where" columns are needed.
                    null     // No "where" columns are used, so no "where" values are needed.
            );

        }
    }

    public void onClickSaveChange(View v) {
        finish();
    }

    public void turnBlue(View v){
        colorText = "#00FFCC";
    }

    public void turnWhite(View v){
        colorText = "#FFFFFF";
    }

    public void turnBlack(View v){
        colorText = "#000000";
    }

    public void turnYellow(View v){
        colorText = "#FFFF00";
    }

    public void turnRed(View v){
        colorText = "#FF0000";
    }

    public void turnPink(View v){
        colorText = "#FF3399";
    }



}
