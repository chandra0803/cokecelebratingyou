/*global jest*/

jest.mock( 'react-redux' );
// jest.mock( 'react-router-dom' );
// jest.mock( '../../../shared/locale-string' );
import { mount } from 'enzyme';

import LoaderButton from '../../../../shared/loader-button/loader-button';
import React from 'react';
import renderer from 'react-test-renderer';
import CodeVerification from '../code-verification';

const defaultProps = {
    login: {
        message: {

        },
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
    passConfirm: {

    },
    fetchPasswordRules: jest.fn(),
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

    const component = renderer.create( <CodeVerification { ...props } /> );
    const tree = component.toJSON();

    expect( tree ).toMatchSnapshot();
} );
it ( 'disables the button when a token has not been entered.', () => {
    const props = {
        ...defaultProps
    };

    const tree = mount( <CodeVerification { ...props } /> );

    const button = tree.find( LoaderButton );

    expect ( button.props().disabled ).toBe( true );

} );
it ( 'enables the button when a token has been entered.', () => {
    const props = {
        ...defaultProps
    };

    const tree = mount( <CodeVerification { ...props } /> );

    const button = tree.find( LoaderButton );

    expect ( button.props().disabled ).toBe( true );

    tree.setState( { token: 'test' } );

    expect ( button.props().disabled ).toBe( false );

} );
it ( 'changes token in state when the user changes something in the token input ', () => {
    const props = {
        ...defaultProps,
        passConfirm: {
            passwordRules: {
                some: 'rule'
            }
        }
    };

    const tree = mount( <CodeVerification { ...props } /> );

    const input = tree.find( 'input[name="token"]' );

    input.simulate( 'change', { target: { name: input.props().name, value: 'test' } } );

    expect ( tree.state( 'token' ) ).toBe( 'test' );
} );
it ( 'changes display state when display is false and there is a login message present', () => {
    const login = {
        login: {
            message: {}
        }
    };
    const props = {
        ...defaultProps,
        ...login
    };

    const tree = mount( <CodeVerification { ...props } /> );

    tree.setState( { display: false } );
    expect ( tree.state( 'display' ) ).toBe( false );
    tree.setState( { display: true } );
    expect ( tree.state( 'display' ) ).toBe( true );

} );
it ( 'changes display state when display is false and there is a login message and token present', () => {
    const login = {
        login: {
            message: {}
        }
    };
    const props = {
        ...defaultProps,
        ...login
    };
    window.location.url = 'http://localhost:8001/g6bb8/login.do?userToken=H810x421';
    const tree = mount( <CodeVerification { ...props } /> );
    tree.setState( { display: false } );
    expect ( tree.state( 'display' ) ).toBe( false );
    tree.setState( { display: true } );
    expect ( tree.state( 'display' ) ).toBe( true );
    window.location.url = null;

} );
it ( 'executes sendToken prop when a token is present in the url', () => {
    const login = {
        login: {
        },
    };
    const props = {
        ...defaultProps,
        ...login,
        sendToken: jest.fn()
    };
    console.log( props );
    window.location.url = 'http://localhost:8001/g6bb8/login.do?userToken=H810x421';

    mount( <CodeVerification { ...props } /> );
    expect ( props.sendToken.mock.calls.length ).toBe( 1 );
    window.location.url = null;

} );
it ( 'executes sendToken prop when a token is present in the url with token and activation is true', () => {
    const login = {
        login: {
        },
    };
    const props = {
        ...defaultProps,
        ...login,
        sendToken: jest.fn()
    };
    window.location.url = 'http://localhost:8001/g6bb8/login.do?userToken=H810x421&activation=true';

    mount( <CodeVerification { ...props } /> );
    expect ( props.sendToken.mock.calls.length ).toBe( 1 );
    window.location.url = null;

} );
it ( 'executes sendToken prop when a token is present in the url and activation is true', () => {
    const props = {
        ...defaultProps,
        sendToken: jest.fn()
    };
    window.location.url = 'http://localhost:8001/g6bb8/login.do?activation=true';

    mount( <CodeVerification { ...props } /> );
    expect ( props.sendToken.mock.calls.length ).toBe( 0 );
    window.location.url = null;

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
    const tree = mount( <CodeVerification { ...props } /> );

    String.prototype.template = function ( o ) { return o; };
    const alerts = tree.find( '.alert-success' );

    expect ( alerts.length ).toBe( 1 );
    tree.setProps( { login: { message: { error: { template: jest.fn() } } } } );
    tree.setProps( { login: {} } );
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
    String.prototype.template = function ( o ) { return o; };
    const tree = mount( <CodeVerification { ...props } /> );

    const alerts = tree.find( '.alert-error' );

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
    window.location.url = 'http://localhost:8001/g6bb8/login.do?userToken=H810x421&activation=true';
    String.prototype.template = function ( o ) { return o; };
    const tree = mount( <CodeVerification { ...props } /> );

    const alerts = tree.find( '.alert-error' );

    expect ( alerts.length ).toBe( 1 );
    window.location.url = null;
} );
it ( 'executes sendToken prop when the user presses enter in the token input.', () => {
    const props = {
        ...defaultProps,
        sendToken: jest.fn()
    };

    const tree = mount( <CodeVerification { ...props } /> );

    tree.setState( { token: 'test' } );

    const tokenInput = tree.find( 'input[name="token"]' );

    tokenInput.simulate( 'keyPress', { charCode: 13 } );

    expect ( props.sendToken.mock.calls.length ).toBe( 1 );

} );
it ( 'does not execute sendToken prop when the user presses a key other than enter in the token input.', () => {
    const props = {
        ...defaultProps,
        sendToken: jest.fn()
    };

    const tree = mount( <CodeVerification { ...props } /> );

    tree.setState( { token: 'test' } );

    const tokenInput = tree.find( 'input[name="token"]' );

    tokenInput.simulate( 'keyPress', { charCode: 12 } );

    expect ( props.sendToken.mock.calls.length ).toBe( 0 );

} );
it ( 'executes change form prop when the top back to login link is clicked.', () => {
    const props = {
        ...defaultProps,
        handleFormChange: jest.fn()
    };
    const tree = mount( <CodeVerification { ...props } /> );

    const link = tree.find( 'a' ).first();

    link.simulate( 'click' );

    expect ( props.handleFormChange.mock.calls.length ).toBe( 1 );
} );
it ( 'executes modal prop when the help link is clicked.', () => {
    const props = {
        ...defaultProps,
        toggleModalDisplay: jest.fn()
    };

    const tree = mount( <CodeVerification { ...props } /> );

    const link3 = tree.find( '.help a' );

    link3.simulate( 'click' );

    expect ( props.toggleModalDisplay.mock.calls.length ).toBe( 1 );
} );
