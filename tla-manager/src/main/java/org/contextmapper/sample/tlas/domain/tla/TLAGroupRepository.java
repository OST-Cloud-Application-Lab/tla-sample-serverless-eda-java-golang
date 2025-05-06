package org.contextmapper.sample.tlas.domain.tla;

import org.jmolecules.ddd.annotation.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TLAGroupRepository {

    TLAGroup save(final TLAGroup group);

    Optional<TLAGroup> findByName(final ShortName name);

    List<TLAGroup> findAll();

}
