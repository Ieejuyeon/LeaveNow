package com.mobileprogramming.leavenow;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    private List<DiaryItem> diaryList;
    private OnDiaryInteractionListener listener;

    public interface OnDiaryInteractionListener {
        void onDeleteClicked(DiaryItem diary);
    }

    public DiaryAdapter(List<DiaryItem> diaryList, OnDiaryInteractionListener listener) {
        this.diaryList = diaryList;
        this.listener = listener;
    }

    @Override
    public DiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_diary_entry, parent, false);
        return new DiaryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DiaryViewHolder holder, int position) {
        DiaryItem currentDiary = diaryList.get(position);

        holder.tvTitle.setText(currentDiary.getTitle());
        holder.tvDate.setText(currentDiary.getTimestamp());
        holder.tvPreview.setText(currentDiary.getContent().length() > 30 ?
                currentDiary.getContent().substring(0, 30) + "..." : currentDiary.getContent());

        holder.btnDelete.setOnClickListener(v -> {
            // 삭제 확인 다이얼로그 표시
            Context context = v.getContext(); // Context 가져오기
            new AlertDialog.Builder(context)
                    .setTitle("삭제 확인")
                    .setMessage("정말로 삭제하시겠습니까?")
                    .setPositiveButton("삭제", (dialog, which) -> {
                        // 삭제 동작 실행
                        if (listener != null) {
                            listener.onDeleteClicked(currentDiary);
                        }
                    })
                    .setNegativeButton("취소", (dialog, which) -> {
                        // 취소 버튼 클릭 시 아무 작업도 하지 않음
                        dialog.dismiss();
                    })
                    .show();
        });


        holder.tvTitle.setOnClickListener(v -> {
            // 클릭된 일기의 ID를 넘겨주면서 DiaryDetailActivity로 이동
            Context context = v.getContext();
            Intent intent = new Intent(context, DiaryDetailActivity.class);
            intent.putExtra("diary_id", currentDiary.getId());  // DiaryItem의 id를 넘긴다.
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return diaryList.size();
    }

    public void updateDiaryList(List<DiaryItem> newDiaryList) {
        this.diaryList = newDiaryList;
        notifyDataSetChanged();
    }

    public static class DiaryViewHolder extends RecyclerView.ViewHolder {
        Button tvTitle;
        TextView tvDate, tvPreview;
        Button btnDelete;
        RatingBar mood;

        public DiaryViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvPreview = itemView.findViewById(R.id.tv_preview);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            mood = itemView.findViewById(R.id.rb_mood);
        }
    }
}
