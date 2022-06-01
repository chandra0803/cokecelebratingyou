/*exported LaunchPageView */
/*global
PageView,
TemplateManager,
LaunchApp,
LaunchPageView:true
*/
LaunchPageView = PageView.extend( {

    //override super-class initialize function
    initialize: function( opts ) {
        //set the appname (getTpl() method uses this)
        this.appName = '';
        // TODO: nav will be moved out
        //this.$topNav = this.$el.find('#LaunchTopNav');

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass ModuleView
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        $( 'body' ).addClass( 'launchApp' );

        this.launchApp = new LaunchApp( {
            mcvSelector: '.moduleContainerViewElement', //the DOM root element of the module container
            userHomeFilter: opts.userHomeFilter,
            allowFollow: opts.allowFollow
        } );

        this.launchApp.moduleCollection.on( 'filterChanged', this.handleFilterChange, this );
        G5.views.globalNav.on( 'rendered', this.handleFilterChange, this );

        this.launchApp.launchModuleContainerView.on( 'moduleLayoutChange', this.handleModuleLayoutChange, this );

        this.doNominationWinnerCheck();
    },

    events: {
        //'click .toggler' : 'handleToggleClick'
    },

    handleFilterChange: function( filter ) {
        var obj = G5.views.globalNav.getCurrentNavObj();

        if( filter !== obj.code ) { return false; }
        this.$el.find( '.page-title' ).text( obj.name );
    },

    handleModuleLayoutChange: function( e ) {
        // console.log('moduleLayoutChange', e.type, e.view);
        this.freezeBodyCheck();
    },


    // this is kind of an odd place to put this, given that it's so app-specific, but we haven't come up with a better way as of yet
    doNominationWinnerCheck: function() {
        //var that = this;

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_PARTICIPANT_PROFILE_NOMINATION_WINNER_MODAL/*,
            success: function(servResp) {
                var data = servResp.data.modal;
                that.trigger('nominationCheckDone', data);
            }*/
        } );

        G5.ServerResponse.on( 'dataUpdate_participantProfile', function( data ) {
            this.trigger( 'nominationCheckDone', data );
        }, this );

        this.on( 'nominationCheckDone', this.doNominationWinner, this );
    },

    doNominationWinner: function( data ) {
        var that = this;

        // only show if there is data and displayNominationWinnerModal
        if( !( data && data.modal && data.modal.displayNominationWinnerModal ) ) {
            return false;
        }

        TemplateManager.get( 'nominationsWinnerModal', function( tpl ) {
            var modal = tpl( data.modal ),
                $modal = $( modal );

            that.$el.append( $modal );

            that.$el.find( '#nominationWinnerModal' ).modal( 'show' );

            that.$el.find( '#nominationWinnerModal' ).on( 'shown.bs.modal', function() {
                setTimeout( function() {
                    $( '.modal-backdrop' ).append( $modal.find( '.circleBgWrap' ).show() );
                    $modal.find( '.modal-body' ).addClass( 'toTop' );
                    $modal.find( '.modal-footer' ).addClass( 'toTop' );
                }, G5.props.ANIMATION_DURATION * 3 );
            } );
        } );
    },


    //*****************************************************************************
    // DEVELOPMENT MODE - allow a subset of modules to be loaded
    //*****************************************************************************
    // doDevMode: function( allMods ) {
    //     var getCookSubset = function( c ) {
    //             var c = document.cookie.match( /moduleSubset=([a-z0-9,]+)/i );
    //             return c && c.length == 2 ? c[ 1 ] : false;
    //         },
    //         getCookSingle = function( c ) {
    //             var c = document.cookie.match( /moduleSingle=([a-z0-9]+)/i );
    //             return c && c.length == 2 ? c[ 1 ] : false;
    //         },
    //         subsetNames = getCookSubset(),
    //         singleName = getCookSingle();

    //     this._allModules = allMods; // keep a reference around

    //     if( subsetNames ) { this.doModuleSubset( subsetNames.split( ',' ) ); }
    //     else if( singleName ) { this.doModuleSingle( singleName ); }
    //     else { this.showModulesList( allMods ); }
    // },

    // doModuleSubset: function( namesArray ) {
    //     var subset = [],
    //         that = this;

    //     console.log( 'DEV MODE: MULTIPLE MODULES: ', namesArray.toString() );

    //     _.each( namesArray, function( name ) {
    //         // find the module obj
    //         var x = _.where( that._allModules, { name: name } );
    //         // add it to subset module array
    //         if( x.length ) {subset.push( x[ 0 ] );}
    //     } );

    //     this.launchApp.moduleCollection.reset( subset );
    //     document.cookie = 'moduleSubset=' + namesArray.toString();
    //     document.cookie = 'moduleSingle='; // clear

    //     this.showSubsetActive();

    // },
    // doModuleSingle: function( name ) {
    //     var sizes = [ '4x4', '4x2', '2x2', '2x1', '1x1' ],
    //         mod = _.where( this._allModules, { name: name } ),
    //         i, m, modsList = [];

    //     mod = mod.length ? mod[ 0 ] : null;

    //     if( !mod ) { return; } // exit

    //     console.log( 'DEV MODE: SINGLE MODULE: ', mod );

    //     // create a clone for all sizes
    //     for( i = 0;i < sizes.length;i++ ) {
    //         m = _.clone( mod );
    //         m.viewName = m.viewName || m.name; // move this to view name as all will share this
    //         m.templateName = m.templateName || m.name; // move this to tpl name as all will share this
    //         m.name = m.name + i; // rename since name is a unique id
    //         m.filters = { // custom filter settings
    //             'default': { size: sizes[ i ], order: i },
    //             'home': { size: sizes[ i ], order: i },
    //             'activities': { size: sizes[ i ], order: i },
    //             'social': { size: sizes[ i ], order: i },
    //             'shop': { size: sizes[ i ], order: i },
    //             'reports': { size: sizes[ i ], order: i },
    //             'throwdown': { size: sizes[ i ], order: i },
    //             'all': { size: sizes[ i ], order: i }
    //         };
    //         modsList.push( m );
    //     }

    //     this.launchApp.moduleCollection.reset( modsList );
    //     document.cookie = 'moduleSingle=' + name;
    //     document.cookie = 'moduleSubset='; // clear

    //     this.showSubsetActive();
    // },
    // showModulesList: function() {
    //     var that = this,
    //         $e = $( '#devModuleSubsetSelect' ),
    //         mods = this._allModules,
    //         $c = $( '<div />' ).css( { 'float': 'left', padding: '0 40px 0 0', 'vertical-align': 'middle' } ),
    //         lastAppName;

    //     mods = _.sortBy( mods, function( m ) {return m.name;} );
    //     mods = _.sortBy( mods, function( m ) {return m.appName;} );

    //     // build control
    //     if( !$e.length ) {
    //         $e = $( '<div id="devModuleSubsetSelect"/>' );
    //         $e.css( {
    //             position: 'fixed', top: 0, 'overflow': 'auto',
    //             left: 0, right: 0, bottom: 0, 'z-index': 999999, color: 'white',
    //             padding: '20px', display: 'none',
    //             background: 'rgb(0,0,0)', opacity: 0.8
    //         } );
    //         $e.append( '<div>Please select modules (from <b>core/tpl/modulesPage.html</b>) to include, they will be saved in your cookie.<br><br>'
    //             + '</div>' );
    //         _.each( mods, function( m, i ) {
    //             if( lastAppName !== m.appName ) {$c.append( '<h5 style="margin-bottom:0">' + m.appName + '</h5>' );}
    //             lastAppName = m.appName;
    //             $c.append( '<label><input type="checkbox" data-name="' + m.name + '" style="opacity:1" /> ' + m.name + '</label>' );
    //             if( i > 0 && i % 10 === 0 ) {
    //                 $e.append( $c );
    //                 $c = $c.clone().empty();
    //             }
    //         } );
    //         $e.append( $c );
    //         $e.append( '<div style="clear:both;padding-top:16px">' +
    //             '<a class="btn btn-small btn-primary save">Save Subset (normal settings)</a>' +
    //             '<a class="btn btn-small btn-primary saveSingle">Single Mode (all sizes for single module)</a></div>' );
    //         $( 'body' ).append( $e );
    //         $e.slideDown();

    //         $e.find( '.save' ).click( function() {
    //             that.saveModulesList();
    //             $e.slideUp();
    //         } );
    //         $e.find( '.saveSingle' ).click( function() {
    //             that.saveSingle();
    //             $e.slideUp();
    //         } );
    //     }
    //     // already created, show it
    //     else {
    //         $e.find( 'input[type=checkbox]' ).removeAttr( 'checked' );
    //         $e.slideDown();
    //     }
    // },
    // showSubsetActive: function() {
    //     var $e = $( '#devModuleSubsetActive' ),
    //         that = this;

    //     if( !$e.length ) {
    //         $e = $( '<div id="devModuleSubsetActive"><i class="icon-cog"/> Module Subset Active</div>' ).css( {
    //             position: 'fixed', bottom: 0, left: 0,
    //             'font-size': '14px', background: 'rgb(250,0,0)', opacity: 0.8,
    //             color: 'white', padding: '4px 8px 4px 8px', 'font-family': 'arial',
    //             cursor: 'pointer', 'z-index': 999999
    //         } );
    //         $( 'body' ).append( $e );
    //         $e.click( function() {
    //             that.showModulesList();
    //         } );
    //     }
    // },
    // saveModulesList: function() {
    //     var $modList = $( '#devModuleSubsetSelect input:checked' ),
    //         list = [];

    //     $modList.each( function() {
    //         list.push( $( this ).data( 'name' ) );
    //     } );

    //     this.doModuleSubset( list );
    // },
    // saveSingle: function() {
    //     var $mod = $( '#devModuleSubsetSelect input:checked:eq(0)' );//, // first module selected
    //         //sizes = [ '4x4', '4x2', '2x2', '2x1', '1x1' ],
    //         //mod, i, m, modsList = [];

    //     this.doModuleSingle( $mod.data( 'name' ) );
    // }
    //***************************
    // eof DEVELOPMENT
    //***************************

} );