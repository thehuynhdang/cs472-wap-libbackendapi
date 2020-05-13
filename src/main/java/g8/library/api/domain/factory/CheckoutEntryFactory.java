package g8.library.api.domain.factory;

import java.util.Date;
import java.util.Optional;

import g8.library.api.dataaccess.SingletoneDataLoader;
import g8.library.api.domain.Book;
import g8.library.api.domain.BookCopy;
import g8.library.api.domain.CheckoutEntry;

public class CheckoutEntryFactory {
	private BookCopy book;
	
	private CheckoutEntryFactory(BookCopy book) {
		this.book = book;
	}
	
	public static CheckoutEntryFactory getInstance(String bookIsbn) {
		Book book = SingletoneDataLoader.getInstance().getBooks().get(bookIsbn);
		Optional<BookCopy> copy = null;
		if(book != null) {
			copy = book.getCopies().stream().filter(p -> p.isAvailable()).findFirst();
			if(!copy.isPresent())
				throw new IllegalArgumentException("Book not found.");
		}
			
		return new CheckoutEntryFactory(copy.get());
	}
	
	public CheckoutEntry createNewCheckoutEntry(Date checkoutDate, Date returnDueDate) {
		this.book.setAvailable(false);
		return CheckoutEntry.newCheckoutEntry(book, checkoutDate, returnDueDate);
	}

}
