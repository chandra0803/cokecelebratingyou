/*global jest*/

jest.mock( 'react-redux' );
import React from 'react';
import RecAdvisorDetail from '../recAdvisorDetail.js';
import renderer from 'react-test-renderer';


window.recognitionAdvisor = {
  raUrl: 'ra/reminders.action',
  raTilePageDisplay: 'yes',
  raDetailPageDisplay: 'no',
  raEndModelPageDisplay: 'no',
  content: []
};

const cmStringsArrayToObject = ( propertiesArray ) => {
    const propertiesObject = {};
    if( propertiesArray ) {

        propertiesArray.forEach( property => {
            propertiesObject[ `${ property.code }.${ property.key.toLowerCase() }` ] = property.content;
        } );
    }
    return propertiesObject;
};

const defaultProps = {
    recognitionAdvisor: {
      raUrl: 'ra/reminders.action',
      raTilePageDisplay: 'yes',
      raDetailPageDisplay: 'no',
      raEndModelPageDisplay: 'no',
      content: []
    }
};

const raDetailModule = {
  'rowNumStart': 1,
  'rowNumEnd': 50,
  'activePage': 0,
  'sortColName': 'NO_VALUE',
  'sortedBy': '',
  'excludeUpcoming': 0,
  'filterValue': 0,
  'pendingStatus': 0
};

it( 'renders the dom like the snapshot when the props have default values.', () => {

  const props = {
      ...defaultProps,
      recognitionAdvisorFunc: jest.fn(),
      raEligibleProgramsFunc: jest.fn()
  };
  const state = {
    attributeData: raDetailModule,
    raTilePage: true,
    raDetailPage: false,
    raEndModelPage: false,
    content: cmStringsArrayToObject( window.recognitionAdvisor.content )
  };
  const component = renderer.create(
    <div>
      <RecAdvisorDetail { ...state } { ...props } />
    </div>
  );

  const tree = component.toJSON();
  expect( tree ).toMatchSnapshot();
} );
