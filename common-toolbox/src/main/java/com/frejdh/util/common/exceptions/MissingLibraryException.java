package com.frejdh.util.common.exceptions;

/**
 * Exception, thrown when an internal/external library is missing.
 *
 * @author Kevin Frejdh
 */
public class MissingLibraryException extends RuntimeException {

	/**
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 */
	public MissingLibraryException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see RuntimeException#RuntimeException(String)
	 */
	public MissingLibraryException(String message) {
		super(message);
	}

	/**
	 * @see RuntimeException#RuntimeException(Throwable)
	 */
	public MissingLibraryException(Throwable cause) {
		super(cause);
	}

	/**
	 * @see RuntimeException#RuntimeException()
	 */
	public MissingLibraryException() {
		super();
	}

}
