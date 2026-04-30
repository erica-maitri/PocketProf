package com.example.prp.models;

public class Category {
    private String CategoryName;
    private int CategoryColor;
    private int CategoryImage;

   public Category(){

   }
   public Category(String CategoryName, int CategoryColor, int CategoryImage){
       this.CategoryName = CategoryName;
       this.CategoryColor = CategoryColor;
       this.CategoryImage = CategoryImage;
   }

   public String getCategoryName(){
       return CategoryName;
   }
   public void setCategoryName(String CategoryName){
       this.CategoryName = CategoryName;
   }
   public int getCategoryColor(){
       return CategoryColor;
   }
   public void setCategoryColor(int CategoryColor){
       this.CategoryColor = CategoryColor;
   }
   public int getCategoryImage(){
       return CategoryImage;
   }
   public void setCategoryImage(int CategoryImage){
       this.CategoryImage = CategoryImage;
   }
}
