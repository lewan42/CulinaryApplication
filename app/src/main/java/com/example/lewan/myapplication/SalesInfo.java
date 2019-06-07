package com.example.lewan.myapplication;

public class SalesInfo {
    private String nameProduct;
    private String nameMagaz;
    private String category;
    private String price;


    public SalesInfo(String nameProduct, String category, String nameMagaz, String price) {

        this.nameProduct = nameProduct;
        this.nameMagaz = nameMagaz;
        this.category = category;
        this.price = price;

    }


    public String getNameProduct() {
        return this.nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getNameMagaz() {
        return nameMagaz;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setNameMagaz(String nameMagaz) {
        this.nameMagaz = nameMagaz;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}