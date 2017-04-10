package activity;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * The RegisterRequest Class connects to my domain and a simple php file I obtained from the internet
 * view Register.php for more info. After the activity obtains the input from the button it sends a
 * request to the database.
 *
 * @author Oscar I. Ricaud
 * @version 1.0 Build November 16, 2016
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

    public RegisterRequest(String firstname, String email, String password, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("firstname", firstname);
        params.put("email", email);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}