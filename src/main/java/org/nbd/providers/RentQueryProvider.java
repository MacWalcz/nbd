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
import org.nbd.model.Rent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class RentQueryProvider {

    private final CqlSession session;
    private EntityHelper<Rent> helper;

    public RentQueryProvider(MapperContext ctx, EntityHelper<Rent> helper) {
        this.session = ctx.getSession();
        this.helper = helper;
    }

    public Optional<Rent> findByIdAndStartDate(UUID id, LocalDate startDate) {

        Select selectRent = QueryBuilder.selectFrom(CqlIdentifier.fromCql("rents")).all()
                .where(Relation.column("id")
                .isEqualTo(literal(id))).where(Relation
                        .column("start_date").isEqualTo(literal(startDate)));

        Row row = session.execute(selectRent.build()).one();

        if (row == null) {
            return Optional.empty();
        }

        Rent rent = new Rent(row.getUuid("id"), row.getLocalDate("start_date"),
                row.getLocalDate("end_date"), row.getUuid("client_id"), row.getUuid("house_id"), row.getDouble("cost"));

        return Optional.of(rent);
    }


}
