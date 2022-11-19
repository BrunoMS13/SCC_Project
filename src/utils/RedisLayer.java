package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import data_classes.AuctionDAO;
import data_classes.Login;
import data_classes.Session;
import data_classes.UserDAO;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;
import java.util.Set;

public class RedisLayer {
    private static final String RedisHostname = "rediseastasia58569.redis.cache.windows.net";
    private static final String RedisKey = "8zPF2NsimpIt6YyGX61oO4JfEFzfu6ZR3AzCaMi8bFg=";

    private static JedisPool pool;

    private static RedisLayer instance;

    // Flag to use cache or not.
    private boolean useCache = false;

    public synchronized static RedisLayer getInstance() {
        if( instance != null)
            return instance;
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        JedisPool jp = new JedisPool(poolConfig, RedisHostname, 6380, 1000, RedisKey, true);
        instance = new RedisLayer(jp);
        return instance;

    }

    public RedisLayer(JedisPool jp) {
        this.pool = jp;
    }


    public void addUser(UserDAO user) {
        if (useCache) {
            try (Jedis jedis = pool.getResource()) {
                ObjectMapper mapper = new ObjectMapper();
                String key = "users:" + user.getId();
                jedis.hset(key, "user", mapper.writeValueAsString(user));
            } catch (Exception e) {
                System.out.println("Could not add user to cache...");
                System.out.println(e.getMessage());
            }
        }
    }

    public void updateUser(UserDAO user) {
        if (useCache) {
            try (Jedis jedis = pool.getResource()) {
                ObjectMapper mapper = new ObjectMapper();
                String key = "users:" + user.getId();
                jedis.hset(key, "user", mapper.writeValueAsString(user));
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void deleteUser(String id) {
        if (useCache) {
            try (Jedis jedis = pool.getResource()) {
                String key = "users:" + id;
                jedis.del(key);
            } catch (Exception e) {
                System.out.println("Could not remove user from cache...");
            }
        }
    }

    public UserDAO getUser(String id) {
        if (useCache) {
            try (Jedis jedis = pool.getResource()) {
                ObjectMapper mapper = new ObjectMapper();
                String key = "users:" + id;
                String res = jedis.hget(key, "user");
                UserDAO temp = mapper.readValue(res, UserDAO.class);
                if (temp != null)
                    return temp;
            } catch (Exception e) {}
        }
        return null;
    }

    public void addAuction(AuctionDAO auc) {
        if (useCache) {
            try (Jedis jedis = pool.getResource()) {
                ObjectMapper mapper = new ObjectMapper();
                String key = "auctions:" + auc.getId();
                jedis.hset(key, "auction", mapper.writeValueAsString(auc));
            } catch (Exception e) {
                System.out.println("Could not add auction to cache...");
                System.out.println(e.getMessage());
            }
        }
    }

    public void updateAuction(AuctionDAO auction) {
        if (useCache) {
            try (Jedis jedis = pool.getResource()) {
                ObjectMapper mapper = new ObjectMapper();
                String key = "auctions:" + auction.getId();
                jedis.hset(key, "auction", mapper.writeValueAsString(auction));
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public AuctionDAO getAuction(String id) {
        if (useCache) {
            try (Jedis jedis = pool.getResource()) {
                ObjectMapper mapper = new ObjectMapper();
                String key = "auctions:" + id;
                String res = jedis.hget(key, "auction");
                AuctionDAO temp = mapper.readValue(res, AuctionDAO.class);
                if (temp != null)
                    return temp;
            } catch (Exception e) {}
        }
        return null;
    }

    public void addSession(Session session) {
        try (Jedis jedis = pool.getResource()){
            ObjectMapper mapper = new ObjectMapper();
            String key = "sessions:" + session.getToken();
            //System.out.println("Redis Session Key --->" + key);
            jedis.hset(key, "session", mapper.writeValueAsString(session));
        } catch (Exception e) {
            System.out.println("Could not add session to cache...");
            System.out.println(e.getMessage());
        }
    }

    public Session getSession(String id) {
        try (Jedis jedis = pool.getResource()) {
            ObjectMapper mapper = new ObjectMapper();
            String key = "sessions:" + id;
            //System.out.println("Getting Key --->" + key);
            String res = jedis.hget(key, "session");
            Session temp = mapper.readValue(res, Session.class);
            if (temp != null)
                return temp;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void printContents() {
        System.out.println("Redis contents:");
        try (Jedis jedis = pool.getResource()) {
            Set<String> keys = jedis.keys("*");
            for (String key : keys) {
                System.out.println(jedis.hgetAll(key));
            }
        } catch (Exception e) {
        }
        System.out.println();
    }

    public void clearCache() {
        try (Jedis jedis = pool.getResource()) {
            Set<String> keys = jedis.keys("*");
            for (String key : keys) {
                jedis.del(key);
            }
        } catch (Exception e) {
        }
    }

}
