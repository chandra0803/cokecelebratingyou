/*jslint browser: true, nomen: true, devel: false, unparam: true*/
/*exported ClaimModuleView*/
/*global
console,
_,
$,
G5,
LaunchModuleView,
TemplateManager,
tpl,
ClaimModuleView:true
*/
ClaimModuleView = LaunchModuleView.extend( {

    //override super-class initialize function
    initialize: function( opts ) {

        //this is how we call the super-class initialize function (inherit its magic)
        LaunchModuleView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass LaunchModuleView
        this.events = _.extend( {}, LaunchModuleView.prototype.events, this.events );

        //on template loaded and attached
        this.on( 'templateLoaded', function() {
            //console.log('The template is done, ajax firing');
            this.fetchClaims();

            // start the loading state and spinner
            this.dataLoad( true );
        } );
    },
    fetchClaims: function() {
        //console.log("[INFO] ClaimModuleView: fetchClaims called");

        var self    = this,
            tplName = 'claimModuleItem',
            tplUrl  = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'productClaim/tpl/',
            render;
            if ( typeof G5.props.URL_JSON_CLAIM_ACTIVITY == 'string' || G5.props.URL_JSON_CLAIM_ACTIVITY instanceof String ) {
            //console.log("IT was a string.");
                render  = function( tpl ) {

                    $.ajax( {
                        dataType: 'g5json',
                        type: 'POST',
                        url: G5.props.URL_JSON_CLAIM_ACTIVITY,
                        // data: params,
                        success: function( serverResp ) {
                            //regular .ajax json object response
                            //console.log("[INFO] ClaimModuleView: LoadData ajax call successfully returned this JSON object: ", serverResp);

                            var claims = serverResp.data.claimApprovals;

                            self.renderClaims( tpl, claims );
                        }
                    } );
                };
                TemplateManager.get( tplName, function( tpl ) { return render( tpl ); }, tplUrl );
            } else {
                console.log( '[INFO] ClaimModuleView: Data loaded from bootstrap: ', G5.props.URL_JSON_CLAIM_ACTIVITY );
                var claims = G5.props.URL_JSON_CLAIM_ACTIVITY.claimApprovals;
                self.renderClaims( tpl, claims );
                TemplateManager.get( tplName, function( tpl ) { return render( tpl ); }, tplUrl );
            }
    },
    renderClaims: function( template, results, callback ) {
        //console.log("[INFO] ClaimModuleView: renderClaims called");

        var $claimsWrapper = this.$el.find( '.claimsWrap ul' );

        // stop the loading state and spinner
        this.dataLoad( false );

        if( results.length <= 0 ) {
            results.push( { _noResults: true } );
        }
        _.forEach( results, function( result, i ) {
            result.eo = ( i % 2 === 0 ) ? 'odd' : 'even';
            $claimsWrapper.append( template( result ) );
        } );
    }

} );
