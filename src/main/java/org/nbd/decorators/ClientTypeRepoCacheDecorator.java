package org.nbd.decorators;

import org.nbd.config.JsonUtil;
import org.nbd.config.RedisConfig;
import org.nbd.model.ClientType;
import org.nbd.repositories.RepoManager;
import org.bson.types.ObjectId;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.stream.Collectors;

public class ClientTypeRepoCacheDecorator implements RepoManager<ClientType> {

    private final RepoManager<ClientType> repo;
    private static final String PREFIX = "clientType:";

    public ClientTypeRepoCacheDecorator(RepoManager<ClientType> repo) {
        this.repo = repo;
    }

    @Override
    public void save(ClientType entity) {
        repo.save(entity);
        invalidateCache(entity.getId());
    }

    @Override
    public ClientType findById(ObjectId id) {
        try (Jedis jedis = RedisConfig.getConnection()) {
            String key = PREFIX + id.toHexString();
            String json = jedis.get(key);
            if (json != null) return JsonUtil.fromJson(json, ClientType.class);
        } catch (Exception ignored) {}

        ClientType entity = repo.findById(id);
        if (entity != null) {
            try (Jedis jedis = RedisConfig.getConnection()) {
                jedis.setex(PREFIX + entity.getId().toHexString(), 3600, JsonUtil.toJson(entity)); // кеш на 1 час
            } catch (Exception ignored) {}
        }
        return entity;
    }

    @Override
    public List<ClientType> findAll() {
        // Для статичных типов клиентов можно кешировать список целиком
        try (Jedis jedis = RedisConfig.getConnection()) {
            String key = PREFIX + "all";
            String json = jedis.get(key);
            if (json != null) return JsonUtil.fromJsonList(json, ClientType.class);
        } catch (Exception ignored) {}

        List<ClientType> list = repo.findAll();
        try (Jedis jedis = RedisConfig.getConnection()) {
            jedis.setex(PREFIX + "all", 3600, JsonUtil.toJson(list));
        } catch (Exception ignored) {}
        return list;
    }

    @Override
    public void update(ObjectId id, ClientType entity) {
        repo.update(id, entity);
        invalidateCache(id);
    }

    @Override
    public void deleteById(ObjectId id) {
        repo.deleteById(id);
        invalidateCache(id);
    }

    @Override
    public void deleteAll() {
        repo.deleteAll();
        try (Jedis jedis = RedisConfig.getConnection()) {
            jedis.del(PREFIX + "all");
        } catch (Exception ignored) {}
    }

    private void invalidateCache(ObjectId id) {
        try (Jedis jedis = RedisConfig.getConnection()) {
            jedis.del(PREFIX + id.toHexString(), PREFIX + "all");
        } catch (Exception ignored) {}
    }
}
