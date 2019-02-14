Assignment 10 Changes:
1.	We updated the model interface to accommodate the ability to save and load portfolios. The portfolios are saved as csv files containing data for our transactions. We added methods in the model to accommodate for the new functionality. The controller also has new commands which will take output from the model, format them into csv data and save the files to a set directory. When saving a portfolio, the method will also save the API information associated with the stock to reduce excessive API calls.
2.	We added methods to the model and controller to save the dollar cost averaging investment strategy. We save the strategies by saving all of the necessary inputs for that strategy from the user in a csv file. We added new methods in the model to load and save, as well as corresponding commands in the controller. When calling for a save, the command will prompt the user for the inputs and save these in a fixed directory. When loading, the method will find the csv file to get the input information from the CSV and use these inputs to carry out the strategy for the specified portfolio. Our strategies are specific for a given portfolio.
3.	We added a separate controller for the new GUI view. This controller implements the ActionListener and gets passed to the view. The GUI controller contains all the possible commands. Each command is represented by a radio button panel in the view. The user can select the command by checking the appropriate radio button. The button selected will indicate which fields are presented for the user to input. Once the fields are filled, there is an “Execute” button that will perform the command specified by the selected radio button. All of the messages or outputs of the commands are put in a text box in the user interface. We used this dynamic design such that all of the information necessary to the user such as the inputs and the outputs are displayed at once in different panels. Any error messages will appear in the output box and the user can easily adjust the inputs accordingly. 
4.	We added a command to the model and controller that will take the value of the portfolio and print it over time.
5.	Our previous controller still works alongside the new GUI controller and view. We did not have to make changes to the existing classes, only additional functions.



Assignment 9 Changes:
1. To accommodate for the various ways to add stock, including higher level investment strategies, we designed all of the add command classes such that they all extend an abstract add class. This abstract add class contains a list that holds transaction objects. When the controller runs any add command, it will populate this list and then execute all the transactions stored in this list. Under this design, the only thing that differs between the different ways to add stock is the creation of this transaction list. Each add command will populate the transactions list differently.
The transaction list contains transaction object that can execute a method call to add stocks to our portfolio model. There are multiple concrete transaction classes extending an abstract transaction object. Each concrete transaction class represents a different type of add, thus allowing us to add stocks by total value or add stocks based on a input number of stocks.
The current supported ways to add stock are:
-	Add individually as we had done in assignment 8
-	Add all stocks in a portfolio based on a total value and specified weights for each stock, either equal or customized. 
-	Add all stocks in a portfolio based on a total value and specified weights for each stock, and allows the user to schedule an end date and start date and the periodicity of the transactions.
This design also allows for other add methods to be added with minimal changes to the existing code, for example adding based on number of stocks rather than a total amount. This can be done by creating a new concrete add command that extends the current abstract class, and populating the transaction list with the new transactions.


2. To add commission fees into the model, we added a new variable for the commissions in the portfolio model. We then modified the add stock method in the model to subtract the commission fee from the cost of the stock when calculating the cost basis, and updated the interface accordingly. Then we added a command in the controller to call the setter for the commission. 
We debated between modifying the existing model or extending and creating a new version of the model with the commissions. We decided to modify the existing model because the new additions require minimal changes to the existing add method in the model. If it were a more complicated modification, or if there were more complicated dependencies between the model and multiple controllers, we would have extend the current interface and extend the current model to create a new model with the commission fee in the add stock method. We would then need to verify that all of the existing controller functions works with the new model. 

3.  We modified the return types of the methods in the model to return the values of the stocks without any formatting. For example, get stock price will return a Double with the stock price. This way, the formatting of the output will be determined in one of the controller command classes, and no changes would be needed to the model.

4. We modified the model such that if a user inputs a date that falls on a weekend or a holiday, the model will use the next available date to purchase the stock. [add more details]




Assignment 8:
Our program follows a model, view, and controller design.

View
Our view contains a main method that will instantiate and run the controller.


Controller
The controller will offer a text menu and allow the user to enter a command. The controller will take the commands entered by the user and execute them. The controller is set up such that the commands are organized into a hashmap, and each hashmap entry points to a separate Command class. Each command class contains a method to execute. Adding new methods to the controller will only require creating a new Command class and adding it to the hashmap. It should not require changes to the existing code.  The commands for this current program include:
-	New: This method will send this command to the stock transactions model to create a new portfolio. 
-	Add: Send the command to add a stock to a given portfolio, based on user inputs for stock symbol, date, number of shares and manually input price. 
-	Print: Request the portfolio model to get the contents of a portfolio at a specified date. It will call the stock query model to obtain the market values for these stocks. 
-	Quit: Exit the program at any time
These commands represents one possible method of adding stock prices to the portfolio and obtaining the portfolio values. Additional commands can be created that pull stock prices from other sources without having to change the controller or model.




Stock Transactions Model:
At a high level, the model is organized into a list of stock portfolios, with each stock portfolio capable of holding stock transactions. 

Collection of portfolios
We created a class to organize the portfolios. This class contains a hashmap of portfolio objects, and the purpose of this is to allow multiple portfolios to be created. 

Stock portfolio object
The portfolio holds stock transactions as a hashmap of arraylists. The key of the hashmap represents the stock symbol, and each stock will have an arraylist of transactions. Storing the transactions separately allows the user to filter for and display all transactions as of a certain date, and the organization of the transactions by stock symbol makes filtering by a stock symbol more efficient. The stock portfolio allows the user to:
-	Add a new transaction – this will add a new transaction object to the portfolio
-	Print all transactions as of a certain date – when this is called, this will display all transactions before a certain date. All transactions of the same stock will be added together, showing only the total number of shares, cost basis, and market value for that stock.


Stock transactions object
Our model uses stock transaction objects to keep track of each individual stock purchase. Each time a stock is added, a transaction object is created. The reason for this design allows for a user to see the stock portfolio composition as of a certain date. The stock transaction contains: 
-	Stock symbol
-	Date of transaction
-	The cost basis on that date
-	The number of shares in the transaction. 





Stock price query model:
This model will pull stock data from an API in order to get the market value data for a stock. This model will take a stock symbol and retrieve data from an online API to obtain the closing price as of a certain date. This model use HashMap to store market price, the key is the stock symbol and the value is another
HashMap storing closing price for each day.
For each stock symbol it'll only call API once because we store the data to the model, so we can get the price from the hashmap when calling the getPortfolio
