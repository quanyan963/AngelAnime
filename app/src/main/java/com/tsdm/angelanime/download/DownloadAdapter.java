package com.tsdm.angelanime.download;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tsdm.angelanime.R;
import com.tsdm.angelanime.application.MyApplication;
import com.tsdm.angelanime.bean.FileInformation;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lzy.okgo.model.Progress.ERROR;
import static com.lzy.okgo.model.Progress.FINISH;
import static com.lzy.okgo.model.Progress.LOADING;
import static com.lzy.okgo.model.Progress.PAUSE;

/**
 * Created by Mr.Quan on 2019/3/28.
 */

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder> {
    private Context context;
    private List<FileInformation> data = new ArrayList<>();
    private OnViewClickListener listener;

    public DownloadAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<FileInformation> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setDownloading(FileInformation information) {
        data.add(0, information);
        notifyItemInserted(0);
    }

    public void updateData(int position, FileInformation information) {
        data.set(position, information);
        notifyItemChanged(position);
    }

    public void deleteData(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }


    public void setListener(OnViewClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public DownloadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_download, null);
        return new DownloadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DownloadViewHolder holder, int position) {
        if (data.get(position).getCreateDate() == null || data.get(position).getCreateDate().equals("")) {
            if (data.get(position).getFileName() == null || data.get(position).getFileName().equals("")) {
                holder.tvFileName.setText(R.string.initing);
            } else {
                holder.tvFileName.setText(data.get(position).getFileName());
            }
            holder.rlRight.setVisibility(View.VISIBLE);
            holder.rlComplete.setVisibility(View.GONE);
            holder.ivPause.setVisibility(View.VISIBLE);
            holder.pbFileDownload.setProgress(data.get(position).getProgress());
            holder.tvFilePercent.setText(data.get(position).getProgress() == 0 ? "" : data
                    .get(position).getProgress() + "%");
            if (data.get(position).getState() == LOADING) {
                holder.tvFileState.setText(R.string.downloading_item);
                holder.ivPause.setImageResource(R.mipmap.pause);
            } else if (data.get(position).getState() == PAUSE) {
                holder.tvFileState.setText(R.string.pause_waite);
                holder.ivPause.setImageResource(R.mipmap.play);
            } else if (data.get(position).getState() == ERROR) {
                holder.tvFileName.setText(R.string.parse_error);
                holder.ivPause.setImageResource(R.mipmap.play);
                holder.tvFileState.setText("");
            } else if (data.get(position).getState() == FINISH) {
                String path = MyApplication.downloadPath + data.get(position).getFileName();
                File file = new File(path);
                notifyItemRemoved(position);
                if (file.exists()){
                    data.add(new FileInformation(data.get(position).getFileName(),
                            new SimpleDateFormat("yyyy-MM-dd HH:mm")
                            .format(new Date(file.lastModified()))));
                }
                data.remove(position);

            }
        } else {
            holder.ivPause.setVisibility(View.GONE);
            holder.rlRight.setVisibility(View.GONE);
            holder.rlComplete.setVisibility(View.VISIBLE);
            holder.tvCompleteName.setText(data.get(position).getFileName());
            holder.tvCreateTime.setText(data.get(position).getCreateDate());
        }

        holder.ivPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onViewClick(data.get(holder.getAdapterPosition()).getId());
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onViewDelete(holder.getAdapterPosition(), data.get(holder.getAdapterPosition()).getId());
                //notifyItemRemoved(position);
            }
        });

        holder.rlComplete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        holder.rlAll.setBackgroundResource(R.color.grey_back);
                        holder.ivDelete.setBackgroundResource(R.color.grey_back);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        holder.rlAll.setBackgroundResource(R.color.white);
                        holder.ivDelete.setBackgroundResource(R.drawable.ripple_item_layout_effect_white);
                        break;
                    case MotionEvent.ACTION_UP:
                        holder.rlAll.setBackgroundResource(R.color.white);
                        holder.ivDelete.setBackgroundResource(R.drawable.ripple_item_layout_effect_white);
                        listener.onFileOpen(holder.getAdapterPosition());
                        break;
                }
                return true;
            }
        });
    }

    public void insertComplete(int loadingPosition, int completePosition, FileInformation information) {
        data.remove(loadingPosition);
        notifyItemRemoved(loadingPosition);
        data.add(completePosition, information);
        notifyItemInserted(completePosition);
    }

    public interface OnViewClickListener {
        void onViewClick(int id);

        void onViewDelete(int position, int id);

        void onFileOpen(int position);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class DownloadViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_file_name)
        TextView tvFileName;
        @BindView(R.id.pb_file_download)
        ProgressBar pbFileDownload;
        @BindView(R.id.tv_file_state)
        TextView tvFileState;
        @BindView(R.id.tv_file_percent)
        TextView tvFilePercent;
        @BindView(R.id.rl_right)
        RelativeLayout rlRight;
        @BindView(R.id.tv_complete_name)
        TextView tvCompleteName;
        @BindView(R.id.tv_create_time)
        TextView tvCreateTime;
        @BindView(R.id.rl_complete)
        RelativeLayout rlComplete;
        @BindView(R.id.iv_delete)
        ImageView ivDelete;
        @BindView(R.id.iv_pause)
        ImageView ivPause;
        @BindView(R.id.rl_all)
        RelativeLayout rlAll;

        public DownloadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
