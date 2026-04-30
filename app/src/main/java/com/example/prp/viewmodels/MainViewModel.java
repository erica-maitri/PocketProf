package com.example.prp.viewmodels;


import static com.example.prp.utils.Constants.SELECTED_STATS_TYPE;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.example.prp.models.Transaction;
import com.example.prp.utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    public MutableLiveData<List<Transaction>> transactions = new MutableLiveData<>();
    public MutableLiveData<List<Transaction>> categoriesTransactions = new MutableLiveData<>();

    public MutableLiveData<Double> totalIncome = new MutableLiveData<>();
    public MutableLiveData<Double> totalExpense = new MutableLiveData<>();
    public MutableLiveData<Double> totalAmount = new MutableLiveData<>();

    FirebaseFirestore firestore;
    Calendar calendar;
    FirebaseAuth auth;

    public MainViewModel(@NonNull Application application) {
        super(application);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }
    private CollectionReference getUserTransactionsRef() {
        String userId = auth.getCurrentUser().getUid();
        return firestore.collection("users")
                .document(userId)
                .collection("transactions");
    }

    public void getTransactions(Calendar calendar, String type) {
        this.calendar = calendar;
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date startTime;
        Date endTime;
        Query query;

        CollectionReference transactionsRef = getUserTransactionsRef();

        if (Constants.SELECTED_TAB_STATS == Constants.DAILY) {
            startTime = calendar.getTime();
            endTime = new Date(startTime.getTime() + (24 * 60 * 60 * 1000));

            query = transactionsRef
                    .whereGreaterThanOrEqualTo("date", startTime)
                    .whereLessThan("date", endTime)
                    .whereEqualTo("type", type)
                    .orderBy("date", Query.Direction.ASCENDING);

            Log.d("MainActivity","Daily Query:Start=" + startTime + "End=" + endTime + "Type=" + type);

        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            startTime = calendar.getTime();
            calendar.add(Calendar.MONTH, 1);
            endTime = calendar.getTime();

            query = transactionsRef
                    .whereGreaterThanOrEqualTo("date", startTime)
                    .whereLessThan("date", endTime)
                    .whereEqualTo("type", type)
                    .orderBy("date", Query.Direction.ASCENDING);

            Log.d("MainActivity","Daily Query:Start=" + startTime + "End=" + endTime + "Type=" + type);
        }

        query.get().addOnSuccessListener(snapshot -> {
            List<Transaction> list = new ArrayList<>();
            for (var doc : snapshot.getDocuments()) {
                Transaction t = doc.toObject(Transaction.class);
                list.add(t);
            }
            categoriesTransactions.setValue(list);
        });
    }

    public void getTransactions(Calendar calendar) {
        this.calendar = calendar;
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date startTime;
        Date endTime;

        CollectionReference transactionsRef = getUserTransactionsRef();

        if (Constants.SELECTED_TAB == Constants.DAILY) {
            startTime = calendar.getTime();
            endTime = new Date(startTime.getTime() + (24 * 60 * 60 * 1000));
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            startTime = calendar.getTime();
            calendar.add(Calendar.MONTH, 1);
            endTime = calendar.getTime();
        }

        transactionsRef
                .whereGreaterThanOrEqualTo("date", startTime)
                .whereLessThan("date", endTime)
                .get()
                .addOnSuccessListener(snapshot -> {
                    List<Transaction> list = new ArrayList<>();
                    double income = 0, expense = 0, total = 0;

                    for (var doc : snapshot.getDocuments()) {
                        Transaction t = doc.toObject(Transaction.class);
                        list.add(t);

                        double amount = t.getAmount();
                        total += amount;
                        if (Constants.INCOME.equals(t.getType())) {
                            income += amount;
                        } else if (Constants.EXPENSE.equals(t.getType())) {
                            expense += amount;
                        }
                    }

                    totalIncome.setValue(income);
                    totalExpense.setValue(expense);
                    totalAmount.setValue(total);
                    transactions.setValue(list);
                });
    }

    public void addTransaction(Transaction transaction) {
        CollectionReference transactionsRef = getUserTransactionsRef();

        transactionsRef.add(transaction)
                .addOnSuccessListener(documentReference -> {
                    transaction.setId(documentReference.getId());
                    documentReference.update("id", transaction.getId());
                    if (calendar != null) {
                        getTransactions(calendar);
                    } else {
                        getTransactions(Calendar.getInstance());
                    }
                });
    }

    public void deleteTransaction(Transaction transaction) {
        if (transaction.getId() != null) {
            CollectionReference transactionsRef = getUserTransactionsRef();
            DocumentReference docRef = transactionsRef.document(transaction.getId());
            docRef.delete().addOnSuccessListener(unused -> getTransactions(calendar));
        }
    }
}
