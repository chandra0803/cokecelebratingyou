function createTextPopup( namespace, id, longDesc )
{

    var popupContent =
      '<table border="0" style="margin-top:5px;">' +
      '  <tr>' +
      '    <td align="left">' +
      		longDesc +
      '    </td>' +
      '  </tr>' +
      '</table>';

  var tooltip = new YAHOO.bi.widget.Tooltip(
    namespace + '_ToolTip_' + id,
    {
      context: namespace + '_' + id,
      text: popupContent,
      showDelay: 250,
      autodismissdelay: 999999,
      width: '280px',
      zindex: 9999, /* for spotlight online */
      xy: [0,0] /* to avoid whitespace at page bottom */
    }
  );

  YAHOO.namespace( namespace );
  eval( 'YAHOO.' + namespace + '.productToolTip = tooltip' );
}



function createProductDetailPopup( namespace, id, imageUrl, shortDesc, longDesc, price )
{
  if ( price != null && price != '' )
  {
    var popupContent =
      '<table border="0" style="margin-top:5px;">' +
      '  <tr>' +
      '    <td align="center">' +
      '      <img border="1" src="' + imageUrl + '" width="250" height="250"/>' +
      '    </td>' +
      '  </tr>' +
      '  <tr>' +
      '    <td align="left">' +
      '      <center><b>' + shortDesc + '</b><br>' + price + '</center><br>' + longDesc +
      '    </td>' +
      '  </tr>' +
      '</table>';
  }
  else
  {
    var popupContent =
      '<table border="0" style="margin-top:5px;">' +
      '  <tr>' +
      '    <td align="center">' +
      '      <img border="1" src="' + imageUrl + '" width="250" height="250"/>' +
      '    </td>' +
      '  </tr>' +
      '  <tr>' +
      '    <td align="left">' +
      '      <center><b>' + shortDesc + '</b></center><br>' + longDesc +
      '    </td>' +
      '  </tr>' +
      '</table>';
  }

  var tooltip = new YAHOO.bi.widget.Tooltip(
    namespace + '_ToolTip_' + id,
    {
      context: namespace + '_' + id,
      text: popupContent,
      showDelay: 250,
      autodismissdelay: 999999,
      width: '280px',
      zindex: 9999, /* for spotlight online */
      xy: [0,0] /* to avoid whitespace at page bottom */
    }
  );

  YAHOO.namespace( namespace );
  eval( 'YAHOO.' + namespace + '.productToolTip = tooltip' );
}

function initializeProductDetailPopups( namespace )
{
  YAHOO.util.Event.addListener( window, "load",
    function()
    {
      YAHOO.util.Dom.addClass( document.body, "bi-yui" );
      YAHOO.bi.Hover.addAllElements( namespace + '-tooltip' );
    }
  );
}


<!--
// This is an attempt to fix the YUI overlap prevention.  The YUI method tried moving the "y"
// up to the top and left the x the same.  This hardly ever works for our larger tool tips.
// There are still some issues with this method but it is better than it was.
YAHOO.namespace("bi.widget");

YAHOO.bi.widget.Tooltip = function(el, userConfig) {
	    // chain the constructors
	    YAHOO.bi.widget.Tooltip.superclass.constructor.call(this, el, userConfig);
	};

YAHOO.lang.extend(YAHOO.bi.widget.Tooltip, YAHOO.widget.Tooltip, {
        preventOverlap: function (pageX, pageY) {

            var height = this.element.offsetHeight,
                width = this.element.offsetWidth,
                mousePoint = new YAHOO.util.Point(pageX, pageY),
                elementRegion = YAHOO.util.Dom.getRegion(this.element),
                viewPortWidth = YAHOO.util.Dom.getViewportWidth(),
                viewPortHeight = YAHOO.util.Dom.getViewportHeight(),
                scrollX = YAHOO.util.Dom.getDocumentScrollLeft(),
                scrollY = YAHOO.util.Dom.getDocumentScrollTop(),
                topConstraint = scrollY + 10,
                leftConstraint = scrollX + 10,
                bottomConstraint = scrollY + viewPortHeight - height - 10,
                rightConstraint = scrollX + viewPortWidth - width - 10;

            elementRegion.top -= 5;
            //elementRegion.left -= 5;
            //elementRegion.right += 5;
            elementRegion.bottom += 5;
            var top = elementRegion.top;
            var left = elementRegion.left;
            var right = elementRegion.right;
            var bottom = elementRegion.bottom;
            var centerX = pageX - (width / 2);
            var centerY = pageY - (height / 2);
			var fitsRight = false;
			var fitsLeft = false;
			var fitsTop = false;
			var allowMoveUp = true;
			//var tempLogger = new YAHOO.widget.LogWriter(this.toString());

			if ((pageX+5) <= rightConstraint) {
			   fitsRight = true;
			}
			if ((leftConstraint + width + 5) < pageX) {
			   fitsLeft = true;
			}
			if ((topConstraint + height + 5) < pageY) {
			   fitsTop = true;
			 }
            // always do this
            if (true || elementRegion.contains(mousePoint)) {
                //tempLogger.log("PageX=" +  pageX + ", PageY=" +  pageY + ", height=" + height  + ", width=" + width , "warn");
                //tempLogger.log("viewportHeight=" +  viewPortHeight + ", viewportWidth=" +  viewPortWidth , "warn");
                //tempLogger.log("ScrollX=" +  scrollX + ", ScrollY=" +  scrollY , "warn");
                //tempLogger.log("leftConstraint=" +  leftConstraint + ", rightContstraint=" +  rightConstraint + ", topContstraint=" +  topConstraint + ", bottonContstraint=" +  bottomConstraint , "warn");
                //tempLogger.log("fitsRight=" + fitsRight + ", fitsLeft=" + fitsLeft + ", fitsTop=" + fitsTop , "warn");

                var newX = 0;
                var newY = 0;
                if (fitsRight) {
                  //tempLogger.log("Moving to the right", "warn");
                  newX = pageX + 5;
                  newY = centerY;
                } else if (fitsLeft) {
                  //tempLogger.log("Moving to the left", "warn");
                  newX = pageX - width - 5;
                  newY = centerY;
                } else if (fitsTop) {
                  //tempLogger.log("Moving to the top", "warn");
                  newX = centerX;
                  newY = pageY - height - 5;
                } else {
                  //tempLogger.log("Moving to the bottom", "warn");
                  // all else failed - put it below even if it doesn't fit.
                  newX = centerX;
                  newY = pageY + 5;
                  allowMoveUp = false;
                }
                if (newX < leftConstraint) {
                  //tempLogger.log("Adjusting to the right", "warn");
                  newX = leftConstraint;
                } else if (newX > rightConstraint) {
                  //tempLogger.log("Adjusting to the left", "warn");
                  newX = rightConstraint;
                }
                if (newY < topConstraint) {
                  //tempLogger.log("Adjusting to the top", "warn");
                  newY = topConstraint;
                } else if (allowMoveUp && newY > bottomConstraint) {
                  //tempLogger.log("Adjusting to the bottom", "warn");
                  newY = bottomConstraint;
                }
                //tempLogger.log("newX=" +  newX + ", newY=" +  newY , "warn");
               // this.cfg.setProperty("x", newX);
               // this.cfg.setProperty("y", newY);
                this.cfg.setProperty("xy", [newX, newY]);
            }

        }

});