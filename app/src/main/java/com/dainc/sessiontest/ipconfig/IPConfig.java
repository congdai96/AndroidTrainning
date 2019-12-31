package com.dainc.sessiontest.ipconfig;

public interface IPConfig {
    String IP="192.168.1.6:8090/training";

    String CHECK_LOGIN="http://"+IP+"/jwt";
    String GET_INF_USER="http://"+IP+"/api-user";
    String GET_LIST_ROLE="http://"+IP+"/api-role";
    String GET_LIST_USER_BY_SEARCH="http://"+IP+"/api-search";
    String GET_LIST_SHUUKEI="http://"+IP+"/api-shuukei";
}
