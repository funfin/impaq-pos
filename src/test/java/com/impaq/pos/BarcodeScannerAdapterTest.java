package com.impaq.pos;

import com.impaq.pos.devices.input.BarcodeScanner;
import com.impaq.pos.devices.input.BarcodeScannerAdapter;
import com.impaq.pos.listeners.BarcodeScanListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BarcodeScannerAdapterTest {

    private BarcodeScannerAdapter tested;

    @Mock
    private BarcodeScanner barcodeScanner;

	@Mock
	private BarcodeScanListener barcodeScanListener;

    @Before
    public void setup(){
        tested = spy(new BarcodeScannerAdapter(barcodeScanner, barcodeScanListener));
    }

    @Test
    public void shouldRunListenerWhenReadCode(){
		//given
		String barcode = "barcode";
		when(barcodeScanner.readBarcode()).thenReturn(barcode);
        //when
        tested.readBarcode();
        //then
        verify(barcodeScanListener).onBarcodeScan(barcode);
    }

}
