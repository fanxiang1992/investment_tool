package portfolio.model;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

/**
 * Created by xiangfan on 11/15/18.
 */
public class PortfolioCollectionImplTest {


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
    date2.put("2018-11-13", 100.00);
    date2.put("2018-11-12", 110.00);
    date2.put("2018-11-09", 90.00);
    date2.put("2018-11-08", 80.00);
    date2.put("2018-11-07", 120.00);
    date2.put("2018-11-06", 105.00);
    date2.put("2018-11-05", 95.00);

    HashMap<String, HashMap<String, Double>> symbolMap1 = new HashMap<>();
    symbolMap1.put("GOOG", date1);
    symbolMap1.put("AMD", date2);
  }

  @Test
  public void testCreateNewPortfolio() {
    PortfolioCollection collection = new PortfolioCollectionImpl();
    collection.createNewPortfolio("P1");
    //test create new portfolio
    assertEquals(collection.getStocks("P1", LocalDate.of(2018, 11, 1)), Arrays.asList());
  }

  @Test
  public void testInvalidGetPortfolio() {
    PortfolioCollection collection = new PortfolioCollectionImpl();
    //test for invalid portfolio name
    try {
      collection.getStocks("P2", LocalDate.of(2018, 11, 10));
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "This portfolio does not exist");
    }
  }

  @Test
  public void testAddStockWithManuallyPrice() {
    PortfolioCollection collection = new PortfolioCollectionImpl();
    collection.createNewPortfolio("P1");
    //test for valid portfolio
    collection.addStock("P1", "GOOG", 1000.0, LocalDate.of(2018, 11, 7), 200.0);
    assertEquals(collection.getPurchasePrice("P1", "GOOG", LocalDate.of(2018, 11, 7)), 1000.00);
    assertEquals(collection.getTotalShares("P1", "GOOG", LocalDate.of(2018, 11, 7)), 5.00);
    assertEquals(collection.getTotalValue("P1", "GOOG", LocalDate.of(2018, 11, 7)), 5466.95, 2);


    //test addStock for invalid portfolio name
    try {
      collection.addStock("P2", "GOOG", 1000.0, LocalDate.of(2018, 11, 7), 200.0);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "This portfolio does not exist");
    }
  }

  @Test
  public void testAddStockWithApiPrice() {
    PortfolioCollection collection = new PortfolioCollectionImpl();
    collection.createNewPortfolio("P1");

    // if use API price, the costPerShare will be 0.0
    collection.addStock("P1", "GOOG", 1000.0, LocalDate.of(2018, 11, 7), 0.0);
    assertEquals(collection.getPurchasePrice("P1", "GOOG", LocalDate.of(2018, 11, 7)), 1000.00);
    assertEquals(collection.getTotalShares("P1", "GOOG", LocalDate.of(2018, 11, 8)), 0.91, 2);
    assertEquals(collection.getTotalValue("P1", "GOOG", LocalDate.of(2018, 11, 8)), 989.95, 2);

  }

  @Test
  public void testInvalidCommision() {
    PortfolioCollection collection = new PortfolioCollectionImpl();
    collection.createNewPortfolio("P1");

    //nagetive
    try {
      collection.addCommissionFee("P1", -5.0);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "Commission needs to be positive.");
    }


  }


}