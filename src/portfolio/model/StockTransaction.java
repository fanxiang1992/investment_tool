package portfolio.model;

import java.time.LocalDate;

/**
 * This class represent a stock transaction.
 */
public class StockTransaction implements Comparable<StockTransaction> {

  private String symbol;
  private double numberOfShares;
  private LocalDate dateOfTransaction;
  private double purchasePricePerShare;


  // This is the constructor for stockTransaction.

  protected StockTransaction(String symbolInput, double dollarAmount,
                             LocalDate dateOfTransactionInput, double costPerShare) {
    String symbol = symbolInput;
    if (costPerShare == 0) {
      throw new IllegalArgumentException("can't devide by zero");
    }
    numberOfShares = dollarAmount / costPerShare;
    dateOfTransaction = dateOfTransactionInput;
    purchasePricePerShare = costPerShare;
  }

  protected String getSymbol() {
    return symbol;
  }

  //This method return the shares.
  protected double getShares() {
    return numberOfShares;
  }

  // This method return the cost basis.
  protected double getPurchasePricePerShare() {
    return purchasePricePerShare;
  }

  //this method return the date.

  protected LocalDate getDateOfTransaction() {
    return dateOfTransaction;
  }


  @Override
  public int compareTo(StockTransaction other) {
    if (this.dateOfTransaction.isBefore(other.dateOfTransaction)) {
      return -1;
    }
    if (this.dateOfTransaction.isAfter(other.dateOfTransaction)) {
      return 1;
    }
    return 0;
  }
}
