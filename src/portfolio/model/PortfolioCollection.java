package portfolio.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * This class represents a collection of stock portfolios.
 */

public interface PortfolioCollection {

  /**
   * Create a new portfolio.
   *
   * @param portfolio The name of the portfolio to create.
   */
  void createNewPortfolio(String portfolio);

  /**
   * This method will add a new transaction to the stock portfolio specified. The portfolio will
   * add the transaction based on the dollar amount and the closing pricing of the specified date.
   * It will allow fractional shares of a stock to be purchased.
   *
   * @param inputPortfolio The name of the portfolio to add to.
   * @param stockSymbol    Stock symbol to input.
   * @param dollarAmount   The dollar amount to input, as a double.
   * @param inputDate      The date of the transaction.
   * @param costPerShare   The cost per share as a double.
   */
  LocalDate addStock(String inputPortfolio, String stockSymbol, Double dollarAmount,
                     LocalDate inputDate,
                     Double costPerShare);

  /**
   * This method takes in a portfolio name and an input date,
   * return a list of stocks in the portfolio.
   * @param portfolio portfolio name
   * @param inputDate input date
   * @return list of stocks
   */
  List<String> getStocks(String portfolio, LocalDate inputDate);

  /**
   * This method takes in a portfolio name, stock symbol and an input date, return the total
   * cost basis.
   * @param portfolio portfolio name
   * @param stock stock symbol
   * @param inputDate input date
   * @return cost basis
   */
  double getPurchasePrice(String portfolio, String stock, LocalDate inputDate);

  /**
   * This method takes in a portfolio name, stock symbol and an input date, return the total
   * shares of the stock.
   * @param portfolio portfolio name
   * @param stock stock symbol
   * @param inputDate input date
   * @return total shares
   */
  double getTotalShares(String portfolio, String stock, LocalDate inputDate);

  /**
   * This method takes in a portfolio name, stock symbol and an input date, return the total value
   * of the portfolio.
   * @param portfolio name of portfolio
   * @param stock stock symbol
   * @param inputDate input date
   * @return total value
   */
  double getTotalValue(String portfolio, String stock, LocalDate inputDate);

  /**
   * This method takes in a double and add it to the portfolio as commission fee.
   * @param commission double
   */
  void addCommissionFee(String portfolioInput, Double commission);

  /**
   * This method takes in a portfolio name and return the commission fee used on it.
   * @param portfolioInput portfolio name
   * @return commission fee
   */
  double getCommission(String portfolioInput);

  /**
   * This method takes in a portfolio name and will save it to a csv file.
   * @param portfolioName portfolio name
   */
  void savePortfolio(String portfolioName);

  /**
   * This method takes in a strategy name, portfolio name, equalweights boolean, weightings,
   * start date, end date, days to add, and dollar amount. It'll save the strategy to a csv file.
   * @param strategyName strategyName
   * @param portfolioName portfolioName
   * @param equalWeights equalWeights
   * @param weightings weightings
   * @param startDate startDate
   * @param endDate endDate
   * @param daysToAdd daysToAdd
   * @param dollarAmount dollarAmount
   */
  void saveStrategy(String strategyName, String portfolioName, Boolean equalWeights,
                    Map<String, Double> weightings, LocalDate startDate, LocalDate endDate,
                    Long daysToAdd, Double dollarAmount);

  /**
   * This method takes a portfolio name and will generate the perfomance plot as an image.
   * @param portfolioName portfolio name
   */
  void plotPerformance(String portfolioName);
}