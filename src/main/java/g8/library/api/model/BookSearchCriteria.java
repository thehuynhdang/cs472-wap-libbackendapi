package g8.library.api.model;

public class BookSearchCriteria {
	private String searchString;
	
	public BookSearchCriteria withSearchString(String searchString) {
		this.searchString = searchString;
		return this;
	}
	
	public String searchString() {
		return this.searchString;
	}

}
