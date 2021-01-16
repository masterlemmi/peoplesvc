package com.lemoncode.util;

import com.lemoncode.person.GenderEnum;
import com.lemoncode.relationship.RelationshipLabel;

public class EntityCreator {
    private static final LabelProvider LABEL_PROVIDER = LabelProvider.getInstance();

    public static RelationshipLabel createLabel(String label, GenderEnum otherGender) {
        String oppositeLabel = LABEL_PROVIDER.getOpposite(label, otherGender);
        return new RelationshipLabel(label, oppositeLabel);
    }
}
