package portfolio.main;


import portfolio.controller.PortfolioControllerGUI;
import portfolio.controller.PortfolioControllerGUIImpl;
import portfolio.model.PortfolioCollection;
import portfolio.model.PortfolioCollectionImpl;
import portfolio.service.StockPriceQuery;
import portfolio.view.GUIView;
import portfolio.view.ViewPanel;


/**
 * This is the class to run the program with a GUI interface.
 */
public class PortfolioMainGUI {


  /**
   * This is the main function to run the whole project with the GUI interface.
   *
   * @param args arguments it takes
   */
  public static void main(String[] args) {
    StockPriceQuery.loadPriceMap();

    GUIView frame = new ViewPanel("Stock Portfolio");

    PortfolioCollection model = new PortfolioCollectionImpl();
    PortfolioControllerGUI controller = new PortfolioControllerGUIImpl(model, frame);

  }


}
