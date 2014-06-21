package com.impaq.pos;

import com.impaq.pos.model.Product;
import com.impaq.pos.model.ProductsReceipt;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProductReceiptTest {

	private ProductsReceipt tested;

	@Before
	public void setup(){
		tested = new ProductsReceipt();
	}


	@Test
	public void shouldHaveSumEqualZeroForNewReceipt(){
		assertEquals("0.0", tested.getSum().toString());
	}

	@Test
	public void shouldGetCorrectSumForProductList(){
		//given
		Product product1 = new ProductBuilder("1").build();
		Product product2 = new ProductBuilder("2").build();

		//when
		tested.add(product1);
		tested.add(product2);

		//then
		assertEquals((product1.getPrice()+ product2.getPrice())+"", tested.getSum().toString());
	}

}
