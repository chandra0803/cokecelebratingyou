// $Id: popaction.js,v 1.1 2012/06/15 13:13:01 babu Exp $
var popups = new Array();

/**
    This function is the main function for displaying modal popup dialogs.  The
    function expects at least ONE argument:  The URL to which to send the Ajax
    request.  After the URL, the arguments can be name/value pairs that should
    be added to the query string.  For example, this:
 
    showPopup("helloWorld.do", "firstName", "Bob");
 
    Sends the Ajax request to helloWorld.do, and appends "firstName=Bob" to 
    the query string.
 
    This function also takes care of showing the modal overlay.
*/
function showPopup() {
    showOverlay();

    var url = arguments[0];
    var ajaxRequest = createAjaxRequest(url);
    addPopupPostRequest(ajaxRequest);
    
    for (var i = 1; i < arguments.length - 1; i += 2) {
        var name = arguments[i];
        var value = arguments[i+1];
        ajaxRequest.addNameValuePair(name,value);
    }
    
    ajaxRequest.sendRequest();
}

/**
    Assumes everything on the query string has been added.
*/
function showPopupWithAjaxRequest(request) {
    showOverlay();
    addPopupPostRequest(request);
    request.sendRequest();
}

function addPopupPostRequest(request) {
    if(request.getPostRequest() != null) {
        //preserve the current postRequest while adding showPopupPostRequest
        var pr = request.getPostRequest();
        request.setPostRequest(function(req) { pr(); showPopupPostRequest(); } );
    }
    else {
        //simply set it
        request.setPostRequest(showPopupPostRequest);
    }
}

/**
    Handles the post request of an Ajax popup dialog.  Any existing popups 
     are hidden and the new one is displayed.
*/
function showPopupPostRequest() {
    //hide the current popup
    hide(getActivePopup());
    
    //reset the array of popups
    popups = findByClassName("popactionnew", "div", null);
    
    //display the active popup
    display(getActivePopup());
}

/**
    Dismisses all of the existing popups on the page.
*/
function dismissAllPopups() {
    var documentBody = document.getElementsByTagName("body")[0];
    while(popups.length > 0) {
        documentBody.removeChild(popups.pop());
    }
    
    hideOverlay();
}

/**
    Dismisses the active popup.
*/
function dismissActivePopup() {
    var pop = popups.pop(); 
    hide(pop);
    document.getElementsByTagName("body")[0].removeChild(pop);
    
    //display the now-current popup (the one that was under the one we just dismissed, if one exists)
    display(getActivePopup());
    hideOverlay();
}

/**
    Dimisses the active and the previous popup.
*/
function dismissActiveAndPreviousPopup() {
    for(var q = 0; i < 2; q++) {
        dismissActivePopup();
    }
}

function display(element) {
    if(element != null) {
        element.style.display = "";
    }
}

function hide(element) {
    if(element != null) {
        element.style.display = "none";
    }
}

/**
    Returns a reference to the active popup, which is a <div class="popactionnew"> element.
*/
function getActivePopup() {
    var currentPopup = null;
    if(popups.length > 0) {
        currentPopup = popups[popups.length - 1];
    }
    return currentPopup;
}

function showOverlay() {
    scrollToTop();
    var overlay = document.getElementById("popoverlay");
    display(overlay);
    hideSelects();
    if ( overlay != null ) {
       if (document.getElementsByTagName("body")[0].scrollHeight > document.getElementsByTagName("body")[0].offsetHeight) {
         overlay.style.height = (document.getElementsByTagName("body")[0].scrollHeight + 50 )+ "px" ;
       } else {
         overlay.style.height = (document.getElementsByTagName("body")[0].offsetHeight + 50 )+ "px" ;
       }
    }
}

function hideOverlay() {

    //only hide the overlay if there are no open popups
    if(popups.length == 0) {
        hide(document.getElementById("popoverlay"));
        showSelects();
        
        //scroll to the previous coordinates
        scrollToPrevious();
    }
}

function hideSelects() {
    var selects = document.getElementsByTagName("select");
    for(var i = 0; i < selects.length; i++) {
        selects[i].style.visibility = "hidden";
    }
}

function showSelects() {
    var selects = document.getElementsByTagName("select");
    for(var i = 0; i < selects.length; i++) {
        selects[i].style.visibility = "visible";
    }
}

//
// Stuff for URL-driven modal popups.
// Such a popup contains an IFRAME loaded with the given URL.
//

var POPUP_SIZE_SMALL = 'Small';
var POPUP_SIZE_MEDIUM  = 'Medium';
var POPUP_SIZE_LARGE = 'Large';

var _saveModalPopupFrameSrc = null;

function openModalPopup( url, size, showLoadingIndicator ) {
  if ( !size ) size = POPUP_SIZE_MEDIUM;
  _saveModalPopupFrameSrc = getModalPopupFrame().src;  
  getModalPopupFrame().src = requestBaseURI + url;
  showOverlay();
  getModalPopup().className = 'modalPopupBase modalPopup' + size;
  display( getModalPopup() );
  getModalPopupClose().focus();
  showModalPopupLoadingIndicator( showLoadingIndicator );
}
function openPurlModalPopup( url, size, showLoadingIndicator ) {
  if ( !size ) size = POPUP_SIZE_LARGE;
  _saveModalPopupFrameSrc = getModalPopupFrame().src;  
  getModalPopupFrame().src = url;
  showOverlay();
  getModalPopup().className = 'modalPopupBase modalPopup' + size;
  display( getModalPopup() );
  getModalPopupClose().focus();
  showModalPopupLoadingIndicator( showLoadingIndicator );
}

function closeModalPopup() {
  hide( getModalPopup() );
  hideOverlay() ;
  getModalPopupFrame().src = _saveModalPopupFrameSrc;
}

function getModalPopup() {
  return document.getElementById('modalPopup');
}

function getModalPopupFrame() {
  return document.getElementById('modalPopupFrame');
}

function getModalPopupClose() {
  return document.getElementById('modalPopupClose');
}

function showModalPopupLoadingIndicator( showLoadingIndicator ) {
  document.getElementById('modalPopupLoadingIndicator').style.display = ( showLoadingIndicator ? 'block' : 'none' );
}
