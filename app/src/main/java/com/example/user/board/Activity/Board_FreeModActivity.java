package com.example.user.board.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.user.board.Adapter.Board_ReplyListAdapter;
import com.example.user.board.Adapter.Board_ReplyRecyclerAdapter;
import com.example.user.board.Models.FreeList;
import com.example.user.board.Models.ReplyList;
import com.example.user.board.R;
import com.example.user.board.Request.Board_FreeReplyInsertRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Board_FreeModActivity extends AppCompatActivity {

    Intent intent;
    TextView titleTxt, dateTxt, contentTxt;
    EditText ReplyTxt;
    Button ReplyBtn;
    RecyclerView reply_recycler;
    private String a;
    private Board_ReplyRecyclerAdapter mAdapter;
    private List<ReplyList> mReplyList;

    // 현재시간을 msec 으로 구한다.
    long now = System.currentTimeMillis();
    // 현재시간을 date 변수에 저장한다.
    Date nowdate = new Date(now);
    // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
    SimpleDateFormat sdfNow = new SimpleDateFormat("MM/dd hh:mm");  //06/19 13:50
    // nowDate 변수에 값을 저장한다.
    String formatDate = sdfNow.format(nowdate);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board__free_mod);

      /*  if(MainActivity.activity!=null){ //액티비티가 살아있다면
            MainActivity activity = (MainActivity) MainActivity.activity;
            activity.finish();
            Log.d("TAG","Board_FreeActivity : " + activity);
        }*/


        intent = getIntent();
        if (intent.getStringExtra("TITLE") != null) //getIntent가 있으면
        {
            new BackgroundTask().execute(); //댓글보여주기 실행
            Log.d("TAG","startReply!!!");
        }
        titleTxt = (TextView) findViewById(R.id.titleTxt);   //제목
        contentTxt = (TextView) findViewById(R.id.contentTxt); //내용
        titleTxt.setText(intent.getStringExtra("TITLE"));
        contentTxt.setText(intent.getStringExtra("CONTENT"));

        dateTxt = (TextView) findViewById(R.id.dateTxt);     //날짜
        dateTxt.setText(intent.getStringExtra("DATE")); // 게시판 작성 시간 저장

        ReplyBtn = (Button) findViewById(R.id.ReplyBtn);  //댓글달기 버튼
        ReplyTxt = (EditText) findViewById(R.id.ReplyTxt);  //댓글 입력
        reply_recycler = (RecyclerView) findViewById(R.id.reply_recycler);
        mReplyList = new ArrayList<ReplyList>();
       /* replyList.add(new ReplyList("댓글입니다.","날짜입니다."));
        replyList.add(new ReplyList("댓글이요.","오늘 날짜요."));*/
        mAdapter = new Board_ReplyRecyclerAdapter(mReplyList);
        reply_recycler.setAdapter(mAdapter);
        Log.i("태그", "setAdapter");

        Log.d("TAG","Reply a : " + a);
       /* try {

            JSONObject jsonObject = new JSONObject(a);
            Log.d("TAG", "getExtra replyList : " + jsonObject);
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;
            String REPLY, DATE;
            while (count < jsonArray.length()) {
                JSONObject object = jsonArray.getJSONObject(count);

                REPLY = object.getString("REPLY");

                DATE = object.getString("DATE");

                ReplyList reply = new ReplyList(REPLY, DATE);
                Log.d("TAG", "getIntent : " + reply);
                mReplyList.add(reply);
                count++;
                mAdapter.swap(mReplyList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/


        ReplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int seq = 0;

                String REPLY = ReplyTxt.getText().toString();
                String DATE = dateTxt.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                new BackgroundTask().execute();
                                Toast.makeText(getApplicationContext(), "댓글이 작성되었습니다.", Toast.LENGTH_LONG).show();

                                Log.i("TAG", "댓글 작성 Ok : " + success);

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Board_FreeModActivity.this);
                                builder.setMessage("작성하는 데 실패 하였습니다.")
                                        .setNegativeButton("다시 시도", null)
                                        .create()
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                Board_FreeReplyInsertRequest board_freeReplyInsertRequest = new Board_FreeReplyInsertRequest(seq, REPLY, DATE, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Board_FreeModActivity.this);
                queue.add(board_freeReplyInsertRequest);
            }
        });

    }


    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {  //Background 작업 시작전에 UI 작업을 진행 한다.
            Log.d("TAG", "onPreExecute : ");
            target = "http://tkdanr2427.cafe24.com/Board_FreeReplyList.php";
        }

        @Override
        protected String doInBackground(Void... voids) { //Background 작업을 진행 한다.
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
        public void onPostExecute(String result) { //Background 작업이 끝난 후 UI 작업을 진행 한다.
            //super.onPostExecute(result);

            Log.d("TAG","onPostExecute : " + a);

            try {
                JSONObject jsonObject = new JSONObject(result);
                Log.d("TAG","getExtra replyList : " + jsonObject);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String REPLY,DATE;
                while(count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);

                    REPLY = object.getString("REPLY");

                    DATE = object.getString("DATE");

                    ReplyList reply = new ReplyList(REPLY,DATE);
                    Log.d("TAG","getIntent : " + reply);
                    mReplyList.add(reply);
                    count++;
                    //mAdapter.swap(mReplyList);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

          /*  try {
                Log.i("태그","리스트뷰에 입력");
                JSONObject jsonObject = new JSONObject(a);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String REPLY2,DATE2;

                    JSONObject object = jsonArray.getJSONObject(count);
                    //Seq = object.getInt("Seq");
                    //Seq = object.getString("Seq");
                    REPLY2 = object.getString("REPLY");
                    DATE2 = object.getString("DATE");
                     refreshList();
                    Log.i("태그","리스트뷰에 입력");
                    ReplyList reply = new ReplyList(REPLY2,DATE2);
                    replyList.add(reply);



            } catch (Exception e) {
                e.printStackTrace();
            }
            adapter = new Board_ReplyListAdapter(getApplicationContext(), replyList,getParent());
            listview2.setAdapter(adapter);*//*


          /*  Intent intent = new Intent(Board_FreeModActivity.this, null);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("ReplyList", result);
            startActivity(intent);*/
        }
    }

    @Override
    public void onBackPressed() {
        // Alert을 이용해 종료시키기

        AlertDialog.Builder dialog = new AlertDialog.Builder(Board_FreeModActivity.this);
        dialog.setTitle("종료 알림")
                .setMessage("메인 화면으로 돌아가시겠습니까?")

                .setPositiveButton("예", new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Toast.makeText(Board_FreeModActivity.this, "메인화면로 돌아갑니다.", Toast.LENGTH_SHORT).show();
                    }

                })

                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(Board_FreeModActivity.this, "돌아가지 않습니다.", Toast.LENGTH_SHORT).show();

                    }

                }).create().show();
    } //뒤로가기 종료버튼

}
