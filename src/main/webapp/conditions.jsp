<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>panel demo</title>
    <link rel="stylesheet" href="http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css">
    <script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
    <script src="http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.js"></script>
    <script src="js/conditions.js"></script>
    <style>
        .panel-content {
            padding:15px;
        }
    </style>
</head>
<body>

<div data-role="page" id="page1">
    <div data-role="header" data-theme="b">
        <h1>Jax Local Conditions</h1>
        <a href="#defaultpanel" data-role="button" data-inline="true" data-icon="bars">Menu</a>
    </div>
    <div data-role="content" id="defaultview" class="epa-view">
        Local conditions around Jacksonville
    </div>
    <div data-role="content" id="fernandina" class="epa-view">
        <img src="http://www.surfline.com/dashboard/buoys/buoy_hvp_large_0_false_132.png"/>;
    </div>
    <div data-role="content" id="barpilots" class="epa-view">
        <img src="http://tidesonline.nos.noaa.gov/temp/8720218.png"/>;
    </div>

    <!-- defaultpanel  -->
    <div data-role="panel" id="defaultpanel" data-theme="b" data-display="reveal">
        <div class="panel-content">
            <h1>
                Observations
            </h1>

            <a href="#defaultview" data-rel="close" data-role="button" data-theme="c" data-icon="delete" data-inline="true">Close panel</a>
            <a href="#fernandina" data-role="button" data-theme="c" data-icon="bars" data-inline="true">Fernandina Buoy</a>
            <a href="#barpilots" data-role="button" data-theme="c" data-icon="bars" data-inline="true">Bar Pilots Dock Wind</a>

        </div><!-- /content wrapper for padding -->
        
    </div><!-- /defaultpanel -->
</div>

</body>
</html>
