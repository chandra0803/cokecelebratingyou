import uuid from 'uuid';
import { showSpinner, addMessage, changeForm, addMessages } from './login';
import ServerResponse from '../../../../utils/server-response';

export const PASSWORD_SUBMIT = 'PASSWORD_SUBMIT';
export const PASSWORD_RULES = 'PASSWORD_RULES';


export function passwordSubmit( passwordResponse ) {
    return { type: PASSWORD_SUBMIT, passwordResponse };
}
export function addPassRules( rules, labels, minRules, minRulesLabel, ignoreValidation ) {
    return { type: PASSWORD_RULES, rules, labels, minRules, minRulesLabel, ignoreValidation };
}
export default function passwordResetReducer( state = {}, action ) {

    switch ( action.type ) {

        case PASSWORD_SUBMIT:
            return {
                ...state,
                confirmation: action.passwordResponse
            };
        case PASSWORD_RULES:
            return {
                ...state,
                ...action.rules,
                ...action.labels,
                minRules: action.minRules,
                minRulesLabel: action.minRulesLabel,
                ignoreValidation: action.ignoreValidation
            };
        default:
            return state;
    }
}

export function submitPass( passObj, formAction, create ) {
    return dispatch => {
        dispatch( showSpinner( true ) );

    return fetch( formAction, {
            method: 'post',
            body: JSON.stringify( passObj ),
            credentials: 'same-origin',
            headers: {
                'post-type': 'ajax',
                'Content-Type': 'application/json',
            },
        } ).then( response => {
            return response.json();
        } )
        .then( passwordResponse => {
            if( !ServerResponse.default( passwordResponse ) && passwordResponse.messages ) {
                //not a redirect or success, but an error from the server
                passwordResponse.messages.filter( message => {
                    if( message.type != 'success' && message.type != 'serverCommand' ) {
                        dispatch( showSpinner( false ) );
                        dispatch( addMessage( { ...message, id: uuid() } ) );
                    }
                } );
            } else if( passwordResponse.responseCode != 200 ) {
                if( passwordResponse.fieldErrors && passwordResponse.fieldErrors.length > 0 ) {
                    //something went wrong, add message and display
                            dispatch( showSpinner( false ) );
                            dispatch( addMessages( passwordResponse.fieldErrors ) );

                } else {
                    //something went wrong differently, add message and change screen to message windo
                    dispatch( addMessage( { message: passwordResponse.responseMessage, id: uuid() } ) );
                    dispatch( changeForm( 'messageWindow' ) );
                    dispatch( showSpinner( false ) );
                }
            } else if ( create ) {
                //came from create password, not reset password - add message and change form to recovery
                const success = passwordResponse.responseMessage;
                dispatch( addMessage( { success, id: uuid() } ) );
                dispatch( passwordSubmit( passwordResponse ) );
                dispatch( changeForm( 'recovery' ) );
                dispatch( showSpinner( false ) );
            } else {
                //came from reset password, not create password - add message and change to message screen
                const success = passwordResponse.responseMessage;
                dispatch( addMessage( { success, id: uuid() } ) );
                dispatch( passwordSubmit( passwordResponse ) );
                dispatch( changeForm( 'messageWindow' ) );
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
export function fetchPasswordRules( formAction ) {
    return dispatch => {

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
        .then( passwordRules => {
            //set rules for password validation
            const rules = setRules( passwordRules );
            //set labels for password helper
            const labels = setLabels( passwordRules );
            dispatch( addPassRules( rules, labels, passwordRules.distinctCharacterTypes, passwordRules.distinctCharacterTypesCheck.label, passwordRules.ignoreValidation ) );
            } )
         .then()
        .catch( ( error ) => {
            dispatch( addMessage( { ...error, id: uuid() } ) );
        } );

    };
}
export function setRules( passwordRules ) {
    const { lowerCase, upperCase, specialCharacter, numeric } = passwordRules;
    let numUsed = 0;
    if( lowerCase.available === true ) {
        numUsed++;
    }
    if( upperCase.available === true ) {
        numUsed++;
    }
    if( specialCharacter.available === true ) {
        numUsed++;
    }
    if( numeric.available === true ) {
        numUsed++;
    }
    return {
        passwordRules: {
            isLowerCaseRequired: lowerCase.available,
            isUpperCaseRequired: upperCase.available,
            isSpecialCharRequired: specialCharacter.available,
            isNumberRequired: numeric.available,
            minLength: passwordRules.minLength,
            numUsed: numUsed
        }
    };
}
export function setLabels( passwordRules ) {
    const { lowerCase, upperCase, specialCharacter, numeric } = passwordRules;
    let numUsed = 0;
    if( lowerCase.available === true ) {
        numUsed++;
    }
    if( upperCase.available === true ) {
        numUsed++;
    }
    if( specialCharacter.available === true ) {
        numUsed++;
    }
    if( numeric.available === true ) {
        numUsed++;
    }
    return {
        passwordLabels: {
            isLowerCaseRequired: lowerCase.label,
            isUpperCaseRequired: upperCase.label,
            isSpecialCharRequired: specialCharacter.label,
            isNumberRequired: numeric.label,
            length: passwordRules.minimumLengthCheck.label,
            numUsed: numUsed
        }
    };
}
