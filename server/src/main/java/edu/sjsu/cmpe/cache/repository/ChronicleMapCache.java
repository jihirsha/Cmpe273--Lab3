package edu.sjsu.cmpe.cache.repository;

import edu.sjsu.cmpe.cache.domain.Entry;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class ChronicleMapCache implements CacheInterface{

    ChronicleMapBuilder<Long, Entry> builder;
    ChronicleMap<Long, Entry> map;

    public ChronicleMapCache(String serverURL){
        try {
            File file = new File(serverURL + ".dat");
            builder = ChronicleMapBuilder.of(Long.class, Entry.class);
            map = builder.createPersistedTo(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Entry save(Entry newEntry){
        checkNotNull(newEntry, "newEntry instance must not be null");
        map.putIfAbsent(newEntry.getKey(),newEntry);
        return newEntry;
    }

    @Override
    public Entry get(Long key) {

        checkArgument(key > 0,
                "Key was %s but expected greater than zero value", key);
        return map.get(key);
    }

    @Override
    public List<Entry> getAll() {
        return new ArrayList<Entry>(map.values());

    }
}