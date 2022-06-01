/*global jest*/

jest.mock( 'react-redux' );
import React from 'react';
import ItemList from '../itemList.js';
import renderer from 'react-test-renderer';

window.recognitionAdvisor = {
  raUrl: 'ra/reminders.action',
  raTilePageDisplay: 'yes',
  raDetailPageDisplay: 'no',
  raEndModelPageDisplay: 'no',
  content: []
};

it( 'renders the dom like the snapshot when the props have default values.', () => {
  const item = {
    'id': 92085,
    'firstName': 'Erica',
    'lastName': 'Jalecia Edwards',
    'participantGroupType': 2,
    'avatarUrl': null,
    'positionType': 'Director',
    'otherMgrApprovedDate': null,
    'currentMgrApprovedDate': '01/08/2018',
    'otherMgrLastRecogDays': null,
    'currentMgrLastRecogDays': 225,
    'otherMgrAwardPoints': null,
    'currentMgrAwardPoints': 10,
    'claimIdByCurrentMgr': '10354',
    'claimIdByOtherMgr': null,
    'noOfDaysPastDueDate': null,
    'participantRecDaysColour': 'red',
    'participantRecDaysColourForOthers': null,
    'claimUrlByCurrentMgr': 'https://g5betaqa.performnet.com/g5beta/login.do',
    'claimUrlByOtherMgr': null,
    'countryId': 6037,
    'nodeId': [ '7202' ],
    'recApprovalStatusType': null
  };

  const component = renderer.create(
    <div>
      <ItemList item={ item } />
    </div>
  );

  const tree = component.toJSON();
  expect( tree ).toMatchSnapshot();
} );
