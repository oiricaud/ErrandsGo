package activity;

import android.util.Log;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * Created by oscarricaud on 4/10/17.
 */
public class RegisterRequest extends StringRequest {

    // This line connects to my domain. Please @see Register class for more details
    private static final String REGISTER_REQUEST_URL = "http://www.narped.com/errandsgo/Register.php";
    private Map<String, String> params;

    /**
     * This method sends a POST request to the database.
     *
     * @param listener  The listener listens to the responses from the user.
     */

    public RegisterRequest(String firstname, String lastname, String phonenumber, String street,
                           String city, String state, String zipCode, String email, String password,
                           Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        Log.w("Firstname", firstname);
        Log.w("lastname", lastname);
        Log.w("phoneNumber", phonenumber);
        Log.w("street", street);
        Log.w("city", city);
        Log.w("state", state);
        Log.w("zipCode", zipCode);
        Log.w("email", email);
        Log.w("password", password);

        params = new HashMap<>();
        params.put("firstname", firstname);
        params.put("lastname", lastname);
        params.put("phonenumber", phonenumber);
        params.put("street", street);
        params.put("city", city);
        params.put("state", state);
        params.put("zipCode", zipCode);
        params.put("email", email);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}