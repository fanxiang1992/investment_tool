package portfolio.controller.commands;

import java.io.IOException;
import java.util.Scanner;

import portfolio.controller.PortfolioCommand;
import portfolio.model.PortfolioCollection;

/**
 * This class is a command that will create a new portfolio.
 */
public class NewPortfolio implements PortfolioCommand {

  String portfolio;
  Scanner scan;
  Appendable output;
  String inputTemp;

  /**
   * This is the constructor.
   *
   * @param input scanner
   * @param out   appendable
   */
  public NewPortfolio(Scanner input, Appendable out) {
    scan = input;
    output = out;
  }


  @Override
  public boolean goTo(PortfolioCollection input) {

    appendToOutput("Input the portfolio name to create.");
    while (portfolio == null) {
      inputTemp = scan.next();
      if (inputTemp.equalsIgnoreCase("quit")) {
        return false;
      }
      portfolio = inputTemp;
      try {
        input.createNewPortfolio(portfolio);
        appendToOutput("New portfolio created.");
      } catch (IllegalArgumentException ex) {
        portfolio = null;
        appendToOutput(ex.getMessage());
      }
    }

    return true;
  }

  private void appendToOutput(String outputString) {
    try {
      output.append(outputString + "\n");
    } catch (IOException io) {
      throw new RuntimeException();
    }
  }

}