/*global jest*/

jest.mock( 'react-redux' );
import React from 'react';
import CardUser from '../cardUser.js';
import renderer from 'react-test-renderer';

window.recognitionAdvisor = {
  raUrl: 'ra/reminders.action',
  raTilePageDisplay: 'yes',
  raDetailPageDisplay: 'no',
  raEndModelPageDisplay: 'no',
  content: []
};

const defaultProps = {
    recognitionAdvisor: {
      raUrl: 'ra/reminders.action',
      raTilePageDisplay: 'yes',
      raDetailPageDisplay: 'no',
      raEndModelPageDisplay: 'no',
      content: {
        'recognition.content.model.info': 'Recognition Advisor',
        'recognition.content.model.info.ra_days_over_due': 'days overdue!',
        'recognition.content.model.info.ra_new_employee': 'New',
        'recognition.content.model.info.ra_recognize': 'Recognize',
        'recognition.content.model.info.ra_caught': 'You all caught up!'
      }
    }
};

const cardUser = [ {
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
} ];

it( 'renders the dom like the snapshot when the props have default values.', () => {
    const content = {
      content: {
        'recognition.content.model.info': 'Recognition Advisor',
        'recognition.content.model.info.ra_days_over_due': 'days overdue!',
        'recognition.content.model.info.ra_new_employee': 'New',
        'recognition.content.model.info.ra_recognize': 'Recognize',
        'recognition.content.model.info.ra_caught': 'You all caught up!'
      }
    };
    const props = {
        ...defaultProps,
        ...content,
        recognitionAdvisorFunc: jest.fn(),
        raEligibleProgramsFunc: jest.fn()
    };

    const groupType = 0;

    const component = renderer.create(
        <div>
            <CardUser cardUser={ cardUser } groupType={ groupType } { ...props } />
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
