/*global jest*/

jest.mock( 'react-redux' );
import React from 'react';
import RecAdvisorModalHeader from '../recAdvisorModalHeader.js';
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

const raRecEndModule = {
  'rowNumStart': 1,
  'rowNumEnd': 2,
  'activePage': 0,
  'sortColName': 'NO_VALUE',
  'sortedBy': '',
  'excludeUpcoming': 1,
  'filterValue': '',
  'pendingStatus': 1
};

it( 'renders the dom like the snapshot when the props have default values.', () => {

  const props = {
      ...defaultProps,
      recognitionAdvisorFunc: jest.fn(),
      raEligibleProgramsFunc: jest.fn()
  };
  const state = {
    attributeData: raRecEndModule,
    raTilePage: false,
    raDetailPage: false,
    raEndModelPage: true,
    raClaimDetail: 'RA Claim Detail',
    content: cmStringsArrayToObject( window.recognitionAdvisor.content )
  };
  const component = renderer.create(
    <div>
      <RecAdvisorModalHeader { ...state } { ...props } />
    </div>
  );

  const tree = component.toJSON();
  expect( tree ).toMatchSnapshot();
} );
