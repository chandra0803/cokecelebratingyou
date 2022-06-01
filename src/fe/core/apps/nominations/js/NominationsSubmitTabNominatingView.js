/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported NominationsSubmitTabNominatingView */
/*global
console,
$,
_,
G5,
PageView,
TemplateManager,
NominationsSubmitTabNominatingView:true
*/
NominationsSubmitTabNominatingView = PageView.extend( {
    initialize: function ( opts ) {
        var self = this;

        // NominationsSubmitPageView parent container for this tab
        this.containerView = opts.containerView;

        this.selectedPromoModel = this.containerView.model.get( 'promotion' );

        //template names
        this.tpl = 'nominationsSubmitNominatingTpl';
        this.tplPath = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'nominations/tpl/';

        this.render();

        this.on( 'rendered', function() {

            self.updateTabName( 'stepNominating' );

        } );
    },

    events: {
        'click .radio input': 'toggleActiveClass'
    },

    render: function() {
        var $container = this.$el,
            self = this;

        TemplateManager.get( this.tpl, function( tpl, vars, subTpls ) {

            $container.empty().append( tpl( self.selectedPromoModel ) );

            self.checkNominatingType();

            self.trigger( 'rendered' );

        }, this.tplPath );
    },

    toggleActiveClass: function( e ) {
        var $tar = $( e.target ),
            $activeLabel = this.$el.find( '.radio.active' );

        $activeLabel.removeClass( 'active' );
        $tar.parents( 'label' ).addClass( 'active' );
        this.containerView.trigger( 'nomTypeChange' );

    },

    checkNominatingType: function() {
        var nomType = this.selectedPromoModel.nominatingType,
            $inputs = this.$el.find( '.radio input' ),
            $indInput = $inputs.closest( '.individualNominee' ),
            $teamInput = $inputs.closest( '.teamNominee' ),
            $orSeparator = this.$el.find( '.orSeparator' );

        if( nomType === 'individual' ) {
            $indInput.attr( 'checked', true ).parents( '.radio' ).addClass( 'active' );
            $teamInput.parents( '.radio' ).hide();
            $orSeparator.hide();

            this.containerView.$el.find( '.stepNominating' ).addClass( 'noEdit' );

        } else if ( nomType === 'team' ) {
            $teamInput.attr( 'checked', true ).parents( '.radio' ).addClass( 'active' );
            $indInput.parents( '.radio' ).hide();
            $orSeparator.hide();

            this.containerView.$el.find( '.stepNominating' ).addClass( 'noEdit' );
        }
    },

    updateTabName: function( tab ) {
        var $fromTab = $( '.stepNominating' );

            $fromTab.find( '.wtvTabName' ).show();

            if( this.selectedPromoModel.individualOrTeam ) {

                $fromTab.find( '.wtvDisplay' ).text( this.selectedPromoModel.individualOrTeam );

                $fromTab.find( '.wtvTabName' ).hide();
            }

            this.updateTab();

    },

    updateTab: function() {
        var $individualInp = this.$el.find( '.individualNominee' ),
            $teamInp = this.$el.find( '.teamNominee' ),
            individualOrTeam;


        if( $individualInp.is( ':checked' ) ) {
            individualOrTeam = 'Individual';
        } else if( $teamInp.is( ':checked' ) ) {
            individualOrTeam = 'Team';
        }

        this.containerView.model.getCertImages( individualOrTeam );
    },

    // validate the state of elements within this tab
    validate: function() {
        var $validate = this.$el.find( '.validateme:visible' ),
            isValid = G5.util.formValidate( $validate ),
            $nodeSelect = this.containerView.$el.find( '.nominationOrgSection .validateme' ),
            isNodeValid = G5.util.formValidate( $nodeSelect );

        // failed generic validation tests (requireds mostly)
        if( !isValid ) {
            return { msgClass: 'msgGenericError', valid: false };
        }

        if( !isNodeValid ) {
            return { msgClass: 'msgGenericError', valid: false };
        }

        return { valid: true };
    }

} );
