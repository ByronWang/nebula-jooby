# nebula-jooby



If you want to be able to write portable JDBC programs that can create tables on a variety of different databases, you have two main choices. First, you can restrict yourself to using only very widely accepted SQL type names such as INTEGER, NUMERIC, or VARCHAR, which are likely to work for all databases. Or second, you can use the java.sql.DatabaseMetaData.getTypeInfo method to discover which SQL types are actually supported by a given database and select a database-specific SQL type name that matches a given JDBC type.