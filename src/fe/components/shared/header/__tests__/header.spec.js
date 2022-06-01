import React from 'react';
import Header from '../header.js';
import renderer from 'react-test-renderer';

test( 'Header renders differently based on the "altText" prop', () => {
    const component = renderer.create(
        <div>
            <Header altText="one alt text" />
            <Header altText="another alt text" />
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
