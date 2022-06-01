import 'babel-polyfill';
import 'whatwg-fetch';

import { connect } from 'react-redux';
import React from 'react';
import { loginFunc, togglePasswordVisible, changeFormFunc, toggleModalDisplay, cmStringsArrayToObject, addMessageFunc, addMessagesFunc }
from './dux/login.js';
import { fetchData } from './dux/forgot-id.js';
import { fetchDataForgotPassword } from './dux/forgot-password.js';
import { sendContact, sendAutoQuery } from './dux/method-of-contact';
import { sendToken } from './dux/code-verification';
import { submitPass, fetchPasswordRules } from './dux/reset-password';
import { fetchActivationData } from './dux/activation';
import { fetchCollectionData } from './dux/activation-attr';
import { fetchDataCollectRecovery, fetchDataCountries } from './dux/recovery';
import LoginPage from './login-page.js';

import { Provider } from 'react-redux';
import { render } from 'react-dom';
import createStore from './create-store.js';

const LoginApp = connect(
    // mapStateToProps
    state => {
      return {
		...state,
		showSpinner: state.login.showSpinner,
        };
    },
    // mapDispatchToProps
    {
        loginFunc,
		togglePasswordVisible,
		fetchDataForgotPassword,
		fetchData,
		changeFormFunc,
        sendAutoQuery,
		sendContact,
		toggleModalDisplay,
		cmStringsArrayToObject,
        sendToken,
        submitPass,
        fetchPasswordRules,
        addMessageFunc,
        addMessagesFunc,
        fetchActivationData,
        fetchCollectionData,
        fetchDataCollectRecovery,
        fetchDataCountries
    }
)( LoginPage );

render(
	<Provider store={ createStore() }>
		<LoginApp />
	</Provider>,
    document.getElementById( 'page--login' )
);

/* A simple templating method based on Douglas Crockford's supplant. */
/* This method replaces all instances of the ${key} pattern with the */
/* corresponding key's value on the object passed into it. it also uses */
/* the pattern [key: text ${key} text] to signify repetition */
/* patterns. The inner text of the pattern is repeated N times, where N */
/* is the number of items at the key. A zero length array, null, */
/* undefined or empty string on the key removes the pattern entirely. */
/* Otherwise an array containing strings or objects gets applied */
/* recursively to the inner text pattern. */
/* https://gist.github.com/Breton/2497986 */
if ( !String.prototype.template ) {
    String.prototype.template = function ( o ) {
        const tag = /\$\{([^}]*)\}/g;
        const many = /\[([a-zA-Z$_][a-zA-Z$_0-9]*?):([^\]]*)\]/g;


        function promote( v, b ) {
            let tmp;
            if ( typeof v !== 'object' ) {
                tmp = {};
                tmp[ b ] = v;
                v = tmp;
            }


            return v;
        }


        function rmany( a, b, c ) {
            let r = ( o[ b ] === 0 ? 0 : o[ b ] || [] ), i, set;
            r = promote( r, b );
            if ( typeof r.length !== 'number' ) {
                r = [ r ];
            }


            for ( i = 0, set = []; i < r.length; i += 1 ) {
                r[ i ] = promote( r[ i ], b );
                set[ i ] = c.template( r[ i ] );
            }


            return set.join( '' );
        }


        function rtag( a, b ) {
            const r = o[ b ];
            return typeof r === 'string' || typeof r === 'number' ? r : '';
        }

        return this.replace( many, rmany ).replace( tag, rtag );
    };
}
