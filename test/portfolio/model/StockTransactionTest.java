package portfolio.model;

import org.junit.Test;

import java.time.LocalDate;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

/**
 * Created by xiangfan on 11/15/18.
 */
public class StockTransactionTest {


  @Test
  public void testTransaction() {
    StockTransaction s1 = new StockTransaction("GOOG", 2000.0, LocalDate.of(2018, 11, 7), 100);
    StockTransaction s2 = new StockTransaction("AMD", 100.0, LocalDate.of(2018, 11, 8), 8);

    assertEquals(s1.getPurchasePricePerShare(), 2000.0, 2);
    assertEquals(s1.getShares(), 20, 2);
    assertEquals(s1.getDateOfTransaction().toString(), "2018-11-07");
    assertEquals(s1.getSymbol(), "GOOG");

    assertEquals(s2.getPurchasePricePerShare(), 100.0, 2);
    assertEquals(s2.getShares(), 12.5, 2);
    assertEquals(s2.getDateOfTransaction().toString(), "2018-11-08");
    assertEquals(s2.getSymbol(), "AMD");
  }

  @Test
  public void testInvalidCostPerShare() {
    try {
      StockTransaction s1 = new StockTransaction("GOOG", 2000.0, LocalDate.of(2018, 11, 7), 0.0);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "can't devide by zero");
    }
  }


}