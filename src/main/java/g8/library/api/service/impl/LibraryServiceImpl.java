package g8.library.api.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import g8.library.api.dataaccess.SerializableDataPersistor;
import g8.library.api.dataaccess.SerializableDataPersistor.SaveMessage;
import g8.library.api.dataaccess.SingletoneDataLoader;
import g8.library.api.dataaccess.storage.StorageType;
import g8.library.api.domain.Author;
import g8.library.api.domain.Book;
import g8.library.api.domain.BookCopy;
import g8.library.api.domain.CheckoutRecord;
import g8.library.api.domain.LibraryMember;
import g8.library.api.domain.LoginCredentials;
import g8.library.api.domain.SystemUser;
import g8.library.api.domain.User;
import g8.library.api.domain.factory.LibraryMemberFactory;
import g8.library.api.service.LibraryServiceInterface;

@Service
public class LibraryServiceImpl implements LibraryServiceInterface {

	@Override
	public SystemUser login(LoginCredentials credentials) throws RuntimeException {
		for(SystemUser u : SingletoneDataLoader.getInstance().getSystemUsers().values()) {
			if(u.login(credentials) != null) return u;
		}
		return null;
	}

	@Override
	public SaveMessage addNewLibraryMember(LibraryMember libraryMember) throws RuntimeException {
		SingletoneDataLoader.getInstance().getLibraryMember().put(libraryMember.getMemberId(), libraryMember);
		return new SerializableDataPersistor<Map<String, LibraryMember>>(
				StorageType.MEMBERS, SingletoneDataLoader.getInstance().getLibraryMember()).save();
	}

	@Override
	public SaveMessage checkoutBook(String bookIsbn, String memberId, Date checkoutDate, Date returnDueDate)
			throws RuntimeException {
		
		SaveMessage saveMessage = new SaveMessage();
		try {
			LibraryMember member = LibraryMemberFactory.getInstance(memberId).addNewCheckoutEntry(
					bookIsbn, checkoutDate, returnDueDate);
			
			// update and save book
			saveMessage = new SerializableDataPersistor<Map<String, Book>>(
					StorageType.BOOKS, SingletoneDataLoader.getInstance().getBooks()).save();
			
			//save checkout record
			if(saveMessage.isSuccessed()) {
				SingletoneDataLoader.getInstance().getLibraryMember().put(member.getMemberId(), member);
				saveMessage = new SerializableDataPersistor<Map<String, LibraryMember>>(
						StorageType.MEMBERS, SingletoneDataLoader.getInstance().getLibraryMember()).save();
			}
		} catch (Exception e) {
			saveMessage.setSuccessed(false);
			saveMessage.setE(e);
			saveMessage.getErrors().add("Bad Data. Checkout Failed.");
		}
		
		return saveMessage;
	}

	@Override
	public SaveMessage saveUser(User user) throws RuntimeException {
		
		if(user instanceof LibraryMember) {
			LibraryMember member = (LibraryMember)user;
			SingletoneDataLoader.getInstance().getLibraryMember().put(member.getMemberId(), member);
			return new SerializableDataPersistor<Map<String, LibraryMember>>(
					StorageType.MEMBERS, SingletoneDataLoader.getInstance().getLibraryMember()).save();
		}
		
		SystemUser systemUser = (SystemUser)user;
		SingletoneDataLoader.getInstance().getSystemUsers().put(systemUser.getUserName(), systemUser);
		return new SerializableDataPersistor<Map<String, SystemUser>>(
				StorageType.USERS, SingletoneDataLoader.getInstance().getSystemUsers()).save();
	}

	@Override
	public Book searchBookByIBSN(String ibsn) {
		return SingletoneDataLoader.getInstance().getBooks().get(ibsn);
	}

	@Override
	public CheckoutRecord retrieveCheckoutRecord(String memberId) {
		LibraryMember member = SingletoneDataLoader.getInstance().getLibraryMember().get(memberId);
		if(member != null) return member.getCheckoutRecord();
		return null;
	}

	@Override
	public Map<String, LibraryMember> fetchAllMembers() {
		return SingletoneDataLoader.getInstance().getLibraryMember();
	}

	@Override
	public Map<String, Book> fetchAllBooks() {
		return SingletoneDataLoader.getInstance().getBooks();
	}

	@Override
	public SaveMessage saveBook(Book book) throws RuntimeException {
		SingletoneDataLoader.getInstance().getBooks().put(book.getIsbn(), book);
		return new SerializableDataPersistor<Map<String, Book>>(
				StorageType.BOOKS, SingletoneDataLoader.getInstance().getBooks()).save();
	}

	@Override
	public Set<BookCopy> fetchAllBookCopies() {
		Set<BookCopy> bookCopies = new HashSet<BookCopy>();
		Map<String, Book> books = SingletoneDataLoader.getInstance().getBooks();
		for(String isbn : books.keySet()) {
			Book book = books.get(isbn);
			if(book != null) {
				bookCopies.addAll(book.getCopies());
			}
		}
		
		return bookCopies;
	}

	@Override
	public LibraryMember searchLibraryMemberById(String memberId) {
		return SingletoneDataLoader.getInstance().getLibraryMember().get(memberId);
	}

	@Override
	public Map<String, Author> fetchAllAuthors() {
		return SingletoneDataLoader.getInstance().getAuthors();
	}
}
