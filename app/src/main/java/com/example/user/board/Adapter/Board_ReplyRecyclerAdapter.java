package com.example.user.board.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.board.Models.FreeList;
import com.example.user.board.Models.ReplyList;
import com.example.user.board.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class Board_ReplyRecyclerAdapter extends RecyclerView.Adapter<Board_ReplyRecyclerAdapter.ViewHolder> {

    private List<ReplyList> mData;
    public void swap(List<ReplyList> replyList) {
        mData = replyList;
        notifyDataSetChanged();
    }



    //Event Bus 클래스
    public static class ItemClickEvent {
        public ItemClickEvent(int position, long id) {
            this.position = position;
            this.id = id;
        }

        public int position;
        public long id;

    }
    //Event Bus 클래스
    public static class ItemLongClickEvent {
        public ItemLongClickEvent(int position, long id) {
            this.position = position;
            this.id = id;
        }

        public int position;
        public long id;

    }

    public Board_ReplyRecyclerAdapter(List<ReplyList> replyList) {
        mData = replyList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //뷰를 새로 만들 때
        View convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reply,parent,false);


        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // 데이터
        final ReplyList replyList = mData.get(position);


        holder.nameTextView.setText("익명");
        holder.replyTextView.setText(replyList.getREPLY());
        holder.dateTextView.setText(replyList.getREPLY());

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivity에 onItemClick이 받음
                EventBus.getDefault().post(new ItemClickEvent(position,replyList.get()));
                Log.d("TAG","onBindViewHolder memo.getId 값 : " + freeList.getId());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                EventBus.getDefault().post(new ItemLongClickEvent(position,freeList.getId()));
                return true;
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView replyTextView;
        TextView dateTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            // 레이아웃 들고 오기
            TextView nameTextView = (TextView) itemView.findViewById(R.id.nameTxt);
            TextView replyTextView = (TextView) itemView.findViewById(R.id.replyTxt);
            TextView dateTextView = (TextView) itemView.findViewById(R.id.dateTxt);

            this.nameTextView = nameTextView;
            this.replyTextView = replyTextView;
            this.dateTextView = dateTextView;

        }
    }
}
