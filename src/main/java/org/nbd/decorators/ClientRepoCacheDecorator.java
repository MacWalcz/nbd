package org.nbd.decorators;

import org.nbd.config.JsonUtil;
import org.nbd.config.RedisConfig;
import org.nbd.model.Client;
import org.nbd.repositories.RepoManager;
import org.bson.types.ObjectId;
import redis.clients.jedis.Jedis;

import java.util.List;

public class ClientRepoCacheDecorator implements RepoManager<Client> {

    private final RepoManager<Client> repo;
    private final String prefix = "client:";

    public ClientRepoCacheDecorator(RepoManager<Client> repo) {
        this.repo = repo;
    }

    @Override
    public void save(Client entity) {
        repo.save(entity);
        invalidateCache(entity.getId());
    }

    @Override
    public Client findById(ObjectId id) {
        try (Jedis jedis = RedisConfig.getConnection()) {
            String key = prefix + id.toHexString();
            String json = jedis.get(key);
            if (json != null) return JsonUtil.fromJson(json, Client.class);
        } catch (Exception ignored) {}

        Client entity = repo.findById(id);
        if (entity != null) {
            try (Jedis jedis = RedisConfig.getConnection()) {
                jedis.setex(prefix + id.toHexString(), 300, JsonUtil.toJson(entity)); // кеш 5 минут
            } catch (Exception ignored) {}
        }
        return entity;
    }

    @Override
    public List<Client> findAll() {
        return repo.findAll();
    }

    @Override
    public void update(ObjectId id, Client entity) {
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
        // Можно почистить весь клиентский кэш, если нужно
    }

    private void invalidateCache(ObjectId id) {
        try (Jedis jedis = RedisConfig.getConnection()) {
            jedis.del(prefix + id.toHexString());
        } catch (Exception ignored) {}
    }
}
