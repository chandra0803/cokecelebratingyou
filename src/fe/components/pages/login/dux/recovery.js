import uuid from 'uuid';
import { showSpinner, addMessage, changeForm } from './login';
import ServerResponse from '../../../../utils/server-response';
import { updateContactData, updateContactMethods } from './method-of-contact';

export const COLLECT_RECOVERY_DATA = 'COLLECT_RECOVERY_DATA';
export const GET_COUNTRY_DATA = 'GET_COUNTRY_DATA';

export function collectRecovery( data ) {
    return { type: COLLECT_RECOVERY_DATA, data };
}
export function getCountries( data ) {
    return { type: GET_COUNTRY_DATA, data };
}

export default function collectRecoveryReducer( state = {}, action ) {

    switch ( action.type ) {
        case COLLECT_RECOVERY_DATA:
        return {
            ...state,
            recovery: action.data
        };
        case GET_COUNTRY_DATA:
        return {
            ...state,
            countries: action.data
        };
        default:
            return state;
    }
}
export function fetchDataCollectRecovery( contact, formAction, final ) {
    return dispatch => {
        dispatch( showSpinner( true ) );
        contact.finalLogin = final;
    return fetch( formAction, {
            method: 'post',
            body: JSON.stringify( contact ),
            credentials: 'same-origin',
            headers: {
                'post-type': 'ajax',
                'Content-Type': 'application/json',
            },
        } )
        .then( response => {
            return response.json();
        } )
        .then( contactResponse => {
            if( !ServerResponse.default( contactResponse ) && contactResponse.messages ) {
                contactResponse.messages.filter( message => {
                    if( message.type != 'success' && message.type != 'serverCommand' ) {
                        dispatch( addMessage( { ...message, id: uuid() } ) );
                    }
                } );
            } else {
                dispatch( collectRecovery( contactResponse ) );
                if( contactResponse.fieldErrors && contactResponse.fieldErrors.length > 0 ) {
                    contactResponse.fieldErrors.filter( message => {
                        dispatch( addMessage( { ...message, id: uuid() } ) );
                        dispatch( showSpinner( false ) );

                    } );
                } else if( contactResponse.responseCode !== 200 ) {
                    const message = contactResponse.responseMessage;
                    dispatch( addMessage( { message, id: uuid() } ) );
                    dispatch( showSpinner( false ) );
                } else if( contactResponse.contactMethods && contactResponse.contactMethods.length ) {
                    // unique email and methods of contact, update contact methods and go to methodOfContact screen
                    dispatch( updateContactData( contactResponse ) );
                    dispatch( updateContactMethods( contactResponse ) );
                    dispatch( changeForm( 'methodOfContact' ) );
                    dispatch( showSpinner( false ) );
                }
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
export function fetchDataCountries( formAction ) {
    return dispatch => {
        dispatch( showSpinner( true ) );

    return fetch( formAction, {
            method: 'get',
            credentials: 'same-origin',
            headers: {
                'post-type': 'ajax',
                'Content-Type': 'application/json',
            },
        } )
        .then( response => {
            return response.json();
        } )
        .then( contactResponse => {
            dispatch( getCountries( contactResponse ) );
            dispatch( changeForm( 'recovery' ) );
                if( contactResponse.fieldErrors && contactResponse.fieldErrors.length > 0 ) {
                    contactResponse.fieldErrors.filter( message => {
                        dispatch( addMessage( { ...message, id: uuid() } ) );
                        dispatch( showSpinner( false ) );
                    } );
                } else if( contactResponse.responseCode !== 200 ) {
                    const message = contactResponse.responseMessage;
                    dispatch( addMessage( { message, id: uuid() } ) );
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
