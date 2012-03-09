<html>
<head>
<META HTTP-EQUIV="Cache-Control" CONTENT="max-age=0">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META http-equiv="expires" content="0">
<META HTTP-EQUIV="Expires" CONTENT="Tue, 01 Jan 1980 1:00:00 GMT">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
</head>
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
<h1>Basic JSON service intro</h1>
<p>Have a look at this file, and AjaxTest.java. That is it.. easy eh! 
Note i havent demo-ed posting json but that is just as easy, dont worry about it
<script type="text/javascript">

$(document).ready(function() {
    // check name availability on focus lost
	 $.getJSON("/server/ajax/test", {name: '/server is awesome' }, function(result) {
        alert('Result from a serialised object! '+result.name);
    });
    
	 $.getJSON("/server/ajax/maptest", {param1: '/server is built on this json shit baby yeah!' }, function(result) {
	      alert('Result from a map! '+result.answer);
	  });
    
});
 


</script>
</html>
