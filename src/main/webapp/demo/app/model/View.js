Ext.define('Demo.model.View', {
    extend: 'Ext.data.Model',

    config: {
        fields: [
            {name: 'id',          type: 'string'},
            {name: 'text',        type: 'string'},
            {name: 'description',        type: 'string'},
            {name: 'view',        type: 'string'}
        ]
    }
});
