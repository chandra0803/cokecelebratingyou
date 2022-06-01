import uuid from 'uuid';
import { showSpinner, addMessage, changeForm } from './login';
import { getCountries } from './recovery';



export const UPDATE_CONTACT_METHODS = 'UPDATE_CONTACT_METHODS';
export const UPDATE_CONTACT_DATA = 'UPDATE_CONTACT_DATA';


export function updateContactData( contactData ) {
    return { type: UPDATE_CONTACT_DATA, contactData };
}

export function updateContactMethods( contactMethods, shared ) {
    return { type: UPDATE_CONTACT_METHODS, contactMethods, shared };
}

export default function methodOfContactReducer( state = {}, action ) {
    switch ( action.type ) {

        case UPDATE_CONTACT_METHODS:
            return {
                ...state,
                contactMethods: action.contactMethods,
                shared: action.shared
            };
        case UPDATE_CONTACT_DATA:
            return {
                ...state,
                contactMsg: action.contactData
            };
        default:
            return state;
    }
}
export function sendAutoQuery( queryString, targetUrl, emailOrPhone ) {
    return dispatch => {

    return fetch( targetUrl, {
            method: 'post',
            body: JSON.stringify( { emailOrPhone: queryString, initialQuery: emailOrPhone } ),
            credentials: 'same-origin',
            headers: {
                'post-type': 'ajax',
                'Content-Type': 'application/json',
            },
        } ).then( response => {
            return response.json();
        } )
        .then( autoResponse => {
                if( autoResponse.fieldErrors && autoResponse.fieldErrors.length > 0 ) {
                    //something went wrong, display errors
                    autoResponse.fieldErrors.map( message => {
                        if( autoResponse.responseCode != 200 ) {
                            dispatch( addMessage( { ...message, id: uuid() } ) );
                        }
                    } );
                } else {
                    //successful query, display updated contact methods
                    const success = autoResponse.responseMessage;
                    //if no contact methods, set to empty array to empty result set, otherwise last result set persists
                    if( !autoResponse.contactMethods ) {
                        autoResponse.contactMethods = [ ];
                    }
                    dispatch( addMessage( { success, id: uuid() } ) );
                    dispatch( updateContactMethods( autoResponse, true ) );

                }

            }
         ).then()
        .catch( ( error ) => {
            dispatch( addMessage( { ...error, id: uuid() } ) );
        } );
    };
}
export function sendContact( contactObj, targetUrl, user, shared ) {
    return dispatch => {
        dispatch( showSpinner( true ) );

    return fetch( targetUrl, {
            method: 'post',
            body: JSON.stringify( contactObj ),
            credentials: 'same-origin',
            headers: {
                'post-type': 'ajax',
                'Content-Type': 'application/json',
            },
        } ).then( response => {
            return response.json();
        } )
        .then( contactData => {
                if( contactData.termedUserToolTip ) {
                  window.login.termedUserToolTip = contactData.termedUserToolTip;
                  window.login.termedUserId = contactData.termedUserId;
                }
                else {
                  window.login.termedUserToolTip = false;
                  window.login.termedUserId = null;
                }
                if( contactData.single === true ) {
                    shared = false;
                }
                if( contactData.fieldErrors && contactData.fieldErrors.length > 0 ) {
                    //something went wrong, display error messages
                    contactData.fieldErrors.map( message => {
                        if( contactData.responseCode != 200 ) {
                            dispatch( showSpinner( false ) );
                            dispatch( addMessage( { ...message, id: uuid() } ) );
                        }
                    } );
                } else if( contactData.termedUserAllowToRedeem ) {
                  // Facility for the termed user to do the account activation. Also have a facility do not select the existing recovery methods
                  dispatch( getCountries( contactData ) );
                  dispatch( changeForm( 'recovery' ) );
                } else {
                    //success, update contact data and add success message
                    const success = contactData.responseMessage;
                    dispatch( addMessage( { success, id: uuid() } ) );
                    dispatch( updateContactData( contactData ) );
                    if( contactData.contactMethods && contactData.contactMethods.length ) {
                        //contact methods returned - user identified, select method of contact
                        dispatch( updateContactMethods( contactData, shared ) );
                    } else {
                        if( contactObj.contactType === 'PHONE' && !user ) {
                            //user selected mobile phone as contact method, send to code verification screen
                            dispatch( changeForm( 'codeVerification' ) );
                        } else if( !shared && contactObj.sendMessage != false ) {
                            //user selected email addess as contact method, display message sent text on message screen
                            dispatch( changeForm( 'messageWindow' ) );
                        }
                    }

                }

                dispatch( showSpinner( false ) );
            }
         ).then()
        .catch( ( error ) => {
            dispatch( showSpinner( false ) );
            dispatch( addMessage( { ...error, id: uuid() } ) );
        } );
    };
}
