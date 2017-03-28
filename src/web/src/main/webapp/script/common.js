function executeHtmlAjaxRequest(/*String*/requestUrl, /*String*/targetDivId, /*boolean*/ replaceContent){
  $.ajax({
      url:  requestUrl,
      type: "GET",
      dataType: "text",
      async: true,
      success:  function(data){
          var divElt = document.getElementById(targetDivId);
          if(replaceContent){
              divElt.innerHTML = data;
          } else {
              divElt.innerHTML = divElt.innerHTML + data;
          }
          divElt.isLocked = false;
          divElt.isLoaded = true;
          var waitImgElt = document.getElementById(targetDivId + "_wait_img");
          waitImgElt.style.display = "none";
      },
      failure: function() {
          var divElt = document.getElementById(targetDivId);
          if(replaceContent){
              divElt.innerHTML = "Ajax request failed!";
          } else {
              divElt.innerHTML = divElt.innerHTML + "<br/>Ajax request failed!";
          }
          divElt.isLocked = false;
          divElt.isLoaded = true;
          var waitImgElt = document.getElementById(targetDivId + "_wait_img");
          waitImgElt.style.display = "none";
      }
  });
}

function showErrorStack(linkElt, detailsDivId, msgShow, msgHide){
    var stackTraceContainer = document.getElementById(detailsDivId);
    if(stackTraceContainer.style.display == "none"){
        stackTraceContainer.style.display = "block";
        linkElt.innerHTML = msgHide;
    }else{
        stackTraceContainer.style.display = "none";
        linkElt.innerHTML = msgShow;
    }
}
