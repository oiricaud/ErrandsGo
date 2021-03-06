package activity;

import Communication.ErrandRequest;
import Communication.LoginRequest;
import Communication.RegisterRequest;
import adapter.ErrandsNamesRecyclerViewAdapter;
import adapter.ImageAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import cs4330.utep.edu.errandsgo.R;
import model.Customer;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by oscarricaud on 4/10/17.
 */
public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {
    List<String> list;
    private int[] imageId = {
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
    private String[] imageTitle = {
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
    private String[] imageDetails = {
            "Do you need a break from your children. Why not use one of ErrandsGo " +
                    "services and get one of the ErrandRunners to take care of your loved one.",
            "Why use uber when you can get a ride from your favorite errands app. Get a lift from point A to " +
                    "point B.",
            "Your time is more valuable than to clean your car. Get your car wash from your home. Begin a new " +
                    "productive life.",
            "Why clean your home when you can get someone else to do it for you. Spend your time on changing this " +
                    "world instead.",
            "Are you tired after a long day of work? Why drive to get food when food can come to you",
            "Get someone to pick up your laundry from your home, work or wherever you are at and get it done for a " +
                    "couple of dollars!",
            "If you think about it, you eat the same foods so why waste time in line at a groceries store when you " +
                    "can have someone else complete your grocery list.",
            "You work over 40 hours a week and you come home to a pet that has been in a cage all day but you're " +
                    "tired, get someone else to walk your pet for you!",
            "The city keeps sending you notices that you gotta clean your yard, but you're busy too work. You are a " +
                    "tap away from getting someone else do it for you!",
    };
    private Customer customer = new Customer();
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private SwipeRefreshLayout swipeLayout;
    private RecyclerView mRecyclerView;
    private ErrandsNamesRecyclerViewAdapter mErrandNamesRecyclerViewAdapter;
    private GridView grid;
    private RelativeLayout availableErrandsJobs;
    private RelativeLayout popUpBackground;
    private RelativeLayout errandsDetailsPopUp;
    private LinearLayout confirmatationPopUp;
    private TextView titleClicked;
    private ProgressBar loading;
    private EditText etTime;
    private EditText etMoneyWillingToSpend;

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

        final EditText etEmail = (EditText) findViewById(R.id.input_email);
        final EditText etPassword = (EditText) findViewById(R.id.input_password);
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.w("jsonResponse", jsonResponse.toString());
                    boolean success = jsonResponse.getBoolean("success");
                    if (success && (!isEmpty(etEmail) || !isEmpty(etPassword))) {
                        toast("Success logon!", 5000);
                        setContentView(R.layout.activity_main);

                        launchHomeView();
                        customer = new Customer(jsonResponse); // load customer before launching the home view
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        toast("Failed, please try again", 5000);
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
        popUpBackground = (RelativeLayout) findViewById(R.id.popupwithbackground);
        errandsDetailsPopUp = (RelativeLayout) findViewById(R.id.errand_details);
        availableErrandsJobs = (RelativeLayout) findViewById(R.id.available_errands);
        confirmatationPopUp = (LinearLayout) findViewById(R.id.confirmation_popup);
        titleClicked = (TextView) findViewById(R.id.titleClicked);
        final FloatingActionButton closePopUp = (FloatingActionButton) findViewById(R.id.closePopup);
        final TextView details = (TextView) findViewById(R.id.details);
        final ImageView imageIcon = (ImageView) findViewById(R.id.imageIcon);
        final Button confirm = (Button) findViewById(R.id.confirm);
        loading = (ProgressBar) findViewById(R.id.loadingCircle);
        loading.setVisibility(View.GONE); // Hide loading
        grid = (GridView) findViewById(R.id.grid_view);
        grid.setVisibility(GridView.VISIBLE);
        closePopUp.bringToFront();
        popUpBackground.setVisibility(RelativeLayout.GONE);
        errandsDetailsPopUp.setVisibility(RelativeLayout.GONE);
        availableErrandsJobs.setVisibility(RelativeLayout.GONE);
        confirmatationPopUp.setVisibility(LinearLayout.GONE);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        titleClicked.setVisibility(TextView.VISIBLE);
        titleClicked.bringToFront();

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        mToolbar.setTitle("Home");

        /* Grid Images Listener */
        ImageAdapter adapter = new ImageAdapter(MainActivity.this, imageTitle, imageId);

        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popUpBackground.setVisibility(RelativeLayout.VISIBLE);
                errandsDetailsPopUp.setVisibility(RelativeLayout.VISIBLE);
                grid.setVisibility(GridView.INVISIBLE);
                titleClicked.setText(imageTitle[position]);
                details.setText(imageDetails[position]);
                imageIcon.setBackgroundResource(imageId[position]);
                imageIcon.bringToFront();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmView();
                confirm.setVisibility(Button.INVISIBLE);
            }
        });

        closePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpBackground.setVisibility(RelativeLayout.INVISIBLE);
                errandsDetailsPopUp.setVisibility(RelativeLayout.INVISIBLE);
                confirmatationPopUp.setVisibility(LinearLayout.INVISIBLE);
                grid.setVisibility(GridView.VISIBLE);
                titleClicked.setVisibility(TextView.VISIBLE);
                confirm.setVisibility(Button.VISIBLE);
            }
        });
    }

    private void confirmView() {
        errandsDetailsPopUp.setVisibility(RelativeLayout.INVISIBLE);
        confirmatationPopUp.setVisibility(LinearLayout.VISIBLE);
        TextView errandClicked = (TextView) findViewById(R.id.errand_title);
        errandClicked.setText(titleClicked.getText());
        titleClicked.setVisibility(TextView.INVISIBLE);
        autoFillForum(customer);
        Button finalize = (Button) findViewById(R.id.finalize);
        finalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Confirm errand?")
                        .setMessage("Are you sure you want to confirm this errand? Once an ErrandRunner approves your" +
                                " request you will not be able to cancel the errand and you will be charged.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                loading.setVisibility(View.VISIBLE); // Show loading circle
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loading.setVisibility(View.GONE); // Hide loading circle
                                    }
                                }, 5500);
                                paymentView();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(R.drawable.ic_eye)
                        .show();
            }
        });
    }

    private void paymentView() {
        // Verify Credit Card here, skip for now

        // Send data to database
        customer.setTime(etTime.getText().toString());
        customer.setPrice(etMoneyWillingToSpend.getText().toString());

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        toast("Errand requested", 5000);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        toast("Failed, please internet connection error", 5000);
                        builder.setMessage("Failed").setNegativeButton("Retry", null).create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        // The next 3 lines calls the @see RegisterRequest class.
        ErrandRequest registerRequest = new ErrandRequest(customer, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(registerRequest);
        toast("Success requesting an Errand", 10000);
        launchHomeView();
    }

    private void autoFillForum(Customer customer) {
        /* Prepare data */
        final EditText etName = (EditText) findViewById(R.id.first_name);
        final EditText etPhone_number = (EditText) findViewById(R.id.phone_number);
        final EditText etAddress = (EditText) findViewById(R.id.street);
        final EditText etEmail = (EditText) findViewById(R.id.input_email);
        etTime = (EditText) findViewById(R.id.input_time);
        etMoneyWillingToSpend = (EditText) findViewById(R.id.input_price);

        /* Auto load fields */
        etName.setText(customer.getFirstName() + ", " + customer.getLastName());
        etPhone_number.setText(customer.getPhonenumber());
        etAddress.setText(customer.getStreet() + ", " + customer.getCity() + ", " + customer.getState());
        etEmail.setText(customer.getEmail());
        etTime.setText("9:00am");
        etMoneyWillingToSpend.setText("e.g $10.00");

    }

    private void availableErrandsView() {
        grid.setVisibility(RelativeLayout.INVISIBLE);
        availableErrandsJobs.setVisibility(RelativeLayout.VISIBLE);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.activity_main_recyclerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mToolbar.setTitle("Available jobs");
        setUpErrands();


        swipeLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setUpErrands();
                        swipeLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });
    }

    private void setUpErrands() {
        mErrandNamesRecyclerViewAdapter = new ErrandsNamesRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mErrandNamesRecyclerViewAdapter);
    }

    /**
     * This method waits for the user to to type in their information then creates an account and stores it to my
     * database.
     */
    private void createAccount() {
        setContentView(R.layout.activity_sign_up);
        /* Prepare data */
        final EditText etFirstname = (EditText) findViewById(R.id.first_name);
        final EditText etLastName = (EditText) findViewById(R.id.last_name);
        final EditText etPhone_number = (EditText) findViewById(R.id.phone_number);
        final EditText etStreet = (EditText) findViewById(R.id.street);
        final EditText etCity = (EditText) findViewById(R.id.city);
        final EditText etState = (EditText) findViewById(R.id.state);
        final EditText etZipCode = (EditText) findViewById(R.id.zip_code);
        final EditText etEmail = (EditText) findViewById(R.id.input_email);
        final EditText etPassword = (EditText) findViewById(R.id.input_password);
        final Button btn_signup = (Button) findViewById(R.id.btn_signup);
        final TextView link_login = (TextView) findViewById(R.id.link_login);

        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeView();
            }
        });

        // User presses the "No account yet? Create one"
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstName = etFirstname.getText().toString();
                final String lastName = etLastName.getText().toString();
                final String phoneNumber = etPhone_number.getText().toString();
                final String street = etStreet.getText().toString();
                final String city = etCity.getText().toString();
                final String state = etState.getText().toString();
                final String zipcode = etZipCode.getText().toString();
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                toast("Success creating account", 5000);
                                restartActivity();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                toast("Failed, please try again", 5000);
                                builder.setMessage("Register Failed").setNegativeButton("Retry", null).create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                // The next 3 lines calls the @see RegisterRequest class.
                RegisterRequest registerRequest = new RegisterRequest(firstName, lastName, phoneNumber, street, city,
                        state, zipcode, email, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(registerRequest);
            }
        });
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
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
        if (position == 0) {
            Log.w("Home ", "Going home");
            confirmatationPopUp.setVisibility(LinearLayout.INVISIBLE);
            drawerFragment.closeDrawers();
            launchHomeView();
        }
        if (position == 1) {
            Log.w("Errands ", "Going to do errands");
            drawerFragment.closeDrawers();
            availableErrandsView();
        }
        if (position == 2) {
            Log.w("Log out ", "logging out");
            restartActivity();
        }
    }

    /**
     * Show a toast message.
     */
    private void toast(String msg, int lengthOfToast) {
        final Toast toast = Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
        new CountDownTimer(500, lengthOfToast) {
            public void onTick(long millisUntilFinished) {
                toast.show();
            }

            public void onFinish() {
                toast.cancel();
            }
        }.start();
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() <= 0;
    }


}