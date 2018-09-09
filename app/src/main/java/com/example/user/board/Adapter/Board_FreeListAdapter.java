package com.example.user.board.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.board.Models.FreeList;
import com.example.user.board.R;

import java.util.List;

public class Board_FreeListAdapter extends BaseAdapter {

    private Context context;
    private List<FreeList> freeList;
    private Activity parentActivity;

    public Board_FreeListAdapter(Context context, List<FreeList> freeList, Activity parentActivity) {
        this.context = context;
        this.freeList = freeList;
        this.parentActivity = parentActivity;
    }
    @Override
    public int getCount() {
        return freeList.size();
    }

    @Override
    public Object getItem(int i) {
        return freeList.get(i);
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
            v = inflater.inflate(R.layout.item, null);
        }
        // final TextView Seq= (TextView) v.findViewById(R.id.seqTxt);
        final TextView TITLE= (TextView) v.findViewById(R.id.titleTxt);
        TextView CONTENT = (TextView) v.findViewById(R.id.contentTxt);
        TextView DATE= (TextView) v.findViewById(R.id.dateTxt);


        //Seq.setText(Integer.toString(i));
        //Seq.setText(freeList.get(i).getSeq());
        TITLE.setText(freeList.get(i).getTITLE());
        CONTENT.setText(freeList.get(i).getCONTENT());
        DATE.setText(freeList.get(i).getDATE());


        v.setTag(freeList.get(i).getTITLE());

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
