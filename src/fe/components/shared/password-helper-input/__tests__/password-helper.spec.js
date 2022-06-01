import React from 'react';
import PasswordHelper from '../password-helper.js';
import renderer from 'react-test-renderer';
import { mount } from 'enzyme';

const mockOptions = {
    name: 'newPassword',
    label: 'New Password',
    value: 'Testing123!',
    disabled: false,
    tabIndex: '1',
    onChange: () => true,
    passwordRules: {
        maxLength: 16,
        minLength: 8,
        isLowerCaseRequired: true,
        isUpperCaseRequired: true,
        isNumberRequired: true,
        isSpecialCharRequired: true,
        hasValidLength: true
    },
    passwordLabels: {
        length: '',
        isLowerCaseRequired: '',
        isUpperCaseRequired: '',
        isNumberRequired: '',
        isSpecialCharRequired: ''
    }
};
const mockOptions2 = {
    name: 'newPassword',
    label: 'New Password',
    value: 'Testing123!',
    disabled: false,
    tabIndex: '1',
    onChange: () => true,
    passwordRules: {
        isLowerCaseRequired: false,
        isUpperCaseRequired: false,
        isSpecialCharRequired: false,
        hasValidLength: false
    },
    passwordLabels: {
        length: '',
        isLowerCaseRequired: '',
        isUpperCaseRequired: '',
        isNumberRequired: '',
        isSpecialCharRequired: ''
    }
};
test( 'Password Helper renders different rules depending on props passed in', () => {
    const component = renderer.create(
        <PasswordHelper label="New Password"
                        name= "newPassword"
                        value= { mockOptions.value }
                        tabIndex="2"
                        onChange={ () => true }
                        passwordRules={ mockOptions.passwordRules }
                        passwordLabels={ mockOptions.passwordLabels } />
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
test( 'Password Helper renders different rules depending on props passed in', () => {
    const component = renderer.create(
        <PasswordHelper label="New Password"
                        name= "newPassword"
                        value= { mockOptions2.value }
                        tabIndex="2"
                        onChange={ () => true }
                        passwordRules={ mockOptions2.passwordRules }
                        passwordLabels={ mockOptions2.passwordLabels } />
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
it ( 'changes focused value in state when password input is focused  ', () => {
    const props = {
        ...mockOptions
    };

    const tree = mount( <PasswordHelper { ...props } /> );

    const input = tree.find( 'input[name="newPassword"]' );

    expect ( tree.state( 'focused' ) ).toBe( false );

    input.simulate( 'focus', { target: { name: input.props().name } } );

    tree.setState( { focused: true } );

    expect ( tree.state( 'focused' ) ).toBe( true );
} );

it ( 'changes focused value in state when password input is blurred ', () => {
    const props = {
        ...mockOptions
    };

    const tree = mount( <PasswordHelper { ...props } /> );

    const input = tree.find( 'input[name="newPassword"]' );

    expect ( tree.state( 'focused' ) ).toBe( false );

    input.simulate( 'blur', { target: { name: input.props().name } } );

    tree.setState( { focused: true } );

    expect ( tree.state( 'focused' ) ).toBe( true );
} );
it( 'executes lengthTest and returns true or false, depending on data passed in', ()=>{
    const props = {
        ...mockOptions
    };
    const tree = mount( <PasswordHelper { ...props } /> );

    const invalid = tree.instance().lengthTest( 'password', 10 );

    expect( invalid ).toBe( false );

    const valid = tree.instance().lengthTest( 'password', 6 );

    expect( valid ).toBe( true );
} );
