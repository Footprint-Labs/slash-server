initSitemap();

 function initSitemap(index){
	 	if(!index){
	 		index=0;
	 	}
	    // --- Initialize sample trees
	    $("#tree").dynatree({
	      fx: { height: "toggle", duration: 200 },
	      autoFocus: false, // Set focus to first child, when expanding or lazy-loading.
	      // In real life we would call a URL on the server like this:
	//          initAjax: {
	//              url: "/getTopLevelNodesAsJson",
	//              data: { mode: "funnyMode" }
	//              },
	      // .. but here we use a local file instead:
	  /*    initAjax: {
	        url: "site1.json"
	        },*/
	
		  onActivate: function(node) {
		     // logMsg("onActivate(%o)", node);
		     //console.info(node.data.key);
		     $('body').trigger('pageActivate', {'pageId':node.data.key});
		   },
	
	      dnd: {
	          onDragStart: function(node) {
	            /** This function MUST be defined to enable dragging for the tree.
	             *  Return false to cancel dragging of node.
	             */
	        //    logMsg("tree.onDragStart(%o)", node);
	            return true;
	          },
	          onDragStop: function(node) {
	            // This function is optional.
	           // logMsg("tree.onDragStop(%o)", node);
	          },
	          autoExpandMS: 1000,
	          preventVoidMoves: true, // Prevent dropping nodes 'before self', etc.
	          onDragEnter: function(node, sourceNode) {
	            /** sourceNode may be null for non-dynatree droppables.
	             *  Return false to disallow dropping on node. In this case
	             *  onDragOver and onDragLeave are not called.
	             *  Return 'over', 'before, or 'after' to force a hitMode.
	             *  Return ['before', 'after'] to restrict available hitModes.
	             *  Any other return value will calc the hitMode from the cursor position.
	             */
	         //   logMsg("tree.onDragEnter(%o, %o)", node, sourceNode);
	            // Prevent dropping a parent below it's own child
	//                    if(node.isDescendantOf(sourceNode))
	//                        return false;
	            // Prevent dropping a parent below another parent (only sort
	            // nodes under the same parent)
	//                    if(node.parent !== sourceNode.parent)
	//                        return false;
	//                  if(node === sourceNode)
	//                      return false;
	            // Don't allow dropping *over* a node (would create a child)
	//            return ["before", "after"];
	            return true;
	          },
	          onDragOver: function(node, sourceNode, hitMode) {
	            /** Return false to disallow dropping this node.
	             *
	             */
	           // logMsg("tree.onDragOver(%o, %o, %o)", node, sourceNode, hitMode);
	            // Prevent dropping a parent below it's own child
	            if(node.isDescendantOf(sourceNode))
	              return false;
	            // Prohibit creating childs in non-folders (only sorting allowed)
	//            if( !node.isFolder && hitMode == "over" )
	//              return "after";
	          },
	          onDrop: function(node, sourceNode, hitMode, ui, draggable) {
	            /** This function MUST be defined to enable dropping of items on
	             * the tree.
	             */
	        	 
	            logMsg("tree.onDrop(%o, %o, %s)", node.data.sitemapPageId, sourceNode.data.sitemapPageId, hitMode);
	            sourceNode.move(node, hitMode);
	            
	            
	            $.getJSON("/server/json/page/movePage", 
	            		{sitemapPageIdNewLocation:node.data.sitemapPageId,
	            			sitemapPageId:sourceNode.data.sitemapPageId,
	            			relationship:hitMode},
	            	function(result) {
	            			//alert("shifted ok");
	            	});
	
	            //console.info("node="+node+" sourceNode="+sourceNode+" hitMode="+hitMode);
	            
	            // expand the drop target
	//            sourceNode.expand(true);
	          },
	          onDragLeave: function(node, sourceNode) {
	            /** Always called if onDragEnter was called.
	             */
	           // logMsg("tree.onDragLeave(%o, %o)", node, sourceNode);
	          }
	        }
	    });
	    
	    //TODO change this to proper ajax call with error handling etc.
	    // load tree from json
	    $.getJSON("/server/json/sitemap/list", function(result) {
	    	sitesList=result;
	    	$('#sites').empty();
	    	$('#sites').append("<select id='siteSelect' class='sitemap-select'/>");
	    	var i=0;
	    	$.each(result, function(index) {
	    		$('#siteSelect').append('<option value="'+i+'">'+result[index].name+'</option>');
	    		i++;
	    	});
	    	$dialog = jQuery('<div/>', {class:'reveal-modal',id:'new-site-dialog'});
			$closeButton = jQuery('<span/>',{class:'close-reveal-modal control_close_dialog'});
			$closeButton.append(jQuery('<span/>', { text: 'Cancel', class :' nice small radius blue button'}));
			$createButton = jQuery('<span/>',{class:'close-reveal-modal control_close_dialog'});
			$createButton.append(jQuery('<span/>', { text: 'Create', class :' nice small radius blue button'}));
			$createButton.click(function(){alert('Coming soon cuz!');});
			$dialog.append($closeButton);
			$dialog.append($createButton);
			$dialog.append('<a class="close-reveal-modal">&#215;</a>');
			$('#sites').append($dialog);
			
	    	$('#siteSelect').change(function(){
	    		
	    		// RCDO bit of a hack here we reload entire list in case of new or delete changes
	    		initSitemap($('#siteSelect').val());
	    	});
	    	
	    	$('#sites').append('<div style="clear:both"/>');
	    	if(index>0){
	    		$('#siteSelect option').eq(index).attr('selected', 'selected');
	    	}
	    	loadSitemap(result,index);
	    });
 }
 
 function loadSitemap(sites,index) {
	//$(".dynatree-container").empty();
	$("#tree").dynatree("getRoot").removeChildren();
	if(sites[index].children.length>0){
		$("#tree").dynatree("getRoot").addChild(sites[index].children);
		$("#tree").dynatree("getTree").activateKey(sites[index].children[0].key);
		 
		$("#tree").dynatree("getRoot").visit(function(node){
	        node.expand(true);
	      });
	}
	else {
		$('body').trigger('pageActivate', {'pageId':null});
	}
	
	// Events 
	$('body').unbind('currentSitemapRequest');
	$('body').bind('currentSitemapRequest', function(event,data) {
			
			$('body').trigger('currentSitemapResponse', {'id':sites[index].sitemapId}); 
	 });
	
	$('body').unbind('pageNameUpdate');
	$('body').bind('pageNameUpdate', function(event,data) {
		var node = $("#tree").dynatree("getTree").getNodeByKey(data.pageId);
		node.data.title = data.name;
		node.render(); 
		$("#tree").dynatree("getTree").activateKey(data.pageId);

	 });
	
	$('body').unbind('newPage');
	$('body').bind('newPage', function(event,data) {
		
		var rootNode = $("#tree").dynatree("getRoot");
		var newNode = rootNode.addChild({'title': data.name,'key':data.pageId });
		newNode.render();
		// special title field needed for dyna tree
		data.title = data.name;
		//sites[index].children.splice(sites[index].children.length,0,data);
		$("#tree").dynatree("getTree").activateKey(data.pageId);
	 });
	
	$('body').bind('deletePage');
	$('body').bind('deletePage', function(event,data) {
		var tree = $("#tree").dynatree("getTree");
		var node = tree.getNodeByKey(data.pageId);
		if(node){
			node.remove();
		}
	 });
	
 }
 

 
