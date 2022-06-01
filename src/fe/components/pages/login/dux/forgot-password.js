import uuid from 'uuid';
import { showSpinner, addMessage, changeForm, navigateToNewPageFunc } from './login';
import { updateContactData, updateContactMethods } from './method-of-contact';
import { updateActivation } from './activation';
import { fetchDataCountries } from './recovery';


export const UPDATE_PASS_DATA = 'UPDATE_PASS_DATA';

export function updatePass( data ) {
    return { type: UPDATE_PASS_DATA, data };
}


export default function forgotPassReducer( state = {}, action ) {

    switch ( action.type ) {
        case UPDATE_PASS_DATA:
        return {
            ...state,
            details: action.data
        };
        default:
            return state;
    }
}
export function fetchDataForgotPassword( username, formAction ) {
    return dispatch => {
        dispatch( showSpinner( true ) );

    return fetch( formAction, {
            method: 'post',
            body: JSON.stringify( { 'userName': username } ),
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
              if( login.responseCode === 200 && login.contactValue === 'accountLock' ) {
                dispatch( navigateToNewPageFunc( login.developerMessage ) );
              } else {
                dispatch( updatePass( login ) );
                    if( login.fieldErrors && login.fieldErrors.length > 0 ) {
                        //something went wrong, display error messages
                        login.fieldErrors.filter( message => {
                            dispatch( addMessage( { ...message, id: uuid() } ) );
                            dispatch( showSpinner( false ) );

                        } );
                    } else if( login.responseCode !== 200 ) {
                        //something went wrong differently, display error messages
                        const message = login.responseMessage;
                        dispatch( addMessage( { message, id: uuid() } ) );
                    } else {
                        //nothing went wrong
                        const success = login.responseMessage;
                        dispatch( addMessage( { success, id: uuid() } ) );
                        if( login.unique && !login.contactMethods ) {
                            //unique email, no methods of contact, display message sent text
                            dispatch( updateContactData( login ) );
                            dispatch( changeForm( 'messageWindow' ) );
                        } else if( login.unique && login.contactMethods ) {
                            // unique email and methods of contact, update contact methods and go to methodOfContact screen
                            if( login.userActivated ) {
                                dispatch( updateContactData( login ) );
                                dispatch( updateContactMethods( login ) );
                                dispatch( changeForm( 'methodOfContact' ) );
                            }
                        } else if( login.activationFields && login.activationFields.length ) {
                            if( login.nonPax ) {
                                dispatch( updateContactMethods( login ) );
                                dispatch( changeForm( 'methodOfContact' ) );
                            } else {
                                dispatch( updateActivation( login, login.userName ) );
                                dispatch( changeForm( 'activAttr' ) );
                            }
                        }
                    }
                    dispatch( showSpinner( false ) );
              }
            }
         )
         .then()
        .catch( ( error ) => {
            dispatch( showSpinner( false ) );
            dispatch( addMessage( { ...error, id: uuid() } ) );
        } );

    };
}
