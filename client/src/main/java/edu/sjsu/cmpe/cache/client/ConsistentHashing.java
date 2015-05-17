package edu.sjsu.cmpe.cache.client;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHashing{

    private final int numberOfReplicas = 1;
    private final SortedMap<Integer, DistributedCacheService> circle = new TreeMap<Integer, DistributedCacheService>();

    public void add(String node) {
        for (int i = 0; i < numberOfReplicas; i++) {;
            DistributedCacheService newNode = new DistributedCacheService(node);
            int key = getHash(node);
            circle.put(key,newNode);
        }
    }
    public DistributedCacheService getBucket(String node) {
        if (circle.isEmpty()) {
            return null;
        }
        int hash = getHash(node);
        if (!circle.containsKey(hash)) {
            SortedMap<Integer,DistributedCacheService> tailMap = circle.tailMap(hash);
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }
        return circle.get(hash);
    }

    public int getHash(String node) {
        int number;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(node.getBytes());
            number = new BigInteger(1, messageDigest).intValue();
            return number;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}






