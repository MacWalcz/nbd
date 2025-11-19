package org.nbd.decorators;

import org.nbd.config.JsonUtil;
import org.nbd.config.RedisConfig;
import org.nbd.exceptions.HouseNotAvaibleException;
import org.nbd.model.Rent;
import org.nbd.repositories.RepoManager;
import org.bson.types.ObjectId;
import redis.clients.jedis.Jedis;

import java.time.LocalDate;
import java.util.List;

public class RentRepoCacheDecorator implements RepoManager<Rent> {

    private final RepoManager<Rent> repo;
    private static final String PREFIX = "rent:";

    public RentRepoCacheDecorator(RepoManager<Rent> repo) {
        this.repo = repo;
    }

    @Override
    public void save(Rent rent) {
        // проверка пересечений через реальный репозиторий
        List<Rent> overlaps = ((org.nbd.repositories.RentRepo) repo)
                .findOverlappingReservations(rent.getHouse().getId(), rent.getStartDate(), rent.getEndDate());

        if (!overlaps.isEmpty()) throw new HouseNotAvaibleException("Dom jest zajęty!");

        repo.save(rent);
        invalidateCache(rent.getId());
    }

    @Override
    public Rent findById(ObjectId id) {
        try (Jedis jedis = RedisConfig.getConnection()) {
            String json = jedis.get(PREFIX + id.toHexString());
            if (json != null) return JsonUtil.fromJson(json, Rent.class);
        } catch (Exception ignored) {}

        Rent rent = repo.findById(id);
        if (rent != null) {
            try (Jedis jedis = RedisConfig.getConnection()) {
                jedis.setex(PREFIX + rent.getId().toHexString(), 300, JsonUtil.toJson(rent));
            } catch (Exception ignored) {}
        }
        return rent;
    }

    @Override
    public List<Rent> findAll() { return repo.findAll(); }

    @Override
    public void update(ObjectId id, Rent entity) { repo.update(id, entity); invalidateCache(id); }

    @Override
    public void deleteById(ObjectId id) { repo.deleteById(id); invalidateCache(id); }

    @Override
    public void deleteAll() { repo.deleteAll(); }

    private void invalidateCache(ObjectId id) {
        try (Jedis jedis = RedisConfig.getConnection()) {
            jedis.del(PREFIX + id.toHexString());
        } catch (Exception ignored) {}
    }
}

