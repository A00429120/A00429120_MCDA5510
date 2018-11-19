package mysql.com.web.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.security.util.Length;

//Does all the SQL Related work
public class MySQLAccess {

	static final SimpleLogging log = new SimpleLogging();
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
	private static String cardType = null;
	public String choice, toWhat;

	// This is to validate the different fields before entering it into the database
	public String validate(String val, String type) {
		if (val.isEmpty() || val == null) {
			return ("No value entered please enter a value: ");
		}

		// Checks if there are invalid character like "; : ! @ # $ % ^ * + ? < >"
		Pattern p = Pattern.compile("[^\\;\\:\\!\\@\\#\\$\\%\\^\\*\\+\\?\\<\\>]*");
		Matcher m = p.matcher(val);
		boolean b = m.matches();
		if (b) {
			if (type.equals("CardNumber")) {
				// validation for the CardNumber Field
				if (val.matches("^5[1-5]\\d{14}$")) {
					cardType = "MasterCard";
					return "";
				} else if (val.matches("^4\\d{15}$")) {
					cardType = "Visa";
					return "";
				} else if (val.matches("^3(4|7)\\\\d{13}$")) {
					cardType = "AmericanExpress";
					return "";
				} else {
					return ("The Card should contain only digits and should be of the size 16 and start with 4 "
							+ "or 51-55 or size 15 and start with 34 or 37 \nplease re-enter");
				}
			} else if (type.equals("ExpDate")) {
				// validation for the ExpiryDate Field
				p = Pattern.compile("^(0[1-9]|1[0-2])/20(1[6-9]|2[0-9]|3[0-1])$");
				m = p.matcher(val);
				b = m.matches();
				if (b) {
					return "";
				} else {
					return ("The Exp date should be of the format mm/yyyy whith years between "
							+ "2016 - 2031 (inclusive) please re-enter :");
				}
			} else if (type.equals("unitPrice") || type.equals("Quantity")) {
				// validation for the UnitPrice and Quantity Fields
				p = Pattern.compile("^\\d*\\.{0,1}\\d{0,2}$");
				m = p.matcher(val);
				b = m.matches();
				if (b) {
					return "";
				} else {
					return ("The " + type + " should be a digit with or without a decimal of 2 points max"
							+ " please re-enter :");
				}

			} else if (type.equals("Id")) {
				p = Pattern.compile("^\\d*$");
				m = p.matcher(val);
				b = m.matches();
				if (b) {
					return "";
				} else {
					return ("The Id should be a digit ");
				}
			}
		} else {
			return ("The input \"" + type + "\" has the following invalid charecters please re-enter "
					+ "the corrected value and hit enter : \\;\\:\\!\\@\\#\\$\\%\\^\\*\\+\\?\\<\\>");
		}

		return "";
	}

	/*
	 * This method is used to fetch the values from the User based on the ID sent
	 * and then insets the values into the data base after validating them via
	 * validate function
	 */
	public String createTransaction(Transaction trans, Connection connect) {
		int success;
		String errors = "";
		errors += validate(trans.getId(), "Id");
		errors += validate(trans.getCardNumber(), "CardNumber");
		errors += validate(trans.getExpDate(), "ExpDate");
		errors += validate(trans.getNameOnCard(), "NameOnCard");
		errors += validate(trans.getUnitPrice(), "unitPrice");
		errors += validate(trans.getQuantity(), "Quantity");

		if (!(errors.isEmpty() || errors == null || errors.length() <= 0)) {
			return errors;
		}
		trans.setTotalPrice(Double.parseDouble(trans.getUnitPrice()) * Double.parseDouble(trans.getQuantity()));
		trans.setCardType(cardType);
		try {
			// Inserting values into the Database
			preparedStatement = connect
					.prepareStatement("insert into v_govindan.transaction values (?,?,?,?,?,?,?,SYSDATE(),?,?)");
			preparedStatement.setInt(1, Integer.parseInt(trans.getId()));
			preparedStatement.setString(2, trans.getCardNumber());
			preparedStatement.setString(3, trans.getNameOnCard());
			preparedStatement.setDouble(4, Double.parseDouble(trans.getUnitPrice()));
			preparedStatement.setInt(5, Integer.parseInt(trans.getQuantity()));
			preparedStatement.setDouble(6, trans.getTotalPrice());
			preparedStatement.setString(7, trans.getExpDate());
			preparedStatement.setString(8, trans.getCreatedBy());
			preparedStatement.setString(9, trans.getCardType());
			success = preparedStatement.executeUpdate();
			if (success == 0)
				return "Transaction creation was declined !\n";
		} catch (SQLIntegrityConstraintViolationException e) {

			log.logIt(Level.SEVERE, "insertTransaction " + e.getMessage());
			return (e.getMessage() + " please try Updating the same for the ID:" + trans.getId());
		} catch (SQLException e) {
			log.logIt(Level.SEVERE, "insertTransaction 1 " + e.getMessage());
			return (e.getMessage() + " please try Updating the same for the ID:" + trans.getId());
		}
		return "Success Transaction Created !\n";
	}

