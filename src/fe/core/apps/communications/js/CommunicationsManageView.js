/*exported CommunicationsManageView */
/*global
TemplateManager,
PageView,
PaginationView,
CommunicationsManageModel,
CommunicationsManageView:true
*/
CommunicationsManageView = PageView.extend( {

    mode: null,

    tableTplMap: {
        'banners': 'communicationBannersTable',
        'news': 'communicationNewsTable',
        'resources': 'communicationResourceTable',
        'tips': 'communicationsTipsTable'
    },

    manageTplMap: {
        'banners': 'CommunicationsManageBanners',
        'news': 'CommunicationsManageNews',
        'resources': 'CommunicationsManageResourceCenter',
        'tips': 'CommunicationsManageTips'
    },

    perPageMap: {
        'banners': 'bannersPerPage',
        'news': 'storiesPerPage',
        'resources': 'resourcesPerPage',
        'tips': 'tipsPerPage'
    },

    initialize: function(  ) {
        var that = this;

        //set the appname (getTpl() method uses this)
        this.appName = 'communications';

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        this.mode = this.options.mode;

        //create model
        this.communicationsManageModel = new CommunicationsManageModel( {}, {
            mode: this.mode
        } );

        G5._globalEvents.on( 'route', this.handleRoute, this );

        // call to set initial route
        G5._globalEvents.trigger( 'route', {}, G5.props.CURRENT_ROUTE.length ? G5.props.CURRENT_ROUTE : [ 'active' ], { trigger: true } );

        this.communicationsManageModel.on( 'loadDataFinished', function() {
            that.render();
        } );

    },

    handleRoute: function( event, route ) {
        var name;
        if ( route ) {
            name = route[ 0 ];

            if ( name ) {
                this.$el.find( '.contentFilterSelect' ).val( name );
                this.doInputChange();
            }            else {
                G5._globalEvents.trigger( 'route', {}, [ 'active' ], { replace: true, trigger: true } );
            }
        }
    },

    events: {
      'change .contentFilterSelect': 'doInputChange'
    },
    render: function() {
        var $contTable = this.$el.find( '.contentTableWrapper' ),
            that = this,
            tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'communications/tpl/';

        G5.util.hideSpin( this.$el );

        $contTable.html( '' );

        //Push Discussion values to template
        TemplateManager.get( this.tableTplMap[ this.mode ], function( tpl ) {
            if ( that.communicationsManageModel.get( 'tabularData' ).results.length === 0 ) {
                $contTable
                    .addClass( 'emptySet' )
                    .append( that.make( 'h3', {}, $contTable.data( 'msgEmpty' ) ) );
                return false;
            }
            $contTable.append( tpl( that.communicationsManageModel.toJSON() ) );

            $contTable.find( 'table th' ).eq( $contTable.find( 'table tr .editColumn' ).first().index() ).addClass( 'editColumn' );

            $contTable.find( 'table' ).responsiveTable();
        } );

        TemplateManager.get( this.manageTplMap[ this.mode ], function( tpl, vars, subTpls ) {
            that.paginationTpl = subTpls.paginationTpl;
            that.renderPagination();

        }, tplUrl );
    },

    renderPagination: function() {
        var that = this;

        // if our data is paginated, add a special pagination view
        if ( that.communicationsManageModel.get( 'total' ) > that.communicationsManageModel.get( that.perPageMap[ that.mode ] ) ) {
            // if no pagination view exists, create a new one
            if ( !that.pagination ) {
                that.pagination = new PaginationView( {
                    el: that.$el.find( '.pagination' ),
                    pages: Math.ceil( that.communicationsManageModel.get( 'total' ) / that.communicationsManageModel.get( that.perPageMap[ that.mode ] ) ),
                    current: that.communicationsManageModel.get( 'currentPage' ),
                    ajax: true,
                    tpl: that.paginationTpl || false
                } );

                this.pagination.on( 'goToPage', function( page ) {
                    that.paginationClickHandler( page );
                } );

                this.communicationsManageModel.on( 'loadDataFinished', function() {
                    that.pagination.setProperties( {
                        rendered: false,
                        pages: Math.ceil( that.communicationsManageModel.get( 'total' ) / that.communicationsManageModel.get( that.perPageMap[ that.mode ] ) ),
                        current: that.communicationsManageModel.get( 'currentPage' )
                    } );
                } );
            } else {
                this.pagination.setElement( that.$el.find( '.pagination' ) );
            }
        }
    },
    paginationClickHandler: function( page ) {
        G5.util.showSpin( this.$el, {
            cover: true
        } );

        this.communicationsManageModel.update( {
            force: true,
            data: {
                pageNumber: page
            },
            type: 'getContent'
        } );
    },
    doInputChange: function( e ) {
        var selectDropVal = e ? $( e.currentTarget ).val() : this.$el.find( '.contentFilterSelect' ).val();

        if ( this.communicationsManageModel.get( 'statusType' ) === selectDropVal ) {
            return;
        }

        this.communicationsManageModel.update( {
            force: true,
            data: {
                statusType: selectDropVal
            },
            type: 'status'
        } );

        G5._globalEvents.trigger( 'navigate', {}, [ selectDropVal ], { trigger: true } );
    }
} );
