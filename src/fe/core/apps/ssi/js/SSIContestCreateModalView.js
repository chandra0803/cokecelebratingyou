/*exported SSIContestCreateModalView*/
/*global
TemplateManager,
SSISharedHelpersView,
SSICreatorContestModel,
SSIContestCreateModalView:true
*/
SSIContestCreateModalView = SSISharedHelpersView.extend( {
    //override super-class initialize function
    resizeDelay: 100,
    // use a model to persist data between qtips
    initialize: function ( opts ) {
        'use strict';

        //set the appname ( getTpl() method uses this )
        this.appName = 'ssi';

        // this.parentView = opts.parentView;
        // this.contests = opts.contests;
        this.tplPath = './../apps/ssi/tpl/';
        this.tplName = 'ssiContestCreateModalTpl';
        // this.model = new Backbone.Model();

        this.parentView = opts.parentView;
        this.target = opts.target;
        this.container = opts.container;
        this.$el = this.target;

        // borrow the parentView's contestData model
        // if it is available, otherwise create a new model
        this.contestData = this.parentView.contestData || new SSICreatorContestModel();

        this.render();

        return this;
    },

    /**
     * function description
     *
     * @param { string } foo - foo description
     * @returns { string }
     */
    prepJSON: function ( jsonData ) {
        'use strict';

        _.each( jsonData.contestTypes, function ( val, key ) {
            val.offset = key * 2;
        } );

        return jsonData;
    },

    /**
     * function description
     *
     * @param { string } foo - foo description
     * @returns { string }
     */
    render: function () {
        'use strict';

        var that = this,
            $spinner = this.parentView.$el.find( '.contestCreateSpinner' ),
            getTemplate,
            renderTemplate;

        getTemplate = _.bind( function () {
            var deferrer = new $.Deferred();

            TemplateManager.get( this.tplName, function ( tpl ) {
                deferrer.resolve( tpl );
            }, this.tplPath );

            return deferrer.promise();
        }, this );

        renderTemplate = _.bind( function ( tpl ) {

            var data = this.prepJSON( this.contestData.toJSON() );

            this.target.append( tpl( data ) );
            that.initTabs();

            this.target.on( 'shown', function() {
                $spinner.spin( false );
            } );
            this.target.on( 'hidden', function() {
                that.destroyView();
            } );
            this.target.modal();
        }, this );

        $spinner.spin( true );

        this.contestData.makeFetchHappen()
            .then( getTemplate )
            .then( renderTemplate );

        return this;
    },

    initTabs: function() {
        // add a class to determine how many tabs we have
        this.target.find( '.nav-tabs' ).addClass( 'tabCount' + this.target.find( '.nav-tabs li' ).length );

        // Select the first contest in the modal when shown
        this.target.find( '.nav-tabs a:eq( 0 )' ).tab( 'show' );
    },

    /**
     * Maybe overly aggressive removal of the qtip.
     * Destroys the qtip and the view, to prevent conflicting references.
     *
     * @returns { undefined }
     */
    destroyView: function() {
        'use strict';

        // remove qtip
        this.target.empty();

        // remove events
        this.undelegateEvents();
        this.$el.removeData().unbind();

        // Remove view from DOM
        // commenting out because we need the empty container to open the modal a second time
        // this.remove();
        // Backbone.View.prototype.remove.call( this );

        return;
    }

} );
