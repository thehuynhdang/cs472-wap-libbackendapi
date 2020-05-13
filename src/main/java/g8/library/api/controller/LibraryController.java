package g8.library.api.controller;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import g8.library.api.common.ErrorMessage;
import g8.library.api.common.ResponseObject;
import g8.library.api.common.ResponseObject.ResponseBuilder;
import g8.library.api.dataaccess.SerializableDataPersistor.SaveMessage;
import g8.library.api.domain.Author;
import g8.library.api.domain.Book;
import g8.library.api.domain.LibraryMember;
import g8.library.api.domain.LoginCredentials;
import g8.library.api.domain.SystemUser;
import g8.library.api.model.BookSearchCriteria;
import g8.library.api.model.SearchUserCriteria;
import g8.library.api.model.UserRolePermission;
import g8.library.api.service.AdvancedSearchInterface;
import g8.library.api.service.LibraryServiceInterface;

@RestController
public class LibraryController {
	private Logger logger = LoggerFactory.getLogger(LibraryController.class);
	
	@Autowired
	private LibraryServiceInterface libraryService;
	
	@Autowired
	private AdvancedSearchInterface searchService;
	
	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/api/book", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject<?>> findBookByISBN(@RequestParam(required = true) String isbn) {
		Book book = libraryService.searchBookByIBSN(isbn);
		if(book == null) {
			ResponseBuilder<?> builder = ResponseBuilder.notFound();
			return new ResponseEntity<ResponseObject<?>>(builder.build(), builder.httpStatus()); 
		}
		
