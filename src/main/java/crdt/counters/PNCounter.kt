package crdt.counters

import java.io.Serializable

/**
 * Increment and decrement counter
 */
class PNCounter(private var serverId: Int, private var numberOfServers: Int) : Serializable {
    private var inc: GCounter = GCounter(serverId, numberOfServers)
    private var dec: GCounter = GCounter(serverId, numberOfServers)

    /**
     * Increment counter
     */
    fun increment() {
        inc.increment()
    }

    /**
     * Get the counter value
     */
    fun get(): Int = inc.get() - dec.get()

    /**
     * Decrement counter
     */
    fun decrement() {
        dec.increment()
    }

    /**
     * Merge another counter into this one
     */
    fun merge(other: PNCounter) {
        inc.merge(other.inc)
        dec.merge(other.dec)
    }

    /**
     * Copy the counter
     */
    fun copy(): PNCounter {
        val copy = PNCounter(serverId, numberOfServers)
        copy.inc = inc.copy()
        copy.dec = dec.copy()
        return copy
    }
}
