import React from 'react';
import classNames from 'classnames';
import RecAdvisorModalHeader from './recAdvisorModalHeader.js';
import RecAdvisorModalBody from './recAdvisorModalBody.js';
import RecAdvisorModalBadgeBody from './recAdvisorModalBadgeBody.js';
import RecAdvisorModalFooter from './recAdvisorModalFooter.js';

class RecAdvisorModal extends React.Component {
		constructor( props ) {
			super( props );

			}

		render() {

			const raEndModal = this.props.recognitionAdvisor.raEndModal;
			const badgeDetailsCount = this.props.recognitionAdvisor.badgeDetailsCount;

			const classes = classNames( {
				'modal': true
			} );

		return (
			<div className="modal-wrapper">
				<div className="modal-backdrop fade in"></div>
					<div id="Modal" className={ classes } >
						<RecAdvisorModalHeader { ...this.state } { ...this.props }/>
							{ raEndModal === true &&
								<RecAdvisorModalBody { ...this.state } { ...this.props }/>
							}
							{  raEndModal === false && badgeDetailsCount >= 1  &&
								<RecAdvisorModalBadgeBody { ...this.state } { ...this.props }/>
							}
								<RecAdvisorModalFooter { ...this.state } { ...this.props }/>
					</div>
				</div>
	);

  }

}

export default RecAdvisorModal;
