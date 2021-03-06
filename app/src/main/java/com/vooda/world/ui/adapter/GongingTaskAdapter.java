package com.vooda.world.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vooda.world.R;
import com.vooda.world.api.ApiManager;
import com.vooda.world.bean.TaskBean;
import com.vooda.world.contant.Contants;
import com.vooda.world.utils.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leijiaxq
 * Data       2016/12/9 10:55
 * Describe
 */
public class GongingTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context                         mContext;
    private List<TaskBean.UserTaskList> mDatas;
    private LayoutInflater                  inflater;


    public GongingTaskAdapter(Context context, List<TaskBean.UserTaskList> datas) {
        this.mContext = context;
        this.mDatas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case Contants.TYPE1:
                view = inflater.inflate(R.layout.item_task_type1, parent, false);
                return new ViewHolderType1(view);
            case Contants.TYPE2:
                view = inflater.inflate(R.layout.item_task_type2, parent, false);
                return new ViewHolderType2(view);
            default:
                throw new RuntimeException("there is no type that matches the type " +
                        viewType + " + make sure your using types correctly");
        }
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {

        TaskBean.UserTaskList dataBean = mDatas.get(position);
        if (TextUtils.isEmpty(dataBean.getTimeFlag())) {
            return Contants.TYPE2;
        }
        return Contants.TYPE1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolderType1) {
            setDataType1((ViewHolderType1) holder, position);
        } else if (holder instanceof ViewHolderType2) {
            setDataType2((ViewHolderType2) holder, position);
        }
    }

    //设置type1 的数据
    private void setDataType1(ViewHolderType1 holder, int position) {
        TaskBean.UserTaskList bean = mDatas.get(position);
        holder.mItemType1Tv.setText(TextUtils.isEmpty(bean.getTimeFlag()) ? "" : bean.getTimeFlag());

    }

    //设置type2 的数据
    private void setDataType2(ViewHolderType2 holder, final int position) {
        TaskBean.UserTaskList bean = mDatas.get(position);

        String iconUrl = "";
        if (!TextUtils.isEmpty(bean.getTaskIconUrl())) {
            if (!bean.getTaskIconUrl().startsWith("http")) {
                iconUrl = ApiManager.BASE_URL_IMAGE + bean.getTaskIconUrl();
            } else {
                iconUrl = bean.getTaskIconUrl();
            }
        }
        ImageLoader.getInstance().displayImage(mContext, iconUrl, holder.mItemIconIv);

        holder.mItemTitleTv.setText(TextUtils.isEmpty(bean.getTaskTitle()) ? "" : bean.getTaskTitle());
        holder.mItemContentTv.setText(TextUtils.isEmpty(bean.getTaskContent()) ? "" : bean.getTaskContent());

        if (bean.isShowLine()) {       //是否隐藏和显示线条
            holder.mItemV.setVisibility(View.VISIBLE);
        } else {
            holder.mItemV.setVisibility(View.GONE);
        }
        if (1 == bean.getIsDownload()) {  //任务完成，但是还没有领取奖金
            holder.mItemOpenRewardTv.setVisibility(View.VISIBLE);
            holder.mItemOpenRewardIv.setVisibility(View.VISIBLE);
            holder.mItemRewardTv.setVisibility(View.GONE);
            holder.mItemOpenRewardTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //领取任务奖金
                    if (mListener != null) {
                        mListener.onReceiveReward(position);
                    }
                }
            });

        } else {
            holder.mItemOpenRewardTv.setVisibility(View.GONE);
            holder.mItemOpenRewardIv.setVisibility(View.GONE);
            holder.mItemRewardTv.setVisibility(View.VISIBLE);
        }
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {

        //领取奖金
        void onReceiveReward(int position);
    }


    static class ViewHolderType1 extends RecyclerView.ViewHolder {
        @BindView(R.id.item_type1_tv)
        TextView mItemType1Tv;

        ViewHolderType1(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderType2 extends RecyclerView.ViewHolder {
        @BindView(R.id.item_v)
        View      mItemV;
        @BindView(R.id.item_icon_iv)
        ImageView mItemIconIv;
        @BindView(R.id.item_title_tv)
        TextView  mItemTitleTv;
        @BindView(R.id.item_content_tv)
        TextView  mItemContentTv;
        @BindView(R.id.item_reward_tv)
        TextView  mItemRewardTv;
        @BindView(R.id.item_open_reward_tv)
        TextView  mItemOpenRewardTv;
        @BindView(R.id.item_reward_iv)
        ImageView mItemOpenRewardIv;

        ViewHolderType2(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
