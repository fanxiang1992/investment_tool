package portfolio.view;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.ScrollPaneConstants;

public class ViewPanel extends JFrame implements GUIView {


  private JButton execute;
  private JButton quit;
  private JLabel radioDisplay;
  private JLabel commandPortfolioLabel;
  private JLabel commandCommissionLabel;
  private JLabel commandStartDateLabel;
  private JLabel commandStockLabel;
  private JLabel commandDollarAmountLabel;
  private JLabel commandCostPerShareLabel;
  private JLabel commandEndDateLabel;
  private JLabel commandWeightLabel;
  private JLabel periodicityLabel;
  private JLabel strategyNameLabel;
  private JPanel mainPanel;
  private JTextArea outputBox;
  private JTextField portfolio;
  private JTextField commission;
  private JTextField stockSymbol;
  private JTextField dollarAmount;
  private JTextField costPerShare;
  private JTextField startDate;
  private JTextField endDate;
  private JTextField periodicity;
  private JTextField strategyName;
  private JRadioButton[] radioButtons;
  private JRadioButton weightSetting;
  private boolean displayWeight = false;
  private Font defaultFont = new Font("Sans Serif", Font.PLAIN, 12);
  private JPanel weightPanel = new JPanel();
  private JPanel inputPanel = new JPanel();
  private List<JTextField> fieldList = new ArrayList<>();
  private Map<String, String> weightsInput = new HashMap<>();
  private List<String> stockInputs;
  private String instructions = "Select a task using the radio " +
          "buttons and follow the instructions to input " +
          "the necessary values. Press \"Execute\" to perform the operation.";


  /**
   * This is the constructor.
   * @param input string
   */
  public ViewPanel(String input) {
    super(input);
    setTitle("Swing features");
    setSize(1000, 800);

    JScrollPane mainScrollPane;
    mainPanel = new JPanel();
    //for elements to be arranged vertically within this panel
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
    //scroll bars around this main panel
    mainScrollPane = new JScrollPane(mainPanel);
    add(mainScrollPane);

    //instructions with a scrollbar
    createInstructions();

    //text area
    //ToDo add scroll, not working
    createOutputTextArea();

    //User inputs. Only relevant fields are visible.
    createUserInputFields();

    mainPanel.add(inputPanel);
    mainPanel.add(weightPanel);
    weightPanel.setVisible(false);

    addRadioButtons();

    quit = new JButton("Quit");
    quit.setActionCommand("Quit");
    mainPanel.add(quit);
    inputPanel.add(quit);
  }


  @Override
  public void setListener(ActionListener listener) {

    for (int i = 0; i < radioButtons.length; i++) {
      radioButtons[i].addActionListener(listener);
    }
    execute.addActionListener(listener);
    weightSetting.addActionListener(listener);
    quit.addActionListener(listener);
  }


  @Override
  public void echoCommandOutput(String s) {
    radioDisplay.setText(s);
  }

  @Override
  public void displayModelOutput(String s) {
    outputBox.setText(outputBox.getText() + "\n" + "==================" + "\n" + s);
  }

  @Override
  public void display() {
    setVisible(true);
  }


