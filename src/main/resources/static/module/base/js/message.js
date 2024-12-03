var message = {
    checkAll:function (t){
        if(t.checked){
            $("#academic-table-msg tbody tr").each(function (){
                $(this).find("input").prop("checked",true);
            })
        }else{
            $("#academic-table-msg tbody tr").each(function (){
                $(this).find("input").prop("checked",false);
            })
        }
    },
    deleteBatch:function (){
        let ids = "";
        $("#academic-table-msg tbody tr").each(function (){
            if($(this).find("input").is(":checked")){
                ids += $(this).find("input").val()+",";
            }
        })
        if(ids.length >0){
            ids = ids.substring(0,ids.length-1);
        }else{
            ui.msg("Please select at least one item to delete!");
            return false;
        }
        let config = {
            data:{"ids":ids},
            url: ctxPath + "/account/deleteBatch",
            callBackFunc:function (obj){
                ui.msg("Delete Success!")
                message.loadData();
            },
            callBackErrorFunc: function(){
                ui.msg("Delete Failed!");
            }
        };
        ui.confirmDoAjax("Are you sure you want to delete message?",config);
    },
    loadData : function () {
        let data = page.getConditionQformValCopy("message_academic_Form");
        function loadHtml(obj) {
            $("#message_content").html(obj.html);
        };
        let config = {
            url : ctxPath + "/account/loadAllMsg",
            data : data,
            eclipseObj : $("#message_content"),
            callBackFunc : loadHtml
        };
        page.doCondition(config);
    },
    allocation : function(mId){
        layer.prompt({title: 'Please input key', formType: 2}, function(value, index, elem){
            if(value === '') return elem.focus();
            let config = {
                data:{"msg":value,"mId":mId},
                url: ctxPath + "/account/allocation",
                callBackFunc:function (){
                    ui.msg("Save Success！")
                    setTimeout(function (){
                        window.location.reload();
                    },1000);
                },
                callBackErrorFunc: function(){
                    ui.closeWin();
                    ui.msg("Save Failed!");
                }
            };
            HD.doAjax(config);
            // 关闭 prompt
            layer.close(index);
        });
    }
}

