<html>
<head>
<META HTTP-EQUIV="Cache-Control" CONTENT="max-age=0">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META http-equiv="expires" content="0">
<META HTTP-EQUIV="Expires" CONTENT="Tue, 01 Jan 1980 1:00:00 GMT">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
</head>
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
<h1>Pages</h1>
<script type="text/javascript">
	$(document).ready(
			function() {prepPageList();});
	
	

	function createPage() {
		$.getJSON("/server/json/page/create", {name:$('#name').val(),url:$('#url').val()}, function(result) {
			//alert("Made a new page with id: "+result.id);
			prepPageList();
	    }
	    
		 );
	}
	
	function prepPageList(){
		// check name availability on focus lost
		$.getJSON("/server/json/page/list", {}, function(result) {
			var contents = "";

			$.each(result.pages, function(index, page) {
				contents += prepPageRow(page)
			});

			$('#pageList').html(contents);
		}

		);
	}
	
	function prepPageRow(page){
		return "<div class='page'>" + page.id + " "+ page.name + " " + page.url + "<a href='#' onclick='deletePage("+page.id+")'>delete</a></div>"
	}
	
	function deletePage(id){
		$.getJSON("/server/json/page/delete", {id:id}, function(result) {
			prepPageList();
	    });
	}
	
</script>


<div id="pageList"></div>

<h2>Create new page</h2>

Name
<input type=text name='name' id="name">
<br /> URL
<input type=text name='url' id="url">
<br />
<input type=submit value='Create page' onclick="createPage()">

</html>
