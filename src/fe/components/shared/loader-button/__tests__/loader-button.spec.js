import React from 'react';
import LoaderButton from '../loader-button';
import renderer from 'react-test-renderer';

// Matrix of props that affect component rendering
const mockOptions = [
    { busy: false, disabled: true,  done: false, fetching: false },
    { busy: false, disabled: false, done: false, fetching: false },
    { busy: true,  disabled: false, done: false, fetching: false },
    { busy: false, disabled: false, done: true, fetching: false  },
    { busy: false, disabled: true,  done: false, fetching: true },
    { busy: false, disabled: false, done: false, fetching: true },
    { busy: true,  disabled: false, done: false, fetching: true },
    { busy: false, disabled: false, done: true, fetching: true  },
];

test( 'Loader button renders different icons depending on props passed in', () => {
    const component = renderer.create(
        <div>
            { mockOptions.map( opt => (
                <div>
                    <LoaderButton
                        busy={ opt.busy }
                        onClick={ () => true }
                        handleClick={ ()=>true }
                        disabled={ opt.disabled }
                        fetching={ opt.fetching }
                        done={ opt.done }>
                        Log in
                    </LoaderButton>
                </div>
            ) ) }
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