		ResponseBuilder<?> builder = ResponseBuilder.success(book);
		return new ResponseEntity<ResponseObject<?>>(builder.build(), builder.httpStatus());
	}

	@RequestMapping(value = "/api/book/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject<?>> findAllBooks() {
		Collection<Book> books = Optional.ofNullable(libraryService.fetchAllBooks())
				.map(Map::values)
				.orElse(Collections.emptyList());
		
		ResponseBuilder<?> builder = ResponseBuilder.success(books);
		return new ResponseEntity<ResponseObject<?>>(builder.build(), builder.httpStatus());
	}

	@RequestMapping(value = "/api/bookcopy/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject<?>> findAllBookCopies() {
		ResponseBuilder<?> builder = ResponseBuilder.success(libraryService.fetchAllBookCopies());
		return new ResponseEntity<ResponseObject<?>>(builder.build(), builder.httpStatus());
	}
	
	@RequestMapping(value = "/api/auth/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject<?>> login(@RequestBody(required = true) LoginCredentials loginCredentials) {
		SystemUser user = libraryService.login(loginCredentials);
		
		ResponseBuilder<?> builder = null;
		if(user == null) {
			String error = messageSource.getMessage("error.004", null, Locale.ENGLISH);
			builder = new ResponseBuilder<String>(HttpStatus.FORBIDDEN).withSuccess(false);
			builder.newError(new ErrorMessage.ErrorMessageBuilder()
					.withCode("004").withFaultType("E")
					.withMessage(error).build());
		} else {
			UserRolePermission userRolePermission = new UserRolePermission(user);
			builder = ResponseBuilder.success(userRolePermission);
		}
		
		return new ResponseEntity<ResponseObject<?>>(builder.build(), builder.httpStatus());
	}
	
	@RequestMapping(value = "/api/book/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject<?>> searchBook(@RequestParam(required = true) String searchString) {
		if("".equals(searchString))
			return findAllBooks();
		
		ResponseBuilder<?> builder = ResponseBuilder.success(
				searchService.searchBook(new BookSearchCriteria().withSearchString(searchString)));
		return new ResponseEntity<ResponseObject<?>>(builder.build(), builder.httpStatus());
	}
	
	@RequestMapping(value = "/api/member/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject<?>> searchLibraryMember(@RequestParam(required = true) String searchString) {
		if("".equals(searchString))
			return findAllMembers();
		
		ResponseBuilder<?> builder = ResponseBuilder.success(
				searchService.searchLibraryMember(new SearchUserCriteria().withSearchString(searchString)));
		return new ResponseEntity<ResponseObject<?>>(builder.build(), builder.httpStatus());
	}

	@RequestMapping(value = "/api/member", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject<?>> findMemberById(@RequestParam(required = true) String memberId) {
		LibraryMember member = libraryService.searchLibraryMemberById(memberId);
		
		if(member == null) {
			ResponseBuilder<?> builder = ResponseBuilder.notFound();
			return new ResponseEntity<ResponseObject<?>>(builder.build(), builder.httpStatus()); 
		}
		
		ResponseBuilder<?> builder = ResponseBuilder.success(member);
		return new ResponseEntity<ResponseObject<?>>(builder.build(), builder.httpStatus());
	}

	@RequestMapping(value = "/api/member/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject<?>> findAllMembers() {
		Collection<LibraryMember> members = Optional.ofNullable(libraryService.fetchAllMembers())
				.map(Map::values)
				.orElse(Collections.emptyList());
		
		ResponseBuilder<?> builder = ResponseBuilder.success(members);
		return new ResponseEntity<ResponseObject<?>>(builder.build(), builder.httpStatus());
	}
	
	@RequestMapping(value = "/api/author/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject<?>> findAllAuthors() {
		Collection<Author> authors = Optional.ofNullable(libraryService.fetchAllAuthors())
				.map(Map::values)
				.orElse(Collections.emptyList());
		
		ResponseBuilder<?> builder = ResponseBuilder.success(authors);
		return new ResponseEntity<ResponseObject<?>>(builder.build(), builder.httpStatus());
	}
	
	@RequestMapping(value = "/api/checkout/book/{bookIsbn}/member/{memberId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject<?>> checkoutBook(@PathVariable(required = true) String bookIsbn, @PathVariable(required = true) String memberId) {
		ResponseBuilder<String> builder = new ResponseBuilder<String>(HttpStatus.EXPECTATION_FAILED).withSuccess(false);
		
		Book book = libraryService.searchBookByIBSN(bookIsbn);
		if(book == null) {
			String error = messageSource.getMessage("error.005", null, Locale.ENGLISH);
			builder.newError(new ErrorMessage.ErrorMessageBuilder()
					.withCode("005").withFaultType("E").withMessage(error).build());
		}
		
		LibraryMember member = libraryService.searchLibraryMemberById(memberId);
		if(member == null) {
			String error = messageSource.getMessage("error.006", null, Locale.ENGLISH);
			builder.newError(new ErrorMessage.ErrorMessageBuilder()
					.withCode("006").withFaultType("E").withMessage(error).build());
		}
		
		if(!builder.errors().isEmpty()) {
			return new ResponseEntity<ResponseObject<?>>(builder.build(), builder.httpStatus()); 
		}
		
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DAY_OF_MONTH, book.getMaxCheckoutLength());
		
		Date checkoutDate = new Date();
		Date returnDueDate = ca.getTime();
		SaveMessage saveMessage = libraryService.checkoutBook(bookIsbn, memberId, checkoutDate, returnDueDate);
		
		if(!saveMessage.isSuccessed()) {
			String error = messageSource.getMessage("error.002", null, Locale.ENGLISH);
			builder = new ResponseBuilder<String>(HttpStatus.EXPECTATION_FAILED).withSuccess(false);
			builder.newError(new ErrorMessage.ErrorMessageBuilder()
					.withCode("002").withFaultType("E").withMessage(error).build());
		} else {
			builder = new ResponseBuilder<String>(HttpStatus.OK).withSuccess(true);
			builder.newMessage(saveMessage.getMessage());
		}
		
		return new ResponseEntity<ResponseObject<?>>(builder.build(), builder.httpStatus());
	}
	
	@RequestMapping(value = "/api/book/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject<?>> saveBook(@RequestBody(required = true) Book book) {
		SaveMessage saveMessage = libraryService.saveBook(book);
		
		ResponseBuilder<String> builder = null;
		if(!saveMessage.isSuccessed()) {
			String error = messageSource.getMessage("error.003", null, Locale.ENGLISH);
			builder = new ResponseBuilder<String>(HttpStatus.EXPECTATION_FAILED).withSuccess(false);
			builder.newError(new ErrorMessage.ErrorMessageBuilder()
					.withCode("003").withFaultType("E").withMessage(error).build());
			
		} else {
			builder = new ResponseBuilder<String>(HttpStatus.OK).withSuccess(true);
			builder.newMessage(saveMessage.getMessage());
		}
		
		return new ResponseEntity<ResponseObject<?>>(builder.build(), builder.httpStatus());
	}
	
	@RequestMapping(value = "/api/book/addcopy/isbn/{isbn}/nbrcopy/{nbrOfCopies}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject<?>> addBookCopy(@PathVariable(required = true) String isbn, @PathVariable(required = true) int nbrOfCopies) {
		ResponseBuilder<String> builder = new ResponseBuilder<String>(HttpStatus.EXPECTATION_FAILED).withSuccess(false);
		Book book = libraryService.searchBookByIBSN(isbn);
		if(book == null) {
			String error = "Book[" + isbn + "] Not Found in system";
			builder.newError(new ErrorMessage.ErrorMessageBuilder().withMessage(error).build());
			return new ResponseEntity<ResponseObject<?>>(builder.build(), builder.httpStatus());
		}
		
		if(nbrOfCopies < 1) {
			String error = "Invalid. Nrb of copy is less than 1";
			builder.newError(new ErrorMessage.ErrorMessageBuilder().withMessage(error).build());
			return new ResponseEntity<ResponseObject<?>>(builder.build(), builder.httpStatus());
		}
		
		book.makeBookCopies(nbrOfCopies);
		return saveBook(book);
	}
	
	@RequestMapping(value = "/api/book/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject<?>> addNewBook(@RequestBody(required = true) String jsonAddBook) {
		JSONObject jsonSchema = new JSONObject(
			      new JSONTokener(this.getClass().getResourceAsStream("/jsonschema/addbook.json")));
				
	    JSONObject jsonBook = new JSONObject(new JSONTokener(jsonAddBook));
	    
		try {
		    Schema schema = SchemaLoader.load(jsonSchema);
		    schema.validate(jsonBook);
		} catch (ValidationException e) {
			logger.error("Scheme Validate Failed. " + e);
			String error = e.getPointerToViolation() + ": " + e.getErrorMessage();
			
			ResponseBuilder<String> builder = new ResponseBuilder<String>(HttpStatus.EXPECTATION_FAILED).withSuccess(false);
			builder.newError(new ErrorMessage.ErrorMessageBuilder()
					.withFaultType("ValidateFailed").withMessage(error).build());
			return new ResponseEntity<ResponseObject<?>>(builder.build(), builder.httpStatus());
		}
		
		ResponseBuilder<String> builder = new ResponseBuilder<String>(HttpStatus.EXPECTATION_FAILED).withSuccess(false);
		Book newBook = new Book(jsonBook.getString("isbn"), jsonBook.getString("title"), jsonBook.getInt("maxCheckoutLength"));
		newBook.makeBookCopies(jsonBook.getInt("nbrOfCopies"));
		final Map<String, Author> authors = libraryService.fetchAllAuthors();
		for(Object authorId : jsonBook.getJSONArray("authors").toList()) {
			Author author = authors.get(authorId);
			if(author == null) {
				String error = "Author[" + authorId + "] Not Found in system";
				builder.newError(new ErrorMessage.ErrorMessageBuilder().withMessage(error).build());
				return new ResponseEntity<ResponseObject<?>>(builder.build(), builder.httpStatus());
			}
			
			newBook.addNewAuthor(author);
		}
		
		return saveBook(newBook);
	}
}
