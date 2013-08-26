Ext.define("Demo.store.Views", {
    extend: 'Ext.data.Store',
    requires: ['Demo.model.View'],
    alias: 'store.View',
    config: {
        model: 'Demo.model.View',
        sorters: 'name',
//        grouper: function(record) {
//            return record.get('name')[0];
//        },
        data: [
            {id:0,text:'View 1',description:'View 1 Description'},
            {id:1,text:'View 2',description:'View 2 Description'}
        ]
    }
});
