/*global jest*/

jest.mock( 'react-redux' );
// jest.mock( 'react-router-dom' );
// jest.mock( '../../../shared/locale-string' );
import { mount, shallow } from 'enzyme';

import LoaderButton from '../../../../shared/loader-button/loader-button';
import React from 'react';
import renderer from 'react-test-renderer';
import CreatePassword from '../create-password';

const defaultProps = {
    login: {

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
    strutsToken: '570210087a24f665d94727351f1c6597',
    verification: {
        activation: {
            userName: 'bhd-001',

        },
        token: {
            token: ''
        }
    },
    passConfirm: {
        ignoreValidation: false,
        minRules: 3,
        minRulesLabel: 'Must be complex.  Match at least 3:',
        passwordRules: {
            isLowerCaseRequired: true,
            isNumberRequired: true,
            isSpecialCharRequired: true,
            isUpperCaseRequired: true,
            minLength: 8
        },
        passwordLabels: {}
    }

};

Object.defineProperty( window.location, 'href', {
  writable: true,
  value: 'http://localhost:8001/g6bb8/login.do?userToken=H810x421'
} );
// const getString = ( key ) => 'Mock Data: ' + key;

it( 'renders the dom like the snapshot when the props have default values.', () => {
    const props = {
        ...defaultProps
    };

    const component = renderer.create( <CreatePassword { ...props } /> );
    const tree = component.toJSON();

    expect( tree ).toMatchSnapshot();
} );
it ( 'disables the button when a password has not been entered', () => {
    const props = {
        ...defaultProps
    };

    const tree = mount( <CreatePassword { ...props } /> );

    const button = tree.find( LoaderButton );

    expect ( button.props().disabled ).toBe( true );

    tree.setState( { password1: '' } );

    expect ( button.props().disabled ).toBe( true );

} );
it ( 'enables the button when passwords are entered.', () => {
    const props = {
        ...defaultProps
    };

    const tree = mount( <CreatePassword { ...props } /> );

    const button = tree.find( LoaderButton );

    expect ( button.props().disabled ).toBe( true );
    const input = tree.find( 'input[name="password1"]' );
    input.simulate( 'change', { target: { name: input.props().name, value: '!Q@W3e4r' } } );

    expect ( button.props().disabled ).toBe( false );

} );
it ( 'executes change form prop when the top back to login link is clicked.', () => {
    const props = {
        ...defaultProps,
        handleFormChange: jest.fn()
    };

    const tree = mount( <CreatePassword { ...props } /> );

    const link = tree.find( 'a' ).first();

    link.simulate( 'click' );

    expect ( props.handleFormChange.mock.calls.length ).toBe( 1 );
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

    const tree = mount( <CreatePassword { ...props } /> );

    const alerts = tree.find( '.alert-error' );

    expect ( alerts.length ).toBe( 1 );
} );
it ( 'shows error alerts when there are error messages.', () => {
    const login = {
        login: {
            ...defaultProps.login,
            messages:
                [ {
                    error: 'error'
                },
                {
                    error: 'error'
                } ]
        }
    };
    const props = {
        ...defaultProps,
        ...login
    };

    const tree = mount( <CreatePassword { ...props } /> );

    const alerts = tree.find( '.alert-error' );

    expect ( alerts.length ).toBe( 2 );
} );
it ( 'shows a success alert when there is a success message.', () => {
    const login = {
        login: {
            ...defaultProps.login,
            message: {
                success: 'success'
            }
        }
    };
    const props = {
        ...defaultProps,
        ...login
    };

    const tree = mount( <CreatePassword { ...props } /> );

    const alerts = tree.find( '.alert-success' );

    expect ( alerts.length ).toBe( 1 );
} );
it ( 'executes submitPass prop when the user presses enter in the password input.', () => {
    const props = {
        ...defaultProps,
        submitPass: jest.fn()
    };

    const tree = mount( <CreatePassword { ...props } /> );

    tree.setState( { password1: 'password', disabled: false } );

    const pass1 = tree.find( 'input[name="password1"]' );

    pass1.simulate( 'keyPress', { charCode: 13 } );

    expect ( props.submitPass.mock.calls.length ).toBe( 1 );
} );
it ( 'does not execute submitPass prop when the user presses key other than enter in the password input with matching passwords.', () => {
    const props = {
        ...defaultProps,
        submitPass: jest.fn()
    };

    const tree = mount( <CreatePassword { ...props } /> );

    tree.setState( { password1: 'password' } );

    const pass1 = tree.find( 'input[name="password1"]' );

    pass1.simulate( 'keyPress', { charCode: 12 } );

    expect ( props.submitPass.mock.calls.length ).toBe( 0 );
} );
it ( 'does not execute submitPass prop when the user presses enter in the password input no password.', () => {
    const props = {
        ...defaultProps,
        submitPass: jest.fn()
    };

    const tree = mount( <CreatePassword { ...props } /> );
    expect( tree.state( 'password1' ) ).toBe( '' );
    const pass1 = tree.find( 'input[name="password1"]' );

    pass1.simulate( 'keyPress', { charCode: 13 } );

    expect ( props.submitPass.mock.calls.length ).toBe( 0 );
} );
it ( 'does not execute submitPass prop when the user presses enter in the password input and disabled.', () => {
    const props = {
        ...defaultProps,
        submitPass: jest.fn()
    };

    const tree = mount( <CreatePassword { ...props } /> );
    tree.setState( { password1: 'password', disabled: true } );
    const pass1 = tree.find( 'input[name="password1"]' );

    pass1.simulate( 'keyPress', { charCode: 13 } );

    expect ( props.submitPass.mock.calls.length ).toBe( 0 );
} );
it ( 'changes passsword in state when the user changes something in the password1 input ', () => {
    const props = {
        ...defaultProps
    };

    const tree = mount( <CreatePassword { ...props } /> );

    const input = tree.find( 'input[name="password1"]' );

    input.simulate( 'change', { target: { name: input.props().name, value: 'test' } } );

    expect ( tree.state( 'password1' ) ).toBe( 'test' );
} );
it ( 'executes modal prop when the help link is clicked.', () => {
    const props = {
        ...defaultProps,
        toggleModalDisplay: jest.fn()
    };

    const tree = mount( <CreatePassword { ...props } /> );

    const link3 = tree.find( '.help a' );

    link3.simulate( 'click' );

    expect ( props.toggleModalDisplay.mock.calls.length ).toBe( 1 );
} );
it ( 'executes submitPass prop when the user presses enter in the password input.', () => {
    const rules = {
        passConfirm: {
            passwordRules: {
                isLowerCaseRequired: false,
                isNumberRequired: false,
                isSpecialCharRequired: false,
                isUpperCaseRequired: false,
                minLength: 0
            },
            passwordLabels: {}
        }
    };
    const props = {
        ...defaultProps,
        ...rules,
        submitPass: jest.fn()
    };

    const tree = mount( <CreatePassword { ...props } /> );

    tree.instance().validate( '!Q@W3e4r' );
    tree.setState( { password1: 'password', disabled: false } );
    const pass1 = tree.find( 'input[name="password1"]' );

    pass1.simulate( 'keyPress', { charCode: 13 } );

    expect ( props.submitPass.mock.calls.length ).toBe( 1 );
} );
