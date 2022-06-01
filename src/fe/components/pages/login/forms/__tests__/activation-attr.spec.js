/*global jest*/

jest.mock( 'react-redux' );
// jest.mock( 'react-router-dom' );
// jest.mock( '../../../shared/locale-string' );
import { mount } from 'enzyme';

import LoaderButton from '../../../../shared/loader-button/loader-button';
import React from 'react';
import renderer from 'react-test-renderer';
import ActivationAttr from '../activation-attr';

const defaultProps = {
    login: {
        activAttrUrl: 'somevalue',
        activInitUrl: 'somevalue',
        locales: [

                {
                    code: 'zh_CN',
                    label: 'Chinese [Simplified]'
                },

                {
                    code: 'zh_TW',
                    label: 'Chinese [Traditional]'
                },

                {
                    code: 'nl_NL',
                    label: 'Dutch [Netherlands]'
                },

                {
                    code: 'en_GB',
                    label: 'English [British]'
                },

                {
                    code: 'en_US',
                    label: 'English [U.S]'
                },

                {
                    code: 'fr_CA',
                    label: 'French [Canadian]'
                },

                {
                    code: 'fr_FR',
                    label: 'French [Europe]'
                },

                {
                    code: 'de_DE',
                    label: 'German [Germany]'
                },

                {
                    code: 'it_IT',
                    label: 'Italian [Italy]'
                },

                {
                    code: 'ja_JP',
                    label: 'Japanese [Japan]'
                },

                {
                    code: 'ar_AE',
                    label: 'Klingon'
                },

                {
                    code: 'ko_KR',
                    label: 'Korean [South Korea]'
                },

                {
                    code: 'pl_PL',
                    label: 'Polish [Poland]'
                },

                {
                    code: 'pt_BR',
                    label: 'Portuguese [Brazil]'
                },

                {
                    code: 'ru_RU',
                    label: 'Russian [Russia]'
                },

                {
                    code: 'es_MX',
                    label: 'Spanish [Latin America]'
                },

                {
                    code: 'es_ES',
                    label: 'Spanish [Spain]'
                },

                {
                    code: 'th_TH',
                    label: 'Thai [Thailand]'
                },

                {
                    code: 'tr_TR',
                    label: 'Turkish [Turkey]'
                },

                {
                    code: 'vi_VN',
                    label: 'Vietnamese [Vietnam]'
                },

        ],

    },
    forgotId: {

    },
    forgotPass: {

    },
    activation: {
        activation:
        {
            'responseCode': 200,
            'exists': true,
            'userActivated': true,
            'activationFields': [
                {
                    'participantIdentifierId': 5005,
                    'label': 'Email Address',
                    'description': 'Enter your corporate email address',
                    'value': null
                },
                {
                    'participantIdentifierId': 5080,
                    'label': 'Email Address2',
                    'description': 'Enter your personal email address',
                    'value': null
                }
            ],

        },
        id: {}
    },
    verification: {

    },
    content: [
        {
            key: 'REGISTER',
            code: 'login.loginpage',
            content: 'Register'
        },
        {
            key: 'TITLE',
            code: 'login.loginpage',
            content: 'Welcome'
        },
        {
            key: 'FORGOT_PASSWORD',
            code: 'login.loginpage',
            content: 'Forgot Password'
        },
        {
            key: 'FORGOT_LOGIN_ID',
            code: 'login.loginpage',
            content: 'Forgot Login ID'
        },
        {
            key: 'REGISTER_NEW_ACCOUNT',
            code: 'login.loginpage',
            content: 'Register a new account'
        },
        {
            key: 'LOG_IN',
            code: 'login.loginpage',
            content: 'Log In'
        },
        {
            key: 'NO_LOGIN_ID_OR_PASSWORD',
            code: 'login.loginpage',
            content: 'No Login ID or Password?'
        },
        {
            key: 'PASSWORD',
            code: 'login.loginpage',
            content: 'Password'
        },
        {
            key: 'I_FORGOT_PASSWORD',
            code: 'login.loginpage',
            content: 'I forgot my password'
        },
        {
            key: 'I_FORGOT_LOGIN_ID',
            code: 'login.loginpage',
            content: 'I forgot my login ID'
        },
        {
            key: 'USERNAME',
            code: 'login.loginpage',
            content: 'Login ID'
        },
        {
            key: 'INTRO_PARA_ONE',
            code: 'login.loginpage',
            content: 'This is your one-stop shop for all of your program information. Check back often as information changes regularly.'
        },
        {
            key: 'INTRO_PARA_TWO',
            code: 'login.loginpage',
            content: '???login.loginpage.INTRO_PARA_TWO???'
        },
        {
            key: 'LOGIN_ID_REQ',
            code: 'login.errors',
            content: 'Login ID is required.'
        },
        {
            key: 'PASSWORD_REQ',
            code: 'login.errors',
            content: 'Password is required.'
        },
        {
            key: 'COPYRIGHT_TEXT',
            code: 'system.general',
            content: 'Copyright &copy;2017 BI WORLDWIDE&trade;. All rights reserved.'
        },
        {
            key: 'CONTACT_US',
            code: 'system.general',
            content: 'Contact Us'
        },
        {
            key: 'T&C',
            code: 'system.general',
            content: 'T&Cs'
        },
        {
            key: 'PRIVACY_POLICY',
            code: 'system.general',
            content: 'Privacy Policy'
        }
    ],            selfEnrollment: false,
    contactUsEmailConfirmation: false,
    strutsToken: '570210087a24f665d94727351f1c6597'
};

// const getString = ( key ) => 'Mock Data: ' + key;

