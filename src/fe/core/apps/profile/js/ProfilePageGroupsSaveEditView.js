/*exported ProfilePageGroupsSaveEditView */
/*global
TemplateManager,
PaxSearchStartView,
PaxSelectedPaxCollection,
ParticipantCollectionView,
ProfileGroupsModel,
ProfilePageGroupsSaveEditView:true
*/
ProfilePageGroupsSaveEditView = Backbone.View.extend( {
    initialize: function ( opts ) {
        this.tplName    = opts.tplName || 'profilePageGroupsSaveEditView';
        this.tplUrl     = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'profile/tpl/';

        this.model = new ProfileGroupsModel();

        // SET UP FLAGS and Template Vars
        // if we are only able to create a group ie from search only turn on and init certain pieces
        this.isCreateOnly = opts.isCreateOnly || false;
        this.paxCollection = opts.paxCollection || new PaxSelectedPaxCollection( null, { } );

        this.model.on( 'groupSaved', this.groupSaved, this );
        this.model.on( 'groupDeleted', this.groupDeleted, this );
        this.model.on( 'groupsReceived', this.groupsReceived, this );
        this.model.on( 'groupDetailsReceived', this.groupDetailsReceived, this );
        this.model.on( 'groupError', this.groupError, this );

        this.currentGroupId = null;
        this.currentGroupDetails = null;



        if( opts.el ) {
            this.setElement( opts.el );
        }
        var that = this;

        if( !this.isCreateOnly ) {

            this.on( 'tabRendered', function() {
                that.groupsView = new ParticipantCollectionView( {
                    el: that.$el.find( '#paxListView' ),
                    model: that.paxCollection,
                    tplName: 'participantRowItem' //override the default template
                } );

                that.participantSearchView = new PaxSearchStartView( {
                    el: that.$el.find( '.paxSearchStartView' ),
                    multiSelect: true, //this.isSingleSelectMode(),
                    selectedPaxCollection: that.paxCollection,
                    //disableSelect:true,
                    addSelectedPaxView: true
                } );
            } );
        }else{
            // we don't need to render so hmmm
            this.$errorBox = this.$el.find( '.group-error' );
        }

        this.paxCollection.on( 'add',  this.paxAdded,  this );
        this.paxCollection.on( 'remove',  this.paxRemoved,  this );

    },

    events: {
        'click .save-as-group': 'openSaveAGroupFromSearch',
        'click #deleteGroupBtn': 'deleteGroupConfirmed',
        'click #genericCloseQtip': 'closeQtip',
        'click .load-group': 'loadGroup',
        'click .save-group': 'saveGroup',
        'keyup .group-name': 'inputChange',
        'click .open-create-group': 'openCreateGroup',
        'click .delete-group': 'deleteGroupClicked',
        'click .cancel-group': 'cancel',
        'click .recognize-group': 'recognizeClicked',
        'click .recognize-group-edit': 'recognizeEditClicked',

    },

    activate: function () {
        'use strict';
        this.render();
    },

     render: function () {
        'use strict';
        var that = this;
        this.$el
            .append( '<span class="spin" />' )
            .find( '.spin' ).spin();

        TemplateManager.get( this.tplName,
            function ( tpl, vars, subTpls ) {

                that.$el.empty().append( tpl( {} ) );
                that.trigger( 'tabRendered' );
                that.groupRowTpl = subTpls.profilePageGroupRow;
                that.profilePageGroupRowDeleteConfirmationTpl = subTpls.profilePageGroupRowDeleteConfirmation;

                that.$errorBox = that.$el.find( '.group-error' );
                if( !that.isCreateOnly ) {
                    that.fetchAllGroups();
                }else{
                    that.loadCreateEdit( );
                    that.showSaveEditView( );
                }
            },
            this.tplUrl );

        return this;
    },
    openSaveAGroupFromSearch: function() {
        // check if we are updating or saving
        var header = this.$el.find( '.createHeader' );
        if( this.currentGroupId ) {
            header.html( header.data( 'editTxt' ) );
        }else{
            header.html( header.data( 'createTxt' ) );
        }
        // show save a group
        this.showSaveEditView( );

     },
    // load create edit
    loadCreateEdit: function( ) {
        // empty the collection
        if( !this.isCreateOnly ) {
            this.paxCollection.reset();
        }
        var header = this.$el.find( '.createHeader' );
        // check to see if we have a group id - if not we are creating a blank form
        if( !this.currentGroupId ) {
            // set any markup we need for new
            this.$el.find( '#saveEditView' ).find( '.recognize-group-edit' ).hide();
            this.$el.find( '#saveEditView' ).find( '.group-name' ).val( '' );
            header.html( header.data( 'createTxt' ) );
        }else{
            // set any markup we need for edit
            this.$el.find( '#saveEditView' ).find( '.recognize-group-edit' ).show();
            this.$el.find( '#saveEditView' ).find( '.group-name' ).val( this.currentGroupDetails.groupName );
            header.html( header.data( 'editTxt' ) );
            // load the paxes
            this.paxCollection.add( this.currentGroupDetails.groupMemebers );
            this.showSaveEditView( );
        }
    },



    /***
     *    888      888                  888 d8b          888
     *    888      888                  888 Y8P          888
     *    888      888                  888              888
     *    88888b.  888888 88888b.       888 888 .d8888b  888888 .d88b.  88888b.   .d88b.  888d888 .d8888b
     *    888 "88b 888    888 "88b      888 888 88K      888   d8P  Y8b 888 "88b d8P  Y8b 888P"   88K
     *    888  888 888    888  888      888 888 "Y8888b. 888   88888888 888  888 88888888 888     "Y8888b.
     *    888 d88P Y88b.  888  888      888 888      X88 Y88b. Y8b.     888  888 Y8b.     888          X88
     *    88888P"   "Y888 888  888      888 888  88888P'  "Y888 "Y8888  888  888  "Y8888  888      88888P'
     *
     *
     *
     */
    recognizeEditClicked: function( e ) {
        this.goToRecognition( this.currentGroupId );
    },

    recognizeClicked: function( e ) {
        this.currentGroupId = this.$el.find( e.target ).closest( '.group-row' ).data( 'id' );
        this.goToRecognition( this.currentGroupId );
    },

    checkValid: function() {
        var string = $.trim( this.$el.find( '.group-name' ).val() ),
            btn = this.$el.find( '.save-group' ),
            tot = this.paxCollection.size();
        if( string.length >= 2 && ( /\S/.test( string ) ) && tot >= 2 ) {
            btn.prop( 'disabled', false );
        }else{
            btn.prop( 'disabled', true );
        }
    },

    cancel: function( e ) {
        // clear it all
        this.clearErrors();
        if( !this.isCreateOnly ) {
            this.currentGroupDetails = null;
            this.currentGroupId = null;
            this.loadCreateEdit(); // calling just to clear out data
        }

        this.showGroupList();
    },

    loadGroup: function( e ) {
        // we need to get the id of the group
        this.currentGroupId = this.$el.find( e.target ).closest( '.group-row' ).data( 'id' );
        // load group info by id - we should already have this info so just load the markup
        this.fetchGroupById();
    },

    saveGroup: function( e ) {
        // check to see if we have a group id - if not we are creating a new set
        this.saveCreateGroup( this.currentGroupId, this.$el.find( '.group-name' ).val(), this.paxCollection.pluck( 'id' ) );
    },

    deleteGroupConfirmed: function( e ) {
        this.deleteGroup( this.groupToDeleteIdGroupId );
    },

    deleteGroupClicked: function( e ) {
        var $tar = $( e.target ),
            $cont = ( this.$el.find( '#confirmDelteGroupDialog' ).clone() ),
            container = this.$el;

        if ( e ) {
            e.preventDefault();
        }

        $tar.attr( 'disabled', 'disabled' );

        /*
         * Creates qtip for view claim history link
         */
         this.groupToDeleteIdGroupId = this.$el.find( e.target ).closest( '.group-row' ).data( 'id' );


        if ( !$tar.data( 'qtip' ) ) {
             $tar.qtip( {
                content: { text: $cont },
                position: {
                    my: 'right center',
                    at: 'left center',
                    container: container,
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
                    classes: 'ui-tooltip-shadow ui-tooltip-light',
                    tip: {
                        corner: true,
                        width: 20,
                        height: 10
                    }
                }
            } );
        }

    },

    closeQtip: function( e ) {
        /*
         * Hides all qtips on page
         */
        if ( e ) {
            e.preventDefault();
        }
        this.$el.find( '.qtip' ).qtip( 'hide' );
    },


    openCreateGroup: function( e ) {
        // show createEdit
        this.loadCreateEdit();
        this.showSaveEditView();
    },



    /***
     *    888 d8b          888
     *    888 Y8P          888
     *    888              888
     *    888 888 .d8888b  888888 .d88b.  88888b.   .d88b.  888d888 .d8888b
     *    888 888 88K      888   d8P  Y8b 888 "88b d8P  Y8b 888P"   88K
     *    888 888 "Y8888b. 888   88888888 888  888 88888888 888     "Y8888b.
     *    888 888      X88 Y88b. Y8b.     888  888 Y8b.     888          X88
     *    888 888  88888P'  "Y888 "Y8888  888  888  "Y8888  888      88888P'
     *
     *
     *
     */
    groupError: function( msg ) {
        // we have and error hold on
        // set the error
        this.$errorBox.find( '.text-error' ).html( '<li>' + msg + '</li>' );
        // show the error
        this.$errorBox.show();
        // clear the loader
        this.setStateLoaded();
    },
    paxAdded: function( participant ) {
        //console.log( 'paxAdded', participant );
        participant.set( 'selected', true, { silent: true } );
        this.checkValid();
    },

    paxRemoved: function ( participant ) {
        //console.log( 'paxRemoved', participant );
        participant.set( 'selected', false, { silent: true } );
        this.checkValid();
    },
    inputChange: function( e ) {
        this.checkValid();
    },
    groupSaved: function( data ) {
        // get new groups group id - this is really only for create Group
        if( this.isCreateOnly ) {
            this.currentGroupId = data.data.groupId;
        }
        // should call to update our list of groups as things have changed
        this.fetchAllGroups();
    },

    groupDeleted: function( data ) {
       //this.setStateLoaded();
       // should call to update our list of groups as things have changed
       this.fetchAllGroups();
    },

    groupsReceived: function( data ) {
        // clear and load template
        var markupData = '',
            that = this;
        this.groups = data.groups;
        this.$el.find( '#groupsListView' ).html( markupData );
        if( this.isCreateOnly ) {
            // show we saved it - update ui
            var btn = this.$el.find( '.save-as-group' );
            btn.html( btn.data( 'updateTxt' ) );
            this.showGroupList();
            return;
        }else if( this.groups.length ) {
             _.each( this.groups, function( group ) {
                markupData += that.groupRowTpl( group );
            } );
             this.showGroupList();
        }else{
            // if we have no groups show create a group

            this.loadCreateEdit( );
            this.showSaveEditView( );
        }

        this.$el.find( '#groupsListView' ).html( markupData );
        this.setStateLoaded();
    },

    groupDetailsReceived: function( data ) {
        this.setStateLoaded();
        this.currentGroupDetails = data;
        this.openCreateGroup();
    },

    /***
    *         888          888                                    888 888
    *         888          888                                    888 888
    *         888          888                                    888 888
    *     .d88888  8888b.  888888  8888b.        .d8888b  8888b.  888 888 .d8888b
    *    d88" 888     "88b 888        "88b      d88P"        "88b 888 888 88K
    *    888  888 .d888888 888    .d888888      888      .d888888 888 888 "Y8888b.
    *    Y88b 888 888  888 Y88b.  888  888      Y88b.    888  888 888 888      X88
    *     "Y88888 "Y888888  "Y888 "Y888888       "Y8888P "Y888888 888 888  88888P'
    *
    *
    *
    */
    saveCreateGroup: function( groupId, groupName, groupMembers ) {
        this.setStateLoading( 'more' );
        this.model.saveCreateGroup( groupId, groupName, groupMembers );
    },
    fetchGroupById: function( groupId ) {
        this.setStateLoading( 'more' );
        this.model.fetchGroupById( this.currentGroupId );
    },
    fetchAllGroups: function( userId ) {
        this.setStateLoading( 'more' );
        this.model.fetchAllGroups( userId );
    },
    deleteGroup: function( groupId ) {
        this.setStateLoading( 'more' );
        this.model.deleteGroup( groupId );
    },

    goToRecognition: function( groupId ) {
        //console.log( groupId, this.groups[ 0 ], this.groups[ 0 ].id );
        var groupObj = _.find( this.groups, function( v, k ) {
            if ( v.id === groupId ) {
              return true;
            } else {
              return false;
            }
          } );
        if( groupObj.groupUserIds ) {
            this.setStateLoading();
            this.model.openRecognitionPage( groupObj.groupUserIds );
        }
        // else we are missing data
    },

    /***
     *    888                        888 d8b
     *    888                        888 Y8P
     *    888                        888
     *    888  .d88b.   8888b.   .d88888 888 88888b.   .d88b.
     *    888 d88""88b     "88b d88" 888 888 888 "88b d88P"88b
     *    888 888  888 .d888888 888  888 888 888  888 888  888
     *    888 Y88..88P 888  888 Y88b 888 888 888  888 Y88b 888
     *    888  "Y88P"  "Y888888  "Y88888 888 888  888  "Y88888
     *                                                     888
     *                                                Y8b d88P
     *                                                 "Y88P"
     */
    setStateLoading: function( mode ) {
        // clear any errors
        this.clearErrors();
        //console.log( 'setStateLoading mode:',mode );
        var spinProps = {};
        spinProps.classes = mode;
        G5.util.showSpin( this.$el.find( '.search-results' ), spinProps );

        this.isLoading = true;
    },

    setStateLoaded: function( ) {
        G5.util.hideSpin( this.$el.find( '.search-results' ) );
        G5.util.hideSpin( this.$el.find( '.searchWrapper' ) );
        this.isLoading = false;
    },

    /***
     *             888    d8b 888
     *             888    Y8P 888
     *             888        888
     *    888  888 888888 888 888
     *    888  888 888    888 888
     *    888  888 888    888 888
     *    Y88b 888 Y88b.  888 888
     *     "Y88888  "Y888 888 888
     *
     *
     *
     */
     clearErrors: function() {
        this.$errorBox.hide();
     },

    showGroupList: function() {
        this.$el.find( '#groupsList' ).show();
        this.$el.find( '#saveEditView' ).hide();
    },
    showSaveEditView: function() {
        this.$el.find( '#saveEditView' ).show();
        this.$el.find( '#groupsList' ).hide();
    },

} );
