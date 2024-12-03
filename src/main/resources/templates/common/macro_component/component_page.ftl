//全刷新分页
<#macro pageform objID callBack >
	<script type="text/javascript">
        function formtoPage(pageNo){
           document.getElementById("pageNo").value=pageNo;
           document.getElementById("form1").submit();
        }
        function toPage(pageNo){ 
          if(pageNo<1 || pageNo>${totalpage!}){
            return;
          }
          if(pageNo==${pageNo!}){
            return;
          }
          
           document.getElementById("pageNo").value=pageNo;
          <#-- document.getElementById("form1").submit(); -->
           
              AjaxClick2('${objID}',pageNo,'${callBack}')
        }
        
        function upanddownpage(pageNo){ alert('desdsds');
          var present=document.getElementById("pageNo").value;
          var gopage=Number(present)+Number(pageNo);
          toPage(gopage);
        }
        
        function postPagesize(pagesize){
         document.getElementById("inputpagesize").value=pagesize;
         formtoPage(1);
        }
        
        function jsSelectItemByValue() { 
          var objSelect = document.getElementById("pagesize");
          var objSelectpage = document.getElementById("selectpage");
          var selectYear = document.getElementById("year");
          var selectMonth=document.getElementById("month");
          for (var i = 0; i < objSelect.options.length; i++) {  
              if (objSelect.options[i].text=='${pagesize!}') {     
               objSelect.options[i].selected = true;           
               break;        
                }        
             }
             
            for (var i = 0; i < objSelectpage.options.length; i++) { 
            
              if (objSelectpage.options[i].text=='${pageNo!}') {     
               objSelectpage.options[i].selected = true;           
               break;        
                }        
             }
             
             for (var i = 0; i < selectYear.options.length; i++) { 
            
              if (selectYear.options[i].text=='${year!}') {     
               selectYear.options[i].selected = true;           
               break;        
                }        
             }
             
             for (var i = 0; i < selectMonth.options.length; i++) { 
            
              if (selectMonth.options[i].text=='${month!}') {     
               selectMonth.options[i].selected = true;           
               break;        
                }        
             }
             
            } 
       
         function goparticularquery(waybill_no){
          url=location.protocol + '//' + location.host+'/Post/post/waybill!waybillparticularquery.action?waybill_no='+waybill_no;
          window.location.href=url;
         }
         
         function seacherreset(){
                  var selectYear = document.getElementById("year");
          var selectMonth=document.getElementById("month");
                   document.getElementById("brand_id").value="";
                    document.getElementById("waybill_no").value="";
                     document.getElementById("sed_name").value="";
                      document.getElementById("rec_name").value="";
                         for (var i = 0; i < selectYear.options.length; i++) { 
            
              if (selectYear.options[i].value=="allyear") {     
               selectYear.options[i].selected = true;           
               break;        
                }        
             }

             for (var i = 0; i < selectMonth.options.length; i++) { 
               
              if (selectMonth.options[i].value=="allmonth") {     
               selectMonth.options[i].selected = true;           
               break;        
                }        
             }
         }
        
    </script>
	<div class="linepage">
       <a href="javascript:toPage(1);" id="up">&lt;&lt;第一页</a> <a href="javascript:void(0);" onclick="upanddownpage('-1')" >&lt;上一页</a> 第${pageNo!}/${totalpage!} <a href="javascript:upanddownpage('+1');">下一页&gt;</a> <a href="javascript:void(0)" onclick='toPage(${totalpage!})'>最后一页&gt;&gt;</a> 跳转至<span class="td-al">
          <select name="select" id="selectpage"  onchange="toPage(this.value);">
             <#list 1..totalpage as t>
                  <option value="${t}">${t}</option>
             </#list>
          </select>
   </span>页 每页显示<span class="td-al">
     <select name="pagesize" id="pagesize" onchange="javascript:postPagesize(this.value);">
       <option value="10">10</option>
       <option value="15">15</option>
       <option value="20">20</option>
     </select>
  </span>条</div>
    <script>
		function AjaxClick2(objID,pageNo,cbalBack){
			$("#"+objID).attr("pageNo",pageNo);
			$("#"+objID).attr("pageNoNow",'1');
        	eval(cbalBack);
		}
	</script>
</#macro>



<#macro test>
    freemarker test ...
</#macro>