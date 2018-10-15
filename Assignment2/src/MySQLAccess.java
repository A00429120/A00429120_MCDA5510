
/**
 * Original source code from 
 * http://www.vogella.com/tutorials/MySQLJava/article.html
 * 
 **/

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

//Does all the SQL Related work
public class MySQLAccess {

	static final SimpleLogging log = new SimpleLogging();
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
	private static String cardType = null;

	// Initializes the connection to the database and return the Connection option
	public Connection setupConnection() throws Exception {

		Connection connect = null;
		try {
			// This will load the MySQL driver, each DB has its own driver
			// Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mysql.cj.jdbc.Driver");
			// Setup the connection with the DB

			connect = DriverManager.getConnection("jdbc:mysql://dev.cs.smu.ca:3306/v_govindan?"
					+ "user=v_govindan&password=A00429120" + "&useSSL=false"
					+ "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");

		} catch (Exception e) {
			throw e;
		} finally {

		}
		return connect;
	}

	// This is to validate the different fields before entering it into the database
	public String validate(String val, String type) {
		BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
		type = type == null ? "empty" : type;
		try {
			if (val.isEmpty()) {
				System.out.println("No value entered please enter a value: ");
				val = sc.readLine();
				validate(val, type);
			}

			// Checks if there are invalid character like "; : ! @ # $ % ^ * + ? < >"
			Pattern p = Pattern.compile("[^\\;\\:\\!\\@\\#\\$\\%\\^\\*\\+\\?\\<\\>]*");
			Matcher m = p.matcher(val);
			boolean b = m.matches();
			if (b) {
				if (type.equals("card")) {
					// validation for the CardNumber Field
					if (val.matches("^5[1-5]\\d{14}$")) {
						cardType = "MasterCard";
						return val;
					} else if (val.matches("^4\\d{15}$")) {
						cardType = "Visa";
						return val;
					} else if (val.matches("^3(4|7)\\\\d{13}$")) {
						cardType = "AmericanExpress";
						return val;
					} else {
						System.out.println(
								"The Card should contain only digits and should be of the size 16 and start with 4 "
										+ "or 51-55 or size 15 and start with 34 or 37 \nplease re-enter :");
						val = sc.readLine();
						validate(val, type);
					}
				} else if (type.equals("exp")) {
					// validation for the ExpiryDate Field
					p = Pattern.compile("^(0[1-9]|1[0-2])/20(1[6-9]|2[0-9]|3[0-1])$");
					m = p.matcher(val);
					b = m.matches();
					if (b) {
						return val;
					} else {
						System.out.println("The Exp date should be of the format mm/yyyy whith years between "
								+ "2016 - 2031 (inclusive) please re-enter :");
						val = sc.readLine();
						validate(val, type);
					}
				} else if (type.equals("digit")) {
					// validation for the UnitPrice and Quantity Fields
					p = Pattern.compile("^\\d*\\.{0,1}\\d{0,2}$");
					m = p.matcher(val);
					b = m.matches();
					if (b) {
						return val;
					} else {
						System.out.println("The value should be a digit with or without a decimal of 2 points max"
								+ " please re-enter :");
						val = sc.readLine();
						validate(val, type);
					}
					return val;
				}
			} else {
				System.out.println("The input has the following invalid charecters please re-enter "
						+ "the corrected value and hit enter : \\;\\:\\!\\@\\#\\$\\%\\^\\*\\+\\?\\<\\>");
				val = sc.readLine();
				validate(val, type);
			}
		} catch (IOException e) {
			log.logIt(Level.SEVERE, "Validate: " + e.getMessage());
		}

		return val;
	}

