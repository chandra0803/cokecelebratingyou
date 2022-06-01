import React from 'react';
import { setEventFilter } from '../dux/eventManageAdmin';

class EventFilter extends React.Component {
	constructor( props ) 
	{
		super( props );
		this.state = 
		{
			selectedEventSource :'',
			selectedSchema : '',
			selectedEventType :'',
			selectedStatus : 'ERROR',
			eventSchemas: [],
			eventTypes: []
		};

		this.handleSearch();
	}

	handleChangeOfEvtSrc ( e ) 
	{
		const filterVal = e.target.value;
		this.setState( {
			selectedEventSource: filterVal,
			selectedSchema: '',
			selectedEventType: '',
			eventSchemas: [],
			eventTypes: []
		} );

		let evtSrcs = this.getEventSources(filterVal);
		var filtered = [];
		for (var j =0; j< evtSrcs.length; j++)
		{
			filtered.push(<option key={ evtSrcs[j].schemaName } value={ evtSrcs[j].schemaName }>{ evtSrcs[j].schemaName }</option>);
		}
		this.setState( {
			eventSchemas: filtered
		});

	}

	handleChangeOfSchema ( e ) 
	{
		const filterVal = e.target.value;
		this.setState( {
			selectedSchema: filterVal,
			selectedEventType: ''
		} );
		let evtSrcs = this.getEventSources(this.state.selectedEventSource);
		var filtered = [];
		for (var i =0; i< evtSrcs.length; i++)
		{
			if (evtSrcs[i].schemaName == filterVal)
			{
				let events = evtSrcs[i].events;
				for (var j=0; j<events.length; j++ )
				{
					filtered.push(<option key={ events[j]} value={ events[j] }>{ events[j] }</option>);
				}
				break;
			}
		}
		this.setState( {
			eventTypes: filtered
		});

	}

	handleChangeOfEventType ( e )
	{
		const filterVal = e.target.value;
		this.setState( {
			selectedEventType: filterVal
		} );
	} 

	handleSearch()
	{
		const { getEventListUrl } = window.eventManageAdmin;
		const { getEventList, setEventFilter } = this.props;
		
		var eventFilter = {
			selectedEventSource : this.state.selectedEventSource,
			selectedSchema : this.state.selectedSchema,
			selectedEventType : this.state.selectedEventType,
			selectedStatus : this.state.selectedStatus,
		};
		
		setEventFilter( eventFilter );
		
		var params = {
			'appName' : this.state.selectedEventSource,
			'schemaName' : this.state.selectedSchema,
			'eventName' : this.state.selectedEventType,
			'state' : this.state.selectedStatus,
			'startRow' : 1
		};
		var queryString = Object.keys(params).map(key => key + '=' + params[key]).join('&');
		var url = getEventListUrl + '?' + queryString;
		getEventList(url);
	}

	getEventSources(appName)
	{
		if (this.props.eventManageAdmin && this.props.eventManageAdmin.eventAttributes)
		{
			const eventSources = this.props.eventManageAdmin.eventAttributes.eventSource;
			if( eventSources ) {
				var filtered = [];
				for (var i = 0; i < eventSources.length; i++) 
				{
					if (eventSources[i].applicationName == appName) 
					{
						for (var j =0; j< eventSources[i].eventSchema.length; j++)
						{
							filtered.push(eventSources[i].eventSchema[j]);
						}
						break;
					}
				}
				return filtered;
			}
		}
	}

	render() 
	{
		let evtSrcs = [];

		if (this.props.eventManageAdmin && this.props.eventManageAdmin.eventAttributes)
		{
			const eventSources = this.props.eventManageAdmin.eventAttributes.eventSource;
			if( eventSources ) 
			{
				evtSrcs = eventSources.map( ( evtSrc ) =>
					<option key={ evtSrc.applicationName } value={ evtSrc.applicationName }>{ evtSrc.applicationName }</option>
				);
			}
		}

		return (
			<div >
				<table>
					<tr>
						<td width="25%">
							<label id="messageLabel">Event Source : </label>

							<select 
								onChange={ ( e ) => this.handleChangeOfEvtSrc( e ) } 
								value={ this.state.selectedEventSource }
								style={{width:"100px"}}          
							>
								<option value=""></option>
								{evtSrcs}
							</select>
						</td>
						<td width="23%">
							<label id="messageLabel">Schema : </label>
							<select 
								onChange={ ( e ) => this.handleChangeOfSchema( e ) } 
								value={ this.state.selectedSchema } 
								style={{width:"100px"}}            
							>
								<option value=""></option>
								{this.state.eventSchemas}
							</select>
						</td>
						<td width="23%">
							<label id="messageLabel">Event Type : </label>
							<select 
								onChange={ ( e ) => this.handleChangeOfEventType( e ) } 
								value={ this.state.selectedEventType }  
								style={{width:"100px"}}        
							>
								<option value=""></option>
								{this.state.eventTypes}
							</select>
						</td>
						<td width="23%">
							<label id="messageLabel">Status : </label>
							<select 
								onChange={ ( e ) => this.setState({ selectedStatus: e.target.value }) } 
								value={ this.state.selectedStatus} 
								style={{width:"100px"}}            
							>
								<option value="ERROR">Error</option>
								<option value="COMPLETED">Completed</option>
							</select>
						</td>
						<td className="left-align">
							<button  type="button" onClick={ ( e ) => this.handleSearch() }>Search</button>
						</td>
					</tr>
				</table>
			</div>
		);
	}
}

export default EventFilter;