it( 'renders the dom like the snapshot when the props have default values.', () => {
    const props = {
        ...defaultProps
    };

    const component = renderer.create( <ActivationAttr { ...props } /> );
    const tree = component.toJSON();

    expect( tree ).toMatchSnapshot();
} );
it ( 'executes change form prop when the top back to login link is clicked.', () => {
    const props = {
        ...defaultProps,
        handleFormChange: jest.fn()
    };

    const tree = mount( <ActivationAttr { ...props } /> );

    const link = tree.find( 'a' ).first();

    link.simulate( 'click' );

    expect ( props.handleFormChange.mock.calls.length ).toBe( 1 );
} );
it ( 'shows a success alert when there is a success message.', () => {
    const login = {
        login: {
            ...defaultProps.login,
            message: {
                success: 'successful'
            }
        }
    };
    const props = {
        ...defaultProps,
        ...login
    };
    const tree = mount( <ActivationAttr { ...props } /> );

    const alerts = tree.find( '.alert-success' );

    expect ( alerts.length ).toBe( 1 );
} );
it ( 'shows a error alert when there is a error message.', () => {
    const login = {
        login: {
            ...defaultProps.login,
            message: {
                error: 'error'
            }
        }
    };
    const props = {
        ...defaultProps,
        ...login
    };

    const tree = mount( <ActivationAttr { ...props } /> );

    const alerts = tree.find( '.alert-error' );

    expect ( alerts.length ).toBe( 1 );
} );
it ( 'executes fetchCollectionData prop when the user presses clicks submit with a valid email address and does not execute if input is empty.', () => {
    const props = {
        ...defaultProps,
        fetchCollectionData: jest.fn()
    };

    const tree = mount( <ActivationAttr { ...props } /> );

    const userInput = tree.find( 'input[name="Email Address"]' );
    userInput.simulate( 'change', { target: { name: userInput.props().name, value: 'test@test.com' } } );
    const button = tree.find( LoaderButton );
    button.simulate( 'click' );

    expect ( props.fetchCollectionData.mock.calls.length ).toBe( 1 );
    userInput.simulate( 'change', { target: { name: userInput.props().name, value: '' } } );

    button.simulate( 'click' );

    expect ( props.fetchCollectionData.mock.calls.length ).toBe( 1 );

} );
it ( 'does not execute fetchCollectionData prop when the user presses enter in the Email Address input with no email.', () => {
    const props = {
        ...defaultProps,
        fetchCollectionData: jest.fn()
    };

    const tree = mount( <ActivationAttr { ...props } /> );

    const userInput = tree.find( 'input[name="Email Address"]' );

    userInput.simulate( 'keyPress', { charCode: 13 } );

    expect ( props.fetchCollectionData.mock.calls.length ).toBe( 0 );

} );
it ( 'does not execute fetchCollectionData prop when key other than enter is pressed', () => {
    const props = {
        ...defaultProps,
        fetchCollectionData: jest.fn()
    };

    const tree = mount( <ActivationAttr { ...props } /> );

    const userInput = tree.find( 'input[name="Email Address"]' );

    userInput.simulate( 'keyPress', { charCode: 12 } );

    expect ( props.fetchCollectionData.mock.calls.length ).toBe( 0 );

} );
it ( 'does execute fetchCollectionData prop when the user presses enter in the Email Address input with an email.', () => {
    const props = {
        ...defaultProps,
        fetchCollectionData: jest.fn()
    };

    const tree = mount( <ActivationAttr { ...props } /> );

    const userInput = tree.find( 'input[name="Email Address"]' );
    tree.setState( { value: 'test@test.com' } );
    userInput.simulate( 'keyPress', { charCode: 13 } );

    expect ( props.fetchCollectionData.mock.calls.length ).toBe( 1 );

} );
it ( 'does execute fetchCollectionData prop when the user presses enter in the Email Address input with an email.', () => {
    const verification = {
        verification: {
            activation:
            {
                'responseCode': 200,
                'exists': true,
                'userActivated': true,
                'activationFields': [
                    {
                        'participantIdentifierId': 5005,
                        'label': 'Email Address',
                        'description': 'Enter your corporate email address',
                        'value': null
                    },
                    {
                        'participantIdentifierId': 5080,
                        'label': 'Email Address2',
                        'description': 'Enter your personal email address',
                        'value': null
                    }
                ]
            },
            token: {

            }
        }
    };
    const activation = { activation: {} };
    const props = {
        ...defaultProps,
        ...verification,
        ...activation,
        fetchCollectionData: jest.fn()
    };
    const tree = mount( <ActivationAttr { ...props } /> );

    const userInput = tree.find( 'input[name="Email Address"]' );
    tree.setState( { value: 'test@test.com' } );
    userInput.simulate( 'keyPress', { charCode: 13 } );

    expect ( props.fetchCollectionData.mock.calls.length ).toBe( 1 );

} );
it ( 'disables the button when nothing has been entered has not been entered.', () => {
    const props = {
        ...defaultProps
    };

    const tree = mount( <ActivationAttr { ...props } /> );

    const button = tree.find( LoaderButton );

    expect ( button.props().disabled ).toBe( true );

} );
it ( 'executes modal prop when the help link is clicked.', () => {
    const props = {
        ...defaultProps,
        toggleModalDisplay: jest.fn()
    };

    const tree = mount( <ActivationAttr { ...props } /> );

    const link3 = tree.find( '.help a' );

    link3.simulate( 'click' );

    expect ( props.toggleModalDisplay.mock.calls.length ).toBe( 1 );
} );
