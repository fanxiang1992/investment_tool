package portfolio.controller.commands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xiangfan on 11/25/18.
 */

/**
 * This class represent a date validator and will be used to check if the input date is valid.
 */
class DateValidator {

  protected void checkValidDate(String date) {

    if (date == null) {
      return;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    sdf.setLenient(false);

    try {
      Date parseDate = sdf.parse(date);

    } catch (ParseException e) {
      throw new IllegalArgumentException("Invalid date.");
    }
  }
}
