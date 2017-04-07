package tesla.cposcanner.emailController;

import javax.inject.Inject;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import tesla.cposcanner.props.EmailProperties;

public class EmailController {
	
	private final EmailProperties emailProperties;
	
	@Inject
	public EmailController(final EmailProperties emailProperties){
		this.emailProperties = emailProperties;
	}

	public void sendEmail(String data) throws EmailException{
		final Email email = buildEmail();
		
		email.setSubject("New Tesla CPO Model Found! \n");
		email.setMsg(data);
		email.addTo(emailProperties.getReceiverAddress());
		email.send();
	}
	
	private Email buildEmail() throws EmailException{
		final Email email = new SimpleEmail();
		email.setHostName(emailProperties.getHost());
		email.setStartTLSEnabled(emailProperties.getIsTLS());
		email.setAuthenticator(new DefaultAuthenticator(emailProperties.getSenderAddress(), emailProperties.getPassword()));
		email.setSSLOnConnect(emailProperties.getIsSSL());
		email.setFrom(emailProperties.getSenderAddress());
		return email;
	}
}
