package g8.library.api.common;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorMessage {
	private final String code;
	private final String faultType;
	private final String message;
	
	private ErrorMessage(ErrorMessageBuilder builder) {
		this.code = builder.code;
		this.faultType = builder.faultType;
		this.message = builder.message;
		
	}
	
	public String getCode() {
		return code;
	}

	public String getFaultType() {
		return faultType;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, faultType, message);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		return Objects.equals(this, obj);
	}

	@Override
	public String toString() {
		return "Message [code=" + code + ", faultType=" + faultType + ", message=" + message + "]";
	}


	public static class ErrorMessageBuilder {
		private String code;
		private String faultType;
		private String message;
		
		public ErrorMessageBuilder withCode(String code) {
			this.code = code;
			return this;
		}
		
		public ErrorMessageBuilder withFaultType(String faultType) {
			this.faultType = faultType;
			return this;
		}
		
		public ErrorMessageBuilder withMessage(String message) {
			this.message = message;
			return this;
		}

		public ErrorMessage build() {
			return new ErrorMessage(this);
		}
		
	}
}
