//-----------------------------------------COMMON-----------------------------------------------------
/**
 * chang language to fr for application when click FR hyperlink on top right screen
 * @returns
 */
$("#fr_lang").click(function(){
	window.location.replace('?lang=fr');
});
/**
 * change language to en for application when click EN hyperlink on top right screen
 * @returns
 */
$("#en_lang").click(function(){
	console.log("english");
	window.location.replace('?lang=en');
});
//--------------------------------------------NEW/EDIT TEMPLATES--------------------------------------
/**
 * Validate field start date and endate to start date alway less equal than end date
 * @returns
 */
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
/**
 * Hight error field if has error mandatory and others
 * @returns
 */
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

/**
 * Check employee is exist or not. get string visa member and convert to visas array list. 
 * call ajax and show error message if has any visa existed on DB
 * @returns
 */
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

$("#pro_member").focusout(function(){
	if($("#pro_member").val()===""){
		$("#error_member").hide();
	}
})

/**
 * Check existed project number when this field on change by keyup event. Hight light and show message if not existed
 * @param event
 * @returns
 */
$("#pro_num").on('keyup',function(event){
	handleProjectNumber();
});

function handleProjectNumber(){
	if($("#pro_num").attr("readonly") == undefined && $("#pro_num").val()!==""){
		$.ajax({
			url:"/project/checkprojectid",
			data:{
				project_number : $("#pro_num").val()
			},
			error:function(){
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
}

//--------------------------------------------LIST TEMPLATES------------------------------------------
/**
 * reset search text query and status project to empty when click "Reset Search" hyperlink
 * to empty and reload with list all project
 * @returns
 */
$("#btn_reset_search").click(function(){
	$("#text-search").val("");
	$("#select_status").val("");
	$("#searchStatusQuery").val("");
	window.location.href="query?status_search=&text_search=";
});
/**
 * Handle change event on status select search form. set value to input hidden 
 * @returns
 */
$("#select_status").change(function(){
	$("#searchStatusQuery").val($("#select_status").val());
});
/**
 * Show delete all label when any checkbox row list be selected, otherwise
 * @returns
 */
$(".checkbox-cus").change(function(){
	var checkedItems = countChecked($(".checkbox-cus"));
	if(checkedItems>0){
		$("#counter_select").text(checkedItems);
		$("#footer-table").show();
	}else{
		$("#footer-table").hide();
	}
})

function countChecked(checkbox){
	var count = 0;
	for(var i=0;i<checkbox.length;i++){
		if(checkbox[i].checked){
			count++;
		}
	}
	return count;
}

/**
 * call ajax to request delete all items be selected when click label or icon delete all
 * @returns
 */
$("#btn_delete").click(function(){
	deleteAll();
})

$("#btn_delete_all").click(function(){
	deleteAll();
})
/**
 * get array list checked to param data request,reload if delete success or show message if has error
 * @returns
 */
function deleteAll(){
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
					switchPage($("#page-number").val());
//					for(var i=checkboxs.length-1;i>=0;i--){
//						if(checkboxs[i].checked){
//							checkboxs[i].parentElement.parentElement.remove();
//						}
//					}					
				}else if(data === "fail_null"){
					$("#error_delete_null").show();
				}else{
					$("#error_delete_status").show();
				}
						
			},
			error:function(){
				window.location.href="/project/error";
			}
		})
		
	}
}
/**
 * delete single project when click icon delete on screen list project
 * reload page after delete success or show message if have errors
 * @param aTag
 * @param idProject
 * @param nameProject
 * @returns
 */
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
					switchPage($("#page-number").val());
				}else if(data === "fail_null"){
					$("#error_delete_null").show();
				}else{
					$("#error_delete_status").show();
				}
			},
			error:function(){
				window.location.href="/project/error";
			}
		})
	}
}
/**
 * Reload page when delete project
 * @param pageNumber
 * @returns
 */
function switchPage(pageNumber){
	window.location.replace('?p='+pageNumber+"&text_search="+$("#text-search").val()+"&status_search="+$("#searchStatusQuery").val());
}


/**
 * 
 * @returns
 */
$(document).ready(function(){
	$("#select_status").val($("#searchStatusQuery").val());	
	hightlighError();
})