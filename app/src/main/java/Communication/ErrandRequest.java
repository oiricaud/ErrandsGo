package Communication;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import model.Customer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oscarricaud on 5/2/17.
 */
public class ErrandRequest extends StringRequest {

    // This line connects to my domain. Please @see Register class for more details
    private static final String REGISTER_REQUEST_URL = "http://www.narped.com/errandsgo/Errands.php";
    private Map<String, String> params;

    /**
     * This method sends a POST request to the database.
     *
     * @param responseListener The listener listens to the responses from the user.
     */

    public ErrandRequest(Customer customer, Response.Listener<String> responseListener) {
        super(Method.POST, REGISTER_REQUEST_URL, responseListener, null);
        String customerNames = customer.getFirstName() + ", " + customer.getLastName();
        String phoneNumber = customer.getPhonenumber();
        String street = (customer.getStreet() + ", " + customer.getCity() + ", " + customer.getState());
        String email = customer.getEmail();
        String time = customer.getTime();
        String price = customer.getPrice();
        params = new HashMap<>();
        params.put("customerNames", customerNames);
        params.put("phoneNumber", phoneNumber);
        params.put("street", street);
        params.put("email", email);
        params.put("time", time);
        params.put("price", price);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}