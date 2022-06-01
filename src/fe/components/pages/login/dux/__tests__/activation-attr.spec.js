const expect = require( 'chai' ).expect;
import thunk from 'redux-thunk';
import configureStore from 'redux-mock-store';
import 'whatwg-fetch';
const fetchMock = require( 'fetch-mock' );

import attributeReducer, { updateAttributes, UPDATE_ATTRIBUTE_DATA, state, fetchCollectionData } from '../activation-attr';

describe( 'activationAttr dux', () => {
    const middlewares = [ thunk ];
    const mockStore = configureStore( middlewares );
    it ( 'should submit a userId', () => {
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
             store.dispatch( updateAttributes( data ) );
             const actions = store.getActions();
             expect ( actions ).to.deep.equal( [ { type: UPDATE_ATTRIBUTE_DATA, data: data } ] );
             expect( attributeReducer( state, actions[ 0 ] ) ).to.deep.equal(
                 { ...state, attributes: actions[ 0 ].data }
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
            expect( attributeReducer( initialState, { type: 'SOME_OTHER_TYPE' } ) ).to.deep.equal(
                 { ...initialState }
             );

    } );
    it ( 'should call fetchCollectionData and CHANGE_FORM should be recovery', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200, contactMethods: [ { phone: 'here' } ] } );
        await store.dispatch( fetchCollectionData( { emailOrPhone: 'test' }, 'some/path', false ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 2 ] ).to.deep.equal( { type: 'UPDATE_ATTRIBUTE_DATA', data: { responseMessage: 'hello', responseCode: 200, contactMethods: [ { phone: 'here' } ] } } );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'GET_COUNTRY_DATA', data: { responseMessage: 'hello', responseCode: 200, contactMethods: [ { phone: 'here' } ] } } );
            expect( actions[ 4 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'recovery' } );
            expect( actions[ 5 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchCollectionData and should add message', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200, fieldErrors: [ { message: 'here' }, { message: '' } ] } );
        await store.dispatch( fetchCollectionData( { emailOrPhone: 'test' }, 'some/path', { token: 'token' } ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 2 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchCollectionData', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 400 } );
        await store.dispatch( fetchCollectionData( { emailOrPhone: 'test' }, 'some/path', { token: 'token' } ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchCollectionData with server error and add message is called', async() => {
        const store = mockStore( {} );
        fetchMock.mock( '*', { throws: { text: 'some error' } } );
        await store.dispatch( fetchCollectionData( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );

        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchCollectionData and should add message', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200, hasRecoveryMethods: true } );
        await store.dispatch( fetchCollectionData( { emailOrPhone: 'test' }, 'some/path', { token: 'token' } ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 2 ] ).to.deep.equal( { type: 'UPDATE_ATTRIBUTE_DATA', data: { responseMessage: 'hello', responseCode: 200, hasRecoveryMethods: true } } );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'UPDATE_CONTACT_METHODS', contactMethods: { responseMessage: 'hello', responseCode: 200, hasRecoveryMethods: true }, shared: undefined } );
            expect( actions[ 4 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'methodOfContact' } );
            expect( actions[ 5 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
} );
