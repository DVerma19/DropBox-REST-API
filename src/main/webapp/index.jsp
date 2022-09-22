<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
<title>DropBox API</title>
<script src="js/myajax.js"></script>
<script>
	
	function getCode() {
	  window.location.assign("Code_Servlet")
	}

	function tokenCallback(result) {
		
		  window.document.getElementById('access_token_div').innerHTML = "<br/>Access Token: &nbsp;" + 
		  result.split(":",8)[1].split(",",2)[0].toString() + "<br/><br/> Token Type: &nbsp;" + 
		  result.split(":",8)[2].split(",",2)[0].toString() + "<br/><br/>Expires In: &nbsp;" + 
		  result.split(":",8)[3].split(",",2)[0].toString() + "<br/><br/>Scope: &nbsp;" + 
		  result.split(":",8)[4].split(",",2)[0].toString() + "<br/><br/>UID: &nbsp;" + 
		  result.split(":",8)[5].split(",",2)[0].toString() + "<br/><br/>Account ID: &nbsp;" + 
		  result.split(":",8)[6].toString() + ":" + result.split(":",8)[7].replace("}","").toString();
		  
		  window.document.getElementById('getCode').disabled = 1;
		  window.document.getElementById('getToken').disabled = 1;
		  window.document.getElementById('getUser').disabled = 0;
		  window.document.getElementById('file').disabled = 1;
		  window.document.getElementById('uploadFiles').disabled = 1;
		  window.document.getElementById('listFiles').disabled = 1;
	}
	
	function userInfoCallback(result) {
		
	
		  window.document.getElementById('user_info_div').innerHTML = "<br/>Given Name: &nbsp;" +
		  result.split("{",3)[2].split(",",10)[0].split(":",2)[1].toString() + "<br/><br/>Surname: &nbsp;" + 
		  result.split("{",3)[2].split(",",10)[1].split(":",2)[1].toString() + "<br/><br/>Familiar Name: &nbsp;" + 
		  result.split("{",3)[2].split(",",10)[2].split(":",2)[1].toString() + "<br/><br/>Display Name: &nbsp;" +
		  result.split("{",3)[2].split(",",10)[3].split(":",2)[1].toString() + "<br/><br/>Abbreviated Name: &nbsp;" +
		  result.split("{",3)[2].split(",",10)[4].split(":",2)[1].replace("}","").toString() + "<br/><br/>Email: &nbsp;" +
		  result.split("{",3)[2].split(",",10)[5].split(":",2)[1].toString() + "<br/><br/>Email Verified: &nbsp;" +
		  result.split("{",3)[2].split(",",10)[6].split(":",2)[1].toString() + "<br/><br/>Disabled: &nbsp;" +
		  result.split("{",3)[2].split(",",10)[7].split(":",2)[1].toString() + "<br/><br/>Is Teammate?: &nbsp;" +
		  result.split("{",3)[2].split(",",10)[8].split(":",2)[1].replace("}","").toString();
		  
		  window.document.getElementById('getCode').disabled = 1;
		  window.document.getElementById('getToken').disabled = 1;
		  window.document.getElementById('getUser').disabled = 1;
		  window.document.getElementById('file').disabled = 0;
		  window.document.getElementById('uploadFiles').disabled = 0;
		  window.document.getElementById('listFiles').disabled = 1;
	}

	function uploadFileCallback(result) {

		window.document.getElementById('upload_file_div').innerHTML = "<br/>Name: &nbsp;" + 
		result.split(",",10)[0].split(":",2)[1].toString() + "<br/><br/>Path Lower: &nbsp;" +
		result.split(",",10)[1].split(":",2)[1].toString() + "<br/><br/>Path Display: &nbsp;" +
		result.split(",",10)[2].split(":",2)[1].toString() + "<br/><br/>ID: &nbsp;" + 
		result.split(",",10)[3].split(":",3)[2].toString() + "<br/><br/>Client Modified: &nbsp;" +
		result.split(",",10)[4].split(":",4)[1].toString() + ":" +result.split(",",10)[4].split(":",4)[2].toString() +
		":"+result.split(",",10)[4].split(":",4)[3].toString() + "<br/><br/>Server Modified: &nbsp;" +
		result.split(",",10)[5].split(":",4)[1].toString() + ":" +result.split(",",10)[4].split(":",4)[2].toString() +
		":"+result.split(",",10)[4].split(":",4)[3].toString() + "<br/><br/>Revision: &nbsp;" + 
		result.split(",",10)[6].split(":",2)[1].toString() + "<br/><br/>Size: &nbsp;" + 
		result.split(",",10)[7].split(":",2)[1].toString() + "<br/><br/>Is Downloadable?: &nbsp;" +
		result.split(",",10)[8].split(":",2)[1].toString() + "<br/><br/>Content Hash: &nbsp;" +
		result.split(",",10)[9].split(":",2)[1].replace("}","").toString();
		
		window.document.getElementById('getCode').disabled = 1;
		window.document.getElementById('getToken').disabled = 1;
		window.document.getElementById('getUser').disabled = 1;
		window.document.getElementById('file').disabled = 1;
		window.document.getElementById('uploadFiles').disabled = 1;
		window.document.getElementById('listFiles').disabled = 0;
	}

	function listFilesCallback(result) {
		
	
		  window.document.getElementById('list_files_div').innerHTML="<br /><br />" + result;
		  window.document.getElementById('getCodeBtn').disabled = 1;
		  window.document.getElementById('getTokenBtn').disabled = 1;
		  window.document.getElementById('getUserInfoBtn').disabled = 1;
		  window.document.getElementById('file').disabled = 1;
		  window.document.getElementById('uploadFileBtn').disabled = 1;
		  window.document.getElementById('listFilesBtn').disabled = 1;
	}
	
	function listFiles() {
		
		const params = new URLSearchParams(window. location.search)
		var code = params.get('code')
		doAjax("File_Servlet", "", 'listFilesCallback', "post", false)
	}
	
	function getToken() {
		
		const params = new URLSearchParams(window. location.search)
		var code = params.get('code')
		doAjax("Token_Servlet", "code=" + code, 'tokenCallback', "post", false)
	}
	
	function getUserInfo() {
		
		doAjax("User_Servlet", "", 'userInfoCallback', "post", false)
	}
	
	function uploadFile(){
		
		let req = new XMLHttpRequest();
		
		let file = document.getElementById("file").files[0];
		let formData = new FormData();
		formData.append("file", file);                                
		
		doAjax("Upload_Servlet", formData, 'uploadFileCallback', "post", false, 'multipart/form-data')
	}
	
</script>

</head>
<body>
	<div id="main">
		<h1>DropBox API</h1><br/><br/>
		<button id=getCode onclick="getCode()" >Get Code</button><br/><br/>
		<button id=getToken onclick="getToken()">Get Token</button><br/>
		<div id="access_token_div"></div><br/>
		<button id=getUser onclick="getUserInfo()" disabled>Get User Info</button><br/>
		<div id="user_info_div"></div><br/>
		<input type="file" id="file" name="file" disabled/>
		<button id=uploadFiles onclick="uploadFile()" disabled>Upload file</button>
		<br/>
		<br/>
		<div id="upload_file_div"></div>
		<br/>
		<button id=listFiles onclick="listFiles()" disabled>List files</button><br/><br/>
		<div id="list_files_div"></div><br/><br/>
	</div>
</body>
</html>