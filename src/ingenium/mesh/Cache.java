package ingenium.mesh;

import java.util.HashMap;

public class Cache<KEY, DATA> {
    private boolean use;
    private String name;
    private HashMap<KEY, DATA> cache = new HashMap<KEY, DATA>();
    private int cacheHits = 0;

    /**
     * 
     * @param cacheName the name of the cache
     * @param useCache  whether to use the cache or not
     */
    public Cache(String cacheName, boolean useCache) {
        this.name = cacheName;
        this.use = useCache;
    }

    /**
     * 
     * @param key  the key
     * @param data the data
     */
    public void add(KEY key, DATA data) {
        if (use)
            if (!cache.containsKey(key))
                cache.put(key, data);
    }

    /**
     * 
     * @param key the key to get
     * @return whether the key exists or not
     */
    public boolean containsKey(KEY key) {
        return cache.keySet().contains(key);
    }

    /**
     * 
     * @param data the data
     * @return whether the value exists or not
     */
    public boolean containsValue(DATA data) {
        return cache.containsValue(data);
    }

    /**
     * 
     * @param key the key
     * @return the value associated with the key
     */
    public DATA getCacheValue(KEY key) {
        if (use && containsKey(key)) {
            cacheHits++;
            return cache.get(key);
        }
        return null;
    }

    /**
     * 
     * @return the name of the cache
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @return the cache hash map
     */
    public HashMap<KEY, DATA> getCache() {
        if (use)
            return cache;
        return null;
    }

    /**
     * 
     * @return whether the cache is being used
     */
    public boolean isUsed() {
        return use;
    }

    /**
     * 
     * @param use whether to use the cache or not
     */
    public void use(boolean use) {
        this.use = use;
    }

    /**
     * 
     * @return the times a value was found in the cache
     */
    public int getCacheHits() {
        return cacheHits;
    }

}
