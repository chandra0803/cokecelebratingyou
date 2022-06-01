import React from 'react';
import Spinner from '../spinner.js';
import renderer from 'react-test-renderer';

test( 'Spinner renders', () => {
    const component = renderer.create(
        <div>
            <Spinner />
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
