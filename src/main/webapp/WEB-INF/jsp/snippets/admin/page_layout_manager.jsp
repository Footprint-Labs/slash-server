<!DOCTYPE html>



<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>  <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>  <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> 

<%@page import="org.codehaus.jackson.map.ObjectMapper"%>
<%@page import="java.util.Map"%>

<html lang="en"> <!--<![endif]-->
<head>
    <meta charset="utf-8" />
    
    <!-- Set the viewport width to device width for mobile -->
    <meta name="viewport" content="width=device-width" />
    
    <title>/server page layout builder</title>
 
    <!-- Included CSS Files -->
    
	<link rel="stylesheet" href="../../stylesheets/globals.css">
	<link rel="stylesheet" href="../../stylesheets/typography.css">
	<link rel="stylesheet" href="../../stylesheets/grid.css">
	<link rel="stylesheet" href="../../stylesheets/ui.css">
	<link rel="stylesheet" href="../../stylesheets/forms.css">
	<link rel="stylesheet" href="../../stylesheets/reveal.css">
	<link rel="stylesheet" href="../../stylesheets/app.css">
	<link rel="stylesheet" href="../../stylesheets/mobile.css">
	<link rel="stylesheet" href="../../stylesheets/admin/slash.css">

    <!--[if lt IE 9]>
        <link rel="stylesheet" href="stylesheets/ie.css">
    <![endif]-->

    
    <!-- IE Fix for HTML5 Tags -->
    <!--[if lt IE 9]>
        <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
<% 
	Map<String, ? extends Object> snippets=(Map<String, ? extends Object>)request.getAttribute("snippets");  
%>
<script>
<% 
ObjectMapper mapper = new ObjectMapper();
String snippetsJson = mapper.writeValueAsString(snippets);
%>

var allSnippets=<%=snippetsJson %>;


</script>
</head>
<body>
	
	
    <div id="slash-server"></div>
	
	
	
    <!-- Included JS Files -->
	
	<script src="../../javascripts/jquery.min.js"></script>
	<script src="../../javascripts/jquery.reveal.js"></script>
	<script src="../../javascripts/jquery.customforms.js"></script>
	<script src="../../javascripts/jquery.placeholder.min.js"></script>
	<script src="../../javascripts/modernizr.foundation.js"></script>
	<script src="../../javascripts/jquery.tooltips.js"></script>
	<script src="../../javascripts/app.js"></script>
	<script src="../../javascripts/admin/slash-common.js"></script>

	<!-- lets get this party started -->
	<script>initLayoutManager()</script>
</body>
</html>