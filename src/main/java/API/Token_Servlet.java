package API;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Servlet implementation class GetDropBoxTokenServlet
 */
@WebServlet("/Token_Servlet")
public class Token_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Token_Servlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			PrintWriter print_writer;    
		    response.setContentType("text/html");    
		    print_writer = response.getWriter();    
		        
		    String dropbox_token = null;
		    String dropbox_code = request.getParameter("code");    
		    
			try {
				dropbox_token = dropbox_access_token(dropbox_code, request);
				
			} catch (URISyntaxException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}   
		        
			print_writer.println(dropbox_token);    
			print_writer.close();    
	}
	

	private String dropbox_access_token(String code_arg, HttpServletRequest request) throws URISyntaxException, IOException {
		
		HttpPost post = new HttpPost("https://api.dropbox.com/oauth2/token");
		String dropbox_code = "" + code_arg;
		String result = "";
		
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("grant_type", "authorization_code"));
        parameters.add(new BasicNameValuePair("client_id", Variables.DropBox_ClientID));
        parameters.add(new BasicNameValuePair("client_secret", Variables.DropBox_ClientSecret));
        parameters.add(new BasicNameValuePair("redirect_uri", Variables.DropBox_RedirectURL));
        parameters.add(new BasicNameValuePair("code", dropbox_code));
        
        post.setEntity(new UrlEncodedFormEntity(parameters));
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault(); CloseableHttpResponse response = httpClient.execute(post)){
        	
        	result = EntityUtils.toString(response.getEntity());
               
               JSONObject token_object = new JSONObject(result);
               
               String access_token_val = token_object.getString("access_token");
               String account_id = token_object.getString("account_id");
               
    		   HttpSession session = request.getSession();
    		   
    		   session.setAttribute("DropBox_AccessToken", access_token_val);
    		   session.setAttribute("DropBox_AccountID", account_id);
           }

           return result;
	}

}
