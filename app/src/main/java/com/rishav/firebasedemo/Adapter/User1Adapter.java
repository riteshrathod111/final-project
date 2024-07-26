package com.rishav.firebasedemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rishav.firebasedemo.Fragments.CommunicationFragment;
import com.rishav.firebasedemo.Model.User;
import com.rishav.firebasedemo.R;
import com.rishav.firebasedemo.chatwindo;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class User1Adapter extends RecyclerView.Adapter<User1Adapter.viewholder> {
    CommunicationFragment mainActivity;
    List<User> usersArrayList; // Change to List<User> here
    public User1Adapter(CommunicationFragment mainActivity, List<User> usersArrayList) {
        this.mainActivity=mainActivity;
        this.usersArrayList=usersArrayList;
    }

    @NonNull
    @Override
    public User1Adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext(); // Get the context from the parent ViewGroup
        View view = LayoutInflater.from(context).inflate(R.layout.user_item1,parent,false);
        return new viewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull User1Adapter.viewholder holder, int position) {

        User users = usersArrayList.get(position);
        holder.username.setText(users.getUsername());
        holder.userstatus.setText(users.getBio());
        Picasso.get().load(users.getImageurl()).into(holder.userimg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the context from the itemView
                Context context = holder.itemView.getContext();

                // Create the intent using the context
                Intent intent = new Intent(context, chatwindo.class);
                intent.putExtra("nameeee", users.getUsername());
                intent.putExtra("reciverImg", users.getImageurl());
                intent.putExtra("uid", users.getId());

                // Start the activity using the context
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView userimg;
        TextView username;
        TextView userstatus;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            userimg = itemView.findViewById(R.id.userimg);
            username = itemView.findViewById(R.id.username);
            userstatus = itemView.findViewById(R.id.userstatus);
        }
    }
}
