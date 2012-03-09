listSnippets();

function listSnippets() {
	var group = "";
	$('#groups').empty();
	$('#groups').append("<label>Snippet group</label><br/><select id='groupSelect'/>");
	$('#groups').append("<label>Group name</label><br/><input class='input-text' type='text' id='groupName'/>");
	$('#groupSelect').change(function() {
		showSnippets($('#groupSelect').val());
		$("#groupName").val($("#groupSelect option:selected").text());
	});
	$.ajax({
		url : "/server/json/snippetgroup/list",
		dataType : 'json',

		success : function(result) {
			group=result;

			$.each(result.groups, function(index) {
				$('#groupSelect').append(
						'<option value="' + result.groups[index].id+ '">'
						+ result.groups[index].name + '</option>');
			});
			// load the details of the current groups
			showSnippets($('#groupSelect').val());
			$("#groupName").val($("#groupSelect option:selected").text());

		},
		error : function(jqXHR, textStatus, errorThrown) {
			alertPage("error", "Whakatane'd", jqXHR, textStatus, errorThrown);
		}
	});
	
	var $buttons = jQuery('<div/>');
	$('#groups').append($buttons);
	var $save = jQuery('<span/>', {
		text : 'Save',
		class : 'slash-action nice small radius blue button'
	});
	var $cancel = jQuery('<span/>', {
		text : 'Cancel',
		class : 'slash-action nice small radius red button'
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
	$buttons.append($cancel);
	$buttons.append($save);
	
	
	$save.click(function() {
		var action="/server/json/snippetgroup/update";
		$.ajax({
			url : action,
			dataType : 'json',
			data : {
				id : $('#groupSelect').val(),
				name : $('#groupName').val()
			},
			success : function(result) {
				alertPage("success", "Saved...");
				listSnippets();
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alertPage("error", "Whakatane'd", jqXHR, textStatus,
						errorThrown);
			}
		});
	});
	$cancel.click(function() {
		showSnippets(group.id);
	});
	$delete.click(function() {
		$.ajax({
			url : "/server/json/snippetgroup/delete",
			dataType : 'json',
			data : {
				id : $('#groupSelect').val()
			},
			success : function(result) {
				alertPage("warning", "Group deleted...");
				listSnippets();
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alertPage("error", "Whakatane'd", jqXHR, textStatus,
						errorThrown);
			}
		});
	});
	
	$new.click(function() {
		$.ajax({
			url : "/server/json/snippetgroup/create",
			dataType : 'json',
			data : {
				name : "new snippet group"
			},
			success : function(result) {
				alertPage("success", "Group created...");
				listSnippets();
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alertPage("error", "Whakatane'd", jqXHR, textStatus,
						errorThrown);
			}
		});
	});
}

function showSnippets(groupId) {
	var snippets = "";

	$('#snippets').empty();
	$('#snippets').append("<label>Snippet</label><br/><select id='snippetSelect'/>");
	$('#snippets').append("<label>Snippet name</label><br/><input class='input-text' type='text' id='snippetName'/>");
	$('#snippets').append("<label>URL</label><br/><input class='input-text' type='text' id='snippetUrl'/>");
	$('#snippetSelect').change(function() {
		$.each(snippets, function(index) {
			if(snippets[index].id==$('#snippetSelect').val()){
				$('#snippetName').val(snippets[index].name);
				$('#snippetUrl').val(snippets[index].url);
			}
		});
		
	});
	// fetch the page details
	if(groupId){
		$.ajax({
			url : "/server/json/snippet/allingroup",
			dataType : 'json',
			data : {groupId:groupId},
			success : function(result) {
				snippets = result.snippets;
				$.each(snippets, function(index) {
					$('#snippetSelect').append(
							'<option value="' + snippets[index].id+ '">'
							+ snippets[index].name + '</option>');
				});
				if(snippets.length>0){
					$('#snippetName').val(snippets[0].name);
					$('#snippetUrl').val(snippets[0].url);
				}	
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alertPage("error", "Whakatane'd", jqXHR, textStatus, errorThrown);
			}
		});
		
	}
	
	var $buttons = jQuery('<div/>');
	$('#snippets').append($buttons);
	var $save = jQuery('<span/>', {
		text : 'Save',
		class : 'slash-action nice small radius blue button'
	});
	var $cancel = jQuery('<span/>', {
		text : 'Cancel',
		class : 'slash-action nice small radius red button'
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
	$buttons.append($cancel);
	$buttons.append($save);
	
	
	$save.click(function() {
		var action="/server/json/snippet/update";
		
		$.ajax({
			url : action,
			dataType : 'json',
			data : {
				id : $('#snippetSelect').val(),
				name : $('#snippetName').val(),
				url : $('#snippetUrl').val(),
				snippetGroupId : $('#groupSelect').val()
			},
			success : function(result) {
				alertPage("success", "Saved...");
				showSnippets($('#groupSelect').val());
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alertPage("error", "Whakatane'd", jqXHR, textStatus,
						errorThrown);
			}
		});
	});
	$cancel.click(function() {
		showSitemap(sitemap.id);
	});
	$delete.click(function() {
		$.ajax({
			url : "/server/json/sitemap/delete",
			dataType : 'json',
			data : {
				id : groupId
			},
			success : function(result) {
				alertPage("warning", "Page deleted...");
				showSnippets($('#groupSelect').val());
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alertPage("error", "Whakatane'd", jqXHR, textStatus,
						errorThrown);
			}
		});
	});
	
	$new.click(function() {
		$.ajax({
			url : "/server/json/snippet/create",
			dataType : 'json',
			data : {
				name : "new snippet",
				snippetGroupId : groupId,
				url : "http://"
			},
			success : function(result) {
				alertPage("success", "Page created...");
				showSnippets($('#groupSelect').val());
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alertPage("error", "Whakatane'd", jqXHR, textStatus,
						errorThrown);
			}
		});
	});
}
