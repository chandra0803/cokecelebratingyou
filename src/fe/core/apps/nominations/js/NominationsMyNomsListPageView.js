/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported NominationsMyNomsListPageView */
/*global
$,
_,
Backbone,
G5,
TemplateManager,
PaginationView,
NominationsMyNomsListPageModel,
NominationsMyNomsListPageView:true
*/
NominationsMyNomsListPageView = PageView.extend( {
    //init function
    initialize: function() {

        //set the appname (getTpl() method uses this)
        this.appName = 'nominations';

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        this.model = new NominationsMyNomsListPageModel( {} );

        this.model.loadData();

        this.setupEvents();
    },

    events: {
        'click .sortable a': 'handleTableSort'
    },

    setupEvents: function() {
        this.model.on( 'loadDataFinished', this.render, this );
    },

    render: function() {
        var that = this,
            $container = that.$el.find( '.nominationsListTableWrapper' ),
            model = that.model.toJSON();

        TemplateManager.get( 'nominationsListPageTable', function( tpl ) {
            $container.empty().append( tpl( model ) );

            that.$el.find( '.table' ).responsiveTable();

            that.renderPagination();

        } );

        G5.util.hideSpin( that.$el );
    },

    renderPagination: function() {
        var that = this;

        // if our data is paginated, add a special pagination view
        if ( this.model.get( 'total' ) > this.model.get( 'perPage' ) ) {
            if ( !this.pagination ) {
                this.pagination = new PaginationView( {
                    el: this.$el.find( '.pagination' ),
                    pages: Math.ceil( this.model.get( 'total' ) / this.model.get( 'perPage' ) ),
                    current: this.model.get( 'currentPage' ),
                    ajax: true,
                    tpl: that.paginationTpl || false
                } );

                this.pagination.on( 'goToPage', function( page ) {
                    that.paginationClickHandler( page );
                } );

                this.model.on( 'loadDataFinished', function() {
                    that.pagination.setProperties( {
                        rendered: false,
                        pages: Math.ceil( that.model.get( 'total' ) / that.model.get( 'perPage' ) ),
                        current: that.model.get( 'currentPage' )
                    } );
                } );
            } else {
                this.pagination.setElement( this.$el.find( '.pagination' ) );

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

        G5.util.showSpin( $( 'body' ), {
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
