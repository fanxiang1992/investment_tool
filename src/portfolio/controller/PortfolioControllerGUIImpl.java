package portfolio.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


import portfolio.controller.commands.Transaction;
import portfolio.controller.commands.TransactionDollarAmount;
import portfolio.model.PortfolioCollection;
import portfolio.view.GUIView;

/**
 * This class represent a GUI controller implementation.
 */
public class PortfolioControllerGUIImpl implements PortfolioControllerGUI, ActionListener {

  private PortfolioCollection model;
  private GUIView view;

  private String portfolio;
  private String strategyName;
  private LocalDate startDate;
  private LocalDate endDate;

  private List<String> stocks;

  private double dollarAmount;
  private double commission = 0.0;

  private String stockSymbol;
  private double costPerShare;

  private Map<String, Double> weightings = new HashMap<>();
  private Boolean equalWeights;
  private ArrayList<Transaction> transactionList = new ArrayList<>();
  private Long daysToAdd;

  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");


  /**
   * This is the constructor.
   *
   * @param modelInput model
   * @param viewInput  view
   */
  public PortfolioControllerGUIImpl(PortfolioCollection modelInput, GUIView viewInput) {
    model = modelInput;
    view = viewInput;
    view.setListener(this);
    view.display();
  }


  /**
   * This method takes in an ActionEvent and will take action according to the input event.
   *
   * @param e action event
   */
  public void actionPerformed(ActionEvent e) {

    switch (e.getActionCommand()) {
      case "Print":
        view.echoCommandOutput("Print portfolio: Get the contents of the portfolio as of " +
                "a certain date.");
        view.getCommand();
        break;
      case "AddPortfolio":
        view.echoCommandOutput("Create a new portfolio. Specify the name of the new portfolio.");
        view.getCommand();
        break;
      case "AddStock":
        view.echoCommandOutput("Add a single stock to the portfolio specified.");
        view.getCommand();

        break;
      case "AddGroup":
        view.echoCommandOutput("Add an amount to all stocks in a portfolio by weight. " +
                "You will be asked to use fixed weights or input your own weights.");
        view.getCommand();

        break;
      case "AddSchedule":
        view.echoCommandOutput("Add a dollar cost averaging strategy. Adds a total " +
                "amount to all stocks in a portfolio by weight. User can specify" +
                " when the transactions start and end, and the periodicity of the stocks.");
        view.getCommand();

        break;
      case "AddCommission":
        view.echoCommandOutput("Add commission to a portfolio.");
        view.getCommand();

        break;
      case "SavePortfolio":
        view.echoCommandOutput("Save a portfolio by portfolio name.");
        view.getCommand();

        break;
      case "LoadPortfolio":
        view.echoCommandOutput("Load a portfolio by portfolio name.");
        view.getCommand();

        break;
      case "SaveStrategy":
        view.echoCommandOutput("Save an investment strategy by name.");
        view.getCommand();

        break;
      case "LoadStrategy":
        view.echoCommandOutput("Load an investment strategy by name. ");
        view.getCommand();

        break;
      case "Plot":
        view.echoCommandOutput("Plot the performance of a portfolio.");
        view.getCommand();

        break;

      case "ExecutePrint":
        printCommand();
        break;

      case "ExecuteAdd":
        addPortfolioCommand();
        break;

      case "ExecuteAddStock":
        addStockCommand();
        break;

      case "ExecuteAddGroup":

        getPortfolio();
        stockSymbol = view.getStockSymbol();
        getStartDate();
        getDollarAmount();
        costPerShare = 0.0;

        if (!getGroupWeights()) {
          break;
        }
        addGroupCommand();
        addTransactions();
        break;

      case "ExecuteAddSchedule":

        getPortfolio();
        stockSymbol = view.getStockSymbol();
        getStartDate();
        getDollarAmount();
        costPerShare = 0.0;
        getEndDate();
        getPeriodicity();

        if (!getGroupWeights()) {
          break;
        }
        addScheduleCommand();
        addTransactions();
        break;

      case "ExecuteSavePortfolio":
        savePortfolioCommand();
        break;

      case "ExecuteLoadPortfolio":
        loadPortfolioCommand();
        break;

      case "ExecuteSaveStrategy":
        strategyName = view.getStrategyName();
        getPortfolio();
        stockSymbol = view.getStockSymbol();
        getStartDate();
        getEndDate();
        getDollarAmount();
        getPeriodicity();
        costPerShare = 0.0;

        if (!getGroupWeights()) {
          break;
        }
        saveStrategyCommand();

        break;

      case "ExecuteLoadStrategy":
        loadStrategyCommand();
        break;

      case "ExecutePlot":
        plotCommand();
        break;
      case "SetWeightOption":
        setWeightCommand();
        break;
      case "ExecuteCommission":
        getPortfolio();
        if (!getCommission()) {
          break;
        }
        addCommissionCommand();
        break;

      case "CheckPortfolioAndDate":
        if (getPortfolio() && getStartDate()) {
          view.setDisplayWeights(true);
          view.getCommand();
        }
        break;
      case "Quit":
        System.exit(0);
        break;
      default:
        break;
    }
  }


