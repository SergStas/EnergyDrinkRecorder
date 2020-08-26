package com.sergstas.lib.sql.res

class StrConsts {
    companion object {
        public const val QUERY_ADD = "insert into %s (%s) values (%s);"
        public const val QUERY_CREATE_TABLE = "create table %s (%s);"
        public const val QUERY_SELECT_BY_ID = "select * from %s where %s = '%s';"
        public const val QUERY_SELECT_ALL = "select * from %s;"
    }
}