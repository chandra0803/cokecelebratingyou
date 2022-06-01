var windowScrollX = 0;
var windowScrollY = 0;

function getByClassName(className, tagName, container) {
    return YAHOO.util.Dom.getElementsByClassName(className, tagName, container);
}

function findByClassName(className, tagName, container) {
    return YAHOO.util.Dom.getElementsByClassName(className, tagName, container);
}

function hasClass(elem, className) {
    return YAHOO.util.Dom.hasClass(elem, className);
}

function removeClass(elem, className) {
    YAHOO.util.Dom.removeClass(elem, className);
}

function addClass(elem, className) {
    YAHOO.util.Dom.addClass(elem, className);
}

function scrollToTop() {
    setCurrentScrollPosition();
    
    //scroll to the top
    scroll(0, 0);
}

function setCurrentScrollPosition() {
    if( document.documentElement.scrollLeft != 0 || document.documentElement.scrollTop != 0) {
        //remember the current scroll position
        windowScrollX = document.documentElement.scrollLeft;
        windowScrollY = document.documentElement.scrollTop;
    }
}

function scrollToPrevious() {
    scroll(windowScrollX, windowScrollY);
}

function createAjaxRequest(url) {
    var ajaxRequest = new AjaxRequest(url);
    ajaxRequest.setUsePOST();
    return ajaxRequest;
}

