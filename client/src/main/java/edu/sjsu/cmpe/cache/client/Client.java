package edu.sjsu.cmpe.cache.client;

import com.google.common.hash.Hashing;

public class Client {

    static ConsistentHashing chObj = new ConsistentHashing();
    static RendezvousHashing chRObj = new RendezvousHashing();
    static String[] nodes = {"http://localhost:3000","http://localhost:3001","http://localhost:3002"};

    public static void main(String[] args) throws Exception {
        System.out.println("Starting Cache Client...");

        for(int i=0;i<nodes.length;i++)
        {
            chRObj.add(nodes[i]);
        }

        /*Consistent Hashing*/
       /* put(1,"a");
        put(2,"b");
        put(3,"c");
        put(4,"d");
        put(5,"e");
        put(6,"f");
        put(7,"g");
        put(8,"h");
        put(9,"i");
        put(10,"j");


        for(int i=1;i<11;i++) {
            get(i);
        }*/


        /*Rendezvous Hashing*/

        put_R(1,"a");
        put_R(2,"b");
        put_R(3,"c");
        put_R(4,"d");
        put_R(5,"e");
        put_R(6,"f");
        put_R(7,"g");
        put_R(8,"h");
        put_R(9,"i");
        put_R(10,"j");

        for(int i=1;i<11;i++) {
            get_R(i);
        }

    }
    /* GET and PUT methods for Consistent Hashing*/
    public static void get(int key)
    {
        int bucket = Hashing.consistentHash(Hashing.md5().hashString(Integer.toString(key)), nodes.length);
        DistributedCacheService node = chObj.getBucket(nodes[bucket]);
        String value = node.get(key);
        System.out.println("Getting from server:" + node.cacheServerUrl);
        System.out.println("Key:"+ key + "==>" + value);
    }
    public static void put(int key,String value)
    {
        int bucket = Hashing.consistentHash(Hashing.md5().hashString(Integer.toString(key)), nodes.length);
        DistributedCacheService node = chObj.getBucket(nodes[bucket]);
        node.put(key,value);
        System.out.println("putting:" + key + "==>" + value + "  on server:" + node.cacheServerUrl);
    }

    /* GET and PUT key-value pair for Rendezvous Hashing */

    public static void get_R(int key)
    {
        DistributedCacheService node = chRObj.getBucket(key);
        System.out.println("Getting from server:" + node.cacheServerUrl);
        String value = node.get(key);
        System.out.println("Key:"+ key + "==>" + value);

    }
    public static void put_R(int key,String value)
    {
        DistributedCacheService node= chRObj.getBucket(key);
        node.put(key,value);
        System.out.println("putting:" + key + "==>" + value + "  on server:" + node.cacheServerUrl);
    }

}
