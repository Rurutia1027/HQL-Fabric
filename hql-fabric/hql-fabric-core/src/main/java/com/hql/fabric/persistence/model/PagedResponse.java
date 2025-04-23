package com.hql.fabric.persistence.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.hql.fabric.persistence.entity.BaseEntity;
import com.hql.fabric.persistence.entity.NamedArtifact;

import java.util.Collection;

/**
 * Wrapper class for returning Persisted Objects in a page friendly format.
 *
 * @param <T> Type of class to be paged
 */
@JsonPropertyOrder(value = {"start", "page_size", "total", "elements"})
public class PagedResponse<T extends NamedArtifact> {
    private int start;
    private int pageSize;
    private int total;
    private Collection<T> elements;

    /**
     * @return start of the page
     */
    public int getStart() {
        return start;
    }

    /**
     * Hibernate page inclusive, 0 start
     * @param start set the page start
     */
    public void setStart(int start) {
        this.start = start;
    }

    @JsonProperty("page_size")
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Collection<T> getElements() {
        return elements;
    }

    public void setElements(Collection<T> elements) {
        this.elements = elements;
    }
}
