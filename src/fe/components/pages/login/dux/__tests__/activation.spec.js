const expect = require( 'chai' ).expect;
import thunk from 'redux-thunk';
import configureStore from 'redux-mock-store';
import 'whatwg-fetch';
const fetchMock = require( 'fetch-mock' );

import activationReducer, { updateActivation, UPDATE_ACTIVATION_DATA, state, fetchActivationData } from '../activation';

describe( 'activation dux', () => {
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
            const id = 23423;
             store.dispatch( updateActivation( data, id ) );
             const actions = store.getActions();
             expect ( actions ).to.deep.equal( [ { type: UPDATE_ACTIVATION_DATA, data: data, id: id } ] );
             expect( activationReducer( state, actions[ 0 ] ) ).to.deep.equal(
                 { ...state, activation: actions[ 0 ].data, id: actions[ 0 ].id }
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
            expect( activationReducer( initialState, { type: 'SOME_OTHER_TYPE' } ) ).to.deep.equal(
                 { ...initialState }
             );

    } );
    it ( 'should call fetchActivationData and CHANGE_FORM should be messageWindow', async() => {
        const store = mockStore( {} );
        fetchMock.get( '*', { responseMessage: 'hello', responseCode: 200, contactMethods: [ { phone: 'here' } ] } );
        await store.dispatch( fetchActivationData( { emailOrPhone: 'test' }, 'some/path', false ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 2 ] ).to.deep.equal( { type: 'UPDATE_ACTIVATION_DATA', data: { responseMessage: 'hello', responseCode: 200, contactMethods: [ { phone: 'here' } ] }, id: { emailOrPhone: 'test' } } );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'messageWindow' } );
            expect( actions[ 4 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchActivationData and CHANGE_FORM should be activAttr', async() => {
        const store = mockStore( {} );
        fetchMock.get( '*', { responseMessage: 'hello', responseCode: 200, userActivated: false, activationFields: [ { something: true } ] } );
        await store.dispatch( fetchActivationData( { emailOrPhone: 'test' }, 'some/path', false ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 2 ] ).to.deep.equal( { type: 'UPDATE_ACTIVATION_DATA', data: { responseMessage: 'hello', responseCode: 200, userActivated: false, activationFields: [ { something: true } ] }, id: { emailOrPhone: 'test' } } );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'activAttr' } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchActivationData and CHANGE_FORM should be Contact Methods', async() => {
        const store = mockStore( {} );
        fetchMock.get( '*', { responseMessage: 'hello', responseCode: 200, userActivated: false, activationFields: [ { something: true } ], nonPax: true } );
        await store.dispatch( fetchActivationData( { emailOrPhone: 'test' }, 'some/path', false ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 2 ] ).to.deep.equal( { type: 'UPDATE_ACTIVATION_DATA', data: { responseMessage: 'hello', responseCode: 200, userActivated: false, activationFields: [ { something: true } ], nonPax: true }, id: { emailOrPhone: 'test' } } );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'UPDATE_CONTACT_METHODS', contactMethods: { responseMessage: 'hello', responseCode: 200, userActivated: false, activationFields: [ { something: true } ], nonPax: true }, shared: undefined } );
            expect( actions[ 4 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'methodOfContact' } );
            expect( actions[ 5 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchActivationData and CHANGE_FORM should be messageWindow', async() => {
        const store = mockStore( {} );
        fetchMock.get( '*', { responseMessage: 'hello', responseCode: 200, userActivated: false, emailUnique: false } );
        await store.dispatch( fetchActivationData( { emailOrPhone: 'test' }, 'some/path', false ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 2 ] ).to.deep.equal( { type: 'UPDATE_ACTIVATION_DATA', data: { responseMessage: 'hello', responseCode: 200, userActivated: false, emailUnique: false }, id: { emailOrPhone: 'test' } } );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'messageWindow' } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchActivationData and should add message', async() => {
        const store = mockStore( {} );
        fetchMock.get( '*', { responseMessage: 'hello', responseCode: 200, fieldErrors: [ { message: 'here' }, { message: '' } ] } );
        await store.dispatch( fetchActivationData( { emailOrPhone: 'test' }, 'some/path', false ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 2 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchActivationData', async() => {
        const store = mockStore( {} );
        fetchMock.get( '*', { responseMessage: 'hello', responseCode: 400 } );
        await store.dispatch( fetchActivationData( { emailOrPhone: 'test' }, 'some/path', false ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchActivationData with server error and add message is called', async() => {
        const store = mockStore( {} );
        fetchMock.mock( '*', { throws: { text: 'some error' } } );
        await store.dispatch( fetchActivationData( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );

        } );

        fetchMock.restore();
    } );
} );
