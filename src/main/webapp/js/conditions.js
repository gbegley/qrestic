$( document ).on( "pageinit", function() {

    console.log('conditions init');
    var page = $( this );

    // global navmenu panel
    $( ".jqm-navmenu-link" ).on( "click", function() {
        page.find( ".jqm-navmenu-panel" ).panel( "open" );
    });

    $( ".jqm-navmenu-link" ).on( "click", function() {
        page.find( ".jqm-navmenu-panel" ).panel( "open" );
    });

    $(".epa-view").hide();
    $("#defaultview").show();

    $("a[href='#barpilots']").on('click',function(){
        $(".epa-view").hide();
        $('#barpilots').show();
    });
    $("a[href='#fernandina']").on('click',function(){
        $(".epa-view").hide();
        $("#fernandina").show();
    });

    $("[data-role='header']").attr("data-position","fixed");



});