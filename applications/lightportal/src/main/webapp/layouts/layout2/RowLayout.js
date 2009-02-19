Ext.layout.Row = Ext.extend(Ext.layout.FitLayout, {
    renderItem : function(c){
        Ext.layout.Row.superclass.renderItem.apply(this, arguments);
        c.header.addClass('x-accordion-hd');
    }
});
Ext.Container.LAYOUTS['row'] = Ext.layout.Row;
