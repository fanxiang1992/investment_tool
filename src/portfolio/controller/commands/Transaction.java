package portfolio.controller.commands;

import portfolio.model.PortfolioCollection;

/**
 * This class abstracts all common methods in transactionDollarAmount class.
 */
public interface Transaction {


  /**
   * This is empty because we will be able to add more commands in the future.
   * @param portfolioInput portfolio
   */
  void addTransaction(PortfolioCollection portfolioInput);

}