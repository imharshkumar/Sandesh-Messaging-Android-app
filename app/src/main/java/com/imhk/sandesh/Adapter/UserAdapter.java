package com.imhk.sandesh.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.imhk.sandesh.MessageActivity;
import com.imhk.sandesh.Model.User;
import com.imhk.sandesh.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<User> mUsers;
    private  boolean isChat;

    public UserAdapter(Context mContext,List<User> mUsers,boolean isChat) {
        this.mContext=mContext;
        this.mUsers=mUsers;
        this.isChat=isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.user_card,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user=mUsers.get(position);
        holder.name.setText(user.getName());
        if (user.getImageURL().equals("null")) {

        }
        else
            Glide.with(mContext).load(user.getImageURL()).into(holder.profileImage);

        if (isChat) {
            if (user.getStatus().equals("online"))
                holder.onlineStatus.setVisibility(View.VISIBLE);
            else
                holder.onlineStatus.setVisibility(View.INVISIBLE);
        }
        else
            holder.onlineStatus.setVisibility(View.INVISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, MessageActivity.class);
                intent.putExtra("number",user.getMobile());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageView profileImage;
        private CardView onlineStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name_text);
            profileImage=itemView.findViewById(R.id.profile_view_card);
            onlineStatus=itemView.findViewById(R.id.online_status);
        }
    }
}

