package g8.library.api.service;

import java.util.Set;

import g8.library.api.domain.Book;
import g8.library.api.domain.LibraryMember;
import g8.library.api.domain.SystemUser;
import g8.library.api.model.BookSearchCriteria;
import g8.library.api.model.SearchUserCriteria;

public interface AdvancedSearchInterface {
	public Set<SystemUser> searchSystemUser(SearchUserCriteria criteria);
	public Set<LibraryMember> searchLibraryMember(SearchUserCriteria criteria);
	public Set<Book> searchBook(BookSearchCriteria criteria);
}
