package com.example.user.board.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.board.Models.ReplyList;
import com.example.user.board.R;

import java.util.List;

public class Board_ReplyListAdapter extends BaseAdapter {

    private Context context;
    private List<ReplyList> replyList;
    private Activity parentActivity;

    public Board_ReplyListAdapter(Context context, List<ReplyList> replyList, Activity parentActivity) {
        this.context = context;
        this.replyList = replyList;
        this.parentActivity = parentActivity;
    }
    @Override
    public int getCount() {
        return replyList.size();
    }

    @Override
    public Object getItem(int i) {
        return replyList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = view;
        if(v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.reply, null);
        }

        TextView NAME = (TextView) v.findViewById(R.id.nameTxt);
        TextView DATE = (TextView) v.findViewById(R.id.dateTxt);
        TextView REPLY = (TextView) v.findViewById(R.id.replyTxt);

        //NAME.setText(replyList.get(i).get));
        NAME.setText("익명");
        REPLY.setText(replyList.get(i).getREPLY());
        DATE.setText(replyList.get(i).getDATE());



        v.setTag(replyList.get(i).getREPLY());

   /*     Button delBtn = (Button) v.findViewById(R.id.delBtn);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success) {
                                freeList.remove(i);
                                notifyDataSetChanged();
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                Board_FreeDeleteRequest board_freeDeleteRequest = new Board_FreeDeleteRequest(TITLE.getText().toString() , responseListener);
                RequestQueue queue = Volley.newRequestQueue(parentActivity);
                queue.add(board_freeDeleteRequest);

            }
        });*/
        return v;
    }
}
