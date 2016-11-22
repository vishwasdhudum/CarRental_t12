public class Reservation{
	private String booking_id;
	private int customer_id;
	private String booking_status;
	private float booking_amount;
	private String booking_date;
	private float discount;
	private String vehical_id;
	private String pick_loc;
	private String drop_loc;
	private String pick_date;
	private String drop_date;
	
	public Reservation(String booking_id, int customer_id, String booking_status, float booking_amount, String booking_date, float discount, 
		String vehical_id, String pick_loc, String drop_loc, String pick_date, String drop_date){
		this.booking_id=booking_id;
		this.customer_id=customer_id;
		this.booking_status=booking_status;
		this.booking_amount=booking_amount;
		this.booking_date=booking_date;
		this.discount=discount;
		this.vehical_id=vehical_id;
		this.pick_loc=pick_loc;
		this.drop_loc=drop_loc;
		this.pick_date=pick_date;
		this.drop_date=drop_date;
		}
	
	public String getBookingId() {
		return booking_id;
	}
	public void setBookingId(String booking_id) {
		this.booking_id = booking_id;
	}

	public int getCustomerId() {
		return customer_id;
	}
	public void setCustomerId(int customer_id) {
		this.customer_id = customer_id;
	}

	public String getStatus() {
		return booking_status;
	}
	public void setStatus(String booking_status) {
		this.booking_status = booking_status;
	}

	public float getBookingAmount() {
		return booking_amount;
	}
	public void setBookingAmount(float booking_amount) {
		this.booking_amount = booking_amount;
	}

	public String getBookingDate() {
		return booking_date;
	}
	public void setBookingDate(String booking_date) {
		this.booking_date = booking_date;
	}

	public float getDiscount() {
		return discount;
	}
	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public String getVehicalId() {
		return vehical_id;
	}
	public void setVehicalId(String vehical_id) {
		this.vehical_id = vehical_id;
	}

	public String getPickupLoc() {
		return pick_loc;
	}
	public void setPickupLoc(String pick_loc) {
		this.pick_loc = pick_loc;
	}

	public String getDropLoc() {
		return drop_loc;
	}
	public void setDropLoc(String drop_loc) {
		this.drop_loc = drop_loc;
	}

	public String getPickupDate() {
		return pick_date;
	}
	public void setPickupDate(String pick_date) {
		this.pick_date = pick_date;
	}

	public String getDropDate() {
		return drop_date;
	}
	public void setDropDate(String drop_date) {
		this.drop_date = drop_date;
	}
}