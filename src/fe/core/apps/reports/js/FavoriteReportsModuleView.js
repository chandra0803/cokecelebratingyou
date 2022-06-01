/*exported FavoriteReportsModuleView*/
/*global
Modernizr,
ReportsModuleView,
FavoriteReportsModuleView:true
*/
FavoriteReportsModuleView = Backbone.View.extend( {

    //override super-class initialize function
    initialize: function() {
        'use strict';

        this.$el = this.options.$el;
        this.$dash = this.$el.closest( '.reportsDashboard' );

        // handy storage for later
        this.favoritesJson = [];
        this.favoritesViews = [];
        this.loadData();

        this.on( 'favoritesLoaded', this.postRender, this );
    },

    events: {
    }, // events

    loadData: function( data, options ) {
        var that = this,
            data = data || {
                method: 'display'
            },
            defaults = {
                silent: false,
                callback: null
            },
            settings = _.extend( {}, defaults, options );

        if( settings.silent === false ) {
            G5.util.showSpin( this.$el );
        }

        $.ajax( {
            dataType: 'g5json', //must set this so SeverResponse can parse
            type: 'POST',
            url: G5.props.URL_JSON_REPORTS_FAVORITES,
            data: data,
            success: function( serverResp ) {
                var json = serverResp.data;
                that.favoritesJson = json.favorites || [];

                if( settings.silent === false ) {
                    G5.util.hideSpin( that.$el );

                    that.trigger( 'favoritesLoaded' );
                }

                if( settings.callback ) {
                    settings.callback( that );
                }
            } // success
        } ); // ajax
    },

    postRender: function() {
        var that = this;

        // initialize (G5.props.REPORTS_MAX_FAVORITES) reports
        for( var i = 0; i < G5.props.REPORTS_MAX_FAVORITES; i++ ) {
            var json = this.favoritesJson[ i ] || {},
                view = new ReportsModuleView( {
                    reportIndex: i,
                    tpl: that.options.favTpl,
                    json: json,
                    $target: json ? this.$el : null
                } );

            this.favoritesViews.push( {
                reportIndex: i,
                name: 'favorite' + i,
                view: view,
                id: view.$el.data( 'id' ),
                parentId: view.$el.data( 'parentId' )
            } );

            view.$el.data( 'view', view );
        }

        this.$favorites = this.$el.find( '.favorite' );

        // this only runs when we don't have this.favoritesJson ahead of time
        _.each( this.favoritesViews, function( obj ) {
            obj.view.on( 'renderDone', function() {
                obj.id = this.$el.data( 'id' );
                obj.parentId = this.$el.data( 'parentId' );
            } );
        } );
        // end

        this.doSortable();
    },

    doSortable: function() {
        var that = this;

        if( this.$favorites.filter( ':not(.isEmpty)' ).length <= 1 ) {
            this.$el.addClass( 'no-sorting' );
            return;
        }

        // lazy-attach plugin
        if( !this.$el.data( 'sortable' ) ) {
            // jquery ui sortable (drag and drop to change pageNumber/order)
            this.$el.sortable( {
                // containment: '#wrapper_inner',
                // delay: 100, // before draging delay (allow clicks)
                // forceHelperSize: true,
                forcePlaceholderSize: true,
                handle: '.reorder',
                // helper: 'clone',
                items: '.favorite:not(.isEmpty)',
                opacity: 0.5,
                placeholder: 'favorite placeholder',
                revert: G5.props.ANIMATION_DURATION, // animate to resting pos (ms)
                scroll: Modernizr.touch === true ? false : true,
                tolerance: 'pointer',
                start: function() {
                    that.$el.addClass( 'ui-sorting' );
                    that.$el.sortable( 'refreshPositions' );
                },
                stop: function( e, ui ) {
                    that.$el.removeClass( 'ui-sorting' );
                    that.$el.sortable( 'refreshPositions' );
                    if( Modernizr.touch === true ) {
                        $.scrollTo( ui.item );
                    }
                },
                update: function( e, ui ) {
                    that.doFavoritesSorted( e, ui ); // do stuff when order changed
                }
            } );

            // adding touch events (?)
            if( 'ontouchend' in document ) {
                //adds touchPad support
                var touchHandler;

                touchHandler = function( event ) {
                    var touches = event.changedTouches,
                        first = touches[ 0 ],
                        type = '';

                    switch( event.type )
                    {
                        case 'touchstart': type = 'mousedown'; break;
                        case 'touchmove':  type = 'mousemove'; break;
                        case 'touchend':   type = 'mouseup'; break;
                        default: return;
                    }

                    var simulatedEvent = document.createEvent( 'MouseEvent' );
                    simulatedEvent.initMouseEvent( type, true, true, window, 1,
                                              first.screenX, first.screenY,
                                              first.clientX, first.clientY, false,
                                              false, false, false, 0/*left*/, null );
                    first.target.dispatchEvent( simulatedEvent );
                };

                that.$favorites.each( function() {
                    //listeners are added when the document is loaded, but this will probably get placed in the view's init.. depending
                    this.addEventListener( 'touchstart', touchHandler, true );
                    this.addEventListener( 'touchmove', touchHandler, true );
                    this.addEventListener( 'touchend', touchHandler, true );
                    this.addEventListener( 'touchcancel', touchHandler, true );
                } );
            }
        }
    },

    doFavoritesSorted: function( event, ui ) {
        var $sorted = ui.item,
            reorderRequestData;

        reorderRequestData = {
            method: 'reOrder',
            reportDashboardId: $sorted.data( 'parentId' ),
            reportDashboardItemId: $sorted.data( 'id' ),
            newIndex: $sorted.index()
        };

        this.loadData( reorderRequestData, { silent: true, callback: this.updateSortables } );
    },

    updateSortables: function( scope ) {
        var that = scope;

        if( that.favoritesJson.length ) {
            _.each( that.favoritesJson, function( fav, index ) {
                var $fav = that.$favorites.filter( '[data-id="' + fav.id + '"]' );
                if( $fav.length ) {
                    $fav.data( 'reportIndex', index );
                }
                // more to be done?
                // just how much checking of matching data do we need to do?
            } );
        }
    }

} );
