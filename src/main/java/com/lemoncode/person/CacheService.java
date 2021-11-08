package com.lemoncode.person;

import com.lemoncode.descendants.DescendantDTO;
import com.lemoncode.relations.ConnectionsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final Map<Long, ConnectionsDTO> connectionsFakeCache = new ConcurrentHashMap<>();
    private final Map<String, List<DescendantDTO>> descendantsFakeCache = new ConcurrentHashMap<>();

    public void addDescendantsCache(String key, List<DescendantDTO> descendantsDTO) {
        this.descendantsFakeCache.put(key, descendantsDTO);
    }

    public List<DescendantDTO> getDescendantsCache(String key) {
        return this.descendantsFakeCache.get(key);
    }

    public void clearDescendants() {
        this.descendantsFakeCache.clear();
    }

    public void addConnectionsCache(Long key, ConnectionsDTO connectionsDTO) {
        this.connectionsFakeCache.put(key, connectionsDTO);
    }

    public ConnectionsDTO getConnectionsCache(Long key) {
        return this.connectionsFakeCache.get(key);
    }

    public void clearConnections() {
        this.connectionsFakeCache.clear();
    }
}
