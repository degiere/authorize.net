package net.degiere.authorizenet;

import net.authorize.xml.Message;

public class AuthorizeNetRuntimeException extends RuntimeException {
	
	Message resultMessage;
	
	public AuthorizeNetRuntimeException(Message message) {
		this.resultMessage = message;
	}

	public Message getResultMessage() {
    	return resultMessage;
    }
	
	@Override
	public String getMessage() {
		return this.resultMessage.getText();
	}

}
