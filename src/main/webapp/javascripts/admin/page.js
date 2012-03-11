 
 function showPage(page){
	 if(!page){
		 page= {name:"",url:"" };
	 }
	$('#page').empty(); 
	$('#page').append('');
	$('#page').append('<form class="nice"><label>Name</label><span><input type="text" id="name" class="input-text large" value="'+page.name+'"></span></form>');
	$('#page').append('<form class="nice"><label>Path <a href="/server/page/'+page.url+'">/server/page/'+page.url+'</a></label><span><input type="text" id="url" class="input-text large" value="'+page.url+'"></span></form>');
	$('#page').append('<label>Page Template</label><span>');
	$('#page').append('</form>');
	$templateSelect = jQuery("<select>");
	$.each(templates,function(index){
		if(page.templateId==templates[index].id){
			$templateSelect.append('<option selected="selected" value='+templates[index].id+'>'+
				templates[index].name+'</option>');
		}
		else {
			$templateSelect.append('<option value='+templates[index].id+'>'+
				templates[index].name+'</option>');
		}
	});
	
	$('#page').append($templateSelect);
	$('#page').append("</span>");
	
	
	var $layout = jQuery('<div/>');
	$('#page').append($layout);
	var $layoutButton = jQuery('<a/>', { text: 'Edit page layout', class :'slash-action nice small radius white button',href:'page_layout_manager?pageId='+page.id,target:'_blank'} );
	if (page.id){
		$layout.append($layoutButton);
	}
	var $buttons = jQuery('<div/>');
	$('#page').append($buttons);
	var $cancel = jQuery('<span/>', { text: 'Cancel', class :'slash-action nice small radius white button'});
	var $save = jQuery('<span/>', { text: 'Save', class :'slash-action nice small radius blue button'});
	var $delete = jQuery('<a/>', { text: 'Delete', class :'slash-action','href' :'#'}); 	
	var $new = jQuery('<a/>', { text: 'New', class :'slash-action','href' :'#'});
	
	$buttons.append($new);
	$buttons.append($delete);
	$buttons.append($save);
	$buttons.append($cancel);
	
	$save.click(function(){
		
		$.ajax({
			  url: "/server/json/page/update",
			  dataType: 'json',
			  data: {id: page.id ,url:$('#url').val(),name:$('#name').val(),templateId:$templateSelect.val()}, //,markup: page.markup
			  success: function(result) {
				  
				    alertPage("success","Saved...");
			        if(page.name!=$('#name').attr('value')){
			        	// name has changed so update tree
			        	$('body').trigger('pageNameUpdate', {'pageId':page.id,'name':$('#name').attr('value')});
			        }
			  },
			  error: function(jqXHR, textStatus, errorThrown){
				  alertPage("error","Whakatane'd",jqXHR, textStatus, errorThrown);
			  }
			});
	});
	$cancel.click(function(){
		showPage(page);
	});
	$delete.click(function(){
		$.ajax({
			  url: "/server/json/page/delete",
			  dataType: 'json',
			  data: {id: page.id},
			  success: function(result) {
				  alertPage("warning","Page deleted...");
				  // update the tree
			        $('body').trigger('deletePage', {'pageId':page.id});
			        // empty out the fields in case it the last one
			        $('#name').val("");
			        $('#url').val("");
			  },
			  error: function(jqXHR, textStatus, errorThrown){
				  alertPage("error","Whakatane'd",jqXHR, textStatus, errorThrown);
			  }
			});
	});
	$new.click(function(){
		
		 $('body').trigger('currentSitemapRequest', {});

	});
	
 }
 

 
 $('body').bind('pageActivate', function(event,data) {
	 if(!data.pageId){
		 showPage("");
	 } else {
		 $.ajax({
			  url: "/server/json/page/viewcomplete",
			  dataType: 'json',
			  data: {id: data.pageId },
			  success: function(result) {
				  	
			        showPage(result);
			  },
			  error: function(jqXHR, textStatus, errorThrown){
				  alertPage("error","Whakatane'd",jqXHR, textStatus, errorThrown);
			  }
		 });
	 }
 });
 
 $('body').bind('currentSitemapResponse', function(event,data) {
		currentSitemapId=data.id;
		$.ajax({
			  url: "/server/json/page/create",
			  dataType: 'json',
			  data: {url:'',name:'New page','sitemapId':currentSitemapId},
			  success: function(result) {
				   alertPage("success","New page created...");
			        $('body').trigger('newPage', {'pageId':result.id,'name':'New page'});
			             
			  },
			  error: function(jqXHR, textStatus, errorThrown){
				  alertPage("error","Whakatane'd",jqXHR, textStatus, errorThrown);
			  }
		});
});