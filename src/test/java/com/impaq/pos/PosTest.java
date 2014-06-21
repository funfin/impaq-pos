package com.impaq.pos;

import com.impaq.pos.dao.ProductDao;
import com.impaq.pos.devices.input.BarcodeScanner;
import com.impaq.pos.devices.output.Display;
import com.impaq.pos.devices.output.Printer;
import com.impaq.pos.model.Product;
import com.impaq.pos.utils.MessageUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.impaq.pos.Pos.EXIT_CODE;
import static com.impaq.pos.Pos.PRODUCT_NOT_FOUND;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import static com.impaq.pos.Pos.INVALID_BAR_CODE;

@RunWith(MockitoJUnitRunner.class)
public class PosTest {

    public static final String BARCODE_EMPTY = "";
    public static final String BARCODE_NULL = null;
    private static final String BARCODE_OF_PRODUCT_1 = "1";
    private static final String BARCODE_OF_PRODUCT_2 = "2";
    private static final String BARCODE_OF_NOT_EXISTING_PRODUCT = "x";

    @Mock
    private ProductDao productDao;

    @Mock
    private BarcodeScanner barcodeScanner;

    @Mock
    private Display display;

    @Mock
    private Printer printer;

//    private BarcodeScannerAdapter barcodeScannerAdapter;

    private Pos tested;

    @Before
    public void setup(){

        when(productDao.getByBarcode(BARCODE_OF_PRODUCT_1)).thenReturn(buildProduct(BARCODE_OF_PRODUCT_1));
        when(productDao.getByBarcode(BARCODE_OF_PRODUCT_2)).thenReturn(buildProduct(BARCODE_OF_PRODUCT_2));

        tested = spy(new Pos(productDao, display, printer));
//        barcodeScannerAdapter = spy(new BarcodeScannerAdapter(barcodeScanner));
//        barcodeScannerAdapter.addObserver(tested);
    }

    private Product buildProduct(String barcode) {
        return new ProductBuilder(barcode).build();
    }

    @Test
    public void shouldReturnProductByBarCode(){

        assertEquals(ProductBuilder.createProductNameByBarcode(BARCODE_OF_PRODUCT_1), productDao.getByBarcode(BARCODE_OF_PRODUCT_1).getName());
    }


    //if the product is found in products database then it's name and price is printed on LCD display
    @Test
    public void shouldPrintProductOnLcdIfExistsInDataBase(){
        //given
		String barcode = BARCODE_OF_PRODUCT_1;
        String expectedMessage = MessageUtils.getProductMessage(buildProduct(barcode));

        //when
		tested.onBarcodeScan(barcode);

        //than
        verify(display).showMessage(expectedMessage);
    }

    /*if the product is not found then error message 'Product not found' is printed on LCD display*/
    @Test
    public void shouldPrintProductNotFoundIfProductIsNotFound(){
        //given
		String barcode = BARCODE_OF_NOT_EXISTING_PRODUCT;

        //when
        tested.onBarcodeScan(barcode);

        //than
        verify(display).showMessage(PRODUCT_NOT_FOUND);
        verifyNoMoreInteractions(display);
    }

    /*if the code scanned is empty then error message 'Invalid bar­code' is printed on LCD display*/
    @Test
    public void shouldPrintInvalidBarCodeMessageOnLcdIfScannedCodeIsEmpty(){
        //given
		String barcode = BARCODE_EMPTY;

        //when
        tested.onBarcodeScan(barcode);

        //than
        verify(display).showMessage(INVALID_BAR_CODE);
        verifyNoMoreInteractions(display);
    }

    @Test
    public void shouldPrintInvalidBarCodeMessageOnLcdIfScannedCodeIsNull(){
        //given
		String barcode = BARCODE_NULL;

        //when
        tested.onBarcodeScan(barcode);

        //than
        verify(display).showMessage(INVALID_BAR_CODE);
        verifyNoMoreInteractions(display);
    }

    /*when 'exit' is input than receipt is printed on printer containing a list of all previously
    scanned items names and prices as well as total getSum to be paid for all items;
    the total getSum is also printed on LCD display*/

    @Test
    public void shouldPrintSumOfZeroWhenExitCodeReadBeforeScannAnyProduct(){
        //given
		String barcode = EXIT_CODE;

        //when
        tested.onBarcodeScan(barcode);

        verify(display).showMessage("0.0");
        verify(printer, atMost(1)).printLine("0.0");
        verifyNoMoreInteractions(display);
        verifyNoMoreInteractions(printer);
    }

    @Test
    public void shouldPrintSumOfProductPriceWhenReadExitCode(){
        //given
		String barcodeOfProduct1 = BARCODE_OF_PRODUCT_1;
		String barcodeOfProduct2 = BARCODE_OF_PRODUCT_2;
		String exitCode = EXIT_CODE;
        Product product1 = buildProduct(barcodeOfProduct1);
        Product product2 = buildProduct(barcodeOfProduct2);

        //when
		tested.onBarcodeScan(barcodeOfProduct1);
		tested.onBarcodeScan(barcodeOfProduct2);
		tested.onBarcodeScan(exitCode);

        verify(display).showMessage(MessageUtils.getProductMessage(product1));
        verify(display).showMessage(MessageUtils.getProductMessage(product2));
        verify(display).showMessage(MessageUtils.getTotalSumMessage(product1.getPrice()+product2.getPrice()));
        verify(printer).printLine(MessageUtils.getProductMessage(product1));
        verify(printer).printLine(MessageUtils.getProductMessage(product2));
        verify(printer).printLine(MessageUtils.getTotalSumMessage(product1.getPrice()+product2.getPrice()));
        verifyNoMoreInteractions(display);
        verifyNoMoreInteractions(printer);
    }




}
