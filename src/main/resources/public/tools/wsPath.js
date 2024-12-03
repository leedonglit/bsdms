var wsPath = "";
var curPath = window.document.location.href;
//console.log("curPath=" + curPath);//curPath=http://localhost:8081/emws/base/module/page.htm
var pathName = window.document.location.pathname;
//console.log("pathName=" + pathName);//pathName=/emws/base/module/page.htm
var pos = curPath.indexOf(pathName);
//console.log("pos=" + pos);//pos=21
var localhostPath = curPath.substring(0,pos);
//console.log("localhostPath=" + localhostPath);//localhostPath=http://localhost:8081
var projectName = "";//pathName.substring(0,pathName.substring(1).indexOf('/') + 1);
//console.log("projectName=" + projectName);//projectName=/emws
//console.log("localhostPath + projectName=" + localhostPath + projectName);//localhostPath + projectName=http://localhost:8081/emws
var projectPath = localhostPath + projectName;
wsPath = projectPath.replace('http://','ws://');
//console.log("wsPath=" + wsPath);//wsPath=ws://localhost:8081/emws
