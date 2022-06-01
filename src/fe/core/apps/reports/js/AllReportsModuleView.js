/*exported AllReportsModuleView*/
/*global
TemplateManager,
ReportCollection,
AllReportsModuleView:true
*/
AllReportsModuleView = Backbone.View.extend( {

    //override super-class initialize function
    initialize: function() {
        'use strict';
        var that = this;

        this.$el = this.options.$el;
        this.$dash = this.$el.closest( '.reportsDashboard' );

        this.reportsModel = new ReportCollection();
        this.reportsModel.loadData();

        G5.util.showSpin( this.$el );

        this.tplName = 'reportsPageAll';
        this.tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'reports/tpl/';

        // this.subView = new ReportsPageAllView({parentView: this});
        this.reportsModel.on( 'dataLoaded', function() {
            that.processData();
        } );

        this.on( 'processDataDone', function() {
            that.render();
        } );
    },

    events: {
        // "click .allReportsModule" : "clickThru"
        'click .switcher': 'showHideReportList'
    },

    // populate module & draw charts
    render: function() {
        'use strict';
        var that = this,
            tplName = this.tplName,
            tplUrl = this.tplUrl;

        TemplateManager.get( tplName, function( tpl, vars, subTpls ) {
            // clean out the view root and append our rendered template
        	that.$el
                .empty()
                .html(
                    tpl( {
                        categories: that.tplJson
                    } )
                );

            // trigger a renderDone event
            that.trigger( 'renderDone' );
        }, tplUrl );

        return this;//chaining
    },

    clickThru: function() {
        // this.$el.find('.allReportsModule').click(function(){
            window.location.href = G5.props.URL_REPORTS_ALL;
        // });
    },
    showHideReportList: function( e ) {
        var $trig = $( e.target ).closest( '.switcher' ),
            $icon = $trig.find( 'i' ),
            $tar = this.$el.find( $trig.data( 'target' ) );

        if( $tar.hasClass( 'hide' ) ) {
            $icon.removeClass( 'icon-plus-circle' ).addClass( 'icon-minus-circle' );
            $tar.slideDown( {
                duration: G5.props.ANIMATION_DURATION,
                complete: function() {
                    $tar.removeClass( 'hide' );
                }
            } );
        }
        else {
            $tar.slideUp( {
                duration: G5.props.ANIMATION_DURATION,
                complete: function() {
                    $tar.addClass( 'hide' );
                    $icon.removeClass( 'icon-minus-circle' ).addClass( 'icon-plus-circle' );
                }
            } );
        }
    },
    processData: function() {
        var thisView = this,
            categoryList = _.unique( this.reportsModel.pluck( 'category' ).sort(), true );

        this.tplJson = _.map( categoryList, function( cat ) {
            return {
                category: cat,
                module: true,
                categoryNameId: thisView.reportsModel.where( { category: cat } )[ 0 ].get( 'categoryNameId' ),
                reports: _.map(
                                _.sortBy( thisView.reportsModel.where( { category: cat } ), function( report ) {
                                    return report.get( 'displayName' );
                                } ),
                                function( report ) {
                                    return report.toJSON();
                                }
                            )
            };
        } );

        this.trigger( 'processDataDone' );
    }
} );
