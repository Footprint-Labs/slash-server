<%@page import="java.util.Enumeration"%>
<html>
<head>
<!-- this is some bollocks that should go into the head of the page -->
</head>
<body>
<h1>If you dont know me by now</h1>
<p>You wont ever know, ever know, ever know me... oooh no you wont</p>
adasd asd sadf asd assf dfgpoj  asd af asf sdgfrwg vasfa sdasd asd sdeadasd asfd ad
asd aasd adasdas dasd asd asd asd asd asdassd asd  asd asd asd <h2>The end</h2>asd asd almost.

<% 

@SuppressWarnings("rawtypes")
Enumeration parameterNames = request.getParameterNames();
while(parameterNames.hasMoreElements()){
	String paramName = (String)parameterNames.nextElement();
	
	out.println(paramName+": " +request.getParameter(paramName)+"<br>");

}




%>

</body>			
<!-- bottom content -->
<script src='/server/javascripts/jquery.min.js'></script>
<script language='javascript'> 
	$(document).ready(
			function(){ 
				//snippet_api.ajax("http://localhost:8080/server/dummydata/ajaxDest",
				//					{value2:"bob2"},
				//					function(data){
				//						
				//						alert("call worked "+data);
				//					},"GET");
				
				$.ajax({
					url:"http://localhost:8080/server/dummydata/ajaxDest",
					data:{value2:"jimmy-via override"},
					success:function(data){
						alert("override call worked "+data);
					},
					type:"GET"
				});
					
			}) ;
</script>
<!-- end bottom content -->
				