package portfolio.controller.commands;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import portfolio.controller.PortfolioCommand;
import portfolio.model.PortfolioCollection;

/**
 * This class represents a print command that will output all of the stocks in a given portfolio.
 */
public class GetPortfolio implements PortfolioCommand {

  private LocalDate inputDate;
  String portfolio;
  private Scanner scan;
  private Appendable output;


  /**
   * constructor.
   *
   * @param in  scanner
   * @param out appendable
   */
  public GetPortfolio(Scanner in, Appendable out) {
    scan = in;
    output = out;
  }


  @Override
  public boolean goTo(PortfolioCollection input) {
    String inputTemp;
    List<String> stocks;
    double totalShares;
    double totalPurchasePrice;
    double totalValue;
    double commission = 0.0;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    appendToOutput("Input the portfolio name to print.");
    while (portfolio == null) {
      inputTemp = scan.next();
      if (inputTemp.equalsIgnoreCase("quit")) {
        return false;
      }
      portfolio = inputTemp;
    }

    appendToOutput("Input a date to view the contents of the portfolio on that date. " +
            "This must be in the format mm/dd/yyyy.");
    while (inputDate == null) {
      try {
        inputTemp = scan.next();
        if (inputTemp.equalsIgnoreCase("quit")) {
          return false;
        }
        inputDate = LocalDate.parse(inputTemp, formatter);
        new DateValidator().checkValidDate(inputTemp);
      } catch (DateTimeParseException ex) {
        appendToOutput("Invalid date format. This must be in the format mm/dd/yyyy.");
      } catch (IllegalArgumentException e) {
        appendToOutput("Invalid date, not an actual date.");
        inputDate = null;
      }
      if (inputDate != null) {
        if (inputDate.isAfter(LocalDate.now())) {
          appendToOutput("Can't input future date.");
          inputDate = null;
        }
      }
    }

    try {
      stocks = input.getStocks(portfolio, inputDate);
      double totalSum = 0.0;

      for (String s : stocks) {
        totalPurchasePrice = input.getPurchasePrice(portfolio, s, inputDate);
        totalShares = input.getTotalShares(portfolio, s, inputDate);
        totalValue = input.getTotalValue(portfolio, s, inputDate);
        commission = input.getCommission(portfolio);
        totalSum = totalSum + totalValue;
        appendToOutput(s + "-> Shares: " + String.format("%.2f", totalShares) + " costBasis: $"
                + String.format("%.2f", totalPurchasePrice) + " Total Value: $"
                + String.format("%.2f", totalValue));
      }
      appendToOutput("\nTotal Sum: $" + String.format("%.2f", totalSum) + " Current commission " +
              "Fee: $" + String.format("%.2f", commission));
    } catch (IllegalArgumentException ex) {
      appendToOutput("Not a valid portfolio.");
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