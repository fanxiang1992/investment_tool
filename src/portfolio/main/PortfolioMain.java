package portfolio.main;

import java.io.InputStreamReader;


import portfolio.controller.PortfolioController;
import portfolio.controller.PortfolioControllerImpl;
import portfolio.model.PortfolioCollection;
import portfolio.service.StockPriceQuery;

/**
 * This is our main class for a text based interface.
 */
public class PortfolioMain {

  /**
   * This is the main function to run the whole project.
   *
   * @param args arguments it takes
   */
  public static void main(String[] args) {
    StockPriceQuery.loadPriceMap();

    PortfolioController main = new PortfolioControllerImpl(new InputStreamReader(System.in),
            System.out);
    PortfolioCollection portfolio = main.createPortfolioCollection();
    main.runPortfolio(portfolio);
  }
}
