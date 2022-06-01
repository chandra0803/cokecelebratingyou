import React from 'react';
import LoaderButton from '../../../shared/loader-button/loader-button';
import NarrowForm from '../../../shared/narrow-form/narrow-form';
import PasswordHelper from '../../../shared/password-helper-input/password-helper';

import { Alert } from 'component-library';
import '../loginReact.scss';




class CreatePassword extends React.Component {

    constructor( props ) {
        super( props );
        this.state = {
            password1: '',
            focusedField: '',
            token: props.verification.token.token,
            disabled: true,
            passwordRulesChecks: {
                isLowerCaseRequired: true,
                isUpperCaseRequired: true,
                isSpecialCharRequired: true,
                isNumberRequired: true,
                hasValidLength: false
            }
        };
    }

    handleField = event => {
        const { name, value } = event.target;
        this.setState( { [ name ]: value } );
        this.validate( value );
    }
    validate = ( value ) => {
        const passwordValue = value;
        const { minRules, ignoreValidation } = this.props.passConfirm;
        const {
            isLowerCaseRequired,
            isUpperCaseRequired,
            isSpecialCharRequired,
            isNumberRequired,
            minLength
        } = this.props.passConfirm.passwordRules;
        const passValidate = {
            passwordRulesChecks: {
                isLowerCaseRequired: isLowerCaseRequired ? /^(?=.*[a-z]).+$/.test( passwordValue ) : true,
                isUpperCaseRequired: isUpperCaseRequired ? /^(?=.*[A-Z]).+$/.test( passwordValue ) : true,
                isSpecialCharRequired: isSpecialCharRequired ? /[-!$%#@^&*()_+|~=`{}[\]:";'<>?,./]/.test ( passwordValue ) : true, //` <-- editor not smart enough to realize backtick in regex not start of string. need this comment to "close" the string.
                isNumberRequired: isNumberRequired ? /[0-9]/.test( passwordValue ) : true
            }
        };
        let disabled = 0;
        Object.keys( passValidate.passwordRulesChecks ).forEach( function ( key ) {
            if( passValidate.passwordRulesChecks[ key ] === true ) {
                disabled++;
            }
        } );
        if( ignoreValidation === true ) {
            this.setState( { disabled: false } );
            return;
        }
        if( disabled >= minRules ) {
            if( this.lengthTest( passwordValue, minLength ) ) {
                this.setState( { disabled: false } );
            } else {
                this.setState( { disabled: true } );
            }
        } else {
            this.setState( { disabled: true } );
        }
    };
    lengthTest = ( passwordValue, minLength ) => {
        if ( passwordValue.length >= minLength ) {
            return true;
        } else {
            return false;
        }
    };
    onSubmit = () => {
            const { resetPwUrl } = this.props.login;
            const create = false;
            const passObj = {
                password: this.state.password1,
                userName: this.props.verification.activation.userName,
                token: this.state.token,
                activation: true
            };
            this.props.submitPass( passObj, resetPwUrl, create );
    }

    checkEnter = ( event ) => {
        const enterPressed = event.charCode === 13;
        const { password1, disabled } = this.state;

        if ( enterPressed && password1.length && !disabled ) {
            event.preventDefault();
            this.onSubmit();
        } else if ( enterPressed ) {
            event.preventDefault();
        }
    }

  render() {


	const loggingIn = false;
    const {
        passwordRules,
        passwordLabels,
        minRules,
        minRulesLabel,
        ignoreValidation
    } = this.props.passConfirm;
    const {
        message,
        toggleModal,
        messages
    } = this.props.login;
    const {
        content
    } = this.props;
    const {
        userName
    } = this.props.verification.activation;
    const {
        password1
    } = this.state;
    let errorMessages;
    if( messages && messages.length ) {
    errorMessages =
        messages.map( function( message, i ) {
            return ( <Alert type={ 'error' } key={ i }>
                <p dangerouslySetInnerHTML={ { __html: message.error } }/>
            </Alert> );
        } );
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
			<h3 className="no-bottom">{ content[ 'login.loginpage.create_password' ] }</h3>

            {
                message &&
                message.error &&
                        <Alert type={ 'error' } key={ message.id }>
                            <p dangerouslySetInnerHTML={ { __html: message.error } }/>
                        </Alert>

            }
            {
                message &&
                message.success &&
                        <Alert type={ 'success' } key={ message.id }>
                            <p dangerouslySetInnerHTML={ { __html: message.success } }/>
                        </Alert>

            }
            {
                messages &&
                messages.length &&
                errorMessages

            }
            <p className="loginID">{ content[ 'login.loginpage.username' ] + ': ' + userName }</p>
            <PasswordHelper name="password1" label={ content[ 'login.loginpage.password' ] } autoFocus={ true } tabIndex="1" passwordRules={ passwordRules } passwordLabels={ passwordLabels } minRules={ minRules } minRulesLabel={ minRulesLabel } ignore={ ignoreValidation } onKeyPress={ this.checkEnter } onChange={ this.handleField } value={ password1 }/>



			<LoaderButton
				fetching={ loggingIn }
				handleClick={ this.onSubmit }
				disabled={ this.state.disabled }
				customClass="btn btn-block btn-primary form-action-btn"
				tabIndex={ 3 }
				done={ this.props.done }>
				{ content[ 'system.button.submit' ] }
			</LoaderButton>
            <p className="help">
                <a tabIndex="3" onClick={ ( e ) => this.props.toggleModalDisplay( !toggleModal, 'help' ) }>
                    <span>{ content[ 'login.loginpage.help' ] }</span>
                </a>
            </p>
		</NarrowForm>
    );
  }
}


export default CreatePassword;
