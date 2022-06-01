/*exported ProfilePageProxiesTabView */
/*global
TemplateManager,
ParticipantCollectionView,
PaxSelectedPaxCollection,
PaxSearchStartView,
ProfilePageProxiesTabView:true
*/
ProfilePageProxiesTabView = Backbone.View.extend( {
    initialize: function ( opts ) {
        'use strict';

        this.tplName    = opts.tplName || 'profilePageProxiesTab';
        this.tplUrl     = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'profile/tpl/';

        this.loadUrl = G5.props.URL_JSON_PROFILE_PAGE_PROXIES;

        this.on( 'templateLoaded', this.fetchExistingProxies, this );
        this.on( 'proxiesLoaded', this.initializeWidgets, this );
    },

    activate: function () {
        'use strict';

        this.render();
    },

    events: {
        'click .editProxyControl': 'editProxy',
        'click .remParticipantControl': 'removeProxy',

        // edit form handlers
        'click #profilePageProxiesTabEdit .participant-popover': 'attachParticipantPopover',
        'click #profilePageProxyTabButtonSaveProxy': 'formHandler',
        'click #profilePageProxiesTabEdit .form-actions button': 'formActions',
        'click #profilePageProxiesTabEdit #profilePageProxyTabButtonCancel': 'editFormHide',
        'change #profilePageProxiesTabEdit input[type=radio]': 'radioCheckboxUpdate',
        'click #profilePageProxiesTabEdit input[type=checkbox]': 'radioCheckboxUpdate'
    },

    render: function () {
        'use strict';
        var that = this;

        // if there is no html in the tab content element, go get the remote contents
        if ( this.$el.html().length === 0 ) {

            this.$el
                .append( '<span class="spin" />' )
                .find( '.spin' ).spin();

            TemplateManager.get( this.tplName,
                function ( tpl, vars, subTpls ) {
                    that.$el.empty().append( tpl );
                    that.proxyItemTpl = subTpls.profilePageProxiesTabListRow;
                    that.trigger( 'templateLoaded' );
                },
            this.tplUrl, true ); // "true" tells TemplateManager not to compile the resulting template, but to treat it as raw HTML instead (similar to an Ajax request/response)
        }

        return this;
    },

    fetchExistingProxies: function() {
        var that = this;
        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: this.loadUrl,
            success: function ( response ) {
                that.trigger( 'proxiesLoaded', response.data.proxies );
            }
        } );
    },

    initializeWidgets: function( proxiesJson ) {
        var that = this,
            tplName = 'profilePageProxiesTab.profilePageProxiesTabListRow',
            tplUrl = '',
            collection = new PaxSelectedPaxCollection();

        // preload the proxies row template
        TemplateManager.get( tplName, function() { return; }, tplUrl );

        this.participantsView = new ParticipantCollectionView( {
            el: this.$el.find( '#participantsView' ),
            tplName: tplName, //override the default template
            tplUrl: tplUrl, // override the default template location (for dev)
            model: collection//new Backbone.Collection(proxiesJson)
        } );

        this.participantSearchView = new PaxSearchStartView( {
                el: this.$el.find( '.paxSearchStartView' ),
                multiSelect: true,
                selectedPaxCollection: collection,
                disableSelect: false,
                addSelectedPaxView: true,
                follow: false
            } );

        collection.reset( proxiesJson );

        // listen for a participant add to the collection
        this.participantsView.model.on( 'add', function( m ) {
            that.$el.find( '[data-participant-cid=' + m.cid + ']' )
                .data( 'model', m )
                .find( '.editProxyControl' ).click();
        }, this );

        // listen for a remove event
        this.participantsView.model.on( 'remove', function() {
            this.editFormHide();
        }, this );

    },

    editFormHide: function( e ) {

        var that = this;
        if ( e ) { e.preventDefault(); }
        $( '#profilePageProxiesTabEdit' ).slideUp( G5.props.ANIMATION_DURATION, function() {
            $( '#profilePageProxiesTabEdit' ).empty();
            that.$el.find( '.participant-item' ).removeClass( 'info' );
        } );
    },

    editFormShow: function( content ) {
        var that = this;
        $( '#profilePageProxiesTabEdit' )
            .html( content )
            .slideDown( G5.props.ANIMATION_DURATION, function() {
                $.scrollTo( $( '#profilePageProxiesTabEdit' ), G5.props.ANIMATION_DURATION, { axis: 'y' } );
                that.radioCheckboxUpdate();
            } );
    },

    editProxy: function( e ) {

        var that = this,
            url = $( e.currentTarget ).attr( 'href' ),
            model = $( e.currentTarget ).parents( '.participant-item' ).data( 'model' ) || this.participantsView.model.getByCid( $( e.currentTarget ).parents( '.participant-item' ).attr( 'data-participant-cid' ) ),
            data = { id: model.get( 'id' ) },
            params = { responseType: 'html' };

        e.preventDefault();

        //$(e.currentTarget).parents('.participant-item').addClass('info');

        params = $.extend( {}, params, data );
        $.ajax( {
            url: ( G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'profile/tpl/' ) + url,
            type: 'GET',
            data: params,
            dataType: 'g5html',
            success: function( data ) {
                //console.log('EDIT PROXY ajax success.');
                ////console.log(data);
                ////console.log(model);
                that.editFormShow( data );
            }
        } );
    },

    formActions: function( e ) {
        var $form = $( e.target ).closest( 'form' );

        $form.data( 'trigger', $( e.target ) );
    },

    formHandler: function( e ) {


        var that = this,
            $form = $( e.target ).closest( 'form' );
       // //console.log('$form: ', $form);
       //      var $trigger = $form.data('trigger');
       // //console.log('$trigger: ', $trigger);
            var method = $form.attr( 'method' );
       // //console.log('method: ', method);
            var action = $form.attr( 'action' ) || $form.data( 'default-action' );
       // //console.log('action: ', action);
            var data = $form.serializeArray();
       // //console.log('data: ', data);
            //var request;
        e.preventDefault();
        // data.push({ name : 'trigger', value : $trigger.val() });

        $.ajax( {
            dataType: 'g5json',
            type: method,
            url: action,
            data: data,
            success: function ( response ) {

            // $form.data('savedState', $data);
                //check for errors
                if ( !G5.util.formValidateHandleJsonErrors( $form, response.data.messages ) ) {
                    return false;
                } else {
                    // otherwise, mark the form as ready to submit and resubmit
                    _.each( response.data.proxies, function( proxy ) {
                        if ( that.participantsView.model.get( proxy.id ) ) {
                            that.participantsView.model.get( proxy.id ).set( proxy );
                        }
                    } );
                    that.participantsView.model.reset( that.participantsView.model.models );

                    that.editFormHide();
                }
            },
            error: function () {
                //console.log(a, b, c);
            }
        } );

        // request = $.ajax({
        //     url : action,
        //     type : method,
        //     data : data,
        //     dataType : 'g5json'
        // });

        // request.done(function(data, textStatus, jqXHR) {
        //     // if the form fails validation on the server, prevent it from doing anything else
        //     if( !G5.util.formValidateHandleJsonErrors($form, data.data.messages) ) {
        //         return false;
        //     }
        //     // otherwise, mark the form as ready to submit and resubmit
        //     else {
        //         _.each(data.data.proxies, function(proxy) {
        //             that.participantsView.model.get(proxy.id).set(proxy);
        //         });
        //         that.participantsView.model.reset(that.participantsView.model.models);

        //         that.editFormHide();
        //     }
        // });

        // request.fail(function(jqXHR, textStatus, errorThrown) {
        //     //console.log('[ERROR] ProfilePageProxiesTabView: form submission .ajax failed', jqXHR, textStatus, errorThrown);
        // });
    },

    radioCheckboxUpdate: function() {
        var that = this;

        this.$el.find( '#profilePageProxiesTabEdit input[data-conditional="onchecked"]' ).each( function() {
            console.log( $( this ).is( ':checked' ) );
            if (  $( this ).is( ':checked' )  ) {
                that.$el.find( $( this ).data( 'conditionalTarget' ) ).slideDown( G5.props.ANIMATION_DURATION );
            }            else {
                that.$el.find( $( this ).data( 'conditionalTarget' ) ).slideUp( G5.props.ANIMATION_DURATION );
            }
        } );

        this.$el.find( '#profilePageProxiesTabEdit input[data-conditional="onunchecked"]' ).each( function() {
            if (  $( this ).is( ':checked' )  ) {
                that.$el.find( $( this ).data( 'conditionalTarget' ) ).slideUp( G5.props.ANIMATION_DURATION );
            }            else {
                that.$el.find( $( this ).data( 'conditionalTarget' ) ).slideDown( G5.props.ANIMATION_DURATION );
            }
        } );
    },

    removeProxy: function( e ) {
        // e.preventDefault();
        if ( $( e.target ).closest( '.participant-item' ).hasClass( 'info' ) ) {
            this.editFormHide();
        }

    },

    attachParticipantPopover: function( e ) {

        var $tar = $( e.target );
        e.preventDefault();
        //attach participant popovers
        if ( !$tar.data( 'participantPopover' ) ) {
            $tar.participantPopover( { containerEl: this.$el } ).qtip( 'show' );
        }
    }
} );
