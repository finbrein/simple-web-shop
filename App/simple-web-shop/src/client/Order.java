package client;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties.Authenticator;
import org.apache.commons.httpclient.auth.AuthPolicy;

public class Order extends JFrame {

	private static final long serialVersionUID = 5657047807595331906L;
	private JTextField dateField = new JTextField(10);
	private JTextField quantityField = new JTextField(10);

	private JButton saveButton = new JButton("Save");
	private JButton cancelButton = new JButton("Cancel");

	public JComboBox productComboBox;
	
	JPanel outputPanel;
	JTable resultTable;

	String dt;

	public Order() {
		super("Order Form");
		setLayout(new GridLayout(5, 2, 5, 5)); // change to GridBagLayout later
		add(new JLabel("Date:"));
		add(dateField);
		add(new JLabel("Product:"));

		String query = "select productid, categoryid, productname, productprice, quantity from product";

		EndpointReference targetEPR = new EndpointReference(
				"https://app2.cc.puv.fi:8443/axis2/services/e0700180_sws_query_ws_shop1");

		String userName = "name";
		String password = "password";

		String result = getServiceResults(targetEPR, userName, password, query);

		String tableData = "";

		// Here we get the data part after the dash line
		if (result.lastIndexOf('-') != -1)
			tableData = result.substring(result.lastIndexOf('-') + 2);

		// Here we get an array of data rows
		String[] dataRows = tableData.split("\n");

		productComboBox = new JComboBox(dataRows);

		add(productComboBox);
		add(new JLabel("Quantity:"));
		add(quantityField);
		add(saveButton);
		add(cancelButton);

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		dt = dateFormat.format(date);

		dateField.setText(dt);

		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String quantity = quantityField.getText();

				final Random randGen = new Random();
				int spot, orderLine;

				spot = randGen.nextInt(6) + 1;

				orderLine = spot;

				String selCommand = (String) productComboBox.getSelectedItem();

				String[] temp;

				/* delimiter */
				String delimiter = "\t";
				/*
				 * given string will be split by the argument delimiter
				 * provided.
				 */
				temp = selCommand.split(delimiter);

				int productid = Integer.parseInt(temp[0]);
				String productname = temp[2];
				String price = temp[3];

				LoginTest lgtest = new LoginTest();
				String customerid = lgtest.getId();

				EndpointReference targetEPR = new EndpointReference(
						"https://app2.cc.puv.fi:8443/axis2/services/e0700180_sws_query_ws_shop1");

				String userName = "name";
				String password = "password";

				String q = "insert  into  orderedcarts (line_number,productid,customerid, ordertime, productname, price, quantity) values ("
						+ orderLine
						+ ","
						+ productid
						+ ","
						+ customerid
						+ ",'"
						+ dt
						+ "','"
						+ productname
						+ "','"
						+ price
						+ "','"
						+ quantity + "')";
				String result = updateService(targetEPR, userName, password, q);

				if (result.equals("Database table update successfully"))
					add(new JLabel(result));
				pack();

			}
		});
		

		// Here we make sure that the application stops after closing its
		// window.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Here we set the size of the window and make it visible.
		this.setSize(new Dimension(500, 170));
		this.setVisible(true);

		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		setLocation(size.width / 2 - getWidth() / 2 - 70, size.height / 2
				- getHeight() / 2);

	}
	

	public String updateService(EndpointReference targetEPR, String userName,
			String password, String query) {

		Options options = new Options();
		options.setTo(targetEPR);
		ServiceClient client;
		String response = "";

		try {

			System.setProperty("javax.net.ssl.trustStore", "src/my_trustStore"
					.replace('/', File.separatorChar));

			options.setTransportInProtocol(Constants.TRANSPORT_HTTPS);

			// HttpTransportProperties.Authenticator
			Authenticator authenticator = new Authenticator();

			authenticator.setUsername("axis2_user");
			authenticator.setPassword("axis2_pass");

			// Here we specify the host
			authenticator.setHost("app.cc.puv.fi");

			authenticator.setAllowedRetry(true);

			ArrayList<String> authSchemes = new ArrayList<String>();

			authSchemes.add(AuthPolicy.BASIC);

			authenticator.setAuthSchemes(authSchemes);

			// Each request will be authenticated preemptively

			authenticator.setPreemptiveAuthentication(true);

			authenticator.setRealm("external-client");

			options.setProperty(HTTPConstants.AUTHENTICATE, authenticator);

			options.setTo(targetEPR);

			client = new ServiceClient();

			client.setOptions(options);

			// String query = "";

			OMFactory omFactory = OMAbstractFactory.getOMFactory();

			OMNamespace omNameSpace = omFactory.createOMNamespace(
					"http://service.db/xsd", "dbs");

			OMElement methodElement = omFactory.createOMElement(
					"UpdateDBQuery", omNameSpace);

			OMElement argumentElement = omFactory.createOMElement("query",
					omNameSpace);

			argumentElement.addAttribute("user_name", userName, omNameSpace);
			argumentElement.addAttribute("password", password, omNameSpace);

			argumentElement.setText(query);
			// argumentElement.addChild(omFactory.createOMText(argumentElement,
			// query));

			methodElement.addChild(argumentElement);

			// Here we call the service and save the results to an
			// OMElement object.
			OMElement responseElement = client.sendReceive(methodElement);
			// Here we get the text node of the result element
			response = responseElement.getFirstElement().getText();

			// System.out.println(response);

		} catch (AxisFault e) {

			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(null, e.getMessage()
					+ " Error with creating ServiceClient");
			// JOptionPane.showMessageDialog(null,
			// "Error with creating ServiceClient");
		}

		return response;
	} // updateService

	public static String getServiceResults(EndpointReference targetEPR,
			String userName, String password, String query) {

		Options options = new Options();
		options.setTo(targetEPR);
		ServiceClient client;
		String response = "";

		try {

			System.setProperty("javax.net.ssl.trustStore", "src/my_trustStore"
					.replace('/', File.separatorChar));

			// client = new ServiceClient();

			// client.setOptions(options);

			options.setTransportInProtocol(Constants.TRANSPORT_HTTPS);

			// HttpTransportProperties.Authenticator

			Authenticator authenticator = new Authenticator();

			// authenticator.setUsername(axisUserName);

			// authenticator.setPassword(axisPassword);

			authenticator.setUsername("axis2_user");

			authenticator.setPassword("axis2_pass");

			// Here we specify the host

			authenticator.setHost("app2.cc.puv.fi");

			authenticator.setAllowedRetry(true);

			ArrayList<String> authSchemes = new ArrayList<String>();

			authSchemes.add(AuthPolicy.BASIC);

			authenticator.setAuthSchemes(authSchemes);

			// Each request will be authenticated preemptively

			authenticator.setPreemptiveAuthentication(true);

			authenticator.setRealm("external-client");

			options.setProperty(HTTPConstants.AUTHENTICATE, authenticator);

			options.setTo(targetEPR);

			client = new ServiceClient();

			client.setOptions(options);

			// String query = "";

			OMFactory omFactory = OMAbstractFactory.getOMFactory();

			OMNamespace omNameSpace = omFactory.createOMNamespace(
					"http://service.db/xsd", "dbs");

			OMElement methodElement = omFactory.createOMElement(
					"GetDBQueryResult", omNameSpace);

			OMElement argumentElement = omFactory.createOMElement("query",
					omNameSpace);

			argumentElement.addAttribute("user_name", userName, omNameSpace);
			argumentElement.addAttribute("password", password, omNameSpace);

			argumentElement.setText(query);
			// argumentElement.addChild(omFactory.createOMText(argumentElement,
			// query));

			methodElement.addChild(argumentElement);

			// Here we call the service and save the results to an
			// OMElement object.
			OMElement responseElement = client.sendReceive(methodElement);
			// Here we get the text node of the result element
			response = responseElement.getFirstElement().getText();

			// System.out.println(response);

		} catch (AxisFault e) {

			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(null, e.getMessage()
					+ " Error with creating ServiceClient");
			// JOptionPane.showMessageDialog(null,
			// "Error with creating ServiceClient");
		}

		return response;
	} // getServiceResults

}
