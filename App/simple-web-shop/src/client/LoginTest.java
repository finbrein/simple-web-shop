package client;

import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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

public class LoginTest {

	private static String id = null;
	private static String name = null;
	private static String pWord = null;

	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPassWord() {
		return pWord;
	}

	private static void createAndShowUI() {
		LoginPanel login = new LoginPanel();
		int response = JOptionPane.showConfirmDialog(null, login, "Please Enter Name and Password", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (response == JOptionPane.OK_OPTION) {
			name = login.getName();
			pWord = login.getPassword();


			String query = "select id, userid, password, firstname, lastname, email from user";

			EndpointReference targetEPR =  new EndpointReference("https://app2.cc.puv.fi:8443/axis2/services/e0700180_sws_query_ws_shop1");

			String userName = "yourid";
			String password = "yourpassword";

			String result = getServiceResults(targetEPR, userName, password, query);

			String tableData="";

			//Here we get the data part after the dash line
			if(result.lastIndexOf('-') !=-1)
				tableData = result.substring(result.lastIndexOf('-')+2);

			//Here we get an array of data rows
			String[] dataRows = tableData.split("\n");

			String delimiter = "\t";
			String[] temp;
			String correctName = null;
			String correctPass = null;

			for(int i=0; i < dataRows.length; i++)
			{
				temp = dataRows[i].split(delimiter);

				for(int j=0; j < temp.length; j++)
					System.out.println(temp[j]);
					
					if(name.equalsIgnoreCase(temp[1]) && pWord.equals(temp[2]))
					{
						id = temp[0];
						correctName = temp[1];
						correctPass = temp[2];
					}
			}
			
			if (name.equals(correctName) && pWord.equals(correctPass)) {
				Product frame = new Product();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}

			else {
				String msg = "Click OK to try again. \nClick Cancel to login in as unkown.";
				int responze = JOptionPane.showConfirmDialog(null, msg, "Please Enter Name and Password", 
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);


				if (responze == JOptionPane.OK_OPTION) {
					createAndShowUI();
				}
				else
				{
					name = "unknown";
					pWord = "unknown";
					Product frame = new Product();
				}
			}
		}

	}

	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				createAndShowUI();
			}
		});
	}
	
	public static String getServiceResults(EndpointReference targetEPR, String userName, String password, String query){

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
			
			//authenticator.setUsername(axisUserName);

			//authenticator.setPassword(axisPassword);

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

			OMElement methodElement = omFactory.createOMElement("GetDBQueryResult", omNameSpace);

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
	} // getServiceResults
}

class LoginPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField nameField = new JTextField(10);
	private JPasswordField passwordField = new JPasswordField(10);

	public LoginPanel() {
		setLayout(new GridLayout(2, 2, 5, 5)); // change to GridBagLayout later
		add(new JLabel("Name:"));
		add(nameField);
		add(new JLabel("Password:"));
		add(passwordField);
	}

	public String getName() {
		return nameField.getText();
	}

	public String getPassword() {
		return new String(passwordField.getPassword()); // shouldn't do this!
	}


}