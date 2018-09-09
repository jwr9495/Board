package com.example.user.board.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.board.Adapter.BoardRecyclerAdapter;
import com.example.user.board.Adapter.Board_FreeListAdapter;
import com.example.user.board.Models.FreeList;
import com.example.user.board.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    public static final int REQUEST_CODE_NEW_MEMO = 1000;
    public static final int REQUEST_CODE_UPDATE_MEMO = 1001;
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView freeboard_recycler;
    public static MainActivity activity = null;

    //private Board_FreeListAdapter adapter;
    private BoardRecyclerAdapter mAdapter;
    private List<FreeList> mFreeList;
    Intent intent;
    private int REQUEST_ACT = 1002;
    private String task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity =this;

        intent = getIntent();
        final String IDText = intent.getStringExtra("userID");

        TextView idText = (TextView) findViewById(R.id.idText);
        idText.setText(IDText + "님 환영합니다.");

        freeboard_recycler = (RecyclerView) findViewById(R.id.freeboard_recycler);

        mFreeList = new ArrayList<FreeList>();
        mAdapter = new BoardRecyclerAdapter(mFreeList);
        freeboard_recycler.setAdapter(mAdapter);
        mAdapter.swap(mFreeList);
        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("freeList"));
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;
            String TITLE,CONTENT,DATE;
            while(count < jsonArray.length())
            {
                JSONObject object = jsonArray.getJSONObject(count);
                //Seq = object.getInt("Seq");
                //Seq = object.getString("Seq");
                TITLE = object.getString("TITLE");
                CONTENT =object.getString("CONTENT");
                DATE = object.getString("DATE");

                FreeList free = new FreeList(TITLE,CONTENT,DATE);
                Log.d("TAG","getIntent : " + free);
                mFreeList.add(free);
                count++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        Button insertBtn = (Button) findViewById(R.id.insertBtn);
        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InsertActivity.class);
                //intent.putExtra("IDText",IDText);

                startActivity(intent);
            }
        });




    } // onCreate

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }//onStart

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }//onStop
    // 보낸이 : MemoRecyclerAdapter
    @Subscribe
    public void onItemClick(BoardRecyclerAdapter.ItemClickEvent event) {
        FreeList freeList = mFreeList.get(event.position);

        Intent intent = new Intent(this, Board_FreeModActivity.class);
        intent.putExtra("TITLE",mFreeList.get(event.position).getTITLE());
        intent.putExtra("CONTENT",mFreeList.get(event.position).getCONTENT());
        intent.putExtra("DATE",mFreeList.get(event.position).getDATE());

        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        //startActivityForResult(intent, REQUEST_ACT);
        //new BackgroundTask().execute();
        Log.d("TAG","MainActivity 1: " + activity);
        Log.d("TAG","putExtra : " + intent);

    }

    @Override
    public void onBackPressed() {
        // Alert을 이용해 종료시키기

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

        dialog.setTitle("종료 알림")

                .setMessage("정말 종료하시겠습니까?")

                .setPositiveButton("종료합니다", new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                    }

                })

                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(MainActivity.this, "종료하지 않습니다", Toast.LENGTH_SHORT).show();

                    }

                }).create().show();

    } //뒤로가기 종료버튼

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://tkdanr2427.cafe24.com/Board_FreeReplyList.php";
        }

        @Override
        protected String doInBackground(Void... voids){
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);
        }

        @Override
        public void onPostExecute(String result) {


            Intent intent  = new Intent(MainActivity.this,Board_FreeModActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("replyList", result);
            Log.d("TAG","putExtra : replyList : " + result);
            Log.d("TAG","MainActivity 2: " + activity);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            //startActivityForResult(intent,REQUEST_ACT);
        }
    }
}

