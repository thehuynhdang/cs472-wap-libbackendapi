package g8.library.api.domain;

import java.io.Serializable;
import java.util.Date;

public final class CheckoutEntry implements Serializable {
	private static final long serialVersionUID = 1L;
	private final BookCopy book;
	private final Date checkoutDate;
	private final Date returnDueDate;
	
	private CheckoutEntry(BookCopy book, Date checkoutDate, Date returnDueDate) {
		this.book = book;
		this.checkoutDate = checkoutDate;
		this.returnDueDate = returnDueDate;
	}
	
	public static CheckoutEntry newCheckoutEntry(BookCopy book, Date checkoutDate, Date returnDueDate) {
		if(book == null) throw new IllegalArgumentException("Not Allowed to create CheckoutEntry.");
		return new CheckoutEntry(book, checkoutDate, returnDueDate);
	}
	
	public BookCopy getBook() {
		return book;
	}

	public Date getCheckoutDate() {
		return checkoutDate;
	}
	public Date getReturnDueDate() {
		return returnDueDate;
	}

	public boolean isOverDue() {
		return checkoutDate != null && getReturnDueDate().before(new Date());
	}

	@Override
	public String toString() {
		return "CheckoutEntry [isbn=" + book.getIsbn() + ", checkoutDate=" + checkoutDate + ", returnDueDate=" + returnDueDate
				+ "]";
	}
	
}
