package activity;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import cs4330.utep.edu.errandsgo.R;

/**
 *
 * Created by oscarricaud on 4/10/17.
 */
public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private String fontPath;

    /* BEGIN GETTERS AND SETTERS */
    private String getFontPath() {
        return fontPath;
    }

    private void setFontPath(String fontPath) {
        this.fontPath = fontPath;
    }

    /* END GETTERS AND SETTERS */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setFontPath("fonts/brandonlight.TTF");
        TextView createAccount = (TextView) findViewById(R.id.link_signup);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_sign_up);
            }
        });
        //    changeFont(errandsGo);

        /* SET UP Toolbar After the user signs in
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {

    }

    /**
     * Changes the font of the activity
     *
     * @param context  Is the
     * @param textView the view we want to change font to
     */
    private void changeFont(TextView textView) {
        Typeface typeface = Typeface.createFromAsset(getAssets(), getFontPath());
        textView.setTypeface(typeface);
    }
}