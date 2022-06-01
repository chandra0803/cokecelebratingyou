/* 
 * Author: Spencer Kathol, Ryan Asleson, Brian Repko
 * Version: 1.1
 * 
 * Sets up the menus to set dynamic style properties that cannot be 
 * described using CSS.  It also implements the SuckerFish methodology to 
 * allow users to use drop-down menus in IE.
 */

window.onload = menu_createDD;

var MENU_HEADER_SPACING = 5;
var SUBMENU_VERTICAL_OFFSET = 0;
var MENU_WIDTH_MARGIN = 15;
var MENU_OVERLAP_IE = 1;
var MENU_OVERLAP_NON_IE = 3;
var MENU_COOKIE_NAME = 'menu_tab';

var menu_agt = navigator.userAgent.toLowerCase();
var menu_is_ie = ((menu_agt.indexOf("msie") != -1) && (menu_agt.indexOf("opera") == -1));
var menu_selected_tab_id;

/**
 * This is the root function which iterates through all ULs with "menu" class
 * and applys the neccessary attributes to create drop-down functionality.
 */
function menu_createDD() {
    var uls = document.getElementsByTagName("UL");
    for (uli = 0; uli < uls.length; uli++) {
        ul = uls[uli];
        if (ul.nodeName == "UL" && ul.className == "menu") {
            // Apply styles and mouse events to Headers
            menu_initializeHeaders(ul);
            menu_createOnMouseOver(ul, 0);
            menu_createOnMouseOut(ul, 0);

            // Process the menus themselves, starting with top-level
            var headers = menu_getChildNodes(ul, "LI");
            for (var headeri = 0; headeri < headers.length; headeri++) {
                var mainULs = menu_getChildNodes(headers[headeri], "UL");
                for (var mainULi = 0; mainULi < mainULs.length; mainULi++) {
                    menu_processMenuUL(mainULs[mainULi], 0);
                }
            }
        }
    }
}

/**
 * Correctly position headers to be spread across the top of their
 * containing block.  Spacing between headers is controlled by the constant 
 * variable MENU_HEADER_SPACING.  
 */
function menu_initializeHeaders(ul) {
    var menu_cookie = cookie_get(MENU_COOKIE_NAME);
    var ULchildren = menu_getChildNodes(ul, "LI");
    var offset = 0;
    for (var i = 0; i < ULchildren.length; i++) {
        var ULchild = ULchildren[i];
        var width = menu_getListElementWidth(ULchild);
        
        // set classname if it is selected
        if (menu_cookie && ULchild.id == menu_cookie) {
            menu_selectTab(ULchild.id, null);
        }
        var links = menu_getChildNodes(ULchild, "A");
        for (var j = 0; j < links.length; j++) {
            menu_createOnClick(links[j]);
        }

        // IE does not seem to like this, but firefox needs it
        if (!menu_is_ie) {
            ULchild.style.width = width + "px";
        }

        ULchild.style.position = "absolute";
        ULchild.style.left = offset + "px";
        
        offset += width + MENU_HEADER_SPACING;
    }
}

/**
 * Processes a given ul, and recursively processes all contained sub-uls.
 */
function menu_processMenuUL(ul, isSubUL) {

    // Process given UL
    menu_initializeMenu(ul, isSubUL); 
    menu_createOnMouseOver(ul, isSubUL);
    menu_createOnMouseOut(ul, isSubUL);

    // Process all subULs
    if (!isSubUL) {
        uls = ul.getElementsByTagName("UL");
        for (var uli2 = 0; uli2 < uls.length; uli2++) {
            subul = uls[uli2];
            menu_processMenuUL(subul, 1);
        }
    }
}

/**
 * Apply class and style for all menus and submenus.
 */
