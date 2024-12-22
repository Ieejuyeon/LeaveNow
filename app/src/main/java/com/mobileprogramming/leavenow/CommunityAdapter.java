package com.mobileprogramming.leavenow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class CommunityAdapter extends BaseAdapter {
    private Context context;
    private List<CommunityPost> postList;

    public CommunityAdapter(Context context, List<CommunityPost> postList) {
        this.context = context;
        this.postList = postList;
    }

    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public Object getItem(int position) {
        return postList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.community_post_item, parent, false);
            holder = new ViewHolder();
            holder.title = convertView.findViewById(R.id.postTitle);
            holder.author = convertView.findViewById(R.id.postAuthor);
            holder.image = convertView.findViewById(R.id.attachmentImageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CommunityPost post = postList.get(position);
        holder.title.setText(post.getTitle());
        holder.author.setText(post.getNickname());

        File imgFile = new File(post.getAttachment());
        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.image.setImageBitmap(bitmap);
        } else {
            holder.image.setVisibility(View.GONE);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView title, author;
        ImageView image;
    }
}
