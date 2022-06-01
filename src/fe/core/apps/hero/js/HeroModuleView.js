/*exported HeroModuleView */
/*global
Modernizr,
LaunchModuleView,
PaxSearchStartView,
HeroModuleCollection,
HeroModuleView:true
*/
HeroModuleView = LaunchModuleView.extend( {
    initialize: function( opts ) {
        console.log( '[INFO] HeroModuleView: Hero Module View initialized', opts );

        //this is how we call the super-class initialize function (inherit its magic)
        LaunchModuleView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass LaunchModuleView
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events );

        //handy storage
        this.app = opts.app;
        this.collection = opts.app.moduleCollection;

        // search and EZ rec
        this.paxSearchStartView = null;
        this.recognitionEzView = null;
        this.resize = true;

        // Turn on and off for follow option 
        this.allowFollow = this.app.options.allowFollow;
        
        // render callback
        this.on( 'templateLoaded', this.postRender, this );

        // listen for global scroll events
        this.on( 'templateLoaded', function() {
            G5._globalEvents.on( 'windowScrolled', this.handleScroll, this );
        }, this );

        // listen for app filter changes
        G5._globalEvents.on( 'route', this.enableDisableSearch, this );
        G5._globalEvents.on( 'route', this.handleScroll, this );

        // listen for ez recognition event
        G5._globalEvents.on( 'openEZrecognition', this.doEzRecognize, this );
    },
    handleScroll: function() {
    	var $body, $bodyoh, $hero, $herooh, bst, hot, scrollPct, hmh, hsr, hptpct, bgData, bgImg, cmImgExists;
    	$body = this.$body;
        $bodyoh = this.$body.outerHeight();
        $hero = this.$liner.not( '.module-empty' );
        $herooh = this.$liner.not( '.module-empty' ).outerHeight();
        size = $hero.data( 'size' );
        bgData = $hero.data( 'img' );
        bgImg = $hero.attr("style");
        bst = Math.max( 0, window.pageYOffset );
        hot = $hero.offset().top;
        scrollPct = Math.min( 1, bst / hot );
        hmh = $hero.css( 'min-height' );
        if ( !this.rendered ) {
            return false;
        }
        if ( $hero.closest( '.is-home' ).length ) {
        	checkRatio(size);
        	cmImgExists = checkCmValue( "background-image", bgImg );
            hsr = setSize.ratio.split( ':' );
            //hsr = $hero.data( 'size' ).ratio.split( ':' );
            hptpct = ( hsr[ 1 ] / hsr[ 0 ] * ( 1 - scrollPct ) * 100 );
            /* commented out intentionally to discuss this calculation because this is affecting the rest of the calculation
             if( ( $bodyoh - parseInt( hmh, 10 ) - $herooh ) <  window.outerHeight  || this.$body.hasClass( 'sidebar-expanded' ) ) {
            	return false;
            }
            */
            if( !cmImgExists ){
            	$hero.css({
            		'background-image': 'url(' + bgData + ')'
            	});
            }
            $hero.css( {
            	'min-height': hmh,
                'padding-top': 'calc(' + hptpct + '% - ' + hmh + ')'
                
            } );
        } else {
            $hero.css( {
            	'background-image': '',
                'min-height': '',
                'padding-top': ''
            } );
        }
        function checkRatio(size) { //Checking and setting ratio based on the admin system var values
        	if(typeof size === "string") {
        		size = size.toLowerCase(); //always size has to be in lower case for case check
        	} else {
				size = "large"; //Sending the size as default if any mismatches found
        	}
        	        	
            switch (size) {
            case "large":
                setSize = {"ratio":"2.35:1"};
                break;
            case "medium":
                setSize = {"ratio":"4.7:1"};
                break;
            case "small":
            setSize = {"ratio":"9.4:1"};
            break; 
            default:
                setSize = {"ratio":"2.35:1"};
                //console.log("either value of the size varies or not given");
            }
        }
        function checkCmValue  ( prop,context ) {
            var styles = context,value;            
            styles && styles.split(";").forEach(function (e) {
                   var style = e.split(/:(.+)/);
                   if ($.trim(style[0]) === prop) {
                       value = style[1].replace(/[()url'"]/g, '');   
                   } 
               });  
           return value;
        }
        

        // don't shrink on touch devices when the input has focus
        // let the device center the input on-screen however it chooses
        if( Modernizr.touch && this.$search.find( '.paxSearchInput' ).is( ':focus' ) ) {
            // scroll the body back to the top when the touch keyboard is put away
            this.$search.find( '.paxSearchInput' ).one( 'blur', function() {
                $body.scrollTop( 1 );
            } );
            return false;
        }

        
    },

    postRender: function() {
        // storing for fun and profit
        this.$body = $( 'body' );
        this.$search = this.$el.find( '.paxSearchStartView' );
        this.initSearch();
        this.rendered = true;
        if(this.rendered) {
        	this.handleScroll();
        }


        // CALL ez recognition - testing only
        /*var that=this;
        setTimeout(function(){
            that.doEzRecognize([{id:0}]);
        },1000);*/
    },

    initSearch: function() {
        var that = this,
            newFilterOpts = [];

        // generate a list of all filters
        this.filters = _.map( this.model.get( 'filters' ), function( val, key ) {
            return {
                name: key,
                props: val,
                isHome: that.collection.getHomeFilter() === key
            };
        } );

        // find all the filters where search is enabled
        this.searchEnabledFilters = _.pluck( _.filter( this.filters, function( f ) {
            return f.props.searchEnabled === true;
        } ), 'name' );

        // clean out any filters that have search enabled but the module is hidden
        // AND hide the module on any non-home filters that don't have search enabled...
        _.each( this.filters, function( filter ) {
            if ( filter.props.searchEnabled === true && filter.props.order === 'hidden' ) {
                filter.props.searchEnabled = false;
                that.searchEnabledFilters = _.without( that.searchEnabledFilters, filter.name );
            }
            if ( filter.props.searchEnabled === false && filter.isHome !== true ) {
                filter.props.order = 'hidden';
            }
            newFilterOpts[ filter.name ] = filter.props;
        } );

        this.model.set( 'filters', newFilterOpts );

        // if any filters have search enabled, we initialize the start view
        if ( this.searchEnabledFilters.length ) {
            this.paxSearchStartView = new PaxSearchStartView( {
                follow: this.allowFollow,
                addSelectedPaxView: true,
                multiSelect: true,
                recognition: true,
                selectGroup: true,
                el: this.$search
            } );
            this.enableDisableSearch();
        }

        // hide ezrec
        //this.$el.find('.ezRecView').hide();

    },
    enableDisableSearch: function() {
        var currFilter = this.collection.getFilter();

        // if the current filter is in the list of searchEnabled filters, show the search
        if ( _.indexOf( this.searchEnabledFilters, currFilter ) >= 0 ) {
            this.$el.addClass( 'searchEnabled' );
        } else {
            this.$el.removeClass( 'searchEnabled' );
        }
    },

    handleResize: function() {
        this.handleScroll();
    },

    

    /*
    turnOffResize: function(){
        this.resize = false;
    },
    turnOnResize: function(){
        this.resize = true;
        //call scroll right away to reset - maybe have this animate so it is smoother
        this.handleScroll();
    },
    scrollHeroTop: function(){
        this.$body.scrollTop(0);
        //call scroll right away to reset - maybe have this animate so it is smoother
        this.handleScroll();
    },
    */

    /*
    doEzRecognize: function(participants) {
        console.log('doEzRecognize???',this);
        this.$el.find('.ezRecView').show();

        this.turnOffResize();
        this.scrollHeroTop();
        this.paxSearchStartView.hide();
        this.$el.addClass('inner-module-loaded');

        console.log(participants);
        if( this.recognitionEzView ) {
            this.recognitionEzView.updateRecipient( participants[0][0], participants[0].promotion  );
            console.log('show???');
        }
        // TODO: make name val pairs for this
        else {
            this.recognitionEzView = new RecognitionEzView({
                recipient : participants[0][0],
                promotion : participants[0].promotion,
                el        : this.$el.find('.ezRecView'),
                close     : function () {
                    //self.resetModule();
                }
            });

            this.recognitionEzView.on('closed', this.closeEzView, this);

            this.recognitionEzView.on('templateReady', function(){

                //this.recognitionEzView.show();
                this.recognitionEzView.$el.find('.ezRecLiner').fadeIn(); // the View hides itself. we need to reshow it
            }, this);
        }

    },
    */
   /*
    closeEzView:function(){
        console.log('closeEzView');
        // hide ezrec
        this.$el.find('.ezRecView').hide();

        // this.recognitionEzView.resetModule();
        // this.recognitionEzView.undelegateEvents();
        // this.recognitionEzView=null;
        this.$el.removeClass('inner-module-loaded');
        // reset resize
        this.turnOnResize();
        this.paxSearchStartView.show();
    },
    */

    resetModule: function() {
        // this.recognitionEzView.$el.hide();
        // this.paxSearchView.$el.show();
    }
} );
