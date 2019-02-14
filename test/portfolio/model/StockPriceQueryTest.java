package portfolio.model;



/**
 * Created by xiangfan on 11/15/18.
 */
public class StockPriceQueryTest {

  /*
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

  @Before
  public void setUp() {
    PortfolioCollection p1;
    PortfolioCollection p2;
    PrintStream sysOut;
    sysOut = System.out;
    System.setOut(new PrintStream(outContent));

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

    HashMap<String, Double> date3 = new HashMap<>();
    date3.put("2018-11-13", 103.0500);
    date3.put("2018-11-12", 103.6300);
    date3.put("2018-11-09", 106.1500);
    date3.put("2018-11-08", 108.4000);
    date3.put("2018-11-07", 109.3900);
    date3.put("2018-11-06", 105.8100);
    date3.put("2018-11-05", 104.0900);

    HashMap<String, HashMap<String, Double>> symbolMap1 = new HashMap<>();
    symbolMap1.put("GOOG", date1);
    symbolMap1.put("AMD", date2);
    symbolMap1.put("AMC", date3);

    StockPriceQuery.setMap(symbolMap1);
    p1 = new PortfolioCollectionImpl();
    p2 = new PortfolioCollectionImpl();
  }

  @Test
  public void testStockQueryGetPriceInvalidSymbol() {
    try {
      StockPriceQuery.getPrice("ABCD", LocalDate.of(2018, 11, 7));
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "invalid stock symbol.");
    }
  }

  @Test
  public void testStockQueryGetPriceInvalidTradeDate() {
    try {
      StockPriceQuery.getPrice("GOOG", LocalDate.of(2018, 11, 11));
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "invalid trade date.");
    }
  }

  @Test
  public void testStockQueryGetPrice() {
    Double p1 = StockPriceQuery.getPrice("GOOG", LocalDate.of(2018, 11, 7));
    Double p2 = StockPriceQuery.getPrice("AMD", LocalDate.of(2018, 11, 8));
    Double p3 = StockPriceQuery.getPrice("AMC", LocalDate.of(2018, 11, 5));
    assertEquals(p1, 1093.39, 2);
    assertEquals(p2, 80.00, 2);
    assertEquals(p3, 104.09, 2);
  }

  @Test
  public void testGetAndStorePriceFromApi() {
    StockPriceQuery.getPriceFromApi("GOGO");
    Double p1 = StockPriceQuery.getPrice("GOGO", LocalDate.of(2018, 11, 7));
    Double p2 = StockPriceQuery.getPrice("GOGO", LocalDate.of(2018, 11, 8));
    assertEquals(p1, 6.92, 2);
    assertEquals(p2, 6.25, 2);
  }


  @Test
  public void testInvalidSymbolFromApi() {
    try {
      StockPriceQuery.getPriceFromApi("FANX");
      fail();
    } catch (RuntimeException e) {
      assertEquals(e.getMessage(), "No stock found for FANX.");
    }

  }
  */

}