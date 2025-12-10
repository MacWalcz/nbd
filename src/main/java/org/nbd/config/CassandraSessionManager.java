package org.nbd.config;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;

import java.net.InetSocketAddress;

public class CassandraSessionManager {

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

    public void close() {
        session.close();
    }
}