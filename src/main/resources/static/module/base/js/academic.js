var keysDoc = [];
var eSignConfig = {};
var academic = {
    checkAll:function (t){
        if(t.checked){
            $("#academic-table-demo tbody tr").each(function (){
                $(this).find("input").prop("checked",true);
            })
        }else{
            $("#academic-table-demo tbody tr").each(function (){
                $(this).find("input").prop("checked",false);
            })
        }
    },
    deleteBatch:function(){
        let ids = "";
        $("#academic-table-demo tbody tr").each(function (){
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
            url: ctxPath + "/academic/projectdeleteBatch",
            callBackFunc:function (obj){
                ui.msg("Delete Success!")
                academic.loadData();
            },
            callBackErrorFunc: function(){
                ui.msg("Delete Failed!");
            }
        };
        ui.confirmDoAjax("Are you sure you want to delete Project?",config);
    },
    addDocToCreatKey:function (checkobj, docId){
        var checked = $(checkobj).find("input")[0].checked;
        if(!checked){
            if(keysDoc.indexOf(docId) == -1){
                keysDoc.push(docId);
                $(checkobj).find("input").attr("checked",true);
            }
        } else{
            keysDoc.splice(keysDoc.indexOf(docId),1);
            $(checkobj).find("input").attr("checked",false);
        }
    },
    reviewFile:function(docId,fileName,kkfileurl,serverUrl){
        var previewUrl = "http://"+serverUrl+"/academic/preview?fileId="+docId+"&fullfilename="+fileName;
        window.open('http://'+kkfileurl+'/onlinePreview?url='+encodeURIComponent(Base64.encode(previewUrl)));
    },
    loadData : function () {
        let data = page.getConditionQformValCopy("search_academic_Form");
        function loadHtml(obj) {
            $("#academic_content").html(obj.html);
        };
        let config = {
            url : ctxPath + "/academic/loadAll",
            data : data,
            eclipseObj : $("#academic_content"),
            callBackFunc : loadHtml
        };
        page.doCondition(config);
    },
    selectAll:function(obj){
        if(obj.checked){
            $("#docToby tr").each(function (){
                $(this).find("input[type='checkbox']").prop("checked",true);
            })
        }else{
            $("#docToby tr").each(function (){
                $(this).find("input[type='checkbox']").prop("checked",false);
            })
        }
    },
    openHistory:function (){
        const key = $("input[name='ACA_KEY']").val();
        const aca = $("input[name='ACA_ID']").val();
        const url = key ===""? "/academic/openHistory/null/"+aca:"/academic/openHistory/"+key+"/"+aca;
        let config = {
            width:960,
            height:584,
            title:false,
            url:ctxPath + url,
        };
        ui.createWind(config);
    },
    openDown:function (ids){
        const aca = $("input[name='ACA_ID']").val();
        // 获取选中的文件
        if (undefined == ids || null == ids){
            ids = "";
            $("#docToby tr input[type='checkbox']").each(function (){
                if($(this).is(":checked")){
                    ids += $(this).val()+",";
                }
            })
            ids = ids.substring(0,ids.length-1);
            if (ids.length == 0){
                ui.msg("Please select at least one item to download!");
                return false;
            }
        }
        let config = {
            width:960,
            height:584,
            title:false,
            url:ctxPath + '/academic/openDown/'+aca+"/"+ids,
        };
        ui.createWind(config);
    },
    openUpload:function (){
        let config = {
            width:960,
            height:584,
            title:false,
            url:ctxPath + '/academic/openUpload',
        };
        ui.createWind(config);
    },
    submitDocument:function (){
        let document = $("#aca_document_upload_form").serialize();
        let acaId = $("input[name='ACA_ID']").val();
        document += "&ACA_ID=" + acaId;
        ui.closeWin();
        academic.openEsign();
        eSignConfig = {
            data:document,
            url: ctxPath + "/academic/submitDocument",
            callBackFunc:function (){
                ui.closeWin();
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
    },
    openEsign:function (){
        let config = {
            width:960,
            height:584,
            title:false,
            url:ctxPath + '/academic/openEsign',
        };
        ui.createWind(config);
    },
    saveEsign:function (eSign){
        ui.lodding();
        eSignConfig.data += "&eSign="+eSign;
        HD.doAjax(eSignConfig);
    },
    createProject:function (acaId){
        let phase = new Array();
        let index = 1;
        let shouldContinue = true;
        $("tr.tab_c1").each(function (){
            if (!shouldContinue) {
                return false; // 终止遍历
            }
            //添加数据校验，没有值则不添加
            if($(this).find("td:eq(1)").html()==""||$(this).find("td:eq(2)").html()==""||$(this).find("td:eq(3)").html()==""||$(this).find("td:eq(4)").html()==""){
                layer.msg("Please fill in all required fields!");
                shouldContinue = false;
                return false;
            }
            phase.push({"PHASE_NAME":$(this).find("td:eq(1)").html(),"PHASE_START_TIME":$(this).find("td:eq(3)").html(),"PHASE_END_TIME":$(this).find("td:eq(4)").html(),"PHASE_DESC":$(this).find("td:eq(2)").html(),"PHASE_INDEX":index});
            index++;
        });
        if(!shouldContinue){
            return false;
        }
        let pro = new Map();
        pro["ACA_CODE"] = $("input[name='ACA_CODE']").val();
        pro["ACA_TITLE"] = $("input[name='ACA_TITLE']").val();
        pro["ACA_CLASS"] = $("input[name='ACA_CLASS']").val();
        pro["ACA_START_TIME"] = $("input[name='ACA_START_TIME']").val();
        pro["ACA_END_TIME"] = $("input[name='ACA_START_TIME']").val();
        pro["ACA_DESC"] = $("textarea[name='ACA_DESC']").val();
        pro["ACA_ID"] = acaId;
        //添加数据校验
        if(pro["ACA_TITLE"]==""||pro["ACA_CLASS"]==""||pro["ACA_START_TIME"]==""){
            layer.msg("Please fill in all required fields!");
            return false;
        }
        const config = {
            data:{"phase":encodeURIComponent(JSON.stringify(phase)),"pro":encodeURIComponent(JSON.stringify(pro))},
            url: ctxPath + "/academic/createProject",
            callBackFunc:function (){
                ui.msg("Save Success！")
            },
            callBackErrorFunc: function(){
                ui.closeWin();
                ui.msg("Save Fail!");
            }
        };
        HD.doAjax(config);
    },
    getAddGroupUrl:function (t){
        let config = {
            data:{"t":t,"acaId":$("input[name='ACA_ID']").val()},
            url: ctxPath + "/academic/toGroup",
            callBackFunc:function (obj){
                $("b[name='add_group_url']").html(obj);
            },
            callBackErrorFunc: function(){
                ui.msg("Create Failed!");
            }
        };
        HD.doAjax(config);
    },
    loadKey:function (acaId){
        function loadHtml(obj) {
            $(".key_table").html(obj);
        };
        let config = {
            url : ctxPath + "/academic/loadKey/"+acaId,
            eclipseObj : $(".key_table"),
            callBackFunc : loadHtml
        };
        page.doCondition(config);
    },
    saveKey:function (acaId){
        const param = $("#key_form").serialize();
        let config = {
            data:param+"&desc_content="+encodeURIComponent($("#desc_content").html())+"&docIds="+encodeURIComponent(keysDoc),
            url: ctxPath + "/academic/saveKey",
            callBackFunc:function (obj){
                ui.msg("Save Success!");
                keysDoc = [];
                academic.loadKey(acaId);
            },
            callBackErrorFunc: function(){
                ui.msg("Save Failed!");
            }
        };
        HD.doAjax(config);
    },
    delKey:function (acaId,id){
        let config = {
            data:{"key":id},
            url: ctxPath + "/academic/delKey",
            callBackFunc:function (obj){
                ui.msg("Delete Success!")
                academic.loadKey(acaId);
            },
            callBackErrorFunc: function(){
                ui.msg("Delete Failed!");
            }
        };
        ui.confirmDoAjax("Are you sure you want to delete key?",config);
        $(".layui-layer").css("background","#fff");
    },
    openKeyPage:function (t){
        $("#first_content").hide();
        $("#second_content").show();
        HD.load({
            url : ctxPath + "/academic/openKeyPage" ,
            data:{"acaId":t},
            obj:$("#second_content"),
            callBackFunc : function (obj) {
            }
        });
    },
    removeGroup:function (id){
        let config = {
            data:{"group":id},
            url: ctxPath + "/academic/rmGroup",
            callBackFunc:function (obj){
                ui.msg("Remove Success！")
                $("li[tag='"+id+"']").remove();
            },
            callBackErrorFunc: function(){
                ui.msg("Remove Failed!");
            }
        };
        ui.confirmDoAjax("Are you sure you want to remove this member?",config);
        $(".layui-layer").css("background","#fff");
    },
    beManager:function (id){
        let config = {
            data:{"group":id},
            url: ctxPath + "/academic/beManager",
            callBackFunc:function (obj){
                ui.msg("Operate Success！")
            },
            callBackErrorFunc: function(){
                ui.msg("Operate Failed!");
            }
        };
        ui.confirmDoAjax("Are you sure you want to upgrade this member to an manager?",config);
        $(".layui-layer").css("background","#fff");
    }
}

