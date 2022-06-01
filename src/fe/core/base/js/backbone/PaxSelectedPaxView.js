/*eslint no-underscore-dangle: ["error", { "allow": ["_globalEvents"] }]*/
/*exported PaxSelectedPaxView */
/*global
TemplateManager,
PaxSelectedPaxCollection,
PaxSelectedPaxView:true
*/

PaxSelectedPaxView = Backbone.View.extend( {
    el: '#PaxSelectedPaxView',
    events: {
        'click .viewAll': 'expandViewMobile',
        'click .selectedView_recognize_button': 'openRecognitionPage',
        'click .selectedView_close_button': 'closeSearch',
        'click .selectedView-card': 'cardClicked',
        //'click .save-as-group': 'openSaveAGroup',
        'click .go-to-groups': 'checkGoToGroups',
        'click .deselectAll': 'removeAll'    },

    initialize: function( opts ) {
        _.bindAll( this );
        //console.log('PaxSelectedPaxView : opts', opts);
        if ( opts.el ) { this.el = opts.el; }

        this.collection = opts.selectedPaxCollection || new PaxSelectedPaxCollection();

        this.collection.setSelectUrl( opts.selectUrl );
        this.collection.setDeselectUrl( opts.deselectUrl );

        this.collection.bind( 'updatePaxSelectedItemView', this.updatePaxSelectedItemView, this );
        this.collection.bind( 'updatePaxSelectedMany', this.updatePaxSelectedMany, this );
        this.collection.bind( 'itemRemoved', this.itemRemoved, this );
        this.collection.bind( 'change:error', this.itemError, this );
        this.collection.bind( 'change:invalid', this.itemError, this );
        this.collection.bind( 'reset', this.reset, this );

        this.preSelectedParticipants = opts.preSelectedParticipants;
        this.tpl = 'PaxSelectedPaxView';

        this.saveGroup = opts.saveGroup || false;
        if ( this.$el.data( 'saveGroup' ) ) { this.saveGroup = this.$el.data( 'saveGroup' ); }
        /*
            // TODO: need to add all options
            hide button
            hide controls
            button action
            style aspects
            sidebar true - set slick breakpoints
        */
        this.recognition = opts.recognition;
        this.hasSidebar = opts.hasSidebar;
        this.hideControls = opts.hideControls;
        if ( !opts.dontRenderthat ) { this.render(); }
    },

    render: function() {
        var that = this;
        //console.log('PaxSelectedPaxView this.$el',this.$el);
        TemplateManager.get( that.tpl, function( tpl, vars, subTpls ) {
            that.$el.append( tpl );
            that.selectedSearchResultsViewTpl = subTpls.paxSelectedCardTpl;
            that.postRender();
        }, G5.props.URL_TPL_ROOT || G5.props.URL_BASE_ROOT + 'tpl/' );
    },

    postRender: function() {
        var ms;

        if( this.saveGroup ) {
            console.log( 'save a group is turned on ' );
            // need to enable/show dom elements
            this.$el.find( '.selectedPaxView' ).addClass( 'sag' );

            // this.groupsSaveEditView = new ProfilePageGroupsSaveEditView( {
            //     el: this.el,
            //     isCreateOnly: true,
            //     paxCollection: this.collection
            // } );
            // this.$el.find( '.save-as-group' ).show();
            this.$el.find( '.go-to-groups' ).show();
            // need to have a reference to save a group model

            //

        }
        //console.log('---------------this.$el',this.$el);
        this.$carousel = this.$el.find( '.selected-carousel' );
        //this.slickDone();
        if ( this.preSelectedParticipants ) {
            ms = [];
            // loop through and set selected parmeter on them
            _.each( this.preSelectedParticipants, function( participant ) {
                console.log( '-------participant', participant );
                participant.selected = true;
                ms.push( participant );
                //that.addItem(participant);
            } );
            this.collection.addManyModels( ms, true );
            this.preSelectedParticipants = null;

        } else if ( this.collection.models.length ) {
            this.updatePaxSelectedMany( this.collection.models );
        } else {
            this.startSlick();
        }


    },

    // slick initialized
    slickDone: function() {
       console.log( 'slickDONE' );
       /* var that=this;
        // if we have preselected participants load them up
        if ( this.preSelectedParticipants){
            console.log('slickDone', this.preSelectedParticipants);
            var ms=[];
            // loop through and set selected parmeter on them
            _.each( this.preSelectedParticipants, function(participant) {
                console.log('-------participant',participant);
                participant.selected = true;
                ms.push(participant);
                //that.addItem(participant);
            });
            this.collection.addManyModels(ms, true);
            this.preSelectedParticipants=null;

        } else if ( this.collection.length){
            console.log('this.collection', this.collection.length);
            _.each( this.collection.models, function(participant) {
                participant.set('selected', true);
                that.updatePaxSelectedItemView(participant);
            });
        } else {
            this.hide();
        }*/


        this.$paxView = this.$el.find( '.selectedPaxView' );
        // set UI elements
        if ( this.hideControls ) {
            this.$paxView.addClass( 'hidden-controls' );
        }

        if ( !this.collection.length ) { this.hide(); }

        if ( this.recognition ) {
            // hide the
            this.$el.find( '.selectedView_recognize_button' ).show();
            this.$el.find( '.selectedView_close_button' ).hide();
        } else {
            this.$el.find( '.selectedView_recognize_button' ).hide();
            this.$el.find( '.selectedView_close_button' ).show();
        }

        this.resizeListener();
        G5._globalEvents.on( 'windowResized', this.resizeListener, this );
        this.trigger( 'postRender' );
    },
    setCollectionReference: function( collectionReference ) {
        this.collection.setSyncSearchCollectionRef( collectionReference );
    },

    /***
     *                                                                 888
     *                                                                 888
     *                                                                 888
     *     .d8888b  8888b.  888d888 .d88b.  888  888 .d8888b   .d88b.  888
     *    d88P"        "88b 888P"  d88""88b 888  888 88K      d8P  Y8b 888
     *    888      .d888888 888    888  888 888  888 "Y8888b. 88888888 888
     *    Y88b.    888  888 888    Y88..88P Y88b 888      X88 Y8b.     888
     *     "Y8888P "Y888888 888     "Y88P"   "Y88888  88888P'  "Y8888  888
     *
     *
     *
     */


    // init our slick carousel-- this only works with 100% width errrr
    startSlick: function() {
        //TODO: this needs to be more dynamic
            /*$global_sidebar_width_tablet: 256px !default;
            $global_sidebar_width_desktop: 256px !default;
            $global_sidebar_width_desktopLarge: 512px !default;*/
        var that = this,
            maxNumCards = 8,
            cardWidth = 160 + 40, //card width plus padding
            offset = 120, // both arrows
            minBreak = 767, // min break we do not want for small mobile
            settingsArr = [];

        if ( !that.hasSidebar ) {
             _.times( maxNumCards, function( i ) {
                var numcards = i;
                var breakpoint;
                numcards = i - 1;
                breakpoint =  ( i * cardWidth ) + offset;
                 if ( breakpoint > minBreak ) {
                    settingsArr.push( {
                        breakpoint: breakpoint,
                        settings: {
                            slidesToShow: numcards,
                            slidesToScroll: numcards
                        }
                    } );

                }
            } );

        } else {
            // hard coding errr
            maxNumCards = 7;
            settingsArr = [
            {
                breakpoint: 640,
                settings: {
                    slidesToShow: 2,
                    slidesToScroll: 2
                }
            },
            {
                breakpoint: 840,
                settings: {
                    slidesToShow: 3,
                    slidesToScroll: 3
                }
            },
            {
                breakpoint: 1250,
                settings: {
                    slidesToShow: 4,
                    slidesToScroll: 4
                }
            },
            {
                breakpoint: 1640,
                settings: {
                    slidesToShow: 5,
                    slidesToScroll: 5
                }
            },
            {
                breakpoint: 1840,
                settings: {
                    slidesToShow: 6,
                    slidesToScroll: 6
                }
            },
            {
                breakpoint: 2000,
                settings: {
                    slidesToShow: 7,
                    slidesToScroll: 7
                }
            }
            ];

        }

        // add our min unslick
        settingsArr.push( {
                    breakpoint: minBreak,
                    settings: 'unslick'
                } );

        this.$carousel.slick( {
            // dots: false,
            infinite: false,
            // speed: 300,
            slidesToShow: maxNumCards,
            draggable: true,
            focusOnSelect: false,
            speed: G5.props.ANIMATION_DURATION,
            slidesToScroll: maxNumCards,
            // centerMode: false,
            // variableWidth: false,
            prevArrow: '<i class="icon-arrow-1-left slick-arrow slick-prev"></i>',
            nextArrow: '<i class="icon-arrow-1-right slick-arrow slick-next"></i>',
            responsive: settingsArr
        } );

        //console.log(settingsArr);

      //   setTimeout( function() {
      //     that.slickDone();
      // }, 0 );
       that.slickDone();
    },


     /***
     *     .d888                            888    d8b
     *    d88P"                             888    Y8P
     *    888                               888
     *    888888 888  888 88888b.   .d8888b 888888 888  .d88b.  88888b.  .d8888b
     *    888    888  888 888 "88b d88P"    888    888 d88""88b 888 "88b 88K
     *    888    888  888 888  888 888      888    888 888  888 888  888 "Y8888b.
     *    888    Y88b 888 888  888 Y88b.    Y88b.  888 Y88..88P 888  888      X88
     *    888     "Y88888 888  888  "Y8888P  "Y888 888  "Y88P"  888  888  88888P'
     *
     *
     *
     */
    itemError: function( participant ) {
        var $el = this.$carousel.find( '[data-id="' + participant.id + '"]' );
        if ( participant.get( 'error' ) || participant.get( 'invalid' ) ) {
            $el.addClass( 'error' );
        } else {
            $el.removeClass( 'error' );
        }
    },
    setSelectedCount: function() {
        var tot = this.totalAdded(),
            btn = this.$el.find( '.save-as-group' );
        this.$el.find( '.selectedCount' ).text( tot );
        if( tot >= 2 ) {
            btn.prop( 'disabled', false );
        }else{
            btn.prop( 'disabled', true );
        }
    },

    totalAdded: function() {
        return this.collection.size();
    },

    hide: function() {
        this.$el.removeClass( 'selectedOpen' );
        this.trigger( 'selectedClosed' );
        if ( this.$el.find( '.hidden-controls' ).length ) { this.$el.fadeOut( G5.props.ANIMATION_DURATION ); }
    },
    show: function() {
        //this.$el.find('.selectedPaxView').show();
        this.$el.addClass( 'selectedOpen' );
        this.trigger( 'selectedOpen' );
        if ( this.$el.find( '.hidden-controls' ).length ) { this.$el.fadeIn( G5.props.ANIMATION_DURATION ); }
    },

    //
    removeItemDomElement: function( id ) {
        //console.log(id);
        var c = this.$carousel.find( '[data-id="' + id + '"]' ),
            that = this,
            num;

        c.addClass( 'fadeOut' );
        c.one( 'transitionend MSTransitionEnd webkitTransitionEnd oTransitionEnd webkitAnimationEnd oanimationend msAnimationEnd animationend', function() {
                // Animation complete.
                $( this ).removeClass( 'fadeOut' );
                num = that.$el.find( '.selectedView-card' ).index( $( this ) );
                if ( that.$carousel.hasClass( 'slick-initialized' ) ) {
                    that.$carousel.slick( 'slickRemove', num );
                    //$( this).parent().remove();
                } else {
                    //that.$carousel.slick('slickRemove',num);
                    $( this ).parent().remove();

                }

                 // hide or show this
                if ( that.collection.length >= 1 ) {
                    that.show();
                } else {
                    that.hide();
                    that.$el.find( '.slick-track' ).html( '' );
                }

                //console.log(that.$carousel);
        } );
        //this.$carousel.find('[data-id="' + id + '"]').parent().remove();
    },
    addItemDomElement: function( participant, many ) {
            //console.log('whynot',participant.attributes);
         var that = this,
            templateText = this.selectedSearchResultsViewTpl( participant.attributes );
            //console.log('why');
        if ( !this.$carousel.hasClass( 'slick-initialized' ) ) {
            this.$carousel.append( templateText );
        } else {
            this.$carousel.slick( 'slickAdd', templateText, true );
            // FIX TO STOP CARD JUMP ON FIRST CARD
             if ( !many ) {
                setTimeout( function() {
                    //that.$carousel.find('[data-id="' + participant.id + '"]').delay(50).fadeTo(200,1);
                    //that.$carousel.find('[data-id="' + participant.id + '"]').animate({ width: '160px'}, 500);
                    that.$carousel.slick( 'setPosition' );
                    // go to first slide
                    that.$carousel.slick( 'slickGoTo', 0, false );
                }, 1 );
            }
        }
        if ( !many ) {
            that.$carousel.find( '[data-id="' + participant.id + '"]' ).addClass( 'fadeIn' );
            that.$carousel.find( '[data-id="' + participant.id + '"]' ).one(
            'transitionend MSTransitionEnd webkitTransitionEnd oTransitionEnd webkitAnimationEnd oanimationend msAnimationEnd animationend',
            function() {
                $( this ).removeClass( 'fadeIn' );

            }
       );
        }

    },

    itemRemoved: function( participant ) {
        //console.log('itemRemoved');
        this.removeItemDomElement( participant.id );
        this.setSelectedCount();

    },
    updatePaxSelectedMany: function( models ) {
        var that = this,
            tempText = '';

        if ( this.$carousel.hasClass( 'slick-initialized' ) ) {
            this.$carousel.slick( 'unslick' );
            this.$carousel.find( '.item' ).css( 'width', '' );
        }

        _.each( models, function( participant ) {
            //that.addItemDomElement(participant,true);
            // check if we have it???
            //console.log(participant.id, participant.attributes.id);
            //console.log(that.$carousel.find('[data-id="' + participant.attributes.id + '"]'));
            if ( !that.$carousel.find( '[data-id="' + participant.attributes.id + '"]' ).length ) {
               // console.log('adding>>>>');
                tempText += that.selectedSearchResultsViewTpl( participant.attributes );
            }

        } );
        this.$carousel.append( tempText );
        this.startSlick();

        setTimeout( function() {
                //that.$carousel.find('[data-id="' + participant.id + '"]').delay(50).fadeTo(200,1);
                //that.$carousel.find('[data-id="' + participant.id + '"]').animate({ width: '160px'}, 500);
                that.$carousel.slick( 'setPosition' );
                // go to first slide
                that.$carousel.slick( 'slickGoTo', 0, false );
            }, 0 );

        if ( this.collection.length >= 1 ) {
            this.show();
        }

        this.setSelectedCount();
    },
    //
    updatePaxSelectedItemView: function( participant ) {
        //preSelectedParticipantsconsole.log('updatePaxSelectedItemView');
        // card has been added or removed need to render it or remove it
        if ( participant.get( 'selected' ) ) {
            this.addItemDomElement( participant );
        } else {
            // remove dom element
            this.removeItemDomElement( participant.id );
        }
        // hide or show this view
        if ( this.collection.length >= 1 ) {
            this.show();
        }
        this.setSelectedCount();
    },

    // remove item
    removeItem: function( id ) {
        //console.log('removeItem');
        var participant = this.collection.get( id ).attributes;
        // check if we need to remove it
        if ( participant ) {
            // remove dom element
            // does this get called when dome el is removed
            //this.removeItemDomElement(id);
            //remove from collections
            this.collection.remove( participant );
            // tell others
            G5._globalEvents.trigger( 'updateItemSelection', { id: id, selected: false } );
        }

        this.setSelectedCount();
        //this.trigger('deselectCard',{id:id});
    },


    // add item
    addItem: function( participant ) {
        // check if it is already loaded - prevent duplicates
        if ( this.collection.get( participant.id ) ) { return; }

        // if not we need to add it
        this.collection.add( participant );
        this.addItemDomElement( participant );

    },

    // collection reset update the dom
    reset: function() {
        // empty cards
        this.$el.find( '.slick-track' ).html( '' );
        // hide this
        this.hide();
    },

    // de select all cards and remove them from the this
    removeAll: function() {
        this.collection.remove( this.collection.models );
    },

    // update the text on view all when this is expanded or not
    setViewAllText: function() {
        var $viewAll = this.$el.find( '.viewAll' );
        if ( this.$el.find( '.selectedCards' ).hasClass( 'expanded' ) ) {
            $viewAll.text( $viewAll.data( 'view-less' ) );
        } else {
            $viewAll.text( $viewAll.data( 'view-all' ) );
        }
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
     //
     // openSaveAGroup: function() {
     //    // check if we are updating or saving

     //    // show save a group
     //    this.$el.find( '#saveEditView' ).show();

     // },
     checkGoToGroups: function() {
         this.$el.find( '.go-to-groups-modal' ).modal( );
     },


    // called when card is clicked
    cardClicked: function( e ) {
        var card = $( e.target ).closest( '.selectedView-card' ),
            paxId = card.data( 'id' );
        this.removeItem( paxId );
    },

    expandViewMobile: function() {
        this.$el.find( '.selectedCards' ).toggleClass( 'expanded' );
        this.setViewAllText();
    },

    resizeListener: function() {

        var breakpoint = G5.breakpoint.value,
            wh,
            dh,
            dif;

        if ( breakpoint !== 'mobile' && breakpoint !== 'mini' ) {
            this.$el.find( '.selectedCards' ).removeClass( 'expanded' );
            this.setViewAllText();
            if ( !this.$carousel.hasClass( 'slick-initialized' ) ) {
                //this.$el.find('.selected-carousel').html('');
                this.startSlick();
                //console.log( this.collection);
            }
            if ( this.$el.hasClass( 'fixedPos' ) ) { this.$el.find( '.selectedCards' ).height( 'initial' ); }
            // TODO: move this to class
            if ( !this.hideControls ) {
                this.$el.find( '.selectedCards' ).css( 'top', '75px' );
            }

        } else {
            if ( this.$carousel.hasClass( 'slick-initialized' ) ) {
                this.$carousel.slick( 'unslick' );
                this.$carousel.find( '.item' ).css( 'width', '' );
            }

            // set height of expanded this and top.
            wh = $( window ).innerHeight();
            dh = this.$el.find( '.selectedPaxView' ).innerHeight();
            dif = wh - dh;

            if ( this.$el.hasClass( 'fixedPos' ) ) {
                //
                this.$el.find( '.selectedCards' ).height( dif );
                this.$el.find( '.selectedCards' ).css( 'top', dif );
            }

        }
    },


    /***
     *                      888                    d8b 888
     *                      888                    Y8P 888
     *                      888                        888
     *    .d8888b  888  888 88888b.  88888b.d88b.  888 888888 .d8888b
     *    88K      888  888 888 "88b 888 "888 "88b 888 888    88K
     *    "Y8888b. 888  888 888  888 888  888  888 888 888    "Y8888b.
     *         X88 Y88b 888 888 d88P 888  888  888 888 Y88b.       X88
     *     88888P'  "Y88888 88888P"  888  888  888 888  "Y888  88888P'
     *
     *
     *
     */

     // closeSearch
     closeSearch: function() {
        //
        G5._globalEvents.trigger( 'paxSearch:exitSearch', {} );
     },
    // recognize all that are added
    openRecognitionPage: function( e ) {
        var url = G5.props.URL_START_RECOGNITION,
            s = '',
            form;

        this.trigger( 'pageLoadFull' );
         e.preventDefault();
         //console.log(e.target)
         // need to get all  participants ids, and send them via post to the participants page
        this.collection.each( function( model, i ) {

            s += '<input type="hidden" name="claimRecipientFormBeans[' + i + '].userId" value="' + model.id + '" />';
         // participantIDs.push(model.id);
        } );

        form = $( '<form action="' + url + '" method="post">' + s + '</form>' );
        $( 'body' ).append( form );
        $( form ).submit();
    },
    destroy: function( dontdestroycollection ) {
        G5._globalEvents.off( 'windowResized', this.resizeListener, this );
        if ( !dontdestroycollection ) { this.collection.destroy(); }
        this.undelegateEvents();
    }

} );
