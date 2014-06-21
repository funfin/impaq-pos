package com.impaq.pos.dao;

import com.impaq.pos.model.Product;

public interface ProductDao {

    Product getByBarcode(String barcode);
}
