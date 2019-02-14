package portfolio.model;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import portfolio.service.StockPriceQuery;

/**
 * This class represent a single stock portfolio.
 */
class StockPortfolioImpl {


  private HashMap<String, ArrayList<StockTransaction>> transactions;
  private Double commission = 0.0;

  /**
   * This is the constructor of class.
   */
  protected StockPortfolioImpl() {
    transactions = new HashMap<>();
  }

  protected void addCommission(Double commissionInput) {
    if (commissionInput < 0 || commissionInput == null) {
      throw new IllegalArgumentException("Commission needs to be positive.");
    }

    commission = commissionInput;
  }

  /**
   * This method takes four inputs and will add a stock to the portfolio.
   *
   * @param stockSymbol name
   * @param dollarInput cost basis
   * @param inputDate   date
   * @param costInput   cost per share
   */
  protected void addStock(String stockSymbol, Double dollarInput, LocalDate inputDate,
                          Double costInput) {

    if (dollarInput < 0.0 || dollarInput - commission < 0.0) {
      throw new IllegalArgumentException("Need to input a positive dollar amount or the amount" +
              " is less than commission fee.");
    }
    if (costInput < 0.0) {
      throw new IllegalArgumentException("Need to input a positive price per share.");
    }

    if (!transactions.containsKey(stockSymbol)) {
      transactions.put(stockSymbol, new ArrayList<>());
    }

    ArrayList<StockTransaction> tempList = transactions.get(stockSymbol);
    tempList.add(new StockTransaction(stockSymbol, dollarInput - commission, inputDate, costInput));

  }


  protected List<String> getStocks(LocalDate inputDate) {
    List<String> output = new ArrayList<String>();
    for (String stock : transactions.keySet()) {
      if (transactions.get(stock).stream().filter(c ->
              c.getDateOfTransaction().isBefore(inputDate.plusDays(1))).count() > 0) {
        output.add(stock);
      }
    }
    return output;
  }


  /**
   * This method takes in a date and a query, will return a string as output.
   *
   * @param dateInput date
   * @return string formatted portfolio
   */
  protected double getTotalShare(LocalDate dateInput, String stock) {


    return transactions.get(stock).stream().filter(c ->
            c.getDateOfTransaction().isBefore(dateInput.plusDays(1))).map(c -> c.getShares())
            .reduce((a, b) -> a + b).orElse(0.0);


  }


  protected double getPurchasePrice(LocalDate dateInput, String stock) {


    return transactions.get(stock).stream().filter(c ->
            c.getDateOfTransaction().isBefore(dateInput.plusDays(1))).map(c ->
            c.getPurchasePricePerShare() * c.getShares()).reduce((a, b) -> a + b).orElse(0.0);

  }

  protected double getTotalValue(LocalDate dateInput, String stock) {

    int maxTry = 5;
    Double closePrice = 0.0;
    Double totalValue = 0.0;
    Double totalShares = this.getTotalShare(dateInput, stock);
    if (totalShares > 0) {
      while (maxTry > 0) {
        try {
          closePrice = StockPriceQuery.getPrice(stock, dateInput);
          break;
        } catch (IllegalArgumentException e) {
          dateInput = dateInput.minusDays(1);
          maxTry--;
        }
      }
      totalValue = this.getTotalShare(dateInput, stock) * closePrice;
    }
    return totalValue;
  }

  protected double getCommission() {
    return this.commission;
  }

  protected void savePortfolio(String portfolioName) throws FileNotFoundException {
    File file = new File("res/portfolio/" + portfolioName + ".csv");
    file.getParentFile().mkdirs();
    PrintWriter pw = new PrintWriter(file);
    for (Map.Entry<String, ArrayList<StockTransaction>> entry : transactions.entrySet()) {
      String stock = entry.getKey();
      ArrayList<StockTransaction> transactionlist = entry.getValue();
      for (StockTransaction s : transactionlist) {
        StringBuilder sb = new StringBuilder();
        sb.append(stock);
        sb.append(",");
        Double costBasis = s.getShares() * s.getPurchasePricePerShare();
        sb.append(costBasis.toString());
        sb.append(",");
        String formattedDate = s.getDateOfTransaction().format(DateTimeFormatter
                .ofPattern("MM/dd/yyyy"));
        sb.append(formattedDate);
        sb.append(",");
        Double pricePerShare = s.getPurchasePricePerShare();
        sb.append(pricePerShare.toString());
        sb.append("\n");
        pw.write(sb.toString());
      }
    }
    pw.write("commission," + commission.toString() + "\n");

    pw.close();
  }

  protected void plotPerformance(String portfolioName) {
    if (getStocks(LocalDate.now()).size() == 0) {
      throw new IllegalArgumentException("No stock in portfolio.");
    }

    ArrayList<StockTransaction> totalTransactions = new ArrayList<StockTransaction>();
    for (Map.Entry<String, ArrayList<StockTransaction>> entry : transactions.entrySet()) {
      ArrayList<StockTransaction> transactionlist = entry.getValue();
      totalTransactions.addAll(transactionlist);
    }
    Collections.sort(totalTransactions);
    LocalDate earliestDate = totalTransactions.get(0).getDateOfTransaction();

    LocalDate date = earliestDate;
    List<String> dateList = new ArrayList<String>();
    List<Double> valueList = new ArrayList<Double>();
    while (date.isBefore(LocalDate.now())) {
      dateList.add(date.format(DateTimeFormatter.ofPattern("MM/dd/yy")));
      valueList.add(getTotalSum(date));
      date = date.plusDays(1);
    }
    try {
      drawPerformance(dateList, valueList, portfolioName);
    } catch (IOException ex) {
      throw new IllegalArgumentException("Plot performance error. ");
    }

  }

  private Double getTotalSum(LocalDate date) {
    List<String> stocks = getStocks(date);
    Double totalSum = 0.0;
    for (String s : stocks) {
      double totalValue = getTotalValue(date, s);
      totalSum = totalSum + totalValue;
    }
    return totalSum;
  }

  private void drawPerformance(List<String> dateList, List<Double> valueList,
                               String portfolioName) throws IOException {
    JFreeChart lineChartObject = ChartFactory.createLineChart(
            "Portfolio Performance",
            "Dates", "Total Value",
            createDataset(dateList, valueList),
            PlotOrientation.VERTICAL,
            true, true, false);

    CategoryPlot plot = (CategoryPlot) lineChartObject.getPlot();
    CategoryAxis domainAxis = plot.getDomainAxis();
    domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
    );

    File lineChart = new File( "res/" + portfolioName + ".jpeg" );
    ChartUtilities.saveChartAsJPEG(lineChart ,lineChartObject, 800, 600);
  }

  private DefaultCategoryDataset createDataset(List<String> dateList, List<Double> valueList) {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    for (int i = 0; i < dateList.size(); i++) {
      dataset.addValue(valueList.get(i), "Total Value", dateList.get(i));
    }

    for (Map.Entry<String, ArrayList<StockTransaction>> entry : transactions.entrySet()) {
      String stock = entry.getKey();
      ArrayList<StockTransaction> transactionlist = entry.getValue();
      Collections.sort(transactionlist);
      LocalDate earliestDate = transactionlist.get(0).getDateOfTransaction();
      LocalDate date = earliestDate;
      while (date.isBefore(LocalDate.now())) {
        String dateLabel = date.format(DateTimeFormatter.ofPattern("MM/dd/yy"));
        Double valueLabel = getTotalValue(date, stock);
        dataset.addValue(valueLabel, stock, dateLabel);
        date = date.plusDays(1);
      }
    }
    return dataset;
  }

}
