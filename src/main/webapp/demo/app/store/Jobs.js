Ext.define("Demo.store.Jobs", {
    extend: 'Ext.data.Store',
    requires: ['Demo.model.Job'],
    alias: 'store.Jobs',
    config: {
        model: 'Demo.model.Job',
        sorters: 'name',
//        grouper: function(record) {
//            return record.get('name')[0];
//        },
        data: [
            {id:0,name:'Job 1'},
            {id:1,name:'Job 2'}
        ]
    }
});
