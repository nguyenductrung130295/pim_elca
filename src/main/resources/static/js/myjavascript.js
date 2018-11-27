$("#fr_lang").click(function(){
	console.log("french");
	window.location.replace('?lang=vi');
})
$(document).ready(function(){
	console.log("read document");
	$("#pro_num").change(function(){
		console.log("changed pro_num");
		$.ajax({
			url:"checkprojectid",
			data:{
				projectid : $("#pro_num").val()
			},
			error:function(){
				console.log("error ajax check id");
			},
			success:function(data){
				console.log("data: "+data);
				if(data ==='success'){
					$("#error_number_project").hide();
					$("#pro_num").css("border-color","#ccc");
				}else if(data === 'error'){
					$("#pro_num").css("border-color","red");
					$("#error_number_project").show();
				}
			}
		})
	});
})