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
		
		if (isset($_POST['tableName'])) {
			$tableName = $_POST['tableName'];
		}else{
			$tableName = 'control';
		}
	
		if($operation== 'nothing'){	
				$sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '".$tableName."'";
			if ($resultList = @$connection->query(sprintf($sql)))
				{
					$i = 0;
					while($row = $resultList->fetch_assoc()){
						
						$columnName["$i"] = $row['COLUMN_NAME'];
						$i++;
					}
				}
				if ($resultList = @$connection->query(sprintf("SELECT * FROM $tableName;")))
				{
					echo "<thead><tr>";
					for ($x = 0; $x < count($columnName); $x++) {
						echo "<th>".$columnName[$x]."</th>";
					} 
					echo "</tr></thead>";
					echo "<tfoot><tr>";
					for ($x = 0; $x < count($columnName); $x++) {
						echo "<th>".$columnName[$x]."</th>";
					} 
					echo "</tr></tfoot>";
					
					while($row = $resultList->fetch_assoc()){
						echo "</tr>";
					for ($x = 0; $x < count($columnName); $x++) {
					echo "<td>".$row[$columnName[$x]]."</td>";
					} 
						echo "</tr>";
					}
				}
		}else if($operation == 'remove'){
			echo $_POST['query'];
			$result = @$connection->query(sprintf($_POST['query']));
		}
		$connection->close();
	}
?>