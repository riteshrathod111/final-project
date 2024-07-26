package com.rishav.firebasedemo.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rishav.firebasedemo.Adapter.PdfAdapter;
import com.rishav.firebasedemo.Model.Pdf;
import com.rishav.firebasedemo.R;
import com.rishav.firebasedemo.UploadActivity; // Import your UploadActivity here

import java.util.ArrayList;
import java.util.List;

public class PdfFragment extends Fragment {

    private RecyclerView recyclerView;
    private PdfAdapter pdfAdapter;
    private List<Pdf> pdfList;
    private EditText searchText;

    public PdfFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pdf, container, false);
        searchText = view.findViewById(R.id.search_text);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView = view.findViewById(R.id.recycler_view_pdfs);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        pdfList = new ArrayList<>();
        pdfAdapter = new PdfAdapter(getContext(), pdfList);
        recyclerView.setAdapter(pdfAdapter);

        fetchPdfData();

        // Floating action button to open UploadActivity
        FloatingActionButton fabUpload = view.findViewById(R.id.fab_upload);
        fabUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UploadActivity.class));
            }
        });
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Filter PDFs based on the search query
                filterPdfs(s.toString());
            }
        });

        return view;
    }

    private void fetchPdfData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("pdfs");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pdfList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Pdf pdf = snapshot.getValue(Pdf.class);
                    pdfList.add(pdf);
                }
                pdfAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
    private void filterPdfs(String searchText) {
        DatabaseReference pdfReference = FirebaseDatabase.getInstance().getReference().child("pdfs");

        // Convert search text to lowercase
        String searchTextLower = searchText.toLowerCase();

        Query query = pdfReference.orderByChild("name").startAt(searchTextLower).endAt(searchTextLower + "\uf8ff");
        Query query2 = pdfReference.orderByChild("description").startAt(searchTextLower).endAt(searchTextLower + "\uf8ff");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pdfList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Pdf pdf = snapshot.getValue(Pdf.class);
                    pdfList.add(pdf);
                }
                pdfAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
            }
        });

        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Pdf pdf = snapshot.getValue(Pdf.class);
                    // Convert description to lowercase for comparison
                    String descriptionLower = pdf.getDescription().toLowerCase();
                    // Add only if it's not already present
                    if (!pdfList.contains(pdf) && descriptionLower.contains(searchTextLower)) {
                        pdfList.add(pdf);
                    }
                }
                pdfAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
            }
        });
    }


}
