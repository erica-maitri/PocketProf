package com.example.prp.views.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.prp.AdmobAds.Admob;
import com.example.prp.R;
import com.example.prp.databinding.FragmentProfileBinding;
import com.example.prp.models.UserModel;
import com.example.prp.views.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    Uri profileUri;
    ProgressDialog progressDialog;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Updating Profile");
        progressDialog.setMessage("Please wait...");

        Admob.loadBannerAd(binding.bannerAd, getContext());

        binding.privacyPolicy.setOnClickListener(view -> 
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/")))
        );

        binding.term.setOnClickListener(view -> 
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/")))
        );

        binding.rate.setOnClickListener(view -> 
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/")))
        );

        binding.contact.setOnClickListener(v -> 
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/")))
        );

        binding.fetchImage.setOnClickListener(v -> {
           Intent intent = new Intent();
           intent.setAction(Intent.ACTION_GET_CONTENT);
           intent.setType("image/*");
           startActivityForResult(intent, 2);
        });

        binding.share.setOnClickListener(v -> {
            String shareBody = "Hey check out this app:";
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(intent);
        });

        binding.logout.setOnClickListener(v -> {
            auth.signOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        loadUserData();

        return binding.getRoot();
    }

    private void loadUserData() {
        if (auth.getUid() != null) {
            firestore.collection("users").document(auth.getUid()).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    UserModel model = documentSnapshot.toObject(UserModel.class);
                    if (model != null) {
                        binding.usersName.setText(model.getName());
                        binding.usersEmail.setText(model.getEmail());

                        if (model.getProfile() != null && !model.getProfile().isEmpty()) {
                            Picasso.get().load(model.getProfile())
                                    .placeholder(R.drawable.account)
                                    .into(binding.profileImage);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            profileUri = data.getData();
            binding.profileImage.setImageURI(profileUri);
            updateProfile(profileUri);
        }
    }

    private void updateProfile(Uri profileUri) {
        progressDialog.show();
        final StorageReference reference = storage.getReference().child("profile").child(auth.getUid());
        reference.putFile(profileUri).addOnSuccessListener(taskSnapshot -> 
            reference.getDownloadUrl().addOnSuccessListener(uri -> 
                firestore.collection("users").document(auth.getUid()).update("profile", uri.toString())
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    })
            )
        ).addOnFailureListener(e -> {
            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        });
    }
}