function menu_initializeMenu(ul, isSubUL) {
    
    // Size ULs, LIs, and As to be the width of the longest menu item
    // Increase that length by the value of MENU_WIDTH_MARGIN, which appears
    // as white space on the right hand side of the menu
    var lis = menu_getChildNodes(ul, "LI");
    var largestLI = 0;
    for (var j = 0; j < lis.length; j++) {
        LIwidth = menu_getListElementWidth(lis[j]);
        if (LIwidth > largestLI) {
            largestLI = LIwidth;
        }
    }
    for (var j = 0; j < lis.length; j++) {
        lis[j].style.width = (largestLI + MENU_WIDTH_MARGIN) + "px"; // LIs
        var links = menu_getChildNodes(lis[j], "A"); // As
        for(var k = 0; k < links.length; k++) {
            menu_createOnClick(links[k]);
            if (menu_is_ie) {
                links[k].style.width = (largestLI + MENU_WIDTH_MARGIN - 3) +"px";
            } else {
                links[k].style.width = (largestLI + MENU_WIDTH_MARGIN - 12) +"px";
            }
        }
    }
    ul.style.width = largestLI + MENU_WIDTH_MARGIN + "px"; // UL
    
    // if ul has an "A" sibling, then it is a submenu, and should be styled 
    // accordingly.  In this case, we apply a new the class, which is defined in css.
    var siblings = ul.parentNode.childNodes;
    for (var i = 0; i < siblings.length; i++) {
        if (siblings[i].nodeName == "A"){
            siblings[i].className += " submenuheader";
        }
    }
    
    // Submenus should be vertically even with their corresponding list element,
    // offset by constant variable SUBMENU_VERTICAL_OFFSET;
    if (isSubUL) {
        ul.style.top = ul.parentNode.offsetTop + SUBMENU_VERTICAL_OFFSET + "px";
        var parentOffsetLeft = menu_findParentOffset(ul);
        var parentWidth = parseInt(menu_getParentULWidth(ul).replace("px", ""));
        var myWidth = parseInt(ul.style.width.replace("px", ""));
        var windowWidth = 0;
        if (menu_is_ie) {
            windowWidth = document.body.clientWidth;
        }
        else {
            windowWidth = window.innerWidth;
        }
        var checkThisWidth = parentOffsetLeft + myWidth + parentWidth;
        //
        // Might this submenu run off off the right side of the screen?
        //
        if (checkThisWidth > windowWidth) {
            if (menu_is_ie) {
                ul.style.left = (MENU_OVERLAP_IE - myWidth) + "px";
            } else {
                ul.style.left = (MENU_OVERLAP_NON_IE - myWidth) + "px";
            }
        } else {
            if (menu_is_ie) {
                ul.style.left = (parentWidth - MENU_OVERLAP_IE) + "px";
            } else {
                ul.style.left = (parentWidth - MENU_OVERLAP_NON_IE) + "px";
            }
        }
    }
}

/**
 * If this is an IE browser, (document.all object exists) then change LI element
 * classes to sfhover on mouse over (suckerfish pattern).
 */
function menu_createOnMouseOver(ul, isSubUL) {
    var lis = menu_getChildNodes(ul, "LI");
    for (var i = 0; i < lis.length; i++){
        var li = lis[i];
        if (!isSubUL) {
            li.onmouseover = function() {
                menu_selected_tab_id = this.id;
                if (document.all) {
                    this.className += " sfhover";
                }
            }
        } else {
            li.onmouseover = function() {
                if (document.all) {
                    this.className += " sfhover";
                }
            }
        }
    }
}

/**
 * If this is an IE browser, (document.all object exists) then remove sfhover class
 * from all LI elements on mouse out (suckerfish pattern).
 */
function menu_createOnMouseOut(ul, isSubUL) {
    var lis = menu_getChildNodes(ul, "LI");
    for (var i = 0; i < lis.length; i++){
        li = lis[i];
        li.onmouseout = function() {
            if (document.all) {
                this.className = menu_removeClass(this.className,"sfhover");
                
            }
        }
    }
}

/**
 * Process a selected menu tab change
 * This method is called from clicking any A element under a main LI element
 */
function menu_createOnClick(link) {
    link.onclick = function() {
        if (menu_selected_tab_id) {
            var original = cookie_get(MENU_COOKIE_NAME);
            menu_selectTab(menu_selected_tab_id,original);
            cookie_set(MENU_COOKIE_NAME,menu_selected_tab_id,null);
        }
    }
}

