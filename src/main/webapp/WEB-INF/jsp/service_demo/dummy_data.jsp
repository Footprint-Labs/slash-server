<html>
<head>
<META HTTP-EQUIV="Cache-Control" CONTENT="max-age=0">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META http-equiv="expires" content="0">
<META HTTP-EQUIV="Expires" CONTENT="Tue, 01 Jan 1980 1:00:00 GMT">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
</head>
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
<h1>Dummy data prep</h1>

<script type="text/javascript">
	function clearData() {
		$.getJSON("/server/dummydata/clear", {}, 
			function(result) {
				alert("Data cleared");
			}
		);
	}

	
	function referenceData() {
		$.getJSON("/server/dummydata/refdata", {}, 
			function(result) {
				alert("Ref data created for Role,Sitemap,PageTemplate,SnippetGroup,Snippet,User,UserRole");
			}
		);
	}

	
	function pageData() {
		$.getJSON("/server/dummydata/pagedata", {}, 
			function(result) {
				alert("Data created for Page,PageSnippet,SitemapPage,UserData");
			}
		);
	}

	
</script>

<ul>
	<li><a href="#" onclick="clearData()">Clear all data</a>
	<li><a href="#" onclick="referenceData()">Dummy Reference data</a>
	<li><a href="#" onclick="pageData()">Dummy Page Data</a>
</ul>

</html>
