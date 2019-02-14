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
 * Created by xiangfan on 12/5/18.
 */
public class LoadStrategy implements PortfolioCommand {

  private String strategyName;
  private String portfolio;

  private Scanner scan;
  private Appendable output;

  public LoadStrategy(Scanner in, Appendable out) {
    this.scan = in;
    this.output = out;
  }

  @Override
  public boolean goTo(PortfolioCollection input) {
    String inputTemp;
    appendToOutput("Input the name of strategy");

    while (strategyName == null) {
      inputTemp = scan.next();
      if (inputTemp.equalsIgnoreCase("quit")) {
        return false;
      }
      strategyName = inputTemp;
    }

    appendToOutput("Input the portfolio name to load.");

    while (portfolio == null) {
      inputTemp = scan.next();
      if (inputTemp.equalsIgnoreCase("quit")) {
        return false;
      }
      portfolio = inputTemp;
    }

    try {
      loadStrategy(strategyName, portfolio, input);
      appendToOutput("Load strategy successfully");
    } catch (IllegalArgumentException ex) {
      appendToOutput(ex.getMessage());
    } catch (FileNotFoundException ex) {
      appendToOutput("Strategy not found, please re-enter strategy name.");
    } catch (RuntimeException ex) {
      appendToOutput(ex.getMessage());
    }
    return true;
  }


  private void loadStrategy(String strategyName, String portfolio, PortfolioCollection model)
          throws FileNotFoundException {
    String line = "";
    String csvFile = "res/strategy/" + strategyName + ".csv";
    BufferedReader br = null;
    StringReader addScheduleInput = null;
    PortfolioController addScheduleCommand = null;

    try {
      br = new BufferedReader(new FileReader(csvFile));
      while ((line = br.readLine()) != null) {
        String[] items = line.split(",");
        StringBuilder sb = new StringBuilder();
        if (!portfolio.equals(items[0])) {
          throw new RuntimeException("Strategy doesn't fit for this portfolio.");
        }
        sb.append("addschedule " + portfolio + " " + items[2] + " " + items[5]);
        if (items[1].equals("equal")) {
          sb.append(" y " + items[3] + " " + items[4] + "\n");
          addScheduleInput = new StringReader(sb.toString());
          addScheduleCommand = new PortfolioControllerImpl(addScheduleInput, System.out);
          addScheduleCommand.runPortfolio(model);
        } else if (items[1].equals("unequal")) {
          String weightString = "";
          for (int i = 6; i < items.length; i++) {
            if (i % 2 == 1) {
              weightString = weightString + items[i] + " ";
            }
          }
          sb.append(" n " + weightString + items[3] + " " + items[4] + "\n");
          addScheduleInput = new StringReader(sb.toString());
          addScheduleCommand = new PortfolioControllerImpl(addScheduleInput, System.out);
          addScheduleCommand.runPortfolio(model);
        } else {
          throw new RuntimeException("Load strategy error.");
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Load strategy error.");
    } catch (RuntimeException e) {
      throw new RuntimeException(e.getMessage());
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          throw new RuntimeException("Load strategy error.");
        }
      }
    }
  }

  protected void appendToOutput(String outputString) {
    try {
      output.append(outputString + "\n");
    } catch (IOException io) {
      throw new RuntimeException();
    }
  }
}
