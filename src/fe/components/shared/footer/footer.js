import React from 'react';

const Content = ( props ) => {
    const key = [ props.view, props.component, props.tag ].join( '.' );
    return <span dangerouslySetInnerHTML={ { __html: props.content[ key ] || key } }></span>;
};
class Footer extends React.Component {
	render() {
	const {
		content,
        showTerms,
        toggleModal
	} = this.props;
    return (
		<footer id="footer" className="site-header">
			<div className="container">
				<div id="globalFooterView">
					<ul className="nav">
						<li>
							<a onClick={ ( e ) => this.props.toggleModalDisplay( !toggleModal, 'privacy' ) } id="privacyPolicyFooterLink">
								<Content
									view="system"
									component="general"
									tag="privacy_policy"
									content={ content } />
							</a>
						</li>
                        {showTerms &&
                        <li>
							<a href="termsAndConditionsView.do?method=review" id="termsFooterLink" >
								<Content
									view="system"
									component="general"
									tag="t&c"
									content={ content } />
							</a>
						</li>
                        }
                        <li className="copyright">
                            <Content
                                view="system"
                                component="general"
                                tag="copyright_text"
                                content={ content } />
                            </li>
					</ul>
				</div>
			</div>
		</footer>
	);
}
}

export default Footer;
