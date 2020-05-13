package g8.library.api.service.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import g8.library.api.dataaccess.SingletoneDataLoader;
import g8.library.api.domain.Book;
import g8.library.api.domain.LibraryMember;
import g8.library.api.domain.SystemUser;
import g8.library.api.model.BookSearchCriteria;
import g8.library.api.model.SearchUserCriteria;
import g8.library.api.service.AdvancedSearchInterface;

@Service
public class AdvancedSearchImpl implements AdvancedSearchInterface {

	@Override
	public Set<SystemUser> searchSystemUser(SearchUserCriteria criteria) {
		Set<SystemUser> results = new HashSet<SystemUser>();
		
		//usename //firstname //lastname //phone
		Map<String, SystemUser> users = SingletoneDataLoader.getInstance().getSystemUsers();
		String searchString = criteria.searchString().toLowerCase();
		for(SystemUser u : users.values()) {
			if(u.getUserName().toLowerCase().contains(searchString) 
					|| u.getFirstName().toLowerCase().contains(searchString)
					|| u.getFirstName().toLowerCase().contains(searchString)
					|| u.getPhoneNumber().contains(searchString)) {
				results.add(u);
			}
			
		}
		return results;
	}

	@Override
	public Set<Book> searchBook(BookSearchCriteria criteria) {
		Set<Book> results = new HashSet<Book>();
		
		Map<String, Book> books = SingletoneDataLoader.getInstance().getBooks();
		
		for(String isbn : books.keySet()) {
			//by isbn //by title //by Author name
			String searchString = criteria.searchString().toLowerCase();
			if(isbn.toLowerCase().contains(searchString) 
					|| books.get(isbn).getTitle().toLowerCase().contains(searchString)
					|| books.get(isbn).getAuthors().stream().anyMatch(
							p -> p.getFirstName().toLowerCase().contains(searchString) 
							|| p.getLastName().toLowerCase().contains(searchString)))
				results.add(books.get(isbn));
			
		}
		
		return results;
	}

	@Override
	public Set<LibraryMember> searchLibraryMember(SearchUserCriteria criteria) {
		Set<LibraryMember> results = new HashSet<LibraryMember>();
		
		//memberId //firstname //lastname //phone
		Map<String, LibraryMember> members = SingletoneDataLoader.getInstance().getLibraryMember();
		String searchString = criteria.searchString().toLowerCase();
		for(LibraryMember u : members.values()) {
			if(u.getMemberId().toLowerCase().contains(searchString)
					|| u.getFirstName().toLowerCase().contains(searchString)
					|| u.getFirstName().toLowerCase().contains(searchString)
					|| u.getPhoneNumber().contains(searchString)) {
				results.add(u);
			}
			
		}
		return results;
	}

}
