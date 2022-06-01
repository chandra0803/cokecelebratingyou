import ReactDOM from 'react-dom';

/*******************************************************************************
 * Initial state
 */

const initialState = {
    ...window.eventManageAdmin,
    showSpinner: false
};

/*
 * action types
 */

export const SHOW_SPINNER = 'SHOW_SPINNER';
export const SHOW_EVENT_LISTENER_STATUS = 'SHOW_EVENT_LISTENER_STATUS';
export const SHOW_EVENT_ATTRIBUTES = 'SHOW_EVENT_ATTRIBUTES';
export const SHOW_EVENT_LIST = 'SHOW_EVENT_LIST';
export const SET_EVENT_FILTER = 'SET_EVENT_FILTER';

/*
 * action creators
 */

export function showSpinner( value ) {
  return { type: SHOW_SPINNER, value };
}

export function showEventListenerStatus( eventListenerStatus, page ) {
    return { type: SHOW_EVENT_LISTENER_STATUS, eventListenerStatus, page };
}

export function showEventAttributes( eventAttributes, page ) {
    return { type: SHOW_EVENT_ATTRIBUTES, eventAttributes, page };
}

export function showEventList(eventList, page) {
    return { type: SHOW_EVENT_LIST, eventList, page };
}

export  function setEventFilterFunc(eventFilter){
    return {type: SET_EVENT_FILTER, eventFilter}
}

export function getEventListenerStatus( formAction ) {
    return dispatch => {
        //dispatch( showSpinner( true ) );
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
            .then( eventListenerStatus => {
                dispatch( showEventListenerStatus( eventListenerStatus, 'eventListenerStatus' ) );
                //dispatch( showSpinner( false ) );
            } )
            .catch( ( error ) => {
                //dispatch( showSpinner( false ) );
            } );
    };
}

export function getEventAttributes( formAction ) {
    return dispatch => {
        //dispatch( showSpinner( true ) );
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
            .then( eventAttributes => {
                dispatch( showEventAttributes( eventAttributes, 'eventAttributes' ) );
                //dispatch( showSpinner( false ) );
            } )
            .catch( ( error ) => {
                //dispatch( showSpinner( false ) );
            } );
    };
}

export function getEventList( formAction ) 
{
    return dispatch => {
        //dispatch( showSpinner( true ) );
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
            .then( eventList => {
                dispatch( showEventList( eventList, 'eventList' ) );
                //dispatch( showSpinner( false ) );
            } )
            .catch( ( error ) => {
                //dispatch( showSpinner( false ) );
            } );
    };
}

export function startEventListener( formAction ) {
    return dispatch => {
        //dispatch( showSpinner( true ) );
        return fetch( formAction, {
                method: 'post',
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
            .then( listenerStatus => {
                dispatch( showEventListenerStatus( listenerStatus, 'listenerStatus' ) );
                //dispatch( showSpinner( false ) );
            } )
            .catch( ( error ) => {
                //dispatch( showSpinner( false ) );
            } );
    };
}


export function setEventFilter( filter ) 
{
    return dispatch => {
        dispatch( setEventFilterFunc( filter ) );
    };
}

/*******************************************************************************
 * Reducer
 */
export default function eventManageAdminReducer( state = initialState, action ) 
{

    switch ( action.type ) 
    {
        case SHOW_SPINNER:
            return {
                ...state,
                showSpinner: action.value
            };
		case SHOW_EVENT_LISTENER_STATUS:
			return {
                ...state,
                eventListenerStatus: action.eventListenerStatus
            };
        case SHOW_EVENT_ATTRIBUTES:
			return {
                ...state,
                eventAttributes: action.eventAttributes
            };
        case SHOW_EVENT_LIST:
			return {
                ...state,
                eventList: action.eventList
            };
        case SET_EVENT_FILTER:
            return {
                ...state,
                eventFilter: action.eventFilter
            };
        default:
            return state;
    }
}

/*******************************************************************************
* Component Unmount util function
*/

export function removeComponent() 
{
  return dispatch=>{
       ReactDOM.unmountComponentAtNode( document.getElementById( 'event-manage-admin' ) );
    };
}
