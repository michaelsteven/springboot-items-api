package com.github.michaelsteven.archetype.springboot.items;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * The Class TestPageImpl.
 * 
 * Used for test cases to provide an implementation of a paged response.
 *
 * @param <T> the generic type
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestPageImpl<T> extends PageImpl<T>
{

    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new test page impl.
     *
     * @param content          the content
     * @param number           the number
     * @param size             the size
     * @param totalElements    the total elements
     * @param pageable         the pageable
     * @param last             the last
     * @param totalPages       the total pages
     * @param sort             the sort
     * @param first            the first
     * @param numberOfElements the number of elements
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public TestPageImpl(@JsonProperty("content") List<T> content, @JsonProperty("number") int number,
            @JsonProperty("size") int size, @JsonProperty("totalElements") Long totalElements,
            @JsonProperty("pageable") JsonNode pageable, @JsonProperty("last") boolean last,
            @JsonProperty("totalPages") int totalPages, @JsonProperty("sort") JsonNode sort,
            @JsonProperty("first") boolean first, @JsonProperty("numberOfElements") int numberOfElements)
    {

        super(content, PageRequest.of(number, size), totalElements);
    }

    /**
     * Instantiates a new test page impl.
     *
     * @param content  the content
     * @param pageable the pageable
     * @param total    the total
     */
    public TestPageImpl(List<T> content, Pageable pageable, long total)
    {
        super(content, pageable, total);
    }

    /**
     * Instantiates a new test page impl.
     *
     * @param content the content
     */
    public TestPageImpl(List<T> content)
    {
        super(content);
    }

    /**
     * Instantiates a new test page impl.
     */
    public TestPageImpl()
    {
        super(new ArrayList<>());
    }
}
