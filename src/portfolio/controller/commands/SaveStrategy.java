package portfolio.controller.commands;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import portfolio.model.PortfolioCollection;

/**
 * Created by xiangfan on 12/2/18.
 */

/**
 * This class represent a save strategy command.
 */
public class SaveStrategy extends AddSchedule {
  private String strategyName;


  /**
   * This is the constructor.
   * @param in scanner
   * @param out appendable
   */
  public SaveStrategy(Scanner in, Appendable out) {
    super(in, out);
    this.scan = in;
    this.output = out;
  }

  @Override
  public boolean goTo(PortfolioCollection input) {

    appendToOutput("Input the name of strategy");

    while (strategyName == null) {
      inputTemp = scan.next();
      if (inputTemp.equalsIgnoreCase("quit")) {
        return false;
      }
      strategyName = inputTemp;
    }


    String inputTemp;
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


    try {
      input.saveStrategy(strategyName, portfolio, equalWeights, weightings, inputDate, endDate,
              daysToAdd, dollarAmount);
      appendToOutput("Strategy saved.");
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
