import React from 'react';

const RecognitionInsights = ( { content } ) => {
	return (
		<div>
		<div className="raInsights">
			<div>
					<div className="raInsightsLogoPosition">
						<div className="raInsightsLogo">
							<svg id="Layer_1" data-name="Layer 1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 191.97 189.22"><defs><clipPath id="clip-path" transform="translate(0 0)"><rect width="191.97" height="189.21" /></clipPath></defs><title>Vector Smart Object</title><g><path className="raBrandingOuter" d="M183.85,33a95.66,95.66,0,0,0-22-31.16,6.69,6.69,0,0,0-9.19,0A82.26,82.26,0,0,1,96,24.31,82.26,82.26,0,0,1,39.35,1.82a6.7,6.7,0,0,0-9.19,0,96,96,0,0,0,7.38,146h0l-8.9,41.4,54.95-22.35h0a96.83,96.83,0,0,0,12.41.8,95.91,95.91,0,0,0,96-96A95.26,95.26,0,0,0,183.85,33m-29.46,97.09A82,82,0,0,1,96,154.27a83.63,83.63,0,0,1-13.79-1.14L46.92,166.91,52.7,142a83.23,83.23,0,0,1-15.12-12A82.51,82.51,0,0,1,35.08,15.9,95.56,95.56,0,0,0,96,37.7a95.56,95.56,0,0,0,60.91-21.8,82.59,82.59,0,0,1-2.51,114.18" transform="translate(0 0)"/><path className="raBrandingInner"  d="M140.63,59.42a24.39,24.39,0,0,0-17,6.88,10.21,10.21,0,0,0-1,.83L96,92.2,69.39,67.13a10.1,10.1,0,0,0-1-.83,24.55,24.55,0,1,0,.32,35A24.74,24.74,0,0,0,72,97.22l17.07,16.09a10,10,0,0,0,13.78,0l17.07-16.09a24.74,24.74,0,0,0,3.32,4.11,24.55,24.55,0,1,0,17.36-41.92M51.34,95.14A11.16,11.16,0,1,1,62.5,84,11.16,11.16,0,0,1,51.34,95.14m89.29,0A11.16,11.16,0,1,1,151.79,84a11.16,11.16,0,0,1-11.16,11.16" transform="translate(0 0)" /></g></svg>
						</div>
					</div>
				<div className="raInsightsBackgrondImg"></div>
				<div className="raInsightsGradgient"></div>
			</div>
			<div className="raInsightsBodyContent">
			<h4>{ content[ 'recognition.content.model.info.ra_insights' ] }</h4>
			<p>{ content[ 'recognition.content.model.info.ra_emp_better_perform_content' ] }</p>
			<div className="clear"></div>
			</div>
		</div>
		</div>
	);
};

export default RecognitionInsights;
