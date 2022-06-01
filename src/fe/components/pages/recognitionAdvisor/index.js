import 'babel-polyfill';
import 'whatwg-fetch';

import { connect } from 'react-redux';
import React from 'react';
import { render } from 'react-dom';
import RecognitionAdvisor from './recognitionAdvisor.js';
import { recognitionAdvisorFunc, raEligibleProgramsFunc, cmStringsArrayToObject, removeComponent } from './dux/recognitionAdvisor.js';

import { Provider } from 'react-redux';
import createStore from './create-store.js';

const RenderAdvisor = connect(

  // mapStateToProps
  state => {
    return {
      ...state,
	  showSpinner: state.recognitionAdvisor.showSpinner
    };
  },
  // mapDispatchToProps
  {
    recognitionAdvisorFunc,
	raEligibleProgramsFunc,
    cmStringsArrayToObject,
    removeComponent

  }
)( RecognitionAdvisor );

export const renderAdvisor = () => {
    render(
        <Provider store={ createStore() }>
          <RenderAdvisor />
        </Provider>,
        document.getElementById( 'rec-advisor' )
    );
};

window.renderAdvisor = renderAdvisor;
