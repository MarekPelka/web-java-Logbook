<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>LogViewer</title>
	<link rel="icon" href="logBlack.png">

    <!-- Bootstrap & CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/custom.css" rel="stylesheet">
	<link href="css/dataTables.bootstrap.min.css" type="text/css" rel="stylesheet">
	
	<!-- JavaScript Essential-->
	<script src="js/tables.js" ></script>
    <script src="js/jquery-3.1.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
	<script type="text/javascript" charset="utf8" src="js/jquery.dataTables.min.js"></script>
	<script type="text/javascript" charset="utf8" src="js/dataTables.bootstrap.min.js"></script>
	<!-- JavaScript Optional-->
	<script src="js/ie10-viewport-bug-workaround.js"></script>
	<script src="js/holder.min.js"></script>

</head>

<body  data-target=".navbar">
    <!-- Navigation -->
    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="index.php">
                	<span class="glyphicon glyphicon-eye-open"></span> 
                	LogViewer
                </a>
            </div>
            <div class="collapse navbar-collapse" id="navbar">
                <ul class="nav navbar-nav">
                    <li class="active">
                        <a href="#">All </a>
                    </li>
					<li >
						<a href="#">Session </a>
					</li>
					<li>
                        <a href="about.html">About </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
	
	<div id="viewerContainer" class="container" style="margin-top: 10px;">     
	<!-- Main Component of Website-->
	<!--<button type="button" class="btn btn-info" id="showButton">Show</button>-->
	  <table id="viewer" class="table table-hover" cellspacing="100">
		
	  </table>
	<button type="button" class="btn btn-danger" id="dB">Delete</button>	
	</div>
	
	<footer>  
		<div class="small-print">
			<div class="container">
				<p>Politechnika Warszawska 2016</p>
			</div>
		</div>
	</footer>
	
	<script type="text/javascript">
		$(document).ready(function() {
			ready();
		});
	</script>
	
</body>

</html>
