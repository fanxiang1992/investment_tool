package portfolio.model;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;


public class StockPortfolioImplTest {



  @Before
  public void setUp() {
    HashMap<String, Double> date1 = new HashMap<>();
    date1.put("2018-11-13", 1036.0500);
    date1.put("2018-11-12", 1038.6300);
    date1.put("2018-11-09", 1066.1500);
    date1.put("2018-11-08", 1082.4000);
    date1.put("2018-11-07", 1093.3900);
    date1.put("2018-11-06", 1055.8100);
    date1.put("2018-11-05", 1040.0900);

    HashMap<String, Double> date2 = new HashMap<>();
    date2.put("2018-11-13", 1036.0500);
    date2.put("2018-11-12", 1038.6300);
    date2.put("2018-11-09", 1066.1500);
    date2.put("2018-11-08", 1082.4000);
    date2.put("2018-11-07", 1093.3900);
    date2.put("2018-11-06", 1055.8100);
    date2.put("2018-11-05", 1040.0900);

    HashMap<String, HashMap<String, Double>> symbolMap1 = new HashMap<>();
    symbolMap1.put("GOOG", date1);
    symbolMap1.put("AMD", date1);
  }

  @Test
  public void testAddStokValid() {
    StockPortfolioImpl testPortfolio = new StockPortfolioImpl();

    testPortfolio.addStock("GOOG", 1000.0, LocalDate.of(2018, 11, 7), 200.0);
    testPortfolio.addStock("GOOG", 2000.0, LocalDate.of(2018, 11, 9), 200.0);
    testPortfolio.addStock("AMD", 1000.0, LocalDate.of(2018, 11, 12), 200.0);
    testPortfolio.addStock("GOOG", 2000.0, LocalDate.of(2018, 11, 13), 200.0);


    //add single stock
    assertEquals(testPortfolio.getPurchasePrice(LocalDate.of(2018, 11, 7), "GOOG"), 1000.00);
    assertEquals(testPortfolio.getTotalShare(LocalDate.of(2018, 11, 7), "GOOG"), 5.00);
    assertEquals(testPortfolio.getTotalValue(LocalDate.of(2018, 11, 7), "GOOG"), 5466.95, 2);

    //buy more share of same stock
    assertEquals(testPortfolio.getPurchasePrice(LocalDate.of(2018, 11, 9), "GOOG"), 3000.00);
    assertEquals(testPortfolio.getTotalShare(LocalDate.of(2018, 11, 9), "GOOG"), 15.00);
    assertEquals(testPortfolio.getTotalValue(LocalDate.of(2018, 11, 9), "GOOG"), 15992.25, 2);

    //buy another stock
    assertEquals(testPortfolio.getPurchasePrice(LocalDate.of(2018, 11, 12), "GOOG"), 3000.00);
    assertEquals(testPortfolio.getTotalShare(LocalDate.of(2018, 11, 12), "GOOG"), 15.00);
    assertEquals(testPortfolio.getTotalValue(LocalDate.of(2018, 11, 12), "GOOG"), 15579.45, 2);
    assertEquals(testPortfolio.getStocks(LocalDate.of(2018, 11, 12)), Arrays.asList("GOOG", "AMD"));
    assertEquals(testPortfolio.getPurchasePrice(LocalDate.of(2018, 11, 12), "AMD"), 1000.00);
    assertEquals(testPortfolio.getTotalShare(LocalDate.of(2018, 11, 12), "AMD"), 5.00);
    assertEquals(testPortfolio.getTotalValue(LocalDate.of(2018, 11, 12), "AMD"), 5193.15, 2);
  }


  @Test
  public void testInvalidAddStock() {
    StockPortfolioImpl testPortfolio = new StockPortfolioImpl();

    try {
      testPortfolio.addStock("GOOG", -1000.0, LocalDate.of(2018, 11, 7), 200.0);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "Need to input a positive dollar amount or the amount is" +
              " less than commission fee.");
    }

    try {
      testPortfolio.addStock("GOOG", 1000.0, LocalDate.of(2018, 11, 7), -200.0);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "Need to input a positive price per share.");
    }


    //test for invalid stock ticker
    try {
      testPortfolio.addStock("xnvb", 1000.0, LocalDate.of(2018, 11, 8), 200.0);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "No price data found for xnvb.");
    }

    //test for invalid stock ticker
    try {
      testPortfolio.addStock("fanx", 1000.0, LocalDate.of(2018, 11, 9), 200.0);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "No price data found for fanx.");
    }

  }



}