public class User{
	private int customer_id;
	private String name;
	private String password;
	private char gender;
	private String dob;
	private String email;
	private String address;
	private String lic_no;
	private String lic_iss_country;
	private String lic_iss_state;
	private String lic_exp_date; 
	
	public User(int customer_id, String name, String password, char gender, String dob, String email, String address, String lic_no, String lic_iss_country, String lic_iss_state, String lic_exp_date){
		this.customer_id=customer_id;
		this.name=name;
		this.password=password;
		this.gender=gender;
		this.dob=dob;
		this.email=email;
		this.address=address;
		this.lic_no=lic_no;
		this.lic_iss_country=lic_iss_country;
		this.lic_iss_state=lic_iss_state;
		this.lic_exp_date=lic_exp_date;
		}
	
	public int getCustomerId() {
		return customer_id;
	}
	public void setCustomerId(int customer_id) {
		this.customer_id = customer_id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public char getGender() {
		return gender;
	}
	public void setGender(char gender) {
		this.gender = gender;
	}

	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}

	public char getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public String getLicenseNumber() {
		return lic_no;
	}
	public void setLicenseNumber(String lic_no) {
		this.lic_no = lic_no;
	}

	public String getLicenseIssueCountry() {
		return lic_iss_country;
	}
	public void setLicenseIssueCountry(String lic_iss_country) {
		this.lic_iss_country = lic_iss_country;
	}

	public String getLicenseIssueState() {
		return lic_iss_state;
	}
	public void setLicenseIssueState(String lic_iss_state) {
		this.lic_iss_state = lic_iss_state;
	}

	public String getLicenseExpDate() {
		return lic_exp_date;
	}
	public void setLicenseExpDate(String lic_exp_date) {
		this.lic_exp_date = lic_exp_date;
	}
}