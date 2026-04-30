package com.example.prp.views.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prp.databinding.ActivityForgetBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetActivity extends AppCompatActivity {

    ActivityForgetBinding binding;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Sending reset email...");

        binding.btnForget.setOnClickListener(v -> {
            String email = binding.edtForgetEmail.getText().toString();

            if (email.isEmpty()) {
                binding.edtForgetEmail.setError("Please enter valid email");
            } else {
                progressDialog.show();
                auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgetActivity.this, "Check Your Email", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgetActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(ForgetActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        binding.BacktoLogin.setOnClickListener(view -> {
            startActivity(new Intent(ForgetActivity.this, LoginActivity.class));
            finish();
        });
    }
}
