var snippet_api={};

snippet_api.ajax = function(url,data,callback,method){

	if(!method){
		method="POST";
	}

	if(!data){
		data={};
	}
	

	$.ajax({
		url:url,
		type:method,
		data:data,
		success:callback
	});

};


(function( $ ){
	superFn = $.ajax;

	$.ajax = function(settings) {

		
		if(!settings.data){
			settings.data={};
		}
		settings.data.pipeTo = settings.url;
		settings.url = "/server/ajax";

		superFn.apply(this,[settings]);

	};
})( jQuery );