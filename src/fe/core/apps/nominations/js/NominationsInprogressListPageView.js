/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported NominationsInprogressListPageView */
/*global
$,
_,
PageView,
G5,
TemplateManager,
PaginationView,
NominationsInprogressListPageModel,
NominationsInprogressListPageView:true
*/
NominationsInprogressListPageView = PageView.extend( {

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
        this.nominationsInprogressListTableTpl = 'nominationsInprogressListTableTpl';
        this.tplPath = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'nominations/tpl/';

        this.model = new NominationsInprogressListPageModel();

        this.model.loadData();

        this.model.on( 'loadDataFinished', function() {
            that.render();
        } );

    },

    events: {
        'click .removeNominationPromotion': 'confirmRemoveNomination',
		'click .sortable a': 'handleTableSort',
		'click #nominationRemoveDialogConfirm': 'doRemoveNomination',
		'click #nominationRemoveDialogCancel': 'cancelRemoveNomination'
    },

    render: function() {
		var that = this,
            $container = this.$el.find( '.nominationsTableWrap' ),
			tableData = this.model.toJSON();

            TemplateManager.get( this.nominationsInprogressListTableTpl, function( tpl, vars, subTpls ) {
                that.subTpls = subTpls;

                if ( tableData.tabularData.results.length === 0 && that.$el.find( '.msgEmpty' ).length === 0 ) {
                    $container
                        .addClass( 'emptySet' )
                        .after( that.make( 'p', { 'class': 'msgEmpty' }, $container.data( 'msgEmpty' ) ) );

                }

                $container.empty().append( tpl( tableData ) );

                that.$el.find( '.table' ).responsiveTable();

                that.renderPagination();
            }, this.tplPath );

		G5.util.hideSpin( this.$el );
    },

	renderPagination: function() {
        var that = this;
        //dont need to use that outside of event bindings, updated below.

        // if our data is paginated, add a special pagination view
        if ( this.model.get( 'total' ) > this.model.get( 'nominationPerPage' ) ) {
            // if no pagination view exists, create a new one
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

        //just need to pass the sorted and pagination data to loadData in model.

        this.model.loadData( {
            method: 'pagination',
            total: this.model.get( 'total' ),
            nominationPerPage: this.model.get( 'nominationPerPage' ),
            currentPage: page,
            sortedOn: this.model.get( 'sortedOn' ),
            sortedBy: this.model.get( 'sortedBy' )
        } );
    },

	confirmRemoveNomination: function( e ) {
        var $tar = $( e.currentTarget ),
            $popoverCont = this.$el.find( '.nominationRemoveConfirmDialog' );

        $tar.parents( 'tr' ).addClass( 'isHiding' );

        if ( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, $popoverCont.eq( 0 ).clone( true ), this.$el );
        }
    },

    cancelRemoveNomination: function( e ) {
        var $tar = $( e.currentTarget ),
            $row = this.$el.find( '#nominationsListPageTable tr' );

            e.preventDefault();

            $tar.closest( '.qtip' ).qtip( 'hide' );

            if ( $row.hasClass( 'isHiding' ) ) {
                $( '.isHiding' ).removeClass( 'isHiding' );
            }

            $( e.currentTarget ).closest( '.qtip' ).qtip( 'hide' );
    },

	doRemoveNomination: function( e ) {
        var $tar = $( e.currentTarget ),
            $row = this.$el.find( '#nominationsListPageTable tr' ),
            id,
            removeParams,
            data;

        e.preventDefault();

        $tar.closest( '.qtip' ).qtip( 'hide' );

        if ( $row.hasClass( 'isHiding' ) ) {
            id = $( '.isHiding' ).data( 'nomineeid' ).toString();
            removeParams = $( '.isHiding' ).data( 'removeparams' ).toString();

            $( '.isHiding' ).hide().removeClass( 'isHiding' ).remove();
        }

		data = {
            nomineeId: id,
			removeParams: removeParams
        };

        this.model.removeNomination( data );

    },

    handleTableSort: function( e ) {
        var $tar = $( e.target ).closest( '.sortable' ),
            sortOn = $tar.data( 'sortOn' ),
            sortBy = $tar.data( 'sortBy' );

        e.preventDefault();

        G5.util.showSpin( this.$el, {
            cover: true
        } );

        //just need to pass the sorted and pagination data to loadData in model.
        this.model.loadData( {
            method: 'sort',
            sortedOn: sortOn,
            sortedBy: sortBy,
            currentPage: this.model.get( 'currentPage' )
        } );
    },

    attachPopover: function( $trig, cont, $container ) {
        $trig.qtip( {
            content: { text: cont },
            position: {
                my: 'left center',
                at: 'right center',
                container: $container,
                viewport: $( 'body' ),
                adjust: {
                    method: 'shift none'
                }
            },
            show: {
                event: 'click',
                ready: true
            },
            hide: {
                event: 'unfocus',
                fixed: true,
                delay: 200
            },
            style: {
                classes: 'ui-tooltip-shadow ui-tooltip-light ',
                tip: {
                    corner: true,
                    width: 20,
                    height: 10
                }
            }
        } );
    }
} );
