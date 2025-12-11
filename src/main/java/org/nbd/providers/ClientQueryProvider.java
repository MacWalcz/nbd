package org.nbd.providers;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import org.nbd.model.Client;

import java.util.Optional;
import java.util.UUID;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class ClientQueryProvider {
    private final CqlSession session;
    private EntityHelper<Client> helper;
    public ClientQueryProvider(MapperContext ctx, EntityHelper<Client> helper) {
        session = ctx.getSession();
        this.helper = helper;
    }

    public Optional<Client> findById(UUID id) {

        Select selectClient = QueryBuilder
                .selectFrom(CqlIdentifier.fromCql("clients"))
                .all()
                .where(Relation.column("id").isEqualTo(literal(id)));
        Row row = session.execute(selectClient.build()).one();

        if (row == null) {
            return Optional.empty();
        }

        Client client = new Client(row.getUuid("id"), row.getString("first_name"), row.getString("last_name"), row.getString("phone_number"));
        return Optional.of(client);
    }
}
