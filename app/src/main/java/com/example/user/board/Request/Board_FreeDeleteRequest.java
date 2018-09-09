package com.example.user.board.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Board_FreeDeleteRequest extends StringRequest {

    final static private String URL = "http://tkdanr2427.cafe24.com/Board_FreeDelete.php";
    private Map<String, String> parameters;

    public Board_FreeDeleteRequest(String Title , Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("TITLE",Title);


    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
