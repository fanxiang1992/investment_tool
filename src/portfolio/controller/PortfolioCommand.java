package portfolio.controller;


/**
 * This class is an interface for commands that can be added to a controller.
 */

import portfolio.model.PortfolioCollection;

/**
 * This is an interface for the controller commands. Each command is in a new class. It will take
 * the user inputs and execute them.
 */

public interface PortfolioCommand {

  /**
   * This method will takes in a PortfolioCollection model and command the model to do something
   * based on the method.
   *
   * @param input Input a PortfolioCollection model that the controller will send the command to.
   * @return false to quit the app, true to continue to new command
   */
  boolean goTo(PortfolioCollection input);

}