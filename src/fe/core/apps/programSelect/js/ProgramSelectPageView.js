/*jslint browser: true, nomen: true, unparam: true*/
/*exported ProgramSelectPageView*/
/*global
PageView,
JSON,

ProgramSelectPageModel,
PaxSelectedPaxView,
PaxSearchStartView,
NominationsSubmitPageView,
RecognitionPageSendView,
TemplateManager,
ProgramSelectPageView:true
*/
ProgramSelectPageView = PageView.extend( {

    //override super-class initialize function
    initialize: function ( opts ) {
        //var self = this;


        // return;
        //set the appname (getTpl() method uses this)
        this.appName = 'programselect';

        // //template names
        // this.tpl = 'programSelectTpl';
        // this.tplPath = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'programSelect/tpl/';

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );
        this.isPurl = null;
        this.calcObj = null;
        this.savedCalcObj = null;
        this.savedCalcRows = [];
        this.calcData = null;
        this.didCalc = false;
        this.levelId = null;
        this.nodeId = opts.idJson.claimId;
        this.levelNumber = opts.idJson.levelNumber;
        this.optsJson = opts.json;
        this.statusSel = opts.idJson.status;
        this.model = new ProgramSelectPageModel();
        this.statusCount = 0;
        this.promoChanged = false;
        this.promotionCtrlChanged = false;
        this.nodeCtrlChanged = false;
        this.promoModel = null;
        this.$dataForm = opts.$dataForm || this.$el.find( '#dataForm' );
        if( this.$dataForm.length === 0 ) {
            console.error( '[ERROR] RecognitionPageSendView - no #dataForm set or found' );
        }
        this.promoInited = false;
        this.recipFormNamePrefix = 'claimRecipientFormBeans';
        this.formSettings = this.parseForm( this.$dataForm );
        this.promotionId = opts.idJson.promotionId;
        if( this.nodeId === null ) {
            this.nodeId = opts.idJson.selectedNodeId;
        }

        this.ids = {
            promotionId: this.promotionId,
            levelNumber: this.levelNumber

        };
        G5.util.showSpin( this.$el, {
            cover: true
        } );
        // this.model.loadApprovalData(this.ids);
        this.model.loadPrograms();

        this.setupEvents();


    },
    test: function() {
        var self = this;
        this.$search = this.$el.find( '#paxSearchStartView' );
        this.$search.data( 'searchUrl', G5.props.SEARCH_URL_DEFAULT );
         //

        // CHECK THAT OPTOUTAWARDS IS A BOOLEAN AND NOT A STRING
        _.each( this.formSettings.recipients, function( recipient ) {
            if( recipient.optOutAwards === 'false' ) {
               recipient.optOutAwards = false;
            }
        } );

        this.paxCollectionView = new PaxSelectedPaxView( {
                el: $( '#PaxSelectedPaxView2' ),
                recognition: false,
                hasSidebar: true,
                //paxCollection: opts.selectedPaxCollection,
                hideControls: true,
                preSelectedParticipants: this.formSettings.recipients,
                dontRenderSelf: false
            } );
        // console.log('this.formSettings.recipients',this.formSettings.recipients);
        // TODO: will need to trigger error on participants in awardee and set prop on collection
        // TODO: make sure our data matches coming back from server so collection model is the same
        // need to pass paramaters all the way through

        this.recipientSearchView = new PaxSearchStartView( {
                el: this.$search,
                multiSelect: true, //this.isSingleSelectMode(),
                selectedPaxCollection: this.paxCollectionView.collection,
                addSelectedPaxView: true

            } );

        this.paxCollectionView.collection.on( 'remove', function() {
            self.validateUsers();
            if( self.isPurl && this.length == 1 ) {
                //

                console.log( 'collection length? ', this.length );
                 self.updatePromotion();
            }

        } );
        this.recipientSearchView.on( 'closedsearch', this.searchClosed, this );
        // moving to search closed
        /*this.paxCollectionView.collection.on('add', function(){
            self.validateUsers();
            self.updatePromotion();
        });*/
        self.updatePromotion();

    },
    events: {
        //promotion events
        'click #nominationChangePromoConfirmBtn': 'changePromotion',
        'click #nominationChangePromoCancelBtn': 'cancelQtip',
        'change #promotionId': 'updatePromotion',
        'change #nodeId': 'updatePromotion',



        // modals
        'click .doViewRules': 'showRulesModal',
        'click .teamAddAward': 'teamAddAwardModal',


        //popovers
        'click .popoverTrigger': 'showGenericPopover',
        'click .budgetIncreaseLink': 'budgetIncreasePopover',
        'click .viewApproverList': 'viewApproversPopover',
        'click .teamMemberTrigger': 'teamMemberPopover',
        'click .profile-popover': 'attachParticipantPopover',
        'click #nominationDoNotCancelBtn': 'cancelQtip',

        //submits and validation

        'click .addAwardBtn': 'submitTeamAward',
        'click #nominationCancelConfirmBtn': 'doCancelApproval',
        'click .showActivityBtn': 'submitFilterForm',
        'click .sendBudgetRequest': 'submitBudgetRequest',
        'click .submitApproval': 'submitApproval',
        'click .updateBtn': 'reloadOrGoBack',


        //testing button
        'click .makeAllWinners': 'allWinners'
    },

    searchClosed: function() {
        //console.log('search closed????');
        this.validateUsers();
        this.updatePromotion();
    },
    validateUsers: function() {
        var self = this;
        self.ineligible = 0;

        _.each( this.paxCollectionView.collection.models, function( parti ) {
            if( parti.get( 'invalid' ) === true ) {

                self.ineligible += 1;

            }

        } );

        if( self.ineligible > 0 ) {
            $( '#invalidRecips' ).show();
            $( '#errorAbove' ).show();
            if( self.ineligible == this.paxCollectionView.collection.models.length ) {
                _.delay( function() {
                    $( '.emptyRow' ).addClass( 'validateme' );
                    $( '.emptyRow' ).data( 'validate-flags', 'nonempty' );
                }, 500 );
            }
        } else {
            $( '#invalidRecips' ).hide();
            $( '#errorAbove' ).hide();
        }
    },
    allWinners: function() {
        var statuses = $( '.statusSelect' );
        _.each( statuses, function( status ) {

            $( status ).val( 'winner' ).change();
        } );
    },
    setupEvents: function() {
        //var that = this;

        this.model.on( 'approvalDataLoaded', this.render, this );

    },
    hideExpCol: function() {
        this.$el.find( '.nominationTableControls' ).hide();
    },
    checkForMobile: function() {
        var $doneBtn = this.$el.find( '.submitApproval' );//,
            //$filterBtn = this.$el.find('.showActivityBtn'),
            //$cont = _.clone(this.$el.find('.mobileDetectTooltip'));

        if( screen.width <= 450 ) {

            this.$el.find( '.mobileDetectTooltip' ).show();
            $doneBtn.attr( 'disabled', 'disabled' );
        }
    },

    render: function() {
        var $promotionListContainer = this.$el.find( '.promotionList' ),
            //promo = this.model.get('promotion'),
            //levels,
            self = this,
            progs = this.model.get( 'programs' ),
            $orgUnitListContainer = this.$el.find( '.nominationOrgSection' ),
            nodes = this.model.get( 'nodes' ),
			raEnable = this.model.get( 'raEnable' );


            progs.totalPromotionCount = progs.length;
            _.each( nodes, function( node ) {
                if( self.nodeId == node.id ) {
                    node.selected = true;
                }
            } );
            _.each( progs, function( program ) {
                if( self.promotionId == program.promoId ) {
                    program.selected = true;
                }
            } );

            // show the org unit only when nodes is present
            if ( nodes !== undefined ) {
                TemplateManager.get( 'orgUnitList', function( tpl ) {
                    self.$el.find( '.orgUnitList' ).remove();
                    $orgUnitListContainer.empty().append( tpl( nodes ) );

                } );
            }

            TemplateManager.get( 'promotionName', function( tpl ) {

               // $promotionListContainer.find('.promoWrapper').hide();
               $promotionListContainer.find( '.promoChangeWrapper' ).remove();
               $promotionListContainer.append( tpl( progs ) );
               $promotionListContainer.find( '.promoChangeWrapper' ).hide();


            //    if($stepContentWrapper.children(':visible').length === 0){
            //        $stepContentWrapper.empty().append($tabContent);
            //    }




           } );
            TemplateManager.get( 'promotionsList', function( tpl ) {
                var promoID = self.$el.find( '#promotionId option:selected' ).val();

                $promotionListContainer.find( '.promoWrapper' ).remove();
                $promotionListContainer.find( '.promoChangeWrapper' ).hide();
                $promotionListContainer.append( tpl( progs ) );

				self.setUpByRAPromoTip();
				
                if( promoID ) {
                  _.each( $promotionListContainer.find( '#promotionId option' ), function( promoIdOpt ) {
                      if( $( promoIdOpt ).val() == promoID ) {
                          $( promoIdOpt ).prop( 'selected', true );
                      }
                  } );
                }

                if( self.promotionId ) {
                  console.log( 'update' );
                  self.updatePromotion();
                }
				
				if( raEnable ) {
					self.$el.find( '.insightSection' ).show();
					self.$el.find( '.raPromo' ).show();
				}

                // self.test();
            } );

            if( !this.recipientSearchView ) {
                this.test();
            }

        G5.util.hideSpin( this.$el );
    },
    parseForm: function( $f ) {
        var that = this,
            arry = $f.serializeArray(),
            json = {};

        //put the form inputs in an object (filter out array obj elements)
        _.each( arry, function( input ) {
            if( input.name.match( /\[(\d+)\]/ ) === null ) {
                json[ input.name ] = input.value;
                // any values that need to be converted to JSON
                if( input.name === 'contributorTeamsSearchFilters' ) {
                    if( $.trim( json[ input.name ] ) ) { // has data
                        json[ input.name ] = JSON.parse( json[ input.name ] );
                    } else { // no data, let's just pretend like there wasn't an input for this
                        delete json[ input.name ];
                    }
                }
            }
        } );

        json.recipients = this.parseObjectsFromForm( arry, that.recipFormNamePrefix );
        // json.contributors = this.parseObjectsFromForm(arry, that.contribFormNamePrefix);

        return json;
    },
    // FORM PARSE: return json array of recipients
    parseObjectsFromForm: function( formArray, prefix ) {
        var map = {}, //map pax id to props
            arry,
            count = -1; //recip count

        _.each( formArray, function( inp ) {
            var brkt, num, key;

            if( inp.name.indexOf( prefix ) > -1 ) {
                //extract count
                if( inp.name == prefix + 'Count' ) {
                    count = parseInt( inp.value, 10 );
                }
                //extract data
                else {
                    brkt = inp.name.match( /\[(\d+)\]/ ); //just the [#] part
                    num = brkt.length === 2 ? parseInt( brkt[ 1 ], 10 ) : false; //get the number
                    key = inp.name.match( /\]\.(.+)$/ )[ 1 ]; //get the key

                    if( !map[ 'recip' + num ] ) {map[ 'recip' + num ] = {};}//not set? set it
                    //map['recip'+num]?true:map['recip'+num]={};//not set? set it

                    map[ 'recip' + num ][ key ] = inp.value;
                    if( key === 'nodes' ) {
                        // special case for the nodes -- they are a string to be converted to JSON
                        map[ 'recip' + num ][ key ] = JSON.parse( map[ 'recip' + num ][ key ] );
                    }
                }
            }
        } );

        arry = _.toArray( map ); //turn it into an array
        if( arry.length !== count ) {
            console.error( '[ERROR] RecognitionPageSendView - dataForm [' + prefix + '] counts do not match ' + arry.length + ' != ' + count );
        }

        console.log( '[INFO] RecognitionPageSendView - DATA FORM - found ' + arry.length + ' pax of type [' + prefix + ']' );

        return  arry;
    },
    // test:function(){
    //     this.$search = this.$el.find('.paxSearchStartView');
    //
    //      //
    //     this.paxCollection = new PaxSelectedPaxView({
    //             el:$('#PaxSelectedPaxView2'),
    //             recognition:false,
    //             hasSidebar:true,
    //             //paxCollection: opts.selectedPaxCollection,
    //             hideControls:true,
    //             preSelectedParticipants: this.participantModel,
    //             dontRenderSelf: false
    //         });
    //     // console.log('this.formSettings.recipients',this.formSettings.recipients);
    //     // TODO: will need to trigger error on participants in awardee and set prop on collection
    //     // TODO: make sure our data matches coming back from server so collection model is the same
    //     // need to pass paramaters all the way through
    //     console.log(this.$search);
    //     this.recipientSearchView = new PaxSearchStartView({
    //             el: this.$search,
    //             multiSelect:true,//this.isSingleSelectMode(),
    //             selectedPaxCollection: this.paxCollectionView.collection,
    //             addSelectedPaxView:true,
    //
    //         });
    //
    //     console.log(this);
    //
    //             this.recipientView = new NominationCollectionView({
    //                 el : this.$el.find('#recipientsView'),
    //                 tplName :'participantRowAwardItem', //override the default template with this inline tpl
    //                 model : this.paxCollectionView.collection,
    //                 parentView: this,
    //                 selectedPromoModel: this.selectedPromoModel
    //             });
    //
    //
    // },
    rerender: function() {
        var $promotionListContainer = this.$el.find( '.promotionList' ),
            //promo = this.model.get('promotion'),
            //levels,
            progs = this.model.get( 'programs' );

            TemplateManager.get( 'promotionsList', function( tpl ) {

                $promotionListContainer.append( tpl( progs ) );


            } );

        G5.util.hideSpin( this.$el );
    },

    // *******************
    // PROMOTION SETUP
    // *******************
    updatePromotion: function(e) {
        var $tar = e ? $( e.target ) : null ;     
        console.log( 'update promotion' );
        var self = this,
            $promoVal = this.$el.find( '#promotionId option:selected' ).val(),
            $promoName = this.$el.find( '#promotionId option:selected' ).text(),
            isPurl = this.$el.find( '#promotionId option:selected' ).data( 'purl' ),
            $nodeId = this.$el.find( '#nodeId option:selected' ).val();

            self.ineligible = 0;

           var recogsView = self.sendRecognitionView || self.nominationSearchView ? true : false;
            
        if( $promoVal ) {
            if( isPurl ) {
                this.isPurl = true;
            }else{
                this.isPurl = null;
            }
        }
        console.log( $nodeId );

        self.promoChanged = true;

        if ( $tar && $tar.attr('id') == 'promotionId' ) {
          self.promotionCtrlChanged = true;  
        }
        else {
            self.promotionCtrlChanged = false;
        }  

        if ( $tar && $tar.attr( 'id' ) == 'nodeId' ) {
            self.nodeCtrlChanged = true;  
        }
        else {
            self.nodeCtrlChanged = false;
        }

        if( $promoVal ) {
            this.$el.find( '.promoChangeWrapper' ).show();
            this.$el.find( '.promoWrapper' ).hide();
            _.delay( function() {$( '.nominationPromotionName' ).text( $promoName );}, 100 );
        }

        self.recipientSearchView.setParticipantSelectModeSingle( true );
        console.log( self.recipientSearchView.selectedPaxCollection );
        if( $promoVal && self.recipientSearchView.selectedPaxCollection.models.length > 1 ) {
            if( isPurl ) {
                console.log( 'pickOne' );
                self.okToRender = false;
                self.recipientSearchView.setParticipantSelectModeSingle( false );
            }
            else if( recogsView && self.promotionCtrlChanged ) {
                self.okToRender = true;
            }
            else if( recogsView && !self.promotionCtrlChanged) {
                self.okToRender = false;
            }           
            else {
                self.okToRender = true;
            }
        } else if( $promoVal && self.recipientSearchView.selectedPaxCollection.models.length == 1 ) { 

            if( recogsView && ( self.promotionCtrlChanged || self.nodeCtrlChanged ) ) {
                self.okToRender = true;
            }
            else if( recogsView && !self.promotionCtrlChanged && !isPurl) {
                self.okToRender = false;
            }           
            else {
                self.okToRender = true;
            }          
            
            if( isPurl ) {
                self.recipientSearchView.setParticipantSelectModeSingle( false );
            }            
        }        
        else {
            self.okToRender = true;
        }
        var userIds = '';
        _.each( self.recipientSearchView.selectedPaxCollection.models, function( pax ) {
            if( userIds.length === 0 ) {
                userIds = pax.id;
            } else {
                userIds += ',' + pax.id;
            }
            console.log( pax.id );
        } );
        var promotionAndUsers = {
            promotionId: $promoVal,
            users: userIds
        };
        //TODO: fix and move this TOM!!1
        console.log( promotionAndUsers );


        G5.props.NOMINATION_SUBMIT_PAGE_URL = './../apps/nominations/tpl/nominationsSubmitPage.html';
        if( $nodeId ) {

            this.nodeId = $nodeId;

        }
        if( $promoVal ) {
            this.recipientSearchView.setSearchUrl( G5.props.SEARCH_URL_PROMO );
        }
        if( $nodeId && $promoVal ) {
            this.promoId = $promoVal;
            // setting these so search is always correct, when you have no one in the drawer
            this.recipientSearchView.clearAjaxParam();
            this.recipientSearchView.setAjaxParam( 'promotionId', this.promoId );
            this.recipientSearchView.setAjaxParam( 'nodeId', this.nodeId );
            $.ajax( {
                dataType: 'g5json',
                type: 'POST',
                url: G5.props.VALID_USERS,
                data: promotionAndUsers,
                success: function ( serverResp ) {

                   var participants = serverResp.data.participants;

                   _.each( self.paxCollectionView.collection.models, function( pax ) {
                       _.each( participants, function( parti ) {
                           if( pax.id == parti.id ) {

                               pax.set( 'invalid', !parti.eligibleForPromotion );
                               pax.set( 'countryRatio', parti.countryRatio );
                           }
                    } );

                   } );
                   console.log( self.paxCollectionView.collection.models );
                   if( self.paxCollectionView.collection.models.length > 0 ) {
                       self.validateUsers();
                       self.loadPromotion();
                   }
                },
                error: function() {

                },
                complete: function() {

                }
            } );
        }
                _.delay( function() {self.showInstructions();}, 1000 );
    },
    loadPromotion: function() {
        var self = this,
        data = {
            promotionId: this.promoId,
            claimId: this.nodeId,
            levelNumber: 1,
            levelId: null
        };

        // CLEAR OUT THE DIV EVEN IF WE ARE NOT RENDERING NEW CONTENT
        if ( this.okToRender || this.isPurl ) { 
            $( '#programContainer' ).empty(); 
        } 

        if( this.okToRender ) {
            $( '#purlTooManyRecips' ).hide();
            $( '#errorAbove' ).hide();
            $( '.paxSearchInput' ).attr( 'disabled', false );


            if( this.nominationSearchView ) {
                this.nominationSearchView.undelegateEvents();
                this.nominationSearchView.remove();
            }
            if( this.sendRecognitionView ) {
                this.sendRecognitionView.undelegateEvents();
                this.sendRecognitionView.remove();
            }






            this.recipientSearchView.clearAjaxParam();
            this.recipientSearchView.setAjaxParam( 'promotionId', this.promoId );
            this.recipientSearchView.setAjaxParam( 'nodeId', this.nodeId );


            console.log( data );

            if( this.$el.find( '#promotionId option:selected' ).data( 'type' ) == 'nom' ) {
                data = {
                    promotionId: this.promoId,
                    nodeId: this.nodeId,
                    claimId: this.options.idJson.claimId
                };
                this.nomsTpl = 'nominationsSubmitPage';
                TemplateManager.get( this.nomsTpl, function( tpl ) {

                    $( '#programContainer' ).append( tpl() );
                    self.nominationSearchView = new NominationsSubmitPageView( {
                        el: '#nominationsSubmitPageView',
                        paxCollection: self.paxCollectionView.collection,
                        recipView: self.recipientSearchView,
                        idJson: data
                    } );

                }, '../apps/nominations/tpl/' );

                // this.$el.find('#programContainer').load(G5.props.NOMINATION_SUBMIT_PAGE_URL , data);
                // this.programView = new NominationsSubmitPageView({
                //     idJson: idJson
                // });
                // console.log(this.programView);
                // this.$el.find('#programContainer').append(this.programView.render().el);
            } else {
                this.recTpl = 'recognitionPageSend';
                TemplateManager.get( this.recTpl, function( tpl ) {

                    $( '#programContainer' ).append( tpl( this ) );

                    self.sendRecognitionView = new RecognitionPageSendView( {
                        el: $( '#recognitionPageSendView' ),
                        $dataForm: $( '#dataForm' ), //pass a reference to the 'data' form (struts populated)
     //form setup data
                        pageNav: {
                            back: {
                                text: 'Back',
                                url: 'layout.html'
                            },
                            home: {
                                text: 'Home',
                                url: 'layout.html?tplPath=base/tpl/&amp;tpl=modulesPage.html'
                            }
                        },
                        pageTitle: 'Send a Recognition/PURL',
                        cancelUrl: 'layout.html',
                        paxCollection: self.paxCollectionView,
                        recipView: self.recipientSearchView,
                        idJson: data,
                        json: self.optsJson
                    } );


                }, '../apps/recognition/tpl/' );
                // self.programView = new RecognitionPageSendView({
                // });
            }

            console.log( this );
        }
        else if ( !this.okToRender && !self.promotionCtrlChanged ) {
                $( '#purlTooManyRecips' ).hide();
                $( '#errorAbove' ).hide();
                $( '.paxSearchInput' ).attr( 'disabled', false );
        } 
        else {           
                $( '#purlTooManyRecips' ).show();
                $( '#errorAbove' ).show();
                $( '.paxSearchInput' ).attr( 'disabled', 'disabled' );            
           
        }

    },
    changePromotion: function() {
        var $promotionPopover = this.$el.find( '.promoChangePopover' );

        $promotionPopover.closest( '.qtip' ).qtip( 'hide' );
        this.promotionId = null;
        this.render();
    },

    renderPromoDropdown: function() {
        var $promotionListContainer = this.$el.find( '.promotionList' ),
            noms = this.model.get( 'nominations' ),
            self = this;

        TemplateManager.get( 'promotionsList', function( tpl ) {
            $promotionListContainer.find( '.promoChangeWrapper' ).hide();
            $promotionListContainer.append( tpl( noms ) );

            _.each( $promotionListContainer.find( '#promotionId option' ), function( promoIdOpt ) {
                if( parseInt( $( promoIdOpt ).val(), 10 ) === self.promotionId ) {
                    $( promoIdOpt ).attr( 'selected', 'selected' );
                }
            } );
        } );
    },

    showRulesModal: function( e ) {
        var $rulesModal = $( 'body' ).find( '#rulesModal' ),
            $cont = $rulesModal.find( '.modal-body' ).empty();

        e.preventDefault();

        $cont.append( this.model.get( 'promotion' ).rulesText || 'ERROR, modal found no rulesText on promo' );

        $rulesModal.modal();
    },

    teamAddAwardModal: function( e ) {
        var $tar = $( e.target ),
            id = $tar.data( 'teamId' ),
            $awardModal = $( 'body' ).find( '#addAwardModal' );


        if( $tar.hasClass( 'disabled' ) ) {
            return false;
        }

        this.renderTeamAward( id );

        $awardModal.modal();
    },

    // *******************
    // POPOVERS
    // *******************
    showGenericPopover: function( e ) {
        var $tar = $( e.target ),
            trigData = $tar.data( 'popoverContent' ),
            $cont = this.$el.find( '.' + trigData + 'Popover' ).first().clone();

        if( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, $cont, this.$el );
        }
    },
    // show instruction if user presented with blank form (just promo/node)
    showInstructions: function() {
        console.log( this );
        if( !this.$el.find( '#promotionId' ).val() ) {
            this.addInstructionTip( this.$el.find( '#promotionId' ), this.$el.find( '#promotionId' ).data( 'msgInstructions' ),
                this.$el.find( '.promoWrapper' ) );
        }
        console.log( this.$el.find( '#nodeId' ) );

        if( !this.$el.find( '#nodeId' ).val() && this.$el.find( '#nodeId' ).is( ':visible' ) ) {
            console.log( 'node' );
            console.log( this.$el.find( '#nodeId' ).data( 'msgInstructions' ) );
            this.addInstructionTip( this.$el.find( '#nodeId' ), this.$el.find( '#nodeId' ).data( 'msgInstructions' ),
                this.$el.find( '.orgUnitWrapper' ) );
        }
    },
    budgetIncreasePopover: function( e ) {
        var $tar = $( e.target ),
            $cont = _.clone( this.$el.find( '.budgetIncreasePopover' ) );

        if( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, $cont, this.$el );
        }
    },

    attachParticipantPopover: function( e ) {
        var $tar = $( e.target ),
            isSheet = { isSheet: true }; //isSheet needs to be true for popover to show over team award modal.
        isSheet.containerEl = this.$el;
        if( !$tar.data( 'participantPopover' ) ) {
            $tar.participantPopover( isSheet ).qtip( 'show' );
        }

        e.preventDefault();
    },

    viewApproversPopover: function( e ) {
        var $tar = $( e.target ),
            $cont = _.clone( $tar.siblings( '.approverListTooltip' ) ); //special for approver list

        e.preventDefault();

        if( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, $cont, this.$el );
        }
    },

    teamMemberPopover: function( e ) {
        var $tar = $( e.target ),
            $cont = _.clone( $tar.parents( 'td' ).find( '.teamMembersTooltip' ) ); //special for team members list

        e.preventDefault();

        if( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, $cont, this.$el, 'approvalsTeamTooltip' );
        }
    },

    attachPopover: function( $trig, content, container, extraClass ) {
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
                classes: 'ui-tooltip-shadow ui-tooltip-light ' + extraClass,
                tip: {
                    corner: true,
                    width: 20,
                    height: 10
                }
            }
        } );
    },
    //add instruction tooltip
    addInstructionTip: function( $trig, cont, $container ) {
        //attach qtip and show
        console.log( $trig, cont, $container );
        $trig.qtip( {
            content: { text: cont },
            position: {
                my: 'left center',
                at: 'right center',
                container: $container,
                viewport: $( 'body' ),
                adjust: { method: 'shift none' }
            },
            show: {
                event: false,
                ready: true,
                effect: false
            },
            hide: {
                event: 'click',
                fixed: true,
                delay: 200
            },
            style: {
                classes: 'ui-tooltip-shadow ui-tooltip-light ui-tooltip-instruction',
                tip: {
                    corner: true,
                    width: 20,
                    height: 10
                }
            },
            events: { // for narrow screens the tooltip will be disp. below (doesn't update on resize)
                visible: function( event, api ) {
                    var overlap = false,
                        $tar = api.elements.target,
                        $tip = api.elements.tooltip;

                    overlap = $tar.offset().left + $tar.width() - 10 > $tip.offset().left;
                    if( overlap ) {
                        api.set( {
                            'position.at': 'bottom center',
                            'position.my': 'top center'
                        } );
                    }
                }
            }
        } );
    },
    // display a qtip for next buttons or tab clicks (uses class name to show proper error)
    onNavErrorTip: function( msgClass, $target, isDestroyOnly, errorMsg ) {
		//Bug id 77595: Removing class pin-body to make the popup error float near the field
		if(msgClass) {
			$( 'body' ).removeClass( 'pin-body' );
		}
        var $cont = this.$el.find( '.errorTipWrapper .errorTip' ).clone(),
            isBtn = $target.hasClass( 'btn' ) || $target.is( 'input' );

        //if old qtip still visible, obliterate!
        if( $target.data( 'qtip_error' ) ) { $target.data( 'qtip_error' ).destroy(); }

        if( isDestroyOnly ) {
            return;
        }

        $cont.find( '.' + msgClass ).show(); // show our message
        if( errorMsg ) {
            $cont.find( '#errorText' ).text( errorMsg );
        }
        //attach qtip and show
        $target.qtip( {
            content: { text: $cont },
            position: {
                my: isBtn ? 'bottom center' : 'top center',
                at: isBtn ? 'top center' : 'bottom center',
                effect: this.isIe7OrIe8 ? false : true,
                viewport: $( 'body' ),
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

    showSameForAllTip: function( e ) {
        var $tar = $( e.target ),
            $table = this.$el.find( '#nominationApprovalTable' );

        // NO TIP: single pending product
        if( $table.find( 'tbody tr.paxRow' ).length <= 1 ) { return; }

        //error tip active? then do nothing (just let the error have the stage)
        if( $tar.closest( '.validateme' ).hasClass( 'error' ) && $tar.qtip().elements.tooltip.is( ':visible' ) ) {
            return;
        }

        //have it already?
        if( $tar.data( 'qtip_sfa' ) ) {
            $tar.data( 'qtip_sfa' ).show();
            setTimeout( function() {
                $tar.data( 'qtip_sfa' ).reposition();
            }, 1000 );
            return;
        }

        $tar.qtip( {
            content: this.$el.find( '#sameForAllTipTpl' ).clone().removeAttr( 'id' ),
            position: {
                my: 'left center',
                at: 'right center',
                container: $table,
                viewport: $( 'body' )
            },
            show: { ready: true, event: false },
            hide: {
                event: 'unfocus click',
                fixed: true,
                delay: 200
            },
            //only show the qtip once
            events: { hide: function() {$tar.qtip( 'destroy' );} },
            style: {
                classes: 'ui-tooltip-shadow ui-tooltip-light',
                tip: {
                    width: 10,
                    height: 5
                }
            }
        } );

        $tar.data( 'qtip_sfa', $tar.data( 'qtip' ) ).removeData( 'qtip' );
    },

    doSameForAllClick: function( e ) {
        var $tar = $( e.target ),
            // find the active input: the link is in a tooltip with a qtip
            // which has a reference to the triggering element (target)
            $actInp = $tar.closest( '.ui-tooltip' ).data( 'qtip' ).elements.target,
            $allInp = this.$el.find( '#nominationApprovalTable .date:not([disabled])' ),
            inpVal = $actInp.val();

        e.preventDefault();

        $allInp.val( inpVal );

        this.$el.find( '#sameForAllTipTpl' ).closest( '.qtip' ).qtip( 'hide' );
    },

    cancelQtip: function( e ) {
        e.preventDefault();

        $( e.currentTarget ).closest( '.qtip' ).qtip( 'hide' );
    },

    // *******************
    // UI STUFF
    // *******************
    responsiveTable: function () {
        var tableData = this.model.get( 'tabularData' ),
            colSpanCount = tableData.meta.columns.length,
            id = 0;

        this.$el.find( '.detailRow td' ).attr( 'colspan', colSpanCount );

        _.each( this.$el.find( '.table tr' ), function( row ) {
            id++;

            $( row ).attr( 'data-row-id', id );
        } );

        this.$el.find( '#nominationApprovalTable tbody tr:visible:odd' ).addClass( 'odd' );

        this.$el.find( '#nominationApprovalTable tr.detailRow:odd' ).addClass( 'odd' );
    },

    changeStatus: function( e ) {
        var self = this,
            $tar = $( e.target ),
            $status = $tar.find( 'option:selected' ).val();
            $tar.addClass( 'changed' );

            if( self.statusCount >= 100 ) {
              console.log( 'popup modal' );
              var $statusModal = $( 'body' ).find( '#statusModal' );
              e.preventDefault();
              $statusModal.modal();
            } else {
              self.statusCount++;
              console.log( self.statusCount );

              switch( $status ) {
                  case 'winner':
                      $tar.siblings( '.attachMessageCont' ).hide();
                      $tar.siblings( '.winnerMessage' ).show();

                      $tar.parents( 'tr' ).find( '#winnerMessage' ).removeAttr( 'disabled' );
                      $tar.parents( 'tr' ).find( '.timePeriod, button, .date' ).removeAttr( 'disabled' );
                      $tar.parents( 'tr' ).find( 'input:not(.teamAwardAmount)' ).removeAttr( 'readonly' );
                      $tar.parents( 'tr' ).find( 'input:not(.teamAwardAmount)' ).removeAttr( 'disabled' );
                      $tar.parents( 'tr' ).find( 'textarea' ).removeAttr( 'disabled' );
                      $tar.parents( 'tr' ).find( '.teamAddAward' ).removeClass( 'disabled' );
                      $tar.parents( 'tr' ).find( '.awardPointsInp' ).parents( 'td' ).addClass( 'validateme' );

                      $tar.parents( 'tr' ).find( '.timePeriod' ).parents( 'td' ).addClass( 'validateme' );
                    break;
                  case 'approv':
                      $tar.siblings( '.attachMessageCont' ).hide();

                      $tar.parents( 'tr' ).find( '#winnerMessage' ).removeAttr( 'disabled' );
                      $tar.parents( 'tr' ).find( '.timePeriod, button' ).removeAttr( 'disabled' );
                      $tar.parents( 'tr' ).find( 'input' ).removeAttr( 'readonly' );
                      $tar.parents( 'tr' ).find( 'input:not(.teamAwardAmount)' ).removeAttr( 'disabled' );
                      $tar.parents( 'tr' ).find( '.teamAddAward' ).removeClass( 'disabled' );

                      break;
                  case 'non_winner':
                      $tar.siblings( '.attachMessageCont' ).hide();
                      $tar.siblings( '.nonwinnerMessage' ).show();

                      $tar.parents( 'tr' ).find( '#deinalReason' ).removeAttr( 'disabled' );
                     $tar.parents( 'tr' ).find( '.timePeriod, button' ).attr( 'disabled', 'disabled' );
                      $tar.parents( 'tr' ).find( 'input' ).attr( 'readonly', 'readonly' );
                      $tar.parents( 'tr' ).find( 'input:not(.teamAwardAmount)' ).removeAttr( 'disabled' );
                      $tar.parents( 'tr' ).find( 'input.compare' ).attr( 'readonly', false );
                      // $tar.parents('tr').find('.awardInput, input.date, .timePeriod').val('');
                      $tar.parents( 'tr' ).find( '.teamAddAward' ).addClass( 'disabled' );
                      $tar.parents( 'tr' ).find( '.awardPointsInp' ).parents( 'td' ).removeClass( 'validateme' );

                      break;
                  case 'more_info':
                      $tar.siblings( '.attachMessageCont' ).hide();
                      $tar.siblings( '.moreinfoMessage' ).show();

                      $tar.parents( 'tr' ).find( '#moreinfoMessage' ).removeAttr( 'disabled' );
                      $tar.parents( 'tr' ).find( '.timePeriod, button' ).attr( 'disabled', 'disabled' );
                      $tar.parents( 'tr' ).find( 'input' ).attr( 'readonly', 'readonly' );
                      $tar.parents( 'tr' ).find( 'input:not(.teamAwardAmount)' ).removeAttr( 'disabled' );
                      $tar.parents( 'tr' ).find( 'input.compare' ).attr( 'readonly', false );
                      // $tar.parents('tr').find('.awardInput, input.date, .timePeriod').val('');
                      $tar.parents( 'tr' ).find( '.teamAddAward' ).addClass( 'disabled' );
                      $tar.parents( 'tr' ).find( '.awardPointsInp' ).parents( 'td' ).removeClass( 'validateme' );

                      break;

                  default:
                      $tar.siblings( '.attachMessageCont' ).hide();

                      $tar.parents( 'tr' ).find( '.timePeriod, button' ).attr( 'disabled', 'disabled' );
                      $tar.parents( 'tr' ).find( 'input' ).attr( 'readonly', 'readonly' );
                      $tar.parents( 'tr' ).find( 'input.compare' ).attr( 'readonly', false );
                      $tar.parents( 'tr' ).find( '.teamAddAward' ).addClass( 'disabled' );
                      $tar.parents( 'tr' ).find( '.awardPointsInp' ).parents( 'td' ).removeClass( 'validateme' );
                      $tar.parents( 'tr' ).find( '.timePeriod' ).parents( 'td' ).removeClass( 'validateme' );
                      $tar.parents( 'tr' ).find( '.awardPointsInp' ).parents( 'td' ).removeClass( 'error' );
                      $tar.parents( 'tr' ).find( '.timePeriod' ).parents( 'td' ).removeClass( 'error' );
                      $tar.parents( 'tr' ).find( '.qtip' ).hide();
                      $tar.parents( 'tr' ).find( '#moreInfoMessage' ).attr( 'disabled', 'disabled' );
                      $tar.parents( 'tr' ).find( '#deinalReason' ).attr( 'disabled', 'disabled' );
                      $tar.parents( 'tr' ).find( '#winnerMessage' ).attr( 'disabled', 'disabled' );
              }
          }
    },

    filterStatusSelect: function( e ) {
        var $tar = $( e.target ),
            promo = this.model.get( 'promotion' ),
            $statusSelect = this.$el.find( '#statusFilter' ),
            $defaultTimePeriod = $tar.find( 'option:selected' ).hasClass( 'defaultOption' );

        if( !$defaultTimePeriod ) {
            if( promo.finalLevelApprover || promo.payoutAtEachLevel ) {
                $statusSelect.find( 'option:not([value="winner"])' ).attr( { 'disabled': true, 'selected': false } );

                $statusSelect.find( 'option[value="winner"]' ).attr( 'selected', 'selected' );
            } else {
                 $statusSelect.find( 'option:not([value="approv"])' ).attr( { 'disabled': true, 'selected': false } );

                $statusSelect.find( 'option[value="approv"]' ).attr( 'selected', 'selected' );
            }
        } else {
            $statusSelect.find( 'option' ).attr( 'disabled', false );
        }
    },

    setStateLoading: function( mode ) {
        var spinProps = {};

        if( mode ) {
            spinProps.classes = mode;
        }

        G5.util.showSpin( this.$el, spinProps );
    },

    // *******************
    // TABLE EVENTS
    // *******************
    sortModal: function( e ) {
        var $tar = $( e.target ).closest( '.sortable' );

            this.$el.find( '#sortModal' ).modal( 'show' );

            $tar.addClass( 'sortIt' );

    },
    handleTableSort: function() {
        var $tar = this.$el.find( '.sortIt' ),
            sortOn = $tar.data( 'sortOn' ),
            sortBy = $tar.data( 'sortBy' ),
            $status = this.$el.find( '#statusFilter option:selected' ).val();

            console.log( 'sort!' );

        $tar.removeClass( 'sortIt' );
        G5.util.showSpin( this.$el, {
            cover: true
        } );

        this.model.loadApprovalTable( {
            promotionId: this.promotionId,
            nodeId: this.nodeId,
            levelId: this.levelId,
            statusFilter: $status,
            currentPage: this.model.get( 'tabularData' ).currentPage,
            sortedOn: sortOn,
            sortedBy: sortBy,
            sorting: true
        } );
    },

    getCumulativeDetails: function( e ) {
        var $form = this.$el.find( '#approvalsSearchForm' ),
            $tar = $( e.currentTarget ),
            formData = $form.serializeArray(),
            data,
            paxId = $tar.parents( 'td' ).data( 'paxid' ),
            groupClaimId = $tar.parents( 'td' ).data( 'groupclaimid' ),
            arrData = {};

        e.preventDefault();
        groupClaimId = parseInt( groupClaimId, 10 );
        _.each( formData, function( el ) {
            var value = _.values( el );

            if( value[ 0 ] == 'statusFilter' ) {

                if( !arrData.hasOwnProperty( value[ 0 ] ) ) {
                    arrData[ value[ 0 ] ] = [];
                    arrData[ value[ 0 ] ].push( value[ 1 ] );

                } else {

                    arrData[ value[ 0 ] ].push( value[ 1 ] );
                }
            } else {
                arrData[ value[ 0 ] ] = value[ 1 ];
            }
        } );

        data = {
            promotionId: this.promotionId,
            nodeId: this.nodeId,
            levelId: this.levelId,
            paxId: paxId,
            claimGroupId: groupClaimId
        };

        this.model.loadCumulativeInfo( $.extend( {}, data, arrData ) );
    },

    expandCollapseDetails: function( e ) {
        var $tar = $( e.currentTarget ),
            $detailRow = $tar.parents( 'tr' ).next( '.detailRow' );

        $detailRow.toggle();

        $tar.addClass( 'hide' );
        $tar.siblings( 'i' ).removeClass( 'hide' );

    },

    expandCollapseAll: function( e ) {
        var $detailRows = this.$el.find( '.detailRow' ),
            isCollapsed = $( e.target ).hasClass( 'collapsed' ),
            $expIcon = this.$el.find( '.expandIcon' ),
            $collIcon = this.$el.find( '.collapseIcon' );

        if( isCollapsed === true ) {
            $detailRows.each( function() {
                if( $( this ).is( ':visible' ) === false ) {
                    $( this ).toggle();
                }
            } );

            $( e.target ).removeClass( 'collapsed' );
            $expIcon.addClass( 'hide' );
            $collIcon.removeClass( 'hide' );

        } else {
            $detailRows.each( function() {
                if( $( this ).is( ':visible' ) === true ) {
                    $( this ).toggle();
                }
            } );

            $( e.target ).addClass( 'collapsed' );
            $collIcon.addClass( 'hide' );
            $expIcon.removeClass( 'hide' );
        }

        this.$el.find( '.cycle' ).parents( '.detailCarousel.active' ).css( 'height', this.$el.find( '.cycle .item' ).first().height() );
    },

    viewMoreTableRows: function() {
        var isMore = true;
        var tabularData = this.model.get( 'tabularData' );
        if( this.model.get( 'allData' ) ) {
          var allData = this.model.get( 'allData' );
          _.each( tabularData.results, function( data ) {
            allData.results.push( data );
          } );
          this.model.set( 'allData', allData );
        } else {
          this.model.set( 'allData', tabularData );
        }
        var $form = this.$el.find( '#approvalsSearchForm' ),
            formData = $form.serializeArray(),
            data,
            arrData = {};


        console.log( formData );
        this.$el.find( '.compareNomineeBlock' ).remove();

        _.each( formData, function( el ) {
            var value = _.values( el );
            if( value[ 0 ] == 'statusFilter' ) {

                if( !arrData.hasOwnProperty( value[ 0 ] ) ) {
                    arrData[ value[ 0 ] ] = [];
                    arrData[ value[ 0 ] ].push( value[ 1 ] );

                } else {

                    arrData[ value[ 0 ] ].push( value[ 1 ] );
                }
            } else {
                arrData[ value[ 0 ] ] = value[ 1 ];
            }
        } );
        var status = arrData[ 'statusFilter' ];
        arrData[ 'statusFilter' ] = '';

        _.each( status, function( value, index ) {

            if( index < status.length - 1 ) {
                arrData[ 'statusFilter' ] += value + ',';

            } else if( index == status.length - 1 ) {
                arrData[ 'statusFilter' ] += value;
            }
        } );

        data = {
            promotionId: this.promotionId,
            nodeId: this.nodeId,
            sortedOn: this.model.get( 'tabularData' ).sortedOn,
            sortedBy: this.model.get( 'tabularData' ).sortedBy,
            currentPage: this.model.get( 'tabularData' ).currentPage,
            isMore: isMore,
            levelId: this.levelId,
            statusFilter: this.statusSel

        };
        this.model.loadApprovalTable( $.extend( {}, data, arrData ) );

    },

    doPointsFocus: function( e ) {
        var $tar = $( e.currentTarget );

        if( $tar.val() === '0' || $tar.val() === 0 ) {
            setTimeout( function() {

                if ( $tar[ 0 ].setSelectionRange ) {
                    $tar[ 0 ].setSelectionRange( 0, 4000 );
                } else {
                    $tar.select();
                }
            }, 0 );
        }
    },

    doPointsKeyup: function( e ) {
        var $tar = $( e.target );

        this.setAwardPointsFor( $tar, true );
    },

    // sync model to input element and validate
    setAwardPointsFor: function( $el ) {
        // validate range
        this.validateRangeOf( $el );
    },

    doPointsKeydown: function( e ) {
        var $tar = $( e.target ),
            $nxtInp = $tar.closest( '.paxRow' )
                .nextAll( '.paxRow:first' ).find( '.awardPointsInp' ),
                that = this;

        // filter non-numerics
        console.log( that );
        if( that.promoModel.payoutType == 'cash' ) {
            this.filterNonNumericKeydown( e );
        } else {
            this.filterNonNumericKeydownNoCash( e );
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
    setCalcObj: function( calcObj ) {
        this.calcObj = calcObj;
    },

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
                var theCriteriaWrapper = '<div class="nominationsCalcCriteriaWrapper" data-criteria-id="' + thisRowId + '">';
                var thisRatingSelectInner = [];
                // filling the text of first option later from a TPL element (should do the whole select element in handlebars, but this is calc and its not worth the effort ATM)
                var thisRatingSelectOutter = '<select class="nominationsCalcRatingSelect"><option value="undefined" class="msgSelRatingTarget">[dynamic js]</option>';

                for ( j = 0; j < thisCriteriaRow.ratings.length; j++ ) {
                    var thisRatingOption = thisCriteriaRow.ratings[ j ];

                    thisRatingSelectInner.push( '<option value="' + thisRatingOption.value + '" data-ratingId="' + thisRatingOption.id + '">' + thisRatingOption.label + '</option>' );
                }

                //close up the select element
                thisRatingSelectOutter += thisRatingSelectInner + '</select>';

                theCriteriaElement += theCriteriaWrapper + '<label>' + thisRowLabel + '</label>' + thisRatingSelectOutter + '<span id="criteriaWeightElm" class="criteriaWeightElm"></span><span id="criteriaScoreElm" class="criteriaScoreElm"></span></div>';
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
                show: function() {
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

	setUpByRAPromoTip: function() {
        var $tar = this.$el.find( '.raPromo' );

        $tar.popover( {
            title: this.$el.find( '.byPromoHelpTip strong' ).text(),
            content: this.$el.find( '.byPromoHelpTip .tipContent' ).html(),
            html: true,
            trigger: 'hover',
            container: 'body',
            placement: 'top'
        } );
    },
	
    doSendCalculatorRatings: function( $theSelects, theQtip ) {
        var thisScope = this;

        var getCriteriaInfo = function() {
            var theObj = {};

            $theSelects.each( function( i ) {
                var $this = $( this );
                var thisRatingValue = parseInt( $this.val(), 10 );
                var thisCriteriaId = parseInt( $this.parent().attr( 'data-criteria-id' ), 10 );

                // var thisCriteriaObj = {
                //     criteriaRating: thisRatingValue,
                //     criteriaId: thisCriteriaId
                // };

                theObj[ 'criteria[' + i + '].criteriaRating' ] = thisRatingValue;
                theObj[ 'criteria[' + i + '].criteriaId' ] = thisCriteriaId;

            } );

            return theObj;
        };

        var theData = {
            promotionId: this.promotionId,
            nodeId: this.nodeId,
            levelId: this.levelId,
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
            //theTotalScore,
            theAwardLevel,
            savedData = data,

            hasRangedAward = function() {
                var hasRanged = false;

                if ( that.calcObj.attributes.awardType === 'range' ) {
                    hasRanged = true;
                    rangedElm = '<span>(' + data.awardRange.min + ' - ' + data.awardRange.max + ')</span><input type="text" data-range-max="' + data.awardRange.max + '" data-range-min="' + data.awardRange.min + '">';
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
                    theAwardLevel = data.awardLevel.name + ' (' + data.awardLevel.value + ')' + '<input type="hidden" id="calcAwardLevelId" value="' + data.awardLevel.value + '">';
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

                    $( '.nominationsCalcCriteriaWrapper[data-criteria-id="' + thisCriteria.criteriaId + '"]' ).find( 'select' ).val( thisCriteria.criteriaRating );
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
                $theRow = $theCalcElm.find( 'div[data-criteria-id="' + theCriteria.criteriaId + '"]' );

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
        var paxIndex = $anchor.parents( 'tr' ).data( 'index' );
        //if the calc obj is not a merchlevel award, do the normal save
        if ( this.calcObj.attributes.awardType !== 'merchlevel' ) {
            $anchor.siblings( 'input' ).val( theCalcAwardObj.awardedAmount );
            $anchor.text( theCalcAwardObj.awardedAmount );
        }else{
            if( this.model.get( 'tabularData' ).results[ paxIndex ].isTeam === true ) {

                var memberCount = this.model.get( 'tabularData' ).results[ paxIndex ].teamMembers.length;

                $anchor.siblings( '[name*="awardLevel"]' ).val( theCalcAwardObj.awardedAmount * memberCount );
                $anchor.text( theCalcAwardObj.awardLevel.name + ' (' + theCalcAwardObj.awardLevel.value * memberCount + ')' );
            } else {
                $anchor.siblings( '[name*="awardLevel"]' ).val( theCalcAwardObj.awardedAmount );
                $anchor.text( theCalcAwardObj.awardLevel.name + ' (' + theCalcAwardObj.awardLevel.value + ')' );
            }
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
        var pax = $el,
            $paxPar = $el.parent(),
            paxIndex = $paxPar.parents( 'tr' ).data( 'index' ),
            //critLength = calcData.criteria.length,
            $thisRowInputs;//,
           // i = 0;


            var theData = this.model.get( 'tabularData' ).results[ paxIndex ];

        //clear our previous hidden inputs
        pax.siblings( '[name*="award"]' ).remove();


        if( theData.isTeam === true ) {
            var teamAward = ( theData.teamMembers.length * calcData.awardedAmount );

            $paxPar.append( '<input type="hidden" name="tabularData.results[' + paxIndex + '].award" value="' + teamAward + '" class="teamAwardCalc">' );
            $thisRowInputs = $( '.teamAwardCalc' );
            _.each( theData.teamMembers, function( member, index ) {

                $( $thisRowInputs ).parent().append( '<input type="hidden" name="tabularData.results[' + paxIndex + '].team[' + index + '].paxId" value="' + member.paxId + '" class="hiddenTeamInp" /> <input type="hidden" name="tabularData.results[' + paxIndex + '].team[' + index + '].value" value="' + calcData.awardedAmount + '" class="hiddenTeamInp" />' );
            } );
        } else {
            $paxPar.append( '<input type="hidden" name="tabularData.results[' + paxIndex + '].award" value="' + calcData.awardedAmount + ' ">' );
        }
    },

    doShowSameForAllCalc: function( $anchor, theCalcAwardObj ) {
        var that = this;
        $anchor.qtip( {
            content: this.$wrapper.find( '#sameForAllTipTpl' ).clone().removeAttr( 'id' ),
            position: {
                my: 'left center', at: 'right center', adjust: { x: 5, y: 0 }, container: this.$wrapper, effect: false, viewport: false
            },
            show: { ready: true, event: false },
            hide: 'unfocus',
            //only show the qtip once
            events: {
                hide: function() {$anchor.qtip( 'destroy' );},
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
                    $theInput = $this.find( '[name*="awardQuantity"]' ),
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
                    $this.find( '.calcDeduction' ).text( that.savedCalcObj.awardedAmount );
                    that.setAwardPointsForCalc( $this.find( '.calcLink' ), theCalcAwardObj );
                }else{
                    that.setAwardPointsForCalc( $this.find( '.calcLink' ), theCalcAwardObj );
                    // $this.find(".calcDeduction").text();

                    if ( thisRatingIdName === '' || thisRatingIdName === undefined ) {
                        thisRatingIdName = thisInputName.substr( 0, thisInputName.indexOf( '.awardLevel' ) ); //in case we don't need to change the name
                    }
                    $thisAnchor.siblings( 'input' ).not( '[name*="calculatorResultBeans"]' ).attr( 'name', thisRatingIdName + '.awardLevel' ).val( theCalcAwardObj.awardedAmount );
                    $thisAnchor.text( theCalcAwardObj.awardLevel.name + ' (' + theCalcAwardObj.awardLevel.value + ')' );
                    $thisAnchor.append( '<input type="hidden" name="' + thisRatingIdName + '.awardLevelName" value="' + theCalcAwardObj.awardLevel.name + '">', '<input type="hidden" name="' + thisRatingIdName + '.awardLevelValue" value="' + theCalcAwardObj.awardLevel.value + '">' );
                }

                $theToolTip.parent( '.ui-tooltip-content' ).parent().qtip( 'hide' );
            } );

        }else{
            $theToolTip.parent( '.ui-tooltip-content' ).parent().qtip( 'hide' );
        }
    },
    reloadOrGoBack: function( e ) {
        var self = this;

        if( self.model.get( 'tabularData' ).totalRows == self.statusCount ) {
            window.history.back();
        } else {

            $( e.target ).parents( '.approvalSuccessModal' ).modal( 'hide' );
            location.reload( true );
        }
    },
    doRenderPreviousCalcInfo: function( $theRow, theQtip ) {
        if ( $theRow.attr( 'data-shared-calc-info' ) === 'true' ) {
            this.doUpdateRecogCalcValues( this.savedCalcObj, theQtip, true );
        }else if ( this.savedCalcRows[ $theRow.attr( 'data-participant-cid' ) ] ) {
            this.doUpdateRecogCalcValues( this.savedCalcRows[ $theRow.attr( 'data-participant-cid' ) ], theQtip, true );
        }else if( $( '#dataForm' ).find( '[name*="].id"]' ).filter( '[value="' + $theRow.attr( 'data-participant-id' ) + '"]' ).val() !== undefined ) {
            this.doRenderRecogCalcValuesOld( $( '#dataForm' ).find( '[name*="].id"]' ), theQtip );
        }
    },

    doRenderRecogCalcValuesOld: function( recipIdInput, theQtip ) {

        var theRecipNameRaw = recipIdInput.attr( 'name' );
        var theRecipCalcNameRaw = theRecipNameRaw.substr( 0, theRecipNameRaw.indexOf( '.id' ) ) + '.calculatorResultBeans';
        var finalSelect;


        $( '#dataForm' ).find( '[name*="' + theRecipCalcNameRaw + '"]' ).filter( '[name*="criteriaId"]' ).each( function() {
            var $this = $( this ),
                thisCriteriaId = $this.val(),
                thisInputName = $this.attr( 'name' ),
                thisRatingIdName = thisInputName.substr( 0, thisInputName.indexOf( '.criteriaId' ) ),
                thisRatingId = $( '[name="' + thisRatingIdName + '.ratingId"]' ).val(),
                theRatingSelectPar = theQtip.find( '.recCalcCriteriaWrapper' ).filter( '[data-criteria-id="' + thisCriteriaId + '"]' ),
                theRatingSelect = theRatingSelectPar.find( 'select' ),
                theRatingOption = $( theRatingSelect ).find( '[data-ratingid="' + thisRatingId + '"]' );
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
                visible: function() {
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

    doComparison: function( e ) {
        var $tar = $( e.target ),
            tarId = $tar.data( 'index' ),
            $compareBlock = this.$el.find( '.compareNomineeBlock' ),
            results = this.model.get( 'tabularData' ).results,
            selectedResult = _.where( results, { index: tarId } ),
            $container = this.$el.find( '.compareSection' ),
            isRemoved = false,
            self = this;

        if( $container.children().length >= 2 ) {
            this.$el.find( '.compare:not(:checked)' ).attr( 'disabled', true );
        }

        _.each( $compareBlock, function( cb ) {
            var compareId = $( cb ).data( 'index' );

            if( tarId === compareId ) {
                $( cb ).remove();

                isRemoved = true;

                self.$el.find( '.compare:not(.disabledJSON)' ).attr( 'disabled', false );
            }
        } );

        if( !isRemoved ) {
            $container.append( this.subTpls.comparePaxTpl( selectedResult[ 0 ] ) );
        }
    },

    removeComparison: function( e ) {
        var $tar = $( e.target ),
            tarId = $tar.parents( '.compareNomineeBlock' ).data( 'index' ),
            //$container = this.$el.find('.compareSection'),
            $compareInp = this.$el.find( 'tbody .compare' );

        $tar.parents( '.compareNomineeBlock' ).remove();

        _.each( $compareInp, function( ci ) {
            var compareId = $( ci ).data( 'index' );

            if( tarId === compareId ) {
                $( ci ).attr( 'checked', false );
            }
        } );
    },

    viewCompareTableRow: function( e ) {
        var $tar = $( e.target ),
            $compareBlock = $tar.parents( '.compareNomineeBlock' ),
            tarId = $compareBlock.data( 'index' ),
            //results = this.model.get('tabularData').results,
            //selectedResult = _.where(results, {index: tarId}),
            $compareInp = this.$el.find( 'input.compare' );

        _.each( $compareInp, function( ci ) {
            var compareId = $( ci ).data( 'index' );

            if( tarId === compareId ) {
                $.scrollTo( $( ci ).parents( 'tr' ), G5.props.ANIMATION_DURATION );

                setTimeout( function() {
                    G5.util.animBg( $( ci ).parents( 'tr' ).find( 'td' ), 'flash' );
                }, 500 );

                return;
            }
        } );
    },

    // *******************
    // SUBMITS AND VALIDATION
    // *******************
    validateDates: function( event ) {
        'use strict';
        var $startDate = this.$el.find( '#dateStart' ),
            $endDate = this.$el.find( '#dateEnd' ),
            $target = $( event.currentTarget ),
            startDate = $startDate.closest( '.datepickerTrigger' ).data( 'datepicker' ).date,
            endDate = $endDate.closest( '.datepickerTrigger' ).data( 'datepicker' ).date;

        event.preventDefault();

        switch( $target.attr( 'id' ) ) {
            case $startDate.attr( 'id' ):
            case undefined:

                // once start date has been set, other dates cannot occur before it
                $endDate.closest( '.datepickerTrigger' ).datepicker( 'setStartDate', $startDate.val() ).datepicker( 'setEndDate', '+0d' );

                // if the new startDate is later than the current endDate AND a value already exists in endDate, clear the value & indicate change
                if ( startDate.valueOf() > endDate.valueOf() && $endDate.val() !== '' ) {
                    G5.util.animBg( $endDate, 'background-flash' );
                    $endDate.val( '' ).closest( '.datepickerTrigger' ).datepicker( 'update' );
                }
                break;
            case $endDate.attr( 'id' ):
                G5.util.formValidate( $( '#approvalsSearchForm' ).find( '.validateme' ) );

                // if endDate is updated, displayEndDate updates automatically to endDate + 10 days
                break;
        }
    },

    validateNumeric: function( e ) {
        var $tar = $( e.target ),
            v = $tar.val(),
            decPlaces = this.model.get( 'promotion' ).payoutType == 'cash' ? '2' : '4',
            reStr = '^-?(\\d{1,9})?(\\.\\d{0,' + ( decPlaces ) + '})?$',
            regEx = new RegExp( reStr );

        if( !regEx.test( v ) ) {
            // if not matching our regex, then set the value back to what it was
            // or empty string if no last val
            $tar.val( $tar.data( 'lastVal' ) || '' );
        } else {
            // store this passing value, we will use this to reset the field
            // if a new value doesn't match
            $tar.data( 'lastVal', v );
        }
    },

    validateRangeOf: function( $el ) {
        var castNum = '';
            if( this.model.get( 'promotion' ).payoutType == 'cash' ) {
              castNum = function( x ) { // convert strings to nums
                      return typeof x === 'string' ? parseFloat( x ) : x;
                  };
            } else {
              castNum = function( x ) { // convert strings to nums
                      return typeof x === 'string' ? parseInt( x, 10 ) : x;
                  };
            }

        var val = castNum( $el.val() || 0 ), // if '' then put a zero in there
            rMin = castNum( this.model.get( 'tabularData' ).awardMin ),
            rMax = castNum( this.model.get( 'tabularData' ).awardMax );

        // OUT of RANGE ERROR
        if( val < rMin || val > rMax ) {
            // disp error
            this.onNavErrorTip( 'awardOutOfRange', $el );
            return false;
        }

        this.onNavErrorTip( '', $el, true );

        return true;
    },

    validateTeamAward: function() {
        var awardValid = true,
            that = this;

        this.$el.find( '#addAwardModal .awardPointsInp' ).each( function() {
            if( !that.validateRangeOf( $( this ) ) ) {
                awardValid = false;

                return false;
            } else {
                awardValid = true;

                return { valid: true };
            }
        } );

        if( awardValid ) {
            return { valid: true };

        } else {
            this.onNavErrorTip( 'msgGenericError', this.$el.find( '.addAwardBtn' ) );
        }
    },

    checkForServerErrors: function () {
        if ( $( '#serverReturnedErrored' ).val() === 'true' ) {
            $( '#approvalsErrorBlock' ).slideDown( 'fast' ); //show error block
        }
    },

    submitTeamAward: function() {
        var tindex = this.$el.find( '#addAwardModal .teamRecipientList ul' ).data( 'teamIndex' ),
            teamAward = 0,
            self = this,
            $thisRowInputs;

        if( !this.validateTeamAward() ) {
            return false;
        }

        var $teamAwardInputs = $( '.teamAddAward' );
        _.each( ( $teamAwardInputs ), function( value, index ) {
            if( tindex == $( value ).data( 'team-id' ) ) {
                $thisRowInputs = $teamAwardInputs[ index ];
            }
        } );
         var $paxRow = this.$el.find( '#addAwardModal li' );
        $( $thisRowInputs ).closest( '.hiddenTeamInp' ).remove();

        _.each( $paxRow, function( row, index ) {
            var paxId = parseInt( $( row ).find( '.profile-popover' ).data( 'participantIds' ), 10 );
            var paxAward = null;

            if( self.promoModel.payoutType == 'cash' ) {
              paxAward = parseFloat( $( row ).find( '.awardPointsInp' ).val() );
            } else {
              paxAward = parseInt( $( row ).find( '.awardPointsInp' ).val(), 10 );
            }

            teamAward += paxAward;

            self.model.setTeamIndividualAward( tindex, paxId, paxAward );

            $( $thisRowInputs ).siblings( '.teamAwardAmount' ).append( '<input type="hidden" name="tabularData.results[' + tindex + '].team[' + index + '].paxId" value="' + paxId + '" class="hiddenTeamInp" /> <input type="hidden" name="tabularData.results[' + tindex + '].team[' + index + '].value" value="' + paxAward + '" class="hiddenTeamInp" />' );
        } );

        this.model.setTeamAward( tindex, teamAward );
        if( teamAward === 0 ) {
            teamAward = null;
        }
        $( $thisRowInputs ).siblings( '.teamAwardAmount' ).val( teamAward );

        this.$el.find( '#addAwardModal' ).modal( 'hide' );
    },

    submitBudgetRequest: function( e ) {
        var $budgetPopover = this.$el.find( '.budgetIncreasePopover' ),
            $validate = $budgetPopover.find( '.validateme' ),
            isValid = G5.util.formValidate( $validate ),
            budget = this.$el.find( '.budgetIncrease' ).val();//,
            //$budgetLink = this.$el.find('.budgetIncreaseLink');

        e.preventDefault();

        // failed generic validation tests (requireds mostly)
        if( !isValid ) {
            return false;
        }

        this.model.sendBudget( budget, this.promotionId );

        $budgetPopover.closest( '.qtip' ).qtip( 'hide' );
    },
    submitFilterForm: function( e ) {
        //TODO: Make this JSON format instead of form data
        var $form = this.$el.find( '#approvalsSearchForm' ),
            formData = $form.serializeArray(),
            data,
            arrData = {};

        e.preventDefault();

        this.$el.find( '.compareNomineeBlock' ).remove();
        this.levelId = $( '#levelsFilter' ).val();
        _.each( formData, function( el ) {
            var value = _.values( el );
            if( value[ 0 ] == 'statusFilter' ) {

                if( !arrData.hasOwnProperty( value[ 0 ] ) ) {
                    arrData[ value[ 0 ] ] = [];
                    arrData[ value[ 0 ] ].push( value[ 1 ] );

                } else {

                    arrData[ value[ 0 ] ].push( value[ 1 ] );
                }
            } else {
                arrData[ value[ 0 ] ] = value[ 1 ];
            }
        } );
        var status = arrData[ 'statusFilter' ];
        arrData[ 'statusFilter' ] = '';

        _.each( status, function( value, index ) {

            if( index < status.length - 1 ) {
                arrData[ 'statusFilter' ] += value + ',';

            } else if( index == status.length - 1 ) {
                arrData[ 'statusFilter' ] += value;
            }
        } );

                data = {
                    promotionId: this.promotionId,
                    nodeId: this.nodeId,
                    levelId: this.levelId
                };





        this.model.loadApprovalTable( $.extend( {}, data, arrData ) );
    },

    doCancelApproval: function( e ) {
        var $tar = $( e.target ),
            url = $tar.attr( 'href' );

        e.preventDefault();

        window.location = url;
    },

    validate: function() {
        var $form = this.$el.find( '.approvalTableWrap' ),
            $validate = $form.find( '.validateme:visible' ),
            isValid = G5.util.formValidate( $validate ),
            awardValid = true,
            that = this;

         // failed generic validation tests (requireds mostly)
        if( !isValid ) {
            return false;
        }

        this.$el.find( 'table .awardPointsInp' ).each( function() {
            if( $( this ).prop( 'readOnly' ) ) {
                awardValid = true;
                return true;
            }

            if( !that.validateRangeOf( $( this ) ) ) {

                awardValid = false;

                return false;
            } else {

                awardValid = true;

                return { valid: true };
            }
        } );

        if( awardValid ) {
            return { valid: true };

        } else {
            this.onNavErrorTip( 'msgGenericError', this.$el.find( '.submitApproval' ) );
            return false;
        }
    },

    submitApproval: function( e ) {
        var $form = this.$el.find( '.approvalTableWrap' ),
            //$startDate = this.$el.find('#dateStart').val(),
            //$startDateInp = this.$el.find('.startDateFilter').val($startDate),
            //$endDate = this.$el.find('#dateEnd').val(),
            //$endDateInp = this.$el.find('.endDateFilter').val($endDate),
            //$promoIdInp = this.$el.find('.promotionIdInp').val(this.promotionId),
            $levelId,
            isValid = this.validate(),
            $tar = $( e.target ),
            that = this;

        e.preventDefault();

        // failed generic validation tests (requireds mostly)
        if( !isValid ) {
            return false;
        }

        G5.util.showSpin( this.$el, {
            cover: true
        } );

        $tar.attr( 'disabled', 'disabled' );

        if( this.$el.find( '#levelsFilter' ).length ) {
            $levelId = this.$el.find( '#levelsFilter option:selected' ).val();
        } else {
            $levelId = this.$el.find( '.levelsFilterInp' ).val();
        }

        this.$el.find( '.levelIdFilter' ).val( $levelId );

        // $form.find('input, select').removeAttr('disabled');
        var $formFields = $form.serializeArray();

        for ( var i = 0; i < $formFields.length; i++ ) {
            if( $formFields[ i ].value == 'pend' ) {
                $formFields.splice( i, 1 );
                i--;
            }
        }

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: $form.attr( 'action' ),
            data: $formFields,
            success: function( serverResp ) {

                if( serverResp.data.messages && serverResp.data.messages.length > 0 ) {
                    if( serverResp.data.messages[ 0 ].type == 'error' ) {
                        that.onNavErrorTip( 'overBudget', that.$el.find( '.submitApproval' ), false, serverResp.data.messages[ 0 ].text );
                    }
                    $tar.removeAttr( 'disabled' );
                } else {
                    that.$el.find( '.approvalSuccessModal' ).modal( 'show' );
                    $tar.removeAttr( 'disabled' );
                }

                G5.util.hideSpin( that.$el );
            }

        } );
    }
} );