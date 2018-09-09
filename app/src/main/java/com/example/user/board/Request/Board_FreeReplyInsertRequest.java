package com.example.user.board.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Board_FreeReplyInsertRequest extends StringRequest {

    final static private String URL = "http://tkdanr2427.cafe24.com/Board_FreeReplyInsert.php";
    private Map<String, String> parameters;

    public Board_FreeReplyInsertRequest(int seq, String REPLY, String DATE, Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("seq",seq + "");
        parameters.put("REPLY",REPLY);
        parameters.put("DATE",DATE);
        //parameters.put("userID",userID);

    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
