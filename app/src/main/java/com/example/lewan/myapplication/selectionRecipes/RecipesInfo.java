package com.example.lewan.myapplication.selectionRecipes;

public class RecipesInfo {

    private String nameRecipe;
    private String descriptionRecipe;
    private int imgRecipe;
    private int idRecipe;
    private int timeRecipe;
    private int servingsRecipe;
    private boolean flag;
    private String tmp;


    public RecipesInfo(int id, String name, int time, int servings, String description, int img,String t) {

        idRecipe = id;
        nameRecipe = name;
        timeRecipe = time;
        servingsRecipe = servings;
        descriptionRecipe = description;
        imgRecipe = img;
        tmp = t;
    }

    public RecipesInfo(int id, String name, int time, int servings, String description, int img, boolean f) {

        idRecipe = id;
        nameRecipe = name;
        timeRecipe = time;
        servingsRecipe = servings;
        descriptionRecipe = description;
        imgRecipe = img;
        flag = f;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public String getDescriptionRecipe() {
        return descriptionRecipe;
    }


    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getNameRecipe() {
        return this.nameRecipe;
    }

    public int getImgRecipe() {
        return this.imgRecipe;
    }

    public String getDescriptionRecipeRecipe() {
        return this.descriptionRecipe;
    }

    public int getIdRecipe() {
        return this.idRecipe;
    }

    public int getTimeRecipe() {
        return this.timeRecipe;
    }

    public int getServingsRecipe() {
        return this.servingsRecipe;
    }

    public void setNameRecipe(String name) {
        this.nameRecipe = name;
    }

    public void setImgRecipe(int img) {
        this.imgRecipe = img;
    }

    public void setDescriptionRecipe(String description) {
        this.descriptionRecipe = description;
    }

    public void setIdRecipe(int id) {
        this.idRecipe = id;
    }

    public void setTimeRecipe(int time) {
        this.timeRecipe = time;
    }

    public void setServingsRecipe(int servings) {
        this.servingsRecipe = servings;
    }

    @Override
    public String toString() {
        return idRecipe + ", " + nameRecipe + ", " + timeRecipe + ", " + servingsRecipe + ", " + descriptionRecipe;
    }

}
