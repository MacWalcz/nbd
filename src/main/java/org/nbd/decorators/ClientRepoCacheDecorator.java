package org.nbd.decorators;

import org.nbd.config.JsonUtil;
import org.nbd.config.RedisConfig;
import org.nbd.model.Client;
import org.nbd.repositories.RepoManager;
import org.bson.types.ObjectId;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

public class ClientRepoCacheDecorator implements RepoManager<Client> {

    private final RepoManager<Client> repo;
    private final String prefix = "client:";
    private final JedisPool jedisPool;

    public ClientRepoCacheDecorator(RepoManager<Client> repo) {
        this.repo = repo;
        this.jedisPool = RedisConfig.getPool();

    }

    public void save(Client entity) {
        repo.save(entity);
        invalidateCache(entity.getId());
    }

    public Client findById(ObjectId id) {
        String key = prefix + id.toHexString();

        try (Jedis jedis = jedisPool.getResource()) {
            String json = jedis.get(key);
            if (json != null) {
                return JsonUtil.fromJson(json, Client.class);
            }
        } catch (Exception e) {
            Client entity = repo.findById(id);
            return entity;
        }

        Client entity = repo.findById(id);
        if (entity != null) {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.setex(key, 300, JsonUtil.toJson(entity));
            }
        }
        return entity;
    }

    public List<Client> findAll() {
        return repo.findAll();
    }

    public void update(ObjectId id, Client entity) {
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
