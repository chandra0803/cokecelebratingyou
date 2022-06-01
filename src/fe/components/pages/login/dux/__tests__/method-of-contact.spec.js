const expect = require( 'chai' ).expect;
import thunk from 'redux-thunk';
import configureStore from 'redux-mock-store';
import 'whatwg-fetch';
const fetchMock = require( 'fetch-mock' );

import methodOfContactReducer, { updateContactData, UPDATE_CONTACT_DATA, updateContactMethods, UPDATE_CONTACT_METHODS, sendContact, sendAutoQuery } from '../method-of-contact';

describe( 'method of contact dux', () => {
    const middlewares = [ thunk ];
    const mockStore = configureStore( middlewares );
    it ( 'should update the contact methods', () => {
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
        const contactMethods = {
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
            const shared = true;
             store.dispatch( updateContactMethods( contactMethods, shared ) );
             const actions = store.getActions();
             expect ( actions ).to.deep.equal( [ { type: UPDATE_CONTACT_METHODS, contactMethods, shared } ] );
             expect( methodOfContactReducer( initialState, actions[ 0 ] ) ).to.deep.equal(
                 { ...initialState, contactMethods: actions[ 0 ].contactMethods, shared: actions[ 0 ].shared }
             );

    } );
    it ( 'should display nothing', () => {
            expect( methodOfContactReducer( undefined, { } ) ).to.deep.equal(
                 { }
             );

    } );
    it ( 'should update the contact data', () => {
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
        const contactData = {
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
             store.dispatch( updateContactData( contactData ) );
             const actions = store.getActions();
             expect ( actions ).to.deep.equal( [ { type: UPDATE_CONTACT_DATA, contactData } ] );
             expect( methodOfContactReducer( initialState, actions[ 0 ] ) ).to.deep.equal(
                 { ...initialState, contactMsg: actions[ 0 ].contactData }
             );

    } );
    it ( 'should call sendContact and CHANGE_FORM should be messageWindow', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200 } );
        await store.dispatch( sendContact( { emailOrPhone: 'test', sendMessage: true }, 'some/path', false, false ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 2 ] ).to.deep.equal( { type: 'UPDATE_CONTACT_DATA', contactData: { responseMessage: 'hello', responseCode: 200 } } );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'messageWindow' } );
            expect( actions[ 4 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call sendContact and add message, change form should not fire', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200 } );
        await store.dispatch( sendContact( { emailOrPhone: 'test' }, 'some/path', false, true ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 2 ] ).to.deep.equal( { type: 'UPDATE_CONTACT_DATA', contactData: { responseMessage: 'hello', responseCode: 200 } } );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call sendContact and should update Contact Methods', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200, contactMethods: [ { phone: 'here' } ], single: true } );
        await store.dispatch( sendContact( { emailOrPhone: 'test' }, 'some/path', 'user', true ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 2 ] ).to.deep.equal( { type: 'UPDATE_CONTACT_DATA', contactData: { responseMessage: 'hello', responseCode: 200, contactMethods: [ { phone: 'here' } ], single: true } } );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'UPDATE_CONTACT_METHODS', contactMethods: { responseMessage: 'hello', responseCode: 200, contactMethods: [ { phone: 'here' } ], single: true }, shared: false } );
            expect( actions[ 4 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call sendContact and CHANGE_FORM should be codeVerification', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200 } );
        await store.dispatch( sendContact( { contactType: 'PHONE' }, 'some/path', false ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 2 ] ).to.deep.equal( { type: 'UPDATE_CONTACT_DATA', contactData: { responseMessage: 'hello', responseCode: 200 } } );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'codeVerification' } );
            expect( actions[ 4 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call sendContact and should add message', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 400, fieldErrors: [ { message: 'here' } ] } );
        await store.dispatch( sendContact( { contactType: 'PHONE' }, 'some/path', false ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );
        } );

        fetchMock.restore();
    } );
    it ( 'should call sendContact', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200, fieldErrors: [ { message: 'here' } ] } );
        await store.dispatch( sendContact( { contactType: 'PHONE' }, 'some/path', false ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call sendContact with server error and add message is called', async() => {
        const store = mockStore( {} );
        fetchMock.mock( '*', { throws: { text: 'some error' } } );
        await store.dispatch( sendContact( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );

        } );

        fetchMock.restore();
    } );
    it ( 'should call sendAutoQuery with server error and add message is called', async() => {
        const store = mockStore( {} );
        fetchMock.mock( '*', { throws: { text: 'some error' } } );
        await store.dispatch( sendAutoQuery( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ].type ).to.deep.equal( 'ADD_MESSAGE' );

        } );

        fetchMock.restore();
    } );
    it ( 'should call sendAutoQuery and add message', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 400, fieldErrors: [ { message: 'here' } ] } );
        await store.dispatch( sendAutoQuery( { contactType: 'PHONE' }, 'some/path', false ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ].type ).to.deep.equal( 'ADD_MESSAGE' );
        } );

        fetchMock.restore();
    } );
    it ( 'should do nothing', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200, fieldErrors: [ { message: 'here' } ] } );
        await store.dispatch( sendAutoQuery( { contactType: 'PHONE' }, 'some/path', false ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( undefined );
        } );

        fetchMock.restore();
    } );
    it ( 'should call sendAutoQuery and add message', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200 } );
        await store.dispatch( sendAutoQuery( { contactType: 'PHONE' }, 'some/path', false, true ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 1 ] ).to.deep.equal( { type: UPDATE_CONTACT_METHODS,  contactMethods: { responseMessage: 'hello', responseCode: 200, contactMethods: [] }, shared: true } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call sendAutoQuery and add message', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200, contactMethods: [ { phone: '43453453' } ] } );
        await store.dispatch( sendAutoQuery( { contactType: 'PHONE' }, 'some/path', false, true ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 1 ] ).to.deep.equal( { type: UPDATE_CONTACT_METHODS,  contactMethods: { responseMessage: 'hello', responseCode: 200, contactMethods: [ { phone: '43453453' } ] }, shared: true } );
        } );

        fetchMock.restore();
    } );
} );
