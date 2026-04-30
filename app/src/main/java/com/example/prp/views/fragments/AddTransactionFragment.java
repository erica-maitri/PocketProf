package com.example.prp.views.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prp.AdmobAds.Admob;
import com.example.prp.R;
import com.example.prp.adapters.AccountsAdapter;
import com.example.prp.adapters.CategoryAdapter;
import com.example.prp.databinding.FragmentAddTransactionBinding;
import com.example.prp.databinding.ListDialogBinding;
import com.example.prp.models.Account;
import com.example.prp.models.Category;
import com.example.prp.models.Transaction;
import com.example.prp.utils.Constants;
import com.example.prp.utils.Helper;
import com.example.prp.views.activities.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class AddTransactionFragment extends BottomSheetDialogFragment {

    public AddTransactionFragment() {
        // Required empty public constructor
    }

    FragmentAddTransactionBinding binding;
    Transaction transaction;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddTransactionBinding.inflate(inflater, container, false);
        transaction = new Transaction();

        binding.incomeBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackgroundResource(R.drawable.income_selector);
            binding.expenseBtn.setBackgroundResource(R.drawable.default_selector);
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.greenColor));

            transaction.setType(Constants.INCOME);
        });

        binding.expenseBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackgroundResource(R.drawable.default_selector);
            binding.expenseBtn.setBackgroundResource(R.drawable.expense_selector);
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.redColor));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.textColor));

            transaction.setType(Constants.EXPENSE);
        });

        binding.date.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
            datePickerDialog.setOnDateSetListener((datePicker, year, month, dayOfMonth) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.YEAR, year);

                String dateToShow = Helper.formatDate(calendar.getTime());
                binding.date.setText(dateToShow);

                transaction.setDate(calendar.getTime());
                transaction.setId(String.valueOf(calendar.getTime().getTime()));
            });
            datePickerDialog.show();
        });

        binding.category.setOnClickListener(view -> {
            ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog categoryDialog = new AlertDialog.Builder(getContext()).create();
            categoryDialog.setView(dialogBinding.getRoot());

            CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), Constants.categories, new CategoryAdapter.CategoryClickListener() {
                @Override
                public void onCategoryClick(Category category) {
                    binding.category.setText(category.getCategoryName());
                    transaction.setCategory(category.getCategoryName());
                    categoryDialog.dismiss();
                }
            });
            dialogBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            dialogBinding.recyclerView.setAdapter(categoryAdapter);

            categoryDialog.show();
        });

        binding.account.setOnClickListener(c -> {
            ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog accountDialog = new AlertDialog.Builder(getContext()).create();
            accountDialog.setView(dialogBinding.getRoot());

            ArrayList<Account> accounts = new ArrayList<>();
            accounts.add(new Account("Cash", 0));
            accounts.add(new Account("Bank", 0));
            accounts.add(new Account("Card", 0));
            accounts.add(new Account("PayTm", 0));
            accounts.add(new Account("Other", 0));

            AccountsAdapter adapter = new AccountsAdapter(getContext(), accounts, new AccountsAdapter.AccountClickListener() {
                @Override
                public void onAccountClick(Account account) {
                    binding.account.setText(account.getAccountName());
                    transaction.setAccount(account.getAccountName());
                    accountDialog.dismiss();
                }
            });
            dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            dialogBinding.recyclerView.setAdapter(adapter);

            accountDialog.show();
        });

        binding.saveTransactionBtn.setOnClickListener(c -> {
            String amountStr = binding.amount.getText().toString();
            if (amountStr.isEmpty()) {
                binding.amount.setError("Enter amount");
                return;
            }
            double amount = Double.parseDouble(amountStr);
            String note = binding.note.getText().toString();

            if (transaction.getType().equals(Constants.INCOME)) {
                transaction.setAmount(amount);
            } else if (transaction.getType().equals(Constants.EXPENSE)) {
                transaction.setAmount(-amount);
            }
            transaction.setNote(note);

            Admob.showInterstitial(requireActivity(), null, true);

            ((MainActivity) requireActivity()).viewModel.addTransaction(transaction);
            ((MainActivity) requireActivity()).getTransactions();
            dismiss();
        });

        return binding.getRoot();
    }
}
