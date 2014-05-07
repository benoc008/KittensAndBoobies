package com.example.KittensAndBoobies.Profile;

import com.example.KittensAndBoobies.Database.DataSource;
import com.example.KittensAndBoobies.Database.Record;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.example.KittensAndBoobies.Profile.AchiAdapter;
import com.example.KittensAndBoobies.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by benoc on 28/04/2014.
 */
public class ProfileActivity extends Activity implements ViewSwitcher.ViewFactory{

    //shared perferences
    private final String PREF_NAME = "MyKitten";
    private int kittyId;
    private String kittyName;
    private ImageView ib;

    //image switcher + popup
    private ImageSwitcher imageSwitcher;
    private DataSource datasource;
    private PopupWindow popupWindow;
    private boolean popup = false;

    //put new image refs here!
    private int imageIds[]={
            R.drawable.profile_kitten1,
            R.drawable.profile_kitten2,
            R.drawable.profile_kitten3,
            R.drawable.profile_kitten4
    };
    private int currentIndex = 0;

    float initialX;

    // db values
    private long bestScore;
    private long numGames;
    private long sumDuration;


    //achievements
    GridView gridView;
    //put achies here
    private List<Achievement> achis;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        //getting users kitten index
        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_WORLD_READABLE);
        kittyId = sp.getInt("MyKittenId", 1);
        kittyName = sp.getString("KittenName", "Mr. Meowington");
        currentIndex = kittyId;

        // when focus is lost on the name text field, the new data is saved
        final EditText nameText = (EditText) findViewById(R.id.textName);
        nameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_WORLD_READABLE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("KittenName", nameText.getText().toString());
                    editor.commit();
                    // hides the keyboard
                    InputMethodManager imm = (InputMethodManager) v.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        // when you click enter/done on the soft keyboard, the focus is lost -> saves the text
        nameText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    nameText.clearFocus();
                }
                return false;
            }
        });

        readDB();
        loadStats();
        loadAchies();
    }

    public void readDB(){
        datasource = new DataSource(this);
        datasource.open();
        List<Record> values = datasource.getAllRecords();
        datasource.close();
        if(values.size() == 0) {
            return;
        }
        bestScore = values.get(0).getScore();
        numGames = values.size();
        sumDuration = 0;
        for(Record r : values){
            sumDuration += r.getDuration();
        }
    }

    /**
     * Loads the stats bar from the database
     */
    public void loadStats(){

        TextView name = (TextView) findViewById(R.id.textName);
        name.setText(kittyName);

        TextView games = (TextView) findViewById(R.id.textGames);
        games.setText("" + numGames);

        TextView score = (TextView) findViewById(R.id.textScore);
        score.setText("" + bestScore);

        //duration
        Date durationDate = new Date(sumDuration - TimeZone.getDefault().getRawOffset());
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        TextView duration = (TextView) findViewById(R.id.textTime);
        duration.setText(formatter.format(durationDate));

        ib = (ImageView) findViewById(R.id.imageButton);
        ib.setImageResource(R.drawable.profile_kitten1 + (int) kittyId);
    }

    /**
     * Defines the achievements, then sets the adapter to the gridview
     * onclicklistener
     */
    public void loadAchies(){
        gridView = (GridView) findViewById(R.id.gridView);

        // Defining the achievements
        // idk, maybe we should get this from an xml?
        achis = new ArrayList<Achievement>();
        achis.add(new Achievement(this, R.drawable.achi_temp, "get 50000 scores", bestScore > 50000));
        achis.add(new Achievement(this, R.drawable.achi_temp, "get 100000 scores", bestScore > 100000));
        achis.add(new Achievement(this, R.drawable.achi_temp, "get 500000 scores", bestScore > 500000));
        achis.add(new Achievement(this, R.drawable.achi_temp, "get 1000000 scores", bestScore > 1000000));
        achis.add(new Achievement(this, R.drawable.achi_temp, "get 50000 score", bestScore > 50000));
        achis.add(new Achievement(this, R.drawable.achi_temp, "get 50000 score", bestScore > 50000));
        achis.add(new Achievement(this, R.drawable.achi_temp, "get 50000 score", bestScore > 50000));
        achis.add(new Achievement(this, R.drawable.achi_temp, "get 50000 score", bestScore > 50000));
        achis.add(new Achievement(this, R.drawable.achi_temp, "get 50000 score", bestScore > 50000));
        achis.add(new Achievement(this, R.drawable.achi_temp, "get 50000 score", bestScore > 50000));
        achis.add(new Achievement(this, R.drawable.achi_temp, "get 50000 score", bestScore > 50000));
        achis.add(new Achievement(this, R.drawable.achi_temp, "get 50000 score", bestScore > 50000));


        gridView.setAdapter(new AchiAdapter(this, achis));

        //the gettext thing might be better to get from a hidden textview??? (and put in the adapter)
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(
                        getApplicationContext(), achis.get(position).getText() , Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**
     * Creates popup window and initialises the imageSwitcher
     * @param view
     */
    public void changePicture(View view){
        if(popup){
            popupWindow.dismiss();
            popup = false;
            return;
        }
        popup = true;
        LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_image_picker, null);
        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.showAsDropDown(findViewById(R.id.imageButton), 10, 0);

        imageSwitcher = (ImageSwitcher) popupWindow.getContentView().findViewById(R.id.imageSwitcher);

        // Set the ViewFactory of the ImageSwitcher that will create ImageView object when asked
        imageSwitcher.setFactory(this);
        imageSwitcher.setImageResource(imageIds[currentIndex]);         // set up the current!!!
    }

    /**
     * Creates the ImageView and sets up animations and onTouch handler
     * @return
     */
    @Override
    public View makeView() {
        final Animation lin = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        final Animation lout = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);

        final Animation rin = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        final Animation rout = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);

        ImageView imageView = new ImageView(this){

            @Override
            public boolean onTouchEvent(MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i("asd","actionUp");
                        float finalX = event.getX();
                        if (initialX < finalX)
                        {
                            currentIndex++;
                            imageSwitcher.setInAnimation(lin);
                            imageSwitcher.setOutAnimation(lout);
                            changeImage();
                        }
                        else
                        {
                            currentIndex--;
                            imageSwitcher.setInAnimation(rin);
                            imageSwitcher.setOutAnimation(rout);
                            changeImage();
                        }
                        break;
                }
                return true;
            }

        };
        imageView.setBackgroundColor(0x00000000);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setLayoutParams(
                new ImageSwitcher.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        return imageView;
    }

    /**
     * Moves the index and sets the new image to the switcher
     */
    public void changeImage(){
        Log.i("asd","changeImage");
        if(currentIndex == imageIds.length)
            currentIndex = 0;
        else if(currentIndex == -1){
            currentIndex = imageIds.length - 1;
        }
        imageSwitcher.setImageResource(imageIds[currentIndex]);
    }

    /**
     * Sets the users new kitten then dismiss popupWindow
     * @param view
     */
    public void dismiss(View view){
        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("MyKittenId", currentIndex);
        editor.commit();

        ib.setImageResource(R.drawable.profile_kitten1 + currentIndex);

        popup = false;
        popupWindow.dismiss();
    }

    @Override
    public void onPause(){
        if(popup){
            popupWindow.dismiss();
        }
        super.onPause();
    }
}
