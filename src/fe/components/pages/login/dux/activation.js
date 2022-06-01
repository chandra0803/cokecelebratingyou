import uuid from 'uuid';
import { showSpinner, addMessage, changeForm } from './login';
import { updateContactMethods } from './method-of-contact';

export const UPDATE_ACTIVATION_DATA = 'UPDATE_ACTIVATION_DATA';


export function updateActivation( data, id ) {
    return { type: UPDATE_ACTIVATION_DATA, data, id };
}

export default function activationReducer( state = {}, action ) {

    switch ( action.type ) {
        case UPDATE_ACTIVATION_DATA:
        return {
            ...state,
            activation: action.data,
            id: action.id
        };
        default:
            return state;
    }
}
export function fetchActivationData( userId, formAction ) {
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
            .then( activation => {
                if( activation.fieldErrors && activation.fieldErrors.length > 0 ) {
                    activation.fieldErrors.filter( message => {
                        if( message.message && message.message.length ) {
                            dispatch( addMessage( { ...message, id: uuid() } ) );
                        }
                        dispatch( showSpinner( false ) );
                    } );
                } else if( activation.responseCode === 200 ) {
                        const success = activation.responseMessage;
                        dispatch( addMessage( { success, id: uuid() } ) );
                        dispatch( updateActivation( activation, userId ) );
                        if( activation.activationFields && activation.activationFields.length > 0 ) {
                            if( activation.nonPax ) {
                                dispatch( updateContactMethods( activation ) );
                                dispatch( changeForm( 'methodOfContact' ) );
                            } else {
                                dispatch( changeForm( 'activAttr' ) );
                            }
                        }else {
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
