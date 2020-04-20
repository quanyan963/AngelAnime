package com.tsdm.angelanime.comment;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.bean.ReplyDetail;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mr.Quan on 2019/3/4.
 */

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {


    private Context mContext;
    private List<ReplyDetail> data;

    public ReplyAdapter(Context mContext, List<ReplyDetail> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_reply, parent,false);
        return new ReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        if (position<2){
            SpannableString spannableString = new SpannableString(data.get(position)
                    .getName()+":"+data.get(position).getCon());
            spannableString.setSpan(new ForegroundColorSpan(mContext.getResources()
                            .getColor(R.color.colorAccent)),0,data.get(position).getName().length()
                    , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.tvRCon.setText(spannableString);
        }
    }

    @Override
    public int getItemCount() {
        int count = data == null ? 0 : data.size();
        if (count > 2){
            count = 2;
        }
        return count;
    }

    public class ReplyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_r_con)
        TextView tvRCon;
        public ReplyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
