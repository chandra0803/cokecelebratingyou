const expect = require( 'chai' ).expect;
import thunk from 'redux-thunk';
import configureStore from 'redux-mock-store';
import 'whatwg-fetch';
const fetchMock = require( 'fetch-mock' );

import forgotPassReducer, { updatePass, UPDATE_PASS_DATA, state, fetchDataForgotPassword } from '../forgot-password';

describe( 'forgot password dux', () => {
    const middlewares = [ thunk ];
    const mockStore = configureStore( middlewares );
    it ( 'should submit an email address', () => {
        const store = mockStore( {} );
        const data = {
                'responseCode': 200,
                'fieldErrors': [
                    {
                        'field': null,
                        'message': 'It looks like we have multiple accounts attached to that email address. Please select an alternate method of contact.'
                    }
                ],
                'emailExists': true,
                'userExists': false,
                'unique': false,
                'validToken': false,
                'passwordResetSuccess': false,
                'paxIDs': [
                    5585, 5583
                ],
                'contactMethods': [
                    {
                        'contactType': 'EMAIL',
                        'value': 'tom*****r@gma*l.com',
                        'contactId': 5505
                    }, {
                        'contactType': 'EMAIL',
                        'value': 'tom******r@biw*******e.com',
                        'contactId': 5504
                    }, {
                        'contactType': 'PHONE',
                        'value': '5*******06',
                        'contactId': 5444
                    }, {
                        'contactType': 'PHONE',
                        'value': '5*******04',
                        'contactId': 5442
                    }
                ]
            };
             store.dispatch( updatePass( data ) );
             const actions = store.getActions();
             expect ( actions ).to.deep.equal( [ { type: UPDATE_PASS_DATA, data: data } ] );
             expect( forgotPassReducer( state, actions[ 0 ] ) ).to.deep.equal(
                 { ...state, details: actions[ 0 ].data }
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
            expect( forgotPassReducer( initialState, { type: 'SOME_OTHER_TYPE' } ) ).to.deep.equal(
                 { ...initialState }
             );

    } );
    it ( 'should call fetchDataForgotPassword and CHANGE_FORM with responseCode 200 should be messageWindow', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseCode: 200, unique: true } );
        await store.dispatch( fetchDataForgotPassword( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'UPDATE_PASS_DATA', data: { responseCode: 200, unique: true } } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'UPDATE_CONTACT_DATA', contactData: { responseCode: 200, unique: true } } );
            expect( actions[ 4 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'messageWindow' } );
            expect( actions[ 5 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchDataForgotPassword with responseCode 400 should add message', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseCode: 400, unique: true } );
        await store.dispatch( fetchDataForgotPassword( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'UPDATE_PASS_DATA', data: { responseCode: 400, unique: true } } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );

        } );
        fetchMock.restore();
    } );
    it ( 'should call fetchDataForgotPassword with responseCode 400 should add message and change form to methodOfContact', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseCode: 200, unique: true, contactMethods: [ { method: 'phone' } ], userActivated: true } );
        await store.dispatch( fetchDataForgotPassword( { emailOrPhone: 'test' }, 'some/path', 'something', true ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'UPDATE_PASS_DATA', data: { responseCode: 200, unique: true, contactMethods: [ { method: 'phone' } ], userActivated: true } } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'UPDATE_CONTACT_DATA', contactData: { responseCode: 200, unique: true, contactMethods: [ { method: 'phone' } ], userActivated: true } } );
            expect( actions[ 4 ] ).to.deep.equal( { type: 'UPDATE_CONTACT_METHODS', contactMethods: { responseCode: 200, unique: true, contactMethods: [ { method: 'phone' } ], userActivated: true }, shared: undefined } );
            expect( actions[ 5 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'methodOfContact' } );
            expect( actions[ 6 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );

        } );
        fetchMock.restore();
    } );
    it ( 'should call fetchDataForgotPassword', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseCode: 200, unique: true } );
        await store.dispatch( fetchDataForgotPassword( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'UPDATE_PASS_DATA', data: { responseCode: 200, unique: true } } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'UPDATE_CONTACT_DATA', contactData: { responseCode: 200, unique: true } } );
            expect( actions[ 4 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'messageWindow' } );
            expect( actions[ 5 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );

        } );
        fetchMock.restore();
    } );
    it ( 'should call fetchDataForgotPassword with responseCode 200 should update pass data and add message', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseCode: 200, unique: true, fieldErrors: [ { message: 'phone' } ], userActivated: false } );
        await store.dispatch( fetchDataForgotPassword( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'UPDATE_PASS_DATA', data: { responseCode: 200, unique: true, fieldErrors: [ { message: 'phone' } ], userActivated: false } } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );

        } );
        fetchMock.restore();
    } );
    it ( 'should call fetchDataForgotPassword with responseCode 200 should update pass data and add message', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseCode: 200, unique: true, contactMethods: [ { method: 'phone' } ], userActivated: false } );
        await store.dispatch( fetchDataForgotPassword( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'UPDATE_PASS_DATA', data: { responseCode: 200, unique: true, contactMethods: [ { method: 'phone' } ], userActivated: false } } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );

        } );
        fetchMock.restore();
    } );
    it ( 'should call fetchDataForgotPassword with responseCode 200 should update pass data and add message', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseCode: 200, unique: false, activationFields: [ { message: 'phone' } ] } );
        await store.dispatch( fetchDataForgotPassword( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'UPDATE_PASS_DATA', data: { responseCode: 200, unique: false, activationFields: [ { message: 'phone' } ] } } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'UPDATE_ACTIVATION_DATA', data: { responseCode: 200, unique: false, activationFields: [ { message: 'phone' } ] }, id: undefined } );
            expect( actions[ 4 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'activAttr' } );
            expect( actions[ 5 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );

        } );
        fetchMock.restore();
    } );
    it ( 'should call fetchDataForgotPassword with responseCode 200 should update pass data and add message', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseCode: 200, unique: false, activationFields: [ { message: 'phone' } ], nonPax: true, hasRecoveryMethods: true } );
        fetchMock.get( '*', { countries: [ { someCountry: 'code' } ] } );
        await store.dispatch( fetchDataForgotPassword( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'UPDATE_PASS_DATA', data: { responseCode: 200, unique: false, activationFields: [ { message: 'phone' } ], nonPax: true, hasRecoveryMethods: true } } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'UPDATE_CONTACT_METHODS', contactMethods: { responseCode: 200, unique: false, activationFields: [ { message: 'phone' } ], nonPax: true, hasRecoveryMethods: true }, shared: undefined } );
            expect( actions[ 4 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'methodOfContact' } );
            expect( actions[ 5 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );

        } );
        fetchMock.restore();
    } );
    it ( 'should call fetchDataForgotPassword with responseCode 200 should update pass data and add message', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseCode: 200, unique: false, activationFields: [ { message: 'phone' } ], nonPax: true, hasRecoveryMethods: false } );
        await store.dispatch( fetchDataForgotPassword( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'UPDATE_PASS_DATA', data: { responseCode: 200, unique: false, activationFields: [ { message: 'phone' } ], nonPax: true, hasRecoveryMethods: false } } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );

        } );
        fetchMock.restore();
    } );
    it ( 'should call fetchDataForgotPassword with responseCode 200 should update pass data and add message', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseCode: 200, unique: false, activationFields: [ ] } );
        await store.dispatch( fetchDataForgotPassword( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'UPDATE_PASS_DATA', data: { responseCode: 200, unique: false, activationFields: [ ] } } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );

        } );
        fetchMock.restore();
    } );
    it ( 'should call fetchDataForgotPassword with server error and add message is called', async() => {
        const store = mockStore( {} );
        fetchMock.mock( '*', { throws: { text: 'some error' } } );
        await store.dispatch( fetchDataForgotPassword( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );

        } );

        fetchMock.restore();
    } );
} );
