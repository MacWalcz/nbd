package org.nbd.decorators;

import org.nbd.config.JsonUtil;
import org.nbd.config.RedisConfig;
import org.nbd.model.House;
import org.nbd.repositories.RepoManager;
import org.bson.types.ObjectId;
import redis.clients.jedis.Jedis;

import java.util.List;

public class HouseRepoCacheDecorator implements RepoManager<House> {

    private final RepoManager<House> repo;
    private final String prefix = "house:";

    public HouseRepoCacheDecorator(RepoManager<House> repo) {
        this.repo = repo;
    }

    public void save(House entity) {
        repo.save(entity);
        invalidateCache(entity.getId());
    }

    public House findById(ObjectId id) {
        try (Jedis jedis = RedisConfig.getConnection()) {
            String key = prefix + id.toHexString();
            String json = jedis.get(key);
            if (json != null) return JsonUtil.fromJson(json, House.class);
        } catch (Exception ignored) {}

        House entity = repo.findById(id);
        if (entity != null) {
            try (Jedis jedis = RedisConfig.getConnection()) {
                jedis.setex(prefix + id.toHexString(), 300, JsonUtil.toJson(entity));
            } catch (Exception ignored) {}
        }
        return entity;
    }

    public List<House> findAll() {
        return repo.findAll();
    }

    public void update(ObjectId id, House entity) {
        repo.update(id, entity);
        invalidateCache(id);
    }

    public void deleteById(ObjectId id) {
        repo.deleteById(id);
        invalidateCache(id);
    }

    public void deleteAll() {
        repo.deleteAll();
    }

    private void invalidateCache(ObjectId id) {
        try (Jedis jedis = RedisConfig.getConnection()) {
            jedis.del(prefix + id.toHexString());
        } catch (Exception ignored) {}
    }
}
