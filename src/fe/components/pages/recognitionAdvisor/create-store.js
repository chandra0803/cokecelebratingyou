/* eslint-disable no-unused-vars */
import { createStore, applyMiddleware, compose, combineReducers } from 'redux';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import thunk from 'redux-thunk';
import { createLogger } from 'redux-logger';


import recognitionAdvisor from './dux/recognitionAdvisor.js';

/*******************************************************************************
 *  Gather all the reducers
 */

export default () => {

    const combinedReducers = combineReducers( {
        recognitionAdvisor
    } );

    const rootReducer = ( state, action ) => {
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
