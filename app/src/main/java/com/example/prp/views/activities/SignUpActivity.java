package com.example.prp.views.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prp.databinding.ActivitySignUpBinding;
import com.example.prp.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We're creating your account");

        binding.btnSignUp.setOnClickListener(v -> {
            String name = binding.edtName.getText().toString();
            String email = binding.edtEmail.getText().toString();
            String password = binding.edtPassword.getText().toString();

            if (name.isEmpty()) {
                binding.edtName.setError("Please enter your name");
            } else if (email.isEmpty()) {
                binding.edtEmail.setError("Please enter valid email");
            } else if (password.isEmpty()) {
                binding.edtPassword.setError("Please enter strong password");
            } else {
                progressDialog.show();
                storeUserData(name, email, password);
            }
        });

        binding.login.setOnClickListener(view -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void storeUserData(String name, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String userId = auth.getCurrentUser().getUid();
                UserModel user = new UserModel(name, email, password, "https://firebasestorage.googleapis.com/v0/b/pocketprof-27dd7.firebasestorage.app/o/profile.png?alt=media&token=57d5031f-5fc9-44e6-afe2-689cc593e0d4");
                firestore.collection("users")
                        .document(userId)
                        .set(user)
                        .addOnCompleteListener(task1 -> {
                            progressDialog.dismiss();
                            if (task1.isSuccessful()) {
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(SignUpActivity.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                progressDialog.dismiss();
                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
