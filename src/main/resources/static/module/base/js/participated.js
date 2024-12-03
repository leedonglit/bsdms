var participated = {
    loadData : function () {
        let data = page.getConditionQformValCopy("search_participated_Form");
        function loadHtml(obj) {
            $("#participated_content").html(obj.html);
        };
        let config = {
            url : ctxPath + "/academic/loadParticipated",
            data : data,
            eclipseObj : $("#participated_content"),
            callBackFunc : loadHtml
        };
        page.doCondition(config);
    }
}

