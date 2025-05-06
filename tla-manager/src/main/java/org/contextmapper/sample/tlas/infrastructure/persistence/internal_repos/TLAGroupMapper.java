package org.contextmapper.sample.tlas.infrastructure.persistence.internal_repos;

import com.google.common.collect.Lists;
import org.contextmapper.sample.tlas.domain.tla.TLAGroup;
import org.contextmapper.sample.tlas.domain.tla.TLAStatus;
import org.contextmapper.sample.tlas.domain.tla.ThreeLetterAbbreviation;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.builder;

public class TLAGroupMapper {

    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String TLAS = "tlas";
    private static final String MEANING = "meaning";
    private static final String ALTERNATIVE_MEANINGS = "alternative_meanings";
    private static final String URL = "url";
    private static final String STATUS = "status";

    private TLAGroupMapper() {
        // hide constructor
    }

    public static TLAGroup tlaGroupFromDynamoDB(Map<String, AttributeValue> items) {
        var builder = new TLAGroup.TLAGroupBuilder(items.get(NAME).s())
                .withDescription(items.get(DESCRIPTION).s());

        for (AttributeValue av : items.get(TLAS).l()) {
            Map<String, AttributeValue> tlaMap = av.m();
            var tlaBuilder = new ThreeLetterAbbreviation.TLABuilder(tlaMap.get(NAME).s())
                    .withMeaning(tlaMap.get(MEANING).s())
                    .withStatus(TLAStatus.valueOf(tlaMap.get(STATUS).s()));
            if (tlaMap.containsKey(ALTERNATIVE_MEANINGS)) {
                tlaBuilder.withAlternativeMeanings(tlaMap.get(ALTERNATIVE_MEANINGS).l().stream()
                        .map(AttributeValue::s)
                        .toList());
            }
            if (tlaMap.containsKey(URL)) {
                tlaBuilder.withLink(tlaMap.get(URL).s());
            }
            builder.withTLA(tlaBuilder.build());
        }

        return builder.build();
    }

    public static Map<String, AttributeValue> tlaGroupToDynamoDb(TLAGroup group) {
        var map = new HashMap<String, AttributeValue>();
        map.put(NAME, builder().s(group.getName().toString()).build());
        map.put(DESCRIPTION, builder().s(group.getDescription()).build());

        List<Map<String, AttributeValue>> tlaList = Lists.newArrayList();
        for (ThreeLetterAbbreviation tla : group.getTLAs()) {
            var tlaMap = new HashMap<String, AttributeValue>();
            tlaMap.put(NAME, builder().s(tla.getName().toString()).build());
            tlaMap.put(MEANING, builder().s(tla.getMeaning()).build());
            if (!tla.getAlternativeMeanings().isEmpty()) {
                tlaMap.put(ALTERNATIVE_MEANINGS, builder().l(tla.getAlternativeMeanings().stream()
                        .map(AttributeValue::fromS)
                        .toList()).build());
            }
            if (tla.getLink() != null && !"".equals(tla.getLink())) {
                tlaMap.put(URL, builder().s(tla.getLink()).build());
            }
            tlaMap.put(STATUS, builder().s(tla.getStatus().toString()).build());
            tlaList.add(tlaMap);
        }
        map.put(TLAS, builder().l(
                tlaList.stream()
                        .map(AttributeValue::fromM)
                        .toList()
        ).build());
        return map;
    }

}
