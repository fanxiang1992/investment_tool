package portfolio.controller.commands;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;

import portfolio.controller.PortfolioCommand;
import portfolio.controller.PortfolioController;
import portfolio.controller.PortfolioControllerImpl;
import portfolio.model.PortfolioCollection;

/**
 * Created by xiangfan on 12/1/18.
 */
public class LoadPortfolio implements PortfolioCommand {

  protected String portfolio;
  protected Scanner scan;
  protected Appendable output;

  protected String inputTemp;
  protected String csvFile;

  public LoadPortfolio(Scanner in, Appendable out) {
    this.scan = in;
    this.output = out;
  }

  @Override
  public boolean goTo(PortfolioCollection input) {
    String line = "";

    appendToOutput("Input the portfolio name to load.");

    while (portfolio == null) {
      inputTemp = scan.next();
      if (inputTemp.equalsIgnoreCase("quit")) {
        return false;
      }
      portfolio = inputTemp;
      csvFile = "res/portfolio/" + portfolio + ".csv";
      BufferedReader br = null;

      try {
        br = new BufferedReader(new FileReader(csvFile));

        // Step 1: create the portfolio
        StringReader newPortfolioInput = new StringReader("new " + portfolio);
        PortfolioController newPortfolioCommand = new PortfolioControllerImpl(newPortfolioInput,
                this.output);
        newPortfolioCommand.runPortfolio(input);

        // Step 2: read csv & add stock transactions
        // Step 3: add commission fee
        while ((line = br.readLine()) != null) {
          // use comma as separator
          String[] transactions = line.split(",");

          if (transactions.length > 0 && !transactions[0].equals("commission")) {
            StringReader addInput = new StringReader("add " + transactions[0] + " " + portfolio +
                    " " + transactions[2] + " " + transactions[1] + " " + transactions[3] + "\n");
            PortfolioController addCommand = new PortfolioControllerImpl(addInput, this.output);
            addCommand.runPortfolio(input);
          } else {
            StringReader commissionInput = new StringReader("addcommission " + portfolio + " " +
                    transactions[1] + "\n");
            PortfolioController commissionCommand = new PortfolioControllerImpl(commissionInput,
                    this.output);
            commissionCommand.runPortfolio(input);
          }
        }
      } catch (FileNotFoundException e) {
        portfolio = null;
        appendToOutput("Failed to load portfolio, please re-enter portfolio name.");
      } catch (IOException e) {
        portfolio = null;
        appendToOutput("Failed to load portfolio, please re-enter portfolio name.");
      } finally {
        if (br != null) {
          try {
            br.close();
            appendToOutput("\n<=============================================>\n" +
                    "Successfully load portfolio " + portfolio + "!");
          } catch (IOException e) {
            portfolio = null;
            appendToOutput("Failed to load portfolio, please re-enter portfolio name.");
          }
        }
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
