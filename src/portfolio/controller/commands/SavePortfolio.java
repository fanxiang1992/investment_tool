package portfolio.controller.commands;

import java.io.IOException;
import java.util.Scanner;

import portfolio.controller.PortfolioCommand;
import portfolio.model.PortfolioCollection;

/**
 * Created by xiangfan on 12/1/18.
 */
public class SavePortfolio implements PortfolioCommand {

  protected String portfolio;
  protected Scanner scan;
  protected Appendable output;
  protected String inputTemp;

  public SavePortfolio(Scanner in, Appendable out) {
    this.scan = in;
    this.output = out;
  }


  @Override
  public boolean goTo(PortfolioCollection input) {

    appendToOutput("Input the portfolio name to save.");

    while (portfolio == null) {
      inputTemp = scan.next();
      if (inputTemp.equalsIgnoreCase("quit")) {
        return false;
      }
      portfolio = inputTemp;
      try {
        input.savePortfolio(portfolio);
        appendToOutput("Portfolio saved.");
      } catch (IllegalArgumentException ex) {
        appendToOutput(ex.getMessage());
        portfolio = null;
      }
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
