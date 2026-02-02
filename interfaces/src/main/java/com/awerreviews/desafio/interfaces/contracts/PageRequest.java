package com.awerreviews.desafio.interfaces.contracts;

import java.awt.print.Pageable;

public class PageRequest implements Pageable {
    private int pageNumber;
    private int pageSize;
    private int totalElements;
    private int totalPages;

    @Override
    public int getNumberOfPages() {
        return 0;
    }

    @Override
    public java.awt.print.PageFormat getPageFormat(int pageIndex) {
        return null;
    }

    @Override
    public java.awt.print.Printable getPrintable(int pageIndex) {
        return null;
    }

    public PageRequest of(int pageNumber, int pageSize) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.pageNumber = pageNumber;
        pageRequest.pageSize = pageSize;
        return pageRequest;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
