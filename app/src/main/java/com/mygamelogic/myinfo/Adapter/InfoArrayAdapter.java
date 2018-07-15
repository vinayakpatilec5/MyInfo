package com.mygamelogic.myinfo.Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mygamelogic.myinfo.Interface.InfoCallback;
import com.mygamelogic.myinfo.Response.PageData;

import java.util.List;
import com.mygamelogic.myinfo.R;
/**
 * Created by admin on 15/07/18.
 */

public class InfoArrayAdapter extends RecyclerView.Adapter<InfoArrayAdapter.MyViewHolder>{
    private List<PageData> pageDataList;

    private Context context;
    private InfoCallback infoCallback;
    public void setInfoCallback(InfoCallback infoCallback) {
        this.infoCallback = infoCallback;
    }

    public InfoArrayAdapter(Context context, List<PageData> pageData) {
        this.pageDataList = pageData;
        this.context = context;
    }
    public void refreshList(List<PageData> pageDataList) {
        this.pageDataList=pageDataList;
        notifyDataSetChanged();
    }
    //variables to handle swipe on row for searc audio rows

    @Override
    public int getItemCount() {
        if (pageDataList == null) {
            return 0;
        }
        return pageDataList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textview_musictitle, textview_musiccontent;
        private ImageView imageview_musicrow;
        public RelativeLayout layout_mainrow,viewBackground;

        public MyViewHolder(View view) {
            super(view);
            layout_mainrow = (RelativeLayout) view.findViewById(R.id.layout_mainrow);
            textview_musictitle = (TextView) view.findViewById(R.id.textview_musictitle);
            textview_musiccontent = (TextView) view.findViewById(R.id.textview_musiccontent);
            imageview_musicrow=(ImageView) view.findViewById(R.id.imageview_musicrow);
            viewBackground=(RelativeLayout) view.findViewById(R.id.viewBackground);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            PageData row=pageDataList.get(position);
            if((row.getThumbnail()!=null)&&(row.getThumbnail().getSource()!=null)&&(!row.getThumbnail().getSource().isEmpty())){
                Glide.with(context).load(row.getThumbnail().getSource())
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imageview_musicrow);
            }else {
                holder.imageview_musicrow.setImageDrawable(context.getResources().getDrawable(R.drawable.mydp));
            }
            if(row.getTitle()!=null){
                holder.textview_musictitle.setText(row.getTitle());
            }else {
                holder.textview_musictitle.setText("");
            }
            if((row.getTerms()!=null)&&(row.getTerms().getDescription()!=null)){
                if(row.getTerms().getDescription().size()>0){
                    holder.textview_musiccontent.setText(row.getTerms().getDescription().get(0));
                }else {
                    holder.textview_musiccontent.setText("");
                }
            }else {
                holder.textview_musiccontent.setText("");
            }
            holder.viewBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    touchEffect(v,position);

                }
            });
        }catch (Exception e) {
            Log.e("Feed", "caught exception" + e.toString());
            e.printStackTrace();
        }
    }

    private void touchEffect(View view,final int position){
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.94f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.94f);
        scaleDownX.setDuration(100);
        scaleDownY.setDuration(100);
        scaleDownX.setRepeatCount(1);
        scaleDownY.setRepeatCount(1);
        scaleDownX.setRepeatMode(ValueAnimator.REVERSE);
        scaleDownY.setRepeatMode(ValueAnimator.REVERSE);
        scaleDownX.setInterpolator(new DecelerateInterpolator());
        scaleDownY.setInterpolator(new DecelerateInterpolator());
        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.play(scaleDownX).with(scaleDownY);
        scaleDown.start();
        scaleDown.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                infoCallback.clickOnRow(position);
                super.onAnimationEnd(animation);
            }
        });
    }
}
