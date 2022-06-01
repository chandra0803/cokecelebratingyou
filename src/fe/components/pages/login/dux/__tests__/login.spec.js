const expect = require( 'chai' ).expect;
import thunk from 'redux-thunk';
import configureStore from 'redux-mock-store';
import 'whatwg-fetch';
const fetchMock = require( 'fetch-mock' );

import loginReducer, {
        showSpinner,
        passwordVisible,
        addMessage,
        changeForm,
        toggleModal,
        SHOW_SPINNER,
        PASSWORD_VISIBLE,
        ADD_MESSAGE,
        CHANGE_FORM,
        TOGGLE_MODAL,
        loginFunc,
        cmStringsArrayToObject,
        changeFormFunc,
        addMessageFunc,
        toggleModalDisplay,
        togglePasswordVisible
    } from '../login';

describe( 'login dux', () => {
    const middlewares = [ thunk ];
    const mockStore = configureStore( middlewares );
    it ( 'should show the spinner', () => {
        const store = mockStore( {} );
        const initialState = {
            ...window.login,
            showSpinner: false,
            emailUnique: true,
            emailAddress: '',
            emailExists: true,
            contactMethods: [],
            messageSent: false,
            toggleModal: false
        };
             store.dispatch( showSpinner( true ) );
             const actions = store.getActions();
             expect ( actions ).to.deep.equal( [ { type: SHOW_SPINNER, value: true } ] );

             expect( loginReducer( initialState, actions[ 0 ] ) ).to.deep.equal(
                 { ...initialState, showSpinner: actions[ 0 ].value }
             );

    } );
    it ( 'should display the password', () => {
        const store = mockStore( {} );
        const initialState = {
            ...window.login,
            showSpinner: false,
            emailUnique: true,
            emailAddress: '',
            emailExists: true,
            contactMethods: [],
            messageSent: false,
            toggleModal: false
        };
             store.dispatch( passwordVisible( true ) );
             const actions = store.getActions();
             expect ( actions ).to.deep.equal( [ { type: PASSWORD_VISIBLE, value: true } ] );

             expect( loginReducer( initialState, actions[ 0 ] ) ).to.deep.equal(
                 { ...initialState, passwordVisible: actions[ 0 ].value }
             );

    } );
    it ( 'should add an error', () => {
        const store = mockStore( {} );
        const initialState = {
            ...window.login,
            showSpinner: false,
            emailUnique: true,
            emailAddress: '',
            emailExists: true,
            contactMethods: [],
            messageSent: false,
            toggleModal: false
        };
        const message = 'some message';
        const id = 221231;
             store.dispatch( addMessage( message, id ) );
             const actions = store.getActions();
             expect ( actions ).to.deep.equal( [ { type: ADD_MESSAGE, message, id } ] );
             expect( loginReducer( initialState, actions[ 0 ] ) ).to.deep.equal(
                 { ...initialState, message: actions[ 0 ].message }
             );

    } );
    it ( 'should change the form', () => {
        const store = mockStore( {} );
        const initialState = {
            ...window.login,
            showSpinner: false,
            emailUnique: true,
            emailAddress: '',
            emailExists: true,
            contactMethods: [],
            messageSent: false,
            toggleModal: false
        };
        const newForm = 'login';
             store.dispatch( changeForm( newForm ) );
             const actions = store.getActions();
             expect ( actions ).to.deep.equal( [ { type: CHANGE_FORM, newForm } ] );
             expect( loginReducer( initialState, actions[ 0 ] ) ).to.deep.equal(
                 { ...initialState, currentForm: actions[ 0 ].newForm }
             );

    } );
    it ( 'should toggle the modal', () => {
        const store = mockStore( {} );
        const initialState = {
            ...window.login,
            showSpinner: false,
            emailUnique: true,
            emailAddress: '',
            emailExists: true,
            contactMethods: [],
            messageSent: false,
            toggleModal: false
        };
             store.dispatch( toggleModal( true, 'help' ) );
             const actions = store.getActions();
             expect ( actions ).to.deep.equal( [ { type: TOGGLE_MODAL, display: true, page: 'help' } ] );
             expect( loginReducer( initialState, actions[ 0 ] ) ).to.deep.equal(
                 { ...initialState, toggleModal: actions[ 0 ].display, modalPage: actions[ 0 ].page }
             );

    } );
    it ( 'should display initialState', () => {
        const initialState = {
            ...window.login,
            showSpinner: false,
            emailUnique: true,
            emailAddress: '',
            emailExists: true,
            contactMethods: [],
            messageSent: false,
            toggleModal: false
        };
            expect( loginReducer( undefined, { } ) ).to.deep.equal(
                 { ...initialState }
             );

    } );
    it ( 'should call loginFunc and should add message', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200, contactMethods: [ { phone: 'here' } ] } );
        await store.dispatch( loginFunc( 'test', 'password',  'some/path', 'token' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );
        } );

        fetchMock.restore();
    } );
    it ( 'should call loginFunc and should add message', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200, messages: [ { type: 'error' }, { type: 'success' } ] } );
        await store.dispatch( loginFunc( 'test', 'password',  'some/path', 'token' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );
        } );

        fetchMock.restore();
    } );
    it ( 'should call loginFunc and show spinner', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200, messages: [ ] } );
        await store.dispatch( loginFunc( 'test', 'password',  'some/path', 'token' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call cmStringsArrayToObject and should format response', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200, contactMethods: [ { phone: 'here' } ] } );
        const response =  store.dispatch( cmStringsArrayToObject( [ { content: 'test', code: 'test', key: 'test'  }, { content: '???test', code: 'test', key: 'test' } ] ) );
        expect( response ).to.deep.equal( { 'test.test': 'test' } );
        fetchMock.restore();
    } );
    it ( 'should call changeFormFunc and should change form', async() => {
        const store = mockStore( {} );
        Object.defineProperty( window.location, 'search', {
          writable: true,
          value: 'http://localhost:8001/g6bb8/login.do'
        } );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200, contactMethods: [ { phone: 'here' } ] } );
        store.dispatch( changeFormFunc( 'login' ) );
        const actions = store.getActions();
        expect( actions[ 0 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'login' } );
        fetchMock.restore();
    } );
    it ( 'should call changeFormFunc and should change form', async() => {
        Object.defineProperty( window.location, 'search', {
          writable: true,
          value: 'http://localhost:8001/gyoda/login.do?cmsLocaleCode=tr_TR'
        } );
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200, contactMethods: [ { phone: 'here' } ] } );
        store.dispatch( changeFormFunc( 'login' ) );
        const actions = store.getActions();
        expect( actions[ 0 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'login' } );
        fetchMock.restore();
    } );
    it ( 'should call changeFormFunc and should change form', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200, contactMethods: [ { phone: 'here' } ] } );
        store.dispatch( changeFormFunc( 'forgot-id' ) );
        const actions = store.getActions();
        expect( actions[ 0 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'forgot-id' } );
        fetchMock.restore();
    } );
    it ( 'should call addMessageFunc and should add message', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200, contactMethods: [ { phone: 'here' } ] } );
        store.dispatch( addMessageFunc( { message: 'some message' } ) );
        const actions = store.getActions();
        expect( actions[ 0 ].type ).to.deep.equal( 'ADD_MESSAGE' );
        fetchMock.restore();
    } );
    it ( 'should call toggleModalDisplay and should toggle modal', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200, contactMethods: [ { phone: 'here' } ] } );
        store.dispatch( toggleModalDisplay( true, 'help' ) );
        const actions = store.getActions();
        expect( actions[ 0 ] ).to.deep.equal( { type: 'TOGGLE_MODAL', display: true, page: 'help' } );
        fetchMock.restore();
    } );
    it ( 'should call togglePasswordVisible and should toggle visibility', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200, contactMethods: [ { phone: 'here' } ] } );
        store.dispatch( togglePasswordVisible( true ) );
        const actions = store.getActions();
        expect( actions[ 0 ] ).to.deep.equal( { type: 'PASSWORD_VISIBLE', value: true } );
        fetchMock.restore();
    } );
} );
