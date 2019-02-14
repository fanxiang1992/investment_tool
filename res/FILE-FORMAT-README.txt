1. We saved the portfolio as a .csv file in a portfolio folder under /res, here is an example of how it looks:
GOOG,1000.0,11/08/2018,1082.4
GOOG,1000.0,11/09/2018,1066.15
GOOG,1000.0,11/12/2018,1038.63
GOOG,1000.0,11/12/2018,1038.63
GOOG,1000.0,11/01/2018,1070.0
ADSK,1000.0,11/12/2018,130.11
AMC,1000.0,11/12/2018,15.19
JD,1000.0,11/12/2018,22.01
commission,0.0

Follow the order of "Stock symbol, cost basis, date(MM/dd/yyyy), price per share".
The last line is commission fee.
Important, if you're mannuly creating a portfolio csv file, make sure don't create more than five different stocks, because our API can only handle 5 calls per minute.

2. We saved the strategy as a .csv file in a strategy folder under /res, here is an example of how it looks like:
-- Equal weight: xiang,equal,11/13/2018,11/20/2018,2,20000.0 
follow the pattern of "portfolio name, equal, start date, end date, days between invest, dollar amount(total)".

-- Fixed weight: xiang,unequal,11/13/2018,11/23/2018,6,2000.0,GOOG,0.2,ADSK,0.3,AMC,0.15,JD,0.35
follow the pattern of "portfolio name, unequal, start date, end date, days between invest, dollar amount(total), stock name, weight, stock name, weight,..."
