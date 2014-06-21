package com.impaq.pos.model;

import java.util.ArrayList;
import java.util.List;

public class ProductsReceipt implements Receipt<Product> {

    private final List<Product> products = new ArrayList<Product>();
	private Double sum=0.0;

	@Override
	public void add(Product product) {
		products.add(product);
		sum+=product.getPrice();
	}

	@Override
	public Double getSum() {
		return sum;
	}

	@Override
	public List<Product> getAll() {
		return products;
	}
}
