package portfolio.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


/**
 * This class allows the user to get stock data from an API to get the market value of a stock.
 */
public class StockPriceQuery {
  private static HashMap<String, HashMap<String, Double>> priceMap =
          new HashMap<String, HashMap<String, Double>>();

  /**
   * This method doesn't take any input, it'll load the api price from local map.
   */
  public static void loadPriceMap() {
    String line = "";
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader("res/priceMap.csv"));

      while ((line = br.readLine()) != null) {
        String[] priceList = line.split(",");

        if (priceMap.containsKey(priceList[0])) {
          priceMap.get(priceList[0]).put(priceList[1], Double.valueOf(priceList[2]));
        } else {
          priceMap.put(priceList[0], new HashMap<String, Double>());
        }
      }
    } catch (FileNotFoundException ex) {
      System.out.println("can't find the file: priceMap.csv");
    } catch (IOException e) {
      System.out.println("read file error.");
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          System.out.println("close file error.");
        }
      }
    }
  }

  private static void savePriceMap() throws FileNotFoundException {
    File file = new File("res/priceMap.csv");
    file.getParentFile().mkdirs();
    PrintWriter pw = new PrintWriter(file);
    for (Map.Entry<String, HashMap<String, Double>> entry : priceMap.entrySet()) {
      String stock = entry.getKey();
      HashMap<String, Double> dateMap = entry.getValue();

      for (Map.Entry<String, Double> dateMapEntry : dateMap.entrySet()) {
        String date = dateMapEntry.getKey();
        Double closePrice = dateMapEntry.getValue();
        StringBuilder sb = new StringBuilder();
        sb.append(stock);
        sb.append(",");
        sb.append(date);
        sb.append(",");
        sb.append(closePrice.toString());
        sb.append("\n");
        pw.write(sb.toString());
      }
    }
    pw.close();
  }

  /**
   * This method takes in a hashMap and set the the priceMap.
   *
   * @param map1 input hashmap
   */
  public static void setMap(HashMap<String, HashMap<String, Double>> map1) {
    priceMap = map1;
  }

  private static void storePrice(String symbol, String output) {
    if (output.contains("Error")) {
      throw new RuntimeException("No stock found for " + symbol + ".");
    }

    HashMap<String, Double> dateMap = new HashMap<String, Double>();
    String[] lines = output.split("\\r?\\n");

    if (lines.length <= 1) {
      return;
    }

    for (int i = 1; i < lines.length; i++) {
      String[] row = lines[i].split(",");
      dateMap.put(row[0], new Double(row[4]));
    }

    priceMap.put(symbol, dateMap);

    try {
      savePriceMap();
    } catch (FileNotFoundException ex) {
      System.out.println(ex.getMessage());
    }
  }

  /**
   * This method will take in a name and data to get the real time stock price.
   *
   * @param stockSymbol name
   * @param date        date
   * @return double
   */
  public static Double getPrice(String stockSymbol, LocalDate date) {
    if (priceMap.containsKey(stockSymbol)) {
      HashMap<String, Double> dateMap = priceMap.get(stockSymbol);

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      String formatDate = date.format(formatter);

      if (dateMap.containsKey(formatDate)) {
        return dateMap.get(formatDate);
      } else {
        throw new IllegalArgumentException("invalid trade date.");
      }

    } else {
      throw new IllegalArgumentException("invalid stock symbol.");
    }
  }

  /**
   * This method will take in a String name, and will return the price.
   *
   * @param stockSymbol name
   */
  public static void getPriceFromApi(String stockSymbol) {
    URL url = null;
    String apiKey = "LBJE8SN9WDCMG26Q";

    // use existing price stored in map, no need to call API again
    if (priceMap.containsKey(stockSymbol)) {
      return;
    }

    try {
      /*
      create the URL. This is the query to the web service. The query string
      includes the type of query (DAILY stock prices), stock symbol to be
      looked up, the API key and the format of the returned
      data (comma-separated values:csv). This service also supports JSON
      which you are welcome to use.
       */
      url = new URL("https://www.alphavantage"
              + ".co/query?function=TIME_SERIES_DAILY"
              + "&outputsize=full"
              + "&symbol"
              + "=" + stockSymbol + "&apikey=" + apiKey + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("the alphavantage API has either changed or "
              + "no longer works");
    }

    InputStream in = null;
    StringBuilder output = new StringBuilder();

    try {
      /*
      Execute this query. This returns an InputStream object.
      In the csv format, it returns several lines, each line being separated
      by commas. Each line contains the date, price at opening time, highest
      price for that date, lowest price for that date, price at closing time
      and the volume of trade (no. of shares bought/sold) on that date.

      This is printed below.
       */
      in = url.openStream();
      int b;

      while ((b = in.read()) != -1) {
        output.append((char) b);
      }

      storePrice(stockSymbol, output.toString());
    } catch (IOException e) {
      throw new IllegalArgumentException("No price data found for " + stockSymbol);
    }
  }
}
