package com.origamisoftware.puzzle.util;

/**
 * Used to single that input data to a graph algorithm is invalid.
 */
public class InvalidGraphSearchParametersException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    InvalidGraphSearchParametersException(String message) {
        super(message);
    }
}
