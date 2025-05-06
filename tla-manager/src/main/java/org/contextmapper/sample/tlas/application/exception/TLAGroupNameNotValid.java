package org.contextmapper.sample.tlas.application.exception;

public class TLAGroupNameNotValid extends RuntimeException {

    public TLAGroupNameNotValid(final String name) {
        super("'" + name + "' is not a valid TLA group name!");
    }

}
