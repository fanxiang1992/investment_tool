package portfolio.controller.commands;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

import portfolio.controller.PortfolioCommand;
import portfolio.model.PortfolioCollection;

/**
 * This class abstracts all common methods used in add.
 */
public abstract class AbstractAdd implements PortfolioCommand {

  protected Scanner scan;
  protected Appendable output;
  protected ArrayList<Transaction> transactionList;


  protected String inputTemp;
  protected String portfolio;
  protected LocalDate inputDate;
  protected Double dollarAmount;

  /**
   * This is the constructor.
   * @param in scanner
   * @param out appendable
   */
  public AbstractAdd(Scanner in, Appendable out) {
    scan = in;
    output = out;
    transactionList = new ArrayList<>();
  }


  protected void appendToOutput(String outputString) {
    try {
      output.append(outputString + "\n");
    } catch (IOException io) {
      throw new RuntimeException();
    }
  }

  protected boolean getInfo(PortfolioCollection input) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    appendToOutput("Input the portfolio name to add the stock(s) to.");
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

    appendToOutput("Input the date of this transaction. This must be in the format " +
            "mm/dd/yyyy.");
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

    appendToOutput("Input the total dollar amount of this transaction. For multiple" +
            "transactions, this is the total amount per day.");
    while (dollarAmount == null) {
      try {
        inputTemp = scan.next();
        if (inputTemp.equalsIgnoreCase("quit")) {
          return false;
        }
        dollarAmount = Double.parseDouble(inputTemp);
      } catch (IllegalArgumentException ex) {
        appendToOutput("Input must be number.");
      }
    }

    return true;
  }

  protected boolean addTransactions(PortfolioCollection portfolioInput) {
    for (Transaction i : transactionList) {
      try {
        i.addTransaction(portfolioInput);
      } catch (IllegalArgumentException ex) {
        appendToOutput("Stock not added successfully.");
        return true;
      }
    }
    appendToOutput("Transactions added successfully.");
    return true;
  }


}