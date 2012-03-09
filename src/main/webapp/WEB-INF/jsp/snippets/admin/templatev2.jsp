<!DOCTYPE html>

<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>  <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>  <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!-->
<html lang="en">
<!--<![endif]-->
<head>
<meta charset="utf-8" />


<META HTTP-EQUIV="Cache-Control" CONTENT="max-age=0">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META http-equiv="expires" content="0">
<META HTTP-EQUIV="Expires" CONTENT="Tue, 01 Jan 1980 1:00:00 GMT">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">

<!-- Set the viewport width to device width for mobile -->
<meta name="viewport" content="width=device-width" />

<title>Page Admin</title>

<!-- Included CSS Files -->
<link rel="stylesheet" href="../../stylesheets/grid.css">
<link rel="stylesheet" href="../../stylesheets/ui.css">
<link rel="stylesheet" href="../../stylesheets/app.css">
<link rel="stylesheet" href="../../stylesheets/typography.css">
<link rel="stylesheet" href="../../stylesheets/globals.css">
<link rel="stylesheet" href="../../stylesheets/ui.dynatree.css">
<link rel="stylesheet" href="../../stylesheets/forms.css">
<link rel="stylesheet" href="../../stylesheets/reveal.css">
<link rel="stylesheet" href="../../stylesheets/mobile.css">
<link rel="stylesheet" href="../../stylesheets/admin/slash.css">
<link rel="stylesheet" href="../../stylesheets/admin/shCoreDefault.css">

<!--[if lt IE 9]>
        <link rel="stylesheet" href="stylesheets/ie.css">
    <![endif]-->


<!-- IE Fix for HTML5 Tags -->
<!--[if lt IE 9]>
        <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

</head>
<body>

	<div id='slashBar' class="container">
		
		<div class="row">
			<div class="three columns">
				<div id="slashBarLogo">
					<a href="/server/"> <img src='../../images/banner_logo.png' />
					</a>
				</div>
			</div>
			<div class="eight columns">
				<ul class="nav-bar">
					<li>
						<a class="main" href="/server/snippets/admin/sitemap">Manage Pages</a>
				    </li>
					<li class="has-flyout">
						<a href="/server/snippets/admin/sitemap_admin" class="main">Manage Sites</a>
						<a href="#" class="flyout-toggle"><span></span></a>
						<div class="flyout small">
				 			<div style='background:#FFFFCC;padding:2px'>
								More menus coming...
				 			</div>
				 			<div style='background:#FFFFCC;padding:2px'>
								We'll build this menu from the sitemap RESTful web service
				 			</div>
				 		</div>
					</li>
					<li>
						<a class="main" href="/server/snippets/admin/snippets">Manage Snippets</a> 
					</li>
					<li class='last'>
						<a class="main" href="/server/snippets/admin/template">Manage Templates</a> 
					</li>
				</ul>
			</div>
			<div class="one columns">
				<div class="actions">
					<a href='/server/logout'>logout</a>&nbsp;|&nbsp;<a href='http://slashserver.com/support' target='_blank'>support</a>
				</div>
			</div>
		</div>
	</div>
	<div class="container" id="slashMain">
		<div class="row" id="alert_container">
			<div class="twelve columns">
				<div id="slash_alert"></div>
			</div>
		</div>
		<div class="row" >
			<div class="four columns" ></div>
			<div id="toolbar" class="four columns" ></div>
			<div class="four columns" ></div>
		</div>
		<div class="row" >
			<div class="eight columns" >
					<label>Choose a page template</label><br/>
					<select id="itemSelect"></select>
					<div id="itemDetails"></div>	
			</div>
			<div class="four columns" ></div>
		</div>
		<div class="row">
			<div class="twelve columns">
			<br/>
				<div class='alert-box warning'>This page will eventually get split into snippets so I'm keeping js and css for each section separate</div>
			</div>
		</div>
	</div>

	<!-- Included JS Files -->
	
	<script src="../../javascripts/jquery.min.js"></script>
	<script src="../../javascripts/jquery.reveal.js"></script>
	<script src="../../javascripts/jquery.customforms.js"></script>
	<script src="../../javascripts/jquery.placeholder.min.js"></script>
	<script src="../../javascripts/modernizr.foundation.js"></script>
	<script src="../../javascripts/jquery.tooltips.js"></script>
	<script src="../../javascripts/app.js"></script>
	<script src="../../javascripts/underscore.js"></script>
	<script src="../../javascripts/backbone.js"></script>
	<script src="../../javascripts/admin/shCore.js"></script>
	<script src="../../javascripts/admin/shBrushXml.js"></script>
	
	
	<script src="../../javascripts/admin/templatev2.js"></script>
</body>
</html>
