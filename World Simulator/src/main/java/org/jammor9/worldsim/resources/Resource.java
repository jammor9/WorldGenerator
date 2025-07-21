package org.jammor9.worldsim.resources;

import java.util.ArrayList;

public abstract class Resource {
    private ArrayList<ResourceTags> tags = new ArrayList<>();

    public void addTag(ResourceTags tag) {
        tags.add(tag);
    }

    public boolean containsTag(ResourceTags tag) {
        return tags.contains(tag);
    }
}
