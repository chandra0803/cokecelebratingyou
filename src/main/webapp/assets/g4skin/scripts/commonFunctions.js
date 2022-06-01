
<!--

function callUrl( urlToCall )
{
	window.location=urlToCall;
}


function getContentFormName()
{
/*
	for(var i = 0; i < document.forms.length;i++)
	{
		if(document.forms[i].id != 'quickSearchForm')
		{
			return document.forms[i].id;
		}
	}
*/
	return "contentForm";
}

function getContentForm()
{
	return document.forms[getContentFormName()];
}


function toLoginWithForm(formName)
{
	document.forms[formName].action="<%=request.getContextPath()%>/login.do";
}
function toLogin()
{
	toLoginWithForm(getContentFormName());
}


function setFocusWithForm(formName)
{
  document.forms[formName].elements[0].focus();
}
function setFocus()
{
  setFocusWithForm(getContentFormName());
}


function setDispatchWithForm(target, formName) 
{
    document.forms[formName].method.value=target;
}
function setDispatch(target) 
{
    setDispatchWithForm(target,getContentFormName());
}


/*  DO NOT USE WITH SUBMIT TYPE BUTTONS */
function setDispatchAndSubmitWithForm(target, formName) 
{
    document.forms[formName].method.value=target;
    document.forms[formName].submit();
}
function setDispatchAndSubmit(target) 
{
	setDispatchAndSubmitWithForm(target, getContentFormName());    
}


function setActionAndDispatchWithForm(myaction, target, formName) 
{
    document.forms[formName].method.value=target;
    document.forms[formName].action=myaction;
}
function setActionAndDispatch(myaction, target) 
{
	setActionAndDispatchWithForm(myaction,target,getContentFormName());    
}

/*  DO NOT USE WITH SUBMIT TYPE BUTTONS */
function setActionDispatchAndSubmitWithForm(myaction, target, formName) 
{
    document.forms[formName].method.value=target;
    document.forms[formName].action=myaction;
    document.forms[formName].submit();
}
function setActionDispatchAndSubmit(myaction, target) 
{
	setActionDispatchAndSubmitWithForm(myaction, target, getContentFormName());
}

function setActionAndSubmit(myaction)
{
  setActionAndSubmitWithForm(myaction, getContentFormName());
}
function setActionAndSubmitWithForm(myaction, formName)
{
    document.forms[formName].action=myaction;
    document.forms[formName].submit();
}

function imgSwap(theImg, theFile) 
{
	image = eval("document.all." + theImg);
	image.src = "/images/" + theFile;
}

function ltrim(str) 
{
   var wspace = new String(" \t\n\r");
   var s = new String(str);
   if (wspace.indexOf(s.charAt(0)) != -1) {
      var j=0, i = s.length;
      while (j < i && wspace.indexOf(s.charAt(j)) != -1) {
         j++;
      }
      s = s.substring(j, i);
   }
   return s;
}

function rtrim(str) 
{
   var wspace = new String(" \t\n\r");
   var s = new String(str);
   if (wspace.indexOf(s.charAt(s.length-1)) != -1) {
      var i = s.length - 1;       // Get length of string
      while (i >= 0 && wspace.indexOf(s.charAt(i)) != -1) {
         i--;
      }
      s = s.substring(0, i+1);
   }

   return s;
}

function trim(str) 
{
   return rtrim(ltrim(str));
}
 
 
/*  This function is to select all options in a multi-valued <select> */
function selectAll(elementId) 
{
    var element = document.getElementById(elementId);
	len = element.length;
	if (len != 0) {
		for (i = 0; i < len; i++) {
			element.options[i].selected = true;
		}
	}
}


var newWindow = null;

function closeWin()
{
	if (newWindow != null){
		if(!newWindow.closed)
			newWindow.close();
	}
}

function popUpWinInMiddle(url, strWidth, strHeight)
{	
	leftVal = (640 - strWidth)/2;
	topVal = (480 - strHeight)/2;
	
	if (screen) {
		topVal = (screen.availHeight - strHeight) / 2;
    	leftVal = (screen.availWidth - strWidth) / 2;
	}
	
	var tools="";
	tools = "resizable,toolbar=no,location=no,scrollbars=no,width="+strWidth+",height="+strHeight+",left="+leftVal+",top="+topVal;
	newWindow = window.open(url, 'newWin', tools);
	newWindow.focus();
}

function popUpWin(url, type, strWidth, strHeight, closeWindowFlag, scrollbars)
{
	if (closeWindowFlag==true)
	{
  	  closeWin();
 	}

	if (type == "fullScreen")
	{
		strWidth = screen.availWidth - 10;
		strHeight = screen.availHeight - 160;
	}

  var strScrollbars="no";
  if (scrollbars==true)
  {
    strScrollbars="yes";
  }

	var tools="";
	if (type == "standard" || type == "fullScreen") tools = "resizable,toolbar=yes,location=yes,scrollbars="+strScrollbars+",menubar=yes,width="+strWidth+",height="+strHeight+",top=0,left=0";
	if (type == "console") tools = "resizable,toolbar=no,location=no,scrollbars="+strScrollbars+",width="+strWidth+",height="+strHeight+",left=0,top=0";
	newWindow = window.open(url, 'newWin', tools);
	newWindow.focus();
}

function popUpWinInMiddle(url, name, type, strWidth, strHeight, closeWindowFlag, scrollbars)
{
	if (closeWindowFlag==true)
	{
  	  closeWin();
 	}

	if (type == "fullScreen")
	{
		strWidth = screen.availWidth - 10;
		strHeight = screen.availHeight - 160;
	}

	if (screen) {
		topVal = (screen.availHeight - strHeight) / 2;
    	leftVal = (screen.availWidth - strWidth) / 2;
	}
	
  var strScrollbars="no";
  if (scrollbars==true)
  {
    strScrollbars="yes";
  }

	var tools="";
	if (type == "standard" || type == "fullScreen") tools = "resizable,toolbar=yes,location=yes,scrollbars="+strScrollbars+",menubar=yes,width="+strWidth+",height="+strHeight+",left="+leftVal+",top="+topVal;
	if (type == "console") tools = "resizable,toolbar=no,location=no,scrollbars="+strScrollbars+",width="+strWidth+",height="+strHeight+",left="+leftVal+",top="+topVal;
	newWindow = window.open(url, name, tools);
	newWindow.focus();
}

  /* Finds an element by name in the form. */
function findElement(theForm, elementName)
{
  for (var i = 0; i<theForm.elements.length; i++) 
  {
      if ((theForm.elements[i].name.indexOf(elementName) > -1)) 
      {
          return  theForm.elements[i];
      }
  }
  return null;
}

/* Clears the date mask from an input field. */
function clearDateMask(field)
{
	//this date mask should match what is in DateUtils.standardDateFormat
  if(field.value == 'MM/DD/YYYY')
  {
  	field.value = '';
  }
  field.blur();
}

/* Sets focus on the first visable, editable input field on the content form */
function setFocusOnFirstInput()
{
  setFocusOnInput(1);
}

/* Sets focus on the (## specified) visable, editable input field on the content form */
function setFocusOnInput(number)
{
  var count = 1;
  // for each element in each form
  for(i=0; i < getContentForm().length; i++)
  {
	 	// if it's not a hidden element
    if (getContentForm()[i].type != "hidden")
    {
      // and it's not disabled
      if (getContentForm()[i].disabled != true)
      {
      	if(count == number)
      	{
      	  // set the focus to it
          getContentForm()[i].focus();
          break;
        }
        else
        {
          //increment count
          count++;
        }
      }
	}
  }
}
 -->