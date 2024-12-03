var manage = {
    batchOperations:function(type){
        let ids = "";
        $("#academic-table-user tbody tr").each(function (){
            if($(this).find("input").is(":checked")){
                ids += $(this).find("input").val()+",";
            }
        })
        if(ids.length >0){
            ids = ids.substring(0,ids.length-1);
        }else{
            ui.msg("Please select at least one item to Batch Operation!");
            return false;
        }
        let config = {
            data:{"ids":ids,"type":type},
            url: ctxPath + "/account/batchOperationUser",
            callBackFunc:function (obj){
                ui.msg("Batch Operation Success!")
                manage.loadData();
            },
            callBackErrorFunc: function(){
                ui.msg("Batch Operation Failed!");
            }
        };
        ui.confirmDoAjax("Are you sure you want to Batch Operation?",config);
    },
    checkAll:function (t){
        if(t.checked){
            $("#academic-table-user tbody tr").each(function (){
                $(this).find("input").prop("checked",true);
            })
        }else{
            $("#academic-table-user tbody tr").each(function (){
                $(this).find("input").prop("checked",false);
            })
        }
    },
    loadData : function () {
        let data = page.getConditionQformValCopy("manage_academic_Form");
        function loadHtml(obj) {
            $("#manage_content").html(obj.html);
        };
        let config = {
            url : ctxPath + "/account/loadAllUser",
            data : data,
            eclipseObj : $("#manage_content"),
            callBackFunc : loadHtml
        };
        page.doCondition(config);
    }
}

