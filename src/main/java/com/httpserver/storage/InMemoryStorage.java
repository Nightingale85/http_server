package com.httpserver.storage;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;


public class InMemoryStorage {
    private static HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();

    public static HazelcastInstance getInstance() {
        return hazelcastInstance;
    }
}
