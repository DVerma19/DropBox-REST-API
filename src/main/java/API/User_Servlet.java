package API;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Servlet implementation class GetUserInfoServlet
 */
@WebServlet("/User_Servlet")
public class User_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public User_Servlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
			
			dropbox_token = user_information(dropbox_code, request);
			
		} 
		catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	        
		print_writer.println("The user info is: " + dropbox_token);    
		print_writer.close();    
	}
	

	private String user_information(String codeStr, HttpServletRequest request) throws URISyntaxException, IOException {
		
		HttpPost post_req = new HttpPost("https://api.dropboxapi.com/2/users/get_account");
		HttpSession session = request.getSession();
		
		String access_token = session.getAttribute("DropBox_AccessToken").toString();
		String account_id = session.getAttribute("DropBox_AccountID").toString();
		
		post_req.setHeader(HttpHeaders.CONTENT_TYPE,"application/json");
		post_req.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + access_token);
		
		StringBuilder json_body = new StringBuilder();
		
		json_body.append("{");
		json_body.append("\"account_id\":\""+account_id+"\"");
		json_body.append("}");

		post_req.setEntity(new StringEntity(json_body.toString()));
		
		String info_result = "";
      
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
               CloseableHttpResponse response = httpClient.execute(post_req)){
        	info_result = EntityUtils.toString(response.getEntity());
           }

           return info_result;
	}

}
