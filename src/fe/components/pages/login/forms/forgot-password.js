import React from 'react';
import classNames from 'classnames';

import LoaderButton from '../../../shared/loader-button/loader-button';
import NarrowForm from '../../../shared/narrow-form/narrow-form';

import { Alert } from 'component-library';
import '../loginReact.scss';

import { run, runValidation } from '../../../utils/form-validation';

import { required, validEmail } from '../../../utils/form-validation-rules';

const fieldValidations = [
  runValidation( 'username', 'First Name', required ),
  //runValidation( 'username', 'First Name', minLength( 6 ) ),

];

class ForgotPassword extends React.Component {

    constructor( props ) {
        super( props );
        this.state = {
            username: '',
            validationErrors: { },
            errors: []
        };
    }

    handleField = ( event ) => {
        const name = event.target.name;
        const newState = Object.assign( {}, this.state, { [ name ]: event.target.value } );
        newState.validationErrors = run( newState, fieldValidations );
        this.setState( newState );
    }

    errorFor = ( field ) => {
        return this.state.validationErrors[ field ] || '';
    }

    onSubmit = () => {
        this.setState( { showErrors: true } );
        if( Object.keys( this.state.validationErrors ).length ) {return null;}
        const {
                fetchDataForgotPassword
            } = this.props;
        const { username } = this.state;
        const {
            forgotPassUrl } = this.props.login;
        fetchDataForgotPassword( username, forgotPassUrl );
    }


    checkEnter = ( event ) => {

        const enterPressed = event.charCode === 13;
        const { username } = this.state;

        if ( enterPressed && username.length ) {
            event.preventDefault();
            this.onSubmit();
        } else if ( enterPressed ) {
            event.preventDefault();
        }
    }

  render() {

    const {
        content
    } = this.props;

    const {
        username
    } = this.state;

    const {
        message,
        toggleModal
    } = this.props.login;
    const {
        details
    } = this.props.forgotPass;

    const userFieldClasses = classNames( {
        'username-field': true,
        'input-wrap': true,
        'focused': true
    } );
    if( message && message.success && details.userActivated === false ) {
        message.success = message.success.template( { linkOpen: '<a href="login.do?activationPage=true&loginID=' + this.state.username + '" >', linkClose: '</a>' } );
    }
    const clientLogo = '';
    const loginErrors = [];
    return (
		<NarrowForm page="login" clientLogo={ clientLogo } errorsExist={ loginErrors.length > 0 }>
            <p>
                <a tabIndex="5" onClick={ ( e ) => this.props.handleFormChange( 'login' ) }>
                    <span><i className="icon-arrow-1-circle-left"></i> { content[ 'login.loginpage.back_to_login' ] }</span>
                </a>
            </p>
            {
                message &&
                !message.success &&              
                <h3 dangerouslySetInnerHTML={ { __html: content[ 'login.loginpage.forgot_your_password' ] } }/> 
            }
            {
                !message &&
                <h3 dangerouslySetInnerHTML={ { __html: content[ 'login.loginpage.forgot_your_password' ] } }/>                
            }
            {
                message &&
                message.success &&
                <h3 dangerouslySetInnerHTML={ { __html: content[ 'login.loginpage.we_found_you' ] } }/>               
            }

            {
                message &&
                message.error &&
                details &&
                        <Alert type={ 'error' } key={ message.id }>
                            <p dangerouslySetInnerHTML={ { __html: message.error } }/>
                        </Alert>

            }
            {
                message &&
                message.success &&
                details &&
                        <Alert type={ 'success' } key={ message.id }>
                            <p dangerouslySetInnerHTML={ { __html: message.success } }/>
                        </Alert>

            }

            <div className={ userFieldClasses }>
                <label htmlFor="formUserId" className="control-label">{ content[ 'login.loginpage.username' ] }</label>
                    <input
                    type="text"
                    name="username"
                    tabIndex={ 1 }
                    autoFocus
                    id="formUserId"
                    className="gq-input"
                    value={ username }
                    onChange={ this.handleField }
                    onKeyPress={ this.checkEnter } />
                    <div className="validation-error"><span className="text">{this.errorFor( 'username' )}</span></div>
            </div>


			<LoaderButton
				handleClick={ this.onSubmit }
				disabled={ !username.length ? true : false }
				customClass="btn btn-block btn-primary form-action-btn"
				tabIndex={ 3 }>			 	
				{ <span dangerouslySetInnerHTML={ { __html: content[ 'system.button.submit' ] } }/> }
			</LoaderButton>
            {message &&
                <p className="help">
                    <a tabIndex="3" onClick={ ( e ) => this.props.toggleModalDisplay( !toggleModal, 'help' ) }>
                        <span>{ content[ 'login.loginpage.help' ] }</span>
                    </a>
                </p>
            }
		</NarrowForm>
    );
  }
}


export default ForgotPassword;
