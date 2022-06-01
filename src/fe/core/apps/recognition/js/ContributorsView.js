/*exported ContributorsView*/
/*global
TemplateManager,
WizardTabsView,
ParticipantCollectionView,
PaxSelectedPaxCollection,
PaxSearchStartView,
ContributorsView:true
*/

/** CONTRIBUTORS VIEW - for adding/modifying contributors for PURL **/
ContributorsView = Backbone.View.extend( {
    initialize: function( opts ) {

        this.tplName = opts.tplName || 'contributorsView';
        this.tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'recognition/tpl/';

        // for creation of PURL, no invitations are ever sent already, so this column should be hidden
        this.showInvitedColumn = typeof opts.showInvitedColumn !== 'undefined' ? opts.showInvitedColumn : true;

        this.contribFormNamePrefix = opts.contribFormNamePrefix || false;
        this.presetSearchFilters = opts.contributorTeamsSearchFilters || null;
		//If Server does not send filters, do not render the Team tab.
        if ( this.presetSearchFilters && this.presetSearchFilters.filters.length === 0 ) {
            this.presetSearchFilters = null;
        }

        this.parentView = opts.parentView;

        this.parentView.on( 'promoNodeChange', function( p, n ) {this.handlePromoNodeChange( p, n );}, this );
        this.parentView.on( 'purlRecipientSet', function( r, nId ) {
            this.handleRecipientChange( r, nId );
        }, this );
        this.parentView.on( 'recipientNodeIdChange', function( r, nId ) {
            this.handleRecipientChange( r, nId );
        }, this );

        //inherit events from the superclass
        this.events = _.extend( {}, this.constructor.__super__.events, this.events );

        //this is how we call the super-class, this will render
        this.constructor.__super__.initialize.apply( this, arguments );

        this.render();
    },

    events: {
        'click .stepContentControls .backBtn': 'goBackAStep',
        'click .stepContentControls .nextBtn': 'goToNextStep',
        'click .addEmailsBtn': 'doAddEmails'
    },
    goToNextStep: function( e ) {
        var fromTab = this.wizTabs.getActiveTab(),
            toTab = this.wizTabs.getNextTab();

        if( e ) {e.preventDefault();}
        this.goFromStepToStep( fromTab.get( 'name' ), toTab.get( 'name' ) );
    },
    goBackAStep: function( e ) {
        var fromTab = this.wizTabs.getActiveTab(),
            toTab = this.wizTabs.getPrevTab();

        if( e ) {e.preventDefault();}
        this.goFromStepToStep( fromTab.get( 'name' ), toTab.get( 'name' ) );
    },
    // WizardTabsView tab click
    handleTabClick: function( e, originTab, destTab ) {
        if( e ) {e.preventDefault();}
        if( destTab.get( 'state' ) !== 'locked' ) {
           this.goFromStepToStep( originTab.get( 'name' ), destTab.get( 'name' ) );
        }
    },
    // generic from step to step
    goFromStepToStep: function( fromName, toName ) {

        this.wizTabs.setTabState( fromName, 'complete' ); // complete
        this.wizTabs.setTabState( toName, 'unlocked' ); // unlock
        this.wizTabs.activateTab( toName ); // go to tab
        this.trigger( 'stepChanged' );

        // business wants us to scrollTo the top of the tabs when they change (especially from next/back buttons)
        $.scrollTo( this.$el.find( '.wizardTabsView' ), G5.props.ANIMATION_DURATION, { axis: 'y', offset: { top: -24, left: 0 } } );

        // if the tab we've moved to is the stepOthers tab
        if( toName == 'stepOthers' ) {
            this.$el.find( '.contribEmailsInput' ).focus();
        }
    },
    doAddEmails: function( e ) {
        var $ems = this.$el.find( '.contribEmailsInput' ),
            $msg = this.$el.find( '.contribEmailsFeedback' ),
            txt = $ems.val(),
          //Custom for TCCC - WIP-26532 - Checking for Allowed Domains to send mail invite starts			
			recipView = this.parentView.getRecipientView(),
			recPurlAllow = recipView&&recipView.model.length?recipView.model.at(0).get('purlAllowOutsideDomains'):false,
			recPurlAllowHidden = this.parentView.$dataForm ? this.parentView.$dataForm.find('#purlAllowOutsideDomains').val() : false, 			
			recogPurlAllowView = this.parentView.formSetup ? this.parentView.formSetup.purlAllowOutsideDomains : null,
			allowedEmailDomains;

			if(recPurlAllow) {
				recPurlAllow = JSON.parse(recPurlAllow); // Convert recPurlAllow to boolean if its string		
			}
			
			if(recPurlAllow == false) {
				recPurlAllowHidden = "false";
			}
			
			if(this.parentView.options.json && recPurlAllowHidden == "false") {
				allowedEmailDomains = this.parentView.options.json.purlAllowOutsideDomains;
			}
			else if ((recogPurlAllowView && !recPurlAllow) && (recogPurlAllowView && recPurlAllowHidden == "false")) {									
				allowedEmailDomains = this.parentView.formSetup.purlAllowOutsideDomains;						
			}
			else {
				allowedEmailDomains = null;
			}
			
            var parsed = G5.util.parseEmails(txt,allowedEmailDomains),
			//Custom for TCCC - WIP-26532 - Checking for Allowed Domains to send mail invite ends
            that = this;

        if( e ) {e.preventDefault();} // prev def

        // resets
        $msg.find( '.msg' ).hide();
        $msg.find( '.errorEmailsList' ).empty();

        if( parsed.errorCode ) {
            $msg.show();
            $msg.find( '.' + parsed.errorCode ).show();
            if( parsed.errorEmails.length ) {
                _.each( parsed.errorEmails, function( em, i ) {
                    $msg.find( '.errorEmailsList' ).append( em.email + ( i < parsed.errorEmails.length - 1 ? '<br>' : '' ) );
                } );
            }
          
            //Custom for TCCC - WIP-26532 - Checking for Allowed Domains to send mail invite starts
			if(parsed.errorDomainEmails.length) {
                _.each(parsed.errorDomainEmails,function(em,i){
                    $msg.find('.errorEmailsList').append(em.email+ (i < parsed.errorDomainEmails.length-1 ? '<br>' : '') );
                });
            }
			//Custom for TCCC - WIP-26532 - Checking for Allowed Domains to send mail invite ends
        }
        // success
        else {
            $msg.show().find( '.emailsFound' ).show().find( '.count' ).text( parsed.emails.length );
            _.each( parsed.emails, function( emObj ) {
                emObj.id = emObj.email; // this is the id, we don't need duplicate emails showing up
                emObj.contribType = 'other';
                that.contributorsView.model.add( emObj );
            } );
            $ems.val( '' );
        }

    },


    render: function() {
        var that = this;//,
            //preselLocked = this.parentView.getFormSettings().preselectedLocked=='true';

        TemplateManager.get( this.tplName, function( tpl, vars ) {
            that.tplVars = vars;
            console.log( that );
            that.$el.append( tpl );
            //console.log(that.parentView.selectedPaxView.collection.models[0]);

            that.initPaxCollViews();
            // init WizardTabsView
            console.log( $( '.wizardTabsView' ) );
            that.wizTabs = new WizardTabsView( {
                el: $( '.wizardTabsView' ),
                onTabClick: function( e, originTab, destTab, wtv ) {
                    // our handleTabClick function replaces the standard function in WTV
                    that.handleTabClick( e, originTab, destTab, wtv );
                },
                tabsJson: that.tplVars.tabsJson,
                scrollOnTabActivate: true,
                pageView: that.parentView
            } );
            console.log( that.wizTabs );
            console.log( that.parentView.selectedPaxView );
            that.wizTabs.on( 'afterTabActivate', function() {
                that.updatePaxCollectionView();
            }, that );
            that.wizTabs.on( 'tabsInitialized', function() {
                if( that.parentView.selectedPaxView ) {
                    var pax = that.parentView.selectedPaxView.collection.models[ 0 ];
                    var node = that.parentView.$node;
                    that.handleRecipientChange( pax, node );
                }

                that.updatePaxCollectionView();
                that.trigger( 'stepChanged' );
                console.log( 'tabs' );
                that.trigger( 'rendered' );
            } );
        }, this.tplUrl );

        return this;
    },

    initPaxCollViews: function() {
        var //$presel = this.$el.find('#preselectedContributorsView'),
            //$addtl = this.$el.find('#additionalContributorsView'),
            //$others = this.$el.find('#otherContributorsView'),
            $all = this.$el.find( '#allContributorsView' ),
            //$search = this.$el.find('#contributorSearchView'),
            contribs = this.parentView.getFormSettings().contributors,
            presel = _.filter( contribs, function( c ) {return c.contribType == 'preselected';} ),
            preselLocked = this.parentView.getFormSettings().preselectedLocked,
            //addtl = _.filter(contribs,function(c){return c.contribType == 'additional';}),
            //others = _.filter(contribs,function(c){return c.contribType == 'other';}),
            //that = this;

        // get this flag
        preselLocked = preselLocked && preselLocked === 'true';

        if( preselLocked ) {
            _.each( presel, function( c ) {
                c._noRemove = true;
            } );
        }

        // convert strings to booleans for invitationsSent
        // _.each(contribs, function(c){
        //     c.invitationSent = c.invitationSent&&(c.invitationSent.toLowerCase()==='true');
        // });
        // NOTE: these aint booleans, they's strings (dates, raisins and prunes)

        // store the preselected
        this.preselectedContributors = presel;


        var collection = new PaxSelectedPaxCollection();

        this.contributorsView = new ParticipantCollectionView( {
            el: $all,
            tplName: 'contributorViewItem',
            tplUrl: this.tplUrl,
            model: collection, //new Backbone.Collection(contribs),
            feedToTpl: { _paxName: this.contribFormNamePrefix, _showInvitedColumn: this.showInvitedColumn }
        } );





        this.participantSearchFilterView = new PaxSearchStartView( {
                el: this.$el.find( '.paxSearchFilterStartView' ),
                filterSearch: true,
                multiSelect: true,
                selectedPaxCollection: collection,
                disableSelect: false,
                addSelectedPaxView: true,
                presetFilters: this.presetSearchFilters,
                follow: false
            } );

        this.participantSearchView = new PaxSearchStartView( {
                el: this.$el.find( '.paxSearchStartView' ),
                multiSelect: true,
                selectedPaxCollection: collection,
                disableSelect: false,
                addSelectedPaxView: true,
                follow: false
            } );
            console.log( this );
        this.hideShowTeamFilter( this.presetSearchFilters );
        // set the collection
        collection.reset( contribs );


        this.contributorsView.$wrapper.find( 'th.invitationSent' )[ this.showInvitedColumn ? 'show' : 'hide' ]();


    },
    hideShowTeamFilter: function( show ) {
        var filterTab = this.$el.find( '.filter-search' ),
            nameTab = this.$el.find( '.name-search' );
        if( show ) {
            filterTab.show();
            nameTab.show();
            this.$el.find( '.filter-search a' ).tab( 'show' );
        }else{
            filterTab.hide();
            nameTab.hide();
            this.$el.find( '.name-search a' ).tab( 'show' );
        }
    },
    updatePaxCollectionView: function() {
        var activeTab = this.wizTabs ? this.wizTabs.getActiveTab() : null,
            $paxColWrap = this.$el.find( '.participantCollectionViewWrapper' );

        // set a special class on the pax collection corresponding to the active step
        if( activeTab ) {
            // reset classes
            $paxColWrap[ 0 ].className = $paxColWrap[ 0 ].className.replace( /\bstepClass_.*?\b/g, '' );
            // add special class for current step
            $paxColWrap.addClass( 'stepClass_' + activeTab.get( 'name' ) );

            $paxColWrap[ ( activeTab.get( 'name' ) == 'stepPreview' ) ? 'removeClass' : 'addClass' ]( 'container-splitter' );

            if( activeTab.get( 'name' ) == 'stepPreview' ) {
                // if there are no selected contributors who have not yet been invited...
                if( this.contributorsView.model.filter( function( pax ) { return !pax.get( 'invitationSentDate' ); } ).length <= 0 ) {
                    // create and show an error message instead of the pax search results
                    this.contributorsView.$wrapper.append( '<div class="alert allInvitedMessage">' + this.contributorsView.$el.data( 'msgAllInvited' ) + '</div>' );
                    this.contributorsView.$el.parents( 'table, .rT-wrapper' ).hide();
                    this.parentView.$el.find( '#recognitionButtonSend' ).addClass( 'disabled' ).attr( 'disabled', 'disabled' );
                }
            }
            // if we're not on the preview step, we need to undo the special display stuff we did in the nested if() above, even if we don't match that condition
            else {
                this.contributorsView.$el.parents( 'table, .rT-wrapper' ).show();
                this.contributorsView.$wrapper.find( '.allInvitedMessage' ).remove();
                this.parentView.$el.find( '#recognitionButtonSend' ).removeClass( 'disabled' ).removeAttr( 'disabled' );
            }
        }
    },
    // promo(setup) and node(setup) come from the parent view
    handlePromoNodeChange: function() {
        this.updateContribSearchViewParams();
    },
    updateContribSearchViewParams: function() {
        var promo = this.parentView.getPromoSetup(),
            recipView = this.parentView.getRecipientView(),
            recipId = recipView && recipView.model.length ? recipView.model.at( 0 ).get( 'id' ) : false;//,
            //searchParam = this.participantSearchFilterView,
            //searchParam2 = this.participantSearchView;

        // clear these if necessary
        /*delete searchParam.promotionId;
        delete searchParam.recipientId;
        delete searchParam.contributorId;
        delete searchParam2.promotionId;
        delete searchParam2.recipientId;
        delete searchParam2.contributorId;*/
        this.participantSearchFilterView.clearAjaxParam();
        this.participantSearchView.clearAjaxParam();

        // sometimes contribSearchView is not yet defined here
        if( this.participantSearchFilterView ) {
            // set the promotionId for the contrib pax search
            this.participantSearchFilterView.setAjaxParam( 'promotionId', promo.id );
            this.participantSearchView.setAjaxParam( 'promotionId', promo.id );
            if( recipId ) {
                this.participantSearchFilterView.setAjaxParam( 'recipientId', recipId );
                this.participantSearchView.setAjaxParam( 'recipientId', recipId );
            }
            this.participantSearchFilterView.setAjaxParam( 'contributorId', _.pluck( this.preselectedContributors, 'id' ) );
            this.participantSearchView.setAjaxParam( 'contributorId', _.pluck( this.preselectedContributors, 'id' ) );
        }

    },
    resetStepsAndData: function() {
        // steps
        this.wizTabs.setTabState( 'stepCoworkers', 'unlocked' );
        this.wizTabs.setTabState( 'stepOthers', 'locked' );
        this.wizTabs.setTabState( 'stepPreview', 'locked' );
        this.wizTabs.activateTab( 'stepCoworkers' );

        // data
        this.contributorsView.model.reset();
    },


    // when a recipient changes, we need to query the server as to what the preselected
    // contributors should be
    handleRecipientChange: function( recip, nodeId ) {
        var that = this;
        console.log( recip );
        console.log( that );
        // this happens only on recipient change (select) from search AND recip nodeId change, so it should
        // blow away existing preselected, from struts or whatever
        if( this.contributorsView /* &&this.contribsPreselected.model.length===0*/ ) {

            // spinner?

            $.ajax( {
                dataType: 'g5json', //must set this so SeverResponse can parse
                url: G5.props.URL_JSON_SEND_RECOGNITION_PRESELECTED_CONTRIBUTORS,
                data: { recipientId: recip.get( 'id' ), nodeId: nodeId },
                type: 'POST',
                success: function( serverResp ) {
                    var err = serverResp.getFirstError(),
                        newList;

                    if( !err ) {
                        //If Server does not send filters, do not render the Team tab.
                        // preset "teams" may change from this ajax JSON
                        if ( serverResp.data.presetSearchFilters && serverResp.data.presetSearchFilters.length === 0 || serverResp.data.presetSearchFilters.filters.length === 0 ) {
                         serverResp.data.presetSearchFilters = null;
                        }

                        // hide or show tab
                        that.hideShowTeamFilter( serverResp.data.presetSearchFilters );

                        // preset "teams" may change from this ajax JSON
                        // TODO: hood this bacck up? tk

                        that.participantSearchFilterView.setFilterPresets( serverResp.data.presetSearchFilters );


                        // remove current 'preselected' contribTypes
                        //Custom for TCCC - WIP-26532 - Checking for Allowed Domains to send mail invite starts -->			
						//removed all contributed types when we select new purl recipient
                        newList = _.reject(that.contributorsView.model.toJSON(), function(c){
                            return c.contribType==='preselected' || c.contribType==='additional' || c.contribType==='other';
                        });

                        // merge old list with new list of 'preselected' contribTypes
                        newList = _.union( serverResp.data.preselectedContributors, newList );

                        that.contributorsView.model.reset( newList );
                        that.updateContribSearchViewParams();
                    } else {
                        // do nothing for now, or maybe
                        console.error( '[ERROR] ContributorsView - error getting preselected contributors for recipientId=' + recip.get( 'id' ) );
                    }

                    // kill spinner?

                }
            } );
        }
    },

    getWizardTabs: function() { return this.wizTabs; }

} );
