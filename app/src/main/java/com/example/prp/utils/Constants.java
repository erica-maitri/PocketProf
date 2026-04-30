package com.example.prp.utils;

import com.example.prp.R;
import com.example.prp.models.Category;

import java.util.ArrayList;

public class Constants {
    public static String INCOME = "Income";
    public static String EXPENSE = "Expense";

    public static ArrayList<Category> categories;

    public static int DAILY = 0;
    public static int MONTHLY = 1;
    public static int CALENDAR = 2;
    public static int SUMMARY = 3;
    public static int NOTES = 4;

    public static int SELECTED_TAB = 0;
    public static int SELECTED_TAB_STATS = 1;
    public static String SELECTED_STATS_TYPE = INCOME;

    public static void setCategories() {
        categories = new ArrayList<>();
        categories.add(new Category("Salary", R.color.category1, R.drawable.ic_salary));
        categories.add(new Category("Bonus", R.color.category2, R.drawable.ic_business));
        categories.add(new Category("Investment", R.color.category3, R.drawable.ic_investment));
        categories.add(new Category("Loan", R.color.category4, R.drawable.ic_loan));
        categories.add(new Category("Rent", R.color.category5, R.drawable.ic_rent));
        categories.add(new Category("Other", R.color.category6, R.drawable.ic_other));
    }

    public static Category getCategory(String categoryName) {
        for (Category cat : categories) {
            if (cat.getCategoryName().equalsIgnoreCase(categoryName)) {
                return cat;
            }
        }
        return null;
    }

    public static int getAccountColor(String accountName) {
        switch (accountName) {
            case "Bank":
                return R.color.bank_color;
            case "Cash":
                return R.color.cash_color;
            case "Card":
                return R.color.card_color;
            default:
                return R.color.default_color;
        }
    }
}
