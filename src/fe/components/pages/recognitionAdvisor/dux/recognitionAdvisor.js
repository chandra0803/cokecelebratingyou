import ReactDOM from 'react-dom';

/*******************************************************************************
 * Initial state
 */

const initialState = {
    ...window.recognitionAdvisor,
    showSpinner: false,
    toggleModal: false
};

/*
 * action types
 */

export const SHOW_SPINNER = 'SHOW_SPINNER';
export const UPDATE_DATA = 'UPDATE_DATA';
export const SHOW_PROGRAMS = 'SHOW_PROGRAMS';

/*
 * action creators
 */

export function showSpinner( value ) {
  return { type: SHOW_SPINNER, value };
}

export function updateData( display, page ) {
    return { type: UPDATE_DATA, display, page };
}

export function showRaPrograms( display, page ) {
    return { type: SHOW_PROGRAMS, display, page };
}

export function recognitionAdvisorFunc( formAction, attributeData ) {
    return dispatch => {
        dispatch( showSpinner( true ) );

        return fetch( formAction, {
                method: 'post',
                body: JSON.stringify( attributeData ),
                credentials: 'same-origin',
                headers: {
                    'post-type': 'ajax',
                    'content-type': 'application/json',
                    'cache-control': 'no-cache'
                }
            } )
            .then( response => {
                return response.json();
            } )
            .then( ra => {
                dispatch( updateData( ra, 'ra' ) );
				dispatch( showSpinner( false ) );
            } )
            .catch( ( error ) => {
                dispatch( showSpinner( false ) );
            } );
    };
}

export function raEligibleProgramsFunc( formAction ) {
    return dispatch => {
        dispatch( showSpinner( true ) );

        return fetch( formAction, {
                method: 'get',
                credentials: 'same-origin',
                headers: {
                    'post-type': 'ajax',
                    'content-type': 'application/json',
                    'cache-control': 'no-cache'
                }
            } )
            .then( response => {
                return response.json();
            } )
            .then( ra => {
                dispatch( showRaPrograms( ra, 'ra' ) );
                dispatch( showSpinner( false ) );
            } )
            .catch( ( error ) => {
                dispatch( showSpinner( false ) );
            } );
    };
}

/*******************************************************************************
 * Reducer
 */
export default function recognitionAdvisorReducer( state = initialState, action ) {

    switch ( action.type ) {
        case SHOW_SPINNER:
            return {
                ...state,
                showSpinner: action.value
            };
        case UPDATE_DATA:
            return {
                ...state,
                ...action.display,
                modalPage: action.page
            };
		case SHOW_PROGRAMS:
			return {
                ...state,
                raEligiblePrograms: action.display.eligiblePrograms
            };
        default:
            return state;
    }
}

/*******************************************************************************
* CM Util function
*/

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

/*******************************************************************************
* Component Unmount util function
*/

export function removeComponent() {
  return dispatch=>{
       ReactDOM.unmountComponentAtNode( document.getElementById( 'rec-advisor' ) );
    };
}
