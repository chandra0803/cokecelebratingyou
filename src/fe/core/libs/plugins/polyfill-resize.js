/*
DragResize v1.0@74147644ad1f26597f1c8d1c42a065f57d9b2e8b
(c) 2005-2013 Angus Turnbull, TwinHelix Designs http://www.twinhelix.com

Licensed under the GNU LGPL, version 3 or later:
https://www.gnu.org/copyleft/lesser.html
This is distributed WITHOUT ANY WARRANTY; without even the implied
warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
*/
if("function"!=typeof addEvent){var addEvent=function(e,t,n,l){var m="addEventListener",d="on"+t,i=e,o=t,a=n,s=l;if(e[m]&&!l)return e[m](t,n,!1);e._evts||(e._evts={}),e._evts[t]||(e._evts[t]=e[d]?{b:e[d]}:{},e[d]=new Function("e",'var r=true,o=this,a=o._evts["'+t+'"],i;for(i in a){o._f=a[i];r=o._f(e||window.event)!=false&&r;o._f=null}return r'),"unload"!=t&&addEvent(window,"unload",function(){removeEvent(i,o,a,s)})),n._i||(n._i=addEvent._i++),e._evts[t][n._i]=n};addEvent._i=1;var removeEvent=function(e,t,n,l){var m="removeEventListener";if(e[m]&&!l)return e[m](t,n,!1);e._evts&&e._evts[t]&&n._i&&delete e._evts[t][n._i]}}function cancelEvent(e,t){e.returnValue=!1,e.preventDefault&&e.preventDefault(),t&&(e.cancelBubble=!0,e.stopPropagation&&e.stopPropagation())}function DragResize(e,t){var n={myName:e,enabled:!0,handles:["tl","tm","tr","ml","mr","bl","bm","br"],isElement:null,isHandle:null,element:null,handle:null,minWidth:10,minHeight:10,minLeft:0,maxLeft:9999,minTop:0,maxTop:9999,gridX:1,gridY:1,zIndex:1,mouseX:0,mouseY:0,lastMouseX:0,lastMouseY:0,mOffX:0,mOffY:0,elmX:0,elmY:0,elmW:0,elmH:0,allowBlur:!0,ondragfocus:null,ondragstart:null,ondragmove:null,ondragend:null,ondragblur:null};for(var l in n)this[l]=void 0===t[l]?n[l]:t[l]}DragResize.prototype.apply=function(e){var t=this;addEvent(e,"mousedown",function(e){t.mouseDown(e)}),addEvent(e,"mousemove",function(e){t.mouseMove(e)}),addEvent(e,"mouseup",function(e){t.mouseUp(e)}),addEvent(e,"touchstart",function(e){t.mouseDown(e)}),addEvent(e,"touchmove",function(e){t.mouseMove(e)}),addEvent(e,"touchend",function(e){t.mouseUp(e)})},DragResize.prototype.select=function(newElement){with(this){if(!document.getElementById||!enabled)return;if(newElement&&newElement!=element&&enabled){element=newElement,element.style.zIndex=++zIndex,this.resizeHandleSet&&this.resizeHandleSet(element,!0);var eCS=element.currentStyle||window.getComputedStyle(element,null);eCS.right&&(element.style.left=element.offsetLeft+"px",element.style.right=""),eCS.bottom&&(element.style.top=element.offsetTop+"px",element.style.bottom=""),elmX=parseInt(element.style.left),elmY=parseInt(element.style.top),elmW=this.elmW,elmH=this.elmH,ondragfocus&&this.ondragfocus()}}},DragResize.prototype.deselect=function(delHandles){with(this){if(!document.getElementById||!enabled)return;delHandles&&(ondragblur&&this.ondragblur(),this.resizeHandleSet&&this.resizeHandleSet(element,!1),element=null),handle=null,mOffX=0,mOffY=0}},DragResize.prototype.mouseDown=function(e){with(this){if(!document.getElementById||!enabled)return!0;e.touches&&e.touches.length&&this.mouseMove(e);for(var elm=e.target||e.srcElement,newElement=null,newHandle=null,hRE=new RegExp(myName+"-([trmbl]{2})","");elm;){if(elm.className&&(newHandle||!hRE.test(elm.className)&&!isHandle(elm)||(newHandle=elm),isElement(elm))){newElement=elm;break}elm=elm.parentNode}element&&element!=newElement&&allowBlur&&deselect(!0),!newElement||element&&newElement!=element||(newHandle&&cancelEvent(e),select(newElement,newHandle),handle=newHandle,handle&&ondragstart&&this.ondragstart(hRE.test(handle.className)))}},DragResize.prototype.mouseMove=function(e){with(this){if(!document.getElementById||!enabled)return!0;var mt=e.touches&&e.touches.length?e.touches[0]:e;mouseX=mt.pageX||mt.clientX+document.documentElement.scrollLeft,mouseY=mt.pageY||mt.clientY+document.documentElement.scrollTop;var diffX=mouseX-lastMouseX+mOffX,diffY=mouseY-lastMouseY+mOffY;if(mOffX=mOffY=0,lastMouseX=mouseX,lastMouseY=mouseY,!handle)return!0;var isResize=!1;if(this.resizeHandleDrag&&this.resizeHandleDrag(diffX,diffY))isResize=!0;else{var dX=diffX,dY=diffY;elmX+dX<minLeft?mOffX=dX-(diffX=minLeft-elmX):elmX+elmW+dX>maxLeft&&(mOffX=dX-(diffX=maxLeft-elmX-elmW)),elmY+dY<minTop?mOffY=dY-(diffY=minTop-elmY):elmY+elmH+dY>maxTop&&(mOffY=dY-(diffY=maxTop-elmY-elmH)),elmX+=diffX,elmY+=diffY}if(element.style.left=Math.round(elmX/gridX)*gridX+"px",element.style.top=Math.round(elmY/gridY)*gridY+"px",isResize&&(element.style.width=Math.round(elmW/gridX)*gridX+"px",element.style.height=Math.round(elmH/gridY)*gridY+"px"),window.opera&&document.documentElement){var oDF=document.getElementById("op-drag-fix");if(!oDF){var oDF=document.createElement("input");oDF.id="op-drag-fix",oDF.style.display="none",document.body.appendChild(oDF)}oDF.focus()}ondragmove&&this.ondragmove(isResize),cancelEvent(e)}},DragResize.prototype.mouseUp=function(e){with(this){if(!document.getElementById||!enabled)return;var hRE=new RegExp(myName+"-([trmbl]{2})","");handle&&ondragend&&this.ondragend(hRE.test(handle.className)),deselect(!1)}},DragResize.prototype.resizeHandleSet=function(elm,show){with(this){if(!elm._handle_tr)for(var h=0;h<handles.length;h++){var hDiv=document.createElement("div");hDiv.className=myName+" "+myName+"-"+handles[h],elm["_handle_"+handles[h]]=elm.appendChild(hDiv)}for(var h=0;h<handles.length;h++)elm["_handle_"+handles[h]].style.visibility=show?"inherit":"hidden"}},DragResize.prototype.resizeHandleDrag=function(diffX,diffY){with(this){var hClass=handle&&handle.className&&handle.className.match(new RegExp(myName+"-([tmblr]{2})"))?RegExp.$1:"",dY=diffY,dX=diffX,processed=!1;return hClass.indexOf("t")>=0&&(elmH-dY<minHeight?mOffY=dY-(diffY=elmH-minHeight):elmY+dY<minTop&&(mOffY=dY-(diffY=minTop-elmY)),elmY+=diffY,elmH-=diffY,processed=!0),hClass.indexOf("b")>=0&&(elmH+dY<minHeight?mOffY=dY-(diffY=minHeight-elmH):elmY+elmH+dY>maxTop&&(mOffY=dY-(diffY=maxTop-elmY-elmH)),elmH+=diffY,processed=!0),hClass.indexOf("l")>=0&&(elmW-dX<minWidth?mOffX=dX-(diffX=elmW-minWidth):elmX+dX<minLeft&&(mOffX=dX-(diffX=minLeft-elmX)),elmX+=diffX,elmW-=diffX,processed=!0),hClass.indexOf("r")>=0&&(elmW+dX<minWidth?mOffX=dX-(diffX=minWidth-elmW):elmX+elmW+dX>maxLeft&&(mOffX=dX-(diffX=maxLeft-elmX-elmW)),elmW+=diffX,processed=!0),processed}};

