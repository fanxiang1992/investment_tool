package portfolio.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import portfolio.service.StockPriceQuery;

/**
 * This class represents a collection of stock portfolios.
 */

public class PortfolioCollectionImpl implements PortfolioCollection {

  private HashMap<String, StockPortfolioImpl> portfolio;


  /**
   * This is the constructor.
   */
  public PortfolioCollectionImpl() {
    portfolio = new HashMap<>();
  }

  @Override
  public void addCommissionFee(String portfolioInput, Double commissionInput) {

    portfolio.get(portfolioInput).addCommission(commissionInput);
  }

  @Override
  public void createNewPortfolio(String inputPortfolio) {
    if (portfolio.containsKey(inputPortfolio)) {
      throw new IllegalArgumentException("Portfolio already exists, please enter another name.");
    }
    portfolio.put(inputPortfolio, new StockPortfolioImpl());
  }


  @Override
  public LocalDate addStock(String inputPortfolio, String stockSymbol,
                            Double dollarAmount, LocalDate inputDate,
                            Double costPerShare) throws IllegalArgumentException {

    try {
      StockPriceQuery.getPriceFromApi(stockSymbol);
      // getPrice will check whether the inputDate is a valid market date
      // if is invalid, will try next date until it finds a valid market date (not in future)
      int maxTry = 5;
      Double apiPrice = null;
      while (maxTry > 0) {
        try {
          apiPrice = StockPriceQuery.getPrice(stockSymbol, inputDate);
          break;
        } catch (IllegalArgumentException ex) {
          if (ex.getMessage().equals("invalid stock symbol.")) {
            throw new IllegalArgumentException(ex.getMessage());
          }
          if (ex.getMessage().equals("invalid trade date.")) {
            inputDate = inputDate.plusDays(1);
            if (inputDate.isAfter(LocalDate.now())) {
              throw new IllegalArgumentException("can't purchase on future date.");
            }
            maxTry--;
          }
        }
      }
      if (apiPrice == null) {
        throw new IllegalArgumentException("try another date");
      }
      if (costPerShare == 0.0) {
        costPerShare = apiPrice;
      }
      portfolio.get(inputPortfolio).addStock(stockSymbol, dollarAmount, inputDate,
              costPerShare);
    } catch (NullPointerException ex) {
      throw new IllegalArgumentException("This portfolio does not exist");
    } catch (RuntimeException ex) {
      throw new IllegalArgumentException(ex.getMessage());
    }
    return inputDate;
  }


  //ToDo change to return only the double
  @Override
  public List<String> getStocks(String inputPortfolio, LocalDate inputDate)
          throws IllegalArgumentException {
    if (!portfolio.containsKey(inputPortfolio)) {
      throw new IllegalArgumentException("This portfolio does not exist");
    }
    try {
      return portfolio.get(inputPortfolio).getStocks(inputDate);
    } catch (NullPointerException np) {
      throw new IllegalArgumentException("This portfolio does not have any stock");
    }
  }

  @Override
  public double getTotalShares(String inputPortfolio, String stock, LocalDate inputDate)
          throws IllegalArgumentException {
    try {
      return portfolio.get(inputPortfolio).getTotalShare(inputDate, stock);
    } catch (NullPointerException np) {
      throw new IllegalArgumentException("This portfolio does not exist");
    }
  }

  @Override
  public double getPurchasePrice(String inputPortfolio, String stock, LocalDate inputDate)
          throws IllegalArgumentException {
    try {
      return portfolio.get(inputPortfolio).getPurchasePrice(inputDate, stock);
    } catch (NullPointerException np) {
      throw new IllegalArgumentException("This portfolio does not exist");
    }
  }

  @Override
  public double getTotalValue(String inputPortfolio, String stock, LocalDate inputDate)
          throws IllegalArgumentException {
    try {
      return portfolio.get(inputPortfolio).getTotalValue(inputDate, stock);
    } catch (NullPointerException np) {
      throw new IllegalArgumentException("This portfolio does not exist");
    }
  }

  @Override
  public double getCommission(String inputPortfolio) {
    return portfolio.get(inputPortfolio).getCommission();
  }

  @Override
  public void savePortfolio(String portfolioName) {

    if (!portfolio.containsKey(portfolioName)) {
      throw new IllegalArgumentException("Portfolio does not exist, please try another name.");
    }
    try {
      portfolio.get(portfolioName).savePortfolio(portfolioName);
    } catch (FileNotFoundException ex) {
      System.out.println(ex.getMessage());
    }
  }

  @Override
  public void saveStrategy(String strategyName, String portfolioName, Boolean equalWeights,
                           Map<String, Double> weightings, LocalDate startDate, LocalDate endDate,
                           Long daysToAdd, Double dollarAmount) {
    if (!portfolio.containsKey(portfolioName)) {
      throw new IllegalArgumentException("Portfolio doesn't exist, " +
              "please re-enter portfolio name.");
    }
    try {
      File file = new File("res/strategy/" + strategyName + ".csv");
      file.getParentFile().mkdirs();
      PrintWriter pw = new PrintWriter(file);
      StringBuilder sb = new StringBuilder();
      sb.append(portfolioName);
      sb.append(",");

      if (equalWeights) {
        sb.append("equal");
      } else {
        sb.append("unequal");
      }
      sb.append(",");
      sb.append(startDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
      sb.append(",");
      sb.append(endDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
      sb.append(",");
      sb.append(daysToAdd.toString());
      sb.append(",");
      sb.append(dollarAmount.toString());
      if (!equalWeights) {
        sb.append(",");
        int i = 0;
        for (String stock : weightings.keySet()) {
          sb.append(stock);
          sb.append(",");
          sb.append(weightings.get(stock).toString());
          if (i != (weightings.size() - 1)) {
            sb.append(",");
          }
          i++;
        }
      }
      sb.append("\n");
      pw.write(sb.toString());
      pw.close();
    } catch (FileNotFoundException ex) {
      System.out.println(ex.getMessage());
    }
  }

  @Override
  public void plotPerformance(String portfolioName) {
    if (!portfolio.containsKey(portfolioName)) {
      throw new IllegalArgumentException("Portfolio doesn't exist, please re-enter portfolio name");
    }

    try {
      portfolio.get(portfolioName).plotPerformance(portfolioName);
    } catch (IllegalArgumentException ex) {
      throw new IllegalArgumentException(ex.getMessage());
    }
  }
}