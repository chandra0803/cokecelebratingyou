import React from 'react';
import classNames from 'classnames';

class MyteamTable extends React.Component {
	constructor( props ) {
		super( props );
		this.toggleClass = this.toggleClass.bind( this );
		this.state = {
		  activeIndex: 0
		};
	}
	toggleClass( index, e ) {
		this.setState( { activeIndex: index } );
	}
	handleRecognize( e ) {
		const origin = window.location.origin;
		const loc = window.location.pathname;
		const context = loc.split( '/' )[ 1 ];
		const paxId = e.target.getAttribute( 'data-id' );
		const url = `${origin}/${context}/ra/${paxId}/sendRecognition.action`;
		window.location.href = url;
	}
	handleSort(	e ) {
			e.preventDefault();
			const sortedBy =	e.target.getAttribute(	'data-sortby'	);
			const sortColName =	e.target.getAttribute(	'data-sort'	);
			this.props.handleSort(	sortedBy,	sortColName	);
			if ( sortedBy === 'asc' ) {
          e.target.setAttribute( 'data-sortBy', 'desc' );
					e.target.nextElementSibling.classList.remove(	'icon-arrow-1-up'	);
					e.target.nextElementSibling.classList.add( 'icon-arrow-1-down' );
       } else {
          e.target.setAttribute( 'data-sortBy', 'asc' );
					e.target.nextElementSibling.classList.remove(	'icon-arrow-1-down'	);
					e.target.nextElementSibling.classList.add( 'icon-arrow-1-up'	);
       }
	}

