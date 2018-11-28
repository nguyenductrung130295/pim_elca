$("#fr_lang").click(function(){
	console.log("french");
	window.location.replace('?lang=vi');
});
$("#btn_reset_search").click(function(){
	$("#text-search").val("");
	$("#select_status").val("");
	$("#searchStatusQuery").val("");
	window.location.href="query?status_search=&text_search=";
});

$("#select_status").change(function(){
	$("#searchStatusQuery").val($("#select_status").val());
});

$(".checkbox-cus").change(function(){
	var checkedItems = countChecked($(".checkbox-cus"));
	showBincycle();
	if(checkedItems>0){
		$("#counter_select").text(checkedItems);
		$("#footer-table").show();
	}else{
		$("#footer-table").hide();
	}
})

function showBincycle(){
	var checkbox = $(".checkbox-cus");
	for(var i=0;i<checkbox.length;i++){
		if(checkbox[i].checked){
			checkbox[i].parentNode.parentNode.lastElementChild.lastElementChild.firstElementChild.style.display="block";
		}else{
			checkbox[i].parentNode.parentNode.lastElementChild.lastElementChild.firstElementChild.style.display="none";
		}
	}
}

function countChecked(checkbox){
	var count = 0;
	for(var i=0;i<checkbox.length;i++){
		if(checkbox[i].checked){
			count++;
		}
	}
	return count;
}

$("#btn_delete").click(function(){
	var conf = confirm("Delete items selected.Are you sure?");
	if(conf){
		var list = [];
		var checkboxs = $(".checkbox-cus");
		for(var i=0;i<checkboxs.length;i++){
			if(checkboxs[i].checked){
				list.push(checkboxs[i].value);
				
			}
		}
		$.ajax({
			url:"delete",
			method:"post",
			data:{
				list_number:list
			},
			success:function(data){
				if(data.length <= 0){
					for(var i=checkboxs.length-1;i>=0;i--){
						if(checkboxs[i].checked){
							checkboxs[i].parentElement.parentElement.remove();
						}
					}
				}else{
					window.location.href="error";	
				}
						
			},
			error:function(){
				window.location.href="error";
			}
		})
		
	}
})


$(document).ready(function(){
	console.log("read document");
	$("#select_status").val($("#searchStatusQuery").val());
	$("#pro_num").change(function(){
		console.log("changed pro_num");
		$.ajax({
			url:"checkprojectid",
			data:{
				project_number : $("#pro_num").val()
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