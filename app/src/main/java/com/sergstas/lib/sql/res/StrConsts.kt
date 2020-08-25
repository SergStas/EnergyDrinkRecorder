package com.sergstas.lib.sql.res

class StrConsts {
    companion object {
        public const val QUERY_ADD = "insert into %s (%s) values (%s);"
        public const val QUERY_CREATE_TABLE = "create table %s (%s);"
    }
}