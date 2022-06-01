import React from 'react';

class EventListenerDetail extends React.Component 
{
	constructor( props ) 
	{
		super( props );
	}

	render() 
	{
		const { instance } = window.eventManageAdmin;
		let status = 'down';
		let message = '';
		let isShowHelpToStartListener = false;
		if(this.props.eventManageAdmin.eventListenerStatus){
			status = this.props.eventManageAdmin.eventListenerStatus.status;
			message = this.props.eventManageAdmin.eventListenerStatus.message;
			if ( status === 'down')
			{
				isShowHelpToStartListener = true;
			}

		}

		return (
			<div >
				<label id="messageLabel">Kinesis Event Listener is </label>
				<b>{status}</b>
				<label id="messageLabel"> in </label>
				<b>{instance}</b>
				<div>
					<b>{message}</b>
				</div>
				{
					isShowHelpToStartListener &&
					<div>
						<p>Start the Kinesis listener immediately please use the Process <b>KinesisListenerAutoRetryProcess</b>. After start successfully, status of the listener view displays as running, but the listener might require some time to consume events.</p>
					</div>
				}
			</div>

		)
	}
}

export default EventListenerDetail;
