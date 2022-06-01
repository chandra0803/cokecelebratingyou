import uuid from 'uuid';
import ServerResponse from '../../../../utils/server-response';
import { Base64 } from 'js-base64';


/*******************************************************************************
 * Initial state
 */
const initialState = {
    ...window.login,
    showSpinner: false,
    emailUnique: true,
    emailAddress: '',
    emailExists: true,
    contactMethods: [],
    messageSent: false,
    toggleModal: false
};

/*
 * action types
 */

export const SHOW_SPINNER = 'SHOW_SPINNER';
export const PASSWORD_VISIBLE = 'PASSWORD_VISIBLE';
export const ADD_MESSAGE = 'ADD_MESSAGE';
export const ADD_MESSAGES = 'ADD_MESSAGES';
export const CHANGE_FORM = 'CHANGE_FORM';
export const TOGGLE_MODAL = 'TOGGLE_MODAL';

/*
 * action creators
 */

export function showSpinner( value ) {
  return { type: SHOW_SPINNER, value };
}

export function passwordVisible( value ) {
  return { type: PASSWORD_VISIBLE, value };
}

export function addMessage( message, id ) {
    if( message.message ) {
        Object.defineProperty( message, 'error',
            Object.getOwnPropertyDescriptor( message, 'message' ) );
        delete message[ 'message' ];
        message.error = message.error.replace( '@', '&#8203;@' );
    } else if ( message.success ) {
        message.success = message.success.replace( '@', '&#8203;@' );
    }
    return { type: ADD_MESSAGE, message, id };
}
export function addMessages( messages ) {
        const errors = [];
        messages.forEach( function( message ) {
            Object.defineProperty( message, 'error',
            Object.getOwnPropertyDescriptor( message, 'message' ) );
            delete message[ 'message' ];
            message.error = message.error.replace( '@', '&#8203;@' );
            errors.push( message );
        } );
    return { type: ADD_MESSAGES, errors };
}
export function changeForm( newForm ) {
    return { type: CHANGE_FORM, newForm };
}

export function toggleModal( display, page ) {
    return { type: TOGGLE_MODAL, display, page };
}

/*******************************************************************************
 * Reducer
 */
export default function loginReducer( state = initialState, action ) {

    switch ( action.type ) {
        case SHOW_SPINNER:
            return {
                ...state,
                showSpinner: action.value
            };
        case PASSWORD_VISIBLE:
            return {
                ...state,
                passwordVisible: action.value
            };
        case ADD_MESSAGE:
            return {
                ...state,
                message: action.message
            };
        case ADD_MESSAGES:
            return {
                ...state,
                messages: action.errors
            };
        case CHANGE_FORM:
            return {
                ...state,
                currentForm: action.newForm
            };
        case TOGGLE_MODAL:
            return {
                ...state,
                toggleModal: action.display,
                modalPage: action.page
            };
        default:
            return state;
    }
}

export function loginFunc( username, password, formAction, strutsToken ) {
    return dispatch => {
        dispatch( showSpinner( true ) );

        const parameters = JSON.stringify( { 'j_username': username, 'j_password': Base64.encode(password) } );
        return fetch( formAction, {
                method: 'post',
                body: parameters,
                credentials: 'same-origin',
                headers: {
                    'post-type': 'ajax',
                    'content-type': 'application/json'
                },
            } )
            .then( response => {
                return response.json();
            } )
            .then( login => {
                if( !ServerResponse.default( login ) && login.messages.length > 0 ) {
                    //login unsuccessful, something other than redirect returned from server with message(s)
                    login.messages.filter( message => {
                        if( message.type != 'success' && message.type != 'serverCommand' ) {
                            //display error messages
                            dispatch( showSpinner( false ) );
                            dispatch( addMessage( { ...message, id: uuid() } ) );
                        }
                    } );
                }
            } )
            .catch( ( error ) => {
                dispatch( showSpinner( false ) );
                dispatch( addMessage( { ...error, id: uuid() } ) );
            } );
    };
}


export function changeFormFunc( newForm ) {
    return dispatch=>{
        if( newForm === 'login' && window.location.search.indexOf( 'cmsLocaleCode' ) < 0 ) {
            const newurl = window.location.protocol + '//' + window.location.host + window.location.pathname;
            window.history.pushState( { path: newurl }, '', newurl );
        }       
        dispatch ( changeForm( newForm ) );
    };
}

export function addMessageFunc( message ) {
    return dispatch=>{
        dispatch( addMessage( { ...message, id: uuid() } ) );
    };
}
export function addMessagesFunc( message ) {
    return dispatch=>{
        dispatch( addMessages( { ...message } ) );
    };
}
export function cmStringsArrayToObject( propertiesArray ) {
    return dispatch=>{

        const propertiesObject = {};
        propertiesArray.forEach( property => {
            if( property.content.substring( 0, 3 ) !== '???' ) {
                propertiesObject[ `${ property.code }.${ property.key.toLowerCase() }` ] = property.content;
            }
        } );
        return propertiesObject;
    };
}
export function toggleModalDisplay( display, page ) {
    return dispatch=>{
        dispatch ( toggleModal( display, page ) );
    };
}
export function togglePasswordVisible( visible ) {
    return dispatch=>{
        dispatch( passwordVisible( visible ) );
    };
}

export function navigateToNewPageFunc( accountLockUrl ) {
  return dispatch=>{
            window.location.href = accountLockUrl;
    };
}
