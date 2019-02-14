package portfolio.controller.commands;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import portfolio.model.PortfolioCollection;

/**
 * This class represent add command that will command a portfolio to add a stock. This class also
 * will access an API if the user chooses to pull market data.
 */
public class AddStock extends AbstractAdd {

  private String stockSymbol;
  private Double costPerShare;
  private Scanner scan;

  /**
   * Constructor that creates an addstock command for the controller.
   *
   * @param in  scanner
   * @param out appendable
   */
  public AddStock(Scanner in, Appendable out) {
    super(in, out);
    scan = in;
    Appendable output = out;


  }


  @Override
  public boolean goTo(PortfolioCollection input) {
    String inputTemp = null;
    appendToOutput("Input the stock symbol.");
    while (stockSymbol == null) {
      inputTemp = scan.next();
      if (inputTemp.equalsIgnoreCase("quit")) {
        return false;
      }
      stockSymbol = inputTemp;
    }

    if (!getInfo(input)) {
      return false;
    }

    appendToOutput("Input the cost per share.(Enter API to use real time data)");
    while (costPerShare == null) {
      try {
        inputTemp = scan.next();
        if (inputTemp.equalsIgnoreCase("quit")) {
          return false;
        }
        if (inputTemp.equals("API")) {
          costPerShare = 0.0;
        } else {
          costPerShare = Double.parseDouble(inputTemp);
        }
      } catch (IllegalArgumentException ex) {
        appendToOutput("Cost per share must be a number.");
      }
    }
    LocalDate purchaseDate = inputDate;
    try {
      purchaseDate = input.addStock(portfolio, this.stockSymbol, this.dollarAmount, this.inputDate,
              this.costPerShare);
    } catch (IllegalArgumentException np) {
      appendToOutput(np.getMessage() + " Stock not added.");
      return true;
    }
    appendToOutput("Stock added successfully on date: " +
            purchaseDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    return true;
  }
}