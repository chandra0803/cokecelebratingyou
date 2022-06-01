/*global jest*/

jest.mock( 'react-redux' );
import React from 'react';
import RecognitionInsights from '../recognitionInsights.js';
import renderer from 'react-test-renderer';

window.recognitionAdvisor = {
  raUrl: 'ra/reminders.action',
  raTilePageDisplay: 'yes',
  raDetailPageDisplay: 'no',
  raEndModelPageDisplay: 'no',
  content: []
};

it( 'renders the dom like the snapshot when the props have default values.', () => {
    const content = {
      content: {
        'recognition.content.model.ra_insights': 'insights',
        'recognition.content.model.info.ra_emp_better_perform_content': 'Emp better perform content'
      }
    };

    const component = renderer.create(
        <div>
            <RecognitionInsights content={ content } />
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
