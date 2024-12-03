// search 搜索
var searchType = "";// 搜索类型
var inds = [];
var auts = [];
var orgs = [];
var yeas = [];
var search = {
    makePage:function (arr,key,data){
        data.condition.push({
            col:key,
            value:arr,
            type:"=",
        })
    },
    loadCheckData:function (){
        let config = {
            data:{},
            url: ctxPath + "/search/loadCheckData",
            callBackFunc:function (res){
                $("#checksList").append(res);
            },
            callBackErrorFunc: function(obj){

            }
        };
        HD.doAjax(config);
    },
    loadData : function () {
        let data = page.getConditionQformValCopy("search_academic_Form");
        if(inds.length > 0){
            search.makePage(inds,"ACA_CLASS",data);
        }
        if(auts.length > 0){
            search.makePage(auts,"ACA_CREATOR",data);
        }
        if(orgs.length > 0){
            search.makePage(orgs,"ACA_ORG",data);
        }
        function loadHtml(obj) {
            $(".query_list").html(obj.html);
        };
        let url = "";
        if(yeas.length>0){
            url += "/search/searchAca/"+yeas[0];
        }else{
            url += "/search/searchAca/"+1970;
        }

        let config = {
            url : ctxPath + url,
            data : data,
            eclipseObj : $(".query_list"),
            callBackFunc : loadHtml
        };
        page.doCondition(config);
    },
    // 搜索按钮
    searchFun:function(val){
        HD.load({
            url : ctxPath + '/search/content',
            data:{
                "searchType":searchType,
                "value":val
            },
            obj:$("#searchContent"),
            callBackFunc : function (obj) {
            }
        });
    },

    //点击列表 查看文档详情
    searchDetailFun:function(id){
        HD.load({
            url : ctxPath + '/search/detail',
            data:{
                "id":id
            },
            obj:$("#searchContent"),
            callBackFunc : function (obj) {
            }
        });
    },
    applyKey:function (id){
        let config = {
            data:{"acaId":id},
            url: ctxPath + "/search/applyKey",
            callBackFunc:function (){
                ui.msg("The application has been submitted, please wait for the administrator to allocate the KEY!")
            },
            callBackErrorFunc: function(obj){
                ui.msg(obj.msg);
            }
        };
        HD.doAjax(config);
    }
}