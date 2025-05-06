package org.contextmapper.sample.tlas.domain.tla.exception;

public class DuplicateTLANameException extends RuntimeException {

    public DuplicateTLANameException(final String message) {
        super(message);
    }

}
