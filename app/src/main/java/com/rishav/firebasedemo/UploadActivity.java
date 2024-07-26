package com.rishav.firebasedemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rishav.firebasedemo.Model.Pdf;

public class UploadActivity extends AppCompatActivity {

    private static final int PICK_PDF_REQUEST = 1;

    private Button btnSelectPdf, btnSharePdf;
    private EditText etDescription;
    private TextView tvSelectedPdf;
    private Uri pdfUri;
    private ProgressDialog progressDialog;

    // Firebase Authentication
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth

        btnSelectPdf = findViewById(R.id.btn_select_pdf);
        btnSharePdf = findViewById(R.id.btn_share_pdf);
        etDescription = findViewById(R.id.et_description);
        tvSelectedPdf = findViewById(R.id.tv_selected_pdf);

        btnSelectPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPdf();
            }
        });

        btnSharePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPdf();
            }
        });
    }

    private void selectPdf() {
        Intent intent = new Intent();
        intent.setType("application/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pdfUri = data.getData();
            tvSelectedPdf.setText("Selected PDF: " + pdfUri.getLastPathSegment());
        }
    }

    private void uploadPdf() {
        FirebaseUser user = mAuth.getCurrentUser(); // Get the current user

        if (user != null) {
            String userId = user.getUid(); // Get the user ID
            if (pdfUri != null) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setTitle("Uploading PDF...");
                progressDialog.setProgress(0);
                progressDialog.show();

                String pdfName = pdfUri.getLastPathSegment(); // Get the name of the PDF file

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs");
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("pdfs/" + System.currentTimeMillis() + ".pdf");

                storageReference.putFile(pdfUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            progressDialog.dismiss();
                            Toast.makeText(UploadActivity.this, "PDF uploaded successfully", Toast.LENGTH_SHORT).show();
                            storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                String description = etDescription.getText().toString().trim();
                                String url = uri.toString();
                                String pdfId = databaseReference.push().getKey();
                                Pdf pdf = new Pdf(pdfId, pdfName, description, url, userId); // Pass pdfName
                                assert pdfId != null;
                                databaseReference.child(pdfId).setValue(pdf);
                            });
                        })
                        .addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Toast.makeText(UploadActivity.this, "Failed to upload PDF", Toast.LENGTH_SHORT).show();
                        })
                        .addOnProgressListener(snapshot -> {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressDialog.setProgress((int) progress);
                        });
            } else {
                Toast.makeText(this, "Select a PDF file to upload", Toast.LENGTH_SHORT).show();
            }
        } else {
            // User is not logged in
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show();
        }
    }

}
