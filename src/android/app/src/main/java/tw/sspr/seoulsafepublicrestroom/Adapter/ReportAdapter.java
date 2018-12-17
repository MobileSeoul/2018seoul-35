package tw.sspr.seoulsafepublicrestroom.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import tw.sspr.seoulsafepublicrestroom.Activity.DeleteReportAuthenticateActivity;
import tw.sspr.seoulsafepublicrestroom.Activity.DetailReportActivity;
import tw.sspr.seoulsafepublicrestroom.Activity.ImageActivity;
import tw.sspr.seoulsafepublicrestroom.Object.ReportItem;
import tw.sspr.seoulsafepublicrestroom.R;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder>{

    private Context parent;

    private List<ReportItem> mReportList;

    public ReportAdapter(Context parent, List<ReportItem> reportList){
        this.parent = parent;
        mReportList = reportList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_report,parent,false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;

    }

    @NonNull


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final ReportItem item = mReportList.get(i);
        viewHolder.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(parent, DeleteReportAuthenticateActivity.class);
                i.putExtra("report_id", item.getReport_id());
                ((DetailReportActivity)parent).startActivityForResult(i,1);
            }
        });
        viewHolder.msgView.setText(item.getMsg());
        viewHolder.writerView.setText("by " + item.getWriter());
        viewHolder.updatedateView.setText(item.getUpdatedate());
        Glide
                .with(parent)
                .load(item.getImg())
                .into(viewHolder.imgView);
        viewHolder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(parent, ImageActivity.class);
                i.putExtra("src", item.getImg());
                parent.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mReportList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView deleteView;
        public TextView msgView;
        public TextView writerView;
        public TextView updatedateView;
        public ImageView imgView;

        public ViewHolder(View itemView){
            super(itemView);
            deleteView = itemView.findViewById(R.id.delete);
            msgView = itemView.findViewById(R.id.msg);
            writerView = itemView.findViewById(R.id.writer);
            updatedateView = itemView.findViewById(R.id.updatedate);
            imgView = itemView.findViewById(R.id.img);

        }
    }
}
