function alertPage(type, message, jqXHR, textStatus, errorThrown){
			$('#slash_alert').empty(); 
			$('#slash_alert').append("<div class='alert-box "+type+"'>"+message+"</div>");
			$('#slash_alert').fadeTo("slow", 1).animate({opacity: 1.0}, 1500).fadeTo("slow", 0);
}

$(function(){
	var Item = Backbone.Model.extend({
    	urlRoot: '/server/json/pagetemplate',
    	defaults: {
    		"name" : "new page template",
			"content" : "<html>\n <head>\n\n </head>\n <body>\n\n  <slash-server/>\n\n </body>\n</html>"
    	}
    });
    var Items = Backbone.Collection.extend({
        url: '/server/json/pagetemplate',
        model: Item
    });
    
    // special function for page templates
    var DetailView = Backbone.View.extend({
    	el:$("#itemDetails"),
    	initialize: function(){
            _.bindAll(this, 'render','showDetail','newItem','deleteItem');
            this.model.fetch({error:function(model,response){alertPage("error","Whakatane'd",null,null,null);}});
            this.model.bind('change', this.showDetail);
            $(this.el).off();
            $(this.el).bind('newItem', this.newItem);
            $(this.el).bind('deleteItem', this.deleteItem);
        },
        render: function(){
            $(this.el).attr('value', this.model.get('id')).html(this.model.get('name'));
            return this;
        },
        showDetail : function(){
        	$(this.el).empty();
        	$(this.el).append('<label>Name</label><div class="details">'+this.model.get('name')+'</div>');
        	$markup = jQuery('<pre class="brush: xml;"/>');
        	$markup.text(this.model.get('content'));
        	$markupContainer = jQuery('<div class="details"/>');
        	$markupContainer.append($markup);
        	$(this.el).append('<label>Template markup</label>');
        	$(this.el).append($markupContainer);
        	
        	SyntaxHighlighter.highlight();
        },
        newItem : function(){
        	this.model = new Item();
        	this.model.save();
        	// add the new Item to the collection
        	$("#itemSelect").trigger('addItem',this.model);
        },
        deleteItem : function(){
        	this.model.destroy();
        	//  remove the item from the collection
        	
        }
    });

	
    // generic function
    var ItemView = Backbone.View.extend({
        tagName: "option",

        initialize: function(){
            _.bindAll(this, 'render');
            
        },
        render: function(){
            $(this.el).attr('value', this.model.get('id')).html(this.model.get('name'));
            return this;
        }
    });
    
    // generic function
    var ItemsView = Backbone.View.extend({
    	events: {
            "change": "changeSelected"
        },
        initialize: function(){
            _.bindAll(this, 'addOne', 'addAll', 'changeSelected');
            this.collection.bind('reset', this.addAll);
            $(this.el).off();
            $(this.el).on('addItem', this.addOne);
        },
        addOne: function(item){
        	//alert(item.attributes.name);
            $(this.el).append(new ItemView({ model: item }).render().el);
        },
        addAll: function(){
        	var firstItem = true;
            this.collection.each(this.addOne);

            // now let get the details for the first one
            var item = new Item({id:this.collection.at(0).id});
            new DetailView({model:item});
        },
        changeSelected : function(){
        	//create a new item with the selected id and send it to the detail view
        	var item = new Item({id:$(this.el).val()});
        	new DetailView({model:item});
        }
    });
    
 // generic function
    // generic function
    var ToolbarView = Backbone.View.extend({
    	events: {
            "click #toolbarNew": "newItem",
            "click #toolbarDelete": "deleteItem",
            "click #toolbarEdit": "editItem"
        },
        initialize: function(){
            _.bindAll(this,'render' ,'newItem','deleteItem','editItem');
            this.render();
        },
        render : function(){
        	$(this.el).append("<span class='toolbarButton nice small radius white button' id='toolbarNew'>New</span>");
            $(this.el).append("<span class='toolbarButton nice small radius white button' id='toolbarDelete'>Delete</span>");
            $(this.el).append("<span class='toolbarButton nice small radius white button' id='toolbarEdit'>Edit</span>");
        },
        newItem : function(){
        	$('#itemDetails').trigger('newItem');
        },
        deleteItem : function(){
        	$('#itemDetails').trigger('deleteItem');
        },
        editItem : function(){
        	$('#itemDetails').trigger('editItem');
        }
    });

    
    // lets get this party started
    var items = new Items();
    new ItemsView({el: $("#itemSelect"), collection: items});
    items.fetch({error:function(model,response){alertPage("error","Whakatane'd",null,null,null);}});
    new ToolbarView({el: $("#toolbar")});
});



