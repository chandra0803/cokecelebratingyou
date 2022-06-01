import React from 'react';

import NarrowForm from '../../../../shared/narrow-form/narrow-form';
import { Alert } from 'component-library';

import '../../loginReact.scss';




class MessageWindow extends React.Component {

    constructor( props ) {
        super( props );
        this.state = {
            username: '',
            email: '',
            focusedField: ''
        };
        this.termedUseronSubmit = this.termedUseronSubmit.bind( this );
    }
    termedUseronSubmit = ( e ) => {
      const user = false;
      const { activContMethUrl } = this.props.login;
      const contact = {
        contactId: window.login.termedUserId,
        contactType: 'NONE'
      };
      this.props.sendContact( contact, activContMethUrl, user );      
    }
  render() {
    const termedUserToolTip = window.login.termedUserToolTip;
    const {
        message,
        toggleModal
    } = this.props.login;
    const {
        content
    } = this.props;
    const clientLogo = '';
    return (
        <NarrowForm page="login" clientLogo={ clientLogo } >
            <p>
                <a tabIndex="5" onClick={ ( e ) => this.props.handleFormChange( 'login' ) }>
                    <span><i className="icon-arrow-1-circle-left"></i> { content[ 'login.loginpage.back_to_login' ] }</span>
                </a>
            </p>
            {this.props.forgotId.details &&                
				<h3 className="no-bottom no-alert-bottom" dangerouslySetInnerHTML={ { __html: content[ 'login.loginpage.message_sent' ] } }/>
            }
            {!this.props.forgotId.details &&
                <h3 className="no-bottom no-alert-bottom" dangerouslySetInnerHTML={ { __html: content[ 'login.loginpage.check_your_email' ] } }/>
            }
            {
                message &&
                message.error &&
                        <Alert type={ 'error' } key={ message.id }>
                            <p dangerouslySetInnerHTML={ { __html: message.error } }/>
                        </Alert>

            }
            {
                message &&
                message.success && !termedUserToolTip &&
                        <Alert className="no-bottom" type={ 'success' } key={ message.id }>
                            <p dangerouslySetInnerHTML={ { __html: message.success } }/>
                        </Alert>

            }
            {
                message &&
                message.success && termedUserToolTip &&
                        <Alert className="no-bottom" type={ 'success' } key={ message.id }>
                            <p dangerouslySetInnerHTML={ { __html: message.success } }/>
                            <div className="moreInfo">
                              <span className="userId">
                                <span className="icon-info"></span>
                                <span className="instructions">{ content[ 'login.account.activation.messages.termed_user_email_tool_tip' ] }</span>
                              </span>
                              <a className="noneWorks" onClick={ this.termedUseronSubmit }>
                                <span>{ content[ 'login.account.activation.messages.termed_user_did_not_get' ] }</span>
                              </a>
                            </div>
                        </Alert>

            }
            <p className="help">
                <a tabIndex="3" onClick={ ( e ) => this.props.toggleModalDisplay( !toggleModal, 'help' ) }>
                    <span>{ content[ 'login.loginpage.help' ] }</span>
                </a>
            </p>
        </NarrowForm>
    );
  }
}


export default MessageWindow;
