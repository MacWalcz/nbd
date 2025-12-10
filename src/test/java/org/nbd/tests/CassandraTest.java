package org.nbd.tests;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.nbd.config.CassandraSessionManager;


import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createKeyspace;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CassandraTest {
    private CqlSession session;

    @BeforeAll
    void setup(){
        CassandraSessionManager cassandraSessionManager = new CassandraSessionManager();
        cassandraSessionManager.initSession();
        session = cassandraSessionManager.getSession();
        CreateKeyspace keyspace = createKeyspace(CqlIdentifier.fromCql("rent_a_house"))
                .ifNotExists()
                .withSimpleStrategy(2)
                .withDurableWrites(true);
        SimpleStatement createKeyspace = keyspace.build();
        session.execute(createKeyspace);
    }

    @Test
    void createTable(){
        Assertions.assertTrue(true);
    }

}
