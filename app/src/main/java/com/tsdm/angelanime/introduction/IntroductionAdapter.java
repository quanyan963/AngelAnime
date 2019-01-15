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
    private int position;

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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detail_list, parent, false);
        return new ListItemHolder(view);
    }

    public void setPosition(int position) {
        int temp = this.position;
        this.position = position;
        notifyItemChanged(temp);
        notifyItemChanged(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemHolder holder, final int position) {
        holder.tvListName.setText(mTitleList.get(position));

        if (position == 0){
            holder.vLeft.setVisibility(View.VISIBLE);
        }else {
            holder.vLeft.setVisibility(View.GONE);
        }
        if (this.position == position) {
            holder.tvListName.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            holder.tvListName.setBackgroundResource(R.drawable.blue_rectangle_selected);
        } else {
            holder.tvListName.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.tvListName.setBackgroundResource(R.drawable.blue_rectangle_unselected);
        }
        holder.tvListName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        view.setBackgroundResource(R.drawable.blue_rectangle_selecting);
                        break;
                    case MotionEvent.ACTION_UP:
                        listener.onItemClick(position);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        ((TextView) view).setTextColor(mContext.getResources().getColor(R.color.black));
                        view.setBackgroundResource(R.drawable.blue_rectangle_unselected);
                        break;
                }
                return false;
            }
        });
        if (position == mTitleList.size() - 1) {
            holder.tvListNew.setVisibility(View.VISIBLE);
        } else {
            holder.tvListNew.setVisibility(View.GONE);
        }
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
        @BindView(R.id.v_left)
        View vLeft;
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
