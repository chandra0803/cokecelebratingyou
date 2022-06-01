/*global jest*/

jest.mock( 'react-redux' );
import React from 'react';
import MyteamTable from '../myteamTable.js';
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
        'recognition.content.model.ra_days': 'days',
        'recognition.content.model.info.ra_day': 'day',
        'recognition.content.model.info.ra_today': 'Today',
        'recognition.content.model.info.ra_pending': 'pending',
        'recognition.content.model.info.ra_pending_message': 'pending message',
        'recognition.content.model.info.ra_status': 'status',
        'recognition.content.model.info.ra_point': 'point',
        'recognition.content.model.info.ra_points': 'points'
      }
    }
};

it( 'renders the dom like the snapshot when the props have default values.', () => {
    const content = {
      content: {
        'recognition.content.model.ra_days': 'days',
        'recognition.content.model.info.ra_day': 'day',
        'recognition.content.model.info.ra_today': 'Today',
        'recognition.content.model.info.ra_pending': 'pending',
        'recognition.content.model.info.ra_pending_message': 'pending message',
        'recognition.content.model.info.ra_status': 'status',
        'recognition.content.model.info.ra_point': 'point',
        'recognition.content.model.info.ra_points': 'points'
      }
    };
    const props = {
        ...defaultProps,
        ...content,
        recognitionAdvisorFunc: jest.fn(),
        raEligibleProgramsFunc: jest.fn()
    };
    const component = renderer.create(
        <div>
            <MyteamTable { ...props } />
        </div>
    );

    const tree = component.toJSON();
    expect( tree ).toMatchSnapshot();
} );
