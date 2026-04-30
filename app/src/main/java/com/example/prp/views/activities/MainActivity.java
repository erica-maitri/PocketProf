package com.example.prp.views.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.MenuItem;

import com.example.prp.R;
import com.example.prp.databinding.ActivityMainBinding;
import com.example.prp.utils.Constants;
import com.example.prp.viewmodels.MainViewModel;
import com.example.prp.views.fragments.ProfileFragment;
import com.example.prp.views.fragments.StatsFragment;
import com.example.prp.views.fragments.TransactionFragment;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    Calendar calendar;
    /*
    0=daily
    1=monthly
    2=calender
    3=summary
    4=notes
     */

    public MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            viewModel = new ViewModelProvider(this).get(MainViewModel.class);

            Constants.setCategories();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, new TransactionFragment());
            transaction.commit();

            binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    if (item.getItemId() == R.id.transactions) {
                        getSupportFragmentManager().popBackStack();
                    } else if (item.getItemId() == R.id.stats) {
                        transaction.replace(R.id.content, new StatsFragment());
                        transaction.addToBackStack(null);
                    } else if (item.getItemId() == R.id.accounts) {
                        transaction.replace(R.id.content, new ProfileFragment());
                        transaction.addToBackStack(null);
                    }else if(item.getItemId() == R.id.more){
                        String shareBody = "Hey check out this app:";
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                        startActivity(intent);
                    }
                    transaction.commit();
                    return true;
                }
            });
    }

    public void getTransactions(){
        viewModel.getTransactions(calendar);
    }

}
