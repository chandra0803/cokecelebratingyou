/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*global
console,
$,
_,
Backbone,
G5,
PageView,
SelectPointsOrTrainingView:true
*/
SelectPointsOrTrainingView = PageView.extend({
    //override super-class initialize function
    initialize: function (opts) {
        var that = this;

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply(this, arguments);

        //inherit events from the superclass
        this.events = _.extend({}, PageView.prototype.events, this.events);
    },

    events: {
        'click #confirmButton': 'openConfirmPopup',
        'click #cancelConfirm': 'closeConfirmPopup',
        'click #selectPointsConfirm .close': 'closeConfirmPopup',
        'click #submitConfirm': 'formSubmit',
        'change .award_type_selected': 'radioChange',
        "click .profile-popover": 'attachParticipantPopover'
    },

    openConfirmPopup: function (e) {
        e.preventDefault();

        var selectedValue = this.$el.find("input[name=award_type_selected]:checked").val(),
            awardValueHolder = this.$el.find('.selected_award');

        this.$el.find('#selectPointsConfirm').modal();
        awardValueHolder.text(selectedValue);

        this.changeAwardButtonText();
    },

    closeConfirmPopup: function (e) {
        this.$el.find('#selectPointsConfirm').modal( 'hide' );
        this.changeAwardButtonText();
    },

    formSubmit: function (e) {
        $form = this.$el.find('.selectpointsformPage');

        var selectedValue = this.$el.find("input[name=award_type_selected]:checked").val(),
            pointsValue = this.$el.find( '#selectPointsValue' ).val(),
            that = this,
            params = $form.serializeArray();

        this.$el.find('#selectPointsConfirm').modal( 'hide' );

        G5.util.showSpin(this.$el, {
            cover : true,
            classes : 'pageView'
        });

        $.ajax({
            dataType: 'g5json',
            type: "POST",
            url: G5.props.URL_JSON_NOMINATION_RECIPIENT_SELECTION,
            data: params,
            success: function ( serverResp ) {

                that.$el.find('#selectPointsSuccess').modal();     

                if( selectedValue === 'points' ) {
                    $('#selectPointsSuccess').find( '.pointsSuccess' ).removeClass( 'hide' );
                    $('#selectPointsSuccess').find( '.pointsNumber' ).text( pointsValue );
                }   else if ( selectedValue === 'training' ) {
                    $('#selectPointsSuccess').find( '.trainingSuccess' ).removeClass( 'hide' );
                }   else {
                    $('#selectPointsSuccess').find( '.pointsOptout' ).removeClass( 'hide' );
                }
                
                G5.util.hideSpin( that.$el );
                               
            },
            error: function(jqXHR, textStatus){
                console.log( "[INFO] SelectPointsOrTrainingView: recipient selection failed: " + textStatus );
            }
        })
    },

    radioChange: function (e) {
        var selectedValue = this.$el.find("input[name=award_type_selected]:checked").val();

        if( selectedValue !== 'points' ) {
            this.$el.find( '#selectPointsValue' ).attr( 'disabled', 'disabled' );
        }

        this.$el.find('#confirmButton').removeAttr('disabled');
        this.changeAwardButtonText();
    },

    changeAwardButtonText: function() {
        var selectedValue = this.$el.find("input[name=award_type_selected]:checked").val();

        if( selectedValue === 'decline' ) {
            $( '#confirmButton' ).text( 'Decline Award' );
            $( '#submitConfirm' ).val( 'Decline Award' );
        } else {
            $( '#confirmButton' ).text( 'Claim Award' );
            $( '#submitConfirm' ).val( 'Claim Award' );
        }
    },

    attachParticipantPopover: function ( e ) {
        'use strict';
        var $tar = $( e.target );

        //attach participant popovers
        if (!$tar.data('participantPopover')) {
            $tar.participantPopover().qtip('show');
        }
        e.preventDefault();
    }
})
