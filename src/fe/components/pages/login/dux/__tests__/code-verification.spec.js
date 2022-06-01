const expect = require( 'chai' ).expect;
import thunk from 'redux-thunk';
import configureStore from 'redux-mock-store';
import 'whatwg-fetch';
const fetchMock = require( 'fetch-mock' );

import codeVerificationReducer, { passwordReset, PASSWORD_RESET, passwordEnter, PASSWORD_ENTER, state, sendToken } from '../code-verification';

describe( 'code verification dux', () => {
    const middlewares = [ thunk ];
    const mockStore = configureStore( middlewares );
    it ( 'should update verification data', () => {
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
            const token = {
                token: 'token'
            };
             store.dispatch( passwordReset( data, token ) );
             const actions = store.getActions();
             expect ( actions ).to.deep.equal( [ { type: PASSWORD_RESET, verificationData: data, token: token } ] );
             expect( codeVerificationReducer( state, actions[ 0 ] ) ).to.deep.equal(
                 { ...state, confirmation: actions[ 0 ].verificationData, token: actions[ 0 ].token }
             );

    } );
    it ( 'should update verification data', () => {
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
            const token = {
                token: 'token'
            };
             store.dispatch( passwordEnter( data, token ) );
             const actions = store.getActions();
             expect ( actions ).to.deep.equal( [ { type: PASSWORD_ENTER, verificationData: data, token: token } ] );
             expect( codeVerificationReducer( state, actions[ 0 ] ) ).to.deep.equal(
                 { ...state, activation: actions[ 0 ].verificationData, token: actions[ 0 ].token }
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
            expect( codeVerificationReducer( initialState, { type: 'SOME_OTHER_TYPE' } ) ).to.deep.equal(
                 { ...initialState }
             );

    } );
    it ( 'should call sendToken and CHANGE_FORM should be resetPassword', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200, contactMethods: [ { phone: 'here' } ] } );
        await store.dispatch( sendToken( { emailOrPhone: 'test' }, 'some/path', false ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 2 ] ).to.deep.equal( { type: 'PASSWORD_RESET', verificationData: { responseMessage: 'hello', responseCode: 200, contactMethods: [ { phone: 'here' } ] }, token: { emailOrPhone: 'test' } } );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'resetPassword' } );
            expect( actions[ 4 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call sendToken and CHANGE_FORM should be activAttr', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200, contactMethods: [ { phone: 'here' } ] } );
        await store.dispatch( sendToken( { emailOrPhone: 'test' }, 'some/path', true ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 2 ] ).to.deep.equal( { type: 'PASSWORD_ENTER', verificationData: { responseMessage: 'hello', responseCode: 200, contactMethods: [ { phone: 'here' } ] }, token: { emailOrPhone: 'test' } } );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'createPass' } );
            expect( actions[ 4 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call sendToken and CHANGE_FORM should be messageWindow', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 400, contactMethods: [ { phone: 'here' } ] } );
        await store.dispatch( sendToken( { emailOrPhone: 'test' }, 'some/path', true ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 2 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'messageWindow' } );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call sendToken and should add message', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseCode: 400, unique: true, messages: [ { type: 'error' }, { type: 'success' } ] } );
        await store.dispatch( sendToken( { emailOrPhone: 'test' }, 'some/path', true ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );
        } );

        fetchMock.restore();
    } );
	it ( 'should call sendToken from Email through activation', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseCode: 400, validToken: false,  fieldErrors: [ { message: 'error' } ] } );
        await store.dispatch( sendToken( { emailOrPhone: 'test', fromEmail: false }, 'some/path', true ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call sendToken and should add message', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseCode: 400, unique: true, fieldErrors: [ { message: 'error' } ] } );
        await store.dispatch( sendToken( { emailOrPhone: 'test' }, 'some/path', true ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );
        } );

        fetchMock.restore();
    } );
    it ( 'should call sendToken and should add message', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseCode: 200, unique: true, fieldErrors: [ { message: 'error' } ] } );
        await store.dispatch( sendToken( { emailOrPhone: 'test' }, 'some/path', true ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call sendToken with server error and add message is called', async() => {
        const store = mockStore( {} );
        fetchMock.mock( '*', { throws: { text: 'some error' } } );
        await store.dispatch( sendToken( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );

        } );

        fetchMock.restore();
    } );
} );
