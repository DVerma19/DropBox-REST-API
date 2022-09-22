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
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class ListFilesServlet
 */
@WebServlet("/File_Servlet")
public class File_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public File_Servlet() {
        super();
        // TODO Auto-generated constructor stub
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
	            
	    String result_val = null;
		try {
			result_val = file_list(request);
		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	        
		print_writer.println("The files are: " + result_val);    
		print_writer.close();    
	}
	
	private String file_list(HttpServletRequest request) throws URISyntaxException, IOException {
		
		HttpPost post_req = new HttpPost("https://api.dropboxapi.com/2/files/list_folder");
		HttpSession session = request.getSession();
		String access_token = session.getAttribute("DropBox_AccessToken").toString();
		
		
		post_req.setHeader(HttpHeaders.CONTENT_TYPE,"application/json");
		post_req.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + access_token);
		
		 StringBuilder json_body = new StringBuilder();
			 
		 	 json_body.append("{");
			 json_body.append("\"path\":\"\",");
			 json_body.append("\"recursive\":false,");
			 json_body.append("\"include_media_info\":false,");
			 json_body.append("\"include_deleted\":false,");
			 json_body.append("\"include_has_explicit_shared_members\":false,");
			 json_body.append("\"include_mounted_folders\":true,");
			 json_body.append("\"include_non_downloadable_files\":true");
			 json_body.append("}");
			 
	        post_req.setEntity(new StringEntity(json_body.toString()));
		
	        String link = "";
	        String result_val = "";
      
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
               CloseableHttpResponse response = httpClient.execute(post_req)){
        		
        		result_val = EntityUtils.toString(response.getEntity());
               
        		JSONObject result_obj = new JSONObject(result_val);
        		JSONArray result_arr = result_obj.getJSONArray("entries");
               
               for(int i = 0; i < result_arr.length(); i++)
               {
                     JSONObject arr_list = result_arr.getJSONObject(i);
                     if(!arr_list.getString(".tag").equals("folder")) {
                    	 link += "<br />" + arr_list.getString("name") + "<br />";	 
                     }
                     
               }
           }

           return link;
	}

}
