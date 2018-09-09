package com.example.user.board.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.user.board.R;
import com.example.user.board.Request.Board_FreeInsertRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InsertActivity extends AppCompatActivity {

    Intent intent;
    // 현재시간을 msec 으로 구한다.
    long now = System.currentTimeMillis();
    // 현재시간을 date 변수에 저장한다.
    Date nowdate = new Date(now);
    // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
    SimpleDateFormat sdfNow = new SimpleDateFormat("MM/dd hh:mm");  //06/19 13:50
    // nowDate 변수에 값을 저장한다.
    String formatDate = sdfNow.format(nowdate);

    private long mSeq = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);


        final TextView titleTxt = (TextView) findViewById(R.id.titleTxt);
        final TextView contentTxt = (TextView) findViewById(R.id.contentTxt);
        final TextView dateTxt = (TextView) findViewById(R.id.dateTxt);
        final TextView userTxt = (TextView) findViewById(R.id.userTxt);
        dateTxt.setText(formatDate);
        intent = getIntent();

        String userID = intent.getStringExtra("IDText");
        userTxt.setText(userID);
        Button insertBtn = (Button) findViewById(R.id.insertBtn);
        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int seq = 0;
                String TITLE = titleTxt.getText().toString();
                String CONTENT = contentTxt.getText().toString();
                String DATE = dateTxt.getText().toString();


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Log.d("TAG","Insert success");
                                Toast.makeText(InsertActivity.this, "글이 게시되었습니다.", Toast.LENGTH_LONG).show();
                                new BackgroundTask().execute();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(InsertActivity.this);
                                builder.setMessage("작성하는 데 실패 하였습니다.")
                                        .setNegativeButton("다시 시도", null)
                                        .create()
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("TAG","Insert error");
                        }

                    }
                };
                Board_FreeInsertRequest board_freeInsertRequest = new Board_FreeInsertRequest(seq, TITLE, CONTENT, DATE, responseListener);
                RequestQueue queue = Volley.newRequestQueue(InsertActivity.this);
                queue.add(board_freeInsertRequest);
            }
        });
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://tkdanr2427.cafe24.com/Board_FreeList.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


        @Override
        public void onPostExecute(String result) {


            Intent intent = new Intent(InsertActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("freeList", result);
            Log.d("TAG","putExtra freeList : " + result);
            InsertActivity.this.startActivity(intent);
        }
    }
}
