package org.nbd.ports.output.clients.types;

import org.nbd.model.ClientType;

public interface ClientTypeCommandPort {
    ClientType save(ClientType clientType);
}
