package org.contextmapper.sample.tlas.application;

import org.contextmapper.sample.tlas.application.exception.TLAGroupNameAlreadyExists;
import org.contextmapper.sample.tlas.application.exception.TLAGroupNameDoesNotExist;
import org.contextmapper.sample.tlas.application.exception.TLAGroupNameNotValid;
import org.contextmapper.sample.tlas.domain.tla.*;
import org.contextmapper.sample.tlas.domain.tla.TLAGroup.TLAGroupBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.contextmapper.sample.tlas.domain.tla.TLAStatus.ACCEPTED;

@Service
public class TlaGroupsApplicationService {

    private final TLAGroupRepository repository;

    public TlaGroupsApplicationService(final TLAGroupRepository repository) {
        this.repository = repository;
    }

    public List<TLAGroup> findAllTLAGroups() {
        return findAllTLAGroups(ACCEPTED);
    }

    public List<TLAGroup> findAllTLAGroups(TLAStatus status) {
        return repository.findAll().stream()
                .map(group -> filterTLAStatus(group, status))
                .filter(group -> !group.getTLAs().isEmpty())
                .toList();
    }

    public TLAGroup addTLAGroup(final TLAGroup tlaGroup) {
        if (tlaGroupAlreadyExists(tlaGroup.getName())) {
            throw new TLAGroupNameAlreadyExists(tlaGroup.getName().toString());
        }
        return repository.save(tlaGroup);
    }

    public TLAGroup addTLA(final String groupName, final ThreeLetterAbbreviation tla) {
        var group = getGroupByName(groupName);
        group.addTLA(tla);
        return repository.save(group);
    }

    public void acceptTLA(final String groupName, final String tlaName) {
        var group = getGroupByName(groupName);
        group.acceptTLA(new ShortName(tlaName));
        repository.save(group);
    }

    private TLAGroup getGroupByName(final String name) {
        try {
            var shortName = new ShortName(name);
            var group = repository.findByName(shortName);
            if (group.isPresent()) {
                return group.get();
            } else {
                throw new TLAGroupNameDoesNotExist(name);
            }
        } catch (IllegalArgumentException e) {
            throw new TLAGroupNameNotValid(name);
        }
    }

    private boolean tlaGroupAlreadyExists(ShortName name) {
        return findAllTLAGroups().stream()
                .anyMatch(group -> group.getName().equals(name));
    }

    private TLAGroup filterTLAStatus(final TLAGroup tlaGroup, final TLAStatus status) {
        return new TLAGroupBuilder(tlaGroup.getName().toString())
                .withDescription(tlaGroup.getDescription())
                .withTLAs(tlaGroup.getTLAs().stream()
                        .filter(tla -> tla.getStatus() == status)
                        .toList())
                .build();
    }

}
