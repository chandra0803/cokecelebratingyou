import React from 'react';
import RecAdvisorModal from './forms/recAdvisorModal';
import RecAdvisorDetail from './forms/recAdvisorDetail';
import RecAdvisorTile from './forms/recAdvisorTile';
import Spinner from '../../shared/spinner/spinner';

export default class RecognitionAdvisor extends React.Component {
	constructor( props ) {
			super( props );

			const { recognitionAdvisorFunc, raEligibleProgramsFunc } = this.props;
			const { raUrl, raEligibleProgramsUrl, raTilePageDisplay, raDetailPageDisplay, raEndModelPageDisplay } = window.recognitionAdvisor;

			const raTileModule = {
				'rowNumStart': 1,
				'rowNumEnd': 7,
				'activePage': 0,
				'sortColName': 'NO_VALUE',
				'sortedBy': '',
				'excludeUpcoming': 0,
				'filterValue': '',
				'pendingStatus': 1
			};
			const raDetailModule = {
				'rowNumStart': 1,
				'rowNumEnd': 50,
				'activePage': 0,
				'sortColName': 'NO_VALUE',
				'sortedBy': '',
				'excludeUpcoming': 0,
				'filterValue': 0,
				'pendingStatus': 0
			};
			const raRecEndModule = {
				'rowNumStart': 1,
				'rowNumEnd': 2,
				'activePage': 0,
				'sortColName': 'NO_VALUE',
				'sortedBy': '',
				'excludeUpcoming': 1,
				'filterValue': '',
				'pendingStatus': 1
			};

			if( raTilePageDisplay === 'yes' && raDetailPageDisplay === 'no' && raEndModelPageDisplay === 'no' )
			{
				this.state = {
					attributeData: raTileModule,
					raTilePage: true,
					raDetailPage: false,
					raEndModelPage: false,
					content: this.props.cmStringsArrayToObject( window.recognitionAdvisor.content )
				};

			} else if ( raTilePageDisplay === 'no' && raDetailPageDisplay === 'yes' && raEndModelPageDisplay === 'no' ) {

				this.state = {
					attributeData: raDetailModule,
					raTilePage: false,
					raDetailPage: true,
					raEndModelPage: false,
					content: this.props.cmStringsArrayToObject( window.recognitionAdvisor.content )
				};

			} else if ( raTilePageDisplay === 'no' && raDetailPageDisplay === 'no' && raEndModelPageDisplay === 'yes' ) {

				this.state = {
					attributeData: raRecEndModule,
					raTilePage: false,
					raDetailPage: false,
					raEndModelPage: true,
					raClaimDetail: this.props.recognitionAdvisor.raClaimDetail,
					content: this.props.cmStringsArrayToObject( this.props.recognitionAdvisor.content )
				};
			}

			if( raUrl && ( this.state.raTilePage || this.state.raDetailPage || this.state.raEndModelPage ) ) {
				recognitionAdvisorFunc( raUrl, this.state.attributeData );
				raEligibleProgramsFunc( raEligibleProgramsUrl );
			}

	}

	componentWillReceiveProps( nextProps ) {
		const raData = nextProps.recognitionAdvisor.raModel;
		if ( raData !== undefined && !raData ) {
			const raDOM = document.getElementsByClassName( 'recognitionAdvisorModule' );
			if( raDOM.length ) {
				raDOM[ 0 ].style.display = 'none';
			}
		}
	}

	render() {
		const  isRAHasDatas  = this.props.recognitionAdvisor.raModel;
		return (
			<div>
				{this.props.showSpinner &&
					<Spinner />
				}
				{this.state.raTilePage && isRAHasDatas &&
					<RecAdvisorTile { ...this.state } { ...this.props }/>
				}
				{this.state.raDetailPage &&
					<RecAdvisorDetail { ...this.state } { ...this.props } />
				}
				{this.state.raEndModelPage &&
					<RecAdvisorModal { ...this.state } { ...this.props }/>
				}
			</div>
		);
	}

}
