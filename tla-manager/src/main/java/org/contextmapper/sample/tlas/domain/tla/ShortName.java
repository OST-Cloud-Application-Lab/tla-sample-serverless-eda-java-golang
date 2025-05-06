package org.contextmapper.sample.tlas.domain.tla;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;

@ValueObject
public class ShortName implements Comparable<ShortName> {

    private final String name;

    public ShortName(final String name) {
        checkArgument(name != null, "Short name cannot be null!");
        checkArgument(!name.isEmpty(), "Short name cannot be empty!");
        checkArgument(!name.contains(" "), "A single short name cannot contain spaces.");

        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShortName that = (ShortName) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(ShortName shortName) {
        return this.name.compareTo(shortName.getName());
    }
}
