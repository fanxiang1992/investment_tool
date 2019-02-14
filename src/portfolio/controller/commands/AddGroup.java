package portfolio.controller.commands;

import portfolio.model.PortfolioCollection;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * This class represent AddGroup command, which Make a purchase of all stocks in a portfolio based
 * on a total amount and weights.
 */
public class AddGroup extends AbstractAdd {

  protected String inputWeight;
  protected Double weight;
  protected Map<String, Double> weightings = new HashMap<>();
  protected Boolean equalWeights;
  protected Double totalWeightCheck = 0.0;


  /**
   * This is the constructor.
   * @param in scanner
   * @param out appendable
   */
  public AddGroup(Scanner in, Appendable out) {
    super(in, out);
  }

  /**
   * This function takes in a model and send command to it.
   * @param input Input a PortfolioCollection model that the controller will send the command to.
   * @return boolean
   */
  public boolean goTo(PortfolioCollection input) {


    if (!getInfo(input)) {
      return false;
    }
    if (input.getStocks(portfolio, inputDate).size() == 0) {
      appendToOutput("No stock in portfolio, add some stocks first.");
      return true;
    }

    if (!getWeights(input)) {
      return true;
    }

    for (String s : weightings.keySet()) {
      try {
        transactionList.add(new TransactionDollarAmount(portfolio, s,
                dollarAmount * weightings.get(s), this.inputDate, 0.0));
      } catch (IllegalArgumentException np) {
        appendToOutput(np.getMessage() + " Stock not added.");
      }
    }
    return addTransactions(input);
  }


  protected boolean getWeights(PortfolioCollection input) {
    String inputTemp = null;
    appendToOutput("Do you want all stocks in the group to have equal weightings? " +
            "Type y for yes or n for no.");
    while (equalWeights == null) {
      inputTemp = scan.next();
      if (inputTemp.equalsIgnoreCase("quit")) {
        return false;
      }
      if (inputTemp.equalsIgnoreCase("y")) {
        equalWeights = true;
      } else if (inputTemp.equalsIgnoreCase("n")) {
        equalWeights = false;
      } else {
        appendToOutput("Input must be either y or n.");
      }
    }


    for (String s : input.getStocks(portfolio, inputDate)) {
      weight = null;

      if (!equalWeights) {
        while (weight == null) {
          appendToOutput("Input the weight for stock: " + s + ". This must be between 0 and 1, " +
                  "and all weights need to add to 1.");
          inputWeight = scan.next();
          if (inputTemp.equalsIgnoreCase("quit")) {
            return false;
          }
          try {
            weight = Double.parseDouble(inputWeight);
          } catch (IllegalArgumentException ex) {
            appendToOutput("The weight must be a number.");
          }
          if (weight > 1 || weight < 0) {
            weight = null;
            appendToOutput("The weight must be a number between 0 and 1.");
          }
        }
      }
      weightings.put(s, weight);
    }

    if (equalWeights ) {
      weight = 1.0 / weightings.size();
      for (String s : weightings.keySet()) {
        weightings.put(s, weight);
      }
    }

    for (String s : weightings.keySet()) {
      totalWeightCheck += weightings.get(s);
    }

    if (Math.abs(totalWeightCheck - 1) < 0.001) {
      appendToOutput("The weights are: " + weightings.toString());
    } else {
      appendToOutput("The weightings must add up to 1. Group stocks not added.");
      return false;
    }

    return true;
  }


}