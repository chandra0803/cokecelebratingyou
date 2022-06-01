/*jslint browser: true, nomen: true, devel: false, expr: true, unparam: true*/
/*exported NominationsSubmitPageView, wizTabView, fwdUrl*/
/*global
console,
$,
_,
G5,
PageView,
TemplateManager,
NominationsWizardTabsVerticalView,
NominationsWizardTabsVerticalModel,
NominationsSubmitModel,
NominationsSubmitTabNomineeView,
NominationsSubmitTabBehaviorView,
NominationsSubmitTabEcardView,
NominationsSubmitTabWhyView,
NominationsSubmitPageView:true
*/
NominationsSubmitPageView = PageView.extend( {
    initialize: function ( opts ) {

        //set the appname (getTpl() method uses this)
        this.appName = 'nominations';

        // this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        // inherit events from the superclass ModuleView
        this.events = _.extend( {}, PageView.prototype.events, this.events );
        this.paxCollection = opts.paxCollection;
        this.wizTabs = null;

        this.wizTabsJson = new NominationsWizardTabsVerticalModel();

        this.activePromotionSetup = null;

        this.promotionId = opts.idJson.promotionId;

        this.claimId = opts.idJson.claimId;

        this.nodeId = opts.idJson.nodeId;

        this.allIds = {
            promotionId: this.promotionId,
            claimId: this.claimId,
            nodeId: this.nodeId
        };

        this.model = new NominationsSubmitModel();

        this.model.loadData( this.allIds );

        this.setupEvents();

        this.$el.find( '.richtext' ).htmlarea( G5.props.richTextEditor );
    },

    events: {
        //promotion events
        'click #nominationChangePromoBtn': 'changePromotionPopover',
        'click #nominationChangePromoConfirmBtn': 'changePromotion',
        'click #nominationChangePromoCancelBtn': 'cancelQtip',
        'change #promotionId': 'updatePromotion',
        'change #nodeId': 'updateNodeId',

        //rules modal
        'click .doViewRules': 'showRulesModal',

        //tabs and navigation events
        'click  .nextBtn': 'goToNextStep',
        'click .saveDraftBtn': 'doSaveDraft',
        'click .submitBtn': 'doSaveNomination',
        'click .cancelBtn': 'cancelNominationPopover',
        'click #nominationCancelConfirmBtn': 'doCancelNomination',

        'click #nominationDoNotCancelBtn': 'cancelQtip'
    },

    setupEvents: function() {
        this.model.on( 'dataLoaded', this.render, this );
        this.model.on( 'nominationsLoaded', this.renderPromoDropdown, this );

        this.model.on( 'saveSuccess', this.handleSaveSuccess, this );
        this.model.on( 'saveStarted', this.handleSaveStarted, this );
        this.model.on( 'saveEnded', this.handleSaveEnded, this );
        this.model.on( 'saveError', this.handleSaveError, this );

        this.model.on( 'error:genericAjax', this.handleSaveError, this );
        this.model.on( 'nodeUpdated', this.nodeUpdate, this );

        this.wizTabsJson.on( 'tabsLoaded', this.loadTabs, this );
        this.paxCollection.on( 'remove', this.paxCounterRemove, this );
        this.paxCollection.on( 'add', this.paxCounterAdd, this );
    },

    render: function() {
        var $tabName = this.$el.find( '.wtvDisplay' ),
            that = this;

        $tabName.remove();

        that.$el.find( '.wtTabVert' ).remove();
        that.wizTabsJson.loadTabs();
    },

    loadTabs: function() {

        this.initTabs();

        this.updateNodeId();
    },

    initTabs: function() {
        var wizTabs = this.wizTabsJson.get( 'tabs' ),
            that = this;

        this.wizTabs = new NominationsWizardTabsVerticalView( {
            el: this.$el.find( '.wizardTabsVerticalView' ),
            tabsJson: this.wizTabsJson.get( 'tabs' ),
            onTabClick: function( e, originTab, destTab, wtv ) {
                // our handleWizardTabClick function replaces the standard function in WTV
                that.handleWizardTabClick( e, originTab, destTab, wtv );
            },
            scrollOnTabActivate: true
        } );

        // let the name of the tab objects match the corresponding data-tab-name + 'TabView'
        // this will allow tab nav/validate logic to flow naturally from the names
        _.each( wizTabs, function( wt ) {

            if ( wt.name === 'stepNominee' ) {
                that.stepNomineeTabView = new NominationsSubmitTabNomineeView( {
                    el: '#nominationsTabNomineeView',
                    containerView: that
                } );
            }

            if ( wt.name === 'stepBehavior' ) {
                that.stepBehaviorTabView = new NominationsSubmitTabBehaviorView( {
                    el: '#nominationsTabBehaviorView',
                    containerView: that
                } );
            }

            if ( wt.name === 'stepEcard' ) {
                that.stepEcardTabView = new NominationsSubmitTabEcardView( {
                    el: '#nominationsTabEcardView',
                    containerView: that
                } );
            }

            if ( wt.name === 'stepWhy' ) {
                that.stepWhyTabView = new NominationsSubmitTabWhyView( {
                    el: '#nominationsTabWhyView',
                    containerView: that
                } );
            }
        } );

        this.wizTabs.on( 'tabsInitialized', function() {

            that.jumpToStep( that.model.getCurrentStep() );

            that.appendContent();
            that.initButtons();
            that.$el.find( '.richtext' ).htmlarea( G5.props.richTextEditor );
        } );
    },

    initButtons: function() {
        var tabs = this.wizTabsJson.get( 'tabs' ),
            lastTab = _.last( tabs ),
            lastTabName = lastTab.name,
            $lastTabContent = this.$el.find( '.' + lastTabName + 'Content' ),
            promo = this.model.get( 'promotion' );


        // $lastTabContent.find('.submitBtn').show();

        if ( promo.privateNomination ) {
            $lastTabContent.find( '.privateNom' ).show();
        } else {
            $lastTabContent.find( '.privateNom' ).hide();
        }

        // $lastTabContent.find('.nextBtn').hide();

    },
    
    isSingleSelectMode: function() {
    	this.selectedPromoModel = this.model.get( 'promotion' );
        return this.selectedPromoModel.individualOrTeam === 'individual';
    },

    appendContent: function() {
        var $tabs = this.$el.find( '.wtTabVert' ),
            wizTabs = this.wizTabsJson.get( 'tabs' ),
            that = this;

        $tabs.hide();
        //moving the step content underneath the step tab for creative flow to work correctly
        //because whole big mess of miscommunication from management on design
        _.each( $tabs, function( tab ) {
            var tabName = $( tab ).data( 'tabName' );

            _.each( wizTabs, function( wt ) {
                if ( tabName === wt.name ) {
                    that.$el.find( '.' + tabName + 'Content' ).insertAfter( $( tab ).show() );
                }
            } );
        } );
    },

    setClaimId: function( id ) {
        this.allIds.claimId = id;
    },

    // *******************
    // PROMOTION SETUP
    // *******************
    updatePromotion: function( e ) {
        var $promoVal = parseInt( $( e.target ).val() ),
            params;

        this.promotionId = $promoVal;
        this.allIds.promotionId = $promoVal;

        params = {
            promotionId: $promoVal,
            claimId: null,
            nodeId: this.nodeId
        };

        this.model.loadData( params, true /*promotion change*/ );

        this.wizTabsJson.loadTabs();
    },

    changePromotion: function() {
        var $promotionPopover = this.$el.find( '.promoChangeConfirmDialog' );

        $promotionPopover.closest( '.qtip' ).qtip( 'hide' );


        this.model.loadPromotionsList( this.allIds );
    },

    renderPromoDropdown: function() {
        var $promotionListContainer = this.$el.find( '.promotionList' ),
            noms = this.model.get( 'nominations' );

        TemplateManager.get( 'promotionsList', function( tpl ) {
            $promotionListContainer.find( '.promoChangeWrapper' ).remove();
            $promotionListContainer.append( tpl( noms ) );
        } );
    },

    showRulesModal: function( e ) {
        var $rulesModal = $( 'body' ).find( '#rulesModal' ),
            $cont = $rulesModal.find( '.modal-body' ).empty();

        e ? e.preventDefault() : false;

        $cont.append( this.model.get( 'promotion' ).rulesText || 'ERROR, modal found no rulesText on promo' );

        $rulesModal.modal();
    },

    updateNodeId: function() {
        var $orgSelect = parseInt( this.$el.find( '#nodeId option:selected' ).val() ) || this.$el.find( '.singleNodeId' ).data( 'id' );

        if ( $orgSelect ) {

            this.allIds.nodeId = $orgSelect;

            this.model.setNodeId( $orgSelect );
        }

    },
    nodeUpdate: function( id ) {

        var that = this;

        that.nodeId = id;
        that.stepNomineeTabView.promoIds.nodeId = id;
        if ( that.stepNomineeTabView.recipientSearchView ) {

          that.stepNomineeTabView.recipientSearchView.model.params.nodeId = id;
        }

    },
    paxCounterRemove: function() {

        if ( this.paxCollection.models.length === 0 ) {
            this.wizTabs.setTabState( 1, 'error' );
        }
    },
    paxCounterAdd: function() {
        if ( this.wizTabs && this.wizTabs.getTabById( 1 ).get( 'state' ) === 'error' ) {

            if ( this.wizTabs.getTabById( 1 ).get( 'isActive' ) === true ) {
                this.wizTabs.setTabState( 1, 'locked' );
                this.wizTabs.setTabState( 1, 'unlocked' );
            } else {
                this.wizTabs.setTabState( 1, 'complete' );
            }
            console.log( this.wizTabs.getTabById( 1 ).get( 'state' ) );
        }
    },
    // *******************
    // POPOVERS
    // *******************
    changePromotionPopover: function( e ) {
        var $tar = $( e.target ),
            $cont = ( this.$el.find( '.promoChangeConfirmDialog' ).clone() );

        if ( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, $cont, this.$el );
        }

        $tar.attr( 'disabled', 'disabled' );
    },

    cancelNominationPopover: function( e ) {
        var $tar = $( e.target ),
            $cont = this.$el.find( '.cancelNominationPopover' );

        if ( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, $cont, this.$el );
        }
    },

    attachPopover: function( $trig, content, container ) {
        $trig.qtip( {
            content: { text: content },
            position: {
                my: 'left center',
                at: 'right center',
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
    },

    cancelQtip: function( e ) {
        e.preventDefault();

        $( e.currentTarget ).closest( '.qtip' ).qtip( 'hide' );

        this.$el.find( '#nominationChangePromoBtn' ).removeAttr( 'disabled' );
    },

    // display a qtip for next buttons or tab clicks (uses class name to show proper error)
    onNavErrorTip: function( msgClass, $target, isDestroyOnly ) {
		//Bug id 77595: Removing class pin-body to make the popup error float near the field
		if(msgClass) {
			$( 'body' ).removeClass( 'pin-body' );
		}
        var $cont = this.$el.find( '.errorTipWrapper .errorTip' ).clone(),
            isBtn = $target.hasClass( 'btn' ) || $target.is( 'input' );

        //if old qtip still visible, obliterate!
        if ( $target.data( 'qtip_error' ) ) { $target.data( 'qtip_error' ).destroy(); }

        if ( isDestroyOnly ) {
            return;
        }

        $cont.find( '.' + msgClass ).show(); // show our message

        //attach qtip and show
        $target.qtip( {
            content: { text: $cont },
            position: {
                my: isBtn ? 'bottom center' : 'top center',
                at: isBtn ? 'top center' : 'bottom center',
                effect: this.isIe7OrIe8 ? false : true,
                viewport: true,
                container: this.$el,
                adjust: {
                    method: 'shift none'
                }
            },
            show: {
                event: false,
                ready: true
            },
            hide: {
                event: 'unfocus click',
                fixed: true,
                delay: 200
            },
            style: {
                classes: 'ui-tooltip-shadow ui-tooltip-red nominationErrorQTip',
                tip: {
                    corner: true,
                    width: 10,
                    height: 5
                }
            }
        } );

        $target.data( 'qtip_error', $target.data( 'qtip' ) ).removeData( 'qtip' );
    },

    // *******************
    // WIZARD TABS
    // *******************
    handleWizardTabClick: function( e, fromTab, toTab, wizTabView ) {
        e.preventDefault();
        console.log( wizTabView );
        // exit if the toTab is locked
        if ( toTab.get( 'state' ) === 'locked' ) { return; }

        // exit if the current tab was clicked
        if ( fromTab.get( 'name' ) === toTab.get( 'name' ) ) { return; }

        this.goFromStepToStep( fromTab.get( 'name' ), toTab.get( 'name' ), $( e.currentTarget ) );
    },

    // generic from step to step checking for validation (guts of tab nav logic)
    goFromStepToStep: function( fromName, toName, $target ) {
        var // call the validation func of apropo tab if it exists
            fObjKey = fromName + 'TabView',
            tObjKey = toName + 'TabView',
            hasSaveFunc = this[ fObjKey ] && this[ fObjKey ].save,
            hasUpdFunc = this[ tObjKey ] && this[ tObjKey ].updateTab,
            validationRes,
            goingDirection = this.wizTabs.getTabByName( fromName ).get( 'id' ) > this.wizTabs.getTabByName( toName ).get( 'id' ) ? 'backward' : 'forward';

                // there was a validation function + not valid
        if ( goingDirection === 'forward' ) {
            // find the validation function for the active step view and call it if it exists
            validationRes = this[ fObjKey ] && this[ fObjKey ].validate ? this[ fObjKey ].validate() : null;
        }

        // there was a validation function + not valid
        if ( goingDirection === 'forward' && validationRes && !validationRes.valid ) {
            // if the target is a tab, it means the user has clicked on the next tab in the wizard, but we want the error to display on the current step's tab
            if ( $target.hasClass( 'wtTabVert' ) ) {
                $target = this.wizTabs.getTabByName( fromName ).$tab;
            }
            this.wizTabs.setTabState( fromName, 'incomplete' ); // tab "error" state
            this.onNavErrorTip( validationRes.msgClass, validationRes.$target || $target ); // apply msg class to error element
            // lock next tabs to prevent the user from moving forward with an error
            this.wizTabs.setTabState( this.wizTabs.getNextTab().id, 'locked', true );
        }        else {
            // store plain jane input values in model
            this.saveInputsToModel();

            this.saveCustomFormElements();

            if ( goingDirection === 'forward' && fromName === 'stepNominating' ) {
                if ( !G5.util.formValidate( this.$el.find( '.nominationOrgSection .validateme' ) ) ) {
                    return false;
                }
                this.handleSaveSuccess( null, fromName, toName, false, false );
                return;
            }

            if ( goingDirection === 'forward' ) {
                if ( hasSaveFunc ) {
                    this[ fObjKey ].save( fromName, toName, this.allIds,  false /*isDraft*/ );
                } else {
                    this.model.save( fromName, toName, this.allIds,  false /*isDraft*/ );
                }

                // update if has method
                if ( hasUpdFunc ) { this[ tObjKey ].updateTab(); }

            } else {
                            // unlock destination tab and activate
                this.wizTabs.setTabState( toName, 'unlocked' ); // unlock
                this.wizTabs.activateTab( toName ); // go to tab

                // update if has method
                if ( hasUpdFunc ) { this[ tObjKey ].updateTab(); }
            }

        }
    },

    goToNextStep: function( e ) {
        var $tar = $( e.currentTarget ),
            fromTab = this.wizTabs.getActiveTab(),
            toTab = this.wizTabs.getNextTab();

        e.preventDefault();

        if( ( fromTab.get( 'name' ) === 'stepEcard' ) && ( this.selectedPromoModel.memesActive || this.selectedPromoModel.stickersActive ) ) {    
            G5._globalEvents.trigger( 'writeEcardText', e );
            G5._globalEvents.trigger( 'drawEcardStickers', e );            
        }
		
		if( this.isSingleSelectMode() === true && this.paxCollection.length > 1 ) {
            this.wizTabs.setTabState( fromTab.get( 'name' ), 'incomplete' ); // tab "error" state
            this.onNavErrorTip( 'msgIndividualPax', $tar); // apply msg class to error element
        }
		else{
			this.goFromStepToStep( fromTab.get( 'name' ), toTab.get( 'name' ), $tar );
		}
    },

    jumpToStep: function( toName ) {
        var that = this,
            tObjKey = toName + 'TabView',
            active = this.wizTabs.getActiveTab(),
            fObjKey = active.get( 'name' ) + 'TabView',
            hasUpdToFunc = this[ tObjKey ] && this[ tObjKey ].updateTab,
            hasUpdFromFunc = this[ fObjKey ] && this[ fObjKey ].updateTabName;

        // unlock and activate
        this.wizTabs.setTabState( toName, 'unlocked' );
        this.wizTabs.activateTab( toName );

        // unlock all prev tabs
        _.each( this.wizTabs.getAllPrevTabs(), function( t ) {
            var name = t.get( 'name' ),
            view = name + 'TabView';
            that.wizTabs.setTabState( t.id, 'complete' );
            if( name != 'stepNominating' ) {
                that[ view ].updateTabName( name );

            }
        } );

        // call update func on tab
        if ( hasUpdToFunc ) {
            this[ tObjKey ].updateTab();

        }

        if ( hasUpdFromFunc ) {
            this[ fObjKey ].updateTabName();

        }
    },

    doSaveDraft: function( e ) {
        var fromTab = this.wizTabs.getActiveTab().get( 'name' ),
            currTabObjKey = fromTab + 'TabView',
            hasSaveDraftFunc = this[ currTabObjKey ] && this[ currTabObjKey ].saveAsDraft,
			$tar = $( e.currentTarget ),
		    $parentEl = e.currentTarget.parentElement,
            $btnToValidate = $( $parentEl ).find( 'button.nextBtn' ) && this.wizTabs.getNextTab() ? $( $parentEl ).find( 'button.nextBtn' ) : $( $parentEl ).find( 'button.submitBtn' ),
            validationRes;


        e.preventDefault();

        if( ( currTabObjKey === 'stepEcardTabView' ) && ( this.selectedPromoModel.memesActive || this.selectedPromoModel.stickersActive ) ) {    
            G5._globalEvents.trigger( 'writeEcardText', e );
            G5._globalEvents.trigger( 'drawEcardStickers', e );            
        }

        if( !this.isSingleSelectMode() && ( this.selectedPromoModel.nominatingType === 'both' || this.selectedPromoModel.nominatingType === 'team' )  && currTabObjKey === 'stepNomineeTabView' ) {
            validationRes = this[ currTabObjKey ] && this[ currTabObjKey ].validate ? this[ currTabObjKey ].validate() : null;
            if( !validationRes.valid ) {
                this.onNavErrorTip( validationRes.msgClass, validationRes.$btnToValidate || $btnToValidate );	
            } else {
                this.triggerSaveDraft.call( this, hasSaveDraftFunc, currTabObjKey, fromTab );
            }
        } else {
            if( this.isSingleSelectMode() === true && this.paxCollection.length > 1 ) {
                if ( $tar.hasClass( 'wtTabVert' ) ) {
                    $tar = this.wizTabs.getTabByName( fromTab ).$tab;
                }
                this.wizTabs.setTabState( fromTab, 'incomplete' ); // tab "error" state
                this.onNavErrorTip( 'msgIndividualPax', $tar ); // apply msg class to error element
            } else {
                this.triggerSaveDraft.call( this, hasSaveDraftFunc, currTabObjKey, fromTab );
            }
        }
    },

    triggerSaveDraft: function( hasSaveDraftFunc, currTabObjKey, fromTab ) {
        if( hasSaveDraftFunc ) {
            // current tab has its own saveAsDraft function, use it instead of the contest model's
            this[ currTabObjKey ].saveAsDraft( fromTab, null, this.allIds, true );
        } else {
            if( this.model.get( 'promotion' ).customFieldsActive ) {
                this.saveCustomFormElements();
            }

            this.saveInputsToModel();

            this.model.save( fromTab, null, this.allIds, true );/*isDraft*/
        } 
    },

    doSaveNomination: function( e ) {
        var fromTab = this.wizTabs.getActiveTab(),
            fromTabName = fromTab.get( 'name' ),
            fObjKey = fromTabName + 'TabView',
            validationRes = this[ fObjKey ] && this[ fObjKey ].validate ? this[ fObjKey ].validate() : null,
            $validate = this.$el.find( '.validateme:visible' ),
            isValid = G5.util.formValidate( $validate ),
            msgClass = 'msgGenericError',
            $tar = $( e.target );

        e.preventDefault();
        G5._globalEvents.trigger( 'writeEcardText', e );
        G5._globalEvents.trigger( 'drawEcardStickers', e );
        
        if( this.isSingleSelectMode() === true && this.paxCollection.length > 1 ) {
        	this.onNavErrorTip( 'msgIndividualPax', $tar ); // apply msg class to error element
            return false;
        }

        // failed generic validation tests (requireds mostly)
        if ( !isValid ) {
            this.onNavErrorTip( msgClass, $tar ); // apply msg class to error element
            return false;
        }

        if ( validationRes && !validationRes.valid ) {
            this.onNavErrorTip( validationRes.msgClass, validationRes.$target || $tar ); // apply msg class to error element
            return false;
        }

        if ( this.model.get( 'promotion' ).defaultWhyActive ) {
            this.saveInputsToModel();
        }

        if ( this.model.get( 'promotion' ).customFieldsActive ) {
            this.saveCustomFormElements();
        }

        this.model.save( fromTabName, null, this.allIds, false, true /*isSubmit*/ );
    },

    doCancelNomination: function( e ) {
        var forwardUrl = this.$el.find( '#nominationCancelConfirmBtn' ).attr( 'href' ),
            data;

        e.preventDefault();

        data = {
            claimId: this.allIds.claimId
        };

        this.model.deleteNom( data, forwardUrl );
    },

    saveInputsToModel: function() {
        var that = this;

        // loop through els and check for data-model-key to lookup value in Model
        this.$el.find( '.stepContent' ).find( 'input, textarea' ).each( function() {
            var $t = $( this ),
                checkedVal,
                mk = $t.data( 'modelKey' ),
                mv = mk ? $t.val() : null,
                type = $t.attr( 'type' );


            if ( mk ) {
                if ( type === 'radio' ) { // set to boolean true/false if one of those strings is the value
                    //console.log('MK',mk);
                    checkedVal = that.$el.find( '.stepContent [data-model-key=' + mk + ']:checked' ).val();
                    // checkedVal = checkedVal ? checkedVal.toLowerCase() : checkedVal;
                    if ( checkedVal === 'true' ) { mv = true; }                    else if ( checkedVal === 'false' ) { mv = false; }                    else { mv = typeof checkedVal === 'undefined' ? null : checkedVal; }
                }
                if ( type === 'checkbox' ) { // boolean
                    mv = $t.is( ':checked' );
                }
                //console.log('Inputs->Model',mk,'...',mv);
                that.model.setInputs( mk, mv, { silent: true } );
            }
        } );
    },

    saveCustomFormElements: function() {
        var $form = this.$el.find( '.customElementsWrap' ),
            $inputs = $form.find( 'input, select, textarea' ),
            that = this;

        _.each( $inputs, function( inp ) {
            var index = $( inp ).data( 'index' ),
                checkedVal,
                val;

            if ( $( inp ).attr( 'type' ) === 'radio' ) {
                checkedVal = that.$el.find( '.radio [data-index=' + index + ']:checked' ).val();
                if ( checkedVal === 'true' ) {
                    val = true;
                } else if ( checkedVal === 'false' ) {
                    val = false;
                } else {
                    val = null;
                }

            } else {
                val = $( inp ).val();
            }

            that.model.setCustomFormElements( index, val );
        } );
    },

    handleSaveStarted: function() {

        G5.util.showSpin( this.$el, { cover: true } );

    },

    handleSaveEnded: function() {

        G5.util.hideSpin( this.$el );

    },

    handleSaveError: function( errors ) {
        if ( !_.isArray( errors ) ) {
            errors = [ errors ]; // make it an array
        }

        errors = _.map( errors, function( e ) {
            return e.text || e;
        } );

        this.showErrorModal( errors );
    },

    handleSaveSuccess: function( serverData, fromStep, toStep, isDraft, isSubmit ) {
        var tObjKey,
            fObjKey,
            hasUpdFunc,
            hasLeaveFunc,
            hasUpdNameFunc,
            goingDirection,
            fwdUrl,
            fromId,
            toId;

        if ( serverData ) {
            fwdUrl = serverData.forwardUrl;
            console.log( fwdUrl );
            this.setClaimId( serverData.claimId );
        }

        if ( isSubmit ) {
            this.$el.find( '.nominationSubmittedModal' ).modal( 'show' );
            return;
        }

        if ( isDraft ) {
            $( '.' + fromStep + 'Content' ).find( '.nominationSaved' ).show();
        }

        // CASE: save success on fromStep - toStep
        if ( fromStep && toStep ) {
            tObjKey = toStep + 'TabView';
            fObjKey = fromStep + 'TabView';
            hasUpdFunc = this[ tObjKey ] && this[ tObjKey ].updateTab;
            hasUpdNameFunc = this[ fObjKey ] && this[ fObjKey ].updateTabName;
            hasLeaveFunc = this[ fObjKey ] && this[ fObjKey ].leaveTab;
            fromId = this.wizTabs.getTabByName( fromStep ).get( 'id' );
            toId = this.wizTabs.getTabByName( toStep ).get( 'id' );
            goingDirection = fromId > toId ? 'backward' : 'forward';

            // assume all locks cleared on step change, toTab.update can do stuff to nav
            //this.clearNavLocks();

            // set the active step/tab
            // this.model.setStepByName(toStep);

            // mark fromTab complete ONLY if we're going forward
            if ( goingDirection === 'forward' && fromStep ) {
                this.wizTabs.setTabState( fromStep, 'complete' ); // complete

                //update tab display name
                if ( hasUpdNameFunc ) { this[ fObjKey ].updateTabName( fromStep ); }

                this.$el.find( '.' + fromStep + 'Content' ).find( '.nominationSaved' ).show();
            }

            // unlock destination tab and activate
            this.wizTabs.setTabState( toStep, 'unlocked' ); // unlock
            this.wizTabs.activateTab( toStep ); // go to tab

            // leave if has method
            if ( hasLeaveFunc ) { this[ fObjKey ].leaveTab(); }

            // update if has method
            if ( hasUpdFunc ) { this[ tObjKey ].updateTab(); }

            return;
        }

    },

    // Display a (list) of strings in a modal.
    showErrorModal: function( content ) {
        var $m = this.$el.find( '.nomsErrorsModal' ),
            $l = $m.find( '.errorsList' );

        if ( !_.isArray( content ) ) { content = [ content ]; } // arrayify

        $l.empty();

        _.each( content, function( c ) {
            $l.append( '<li>' + c + '</li>' );
        } );

        $m.modal();
    }

} );
