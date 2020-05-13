package g8.library.api.dataaccess;

import java.util.HashMap;
import java.util.Map;

import g8.library.api.dataaccess.SerializableDataPersistor.SaveMessage;
import g8.library.api.dataaccess.storage.StorageType;
import g8.library.api.domain.Address;
import g8.library.api.domain.Author;
import g8.library.api.domain.Book;
import g8.library.api.domain.LibraryMember;
import g8.library.api.domain.LoginCredentials;
import g8.library.api.domain.PermissionType;
import g8.library.api.domain.SystemUser;
import g8.library.api.domain.UserRole;
import g8.library.api.domain.UserRoleType;

public class DataCleanOneTimeCreator {
	public final static DataCleanOneTimeCreator instance = new DataCleanOneTimeCreator();
	
	static Map<UserRoleType, UserRole> createUserRoles() {
		Map<UserRoleType, UserRole> userRoles = new HashMap<UserRoleType, UserRole>();
		UserRole role = new UserRole(UserRoleType.ADMIN);
		role.addNewAccessPermission(PermissionType.UPDATE_MEMBER);
		role.addNewAccessPermission(PermissionType.ADD_BOOK);
		role.addNewAccessPermission(PermissionType.DELETE_MEMBER);
		role.addNewAccessPermission(PermissionType.ADD_MEMBER);
		userRoles.put(UserRoleType.ADMIN, role);
		
		role = new UserRole(UserRoleType.LIBRARIAN);
		role.addNewAccessPermission(PermissionType.CHECKOUT_BOOK);
		userRoles.put(UserRoleType.LIBRARIAN, role);
		
		return userRoles;
	}
	
	static Map<String, SystemUser> createSystemUsers(Map<UserRoleType, UserRole> userRoles) {
		Map<String, SystemUser> systemUsers = new HashMap<String, SystemUser>();
		SystemUser admin = new SystemUser(new LoginCredentials("admin", "12345"), 
				"Admin", "Admin", "319-111-222", new Address("1311 2nd street", "Fairfield", "IA", 525567));
		admin.addNewRole(userRoles.get(UserRoleType.ADMIN));
		systemUsers.put(admin.getUserName(), admin);
		
		SystemUser librarian = new SystemUser(new LoginCredentials("librarian", "12345"), 
				"Librarian", "Librarian", "319-111-333", new Address("1311 4nd street", "Fairfield", "IA", 525567));
		librarian.addNewRole(userRoles.get(UserRoleType.LIBRARIAN));
		systemUsers.put(librarian.getUserName(), librarian);
		
		SystemUser superAdmin = new SystemUser(new LoginCredentials("superadmin", "12345"), 
				"Super", "Admin", "319-111-444", new Address("1311 5nd street", "Fairfield", "IA", 525567));
		superAdmin.addNewRole(userRoles.get(UserRoleType.LIBRARIAN));
		superAdmin.addNewRole(userRoles.get(UserRoleType.ADMIN));
		systemUsers.put(superAdmin.getUserName(), superAdmin);
		
		return systemUsers;
	}
	
	static Map<String, Book> createBooks() {
		Map<String, Book> books = new HashMap<String, Book>();
		
		Book book = new Book("ISBN1", "Java Core 8", 30);
		book.addNewAuthor(new Author("001", "BIO 111", "Jone", "Kathy", "(333) 123-1234", new Address("2224 Arlington street", "San Jose", "CA", 52000)));
		book.makeABookCopy(1).makeABookCopy(2).makeABookCopy(3).makeABookCopy(4);
		books.put(book.getIsbn(), book);
		
		book = new Book("ISBN2", "Java Core 11", 30);
		book.addNewAuthor(new Author("002", "BIO 112", "Jone", "Kathy", "(703) 123-3344", new Address("1231 Backlick street", "Springfield", "VA", 55576)));
		book.makeABookCopy(1).makeABookCopy(2).makeABookCopy(3).makeABookCopy(4);
		books.put(book.getIsbn(), book);
		
		book = new Book("ISBN3", "Java Core 9", 30);
		book.addNewAuthor(new Author("003", "BIO 113", "Jone", "Kathy", "(319) 443-4412", new Address("1311 7nd street", "Fairfield", "IA", 525567)));
		book.makeABookCopy(1).makeABookCopy(2).makeABookCopy(3).makeABookCopy(4);
		books.put(book.getIsbn(), book);
		
		return books;
	}
	
	static Map<String, LibraryMember> createLibraryMembers() {
		Map<String, LibraryMember> members = new HashMap<String, LibraryMember>();
		LibraryMember member = new LibraryMember("spring201", "John", "Tran", "(319) 123-4412", new Address("001 5nd street", "Fairfield", "IA", 525567));
		members.put(member.getMemberId(), member);
		
		member = new LibraryMember("spring202", "Anna", "Smith", "(319) 123-4413", new Address("555 2nd street", "Fairfield", "IA", 525567));
		members.put(member.getMemberId(), member);
		
		return members;
	}
	
	static Map<String, Author> createAuthors() {
		Map<String, Author> authors = new HashMap<>();
		Author author = new Author("001", "Ralph Johnson (computer scientist) (born 1955), computer science professor at the University of Illinois at Urbana-Champaign", "Ralph", "Johnson", "2234234343", null);
		authors.put(author.getId(), author);
		
		author = new Author("002", "Erich Gamma (born 1961 in Z�rich) is a Swiss computer scientist.", "Erich", "Gamma", "656456456", null);
		authors.put(author.getId(), author);
		
		author = new Author("003", "Mark Cao (computer scientist) is a computer scientist.", "Mark", "Cao", "(123) 223-432", null);
		authors.put(author.getId(), author);
		
		return authors;
	}
	
	public static void main(String[] args) {
		Map<UserRoleType, UserRole> userRoles = createUserRoles();
		SaveMessage saveMessage = new SerializableDataPersistor<Map<String, SystemUser>>(StorageType.USERS, createSystemUsers(userRoles)).save();
		System.out.println(saveMessage);
		
		saveMessage = new SerializableDataPersistor<Map<String, LibraryMember>>(StorageType.MEMBERS, createLibraryMembers()).save();
		System.out.println(saveMessage);
		
		saveMessage = new SerializableDataPersistor<Map<String, Book>>(StorageType.BOOKS, createBooks()).save();
		System.out.println(saveMessage);
		
		saveMessage = new SerializableDataPersistor<Map<String, Author>>(StorageType.AUTHORS, createAuthors()).save();
		System.out.println(saveMessage);
		
	}

}