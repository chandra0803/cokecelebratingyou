const expect = require( 'chai' ).expect;
import thunk from 'redux-thunk';
import configureStore from 'redux-mock-store';
import 'whatwg-fetch';
const fetchMock = require( 'fetch-mock' );

import forgotIdReducer, { updatelogin, UPDATE_LOGIN_DATA, state, fetchData } from '../forgot-id';

describe( 'forgot id dux', () => {
    const middlewares = [ thunk ];
    const mockStore = configureStore( middlewares );
    it ( 'should update the login data', () => {
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
            const emailOrPhone = 'someemailaddres@test.com';
             store.dispatch( updatelogin( data, emailOrPhone ) );
             const actions = store.getActions();
             expect ( actions ).to.deep.equal( [ { type: UPDATE_LOGIN_DATA, data: data, emailOrPhone: emailOrPhone } ] );
             expect( forgotIdReducer( state, actions[ 0 ] ) ).to.deep.equal(
                 { ...state, details: actions[ 0 ].data, emailOrPhone: actions[ 0 ].emailOrPhone }
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
            expect( forgotIdReducer( initialState, { type: 'SOME_OTHER_TYPE' } ) ).to.deep.equal(
                 { ...initialState }
             );

    } );
    it ( 'should call fetchData and CHANGE_FORM should be methodOfContact', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', single: true, contactMethods: [ { phone: 'here' }, { email: 'email@email.com' } ] } );
        await store.dispatch( fetchData( { emailOrPhone: 'test' }, 'some/path', 'some@email.address', true ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'UPDATE_LOGIN_DATA', data: { responseMessage: 'hello', single: true, contactMethods: [ { phone: 'here' }, { email: 'email@email.com' } ] }, emailOrPhone: { emailOrPhone: 'test' } } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'UPDATE_CONTACT_METHODS', contactMethods: { responseMessage: 'hello', single: true, contactMethods: [ { phone: 'here' }, { email: 'email@email.com' } ] }, shared: false } );
            expect( actions[ 4 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'methodOfContact' } );
            expect( actions[ 5 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchData and CHANGE_FORM should be methodOfContact', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', single: false, contactMethods: [ { phone: 'here' }, { email: 'email@email.com' } ] } );
        await store.dispatch( fetchData( { emailOrPhone: 'test' }, 'some/path', 'some@email.address', true ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'UPDATE_LOGIN_DATA', data: { responseMessage: 'hello', single: false, contactMethods: [ { phone: 'here' }, { email: 'email@email.com' } ] }, emailOrPhone: { emailOrPhone: 'test' } } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'UPDATE_CONTACT_METHODS', contactMethods: { responseMessage: 'hello', single: false, contactMethods: [ { phone: 'here' }, { email: 'email@email.com' } ] }, shared: true } );
            expect( actions[ 4 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'methodOfContact' } );
            expect( actions[ 5 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchData and CHANGE_FORM should be messageWindow', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', unique: true } );
        await store.dispatch( fetchData( { emailOrPhone: 'test' }, 'some/path', 'some@email.address' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'UPDATE_LOGIN_DATA', data: { responseMessage: 'hello', unique: true }, emailOrPhone: { emailOrPhone: 'test' } } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'messageWindow' } );
            expect( actions[ 4 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchData and update login data', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', unique: false } );
        await store.dispatch( fetchData( { emailOrPhone: 'test' }, 'some/path', 'some@email.address' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'UPDATE_LOGIN_DATA', data: { responseMessage: 'hello', unique: false }, emailOrPhone: { emailOrPhone: 'test' } } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchData and should add message', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', fieldErrors: [ { message: 'here' } ] } );
        await store.dispatch( fetchData( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'UPDATE_LOGIN_DATA', data: { responseMessage: 'hello', fieldErrors: [ { message: 'here' } ] }, emailOrPhone: { emailOrPhone: 'test' } } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchData', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', fieldErrors: [ { error: 'here' } ] } );
        await store.dispatch( fetchData( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'UPDATE_LOGIN_DATA', data: { responseMessage: 'hello', fieldErrors: [ { error: 'here' } ] }, emailOrPhone: { emailOrPhone: 'test' } } );
            expect( actions[ 2 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchData', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: '' } );
        await store.dispatch( fetchData( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'UPDATE_LOGIN_DATA', data: { responseMessage: '' }, emailOrPhone: { emailOrPhone: 'test' } } );
            expect( actions[ 2 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchData with server error and add message is called', async() => {
        const store = mockStore( {} );
        fetchMock.mock( '*', { throws: { text: 'some error' } } );
        await store.dispatch( fetchData( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );

        } );

        fetchMock.restore();
    } );
} );
