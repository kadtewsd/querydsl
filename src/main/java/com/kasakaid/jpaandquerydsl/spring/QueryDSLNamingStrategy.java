package com.kasakaid.jpaandquerydsl.spring;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class QueryDSLNamingStrategy extends PhysicalNamingStrategyStandardImpl
{

    @Override
    public Identifier toPhysicalTableName(Identifier name,
                                          JdbcEnvironment jdbcEnvironment) {
        Identifier identifier = super.toPhysicalTableName(name, jdbcEnvironment);
        return identifier(identifier);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment environment)  {
        Identifier identifier = super.toPhysicalColumnName(name, environment);
        return identifier(identifier);
    }

    private Identifier identifier(Identifier name) {
        if (name.getText().matches("^[A-Z]+$"))  return Identifier.toIdentifier(name.getText().toLowerCase());
        String first = name.getText().substring(0, 1).toLowerCase();
        String later = name.getText().substring(1);
        return Identifier.toIdentifier(first + later);
    }

}
