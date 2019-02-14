
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.HashMap;


import portfolio.controller.PortfolioController;
import portfolio.controller.PortfolioControllerImpl;
import portfolio.model.PortfolioCollection;
import portfolio.model.PortfolioCollectionImpl;


import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;


public class PortfolioControllerImplTest {

  private PortfolioCollection p1;
  private PortfolioCollection p2;

  private PrintStream sysOut;
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();


  @Before
  public void setUp() {
    sysOut = System.out;
    System.setOut(new PrintStream(outContent));

    HashMap<String, Double> date1 = new HashMap<>();
    date1.put("2018-11-16", 1056.0500);
    date1.put("2018-11-15", 1136.0500);
    date1.put("2018-11-14", 1099.0500);
    date1.put("2018-11-13", 1036.0500);
    date1.put("2018-11-12", 1038.6300);
    date1.put("2018-11-09", 1066.1500);
    date1.put("2018-11-08", 1082.4000);
    date1.put("2018-11-07", 1093.3900);
    date1.put("2018-11-06", 1055.8100);
    date1.put("2018-11-05", 1040.0900);

    HashMap<String, Double> date2 = new HashMap<>();
    date2.put("2018-11-16", 140.00);
    date2.put("2018-11-15", 125.00);
    date2.put("2018-11-14", 130.00);
    date2.put("2018-11-13", 100.00);
    date2.put("2018-11-12", 110.00);
    date2.put("2018-11-09", 90.00);
    date2.put("2018-11-08", 80.00);
    date2.put("2018-11-07", 120.00);
    date2.put("2018-11-06", 105.00);
    date2.put("2018-11-05", 95.00);

    HashMap<String, Double> date3 = new HashMap<>();
    date3.put("2018-11-16", 107.0500);
    date3.put("2018-11-15", 119.0500);
    date3.put("2018-11-14", 110.0500);
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

    p1 = new PortfolioCollectionImpl();
    p2 = new PortfolioCollectionImpl();
  }

  @After
  public void revertStreams() {
    System.setOut(sysOut);
  }

  @Test
  public void testNullConstructor() {
    try {
      PortfolioController c1 = new PortfolioControllerImpl(null, System.out);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "Invalid input");
    }

