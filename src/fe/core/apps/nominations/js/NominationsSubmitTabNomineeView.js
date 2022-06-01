/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported NominationCollectionView, NominationsSubmitTabNomineeView */
/*global
console,
$,
_,
G5,
Backbone,
PageView,
TemplateManager,
NominationCollectionView,
ParticipantCollectionView,
NominationsSubmitTabNomineeView:true
*/
NominationsSubmitTabNomineeView = PageView.extend( {

    initialize: function ( opts ) {
        var that = this;

        this.calcObj = null;
        this.savedCalcObj = null;
        this.savedCalcRows = [];
        this.calcData = null;
        this.didCalc = false;
        // NominationsSubmitPageView parent container for this tab
        this.containerView = opts.containerView;
        this.promoIds = opts.containerView.allIds;
        this.paxCollection = this.containerView.paxCollection;
        this.recipientSearchView = this.options.recipView;
        this.selectedPromoModel = this.containerView.model.get( 'promotion' );
        console.log( this );

        // participant model/collection
        console.log( this.containerView );
        // console.log(this.containerView);
        // this.participantModel = this.containerView.model.get('participants');
        // this.paxCollection = new Backbone.Collection(this.participantModel);
        console.log( this );
        //template names
        this.tpl = 'nominationsSubmitNomineeTpl';
        this.tplPath = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'nominations/tpl/';

        this.$wrapper = this.$el.closest( '.participantCollectionViewWrapper' );
        this.$wrapper.on( 'click', '.sameForAllTip a', function( e ) {that.doSameForAllClick( e );} );
        G5._globalEvents.on( 'paxSearch:exitSearch', this.initTeamSections, this );
        if( this.selectedPromoModel.individualOrTeam ) {
            this.selectedPromoModel.individualOrTeam = this.selectedPromoModel.individualOrTeam.toLowerCase();
        }
        this.render();

        this.setupEvents();


    },
    initRecipientView: function () {

                this.recipientView = new NominationCollectionView( {
                    el: this.$el.find( '#recipientsView' ),
                    tplName: 'participantRowAwardItem', //override the default template with this inline tpl
                    model: this.containerView.paxCollection,
                    parentView: this,
                    selectedPromoModel: this.selectedPromoModel
                } );

                this.awardTypeSetup();

    },
    events: {
        'keyup .awardPointsInp': 'doPointsKeyup',
        'keydown .awardPointsInp': 'doPointsKeydown',
        'focus .awardPointsInp': 'doPointsFocus',
        'blur .awardPointsInp': 'decimalCheck',
        'mouseenter .optOut': 'showOptOutTip',
        'mouseleave .optOut': 'hideOptOutTip',

        'click .showRecipNodes': 'showRecipNodesTip',
        'click .changeRecipNodeTip a': 'selectRecipNode',
        'click #saveTeamAsGroup': 'toggleGroupValidation',


        'click .calcLink': 'doViewRecognitionCalculator',

        'click #nomNext': 'triggerDone',
        'change #selectGroup': 'changeGroup'
    },

    setupEvents: function() {
        var that = this;

        this.containerView.model.on( 'calcLoaded', function( calcObj ) {
            that.setCalcObj( calcObj );
        } );

        // this.containerView.model.on('paxGroupsLoaded', this.renderPaxGroups, this);

        this.containerView.on( 'participantsLoaded', function( data ) {
            //  that.setPaxGroups(data);
            console.log( 'loaded' );
             that.updateTabName( 'stepNominee' );
        } );

		this.containerView.paxCollection.on( 'remove', this.removeParticipant, this );

        // this.containerView.model.on('participantsLoadedGroups', function(data){
        //     that.setPaxGroups(data);
        // });
    //     this.containerView.model.on('nodeUpdated', this.resetSearchOnChange, this);
    //     this.containerView.on('nomTypeChange', this.resetSearchOnChange, this);
    },

    render: function() {
        var $container = this.$el,
            // editMode = this.selectedPromoModel.isEditMode,
            self = this;

        TemplateManager.get( this.tpl, function( tpl, vars, subTpls ) {

            $container.empty().append( tpl( self.selectedPromoModel ) );


                // self.containerView.model.loadParticipants(self.promoIds);


            if( self.containerView.model.promoChanged ) {
                self.$el.find( '.wtvTabName' ).show();

            }

            // self.initParticipantSearchSection();
                self.initTeamSections();

                // self.test();
                self.initRecipientView();
        }, this.tplPath );
    },


    initRecipientSection: function() {


    },

    initTeamSections: function() {
        var $teamName = this.$el.find( '.teamSection' );
		var selectedPaxCount = this.containerView.paxCollection;
		var $nomiPromoType = this.selectedPromoModel.nominatingType;
		if( typeof this.recipientSearchView !== 'undefined' ) {
			if( this.selectedPromoModel.individualOrTeam === 'individual' ) {
				this.recipientSearchView.participantSelectMode = 'single';
			} else {
				this.recipientSearchView.participantSelectMode = 'multiple';
			}
		}
		if( ( this.isSingleSelectMode() === false ) && ( this.selectedPromoModel.nominatingType === 'both' && selectedPaxCount.length > 1 ) ) {
            this.containerView.model.loadPaxGroups( this.promoIds );

            $teamName.show();
            // $groupSelect.show();
        }
		else if( ( this.selectedPromoModel.nominatingType === 'both' && selectedPaxCount.length === 1 ) ) {

            $teamName.hide();
            // $groupSelect.show();
        }
        else if( this.isSingleSelectMode() === false ) {
            this.containerView.model.loadPaxGroups( this.promoIds );

            $teamName.show();
            // $groupSelect.show();
        }

		else {
            $teamName.hide();
            // $groupSect.hide();
            // $groupSelect.hide();

        }

		// set inidividual or team value in hidden
		this.$el.find( '#hiddenNominationType' ).val ( $nomiPromoType );

    },
    resetSearchOnChange: function() {
        console.log( this );
        if( this.recipientSearchView ) {
            this.paxCollection.reset();
            this.recipientSearchView.model.removeAllFilters();
                this.$el.find( '.addRecipientsWrapper' ).hide();
                // make sure the search is visible
                this.recipientSearchView.$el.show();
            }

    },
    renderPaxGroups: function() {
        // var $selectGroupCont = this.$el.find( '.selectGroupContainer' ),
        //     groupModel = this.containerView.model.get( 'groups' );

        // TemplateManager.get('selectGroup', function(tpl) {
        //     $selectGroupCont.empty().append(tpl(groupModel));
        // });
    },

    setPaxGroups: function( participants ) {
        this.paxCollection.reset();

        this.paxCollection.add( participants );
    },

    changeGroup: function( e ) {
        var $tar = $( e.currentTarget ),
            $selectedOption = $tar.find( 'option:selected' ),
            $count = $selectedOption.data( 'paxCount' ),
            val = parseInt( $tar.val(), 10 ),
            maxParticipants = this.selectedPromoModel.maxParticipants,
            $groupCheck = this.$el.find( '#saveTeamAsGroup' ),
            $groupName = this.$el.find( '#groupName' ),
            isGroup = true,
            data;

        if( maxParticipants && $count > maxParticipants ) {
            this.containerView.onNavErrorTip( 'maxParticipantsGroup', $tar );
            return false;
        }

        if( $groupCheck.is( ':checked' ) && $selectedOption.is( ':not(.defaultOption)' ) ) {
            $groupName.val( $selectedOption.text() );
        } else {
            $groupName.val( '' );
        }

        data = {
            groupId: val,
            claimId: this.promoIds.claimId,
            promotionId: this.promoIds.promotionId
        };

        if( $selectedOption.is( ':not(.defaultOption)' ) ) {
            this.containerView.model.setGroup( parseInt( $selectedOption.val(), 10 ) );

            this.containerView.model.loadParticipants( data, isGroup );
        }

    },

    toggleGroupValidation: function() {
        var $groupSect = this.$el.find( '.groupSection' ),
            $groupSelect = this.$el.find( '#selectGroup' ),
            $selectedOption = $groupSelect.find( 'option:selected:not(.defaultOption)' ),
            $groupName = this.$el.find( '#groupName' );

        $groupSect.slideToggle();

        if( $selectedOption ) {
            $groupName.val( $selectedOption.text() );
        }
    },

    isSingleSelectMode: function() {

        return this.selectedPromoModel.individualOrTeam === 'individual';

    },

    awardTypeSetup: function() {
        console.log( this );
        var $awardTh = this.$el.find( 'th.award' ),
            promo = this.selectedPromoModel,
            awardType = promo.awardType,
            currency = promo.currencyLabel,
            awardTypeCap = awardType.charAt( 0 ).toUpperCase() + awardType.slice( 1 ),
            pointsTxt = $awardTh.data( 'msgPoints' );
            console.log( promo );
        if( promo.recommendedAward === false ) {
            if( promo.awardType == 'pointsFixed' ) {

            } else {
                $awardTh.hide();
                return false;
            }

        } else {
            this.$el.find( '.recAwardMsg' ).show();
        }

        $awardTh.show();

        // set the text via data attrs
        $awardTh.text( $awardTh.data( 'msg' + awardTypeCap ) );

        // change some elements for various AwardType states
        if( awardType === 'none' ) {
            // hide middle columns
            $awardTh.hide();

        } else if ( awardType === 'pointsRange' ) {
            // add the range to the TH
            if( currency ) {
                $awardTh.text( $awardTh.text() + ' (' + promo.awardMin + '-' + promo.awardMax + ')' + ' ' + currency );
            } else {
                $awardTh.text( $awardTh.text() + ' (' + promo.awardMin + '-' + promo.awardMax + ') ' + pointsTxt );
            }

        } else if ( awardType === 'calculated' ) {
            this.containerView.model.getCalcSetup( this.promoIds );

            if( currency ) {
                $awardTh.text( $awardTh.text() + ' ' + currency );
            } else {
                $awardTh.text( $awardTh.text() + ' ' + pointsTxt );
            }

        } else if ( awardType === 'pointsFixed' ) {
            if( currency ) {
                $awardTh.text( $awardTh.text() + ' ' + currency );
            } else {
                $awardTh.text( $awardTh.text() + ' ' + pointsTxt );
            }

        } else if ( awardType === 'other' ) {
            $awardTh.text( $awardTh.text() );
        }

    },

    setCalcObj: function( calcObj ) {
        this.calcObj = calcObj;
    },

    changeParticipant: function( participant ) {
        var that = this;

        if( this.selectedPromoModel.maxParticipants && this.recipientView.model.length > this.selectedPromoModel.maxParticipants ) {

            this.recipientView.model.remove( participant );

            this.containerView.onNavErrorTip( 'maxParticipantsReached', this.$el.find( '.showHideBtn' ) );

            return false;
        }

        this.containerView.model.setParticipants( that.paxCollection );

    },

    // ========================
    // PARTICIPANT NODE CHANGE
    // ========================
    showRecipNodesTip: function( e ) {
        var $el = $( e.currentTarget ),
            onShow;

        e.preventDefault();

        //have it already?
        if( $el.data( 'qtip' ) ) { return; }

        //onShow func -- style selected
        onShow = function( evt, api ) {
            var $ns = api.elements.content.find( 'a' );
            // clear selected style
            $ns.removeClass( 'selected' ).find( 'i' ).remove();

            $ns.prepend( '<i class="icon icon-stop"></i>' );
            // match based on text (yes, gross, ikno)
            $ns = $ns.filter( function() {
                return $( this ).text() == $el.find( '.limitedWidth' ).text();
            } ).addClass( 'selected' );
            $ns.find( 'i' ).remove();
            $ns.prepend( '<i class="icon icon-check-square-2"></i>' );
        };

        $el.qtip( {
            content: $el.closest( '.participant' ).find( '.changeRecipNodeTip' ),
            position: { my: 'bottom center', at: 'top center', container: this },
            show: { ready: true, event: 'click', effect: false },
            hide: { event: 'unfocus', effect: false },
            style: { classes: 'ui-tooltip-shadow ui-tooltip-light', tip: { width: 10, height: 5 } },
            events: { show: onShow }
        } );

        // IE8 - two lines below are for initial position bug in IE8
        $el.qtip( 'show' );
        $el.qtip( 'reposition' );
        // EOF IE8

    },

    selectRecipNode: function( e ) {
        var $tar = $( e.target );
        e.preventDefault();

        if( $tar.hasClass( 'icon' ) ) {
            $tar = $tar.closest( 'a' );
        }

        // because of the checkboxes and IE7
        $tar.find( 'i' ).remove();

        // change the hidden input value for this recip to selected node id val/text
        this.$el.find( '#' + $tar.data( 'inputId' ) ).val( $tar.data( 'nodeId' ) );
        this.$el.find( '#' + $tar.data( 'dispId' ) + ' .limitedWidth' ).text( $tar.text() );

        // hide the qtip containing the clicked element
        $tar.closest( '.qtip' ).qtip( 'hide' );

    },

    // ===================
    // AWARD INPUTS
    // ===================
    doPointsFocus: function( e ) {
        var $tar = $( e.currentTarget );

        if( $tar.val() === '0' || $tar.val() === 0 ) {
            setTimeout( function() {
                if ( $tar[ 0 ].setSelectionRange ) {
                    $tar[ 0 ].setSelectionRange( 0, 4000 );
                }
                else{
                    $tar.select();
                }
            }, 0 );
        }

    },

    doPointsKeyup: function( e ) {

        this.setAwardPointsFor( e, true );
        //all inputs sync for range
        // this.setAllAwardRanges($tar.val());

    },
    //
    // setAllAwardRanges: function(val){
    //     var that = this;
    //
    //     this.$el.find('input.awardPointsInp').each(function(){
    //         var $this = $(this);
    //
    //         if($this.val() !== val) {
    //             $this.val(val);
    //             // alters the value in the model
    //             that.setAwardPointsFor($this);
    //         }
    //     });
    //
    // },

    // sync model to input element and validate
    setAwardPointsFor: function( e ) {
        var self = this;
        console.log( self.paxCollection );
        console.log( self.recipientSearchView );
        console.log( self.recipientView );
        var $el = $( e.target ),
            paxId = $el.parents( 'tr' ).data( 'participant-id' ),
            paxes = this.paxCollection,
            pax = _.where( paxes.models, { 'id': paxId } );
            //  paxes.find(function(model){return model.get('id') === paxId});
            console.log( paxId );
            console.log( paxes );
            console.log( this );
        console.log( pax );
        // validate range
        this.validateRangeOf( $el );
        // set value on model
        pax[ 0 ].set( 'awardQuantity', $el.val(), { silent: true } );

    },

    validateRangeOf: function( $el ) {
        var castNum = function( x ) { // convert strings to nums
                return typeof x === 'string' ? parseInt( x, 10 ) : x;
            },
            val = castNum( $el.val() || 0 ), // if '' then put a zero in there
            rMin = castNum( this.selectedPromoModel.awardMin ),
            rMax = castNum( this.selectedPromoModel.awardMax );
            //Check to see if user opted out of awards, if so, do no validation
            if( $el.hasClass( 'optOut' ) ) {
                return true;
            } else {
                // OUT of RANGE ERROR
                if( val < rMin || val > rMax ) {
                    // disp error
                    if( $el.val() == '' ) {
                        this.containerView.onNavErrorTip( 'fillAward', $el );
                    }
                    else {
                        this.containerView.onNavErrorTip( 'awardOutOfRange', $el );
                    }
                    return false;
                }
            }

        this.containerView.onNavErrorTip( '', $el, true );
        return true;
    },

    doPointsKeydown: function( e ) {
        var $tar = $( e.target ),
            $nxtInp = $tar.closest( '.participant-item' )
                .next( '.participant-item' ).find( '.awardPointsInp' ),
            that = this;

            console.log( that );
        // filter non-numerics
        if( that.selectedPromoModel.currencyLabel ) {
            that.filterNonNumericKeydown( e );
        } else {
            that.filterNonNumericKeydownNoCash( e );
        }

        // if enter||tab press, focus on next input
        if( e.which === 13 || e.which === 9 ) {
            e.preventDefault(); // stop it
            $nxtInp.length ? $nxtInp.focus().select() : false; // move to next inp
        }

    },

    // helper, filter non-numeric keydown
    filterNonNumericKeydown: function( event ) {
        // http://stackoverflow.com/questions/995183/how-to-allow-only-numeric-0-9-in-html-inputbox-using-jquery
        // Allow: backspace, delete, tab, escape, and enter
        if ( event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 27 || event.keyCode == 13 || event.keyCode == 110 || event.keyCode == 190 ||
             // Allow: Ctrl+A
            ( event.keyCode == 65 && event.ctrlKey === true ) ||
             // Allow: home, end, left, right
            ( event.keyCode >= 35 && event.keyCode <= 39 ) ) {
                 // let it happen, don't do anything
                 return ;
        } else {
            // Ensure that it is a number and stop the keypress
            if ( event.shiftKey || ( event.keyCode < 48 || event.keyCode > 57 ) && ( event.keyCode < 96 || event.keyCode > 105 ) ) {
                event.preventDefault();
            }
        }
    },
    filterNonNumericKeydownNoCash: function( event ) {
        // http://stackoverflow.com/questions/995183/how-to-allow-only-numeric-0-9-in-html-inputbox-using-jquery
        // Allow: backspace, delete, tab, escape, and enter
        if ( event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 27 || event.keyCode == 13 ||
             // Allow: Ctrl+A
            ( event.keyCode == 65 && event.ctrlKey === true ) ||
             // Allow: home, end, left, right
            ( event.keyCode >= 35 && event.keyCode <= 39 ) ) {
                 // let it happen, don't do anything
                 return ;
        } else {
            // Ensure that it is a number and stop the keypress
            if ( event.shiftKey || ( event.keyCode < 48 || event.keyCode > 57 ) && ( event.keyCode < 96 || event.keyCode > 105 ) ) {
                event.preventDefault();
            }
        }
    },
    decimalCheck: function( e ) {
        var self = this,
            amount = $( e.target ).val();

        if( self.selectedPromoModel.currencyLabel ) {
            amount = parseFloat( amount ).toFixed( 2 );
            $( e.target ).val( amount );
        }

    },
    showOptOutTip: function( e ) {

    var $tar = $( e.target );
    var cont = $tar.data( 'msg' );
    $tar.qtip( {
        content: {
            text: cont
        },
        position: {
            my: 'top left',
            at: 'bottom left',
            adjust: {
                // x: getQtipOffset(),
                // y: 0
                method: 'shift none' // shift x-axis inside viewport
            },
            container: $( 'body' ),
            //Used to fix qtip position in in ie7&8.
            effect: false,
            viewport: $( 'body' )// $('body'), // shift will try to keep tip inside this
        },
        show: {
            ready: true,
            event: false
        },
        hide: {
            event: false,
            fixed: true,
            delay: 200
        },
        style: {
            padding: 0,
            classes: 'ui-tooltip-shadow ui-tooltip-light',
            tip: {
                corner: true,
                width: 20,
                height: 10
            }
        }
    } );
    },
    hideOptOutTip: function ( e ) {

        $( e.target ).qtip( 'destroy' );
    },
    // ===================
    // AWARD CALCULATOR
    // ===================
    doViewRecognitionCalculator: function( event ) {
        var that = this,
            tplName = 'nominationsCalcTemplate',
            theWeightLabel,
            theScoreLabel,
            thisQtip,
            $anchor = $( event.target );


        var getCriteriaElements = function() {
            var theCriteriaElement = '',
                i = 0,
                j = 0;

            for ( i = 0; i < that.calcObj.criteria.length; i++ ) {
                var thisCriteriaRow = that.calcObj.criteria[ i ];
                var thisRowLabel = thisCriteriaRow.label;
                var thisRowId = thisCriteriaRow.id;
                var theCriteriaWrapper = "<div class='nominationsCalcCriteriaWrapper' data-criteria-id='" + thisRowId + "'>";
                var thisRatingSelectInner = [];
                // filling the text of first option later from a TPL element (should do the whole select element in handlebars, but this is calc and its not worth the effort ATM)
                var thisRatingSelectOutter = "<select class='nominationsCalcRatingSelect'><option value='undefined' class='msgSelRatingTarget'>[dynamic js]</option>";

                for ( j = 0; j < thisCriteriaRow.ratings.length; j++ ) {
                    var thisRatingOption = thisCriteriaRow.ratings[ j ];

                    thisRatingSelectInner.push( "<option value='" + thisRatingOption.value + "' data-ratingId='" + thisRatingOption.id + "'>" + thisRatingOption.label + '</option>' );
                }

                //close up the select element
                thisRatingSelectOutter += thisRatingSelectInner + '</select>';

                theCriteriaElement += theCriteriaWrapper + '<label>' + thisRowLabel + '</label>' + thisRatingSelectOutter + "<span id='criteriaWeightElm' class='criteriaWeightElm'></span><span id='criteriaScoreElm' class='criteriaScoreElm'></span></div>";
            }

            return theCriteriaElement;
        };

        var bindTheSelects = function( theQtip ) {
            var $theSelects = theQtip.find( '.nominationsCalcRatingSelect' );

            $theSelects.change( function() {
                if ( selectsAllReady( $theSelects ) ) {
                    that.doSendCalculatorRatings( $theSelects, theQtip );
                }
            } );
        };

        var selectsAllReady = function( $elms ) {
            var isReady = true;

            $elms.each( function() {
                var $this = $( this );

                if ( $this.val() === 'undefined' || $this.val() === undefined ) {
                    isReady = false;
                }
            } );

            return isReady;
        };

        var bindCloseButton = function( theQtip ) {
            var $theCloseBtn = theQtip.find( '#nominationCalcCloseBtn' );

            $theCloseBtn.click( function() {
               theQtip.qtip( 'hide' );

            } );
        };

        var bindPayoutLink = function( $this ) {
            $this.find( '#nominationsCalcPayoutTableLink' ).off().click( function( event ) {
                event.preventDefault ? event.preventDefault() : event.returnValue = false;
                that.showRecogCalcPayout( this );
            } );
        };

        if ( this.calcObj.attributes.hasWeight ) {
            theWeightLabel = this.calcObj.attributes.weightLabel;
        }else{
            theWeightLabel = '';
        }

        if ( this.calcObj.attributes.hasScore ) {
            theScoreLabel =  this.calcObj.attributes.scoreLabel;
        }else{
            theScoreLabel = '';
        }

        var renderedTemplate = '';

        $anchor.qtip( {
            content: {
                text: ''
            },
            position: {
                my: 'top center',
                at: 'bottom center',
                adjust: {
                    // x: getQtipOffset(),
                    // y: 0
                    method: 'shift none' // shift x-axis inside viewport
                },
                container: $( 'body' ),
                //Used to fix qtip position in in ie7&8.
                effect: false,
                viewport: $( 'body' )// $('body'), // shift will try to keep tip inside this
            },
            show: {
                ready: false,
                event: false
            },
            hide: {
                event: false,
                fixed: true,
                delay: 200
            },
            style: {
                padding: 0,
                classes: 'ui-tooltip-shadow ui-tooltip-light nominationsCalcTipWrapper',
                tip: {
                    corner: true,
                    width: 20,
                    height: 10
                }
            },
            events: {
                show: function( event, api ) {
                    var $this = $( this );
                    bindTheSelects( $this );
                    bindCloseButton( $this );
                    thisQtip = $this;
                    $( '.nominationsCalcTipWrapper' ).not( thisQtip ).hide(); //hide any other open calculators
                    $( '.nominationsCalcPayoutTableLink' ).not( $this.find( '#nominationsCalcPayoutTableLink' ) ).qtip( 'hide' );
                    that.trigger( 'nominationsCalcShowing' );
                },
                visible: function() {
                    var $this = $( this ),
                        scrollOffset = -( $( window ).height() / 2 ) + ( $this.height() / 2 );
                    bindPayoutLink( $this );
                    $this.find( '#nominationsCalcPayoutTableLink' ).click(); //open the payout link
                    $.scrollTo( $this, 500, { offset: { top: scrollOffset, left: 0 } } );
                },
                hide: function() {
                    $( this ).find( '#nominationsCalcPayoutTableLink' ).qtip( 'hide' );
                }
            }
        } );

        event.preventDefault ? event.preventDefault() : event.returnValue = false;

        TemplateManager.get( tplName, function( tpl ) {
                var data = {
                    weightLabel: theWeightLabel,
                    scoreLabel: theScoreLabel,
                    criteriaDiv: getCriteriaElements(),
                    hasWeight: that.calcObj.attributes.hasWeight,
                    hasScore: that.calcObj.attributes.hasScore,
                    showPayTable: that.calcObj.attributes.showPayTable
                };
                renderedTemplate = $( tpl( data ) );
                renderedTemplate.find( '.msgSelRatingTarget' ).html( renderedTemplate.find( '.msgSelectRatingInstruction' ).html() );

                $anchor.qtip( 'option', 'content.text', renderedTemplate );
                $anchor.qtip( 'show' );
                that.doRenderPreviousCalcInfo( $anchor.closest( 'tr' ), thisQtip );
            }, this.tplPath );
    },

    doSendCalculatorRatings: function( $theSelects, theQtip ) {
        var thisScope = this;

        var getCriteriaInfo = function() {
            var theObj = {};

            $theSelects.each( function( i ) {
                var $this = $( this );
                var thisRatingValue = parseInt( $this.val(), 10 );
                var thisCriteriaId = parseInt( $this.parent().attr( 'data-criteria-id' ), 10 );

                theObj[ 'criteria[' + i + '].criteriaRating' ] = thisRatingValue;
                theObj[ 'criteria[' + i + '].criteriaId' ] = thisCriteriaId;

            } );

            return theObj;
        };

        var theData = {
            promotionId: this.promoIds.promotionId,
            participantId: $( $( '.nominationsCalcTipWrapper' ).qtip( 'api' ).options.position.target ).closest( 'tr' ).attr( 'data-participant-id' )
        };

        theData = $.extend( {}, theData, getCriteriaInfo() );

        var dataSent = $.ajax( {
                type: 'POST',
                dataType: 'g5json',
                url: G5.props.URL_JSON_NOMINATIONS_CALCULATOR_SEND_INFO,
                 data: theData,
                 beforeSend: function() {
                    console.log( '[INFO] NominationCollectionView: doSendCalculatorRatings ajax post starting. Sending this: ', theData );
                 },
                success: function( serverResp ) {
                    console.log( '[INFO] NominationCollectionView: doSendCalculatorRatings ajax post sucess: ', serverResp );
                    thisScope.doUpdateRecogCalcValues( serverResp.data, theQtip, false );
                }
            } );

            dataSent.fail( function( jqXHR, textStatus ) {
                console.log( '[INFO] NominationCollectionView: doSendCalculatorRatings ajax post failed: ' + textStatus, jqXHR );
            } );
    },

    doUpdateRecogCalcValues: function( data, theQtip, hasPreviousData ) {
        var that = this,
            $theCalcElm = theQtip.find( '#nominationsCalcInnerWrapper' ),
            scorElmTplName = 'nominationsCalcScoreWrapperTpl',
            scorElmTplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'nominations/tpl/',
            $theCalc = theQtip.find( '#nominationsCalcScoreWrapper' ),
            rangedElm = '',
            theFixedAmount = '',
            theAwardLevel,
            savedData = data,

            hasRangedAward = function() {
                var hasRanged = false;

                if ( that.calcObj.attributes.awardType === 'range' ) {
                    hasRanged = true;
                    rangedElm = '<span>(' + data.awardRange.min + ' - ' + data.awardRange.max + ")</span><input type='text' data-range-max='" + data.awardRange.max + "' data-range-min='" + data.awardRange.min + "'>";
                }

                return hasRanged;
            },

            hasFixedAward = function() {
                var hasFixed = false;

                if ( that.calcObj.attributes.awardType === 'fixed' ) {
                    theFixedAmount = data.fixedAward;
                    hasFixed = true;
                }

                return hasFixed;
            },

            hasAwardLevel = function() {
                var hasAwardLevel = false;

                if ( that.calcObj.attributes.awardType === 'merchlevel' ) {
                    theAwardLevel = data.awardLevel.name + ' (' + data.awardLevel.value + ')' + "<input type='hidden' id='calcAwardLevelId' value='" + data.awardLevel.id + "'>";
                    hasAwardLevel = true;
                }

                return hasAwardLevel;
            },

            is_int = function( value ) {
                if( ( parseFloat( value ) == parseInt( value, 10 ) ) && !isNaN( value ) ) {
                    return true;
                } else {
                    return false;
                }
            },

            bindAwardInput = function() {
                var $theInput = theQtip.find( 'input' ),
                    $saveButton = theQtip.find( '#nominationsCalcScoreWrapper' ).find( 'button' ),
                    $rangeText = $theInput.closest( 'p' ),
                    maxAward = parseInt( $theInput.attr( 'data-range-max' ), 10 ),
                    minAward = parseInt( $theInput.attr( 'data-range-min' ), 10 );

                if ( hasRangedAward() ) {
                    $theInput.keyup( function() {
                        if ( $theInput.val() > maxAward || $theInput.val() < minAward || !is_int( $theInput.val() ) ) {
                            $theInput.addClass( 'input-error' );
                            $saveButton.attr( 'disabled', 'disabled' );
                            $rangeText.addClass( 'range-error' );
                        }else if ( $theInput.hasClass( 'input-error' ) ) {
                            $theInput.removeClass( 'input-error' );
                            $saveButton.removeAttr( 'disabled' );
                            $rangeText.removeClass( 'range-error' );
                        }else if ( $saveButton.attr( 'disabled' ) === 'disabled' ) {
                            $saveButton.removeAttr( 'disabled' );
                        }
                    } );
                }else{
                    $saveButton.removeAttr( 'disabled' );
                }
            },

            bindSaveButton = function() {
                var $theButton = theQtip.find( '#nominationsCalcScoreWrapper' ).find( 'button' );

                $theButton.off();

                $theButton.click( function() {
                    if ( allCriteriaSelected() ) {
                        that.doSaveCalculatedAward( savedData, theQtip );
                    }
                } );
            },

            renderPreviousRatingSeletions = function() {
                var thisCriteria,
                i = 0;
                for ( i = 0; i < data.criteria.length; i++ ) {
                    thisCriteria = data.criteria[ i ];

                    $( ".nominationsCalcCriteriaWrapper[data-criteria-id='" + thisCriteria.criteriaId + "']" ).find( 'select' ).val( thisCriteria.criteriaRating );
                }
            },

            allCriteriaSelected = function() {
                var allSelected = true,
                    $criteriaSelects = theQtip.find( 'select' );

                $criteriaSelects.each( function() {
                    var $theSelect = $( this );

                    if ( $theSelect.val() === 'undefined' ) {
                        allSelected = false;
                        $theSelect.addClass( 'input-error' );
                    }else{
                        $theSelect.removeClass( 'input-error' );
                    }
                } );

                return allSelected;
            },

            theData = {
                isRange: hasRangedAward(),
                awardRange: rangedElm,
                totalScore: data.totalScore,
                hasFixed: hasFixedAward(),
                hasAwardLevel: hasAwardLevel(),
                awardLevel: theAwardLevel,
                fixedAmount: theFixedAmount
            };


        for ( var i = data.criteria.length - 1; i >= 0; i-- ) {
            var theCriteria = data.criteria[ i ],
                $theRow = $theCalcElm.find( "div[data-criteria-id='" + theCriteria.criteriaId + "']" );

            if ( that.calcObj.attributes.hasWeight ) {
                $theRow.find( '#criteriaWeightElm' ).html( theCriteria.criteriaWeight );
            }

            if ( that.calcObj.attributes.hasScore ) {
                $theRow.find( '#criteriaScoreElm' ).html( theCriteria.criteriaScore );
            }
        }

        var startTxt, selectAmtText;

        //check to see if score template is there. If not, make it and populate it. If it is, just populate.
        if ( !$theCalc.hasClass( 'nominationsCalcScoreWrapper' ) ) {
            TemplateManager.get( scorElmTplName, function( tpl ) {
                theQtip.find( '#nominationsCalcWrapper' ).append( tpl( theData ) );
                bindAwardInput();
                bindSaveButton();

                if ( hasPreviousData ) {
                    renderPreviousRatingSeletions();
                }
            }, scorElmTplUrl );
        }else{
            if ( theData.isRange ) {
                startTxt = $theCalc.find( 'p' ).text();
                selectAmtText = startTxt.substring( 0, startTxt.indexOf( '(' ) );
                $theCalc.find( 'p' ).html( selectAmtText + ' ' + theData.awardRange ); //update the range award
                bindAwardInput();
                bindSaveButton();
            }else if( theData.hasFixed ) {
                startTxt = $theCalc.find( 'p' ).text();
                selectAmtText = startTxt.substring( 0, startTxt.indexOf( ':' ) );
                bindSaveButton();
                $theCalc.find( 'p' ).html( selectAmtText + ': ' + theData.fixedAmount ); //update the fixed award
            }else{
                //must be merch level
                startTxt = $theCalc.find( 'p' ).text();
                selectAmtText = startTxt.substring( 0, startTxt.indexOf( ':' ) );
                bindSaveButton();
                $theCalc.find( 'p' ).html( selectAmtText + ': ' + data.awardLevel.name + ' (' + data.awardLevel.value + ')' ); //update the fixed award
            }

            $theCalc.find( '#nominationsCalcTotal' ).html( data.totalScore ); //update total score

        }

    },

    doSaveCalculatedAward: function( data, theQtip ) {
        var theCalcAwardObj = data,
            $anchor = $( theQtip.qtip( 'api' ).options.position.target ),
            $thisRow = $anchor.closest( 'tr' ),
            $rowSiblings = $thisRow.siblings( 'tr.participant-item' );

        if ( this.calcObj.attributes.awardType === 'range' ) {
            theCalcAwardObj.awardedAmount = theQtip.find( 'input' ).val();
        }else if ( this.calcObj.attributes.awardType === 'fixed' ) {
            theCalcAwardObj.awardedAmount = data.fixedAward;
        }else{
            theCalcAwardObj.awardedAmount = theQtip.find( '#calcAwardLevelId' ).val();
        }

        this.savedCalcObj = theCalcAwardObj;

        console.log( '[INFO] NominationCollectionView: doSaveCalculatedAward: saved this calcInfo: ', this.savedCalcObj );

        //if the calc obj is not a merchlevel award, do the normal save
        if ( this.calcObj.attributes.awardType !== 'merchlevel' ) {
            $anchor.siblings( 'input' ).val( theCalcAwardObj.awardedAmount );
            $anchor.text( theCalcAwardObj.awardedAmount );
        }else{
            //if it is a merchlevel, change the name of the input and display the award level name
            var thisInputName = $thisRow.find( "[name*='.userId']" ).attr( 'name' );
            var thisRatingIdName = thisInputName.substr( 0, thisInputName.indexOf( '.userId' ) );
            if ( thisRatingIdName === '' || thisRatingIdName === undefined ) {
                thisRatingIdName = thisInputName.substr( 0, thisInputName.indexOf( '.awardLevel' ) ); //in case we don't need to change the name
            }
            $anchor.siblings( 'input' ).not( "[name*='calculatorResultBeans']" ).attr( 'name', thisRatingIdName + '.awardLevel' ).val( theCalcAwardObj.awardedAmount );
            $anchor.text( theCalcAwardObj.awardLevel.name + ' (' + theCalcAwardObj.awardLevel.value + ')' );
            $anchor.append( "<input type='hidden' name='" + thisRatingIdName + ".awardLevelName' value='" + theCalcAwardObj.awardLevel.name + "'>", "<input type='hidden' name='" + thisRatingIdName + ".awardLevelValue' value='" + theCalcAwardObj.awardLevel.value + "'>", "<input type='hidden' name='" + thisRatingIdName + ".awardLevelId' value='" + theCalcAwardObj.awardLevel.id + "'>" );
        }

        if ( this.calcObj.attributes.awardType !== 'merchlevel' )
        {
            // $thisRow.find(".calcDeduction").text();
        }else
        {
            // $thisRow.find(".calcDeduction").text();
        }

        //not a shared data-set. Add it's personal row info to global var
        $thisRow.attr( 'data-shared-calc-info', 'false' );
        this.savedCalcRows[ $thisRow.attr( 'data-participant-cid' ) ] = data;

        this.setAwardPointsForCalc( $anchor, theCalcAwardObj );

        theQtip.qtip( 'hide' );

        if ( $rowSiblings.length > 0 ) {
            this.doShowSameForAllCalc( $anchor, theCalcAwardObj );
        }

    },

    setAwardPointsForCalc: function( $el, calcData ) {
        var pax = this.getParticipantByDomEl( $el );
        // var $paxPar = $el.parent();

        // set value on model
        if ( this.calcObj.attributes.awardType !== 'merchlevel' ) {
            pax.set( 'awardQuantity', parseInt( $el.siblings( 'input' ).val(), 10 ) );
        }else{
            // Must be a merch level
            pax.set( 'awardQuantity', calcData.awardLevel.value );
        }

        // var calcAttrNameRaw = calcData.awardLevel.name;
        // var calcAttrName = calcData.awardLevel.name + ".calculatorResultBeans";
        // $paxPar.find("[name*='calculatorResultBeans']").remove(); //clear out any previous calc inputs

        // for (var i = 0; i < calcData.criteria.length; i++) {
        //     var thisCriteria = calcData.criteria[i];

        //     $paxPar.append("<input type='hidden' name='" + calcAttrName + "[" + i + "].criteriaId' value='" + thisCriteria.criteriaId + " '>" + "<input type='hidden' name='" + calcAttrName + "[" + i + "].ratingId' value='" + thisCriteria.criteriaRatingId  + " '>" + "<input type='hidden' name='" + calcAttrName + "[" + i + "].criteriaRating' value='" + parseInt(thisCriteria.criteriaRating, 10) + "'>");
        // }

        // //clear out other data on the form
        // $paxPar.siblings(".participant").find("input[name*='calculatorResultBeans']").remove();

    },

    doShowSameForAllCalc: function( $anchor, theCalcAwardObj ) {
        var that = this;
        $anchor.qtip( {
            content: this.$el.find( '#sameForAllTipTpl' ).clone().removeAttr( 'id' ),
            position: {
                my: 'left center', at: 'right center', adjust: { x: 5, y: 0 }, container: this.$el, effect: false, viewport: false
            },
            show: { ready: true, event: false },
            hide: 'unfocus',
            //only show the qtip once
            events: {
                hide: function( evt, api ) {$anchor.qtip( 'destroy' );},
                show: function() {
                    var $theDoSameLink = $( '.sameForAllTip' );
                    $theDoSameLink.click( function( event ) {
                        event.preventDefault ? event.preventDefault() : event.returnValue = false;
                        that.doSameForAllCalc( $anchor, $theDoSameLink, theCalcAwardObj );
                    } );
                }
            },
            style: { classes: 'ui-tooltip-shadow ui-tooltip-light' }
        } );

        $anchor.qtip( 'show' );
    },

    doSameForAllCalc: function( $anchor, $theToolTip, theCalcAwardObj ) {
        var that = this;
        this.didSameForAll = true;

        var $original = $anchor.closest( 'tr' ),
            $theRows = $( '.participant-item' ).not( $original );

        if ( $theRows.length > 0 ) {

            $theRows.each( function() {
                var $this = $( this ),
                    $theInput = $this.find( "[name*='awardQuantity']" ),
                    $thisAnchor = $this.find( '.calcLink' );

                var thisInputName = $thisAnchor.siblings( 'input' ).attr( 'name' );
                var thisRatingIdName = thisInputName.substr( 0, thisInputName.indexOf( '.awardQuantity' ) );

                $original.attr( 'data-shared-calc-info', 'true' );

                $this.attr( 'data-shared-calc-info', 'true' );
                $theInput.val( that.savedCalcObj.awardedAmount );

                if ( that.calcObj.attributes.awardType !== 'merchlevel' ) {
                    $theInput.siblings( '.calcLink' ).text( that.savedCalcObj.awardedAmount );
                }else{
                    $theInput.siblings( '.calcLink' ).text( that.savedCalcObj.awardLevel.name + ' (' + that.savedCalcObj.awardLevel.value + ')' );
                }

                if ( that.calcObj.attributes.awardType !== 'merchlevel' ) {
                    // $this.find(".calcDeduction").text();
                    that.setAwardPointsForCalc( $this.find( '.calcLink' ), theCalcAwardObj );
                }else{
                    that.setAwardPointsForCalc( $this.find( '.calcLink' ), theCalcAwardObj );
                    // $this.find(".calcDeduction").text();

                    if ( thisRatingIdName === '' || thisRatingIdName === undefined ) {
                        thisRatingIdName = thisInputName.substr( 0, thisInputName.indexOf( '.awardLevel' ) ); //in case we don't need to change the name
                    }
                    $thisAnchor.siblings( 'input' ).not( "[name*='calculatorResultBeans']" ).attr( 'name', thisRatingIdName + '.awardLevel' ).val( theCalcAwardObj.awardedAmount );
                    $thisAnchor.text( theCalcAwardObj.awardLevel.name + ' (' + theCalcAwardObj.awardLevel.value + ')' );
                    $thisAnchor.append( "<input type='hidden' name='" + thisRatingIdName + ".awardLevelName' value='" + theCalcAwardObj.awardLevel.name + "'>", "<input type='hidden' name='" + thisRatingIdName + ".awardLevelValue' value='" + theCalcAwardObj.awardLevel.value + "'>" );
                }



                $theToolTip.parent( '.ui-tooltip-content' ).parent().qtip( 'hide' );
            } );

        }else{
            $theToolTip.parent( '.ui-tooltip-content' ).parent().qtip( 'hide' );
        }
    },

    doRenderPreviousCalcInfo: function( $theRow, theQtip ) {
        if ( $theRow.attr( 'data-shared-calc-info' ) === 'true' ) {
            this.doUpdateRecogCalcValues( this.savedCalcObj, theQtip, true );
        }else if ( this.savedCalcRows[ $theRow.attr( 'data-participant-cid' ) ] ) {
            this.doUpdateRecogCalcValues( this.savedCalcRows[ $theRow.attr( 'data-participant-cid' ) ], theQtip, true );
        }else if( $( '#dataForm' ).find( "[name*='].id']" ).filter( "[value='" + $theRow.attr( 'data-participant-id' ) + "']" ).val() !== undefined ) {
            this.doRenderRecogCalcValuesOld( $( '#dataForm' ).find( "[name*='].id']" ), theQtip );
        }
    },

    doRenderRecogCalcValuesOld: function( recipIdInput, theQtip ) {

        var theRecipNameRaw = recipIdInput.attr( 'name' );
        var theRecipCalcNameRaw = theRecipNameRaw.substr( 0, theRecipNameRaw.indexOf( '.id' ) ) + '.calculatorResultBeans';
        var finalSelect;


        $( '#dataForm' ).find( "[name*='" + theRecipCalcNameRaw + "']" ).filter( "[name*='criteriaId']" ).each( function() {
            var $this = $( this ),
                thisCriteriaId = $this.val(),
                thisInputName = $this.attr( 'name' ),
                thisRatingIdName = thisInputName.substr( 0, thisInputName.indexOf( '.criteriaId' ) ),
                thisRatingId = $( "[name='" + thisRatingIdName + ".ratingId']" ).val(),
                theRatingSelectPar = theQtip.find( '.recCalcCriteriaWrapper' ).filter( "[data-criteria-id='" + thisCriteriaId + "']" ),
                theRatingSelect = theRatingSelectPar.find( 'select' ),
                theRatingOption = $( theRatingSelect ).find( "[data-ratingid='" + thisRatingId + "']" );
            theRatingSelect.val( theRatingOption.val() );
            finalSelect = theRatingSelect;
        } );

        if ( finalSelect ) {
            finalSelect.change();
        }

    },

    showRecogCalcPayout: function( anchor ) {
        var that = this,
            $theAnchor = $( anchor ),
            tplName = 'nominationsCalcPayoutGridTpl',
            finishedTemplate = '',
            payTableLength = that.calcObj.payTable.rows.length,
            i = 0,
            thisPayoutRow,

            populateGrid = function( $theQtip ) {
                $theQtip.find( 'td' ).remove(); //empty it out.

                for ( i = 0; i < payTableLength; i++ ) {
                    thisPayoutRow = that.calcObj.payTable.rows[ i ];

                    $theQtip.find( 'tbody' ).append( '<tr><td>' + thisPayoutRow.score + '</td><td>' + thisPayoutRow.payout + '</td></tr>' );
                }

            },

            bindCloseButton = function( $this ) {
                var $theButton = $this.find( '#nominationsCalcPayoutCloseBtn' );
                $theButton.click( function() {
                    $this.qtip( 'hide' );
                } );
            };

        $theAnchor.qtip( {
            content: {
                text: 'Cannot find content'
            },
            position: {
                my: 'top left',
                at: 'bottom left',
                container: $( 'body' )
            },
            show: {
                ready: false,
                event: false
            },
            hide: {
                event: false,
                fixed: true,
                delay: 200
            },
            style: {
                padding: 0,
                classes: 'ui-tooltip-shadow ui-tooltip-light nominationsCalcPayoutTableWrapper',
                tip: {
                    corner: true,
                    width: 20,
                    height: 10
                }
            },
            events: {
                visible: function( event, api ) {
                    var $this = $( this );
                    populateGrid( $this );
                    bindCloseButton( $this );
                }
            }
        } );

        TemplateManager.get( tplName, function( tpl ) {
            finishedTemplate = tpl().toString();
            $theAnchor.qtip( 'option', 'content.text', finishedTemplate );
            $theAnchor.qtip( 'show' );
        }, this.tplPath );

    },

    checkForSavedCalc: function() {
        var that = this,
            hasCalcData = false,
        // get ready for this -- http://www.youtube.com/watch?v=0P9HCPAEc48
            calcData = function() {
                if ( !that.calcData && !that.didCalc ) { //if there isn't already calc data, create it
                    var arr = function() {
                        var theArr = {};
                        _.each( that.paxCollection.models, function( collection, collIndex ) {
                            var innerArr = {};
                            _.each( collection.attributes, function( attribute, attrIndex ) {
                                if ( attrIndex.indexOf( 'calculatorResultBeans' ) > -1 && attribute !== '' ) {
                                    innerArr[ attrIndex ] = attribute;
                                    hasCalcData = true;
                                }else if( attrIndex.indexOf( 'awardLevel' ) > -1 && attribute !== '' ) {
                                    innerArr[ attrIndex ] = attribute;
                                }
                            } );
                            innerArr.id = collection.attributes.id;
                            theArr[ collIndex ] = innerArr;
                        } );
                        return theArr;
                    }(); //self calling
                    that.didCalc = true; //make sure we don't calculate or populate this again
                    return arr;
                }
            }(); //also self calling

        that.calcData = calcData;


        if ( calcData && hasCalcData ) {
            _.each( calcData, function( recipObj, index ) {
                _.each( recipObj, function( recip, recipName ) {
                    if ( recip !== recipObj.id && recip !== '' ) {
                        $( "[name*='.userId']" ).filter( "[value='" + recipObj.id + "']" ).parent().append( "<input type='hidden' name='claimRecipientFormBeans[" + index + '].' + recipName + "' value='" + recip + "'>" );
                    }

                    if ( recipName.indexOf( 'awardLevelValue' ) > -1 && recip !== '' && recip !== undefined ) {
                        var $el = $( "[name*='.userId']" ).filter( "[value='" + recipObj.id + "']" ),
                            pax = that.getParticipantByDomEl( $el ),
                            $parItem = $el.closest( '.participant-item' );

                        pax.set( 'awardQuantity', parseInt( recip, 10 ) );
                        $parItem.find( '.calcLink' ).text( recip );
                        $parItem.find( "[name*='.awardQuantity']" ).remove();
                    }
                } );
            } );
        }
    },

        // Hackey function below:
    // CASE: edit - server sends calculatorResultsBeans[n] in #dataForm which need to be in the #sendForm
    renderCalcInputsForPaxHack: function( json, $pi ) {
        var $tarEl = $pi.find( '.award' );

        // look @ each json field, if it is calc res bean then dump into apropo element as (hidden) INPUT
        // this imitates data dumped in same place when user selects ratings etc. from qtip+ajax json calls
        //
        // extra hacky: the original .append in the .each looked like this: $tarEl.append('<input type="hidden" name="'+k+'" value="'+v+'" >');
        // I added the json._paxName and [json.autoIndex] because that seemed to be necessary to fix the issue with these values not filling in when the user hits "Edit" from the preview page
        _.each( json, function( v, k ) {
            if( k.match( /calculatorResultBeans\[/ ) ) {
                $tarEl.append( '<input type="hidden" name="' + json._paxName + '[' + json.autoIndex + '].' + k + '" value="' + v + '" >' );
            }
        } );
    },

    // ===================
    // UTILITY FUNCTIONS
    // ===================
    getParticipantByDomEl: function( $el ) {
        var $pax = $el.closest( '.participant-item[data-participant-cid]' ),
            cid = $pax.data( 'participantCid' );
            console.log( $el );
            console.log( $pax );
            console.log( cid );
            console.log( this.paxCollection );
        return this.paxCollection.getByCid(cid);
    },

    // // sync the visual elements with the model
    // updateTab: function() {
    //
    //     this.initTeamSections();
    //
    // },

    updateTabName: function( tab ) {
        var $fromTab = this.containerView.$el.find( ".wtTabVert[data-tab-name='" + tab + "']" ),
            $fromTabDisplay = $fromTab.find( '.wtvDisplay' ),
            teamName = this.selectedPromoModel.teamName || this.selectedPromoModel.recipientName,
            firstName = '',
            lastName = '';

        $fromTab.find( '.wtvTabName' ).show();

		if ( ( this.selectedPromoModel.nominatingType === 'both' ) &&  ( this.paxCollection && this.paxCollection.models.length == 1 ) ) {
			firstName = this.paxCollection.models[ 0 ].get( 'firstName' );
            lastName = this.paxCollection.models[ 0 ].get( 'lastName' );
			$fromTabDisplay.text( firstName + ' ' + lastName );
			$fromTab.find( '.wtvTabName' ).hide();
		}

        else if( !this.isSingleSelectMode() ) {
            $fromTabDisplay.text( teamName );
            $fromTab.find( '.wtvTabName' ).hide();

            //return;
        }
        // debugger;
        else if( this.paxCollection && this.paxCollection.models.length == 1 ) {
            firstName = this.paxCollection.models[ 0 ].get( 'firstName' );
            lastName = this.paxCollection.models[ 0 ].get( 'lastName' );

            $fromTabDisplay.text( firstName + ' ' + lastName );
            $fromTab.find( '.wtvTabName' ).hide();
        }

    },

    save: function( fromName, toName, ids,  isDraft ) {
        this.containerView.model.setParticipants( this.paxCollection );

        this.containerView.model.save( fromName, toName, ids,  isDraft );
    },

    saveAsDraft: function( fromName, toName, ids,  isDraft ) {
    	this.containerView.model.setParticipants( this.paxCollection );

    	this.containerView.model.save( fromName, toName, ids,  isDraft );
    },

    // validate the state of elements within this tab
    validate: function() {
        var $validate = this.$el.find( '.validateme:visible' ),
            isValid = G5.util.formValidate( $validate ),
            awardValid = true,
            groupValid = true,
            that = this;


            _.each( this.paxCollection.models, function( pax, index ) {
                if( pax.get( 'invalid' ) === true ) {
                    that.paxCollection.remove( pax );
                }
            } );
        if( this.paxCollection.models.length === 0 ) {
            return { msgClass: 'msgNoParticipants', valid: false };
        }

        if( this.isSingleSelectMode() === true && this.paxCollection.length > 1 ) {
            return { msgClass: 'msgIndividualPax', valid: false };
        }
        
        if ( this.selectedPromoModel.maxParticipants && this.paxCollection.models.length > this.selectedPromoModel.maxParticipants ) {
            return { msgClass: 'maxParticipantsReached', valid: false };            
        }

        this.$el.find( '.awardPointsInp:visible' ).each( function() {
            if( !that.validateRangeOf( $( this ) ) ) {
                awardValid = false;
                return false;

            } else {
                awardValid = true;
                return { valid: true };
            }
        } );
                // failed generic validation tests (requireds mostly)
        if( !isValid ) {
            return { msgClass: 'msgGenericError', valid: false };
        }

        this.$el.find( '#selectGroup option' ).each( function() {

            if( !$( this ).is( ':selected' ) && $( this ).text() === that.$el.find( '#groupName' ).val() ) {
                groupValid = false;

            }
        } );

        if( this.selectedPromoModel.awardType === 'calculated' && this.selectedPromoModel.recommendedAward ) {
            this.$el.find( '.award input[name*="awardQuantity"]' ).each( function() {
                if( parseInt( $( this ).val(), 10 ) === 0 ) {
                    awardValid = false;
                } else {
                    awardValid = true;
                }
            } );
        }

        if( awardValid && groupValid ) {
            return { valid: true };
        } else {
            if( !awardValid ) {
                return { msgClass: 'msgNoAward', valid: false };
            }
            if( !groupValid ) {
                return { msgClass: 'msgGroupName', valid: false };
            }
        }

    },
    triggerDone: function ( e ) {
        console.log( e );
        $( '.showHideBtn' ).click();
        console.log( 'triggered' );
    }

} );

// =========================
// COLLECTION VIEW OVERRIDE
// =========================
//
// small collection view set here to override the add particpant template and pass extra JSON values
NominationCollectionView = ParticipantCollectionView.extend( {
    initialize: function( opts ) {
        console.log( opts );
        this.opts = opts;

        this.parentView = opts.parentView;

        //inherit events from the superclass
        this.events = _.extend( {}, this.constructor.__super__.events, this.events );

        //this is how we call the super-class, this will render
        this.constructor.__super__.initialize.apply( this, arguments );
		this.parentView.paxCollection.on( 'remove', this.paxRemove, this );
    },

    // override parent method -- adding some massage of json
    addParticipant: function( participant, index ) {
        var that = this;
        console.log( that );
        console.log( participant );
        if( participant.models ) {
            _.each( participant.models, function( model, index ) {
                that.addParticipant( model );
            } );
            return;
        }

        var json = participant.toJSON(),
            promo = this.opts.selectedPromoModel,
            awardType = promo.awardType;
            console.log( json );
        json.cid = participant.cid;

        //adding variables for handlebars tpl
        json._isAwardNone = awardType === 'none';
        json._isAwardRange = awardType === 'pointsRange';
        json._isAwardFixed = awardType === 'pointsFixed';
        json._isAwardCalc = awardType === 'calculated';
        json._isAwardOther = awardType === 'other';
        json._isAwardCalcLevels = promo._isAwardCalcLevels || false;
        json._awardMin = promo.awardMin || false;
        json._awardMax = promo.awardMax || false;
        json._awardFixed = promo.awardFixed || false;
        json._awardLevels = promo.awardLevels ? promo.awardLevels[ json.countryCode ] : false;
        json._awardOther = promo.payoutDescription || false;
        json._isShowRemCol = true;
        json._isShowCalcCol = false;
        json.awardQuantity = promo.awardQuantity || false;
        // check to see if our participant object is a Collection or Model and if our json object is an array or not. Turn both into arrays
        participant = participant.models ? participant.models : [ participant ];
        console.log( participant );
        json = _.isArray( json ) ? json : [ json ];

        // iterate through the JSON, running a few manipulations on the raw data
        _.each( json, function( obj, index ) {
            obj.cid = participant[ index ].cid;

            obj.autoIndex = that.autoIncrement;
            that.autoIncrement++;

            obj = _.extend( obj, that.feedToTpl );
            console.log( obj );
        } );

        TemplateManager.get( this.tplName, function( tpl ) {
            var pi = '',
                $pi;

            // for each object in the json, pass it to the template function and append it to a raw string variable
            _.each( json, function( obj, index ) {
                pi += tpl( obj );
            } );

            // create a jQuery object of our raw string data and prepend it to the table (prepending assures that the newest entry will always be visible)
            $pi = $( pi );
            that.$el.prepend( $pi );

            // run a background color animation only if we're adding fewer than some number of entries
            if( json.length <= 20 ) {
                G5.util.animBg( $pi.find( 'td' ), 'background-flash' );
            }

            //attach parti. popover.
            $pi.find( '.participant-popover' ).participantPopover( { containerEl: that.$el } );

            that.renderEmpty();
            that.updateCount();

            // when a user clicks edit from the preview page, we need to transfer
            // calculator values to each parti, this is a stopgap hack to the
            // already quite hacky implementation of calculator
            // that.parentView.renderCalcInputsForPaxHack(json,$pi);

            if ( ( index + 1 ) >= that.model.length ) {
                that.checkForSavedCalc();
            }

            if( promo.recommendedAward === false ) {
                if( promo.awardType == 'pointsFixed' ) {

                } else {
                    that.$el.find( 'td.award' ).hide();
                }

            }

            that.trigger( 'participantAdded', participant );

        }, G5.props.URL_TPL_ROOT || G5.props.URL_BASE_ROOT + 'tpl/' );

        return this;

    },

	removeParticipant: function( participant ) {
		var that = this;
		var $teamNameWrapper = $( '.teamSection' ),
 		    $fromTabName = $( '.stepNominee.stateUnlocked' ).find( '.wtvTabName' ),
            $fromDisplayName =  $( '.stepNominee.stateLocked' ).find( '.wtvDisplay' ),
            $fromStateDisplayName =  $( '.stepNominee.stateComplete' ).find( '.wtvDisplay' ),
            firstName = '',
            lastName = '';
		    var checkNomiPromoType = $( '#hiddenNominationType' ) ? $( '#hiddenNominationType' ).val() : null ;
			that.$el.find( '[data-participant-cid=' + participant.cid + ']' ).remove();
			that.renderEmpty();
			that.updateCount();
			// Checking of the paxcollection length and display team seaction based on it.
			if( checkNomiPromoType !== 'team' ) {
			   if ( checkNomiPromoType == 'both' && that.parentView.paxCollection.length > 1 ) {
				$teamNameWrapper.show();
			   } else {
					if( that.parentView.paxCollection.length === 1 ) {
						firstName = that.parentView.paxCollection.models[ 0 ].get( 'firstName' );
						lastName = that.parentView.paxCollection.models[ 0 ].get( 'lastName' );
						$fromTabName.text( firstName + ' ' + lastName );
						$fromDisplayName.text( firstName + ' ' + lastName );
						$fromStateDisplayName.text( firstName + ' ' + lastName );
						$teamNameWrapper.hide();
					}
				}
			  }
		    else {
				$teamNameWrapper.show();
			}

	}

} );
