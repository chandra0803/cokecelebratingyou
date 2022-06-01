import React from 'react';
import { IconSetPreview } from '../icons';
import renderer from 'react-test-renderer';

it( 'renders the dom like the snapshot when the props have default values.', () => {
    const component = renderer.create( <IconSetPreview /> );
    const tree = component.toJSON();

    expect( tree ).toMatchSnapshot();
} );
