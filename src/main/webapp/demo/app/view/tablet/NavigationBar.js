Ext.define('Demo.view.tablet.NavigationBar', {
    extend: 'Ext.TitleBar',
    xtype: 'tabletnavigationbar',

    config: {
        ui: 'dark'
    },

    platformConfig: [{
        platform: 'blackberry',
        ui: 'light'
    }]
});
