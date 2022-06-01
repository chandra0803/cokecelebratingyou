import React from 'react';
import classNames from 'classnames';
import { Alert } from 'component-library';

import LoaderButton from '../loader-button/loader-button';

import './modal.scss';
class Modal extends React.Component {
	constructor( props ) {
        super( props );
        this.state = {
            token: ''
        };
    }
	handleField = ( event ) => {
        const name = event.target.name;
        const newState = Object.assign( {}, this.state, { [ name ]: event.target.value } );
        this.setState( newState );
    }
	checkEnter = ( event ) => {
        const enterPressed = event.charCode === 13;
        const { token } = this.state;

        if ( enterPressed && token.length ) {
            event.preventDefault();
            this.onSubmit();
        } else if ( enterPressed ) {
            event.preventDefault();
        }
    }
	onSubmit = () => {

        const {
            selfEnrollUrl } = this.props.login;
        const { token } = this.state;
        this.props.sendToken( { registrationCode: token }, selfEnrollUrl, false );

    }
	render() {
		const { token } = this.state;
		const {
			toggleModal,
			modalPage,
			message
		} = this.props.login;
		const classes = classNames( {
			'modal': true,
			'privacy': modalPage === 'privacy' ? true : false
		} );
		const closeClasses = classNames( {
			'btn': true,
			'btn-block': true,
			'btn-primary': modalPage === 'register' ? false : true
		} );
		return (
			<div className="modal-wrapper">
				<div className="modal-backdrop fade in"></div>
				<div id="Modal" className={ classes } >
					<div id="ModalHeader" className="modal-header"><h3>{
						modalPage === 'privacy' &&
						<span dangerouslySetInnerHTML={ { __html: this.props.content[ 'system.general.privacy_policy' ] } } />						
					}
					{
						modalPage === 'help' &&
						<span dangerouslySetInnerHTML={ { __html: this.props.content[ 'system.general.help_title' ] } } />						
					}
					{
						modalPage === 'noCode' &&
						<span dangerouslySetInnerHTML={ { __html: this.props.content[ 'login.forgotpwd.carrier_support' ] } } />						
					}
					{
						modalPage === 'tnc' &&
						<span dangerouslySetInnerHTML={ { __html: this.props.content[ 'participant.termsAndConditions.page_title' ] } } />						
					}
					{
						modalPage === 'register' &&
						<span dangerouslySetInnerHTML={ { __html: this.props.content[ 'login.registration.register' ] } } />						
					}
				</h3><button onClick={ ( e ) => this.props.toggleModalDisplay( !toggleModal, 'privacy' ) } data-dismiss="modal" className="close" type="button"><i className="icon-close"></i></button></div>
						<div id="ModalBody" className="modal-body">
						{
						message &&
							message.type &&
								!message.error &&
									!message.success &&
										<Alert type={ message.type } key={ message.id }>
											<p dangerouslySetInnerHTML={ { __html: message.name } }/>
											<p dangerouslySetInnerHTML={ { __html: message.text } }/>
											</Alert>

						}
						{
							modalPage === 'privacy' &&
							<p dangerouslySetInnerHTML={ { __html: this.props.content[ 'admin.privacy.html' ] } }></p>
						}
						{
							modalPage === 'help' &&
							<span dangerouslySetInnerHTML={ { __html: this.props.content[ 'system.general.help_text' ] } }/>							
						}
						{
							modalPage === 'tnc' &&
							<div>
								<span dangerouslySetInnerHTML={ { __html: this.props.content[ 'login.forgotpwd.recovery_tnc' ] } }/>
								<h4 dangerouslySetInnerHTML={ { __html: this.props.content[ 'login.forgotpwd.carrier_support' ] } }/>
								<span dangerouslySetInnerHTML={ { __html: this.props.content[ 'login.forgotpwd.carrier_support_info' ] } }/>
							</div>
						}
						{
							modalPage === 'noCode' &&
							<span dangerouslySetInnerHTML={ { __html: this.props.content[ 'login.forgotpwd.carrier_support_info' ] } }/>							
						}
						{
							modalPage === 'register' &&
							<div>
							<label htmlFor="formToken">{this.props.content[ 'login.registration.regi_code' ]}</label>
								<input
								type="text"
								name="token"
								tabIndex={ 1 }
								autoFocus
								id="formToken"
								className="gq-input"
								value={ token }
								onChange={ this.handleField }
								onKeyPress={ this.checkEnter } />
							</div>
						}
						</div>
					<div id="ModalFooter" className="modal-footer">{modalPage == 'register' && <LoaderButton customClass="btn btn-block btn-primary register" handleClick={ this.onSubmit } disabled={ token.length ? false : true } ><span dangerouslySetInnerHTML={ { __html: this.props.content[ 'login.registration.register' ] } }/></LoaderButton>}<a className={ closeClasses } onClick={ ( e ) => this.props.toggleModalDisplay( !toggleModal ) }><span dangerouslySetInnerHTML={ { __html: this.props.content[ 'system.button.close' ] } }/></a></div>
				</div>
			</div>
		);
	}
}

export default Modal;
