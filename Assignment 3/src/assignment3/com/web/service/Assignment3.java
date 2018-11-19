package assignment3.com.web.service;

import java.util.Collection;
import mysql.connections.com.*;
import java.util.logging.Level;
import mysql.com.web.service.*;

public class Assignment3 {

	static final SimpleLogging log = new SimpleLogging();
	static MySQLAccess dao = new MySQLAccess();
	static Connections connect = Connections.getCon();
	static Transaction trans;
	static boolean status;
	static int count = 0;

	public String create(String id, String cardNumber, String expDate, String nameOnCard, String unitPrice,
			String quantity) {
		if (id == null || cardNumber == null || expDate == null || nameOnCard == null || unitPrice == null)
			return "All values are required to create a transaction";
		trans = new Transaction();
		try {
			trans.setId(id);
			trans.setCardNumber(cardNumber);
			trans.setNameOnCard(nameOnCard);
			trans.setUnitPrice(unitPrice);
			trans.setQuantity(quantity);
			trans.setExpDate(expDate);
			trans.setCreatedBy(System.getProperty("user.name"));
			return dao.createTransaction(trans, connect.setupConnection());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.logIt(Level.SEVERE, "create " + e.getMessage());
			return (e.getMessage());
		}

	}

	public String getAll() {
		String result = "";
		try {
			Collection<Transaction> trxns = dao.getAllTransactions(connect.setupConnection());
			for (Transaction trxn : trxns) {
				result += trxn.toString();
			}
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.logIt(Level.SEVERE, "getAll " + e.getMessage());
			return (e.getMessage());
		}
	}

	public String get(String id) {
		if (id == null)
			return "Please Id the values";
		try {
			return dao.getTransaction(id, connect.setupConnection(), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.logIt(Level.SEVERE, "get " + e.getMessage());
			return (e.getMessage());
		}
	}

	public String remove(String id) {
		if (id == null)
			return "Please Id the values";
		try {
			return dao.removeTransaction(id, connect.setupConnection());
		} catch (Exception e) {
			log.logIt(Level.SEVERE, "remove " + e.getMessage());
			return (e.getMessage());
		}
	}

	public String update(String id, String choice, String value) {
		if (id == null || choice == null || value == null)
			return "Please enter all the values";
		trans = new Transaction();
		trans.setId(id);
		dao.choice = choice;
		dao.toWhat = value;
		try {
			return dao.updateTransaction(trans, connect.setupConnection());
		} catch (Exception e) {
			return (e.getMessage());
		}
	}

}
