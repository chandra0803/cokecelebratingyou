import React from 'react';
import classNames from 'classnames';
import Pagination from 'react-js-pagination';

class EventList extends React.Component {
	constructor( props ) 
	{
		super( props );
		this.state = {
			activePage : 1
		};
	}

	handlePageChange( pageNumber )
	{
		const { getEventListUrl } = window.eventManageAdmin;
		var filter =  this.props.eventManageAdmin.eventFilter;
		const { getEventList } = this.props;
		console.log('filterParms', filter);

		var rowStart =  ( pageNumber * 10  - 10 ) + 1;
		var params = {
			'appName' : filter.selectedEventSource,
			'schemaName' : filter.selectedSchema,
			'eventName' : filter.selectedEventType,
			'state' : filter.selectedStatus,
			'startRow' : rowStart
		};

		this.setState(	{
			activePage: pageNumber
		}, () => {
			var queryString = Object.keys(params).map(key => key + '=' + params[key]).join('&');
			var url = getEventListUrl + '?' + queryString;
			getEventList(url);
		});
	}

	showMsg(msg)
	{
		alert(msg);
	}

	render() 
	{
		let events, eventLst, totRecords=0, startRow, endRow, rowDisplayMsg;
		if ( this.props.eventManageAdmin.eventList )
		{
			events = this.props.eventManageAdmin.eventList.events;
			totRecords = this.props.eventManageAdmin.eventList.totalRecords;
			startRow =  ( this.state.activePage * 10  - 10 ) + 1;
			if ( (startRow - 1+ 10) >  totRecords)
			{
				endRow = totRecords;
			}
			else
			{
				endRow = startRow - 1+ 10;
			}
			rowDisplayMsg = 'Showing ' + startRow + ' - ' + endRow + ' of ' + totRecords;
		}

		if ( events )
		{
			eventLst = events.map( ( event, idx ) =>
				<tr key={ event.id } className={ `${idx%2 == 0 ? 'crud-table-row2' : 'crud-table-row1' }` }>
					<td className="crud-content left-align">
						{event.id}
					</td>
					<td className="crud-content left-align">
						{event.creatdDate}
					</td>
					<td className="crud-content left-align">
						{event.applicationName}
					</td>
					<td className="crud-content left-align">
						{event.schemaName}
					</td>
					<td className="crud-content left-align">
						{event.eventName}
					</td>
					<td className="crud-content left-align">
						{event.state}
					</td>
					<td className="crud-content left-align">
						{event.message}
					</td>
					<td className="crud-content left-align">
						<button  onClick={ ( e ) => this.showMsg(`${event.log}`) }>View</button>
		
					</td>
					<td className="crud-content left-align">
						<button  onClick={ ( e ) => this.showMsg(`${event.data}`) }>View</button>
					</td>
				</tr>
			);
		}

		return (
			<div>
				{totRecords > 0 &&
				<table width="100%">
					<tr>
						<td className="left-align" width="60%">
							 {rowDisplayMsg}
						</td>
						<td>
							<Pagination
								firstPageText={ 'First' }
								lastPageText={ 'Last' }
								prevPageText={ 'Previous' }
								nextPageText={ 'Next' }
								activePage={ this.state.activePage }
								itemsCountPerPage={ 10 }
								totalItemsCount={this.props.eventManageAdmin.eventList.totalRecords}
								pageRangeDisplayed={ 10 }
								onChange={ ( e ) => this.handlePageChange( e ) }
							/>
						</td>
					</tr>
				</table>
				}
				<table className="crud-table">
					<thead>
						<tr>
							<th className="crud-table-header-row">
								#
							</th>
							<th className="crud-table-header-row">
								Created Date
							</th>
							<th className="crud-table-header-row">
								Source
							</th>
							<th className="crud-table-header-row">
								Schema Name
							</th>
							<th className="crud-table-header-row">
								Event Type
							</th>
							<th className="crud-table-header-row">
								Status
							</th>
							<th className="crud-table-header-row">
								Message
							</th>
							<th className="crud-table-header-row">
								Log
							</th>
							<th className="crud-table-header-row">
								Data
							</th>
						</tr>
					</thead>
					<tbody>
						{eventLst}
					</tbody>
				</table>
			</div>
		);
	}
}

export default EventList;
