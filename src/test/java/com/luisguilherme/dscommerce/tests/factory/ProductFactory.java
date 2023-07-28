package com.luisguilherme.dscommerce.tests.factory;

import com.luisguilherme.dscommerce.entities.Category;
import com.luisguilherme.dscommerce.entities.Product;

public class ProductFactory {
	
	public static Product createProduct() {
		
		Category category = CategoryFactory.createCategory();
		Product product = new Product(1L, "Playstation 5", "Lorem ipsum", 4000.0, "https://teste.com/teste.jpg");
		product.getCategories().add(category);
		return product; 
	}
	
	public static Product createProduct(String name) {		
		
		Product product = createProduct();
		product.setName(name);
		return product; 
	}

}
