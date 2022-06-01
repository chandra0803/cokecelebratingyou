import React from 'react';
import classNames from 'classnames';

import LoaderButton from '../../../shared/loader-button/loader-button';
import NarrowForm from '../../../shared/narrow-form/narrow-form';
import 'whatwg-fetch';
import { Alert } from 'component-library';

import '../loginReact.scss';

import { run, runValidation } from '../../../utils/form-validation';

import { required, validEmail } from '../../../utils/form-validation-rules';

const fieldValidations = [
  runValidation( 'username', 'First Name', required ),
  runValidation( 'email', 'Email Address', required ),
  runValidation( 'email', 'Email Address', validEmail ),

];
class ForgotUserIdForm extends React.Component {

    constructor( props ) {
        super( props );
        this.state = {
            emailOrPhone: '',
            focusedField: '',
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

    onSubmitEmailOrPhone = () => {

        const {
            fetchData } = this.props;
        const {
            forgotIdUrl } = this.props.login;
        const { emailOrPhone } = this.state;
        fetchData( emailOrPhone, forgotIdUrl );

    }

    checkEnter = ( event ) => {

        const enterPressed = event.charCode === 13;
        const { emailOrPhone } = this.state;
        if ( enterPressed && emailOrPhone.length ) {
            event.preventDefault();
            this.onSubmitEmailOrPhone();
        } else if ( enterPressed ) {
            event.preventDefault();
        }
    }

  render() {
	const loggingIn = false;
    const {
        content
    } = this.props;
    const {
        message,
        toggleModal
    } = this.props.login;
    const { details } = this.props.forgotId;
    const {
        emailOrPhone
    } = this.state;

    const emailFieldClasses = classNames( {
        'email-field': true,
        'input-wrap': true,
        'focused': true,
		'forgot-emailId': true
    } );
    const clientLogo = '';
    const loginErrors = [];

    return (
		<NarrowForm page="login" clientLogo={ clientLogo } errorsExist={ loginErrors.length > 0 } method="post">
            <p>
                <a tabIndex="4" onClick={ ( e ) => this.props.handleFormChange( 'login' ) }>
                    <span><i className="icon-arrow-1-circle-left"></i> { content[ 'login.loginpage.back_to_login' ] }</span>
                </a>
            </p>
            <h3 dangerouslySetInnerHTML={ { __html: content[ 'login.loginpage.forgot_login_id' ] } }/>
            <p className="userIdHint" dangerouslySetInnerHTML={ { __html: content[ 'login.loginpage.user_id_is_your_employee_number' ] } }/>
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



                <div className={ emailFieldClasses }>
                    <label htmlFor="formUserId" className="control-label forgotUserId">{ content[ 'login.loginpage.email_or_phone' ] }</label>
                    <input
                        type="text"
                        name="emailOrPhone"
                        tabIndex="1"
                        id="formUserId"
                        className="gq-input"
                        autoFocus
                        value={ emailOrPhone }
                        onChange={ this.handleField }
                        onFocus={ this.handleInputFocus }
                        onBlur={ this.handleInputBlur }
                        onKeyPress={ this.checkEnter } />
                    </div>

                    <LoaderButton
                        fetching={ loggingIn }
                        handleClick={ this.onSubmitEmailOrPhone }
                        disabled={ !emailOrPhone.length ? true : false }
                        customClass="btn btn-block btn-primary form-action-btn"
                        tabIndex={ 2 }
                        done={ this.props.done }>
                    	<span dangerouslySetInnerHTML={ { __html: content[ 'system.button.submit' ] } }/>                        
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


export default ForgotUserIdForm;