  private void loadStrategy(String strategyName, String portfolio) throws FileNotFoundException {
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

  private boolean getGroupWeights() {
    Map<String, String> inputWeight;
    Double weight;
    Double totalWeightCheck = 0.0;

    stocks = model.getStocks(portfolio, startDate);
    totalWeightCheck = 0.0;

    for (String s : stocks) {

      weight = null;
      equalWeights = !view.getWeightSetting();

      if (!equalWeights) {
        view.populateWeights();
        inputWeight = view.getWeightsInput();
        try {
          for (String t : inputWeight.keySet()) {
            weight = Double.parseDouble(inputWeight.get(t));
            weightings.put(t, weight);
          }
        } catch (IllegalArgumentException ex) {
          view.displayModelOutput("The weight must be a number.");
          return false;
        }
        if (weight > 1 || weight < 0) {
          weight = null;
          view.displayModelOutput("The weight must be a number between 0 and 1.");
          return false;
        }
      }
      weightings.put(s, weight);

    }

    if (equalWeights) {
      weight = 1.0 / weightings.size();
      for (String s : weightings.keySet()) {
        weightings.put(s, weight);
      }
    }

    for (String s : weightings.keySet()) {
      totalWeightCheck += weightings.get(s);
    }

    if (Math.abs(totalWeightCheck - 1) < 0.001) {
      view.displayModelOutput("The weights are: " + weightings.toString());
    } else {
      view.displayModelOutput("The weightings must add up to 1. Group stocks not added.");
      return false;
    }
    return true;
  }


  private void addTransactions() {


    for (Transaction i : transactionList) {
      try {
        i.addTransaction(model);
      } catch (IllegalArgumentException ex) {
        view.displayModelOutput("Stock not added successfully.");
      }
    }
    view.displayModelOutput("Transactions added successfully.");

  }


  private boolean getPortfolio() {
    portfolio = view.getPortfolioEntry();
    try {
      model.getStocks(portfolio, LocalDate.now());
    } catch (IllegalArgumentException ex) {
      view.displayModelOutput(ex.getMessage());
      portfolio = null;
      return false;
    }
    return true;
  }


  private boolean getStartDate() {
    try {
      checkValidDate(view.getStartDate());
      startDate = LocalDate.parse(view.getStartDate(), formatter);
    } catch (DateTimeParseException dateEx) {
      view.displayModelOutput("Invalid date format. This must be in the format mm/dd/yyyy.");
      return false;
    } catch (IllegalArgumentException dateExInvalid) {
      view.displayModelOutput("Invalid date, not an actual date.");
      return false;
    }

    if (startDate.isAfter(LocalDate.now())) {
      view.displayModelOutput("Can't input future date.");
      return false;
    }
    return true;
  }

  private void getDollarAmount() {
    try {
      dollarAmount = Double.parseDouble(view.getTotalValue());
    } catch (IllegalArgumentException ex) {
      view.displayModelOutput("Input must be number.");
    }
  }

  private boolean getCommission() {
    try {
      commission = Double.parseDouble(view.getCommission());
    } catch (IllegalArgumentException ex) {
      view.displayModelOutput("Commission fee must be number");
      return false;
    }
    return true;
  }

  private void getCostPerShare() {
    if (view.getPricePerShare().equals("API")) {
      costPerShare = 0.0;
    } else {
      try {
        costPerShare = Double.parseDouble(view.getPricePerShare());
      } catch (IllegalArgumentException ex) {
        view.displayModelOutput("Cost per share must be a number.");
      }
    }
  }

  private void getEndDate() {
    try {
      endDate = LocalDate.parse(view.getEndDate(), formatter);
      checkValidDate(view.getEndDate());

      if (endDate.isBefore(startDate)) {
        throw new RuntimeException("End date must be before start date.");
      }

    } catch (DateTimeParseException ex) {
      view.displayModelOutput("Invalid date. This must be in the format mm/dd/yyyy.");
    } catch (IllegalArgumentException e) {
      view.displayModelOutput("Not a valid date.");
      endDate = null;
    } catch (RuntimeException ix) {
      view.displayModelOutput("End date needs to be before start date. " +
              "Please enter a date that is after the start date.");
      endDate = null;
    }

  }


  private void getPeriodicity() {
    try {
      daysToAdd = Long.parseLong(view.getPeriodicity());
      if (daysToAdd < 1) {
        throw new RuntimeException("Must input a number greater than 0.");
      }
    } catch (IllegalArgumentException ex) {
      view.displayModelOutput("Input must be integer.");
    } catch (RuntimeException days) {
      view.displayModelOutput("Must input a number greater than 0.");
      daysToAdd = null;
    }
  }


  private void addStockCommand() {

    getPortfolio();
    stockSymbol = view.getStockSymbol();
    getStartDate();
    getDollarAmount();
    getCostPerShare();
    LocalDate addedDate = startDate;

    try {
      addedDate = model.addStock(portfolio, this.stockSymbol, this.dollarAmount, this.startDate,
              this.costPerShare);
    } catch (IllegalArgumentException np) {
      view.displayModelOutput(np.getMessage() + " Stock not added.");
    }
    view.displayModelOutput("Stock " + stockSymbol + " added successfully on date: " +
            addedDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
  }


  private void addCommissionCommand() {


    try {
      model.addCommissionFee(portfolio, commission);
      view.displayModelOutput("Commission fee added");
    } catch (IllegalArgumentException ex) {
      view.displayModelOutput(ex.getMessage());
    }
  }


  private void addGroupCommand() {
    for (String s : weightings.keySet()) {
      view.displayModelOutput(portfolio + " " + dollarAmount + " " + weightings.get(s) + " "
              + startDate);
      try {
        transactionList.add(new TransactionDollarAmount(portfolio, s,
                dollarAmount * weightings.get(s), this.startDate, 0.0));
      } catch (IllegalArgumentException np) {
        view.displayModelOutput(np.getMessage() + " Stock not added.");
      }
    }
  }


  private void addScheduleCommand() {
    for (LocalDate i = startDate; i.isBefore(LocalDate.now()) && i.isBefore(endDate);
         i = i.plusDays(daysToAdd)) {
      for (String s : weightings.keySet()) {
        try {
          transactionList.add(new TransactionDollarAmount(portfolio, s,
                  dollarAmount * weightings.get(s), i, 0.0));
        } catch (IllegalArgumentException np) {
          view.displayModelOutput(np.getMessage() + " Stock not added.");
        }
      }
    }
  }


  private void savePortfolioCommand() {
    getPortfolio();
    try {
      model.savePortfolio(portfolio);
      view.displayModelOutput("Portfolio saved");
    } catch (IllegalArgumentException ex) {
      view.displayModelOutput(ex.getMessage());
    }
  }

  private void loadPortfolioCommand() {

    portfolio = view.getPortfolioEntry();
    StringReader loadPortfolioInput = new StringReader("loadportfolio " + portfolio);

    PortfolioController loadPortfolioCommand = new PortfolioControllerImpl(loadPortfolioInput,
            System.out);
    try {
      loadPortfolioCommand.runPortfolio(model);
      view.displayModelOutput("Portfolio " + portfolio + " loaded successfully.");
    } catch (NoSuchElementException ex) {
      view.displayModelOutput("Failed to load portfolio, please re-enter portfolio name.");
    }
  }


  private void saveStrategyCommand() {
    try {
      model.saveStrategy(strategyName, portfolio, equalWeights, weightings, startDate, endDate,
              daysToAdd, dollarAmount);
      view.displayModelOutput("Strategy saved");
    } catch (IllegalArgumentException ex) {
      view.displayModelOutput(ex.getMessage());
    }
  }

  private void loadStrategyCommand() {
    strategyName = view.getStrategyName();
    getPortfolio();
    try {
      loadStrategy(strategyName, portfolio);
      view.displayModelOutput("Load strategy successfully");
    } catch (IllegalArgumentException ex) {
      view.displayModelOutput(ex.getMessage());
    } catch (FileNotFoundException ex) {
      view.displayModelOutput("Strategy not found, please re-enter strategy name.");
    } catch (RuntimeException ex) {
      view.displayModelOutput(ex.getMessage());
    }
  }

  private void plotCommand() {
    getPortfolio();
    try {
      model.plotPerformance(portfolio);
      view.displayModelOutput("Successfully plot the performance to " + portfolio + ".jpeg");
    } catch (IllegalArgumentException ex) {
      view.displayModelOutput(ex.getMessage());
    }
  }

  private void setWeightCommand() {
    if (view.getWeightSetting()) {
      view.viewWeightInputs(model.getStocks(portfolio, startDate));
      view.getCommand();
    } else {
      view.resetWeightInputs();
      view.getCommand();
    }
  }


  private void printCommand() {
    Double totalValue;
    Double totalPurchasePrice;
    Double totalShares;
    getPortfolio();
    getStartDate();

    StringBuilder output = new StringBuilder("");

    try {
      stocks = model.getStocks(portfolio, startDate);
      double totalSum = 0.0;

      for (String s : stocks) {
        totalPurchasePrice = model.getPurchasePrice(portfolio, s, startDate);
        totalShares = model.getTotalShares(portfolio, s, startDate);
        totalValue = model.getTotalValue(portfolio, s, startDate);
        commission = model.getCommission(portfolio);
        totalSum = totalSum + totalValue;
        output.append(s + "-> Shares: " + String.format("%.2f", totalShares) + " costBasis: $"
                + String.format("%.2f", totalPurchasePrice) + " Total Value: $"
                + String.format("%.2f", totalValue) + "\n");
      }
      output.append("\nTotal Sum: $" + String.format("%.2f", totalSum) + " Current commission " +
              "Fee: $" + String.format("%.2f", commission));
      view.displayModelOutput(output.toString());
    } catch (IllegalArgumentException ex) {
      view.displayModelOutput("Not a valid portfolio.");
    }

  }

  private void addPortfolioCommand() {
    portfolio = view.getPortfolioEntry();
    try {
      model.createNewPortfolio(portfolio);
      view.displayModelOutput("New portfolio created");
    } catch (IllegalArgumentException ex) {
      portfolio = null;
      view.displayModelOutput(ex.getMessage());
    }

  }


  private void checkValidDate(String dateInput) {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    if (dateInput == null) {
      return;
    }

    sdf.setLenient(false);

    try {
      Date parseDate = sdf.parse(dateInput);

    } catch (ParseException e) {
      throw new IllegalArgumentException("Invalid date.");
    }
  }

}
