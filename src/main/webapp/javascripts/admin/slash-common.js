
//Globals
	var dialogCount=0;
	var classArray = new Array();
	var colSize =0;
	classArray[0]='one';
	classArray[1]='two';
	classArray[2]='three';
	classArray[3]='four';
	classArray[4]='five';
	classArray[5]='six';
	classArray[6]='seven';
	classArray[7]='eight';
	classArray[8]='nine';
	classArray[9]='ten';
	classArray[10]='eleven';
	classArray[11]='twelve';
	classArray[12]='columns';
	classArray[13]='row';
	classArray[14]='design_row';
	classArray[15]='design_column';
	classArray[16]='container';
	classArray[17]='design_container';
	classArray[18]='hide-on-phones';
	classArray[19]='hide-on-desktops';
	classArray[20]='hide-on-tablets';
	var page;
	

function initLayoutManager(){
	
	validateTemplate();
}
	
	
	var $snippet = jQuery("snippet:<br><snippet/><br>:end snippet");
	
	function loadLayout(){
		
		var pageId = getURLParameter("pageId");
		if (pageId != 'null'){
			$.ajax({
				  url: "/server/json/pageMarkup/load",
				  dataType: 'json',
				  data: {id: pageId },
				  success: function(result) {
					  	page=result;
				        if(page.markup && page.markup!= 'null'){
				        	$('#slash-server').append(decodeURI(page.markup));
				        }
				        showControls();
				        
				  },
				  error: function(jqXHR, textStatus, errorThrown){
					  alertPage("error","Whakatane'd",jqXHR, textStatus, errorThrown);
				  }
			});
		}
		else {
			showControls();
		}
		
		
	}
	
	function validateTemplate(){
		$('body').prepend('<p id="preview_toggle"/>');
		$closeButton = jQuery('<span/>', { text: 'Close', class :'admin-button nice small radius red button'});
		$closeButton.click(function(){
			// this should start working when we open the window ourselves
			window.close();
		});
		
		$('#preview_toggle').append($closeButton);
		
		$saveButton = jQuery('<span/>', { text: 'Save', class :'admin-button nice small radius blue button'});
		
		//AM: this is only called from markup page
		$saveButton.click(function(){
			hideControls();
			//addInstanceNumbers();
			
			$.ajax({
				  url: "/server/json/pageMarkup/commit",
				  method:'post',
				  dataType: 'json',
				  data: {id: page.id ,markup:encodeURI($('#slash-server').html())},
				  success: function(result) {
				        if(page.name!=$('#name').attr('value')){
				        	// name has changed so update tree
				        	$('body').trigger('pageNameUpdate', {'pageId':page.id,'name':$('#name').attr('value')});
				        }
				        alertPage("success","Layout saved...");
				  },
				  error: function(jqXHR, textStatus, errorThrown){
					  alertPage("error","Whakatane'd",jqXHR, textStatus, errorThrown);
				  }
				});
			showControls();
			
		});
		
		$('#preview_toggle').append($saveButton);
		
		// check for only 1 slash-server div
		if ($('#slash-server').length==0){
			alert ("Invalid page template, couldn't find DIV with id slash-server");
			return false;
		}
		
		else {
			
			$('#slash-server').attr('id','slash-server-temp');
			if ($('#slash-server').length>0){
				alert ("Invalid page template, more than one DIV with id slash-server was found.");
				return false;
			}
		}
		$('#slash-server-temp').attr('id','slash-server');
		$('#preview_toggle').prepend('<a href=# onclick="hideControls()" class="inactive-toggle" >preview</a>');
		$('#preview_toggle').prepend('<a href=# onclick="showControls()" class="active-toggle" >design</a> | ');
		$('#preview_toggle').append('<div style="float:right"><div id="slash_alert"></div></div>');
		loadLayout();
		
	}
	
	function addContainerActions(){
		$(".container").each(function() {
			$options = new jQuery('<span/>',{text:'options',class:'control_container_option nice small radius black button'});
			$options.css('padding','4px 4px 7px');
			//$options.attr('data-reveal-id',dialogId);
			$options.click(function(e) {
				//e.preventDefault();
				$(this).next().reveal();
				
				// add the css classes for this container
				$containerCssClassesSpan = jQuery('<span/>');
				$containerCssClassesAdd = jQuery('<input/>',{style:'width:80px'});
				$containerCssClassesAdd.keypress(function(event) {
					if ( event.which == 13 ) {
						event.preventDefault();
						// add the new css item to the container
						$(this).parent().parent().parent().parent().addClass($(this).attr('value'));
						// add the new css item to the dialog
						var $close = jQuery('<a/>', {text:'  x ',class:'control_css_close'});
						$close.click(function() {
							$(this).parent().parent().parent().parent().parent().removeClass($(this).parent().text());
							$(this).parent().remove();
						});
						var $item = jQuery('<span class="control_dialog_css_item">'+$(this).attr('value')+'</span>');
						$item.append($close);
						$(this).parent().append($item);
						$(this).attr('value','');
					}
				});
				
				$containerCssClassesSpan.append($containerCssClassesAdd);
				
				var containerCssClasses = $(this).parent().attr('class').split(' ');
				
				for (var i=0; i<containerCssClasses.length; i++) {
					if(jQuery.inArray(containerCssClasses[i],classArray)<0){
						var $close = jQuery('<a/>', {text:'  x ',class:'control_css_close'});
						$close.click(function() {
							$(this).parent().parent().parent().parent().parent().removeClass($(this).parent().text());
							$(this).parent().remove();
						});
						var $item = jQuery('<span class="control_dialog_css_item">'+containerCssClasses[i]+'</span>');
						$item.append($close);
						$containerCssClassesSpan.append($item);	
					}
				}
				
				$(this).next().children('.control_dialog_css_container').empty();
				$(this).next().children('.control_dialog_css_container').append($containerCssClassesSpan);
			});
	
			$dialog = jQuery('<div/>', {class:'reveal-modal control_container_dialog',style:'position:fixed;top:50px;overflow:scroll'});
			
			// hide on mobile
			$containerHideOnMobile = jQuery('<p class="control_dialog_action" />');
			$containerHideOnMobileCheck= jQuery('<input/>', {type:'checkbox'});
			$containerHideOnMobile.append($containerHideOnMobileCheck);
			$containerHideOnMobile.append('<span style="color:#909090">hide on phones</span>');
			
			// hide on tablet
			$containerHideOnTablet = jQuery('<p class="control_dialog_action" />');
			$containerHideOnTabletCheck= jQuery('<input/>', {type:'checkbox'});
			$containerHideOnTablet.append($containerHideOnTabletCheck);
			$containerHideOnTablet.append('<span style="color:#909090">hide on tablets</span>');
			
			// hide on desktop
			$containerHideOnDesktop = jQuery('<p class="control_dialog_action" />');
			$containerHideOnDesktopCheck= jQuery('<input/>', {type:'checkbox'});
			$containerHideOnDesktop.append($containerHideOnDesktopCheck);
			$containerHideOnDesktop.append('<span style="color:#909090">hide on desktops</span>');
			
			// manage the id field
			$containerId = jQuery('<input/>',{style:'width:80px'});
			
			$deleteContainer= jQuery('<span/>', {text:'Delete entire section'});
			
			$dialog.append('<p class="control_dialog_heading">Section Options<br/><hr/></p>');		
			$dialog.append(jQuery('<p class="control_dialog_action" />').append($deleteContainer));
			$dialog.append('<br/><span style="color:#909090;">Additional section CSS classes (optional)</span>');
			$dialog.append(jQuery('<div/>',{class:'control_dialog_css_container'}));
			$dialog.append('<br/><div style="color:#909090;">Section id value (optional)</div>');
			$dialog.append($containerId);
			$dialog.append("<div style='padding:4px 4px 7px' class='control_button_small nice small radius white button'>update</div>");
			$dialog.append('<br/><br/>');
			$dialog.append().append($containerHideOnMobile);
			$dialog.append().append($containerHideOnTablet);
			$dialog.append().append($containerHideOnDesktop);
			
			$closeButton = jQuery('<span/>',{class:'close-reveal-modal control_close_dialog'});
			$closeButton.append(jQuery('<span/>', { text: 'Close', class :' nice small radius blue button'}));
			$dialog.append($closeButton);
			$dialog.append('<a class="close-reveal-modal">&#215;</a>');
			
			$(this).prepend($dialog);
			$(this).prepend($options);	
			
			
			$deleteContainer.click(function () {
				$(this).parent().parent().parent().remove();
				removeContainerControls();
				addContainerControls();
				$('.reveal-modal-bg').remove();
			});
			
			$containerId.attr('value',$containerId.parent().parent().attr('id'));
			$containerId.change(function(){
				// add the id to the container
				$(this).parent().parent().attr('id',$(this).attr('value'));
			});
			
			// desktop check change
			$containerHideOnDesktopCheck.change(function() {
				if($(this).attr('checked')=='checked'){
					// add it
					$(this).parent().parent().parent().addClass('hide-on-desktops');
				}
				else {
					//remove it
					$(this).parent().parent().parent().removeClass('hide-on-desktops');
				}
			});
			//desktop check set the original value
			if ($containerHideOnDesktopCheck.parent().parent().parent().hasClass('hide-on-desktops')){
				$containerHideOnDesktopCheck.attr('checked','checked');
			}
			
			// mobile check change
			$containerHideOnMobileCheck.change(function() {
				if($(this).attr('checked')=='checked'){
					// add it
					$(this).parent().parent().parent().addClass('hide-on-phones');
				}
				else {
					//remove it
					$(this).parent().parent().parent().removeClass('hide-on-phones');
				}
			});
			//mobile check set the original value
			if ($containerHideOnMobileCheck.parent().parent().parent().hasClass('hide-on-phones')){
				$containerHideOnMobileCheck.attr('checked','checked');
			}
			
			// tablet check change
			$containerHideOnTabletCheck.change(function() {
				if($(this).attr('checked')=='checked'){
					// add it
					$(this).parent().parent().parent().addClass('hide-on-tablets');
				}
				else {
					//remove it
					$(this).parent().parent().parent().removeClass('hide-on-tablets');
				}
			});
			//desktop check set the original value
			if ($containerHideOnTabletCheck.parent().parent().parent().hasClass('hide-on-tablets')){
				$containerHideOnTabletCheck.attr('checked','checked');
			}
		});
		
	}
	
	function removeContainerActions() {
		$(".control_container_option").remove();
		$(".control_container_dialog").remove();
	}
	
	
	function addColumnActions() {
		$(".columns").each(function() {
			$options = new jQuery('<span/>',{text:'options',class:'control_column_option nice small radius black button'});
			$options.css('padding','4px 4px 7px');
			$options.click(function(e) {
				$(this).next().reveal();
				
				// add the css classes for this column
				$columnCssClassesSpan = jQuery('<span/>');
				$columnCssClassesAdd = jQuery('<input/>',{style:'width:80px'});
				$columnCssClassesAdd.keypress(function(event) {
					if ( event.which == 13 ) {
						event.preventDefault();
						// add the new css item to the column
						$(this).parent().parent().parent().parent().addClass($(this).attr('value'));
						// add the new css item to the dialog
						var $close = jQuery('<a/>', {text:'  x ',class:'control_css_close'});
						$close.click(function() {
							$(this).parent().parent().parent().parent().parent().removeClass($(this).parent().text());
							$(this).parent().remove();
						});
						var $item = jQuery('<span class="control_dialog_css_item">'+$(this).attr('value')+'</span>');
						$item.append($close);
						$(this).parent().append($item);
						$(this).attr('value','');
					}
				});
				
				$columnCssClassesSpan.append($columnCssClassesAdd);
				
				var columnCssClasses = $(this).parent().attr('class').split(' ');
				
				for (var i=0; i<columnCssClasses.length; i++) {
					if(jQuery.inArray(columnCssClasses[i],classArray)<0){
						var $close = jQuery('<a/>', {text:'  x ',class:'control_css_close'});
						$close.click(function() {
							$(this).parent().parent().parent().parent().parent().removeClass($(this).parent().text());
							$(this).parent().remove();
						});
						var $item = jQuery('<span class="control_dialog_css_item">'+columnCssClasses[i]+'</span>');
						$item.append($close);
						$columnCssClassesSpan.append($item);	
					}
				}
				
				$(this).next().children('.control_dialog_css_container').empty();
				$(this).next().children('.control_dialog_css_container').append($columnCssClassesSpan);
				
				// add the css classes for this row
				$rowCssClassesSpan = jQuery('<span/>');
				$rowCssClassesAdd = jQuery('<input/>',{style:'width:80px'});
				$rowCssClassesAdd.keypress(function(event) {
					if ( event.which == 13 ) {
						event.preventDefault();
						// add the new css item to the row
						$(this).parent().parent().parent().parent().parent().addClass($(this).attr('value'));
						// add the new css item to the dialog
						var $close = jQuery('<a/>', {text:'  x ',class:'control_css_close'});
						$close.click(function() {
							$(this).parent().parent().parent().parent().parent().parent().removeClass($(this).parent().text());
							$(this).parent().remove();
						});
						var $item = jQuery('<span class="control_dialog_css_item">'+$(this).attr('value')+'</span>');
						$item.append($close);
						$(this).parent().append($item);
						$(this).attr('value','');
					}
				});
				
				$rowCssClassesSpan.append($rowCssClassesAdd);
				
				
				var rowCssClasses = $(this).parent().parent().attr('class').split(' ');
				for (i=0; i<rowCssClasses.length; i++) {
					if(jQuery.inArray(rowCssClasses[i],classArray)<0){
						var $close = jQuery('<a/>', {text:'  x ',class:'control_css_close'});
						$close.click(function() {
							$(this).parent().parent().parent().parent().parent().parent().removeClass($(this).parent().text());
							$(this).parent().remove();
						});
						var $item = jQuery('<span class="control_dialog_css_item">'+rowCssClasses[i]+'</span>');
						$item.append($close);
						$rowCssClassesSpan.append($item);
					}
				}
				$(this).next().children('.control_dialog_css_row').empty();
				$(this).next().children('.control_dialog_css_row').append($rowCssClassesSpan);
			});
	
			$dialog = jQuery('<div/>', {class:'reveal-modal control_options_dialog',style:'position:fixed;top:50px'});
		
			$addSnippet= jQuery('<span/>', {text:'Add snippet', class :' nice small radius white button'});
			$removeSnippets= jQuery('<span/>', {text:'Remove all snippets'});
			$deleteColumn= jQuery('<span/>', {text:'Delete column'});
			$insertRow= jQuery('<span/>', {text:'Insert row in column'});
			$deleteRow= jQuery('<span/>', {text:'Delete entire row'});
			
			// hide on mobile
			$containerHideOnMobile = jQuery('<p class="control_dialog_action" />');
			$containerHideOnMobileCheck= jQuery('<input/>', {type:'checkbox'});
			$containerHideOnMobile.append($containerHideOnMobileCheck);
			$containerHideOnMobile.append('<span style="color:#909090">hide on phones</span>');
			
			// hide on tablet
			$containerHideOnTablet = jQuery('<p class="control_dialog_action" />');
			$containerHideOnTabletCheck= jQuery('<input/>', {type:'checkbox'});
			$containerHideOnTablet.append($containerHideOnTabletCheck);
			$containerHideOnTablet.append('<span style="color:#909090">hide on tablets</span>');
			
			// hide on desktop
			$containerHideOnDesktop = jQuery('<p class="control_dialog_action" />');
			$containerHideOnDesktopCheck= jQuery('<input/>', {type:'checkbox'});
			$containerHideOnDesktop.append($containerHideOnDesktopCheck);
			$containerHideOnDesktop.append('<span style="color:#909090">hide on desktops</span>');
			
			// manage the id fields
			$columnId = jQuery('<input/>',{style:'width:80px'});
			$rowId = jQuery('<input/>',{style:'width:80px'});
			
			// the select lists of snippets			
			$groupSelect = jQuery("<select/>");
			$snippetSelect = jQuery("<select/>");

			$groupContainer = jQuery("<span style='color:#909090'/>");
			$snippetContainer = jQuery("<span style='color:#909090'/>");
			
			$groupContainer.append("<label>Snippet group</label><br/>");
			$groupContainer.append($groupSelect);
			
			$snippetContainer.append("<label>Snippet name</label><br/>");
			$snippetContainer.append($snippetSelect);
			$snippetContainer.append($addSnippet);
			
			$.each(allSnippets.groups, function(index) {
				//var $option = jQuery("<option/>");
				$groupSelect.append("<option value='"+allSnippets.groups[index].id+"'>"+allSnippets.groups[index].name+"</option>");	
			});	
			
			$.each(allSnippets.groups[0].snippets, function(index) {
				//var $option = jQuery("<option/>");
				$snippetSelect.append("<option value='"+allSnippets.groups[0].snippets[index].id+"'>"
						+allSnippets.groups[0].snippets[index].name+"</option>");	
			});	
			
			// this all has to be done dynamically because of the mulitiple instances of dialogs (fucken painful)
			$groupSelect.change(function(){
				$span = $(this).parent().parent().next().children();
				$myGroup = jQuery($(this));
				$span.children().each(function(){
					if($(this).is("select")){
						$mySelect=jQuery($(this));
						$mySelect.empty();						
					}		
				});
			
				$.each(allSnippets.groups, function(index) {
					if(allSnippets.groups[index].id==$myGroup.val()){
						$.each(allSnippets.groups[index].snippets, function(snippetIndex){
							$mySelect.append(
									'<option value="' + allSnippets.groups[index].snippets[snippetIndex].id+ '">'
									+ allSnippets.groups[index].snippets[snippetIndex].name + '</option>');
						});
					}
				});
			});	
			

			$dialog.append('<p class="control_dialog_heading">Column Options<br/><hr/></p>');
			$dialog.append(jQuery('<span class="control_dialog_action" />').append($groupContainer));
			$dialog.append(jQuery('<span class="control_dialog_action" />').append($snippetContainer));
			$dialog.append(jQuery('<br/><br/><p class="control_dialog_action" />').append($removeSnippets));
			$dialog.append(jQuery('<p class="control_dialog_action" />').append($insertRow));
			$dialog.append(jQuery('<p class="control_dialog_action" />').append($deleteColumn));		
	
			$dialog.append('<br/><span style="color:#909090;">Additional column CSS classes</span>');
			$dialog.append(jQuery('<div/>',{class:'control_dialog_css_container'}));
			$dialog.append('<br/><div style="color:#909090">Column id value (optional)</div>');
			$dialog.append($columnId);
			$dialog.append("<div style='padding:5px 8px 4px;margin: 6px 5px' class='control_button_small nice small radius white button'>update</div><br/><br/>");
	
			$dialog.append().append($containerHideOnMobile);
			$dialog.append().append($containerHideOnTablet);
			$dialog.append().append($containerHideOnDesktop);
			
			$dialog.append('<br/><p class="control_dialog_heading">Row Options<br/><hr/></p>');
			$dialog.append(jQuery('<p class="control_dialog_action" />').append($deleteRow));
			$dialog.append('<br/><span style="color:#909090;">Additional row CSS classes</span>');
			$dialog.append(jQuery('<div/>',{class:'control_dialog_css_row'}));
			$dialog.append('<br/><div style="color:#909090;">Row id value (optional)</div>');
			$dialog.append($rowId);
			$dialog.append("<div style='padding:5px 8px 4px;margin: 6px 5px' class='control_button_small nice small radius white button'>update</div>");
	
			$closeButton = jQuery('<span/>',{class:'close-reveal-modal control_close_dialog'});
			$closeButton.append(jQuery('<span/>', { text: 'Close', class :' nice small radius blue button'}));
			$dialog.append($closeButton);
			$dialog.append('<a class="close-reveal-modal">&#215;</a>');
			
			$(this).prepend($dialog);
			$(this).prepend($options);
			
			
			
			$deleteColumn.click(function () {
				$(this).parent().parent().parent().remove();
				removeColumnSelectors();
				addColumnSelectors();
				$('.reveal-modal-bg').remove();
			});
			
			$deleteRow.click(function () {
				$(this).parent().parent().parent().parent().remove();
				removeRowControls();
				addRowControls();
				removeColumnSelectors();
				addColumnSelectors();
				removeContainerControls();
				addContainerControls();
				$('.reveal-modal-bg').remove();
			});
			
			$insertRow.click(function () {
				$(this).parent().parent().parent().append("<div class='row design_row'/>");
				removeRowControls();
				addRowControls();
				removeColumnSelectors();
				addColumnSelectors();
				$('.reveal-modal-bg').remove();
			});
			
			$addSnippet.click(function () {	
				//AM: add snippet.
				snippetName = $(this).prev().parent().children('select').children('option:selected').text();
				snippetId = $(this).prev().parent().children('select').val();
				var pageId = getURLParameter("pageId");
				var thisOne = $(this);
				$.ajax({
					  url: "/server/json/pageMarkup/addSnippet",
					  dataType: 'json',
					  data: {pageId: pageId,snippetId:snippetId },
					  success: function(result) {
						  	var uuid=result.uuid;
					        
						  	thisOne.parent().parent().parent().parent().append(jQuery("<snippet id='"+snippetId+"' uuid='"+uuid+"'><div class='design_snippet'>"+snippetName+"</div></snippet>"));		
							removeRowControls();
							addRowControls();
							$('.reveal-modal-bg').remove();
					        
					  },
					  error: function(jqXHR, textStatus, errorThrown){
						  alertPage("error","Whakatane'd",jqXHR, textStatus, errorThrown);
					  }
				});
				
				
			});
			
			$removeSnippets.click(function () {
				//AM: remove snippet.
				
				
				var snippets = $(this).parent().parent().parent().find("snippet");
				var pageId = getURLParameter("pageId");
				snippets.each(function(index){
					var uuidToDelete = $(this).attr("uuid");
					$.ajax({
						  url: "/server/json/pageMarkup/removeSnippet",
						  dataType: 'json',
						  data: {pageId: pageId,uuid:uuidToDelete },
						  success: function(result) {
							  	
						  },
						  error: function(jqXHR, textStatus, errorThrown){
							  alertPage("error","Whakatane'd",jqXHR, textStatus, errorThrown);
						  }
					});
				});
				
				$(this).parent().parent().parent().empty();
				
				
				removeRowControls();
				addRowControls();
				removeColumnSelectors();
				addColumnSelectors();	
				$('.reveal-modal-bg').remove();
			});
				
			$columnId.attr('value',$columnId.parent().parent().attr('id'));
			$columnId.change(function(){
				// add the id to the container
				$(this).parent().parent().attr('id',$(this).attr('value'));
			});

			$rowId.attr('value',$rowId.parent().parent().parent().attr('id'));
			$rowId.change(function(){
				// add the id to the container
				$(this).parent().parent().parent().attr('id',$(this).attr('value'));
			});
			
			// desktop check change
			$containerHideOnDesktopCheck.change(function() {
				if($(this).attr('checked')=='checked'){
					// add it
					$(this).parent().parent().parent().addClass('hide-on-desktops');
				}
				else {
					//remove it
					$(this).parent().parent().parent().removeClass('hide-on-desktops');
				}
			});
			//desktop check set the original value
			if ($containerHideOnDesktopCheck.parent().parent().parent().hasClass('hide-on-desktops')){
				$containerHideOnDesktopCheck.attr('checked','checked');
			}
			
			// mobile check change
			$containerHideOnMobileCheck.change(function() {
				if($(this).attr('checked')=='checked'){
					// add it
					$(this).parent().parent().parent().addClass('hide-on-phones');
				}
				else {
					//remove it
					$(this).parent().parent().parent().removeClass('hide-on-phones');
				}
			});
			//mobile check set the original value
			if ($containerHideOnMobileCheck.parent().parent().parent().hasClass('hide-on-phones')){
				$containerHideOnMobileCheck.attr('checked','checked');
			}
			
			// tablet check change
			$containerHideOnTabletCheck.change(function() {
				if($(this).attr('checked')=='checked'){
					// add it
					$(this).parent().parent().parent().addClass('hide-on-tablets');
				}
				else {
					//remove it
					$(this).parent().parent().parent().removeClass('hide-on-tablets');
				}
			});
			//desktop check set the original value
			if ($containerHideOnTabletCheck.parent().parent().parent().hasClass('hide-on-tablets')){
				$containerHideOnTabletCheck.attr('checked','checked');
			}
		});
	}
	
	function removeColumnActions() {
		$(".control_column_option").remove();
		$(".control_options_dialog").remove();
	}
	
	function addRowControls() {
		$afterLink = jQuery('<span/>', {text:'add row'});
		$afterLink.css('padding','4px 4px 7px');
		$afterLink.click(function () {
			//alert($(this).parent().parent().text());
			$(this).parent().parent().append("<div class='row design_row'/>");
			removeRowControls();
			addRowControls();
			removeColumnSelectors();
			addColumnSelectors();
			removeColumnActions();
			addColumnActions();
			removeContainerControls();
			addContainerControls();
		});
				
		$after = jQuery("<div class='control_add_row nice small radius white button'></div>");	
		$after.css('padding','4px 4px 7px');
		$after.append($afterLink);
		
		//$(".container").append($after);
		$(".row").after($after);
		
		//$(".columns").append($after);
		
		$beforeLink = new jQuery('<span/>', {text:'insert row'});
		$beforeLink.click(function () {
			$(this).parent().before("<div class='row design_row'/>");
			removeRowControls();
			addRowControls();
			removeColumnSelectors();
			addColumnSelectors();
			removeColumnActions();
			addColumnActions();
			removeContainerControls();
			addContainerControls();
		});
			
		$before = jQuery("<div class='control_insert_row nice small radius white button'/>");	
		$before.css('padding','4px 4px 7px');
		$before.append($beforeLink);
		$(".row").before($before);
		
		// remove the extra add rows
		$(".control_insert_row").prev('.control_add_row').remove();
		$(".control_add_row").prev('.control_add_row').remove();
		
		//update the page controls
		removeColumnSelectors();
		addColumnSelectors();
		removeColumnActions();
		addColumnActions();

	}
	
	function addContainerControls() {
		$afterLink = jQuery('<span/>', {text:'add section'});
		$afterLink.css('padding','4px 4px 7px');
		$afterLink.click(function () {
			//alert($(this).parent().parent().text());
			$(this).parent().parent().append("<div class='container design_container'/>");			
			removeColumnSelectors();
			addColumnSelectors();
			removeColumnActions();
			addColumnActions();
			removeContainerActions();
			addContainerActions();
			removeRowControls();
			addRowControls();
			removeContainerControls();
			addContainerControls();
		});
				
		$after = jQuery("<div class='control_add_container nice small radius white button'></div>");	
		$after.css('padding','4px 4px 7px');
		$after.append($afterLink);
		$(".container").after($after);
		$("#slash-server").append($after);
		
		$beforeLink = new jQuery('<span/>', {text:'insert section'});
		$beforeLink.click(function () {
			$(this).parent().before("<div class='container design_container'/>");
			removeColumnSelectors();
			addColumnSelectors();
			removeColumnActions();
			addColumnActions();
			removeContainerActions();
			addContainerActions();
			removeRowControls();
			addRowControls();
			removeContainerControls();
			addContainerControls();
		});
			
		$before = jQuery("<div class='control_insert_container nice small radius white button'/>");	
		$before.css('padding','4px 4px 7px');
		$before.append($beforeLink);
		$(".container").before($before);
		
		// remove the extra add section
		$(".control_insert_container").prev('.control_add_container').remove();
		
		// loop through each container and add and extra add row button to it.
		//TODO 
		
		
		$(".container").each(function() {
			
			if($(this).children('.row').length==0){
				$afterLink = jQuery('<span/>', {text:'add row'});
				$afterLink.css('padding','4px 4px 7px');
				$after = jQuery("<div class='control_add_row nice small radius white button'></div>");	
				$after.css('padding','4px 4px 7px');
				$after.append($afterLink);
				
				$(this).append($after);
				$afterLink.click(function () {
					$(this).parent().parent().append("<div class='row design_row'/>");
					removeRowControls();
					addRowControls();
					removeColumnSelectors();
					addColumnSelectors();
					removeColumnActions();
					addColumnActions();
					removeContainerControls();
					addContainerControls();
				});
						
				
			}
		});
		
		
	}
	
	function removeContainerControls() {
		$(".control_add_container").remove();
		$(".control_insert_container").remove();
	
		
	}
	
	function removeRowControls() {
		$(".control_add_row").remove();
		$(".control_insert_row").remove();
	}
	
	function addColumnSelectors(){
		var colCount=0;
		// check for rows with no children
		$(".row").each(function() {
			//alert("this="+$(this).text());
			if ($(this).hasClass('row') && $(this).children().size()==0 ) {
				addColumnControls($(this),12);
			}
		});
		
		// no we add all the column selectors that are missing
		$(".row").children().each(function() {

			for (var i=1;i<13;i++) {
				//alert('i='+i);
				if ($(this).hasClass('columns') && $(this).hasClass(classArray[i-1])) {
					colCount+=i;
					break;
				}
			}

			if ($(this).hasClass('columns') && $(this).next().size()==0 && colCount<12){
				addColumnControls($(this).parent(),12-colCount);
				colCount=0;
			}
			if (colCount==12) {colCount=0;}
			
		});
	}
	
	function removeColumnSelectors(){
		$(".control_column_selectors").remove();
	}
	
	function addColumnControls($target,size) {
		
		var overrideMargin="";
		if (size==12) { 
			overrideMargin="margin-left:0;"; 
		}
		
		var width = (100-(((12/size)-1)*4.4))/(12/size);
		var $allControls = jQuery("<div style='width:"+width+"%;"+overrideMargin+"' class='control_column_selectors'/>");
		
		var individualWidth=100/size;
		if (size==12){
			individualWidth=100/(size+1);
		}
		for(var i=1;i<size+1;i++) {
			colSize = i;
			$link = new jQuery("<span/>", { text: i ,href:'#',class:'nice small radius blue button'});
			$link.css('padding','4px 5px 7px');
			$link.click(function () {

				$(this).parent().parent().parent().append("<div class='"+classArray[$(this).text()-1]+" columns design_column'></div>");
				removeColumnSelectors();
				addColumnSelectors();
				removeColumnActions();
				addColumnActions();
			});
			var $controllerLinks = jQuery("<div class='control_column_link_container' style='width:"+individualWidth+"%'></div>");
			
			$controllerLinks.append($link);
			$allControls.append($controllerLinks);
		}
		
		if (size==12) { 
			$deleteRow = new jQuery('<span/>', { text: 'delete', class :' nice small radius red button'});
			$deleteRow.css('padding','4px 2px 7px');
			$deleteRow.click(function () {
				$(this).parent().parent().remove();
				removeRowControls();
				addRowControls();
				removeContainerControls();
				addContainerControls();
			});
			$allControls.append("<div class='control_row_action' style='width:"+100/(size+1)+"%'></div>").append($deleteRow);
		}
		$target.append($allControls);
	}
	
