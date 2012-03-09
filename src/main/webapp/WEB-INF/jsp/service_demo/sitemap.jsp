
<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>  <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>  <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!-->
<html lang="en">
<!--<![endif]-->
<head>
<meta charset="utf-8" />

<!-- Set the viewport width to device width for mobile -->
<meta name="viewport" content="width=device-width" />

<META HTTP-EQUIV="Cache-Control" CONTENT="max-age=0">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META http-equiv="expires" content="0">
<META HTTP-EQUIV="Expires" CONTENT="Tue, 01 Jan 1980 1:00:00 GMT">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">


<title>Page Admin</title>

<!-- Included CSS Files -->
<link rel="stylesheet" href="../stylesheets/grid.css">
<link rel="stylesheet" href="../stylesheets/ui.css">
<link rel="stylesheet" href="../stylesheets/app.css">
<link rel="stylesheet" href="../stylesheets/typography.css">
<link rel="stylesheet" href="../stylesheets/globals.css">
<link rel="stylesheet" href="../stylesheets/ui.dynatree.css">
<link rel="stylesheet" href="../stylesheets/forms.css">
<link rel="stylesheet" href="../stylesheets/reveal.css">
<link rel="stylesheet" href="../stylesheets/admin/slash.css">
<!--[if lt IE 9]>
        <link rel="stylesheet" href="stylesheets/ie.css">
    <![endif]-->


<!-- IE Fix for HTML5 Tags -->
<!--[if lt IE 9]>
        <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

</head>
<body>



	<h1>Site maps</h1>

	<script src="../javascripts/jquery.min.js"></script>
	<script src="../javascripts/app.js"></script>
	<script src="../javascripts/jquery-ui.custom.min.js"></script>
	<script src="../javascripts/jquery.dynatree.js"></script>
	<script src="../javascripts/jquery.reveal.js"></script>



	<script type="text/javascript">
		$(document).ready(function() {

			listSitemaps();
		});

		function listSitemaps() {
			$.getJSON("/server/json/sitemap/list", {}, function(result) {
				//debugger;
				//alert("Got the sitemap back: " + result);
				renderSitemaps(result);
			});
		}

		function createSitemap() {
			$.getJSON("/server/json/sitemap/create", {
				name : $('#name').val()
			}, function(result) {
				//alert("Made a new sitemap with id: " + result.id);
				listSitemaps();
			}

			);
		}

		function renderSitemaps(sitemapList) {
			var newHtml = "";
			$
					.each(
							sitemapList,
							function(index, sitemapNode) {

								newHtml += "<div style='row'><div style='five columns'>"
										+ sitemapNode.name
										+ "<a href='#' onclick=\"deleteSitemap("
										+ sitemapNode.sitemapId
										+ ")\">delete</a>"
										+ " <input type='text' id='child_for_site_"+sitemapNode.sitemapId+"'>"
										+ "<a href='#' onclick=\"createPageForSitemap('child_for_site_"
										+ sitemapNode.sitemapId
										+ "',"
										+ sitemapNode.sitemapId
										+ ")\">new page</a></div><div style='five columns'>"
										+ getChildTable(sitemapNode.children,
												sitemapNode.sitemapId)
										+ "</div></div>";

							});
			$('#sitemapList').html(newHtml);
		}

		function getChildTable(sitemapPageNodes, sitemapId) {
			var ret = "<div class='container'>";
			$
					.each(
							sitemapPageNodes,
							function(index, sitemapPageNode) {

								ret += "<div style='row'><div style='five columns'>"
										+sitemapPageNode.index+" "+ sitemapPageNode.name
										+ "  <input type='text' id='child_for_page_"+sitemapPageNode.pageId+"'><a href='#' onclick=\"createChildForPage('child_for_page_"
										+ sitemapPageNode.pageId
										+ "',"
										+ sitemapPageNode.pageId
										+ ","
										+ sitemapId
										+ ")\">new page</a></div>"
										+ "<div style='seven columns'>"
										+ getChildTable(
												sitemapPageNode.children,
												sitemapId) + "</div>" +

										"</div>";

							});
			ret += "</div>";
			return ret;

		}

		function deleteSitemap(sitemapId) {

			$.getJSON("/server/json/sitemap/delete", {
				sitemapId : sitemapId
			}, function(result) {
				listSitemaps();
			}

			);

		}

		function createPageForSitemap(nameField, sitemapId) {

			$.getJSON("/server/json/page/createForSitemap", {
				name : $('#' + nameField).val(),
				sitemapId : sitemapId,
				orderIndex:1
			}, function(result) {
				alert("Made a new sitemap page with id: "
						+ result.sitemap_page_id);
				listSitemaps();
			}

			);

		}

		function createChildForPage(nameField, pageId, sitemapId) {

			$.getJSON("/server/json/page/createChildForPage", {
				name : $('#' + nameField).val(),
				sitemapId : sitemapId,
				pageId : pageId,
				orderIndex:1
			}, function(result) {
				alert("Made a new page with id: " + result.page_id);
				listSitemaps();
			}

			);

		}
	</script>

	<div id="sitemapList" class="container"></div>

	<h2>Create new sitemap</h2>

	Name
	<input type=text name='name' id="name">
	<br />
	<input type=submit value='Create sitemap' onclick="createSitemap()">


</body>
</html>
