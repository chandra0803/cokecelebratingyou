import uuid from 'uuid';
import { showSpinner, addMessage, changeForm } from './login';
import { updateContactMethods } from './method-of-contact';

export const UPDATE_LOGIN_DATA = 'UPDATE_LOGIN_DATA';


export function updatelogin( data, emailOrPhone ) {
    return { type: UPDATE_LOGIN_DATA, data, emailOrPhone };
}

export default function forgotIdReducer( state = {}, action ) {

    switch ( action.type ) {
        case UPDATE_LOGIN_DATA:
        return {
            ...state,
            details: action.data,
            emailOrPhone: action.emailOrPhone
        };
        default:
            return state;
    }
}
export function fetchData( emailOrPhone, formAction ) {
    return dispatch => {
        dispatch( showSpinner( true ) );

        return fetch( formAction, {
                method: 'post',
                body: JSON.stringify( { 'emailOrPhone': emailOrPhone } ),
                credentials: 'same-origin',
                headers: {
                    'post-type': 'ajax',
                    'Content-Type': 'application/json',
                },
            } )
            .then( response => {
                return response.json();
            } )
            .then( login => {
                dispatch( updatelogin( login, emailOrPhone ) );
                if( login.fieldErrors && login.fieldErrors.length > 0 ) {
                    //errors - display them
                    login.fieldErrors.filter( message => {
                        if( message.message && message.message.length ) {
                            dispatch( addMessage( { ...message, id: uuid() } ) );
                        }
                        dispatch( showSpinner( false ) );
                    } );
                } else if( login.responseMessage.length ) {
                    const success = login.responseMessage;
                        dispatch( addMessage( { success, id: uuid() } ) );
                        if( login.contactMethods && login.contactMethods.length > 1 ) {
                            //multiple methods of contact
                            let shared;
                            if( login.single ) {
                                shared = false;
                            } else {
                                shared = true;
                            }
                            dispatch( updateContactMethods( login, shared ) );
                            dispatch( changeForm( 'methodOfContact' ) );
                        } else if( login.unique === true ) {
                            //unique primary email
                            dispatch( changeForm( 'messageWindow' ) );
                        }
                    }
                    dispatch( showSpinner( false ) );
                }
             )
            .then()
            .catch( ( error ) => {
                dispatch( showSpinner( false ) );
                dispatch( addMessage( { ...error, id: uuid() } ) );
            } );

    };
}
