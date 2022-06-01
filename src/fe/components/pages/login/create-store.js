/* eslint-disable no-unused-vars */
import { createStore, applyMiddleware, compose, combineReducers } from 'redux';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import thunk from 'redux-thunk';
import { createLogger } from 'redux-logger';


import login from './dux/login.js';
import forgotId from './dux/forgot-id.js';
import forgotPass from './dux/forgot-password.js';
import methodOfContact from './dux/method-of-contact.js';
import verification from './dux/code-verification.js';
import passConfirm from './dux/reset-password';
import activation from './dux/activation';
import attributes from './dux/activation-attr';
import recovery from './dux/recovery';

/*******************************************************************************
 *  Gather all the reducers
 */

export default () => {

    const combinedReducers = combineReducers( {
        login,
        forgotId,
        forgotPass,
        methodOfContact,
        verification,
        passConfirm,
        activation,
        attributes,
        recovery
    } );

    const rootReducer = ( state, action ) => {
        if ( action.type === 'CHANGE_FORM' ) {
        if ( action.newForm === 'login' || action.newForm === 'forgotUserId' || action.newForm === 'forgotPassword' ) {
            state = {};
            window.login.termedUserId = null;
            window.login.termedUserToolTip = false;
            }
          }
        return combinedReducers( state, action );
    };

    /*******************************************************************************
     *  Create Store
     */
     
    const middleware = [ thunk ];

    if ( window.location.port === '8001' ) {
        middleware.push( createLogger( {
            collapsed: true
        } ) );
    }
    const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
    return createStore(
        rootReducer,
        composeEnhancers( applyMiddleware.apply( null, middleware ) )
    );
};
