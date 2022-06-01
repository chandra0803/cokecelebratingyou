import React from 'react';

import './spinner.scss';
const Spinner = ( ) => {
	return (
        <div id="pageLoadingSpinner" className="spincover pageLoading" >
            <img src="assets/img/pageLoadingSpinner.gif" alt="Loading..."/>
        </div>
	);
};

export default Spinner;