	/*
	 * This method is used to fetch the values from the User based on the ID sent
	 * and then insets the values into the data base after validating them via
	 * validate function
	 */
	public Boolean createTransaction(Transaction trans, Connection connect) {
		int success;
		Scanner scn = new Scanner(System.in);
		List<String> mustValidate = new ArrayList<String>();
		mustValidate.add("NameOnCard");
		mustValidate.add("CardNumber");
		mustValidate.add("ExpDate");
		List<String> values = new ArrayList<String>();
		String[] fields = { "NameOnCard", "CardNumber", "UnitPrice", "Quantity", "ExpDate" };

		for (String field : fields) {
			System.out.println("Enter the value for \"" + field + "\" : ");
			String val = scn.nextLine();
			if (mustValidate.contains(field)) {
				if (field.equals("CardNumber"))
					values.add(validate(val, "card"));
				else if (field.equals("ExpDate"))
					values.add(validate(val, "exp"));
				else if (field.equals("NameOnCard"))
					values.add(validate(val, null));
				else
					values.add(validate(val, "digit"));
			} else {
				values.add(val);
			}
		}
		// setting the values to the transaction object
		trans.setCardNumber(values.get(0));
		trans.setNameOnCard(values.get(1));
		trans.setUnitPrice(Double.parseDouble(values.get(2)));
		trans.setQuantity(Integer.parseInt(values.get(3)));
		trans.setTotalPrice(trans.getQuantity() * trans.getUnitPrice());
		trans.setExpDate(values.get(4));
		trans.setCreatedBy(System.getProperty("user.name"));
		trans.setCardType(cardType);

		try {
			// Inserting values into the Database
			preparedStatement = connect
					.prepareStatement("insert into v_govindan.transaction values (?,?,?,?,?,?,?,SYSDATE(),?,?)");
			preparedStatement.setInt(1, trans.getId());
			preparedStatement.setString(2, trans.getCardNumber());
			preparedStatement.setString(3, trans.getNameOnCard());
			preparedStatement.setDouble(4, trans.getUnitPrice());
			preparedStatement.setInt(5, trans.getQuantity());
			preparedStatement.setDouble(6, trans.getTotalPrice());
			preparedStatement.setString(7, trans.getExpDate());
			preparedStatement.setString(8, trans.getCreatedBy());
			preparedStatement.setString(9, trans.getCardType());
			success = preparedStatement.executeUpdate();
			if (success == 0)
				return false;
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println(e.getMessage() + " please try Updating the same for the ID:" + trans.getId());
			log.logIt(Level.SEVERE, "insertTransaction " + e.getMessage());
			return false;
		} catch (SQLException e) {
			System.out.println(e.getMessage() + " please try Updating the same for the ID:" + trans.getId());
			log.logIt(Level.SEVERE, "insertTransaction 1 " + e.getMessage());
			return false;
		}
		return true;
	}

