var xmlHttp;
//get the value of the input
function getMoreContents(){
    //get the input
    var content=document.getElementById("keyword");
    if(content.value==""){
        clearContent();
        return;
    }
    //send value to the servlet
    xmlHttp=createXMLHttp();
    //alert(xmlHttp);
    var url="Request?keyword="+content.value;
    //true stand for javascript will run after send() even if the servlet did not response
    xmlHttp.open("GET",url,true);
    //xmlHttp sign for callback()
    //xmlHttp have 0-4 conditions we only care 4(complete)
    xmlHttp.onreadystatechange=callback;
    xmlHttp.send(null);

}
//function to get the http
function createXMLHttp(){
    //version works for most browsers
    var xmlHttp;
    if(window.XMLHttpRequest){
        xmlHttp=new XMLHttpRequest();
    }
    //version works for other browsers
    if(window.ActiveXObject){
        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
        if(!xmlHttp){
            xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
        }
    }
    return xmlHttp;
}

//callback
function callback(){
    if(xmlHttp.readyState==4){
        if(xmlHttp.status==200){
            var result = xmlHttp.responseText;
            //json si different from js and java so we need to add ()
            var json=eval("("+result+")");
            //alert(json);
            setContent(json);

        }
    }
}
//show the contents
function setContent(contents){
    //clear the search table
    clearContent();
    setLocation();
    //to get length first to ensure how many tr to generate
    var size=contents.length;
    //set content
    for(var i=0; size > i; i++){
        var nextNode=contents[i];//代表的是json格式数据的第i个元素
        var tr=document.createElement("tr");
        var td=document.createElement("td");
        td.setAttribute("border","0");
        td.setAttribute("bgcolor","black");
        td.onmouseover=function(){
            this.className='mouseOver';
        };
        td.onmouseout=function(){
            this.className='mouseOut';
        };
        td.onmousedown=function(){
            document.getElementById("keyword").value = this.innerHTML;
        };
        var text=document.createTextNode(nextNode);
        td.appendChild(text);
        tr.appendChild(td);
        document.getElementById("content_table_body").appendChild(tr);
    }

}
function clearContent(){
    var contentTableBody=document.getElementById("content_table_body");
    var size=contentTableBody.childNodes.length;
    for(var i=size-1;i>=0;i--){
        contentTableBody.removeChild(contentTableBody.childNodes[i]);
    }
    document.getElementById("popDiv").style.border="none";
}
//When we point to another place, then clean the search table
function keywordBlur(){
    clearContent();
}
// to set the location of the search elements
function setLocation(){
    //the length of the elements should be the same as the input
    var content=document.getElementById("keyword");
    var width=content.offsetWidth;
    var left=content["offsetLeft"];
    var top=content["offsetTop"]+content.offsetHeight;
    //get the elements in div
    var popDiv=document.getElementById("popDiv");
    popDiv.style.border="black 1px solid";
    popDiv.style.left=left+"px";
    popDiv.style.top=top+"px";
    popDiv.style.width=width+"px";
    document.getElementById("content_table").style.width=width+"px";
}