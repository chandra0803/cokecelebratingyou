/*global jest*/

jest.mock( 'react-redux' );
import { mount } from 'enzyme';
import React from 'react';
import LoginPage from '../login-page';
import renderer from 'react-test-renderer';
    const defaultProps = {
            login: {
                message: {},
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
                toggleModal: true,

            },
            forgotId: {
                details: {}
            },
            forgotPass: {

            },
            methodOfContact: {
                contactMethods: {
                    contactMethods: [
                        {
                            contactId: 12,
                            contactType: 'EMAIL',
                            value: 'tom***r@gmail.com'
                        },
                        {
                            contactId: 13,
                            contactType: 'PHONE',
                            value: '6*******71'
                        }
                    ]

                }
            },
            passConfirm: {},
            verification: {
                confirmation: {
                    userName: 'bhd-001'
                },
                activation: {
                    userName: 'bhd-002',
                    activationFields: []
                },
                token: {
                    token: ''
                }
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
                        }
                    ],

                },
                id: {}
            },
            attributes: {
                attributes: {
                    countryPhones: [
                        {
                            countryId: {

                            }
                        }
                    ]
                }
            },
            recovery: {
                countries: {
                    countryPhones: [
                        {
                            countryId: {

                            }
                        }
                    ]
                }
            },
            passwordRules: {
                length: 8,
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
                },
                {
                    key: 'MBLOX_TNC',
                    code: 'login.forgotpwd',
                    content: 'template'
                },
            ],            selfEnrollment: false,
            contactUsEmailConfirmation: false,
            strutsToken: '570210087a24f665d94727351f1c6597',
            fetchPasswordRules: jest.fn()

    };

const cmStringsArrayToObject = ( propertiesArray ) => {
    const propertiesObject = {};
    if( propertiesArray ) {

        propertiesArray.forEach( property => {
            // if( property.content.substring( 0, 3 ) !== '???' ) {
            propertiesObject[ `${ property.code }.${ property.key.toLowerCase() }` ] = property.content;
            // }
        } );
    }
    return propertiesObject;
};