	/*
	 * This method is used to either print a single transaction or to initialize a
	 * transaction to use it for update or insert
	 */
	public String getTransaction(String transId, Connection connect, boolean print) throws SQLException {
		// initializing the Transaction object
		Transaction transObj = new Transaction();
		transObj.setId(transId);
		String finaResult = "";
		// Result set get the result of the SQL query
		try {

			int count = 0;
			// Statements allow to issue SQL queries to the database
			preparedStatement = connect.prepareStatement("select * from v_govindan.transaction where id = ?");
			preparedStatement.setInt(1, Integer.parseInt(transId));
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				transObj.setNameOnCard(resultSet.getString("NameOnCard"));
				transObj.setCardNumber(resultSet.getString("CardNumber"));
				transObj.setExpDate(resultSet.getString("ExpDate"));
				transObj.setUnitPrice(resultSet.getString("unitPrice"));
				transObj.setQuantity(resultSet.getString("Quantity"));
				transObj.setTotalPrice(resultSet.getDouble("TotalPrice"));
				transObj.setCreatedOn(resultSet.getString("CreatedOn"));
				transObj.setCreatedBy(resultSet.getString("CreatedBy"));
				count++;
				if (print) {
					// printing the values
					finaResult += transObj.toString();
				}
			}

			if (resultSet != null) {
				resultSet.close();
			}
			if (count == 0 && print)
				return ("No Transaction found for the ID:" + transId);
			return finaResult;
		} catch (SQLException e) {
			log.logIt(Level.SEVERE, "getTransaction : " + e.getMessage());
			throw e;
		} finally {
			resultSet = null;
		}

	}

	/*
	 * This method is used to update the values after initializing the ID to be
	 * updated the user gets to select the filed to be updated and the update is
	 * made into the database after validation
	 */
	public String updateTransaction(Transaction trans, Connection connect) {
		String what;
		String error = "";
		int success;
		what = "";
		// displaying different fields that can be updated

		switch (choice) {
		case "1":
			error += validate(toWhat, "NameOnCard");
			trans.setNameOnCard(toWhat);
			what = "NameOnCard=\"" + toWhat + "\"";
			break;
		case "2":
			error += validate(toWhat, "CardNumber");
			trans.setCardNumber(toWhat);
			what = "CardNumber=\"" + toWhat + "\",CardType=\"" + cardType + "\"";
			break;
		case "3":
			error += validate(toWhat, "ExpDate");
			trans.setExpDate(toWhat);
			what = "ExpDate=\"" + toWhat + "\"";
			break;
		case "4":
			error += validate(toWhat, "unitPrice");
			if (!(error.isEmpty() || error == null || error.length() <=0)) {
				return error;
			}
			trans.setUnitPrice(toWhat);
			what = "UnitPrice=" + toWhat;
			what = what + ",TotalPrice=" + Double.parseDouble(trans.getUnitPrice()) + " * quantity ";
			break;
		case "5":
			error += validate(toWhat, "Quantity");
			if (!(error.isEmpty() || error == null || error.length() <=0)) {
				return error;
			}
			trans.setQuantity(toWhat);
			what = "Quantity=" + toWhat;
			what = what + ",TotalPrice=" + " totalprice "  + Double.parseDouble(trans.getQuantity());
			break;
		case "6":
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime time = LocalDateTime.now();
			toWhat = dtf.format(time);
			trans.setCreatedOn(toWhat);
			what = "CreatedOn=\"" + toWhat + "\"";
			break;
		default :
			return "Enter a valid choice";
		}

		if (!(error.isEmpty() || error == null )) {
			return error;
		} 
		try {
			// Updating the values in the database
			preparedStatement = connect.prepareStatement("update v_govindan.transaction set " + what + " where id = ?");
			preparedStatement.setInt(1, Integer.parseInt(trans.getId()));
			success = preparedStatement.executeUpdate();
			if (success == 0) {
				log.logIt(Level.SEVERE,
						"updateTransaction:" + " please try Inserting Transaction for the ID:" + trans.getId());
				return("Please try Inserting Transaction for the ID:" + trans.getId());
			}

		} catch (SQLException e) {
			log.logIt(Level.SEVERE, "updateTransaction" + e.getMessage());
			return(e.getMessage() + " please try Inserting Transaction for the ID:" + trans.getId());
		}

		return "Transaction successfully updated";
	}

	
	// This method deleted an entry from the database based on the ID given
	public String removeTransaction(String transId, Connection connect) {
		int success;
		try {
			preparedStatement = connect.prepareStatement("delete from v_govindan.transaction where id = ?");
			preparedStatement.setInt(1, Integer.parseInt(transId));
			success = preparedStatement.executeUpdate();
			if (success <= 0) {
				log.logIt(Level.SEVERE,
						"removeTransaction:" + " please try Inserting Transaction for the ID:" + transId);
				return ("Please try Inserting Transaction for the ID:" + transId);

			}

		} catch (SQLException e) {
			log.logIt(Level.SEVERE, "removeTransaction" + e.getMessage());
			return (e.getMessage() + " please try Inserting Transaction for the ID:" + transId);
		}
		return "Success Transaction Removed !\n";
	}

	/*
	 * used the return a collection of all the transaction object based on the
	 * transactions available in the database
	 */

	public Collection<Transaction> getAllTransactions(Connection connection) {
		Statement statement = null;
		ResultSet resultSet = null;
		Collection<Transaction> results = null;
		// Result set get the result of the SQL query
		try {
			// Statements allow to issue SQL queries to the database
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from v_govindan.transaction");
			results = createTrxns(resultSet);

			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			statement = null;
			resultSet = null;
		}
		return results;

	}

	private Collection<Transaction> createTrxns(ResultSet resultSet) throws SQLException {
		Collection<Transaction> results = new ArrayList<Transaction>();

		// ResultSet is initially before the first data set
		while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g. resultSet.getSTring(2);
			Transaction trxn = new Transaction();
			trxn.setId(resultSet.getString("ID"));
			trxn.setNameOnCard(resultSet.getString("NameOnCard"));
			trxn.setCardNumber(resultSet.getString("CardNumber"));
			trxn.setExpDate(resultSet.getString("ExpDate"));
			trxn.setUnitPrice(resultSet.getString("UnitPrice"));
			trxn.setQuantity(resultSet.getString("Quantity"));
			trxn.setTotalPrice(resultSet.getDouble("TotalPrice"));
			trxn.setCreatedOn(resultSet.getString("CreatedOn"));
			trxn.setCreatedBy(resultSet.getString("CreatedBy"));
			trxn.setCardType(resultSet.getString("CardType"));

			results.add(trxn);

		}

		return results;

	}

}
