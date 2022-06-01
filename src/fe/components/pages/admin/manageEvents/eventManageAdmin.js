import React from 'react';
import EventListenerDetail from './forms/eventListenerDetail';
import EventHeader from './forms/eventHeader';
import EventFilter from './forms/eventFilter';
import EventList from './forms/eventList';


export default class EventManageAdmin extends React.Component 
{
	constructor( props ) 
	{
		super( props );

		const { getEventListenerStatus, getEventAttributes} = this.props;
		const { getEventListenerStatusUrl, getEventAttributesUrl } = window.eventManageAdmin;

		getEventListenerStatus( getEventListenerStatusUrl );
		getEventAttributes(getEventAttributesUrl);
	}

	render() 
	{
		return (
			<div>
				<EventHeader { ...this.state } { ...this.props }/>
				<br/>
				<EventListenerDetail { ...this.state } { ...this.props }/>
				<br/>
				<EventFilter { ...this.state } { ...this.props }/>
				<br/>
				<EventList { ...this.state } { ...this.props }/>
			</div>
		);
	}

}
