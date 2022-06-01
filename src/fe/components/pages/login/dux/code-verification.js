import uuid from 'uuid';
import { showSpinner, addMessage, changeForm } from './login';
import ServerResponse from '../../../../utils/server-response';

export const PASSWORD_RESET = 'PASSWORD_RESET';
export const PASSWORD_ENTER = 'PASSWORD_ENTER';

export function passwordReset( verificationData, token ) {
    return { type: PASSWORD_RESET, verificationData, token };
}
export function passwordEnter( verificationData, token ) {
    return { type: PASSWORD_ENTER, verificationData, token };
}
export default function codeVerificationReducer( state = {}, action ) {

    switch ( action.type ) {

        case PASSWORD_RESET:
            return {
                ...state,
                confirmation: action.verificationData,
                token: action.token
            };
        case PASSWORD_ENTER:
            return {
                ...state,
                activation: action.verificationData,
                token: action.token
            };
        default:
            return state;
    }
}
export function sendToken( token, formAction, activation ) {
    return dispatch => {
        dispatch( showSpinner( true ) );
        return fetch( formAction, {
            method: 'post',
            body: JSON.stringify( token ),
            credentials: 'same-origin',
            headers: {
                'post-type': 'ajax',
                'Content-Type': 'application/json',
            },
        } ).then( response => {
            return response.json();
        } )
        .then( verificationData => {
            if( !ServerResponse.default( verificationData ) && verificationData.messages ) {
                //not a redirect, but an error from server
                verificationData.messages.filter( message => {
                    if( message.type != 'success' && message.type != 'serverCommand' ) {
                        dispatch( showSpinner( false ) );
                        dispatch( addMessage( { ...message, id: uuid() } ) );
                    }
                } );
            } else {
                if( verificationData.responseCode != 200 ) {
                    //something went wrong
                    if( verificationData.fieldErrors && verificationData.fieldErrors.length > 0 ) {
                        verificationData.fieldErrors.map( message => {
                            dispatch( showSpinner( false ) );
                            dispatch( addMessage( { ...message, id: uuid() } ) );
                        } );
                    } else {
                        dispatch( addMessage( { message: verificationData.responseMessage, id: uuid() } ) );
                        dispatch( changeForm( 'messageWindow' ) );
                    }
                } else {
                    const success = verificationData.responseMessage;
                    dispatch( addMessage( { success, id: uuid() } ) );
                    if( activation ) {
                        //activation flow - go to create password
                        dispatch( passwordEnter( verificationData, token ) );
                        dispatch( changeForm( 'createPass' ) );
                    } else {
                        //forgot pass flow - got to reset password
                        dispatch( passwordReset( verificationData, token ) );
                        dispatch( changeForm( 'resetPassword' ) );
                    }

                }

                dispatch( showSpinner( false ) );
            }
        }
         ).then()
        .catch( ( error ) => {
            dispatch( showSpinner( false ) );
            dispatch( addMessage( { ...error, id: uuid() } ) );
        } );
    };
}
