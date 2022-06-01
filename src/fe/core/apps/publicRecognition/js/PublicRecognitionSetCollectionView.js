/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported PublicRecognitionSetCollectionView*/
/*global
$,
_,
Backbone,
G5,
PublicRecognitionSetCollection,
PublicRecognitionModelView,
PublicRecognitionSetCollectionView:true
*/
//Public Recognition Set Collection VIEW
// - several templates required

PublicRecognitionSetCollectionView = Backbone.View.extend( {

    //override super-class initialize function
    initialize: function( opts ) {

        //handy jquery handles
        this.$tabs = opts.$tabsParent;
        this.$recs = opts.$recognitionsParent;
        this.isLoading = false;
        this.scrollPos = 0;
        //templates
        this.tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'publicRecognition/tpl/';
        this.recogTplName = 'publicRecognitionItem';
        this.commentTplName = 'publicRecognitionComment';

        //our model
        this.model = new PublicRecognitionSetCollection();

        //when this model gets loaded, then render this
        this.model.on( 'dataLoaded', function( nameId, fromIndex ) {
            this.render( nameId, fromIndex );

            // When a tab is switched, the placeholder plugin for IE breaks.
            // This clears any inputs and reinitializes the placeholder() plugin.
            // Removing the value clear on switching tabs for bugzilla[64529] as the readonly fixed value points were being removed.
            this.$el
                .find( 'input, textarea' )
                .removeClass( 'placeholder' )
                .placeholder();
        }, this );

        //mark the view as dataLoading for the first time we load data
        this.$el.addClass( 'dataLoading' );

        //put the view into a loading state
        this.setStateLoading();

        //load budgets (data-* attrs are added to request params)
        this.model.loadData( opts.recogSetId, null, null, opts.participantId, opts.modelParams );

        this.on( 'recViewRendered', this.groupMultiRecipients, this );
        $( '.publicRecognition' ).on( 'click', '.dropdown-menu', function ( e ) {

          //console.log( $( e.target ).closest( 'a' ) );

          if( $( e.target ).closest( 'a' ).data( 'name-id' ) === 'division' || $( e.target ).closest( 'a' ).data( 'name-id' ) === 'department' || $( e.target ).closest( 'a' ).data( 'name-id' ) === 'country' ) {
              e.stopPropagation();

          }
        } );
        $( '.publicRecognition' ).on( 'click', '.dropdown-toggle', function( e ) {
            console.log( $( this ).parents( '.publicRecognition' ).height() );
            if( $( this ).parents( '.publicRecognition' ).height() < 650 ) {
                $( '.pubRecTabs' ).addClass( 'fix-height' );
            } else {
                $( '.pubRecTabs' ).removeClass( 'fix-height' );
            }
        } );
        this._recViewCache = {};

        G5._globalEvents.on( 'windowScrolled', this.doViewMore, this );
        G5._globalEvents.on( 'updatePRWall', function( canReload ) {
        	if ( canReload ) {
        		var activeNameId = $( '.publicRecognition .pubRecTabs' ).find('li.active a').data('nameId');
            	this.model.loadData( activeNameId, null, null, opts.participantId, opts.modelParams );
        	}        	
        }, this );

    },

    //smart render, based on the args, it will decide what needs to be done
    render: function( nameId, fromIndex ) {
        var defSet,
        that = this;
        // no nameId? then this is first render, render tabs
        if( !nameId ) {
            this.renderTabs( function() {} );
            defSet = this.model.getDefaultRecognitionSet();

            if( defSet ) {
            		//If nameId is global,don't ignore filterToken.Load fiter. 
              
                    //this.$tabs.find( '.pub-rec-tab[data-name-id=' + defSet.get( 'nameId' ) + ']' ).click();
                	this.addFilterToken( $( '.pub-rec-tab[data-name-id=' + defSet.get( 'nameId' ) + ']' ) ); //Triggering the Filter token by default onload
                    this.renderTabContent( defSet.get( 'nameId' ) ); //No need to trigger the click event manually
                
            } else {
                // simulate click on first tab
                this.renderTabContent( 'global' );
            }

            // remove the dataLoading class now that the first load is done
            this.$el.removeClass( 'dataLoading' );
        }

        // else, this has been a specific set load
        else if( nameId ) {
            // console.log( nameId );
            // if from index, then this was a load more
            if( fromIndex ) {
                //render more
                this.renderMoreRecognitions( nameId, fromIndex );
            }

            //else, just the first recognitions of a set
            else {
                //render recognitions for set
                this.renderTabContent( nameId );
            }
        }

        // take the view out of the loading state
        setTimeout( function() {
            that.setStateLoaded();
        }, 100 );
    },

    renderTabs: function() {
        var that = this;
        //TABS - each 'recognitionSet' gets a tab
        that.$tabs.empty();
        _.each( this.model.models, function( recSet ) {

            //get the count of new items
            //not sure about this one, commented out for now (aaron)
            // var numNew = 0;//(_.filter(recSet.get('recognitions'),function(rec){return rec.isNew})).length;

            //build the li>a element
            var $a = $( '<a />' )
                        .attr( {
                            'title': recSet.get( 'name' ),
                            'href': '#',
                            'class': 'pub-rec-tab pub-rec-tab-' + recSet.get( 'nameId' ),
                            'data-name-id': recSet.get( 'nameId' ),
                            'data-total-count': recSet.get( 'totalCount' )
                        } )
                        .html( '<span>' + recSet.get( 'name' ) + '</span>' ),
                $li = $( '<li />' ).html( $a );

                if( $li.find( 'a' ).data( 'name-id' ) === 'country' ) {
                    $li.prepend( '<hr/>' );
                }
                if( recSet.get( 'nameId' ) === 'division' ) {

                    var $listHtml = '<ul style="display:none;">';
                    // console.log( that.model.get( 'list' ) );
                    _.each( recSet.get( 'list' ), function( item ) {

                        var $li = '<li data-list-val="' + item.division + '" >' + item.division + '</li>';
                        $listHtml += $li;
                    } );
                    $listHtml += '</ul>';
                        $li.find( 'a' ).after( $listHtml );
                        $li.find( 'a' ).prepend( '<i class="icon-plus-circle"></i>' );
                } 
                else if( recSet.get( 'nameId' ) === 'department' ) {

                    var $listHtml = '<ul style="display:none;">';
                    // console.log( that.model.get( 'list' ) );
                    _.each( recSet.get( 'list' ), function( item ) {

                        var $li = '<li data-list-val="' + item.code + '" >' + item.name + '</li>';
                        $listHtml += $li;
                    } );
                    $listHtml += '</ul>';
                        $li.find( 'a' ).after( $listHtml );
                        $li.find( 'a' ).prepend( '<i class="icon-plus-circle"></i>' );
                } else if( recSet.get( 'nameId' ) === 'country' ) {

                    var $listHtml = '<ul style="display:none;">';
                    // console.log( that.model.get( 'list' ) );
                    _.each( recSet.get( 'list' ), function( item ) {

                        var $li = '<li data-list-val="' + item.countryCode + '" >' + item.countryName + '</li>';
                        $listHtml += $li;
                    } );
                    $listHtml += '</ul>';
                        $li.find( 'a' ).after( $listHtml );
                        $li.find( 'a' ).prepend( '<i class="icon-plus-circle"></i>' );
                }
            //append to tabs container
            that.$tabs.append( $li );
        } );
    },

    renderTabContent: function( id ) {
      // console.log(id);
        var recogSet = this.model.getRecognitionSet( id ),
            hasFollowees = recogSet.get( 'hasFollowees' ),
            recogModels = recogSet.recognitions.models,
            $recsCont = $( this.$recs.find( '.publicRecognitionItems' )[ 0 ] ),
            $tab = this.$tabs.find( '[data-name-id=' + id + ']' ),
            $followControls = this.$el.find( '.follow-list-links' ),
            that = this;

            // console.log(recogModels);

        //set active pub rec set nameId
        this._activeRecSet = recogSet;

        this.$tabs.find( 'li' ).removeClass( 'active' );
        $tab.closest( 'li' ).addClass( 'active' );

        this.$el.find( '.createFollowListWrapper' ).hide();
        this.$el.find( '.noRecognitionsFollowListWrapper' ).hide();

        this.$recs.show();

        //set a classname for styling (remove previous)
        this.$recs.removeClass( function( i, c ) {
            c = c.match( /[a-zA-Z]+Collection($| )/ );
            return c && c.length ? c[ 0 ] : '';
        } ).addClass( recogSet.get( 'nameId' ) + 'Collection' );


        $recsCont.removeClass( 'emptySet' );

        $recsCont.empty();

        this.appendRecognitionViews( recogModels );

        //if we are in the 'followed' tab, show the view/edit follow list link
        $followControls[ id === 'followed' ? 'show' : 'hide' ]();

        //no recognitions? render empty message
        if( recogModels.length === 0 ) {
            //if we are in the 'followed' PubRecSet, then disp. special message
            if ( ( id === 'followed' ) && ( hasFollowees === false ) ) {
                //hide the controls b/c we are showing the button to create follow list
                $followControls.hide();
                this.$recs.hide();
                this.$el.find( '.createFollowListWrapper' ).show();
            }else if( ( id === 'followed' ) && ( hasFollowees === true ) ) {
                this.$recs.hide();
                this.$el.find( '.noRecognitionsFollowListWrapper' ).show();
            }else{
                $recsCont
                    .addClass( 'emptySet' )
                    .append( this.make( 'h2', {}, this.$recs.data( 'msgEmpty' ) ) )
                    .find( 'h2' ).prepend( '<i class="icon-team-1" />' );
            }
        }

        // checking for animations on screen already with a slight delay for visual pleasure
        setTimeout( function() {
            that.checkAnimations();
        }, 400 );
    },

    renderMoreRecognitions: function( nameId, startInd ) {
        var recSet = this.model.getRecognitionSet( nameId );
        //apend the tail (_.rest) of the recognitions
        this.appendRecognitionViews( _.rest( recSet.recognitions.models, startInd ), startInd );

    },

    events: {
        'click .pub-rec-tab': 'doTabClick',
        'click .viewAllRecognitions': 'doViewMore',
        'click .tokenName': 'doResetDrawer',
        'click .pubRecTabs li ul li': 'doCountryDeptFilterSelect'
    },

    doTabClick: function( e ) {
        var $tar = $( e.currentTarget ),
            id = $tar.data( 'nameId' );

        e.preventDefault();

        if( id === 'country' || id === 'department' || id === 'division' ) {
            // $tar.parents( '.dropdown' ).addClass( 'open' );
            this.displayFilterDropdown( id, $tar );
        } else {

            this.setStateLoading( 'tabChange' );
            this.addFilterToken( $tar );
			this.$el.find( '#countryDeptFilter' ).val( '' );
            this.model.loadData( id, null, null, null, this.options.modelParams );
        }


    },
    doCountryDeptFilterSelect: function( e ) {
        var $tar = $( e.currentTarget ),
            id = $tar.parent( 'ul' ).siblings( 'a' ).data( 'nameId' ),
            val = { 'listValue': $tar.data( 'list-val' ) };

            this.setStateLoading( 'tabChange' );

            this.addFilterToken( $tar );

            this.model.loadData( id, null, null, null, this.setCountryDeptValue( true, $tar.data( 'list-val' ) ) );
			
            e.preventDefault();
    },
    displayFilterDropdown: function( id, $tar ) {
        $tar.siblings( 'ul' ).slideToggle(  );
        if( $tar.find( 'i' ).hasClass( 'icon-plus-circle' ) ) {
            $tar.find( 'i' ).removeClass( 'icon-plus-circle' );
            $tar.find( 'i' ).addClass( 'icon-minus-circle' );
        } else {
            $tar.find( 'i' ).removeClass( 'icon-minus-circle' );
            $tar.find( 'i' ).addClass( 'icon-plus-circle' );
        }

    },
    addFilterToken: function( target ) {
        var $filterName, $span, $i;
        if( target.parent( 'ul' ).siblings( 'a' ).data( 'name-id' ) === 'country' || target.parent( 'ul' ).siblings( 'a' ).data( 'name-id' ) === 'department' || target.parent( 'ul' ).siblings( 'a' ).data( 'name-id' ) === 'division'  ) {
            $filterName = target.parent( 'ul' ).siblings( 'a' ).text() + ': ' + target.text();
        } else {
            $filterName = target.text();
        }

        $span = $( '<span />' )
                    .attr( {
                        'class': 'tokenName btn btn-inverse btn-primary dropdown-toggle',
                        'data-toggle': 'dropdown'
                    } )
                    .html( $filterName );
        $i = $( '<i />' )
                .attr( {
                    'class': 'icon-arrow-1-down arrowToken'
                } );

        $span.append( $i );

        this.$el.find( '.tokenName' ).remove();
        this.$el.find( '.dropdown' ).prepend( $span );

        this.$el.find( '.addNewFilter' ).hide();

    },

    doResetDrawer: function( e ) {
        var $dropdownFilters = this.$el.find( '.pubRecTabs li' ),
            icons = $dropdownFilters.find( 'i' );

        $dropdownFilters.find( 'ul' ).hide();
        _.each( icons, function( val ) {
                $( val ).removeClass( 'icon-minus-circle' );
                $( val ).addClass( 'icon-plus-circle' );
        } );


    },

    appendRecognitionViews: function( recModels, startInd ) {
        var that = this,
            $recsCont = $( this.$recs.find( '.publicRecognitionItems' )[ 0 ] );
        _.each( recModels, function( rec, i ) {
          // console.log(rec);
          // console.log(i);
          var recView = that._recViewCache[ rec.id ] || new PublicRecognitionModelView( {
                    model: rec, //give the view this recognition model
                    tplName: that.recogTplName,
                    commentTplName: that.commentTplName,
                    isHideComments: true //hide all but first comment
                } );
              //$rendered = $( recView.render().el );

            //$recsCont.append( $( recView.render().el ) );

            recView.render().el;

             recView.on( 'recItemRendered', function( el, modelId, videoUrl ) {
                if( rec.get( 'promotionType' ) === 'purl' ) {
                    if( rec.get( 'hasPurl' ) === true ) {// This is the place you have to add the New SA logic for SA newSaItem
                        el.addClass( 'newSaItem' );
                    } else {
                        el.addClass( 'purlItem' );
                    }                    
                } else if ( rec.get( 'promotionType' ) === 'nomination' ) {
                    el.addClass( 'nominationItem' );
                }
                // Cheers Customization - Celebrating you wip-62128 - starts - Added additional cheers promo check
                else if ( rec.get( 'cheersPromotion' ) === true ) {
                    el.addClass( 'cheersItem' );
                }
                
                el.hide();
                $recsCont.append( el );
                el.fadeIn(  );
                if( videoUrl ) {
                    recView.initVideoJs( modelId );
                }
            } );
















            // scroll the new recognitions into place (only on the first one)(and if scrolling isn't suppressed)
            // if(startInd && i === recsTotal && that.options.suppressScrolling !== true ) {
            //     $(window).scrollTo($rendered, G5.props.ANIMATION_DURATION*2, {
            //         axis : 'y',
            //         offset : {
            //             top: 0,
            //             left: 0
            //         }
            //     });
            // }


            recView.initReadMore();

            //if was cached, then delegateEvents (they were undelegated)
            if( that._recViewCache[ rec.id ] ) {
                recView.delegateEvents();
            }

            //cache view
            that._recViewCache[ rec.id ] = recView;

        } );

    },

    // check animations
    checkAnimations: function() {

         // check if el is on the screen or not
        var windowTop = $( window ).scrollTop() + $( window ).height();
       this.$el.find( '.celebrationBalloons.animation-not-started' ).each( function ( index ) {
            if ( windowTop > ( $( this ).offset().top + 100 ) ) {
                $( this ).removeClass( 'animation-not-started' );
                $( this ).find( '.balloon' ).addClass( 'balloonAnimation' );
            }
        } );
       this.$el.find( '.sparkleContainer.animation-not-started' ).each( function ( index ) {
            if ( windowTop > ( $( this ).offset().top + 100 ) ) {
                $( this ).removeClass( 'animation-not-started' );
                $( this ).find( '.sparkleImg' ).addClass( 'sparkleAnimation' );
            }
        } );

    },

    doViewMore: function( e ) {
        if( !this._activeRecSet ) {return;}
        var rs = this._activeRecSet,
            nameId = rs.get( 'nameId' ),
            //start on next element after last of old length
            startInd = rs.recognitions.length,
            recogCount = rs.get( 'totalCount' ),
            isMore = true;

        e.preventDefault();

       this.checkAnimations();
       var cpos = $( window ).scrollTop();
       // if we are scrolling up do not lazy load // or if the sidebar is expanded
       if( this.scrollPos  > cpos  || $( 'body' ).hasClass( 'sidebar-expanded' ) ) {
            this.scrollPos = cpos;
            return;
       }
       this.scrollPos = cpos;


        if( !this.isLoading && cpos + $( window ).height() > $( document ).height() - 200 ) {
            if( startInd && recogCount > startInd ) {

                this.setStateLoading( 'more' );

                if( nameId === 'country' || nameId === 'department'  || nameId === 'division') {
					this.model.loadData( nameId, startInd, isMore, null, this.setCountryDeptValue(true) );
				}  
				else {
					this.model.loadData( nameId, startInd, isMore, null, this.options.modelParams );
				}
            }

        }
    },

    getViews: function() {
        return this._recViewCache || null;
    },

    setStateLoading: function( mode ) {
        var spinProps = {};

        if( mode ) {
            spinProps.classes = mode;
        }

        G5.util.showSpin( this.$el, spinProps );

        this.isLoading = true;
    },

    setStateLoaded: function() {


        G5.util.hideSpin( this.$el );

        this.isLoading = false;
    },
	
	setCountryDeptValue: function( flag , v ) {
		var aDD, additionalParam;
		
		if( v ) {
			aDD = this.$el.find( '#countryDeptFilter' ).val( v ); 
		}
		if( this.$el.find( '#countryDeptFilter' ).val() != '' ) {
			additionalParam = { 'listValue': this.$el.find( '#countryDeptFilter' ).val() };
		}
		return additionalParam;
	}

} );
