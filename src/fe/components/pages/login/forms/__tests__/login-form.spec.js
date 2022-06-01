/*global jest*/

jest.mock( 'react-redux' );
// jest.mock( 'react-router-dom' );
// jest.mock( '../../../shared/locale-string' );
import { mount } from 'enzyme';

import LoaderButton from '../../../../shared/loader-button/loader-button';
import React from 'react';
import renderer from 'react-test-renderer';
import LoginForm from '../login-form';

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
            content: {
                'login.errors.login_id_req': 'Login ID is required.',
                'login.errors.password_req': 'Password is required.',
                'login.loginpage.activate_account': 'Activate your account',
                'login.loginpage.activation': 'Activation',
                'login.loginpage.activation_attributes': 'Activation Attributes',
                'login.loginpage.back_to_login': 'Back to Login',
                'login.loginpage.code_verification': 'Code Verification',
                'login.loginpage.confirm_password': 'Confirm Password',
                'login.loginpage.contact_information': 'Contact Information',
                'login.loginpage.country_code': 'Country Code',
                'login.loginpage.create_password': 'Create Password',
                'login.loginpage.forgot_login_id': 'Forgot Login ID',
                'login.loginpage.forgot_password': 'Forgot Password',
                'login.loginpage.help': 'Help',
                'login.loginpage.i_forgot_login_id': 'I forgot my login ID',
                'login.loginpage.i_forgot_password': 'I forgot my password',
                'login.loginpage.intro_para_one': 'This is the one-stop shop for your program. Check back often for updates!',
                'login.loginpage.log_in': 'Log In',
                'login.loginpage.message_sent': 'Message Sent',
                'login.loginpage.method_of_contact': 'Method of Contact',
                'login.loginpage.new_password': 'New Password',
                'login.loginpage.no_login_id_or_password': 'No Login ID or Password?',
                'login.loginpage.no_password': 'No Password?',
                'login.loginpage.one_time_code': 'One Time Code',
                'login.loginpage.password': 'Password',
                'login.loginpage.recovery_information': 'Recovery Information',
                'login.loginpage.register': 'Register',
                'login.loginpage.register_new_account': 'Register a new account',
                'login.loginpage.reset_password': 'Reset Password',
                'login.loginpage.title': 'Welcome',
                'login.loginpage.username': 'Login ID',
				'login.loginpage.user_id_is_your_employee_number': 'User id is your employee number',
                'login.password.reset.phone_number': 'Phone Number',
                'participant.participant.email_address': 'Email Address',
                'system.button.close': 'Close',
                'system.button.submit': 'Submit',
                'system.general.contact_us': 'Contact Us',
                'system.general.copyright_text': 'Copyright &copy;2017 BI WORLDWIDE&trade;. All rights reserved.',
                'system.general.help_text': 'Contact your program administrator for help.',
                'system.general.help_title': 'Help',
                'system.general.privacy_policy': 'Privacy Policy',
                'system.general.t&c': 'T&Cs' },
            selfEnrollment: false,
            contactUsEmailConfirmation: false,
            strutsToken: '570210087a24f665d94727351f1c6597'
    };

