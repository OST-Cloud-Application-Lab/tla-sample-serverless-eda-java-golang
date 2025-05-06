package org.contextmapper.sample.tlas.application.exception;

public class TLAGroupNameAlreadyExists extends RuntimeException {

    public TLAGroupNameAlreadyExists(final String name) {
        super("A TLA group with name '" + name + "' already exists!");
    }

}
