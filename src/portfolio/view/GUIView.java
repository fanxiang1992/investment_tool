package portfolio.view;

import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;


/**
 * This class represents a GUI user interface.
 */
public interface GUIView {

  /**
   * This method takes in an ActionListerner, and will set the listener.
   * @param listener ActonListerner
   */
  void setListener(ActionListener listener);

  /**
   * This method takes in a string and will set the output string.
   * @param s input string
   */
  void echoCommandOutput(String s);

  /**
   * This method takes in a string and will set the output model string.
   * @param s input string
   */
  void displayModelOutput(String s);

  /**
   * This method does not take any input, will set the display message.
   */
  void display();

  /**
   * This method does not take any input, it will get the select command from UI.
   */
  void getCommand();

  /**
   * This method does not take any input, it will return the entered portfolio from UI.
   * @return portfolio
   */
  String getPortfolioEntry();

  /**
   * This method does not take any input, it will return the entered start date from UI.
   * @return start date
   */
  String getStartDate();

  /**
   * This method does not take any input, it will return the entered end date from UI.
   * @return end date
   */
  String getEndDate();

  /**
   * This method does not take any input, it will return the entered stock symbol from UI.
   * @return stock symbol
   */
  String getStockSymbol();

  /**
   * This method does not take any input, it will return the entered commission fee from UI.
   * @return commission fee
   */
  String getCommission();

  /**
   * This method does not take any input, it will return the entered total value from UI.
   * @return total value
   */
  String getTotalValue();

  /**
   * This method does not take any input, it will return the entered price per share from UI.
   * @return price per share
   */
  String getPricePerShare();

  /**
   * This method does not take any input, it will return the entered periodicity from UI.
   * @return periodicity
   */
  String getPeriodicity();

  /**
   * This method does not take any input, it will return true when user choose to manually enter
   * weights, false when use equal weights.
   * @return boolean
   */
  boolean getWeightSetting();

  /**
   * This method does not take any input, it will return the strategy name entered from UI.
   * @return strategy name
   */
  String getStrategyName();

  /**
   * This method does not take any input, it will set the weight list entered from UI.
   * @param stocks stock symbol
   */
  void viewWeightInputs(List<String> stocks);

  /**
   * this method does not take any input, it will reset the weight inputs.
   */
  void resetWeightInputs();

  /**
   * This method does not take any input, it will populate weights.
   */
  void populateWeights();

  /**
   * This method does not take any input, it will set the display weights.
   * @param toDisplay boolean
   */
  void setDisplayWeights(boolean toDisplay);

  /**
   * This method does not take any input and it'll returns a map of input weights.
   * @return weights map
   */
  Map<String, String> getWeightsInput();

}