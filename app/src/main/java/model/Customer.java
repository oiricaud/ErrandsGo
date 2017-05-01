package model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by oscarricaud on 4/30/17.
 */
public class Customer {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String homeAddress;
    private String creditCard;
    private String imagePath;

    public Customer(JSONObject jsonResponse) throws JSONException {
        this.firstName = jsonResponse.get("firstname").toString();
        this.lastName = jsonResponse.get("lastname").toString();
        this.phoneNumber = jsonResponse.get("phonenumber").toString();
        this.homeAddress = jsonResponse.get("homeaddress").toString();
        this.creditCard = jsonResponse.get("creditcard").toString();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public String getImagePath() {
        return imagePath;
    }
}
