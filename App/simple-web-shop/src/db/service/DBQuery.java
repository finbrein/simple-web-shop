package db.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class DBQuery {

	String userName, password;

	public DBQuery(String userName, String password){

		this.userName=userName;
		this.password=password;


	}
	

	public String executeUpdate(String query) {

		/*
		 * 
		 * Here we initializae tools for making the database connection
		 * 
		 * and reading from the database
		 */

		Connection connection = null;
		Statement statement = null;

		try {
			// For mySQL database the above code would look like this:
			Class.forName("com.mysql.jdbc.Driver");

			// For mySQL database 
			String url="jdbc:mysql://mysql.cc.puv.fi:3306/e0700180_shopping_cart";

			// Here we create a connection to the database

			connection = DriverManager.getConnection(url, userName, password);

			// Here we create the statement object for executing SQL commands.
			statement = connection.createStatement();

			// Here we executethe SQL query and save the results to a ResultSet
			// object

			statement.executeUpdate(query);


		} catch (Exception ex) {

			return ex.getMessage();

		} finally {

			try {

				// Here we close all open streams

				if (statement != null)

					statement.close();

				if (connection != null)

					connection.close();

			} catch (SQLException sqlexc) {

				return sqlexc.getMessage();

			}

		}

		return "Database table update successfully";

	} // method executeUpdate


	public String executeQuery(String query) {

		/*
		 * 
		 * Here we initialize tools for making the database connection
		 * 
		 * and reading from the database
		 */

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		//Here we declare ResultSetMetaData object to get
		//data about the result set
		ResultSetMetaData resultSetMetaData = null;

		String columnHeadings = "", columnValues = "";

		try {
			// For mySQL database the above code would look like this:
			Class.forName("com.mysql.jdbc.Driver");

			// For mySQL database 
			String url="jdbc:mysql://mysql.cc.puv.fi:3306/e0700180_shopping_cart";

			// Here we create a connection to the database

			connection = DriverManager.getConnection(url, userName, password);

			// Here we create the statement object for executing SQL commands.
			statement = connection.createStatement();

			// Here we execute the SQL query and save the results to a ResultSet object

			resultSet = statement.executeQuery(query);

			// Here we get the metadata of the query results

			resultSetMetaData = resultSet.getMetaData();

			// Here we calculate the number of columns

			int columns = resultSetMetaData.getColumnCount();			
			

			// Here we print column names in table header cells

			// Pay attention that the column index starts with 1

			for (int i = 1; i <= columns; i++) {

				columnHeadings += resultSetMetaData.getColumnName(i) + "\t";
			}

			columnHeadings += "\n";

			for (int i = 1; i <= columns; i++)
				columnHeadings += "-------";

			columnHeadings += "\n";

			while (resultSet.next()) {

				// Here we print the value of each column

				for (int i = 1; i <= columns; i++)
					columnValues += resultSet.getString(i) + "\t";


				columnValues += "\n";

			}

		} catch (Exception ex) {

			return ex.getMessage();

		} finally {

			try {

				// Here we close all open streams

				if (statement != null)

					statement.close();

				if (connection != null)

					connection.close();

			} catch (SQLException sqlexc) {

				return sqlexc.getMessage();

			}

		}

		return columnHeadings + columnValues;

	}

}