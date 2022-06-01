import React from 'react';
import classNames from 'classnames';
import LoaderButton from '../../../../shared/loader-button/loader-button';
import NarrowForm from '../../../../shared/narrow-form/narrow-form';

import { Alert } from 'component-library';
import '../../loginReact.scss';




class MethodOfContact extends React.Component {

    constructor( props ) {
        super( props );
        this.state = {
            moc: {
                selectedOption: null,
                selectedType: null,
                shared: null
            },
            focusedField: '',
            filterText: ''
        };
        this.handleOptionChange = this.handleOptionChange.bind( this );
        this.onSubmit = this.onSubmit.bind( this );
    }

    onSubmit = ( event ) => {
        let contact = {};
        let isNoneWorks;
        if ( event ) {
          isNoneWorks = event.target.parentElement.classList.contains( 'noneWorks' );
        }
        if ( isNoneWorks ) {
          const cType = event.target.parentElement.getAttribute( 'data-contactType' );
          const cId = event.target.parentElement.getAttribute( 'data-contactId' );
          contact = {
              contactId: cId,
              contactType: cType
          };
        }else {
          contact = {
              contactId: this.state.moc.selectedOption,
              contactType: this.state.moc.selectedType
          };
        }
        const { methodIdUrl, methodPassUrl, activContMethUrl } = this.props.login;
        if( this.props.forgotPass.details ) {
            const user = false;
            this.props.sendContact( contact, methodPassUrl, user );
        } else if ( this.props.forgotId && this.props.forgotId.details ) {
            contact.sendMessage = !this.props.methodOfContact.shared;
            const user = true;
            this.props.sendContact( contact, methodIdUrl, user, this.props.methodOfContact.shared );
        } else {
            const user = false;
            this.props.sendContact( contact, activContMethUrl, user );
            if( contact.contactType != 'NONE' )
            {
              const newurl = window.location.protocol + '//' + window.location.host + window.location.pathname + '?activation=true';
              window.history.pushState( { path: newurl }, '', newurl );
            }

        }
    }

    handleOptionChange = ( changeEvent ) =>  {
      this.setState( {
          moc: {
              selectedType: changeEvent.currentTarget.getAttribute( 'data-contact-type' ),
              selectedOption: changeEvent.currentTarget.value,
              shared: changeEvent.currentTarget.getAttribute( 'data-shared' )
          }
      } );
    }
    handleField = ( event ) => {
        const count = event.target.value.length;
        const { name, value } = event.target;
        this.setState( { [ name ]: value } );
        if( count >= 3 || count === 0 ) {
            this.autoComplete( event.target.value );
        }
    }
    autoComplete = ( query ) => {
        const { autoCompleteUrl } = this.props.login;
        const { emailOrPhone } = this.props.forgotId;
        this.props.sendAutoQuery( query, autoCompleteUrl, emailOrPhone );
    }

