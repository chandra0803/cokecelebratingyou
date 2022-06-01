/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported NominationsSubmitTabBehaviorView*/
/*global
console,
$,
_,
G5,
Backbone,
PageView,
TemplateManager,
NominationsSubmitTabBehaviorView:true
*/
NominationsSubmitTabBehaviorView = PageView.extend( {
    initialize: function ( opts ) {

        var that = this;

        // NominationsSubmitPageView parent container for this tab
        this.containerView = opts.containerView;

        this.selectedPromoModel = this.containerView.model.get( 'promotion' );

        //template names
        this.tpl = 'nominationsSubmitBehaviorsTpl';
        this.tplPath = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'nominations/tpl/';

        this.render();

        this.containerView.wizTabs.on( 'tabsInitialized', function() {
            that.updateTabName( 'stepBehavior' );
        } );

    },

    events: {
        'click .behaviorList input': 'compareAllowedAndSelected'
    },

    render: function() {
        var $container = this.$el,
            that = this;

        TemplateManager.get( this.tpl, function( tpl ) {

            $container.empty().append( tpl( that.selectedPromoModel ) );


            that.updateTabName( 'stepBehavior' );


        }, this.tplPath );
    },

    checkSelectedInputs: function() {
        var $checkboxes = this.$el.find( '.behaviorList input[type="checkbox"]' ),
            numberChecked = 0;

        _.each( $checkboxes, function( cb ) {
            if ( cb.checked ) {
                numberChecked++;
            }
        } );

        return numberChecked;

    },

    compareAllowedAndSelected: function( e ) {
        var maxAllowed = this.selectedPromoModel.moreThanOneBehavioursAllowed,
            $checkboxes = this.$el.find( '.behaviorList input[type="checkbox"]' ),
            $behaviorListItem = this.$el.find( '.behaviorList li' ),
            $tar = $( e.currentTarget ),
            numberChecked = this.checkSelectedInputs(),
            id = $tar.data( 'id' ).toString(),
            isSelected = $tar.is( ':checked' );

        $tar.parents( 'li' ).toggleClass( 'active' );

        if ( !maxAllowed && numberChecked > 1 ) {
            $tar.parents( 'li' ).removeClass( 'active' );

            $behaviorListItem.not( '.active' )
                .addClass( 'disabled' )
                .find( $checkboxes )
                .attr( 'disabled', 'disabled' );

            $tar.attr( 'checked', false );
            isSelected = false;
        } else {
            $behaviorListItem.removeClass( 'disabled' );
            $checkboxes.removeAttr( 'disabled' );
        }

        this.containerView.model.setBehavior( id, isSelected );
    },

    updateTabName: function( tab ) {
        var $fromTab = this.containerView.$el.find( ".wtTabVert[data-tab-name='" + tab + "']" ),
            $selectedText = this.$el.find( '.behaviorSelected' ).text(),
            $numberSelected = this.$el.find( '.behaviorMultiSelected .numSelected' ).text( this.checkSelectedInputs() + '\x20' ),
            $multiSelectedText = this.$el.find( '.behaviorMultiSelected' ).text(),
            $notSelectedText = this.$el.find( '.behaviorNotSelected' ).text();


        $fromTab.find( '.wtvTabName' ).show();
        console.log( $numberSelected );
        console.log( $notSelectedText );
        if ( this.checkSelectedInputs() === 0 ) {
            $fromTab.find( '.wtvTabName' ).show();
        }        else if ( this.checkSelectedInputs() === 1 ) {
            $fromTab.find( '.wtvDisplay' ).text( $selectedText );
            $fromTab.find( '.wtvTabName' ).hide();
        }        else if ( this.checkSelectedInputs() > 1 ) {
            $fromTab.find( '.wtvDisplay' ).text( $multiSelectedText );
            $fromTab.find( '.wtvTabName' ).hide();
        }
    },

    validate: function() {
        var $validate = this.$el.find( '.validateme:visible' ),
            isValid = G5.util.formValidate( $validate );

        // failed generic validation tests (requireds mostly)
        if ( !isValid ) {
            return { msgClass: 'msgGenericError', valid: false };
        }

        if ( this.checkSelectedInputs() === 0 ) {
            return { msgClass: 'msgNoBehaviors', valid: false };
        }

        return { valid: true };
    }

} );
