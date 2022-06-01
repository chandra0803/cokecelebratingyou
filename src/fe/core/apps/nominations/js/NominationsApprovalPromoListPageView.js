/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported NominationsApprovalPromoListPageView */
/*global
$,
_,
Backbone,
G5,
TemplateManager,
PaginationView,
PageView,
NominationsApprovalPromoListPageModel,
NominationsApprovalPromoListPageView:true
*/
NominationsApprovalPromoListPageView = PageView.extend( {
    //init function
    initialize: function() {
        var that = this;

        //set the appname (getTpl() method uses this)
        this.appName = 'nominations';

		//this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        //template names
        this.nominationsApprovalPromoListTpl = 'nominationsApprovalPromoListTpl';
        this.tplPath = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'nominations/tpl/';

        this.model = new NominationsApprovalPromoListPageModel( {} );

        this.model.loadData();

        this.model.on( 'loadDataFinished', function() {
            that.render();
        } );
    },

	events: {
		'click .sortable a': 'handleTableSort'
    },

    render: function() {
		var that = this,
            $container = that.$el.find( '.nominationsApprovalPromoWrap' ),
			tableData = that.model.toJSON();

		TemplateManager.get( that.nominationsApprovalPromoListTpl, function( tpl, vars, subTpls ) {
			that.subTpls = subTpls;

            if ( tableData.tabularData.results.length === 0 && that.$el.find( '.msgEmpty' ).length === 0 ) {
                $container
                    .addClass( 'emptySet' )
                    .after( that.make( 'p', { 'class': 'msgEmpty' }, $container.data( 'msgEmpty' ) ) );

            }

			$container.empty().append( tpl( tableData ) );

			that.$el.find( '.table' ).responsiveTable();

			that.renderPagination();

		}, that.tplPath );

		G5.util.hideSpin( that.$el );
    },

	renderPagination: function() {
        var that = this;

        // if our data is paginated, add a special pagination view
        if ( this.model.get( 'total' ) > this.model.get( 'nominationPerPage' ) ) {
            if ( !this.pagination ) {
                this.pagination = new PaginationView( {
                    el: this.$el.find( '.paginationControls' ),
                    pages: Math.ceil( this.model.get( 'total' ) / this.model.get( 'nominationPerPage' ) ),
                    current: this.model.get( 'currentPage' ),
                    ajax: true,
                    tpl: this.subTpls.paginationTpl || false
                } );

                this.pagination.on( 'goToPage', function( page ) {
                    that.paginationClickHandler( page );
                } );

                this.model.on( 'loadDataFinished', function() {
                    that.pagination.setProperties( {
                        rendered: false,
                        pages: Math.ceil( that.model.get( 'total' ) / that.model.get( 'nominationPerPage' ) ),
                        current: that.model.get( 'currentPage' )
                    } );
                } );
            } else {
                this.pagination.setElement( this.$el.find( '.paginationControls' ) );

                if ( !this.pagination.$el.children().length ) {
                    this.pagination.render();
                }
            }
        }
    },

	paginationClickHandler: function( page ) {
        G5.util.showSpin( this.$el, {
            cover: true
        } );

        this.model.loadData( {
            method: 'pagination',
            total: this.model.get( 'total' ),
            nominationPerPage: this.model.get( 'nominationPerPage' ),
            currentPage: page,
            sortedOn: this.model.get( 'sortedOn' ),
            sortedBy: this.model.get( 'sortedBy' )
        } );
    },

	handleTableSort: function( e ) {
        var $tar = $( e.target ).closest( '.sortable' ),
            sortOn = $tar.data( 'sortOn' ),
            sortBy = $tar.data( 'sortBy' );

        e.preventDefault();

        G5.util.showSpin( this.$el, {
            cover: true
        } );

        this.model.loadData( {
            method: 'sort',
            sortedOn: sortOn,
            sortedBy: sortBy,
            currentPage: this.model.get( 'currentPage' )
        } );
    }
} );
