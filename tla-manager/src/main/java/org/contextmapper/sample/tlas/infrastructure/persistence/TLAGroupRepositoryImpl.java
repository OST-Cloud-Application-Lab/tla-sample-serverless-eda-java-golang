package org.contextmapper.sample.tlas.infrastructure.persistence;

import org.contextmapper.sample.tlas.domain.tla.ShortName;
import org.contextmapper.sample.tlas.domain.tla.TLAGroup;
import org.contextmapper.sample.tlas.domain.tla.ThreeLetterAbbreviation;
import org.contextmapper.sample.tlas.domain.tla.TLAGroupRepository;
import org.contextmapper.sample.tlas.infrastructure.persistence.internal_repos.DynamoDBThreeLetterAbbreviationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Repository
public class TLAGroupRepositoryImpl implements TLAGroupRepository {

    private DynamoDBThreeLetterAbbreviationRepository dynamoInternalRepo;

    public TLAGroupRepositoryImpl(final DynamoDBThreeLetterAbbreviationRepository dynamoInternalRepo) {
        this.dynamoInternalRepo = dynamoInternalRepo;
    }

    @Override
    public TLAGroup save(final TLAGroup group) {
        dynamoInternalRepo.putTLAGroup(group);
        return findByName(group.getName()).orElseThrow();
    }

    @Override
    public Optional<TLAGroup> findByName(final ShortName name) {
        var optionalTLAGroup = dynamoInternalRepo.findById(name.toString());
        if (optionalTLAGroup.isPresent())
            return of(optionalTLAGroup.get());
        return empty();
    }

    @Override
    public List<TLAGroup> findAll() {
        return dynamoInternalRepo.findAll();
    }
}
