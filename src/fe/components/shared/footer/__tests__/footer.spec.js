/*global jest*/

jest.mock( 'react-redux' );
import React from 'react';
import Footer from '../footer.js';
import renderer from 'react-test-renderer';
import { mount } from 'enzyme';
const defaultProps = {
    content: [
    {
        key: 'COPYRIGHT_TEXT',
        code: 'system.general',
        content: 'copyright'
    },
    {
        key: 'CONTACT_US',
        code: 'system.general',
        content: 'contact'
    },
    {
        key: 'T&C',
        code: 'system.general',
        content: 'T&C'
    },
    {
        key: 'PRIVACY_POLICY',
        code: 'system.general',
        content: 'privacy'
    }
],
showTerms: true
};
test( 'Footer renders properly', () => {
    const props = {
        ...defaultProps
    };
    const component = renderer.create(
        <div>
            <Footer { ...props }/>
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
it ( 'toggles help modal when help link is clicked.', () => {
    const props = {
        ...defaultProps,
        toggleModalDisplay: jest.fn()
    };

    const tree = mount( <Footer { ...props } /> );

    const link = tree.find( '#privacyPolicyFooterLink' );

    link.simulate( 'click' );

    expect ( props.toggleModalDisplay.mock.calls.length ).toBe( 1 );
} );
