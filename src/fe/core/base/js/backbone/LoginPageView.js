/*exported LoginPageView */
/*global
PageView,
TemplateManager,
LoginFormView,
LoginPageView:true
*/
LoginPageView = PageView.extend( {

    //override super-class initialize function
    initialize: function() {
        //set the appname (getTpl() method uses this)
        this.appName = '';

        //set the initial local code
        this.localeCode = this.languageParam( 'cmsLocaleCode' ) || 'en_US';

        //this is how we call the super-class initialize function (inherit its magic)
        this.constructor.__super__.initialize.apply( this, arguments );

        //inherit events from the superclass ModuleView
        this.events = _.extend( {}, this.constructor.__super__.events, this.events );

        this.render();
    },

    events: {
        'click .trigger-modal': 'addModalContent',
        'change #language-select': 'changeLanguage',
        'keypress input[type=password]': 'focusOnEnter'
    },

    render: function() {
        // set select to localeCode
        this.$el.find( 'select option[value=' + this.localeCode + ']' ).prop( 'selected', true );

        // focus on the first text field
        this.$el.find( 'input:first' ).focus();

        this.formView = new LoginFormView( {
            $el: this.$el.find( 'form' )
        } );
    },

    focusOnEnter: function( e ) {
        if( $.browser.msie && e.keyCode === 13 ) {
            this.$el.find( '#loginFormSubmit' ).focus();
        }
    },

    changeLanguage: function( e ) {
        var localeCode = e.target.value,
            url = window.location.href;

        if( url.indexOf( 'cmsLocaleCode' ) >= 0 ) {
            url = url.replace( /cmsLocaleCode=([a-z|A-Z|_])+/i, 'cmsLocaleCode=' + localeCode );
        }
        else {
            url = url + ( url.indexOf( '?' ) >= 0 ? '&' : '?' ) + 'cmsLocaleCode=' + localeCode;
        }

        window.location.href = url;
    },

    addModalContent: function( e ) {
        var $target = $( '#' + e.target.id + 'Modal .modal-body-footer' ),
            template = e.target.id;

        $( '.qtip' ).hide();
        $( '.error' ).removeClass( 'error' );

        TemplateManager.get( template, function( tpl ) {
            $target.empty().html( tpl( {} ) ); // if template === 'loginPageFormContact' used for handlebars to not render some fields
            $target.find( '.cancel-btn' ).removeAttr( 'href' ).attr( 'data-dismiss', 'modal' );

            $target.formView = new LoginFormView( {
                $el: $target.find( 'form' )
            } );
        }, G5.props.URL_TPL_ROOT || G5.props.URL_BASE_ROOT + 'tpl/' );

    },

    languageParam: function( name ) {
        var results = new RegExp( '[\?&]' + name + '=([^&#]*)' ).exec( window.location.href );
        if ( results === null ) {
           return null;
        }
        else{
            return results[ 1 ] || 0;
        }
    }
} );