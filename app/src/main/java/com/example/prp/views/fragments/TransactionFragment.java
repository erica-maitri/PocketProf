package com.example.prp.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prp.R;
import com.example.prp.adapters.TransactionAdapter;
import com.example.prp.databinding.FragmentTransactionBinding;
import com.example.prp.models.Transaction;
import com.example.prp.models.UserModel;
import com.example.prp.utils.Constants;
import com.example.prp.utils.Helper;
import com.example.prp.viewmodels.MainViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

public class TransactionFragment extends Fragment {

    public TransactionFragment() {
    }

    FragmentTransactionBinding binding;
    FirebaseFirestore firestore;
    Calendar calendar;
    public MainViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTransactionBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        firestore = FirebaseFirestore.getInstance();
        loadUserData();

        calendar = Calendar.getInstance();
        updateDate();

        binding.nextDateBtn.setOnClickListener(c -> {
            if (Constants.SELECTED_TAB == Constants.DAILY) {
                calendar.add(Calendar.DATE, 1);
            } else if (Constants.SELECTED_TAB == Constants.MONTHLY) {
                calendar.add(Calendar.MONTH, 1);
            }
            updateDate();
        });

        binding.previousDateBtn.setOnClickListener(c -> {
            if (Constants.SELECTED_TAB == Constants.DAILY) {
                calendar.add(Calendar.DATE, -1);
            } else if (Constants.SELECTED_TAB == Constants.MONTHLY) {
                calendar.add(Calendar.MONTH, -1);
            }
            updateDate();
        });

        binding.floatingActionButton.setOnClickListener(c -> {
            new AddTransactionFragment().show(getParentFragmentManager(), null);
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("Monthly")) {
                    Constants.SELECTED_TAB = Constants.MONTHLY;
                } else if (tab.getText().equals("Daily")) {
                    Constants.SELECTED_TAB = Constants.DAILY;
                }
                updateDate();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        binding.transactionsList.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.transactions.observe(getViewLifecycleOwner(), transactions -> {
            TransactionAdapter transactionAdapter = new TransactionAdapter(getContext(), (java.util.ArrayList<Transaction>) transactions);
            binding.transactionsList.setAdapter(transactionAdapter);
            if (transactions.size() > 0) {
                binding.emptyState.setVisibility(View.GONE);
            } else {
                binding.emptyState.setVisibility(View.VISIBLE);
            }
        });

        viewModel.totalIncome.observe(getViewLifecycleOwner(),new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.incomeLbl.setText(String.valueOf(aDouble));
            }
        });
        viewModel.totalExpense.observe(getViewLifecycleOwner(), aDouble -> binding.expenseLbl.setText(String.valueOf(aDouble)));
        viewModel.totalAmount.observe(getViewLifecycleOwner(), aDouble -> binding.totalLbl.setText(String.valueOf(aDouble)));

        viewModel.getTransactions(calendar);

        return binding.getRoot();
    }

    public void updateDate() {
        if (Constants.SELECTED_TAB == Constants.DAILY) {
            binding.currentDate.setText(Helper.formatDate(calendar.getTime()));
        } else if (Constants.SELECTED_TAB == Constants.MONTHLY) {
            binding.currentDate.setText(Helper.formatDateByMonth(calendar.getTime()));
        }
        viewModel.getTransactions(calendar);
    }

    private void loadUserData() {
        if (FirebaseAuth.getInstance().getUid() != null) {
            firestore.collection("users").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        UserModel model = documentSnapshot.toObject(UserModel.class);
                        if (model != null) {
                            binding.userName.setText(model.getName());
                            // Note: usersEmail was not found in fragment_transaction.xml, so I've removed that line to prevent crashes.
                            
                            if (model.getProfile() != null && !model.getProfile().isEmpty()) {
                                Picasso.get().load(model.getProfile())
                                        .placeholder(R.drawable.account)
                                        .into(binding.profileImage);
                            }
                        }
                    }
                }
            });
        }
    }
}
