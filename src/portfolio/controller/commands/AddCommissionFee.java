package portfolio.controller.commands;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import portfolio.controller.PortfolioCommand;
import portfolio.model.PortfolioCollection;

/**
 * This class represent command AddCommissionFee, which add a commission fee that will
 * be deducted per transaction.
 */
public class AddCommissionFee implements PortfolioCommand {

  private Scanner scan;
  private Appendable output;
  private String portfolio;


  /**
   * This is the constructor.
   * @param in scanner
   * @param out appendable
   */
  public AddCommissionFee(Scanner in, Appendable out) {
    scan = in;
    output = out;
  }

  /**
   * This method takes in a model and send the command to the model.
   * @param input Input a PortfolioCollection model that the controller will send the command to.
   * @return boolean
   */
  public boolean goTo(PortfolioCollection input) {
    String inputTemp;
    Double commission = null;

    appendToOutput("Input the portfolio name to add the commission fee to. This will " +
            "apply for all future transactions in this portfolio until specified otherwise.");
    while (portfolio == null) {
      inputTemp = scan.next();
      if (inputTemp.equalsIgnoreCase("quit")) {
        return false;
      }
      portfolio = inputTemp;
      try {
        input.getStocks(portfolio, LocalDate.now());
      } catch (IllegalArgumentException ex) {
        appendToOutput("Portfolio does not exist. Please input portfolio again.");
        portfolio = null;
      }
    }



    appendToOutput("Input the dollar amount of the commission fee per transaction.");
    while (commission == null) {
      try {
        inputTemp = scan.next();
        if (inputTemp.equalsIgnoreCase("quit")) {
          return false;
        }
        commission = Double.parseDouble(inputTemp);
      } catch (IllegalArgumentException ex) {
        appendToOutput("Input must be number.");
      }
    }

    try {
      input.addCommissionFee(portfolio, commission);
      appendToOutput("Commission fee of " + commission.toString()  + " added.");
    } catch (IllegalArgumentException ex) {
      appendToOutput(ex.getMessage());
    }
    return true;
  }

  protected void appendToOutput(String outputString) {
    try {
      output.append(outputString + "\n");
    } catch (IOException io) {
      throw new RuntimeException();
    }
  }
}