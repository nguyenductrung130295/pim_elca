$("#fr_lang").click(function(){
	console.log("french");
	window.location.replace('?lang=fr');
});
$("#en_lang").click(function(){
	console.log("english");
	window.location.replace('?lang=en');
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
	var conf = confirm("Delete all items selected.Are you sure?");
	if(conf){
		$("#footer-table").hide();
		var list = [];
		var checkboxs = $(".checkbox-cus");
		for(var i=0;i<checkboxs.length;i++){
			if(checkboxs[i].checked){
				list.push(checkboxs[i].value);
			}
		}
		$.ajax({
			url:"/project/delallselect",
			method:"post",
			data:{
				list_number:list
			},
			success:function(data){
				if(data === "success"){
					for(var i=checkboxs.length-1;i>=0;i--){
						if(checkboxs[i].checked){
							checkboxs[i].parentElement.parentElement.remove();
						}
					}
				}else{
					window.location.href="/project/error";	
				}
						
			},
			error:function(){
				window.location.href="/project/error";
			}
		})
		
	}
})

$("#pro_enddate").keyup(function(){
	if($("#pro_enddate").val().length == 10 && $("#pro_startdate").val().length == 10){
		var date_start = new Date($("#pro_startdate").val());
		var date_end = new Date($("#pro_enddate").val());
		if(date_end < date_start){
			$("#pro_startdate").val($("#pro_enddate").val());//start = end
		}
		
	}
})

$("#pro_startdate").keyup(function(){
	if($("#pro_enddate").val().length == 10 && $("#pro_startdate").val().length == 10){
		var date_start = new Date($("#pro_startdate").val());
		var date_end = new Date($("#pro_enddate").val());
		if(date_end < date_start){
			$("#pro_enddate").val($("#pro_startdate").val());//end = start
		}
		
	}
})

function hightlighError(){
	var error = $("#have_errors").val();
	if(error){
		if($("#pro_num").val() === ""){
			$("#pro_num").css("border-color","red");
		}
		if($("#pro_name").val() === ""){
			$("#pro_name").css("border-color","red");
		}
		if($("#pro_customer").val() === ""){
			$("#pro_customer").css("border-color","red");
		}
		if($("#pro_group").val() === ""){
			$("#pro_group").css("border-color","red");
		}
		
		if($("#pro_status").val() === ""){
			$("#pro_status").css("border-color","red");
		}
		if($("#pro_startdate").val() === ""){
			$("#pro_startdiv").css("border-color","red");
		}
		
		var existError = $("#sumit_exists");
		console.log(existError.val());
		if(existError.val()==="true"){
			$("#pro_num").css("border-color","red");
			$("#error_number_project").show();
		}else{
			$("#pro_num").css("border-color","#ddd");
			$("#error_number_project").hide();
		}
	}
}

function deleteItem(aTag, idProject, nameProject){
	if(confirm("Delete project name:" + nameProject +". Are you sure?")){
		$.ajax({
			url:"/project/delete",
			method:"post",
			data:{
				idproject:idProject
			},
			success:function(data){
				if(data==="success"){
					aTag.parentNode.parentNode.remove();
				}else{
//					console.log("error delete item" +nameProject);
					window.location.href="/project/error";
				}
			},
			error:function(){
//				console.log("error request");
				window.location.href="/project/error";
			}
		})
	}
}

function switchPage(pageNumber){
	window.location.replace('?p='+pageNumber+"&text_search="+$("#text-search").val()+"&status_search="+$("#searchStatusQuery").val());
}


$("#pro_member").keyup(function(){
	var list = $("#pro_member").val().split(",");
	var list_actual = [];
	for(var i in list){
		if(list[i].trim().length>0){
			list_actual.push(list[i]);
		}
	}
	if(list_actual.length==0){
		return;
	}
	$.ajax({url:"/project/checkvisa",
		method:"post",
		data:{
			list_visa:list_actual
		},
		success:function(data){
			if(data!==""){
				$("#list_visa_er").text(data);
				$("#error_member").show();
			}else{
				$("#error_member").hide();
			}
		},
		error:function(){
			window.location.href="/project/error";
		}
	})
})

$(document).ready(function(){
	console.log("read document");
	$("#select_status").val($("#searchStatusQuery").val());
	$("#pro_num").keyup(function(){
		if($("#pro_num").attr("readonly") == undefined && $("#pro_num").val()!==""){
			$.ajax({
				url:"/project/checkprojectid",
				data:{
					project_number : $("#pro_num").val()
				},
				error:function(){
//					console.log("error ajax check id");
					window.location.href="/project/error";
				},
				success:function(data){
					console.log("data: "+data);
					if(data ==='success'){
						$("#error_number_project").hide();
						$("#pro_num").css("border-color","#ccc");
						 $("#sumit_exists").val("false");
					}else if(data === 'error'){
						$("#pro_num").css("border-color","red");
						$("#error_number_project").show();
						$("#sumit_exists").val("true");
					}
				}
			})
		}else{
			$("#error_number_project").hide();
			$("#pro_num").css("border-color","#ccc");
		}
		
	});
	hightlighError();

})