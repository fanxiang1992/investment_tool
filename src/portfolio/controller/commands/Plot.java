package portfolio.controller.commands;

import java.io.IOException;
import java.util.Scanner;

import portfolio.controller.PortfolioCommand;
import portfolio.model.PortfolioCollection;

/**
 * Created by xiangfan on 12/5/18.
 */
public class Plot implements PortfolioCommand {

  private String portfolio;

  private Scanner scan;
  private Appendable output;


  public Plot(Scanner in, Appendable out) {
    this.scan = in;
    this.output = out;
  }

  @Override
  public boolean goTo(PortfolioCollection input) {
    String inputTemp;
    appendToOutput("Input the portfolio name to plot.");

    while (portfolio == null) {
      inputTemp = scan.next();
      if (inputTemp.equalsIgnoreCase("quit")) {
        return false;
      }
      portfolio = inputTemp;
    }

    try {
      input.plotPerformance(portfolio);
      appendToOutput("Successfully plot the performance to " + portfolio + ".jpeg");
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
