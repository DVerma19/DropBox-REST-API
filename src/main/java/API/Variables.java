package API;

public class Variables {
	
	public static String DropBox_ClientID = "9fe4zaol4scazfc";
	public static String DropBox_ClientSecret = "u4wsask1lz6uv7d";
	public static String DropBox_RedirectURL = "http://localhost:8080/DropBox_API/";
	public static String DropBox_RedirectURI = "https://www.dropbox.com/oauth2/authorize?client_id=" 
												+ DropBox_ClientID + "&redirect_uri=" + DropBox_RedirectURL
												+ "&response_type=code";

}
