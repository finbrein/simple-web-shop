package client;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
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

public class User extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5657047807595331907L;
	private JTextField userIdField = new JTextField(10);
	private JPasswordField passwordField = new JPasswordField(10);
	private JTextField fnameField = new JTextField(10);
	private JTextField lnameField = new JTextField(10);

	private JTextField emailField = new JTextField(10);

	private JButton saveButton = new JButton("Save");
	private JButton cancelButton = new JButton("Cancel");


	public User() {
		super("User Form");
		setLayout(new GridLayout(7, 2, 5, 5)); // change to GridBagLayout later
		add(new JLabel("User ID:"));		
		add(userIdField);
		add(new JLabel("Password:"));
		add(passwordField);
		add(new JLabel("First Name:"));
		add(fnameField);
		add(new JLabel("Last Name:"));
		add(lnameField);
		add(new JLabel("Email:"));
		add(emailField);
		add(saveButton);
		add(cancelButton);

		saveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String userIdFieldText = userIdField.getText();

				char[] passwordChars = passwordField.getPassword();
				String password="";
				for(char c : passwordChars)
					password+=c;

				String fnameFieldText = fnameField.getText();
				String lnameFieldText = lnameField.getText();

				String emailFieldText = emailField.getText();




				//String result = updateService(targetEPR, userName, password, query);

				//stmt.executeUpdate("insert into " + table1 + " values ("+ orderLine + ",product, '+35812345678')");

				EndpointReference targetEPR =  new EndpointReference("https://app2.cc.puv.fi:8443/axis2/services/e0700180_sws_query_ws_shop1");

				String userName = "name";
				String password2 = "password";

				String q = "insert  into  user (userid, password, firstname, lastname, email) values ('" + userIdFieldText + "','" + password + "','" + fnameFieldText + "','" + lnameFieldText + "','" + emailFieldText + "')";
				String result = updateService(targetEPR, userName, password2, q);
				
				if (result.equals("Database table update successfully"))
					add(new JLabel(result));
				pack();

			}
		});
		
		//Here we make sure that the application stops after closing its window.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Here we set the size of the window and make it visible.
		this.setSize(new Dimension(550, 200));
		this.setVisible(true);
		
		 Toolkit toolkit = getToolkit();
		 Dimension size = toolkit.getScreenSize();
		 setLocation( size.width/2 - getWidth()/2 - 70, size.height/2 - getHeight()/2 );

	}

	public String updateService(EndpointReference targetEPR, String userName, String password, String query){

		Options options = new Options();
		options.setTo(targetEPR);
		ServiceClient client;
		String response="";


		try {

			System.setProperty("javax.net.ssl.trustStore", "src/my_trustStore".replace('/', File.separatorChar));

			//client = new ServiceClient();

			//client.setOptions(options);

			options.setTransportInProtocol(Constants.TRANSPORT_HTTPS);

			// HttpTransportProperties.Authenticator

			Authenticator authenticator = new Authenticator();

			authenticator.setUsername("axis2_user");

			authenticator.setPassword("axis2_pass");

			// Here we sepcify the host

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

			//String query = "";

			OMFactory omFactory = OMAbstractFactory.getOMFactory();

			OMNamespace omNameSpace = omFactory.createOMNamespace("http://service.db/xsd", "dbs");

			OMElement methodElement = omFactory.createOMElement("UpdateDBQuery", omNameSpace);

			OMElement argumentElement = omFactory.createOMElement("query", omNameSpace);


			argumentElement.addAttribute("user_name", userName, omNameSpace);
			argumentElement.addAttribute("password", password, omNameSpace);


			argumentElement.setText(query);
			//argumentElement.addChild(omFactory.createOMText(argumentElement, query));

			methodElement.addChild(argumentElement);

			//Here we call the service and save the results to an
			//OMElement object. 
			OMElement responseElement = client.sendReceive(methodElement);
			//Here we get the text node of the result element
			response = responseElement.getFirstElement().getText(); 

			// System.out.println(response);

		} catch (AxisFault e) {

			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(null, e.getMessage() + " Error with creating ServiceClient");
			//JOptionPane.showMessageDialog(null, "Error with creating ServiceClient");
		}

		return response;
	} // updateService

}
