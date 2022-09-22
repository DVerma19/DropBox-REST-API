package API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.commons.io.IOUtils;

/**
 * Servlet implementation class UploadFileServlet
 */
@WebServlet("/Upload_Servlet")
@MultipartConfig
public class Upload_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Upload_Servlet() {
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

	    String dropbox_token = "";
		try {
			
			dropbox_token = file_upload(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	        
		print_writer.println(dropbox_token);    
		print_writer.close(); 
	}
	
	
	public String file_upload(HttpServletRequest request) throws URISyntaxException, IOException, ServletException {
		HttpSession session = request.getSession();
		String dropbox_access_token = session.getAttribute("DropBox_AccessToken").toString();
		
		Part file_part = request.getPart("file");
	    String file_name = Paths.get(file_part.getSubmittedFileName()).getFileName().toString();
	    InputStream file_contents = file_part.getInputStream();
	    
	    byte[] bytes = IOUtils.toByteArray(file_contents);	 
	    
		String file_content = "{\"path\": \"/"+file_name+"\",\"mode\":\"add\",\"autorename\": true,\"mute\": false,\"strict_conflict\": false}";
		URL upload_url = new URL("https://content.dropboxapi.com/2/files/upload");
		HttpURLConnection post_conn = (HttpURLConnection) upload_url.openConnection();
		
		try {
			
			post_conn.setDoOutput(true);
			post_conn.setRequestMethod("POST");
			post_conn.setRequestProperty("Authorization", "Bearer " + dropbox_access_token);
			post_conn.setRequestProperty("Content-Type", "application/octet-stream");
			post_conn.setRequestProperty("Dropbox-API-Arg", "" + file_content);
			post_conn.setRequestProperty("Content-Length", String.valueOf(bytes.length));
			OutputStream outputStream = post_conn.getOutputStream();
			
			outputStream.write(bytes);
			outputStream.flush();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(post_conn.getInputStream()));
			
			String input_val;
			StringBuffer response = new StringBuffer();
			while ((input_val = in.readLine()) != null) { response.append(input_val); }
			in.close();
			String queries = response.toString();
			return queries;
		}
		
		finally { 
			post_conn.disconnect(); 
		}
	}

}
