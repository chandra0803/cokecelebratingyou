/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported NominationsSubmitModel */
/*global
$,
G5,
Backbone,
NominationsSubmitModel:true
*/
NominationsSubmitModel = Backbone.Model.extend( {
    initialize: function () {
        var that = this;

        this.calcSetup = null;

        this.on( 'largeEcertsLoaded', function( data ) {
            that.setLargeImgs( data );
        } );
    },

    loadData: function( params, changePromo ) {
        var data = {},
            that = this;

        if ( params ) {
            data =  params;
        }

        this.trigger( 'saveStarted' );

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_NOMINATIONS_SUBMIT_DATA,
            data: data,
            success: function ( serverResp ) {
               var data = serverResp.data;

               that.set( data );

                if ( changePromo ) {
                    that.promoChanged = true;
                    that.unset( 'participants' );
                    that.trigger( 'promotionChanged' );
                }

               that.trigger( 'dataLoaded', data );
            },
            error: function( jqXHR, textStatus, errorThrown ) {
                console.log( '[ERROR] CommunicationsBannerEditModel: ', jqXHR, textStatus, errorThrown );
            },
            complete: function() {
                that.trigger( 'saveEnded' );
            }
        } );
    },

    loadPromotionsList: function( params ) {
        var data,
            that = this;

        if ( params ) {
            data =  params;
        }

        this.trigger( 'saveStarted' );

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_NOMINATIONS_LIST,
            data: data,
            success: function ( serverResp ) {
               var promotions = serverResp.data.nominations;

               that.set( 'nominations', promotions );
               that.trigger( 'nominationsLoaded', promotions );
            },
            complete: function() {
                that.trigger( 'saveEnded' );
            }
        } );

    },

    loadParticipants: function( params, group ) {
        var data,
            that = this;

        if ( params ) {
            data =  params;
        }

        this.trigger( 'saveStarted' );

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_NOMINATIONS_PARTICIPANTS,
            data: data,
            success: function ( serverResp ) {
                var participants = serverResp.data.participants;

                that.set( 'participants', participants );

                if ( group ) {
                    that.trigger( 'participantsLoadedGroups', participants );
                } else {
                    that.trigger( 'participantsLoaded', participants );
                }

            },
            complete: function() {
                that.trigger( 'saveEnded' );
            }
        } );
    },

    loadPaxGroups: function( params ) {
        var data,
            that = this;

        if ( params ) {
            data =  params;
        }

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_SAVED_PARTICIPANT_GROUPS,
            data: data,
            success: function ( serverResp ) {
               var groups = serverResp.data.groups;

               that.set( 'groups', groups );
               that.trigger( 'paxGroupsLoaded', groups );
            }
        } );
    },

    getCalcSetup: function( params ) {
        var data,
            that = this;

        if ( params ) {
            data =  params;
        }

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_NOMINATIONS_CALCULATOR_DATA,
            data: data,
            success: function ( serverResp ) {

               this.calcSetup = serverResp.data.nominationsCalculator;

               that.trigger( 'calcLoaded', this.calcSetup );

            }
        } );
    },

    getCertImages: function( indOrTeam ) {
        var that = this,
            individualOrTeam = indOrTeam;

            var data = {
                'promotion.individualOrTeam': individualOrTeam,
                'promotion.id': this.get( 'promotion' ).id
            };

            $.ajax( {
                dataType: 'g5json',
                type: 'POST',
                url: G5.props.URL_GET_CERT_IMAGES,
                data: data,
                success: function ( serverResp ) {
                    that.trigger( 'largeEcertsLoaded', serverResp.data.eCards );
                }
            } );


    },

    loadCustomElements: function( params ) {
        var data,
            that = this;

        if ( params ) {
            data =  params;
        }

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_CUSTOM_FORM_ELEMENTS,
            data: data,
            success: function ( serverResp ) {
               var customElements = serverResp.data.customElements;

                that.set( 'customElements', customElements );

                if ( that.get( 'promotion' ).customFieldsActive ) {
                    that.twiddleDeeDiddle( customElements );
                }


               that.trigger( 'customElementsLoaded', customElements );
            },
            error: function( jqXHR, textStatus, errorThrown ) {
                console.log( '[ERROR] loadCustomElements: ', jqXHR, textStatus, errorThrown );
            }
        } );
    },

    // override Backbone.Model.save()
    save: function( fromStep, toStep, ids, isDraft, isSubmit ) {
        var that = this,
            data,
            url = G5.props.URL_JSON_NOMINATIONS_EDIT_SAVE + '?method=' + fromStep,
            request;

        ids.claimId = that.get( 'promotion' ).claimId;
        data  = this.serializeForStruts( fromStep, isDraft, ids );

        this.trigger( 'saveStarted' );

        // otherwise, continue with the ajax submit
        request = $.ajax( {
            url: url,
            type: 'post',
            data: data,
            dataType: 'g5json'
        } );

        request.done( function( serverResp ) {
            var err = serverResp.getFirstError(),
                claimId = serverResp.data.claimId,
                updatedClaimId;

            if ( err ) {
                console.error( '[ERROR] NomsModel: server error', err );

                that.trigger( 'saveError', serverResp.getErrors() );
            } else {
                if ( claimId ) {
                    updatedClaimId = that.get( 'promotion' );
                    updatedClaimId.claimId = claimId;

                    that.set( 'promotion', updatedClaimId );
                }

                that.trigger( 'saveSuccess', serverResp.data, fromStep, toStep, isDraft, isSubmit );
            }
        } );

        request.fail( function( jqXHR, textStatus, errorThrown ) {
            var errors;

            console.error( '[ERROR] NomsModel: ajax call to save failed', jqXHR, textStatus, errorThrown );

            // struts returns full HTML for FORM VALIDATION - BOOO!
            if ( textStatus === 'parsererror' ) {
                errors = G5.util.parseErrorsFromStrutsFormErrorHtml( jqXHR.responseText );

                if ( errors ) {
                    that.trigger( 'error:genericAjax', errors );
                }
            }
        } );

        request.always( function() {
            that.trigger( 'saveEnded' );
        } );
    },

    serializeForStruts: function( fromStep, isDraft, ids ) {
        var fromStepJson = 'toJson_' + fromStep,
            dat = this[ fromStepJson ] ? this[ fromStepJson ]() : this.toJSON(),
            rem = null;

        // for struts
        dat.method = fromStep;
        dat.draft = isDraft ? true : false;

        dat.promotionId = ids.promotionId;
        dat.claimId = ids.claimId;
        dat.nodeId = ids.nodeId;

        // WRITING draw tool settings - sizes null per JAVA 3-31-2017
        if( dat.promotion && dat.promotion.drawToolSettings && dat.promotion.drawToolSettings.sizes ) {
            dat.promotion.drawToolSettings.sizes = null;
        }

        // this gives a query string
        dat = $.param( dat );

        // this replaces the arrayName[0][subArrayName][0][keyName] notation with:
        // arrayName[0].subArrayName[0].keyName
        while ( ( rem = dat.match( /(\?|&).*?%5B([a-zA-Z_]+)%5D.*?=/ ) ) ) {
            dat = dat.replace( '%5B' + rem[ 2 ] + '%5D', '.' + rem[ 2 ] );
        }

        return dat;
    },

    deleteNom: function( params, url ) {
        var data = params;

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: url,
            data: data,
            success: function () {
                //expecting server redirect
            }
        } );
    },

    setNodeId: function( id ) {
        var that = this;
        var nodes = this.get( 'nodes' ),
            updatedNode = _.where( nodes, { id: id } );
        that.trigger( 'nodeUpdated', id );
        updatedNode[ 0 ].selected = true;

    },

    setInputs: function( val, key ) {
        var updatedPromo = this.get( 'promotion' );

        updatedPromo[ val ] = key;
        this.set( 'promotion', updatedPromo );
    },

    setLargeImgs: function( eCards ) {
        var promotion = this.get( 'promotion' ),
            oldCards = promotion.eCards,
            newCards = eCards;

        _.each( newCards, function( nCard ) {
            _.each( oldCards, function( oCard ) {
                if ( nCard.id === oCard.id ) {
                    oCard.largeImage = nCard.largeImage;
                }
            } );
        } );
    },

    setCustomFormElements: function( index, val ) {
        var fields = this.get( 'customElements' ).fields,
            updatedField = _.where( fields, { index: index } );

        if ( $.isArray( val ) ) {
           val = val.join();
        }

        updatedField[ 0 ].value = val;
    },

    setParticipants: function( paxCollection ) {
        //var updatedPax = this.get("participants");

        //updatedPax.participants = paxCollection.toJSON();
        this.set( 'participants', paxCollection.toJSON() );
    },

    setBehavior: function( id, selected ) {
        var behaviors = this.get( 'promotion' ).behaviors,
            selectedBehavior = _.where( behaviors, { id: id } );

        selectedBehavior[ 0 ].selected = selected;
    },

    setEcard: function( model, drawingData, cardData ) {
        var updatedEcard = this.get( 'promotion' );

        updatedEcard.cardId = parseInt( model.get( 'drawToolCardId' ) );
        updatedEcard.cardType = model.get( 'drawToolCardType' );
        updatedEcard.videoUrl = model.get( 'drawToolVideoUrl' );
        updatedEcard.cardUrl = model.get( 'drawToolCardUrl' );
        updatedEcard.drawingData = drawingData;
        updatedEcard.cardData = cardData;

        this.set( 'promotion', updatedEcard );
    },

    setGroup: function( id ) {
        var updatedGroup = this.get( 'groups' ),
            selectedGroup = _.where( updatedGroup, { id: id } );

        selectedGroup[ 0 ].selected = true;
    },

    setAttachmentLink: function( link, url, name, id ) {
        var updatedLink = this.get( 'promotion' ),
        lid;

        if( !id) {
            lid = updatedLink.nominationLinks.length + 1;
        }
        else {
            lid = id;
        }        

        updatedLink.nominationLinks.push({
            nominationLink: link,
            nominationUrl: url,
            fileName: name,
            linkid: lid            
        });        

        this.set( 'promotion', updatedLink );
        this.updateAttachmentCount();
    },

    updateAttachmentCount: function() {
        var promoObj = this.get( 'promotion' ),
        uploadsCount = promoObj.nominationLinks.length == 0 ? null : promoObj.nominationLinks.length;        
        promoObj.updatedDocCount = uploadsCount;
        this.set( 'promotion', promoObj );        
    },

    removeAttachedDocuments: function( ) {
        var updatedLink = this.get( 'promotion' );
        this.get( 'promotion' ).nominationLinks = [];
        this.set( 'promotion', updatedLink );
        this.updateAttachmentCount();
        this.trigger( 'docUploaded' );
    },

    deleteAttachments: function( linkId ) {
        var updatedLink = this.get( 'promotion' );
        nominationLinks = _.without(updatedLink.nominationLinks, _.findWhere(updatedLink.nominationLinks, {
          linkid: parseInt(linkId)
        }));
        this.get('promotion').nominationLinks = nominationLinks;
        this.updateAttachmentCount();
        this.trigger( 'docUploaded' );
    },

    getCurrentStep: function() {
        return this.get( 'promotion' ).currentStep;
    },

    removeUploadedDoc: function( claimId, linkId, isReplacement, event ) {
        var data = { claimId: claimId, linkId : linkId },
            that = this;

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_NOMINATIONS_UPLOAD_DOC_REMOVE,
            data: data,
            success: function ( serverResp ) {
               var err = serverResp.getFirstError();
                
                that.deleteAttachments(linkId);

                /*if ( err ) {
                    console.error( '[ERROR] NomsModel: server error', err );

                    that.trigger( 'saveError', serverResp.getErrors() );
                } else {

                    /*if ( isReplacement ) {
                        that.trigger( 'docReplaced', event );
                    } else {
                        //that.trigger( 'docRemoved' );
                        that.deleteAttachments(linkId);
                    }*/
                   // that.deleteAttachments(linkId);
                //}*/
            }
        } );
    },

    //massage data coming in from JAVA

    // ORDERING: we only assume the index of all address fields is sorted, others sortBy sequenceNumber

    // ALL Fields
    // 1) sort by "sequenceNumber"
    // 2) generate "index" field from sorted list order

    // Address Fields
    // 1) for all fields with fieldGroup="address"
    // 2) they will all have same ID, since they are not going into a Collection (a set) this should be fine
    // 3) index of address fields will be used for ordering, so preserve their order when others are sorted
    twiddleDeeDiddle: function( data ) {
        var fields = data.fields || [],
            // no address fields except country field
            fieldsPlusCountry = _.filter( fields, function( f ) {
                if ( !f.fieldGroup || f.fieldGroup !== 'address' ) { return true; } // keep it
                if ( f.fieldGroup === 'address' && f.subType === 'country' ) { return true; } // keep only this addy field
                return false; // dump the rest
            } );

        // sort by sequence num
        fieldsPlusCountry = _.sortBy( fieldsPlusCountry, function( f ) { return f.sequenceNumber; } );
        console.log( fieldsPlusCountry );
        // add "index"
        _.each( fields, function( f, i ) { f[ 'index' ] = i; } );

        data.fields = fields;

        return data;
    }

} );