  render() {
    const {
        content,
    } = this.props;
    const {
        message,
        toggleModal
    } = this.props.login;

    const {
        contactMethods,
        shared
    } = this.props.methodOfContact;
    const {
        single,
        showAutocomplete
    } = this.props.methodOfContact.contactMethods;

    const {
        filterText
    } = this.state;
    const filterFieldClasses = classNames( {
        'input-wrap': true,
        'focused': true,
    } );
    const clientLogo = '';
    const loginErrors = [];
    let radioBtns;
    let noneWorks = false;
    let cId = '';
    let cType = '';
    if( contactMethods.contactMethods && contactMethods.contactMethods.length ) {
        contactMethods.contactMethods.map( ( method, index ) => {
            if( method.contactType === 'NONE' ) {
              noneWorks = true;
              cId = method.contactId;
              cType = method.contactType;
            }
            if( method.value.indexOf( '&#8203;' ) >= 0 ) {
                return;
            }
            method.value = method.value.replace( '@', '&#8203;@' );
        } );
        radioBtns = contactMethods.contactMethods.map( ( method, index ) =>
        <div className="radio" key={ index.toString() } >
            { method.contactType !== 'NONE' &&
              <label>
                  <input type="radio" name="radio" data-shared={ method.unique === false ? true : false } data-contact-type={ method.contactType } value={ method.contactId.toString() } checked={ method.contactId.toString() === this.state.moc.selectedOption }  onChange={ this.handleOptionChange } />
                  {method.contactType === 'PHONE' ? <span className="icon-smartphone"></span> : <span className="icon-email"></span>}{method.unique === false ? <span className="info"><span className="icon-users-male-female"></span><span className="shared">{content[ 'login.loginpage.shared_contact_warning' ]}</span></span> : ''}<span className="emailMOC" dangerouslySetInnerHTML={ { __html: method.value } }></span>
              </label>
            }
        </div>

    );
    }

    return (
        <NarrowForm page="login" clientLogo={ clientLogo } errorsExist={ loginErrors.length > 0 }>
            <p>
                <a tabIndex="5" onClick={ ( e ) => this.props.handleFormChange( 'login' ) }>
                    <span><i className="icon-arrow-1-circle-left"></i> { content[ 'login.loginpage.back_to_login' ] }</span>
                </a>
            </p>
            { shared &&
                <h3 className="no-bottom" dangerouslySetInnerHTML={ { __html: content[ 'login.loginpage.getting_closer' ] } }/>
            }
            {!shared &&
                <h3 className="no-bottom" dangerouslySetInnerHTML={ { __html: content[ 'login.loginpage.we_found_you' ] } }/>
            }
            {
                message &&
                message.error &&
                contactMethods &&
                !noneWorks &&
                        <Alert type={ 'error' } key={ message.id }>
                            <p dangerouslySetInnerHTML={ { __html: message.error } }/>
                        </Alert>

            }
            {
                message &&
                message.success &&
                contactMethods &&
                !showAutocomplete &&
                !noneWorks &&
                            <p className="message" dangerouslySetInnerHTML={ { __html: message.success } }/>

            }
            {
                single !== undefined &&
                !single &&
                showAutocomplete &&
                <div>
                    <p>{ content[ 'login.loginpage.forgot_id_search' ] }</p>
                    <div className={ filterFieldClasses }>
                        <label htmlFor="filterText" className="control-label"></label>
                        <input
                            type="text"
                            name="filterText"
                            tabIndex="1"
                            id="filterText"
                            className="gq-input"
                            autoFocus
                            value={ filterText }
                            onChange={ this.handleField }
                            onKeyPress={ this.checkEnter } />
                    </div>
                </div>
            }
            {radioBtns &&
                radioBtns.length < 13 &&
                <div className="radio-wrapper overflow">
                    { radioBtns }
                </div>
            }
            {radioBtns &&
                radioBtns.length >= 13 &&
                <div className="radio-wrapper">
                    { radioBtns }
                </div>
            }
            {!radioBtns &&
                <Alert type={ 'error' } key={ 1 }>
                    {content[ 'login.password.reset.errors.nothing_found' ]}
                </Alert>
            }
            {noneWorks &&
              <p className="help">
                  <a className="noneWorks" tabIndex="3" data-contactId={ cId } data-contactType={ cType } onClick={ this.onSubmit }>
                      <span>{ content[ 'login.account.activation.messages.termed_user_none_of_these_work' ] }</span>
                  </a>
              </p>
            }
            <LoaderButton
                handleClick={ this.onSubmit }
                customClass="btn btn-block btn-primary form-action-btn"
                tabIndex={ 4 }
                disabled={ !this.state.moc.selectedOption ? true : false }
                done={ this.props.done }>
				{ <span dangerouslySetInnerHTML={ { __html: content[ 'system.button.submit' ] } }/> }               
            </LoaderButton>
            <p className="help">
                <a tabIndex="5" onClick={ ( e ) => this.props.toggleModalDisplay( !toggleModal, 'help' ) }>
                    <span>{ content[ 'login.loginpage.help' ] }</span>
                </a>
            </p>
        </NarrowForm>
    );
  }
}


export default MethodOfContact;
