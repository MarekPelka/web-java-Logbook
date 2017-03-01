function ready(tableName){
	show();

	var indexOfRow = new Array();
	var table;
	$('#delB').click( function() {
		del();
	});

	
	function show(){
		var values = {
			"operation": 'nothing',	
			"tableName": tableName,	
		}
		 $.ajax({ type: "POST",   
			 url: "server_processing_for_tables.php",
			 data: values,		 
			 async: false,
			 success : function(text)
			{
				document.getElementById("viewer").innerHTML = text;
				table = $('#viewer').DataTable();

				$('#viewer tbody').on( 'click', 'tr', function () {
					if ( $(this).hasClass('active') ) {
						$(this).removeClass('active');
						var index = indexOfRow.indexOf($(this).children(":first").text());
						if (index > -1) {
							indexOfRow.splice(index, 1);
						}
					}else {
						$(this).addClass('active');
						var $name = $(this).children(":first").text();
						indexOfRow.push($name);
					}
				});
		
				$('#delB').click(function del(){
					var query = 'DELETE FROM `' + tableName + '` WHERE `id` in(';
							for(i=0; i< indexOfRow.length; i++){
									query=query + indexOfRow[i]+',';
								}
								query = query.slice(0, -1);
								query=query+')';
					var values = {
						'operation':'remove',
						'query': query
					}
					$.ajax({ type: "POST",   
						url: "server_processing_for_tables.php",
						data: values,		 
						async: false,
						success : function()
						{	
							table.rows('.active').remove().draw(false);
						},
						failure: function(errMsg) {
							alert(errMsg);
						}
					});
				});
		
				$('#viewer tfoot th').each( function () {
					var title = $(this).text();
					$(this).html( '<input type="text" placeholder="Search '+title+'" />' );
				});
				
				table.columns().every( function () {
					var that = this;
					$( 'input', this.footer() ).on( 'keyup change', function () {
						if ( that.search() !== this.value ) {
							that.search(this.value).draw();
						}
					});
				});
			},
			failure: function(errMsg) {
				alert(errMsg);
			}
		});
		$("#viewer").show();
	}
	
	
	
}
	