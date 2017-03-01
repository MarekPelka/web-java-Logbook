<?php
include("connect.php");

$connection = @new mysqli($host, $db_user, $db_password, $db_name);
$connection->set_charset("utf8");	
	if ($connection->connect_errno!=0)
	{
		echo "Error: ".$connection->connect_errno;
	}
	else
	{
		if (isset($_POST['operation'])) {
			$operation = $_POST['operation'];
		}else{
			$operation = 'nothing';
		}
	
		if($operation== 'nothing'){	
				if ($resultList = @$connection->query(sprintf("SELECT * FROM control;")))
				{
					echo 
					"<thead>
						<tr>
							<th>Id</th>
							<th>Name</th>
							<th>Type</th>
							<th>Source</th>
							<th>Starting date</th>
							<th>Endig date</th>
							<th>Edited</th>
						</tr>
					</thead>
					<tfoot>
						<tr>
							<th>Id</th>
							<th>Name</th>
							<th>Type</th>
							<th>Source</th>
							<th>Starting date</th>
							<th>Endig date</th>
							<th>Edited</th>
						</tr>
					</tfoot>";
					
					while($row = $resultList->fetch_assoc()){
						
						$id = $row['id'];
						$tableNameHref = $row['tableName'];
						$typeName = $row['typeName'];
						$tableName = str_replace("_", " ", $tableNameHref);
						$source = $row['source'];
						$dateStart = $row['dateStart'];
						$dateStop = $row['dateStop'];
						if($dateStop == null)
							$dateStop = "In progress";
						$wasEdited = $row['edited'];
						if($wasEdited == 0)
							$wasEdited = "No";
						else
							$wasEdited = "Yes";
						echo "<tr>
								<td>$id</td>
								<td><a href=tables.php?table=$tableNameHref>$tableName<a></td>
								<td>$typeName</td>
								<td>$source</td>
								<td>$dateStart</td>
								<td>$dateStop</td>
								<td>$wasEdited</td>
							</tr>";
					}
				}	
		}else if($operation == 'remove'){
			echo $_POST['queryControl']."; ".$_POST['queryDrop'];
			$result = @$connection->query(sprintf($_POST['queryControl']));
			$result = @$connection->query(sprintf($_POST['queryDrop']));
	}
		$connection->close();
	}
?>