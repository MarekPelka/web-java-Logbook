function ready(){
	show();

	var indexOfRow;
	var name;

	$('#dB').click( function() {
		del();
	});
		
	function show(){
		var values = {
			"operation": 'nothing',
		}
		$.ajax({ type: "POST",   
			 url: "server_processing.php",
			 data: values,		 
			 async: false,
			 success : function(text)
			{
				document.getElementById("viewer").innerHTML = text;
				var table =$('#viewer').DataTable();
				table.destroy();
				table =$('#viewer').DataTable();
				$('#viewer tbody').on( 'click', 'tr', function () {
					if ($(this).hasClass('active') ) {
						$(this).removeClass('active');
					}
					else {
						table.$('tr.active').removeClass('active');
						$(this).addClass('active');
						indexOfRow = $(this).children(":first").text();
						name = table.row(this).data().toString();
						var start = name.search('table=');
						start = start + 6;
						var stop = name.search('">');
						name = name.slice(start,stop);
					}
				});
				
				$('#dB').click(function del(){
					var query1 = "DELETE FROM `control` WHERE `control`.`id` = " + indexOfRow;
					var query2 = "DROP TABLE " + name;
					var values = {
						'operation':'remove',
						'queryControl': query1,
						'queryDrop': query2
					}
					$.ajax({ type: "POST",   
						url: "server_processing.php",
						data: values,		 
						async: false,
						success : function()
						{	
							table.rows( '.active' ).remove().draw();
						},
						failure: function(errMsg) {
							alert(errMsg);
						}
					});
				});
				
				table = $('#viewer').DataTable();
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