	/*
	 * This method is used to either print a single transaction or to initialize a
	 * transaction to use it for update or insert
	 */
	public Transaction getTransaction(int transId, Connection connect, boolean print) throws SQLException {
		// initializing the Transaction object
		Transaction transObj = new Transaction();
		transObj.setId(transId);
		// Result set get the result of the SQL query
		try {

			int count = 0;
			// Statements allow to issue SQL queries to the database
			preparedStatement = connect.prepareStatement("select * from v_govindan.transaction where id = ?");
			preparedStatement.setInt(1, transId);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				transObj.setNameOnCard(resultSet.getString("NameOnCard"));
				transObj.setCardNumber(resultSet.getString("CardNumber"));
				transObj.setExpDate(resultSet.getString("ExpDate"));
				transObj.setUnitPrice(resultSet.getDouble("UnitPrice"));
				transObj.setQuantity(resultSet.getInt("Quantity"));
				transObj.setTotalPrice(resultSet.getDouble("TotalPrice"));
				transObj.setCreatedOn(resultSet.getString("CreatedOn"));
				transObj.setCreatedBy(resultSet.getString("CreatedBy"));
				count++;
				if (print) {
					// printing the values
					System.out.println(transObj.toString());
				}
			}

			if (resultSet != null) {
				resultSet.close();
			}
			if (count == 0 && print)
				System.out.println("No Transaction found for the ID:" + transId);
		} catch (SQLException e) {
			log.logIt(Level.SEVERE, "getTransaction : " + e.getMessage());
			throw e;
		} finally {

			resultSet = null;
		}
		return transObj;

	}

	/*
	 * This method is used to update the values after initializing the ID to be
	 * updated the user gets to select the filed to be updated and the update is
	 * made into the database after validation
	 */
	public Boolean updateTransaction(Transaction trans, Connection connect) {
		String what, toWhat, choice;
		int success;
		what = "";
		// displaying different fields that can be updated
		System.out.println("Enter the coresponding number to update that field:\n 1->Name on Card\n "
				+ "2->Card Number\n 3->Exp Date\n 4->Unit Price\n 5->Qty\n 6->Created On\n " + "7->Exit\\Cancel\n");
		Scanner scn = new Scanner(System.in);
		choice = scn.next();

		scn.nextLine();

		switch (choice) {
		case "1":
			System.out.println("What do you want to update it to ? : ");
			toWhat = scn.nextLine();
			toWhat = validate(toWhat, null);
			trans.setNameOnCard(toWhat);
			what = "NameOnCard=\"" + toWhat + "\"";
			break;
		case "2":
			System.out.println("The Card should contain only digits and shoul be of the size 15 or 16 ");
			toWhat = scn.nextLine();
			toWhat = validate(toWhat, "card");
			trans.setCardNumber(toWhat);
			what = "CardNumber=\"" + toWhat + "\",CardType=\"" + cardType + "\"";
			break;
		case "3":
			System.out.println(
					"The Exp date should be of the format mm/yyyy whith years between " + "2016 - 2031 (inclusive) :");
			toWhat = scn.nextLine();
			toWhat = validate(toWhat, "exp");
			trans.setExpDate(toWhat);
			what = "ExpDate=\"" + toWhat + "\"";
			break;
		case "4":
			System.out.println("What do you want to update it to ? : ");
			toWhat = scn.nextLine();
			toWhat = validate(toWhat, "digit");
			trans.setUnitPrice(Double.parseDouble(toWhat));
			what = "UnitPrice=" + toWhat;
			what = what + ",TotalPrice=" + trans.getUnitPrice() * trans.getQuantity();
			break;
		case "5":
			System.out.println("What do you want to update it to ? : ");
			toWhat = scn.nextLine();
			toWhat = validate(toWhat, "digit");
			trans.setQuantity(Integer.parseInt(toWhat));
			what = "Quantity=" + toWhat;
			what = what + ",TotalPrice=" + trans.getUnitPrice() * trans.getQuantity();
			break;
		case "6":
			System.out.println("What do you want to update it to ? : ");
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime time = LocalDateTime.now();
			toWhat = dtf.format(time);
			trans.setCreatedOn(toWhat);
			what = "CreatedOn=\"" + toWhat + "\"";
			break;
		case "7":
			return null;
		default:
			System.out.println("Select a Valid option");
			updateTransaction(trans, connect);
		}
		try {
			// Updating the values in the database
			preparedStatement = connect.prepareStatement("update v_govindan.transaction set " + what + " where id = ?");
			preparedStatement.setInt(1, trans.getId());
			success = preparedStatement.executeUpdate();
			if (success == 0) {
				System.out.println("Please try Inserting Transaction for the ID:" + trans.getId());
				log.logIt(Level.SEVERE,
						"updateTransaction:" + " please try Inserting Transaction for the ID:" + trans.getId());
				return false;
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage() + " please try Inserting Transaction for the ID:" + trans.getId());
			log.logIt(Level.SEVERE, "updateTransaction" + e.getMessage());
			return false;
		}

		return true;
	}

	// This method deleted an entry from the database based on the ID given
	public Boolean removeTransaction(int transId, Connection connect) {
		int success;
		try {
			preparedStatement = connect.prepareStatement("delete from v_govindan.transaction where id = ?");
			preparedStatement.setInt(1, transId);
			success = preparedStatement.executeUpdate();
			if (success <= 0) {
				System.out.println("Please try Inserting Transaction for the ID:" + transId);
				log.logIt(Level.SEVERE,
						"removeTransaction:" + " please try Inserting Transaction for the ID:" + transId);
				return false;
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage() + " please try Inserting Transaction for the ID:" + transId);
			log.logIt(Level.SEVERE, "removeTransaction" + e.getMessage());
			return false;
		}
		return true;
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
			trxn.setId(resultSet.getInt("ID"));
			trxn.setNameOnCard(resultSet.getString("NameOnCard"));
			trxn.setCardNumber(resultSet.getString("CardNumber"));
			trxn.setExpDate(resultSet.getString("ExpDate"));
			trxn.setUnitPrice(resultSet.getDouble("UnitPrice"));
			trxn.setQuantity(resultSet.getInt("Quantity"));
			trxn.setTotalPrice(resultSet.getDouble("TotalPrice"));
			trxn.setCreatedOn(resultSet.getString("CreatedOn"));
			trxn.setCreatedBy(resultSet.getString("CreatedBy"));
			trxn.setCardType(resultSet.getString("CardType"));

			results.add(trxn);

		}

		return results;

	}

}
