package com.impaq.pos;

import com.impaq.pos.model.Product;

public class ProductBuilder {

    public static final String PRODUCT_NAME_PREFIX = "product_";

    private String name;
    private Double price;
    private String barcode;

    public ProductBuilder(String barcode){
        withBarcode(barcode);
        withName(createProductNameByBarcode(barcode));
        withPrice(createProductPriceByBarcode(barcode));
    }

    public ProductBuilder withName(String name){
        this.name=name;
        return this;
    }

    public ProductBuilder withPrice(Double price){
        this.price=price;
        return this;
    }

    public ProductBuilder withBarcode(String barcode){
        this.barcode=barcode;
        return this;
    }

    public Product build(){
        Product product = new Product();
        product.setBarcode(barcode);
        product.setName(name);
        product.setPrice(price);
        return product;
    }


    public static String createProductNameByBarcode(String barcode){
        return PRODUCT_NAME_PREFIX +barcode;
    }

    public static  Double createProductPriceByBarcode(String barcode){
        return Double.valueOf(barcode);
    }
}