//	function addInstanceNumbers(){
//		$('snippet').each(function(index){
//			$(this).attr("instance",index+1);
//		});
//	}
		
	
	function hideControls (){
		if ($('.active-toggle').text()=='preview'){
			$('.row').removeClass('design_row');
			$('.columns').removeClass('design_column');
			$('.container').removeClass('design_container');
			removeColumnActions();
			removeContainerActions();
			removeRowControls();
			removeContainerControls();
			removeColumnSelectors();
			$('.inactive-toggle').addClass('toggle-temp');
			$('.inactive-toggle').removeClass('inactive-toggle');
			$('.active-toggle').addClass('inactive-toggle');
			$('.active-toggle').removeClass('active-toggle');
			$('.toggle-temp').addClass('active-toggle');
			$('.toggle-temp').removeClass('toggle-temp');
		}
	}
	
	function showControls (){	
		if ($('.active-toggle').text()=='design'){
			$('.container').addClass('design_container');
			$('.row').addClass('design_row');
			$('.columns').addClass('design_column');
			addColumnActions();
			addContainerActions();
			addRowControls();
			addContainerControls();
			addColumnSelectors();
			$('.inactive-toggle').addClass('toggle-temp');
			$('.inactive-toggle').removeClass('inactive-toggle');
			$('.active-toggle').addClass('inactive-toggle');
			$('.active-toggle').removeClass('active-toggle');
			$('.toggle-temp').addClass('active-toggle');
			$('.toggle-temp').removeClass('toggle-temp');
		}
	}
	
	function getURLParameter(name) {
	    return decodeURI(
	        (RegExp(name + '=' + '(.+?)(&|$)').exec(location.search)||[,null])[1]
	    );
	}
	
	function htmlEncode(value){
		  return $('<div/>').text(value).html();
	}

	function htmlDecode(value){
		  return $('<div/>').html(value).text();
	}
	 
	 function alertPage(type, message, jqXHR, textStatus, errorThrown){
			$('#slash_alert').empty(); 
			$('#slash_alert').append("<div class='alert-box "+type+"'>"+message+"</div>");
			$('#slash_alert').fadeTo("slow", 1).animate({opacity: 1.0}, 1500).fadeTo("slow", 0);
	}