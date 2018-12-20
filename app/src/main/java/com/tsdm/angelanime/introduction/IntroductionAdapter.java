package com.tsdm.angelanime.introduction;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tsdm.angelanime.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mr.Quan on 2018/12/12.
 */

public class IntroductionAdapter extends RecyclerView.Adapter<IntroductionAdapter.ListItemHolder> {
    private List<String> mTitleList;
    private Context mContext;
    private MusicClickListener listener;

    public IntroductionAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addList(List<String> mTitleList) {
        this.mTitleList = mTitleList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detail_list, null);
        return new ListItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemHolder holder, final int position) {
        holder.tvListName.setText(mTitleList.get(position));

        if (position == mTitleList.size()-1){
            holder.tvListNew.setVisibility(View.VISIBLE);
        }else {
            holder.tvListNew.setVisibility(View.GONE);
        }
        holder.tvListName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                    listener.onItemClick(position);
                return false;
            }
        });
//        holder.tvListName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                listener.onItemClick(position);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mTitleList == null ? 0 : mTitleList.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_list_name)
        TextView tvListName;
        @BindView(R.id.tv_list_new)
        ImageView tvListNew;

        public ListItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnClickListener(MusicClickListener listener) {
        this.listener = listener;
    }

    public interface MusicClickListener {
        void onItemClick(int position);
    }
}
