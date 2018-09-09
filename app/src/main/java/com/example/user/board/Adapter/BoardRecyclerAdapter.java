package com.example.user.board.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.board.Models.FreeList;
import com.example.user.board.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class BoardRecyclerAdapter extends RecyclerView.Adapter<BoardRecyclerAdapter.ViewHolder> {

    private List<FreeList> mData;
    public void swap(List<FreeList> freeList) {
        mData = freeList;
        notifyDataSetChanged();
    }
    public void insert(List<FreeList> freeList) {
        mData = freeList;
        //notifyItemInserted(0);
        notifyDataSetChanged();

    }
    public void update(List<FreeList> freeList, int position) {
        mData = freeList;
        notifyItemChanged(position);
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

    public BoardRecyclerAdapter(List<FreeList> freeList) {
        mData = freeList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //뷰를 새로 만들 때
        View convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item,parent,false);


        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // 데이터
        final FreeList freeList = mData.get(position);

        // 화면에 뿌리기
        holder.titleTextView.setText(freeList.getTITLE());
        holder.contentTextView.setText(freeList.getCONTENT());
        holder.dateTextView.setText(freeList.getDATE());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivity에 onItemClick이 받음
                EventBus.getDefault().post(new ItemClickEvent(position,freeList.getId()));
                Log.d("TAG","onBindViewHolder memo.getId 값 : " + freeList.getId());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                EventBus.getDefault().post(new ItemLongClickEvent(position,freeList.getId()));
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView contentTextView;
        TextView dateTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            // 레이아웃 들고 오기
            TextView titleTextView = (TextView) itemView.findViewById(R.id.titleTxt);
            TextView contentTextView = (TextView) itemView.findViewById(R.id.contentTxt);
            TextView dateTextView = (TextView) itemView.findViewById(R.id.dateTxt);

            this.titleTextView = titleTextView;
            this.contentTextView = contentTextView;
            this.dateTextView = dateTextView;

        }
    }
}
