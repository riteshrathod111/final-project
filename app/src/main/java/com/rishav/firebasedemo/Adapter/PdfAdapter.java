package com.rishav.firebasedemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rishav.firebasedemo.Model.Pdf;
import com.rishav.firebasedemo.Model.User;
import com.rishav.firebasedemo.R;

import java.util.List;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.PdfViewHolder> {

    private Context context;
    private List<Pdf> pdfList;

    public PdfAdapter(Context context, List<Pdf> pdfList) {
        this.context = context;
        this.pdfList = pdfList;
    }

    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_item, parent, false);
        return new PdfViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PdfViewHolder holder, int position) {
        Pdf pdf = pdfList.get(position);

        // Setting the PDF name and description
        holder.tvName.setText(pdf.getName());
        holder.tvDescription.setText(pdf.getDescription());

        // Check if userId is not null
        if (pdf.getUserId() != null) {
            // Retrieve username from Firebase and set it to TextView
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(pdf.getUserId());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String username = dataSnapshot.child("username").getValue(String.class);
                        holder.username.setText(username != null ? username : "Unknown");
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        } else {
            // Set default username if userId is null
            //holder.username.setText("Unknown");
        }

        // Click listener to open PDF
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPdf(pdf.getUrl());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPdf(pdf.getUrl());
            }
        });
    }


    private void openPdf(String pdfUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(pdfUrl), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return pdfList.size();
    }

    static class PdfViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView tvName;
        TextView tvDescription;

        PdfViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDescription = itemView.findViewById(R.id.tv_description);
            username = itemView.findViewById(R.id.username);
        }
    }
}