it( 'renders the dom like the snapshot when the props have default values.', () => {
    const props = {
        ...defaultProps,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn(),
        showSpinner: true
    };
    const component = renderer.create(
        <div>
            <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } />
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
it( 'renders the dom like the snapshot when the currentForm is forgotUserId.', () => {
    const login = {
        login: {
            locales: [],
            currentForm: 'forgotUserId'
        }
    };
    const props = {
        ...defaultProps,
        ...login,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn()
    };
    const component = renderer.create(
        <div>
            <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } />
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
it( 'renders the dom like the snapshot when the currentForm is login.', () => {
    const login = {
        login: {
            locales: [],
            currentForm: 'login'
        }
    };
    const props = {
        ...defaultProps,
        ...login,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn()
    };
    const component = renderer.create(
        <div>
            <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } />
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
it( 'renders the dom like the snapshot when the currentForm is forgotPassword.', () => {
    const login = {
        login: {
            locales: [],
            currentForm: 'forgotPassword'
        }
    };
    const props = {
        ...defaultProps,
        ...login,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn()
    };
    const component = renderer.create(
        <div>
            <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } />
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
it( 'renders the dom like the snapshot when the currentForm is methodOfContact.', () => {
    const login = {
        login: {
            locales: [],
            currentForm: 'methodOfContact'
        }
    };
    const props = {
        ...defaultProps,
        ...login,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn()
    };
    const component = renderer.create(
        <div>
            <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } />
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
it( 'renders the dom like the snapshot when the currentForm is messageWindow.', () => {
    const login = {
        login: {
            locales: [],
            currentForm: 'messageWindow'
        }
    };
    const props = {
        ...defaultProps,
        ...login,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn()
    };
    const component = renderer.create(
        <div>
            <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } />
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
it( 'renders the dom like the snapshot when the currentForm is codeVerification.', () => {
    const login = {
        login: {
            locales: [],
            currentForm: 'codeVerification',
            message: {}
        }
    };
    const props = {
        ...defaultProps,
        ...login,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn()
    };
    const component = renderer.create(
        <div>
            <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } />
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
it( 'renders the dom like the snapshot when the currentForm is resetPassword.', () => {
    const login = {
        login: {
            locales: [],
            currentForm: 'resetPassword'
        },

        passConfirm: {
            passwordLabels: {
                isLowerCaseRequired: '???login.forgotpwd.MUST_LOWER_CASE???',
                isNumberRequired: '???login.forgotpwd.MUST_NUMBER???',
                isSpecialCharRequired: '???login.forgotpwd.MUST_SPECIAL_SYMBOL???',
                isUpperCaseRequired: '???login.forgotpwd.MUST_UPPER_CASE???',
                length: 'Password is too short. Password should have at least 8 characters.'
            },
            passwordRules: {
                isLowerCaseRequired: true,
                isNumberRequired: true,
                isSpecialCharRequired: true,
                isUpperCaseRequired: true,
                minLength: 8
            }
        },
    };
    const props = {
        ...defaultProps,
        ...login,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn()
    };
    const component = renderer.create(
        <div>
            <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } />
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
it( 'renders the dom like the snapshot when the currentForm is activation.', () => {
    const login = {
        login: {
            locales: [],
            currentForm: 'activation'
        }
    };
    const props = {
        ...defaultProps,
        ...login,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn()
    };
    const component = renderer.create(
        <div>
            <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } />
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
it( 'renders the dom like the snapshot when the currentForm is activAttr.', () => {
    const login = {
        login: {
            locales: [],
            currentForm: 'activAttr'
        }
    };
    const props = {
        ...defaultProps,
        ...login,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn()
    };
    const component = renderer.create(
        <div>
            <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } />
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
it( 'renders the dom like the snapshot when the currentForm is createPass.', () => {
    const login = {
        login: {
            locales: [],
            currentForm: 'createPass'
        },
        passConfirm: {
            passwordLabels: {
                isLowerCaseRequired: '???login.forgotpwd.MUST_LOWER_CASE???',
                isNumberRequired: '???login.forgotpwd.MUST_NUMBER???',
                isSpecialCharRequired: '???login.forgotpwd.MUST_SPECIAL_SYMBOL???',
                isUpperCaseRequired: '???login.forgotpwd.MUST_UPPER_CASE???',
                length: 'Password is too short. Password should have at least 8 characters.'
            },
            passwordRules: {
                isLowerCaseRequired: true,
                isNumberRequired: true,
                isSpecialCharRequired: true,
                isUpperCaseRequired: true,
                minLength: 8
            }
        }
    };
    const props = {
        ...defaultProps,
        ...login,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn()
    };
    const component = renderer.create(
        <div>
            <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } />
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
it( 'renders the dom like the snapshot when the currentForm is recovery.', () => {
    const login = {
        login: {
            locales: [],
            currentForm: 'accountLock'
        }
    };
    const props = {
        ...defaultProps,
        ...login,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn()
    };
    const component = renderer.create(
        <div>
            <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } />
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
it ( 'changes currentForm in state to codeVerification when token present', () => {

    const props = {
        ...defaultProps,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn()
    };
    window.location.url = 'http://localhost:8001/g6bb8/login.do?userToken=H810x421';
    const tree = mount( <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } /> );
    expect ( tree.state( 'currentForm' ) ).toBe( 'codeVerification' );
    window.location.url = null;

} );
it ( 'changes currentForm in state to forgotUserId when forgotUser present', () => {

    const props = {
        ...defaultProps,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn()
    };
    window.location.url = 'http://localhost:8001/g6bb8/login.do?forgotUser=true';
    const tree = mount( <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } /> );
    expect ( tree.state( 'currentForm' ) ).toBe( 'forgotUserId' );
    window.location.url = null;

} );
it ( 'changes currentForm in state to codeVerification when welcomeToken present', () => {

    const props = {
        ...defaultProps,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn()
    };
    window.location.url = 'http://localhost:8001/g6bb8/login.do?welcomeToken=H810x421';
    const tree = mount( <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } /> );
    expect ( tree.state( 'currentForm' ) ).toBe( 'codeVerification' );
    window.location.url = null;

} );
it ( 'changes currentForm in state to recovery when recovery present in url', () => {

    const props = {
        ...defaultProps,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn()
    };
    window.location.url = 'http://localhost:8001/g6bb8/login.do?recovery=true';
    const tree = mount( <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } /> );
    expect ( tree.state( 'currentForm' ) ).toBe( 'recovery' );
    window.location.url = null;

} );
it ( 'changes currentForm in state to accountLock when accountLock present in url', () => {

    const props = {
        ...defaultProps,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn()
    };
    window.location.url = 'http://localhost:8001/g6bb8/login.do?accountLock=true';
    const tree = mount( <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } /> );
    expect ( tree.state( 'currentForm' ) ).toBe( 'accountLock' );
    window.location.url = null;

} );
it ( 'changes currentForm in state to forgotPassword when forgotPass present in url', () => {

    const props = {
        ...defaultProps,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn()
    };
    window.location.url = 'http://localhost:8001/g6bb8/login.do?forgotPass=true';
    const tree = mount( <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } /> );
    expect ( tree.state( 'currentForm' ) ).toBe( 'forgotPassword' );
    window.location.url = null;

} );
it ( 'changes currentForm in state to activation when activationPage present in url', () => {

    const props = {
        ...defaultProps,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn()
    };
    window.location.url = 'http://localhost:8001/g6bb8/login.do?activationPage=true';
    const tree = mount( <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } /> );
    expect ( tree.state( 'currentForm' ) ).toBe( 'activation' );
    window.location.url = null;

} );
it ( 'calls fetchDataCountries when recovery present in url', () => {

    const props = {
        ...defaultProps,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn()
    };
    window.location.url = 'http://localhost:8001/g6bb8/login.do?recovery=true';
    mount( <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } /> );
    expect ( props.fetchDataCountries.mock.calls.length ).toBe( 1 );
    window.location.url = null;

} );
it ( 'calls fetchDataCountries when recovery present in url', () => {
    const passwordRules = {
        passwordRules: {}
    };
    const props = {
        ...defaultProps,
        ...passwordRules,
        changeFormFunc: jest.fn(),
        fetchPasswordRules: jest.fn()
    };
    mount( <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } /> );
    expect ( props.fetchPasswordRules.mock.calls.length ).toBe( 1 );

} );
it ( 'sets language in state when setLanguage function called', () => {
    const props = {
        ...defaultProps,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn()
    };

    const tree = mount( <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } /> );
    tree.instance().setLanguage();
    expect( tree.state( 'languageSelected' ) ).toBe( 'en_US' );
} );
it ( 'changes href when changeLanguage function called', () => {
    const props = {
        ...defaultProps,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn()
    };
    Object.defineProperty( window.location, 'href', {
      writable: true,
      value: 'http://localhost:8001/g6bb8/login.do'
    } );
    const tree = mount( <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } /> );
    tree.instance().changeLanguage( 'en_GB' );
    expect( window.location.href ).toBe( 'http://localhost:8001/g6bb8/login.do?cmsLocaleCode=en_GB' );
} );
it ( 'changes href when changeLanguage function called and query param present in url', () => {
    const props = {
        ...defaultProps,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn()
    };
    Object.defineProperty( window.location, 'href', {
      writable: true,
      value: 'http://localhost:8001/g6bb8/login.do?recovery=true'
    } );
    const tree = mount( <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } /> );
    tree.instance().changeLanguage( 'en_GB' );
    expect( window.location.href ).toBe( 'http://localhost:8001/g6bb8/login.do?recovery=true&cmsLocaleCode=en_GB' );
} );
it ( 'changes href when changeLanguage function called when href already has cmsLocaleCode', () => {
    const props = {
        ...defaultProps,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn()
    };
    Object.defineProperty( window.location, 'href', {
      writable: true,
      value: 'http://localhost:8001/g6bb8/login.do?cmsLocaleCode=en_GB'
    } );
    const tree = mount( <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } /> );
    tree.instance().changeLanguage( 'en_US' );
    expect( window.location.href ).toBe( 'http://localhost:8001/g6bb8/login.do?cmsLocaleCode=en_US' );
} );

it ( 'languageParam function returns language when present in url', () => {
    const props = {
        ...defaultProps,
        changeFormFunc: jest.fn(),
        fetchDataCountries: jest.fn()
    };
    const tree = mount( <LoginPage cmStringsArrayToObject={ cmStringsArrayToObject } { ...props } /> );
    const language = tree.instance().languageParam( 'cmsLocaleCode' );
    expect( language ).toBe( 'en_US' );
} );
