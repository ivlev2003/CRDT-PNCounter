package crdt.counters

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GCounterTest {
    @Test
    fun testConstructorWithValidValues() {
        val serverId = 0
        val numberOfServers = 3
        val gCounter = GCounter(serverId, numberOfServers)
        Assertions.assertEquals(serverId, gCounter.serverId)
        Assertions.assertEquals(numberOfServers, gCounter.servers.size)
    }

    @Test
    fun testConstructorWithInvalidValues() {
        Assertions.assertThrows(IllegalArgumentException::class.java) { GCounter(0, -1) }
        Assertions.assertThrows(IllegalArgumentException::class.java) { GCounter(0, 0) }
        Assertions.assertThrows(IllegalArgumentException::class.java) { GCounter(-1, 1) }
        Assertions.assertThrows(IllegalArgumentException::class.java) { GCounter(1, 1) }
    }

    @Test
    fun testIncrementMethodForSingleInstance() {
        val gCounter = GCounter(0, 3)
        gCounter.inc()
        Assertions.assertEquals(1, gCounter.get())
    }

    @Test
    fun testIncrementMethodForMultipleInstances() {
        val gCounter1 = GCounter(0, 3)
        val gCounter2 = GCounter(1, 3)
        gCounter1.inc()
        gCounter2.inc()
        Assertions.assertEquals(1, gCounter1.get())
        Assertions.assertEquals(1, gCounter2.get())
    }

    @Test
    fun testGetMethodForSingleInstance() {
        val gCounter = GCounter(0, 3)
        gCounter.inc()
        gCounter.inc()
        gCounter.inc()
        Assertions.assertEquals(3, gCounter.get())
    }

    @Test
    fun testGetMethodForMultipleInstances() {
        val gCounter1 = GCounter(0, 3)
        val gCounter2 = GCounter(1, 3)
        gCounter1.inc()
        gCounter1.inc()
        gCounter2.inc()
        Assertions.assertEquals(2, gCounter1.get())
        Assertions.assertEquals(1, gCounter2.get())
    }

    @Test
    fun testMergeMethodWithDifferentValues() {
        val gCounter1 = GCounter(0, 3)
        val gCounter2 = GCounter(1, 3)
        gCounter1.inc()
        gCounter2.inc()
        gCounter2.inc()
        gCounter1.merge(gCounter2)
        Assertions.assertEquals(3, gCounter1.get())
    }

    @Test
    fun testMergeMethodWithSameValues() {
        val gCounter1 = GCounter(0, 3)
        val gCounter2 = GCounter(1, 3)
        gCounter1.inc()
        gCounter2.inc()
        gCounter1.merge(gCounter2)
        gCounter2.merge(gCounter1)
        Assertions.assertEquals(2, gCounter1.get())
        Assertions.assertEquals(2, gCounter2.get())
    }

    @Test
    fun testCopyMethod() {
        val gCounter1 = GCounter(0, 3)
        gCounter1.inc()
        gCounter1.inc()
        val gCounter2 = gCounter1.copy()
        gCounter2.inc()
        Assertions.assertEquals(2, gCounter1.get())
        Assertions.assertEquals(3, gCounter2.get())
    }

    @Test
    @Throws(InterruptedException::class)
    fun testConcurrency() {
        val gCounter0 = GCounter(0, 3)
        val gCounter1 = GCounter(1, 3)
        val gCounter2 = GCounter(2, 3)
        val t1 = Thread {
            for (i in 0..99) {
                for(j in 0..10) {
                    gCounter0.inc()
                }
                gCounter0.merge(gCounter1)
            }
        }
        val t2 = Thread {
            for (i in 0..99) {
                for (j in 0..10) {
                    gCounter1.inc()
                }
                gCounter1.merge(gCounter2)
            }
        }
        val t3 = Thread {
            for (i in 0..99) {
                for (j in 0..10) {
                    gCounter2.inc()
                }
                gCounter2.merge(gCounter0)
            }
        }
        t1.start()
        t2.start()
        t3.start()
        t1.join()
        t2.join()
        t3.join()
        gCounter0.merge(gCounter1)
        gCounter0.merge(gCounter2)
        Assertions.assertEquals(3300, gCounter0.get())
    }
}
