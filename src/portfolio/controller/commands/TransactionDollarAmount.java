package portfolio.controller.commands;

import java.time.LocalDate;

import portfolio.model.PortfolioCollection;

/**
 * This class represent a transaction list.
 */
public class TransactionDollarAmount implements Transaction {

  private String stockSymbol;
  private LocalDate date;
  private double totalAmount;
  private String portfolio;
  private double price;

  /**
   * This is the constructor.
   * @param portfolioInput portfolio name
   * @param inputSymbol stock symbol
   * @param amount dollar amount
   * @param inputDate start date
   * @param inputPrice price per share
   */
  public TransactionDollarAmount(String portfolioInput, String inputSymbol, double amount,
                                    LocalDate inputDate, double inputPrice) {

    portfolio = portfolioInput;
    stockSymbol = inputSymbol;
    totalAmount = amount;
    date = inputDate;
    price = inputPrice;
  }

  @Override
  public void addTransaction(PortfolioCollection portfolioInput) throws IllegalArgumentException {
    portfolioInput.addStock(portfolio, stockSymbol, totalAmount, date, price);
  }

}
