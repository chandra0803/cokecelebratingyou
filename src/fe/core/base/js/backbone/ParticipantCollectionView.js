/*exported ParticipantCollectionView */
/*global
TemplateManager,
PaxCollection,
ParticipantCollectionView:true
*/

/** PLAIN PARTICIPANTS VIEW **/
ParticipantCollectionView = Backbone.View.extend( {
    initialize: function( opts ) {
        this.autoIncrement = 0;

        if ( !this.model ) {
            this.model = new PaxCollection();
        }
        console.log( this.model );
        // optional identifier to pass into templates
        this.feedToTpl = opts.feedToTpl || {};

        this.model.bind( 'reset', this.render, this );
        this.tplName = opts.tplName || 'participantRowItem';
        this.tplUrl = opts.tplUrl || G5.props.URL_BASE_ROOT + 'tpl/';
        this.model.bind( 'add', this.addParticipant, this );
        this.model.bind( 'remove', this.removeParticipant, this );
        this.hideOnEmpty = this.$el.data( 'hideOnEmpty' ) || false;
        this.$wrapper = this.$el.closest( '.participantCollectionViewWrapper' );
        this.participantsList = '';
        this.participantsLength = 0;
        this.participantCounter = 0;
        this.render();//initial render

        // because of how recipients are added when bootstrapped in, we need to wait a moment before making it normal then re-making it responsive. Gross to the nth degree.
        this.$el.closest( 'table' ).responsiveTable( { reset: true, duration: G5.props.ANIMATION_DURATION * 6 } ); // G5.props.ANIMATION_DURATION*6 is the amount of time G5.util.animBg takes to fade out the color background
    },
    render: function() {
        this.$el.empty();

        this.processModel();
        this.initSort();

        this.addParticipant( this.model );

        this.renderEmpty();
        return this;
    },
    processModel: function() {
        // make sure all participant IDs are numeric
        this.model.each( function( p ) {
            p.id = parseInt( p.id );
        } );
    },
    initSort: function() {
        var that = this;

        this.$wrapper.find( 'th.sortable' ).each( function() {
            var $t = $( this );
            $t.addClass( 'sortHeader' );
            $t.find( 'i' ).remove();// in case we get called again
            $t.append( ' <i class="sortControl icon-arrow-1-down"></i>' );
            // bind here, the $wrapper is technically outside this view's $el so we can't use events obj.
            $t.off( 'click' ).on( 'click', function( e ) {
                that.doSort( e );
            } );
        } );
    },
    addParticipant: function( participant ) {

        var that = this,
            json = participant.toJSON();
        that.participantsList = '';
        that.participantCounter = 0;
        that.participantsLength = this.model.length;
        if ( participant.models ) {
            _.each( participant.models, function( model ) {
                //console.log(model);
                if ( !model.get( 'invalid' ) ) {
                    that.addParticipant( model );
                }
            } );
            return;
        } else if ( participant.get( 'invalid' ) ) {
            // we have participant not collection return false
            return false;
        }
        // see if we already have this model in a view?
        // may need to remove the old view and recreate it?
        if ( this.$el.find( '[data-participant-id="' + participant.id + '"]' ).length  || this.$el.find( '[data-participant-cid="' + participant.cid + '"]' ).length ) {
            console.log( 'duplicate ?????? ' );
            return;
        }


        // check to see if our participant object is a Collection or Model and if our json object is an array or not. Turn both into arrays
        participant = participant.models ? participant.models : [ participant ];
        json = _.isArray( json ) ? json : [ json ];

        // iterate through the JSON, running a few manipulations on the raw data
        _.each( json, function( obj, index ) {
            obj.cid = participant[ index ].cid;

            obj.autoIndex = that.autoIncrement;
            that.autoIncrement++;

            obj = _.extend( obj, that.feedToTpl );
        } );

        // get the template
        TemplateManager.get( this.tplName, function( tpl ) {
            var $pi;
            // for each object in the json, pass it to the template function and append it to a raw string variable
            _.each( json, function( obj ) {
                // Adding current template first before the old data to make the recent data append at top
                that.participantsList = tpl( obj ) + that.participantsList;
                that.participantCounter++;
            } );

            // create a jQuery object of our raw string data and prepend it to the table (prepending assures that the newest entry will always be visible)

            // Appending to the dom only once after all the records are added since it looks awkward when large data is processed
            if ( ( that.participantCounter + that.$el.find( 'tr.participant-item' ).length ) === that.participantsLength ) {
                $pi = $( that.participantsList );
                that.$el.prepend( $pi );

                // run a background color animation only if we're adding fewer than some number of entries
                // Since the json length was always one, this logic was never happening and the animation flashed regardless of total participants size. So changing it to participants length
                if ( that.participantsLength <= 20 ) {
                    G5.util.animBg( $pi.find( 'td' ), 'background-flash' );
                }
    
                //attach parti. popover.
                $pi.find( '.participant-popover' ).participantPopover( { containerEl: that.$el } );
            }

            that.renderEmpty();
            that.updateCount();

            that.trigger( 'participantAdded', participant );

        }, G5.props.URL_TPL_ROOT || this.tplUrl );

        return this;
    },
    removeParticipant: function( participant ) {
        this.$el.find( '[data-participant-cid=' + participant.cid + ']' ).remove();
        this.renderEmpty();
        this.updateCount();
        return this;
    },

    renderEmpty: function() {
        //render empty
        var that = this,
            emptyMsg = this.$el.data( 'msg-empty' ),
            emptyEl,
            cols;


        // filter invalid users
        //console.log(this.model.models);
        // filtering out the eligible participants to see if we should showempty or not
        var filterCollection = _.filter( this.model.models, function( model ) {
            return !model.get( 'invalid' );
        } );
        //console.log(filterCollection);

        //CASE: not empty
        if ( filterCollection.length > 0 ) {
            this.$el.find( '.emptyMsg' ).closest( 'tr' ).remove();
            this.$wrapper.removeClass( 'emptyParticipantCollection' );
            this.$el.closest( 'table' ).responsiveTable( { reset: true, duration: G5.props.ANIMATION_DURATION * 6 } ); // G5.props.ANIMATION_DURATION*6 is the amount of time G5.util.animBg takes to fade out the color background

            if ( this.hideOnEmpty ) {
                //sliding up, finish fast and show
                if ( this.$wrapper.queue().length > 0 ) {
                    this.$wrapper.stop( true, true ).show();
                    this.trigger( 'shown' );
                } else { //not animating, so animate
                    this.$wrapper.slideDown( G5.props.ANIMATION_DURATION, function() {
                        that.trigger( 'shown' );
                    } );
                }

            }
            return this; // EXIT
        }

        //CASE: empty

        // hide on empty
        if ( this.hideOnEmpty ) {
            if ( this.$wrapper.is( ':visible' ) ) {
                this.$wrapper.slideUp( G5.props.ANIMATION_DURATION );
            } else {
                this.$wrapper.hide();
            }
        }
        this.$wrapper.addClass( 'emptyParticipantCollection' );

        // empty message
        if ( emptyMsg ) { // has msg
            cols = this.$el.closest( 'table' ).find( 'thead th:visible' ).length;

            // if cols is zero length, set to at least 1 (ie7)
            cols = cols === 0 ? 1 : cols;

            // if already appended, update colspan
            if ( this.$el.find( '.emptyMsg' ).length ) {
                this.$el.find( '.emptyRow td' ).attr( 'colspan', cols );
            } else { // if not already appended, append
                var type = this.$el.data( 'alert-type' );
                if( type == 'info' ) {
                    emptyEl = this.make( 'div', { 'class': 'emptyMsg alert-info alert-block' }, emptyMsg );
                }else if( type == 'warning' ) {
                    emptyEl = this.make( 'div', { 'class': 'emptyMsg alert alert-block' }, emptyMsg );
                }else if( type == 'success' ) {
                    emptyEl = this.make( 'div', { 'class': 'emptyMsg alert-success alert-block' }, emptyMsg );
                }else{
                    emptyEl = this.make( 'div', { 'class': 'emptyMsg alert-danger alert-block' }, emptyMsg );
                }


                if ( this.$el.prop( 'tagName' ) === 'TBODY' ) {
                    emptyEl = $( '<tr class="emptyRow"><td colspan="' + cols + '"></td></tr>' ).find( 'td' )
                        .append( emptyEl ).closest( 'tr' );
                }

                this.$el.append( emptyEl );
            }
        }

        return this;
    },
    updateCount: function() {
        var $cnt = this.$wrapper.find( '.participantCount' );
        $cnt.val( this.model.models.length );
    },
    events: {
        'click .remParticipantControl': 'removeParticipantAction'
    },
    removeParticipantAction: function( e ) {
        //hits the data model
        var cidToRem = $( e.currentTarget ).closest( '[data-participant-cid]' )
            .data( 'participant-cid' );
        var remModel = this.model.getByCid( cidToRem );
        this.model.remove( remModel );
        this.trigger( 'participantRemoved', remModel );

        // IE8 needs this else it will barf an error (due to Jquery event stuff)
        return false;
    },
    doSort: function( e ) {
        var $tar = $( e.currentTarget ),
            selector = $tar.data( 'sortSelector' ) || null,
            colIdx = $tar.index(),
            sorted = this.$el.find( 'tr td:nth-child(' + ( colIdx + 1 ) + ')' ),
            $sCont = $tar.find( '.sortControl' ),
            isAsc = $sCont.hasClass( 'asc' ),
            isDesc = $sCont.hasClass( 'desc' ),
            notSorted = !isAsc && !isDesc,
            that = this;

        // clear all sort state classes
        this.$wrapper.find( '.sortControl' ).removeClass( 'asc icon-arrow-1-up desc icon-arrow-1-down sorted' );

        // sort the elements using sortSelector or just use the text
        sorted = _.sortBy( sorted, function( td ) {
            var $e = $( td );

            if ( selector ) {
                $e = $e.find( selector );
            }

            return $e.text() || $e.attr( 'class' );
        } );

        // if was asc, reverse the sort order to desc
        if ( isAsc ) {
            sorted.reverse();
        }

        // append elements in sorted order
        _.each( sorted, function( td ) {
            that.$el.append( $( td ).closest( 'tr' ) );
        } );

        // apply sort state class(es)
        $sCont.addClass( notSorted || isDesc ? 'asc icon-arrow-1-up sorted' : 'desc icon-arrow-1-down sorted' );

    }
} );