	render() {

		const {
      content
    } = this.props;

		const inverseClasses = classNames( {
				'label-inverse': true,
				'label-warning': false,
				'label-important': false
		} );

		const warningClasses = classNames( {
				'label-inverse': false,
				'label-warning': true,
				'label-important': false
		} );

		const importantClasses = classNames( {
				'label-inverse': false,
				'label-warning': false,
				'label-important': true
		} );
		
		const raReminderParticipants = this.props.recognitionAdvisor.raReminderParticipants;
		let employees, employeesList ;

		if( raReminderParticipants ) {
			employees = raReminderParticipants.sortingAndPaginationParticipants;
		}

		if ( employees && content ) {
			employeesList = employees.map( ( emp ) =>
				<tr key={ emp.id }>
					<td className="status-col">
					{ emp.participantGroupType === 1 ? ( <span className="statusIcon icon-star"></span> )
					: emp.participantGroupType === 2 ? ( <span className="statusIcon icon-clock"></span> )
					: ( <span> </span> )
					}
					{ emp.currentMgrLastRecogDays !== null && ( <span>
								{  emp.currentMgrLastRecogDays > 0 &&
									<span className = { `label ${emp.participantRecDaysColour === 'gray' &&  inverseClasses }
										${emp.participantRecDaysColour === 'orange' &&  warningClasses }
										${emp.participantRecDaysColour === 'red' &&  importantClasses }` }>
										<span>{ emp.currentMgrLastRecogDays } { emp.currentMgrLastRecogDays > 1 ?
											( <span>{ content[ 'recognition.content.model.info.ra_days' ] }</span> )
										: ( <span> { content[ 'recognition.content.model.info.ra_day' ] } </span> )  }</span></span>
								}
								{  emp.currentMgrLastRecogDays === 0 &&
									<span className = { `label ${inverseClasses}` }>{ content[ 'recognition.content.model.info.ra_today' ] }</span>
								}
						</span>
					)}
					</td>
					<td>
						<span className="avatarContainer">
							<span className="avatar-wrapper">
								{ emp.avatarUrl ? (
										<img src={ G5.util.generateTimeStamp( emp.avatarUrl ) } />
									) :
									(
										<span className="avatar-initials">{emp.firstName.charAt( 0 )} {emp.lastName.charAt( 0 )}</span>
									)
								}

							</span>
						</span>
						<span className="empName">
							<a className="itemName prfile-popover" data-participant-ids={ `[ ${emp.id} ]` }>{emp.firstName} {emp.lastName} </a>
						</span>
						<span className="emp-title">{emp.positionType}</span>
					</td>
					<td>{ emp.claimIdByCurrentMgr !== null && ( <span>
								{ emp.recApprovalStatusType == 'pend' && ( <span className="label label-success pending">{ content[ 'recognition.content.model.info.ra_pending' ] } <i className="icon-info"></i>
												<span className="instructions">{ content[ 'recognition.content.model.info.ra_pending_message' ] }</span>
									</span> ) }

									{ emp.recApprovalStatusType == 'pend' && (
										<span className="empNumberDays">{ emp.currentMgrApprovedDate }</span>
									) }

									{ emp.recApprovalStatusType == null && (
										<a href={ emp.claimUrlByCurrentMgr } className="regDetailView" target="_sheet" data-url-name="Recognition Detail">
											<span className="empNumberDays">{ emp.currentMgrApprovedDate }</span>
											</a> ) }

									</span>  )}

								{ emp.claimIdByCurrentMgr !== null  &&
									( emp.currentMgrAwardPoints !== null ? ( <b>{emp.currentMgrAwardPoints} { emp.currentMgrAwardPoints > 1 ? ( <span>{ content[ 'recognition.content.model.info.ra_points' ] }</span> )
									: ( <span> { content[ 'recognition.content.model.info.ra_point' ] } </span> )  }</b> ) : ( <b></b> ) )
								}
					</td>
					<td>{ emp.otherMgrApprovedDate !== null && ( <span> <a href={ emp.claimUrlByOtherMgr } className="regDetailView" target="_sheet" data-url-name="Recognition Detail">
									<span>{ emp.otherMgrApprovedDate}</span> </a>
								</span> )}

								{ emp.claimIdByOtherMgr !== null &&
									( emp.otherMgrAwardPoints !== null ? ( <b>{emp.otherMgrAwardPoints} { emp.otherMgrAwardPoints > 1 ? ( <span>{ content[ 'recognition.content.model.info.ra_points' ] }</span> )
									: ( <span> { content[ 'recognition.content.model.info.ra_point' ] } </span> )  }</b> ) : ( <b></b> ) )
								}
					</td>
					<td className="reallocationAmount">
					{ emp.participantGroupType === 3 ? ( <button type="button" className="btnRecognize btn btn-primary flipButton btn-inverse" data-id={ emp.id } onClick={ ( e ) => this.handleRecognize( e ) }>{content[ 'recognition.content.model.info.ra_recognize' ]}</button> )
					: ( <button type="button" className="btnRecognize btn btn-primary flipButton" data-id={ emp.id } onClick={ ( e ) => this.handleRecognize( e ) }>{content[ 'recognition.content.model.info.ra_recognize' ]}</button> )
					}
					</td>
				</tr>
			);
		}

		return (
			<div className="myteamTable">
				<table className="table table-striped">
					<thead>
						<tr>
							<th className={ `${this.state.activeIndex == 0 ? 'active' : null }` } onClick={ this.toggleClass.bind( this, 0 ) } >
								<a href="#" data-sort="is_new_hire" data-sortBy="desc" onClick={ ( e ) => this.handleSort( e ) } >{ content[ 'recognition.content.model.info.ra_status' ] } </a> <i className="icon-arrow-1-down"></i>
							</th>
							<th className={ `${this.state.activeIndex == 1 ? 'active' : null }` } onClick={ this.toggleClass.bind( this, 1 ) } >
								<a href="#" data-sort="first_name" data-sortBy="asc" onClick={ ( e ) => this.handleSort( e ) }>{ content[ 'recognition.content.model.info.ra_employees' ] }</a> <i className="icon-arrow-1-up"></i>
							</th>
							<th className={ `${this.state.activeIndex == 2 ? 'active' : null }` } onClick={ this.toggleClass.bind( this, 2 ) } >
								<a href="#" data-sort="by_me_date_sent" data-sortBy="asc" onClick={ ( e ) => this.handleSort( e ) }>{ content[ 'recognition.content.model.info.ra_by_me' ] }</a> <i className="icon-arrow-1-up"></i>
							</th>
							<th className={ `${this.state.activeIndex == 3 ? 'active' : null }` } onClick={ this.toggleClass.bind( this, 3 ) }>
								<a href="#" data-sort="by_oth_date_sent" data-sortBy="asc" onClick={ ( e ) => this.handleSort( e ) }>{ content[ 'recognition.content.model.info.ra_by_others' ] }</a> <i className="icon-arrow-1-up"></i>
							</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						{ employeesList }
					</tbody>
				</table>
			</div>
		);
	}
}
export default MyteamTable;
