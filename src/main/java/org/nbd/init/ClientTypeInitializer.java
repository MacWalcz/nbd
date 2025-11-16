package org.nbd.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.nbd.model.ClientType;
import org.nbd.model.Default;
import org.nbd.model.Luxury;
import org.nbd.model.Premium;
import org.nbd.repositories.ClientTypeRepo;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientTypeInitializer {

    private final ClientTypeRepo repo;

    @PostConstruct
    public void init() {
        if (repo.count() == 0) {
            ClientType def = new Default();
            def.setId("1");
            repo.save(def);
            ClientType pre = new Premium();
            pre.setId("2");
            repo.save(pre);
            ClientType lux = new Luxury();
            lux.setId("3");
            repo.save(lux);
        }
    }
}
