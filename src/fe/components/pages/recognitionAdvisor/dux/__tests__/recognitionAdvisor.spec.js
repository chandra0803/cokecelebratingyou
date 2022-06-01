const expect = require( 'chai' ).expect;
import thunk from 'redux-thunk';
import configureStore from 'redux-mock-store';
import 'whatwg-fetch';
const fetchMock = require( 'fetch-mock' );


import recognitionAdvisorReducer,
        {
        showSpinner,
        updateData,
        showRaPrograms,
        SHOW_SPINNER,
        UPDATE_DATA,
        SHOW_PROGRAMS,
        removeComponent,
        cmStringsArrayToObject
  } from '../recognitionAdvisor';

const raTileModule = {
  'rowNumStart': 1,
  'rowNumEnd': 7,
  'activePage': 0,
  'sortColName': 'NO_VALUE',
  'sortedBy': '',
  'excludeUpcoming': 0,
  'filterValue': '',
  'pendingStatus': 1
};

describe( 'recognition Advisor dux', () => {
  const middlewares = [ thunk ];
  const mockStore = configureStore( middlewares );
  it ( 'should show the spinner', () => {
      const store = mockStore( {} );
      const initialState = {
        ...window.recognitionAdvisor,
        showSpinner: false,
        toggleModal: false
      };
      store.dispatch( showSpinner( true ) );
      const actions = store.getActions();
      expect ( actions ).to.deep.equal( [ { type: SHOW_SPINNER, value: true } ] );

      expect( recognitionAdvisorReducer( initialState, actions[ 0 ] ) ).to.deep.equal(
          { ...initialState, showSpinner: actions[ 0 ].value }
      );

  } );

  it ( 'should display initialState', () => {
      const initialState = {
        ...window.recognitionAdvisor,
        showSpinner: false,
        toggleModal: false
      };

      expect( recognitionAdvisorReducer( undefined, { } ) ).to.deep.equal(
          { ...initialState }
      );

  } );

  it ( 'should call cmStringsArrayToObject and should format response', async() => {
      const store = mockStore( {} );
      fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200, contactMethods: [ { phone: 'here' } ] } );
      const response =  store.dispatch( cmStringsArrayToObject( [ { content: 'test', code: 'test', key: 'test'  }, { content: '???test', code: 'test', key: 'test' } ] ) );
      expect( response ).to.deep.equal( { 'test.test': 'test' } );
      fetchMock.restore();
  } );

} );
