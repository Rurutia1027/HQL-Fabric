package com.hql.fabric.persistence.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Wrapper class for returning Persisted Objects in a page friendly format.
 */
public class PageInfo {
    private static final Logger LOG = LoggerFactory.getLogger(PageInfo.class);

    private Integer start;
    private Integer pageSize;
    private String orderBy;
    private String sort;
    private Date begin;
    private Date end;
    private static String SORT_ASC = "ascending";
    private static String SORT_DESC = "descending";
    private static String ORDER_BY_NAME = "name";
    public static String ORDER_BY_DISPLAY_NAME = "displayName";

    public PageInfo() {
        // default params
        this.start = 0;
        this.pageSize = 20;
        this.orderBy = ORDER_BY_NAME;
        this.sort = SORT_ASC;
    }

    public PageInfo(Integer start, Integer pageSize, String orderBy, String sort) {
        this.start = (start == null || start < 0) ? 0 : start;
        this.pageSize = (pageSize == null || pageSize < 0) ? 0 : pageSize;
        this.orderBy = orderBy;
        this.sort = sort;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public static String getSortAsc() {
        return SORT_ASC;
    }

    public static void setSortAsc(String sortAsc) {
        SORT_ASC = sortAsc;
    }

    public static String getSortDesc() {
        return SORT_DESC;
    }

    public static void setSortDesc(String sortDesc) {
        SORT_DESC = sortDesc;
    }

    public static String getOrderByName() {
        return ORDER_BY_NAME;
    }

    public static void setOrderByName(String orderByName) {
        ORDER_BY_NAME = orderByName;
    }

    public static String getOrderByDisplayName() {
        return ORDER_BY_DISPLAY_NAME;
    }

    public static void setOrderByDisplayName(String orderByDisplayName) {
        ORDER_BY_DISPLAY_NAME = orderByDisplayName;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        try {
            if (StringUtils.hasText(begin)) {
                SimpleDateFormat dateformat = new SimpleDateFormat();
                Date date = dateformat.parse(begin);
                this.begin = date;
            }
        } catch (ParseException e) {
            LOG.error("dateBegin:" + begin + " is not a valid UTC Date format", e);
        }

    }

    public boolean hasBegin() {
        return (this.begin != null);
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(String end) {
        try {
            if (StringUtils.hasText(end)) {
                SimpleDateFormat dateformat = new SimpleDateFormat();
                Date date = dateformat.parse(end);
                this.end = date;
            }
        } catch (ParseException e) {
            LOG.error("Failed to set end data in string {}", end, e);
        }
    }

    public boolean hasEnd() {
        return (this.end != null);
    }
}
