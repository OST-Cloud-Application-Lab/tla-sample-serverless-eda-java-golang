package org.contextmapper.sample.tlas.domain.tla.exception;

public class TLANameAlreadyExistsInGroupException extends RuntimeException {

    public TLANameAlreadyExistsInGroupException(final String message) {
        super(message);
    }

}
