const expect = require( 'chai' ).expect;
import thunk from 'redux-thunk';
import configureStore from 'redux-mock-store';
import 'whatwg-fetch';
const fetchMock = require( 'fetch-mock' );

import resetPassReducer, { passwordSubmit, PASSWORD_SUBMIT, state, submitPass, PASSWORD_RULES, addPassRules, fetchPasswordRules } from '../reset-password';

describe( 'reset password dux', () => {
    const middlewares = [ thunk ];
    const mockStore = configureStore( middlewares );
    it ( 'should submit password and user', () => {
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
             store.dispatch( passwordSubmit( data ) );
             const actions = store.getActions();
             expect ( actions ).to.deep.equal( [ { type: PASSWORD_SUBMIT, passwordResponse: data } ] );
             expect( resetPassReducer( state, actions[ 0 ] ) ).to.deep.equal(
                 { ...state, confirmation: actions[ 0 ].passwordResponse }
             );

    } );
    it ( 'should display initialState', () => {
        const store = mockStore( {} );
        const data = {
            'lowerCaseRequired': {
                'label': 'Lower Case Required',
                'required': true
            },
            'upperCaseRequired': {
                'label': '???login.forgotpwd.MUST_UPPER_CASE???',
                'required': true
            },
            'numericRequired': {
                'label': '???login.forgotpwd.MUST_NUMBER???',
                'required': true
            },
            'specialCharacterRequired': {
                'label': 'Special Char Required',
                'required': true
            },
            'minimumLengthCheck': {
                'label': 'Password is too short. Password should have at least {0} characters.',
                'required': true
            },
            'minLength': 8
        };
        const labels = {};

        store.dispatch( addPassRules( data, labels, 3, 'some label', false ) );
        const actions = store.getActions();
        expect ( actions ).to.deep.equal( [ { type: PASSWORD_RULES, rules: data, labels: labels, minRules: 3, minRulesLabel: 'some label', ignoreValidation: false } ] );

        const theActions = actions[ 0 ];
        expect( resetPassReducer( state, actions[ 0 ] ) ).to.deep.equal(
            { ...theActions.rules, ...theActions.labels, minRules: theActions.minRules, minRulesLabel: theActions.minRulesLabel, ignoreValidation: theActions.ignoreValidation  }
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
            expect( resetPassReducer( initialState, { type: 'SOME_OTHER_TYPE' } ) ).to.deep.equal(
                 { ...initialState }
             );

    } );
    it ( 'should call submitPass and CHANGE_FORM with responseCode 200 should be messageWindow', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseCode: 200, unique: true } );
        await store.dispatch( submitPass( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 2 ] ).to.deep.equal( { type: 'PASSWORD_SUBMIT', passwordResponse: { responseCode: 200, unique: true } } );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'messageWindow' } );
            expect( actions[ 4 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call submitPass and CHANGE_FORM with responseCode 400 should be messageWindow', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseCode: 400, unique: true } );
        await store.dispatch( submitPass( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 2 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'messageWindow' } );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call submitPass and CHANGE_FORM with responseCode 200 and create=true should be recovery', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseCode: 200, unique: true } );
        await store.dispatch( submitPass( { emailOrPhone: 'test' }, 'some/path', true ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ].type ).to.deep.equal( 'ADD_MESSAGE' );
            expect( actions[ 2 ] ).to.deep.equal( { type: 'PASSWORD_SUBMIT', passwordResponse: { responseCode: 200, unique: true } } );
            expect( actions[ 3 ] ).to.deep.equal( { type: 'CHANGE_FORM', newForm: 'recovery' } );
            expect( actions[ 4 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
        } );

        fetchMock.restore();
    } );
    it ( 'should call submitPass and should add message', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { fieldErrors: [ { message: 'error' } ] } );
        await store.dispatch( submitPass( { emailOrPhone: 'test' }, 'some/path', false ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGES' );
        } );

        fetchMock.restore();
    } );
    it ( 'should call submitPass with server error and add message is called', async() => {
        const store = mockStore( {} );
        fetchMock.mock( '*', { throws: { text: 'some error' } } );
        await store.dispatch( submitPass( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );

        } );

        fetchMock.restore();
    } );
    it ( 'should call submitPass with server response error message and add message is called', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', responseCode: 200, messages: [ { type: 'error' }, { type: 'success' } ] } );
        await store.dispatch( submitPass( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );
            expect( actions[ 1 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: false } );
            expect( actions[ 2 ].type ).to.deep.equal( 'ADD_MESSAGE' );

        } );

        fetchMock.restore();
    } );
    it ( 'should call submitPass', async() => {
        const store = mockStore( {} );
        fetchMock.post( '*', { responseMessage: 'hello', 'responseCode': 200, 'fieldErrors': [ { 'field': null, 'message': 'It looks like we have multiple accounts attached to that email address. Please select an alternate method of contact.' } ] } );
        await store.dispatch( submitPass( { emailOrPhone: 'test' }, 'some/path' ) ).then( response =>{
            const actions = store.getActions();
            expect( actions[ 0 ] ).to.deep.equal( { type: 'SHOW_SPINNER', value: true } );

        } );

        fetchMock.restore();
    } );
    it ( 'should call fetchPasswordRules', async() => {
        const store = mockStore( {} );
        fetchMock.get( '*', {
            distinctCharacterTypes: 3,
            distinctCharacterTypesCheck: { label: 'Must be complex.  Match at least 3:', required: true },
            ignoreValidation: false,
            lowerCase: { label: 'Lower Case', available: true },
            minLength: 14,
            minimumLengthCheck: { label: 'Password should have at least 14 characters.', required: true },
            numeric: { label: 'Number', available: true },
            responseCode: 200,
            specialCharacter: { label: 'Special Chararacters', available: true },
            upperCase: { label: 'Upper Case', available: true } } );
        await store.dispatch( fetchPasswordRules( 'some/path' ) ).then( response =>{
            const actions = store.getActions();

            expect( actions[ 0 ] ).to.deep.equal( { type: 'PASSWORD_RULES',
                rules:
                    { passwordRules:
                      { isLowerCaseRequired: true,
                        isUpperCaseRequired: true,
                        isSpecialCharRequired: true,
                        isNumberRequired: true,
                        minLength: 14,
                        numUsed: 4 } },
                labels:
                    { passwordLabels:
                      { isLowerCaseRequired: 'Lower Case',
                        isUpperCaseRequired: 'Upper Case',
                        isSpecialCharRequired: 'Special Chararacters',
                        isNumberRequired: 'Number',
                        length: 'Password should have at least 14 characters.',
                        numUsed: 4 } },
                minRules: 3,
                minRulesLabel: 'Must be complex.  Match at least 3:',
                ignoreValidation: false } );
                } );

        fetchMock.restore();
    } );
    it ( 'should call fetchPasswordRules', async() => {
        const store = mockStore( {} );
        fetchMock.get( '*', {
            distinctCharacterTypes: 0,
            distinctCharacterTypesCheck: { label: 'Must be complex.  Match at least 3:', required: true },
            ignoreValidation: false,
            lowerCase: { label: 'Lower Case', available: false },
            minLength: 14,
            minimumLengthCheck: { label: 'Password should have at least 14 characters.', required: true },
            numeric: { label: 'Number', available: false },
            responseCode: 200,
            specialCharacter: { label: 'Special Chararacters', available: false },
            upperCase: { label: 'Upper Case', available: false } } );
        await store.dispatch( fetchPasswordRules( 'some/path' ) ).then( response =>{
            const actions = store.getActions();

            expect( actions[ 0 ] ).to.deep.equal( { type: 'PASSWORD_RULES',
                rules:
                    { passwordRules:
                      { isLowerCaseRequired: false,
                        isUpperCaseRequired: false,
                        isSpecialCharRequired: false,
                        isNumberRequired: false,
                        minLength: 14,
                        numUsed: 0 } },
                labels:
                    { passwordLabels:
                      { isLowerCaseRequired: 'Lower Case',
                        isUpperCaseRequired: 'Upper Case',
                        isSpecialCharRequired: 'Special Chararacters',
                        isNumberRequired: 'Number',
                        length: 'Password should have at least 14 characters.',
                        numUsed: 0 } },
                minRules: 0,
                minRulesLabel: 'Must be complex.  Match at least 3:',
                ignoreValidation: false } );
                } );

        fetchMock.restore();
    } );
    it ( 'should call submitPass with server error and add message is called', async() => {
        const store = mockStore( {} );
        fetchMock.mock( '*', { throws: { text: 'some error' } } );
        await store.dispatch( fetchPasswordRules( 'some/path' ) ).then( response =>{
            const actions = store.getActions();

            expect( actions[ 0 ].type ).to.deep.equal( 'ADD_MESSAGE' );

        } );

        fetchMock.restore();
    } );
} );
