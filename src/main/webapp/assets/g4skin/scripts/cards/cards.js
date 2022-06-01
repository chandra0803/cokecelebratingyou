<!--
YAHOO.namespace("YAHOO.bi");

YAHOO.bi.Hover = (function()
{
	/* =================== */
	/*   Private Members   */
	/* =================== */
	/**
	* Get all animatable colors from the element
	* @param {DomNode} el The elemnent to read the colors from
	* @returns {Object}
	*/
	function getColors(el)
	{
		var dom = YAHOO.util.Dom;
		var colors = {};
		colors.color = dom.getStyle(el, "color");
		colors.backgroundColor = dom.getStyle(el, "backgroundColor");
		colors.borderTopColor = dom.getStyle(el, "borderTopColor");
		colors.borderRightColor = dom.getStyle(el, "borderRightColor");
		colors.borderBottomColor = dom.getStyle(el, "borderBottomColor");
		colors.borderLeftColor = dom.getStyle(el, "borderLeftColor");

		// No background animation for images
		if(dom.getStyle(el, "backgroundImage") != "none"){
			colors.backgroundColor = null;
		}

		// Resolve transparency
		if(isTransparent(colors.backgroundColor)){
			colors.backgroundColor = resolveTransparency(el);
		}

		return colors;
	}

	/**
	* Detect if a color string means transparent
	* @param {String} color The color string
	* @returns boolean
	*/
	function isTransparent(color){
		return (color == "transparent" || color == "rgba(0, 0, 0, 0)");
	}

	/**
	* Resolve tranparent background.
	* Climb up node heirarcy until you find a defined background color
	* @param {DomNode} el The element to get the background for
	*/
	function resolveTransparency(el)
	{
		var dom = YAHOO.util.Dom;
		var node = el;
		var bg = dom.getStyle(node, "backgroundColor");

		while(node && isTransparent(bg)){
			node = node.parentNode
			if(node == document){
				break;
			}
			bg = dom.getStyle(node, "backgroundColor");
			if(dom.getStyle(node, "backgroundImage") != "none"){
				bg = null;
				break;
			}
		}
		return bg;
	}

	/**
	* Get animation attributes
	* @param {DomNode} el The element that will be animated
	* @param {String} dir The hover direction ("on" or "off")
	* @param {Object} attributes The base custom attributes object
	* @return {Object} The attributes object
	*/
	function generateAttributes(el, dir, attributes)
	{
		var attr = attributes;
		var dom = YAHOO.util.Dom;
		var from = el.hoverConfig.colorsOff;
		var to = el.hoverConfig.colorsOff;

		// Hover on
		if(dir == "on")
		{
			if(el.hoverConfig.colorsOn)
			{
				to = el.hoverConfig.colorsOn;
			}
			else
			{
				dom.addClass(el, el.hoverConfig.className);
				to = getColors(el);
				el.hoverConfig.colorsOn = to;
				dom.removeClass(el, el.hoverConfig.className);

				// Reset colors
				for(prop in from){
					dom.setStyle(el, prop, from[prop]);
				}
			}
		}
		// Hover off
		else
		{
			from = el.hoverConfig.colorsOn;
		}

		// Diff and creat config
		for(prop in from){

			if(from[prop] && to[prop] && from[prop] != to[prop] && !isTransparent(to[prop]) && !isTransparent(from[prop])){
				attr[prop] = { 'to' : to[prop] };
			}
		}

		return attr;
	}

	/**
	* Start hover animation
	* @param {DomNode} el The element to hover
	*/
	function hoverOn(el, i)
	{
		var radio = getSelectedElementIndex( el ) ;
		if ( radio!=null && radio.checked==true )
			return ;
			
	        YAHOO.util.Dom.replaceClass(el, "outerCard", "outerCardOn");
		//YAHOO.util.Dom.setStyle(el, "border", "1px solid #000000");
		//generateAttributes(el, "on", el.hoverConfig.attrOn), el.hoverConfig.durationOn, el.hoverConfig.methodOn)
		//(new YAHOO.util.ColorAnim(el,{ backgroundColor: { from: '#FFFFFF', to: '#DEDEDE' } }, 0)).animate();
	}

	function getSelectedElementIndex( el )
	{
		if ( !el.id )
			return null;
			
		if ( el.id.indexOf("_")>-1 )
		{
			var index = el.id.substring( el.id.indexOf("_")+1, el.id.length ) ;
			var radio = document.getElementById( "radio_"+index ) ;
			
			if ( radio )
				return radio ;
			else
				return null ;
		}
		else
			return null ;
	}

	/**
	* Start over off animation
	* @param {DomNode} el The element to hover
	*/
	function hoverOff(el)
	{
		var radio = getSelectedElementIndex( el ) ;
		if ( radio!=null && radio.checked==true )
			return ;
			
	        YAHOO.util.Dom.replaceClass(el, "outerCardOn", "outerCard");
		//YAHOO.util.Dom.setStyle(el, "border", "1px solid #FFFFFF");
		//(new YAHOO.util.ColorAnim(el,{ backgroundColor: { from: '#DEDEDE', to: '#FFFFFF' } }, 0)).animate();
	}

	return {

		/* ================== */
		/*   Public Members   */
		/* ================== */

		/**
		* The default animation duration (in seconds) when the element is hovered on.
		*/
		durationOn : .7,

		/**
		* The default animation duration (in seconds) when the element is hovered off.
		*/
		durationOff : .7,

		/**
		* Setup an element for hovering
		* @param {Object} el The element or element ID to setup
		* @param {String} className The CSS class to be added to envoke the hover colors (you can thank IE for this)
		* @param {Object} config Custom animation object.
		* 	<strong>durationOn</strong>: The animation duration (in seconds) when the element is hovered on.
		* 	<strong>durationOff</strong>: The animation duration (in seconds) when the element is hovered on.
		* 	<strong>methodOn</strong>: The animation method to use when the element is hovered on. (defaults YAHOO.util.Easing.easeNone)
		* 	<strong>methodOff</strong>: The animation method to use when the element is hovered off. (defaults YAHOO.util.Easing.easeNone)
		* 	<strong>attrOn:</style> Extra style attributes to be animated when the element is hoverd on (ie: height, width).
		* 	<strong>attrOff:</style> Extra style attributes to be animated when the element is hoverd off (ie: height, width).
		*/
		addElement : function(el, className, config)
		{
			if(typeof el == "string"){
				el = document.getElementById(el);
			}

			if(!el){
				return;
			}

			if(!config)
			{
				config = {}
			}

			// Set configuration
			el.hoverConfig = {};
			el.hoverConfig.colorsOff = getColors(el);
			el.hoverConfig.className = className
			el.hoverConfig.methodOn = config.methodOn || null;
			el.hoverConfig.methodOff = config.methodOff || null;
			el.hoverConfig.attrOn = config.attrOn || {};
			el.hoverConfig.attrOff = config.attrOff || {};
			el.hoverConfig.durationOn = config.durationOn || this.durationOn;
			el.hoverConfig.durationOff = config.durationOff || this.durationOff;

			// Add events
			YAHOO.util.Event.addListener(el, "mouseover", function(){ hoverOn(el) });
			YAHOO.util.Event.addListener(el, "mouseout", function(){ hoverOff(el) });
		},

		/**
		* Setup all <a> elements on the page for hovering
		* @param {String} className The CSS class to be added to envoke the hover colors (you can thank IE for this)
		* @param {Object} config The configuration object (see addElement() for list).
		*/
		addAllElements : function(className, config)
		{
			var dom = YAHOO.util.Dom;
			var a = dom.getElementsByClassName  ( className , null , null , null ) ;

			for(var i = 0; i < a.length; i++)
			{
				this.addElement(a[i], className, config);
			}
		}
	}
})();
-->