package portfolio.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.function.Function;

import portfolio.controller.commands.AddCommissionFee;
import portfolio.controller.commands.AddGroup;
import portfolio.controller.commands.AddSchedule;
import portfolio.controller.commands.AddStock;
import portfolio.controller.commands.GetPortfolio;
import portfolio.controller.commands.LoadPortfolio;
import portfolio.controller.commands.LoadStrategy;
import portfolio.controller.commands.NewPortfolio;
import portfolio.controller.commands.Plot;
import portfolio.controller.commands.SavePortfolio;
import portfolio.controller.commands.SaveStrategy;
import portfolio.model.PortfolioCollection;
import portfolio.model.PortfolioCollectionImpl;

/**
 * This class represent a controller for a portfolio.
 */
public class PortfolioControllerImpl implements PortfolioController {


  private final Appendable out;
  Map<String, Function<Scanner, PortfolioCommand>> allCommands = new HashMap<>();
  Stack<PortfolioCommand> commandLog = new Stack<>();
  Scanner scan;

  /**
   * This is the constructor. It will take in a readable which will be used to take user input.
   * The appendable is for the output that will be passed back into the view.
   *
   * @param in  readable
   * @param out appendable
   */
  public PortfolioControllerImpl(Readable in, Appendable out) {
    Readable input;
    //Check inputs
    if (in == null) {
      throw new IllegalArgumentException("Invalid input");
    }
    if (out == null) {
      throw new IllegalArgumentException("Invalid output");
    }

    input = in;
    this.out = out;
    scan = new Scanner(input);
  }



  @Override
  public PortfolioCollection createPortfolioCollection() {
    return new PortfolioCollectionImpl();
  }

  @Override
  public void runPortfolio(PortfolioCollection portfolio) {

    //Add a new portfolio
    allCommands.put("new", s -> new NewPortfolio(scan, out));

    //Add price manually
    allCommands.put("add", s -> new AddStock(scan, out));

    //print out all stocks of a portfolio
    allCommands.put("print", s -> new GetPortfolio(scan, out));

    //Add a group of stocks
    allCommands.put("addgroup", s -> new AddGroup(scan, out));

    //Add a group of stocks
    allCommands.put("addschedule", s -> new AddSchedule(scan, out));

    //Add a commission
    allCommands.put("addcommission", s -> new AddCommissionFee(scan, out));

    //load a portfolio
    allCommands.put("loadportfolio", s -> new LoadPortfolio(scan, out));

    //save a portfolio
    allCommands.put("saveportfolio", s -> new SavePortfolio(scan, out));

    //save a strategy
    allCommands.put("savestrategy", s -> new SaveStrategy(scan, out));

    //load a strategy
    allCommands.put("loadstrategy", s -> new LoadStrategy(scan, out));

    //plot a portfolio perfomance
    allCommands.put("plot", s -> new Plot(scan, out));

    try {
      out.append("Enter a command. The options are:\nnew - create a new portfolio\nadd - " +
              "add a new stock with real time price from API or manually input price.\nprint " +
              "- output the contents of the portfolio\n" +
              "addgroup - Make a purchase of all stocks in a portfolio based on a total amount" +
              " and weights.\n" + "addschedule - Implement a dollar cost averaging strategy " +
              "based on total amount and weights. This is similar to the addgoup bus allows " +
              "the user to schedule multiple transactions. User will specify a start and end " +
              "date, the frequency of transactions, and the amount per transaction.\n" +
              "addcommission - add a commission fee that will be deducted per transaction. This" +
              " can be set for each portfolio.\n" +
              "saveportfolio - save a portfolio to a csv file.\n" +
              "loadportfolio - load a portfolio from a csv.\n" +
              "savestrategy - save a strategy to a csv file.\n" +
              "loadstrategy - load a strategy from a csv.\n" +
              "plot - plot a portfolio performance to an image file.\n" +
              "quit - Input anytime to exit the program\n");
    } catch (IOException io) {
      throw new IllegalStateException("Invalid input.");
    }
    while (scan.hasNext()) {

      PortfolioCommand command;

      String in = scan.next();
      if (in.equalsIgnoreCase("quit")) {
        return;
      }
      Function<Scanner, PortfolioCommand> cmd = allCommands.getOrDefault(in, null);
      if (cmd == null) {
        try {
          out.append(in + " is not a valid command.\n");
        } catch (IOException io) {
          throw new IllegalStateException("Invalid input.");
        }
      } else {
        command = cmd.apply(scan);
        commandLog.add(command);
        if (!command.goTo(portfolio)) {
          return;
        }

        try {
          out.append("\n#############################################################\n" +
                  "Enter a command. The options are:\nnew - create a new portfolio\nadd - " +
                  "add a new stock with real time price from API or manually input price.\nprint " +
                  "- output the contents of the portfolio\n" +
                  "addgroup - Make a purchase of all stocks in a portfolio based on a " +
                  "total amount" +
                  " and weights.\n" + "addschedule - Implement a dollar cost averaging strategy " +
                  "based on total amount and weights. This is similar to the addgoup bus allows " +
                  "the user to schedule multiple transactions. User will specify a start and end " +
                  "date, the frequency of transactions, and the amount per transaction.\n" +
                  "addcommission - add a commission fee that will be deducted per transaction." +
                  " This can be set for each portfolio.\n" +
                  "saveportfolio - save a portfolio to a csv file.\n" +
                  "loadportfolio - load a portfolio from a csv.\n" +
                  "savestrategy - save a strategy to a csv file.\n" +
                  "loadstrategy - load a strategy from a csv.\n" +
                  "plot - plot a portfolio performance to an image file.\n" +
                  "quit - Input anytime to exit the program\n");
        } catch (IOException io) {
          throw new IllegalStateException("Invalid input.");
        }
      }
    }

  }


}