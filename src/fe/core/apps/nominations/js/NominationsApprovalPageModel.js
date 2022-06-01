/*jslint browser: true, nomen: true, devel: false, unparam: true, unused: false */
/*exported NominationsApprovalPageModel */
/*global
Backbone,
NominationsApprovalPageModel:true
*/
NominationsApprovalPageModel = Backbone.Model.extend( {

    initialize: function() {

    },

    loadApprovalData: function( opts ) {
        var data = {},
            that = this;

        data = $.extend( {}, data, opts );

        this.trigger( 'saveStarted' );

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_NOMINATIONS_APPROVAL_PAGE_DATA,
            data: data,
            success: function ( serverResp ) {
               var promotion = serverResp.data.promotion;

               that.set( 'promotion', promotion );
               that.trigger( 'approvalDataLoaded', promotion );
            },
            error: function( jqXHR, textStatus, errorThrown ) {
                console.log( '[ERROR] nominationsApprovalPageModel: ', jqXHR, textStatus, errorThrown );
            },
            complete: function() {
                that.trigger( 'saveEnded' );
            }
        } );
    },
    loadNewApprovalData: function( opts ) {
        var data = {},
            that = this;

        data = $.extend( {}, data, opts );

        this.trigger( 'saveStarted' );

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_NOMINATIONS_APPROVAL_PAGE_DATA,
            data: data,
            success: function ( serverResp ) {
               var promotion = serverResp.data.promotion;

               that.set( 'promotion', promotion );
               that.trigger( 'approvalDataReloaded', promotion );
            },
            error: function( jqXHR, textStatus, errorThrown ) {
                console.log( '[ERROR] nominationsApprovalPageModel: ', jqXHR, textStatus, errorThrown );
            },
            complete: function() {
                that.trigger( 'saveEnded' );
            }
        } );
    },
    loadApprovalTable: function( opts ) {
        var data = {},
            that = this;

        data = $.extend( {}, data, opts );

        this.trigger( 'saveStarted' );

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_NOMINATIONS_APPROVAL_TABLE_DATA,
            data: data,
            success: function ( serverResp ) {
                var tableData = serverResp.data.tabularData;

                _.each( tableData.results, function( tb, index ) {
                    var awardTotal;
                    if ( tb.isTeam ) {
                        awardTotal = 0;
                        _.each( tb.teamMembers, function( tm ) {
                            awardTotal += tm.award;
                        } );
                        if ( awardTotal === 0 ) {
                            awardTotal = null;
                        }
                        tableData.results[ index ].award = awardTotal;
                    }
                } );
                that.set( 'tabularData', tableData );
                // console.log(tableData.results);
                if ( tableData.isMore ) {
                    that.trigger( 'loadMoreApprovals', tableData );
                } else {
                    that.trigger( 'approvalTableLoaded', tableData );
                }

            },
            error: function( jqXHR, textStatus, errorThrown ) {
                console.log( '[ERROR] nominationsApprovalPageModel: ', jqXHR, textStatus, errorThrown );
            },
            complete: function() {
                that.trigger( 'saveEnded' );
            }
        } );
    },

    loadPromotionsList: function( opts ) {
        var data,
            that = this;

        data = $.extend( {}, data, opts );

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

    loadCumulativeInfo: function( opts ) {
        var data = {},
            that = this;

        data = $.extend( {}, data, opts );

        this.trigger( 'saveStarted' );

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_NOMINATIONS_CUMULATIVE,
            data: data,
            success: function ( serverResp ) {
                var cumulativeInfo = serverResp.data.cumulativeInfo;
                that.set( 'cumulativeInfo', cumulativeInfo );
                that.trigger( 'cumulativeDataLoaded', cumulativeInfo, { paxId: opts.paxId, claimGroupId: opts.claimGroupId } );
                console.log( that );

            },
            error: function( jqXHR, textStatus, errorThrown ) {
                console.log( '[ERROR] nominationsApprovalPageModel: ', jqXHR, textStatus, errorThrown );
            },
            complete: function() {
                that.trigger( 'saveEnded' );
            }
        } );
    },
	translateData: function( $attr ) {
        var that = this,
            data = {
            };
        $.ajax( {
            dataType: 'g5json', //must set this so SeverResponse can parse and return to success()
            data: data,
            url: G5.props.URL_JSON_NOMINATIONS_APPROVALS_TRANSLATE_COMMENT + '?' + $attr,
            type: 'POST',
            success: function( serverResp ) {
                //regular .ajax json object response
                var data = serverResp.data,
                    callback,
                    comment = data.comment;
                if ( serverResp.getFirstError() ) { return; }//ERROR just return for now
                that.trigger( 'translated', comment );
                console.log( '[INFO] Nomination - Reason Translated' );
                if ( typeof callback === 'function' ) { callback(); }
            }
        } );
    },
    loadCumulativePDFInfo: function( opts ) {
        var data = {},
            that = this;

        data = $.extend( {}, data, opts );

        this.trigger( 'saveStarted' );

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_NOMINATIONS_CUMULATIVE,
            data: data,
            success: function ( serverResp ) {

                var cumulativeInfo = serverResp.data.cumulativeInfo;

                var tabularData = that.get( 'tabularData' );
                _.each( tabularData.results, function( result, index ) {

                    if ( typeof opts.paxId !== 'undefined' ) {

                        if ( result.paxId === opts.paxId ) {

                            tabularData.results[ index ].cumulativeInfo = cumulativeInfo;
                        }
                    }
                } );
                console.log( tabularData );
                // console.log(results);
                that.set( 'tabularData', tabularData );
                // that.set('cumulativeInfo', cumulativeInfo);
                if ( typeof tabularData.results[ tabularData.results.length - 1 ].cumulativeInfo !== 'undefined' ) {

                    that.trigger( 'cumulativePDFDataLoaded', tabularData );
                }

            },
            error: function( jqXHR, textStatus, errorThrown ) {
                console.log( '[ERROR] nominationsApprovalPageModel: ', jqXHR, textStatus, errorThrown );
            },
            complete: function() {
                that.trigger( 'saveEnded' );
            }
        } );
    },

    sendBudget: function( budget, id ) {
        var data = {},
            that = this;

        data = {
            budgetIncrease: budget,
            promotionId: id,
            awardType: that.get( 'promotion' ).payoutType
        };

        $.ajax( {
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_NOMINATIONS_SEND_BUDGET_REQUEST,
            data: data,
            success: function () {

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

    setTeamIndividualAward: function( index, id, award ) {
        var results = this.get( 'tabularData' ).results,
            team = _.where( results, { index: index } ),
            pax = _.where( team[ 0 ].teamMembers, { paxId: id } );

        pax[ 0 ].award = award;

    },

    setTeamAward: function( index, teamAward ) {
        var results = this.get( 'tabularData' ).results,
            team = _.where( results, { index: index } );

        team.award = teamAward;
    }

} );
