package crdt.counters

import java.io.Serializable
import crdt.counters.PNCounter
/**
 * Grow only counter. Can only be incremented
 */
class GCounter(serverId: Int, numberOfServers: Int) : Serializable {
    @JvmField
    var serverId: Int

    @JvmField
    var servers: ArrayList<Int>

    init {
        require(numberOfServers >= 1) { "Number of servers must be greater than 0" }
        require(serverId >= 0) { "Server ID must be greater than 0" }
        require(serverId < numberOfServers) { "Server ID must be less than number of servers" }
        this.serverId = serverId
        servers = ArrayList(numberOfServers)
        for (i in 0 until numberOfServers) {
            servers.add(0)
        }
    }

    /**
     * Increment a given key
     */
    fun increment() {
        servers[serverId] = servers[serverId] + 1
    }

    /**
     * Get the counter value
     */
    fun get(): Int {
        var sum = 0
        servers.stream().forEach { server: Int -> sum += server }
        return sum
    }

    /**
     * Merge another counter into this one
     */
    @Synchronized
    fun merge(other: GCounter) {
        (0 until servers.size.coerceAtMost(other.servers.size)).forEach { i: Int ->
            servers[i] = servers[i].coerceAtLeast(other.servers[i])
        }
    }

    /**
     * Copy the counter
     */
    fun copy(): GCounter {
        val copy = GCounter(serverId, servers.size)
        copy.servers = ArrayList(servers)
        return copy
    }
}
