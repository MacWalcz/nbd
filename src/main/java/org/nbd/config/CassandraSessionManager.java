package org.nbd.config;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;

import java.net.InetSocketAddress;

public class CassandraSessionManager implements AutoCloseable {

    private static CqlSession session;

    public void initSession() {
        session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("cassandra1", 9042))
                .addContactPoint(new InetSocketAddress("cassandra2", 9043))
                .withLocalDatacenter("datacenter1")
                //.withKeyspace(CqlIdentifier.fromCql("rent_a_car"))
                .build();
    }

    public CqlSession getSession() {
        return session;
    }

    @Override
    public void close() {
        if (session != null && !session.isClosed()) {
            session.close();
            System.out.println("Cassandra CqlSession closed successfully.");
        }
    }
}