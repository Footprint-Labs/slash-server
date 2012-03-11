listTemplates();

function listTemplates() {
	$('#template').empty();
	$('#template').append("<select id='templateSelect'/>");
	$('#templateSelect').change(function() {
		showPageTemplate($('#templateSelect').val());
	});
	$.ajax({
		url : "/server/json/pagetemplate",
		dataType : 'json',
		type : "GET",
		success : function(result) {
			// alertPage("success","Saved...");

			$.each(result, function(index) {
				$('#templateSelect').append(
						'<option value="' + result[index].id+ '">'
						+ result[index].name + '</option>');
			});
			// load the details of the current template
			showPageTemplate($('#templateSelect').val());

		},
		error : function(jqXHR, textStatus, errorThrown) {
			alertPage("error", "Whakatane'd", jqXHR, textStatus, errorThrown);
		}
	});

}

function showPageTemplate(pageTemplateId) {
	var pageTemplate = "";
	$('#templateDetails').empty();
	$('#templateDetails').append("<label>Name</label><br/><input class='input-text' type='text' id='templateName'/>");
	$('#templateDetails').append("<label>Template markup<label><textarea rows='20' cols='50' id='templateContent'/>");
	// fetch the page details
	if(pageTemplateId){
		$.ajax({
			url : "/server/json/pagetemplate/"+pageTemplateId,
			type: "GET",
			dataType : 'json',
			success : function(result) {
				pageTemplate = result;
				// alertPage("success","Saved...");
				$('#templateName').val(pageTemplate.name);
				$('#templateContent').val(pageTemplate.content);
				
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alertPage("error", "Whakatane'd", jqXHR, textStatus, errorThrown);
			}
		});
	}

	var $buttons = jQuery('<div/>');
	$('#templateDetails').append($buttons);
	var $save = jQuery('<span/>', {
		text : 'Save',
		class : 'slash-action nice small radius blue button'
	});
	var $cancel = jQuery('<span/>', {
		text : 'Cancel',
		class : 'slash-action nice small radius white button'
	});
	var $delete = jQuery('<a/>', {
		text : 'Delete',
		class : 'slash-action',
		'href' : '#'
	});
	var $new = jQuery('<a/>', {
		text : 'New',
		class : 'slash-action',
		'href' : '#'
	});

	$buttons.append($new);
	$buttons.append($delete);
	$buttons.append($save);
	$buttons.append($cancel);
	
	
	
	$save.click(function() {
		var method="PUT";
		var url = "/server/json/pagetemplate/";
		if(!pageTemplate.id){
			// its a create
			method="POST";
		}
		else {
			url+= pageTemplate.id;
			url+="?name="+htmlEncode($('#templateName').val());
			url+="&content="+htmlEncode($('#templateContent').val());
		}
		
		//url += "?name="+$('#templateName').val()+"&content=html";
		
		$.ajax({
			url : url,
			type: method,
			dataType : 'json',
			contentType: 'application/json',
			data: {name: $('#templateName').val(),
					content: $('#templateContent').val()},
			success : function(result) {
				alertPage("success", "Saved...");
				listTemplates();
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alertPage("error", "Whakatane'd", jqXHR, textStatus,
						errorThrown);
			}
		});
	});
	$cancel.click(function() {
		showPageTemplate(pageTemplate.id);
	});
	$delete.click(function() {
		$.ajax({
			url : "/server/json/pagetemplate/"+pageTemplate.id,
			type : "DELETE",
			dataType : 'json',
			success : function(result) {
				alertPage("warning", "Page deleted...");
				listTemplates();
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alertPage("error", "Whakatane'd", jqXHR, textStatus,
						errorThrown);
			}
		});
	});
	
	$new.click(function() {
		$.ajax({
			url : "/server/json/pagetemplate/",
			type: "POST",
			dataType : 'json',
			data : {
				name : "new page template",
				content : "<html>\n <head>\n\n </head>\n <body>\n\n  <slash-server/>\n\n </body>\n</html>"
			},
			success : function(result) {
				alertPage("success", "Page created...");
				listTemplates();
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alertPage("error", "Whakatane'd", jqXHR, textStatus,
						errorThrown);
			}
		});
	});
}
