import React from 'react';
import CardUser from './cardUser';
import ItemList from './itemList';

class RecAdvisorTile extends React.Component {
	constructor( props ) {
      super( props );
   }
	shouldComponentUpdate(	nextProps, nextState	) {
		if (	nextProps.hasFetched	)	{
			return false;
		} else {
			return true;
		}
	}
   render() {
		const {
			content
		} = this.props;

		let userProfileData = [];
		let overDueProfileData = [];
		const baseURI = window.recognitionAdvisor.baseURI;
		const raReminderParticipants = this.props.recognitionAdvisor.raReminderParticipants;
		let cardUser, groupType;
		if( raReminderParticipants ) {
			if( raReminderParticipants.recentlyAddedParticipants !== undefined ) {
				userProfileData = raReminderParticipants.recentlyAddedParticipants;
			}
			if( raReminderParticipants.overDueParticipants !== undefined ) {
				overDueProfileData = raReminderParticipants.overDueParticipants;
			}
		}
		const boxes = [];
		if( userProfileData.length ) {
			const userProfileDataClone = userProfileData.slice( 0 );
			cardUser = userProfileDataClone.splice( 0, 1 );
			userProfileDataClone.forEach( ( item, i ) => {
				if( i >= 0 && i <= 6 ) {
					let toggleClass;
					if ( i <= 2 ) {
					toggleClass =  i % 2 ? 'even' : 'odd';
					} else {
					toggleClass =  i % 2 ? 'odd' : 'even';
					}
					boxes.push( <li key = { i } className={ toggleClass }>
						<div className="newEmployee">
							<ItemList item={ item } />
						</div>
					</li> );
				}

		} );
		boxes.forEach( ( box, i )=>{
			if( i === 0 ) {
				boxes.splice( 0, 0, <h4 key={ i + 10 } > <span className="icon-star"></span> {content[ 'recognition.content.model.info.ra_new' ]} </h4> );
			}
			if( i === 3 ) {
				boxes.splice( 4, 0, <h4 key={ i + 10 } className="headerTextSpace"> { boxes.length < 4 &&
					<span className="icon-star"></span>} {content[ 'recognition.content.model.info.ra_new' ]}</h4> );
			}

			} );
		}
		const firstColumn = boxes.splice( 0, 4 );
		const secondColumn = boxes.splice( 0, 4 );

		const odboxes = [];
		if( overDueProfileData.length ) {
			const overDueProfileDataClone = overDueProfileData.slice( 0 );
			if( !userProfileData.length ) {
				cardUser = overDueProfileDataClone.splice( 0, 1 );
			}

			overDueProfileDataClone.forEach( ( item, i ) => {
				if( i >= 0 && i <= 6 ) {
					let toggleClass;
					if ( i <= 2 ) {
					toggleClass =  i % 2 ? 'even' : 'odd';
					} else {
					toggleClass =  i % 2 ? 'odd' : 'even';
					}
					odboxes.push( <li key = { i }  className={ toggleClass }>
						<div className="newEmployee">
							<ItemList item={ item } />
						</div>
					</li> );
				}

			} );
			odboxes.forEach( ( box, i )=>{
				if( i === 0 ) {
					odboxes.splice( 0, 0, <h4 key={ i + 10 }><span className="icon-clock"></span> {content[ 'recognition.content.model.info.ra_overdue' ]}</h4> );
				}
				if( i === 3 ) {
					odboxes.splice( 4, 0, <h4 key={ i + 10 } className="headerTextSpace">{ odboxes.length < 4 &&
						<span className="icon-clock"></span>}{content[ 'recognition.content.model.info.ra_overdue' ]}</h4> );
				}

			} );
		}
		const firstColumnOD = odboxes.splice( 0, 4 );
		const secondColumnOD = odboxes.splice( 0, 4 );
		let upcomingProfileData = [];


		if( raReminderParticipants ) {
			if( raReminderParticipants.upComingParticipants !== undefined ) {
				upcomingProfileData = raReminderParticipants.upComingParticipants;
			}
		}
		const upboxes = [];
		if( upcomingProfileData.length ) {
			if( !userProfileData.length && !overDueProfileData.length ) {
				groupType = 3;
			}else {
				groupType = 0;
			}
			upcomingProfileData.forEach( ( item, i ) => {
				if( i <= 5 ) {
					let toggleClass;
					if ( i <= 2 ) {
					toggleClass =  i % 2 ? 'even' : 'odd';
					} else {
					toggleClass =  i % 2 ? 'odd' : 'even';
					}
					upboxes.push( <li key = { i }  className={ toggleClass }>
						<div className="newEmployee">
							<ItemList item={ item } />
						</div>
					</li> );
				}

			} );
			upboxes.forEach( ( box, i )=>{
				if( i === 0 ) {
					upboxes.splice( 0, 0, <h4 key={ i + 10 }><span className="icon-more-circles-horizontal"></span> {content[ 'recognition.content.model.info.ra_upcoming' ]}</h4> );
				}
				if( i === 3 ) {
					upboxes.splice( 4, 0, <h4 key={ i + 10 } className="headerTextSpace"> <span class="icon-more-circles-horizontal"></span>{content[ 'recognition.content.model.info.ra_upcoming' ]}</h4> );
				}

			} );
		}
		const firstColumnUp = upboxes.splice( 0, 4 );
		const secondColumnUp = upboxes;
		return (
				
				<div>
					{!this.props.raDetailPage ?
						<h3 className="module-title">{content[ 'recognition.content.model.info.ra_recognition_advisor' ]}</h3> :
						<h2 className="module-title">{content[ 'recognition.content.model.info.ra_recognition_advisor' ]}</h2>
						}
						<div className="ra-wrapper">
							{ ( cardUser !== undefined ||  groupType === 3 ) &&
								<CardUser cardUser={ cardUser } groupType={ groupType } { ...this.props } />
							}
							<div className="clearfix list-container col-count-2">
									{firstColumn.length > 1 &&
										<ol className="recAdvisor-col recAdvisor-col-1">
										{firstColumn}
										</ol>}

									{secondColumn.length >= 1 &&
									<ol className="recAdvisor-col recAdvisor-col-2">
										{secondColumn}
									</ol>}

									{firstColumnOD.length > 1 &&
										<ol className="recAdvisor-col recAdvisor-col-1">
											{firstColumnOD}
										</ol>}

									{secondColumnOD.length >= 1 &&
										<ol className="recAdvisor-col recAdvisor-col-2">
											{secondColumnOD}
										</ol>
									}

									{firstColumnUp.length > 1 &&
										<ol className="recAdvisor-col recAdvisor-col-1">
											{firstColumnUp}
										</ol>
									}

									{secondColumnUp.length >= 1 &&
										<ol className="recAdvisor-col recAdvisor-col-2">
											{secondColumnUp}
										</ol>
									}

							</div>

						</div>
						{!this.props.raDetailPage &&
							<div className="module-actions">
								<a href={ `${ baseURI }/raDetails.do` }>{content[ 'recognition.content.model.info.ra_details' ]}</a>
							</div>
						}
			</div>
		);
	}
}

export default RecAdvisorTile;