it( 'renders the dom like the snapshot when the props have default values.', () => {
    const props = {
        ...defaultProps,
        setLanguage: jest.fn()
    };
    const component = renderer.create(
        <div>
            <LoginForm { ...props } />
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
    } );
    it( 'renders the dom like the snapshot when the props have default values.', () => {
        const content = {
            content: {
                'login.errors.login_id_req': 'Login ID is required.',
                'login.errors.password_req': 'Password is required.',
                'login.loginpage.activate_account': 'Activate your account',
                'login.loginpage.activation': 'Activation',
                'login.loginpage.activation_attributes': 'Activation Attributes',
                'login.loginpage.back_to_login': 'Back to Login',
                'login.loginpage.code_verification': 'Code Verification',
                'login.loginpage.confirm_password': 'Confirm Password',
                'login.loginpage.contact_information': 'Contact Information',
                'login.loginpage.country_code': 'Country Code',
                'login.loginpage.create_password': 'Create Password',
                'login.loginpage.forgot_login_id': 'Forgot Login ID',
                'login.loginpage.forgot_password': 'Forgot Password',
                'login.loginpage.help': 'Help',
                'login.loginpage.i_forgot_login_id': 'I forgot my login ID',
                'login.loginpage.i_forgot_password': 'I forgot my password',
                'login.loginpage.intro_para_one': 'This is the one-stop shop for your program. Check back often for updates!',
                'login.loginpage.intro_para_two': 'paragraph 2!',
                'login.loginpage.log_in': 'Log In',
                'login.loginpage.message_sent': 'Message Sent',
                'login.loginpage.method_of_contact': 'Method of Contact',
                'login.loginpage.new_password': 'New Password',
                'login.loginpage.no_login_id_or_password': 'No Login ID or Password?',
                'login.loginpage.no_password': 'No Password?',
                'login.loginpage.one_time_code': 'One Time Code',
                'login.loginpage.password': 'Password',
                'login.loginpage.recovery_information': 'Recovery Information',
                'login.loginpage.register': 'Register',
                'login.loginpage.register_new_account': 'Register a new account',
                'login.loginpage.reset_password': 'Reset Password',
                'login.loginpage.title': 'Welcome',
                'login.loginpage.username': 'Login ID',
				'login.loginpage.user_id_is_your_employee_number': 'User id is your employee number',
                'login.password.reset.phone_number': 'Phone Number',
                'participant.participant.email_address': 'Email Address',
                'system.button.close': 'Close',
                'system.button.submit': 'Submit',
                'system.general.contact_us': 'Contact Us',
                'system.general.copyright_text': 'Copyright &copy;2017 BI WORLDWIDE&trade;. All rights reserved.',
                'system.general.help_text': 'Contact your program administrator for help.',
                'system.general.help_title': 'Help',
                'system.general.privacy_policy': 'Privacy Policy',
                'system.general.t&c': 'T&Cs' },
        };
        const login = {
            login: {
                locales: []
            }
        };
        const props = {
            ...content,
            ...login,
            setLanguage: jest.fn()
        };
        const component = renderer.create(
            <div>
                <LoginForm { ...props } />
            </div>
        );

        const tree = component.toJSON();
        expect( tree ).toMatchSnapshot();
        } );
it ( 'disables the button when username and password have not been entered', () => {
    const props = {
        ...defaultProps,
        setLanguage: jest.fn()
    };
    const tree = mount( <LoginForm { ...props } /> );

    const button = tree.find( LoaderButton );

    expect ( button.props().disabled ).toBe( true );

    tree.setState( { username: 'testing' } );
    expect ( button.props().disabled ).toBe( true );

    tree.setState( { username: '', password: 'testing' } );
    expect ( button.props().disabled ).toBe( true );


    tree.setState( { username: 'testing', password: 'testing' } );
    expect ( button.props().disabled ).toBe( false );

} );
it ( 'executes changeForm when the Forgot Login ID link is clicked.', () => {
    const props = {
        ...defaultProps,
        handleFormChange: jest.fn(),
        setLanguage: jest.fn()
    };

    const tree = mount( <LoginForm { ...props } /> );

    const link = tree.find( '.forgot-link' ).first();

    link.simulate( 'click' );

    expect ( props.handleFormChange.mock.calls.length ).toBe( 1 );
} );
it ( 'executes changeForm when the Forgot Password ID link is clicked.', () => {
    const props = {
        ...defaultProps,
        handleFormChange: jest.fn(),
        setLanguage: jest.fn()
    };

    const tree = mount( <LoginForm { ...props } /> );

    const link = tree.find( '.forgot-link' ).at( 1 );

    link.simulate( 'click' );

    expect ( props.handleFormChange.mock.calls.length ).toBe( 1 );
} );
it ( 'executes changeForm when the activate account link is clicked.', () => {
    const props = {
        ...defaultProps,
        handleFormChange: jest.fn(),
        setLanguage: jest.fn()
    };

    const tree = mount( <LoginForm { ...props } /> );

    const link = tree.find( 'a' ).at( 2 );

    link.simulate( 'click' );

    expect ( props.handleFormChange.mock.calls.length ).toBe( 1 );
} );
it ( 'executes togglePasswordVisible when the eye icon is clicked.', () => {
    const props = {
        ...defaultProps,
        togglePasswordVisible: jest.fn(),
        setLanguage: jest.fn()
    };

    const tree = mount( <LoginForm { ...props } /> );

    const link = tree.find( '.eye' );

    link.simulate( 'click' );

    expect ( props.togglePasswordVisible.mock.calls.length ).toBe( 1 );
} );
it ( 'executes setLanguage and changeLanguage when the language select is changed.', () => {
    const props = {
        ...defaultProps,
        setLanguage: jest.fn(),
        changeLanguage: jest.fn()
    };

    const tree = mount( <LoginForm { ...props } /> );

    const select = tree.find( 'select' );

    select.simulate( 'change', { target: { value: 'en_GB' } } );

    expect ( props.setLanguage.mock.calls.length ).toBe( 1 );
    expect ( props.changeLanguage.mock.calls.length ).toBe( 1 );
} );
it ( 'executes loginFunc prop when enter is pressed.', () => {
    const props = {
        ...defaultProps,
        setLanguage: jest.fn(),
        loginFunc: jest.fn()
    };

    const tree = mount( <LoginForm { ...props } /> );

    const input = tree.find( 'input[name="username"]' );

    tree.setState( { username: 'testing', password: 'testing' } );

    input.simulate( 'keyPress', { charCode: 13 } );


    expect ( props.loginFunc.mock.calls.length ).toBe( 1 );
} );
it ( 'does not execute loginFunc prop when enter is pressed with empty inputs.', () => {
    const props = {
        ...defaultProps,
        setLanguage: jest.fn(),
        loginFunc: jest.fn()
    };

    const tree = mount( <LoginForm { ...props } /> );

    const input = tree.find( 'input[name="username"]' );

    tree.setState( { username: '', password: '' } );

    input.simulate( 'keyPress', { charCode: 13 } );

    expect ( props.loginFunc.mock.calls.length ).toBe( 0 );
    const password = tree.find( 'input[name="password"]' );
    password.simulate( 'keyPress', { charCode: 13 } );
    expect ( props.loginFunc.mock.calls.length ).toBe( 0 );
} );
it ( 'changes username in state when the user changes something in the username input ', () => {
    const props = {
        ...defaultProps,
        setLanguage: jest.fn()
    };

    const tree = mount( <LoginForm { ...props } /> );

    const input = tree.find( 'input[name="username"]' );

    input.simulate( 'change', { target: { name: input.props().name, value: 'test' } } );

    expect ( tree.state( 'username' ) ).toBe( 'test' );
} );
it ( 'changes password in state when the user changes something in the password input ', () => {
    const props = {
        ...defaultProps,
        setLanguage: jest.fn()
    };

    const tree = mount( <LoginForm { ...props } /> );

    const input = tree.find( 'input[name="password"]' );

    input.simulate( 'change', { target: { name: input.props().name, value: 'test' } } );

    expect ( tree.state( 'password' ) ).toBe( 'test' );
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
        setLanguage: jest.fn(),
        ...login
    };

    const tree = mount( <LoginForm { ...props } /> );

    const alerts = tree.find( '.alert-error' );

    expect ( alerts.length ).toBe( 1 );
} );
it ( 'shows an  alert when there is a server message.', () => {
    const login = {
        login: {
            ...defaultProps.login,
            message: {
                type: 'success',
                name: '',
                id: '',
                text: ''
            }
        }
    };
    const props = {
        ...defaultProps,
        setLanguage: jest.fn(),
        ...login
    };

    const tree = mount( <LoginForm { ...props } /> );

    const alerts = tree.find( '.alert-success' );

    expect ( alerts.length ).toBe( 1 );
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
        setLanguage: jest.fn(),
        ...login
    };

    const tree = mount( <LoginForm { ...props } /> );

    const alerts = tree.find( '.alert-success' );

    expect ( alerts.length ).toBe( 1 );
} );
it ( 'shows an eye open icon when passwordVisible is true.', () => {
    const login = {
        login: {
            ...defaultProps.login,
            passwordVisible: true
        }
    };
    const props = {
        ...defaultProps,
        setLanguage: jest.fn(),
        ...login
    };

    const tree = mount( <LoginForm { ...props } /> );

    const eye = tree.find( '.eye' );
    expect ( eye.length ).toBe( 1 );
} );
it ( 'executes toggleModal when the register account link is clicked.', () => {
    const login = {
        login: {
            ...defaultProps.login,
            selfEnrollment: true
        }
    };
    const props = {
        ...defaultProps,
        ...login,
        toggleModalDisplay: jest.fn(),
        setLanguage: jest.fn()
    };

    const tree = mount( <LoginForm { ...props } /> );

    const link = tree.find( 'a' ).at( 3 );

    link.simulate( 'click' );

    expect ( props.toggleModalDisplay.mock.calls.length ).toBe( 1 );
} );
