package com.example.lewan.myapplication;

public class State {
    private String name;
    private boolean favourRecipe;
    private boolean flag;//
    private String numberPiker;


    private String nameProduct;
    private String countProduct;
    private String typeProduct;

    private int imgProduct;

    public State(String name) {

        this.name = name;
        this.flag = false;
        this.numberPiker = "0";
    }

    public State(String name, String type) {

        this.name = name;
        this.flag = false;
        this.numberPiker = "0";
        typeProduct = type;
    }

    public State(String name, String type, String count, boolean flag) {

        this.name = name;
        this.flag = flag;
        this.numberPiker = count;
        typeProduct = type;
    }

    public State(String name, String count, String type) {

        this.nameProduct = name;
        this.countProduct = count;
        this.typeProduct = type;
    }

    public State(String name, boolean f) {

        this.name = name;
        this.flag = f;
    }

    public State(String nameProduct, int imgProduct) {
        this.nameProduct = nameProduct;
        this.imgProduct = imgProduct;
    }


    public boolean getFavour() {
        return this.favourRecipe;
    }

    public void setFavour(boolean f) {
        this.favourRecipe = f;
    }

    public String getNumberPiker() {
        return this.numberPiker;
    }

    public String getTypeProduct() {
        return this.typeProduct;
    }

    public String getCountProduct() {
        return this.countProduct;
    }

    public void setNumberPiker(String num) {
        numberPiker = num;
    }

    public void setTypeProduct(String type) {
        typeProduct = type;
    }

    public void setCountProduct(String count) {
        countProduct = count;
    }

    public String getNameProduct() {
        return this.nameProduct;
    }

    public void setNameProduct(String name) {
        this.nameProduct = name;
    }

    public int getImgProduct() {
        return this.imgProduct;
    }

    public void setImgProduct(int img) {
        this.imgProduct = img;
    }
//----------------------------


    public void setFlag(boolean f) {
        this.flag = f;
    }

    public boolean getFlag() {
        return this.flag;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVisible(String name, boolean f) {

    }
}
