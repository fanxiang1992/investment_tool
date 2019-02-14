package portfolio.controller;


import portfolio.model.PortfolioCollection;


/**
 * This class is a controller that will send commands to run a stock portfolio.
 *
 * @param <StockTransaction> A custom made class representing stock transactions.
 */
public interface PortfolioController<StockTransaction> {


  /**
   * This method runs the controller.
   *
   * @param portfolio The portfolio collection controlled by the class.
   */
  void runPortfolio(PortfolioCollection portfolio);

  /**
   * Creates a new instance of a portfolio collection.
   *
   * @return Returns a new portfolio collection.
   */
  PortfolioCollection createPortfolioCollection();
}