  private void addRadioButtons() {
    //radio buttons
    JPanel radioPanel = new JPanel();
    int numberOfCommands = 11;
    radioPanel.setBorder(BorderFactory.createTitledBorder("Commands"));


    radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.PAGE_AXIS));
    radioButtons = new JRadioButton[numberOfCommands];
    ButtonGroup rGroup = new ButtonGroup();

    //buttons groups are used to combine radio buttons. Only one radio
    // button in each group can be selected.

    radioDisplay = new JLabel("Which one did the user select?");
    radioDisplay.setFont(defaultFont);
    radioPanel.add(radioDisplay);

    radioButtons[0] = new JRadioButton("Add Portfolio");
    radioButtons[0].setActionCommand("AddPortfolio");

    radioButtons[1] = new JRadioButton("Add Stock");
    radioButtons[1].setActionCommand("AddStock");

    radioButtons[2] = new JRadioButton("Add Group of Stocks");
    radioButtons[2].setActionCommand("AddGroup");

    radioButtons[3] = new JRadioButton("Add Investment Strategy");
    radioButtons[3].setActionCommand("AddSchedule");

    radioButtons[4] = new JRadioButton("Add Commission");
    radioButtons[4].setActionCommand("AddCommission");

    radioButtons[5] = new JRadioButton("Print Portfolio");
    radioButtons[5].setActionCommand("Print");

    radioButtons[6] = new JRadioButton("Save Portfolio");
    radioButtons[6].setActionCommand("SavePortfolio");

    radioButtons[7] = new JRadioButton("Load Portfolio");
    radioButtons[7].setActionCommand("LoadPortfolio");

    radioButtons[8] = new JRadioButton("Save Investment Strategy");
    radioButtons[8].setActionCommand("SaveStrategy");

    radioButtons[9] = new JRadioButton("Load Investment Strategy");
    radioButtons[9].setActionCommand("LoadStrategy");

    radioButtons[10] = new JRadioButton("Plot Performance");
    radioButtons[10].setActionCommand("Plot");


    for (int i = 0; i < radioButtons.length; i++) {
      radioPanel.add(radioButtons[i]);
      rGroup.add(radioButtons[i]);
    }


    radioButtons[0].doClick();

    mainPanel.add(radioPanel);
  }

  private void resetLabelAndInput() {
    commandPortfolioLabel.setVisible(true);
    portfolio.setVisible(true);
    commandCommissionLabel.setVisible(false);
    commission.setVisible(false);
    commandStockLabel.setVisible(false);
    stockSymbol.setVisible(false);
    commandDollarAmountLabel.setVisible(false);
    dollarAmount.setVisible(false);
    commandCostPerShareLabel.setVisible(false);
    costPerShare.setVisible(false);
    commandStartDateLabel.setVisible(false);
    startDate.setVisible(false);

    commandWeightLabel.setVisible(false);
    weightSetting.setVisible(false);
    weightPanel.setVisible(false);

    periodicityLabel.setVisible(false);
    periodicity.setVisible(false);
    commandEndDateLabel.setVisible(false);
    endDate.setVisible(false);
    strategyNameLabel.setVisible(false);
    strategyName.setVisible(false);
  }

  @Override
  public void getCommand() {
    resetLabelAndInput();

    if (radioButtons[0].isSelected()) {
      execute.setActionCommand("ExecuteAdd");
    } else if (radioButtons[1].isSelected()) {
      execute.setActionCommand("ExecuteAddStock");
      commandStartDateLabel.setVisible(true);
      startDate.setVisible(true);
      commandStockLabel.setVisible(true);
      stockSymbol.setVisible(true);
      commandDollarAmountLabel.setVisible(true);
      dollarAmount.setVisible(true);
      commandCostPerShareLabel.setVisible(true);
      costPerShare.setVisible(true);
    } else if (radioButtons[5].isSelected()) {
      execute.setActionCommand("ExecutePrint");
      commandStartDateLabel.setVisible(true);
      startDate.setVisible(true);
    } else if (radioButtons[4].isSelected()) {
      execute.setActionCommand("ExecuteCommission");
      commandCommissionLabel.setVisible(true);
      commission.setVisible(true);
    } else if (radioButtons[2].isSelected()) {
      if (displayWeight) {
        execute.setActionCommand("ExecuteAddGroup");
      } else {
        execute.setActionCommand("CheckPortfolioAndDate");
      }
      commandStartDateLabel.setVisible(true);
      startDate.setVisible(true);
      commandDollarAmountLabel.setVisible(true);
      dollarAmount.setVisible(true);
      commandWeightLabel.setVisible(displayWeight);
      weightSetting.setVisible(displayWeight);
      weightPanel.setVisible(displayWeight);
    } else if (radioButtons[3].isSelected()) {
      if (displayWeight) {
        execute.setActionCommand("ExecuteAddSchedule");
      } else {
        execute.setActionCommand("CheckPortfolioAndDate");
      }
      commandStartDateLabel.setVisible(true);
      startDate.setVisible(true);
      commandDollarAmountLabel.setVisible(true);
      dollarAmount.setVisible(true);
      commandEndDateLabel.setVisible(true);
      endDate.setVisible(true);
      commandWeightLabel.setVisible(displayWeight);
      weightSetting.setVisible(displayWeight);
      weightPanel.setVisible(displayWeight);
      periodicityLabel.setVisible(true);
      periodicity.setVisible(true);
    } else if (radioButtons[6].isSelected() || radioButtons[7].isSelected()) {
      if (radioButtons[6].isSelected()) {
        execute.setActionCommand("ExecuteSavePortfolio");
      } else {
        execute.setActionCommand("ExecuteLoadPortfolio");
      }
    } else if (radioButtons[8].isSelected()) {
      if (displayWeight) {
        execute.setActionCommand("ExecuteSaveStrategy");
      } else {
        execute.setActionCommand("CheckPortfolioAndDate");
      }
      strategyNameLabel.setVisible(true);
      strategyName.setVisible(true);
      commandStartDateLabel.setVisible(true);
      startDate.setVisible(true);
      commandDollarAmountLabel.setVisible(true);
      dollarAmount.setVisible(true);
      commandEndDateLabel.setVisible(true);
      endDate.setVisible(true);
      commandWeightLabel.setVisible(displayWeight);
      weightSetting.setVisible(displayWeight);
      weightPanel.setVisible(displayWeight);
      periodicityLabel.setVisible(true);
      periodicity.setVisible(true);
    } else if (radioButtons[9].isSelected()) {
      execute.setActionCommand("ExecuteLoadStrategy");
      strategyNameLabel.setVisible(true);
      strategyName.setVisible(true);
    } else if (radioButtons[10].isSelected()) {
      execute.setActionCommand("ExecutePlot");
    }
    // TODO: execute add commission

  }


  @Override
  public String getPortfolioEntry() {
    return portfolio.getText();
  }

  @Override
  public String getStartDate() {
    return startDate.getText();
  }

  @Override
  public String getEndDate() {
    return endDate.getText();
  }

  @Override
  public String getStockSymbol() {
    return stockSymbol.getText();
  }

  @Override
  public String getCommission() {
    return commission.getText();
  }

  @Override
  public String getTotalValue() {
    return dollarAmount.getText();
  }

  @Override
  public String getPricePerShare() {
    return costPerShare.getText();
  }


  @Override
  public String getPeriodicity() {
    return periodicity.getText();
  }

  @Override
  public boolean getWeightSetting() {
    return weightSetting.isSelected();
  }

  @Override
  public String getStrategyName() {
    return strategyName.getText();
  }


  @Override
  public void viewWeightInputs(List<String> stocks) {
    stockInputs = stocks;

    resetWeightInputs();
    weightPanel.setVisible(true);
    fieldList = new ArrayList<>();
    for (int i = 0; i < stocks.size(); i++) {
      weightPanel.add(new JLabel(stocks.get(i)));
      fieldList.add(new JTextField(10));
      weightPanel.add(fieldList.get(i));
    }
  }

  @Override
  public void resetWeightInputs() {
    weightPanel.removeAll();
  }

  @Override
  public void populateWeights() {

    for (int i = 0; i < fieldList.size(); i++) {
      weightsInput.put(stockInputs.get(i), fieldList.get(i).getText());
    }

  }

  @Override
  public void setDisplayWeights(boolean toDisplay) {
    this.displayWeight = toDisplay;
  }

  @Override
  public Map<String, String> getWeightsInput() {
    return weightsInput;
  }


  private void createOutputTextArea() {
    outputBox = new JTextArea(8, 10);

    JScrollPane outputScroll = new JScrollPane(outputBox);
    outputScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    outputBox.setBorder(BorderFactory.createTitledBorder("Model Output"));
    outputBox.setFont(defaultFont);

    mainPanel.add(outputScroll);

    outputBox.setEditable(false);


  }


  private void createUserInputFields() {

    inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));
    inputPanel.setBorder(BorderFactory.createTitledBorder("Command Inputs"));

    strategyNameLabel = new JLabel("Strategy Name");
    inputPanel.add(strategyNameLabel);
    strategyNameLabel.setFont(defaultFont);
    strategyName = new JTextField();
    inputPanel.add(strategyName);
    strategyNameLabel.setVisible(false);
    strategyName.setVisible(false);

    //Input area for portfolio
    commandPortfolioLabel = new JLabel("Portfolio Name");
    commandPortfolioLabel.setFont(defaultFont);
    inputPanel.add(commandPortfolioLabel);
    portfolio = new JTextField(10);
    inputPanel.add(portfolio);
    commandPortfolioLabel.setVisible(true);
    portfolio.setVisible(true);
    portfolio.setName("portfolio");

    commandCommissionLabel = new JLabel("Enter a positive number for Commission Fee");
    inputPanel.add(commandCommissionLabel);
    commandCommissionLabel.setFont(defaultFont);
    commission = new JTextField(10);
    inputPanel.add(commission);
    commandCommissionLabel.setVisible(false);
    commission.setVisible(false);

    commandStockLabel = new JLabel("Stock Symbol");
    inputPanel.add(commandStockLabel);
    commandStockLabel.setFont(defaultFont);
    stockSymbol = new JTextField(10);
    inputPanel.add(stockSymbol);
    commandStockLabel.setVisible(false);
    stockSymbol.setVisible(false);


    commandDollarAmountLabel = new JLabel("Total Dollar Amount of Transaction as a" +
            " positive number");
    inputPanel.add(commandDollarAmountLabel);
    commandDollarAmountLabel.setFont(defaultFont);
    dollarAmount = new JTextField(10);
    inputPanel.add(dollarAmount);
    commandDollarAmountLabel.setVisible(false);
    dollarAmount.setVisible(false);

    commandCostPerShareLabel = new JLabel("Cost Per Share. Enter as a positive number or enter" +
            " API to use real time data.");
    inputPanel.add(commandCostPerShareLabel);
    commandCostPerShareLabel.setFont(defaultFont);
    costPerShare = new JTextField(10);
    inputPanel.add(costPerShare);
    commandCostPerShareLabel.setVisible(false);
    costPerShare.setVisible(false);


    commandStartDateLabel = new JLabel("Start Date. Must be in the format mm/dd/yyyy");
    inputPanel.add(commandStartDateLabel);
    commandStartDateLabel.setFont(defaultFont);
    startDate = new JTextField();
    inputPanel.add(startDate);
    commandStartDateLabel.setVisible(false);
    startDate.setVisible(false);
    startDate.setName("startDate");

    commandWeightLabel = new JLabel("Indicate whether all stocks will have equals weights." +
            " Default is using equal weights, when selected it will be prompted to enter weights.");
    inputPanel.add(commandWeightLabel);
    commandWeightLabel.setFont(defaultFont);
    weightSetting = new JRadioButton("User input weights");
    weightSetting.setActionCommand("SetWeightOption");
    inputPanel.add(weightSetting);
    commandWeightLabel.setVisible(false);
    weightSetting.setVisible(false);
    weightPanel.setBorder(BorderFactory.createTitledBorder("Input Weights"));


    periodicityLabel = new JLabel("Periodicity between transactions. Enter as a positive number.");
    inputPanel.add(periodicityLabel);
    periodicityLabel.setFont(defaultFont);
    periodicity = new JTextField();
    inputPanel.add(periodicity);
    periodicityLabel.setVisible(false);
    periodicity.setVisible(false);


    commandEndDateLabel = new JLabel("End Date. Must be in the format mm/dd/yyyy");
    inputPanel.add(commandEndDateLabel);
    commandEndDateLabel.setFont(defaultFont);
    endDate = new JTextField();
    inputPanel.add(endDate);
    commandEndDateLabel.setVisible(false);
    endDate.setVisible(false);

    execute = new JButton("Execute");
    inputPanel.add(execute);
  }

  private void createInstructions() {
    JTextArea sTextArea = new JTextArea(3, 10);
    JScrollPane scrollPane = new JScrollPane(sTextArea);
    sTextArea.setLineWrap(true);
    sTextArea.setBorder(BorderFactory.createTitledBorder("Instructions"));

    mainPanel.add(scrollPane);
    sTextArea.setEditable(false);
    sTextArea.setFont(defaultFont);
    sTextArea.insert(instructions, 0);

  }

}
