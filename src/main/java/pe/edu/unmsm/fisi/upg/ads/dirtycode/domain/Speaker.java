package pe.edu.unmsm.fisi.upg.ads.dirtycode.domain;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.Arrays;
import java.util.List;

import pe.edu.unmsm.fisi.upg.ads.dirtycode.exceptions.NoSessionsApprovedException;
import pe.edu.unmsm.fisi.upg.ads.dirtycode.exceptions.SpeakerDoesntMeetRequirementsException;

public class Speaker {
	private String firstName;
	private String lastName;
	private String email;
	private int yearsExperience;
	private boolean hasBlog;
	private String blogURL;
	private WebBrowser browser;
	private List<String> certifications;
	private String employer;
	private int registrationFee;
	private List<Session> sessions;
	
	static final int[] MINIMUM_YEARS_EXPERIENCE = 	{0		,2		,4		,6};
	static final int[] MAXIMUM_YEARS_EXPERIENCE = 	{1		,3		,5		,9};
	static final int[] REGISTRATION_FEE_VALUE =		{500	,250	,100	,50};

	public Integer register(IRepository repository) throws Exception {		
		Integer speakerId = null;				
		this.validateRegistration();		
		this.calculateRegistrationFee();
		speakerId =this.saveSpeakerToDB(repository);
		
		return speakerId;		
	}

	public void validateRegistration()  throws Exception   {		
		this.validateDataEmptyAndSession();		
		if(this.hasRequirementsComplete() || !this.isInDomainOrBrowser() ) {
			this.approvedSession();
		}else {
			throw new SpeakerDoesntMeetRequirementsException(
					"Speaker doesn't meet our abitrary and capricious standards.");
		}			
	}
	
	public void validateDataEmptyAndSession()  {
		if (this.firstName.isEmpty())
			throw new IllegalArgumentException("First Name is required");
		if (this.lastName.isEmpty())
			throw new IllegalArgumentException("Last name is required.");
		if (this.email.isEmpty())
			throw new IllegalArgumentException("Email is required.");
		if (this.sessions.size() == 0) 
			throw new IllegalArgumentException("Can't register speaker with no sessions to present.");
	}
	
	public boolean hasRequirementsComplete() {
		if (this.yearsExperience > 10) return true;
		if (this.hasBlog) return true;
		if (this.certifications.size() > 3) return true;
		if (this.isInEmployerList()) return true;
		
		return false;		
	}
	
	public boolean isInEmployerList() {
		List<String> employerList = Arrays.asList("Pluralsight", "Microsoft", "Google", "Fog Creek Software", "37Signals","Telerik");
		return employerList.contains(this.getEmployer());
	}
			
	public boolean isInDomainOrBrowser() {
		return this.isInDomainList() || this.isBrowserValid();
	}	
	public boolean isInDomainList() {
		// need to get just the domain from the email
		String[] splitted = this.getEmail().split("@");
		String emailDomain = splitted[splitted.length - 1];
		List<String> domainList = Arrays.asList("aol.com", "hotmail.com", "prodigy.com", "compuserve.com");
		
		return domainList.contains(emailDomain);								
	}
	
	public boolean isBrowserValid() {		
		return browser.getName() == WebBrowser.BrowserName.InternetExplorer	&& browser.getMajorVersion() < 9;
	}
	
	
	public void approvedSession()  throws Exception  {			
		
		String[] technologies = new String[] { "Cobol", "Punch Cards", "Commodore", "VBScript" };
		boolean isApprovedSession = false;
				
		for (Session session : this.getSessions()) {
			for (String tech : technologies) {
				if (session.getTitle().contains(tech) || session.getDescription().contains(tech)) {
					session.setApproved(false);
					break;
				} else {
					session.setApproved(true);		
					isApprovedSession = true;
				}
			}
		}
		if(!isApprovedSession) throw new NoSessionsApprovedException("No sessions approved.");
	}	
	
	
	public void  calculateRegistrationFee() {
		
		int registrationFee = 0;
		for(int i=0; i < MINIMUM_YEARS_EXPERIENCE.length; i++) {
			if(MINIMUM_YEARS_EXPERIENCE[i] <= this.getYearsExperience() && this.getYearsExperience() <= MAXIMUM_YEARS_EXPERIENCE[i]) {
				registrationFee = REGISTRATION_FEE_VALUE[i];				
				break;
			}
		}
		this.setRegistrationFee(registrationFee);			
	}	
	
	public Integer saveSpeakerToDB(IRepository repository) {
		// Now, save the speaker and sessions to the db.
		Integer speakerId = null;
		try {
			speakerId = repository.saveSpeaker(this);
		} catch (Exception e) {
			// in case the db call fails
		}
		
		return speakerId;		
	}

	
	public List<Session> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	

	public int getYearsExperience() {
		return yearsExperience;
	}

	public void setYearsExperience(int yearsExperience) {
		this.yearsExperience = yearsExperience;
	}

	public boolean isHasBlog() {
		return hasBlog;
	}

	public void setHasBlog(boolean hasBlog) {
		this.hasBlog = hasBlog;
	}

	public String getBlogURL() {
		return blogURL;
	}

	public void setBlogURL(String blogURL) {
		this.blogURL = blogURL;
	}

	public WebBrowser getBrowser() {
		return browser;
	}

	public void setBrowser(WebBrowser browser) {
		this.browser = browser;
	}

	public List<String> getCertifications() {
		return certifications;
	}

	public void setCertifications(List<String> certifications) {
		this.certifications = certifications;
	}

	public String getEmployer() {
		return employer;
	}

	public void setEmployer(String employer) {
		this.employer = employer;
	}

	public int getRegistrationFee() {
		return registrationFee;
	}

	public void setRegistrationFee(int registrationFee) {
		this.registrationFee = registrationFee;
	}
}