package com.sergstas.lib.sql.res

class StrConstants {
    companion object {
        public const val QUERY_DELETE_FROM_WHERE = "delete from %s where %s = %s;"
        public const val QUERY_DELETE_ALL = "delete from %s;"
        public const val QUERY_ADD = "insert into %s (%s) values (%s);"
        public const val QUERY_CREATE_TABLE = "create table %s (%s);"
        public const val QUERY_SELECT_WHERE = "select * from %s where %s = '%s';"
        public const val QUERY_SELECT_ALL = "select * from %s;"
    }
}