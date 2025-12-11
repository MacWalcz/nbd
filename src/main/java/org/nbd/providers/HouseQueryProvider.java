package org.nbd.providers;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import org.nbd.model.House;

import java.util.Optional;
import java.util.UUID;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class HouseQueryProvider {

    private final CqlSession session;
    private EntityHelper<House> helper;
    public HouseQueryProvider(MapperContext ctx,EntityHelper<House> helper) {
        session = ctx.getSession();
        this.helper = helper;
    }

    public Optional<House>  findById(UUID id) {

        Select selectHouse = QueryBuilder
                .selectFrom(CqlIdentifier.fromCql("houses"))
                .all()
                .where(Relation.column("id").isEqualTo(literal(id)));
        Row row = session.execute(selectHouse.build()).one();

        if (row == null) {
            return Optional.empty();
        }

        House house = new House(row.getUuid("id"), row.getString("house_number"), row.getDouble("price"), row.getDouble("area"));
        return Optional.of(house);
    }
}
