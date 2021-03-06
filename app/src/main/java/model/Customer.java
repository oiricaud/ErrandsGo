package model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by oscarricaud on 4/30/17.
 */
public class Customer {
    private String firstName;
    private String lastName;
    private String phonenumber;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String email;
    private String password;
    private String time;
    private String price;
    private boolean isLoggedIn;

    public Customer() {
        this.isLoggedIn = false;
    }

    public Customer(JSONObject jsonResponse) throws JSONException {
        this.firstName = jsonResponse.get("firstname").toString();
        this.lastName = jsonResponse.get("lastname").toString();
        this.phonenumber = jsonResponse.get("phonenumber").toString();
        this.street = jsonResponse.get("street").toString();
        this.city = jsonResponse.get("city").toString();
        this.state = jsonResponse.get("state").toString();
        this.zip = jsonResponse.get("zipcode").toString();
        this.email = jsonResponse.get("email").toString();
        this.password = jsonResponse.get("password").toString();
        this.isLoggedIn = true;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public String getPhonenumber() {
        return phonenumber;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getZip() {
        return zip;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
