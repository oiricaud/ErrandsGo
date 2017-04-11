package activity;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by oscarricaud on 4/10/17.
 */
public class LoginRequest extends StringRequest {

    // This line connects to my domain. Please @see Register class for more details
    private static final String REGISTER_REQUEST_URL = "http://www.narped.com/errandsgo/Login.php";
    private Map<String, String> params;

    /**
     * This method sends a POST request to the database.
     *
     * @param username The username the user needs to be able to log in to their account.
     * @param password The user's password.
     * @param listener The listener listens to the responses from the user.
     */

    public LoginRequest(String email, String password, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}