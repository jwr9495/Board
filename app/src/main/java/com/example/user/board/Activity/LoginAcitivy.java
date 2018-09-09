package com.example.user.board.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.board.Kakao.SessionCallback;
import com.example.user.board.R;
import com.example.user.board.Request.LoginRequest;
import com.kakao.auth.AuthType;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.kakao.auth.Session;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginAcitivy extends AppCompatActivity {
    // private Context mContext;

    public static LoginAcitivy activity = null;
    private Button btn_custom_login;
    // private LoginButton btn_kakao_login;

    private long lastTimeBackPressed;

    CheckBox chk_auto;
    TextView idText, passwordText;
    Button loginButton, registerButton;
    Intent intent;

    SharedPreferences setting;    //아이디 저장 기능
    SharedPreferences.Editor editor;

    private AlertDialog dialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acitivy);
       /* mContext = getApplicationContext();
        getHashKey(mContext);
*/


        btn_custom_login = (Button) findViewById(R.id.btn_custom_login);
        btn_custom_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Session session = Session.getCurrentSession();
                session.addCallback(new SessionCallback());
                session.open(AuthType.KAKAO_LOGIN_ALL, LoginAcitivy.this);
                // btn_kakao_login.performClick();
            }
        });
        //btn_kakao_login = (LoginButton) findViewById(R.id.btn_kakao_login);

        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String userID = idText.getText().toString();
                String userPassword = passwordText.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginAcitivy.this);
                                dialog = builder.setMessage("로그인에 성공했습니다.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                       /* Intent intent  = new Intent(LoginAcitivy.this, MgMainActivity.class);
                                                        startActivity(intent);

                                                            Intent intent  = new Intent(LoginAcitivy.this, MainActivity.class);
                                                            intent.putExtra("userID",userID);
                                                            startActivity(intent);*/
                                                new BackgroundTask().execute();
                                                finish();
                                                Log.d("TAG", "LoginActivity : " + activity);

                                            }
                                        }).create();
                                dialog.show();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginAcitivy.this);
                                dialog = builder.setMessage("계정을 다시 확인하세요..")
                                        .setNegativeButton("다시 시도", null)
                                        .create();
                                dialog.show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginAcitivy.this);
                queue.add(loginRequest);

            }
        });
        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);

            }

        });

        chk_auto = (CheckBox) findViewById(R.id.chk_auto);

        setting = getSharedPreferences("setting", 0);

        editor = setting.edit();

        if (setting.getBoolean("chk_auto", false)) {

            idText.setText(setting.getString("ID", ""));

            //et_pw.setText(setting.getString("PW", "")); 패스워드

            chk_auto.setChecked(true);

        }
        //loginButton.setOnClickListener(this);

        chk_auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (chk_auto.isChecked()) {


                    String ID = idText.getText().toString();

                    //String PW = et_pw.getText().toString();

                    editor.putString("ID", ID);
                    //editor.putString("PW", PW);
                    editor.putBoolean("chk_auto", true);
                    editor.commit();
                } else {
                    editor.clear();
                    editor.commit();
                }
            }
        }); // 아이디 저장 기능

    }
// 프로젝트의 해시키를 반환

   /* @Nullable

    public static String getHashKey(Context context) {

        //final String TAG = "KeyHash";

        String keyHash = null;

        try {

            PackageInfo info =

                    context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);



            for (Signature signature : info.signatures) {

                MessageDigest md;

                md = MessageDigest.getInstance("SHA");

                md.update(signature.toByteArray());

                keyHash = new String(Base64.encode(md.digest(), 0));

                Log.d("Test", keyHash);

            }

        } catch (Exception e) {

            Log.e("name not found", e.toString());

        }



        if (keyHash != null) {

            return keyHash;

        } else {

            return null;

        }

    }*/


    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }


    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
            System.exit(0);
            return;
        }

        Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show();
        lastTimeBackPressed = System.currentTimeMillis();
    } //뒤로가기 종료버튼

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

            Intent intent = new Intent(LoginAcitivy.this, MainActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("freeList", result);

            startActivity(intent);
        }
    }
}
