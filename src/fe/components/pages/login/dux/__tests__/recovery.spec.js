const expect = require( 'chai' ).expect;
import thunk from 'redux-thunk';
import configureStore from 'redux-mock-store';
import 'whatwg-fetch';
const fetchMock = require( 'fetch-mock' );

import collectRecoveryReducer, { collectRecovery, COLLECT_RECOVERY_DATA, getCountries, GET_COUNTRY_DATA, fetchDataCollectRecovery, fetchDataCountries } from '../recovery';

describe( 'recovery dux', () => {
    const middlewares = [ thunk ];
    const mockStore = configureStore( middlewares );
    it ( 'should update countries', () => {
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
        const countryPhones = {
                'responseCode': 200,
                'responseMessage': null,
                'developerMessage': null,
                'fieldErrors': null,
                'errorCode': null,
                'countryPhones': [ {
                    'countryId': 6037,
                    'label': 'United States +1'
                }, {
                    'countryId': 6004,
                    'label': 'Australia +61'
                }, {
                    'countryId': 6020,
                    'label': 'France +33'
                }, {
                    'countryId': 6024,
                    'label': 'India +91'
                }, {
                    'countryId': 6025,
                    'label': 'Italy +39'
                }, {
                    'countryId': 6021,
                    'label': 'United Kingdom +44'
                } ]
            };
             store.dispatch( getCountries( countryPhones ) );
             const actions = store.getActions();
             expect ( actions ).to.deep.equal( [ { type: GET_COUNTRY_DATA, data: countryPhones } ] );
             expect( collectRecoveryReducer( initialState, actions[ 0 ] ) ).to.deep.equal(
                 { ...initialState, countries: actions[ 0 ].data }
             );

    } );
    it ( 'should update recovery information', () => {
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
        const dummyData = {
            test: true
        };
             store.dispatch( collectRecovery( dummyData ) );
             const actions = store.getActions();
             expect ( actions ).to.deep.equal( [ { type: COLLECT_RECOVERY_DATA, data: dummyData } ] );
             expect( collectRecoveryReducer( initialState, actions[ 0 ] ) ).to.deep.equal(
                 { ...initialState, recovery: actions[ 0 ].data }
             );

    } );
    it ( 'should return nothing', () => {

        expect( collectRecoveryReducer( undefined, { } ) ).to.deep.equal(
             { }
         );


    } );
    it ( 'should call fetchDataCollectRecovery', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseCode: 400, unique: true } );
        await store.dispatch( fetchDataCollectRecovery( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'COLLECT_RECOVERY_DATA', data: { responseCode: 400, unique: true } } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchDataCollectRecovery', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseCode: 200, unique: true } );
        await store.dispatch( fetchDataCollectRecovery( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'COLLECT_RECOVERY_DATA', data: { responseCode: 200, unique: true } } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchDataCollectRecovery, update contact methods and change form', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseCode: 200, contactMethods: [ { method: 'phone' } ] } );
        await store.dispatch( fetchDataCollectRecovery( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'COLLECT_RECOVERY_DATA', data: { responseCode: 200, contactMethods: [ { method: 'phone' } ] } } );
            expect( actions[ 2 ] ).to.deep.equal( { type: 'UPDATE_CONTACT_DATA', contactData: { responseCode: 200, contactMethods: [ { method: 'phone' } ] } } );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'UPDATE_CONTACT_METHODS', contactMethods: { responseCode: 200, contactMethods: [ { method: 'phone' } ] }, shared: undefined } );
            expect( actions[ 4 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'methodOfContact' } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchDataCollectRecovery with server error and add message is called', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseCode: 400, unique: true, messages: [ { type: 'error' }, { type: 'success' } ] } );
        await store.dispatch( fetchDataCollectRecovery( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ].type ).to.deep.equal( 'ADD_MESSAGE' );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchDataCountries and change form to recovery', async() => {
        const store = mockStore( {} );
        fetchMock.get( '*', { responseCode: 400, unique: true } );
        await store.dispatch( fetchDataCountries( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'GET_COUNTRY_DATA', data: { responseCode: 400, unique: true } } );
            expect( actions[ 2 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'recovery' } );
            expect( actions[ 3 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 4 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchDataCountries and change form to recovery', async() => {
        const store = mockStore( {} );
        fetchMock.get( '*', { responseCode: 200, unique: true } );
        await store.dispatch( fetchDataCountries( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'GET_COUNTRY_DATA', data: { responseCode: 200, unique: true } } );
            expect( actions[ 2 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'recovery' } );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchDataCountries and change form to recovery', async() => {
        const store = mockStore( {} );
        fetchMock.get( '*', { responseCode: 400, unique: true, fieldErrors: [ { message: 'error' } ] } );
        await store.dispatch( fetchDataCountries( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'GET_COUNTRY_DATA', data: { responseCode: 400, unique: true, fieldErrors: [ { message: 'error' } ] } } );
            expect( actions[ 2 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'recovery' } );
            expect( actions[ 3 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 4 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchDataCollectRecovery with server error and add message is called', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseCode: 400, unique: true, fieldErrors: [ { message: 'error' } ] } );
        await store.dispatch( fetchDataCollectRecovery( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'COLLECT_RECOVERY_DATA', data: { responseCode: 400, unique: true, fieldErrors: [ { message: 'error' } ] } } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchDataCountries with server error and add message is called', async() => {
        const store = mockStore( {} );
        fetchMock.mock( '*', { throws: { text: 'some error' } } );
        await store.dispatch( fetchDataCountries( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );

        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchDataCollectRecovery with server error and add message is called', async() => {
        const store = mockStore( {} );
        fetchMock.mock( '*', { throws: { text: 'some error' } } );
        await store.dispatch( fetchDataCollectRecovery( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );

        } );

        fetchMock.restore();
    } );
} );
