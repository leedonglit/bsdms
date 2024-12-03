var kkfileview = {

    fileReview:function(){
        var url = 'http://127.0.0.1:9091/Neo4j.docx'; //要预览文件的访问地址
        window.open('http://127.0.0.1:8012/onlinePreview?url='+encodeURIComponent(Base64.encode(url)));
    }

}