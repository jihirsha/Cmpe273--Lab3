package edu.sjsu.cmpe.cache.client;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;


public class RendezvousHashing {

    ConcurrentSkipListSet<String> orderedList = new ConcurrentSkipListSet<String>();
    ConcurrentSkipListMap<String, DistributedCacheService> orderedMap = new ConcurrentSkipListMap<String, DistributedCacheService>();
    HashFunction hashFunction = Hashing.md5(); // Hashing Function

    /* Function to add cache machine */
    public void add(String node) {
        DistributedCacheService newNode = new DistributedCacheService(node);
            orderedMap.put(node,newNode);
            orderedList.add(node);
    }

    /* Function for getting highest weighted bucket to store the key-value pair*/
    public DistributedCacheService getBucket(int key) {

        long maxValue = Long.MIN_VALUE;
        DistributedCacheService max = null;
        try {
            for (String node : orderedList) {
                long nodesHash = hashFunction.newHasher()
                        .putInt(key)
                        .putString(node)
                        .hash().asLong();
                if (nodesHash > maxValue) {
                    max = orderedMap.get(node);
                    maxValue = nodesHash;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return max;
    }
}