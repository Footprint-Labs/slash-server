function Template(data) {
	this.id = ko.observable(data.id);
    this.name = ko.observable(data.name);
    //this.content = ko.observable(data.content);
}

function TemplateViewModel() {
    // Data
    var self = this;
    self.templates = ko.observableArray([]);
    self.newTemplateName = ko.observable();
    self.newTemplateId = ko.observable();
   // self.newContent = ko.observable();
    
    $.ajax("/server/json/pagetemplate/", {
        type: "GET", contentType: "application/json",
        success: function(result) { 
        	$.each(result, function(index) {
        		self.templates.push(new Template({
        			id :result[index].id,
        			name:result[index].name}
        			));
	    	});
        }
    });

    // Operations
    self.addTemplate = function() {
        self.templates.push(new Template({ name: "new page template" }));
        self.newTemplateName("");
    };
    
    self.removeTemplate = function(Template) { self.templates.remove(template) };
    
    self.save = function() {
        $.ajax("/tasks", {
            data: ko.toJSON({ templates: self.templates }),
            type: "post", contentType: "application/json",
            success: function(result) { alert(result) }
        });
    }; 
}

ko.applyBindings(new TemplateViewModel());