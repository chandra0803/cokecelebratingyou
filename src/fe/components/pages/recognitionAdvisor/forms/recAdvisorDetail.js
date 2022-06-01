import React from 'react';
import classNames from 'classnames';
import Pagination from 'react-js-pagination';
import ReactTooltip from 'react-tooltip';
import RecAdvisorTile from './recAdvisorTile';
import RecognitionInsights from './recognitionInsights';
import MyteamTable from './myteamTable';

class RecAdvisorDetail extends React.Component {
	constructor( props ) {
		super( props );
		this.state = {
			hasFetched: false,
			raTable: {
				'rowNumStart': 1,
				'activePage': 1,
				'rowNumEnd': 50,
				'sortColName': 'NO_VALUE',
				'sortedBy': '',
				'excludeUpcoming': 0,
				'filterValue': 0,
				'pendingStatus': 0
			}
		};
	}



	handleChange ( e ) {
		const filterVal = e.target.value;
		this.setState(	{
			raTable: Object.assign(	this.state.raTable, {	filterValue: filterVal, activePage: 1	}	),
			hasFetched:	true
		}, () => {
			this.aPICallFunction();
		}	);
	}

	
	aPICallFunction () {
		const { recognitionAdvisorFunc } = this.props;
		const { raUrl } = window.recognitionAdvisor;
		if( raUrl ) {
			recognitionAdvisorFunc( raUrl, this.state.raTable );
		}
	}

	handlePageChange( pageNumber ) {
		const tableElmnt = document.getElementById(	'my_team'	);
		tableElmnt.scrollIntoView();
		this.setState(	{
			raTable: Object.assign(	this.state.raTable, {	activePage: pageNumber	}	),
			hasFetched:	true
		}, () => {
			this.aPICallFunction();
		}	);
	}

	handleSort( sortedBy, sortColName ) {
		this.setState(	{
			raTable: Object.assign(	this.state.raTable, {	sortedBy: sortedBy, sortColName: sortColName, activePage: 1	}	),
			hasFetched:	true
		}, () => {
			this.aPICallFunction();
		}	);
	}

	render() {

		const {
			content
		} = this.props;

		const classes = classNames( {
			'raDetailPage': true
		} );

		const raEligiblePrograms = this.props.recognitionAdvisor.raEligiblePrograms;
		let	programList;

		if( raEligiblePrograms ) {
			programList = raEligiblePrograms.map( ( promo ) =>
				<li key={ promo.programId }>{ promo.programName }</li>
			);
		}
		
		return (
			<div className={ classes }>
				<RecAdvisorTile { ...this.props } hasFetched={	this.state.hasFetched	} />
				<RecognitionInsights content={ this.props.content } />
				<div>
					<h3 id="my_team">{ content[ 'ssi_contest.creator.my_team' ] }</h3>
					<div className="raFilterWrap">
							<i className="icon-arrow-1-down detailArrowToken"></i>
							<span className="filterLabel">{ content[ 'recognition.content.model.info.filter_by' ] }</span>
							<select className="filterTokens" onChange={ ( e ) => this.handleChange( e ) } value={ this.state.raTable.filterValue } >
								<option value="0">{ content[ 'system.general.all' ] }</option>
								<option value="1" disabled={ this.props.recognitionAdvisor.newHireAvailable === '0' }>{ content[ 'recognition.content.model.info.ra_new' ] }</option>
								<option value="2" disabled={ this.props.recognitionAdvisor.overDueAvailable === '0' }>{ content[ 'recognition.content.model.info.ra_overdue' ] }</option>
								<option value="3" disabled={ this.props.recognitionAdvisor.upComingAvailable === '0' }>{ content[ 'recognition.content.model.info.ra_upcoming' ] }</option>
							</select>
							<a className="raPromo fr" data-tip="React-tooltip">{ content[ 'recognition.content.model.info.ra_programs_display' ] }</a>
					</div>
					<div className="pagination">
						<div className="counts">
							<span className="start">
								{ this.props.recognitionAdvisor.tableRowNumStart }
							</span>–
							<span className="end">
								{ this.props.recognitionAdvisor.tableRowNumEnd }
							</span> <em className="of">{ content[ 'system.general.of' ] }</em>
							<span className="total"> { this.props.recognitionAdvisor.raTotalParticipants } </span>
						</div>
						<Pagination
							firstPageText={ <i className="icon-double-arrows-1-left"/> }
							lastPageText={ <i className="icon-double-arrows-1-right"/> }
							prevPageText={ <i className="icon-arrow-1-left" /> }
							nextPageText={ <i className="icon-arrow-1-right" /> }
							activePage={ this.state.raTable.activePage }
							itemsCountPerPage={ 50 }
							totalItemsCount={ this.props.recognitionAdvisor.raTotalParticipants }
							pageRangeDisplayed={ 5 }
							onChange={ ( e ) => this.handlePageChange( e ) }
						/>
					</div>
					<ReactTooltip place="top" type="light" effect="solid">
						<div className="byPromoHelpTip">
							<strong>{ content[ 'recognition.content.model.info.ra_programs_heading' ] }</strong>
							<ul> {programList} </ul>
						</div>
					</ReactTooltip>
					<MyteamTable { ...this.props } handleSort={	this.handleSort.bind(	this )	} />
					<div className="pagination">
						<div className="counts">
							<span className="start">
								{ this.props.recognitionAdvisor.tableRowNumStart }
							</span>–
							<span className="end">
								{ this.props.recognitionAdvisor.tableRowNumEnd }
							</span> <em className="of">{ content[ 'system.general.of' ] }</em>
							<span className="total"> { this.props.recognitionAdvisor.raTotalParticipants } </span>
						</div>
						<Pagination
							firstPageText={ <i className="icon-double-arrows-1-left"/> }
							lastPageText={ <i className="icon-double-arrows-1-right"/> }
							prevPageText={ <i className="icon-arrow-1-left" /> }
							nextPageText={ <i className="icon-arrow-1-right" /> }
							activePage={ this.state.raTable.activePage }
							itemsCountPerPage={ 50 }
							totalItemsCount={ this.props.recognitionAdvisor.raTotalParticipants }
							pageRangeDisplayed={ 5 }
							onChange={ ( e ) => this.handlePageChange( e ) }
						/>
					</div>
				</div>
			</div>
		);
	}
}

export default RecAdvisorDetail;