/*
 * resize-polyfill 1.1.0
 * (c) 2005-2013 Cezary Daniel Nowak
 * https://github.com/CezaryDanielNowak/css-resize-polyfill
 * Licenced under the MIT
 */
( function() {
  var resizeSupported =
    document.createElement( 'textarea' ).style.resize !== undefined;
  var resizeHandlerPolyfill = function( target, force ) {
    if ( !target ) {
      return resizeSupported ? 'native' : 'polyfill';
    }

    if ( target.length ) {
      // element list provided
      for ( var i = target.length; i--; ) {
        resizeHandlerPolyfill( target[ i ], force );
      }
    } else {
      if ( target.tagName === 'TEXTAREA' ) {
        target = target.parentNode;
      }
      target.className += ' resize-polyfill-wrapper';
      if ( resizeSupported && !force ) {
        return;
      }

      var currentClientWidth,
        initialBoxHeight = 160;

      if( window.outerWidth < 1200 && window.outerWidth > 979 ) {
        currentClientWidth = 526;
      } else if ( window.outerWidth < 980 && window.outerWidth > 767 ) {
        currentClientWidth = 346;
      } else if ( window.outerWidth < 768 ) {
        currentClientWidth = 226;
      } else {
        currentClientWidth = 688;
      }

      var dragresize = new DragResize( 'resize-polyfill', {
        handles: [ 'br' ],
        minWidth: 50,
        minHeight: 50,
        elmW: currentClientWidth,
        elmH: initialBoxHeight,
        allowBlur: false
      } );

      dragresize.isElement = function( elm ) {
        return elm === target;
      };
      dragresize.isHandle = function( elm ) {
        return false;
      };

      dragresize.ondragfocus = function() {};
      dragresize.ondragstart = function( isResize ) {};
      dragresize.ondragmove = function( isResize ) {};
      dragresize.ondragend = function( isResize ) {};
      dragresize.ondragblur = function() {};

      var child = target.children[ 0 ];
      var topParent = child.parentNode.parentNode;
      if( topParent && topParent.nextElementSibling !== null ) {
        target.style.marginBottom = 40 + 'px';
      }

      if ( child && child.tagName === 'TEXTAREA' ) {
        target.style.width = child.offsetWidth + currentClientWidth + 'px';
        target.style.height = child.offsetHeight + initialBoxHeight + 'px';
        child.style.resize = 'none';
      } else {
        target.style.resize = 'none';
      }

      dragresize.apply( document );
      dragresize.select( target ); // required to show handler on bottom-right
      target.className += ' resize-polyfill-polyfilled';
    }
  };

  if ( typeof module !== 'undefined' ) {
    module.exports = resizeHandlerPolyfill;
  } else {
    window.resizeHandlerPolyfill = resizeHandlerPolyfill;
  }

  if ( typeof window !== 'undefined' ) {
    var css =
        '.resize-polyfill-wrapper {position: relative;top: auto !important;left: auto !important;}\n\
.resize-polyfill-polyfilled textarea {width: 100%;height: 100%;}\n\
.resize-polyfill-br {background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAgAAAAICAYAAADED76LAAAALUlEQVR42mNgwAH279//H4QZyFYAAv///5fBqQurCQQVEHQMVjaMAQQc2BQAABXMU79BvB5bAAAAAElFTkSuQmCC") no-repeat center center;border: 2px solid transparent;bottom: 0px;cursor: se-resize;height: 8px;position: absolute;right: 0px;width: 8px;}',
      head = document.head || document.getElementsByTagName( 'head' )[ 0 ],
      style = document.createElement( 'style' );

    if ( style.styleSheet ) {
      style.styleSheet.cssText = css;
    } else {
      style.appendChild( document.createTextNode( css ) );
    }

    head.appendChild( style );
  }
} )();