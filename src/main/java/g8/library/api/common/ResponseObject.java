package g8.library.api.common;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(value = { "httpStatus" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseObject<T> {
	private final Set<ErrorMessage> errors;
	private final Set<String> messages;
	private final transient HttpStatus httpStatus;
	private final boolean success;
	private final T data;

	private ResponseObject(ResponseBuilder<T> builder) {
		this.errors = builder.errors;
		this.messages = builder.messages;
		this.httpStatus = builder.httpStatus;
		this.success = builder.success;
		this.data = builder.data;
	}
	
	public Set<ErrorMessage> getErrors() {
		return errors;
	}

	public Set<String> getMessages() {
		return messages;
	}

	public T getData() {
		return data;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public boolean isSuccess() {
		return success;
	}

	@Override
	public String toString() {
		return "ResponseObject [errors=" + errors + ", messages=" + messages + ", success=" + success + ", data=" + data
				+ "]";
	}

	public static class ResponseBuilder<T> {
		private HttpStatus httpStatus;
		private T data;
		private Set<ErrorMessage> errors;
		private Set<String> messages;
		private boolean success;

		public ResponseBuilder(HttpStatus httpStatus) {
			this.httpStatus = httpStatus;
			this.errors = new HashSet<ErrorMessage>();
			this.messages = new HashSet<String>();
		}

		public ResponseBuilder<T> withMessages(Set<String> messages) {
			this.messages = messages;
			return this;
		}

		public ResponseBuilder<T> withData(T data) {
			this.data = data;
			return this;
		}

		public T data() {
			return this.data;
		}

		public ResponseBuilder<T> withErrors(Set<ErrorMessage> errors) {
			this.errors = errors;
			return this;
		}

		public ResponseBuilder<T> newError(ErrorMessage error) {
			this.errors.add(error);
			return this;
		}

		public ResponseBuilder<T> newMessage(String message) {
			this.messages.add(message);
			return this;
		}

		public ResponseBuilder<T> withSuccess(boolean success) {
			this.success = success;
			return this;
		}

		public Set<String> messages() {
			return this.messages;
		}

		public Set<ErrorMessage> errors() {
			return this.errors;
		}

		public boolean success() {
			return this.success;
		}

		public ResponseBuilder<T> withHttpStatus(HttpStatus httpStatus) {
			this.httpStatus = httpStatus;
			return this;
		}

		public HttpStatus httpStatus() {
			return this.httpStatus;
		}

		public ResponseObject<T> build() {
			return new ResponseObject<T>(this);
		}

		public static <T> ResponseBuilder<T> success(T data) {
			ResponseBuilder<T> builder = new ResponseBuilder<T>(HttpStatus.OK);
			builder.withSuccess(true);
			builder.withData(data);
			return builder;
		}
		
		public static ResponseBuilder<String> notFound() {
			ResponseBuilder<String> builder = new ResponseBuilder<String>(HttpStatus.OK);
			builder.withSuccess(true);
			builder.newError(
					new ErrorMessage.ErrorMessageBuilder()
					.withCode("001")
					.withFaultType("E")
					.withMessage("Record Not Found").build());
			return builder;
		}
		
		public <E> ResponseBuilder<E> castResponseData(Class<E> type) throws ClassCastException {
			ResponseBuilder<E> builder = new ResponseBuilder<E>(httpStatus);
			builder.withErrors(errors);
			builder.withSuccess(success);
			builder.withData(type.cast(data));
			return builder;
		}

		@Override
		public String toString() {
			return "ResponseBuilder [httpStatus=" + httpStatus + ", data=" + data + ", errors=" + errors + ", success="
					+ success + "]";
		}

	}

}
