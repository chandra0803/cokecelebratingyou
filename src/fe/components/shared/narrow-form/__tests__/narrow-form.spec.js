import React from 'react';
import NarrowForm from '../narrow-form';
import renderer from 'react-test-renderer';

test( 'Form renders', () => {
    const props = {
        login: {
            toggleModal: true,
        },
        content: {
            'system.general.help_text': '???system.general.HELP_TEXT???',
            'system.general.help_title': '???system.general.HELP_TITLE???'
        },
        toggleModalDisplay: function( toggleModal ) {
            return !toggleModal;
        }
    };
    const component = renderer.create(
        <div>
            <NarrowForm { ...props }/>
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