    try {
      PortfolioController c1 = new PortfolioControllerImpl(new StringReader("new"), null);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "Invalid output");
    }
  }

  @Test
  public void testValidAddSingle() {

    StringReader in = new StringReader("new p1 add GOOG p1 11/05/2018 2000 200 print p1" +
            " 11/05/2018 quit");

    PortfolioController test = new PortfolioControllerImpl(in, System.out);
    test.runPortfolio(p1);

    assertEquals(refineOutput(outContent.toString()),
            "Input the portfolio name to create.\n" +
                    "New portfolio created.\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/05/2018\n" +
                    "Input the portfolio name to print.\n" +
                    "Input a date to view the contents of the portfolio on that date. " +
                    "This must be in the format mm/dd/yyyy.\n" +

                    //print portfolio
                    "GOOG-> Shares: 10.00 costBasis: $2000.00 Total Value: $10400.90\n" +
                    "\n" +
                    "Total Sum: $10400.90 Current commission Fee: $0.00\n");
  }

  @Test
  public void testValidAddMultiple() {
    StringReader in = new StringReader("new p1 add GOOG p1 11/05/2018 12.0 12.0" +
            " add AMD p1 11/07/2018 50.0 5.0 print p1 11/11/2018");
    PortfolioController test = new PortfolioControllerImpl(in, System.out);
    test.runPortfolio(p1);
    assertEquals(refineOutput(outContent.toString()),
            "Input the portfolio name to create.\n" +
                    "New portfolio created.\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/05/2018\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/07/2018\n" +
                    "Input the portfolio name to print.\n" +
                    "Input a date to view the contents of the portfolio on that date. " +
                    "This must be in the format mm/dd/yyyy.\n" +

                    //print portfolio
                    "GOOG-> Shares: 1.00 costBasis: $12.00 Total Value: $1066.15\n" +
                    "AMD-> Shares: 10.00 costBasis: $50.00 Total Value: $900.00\n" +
                    "\n" +
                    "Total Sum: $1966.15 Current commission Fee: $0.00\n");

  }

  @Test
  public void testAddSingleWithApi() {
    StringReader in = new StringReader("new p1 add GOOG p1 11/05/2018 2000 API print p1" +
            " 11/05/2018 quit");

    PortfolioController test = new PortfolioControllerImpl(in, System.out);
    test.runPortfolio(p1);

    assertEquals(refineOutput(outContent.toString()),
            "Input the portfolio name to create.\n" +
                    "New portfolio created.\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/05/2018\n" +
                    "Input the portfolio name to print.\n" +
                    "Input a date to view the contents of the portfolio on that date. " +
                    "This must be in the format mm/dd/yyyy.\n" +

                    //print portfoliio
                    "GOOG-> Shares: 1.92 costBasis: $2000.00 Total Value: $2000.00\n" +
                    "\n" +
                    "Total Sum: $2000.00 Current commission Fee: $0.00\n");
  }

  @Test
  public void testAddMultiWithApi() {
    StringReader in = new StringReader("new p1 add GOOG p1 11/05/2018 12.0 API" +
            " add AMD p1 11/07/2018 50.0 5.0 print p1 11/11/2018");
    PortfolioController test = new PortfolioControllerImpl(in, System.out);
    test.runPortfolio(p1);
    assertEquals(refineOutput(outContent.toString()),
            "Input the portfolio name to create.\n" +
                    "New portfolio created.\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/05/2018\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/07/2018\n" +
                    "Input the portfolio name to print.\n" +
                    "Input a date to view the contents of the portfolio on that date. " +
                    "This must be in the format mm/dd/yyyy.\n" +

                    //print portfolio
                    "GOOG-> Shares: 0.01 costBasis: $12.00 Total Value: $12.30\n" +
                    "AMD-> Shares: 10.00 costBasis: $50.00 Total Value: $900.00\n" +
                    "\n" +
                    "Total Sum: $912.30 Current commission Fee: $0.00\n");
  }



  @Test
  public void testWeekendDateForGetPortfolio() {
    //test for Weekend should be same value as friday
    StringReader in = new StringReader("new p1 add GOOG p1 11/05/2018 2000 200 print p1 " +
            "11/09/2018 print p1 11/10/2018 print p1 11/11/2018");

    PortfolioController test = new PortfolioControllerImpl(in, System.out);
    test.runPortfolio(p1);
    assertEquals(refineOutput(outContent.toString()), "Input the portfolio name to create.\n" +
            "New portfolio created.\n" +
            "Input the stock symbol.\n" +
            "Input the portfolio name to add the stock(s) to.\n" +
            "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
            "Input the total dollar amount of this transaction. For multipletransactions" +
            ", this is the total amount per day.\n" +
            "Input the cost per share.(Enter API to use real time data)\n" +
            "Stock added successfully on date: 11/05/2018\n" +
            "Input the portfolio name to print.\n" +
            "Input a date to view the contents of the portfolio on that date. " +
            "This must be in the format mm/dd/yyyy.\n" +

            //Friday
            "GOOG-> Shares: 10.00 costBasis: $2000.00 Total Value: $10661.50\n" +
            "\n" +
            "Total Sum: $10661.50 Current commission Fee: $0.00\n" +
            "Input the portfolio name to print.\n" +
            "Input a date to view the contents of the portfolio on that date. " +
            "This must be in the format mm/dd/yyyy.\n" +

            //Saturday
            "GOOG-> Shares: 10.00 costBasis: $2000.00 Total Value: $10661.50\n" +
            "\n" +
            "Total Sum: $10661.50 Current commission Fee: $0.00\n" +
            "Input the portfolio name to print.\n" +
            "Input a date to view the contents of the portfolio on that date. " +
            "This must be in the format mm/dd/yyyy.\n" +

            //Sunday
            "GOOG-> Shares: 10.00 costBasis: $2000.00 Total Value: $10661.50\n" +
            "\n" +
            "Total Sum: $10661.50 Current commission Fee: $0.00\n");
  }

  @Test
  public void testInvalidCommandNew() {
    //invalid command for new
    StringReader in = new StringReader("ne quit");
    StringReader in1 = new StringReader("neww@# quit");
    StringReader in2 = new StringReader("new09 quit");

    PortfolioController test = new PortfolioControllerImpl(in, System.out);
    PortfolioController test1 = new PortfolioControllerImpl(in1, System.out);
    PortfolioController test2 = new PortfolioControllerImpl(in2, System.out);

    test.runPortfolio(p2);
    test1.runPortfolio(p2);
    test2.runPortfolio(p2);
    assertEquals("ne is not a valid command.\n" +
            "neww@# is not a valid command.\n" +
            "new09 is not a valid command.\n", refineOutput(outContent.toString()));
  }

  @Test
  public void testInvalidCommandAdd() {
    //invalid command for add
    StringReader in = new StringReader("new hello sdf");
    StringReader in1 = new StringReader("new hello afff");
    StringReader in2 = new StringReader("new hello 039f3");

    PortfolioController test = new PortfolioControllerImpl(in, System.out);
    PortfolioController test1 = new PortfolioControllerImpl(in1, System.out);
    PortfolioController test2 = new PortfolioControllerImpl(in2, System.out);


    test.runPortfolio(p1);
    test1.runPortfolio(p1);
    test2.runPortfolio(p1);
    assertEquals("sdf is not a valid command.\n" +
                    "afff is not a valid command.\n" +
                    "039f3 is not a valid command.\n",
            refineOutputNewPortfolio(refineOutput(outContent.toString())));
  }

  @Test
  public void testInvalidCommandPrint() {
    StringReader in = new StringReader("new p1 printttt p1 11/11/2018");

    PortfolioController test = new PortfolioControllerImpl(in, System.out);

    test.runPortfolio(p1);
    assertEquals("printttt is not a valid command.\n"
                    + "p1 is not a valid command.\n"
                    + "11/11/2018 is not a valid command.\n",
            refineOutputNewPortfolio(refineOutput(outContent.toString())));
  }

  @Test
  public void testInvalidInputDateFormat() {

    StringReader in = new StringReader("new p1 add GOOG p1 1000 11/7/2018 quit");

    PortfolioController test = new PortfolioControllerImpl(in, System.out);

    test.runPortfolio(p1);
    assertEquals("Input the portfolio name to create.\n" +
                    "New portfolio created.\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Invalid date format. This must be in the format mm/dd/yyyy.\n" +
                    "Invalid date format. This must be in the format mm/dd/yyyy.\n",
            refineOutput(outContent.toString()));
  }

  @Test
  public void testInvalidInputDateFormat1() {

    StringReader in = new StringReader("new p1 add GOOG p1 11/1992/01 2000 quit");

    PortfolioController test = new PortfolioControllerImpl(in, System.out);
    test.runPortfolio(p1);

    assertEquals("Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Invalid date format. This must be in the format mm/dd/yyyy.\n" +
                    "Invalid date format. This must be in the format mm/dd/yyyy.\n",
            refineOutputNewPortfolio(refineOutput(outContent.toString())));
  }

  @Test
  public void testInvalidInputDateFormat2() {

    StringReader in = new StringReader("new p1 add GOOG p1 09/31/10 2000  quit");

    PortfolioController test = new PortfolioControllerImpl(in, System.out);
    test.runPortfolio(p1);
    assertEquals("Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Invalid date format. This must be in the format mm/dd/yyyy.\n" +
                    "Invalid date format. This must be in the format mm/dd/yyyy.\n",
            refineOutputNewPortfolio(refineOutput(outContent.toString())));
  }

  @Test
  public void testInvalidInputNumber() {
    StringReader in = new StringReader("new p1 add GOOG p1 abc quit");

    PortfolioController test = new PortfolioControllerImpl(in, System.out);
    test.runPortfolio(p1);
    assertEquals("Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Invalid date format. This must be in the format mm/dd/yyyy.\n",
            refineOutputNewPortfolio(refineOutput(outContent.toString())));
  }

  @Test
  public void testInvalidInputNumberPricePerShare() {
    StringReader in = new StringReader("new p1 add GOOG p1 11/07/2018 2000 abc quit");

    PortfolioController test = new PortfolioControllerImpl(in, System.out);
    test.runPortfolio(p1);
    assertEquals("Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Cost per share must be a number.\n",
            refineOutputNewPortfolio(refineOutput(outContent.toString())));
  }

  @Test
  public void testInvalidInputDatePrint() {
    StringReader in = new StringReader("new p1 add GOOG p1 11/07/2018 2000 200 print " +
            "p1 2018-11-08 quit");

    PortfolioController test = new PortfolioControllerImpl(in, System.out);
    test.runPortfolio(p1);
    String output = refineOutputAdd(refineOutputNewPortfolio(refineOutput(outContent.toString())));
    assertEquals("Input the stock symbol.\n" +
            "Input the portfolio name to add the stock(s) to.\n" +
            "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
            "Input the total dollar amount of this transaction. For multipletransactions" +
            ", this is the total amount per day.\n" +
            "Input the cost per share.(Enter API to use real time data)\n" +
            "Stock added successfully on date: 11/07/2018\n" +
            "Input the portfolio name to print.\n" +
            "Input a date to view the contents of the portfolio on that date. " +
            "This must be in the format mm/dd/yyyy.\n" +
            "Invalid date format. This must be in the format mm/dd/yyyy.\n", output);
  }

  @Test
  public void testInputWithQ1() {
    StringReader in = new StringReader("quit new p1 print p1 11/11/2018");
    PortfolioController test = new PortfolioControllerImpl(in, System.out);
    test.runPortfolio(p1);
    assertEquals(refineOutput(outContent.toString()), "");

    StringReader in1 = new StringReader("new quit");
    PortfolioController test1 = new PortfolioControllerImpl(in1, System.out);
    test1.runPortfolio(p1);
    assertEquals(refineOutput(outContent.toString()), "Input the portfolio name to create.\n");
  }

  @Test
  public void testInputWithQ2() {
    StringReader in2 = new StringReader("new p1 quit 11/11/2018");
    PortfolioController test2 = new PortfolioControllerImpl(in2, System.out);
    test2.runPortfolio(p1);
    assertEquals("Input the portfolio name to create.\n" +
            "New portfolio created.\n", refineOutput(outContent.toString()));
  }

  @Test
  public void testInputWithQ3() {
    StringReader in3 = new StringReader("new p1 add quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals("Input the stock symbol.\n",
            refineOutputNewPortfolio(refineOutput(outContent.toString())));
  }

  @Test
  public void testInputWithQ4() {
    StringReader in4 = new StringReader("new p1 add GOOG quit");
    PortfolioController test4 = new PortfolioControllerImpl(in4, System.out);
    test4.runPortfolio(p1);
    assertEquals("Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n",
            refineOutputNewPortfolio(refineOutput(outContent.toString())));
  }

  @Test
  public void testInputWithQ5() {
    StringReader in5 = new StringReader("new p1 add GOOG p1 2000 quit");
    PortfolioController test5 = new PortfolioControllerImpl(in5, System.out);
    test5.runPortfolio(p1);
    assertEquals("Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Invalid date format. This must be in the format mm/dd/yyyy.\n",
            refineOutputNewPortfolio(refineOutput(outContent.toString())));
  }

  @Test
  public void testInputWithQ6() {
    StringReader in6 = new StringReader("new p1 add GOOG p1 11/07/2018 2000 quit");
    PortfolioController test6 = new PortfolioControllerImpl(in6, System.out);
    test6.runPortfolio(p1);
    assertEquals("Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n",
            refineOutputNewPortfolio(refineOutput(outContent.toString())));
  }


  private String refineOutput(String output) {
    return output.replace("Enter a command. The options are:\n" +
            "new - create a new portfolio\n" +
            "add - add a new stock with real time price from API or manually input price.\n" +
            "print - output the contents of the portfolio\n" +
            "addgroup - Make a purchase of all stocks in a portfolio based on a total amount " +
            "and weights.\n" +
            "addschedule - Implement a dollar cost averaging strategy based on total amount " +
            "and weights. This is similar to the addgoup bus allows the user to schedule " +
            "multiple transactions. User will specify a start and end date, the frequency " +
            "of transactions, and the amount per transaction.\n" +
            "addcommission - add a commission fee that will be deducted per transaction. " +
            "This can be set for each portfolio.\n" +
            "quit - Input anytime to exit the program\n", "").replace("\n####################" +
            "#########################################\n", "");
  }

  private String refineOutputNewPortfolio(String output) {
    return output.replace("Input the portfolio name to create.\n" +
            "New portfolio created.\n", "");
  }

  private String refineOutputAdd(String output) {
    return output.replace("Input the portfolio name to add the stock to.\n" +
            "Input the stock symbol.\n" +
            "Input the dollar amount.\n" +
            "Input the date. This must be in the format mm/dd/yyyy.\n" +
            "Input the cost per share.(Enter API to use real time data)\n" +
            "Stock added successfully.\n", "");
  }


  @Test
  public void testAddGroupFixedWeight() {
    //test fixed weight 0.2 0.3 0.5
    StringReader in3 = new StringReader("new p1 add GOOG p1 11/05/2018 2000 200 add FB p1" +
            " 11/06/2018 500 50 add AMD p1 11/06/2018 1000 100 addgroup p1 11/07/2018 5000" +
            " n 0.2 0.3 0.5 print p1 11/12/2018 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(
            outContent.toString())), "Input the stock symbol.\n" +
            "Input the portfolio name to add the stock(s) to.\n" +
            "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
            "Input the total dollar amount of this transaction. For multipletransactions" +
            ", this is the total amount per day.\n" +
            "Input the cost per share.(Enter API to use real time data)\n" +
            "Stock added successfully on date: 11/05/2018\n" +
            "Input the stock symbol.\n" +
            "Input the portfolio name to add the stock(s) to.\n" +
            "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
            "Input the total dollar amount of this transaction. For multipletransactions" +
            ", this is the total amount per day.\n" +
            "Input the cost per share.(Enter API to use real time data)\n" +
            "Stock added successfully on date: 11/06/2018\n" +
            "Input the stock symbol.\n" +
            "Input the portfolio name to add the stock(s) to.\n" +
            "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
            "Input the total dollar amount of this transaction. For multipletransactions" +
            ", this is the total amount per day.\n" +
            "Input the cost per share.(Enter API to use real time data)\n" +
            "Stock added successfully on date: 11/06/2018\n" +
            "Input the portfolio name to add the stock(s) to.\n" +
            "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
            "Input the total dollar amount of this transaction. For multipletransactions" +
            ", this is the total amount per day.\n" +
            "Do you want all stocks in the group to have equal weightings? " +
            "Type y for yes or n for no.\n" +
            "Input the weight for stock: GOOG. This must be between 0 and 1," +
            " and all weights need to add to 1.\n" +
            "Input the weight for stock: AMD. This must be between 0 and 1, " +
            "and all weights need to add to 1.\n" +
            "Input the weight for stock: FB. This must be between 0 and 1," +
            " and all weights need to add to 1.\n" +

            //fixed weight
            "The weights are: {GOOG=0.2, AMD=0.3, FB=0.5}\n" +
            "Transactions added successfully.\n" +
            "Input the portfolio name to print.\n" +
            "Input a date to view the contents of the portfolio on that date. " +
            "This must be in the format mm/dd/yyyy.\n" +

            //print portfolio
            "GOOG-> Shares: 10.91 costBasis: $3000.00 Total Value: $11336.22\n" +
            "AMD-> Shares: 22.50 costBasis: $2500.00 Total Value: $2475.00\n" +
            "FB-> Shares: 26.50 costBasis: $3000.00 Total Value: $3750.85\n" +
            "\n" +
            "Total Sum: $17562.06 Current commission Fee: $0.00\n");
  }


  @Test
  public void testAddGroupEqualWeight() {
    StringReader in3 = new StringReader("new p1 add GOOG p1 11/05/2018 2000 200 add FB p1" +
            " 11/06/2018 500 50 add AMD p1 11/06/2018 1000 100 addgroup p1 11/07/2018 5000" +
            " y print p1 11/12/2018 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals("Input the stock symbol.\n" +
            "Input the portfolio name to add the stock(s) to.\n" +
            "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
            "Input the total dollar amount of this transaction. For multipletransactions" +
            ", this is the total amount per day.\n" +
            "Input the cost per share.(Enter API to use real time data)\n" +
            "Stock added successfully on date: 11/05/2018\n" +
            "Input the stock symbol.\n" +
            "Input the portfolio name to add the stock(s) to.\n" +
            "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
            "Input the total dollar amount of this transaction. For multipletransactions" +
            ", this is the total amount per day.\n" +
            "Input the cost per share.(Enter API to use real time data)\n" +
            "Stock added successfully on date: 11/06/2018\n" +
            "Input the stock symbol.\n" +
            "Input the portfolio name to add the stock(s) to.\n" +
            "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
            "Input the total dollar amount of this transaction. For multipletransactions" +
            ", this is the total amount per day.\n" +
            "Input the cost per share.(Enter API to use real time data)\n" +
            "Stock added successfully on date: 11/06/2018\n" +
            "Input the portfolio name to add the stock(s) to.\n" +
            "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
            "Input the total dollar amount of this transaction. For multipletransactions" +
            ", this is the total amount per day.\n" +

            //type y for quickly use equal weight
            "Do you want all stocks in the group to have equal weightings? " +
            "Type y for yes or n for no.\n" +

            //print weights
            "The weights are: " +
            "{GOOG=0.3333333333333333, AMD=0.3333333333333333, FB=0.3333333333333333}\n" +
            "Transactions added successfully.\n" +
            "Input the portfolio name to print.\n" +
            "Input a date to view the contents of the portfolio on that date. " +
            "This must be in the format mm/dd/yyyy.\n" +
            "GOOG-> Shares: 11.52 costBasis: $3666.67 Total Value: $11969.50\n" +
            "AMD-> Shares: 23.89 costBasis: $2666.67 Total Value: $2627.78\n" +
            "FB-> Shares: 21.00 costBasis: $2166.67 Total Value: $2972.40\n" +
            "\n" +
            "Total Sum: $17569.67 Current commission Fee: $0.00\n",
            refineOutputNewPortfolio(refineOutput(
            outContent.toString()))
    );
  }



  @Test
  public void testAddSchedule() {
    //Equal weight with end date
    StringReader in3 = new StringReader("new p1 add GOOG p1 11/05/2018 1000 100 add AMD p1 " +
            "11/05/2018 500 50 addschedule p1 11/06/2018 5000 y 11/12/2018 1 print p1 11/08/2018" +
            " print p1 11/12/2018 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            "Input the stock symbol.\n" +
            "Input the portfolio name to add the stock(s) to.\n" +
            "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
            "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
            "Input the cost per share.(Enter API to use real time data)\n" +
            "Stock added successfully on date: 11/05/2018\n" +
            "Input the stock symbol.\n" +
            "Input the portfolio name to add the stock(s) to.\n" +
            "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
            "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
            "Input the cost per share.(Enter API to use real time data)\n" +
            "Stock added successfully on date: 11/05/2018\n" +
            "Input the portfolio name to add the stock(s) to.\n" +
            "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
            "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
            "Do you want all stocks in the group to have equal weightings? " +
                    "Type y for yes or n for no.\n" +
            "The weights are: {GOOG=0.5, AMD=0.5}\n" +
            "Input the end date. This must be in the format mm/dd/yyyy. " +
                    "Input \"now\" to use current date. Type \"none\" " +
                    "to have no end date. Future dates can be input. \n" +
            "Enter the number of days between transactions. " +
                    "ie 7 days means the transaction will take place weekly." +
                    " If the date falls on a holiday or weekend, " +
                    "then the next business day will be used.\n" +
            "Transactions added successfully.\n" +
            "Input the portfolio name to print.\n" +
            "Input a date to view the contents of the portfolio on that date." +
                    " This must be in the format mm/dd/yyyy.\n" +


            "GOOG-> Shares: 16.96 costBasis: $8500.00 Total Value: $18361.83\n" +
            "AMD-> Shares: 85.89 costBasis: $8000.00 Total Value: $6871.43\n" +
            "\n" +
            "Total Sum: $25233.26 Current commission Fee: $0.00\n" +
            "Input the portfolio name to print.\n" +
            "Input a date to view the contents of the portfolio on that date. " +
                    "This must be in the format mm/dd/yyyy.\n" +


            "GOOG-> Shares: 24.12 costBasis: $16000.00 Total Value: $25054.79\n" +
            "AMD-> Shares: 159.13 costBasis: $15500.00 Total Value: $17503.77\n" +
            "\n" +
            "Total Sum: $42558.56 Current commission Fee: $0.00\n");
  }



  @Test
  public void testAddScheduleDifferentWeight() {
    StringReader in3 = new StringReader("new p1 add GOOG p1 11/05/2018 1000 100 add AMD p1 " +
            "11/05/2018 500 50 addschedule p1 11/06/2018 5000 n 0.3 0.7 11/12/2018 1 print p1" +
            " 11/08/2018 print p1 11/12/2018 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/05/2018\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/05/2018\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Do you want all stocks in the group to have equal weightings? " +
                    "Type y for yes or n for no.\n" +
                    "Input the weight for stock: GOOG. This must be between 0 and 1," +
                    " and all weights need to add to 1.\n" +
                    "Input the weight for stock: AMD. This must be between 0 and 1," +
                    " and all weights need to add to 1.\n" +

                    //print different weight
                    "The weights are: {GOOG=0.3, AMD=0.7}\n" +
                    "Input the end date. This must be in the format mm/dd/yyyy. Input" +
                    " \"now\" to use current date. Type \"none\" to have no end date. " +
                    "Future dates can be input. \n" +
                    "Enter the number of days between transactions. ie 7 days means " +
                    "the transaction will take place weekly. If the date falls on a " +
                    "holiday or weekend, then the next business day will be used.\n" +
                    "Transactions added successfully.\n" +
                    "Input the portfolio name to print.\n" +
                    "Input a date to view the contents of the portfolio on that date. " +
                    "This must be in the format mm/dd/yyyy.\n" +

                    //print portfolio before
                    "GOOG-> Shares: 14.18 costBasis: $5500.00 Total Value: $15346.70\n" +
                    "AMD-> Shares: 116.25 costBasis: $11000.00 Total Value: $9300.00\n" +
                    "\n" +
                    "Total Sum: $24646.70 Current commission Fee: $0.00\n" +
                    "Input the portfolio name to print.\n" +
                    "Input a date to view the contents of the portfolio on that date. " +
                    "This must be in the format mm/dd/yyyy.\n" +

                    //print portfolio after
                    "GOOG-> Shares: 18.47 costBasis: $10000.00 Total Value: $19187.39\n" +
                    "AMD-> Shares: 218.78 costBasis: $21500.00 Total Value: $24065.28\n" +
                    "\n" +
                    "Total Sum: $43252.67 Current commission Fee: $0.00\n");
  }


  @Test
  public void testAddScheduleFuture() {
    //test future date with different weight 0.2 0.3 0.5
    StringReader in3 = new StringReader("new p1 add GOOG p1 11/05/2018 1000 100 add AMD p1 " +
            "11/05/2018 500 50 add AMC p1 11/06/2018 1000 100 addschedule p1 11/06/2018 5000 n" +
            " 0.2 0.3 0.5 12/20/2018 4 print p1" +
            " 11/08/2018 print p1 11/16/2018 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock to.\n" +
                    "Input the date. This must be in the format mm/dd/yyyy.\n" +
                    "Input the dollar amount.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/05/2018\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock to.\n" +
                    "Input the date. This must be in the format mm/dd/yyyy.\n" +
                    "Input the dollar amount.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/05/2018\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock to.\n" +
                    "Input the date. This must be in the format mm/dd/yyyy.\n" +
                    "Input the dollar amount.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/06/2018\n" +
                    "Input the portfolio name to add the stock to.\n" +
                    "Input the date. This must be in the format mm/dd/yyyy.\n" +
                    "Input the dollar amount.\n" +
                    "Do you want all stocks in the group to have equal weightings? Type y for " +
                    "yes or n for no.\n" +
                    "Input the weight for stock: GOOG. This must be between 0 and 1, and all " +
                    "weights need to add to 1.\n" +
                    "Input the weight for stock: AMC. This must be between 0 and 1, and all " +
                    "weights need to add to 1.\n" +
                    "Input the weight for stock: AMD. This must be between 0 and 1, and all " +
                    "weights need to add to 1.\n" +

                    //print different weight
                    "The weights are: {GOOG=0.2, AMC=0.3, AMD=0.5}\n" +
                    "Input the end date. This must be in the format mm/dd/yyyy. Input " +
                    "\"now\" to use current date. Type \"none\" to have no end date. Future " +
                    "dates can be input. \n" +
                    "Enter the number of days between transactions. ie 7 days means the " +
                    "transaction will take place weekly. If the date falls on a holiday or" +
                    " weekend, then the next business day will be used.\n" +
                    "Stock not added successfully.\n" +
                    "Input the portfolio name to print.\n" +
                    "Input a date to view the contents of the portfolio on that date. This " +
                    "must be in the format mm/dd/yyyy.\n" +

                    //print portfolio
                    "GOOG-> Shares: 10.95 costBasis: $2000.00 Total Value: $11849.18\n" +
                    "AMC-> Shares: 24.18 costBasis: $2500.00 Total Value: $2620.72\n" +
                    "AMD-> Shares: 33.81 costBasis: $3000.00 Total Value: $2704.76\n" +
                    "\nTotal Sum: $17174.66\n" +

                    "Input the portfolio name to print.\n" +
                    "Input a date to view the contents of the portfolio on that date. This must" +
                    " be in the format mm/dd/yyyy.\n" +

                    //print portfolio
                    "GOOG-> Shares: 12.82 costBasis: $4000.00 Total Value: $13538.37\n" +
                    "AMC-> Shares: 52.28 costBasis: $5500.00 Total Value: $5596.69\n" +
                    "AMD-> Shares: 75.77 costBasis: $8000.00 Total Value: $10607.46\n" +
                    "\nTotal Sum: $29742.53\n");
  }


  @Test
  public void testAddCommissionFee() {
    StringReader in = new StringReader("new p1 addcommission p1 5 add GOOG p1 11/05/2018 2000 200 "
            + "add AMD p1 11/07/2018 1000 100 print p1 11/08/2018 quit");

    PortfolioController test = new PortfolioControllerImpl(in, System.out);
    test.runPortfolio(p1);

    assertEquals(refineOutput(outContent.toString()),
            "Input the portfolio name to create.\n" +
                    "New portfolio created.\n" +
                    "Input the portfolio name to add the commission fee to. " +
                    "This will apply for all future transactions in this " +
                    "portfolio until specified otherwise.\n" +
                    "Input the dollar amount of the commission fee per transaction.\n" +
                    "Commission fee of 5.0 added.\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/05/2018\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/07/2018\n" +
                    "Input the portfolio name to print.\n" +
                    "Input a date to view the contents of the portfolio on that date. " +
                    "This must be in the format mm/dd/yyyy.\n" +

                    //print result for $5 commission
                    "GOOG-> Shares: 9.98 costBasis: $1995.00 Total Value: $10796.94\n" +
                    "AMD-> Shares: 9.95 costBasis: $995.00 Total Value: $796.00\n" +
                    "\n" +
                    "Total Sum: $11592.94 Current commission Fee: $5.00\n");
  }

  @Test
  public void testAddScheduleOnEmptyPortfolio() {
    StringReader in3 = new StringReader("new p1 addschedule p1 11/06/2018 2000 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +

                    //output message
                    "No stock in portfolio, add some stocks first.\n\n");

  }

  @Test
  public void testAddGroupOnEmptyPortfolio() {
    StringReader in3 = new StringReader("new p1 addgroup p1 11/06/2018 2000 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +

                    //output message
                    "No stock in portfolio, add some stocks first.\n");

  }

  @Test
  public void testAddGroupWrongWeights() {
    //test weight 0.4 0.3 0.5
    StringReader in3 = new StringReader("new p1 add GOOG p1 11/05/2018 2000 200 add FB p1" +
            " 11/06/2018 500 50 add AMD p1 11/06/2018 1000 100 addgroup p1 11/07/2018 5000" +
            " n 0.4 0.3 0.5 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(
            outContent.toString())), "Input the stock symbol.\n" +
            "Input the portfolio name to add the stock(s) to.\n" +
            "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
            "Input the total dollar amount of this transaction. For multipletransactions" +
            ", this is the total amount per day.\n" +
            "Input the cost per share.(Enter API to use real time data)\n" +
            "Stock added successfully on date: 11/05/2018\n" +
            "Input the stock symbol.\n" +
            "Input the portfolio name to add the stock(s) to.\n" +
            "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
            "Input the total dollar amount of this transaction. For multipletransactions" +
            ", this is the total amount per day.\n" +
            "Input the cost per share.(Enter API to use real time data)\n" +
            "Stock added successfully on date: 11/06/2018\n" +
            "Input the stock symbol.\n" +
            "Input the portfolio name to add the stock(s) to.\n" +
            "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
            "Input the total dollar amount of this transaction. For multipletransactions" +
            ", this is the total amount per day.\n" +
            "Input the cost per share.(Enter API to use real time data)\n" +
            "Stock added successfully on date: 11/06/2018\n" +
            "Input the portfolio name to add the stock(s) to.\n" +
            "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
            "Input the total dollar amount of this transaction. For multipletransactions" +
            ", this is the total amount per day.\n" +
            "Do you want all stocks in the group to have equal weightings? " +
            "Type y for yes or n for no.\n" +
            "Input the weight for stock: GOOG. This must be between 0 and 1," +
            " and all weights need to add to 1.\n" +
            "Input the weight for stock: AMD. This must be between 0 and 1," +
            " and all weights need to add to 1.\n" +
            "Input the weight for stock: FB. This must be between 0 and 1," +
            " and all weights need to add to 1.\n" +

            //output message
            "The weightings must add up to 1. Group stocks not added.\n");

  }


  @Test
  public void testCommissionFeeBiggerThanCost() {
    //cost basis 40 commission 50
    StringReader in = new StringReader("new p1 addcommission p1 50 add GOOG p1 11/05/2018 40 200 " +
            "quit");

    PortfolioController test = new PortfolioControllerImpl(in, System.out);
    test.runPortfolio(p1);

    assertEquals(refineOutput(outContent.toString()),
            "Input the portfolio name to create.\n" +
                    "New portfolio created.\n" +
                    "Input the portfolio name to add the commission fee to." +
                    " This will apply for all future transactions in this portfolio" +
                    " until specified otherwise.\n" +
                    "Input the dollar amount of the commission fee per transaction.\n" +
                    "Commission fee of 50.0 added.\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions," +
                    " this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +

                    //output message
                    "Need to input a positive dollar amount or the amount is less than commission" +
                    " fee. Stock not added.\n");
  }

  @Test
  public void testAddOnHoliday() {
    //test thanksgiving 11/22/2018
    StringReader in = new StringReader("new p1 addcommission p1 5 add JD p1 11/22/2018 2000 API " +
            "print p1 11/23/2018 quit");

    PortfolioController test = new PortfolioControllerImpl(in, System.out);
    test.runPortfolio(p1);

    assertEquals(refineOutput(outContent.toString()),
            "Input the portfolio name to create.\n" +
                    "New portfolio created.\n" +
                    "Input the portfolio name to add the commission fee to." +
                    " This will apply for all future transactions in this " +
                    "portfolio until specified otherwise.\n" +
                    "Input the dollar amount of the commission fee per transaction.\n" +
                    "Commission fee of 5.0 added.\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For " +
                    "multipletransactions, this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +

                    //Bought on next working date after thanksgiving
                    "Stock added successfully on date: 11/23/2018\n" +
                    "Input the portfolio name to print.\n" +
                    "Input a date to view the contents of the portfolio on that date. " +
                    "This must be in the format mm/dd/yyyy.\n" +

                    //print on 11/23/2018
                    "JD-> Shares: 103.53 costBasis: $1995.00 Total Value: $1995.00\n" +
                    "\n" +
                    "Total Sum: $1995.00 Current commission Fee: $5.00\n");
  }

  @Test
  public void testAddOnWeekends() {
    //test Saturday 11/24/2018
    StringReader in = new StringReader("new p1 addcommission p1 5 add JD p1 11/24/2018 2000 API " +
            "print p1 11/26/2018 quit");

    PortfolioController test = new PortfolioControllerImpl(in, System.out);
    test.runPortfolio(p1);

    assertEquals(refineOutput(outContent.toString()), "Input the portfolio name to create.\n" +
            "New portfolio created.\n" +
            "Input the portfolio name to add the commission fee to. This will apply for" +
            " all future transactions in this portfolio until specified otherwise.\n" +
            "Input the dollar amount of the commission fee per transaction.\n" +
            "Commission fee of 5.0 added.\n" +
            "Input the stock symbol.\n" +
            "Input the portfolio name to add the stock(s) to.\n" +
            "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
            "Input the total dollar amount of this transaction. For multipletransactions, " +
            "this is the total amount per day.\n" +
            "Input the cost per share.(Enter API to use real time data)\n" +

            //added on next Monday 11/26/2018
            "Stock added successfully on date: 11/26/2018\n" +
            "Input the portfolio name to print.\n" +
            "Input a date to view the contents of the portfolio on that date. " +
            "This must be in the format mm/dd/yyyy.\n" +

            //print portfolio on 11/26/2018
            "JD-> Shares: 97.51 costBasis: $1995.00 Total Value: $1995.00\n" +
            "\n" +
            "Total Sum: $1995.00 Current commission Fee: $5.00\n");
  }

  @Test
  public void testAddScheduleOnHoliday() {
    //test on thanksgiving 11/22/2018
    StringReader in3 = new StringReader("new p1 add JD p1 11/05/2018 1000 100 add FB p1 " +
            "11/05/2018 500 50 addschedule p1 11/21/2018 5000 n" +
            " 0.3 0.7 12/20/2018 1 print p1" +
            " 11/23/2018 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/05/2018\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/05/2018\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Do you want all stocks in the group to have equal weightings? Type y for " +
                    "yes or n for no.\n" +
                    "Input the weight for stock: JD. This must be between 0 and 1," +
                    " and all weights need to add to 1.\n" +
                    "Input the weight for stock: FB. This must be between 0 and 1," +
                    " and all weights need to add to 1.\n" +
                    "The weights are: {JD=0.3, FB=0.7}\n" +
                    "Input the end date. This must be in the format mm/dd/yyyy. " +
                    "Input \"now\" to use current date. Type \"none\" to have no" +
                    " end date. Future dates can be input. \n" +
                    "Enter the number of days between transactions. ie 7 days" +
                    " means the transaction will take place weekly. If the date " +
                    "falls on a holiday or weekend, then the next business day will be used.\n" +
                    "Transactions added successfully.\n" +
                    "Input the portfolio name to print.\n" +
                    "Input a date to view the contents of the portfolio on that date. " +
                    "This must be in the format mm/dd/yyyy.\n" +

                    //print portfolio for 11/23/2018
                    "JD-> Shares: 239.43 costBasis: $5500.00 Total Value: $4613.79\n" +
                    "FB-> Shares: 89.10 costBasis: $11000.00 Total Value: $11737.08\n" +
                    "\n" +
                    "Total Sum: $16350.87 Current commission Fee: $0.00\n");
  }


  @Test
  public void testCommissionOnOneTransaction() {
    //test no commission on first transaction, but with $5 commission on second transaction
    StringReader in3 = new StringReader("new p1 add JD p1 11/05/2018 1000 100 addcommission p1 5 " +
            "add FB p1 11/05/2018 500 50 print p1 11/23/2018 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/05/2018\n" +
                    "Input the portfolio name to add the commission fee to. This will apply for " +
                    "all future transactions in this portfolio until specified otherwise.\n" +
                    "Input the dollar amount of the commission fee per transaction.\n" +
                    "Commission fee of 5.0 added.\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/05/2018\n" +
                    "Input the portfolio name to print.\n" +
                    "Input a date to view the contents of the portfolio on that date. This must " +
                    "be in the format mm/dd/yyyy.\n" +

                    //Only second transaction has commission fee $5
                    "JD-> Shares: 10.00 costBasis: $1000.00 Total Value: $192.70\n" +
                    "FB-> Shares: 9.90 costBasis: $495.00 Total Value: $1304.13\n" +
                    "\n" +
                    "Total Sum: $1496.83 Current commission Fee: $5.00\n");
  }


  @Test
  public void testChangeCommission() {
    //first commission $10, second commission $5
    StringReader in3 = new StringReader("new p1 addcommission p1 10 add JD p1 11/05/2018 1000 100" +
            " addcommission p1 5 add FB p1 11/05/2018 500 50 print p1 11/23/2018 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            "Input the portfolio name to add the commission fee to. This will apply for all" +
                    " future transactions in this portfolio until specified otherwise.\n" +
                    "Input the dollar amount of the commission fee per transaction.\n" +
                    "Commission fee of 10.0 added.\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/05/2018\n" +
                    "Input the portfolio name to add the commission fee to. This will apply" +
                    " for all future transactions in this portfolio until specified otherwise.\n" +
                    "Input the dollar amount of the commission fee per transaction.\n" +
                    "Commission fee of 5.0 added.\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/05/2018\n" +
                    "Input the portfolio name to print.\n" +
                    "Input a date to view the contents of the portfolio on that date. This must" +
                    " be in the format mm/dd/yyyy.\n" +

                    //first commission $10, second commission $5
                    "JD-> Shares: 9.90 costBasis: $990.00 Total Value: $190.77\n" +
                    "FB-> Shares: 9.90 costBasis: $495.00 Total Value: $1304.13\n" +
                    "\n" +
                    "Total Sum: $1494.90 Current commission Fee: $5.00\n");
  }

  @Test
  public void testAddGroupWithCommission() {
    StringReader in3 = new StringReader("new p1 addcommission p1 5 add GOOG p1 11/05/2018 2000 200"
            + " add FB p1 11/06/2018 500 50 add AMD p1 11/06/2018 1000 100 addgroup p1 11/07/2018" +
            " 5000 n 0.2 0.3 0.5 print p1 11/15/2018 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            "Input the portfolio name to add the commission fee to. This will apply for all " +
                    "future transactions in this portfolio until specified otherwise.\n" +
                    "Input the dollar amount of the commission fee per transaction.\n" +
                    "Commission fee of 5.0 added.\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/05/2018\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/06/2018\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/06/2018\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Do you want all stocks in the group to have equal weightings? " +
                    "Type y for yes or n for no.\n" +
                    "Input the weight for stock: GOOG. This must be between 0 and 1" +
                    ", and all weights need to add to 1.\n" +
                    "Input the weight for stock: AMD. This must be between 0 and 1," +
                    " and all weights need to add to 1.\n" +
                    "Input the weight for stock: FB. This must be between 0 and 1," +
                    " and all weights need to add to 1.\n" +
                    "The weights are: {GOOG=0.2, AMD=0.3, FB=0.5}\n" +
                    "Transactions added successfully.\n" +
                    "Input the portfolio name to print.\n" +
                    "Input a date to view the contents of the portfolio on that date." +
                    " This must be in the format mm/dd/yyyy.\n" +

                    //output result with $5 commission
                    "GOOG-> Shares: 10.89 costBasis: $2990.00 Total Value: $12365.92\n" +
                    "AMD-> Shares: 22.41 costBasis: $2490.00 Total Value: $2801.04\n" +
                    "FB-> Shares: 26.37 costBasis: $2990.00 Total Value: $3792.66\n" +
                    "\n" +
                    "Total Sum: $18959.62 Current commission Fee: $5.00\n");
  }


  @Test
  public void testAddScheduleWithCommission() {
    StringReader in3 = new StringReader("new p1 addcommission p1 10 add JD p1 03/05/2018 1000 API" +
            " add FB p1 03/05/2018 500 API addschedule p1 04/02/2018 5000 n 0.3 0.7 11/20/2018 " +
            "7 print p1 05/23/2018 print p1 06/23/2018 print p1 07/23/2018 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            "Input the portfolio name to add the commission fee to. This will apply for all" +
                    " future transactions in this portfolio until specified otherwise.\n" +
                    "Input the dollar amount of the commission fee per transaction.\n" +
                    "Commission fee of 10.0 added.\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 03/05/2018\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 03/05/2018\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Do you want all stocks in the group to have equal weightings? " +
                    "Type y for yes or n for no.\n" +
                    "Input the weight for stock: JD. This must be between 0 and 1," +
                    "and all weights need to add to 1.\n" +
                    "Input the weight for stock: FB. This must be between 0 and 1," +
                    " and all weights need to add to 1.\n" +
                    "The weights are: {JD=0.3, FB=0.7}\n" +
                    "Input the end date. This must be in the format mm/dd/yyyy. " +
                    "Input \"now\" to use current date. Type \"none\" to have no end date." +
                    " Future dates can be input. \n" +
                    "Enter the number of days between transactions. ie 7 days means the" +
                    " transaction will take place weekly. If the date falls on a holiday " +
                    "or weekend, then the next business day will be used.\n" +
                    "Transactions added successfully.\n" +
                    "Input the portfolio name to print.\n" +
                    "Input a date to view the contents of the portfolio on that date. " +
                    "This must be in the format mm/dd/yyyy.\n" +

                    //print portfolio on 05/23/2018
                    "JD-> Shares: 336.37 costBasis: $12910.00 Total Value: $12351.64\n" +
                    "FB-> Shares: 167.01 costBasis: $28410.00 Total Value: $31213.94\n" +
                    "\n" +
                    "Total Sum: $43565.58 Current commission Fee: $10.00\n" +
                    "Input the portfolio name to print.\n" +
                    "Input a date to view the contents of the portfolio on that date. " +
                    "This must be in the format mm/dd/yyyy.\n" +

                    //print portfolio on 06/23/2018
                    "JD-> Shares: 487.85 costBasis: $18870.00 Total Value: $19913.87\n" +
                    "FB-> Shares: 239.67 costBasis: $42370.00 Total Value: $48351.96\n" +
                    "\n" +
                    "Total Sum: $68265.83 Current commission Fee: $10.00\n" +
                    "Input the portfolio name to print.\n" +
                    "Input a date to view the contents of the portfolio on that date. " +
                    "This must be in the format mm/dd/yyyy.\n" +

                    //print portfolio on 07/23/2018
                    "JD-> Shares: 683.06 costBasis: $26320.00 Total Value: $24357.83\n" +
                    "FB-> Shares: 325.57 costBasis: $59820.00 Total Value: $68665.33\n" +
                    "\n" +
                    "Total Sum: $93023.16 Current commission Fee: $10.00\n");
  }


  @Test
  public void testAddGroupInvestDateBeforeTransactionDate() {
    //Three stocks were bought on 11/05/2018, 11/06/2018, 11/09/2018,
    //investment date is 11/07/2018
    StringReader in3 = new StringReader("new p1 addcommission p1 5 add GOOG p1 11/05/2018 2000 200"
            + " add FB p1 11/09/2018 500 50 add AMD p1 11/06/2018 1000 100 addgroup p1 11/07/2018" +
            " 5000 n 0.2 0.8 print p1 11/15/2018 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            "Input the portfolio name to add the commission fee to. " +
                    "This will apply for all future transactions in " +
                    "this portfolio until specified otherwise.\n" +
                    "Input the dollar amount of the commission fee per transaction.\n" +
                    "Commission fee of 5.0 added.\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/05/2018\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/09/2018\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 11/06/2018\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Do you want all stocks in the group to have equal weightings? " +
                    "Type y for yes or n for no.\n" +
                    "Input the weight for stock: GOOG. This must be between 0 and 1" +
                    ", and all weights need to add to 1.\n" +
                    "Input the weight for stock: AMD. This must be between 0 and 1," +
                    " and all weights need to add to 1.\n" +

                    //only two stocks were valid at the input date
                    "The weights are: {GOOG=0.2, AMD=0.8}\n" +
                    "Transactions added successfully.\n" +
                    "Input the portfolio name to print.\n" +
                    "Input a date to view the contents of the portfolio on that date. " +
                    "This must be in the format mm/dd/yyyy.\n" +

                    //FB didn't not change, only the other two stocks changed
                    "GOOG-> Shares: 10.89 costBasis: $2990.00 Total Value: $12365.92\n" +
                    "AMD-> Shares: 43.24 costBasis: $4990.00 Total Value: $5405.21\n" +
                    "FB-> Shares: 9.90 costBasis: $495.00 Total Value: $1424.12\n" +
                    "\n" +
                    "Total Sum: $19195.24 Current commission Fee: $5.00\n");
  }



  @Test
  public void testAddScheduleInvestDateBeforeTransactionDate() {
    //JD 03/05/2018, FB 05/05/2018, invest date 04/02/2018
    StringReader in3 = new StringReader("new p1 addcommission p1 10 add JD p1 03/05/2018 1000 API" +
            " add FB p1 05/05/2018 500 API addschedule p1 04/02/2018 5000 n 1 11/20/2018 " +
            "7 print p1 05/23/2018 print p1 06/23/2018 print p1 07/23/2018 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            "Input the portfolio name to add the commission fee to. " +
                    "This will apply for all future transactions in this portfolio until" +
                    " specified otherwise.\n" +
                    "Input the dollar amount of the commission fee per transaction.\n" +
                    "Commission fee of 10.0 added.\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions," +
                    " this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 03/05/2018\n" +
                    "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 05/07/2018\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions," +
                    " this is the total amount per day.\n" +
                    "Do you want all stocks in the group to have equal weightings? Type y for yes" +
                    " or n for no.\n" +
                    "Input the weight for stock: JD. This must be between 0 and 1, and all weight" +
                    "s need to add to 1.\n" +


                    //only JD was valid at input date
                    "The weights are: {JD=1.0}\n" +
                    "Input the end date. This must be in the format mm/dd/yyyy. Input \"now\" " +
                    "to use current date. Type \"none\" to have no end date. Future dates can " +
                    "be input. \n" +
                    "Enter the number of days between transactions. ie 7 days means the " +
                    "transaction will take place weekly. If the date falls on a holiday or " +
                    "weekend, then the next business day will be used.\n" +
                    "Transactions added successfully.\n" +
                    "Input the portfolio name to print.\n" +
                    "Input a date to view the contents of the portfolio on that date. This" +
                    " must be in the format mm/dd/yyyy.\n" +

                    //print portfolio on 05/23/208, FB didn't change
                    "JD-> Shares: 1072.42 costBasis: $40910.00 Total Value: $39379.24\n" +
                    "FB-> Shares: 2.75 costBasis: $490.00 Total Value: $514.59\n" +
                    "\n" +
                    "Total Sum: $39893.82 Current commission Fee: $10.00\n" +
                    "Input the portfolio name to print.\n" +
                    "Input a date to view the contents of the portfolio on that date. " +
                    "This must be in the format mm/dd/yyyy.\n" +

                    //print portfolio on 06/23/208, FB didn't change
                    "JD-> Shares: 1579.70 costBasis: $60870.00 Total Value: $64483.30\n" +
                    "FB-> Shares: 2.75 costBasis: $490.00 Total Value: $555.45\n" +
                    "\n" +
                    "Total Sum: $65038.74 Current commission Fee: $10.00\n" +
                    "Input the portfolio name to print.\n" +
                    "Input a date to view the contents of the portfolio on that date. " +
                    "This must be in the format mm/dd/yyyy.\n" +

                    //print portfolio on 07/23/208, FB didn't change
                    "JD-> Shares: 2233.46 costBasis: $85820.00 Total Value: $79645.22\n" +
                    "FB-> Shares: 2.75 costBasis: $490.00 Total Value: $580.69\n" +
                    "\n" +
                    "Total Sum: $80225.91 Current commission Fee: $10.00\n");
  }


  @Test
  public void testDateFormat() {
    StringReader in = new StringReader("new p1 addcommission p1 5 add JD p1 02/29/2018 02/30/2018" +
            "02/31/2018 02/32/2018 quit");

    PortfolioController test = new PortfolioControllerImpl(in, System.out);
    test.runPortfolio(p1);

    assertEquals(refineOutput(outContent.toString()),
            "Input the portfolio name to create.\n" +
            "New portfolio created.\n" +
            "Input the portfolio name to add the commission fee to. " +
                    "This will apply for all future transactions in this " +
                    "portfolio until specified otherwise.\n" +
            "Input the dollar amount of the commission fee per transaction.\n" +
            "Commission fee of 5.0 added.\n" +
            "Input the stock symbol.\n" +
            "Input the portfolio name to add the stock(s) to.\n" +
            "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
            "Invalid date, not an actual date.\n" +


            "Invalid date format. This must be in the format mm/dd/yyyy.\n" +
            "Invalid date format. This must be in the format mm/dd/yyyy.\n");
  }

  @Test
  public void testZeroAndNagetiveDaysBetweenInvest() {
    //test -1 and 0 days between invest
    StringReader in3 = new StringReader("new p1 add JD p1 03/05/2018 1000 API" +
            " addschedule p1 04/02/2018 5000 y 11/20/2018 -1 0 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            "Input the stock symbol.\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For " +
                    "multipletransactions, this is the total amount per day.\n" +
                    "Input the cost per share.(Enter API to use real time data)\n" +
                    "Stock added successfully on date: 03/05/2018\n" +
                    "Input the portfolio name to add the stock(s) to.\n" +
                    "Input the date of this transaction. This must be in the format mm/dd/yyyy.\n" +
                    "Input the total dollar amount of this transaction. For multipletransactions" +
                    ", this is the total amount per day.\n" +
                    "Do you want all stocks in the group to have equal weightings? " +
                    "Type y for yes or n for no.\n" +
                    "The weights are: {JD=1.0}\n" +
                    "Input the end date. This must be in the format mm/dd/yyyy. " +
                    "Input \"now\" to use current date. Type \"none\" " +
                    "to have no end date. Future dates can be input. \n" +
                    "Enter the number of days between transactions. " +
                    "ie 7 days means the transaction will take place weekly. " +
                    "If the date falls on a holiday or weekend, " +
                    "then the next business day will be used.\n" +

                    //output message
                    "Must input a number greater than 0.\n" +
                    "Must input a number greater than 0.\n");
  }

  @Test
  public void testSavePortfolio() {
    StringReader in3 = new StringReader("new p1 addcommission p1 1 add GOOG p1 " +
            "11/05/2018 1000 API" +
            " add AMD p1 11/07/2018 500 API saveportfolio p1 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            "Commission fee of 1.0 added.\n" +
            "Stock added successfully on date: 11/05/2018\n" +
                    "Stock added successfully on date: 11/07/2018\n" +
                    "Portfolio p1 saved");
  }

  @Test
  public void testLoadPortfolio() {
    StringReader in3 = new StringReader("loadportfolio p1 print p1 11/11/2018 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            "GOOG-> Shares: 0.96 costBasis: $999.00 Total Value: $1024.06\n" +
                    "AMD-> Shares: 22.89 costBasis: $499.00 Total Value: $480.46\n" +
                    "\n" +
                    "Total Sum: $1504.51 Current commission Fee: $1.00\n");
  }

  @Test
  public void testLoadPortfolioInvalidName() {
    StringReader in3 = new StringReader("loadportfolio alsdjkllkw quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            "Failed to load portfolio, please re-enter portfolio name.");
  }

  @Test
  public void testSavePortfolioWithSameName() {
    StringReader in3 = new StringReader("new p2 saveportfolio p2 add p2 GOOG 11/11/2018 1000" +
            "API saveportfolio p2 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            "Portfolio p2 saved.");
  }

  @Test
  public void testSaveStrategyEqualWeight() {
    StringReader in3 = new StringReader("loadportfolio p1 savestrategy s1 p1 20000 11/08/2018 y 2" +
            " 11/20/2018q uit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            "The weights are: {GOOG = 0.5, AMD = 0.5} Frequency: 2 day " +
            "Strategy saved.");
  }

  @Test
  public void testSaveStrategyFixedWeights() {
    StringReader in3 = new StringReader("loadportfolio p1 savestrategy s2 p1 20000 11/08/2018 n " +
            "0.3 0.7 2 11/20/2018 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            "The weights are: {GOOG = 0.3, AMD = 0.7} Frequency: 2 day " +
                    "Strategy saved.");
  }

  @Test
  public void testSaveStrategyInvalidWeights() {
    StringReader in3 = new StringReader("loadportfolio p1 savestrategy s2 p1 20000 11/08/2018 " +
            "n 0.2 0.7 2 11/20/2018 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            "The weights need to be added up to 1 " +
                    "Strategy not saved.");
  }


  @Test
  public void testLoadStrategyEqualWeights() {
    StringReader in3 = new StringReader("loadportfolio p1 loadstrategy s1 p1 print " +
            "p1 11/19/2018 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            "Portfoio p1 load successfully" +
            "The weights are: {GOOG = 0.5, AMD = 0.5} Frequency: 10 day " +
                    "Load strategy successfully.\n" +
                    "GOOG-> Shares: 4.8 costBasis: $5998.00 Total Value: $6124.06\n" +
                    "AMD-> Shares: 228.9 costBasis: $5498.00 Total Value: $4804.69\n" +
                    "\n" +
                    "Total Sum: $10928.75 Current commission Fee: $1.00\n"
    );
  }

  @Test
  public void testLoadStrategyFixedWeights() {
    StringReader in3 = new StringReader("loadportfolio p1 loadstrategy s2 p1 " +
            "print p1 11/19/2018 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            "Portfoio p1 load successfully" +
                    "The weights are: {GOOG = 0.3, AMD = 0.7} Frequency: 10 day " +
                    "Load strategy successfully.\n" +
                    "GOOG-> Shares: 4.8 costBasis: $3998.00 Total Value: $4324.06\n" +
                    "AMD-> Shares: 228.9 costBasis: $7498.00 Total Value: $6804.69\n" +
                    "\n" +
                    "Total Sum: $11128.75 Current commission Fee: $1.00\n");
  }

  @Test
  public void testLoadStrategyWithWrongPortfolio() {
    StringReader in3 = new StringReader("new p2 loadportfolio p1 loadstrategy s1 p2 print " +
            "p1 11/19/2018 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            "Portfoio p1 load successfully" +
                    "Strategy doesn't fit for this portfolio.\n"
    );
  }

  @Test
  public void testLoadStrategyWithInvalidName() {
    StringReader in3 = new StringReader("loadportfolio p1 loadstrategy asdfwe p1 print " +
            "p1 11/19/2018 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            "Portfoio p1 load successfully" +
                    "Strategy not found, please re-enter strategy name.\n"
    );
  }

  @Test
  public void testLoadStrategyWithMultiDays() {
    StringReader in3 = new StringReader("new p1 addcommission p1 10 add JD p1 03/05/2018 1000 API" +
            " add FB p1 03/05/2018 500 API save p1 save schedule p1 s1 04/02/2018 5000 n " +
            "0.3 0.7 11/20/2018 " + "loadstrategy s1 p1" +
            "7 print p1 05/23/2018 print p1 06/23/2018 print p1 07/23/2018 quit");
    PortfolioController test3 = new PortfolioControllerImpl(in3, System.out);
    test3.runPortfolio(p1);
    assertEquals(refineOutputNewPortfolio(refineOutput(outContent.toString())),
            //print portfolio on 05/23/2018
            "JD-> Shares: 336.37 costBasis: $12910.00 Total Value: $12351.64\n" +
                    "FB-> Shares: 167.01 costBasis: $28410.00 Total Value: $31213.94\n" +
                    "\n" +
                    "Total Sum: $43565.58 Current commission Fee: $10.00\n" +
                    "Input the portfolio name to print.\n" +
                    "Input a date to view the contents of the portfolio on that date. " +
                    "This must be in the format mm/dd/yyyy.\n" +

                    //print portfolio on 06/23/2018
                    "JD-> Shares: 487.85 costBasis: $18870.00 Total Value: $19913.87\n" +
                    "FB-> Shares: 239.67 costBasis: $42370.00 Total Value: $48351.96\n" +
                    "\n" +
                    "Total Sum: $68265.83 Current commission Fee: $10.00\n" +
                    "Input the portfolio name to print.\n" +
                    "Input a date to view the contents of the portfolio on that date. " +
                    "This must be in the format mm/dd/yyyy.\n" +

                    //print portfolio on 07/23/2018
                    "JD-> Shares: 683.06 costBasis: $26320.00 Total Value: $24357.83\n" +
                    "FB-> Shares: 325.57 costBasis: $59820.00 Total Value: $68665.33\n" +
                    "\n" +
                    "Total Sum: $93023.16 Current commission Fee: $10.00\n");
  }
}