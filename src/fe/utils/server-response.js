// this module is largely a replica of src/fe/core/base/js/backbone/ServerResponse.js
// it's just here to be used in the new React/ES6 world
var commands = {
	redirect: function( cmd ) {
        if( cmd.url ) {
            window.location = cmd.url;
        }
	}
};
module.exports = {
	commands: commands,
	default: function( response ) {
		if( response.messages && response.messages.length > 0 ) {

			for( var i = 0; i < response.messages.length; i++ ) {
				console.log( response.messages[ i ] );
				if( response.messages[ i ].type === 'serverCommand' ) {
					if( typeof commands[ response.messages[ i ].command ] === 'function' ) {
						commands[ response.messages[ i ].command ]( response.messages[ i ] );
					}
				}
			}
		}
	}
};
// exports['default'] = function( response ) {
// 	if( response.type === 'serverCommand' ) {
// 		if( response.cmd === 'redirect' ) {
// 			console.log( 'handle the redirect command' );
// 		}
// 	}
// };
