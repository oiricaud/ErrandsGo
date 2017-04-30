package activity;

import adapter.ImageAdapter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import cs4330.utep.edu.errandsgo.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 *
 * Created by oscarricaud on 4/10/17.
 */
public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    List<String> list;
    int[] imageId = {
            R.drawable.ic_stroller,
            R.drawable.ic_car,
            R.drawable.ic_carwash,
            R.drawable.ic_house_clean,
            R.drawable.ic_food_delivery,
            R.drawable.ic_laundry,
            R.drawable.ic_groceries,
            R.drawable.ic_pet,
            R.drawable.ic_fence,
    };
    String[] imageTitle = {
            "Baby sit",
            "Car Ride",
            "Car Wash",
            "Housekeeping",
            "Take Out",
            "Laundry",
            "Groceries",
            "Walk pet",
            "Yard Services",
    };
    String[] imageDetails = {
            "Are you often busy at times with work or school? You can hire a Baby sitter but that takes too much time" +
                    ". Why not use one of ErrandsGo services and get one of the ErrandRunners to take care of your " +
                    "loved one. You can view their ratings and pictures of the ErrandRunner! ",
            " Why use uber when you can get a ride from your favorite errands app. Get a lift from point A to " +
                    "point B.",
            "Your time is more valuable than to clean your car. Get your car wash from your home. Begin a new " +
                    "productive life.",
            "Why clean your home when you can get someone else to do it for you. Spend your time on changing this " +
                    "world instead.",
            "Are you tired after a long day of work? Why drive to get food restaurant when food can come to you",
            "Laundry",
            "Groceries",
            "Walk pet",
            "Yard Services",
    };
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView createAccount = (TextView) findViewById(R.id.link_signup);
        Button login = (Button) findViewById(R.id.btn_login);

        // If user decides to create an account, change view
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
        // If the user decides to log in
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }

    /**
     * This method verifies the users credentials and if they are correct, it launches the home menu.
     */
    private void userLogin() {
        final EditText etUsername = (EditText) findViewById(R.id.input_email);
        final EditText etPassword = (EditText) findViewById(R.id.input_password);
        final String email = etUsername.getText().toString();
        final String password = etPassword.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        toast("Success logon!");
                        launchHomeView();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        toast("Failed, please try again");
                        builder.setMessage("Login Failed").setNegativeButton("Retry", null).create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(email, password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(loginRequest);
    }

    private void launchHomeView() {
        setContentView(R.layout.activity_main);
        final RelativeLayout popUpBackground = (RelativeLayout) findViewById(R.id.popupwithbackground);
        final RelativeLayout popUpBorder = (RelativeLayout) findViewById(R.id.popupblackborder);
        final TextView titleClicked = (TextView) findViewById(R.id.titleClicked);
        final FloatingActionButton closePopUp = (FloatingActionButton) findViewById(R.id.closePopup);
        final TextView details = (TextView) findViewById(R.id.details);
        popUpBackground.setVisibility(RelativeLayout.GONE);
        popUpBorder.setVisibility(RelativeLayout.GONE);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        mToolbar.setTitle("Pick an errand");
        /* Grid Images Listener */
        ImageAdapter adapter = new ImageAdapter(MainActivity.this, imageTitle, imageId);
        final GridView grid = (GridView) findViewById(R.id.grid_view);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popUpBackground.setVisibility(RelativeLayout.VISIBLE);
                popUpBorder.setVisibility(RelativeLayout.VISIBLE);
                grid.setVisibility(GridView.INVISIBLE);
                titleClicked.setText(imageTitle[position]);
                details.setText(imageDetails[position]);
            }
        });
        closePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpBackground.setVisibility(RelativeLayout.INVISIBLE);
                popUpBorder.setVisibility(RelativeLayout.INVISIBLE);
                grid.setVisibility(GridView.VISIBLE);
            }
        });
    }

    /**
     * This method waits for the user to to type in their information then creates an account and stores it to my
     * database.
     */
    private void createAccount() {
        setContentView(R.layout.activity_sign_up);
          /* Prepare data */
        final EditText etFirstname = (EditText) findViewById(R.id.input_name);
        final EditText etEmail = (EditText) findViewById(R.id.input_email);
        final EditText etPassword = (EditText) findViewById(R.id.input_password);
        final Button btn_signup = (Button) findViewById(R.id.btn_signup);

        // User presses the "No account yet? Create one"
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstname = etFirstname.getText().toString();
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                toast("Success creating account");
                                //userLogin(etEmail, etPassword);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                toast("Failed, please try again");
                                builder.setMessage("Register Failed").setNegativeButton("Retry", null).create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                // The next 3 lines calls the @see RegisterRequest class.
                RegisterRequest registerRequest = new RegisterRequest(firstname, email, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(registerRequest);
            }
        });
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


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {

    }

    /**
     * Show a toast message.
     */
    private void toast(String msg) {
        final Toast toast = Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
        new CountDownTimer(500, 10000) {
            public void onTick(long millisUntilFinished) {
                toast.show();
            }

            public void onFinish() {
                toast.cancel();
            }
        }.start();
    }
}