import React from 'react';
import PasswordInput from '../password-input.js';
import renderer from 'react-test-renderer';
import { mount } from 'enzyme';

const mockOptions = {
    label: 'Password',
    name: 'password',
    tabIndex: '1',
    passwordVisible: false,
    disabled: false
};

const helpComponent = <a> I forgot my password </a>;

test( 'Password input renders depending on label passed in', () => {
    const component = renderer.create(
        <PasswordInput
            { ...mockOptions }
            onChange={ () => true }
            onKeyPress={ () => true }
            helpComponent= { helpComponent }/>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );

it ( 'executes togglePasswordVisible prop when the user clicks on eye icon ', () => {
    const props = {
        ...mockOptions
    };

    const tree = mount( <PasswordInput { ...props }/> );

    tree.setState( { passwordVisible: true } );

    const input = tree.find( '.eye' );

    input.simulate( 'click' );

    expect( tree.state().passwordVisible ).toBe( false );
} );
