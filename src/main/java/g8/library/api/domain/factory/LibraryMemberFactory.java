package g8.library.api.domain.factory;

import java.util.Date;

import g8.library.api.dataaccess.SingletoneDataLoader;
import g8.library.api.domain.CheckoutEntry;
import g8.library.api.domain.LibraryMember;

public class LibraryMemberFactory {
	private LibraryMember libraryMember;

	private LibraryMemberFactory(LibraryMember libraryMember) {
		this.libraryMember = libraryMember;
	}

	public static LibraryMemberFactory getInstance(String memberId) {
		LibraryMember member = SingletoneDataLoader.getInstance().getLibraryMember().get(memberId);
		if(member == null) throw new IllegalArgumentException("Lib Member Not Found.");
		return new LibraryMemberFactory(member);
	}
	
	public LibraryMember addNewCheckoutEntry(String bookIsbn, Date checkoutDate, Date returnDueDate) {
		CheckoutEntry checkoutEntry = 
				CheckoutEntryFactory.getInstance(bookIsbn).createNewCheckoutEntry(checkoutDate, returnDueDate);
		libraryMember.addNewCheckoutEntry(checkoutEntry);
		return libraryMember;
	}

}
