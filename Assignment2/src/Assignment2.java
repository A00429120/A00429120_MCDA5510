import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.Collection;
import java.util.logging.Level;

public class Assignment2 {

	static final SimpleLogging log = new SimpleLogging();
	static Transaction trns;
	static boolean status;
	static int count = 0;

	public static void main(String[] args) {

		MySQLAccess dao = new MySQLAccess();
		int id;
		String choice;
		boolean cont = true;

		BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
		try {
			Connection connection = dao.setupConnection();

			System.out.println("Transcation App:\\n\\n");

			while (cont) {

				System.out.println(" What would you like to do today ? please enter the corresponding number to the"
						+ " operation :\n  1-> Get Transaction\n  2-> Create Transaction\n  3-> Update Transaction\n  4-> Remove Transaction\n"
						+ "  5-> Get All Transaction\n  6-> Exit");
				choice = sc.readLine();
				// sc.readLine();
				switch (choice) {
				case "1":
					System.out.println("Please choose what you want to do :\n  1-> See the Transaction\n"
							+ "  2-> Initialize a transaction for insert/Update\n  3-> Main Menu\n");
					choice = sc.readLine();
					if (choice.equals("1")) {
						System.out.println("Please enter the ID of the transaction you want to get ? ");
						id = Integer.parseInt(sc.readLine());
						trns = dao.getTransaction(id, connection, true);
					} else if (choice.equals("2")) {
						System.out.println("Please enter the ID of the transaction you want to Initialize ? ");
						id = Integer.parseInt(sc.readLine());
						trns = dao.getTransaction(id, connection, false);
					} else if (choice.equals("3")) {
						continue;
					} else {
						System.out.println("Select a Valid option !\nBack to the main menu\n");
					}
					count++;
					break;
				case "2":
					if (trns == null) {
						System.out.println(
								"Please initialize the transaction you want to create under the \"Get Transaction\" option\n");
						continue;
					}

					status = dao.createTransaction(trns, connection);
					if (status) {
						System.out.println("Success Transaction Created !\n");
					} else {
						System.out.println("Transaction creation was declined !\n");
					}
					break;
				case "3":
					if (trns == null) {
						System.out.println(
								"Please initialize the transaction you want to update under the \"Get Transaction\" option\n");
						continue;
					}

					status = dao.updateTransaction(trns, connection);
					if (status) {
						System.out.println("Success Transaction Updated !\n");
					} else {
						System.out.println("Transaction creation was declined !\n");
					}
					break;
				case "4":
					System.out.println("Please enter the ID of the transaction you want to Remove ? ");
					id = Integer.parseInt(sc.readLine());
					status = dao.removeTransaction(id, connection);
					if (status) {
						System.out.println("Success Transaction Removed !\n");
					} else {
						System.out.println("No such transaction exists !\n");
					}
					break;
				case "5":
					Collection<Transaction> trxns = dao.getAllTransactions(connection);
					for (Transaction trxn : trxns) {
						System.out.println(trxn.toString());
					}
					break;
				case "6":
					System.out.println("    Bye !");
					sc.close();
					cont = false;
					break;
				default:
					System.out.println("Select a Valid option");
				}

			}

			if (connection != null) {
				connection.close();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.logIt(Level.SEVERE, "main " + e.getMessage());
		}

	}

}
