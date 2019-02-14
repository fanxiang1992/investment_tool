package portfolio.controller.commands;

import portfolio.model.PortfolioCollection;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * This class represent AddSchedule command, which implement a dollar cost averaging strategy
 * based on total amount and weights. This is similar to the addgoup bus allows the user to
 * schedule multiple transactions.
 */
public class AddSchedule extends AddGroup {

  protected Scanner scan;
  protected Long daysToAdd;
  protected LocalDate endDate;

  /**
   * This is the constructor.
   * @param in scanner
   * @param out appendable
   */
  public AddSchedule(Scanner in, Appendable out) {
    super(in, out);
    scan = in;
    output = out;

  }

  @Override
  public boolean goTo(PortfolioCollection input) {

    String inputTemp = null;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    if (!getInfo(input)) {
      return false;
    }
    if (input.getStocks(portfolio, inputDate).size() == 0) {
      appendToOutput("No stock in portfolio, add some stocks first.");
      return true;
    }
    if (!getWeights(input)) {
      return false;
    }

    appendToOutput("Input the end date. This must be in the format mm/dd/yyyy." +
            " Input \"now\" to use current date. Type \"none\" to have no end date." +
            " Future dates can be input. ");
    while (endDate == null) {
      try {
        inputTemp = scan.next();
        if (inputTemp.equalsIgnoreCase("quit")) {
          return false;
        }

        if (inputTemp.equalsIgnoreCase("now")) {
          endDate = (LocalDate.now());
          continue;
        }

        if (inputTemp.equalsIgnoreCase("none")) {
          endDate = (inputDate.plusDays(999999));
          continue;
        }

        endDate = LocalDate.parse(inputTemp, formatter);
        new DateValidator().checkValidDate(inputTemp);

        if (endDate.isBefore(inputDate)) {
          throw new RuntimeException("End date must be before start date.");
        }


      } catch (DateTimeParseException ex) {
        appendToOutput("Invalid date. This must be in the format mm/dd/yyyy.");
      } catch (IllegalArgumentException e) {
        appendToOutput("Not a valid date.");
        endDate = null;
      } catch (RuntimeException ix) {
        appendToOutput("End date needs to be before start date. " +
                "Please enter a date that is after the start date.");
        endDate = null;
      }
    }


    appendToOutput("Enter the number of days between transactions. " +
            "ie 7 days means the transaction will take place weekly. " +
            "If the date falls on a holiday or weekend, then the next business day will be used.");
    while (daysToAdd == null) {
      try {
        inputTemp = scan.next();
        if (inputTemp.equalsIgnoreCase("quit")) {
          return false;
        }
        daysToAdd = Long.parseLong(inputTemp);
        if (daysToAdd < 1) {
          throw new RuntimeException("Must input a number greater than 0.");
        }
      } catch (IllegalArgumentException ex) {
        appendToOutput("Input must be integer.");
      } catch (RuntimeException e) {
        appendToOutput("Must input a number greater than 0.");
        daysToAdd = null;
      }
    }

    //Add to the transactions until the current date.
    for (LocalDate i = inputDate; i.isBefore(LocalDate.now()) && i.isBefore(endDate);
         i = i.plusDays(daysToAdd)) {
      for (String s : weightings.keySet()) {
        try {
          transactionList.add(new TransactionDollarAmount(portfolio, s,
                  dollarAmount * weightings.get(s), i, 0.0));
        } catch (IllegalArgumentException np) {
          appendToOutput(np.getMessage() + " Stock not added.");
        }
      }
    }

    return addTransactions(input);


  }


}
