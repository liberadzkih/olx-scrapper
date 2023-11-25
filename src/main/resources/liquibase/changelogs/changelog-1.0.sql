-- liquibase formatted sql
-- changeset HLiberadzki:1

SET NAMES 'utf8';
SET CHARACTER SET utf8;

CREATE TABLE OLX_ITEM
(
    URL             VARCHAR(255) NOT NULL,
    SEARCH_URL      VARCHAR(255) NOT NULL,
    ID              VARCHAR(255) NOT NULL,
    TITLE           VARCHAR(255) NOT NULL,
    SUB_TITLE       VARCHAR(255),
    DESCRIPTION     VARCHAR(4000),
    PRICE           INT NOT NULL,
    ADDRESS         VARCHAR(255),
    primary key (ID)
);