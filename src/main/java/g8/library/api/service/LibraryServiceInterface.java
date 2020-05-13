package g8.library.api.service;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import g8.library.api.dataaccess.SerializableDataPersistor.SaveMessage;
import g8.library.api.domain.Author;
import g8.library.api.domain.Book;
import g8.library.api.domain.BookCopy;
import g8.library.api.domain.CheckoutRecord;
import g8.library.api.domain.LibraryMember;
import g8.library.api.domain.LoginCredentials;
import g8.library.api.domain.SystemUser;
import g8.library.api.domain.User;

public interface LibraryServiceInterface {
	public SystemUser login(LoginCredentials credentials) throws RuntimeException;
	
	public SaveMessage addNewLibraryMember(LibraryMember libraryMember) throws RuntimeException;
	
	public SaveMessage checkoutBook(String bookIsbn, String memberId, Date checkoutDate, 
			Date returnDueDate) throws RuntimeException;
	
	public SaveMessage saveUser(User user) throws RuntimeException;
	
	public SaveMessage saveBook(Book book) throws RuntimeException;
	
	public Book searchBookByIBSN(String ibsn);
	
	public CheckoutRecord retrieveCheckoutRecord(String memberId);
	
	public Map<String, LibraryMember> fetchAllMembers();
	
	public Map<String, Book> fetchAllBooks();
	
	public Map<String, Author> fetchAllAuthors();
	
	public Set<BookCopy> fetchAllBookCopies();

	public LibraryMember searchLibraryMemberById(String memberId);
	
}
