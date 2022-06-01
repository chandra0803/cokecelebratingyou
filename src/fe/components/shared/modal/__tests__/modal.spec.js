/*global jest*/

jest.mock( 'react-redux' );
// jest.mock( 'react-router-dom' );
// jest.mock( '../../../shared/locale-string' );
import { mount } from 'enzyme';

import React from 'react';
import renderer from 'react-test-renderer';
import Modal from '../modal.js';
import LoaderButton from '../../loader-button/loader-button';
const defaultProps = {
    login: {
        toggleModal: true,
    },
    content: {
        'system.general.help_text': '???system.general.HELP_TEXT???',
        'system.general.help_title': '???system.general.HELP_TITLE???'
    }
};
it( 'renders the dom like the snapshot when the props have default values.', () => {
    const props = {
        ...defaultProps,
        login: {
            modalPage: 'help',
        },
        toggleModalDisplay: jest.fn()
    };
    const component = renderer.create(
        <div>
            <Modal { ...props }/>
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
it( 'renders the dom like the snapshot when the props have default values.', () => {
    const props = {
        ...defaultProps,
        login: {
            modalPage: 'privacy',
        },
        toggleModalDisplay: jest.fn()
    };
    const component = renderer.create(
        <div>
            <Modal { ...props }/>
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
it( 'renders the dom like the snapshot when the props have default values.', () => {
    const props = {
        ...defaultProps,
        login: {
            modalPage: 'tnc',
        },
        toggleModalDisplay: jest.fn()
    };
    const component = renderer.create(
        <div>
            <Modal { ...props }/>
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
it( 'renders the dom like the snapshot when the props have default values.', () => {
    const props = {
        ...defaultProps,
        login: {
            modalPage: 'register',
        },
        toggleModalDisplay: jest.fn()
    };
    const component = renderer.create(
        <div>
            <Modal { ...props }/>
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
it ( 'executes modal prop when the close X is clicked.', () => {
    const props = {
        ...defaultProps,
        toggleModalDisplay: jest.fn()
    };

    const tree = mount( <Modal { ...props } /> );

    const link3 = tree.find( '.close' );

    link3.simulate( 'click' );

    expect ( props.toggleModalDisplay.mock.calls.length ).toBe( 1 );
} );
it ( 'executes modal prop when the close button is clicked.', () => {
    const props = {
        ...defaultProps,
        toggleModalDisplay: jest.fn()
    };

    const tree = mount( <Modal { ...props } /> );

    const link3 = tree.find( '.btn' );

    link3.simulate( 'click' );

    expect ( props.toggleModalDisplay.mock.calls.length ).toBe( 1 );
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
        ...login
    };

    const tree = mount( <Modal { ...props } /> );

    const alerts = tree.find( '.alert-success' );

    expect ( alerts.length ).toBe( 1 );
} );
it ( 'changes token in state when the user changes something in the token input ', () => {
    const props = {
        ...defaultProps,
        login: {
            modalPage: 'register'
        }
    };

    const tree = mount( <Modal { ...props } /> );

    const input = tree.find( 'input[name="token"]' );

    input.simulate( 'change', { target: { name: input.props().name, value: 'test' } } );

    expect ( tree.state( 'token' ) ).toBe( 'test' );
} );
it ( 'does not execute sendToken prop when enter is pressed with empty inputs.', () => {
    const props = {
        ...defaultProps,
        sendToken: jest.fn(),
        login: {
            modalPage: 'register'
        }
    };

    const tree = mount( <Modal { ...props } /> );

    const input = tree.find( 'input[name="token"]' );

    tree.setState( { token: '' } );

    input.simulate( 'keyPress', { charCode: 13 } );

    expect ( props.sendToken.mock.calls.length ).toBe( 0 );
} );
it ( 'does not execute sendToken prop when key other than enter is pressed in input', () => {
    const props = {
        ...defaultProps,
        sendToken: jest.fn(),
        login: {
            modalPage: 'register'
        }
    };

    const tree = mount( <Modal { ...props } /> );

    const input = tree.find( 'input[name="token"]' );

    tree.setState( { token: '' } );

    input.simulate( 'keyPress', { charCode: 12 } );

    expect ( props.sendToken.mock.calls.length ).toBe( 0 );
} );
it ( 'executes sendToken prop when enter is pressed with filled input.', () => {
    const props = {
        ...defaultProps,
        sendToken: jest.fn(),
        login: {
            modalPage: 'register'
        }
    };

    const tree = mount( <Modal { ...props } /> );

    const input = tree.find( 'input[name="token"]' );

    tree.setState( { token: 'test' } );

    input.simulate( 'keyPress', { charCode: 13 } );

    expect ( props.sendToken.mock.calls.length ).toBe( 1 );
} );
it ( 'enables the button when token is entered.', () => {
    const props = {
        ...defaultProps,
        sendToken: jest.fn(),
        login: {
            modalPage: 'register'
        }
    };

    const tree = mount( <Modal { ...props } /> );

    const button = tree.find( LoaderButton );

    expect ( button.props().disabled ).toBe( true );

    const input = tree.find( 'input[name="token"]' );

    input.simulate( 'change', { target: { name: input.props().name, value: '!Q@W3e4r' } } );

    expect ( button.props().disabled ).toBe( false );

} );
