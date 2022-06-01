windowLoader = function() {
        DisableEnableLinks(true)
}

function addLoadEvent(func) {
  var oldonload = window.onload;
  if (typeof window.onload != 'function') {
    window.onload = func;
  } else {
    window.onload = function() {
      if (oldonload) {
        oldonload();
      }
      func();
    }
  }
}

function DisableEnableLinks(){
  objLinks = document.links;
  for(i=0;i<objLinks.length;i++){
	var test2 = objLinks[i].className;
	var test3 = test2.indexOf('previewable');
	if (test2.indexOf('previewable') <= 0) {
    	objLinks[i].disabled = true;
	    objLinks[i].onclick = function(){return false;};
	}
  }
  forms = document.forms;
  for(i=0;i<forms.length;i++){
	var test = forms[i];
    test.disabled = true;
    test.action = 'javascript:return false()';
  }
}
// document.title = document.title + " - CMS Preview" ;
addLoadEvent(windowLoader);