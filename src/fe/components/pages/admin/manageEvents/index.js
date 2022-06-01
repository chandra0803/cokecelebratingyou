import 'babel-polyfill';
import 'whatwg-fetch';

import { connect } from 'react-redux';
import React from 'react';
import { render } from 'react-dom';

import { Provider } from 'react-redux';
import createStore from './create-store.js';
import EventManageAdmin from './eventManageAdmin.js';
import { getEventListenerStatus, getEventAttributes, getEventList, setEventFilter, startEventListener } from './dux/eventManageAdmin.js'

const RenderEventManageAdmin = connect(

  // mapStateToProps
  state => {
    return {
      ...state
    };
  },
  // mapDispatchToProps
  {
    getEventListenerStatus,
    getEventAttributes,
    getEventList,
    setEventFilter,
    startEventListener
  }
)( EventManageAdmin );

export const renderEventManageAdmin = () => {
    render(
        <Provider store={ createStore() }>
          <RenderEventManageAdmin />
        </Provider>,
        document.getElementById( 'event-manage-admin' )
    );
};

window.renderEventManageAdmin = renderEventManageAdmin;