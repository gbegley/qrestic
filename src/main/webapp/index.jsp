<!DOCTYPE html>
<html>
<head>
    <title>My Page</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="http://code.jquery.com/mobile/1.2.1/jquery.mobile-1.2.1.min.css" />
    <script src="http://code.jquery.com/jquery-1.8.3.min.js"></script>
    <script src="http://code.jquery.com/mobile/1.2.1/jquery.mobile-1.2.1.min.js"></script>
    <script src="js/mustache.js"></script>
    <script src="js/jquery.mobile.router.min.js"></script>
    <style>
        /*body {padding:50px;padding-left:250px;}*/
        /*.right-panel {float:right;position: absolute;top:50;right:50;width:220;padding:10px 5px;background: #eee;border:1px groove slategray;}*/
        /*.right-panel ul li {font:bold 18px Arial;padding:10px;}*/
        /*h1, h2, h3, h4 { color:#555; width:55%;padding-bottom: 8px;padding-left:15px; margin-bottom: 0px;}*/
        /*h1, h2, h3 { border-bottom: 2px inset #666699;}*/
        /*p {padding-left: 25px;color: #333;border: 1px solid #eee;padding:15px;font:normal 14px Helvetica; width:55%;}*/

        /*.hey { font: bold 26 "Helvetica Neue"; color:#333366;}*/

        .panel-content {
            padding:15px;
        }

    </style></head>
<body>




<div data-role="page">
    <div data-role="header" data-theme="b">
        <a href="#defaultpanel" data-role="button" data-inline="true" data-icon="bars">Default panel</a>
        <h1>
            RESTful Query Tools and API
        </h1>
    </div>
    <div data-role="content" data-theme="c">
<%--
        <ul data-role="listview">
            <li><a href="#models">Models</a></li>
            <li><a href="#views">Views</a></li>
            <li><a href="#controllers">Controllers</a></li>
            <li><a href="#data">Data Links</a></li>
        </ul>
--%>

        <!-- defaultpanel  -->
        <div data-role="panel" id="defaultpanel" data-theme="b">
            <div class="panel-content">
                <h3>Default panel options</h3>
                <p>This panel has all the default options: positioned on the left with the reveal display mode. The panel markup is <em>before</em> the header, content and footer in the source order.</p>
                <p>To close, click off the panel, swipe left or right, hit the Esc key, or use the button below:</p>
                <a href="#demo-links" data-rel="close" data-role="button" data-theme="c" data-icon="delete" data-inline="true">Close panel</a>
            </div><!-- /content wrapper for padding -->
        </div><!-- /defaultpanel -->
    </div>
</div><!-- /page -->

<div data-role="page" id="models">
    <h2>Models</h2>

    <h3>Input</h3>
    <p>Request made of the system</p>
    <h4>GET</h4><p>Access to results by identity, or by a parameter relation</p>

    <h4>POST</h4><p>GET + ???</p>

    <h4>PUT</h4><p>Add items</p>

    <h4>DELETE</h4><p>Remove items, either by identity or a parameter relation</p>

    <h3>Ouptut</h3>
    <p>Row Set, or system response</p>

</div>    
<div data-role="page" id="views">
    <div data-role="header" data-theme="b">
        <h1>Views</h1>
    </div>
    <ul data-role="listview">
        <li><a href="#csv">CSV</a></li>
        <li><a href="#json">JSON</a></li>
        <li><a href="#xml">XML</a></li>
    </ul>

</div>
<div data-role="page" id="controllers">
    <div data-role="header" data-theme="b">
        <h1>Controllers</h1>
    </div>


    <h3>Request</h3>
    <p><b>
        from <span class="hey">source</span>
        <br/>where <span class="hey">criteria</span>
        <br/>select <span class="hey">fields</span>
    </b></p>

    <h3>View</h3>
    <p>
        Process RowSet, generating content.
    </p>
    <h4>TSV</h4>
    <h4>DataGrid</h4>
    <h4>Graphical</h4>
    <p>Column and Bar Charts, Scatter Plot, Line</p>

    <div class="right-panel">
        <h2>Links</h2>
    </div>
</div>
<div data-role="page" id="data">
    <div data-role="header" data-theme="b">
        <h1>Data Links</h1>
    </div>
    <ul>
        <li><a target="_touch" href="/touch/examples">Touch</a></li>
        <li><a target="_touch" href="/demo/touch.jsp">My Touch</a></li>
        <li><a target="_data" href="/data/data/table/job">Jobs</a></li>
        <li><a target="_data" href="/data/data/table/entity">Entity</a></li>
        <li><a target="_data" href="/data/data/table/matrix">Matrix</a></li>
        <li><a target="_data" href="conditions.jsp">Conditions</a></li>
    </ul>
</div>


</body>
</html>