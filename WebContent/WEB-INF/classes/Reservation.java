public class Reservation{
	private String booking_id;
	private int customer_id;
	private String status;
	private float bookingAmount;
	private String bookingDate;
	private float discount;
	private String vehicalId;
	private String pickup;
	private String drop;
	private String pickupDate;
	private String dropDate;
	
	public Reservation(String booking_id, int customer_id, String status, float bookingAmount, String bookingDate, float discount, String vehicalId, String pickup, String drop, String pickupDate, String dropDate){
		this.booking_id=booking_id;
		this.customer_id=customer_id;
		this.status=status;
		this.bookingAmount=bookingAmount;
		this.bookingDate=bookingDate;
		this.discount=discount;
		this.vehicalId=vehicalId;
		this.pickup=pickup;
		this.drop=drop;
		this.pickupDate=pickupDate;
		this.dropDate=dropDate;
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
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public float getBookingAmount() {
		return bookingAmount;
	}
	public void setBookingAmount(float bookingAmount) {
		this.bookingAmount = bookingAmount;
	}

	public String getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}

	public float getDiscount() {
		return discount;
	}
	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public String getVehicalId() {
		return vehicalId;
	}
	public void setVehicalId(String vehicalId) {
		this.vehicalId = vehicalId;
	}

	public String getPickupLoc() {
		return pickup;
	}
	public void setPickupLoc(String pickup) {
		this.pickup = pickup;
	}

	public String getDropLoc() {
		return drop;
	}
	public void setDropLoc(String drop) {
		this.drop = drop;
	}

	public String getPickupDate() {
		return pickupDate;
	}
	public void setPickupDate(String pickupDate) {
		this.pickupDate = pickupDate;
	}

	public String getDropDate() {
		return dropDate;
	}
	public void getDropDate(String dropDate) {
		this.dropDate = dropDate;
	}
}