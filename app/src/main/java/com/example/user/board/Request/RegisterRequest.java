package com.example.user.board.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */

public class RegisterRequest extends StringRequest {

    final static private String URL = "http://tkdanr2427.cafe24.com/UserRegister.php";
    private Map<String, String> parameters;

    public RegisterRequest(String userID, String userPassword, String userPhoneNumber, String userName, String userNumber, String userAddress, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userPassword", userPassword);
        parameters.put("userPhoneNumber", userPhoneNumber);
        parameters.put("userName",userName);
        parameters.put("userNumber", userNumber);
        parameters.put("userAddress", userAddress);

    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }


}
