package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

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

public class Product extends JFrame{	

	/**
	 * 
	 */
	private static final long serialVersionUID = -672622259997193905L;
	
	Container contentPane;

	//EndpointReference targetEPR =  new EndpointReference(targetEPRText);
	
	
	String userName = "";
	String password = "";
	
	JButton submitButton, newCustomerButton, orderButton;
	JPanel inputPanel, outputPanel;
	JTable resultTable;
	
	String[] prodata;
	
	public Product() {

		super("Product Form");

		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		userName = "name";
		password = "password";
		
		inputPanel = new JPanel();		
		outputPanel = new JPanel();

		newCustomerButton = new JButton("New Customer");
		
		newCustomerButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				User cust = new User();
				cust.setVisible(true);
				//int response = JOptionPane.showConfirmDialog(null, cust, "Please Enter Name and Password", 
				//		JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			}
			
		});
		
		orderButton = new JButton("Order");
		
		orderButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Order order = new Order();
				order.setVisible(true);
			}
			
		});
		
		
		submitButton = new JButton("View Product");
		submitButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {				

				String query = "select productid, categoryid, productname, productprice, quantity from product";
				
				EndpointReference targetEPR =  new EndpointReference("https://app.cc.puv.fi:8443/axis2/services/e0700180_sws_query_ws_40");
					
				String result = getServiceResults(targetEPR, userName, password, query);
				//Here we get the first line so that we don't include line break				

				String tableHeading ="";
				if(result.indexOf('\n') !=-1)
					tableHeading=result.substring(0, result.indexOf('\n'));

				String tableData="";

				//Here we get the data part after the dash line
				if(result.lastIndexOf('-') !=-1)
					tableData = result.substring(result.lastIndexOf('-')+2);

				//Here we get column titles, which are separated
				//by \t from each other.
				String[] colHeads = tableHeading.split("\t");

				//Here we get an array of data rows
				String[] dataRows = tableData.split("\n");
				
				//Here we declare a matrix to save table data 
				Object[][] data = new Object[dataRows.length][];

				//Here we form the data table by getting columns
				//separated by \t from each other 
				for ( int i = 0; i < dataRows.length; i++)
					data[i] = dataRows[i].split("\t");					

				//Here we create the table with table data and column headings
				resultTable = new JTable(data, colHeads);

				int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
				int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
				JScrollPane jsp = new JScrollPane(resultTable, v, h);

				outputPanel.add(jsp, BorderLayout.CENTER);

				Graphics g = outputPanel.getGraphics();
				paintAll(g);
				
			}


		});

		
		LoginTest lgtest = new LoginTest();
		String customerName = lgtest.getName();		
		
		inputPanel.add(submitButton);
		
		if (customerName.equals("unknown"))
			inputPanel.add(newCustomerButton);
		else
			inputPanel.add(orderButton);
		
		contentPane.add(inputPanel, BorderLayout.SOUTH);

		
		outputPanel.setLayout(new BorderLayout());
		contentPane.add(outputPanel, BorderLayout.CENTER);

		//Here we make sure that the application stops after closing its window.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Here we set the size of the window and make it visible.
		this.setSize(new Dimension(550, 600));
		this.setVisible(true);
		
		 Toolkit toolkit = getToolkit();
		 Dimension size = toolkit.getScreenSize();
		 setLocation( size.width/2 - getWidth()/2 - 70, size.height/2 - getHeight()/2 );

	}
	
	public String getServiceResults(EndpointReference targetEPR, String userName, String password, String query){

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
