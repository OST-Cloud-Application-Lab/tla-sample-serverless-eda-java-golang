package org.contextmapper.sample.tlas.application.exception;

public class TLAGroupNameDoesNotExist extends RuntimeException {

    public TLAGroupNameDoesNotExist(final String name) {
        super("A TLA group '" + name + "' does not exist!");
    }

}
