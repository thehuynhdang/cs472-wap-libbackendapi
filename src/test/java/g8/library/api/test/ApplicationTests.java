package g8.library.api.test;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

class ApplicationTests {
	private static final String HOST = "http://localhost:8080";
	
	public static void main(String[] args) {
		testFetchAllBooks();
	}

	private static void testFetchAllBooks() {
		RestTemplate restTemplate = new RestTemplate();
		
		String url = HOST + "/api/books/list";
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		HttpEntity<byte[]> httpEntity = new HttpEntity<byte[]>(headers);
		Object books = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Object.class);
		System.out.println(books);
	}
	
}
