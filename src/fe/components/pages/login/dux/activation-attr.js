import uuid from 'uuid';
import { showSpinner, addMessage, changeForm } from './login';
import { updateContactMethods } from './method-of-contact';
import { getCountries } from './recovery';

export const UPDATE_ATTRIBUTE_DATA = 'UPDATE_ATTRIBUTE_DATA';


export function updateAttributes( data ) {
    return { type: UPDATE_ATTRIBUTE_DATA, data };
}

export default function attributeReducer( state = {}, action ) {

    switch ( action.type ) {
        case UPDATE_ATTRIBUTE_DATA:
        return {
            ...state,
            attributes: action.data
        };
        default:
            return state;
    }
}
export function fetchCollectionData( attributeData, formAction, token ) {
    return dispatch => {
        dispatch( showSpinner( true ) );
        return fetch( formAction, {
                method: 'post',
                body: JSON.stringify( attributeData ),
                credentials: 'same-origin',
                headers: {
                    'post-type': 'ajax',
                    'Content-Type': 'application/json',
                },
            } )
            .then( response => {
                return response.json();
            } )
            .then( attributes => {
                if( attributes.fieldErrors && attributes.fieldErrors.length > 0 ) {
                    attributes.fieldErrors.filter( message => {
                        if( message.message && message.message.length ) {
                            dispatch( addMessage( { ...message, id: uuid() } ) );
                        }
                        dispatch( showSpinner( false ) );
                    } );
                } else if( attributes.responseCode === 200 ) {
                    const success = attributes.responseMessage;
                    dispatch( addMessage( { success, id: uuid() } ) );
                    dispatch( updateAttributes( attributes ) );
                    if( attributes.hasRecoveryMethods ) {
                        dispatch( updateContactMethods( attributes ) );
                        dispatch( changeForm( 'methodOfContact' ) );
                    } else {
                        dispatch( getCountries( attributes ) );
                        dispatch( changeForm( 'recovery' ) );
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
