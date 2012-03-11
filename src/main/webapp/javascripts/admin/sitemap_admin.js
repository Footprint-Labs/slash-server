listSitemaps();

function listSitemaps() {
	$('#sitemap').empty();
	$('#sitemap').append("<select id='sitemapSelect'/>");
	$('#sitemapSelect').change(function() {
		showSitemap($('#sitemapSelect').val());
	});
	$.ajax({
		url : "/server/json/sitemap/list",
		dataType : 'json',

		success : function(result) {
			// alertPage("success","Saved...");

			$.each(result, function(index) {
				$('#sitemapSelect').append(
						'<option value="' + result[index].sitemapId+ '">'
						+ result[index].name + '</option>');
			});
			// load the details of the current sitemaps
			showSitemap($('#sitemapSelect').val());

		},
		error : function(jqXHR, textStatus, errorThrown) {
			alertPage("error", "Whakatane'd", jqXHR, textStatus, errorThrown);
		}
	});

}

function showSitemap(sitemapId) {
	var sitemap = "";
	$('#sitemapDetails').empty();
	$('#sitemapDetails').append("<label>Name</label><br/><input class='input-text' type='text' id='sitemapName'/>");
	// fetch the page details
	if(sitemapId){
		$.ajax({
			url : "/server/json/sitemap/view",
			dataType : 'json',
			data : {id:sitemapId},
			success : function(result) {
				sitemap = result;
				// alertPage("success","Saved...");
				$('#sitemapName').val(sitemap.name);
				
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alertPage("error", "Whakatane'd", jqXHR, textStatus, errorThrown);
			}
		});
	}

	var $buttons = jQuery('<div/>');
	$('#sitemapDetails').append($buttons);
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
		var action="/server/json/sitemap/update";
		if(!sitemap.id){
			action="/server/json/sitemap/create";
		}
		$.ajax({
			url : action,
			dataType : 'json',
			data : {
				id : sitemap.id,
				name : $('#sitemapName').val()
			},
			success : function(result) {
				alertPage("success", "Saved...");
				listSitemaps();
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
				sitemapId : sitemap.id
			},
			success : function(result) {
				alertPage("warning", "Page deleted...");
				listSitemaps();
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alertPage("error", "Whakatane'd", jqXHR, textStatus,
						errorThrown);
			}
		});
	});
	
	$new.click(function() {
		$.ajax({
			url : "/server/json/sitemap/create",
			dataType : 'json',
			data : {
				
				name : "new sitemap"
			},
			success : function(result) {
				alertPage("success", "Page created...");
				listSitemaps();
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alertPage("error", "Whakatane'd", jqXHR, textStatus,
						errorThrown);
			}
		});
	});
}
