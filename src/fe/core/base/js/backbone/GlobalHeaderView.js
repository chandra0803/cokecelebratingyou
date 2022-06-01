/*exported GlobalHeaderView */
/*global
TemplateManager,
GlobalHeaderView:true
*/
GlobalHeaderView = Backbone.View.extend( {


    el: '#header',

    //override super-class initialize function
    initialize: function( opts ) {
        'use strict';
        var that = this;
        console.log( '[INFO] GlobalHeaderView:', this, opts, this.options );

        // contain the model in the view if it stays simple
        this.model = new Backbone.Model();
        this.showMenu = true;
        this.page = opts.page;
        this.options.globalHeader = that.options.globalHeader || G5.props.globalHeader || {};        
        this.options.globalHeader.loggedIn = opts.loggedIn;

        if( G5.props.NEW_REDEEM_MENU ) {
            var redeemObj = JSON.parse( G5.props.NEW_REDEEM_MENU );
            this.options.globalHeader.redeem = redeemObj.data;           		   
        } 
	    else {			
            this.options.globalHeader.redeem = {};
        }
		
        this.redeemMenu = that.options.globalHeader.redeem;     

		
        //listen for global data updates intended for parti. profile - and update data
        G5.ServerResponse.on( 'dataUpdate_participantProfile', function( data ) {
            this.model.set( data );
        }, this );

        // listen for data change and update
        this.model.on( 'change:points', this.updateView, this );
        this.render( opts );    },
	

    render: function( opts ) {
        'use strict';
        if( opts.loggedIn === true ) {
         this.loadPoints();
         this.loadRedeem();
        } else {
            this.templateUpdate();
        }       
        return this;
    },	

    templateUpdate: function() {
        var that = this;
        TemplateManager.get( 'globalHeaderView', function( tpl ) {
            that.$el.find( '#globalHeaderView' ).remove();
            that.$el.find( '.container' ).append( tpl( that.options.globalHeader ) );            
            that.updateView();
        }, G5.props.URL_TPL_ROOT || G5.props.URL_BASE_ROOT + 'tpl/' );
			 
        return that;
    },
    loadRedeem: function() {
        var that = this,
	        menuStatus = false,
            globalRedeemMenu = this.redeemMenu;
          

            if( G5.props.NEW_REDEEM_MENU ) {
				
				globalRedeemMenu.menu.sort( that.compare );
				
                if( globalRedeemMenu.menu.length > 1 ) {                
                        globalRedeemMenu.is_double_menu = true;
                } 
                else {
                        globalRedeemMenu.is_double_menu = false;
                }
    
                if( globalRedeemMenu.is_double_menu ) {               
                        globalRedeemMenu.status = true;
                        globalRedeemMenu.is_dm_exist = true;
                        globalRedeemMenu.is_rm_icon = false;
                        globalRedeemMenu.no_rm_exist = true;
                        menuStatus = true;
                } 
                else {                
                        globalRedeemMenu.status = true;
                        globalRedeemMenu.is_dm_exist = false;
                        globalRedeemMenu.is_rm_icon = true;
                        globalRedeemMenu.no_rm_exist = true;                    
                        globalRedeemMenu.rm_btn_url = globalRedeemMenu.menu[ 0 ].url;
                        menuStatus = true;
                }
           } 
           else {
                  globalRedeemMenu.no_rm_exist = false;
                  globalRedeemMenu.status = false;
                  menuStatus = true;
           }
    
           if( menuStatus ) {
                that.templateUpdate();
           }  
    },
    loadPoints: function() {   
        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_PARTICIPANT_PROFILE_POINTS
        } );
    },

    compare: function( a, b ) {
        
        var comparison = 0;
        if( a.pos > b.pos ) {
            comparison = 1;
        } 
	    else if( a.pos < b.pos ) {
            comparison = -1;
        }
        
        return comparison;
    },

    //update view with latest model data
    updateView: function() {
        var data = this.model.toJSON(),
            $ptCt = this.$el.find( '.pointsContainer' ),
            $pts = this.$el.find( '.pointsLink' );

        // POINTS UPDATE
        // check to see if the automatic data update comes back with a N/A bank account
        // changed check by vdudam to -1 for maintaining both conditions with same datatype
        if( data.points == -1 ) {
            $ptCt.removeClass( 'showPoints' ).addClass( 'hidePoints' );
        }
        // otherwise, assume it's a number of points coming back
        else if( typeof data.points === 'number' ) {
            $ptCt.addClass( 'showPoints' ).removeClass( 'hidePoints' );

            if( data.points > 0 ) {
                //animate the points as they change
                $pts.addClass( 'changing' ).animateNumber( data.points, 1500, {
                    addCommas: true
                }, function() {$pts.removeClass( 'changing' );} );
	    } 
	    else {
	        $pts.text( data.points );
            }
        }
	
        //if points are not turned on hide
	if( data.showPoints !== true ) {
	     $ptCt.removeClass( 'showPoints' ).addClass( 'hidePoints' );
        }
     }
     
} );
