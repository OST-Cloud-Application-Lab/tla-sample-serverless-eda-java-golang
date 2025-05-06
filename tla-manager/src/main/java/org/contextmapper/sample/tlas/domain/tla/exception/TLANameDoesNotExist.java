package org.contextmapper.sample.tlas.domain.tla.exception;

public class TLANameDoesNotExist extends RuntimeException {

    public TLANameDoesNotExist(final String name) {
        super("A TLA '" + name + "' does not exist!");
    }

}
