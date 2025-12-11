package org.nbd.tests;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.nbd.config.CassandraSessionManager;
import org.nbd.dao.ClientDao;
import org.nbd.dao.HouseDao;
import org.nbd.dao.RentDao;
import org.nbd.mappers.AppMapper;
import org.nbd.mappers.AppMapperBuilder;
import org.nbd.model.Client;
import org.nbd.model.House;
import org.nbd.model.Rent;


import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createKeyspace;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CassandraTest {
    private CqlSession session;

    @BeforeAll
    void setup() {
        CassandraSessionManager cassandraSessionManager = new CassandraSessionManager();
        cassandraSessionManager.initSession();
        session = cassandraSessionManager.getSession();
        CreateKeyspace keyspace = createKeyspace(CqlIdentifier.fromCql("rent_a_house"))
                .ifNotExists()
                .withSimpleStrategy(2)
                .withDurableWrites(true);
        SimpleStatement createKeyspace = keyspace.build();
        session.execute(createKeyspace);

        SimpleStatement createHouses =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("houses"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("id"), DataTypes.UUID)
                        .withClusteringColumn(CqlIdentifier.fromCql("price"), DataTypes.DOUBLE)
                        .withColumn(CqlIdentifier.fromCql("house_number"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("area"), DataTypes.DOUBLE)
                        .withClusteringOrder(CqlIdentifier.fromCql("price"), ClusteringOrder.ASC)
                        .build();
        session.execute(createHouses);

//        SimpleStatement dropClients =
//                SchemaBuilder.dropTable("clients")
//                        .build();
//        session.execute(dropClients);

        SimpleStatement createClients =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("clients"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("id"), DataTypes.UUID)
                        .withColumn(CqlIdentifier.fromCql("first_name"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("last_name"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("phone_number"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("active"), DataTypes.BOOLEAN)
                        .build();
        session.execute(createClients);

//        SimpleStatement dropRents =
//                SchemaBuilder.dropTable("rents")
//                        .build();
//        session.execute(dropRents);

        SimpleStatement createRents =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("rents"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("id"), DataTypes.UUID)
                        .withClusteringColumn(CqlIdentifier.fromCql("start_date"), DataTypes.DATE)
                        .withColumn(CqlIdentifier.fromCql("end_date"), DataTypes.DATE)
                        .withColumn(CqlIdentifier.fromCql("client_id"), DataTypes.UUID)
                        .withColumn(CqlIdentifier.fromCql("house_id"), DataTypes.UUID)
                        .withColumn(CqlIdentifier.fromCql("cost"), DataTypes.DOUBLE)
                        .withClusteringOrder(CqlIdentifier.fromCql("start_date"), ClusteringOrder.ASC)
                        .build();
        session.execute(createRents);


    }

    @Test
    void createHouse() {

        UUID uuid = UUID.randomUUID();
        AppMapper appMapper = new AppMapperBuilder(session).build();
        HouseDao houseDao = appMapper.houseDao();

        House house1 = new House(uuid,"67",21.37,13.37);
        houseDao.create(house1);
        House house2 = houseDao.findById(uuid).get();

        Assertions.assertEquals(house1.getPrice(),house2.getPrice());
    }

    @Test
    void createClient() {
        UUID uuid = UUID.randomUUID();
        AppMapper appMapper = new AppMapperBuilder(session).build();
        ClientDao clientDao = appMapper.clientDao();

        Client client1 = new Client(uuid,"Paweł","Dżej","504420021");

        clientDao.create(client1);

        Client client2 = clientDao.findById(uuid).get();

        Assertions.assertEquals(client1.getPhoneNumber(),client2.getPhoneNumber());
    }

    @Test
    void createRent() {
        UUID uuid_client = UUID.randomUUID();
        UUID uuid_house = UUID.randomUUID();
        UUID uuid_rent = UUID.randomUUID();
        AppMapper appMapper = new AppMapperBuilder(session).build();
        RentDao rentDao = appMapper.rentDao();

        HouseDao houseDao = appMapper.houseDao();
        House house1 = new House(uuid_client,"67",21.37,13.37);
        houseDao.create(house1);

        ClientDao clientDao = appMapper.clientDao();
        Client client1 = new Client(uuid_house,"Loh","Najober","504420021");
        clientDao.create(client1);

        Rent rent1 = new Rent(uuid_rent, LocalDate.of(2025,12,11), LocalDate.of(2025,12,15),uuid_client,uuid_house,20);
        rentDao.create(rent1);

        Rent rent2 = rentDao.findByIdAndStartDate(uuid_rent, LocalDate.of(2025,12,11)).get();

        Assertions.assertEquals(rent2.getEndDate(),rent1.getEndDate());

    }

    @Test
    void delete() {
        UUID uuid_client = UUID.randomUUID();
        UUID uuid_house = UUID.randomUUID();
        UUID uuid_rent = UUID.randomUUID();
        AppMapper appMapper = new AppMapperBuilder(session).build();
        RentDao rentDao = appMapper.rentDao();

        HouseDao houseDao = appMapper.houseDao();
        House house1 = new House(uuid_house,"67",21.37,13.37);
        houseDao.create(house1);

        ClientDao clientDao = appMapper.clientDao();
        Client client1 = new Client(uuid_client,"Loh","Najober","504420021");
        clientDao.create(client1);

        RentDao rentDao1 = appMapper.rentDao();
        Rent rent1 = new Rent(uuid_rent, LocalDate.of(2025,12,11), LocalDate.of(2025,12,15),uuid_client,uuid_house,20);
        rentDao1.create(rent1);

        houseDao.delete(house1);
        clientDao.delete(client1);
        rentDao.delete(rent1);

        Client client2 = clientDao.findById(uuid_client).orElse(null);
        House house2 = houseDao.findById(uuid_house).orElse(null);
        Rent rent2 = rentDao.findByIdAndStartDate(uuid_rent, LocalDate.of(2025,12,11)).orElse(null);

        Assertions.assertNull(rent2);
        Assertions.assertNull(client2);
        Assertions.assertNull(house2);
    }

    @Test
    void update() {
        UUID uuid_client = UUID.randomUUID();
        UUID uuid_house = UUID.randomUUID();
        UUID uuid_rent = UUID.randomUUID();
        AppMapper appMapper = new AppMapperBuilder(session).build();
        RentDao rentDao = appMapper.rentDao();

        HouseDao houseDao = appMapper.houseDao();
        House house1 = new House(uuid_house,"67",21.37,13.37);
        houseDao.create(house1);

        ClientDao clientDao = appMapper.clientDao();
        Client client1 = new Client(uuid_client,"Loh","Najober","504420021");
        clientDao.create(client1);

        RentDao rentDao1 = appMapper.rentDao();
        Rent rent1 = new Rent(uuid_rent, LocalDate.of(2025,12,11), LocalDate.of(2025,12,15),uuid_client,uuid_house,20);
        rentDao1.create(rent1);

        client1.setFirstName("Tomek");
        clientDao.update(client1);

        house1.setArea(6.7);
        houseDao.update(house1);

        rent1.setStartDate(LocalDate.of(2026,12,11));
        rentDao1.update(rent1);


        Client client2 = clientDao.findById(uuid_client).orElse(null);
        House house2 = houseDao.findById(uuid_house).orElse(null);
        Rent rent2 = rentDao1.findByIdAndStartDate(uuid_rent, LocalDate.of(2026,12,11)).orElse(null);

        Assertions.assertEquals(client2.getFirstName(),client1.getFirstName());
        Assertions.assertEquals(house2.getArea(),house1.getArea());
        Assertions.assertEquals(rent2.getStartDate(),rent1.getStartDate());

    }

}
