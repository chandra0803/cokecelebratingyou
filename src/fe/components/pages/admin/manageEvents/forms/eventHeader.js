import React from 'react';
import classNames from 'classnames';

class EventHeader extends React.Component 
{
	constructor( props ) 
	{
		super( props );
	}

	render() 
	{
		return (
			<div >
				<table border="0" cellpadding="10" cellspacing="0" width="100%">
					<tr>
						<td>
							<span className="headline">Manage Kinesis Event</span>
							<br/>
							<span className="content-instruction">
								<p>If you're not familiar with the internal functions of Kinesis, you should <i>not</i> use these links/buttons without developer assistance.</p>
							</span>
						</td>
					</tr>
				</table>		
			</div>
		);
	}
}

export default EventHeader;