function menu_selectTab(newTabId,oldTabId) {
    if (oldTabId) {
         var tab = document.getElementById(oldTabId);
         if (tab) {
             tab.className = menu_removeClass(tab.className,"selected");
             menu_selected_tab_id = null;
         }
    }
    if (newTabId) {
         var tab = document.getElementById(newTabId);
         if (tab) {
             tab.className += " selected";
             menu_selected_tab_id = newTabId;
         }
    }
}

function menu_removeClass(oldClass,classToRemove) {
    var original = " " + oldClass;
    return original.replace(new RegExp(" " + classToRemove),"");
}

/**
 * Helper function to get the width, in pixels, of text within a given LI 
 * element.  This assumes there is 1 link, or no links within the LI.  If
 * there are more than one link, it will choose the first one, and if there
 * are no links, then it will return 0.
 */
function menu_getListElementWidth(li){
    var LIchildren = menu_getChildNodes(li, "A");
    for (var j = 0; j < LIchildren.length; j++) {
        var LIchild = LIchildren[j];
        return LIchild.offsetWidth;
    }
    return 0;
}

/**
 * The standard getElementsByTagName(name) method gets all DOM objects with 
 * the nodeName value "name" under the root element recursively (including
 * subelements) This method provides a way to retrive the children of "element"
 * with the nodeName "nodeTagName."  
 */
function menu_getChildNodes(element, nodeTagName) {
    var children = element.childNodes;
    var result = new Array();
    var currentIndex = 0;
    for (var childi = 0; childi < children.length; childi++) {
        if (children[childi].nodeName == nodeTagName) {
            result[currentIndex] = children[childi];
            currentIndex++;
        }
    }
    return result;
}

/**
 * DHTML method to get offsetLeft of an object
 */
function menu_calculateOffsetLeft(field) {
    return menu_calculateOffset(field, "offsetLeft");
}

/**
 * DHTML method to get any attribute of a object
 */
function menu_calculateOffset(field, attr) {
    var offset = 0;
    while (field) {
        offset += field[attr]; 
        field = field.offsetParent;
    }
    return offset;
}

/**
 * Get parent menus left offset
 */
function menu_findParentOffset(node) {
    var offsetLeft = 0;
    var newParent = node;
    while (node) {
        if (newParent.parentNode.className == "menu") {
            offsetLeft = menu_calculateOffsetLeft(newParent);
            break;
        }
        newParent = newParent.offsetParent;
    }
    return offsetLeft;
}

/**
 * Get parent menu UL width
 */
function menu_getParentULWidth(node) {
    var parent = menu_getParentUL(node);
    return parent.style.width;
}

/**
 * Get parent menu UL
 */
function menu_getParentUL(ulNode) {
    var currentNode = ulNode;
    while (true) {
        if (currentNode.parentNode == null) {
            break;
        }
        currentNode = currentNode.parentNode;
        if (currentNode.tagName == "UL" || currentNode.tagName == "ul") {
            break;
        }
    }
    return currentNode;
}

function menu_changecss(theClass,element,value)
{
  // if IE, need to hide all of the select box elements
  if (document.all) 
  {
   		var selects = document.getElementsByTagName("SELECT");
   		//var texts is added as a code fix for bug 18209
   		var texts = document.getElementsByTagName("input"); 			 		
      for (var selecti = 0; selecti < selects.length; selecti++) 
      {
          select = selects[selecti];
          if (select.className.indexOf("killme") != -1 )  
          {
              select.style.visibility = value;
          }
      }
      for (var texti = 0; texti < texts.length; texti++) 
  	  {
		  		text = texts[texti];            
		  		if(text.type !=null&&text.type !='undefined' && text.type =='text')
		  		{
		  				if(text.styleClass == null)
		  				{
		  					continue;
		  				}
		  				else if ( text.styleClass.indexOf("killme") != -1 ) 
		  				{
		  					text.style.visibility = value;
		  		  	}
		  		} 	   
      }     
  }
}
	 

