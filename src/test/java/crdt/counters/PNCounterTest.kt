package crdt.counters

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PNCounterTest {
    @Test
    fun testConstructor() {
        var counter = PNCounter(0, 2)
        Assertions.assertEquals(0, counter.get())
        Assertions.assertEquals(0, counter.copy().get())
        counter = PNCounter(1, 3)
        Assertions.assertEquals(0, counter.get())
        Assertions.assertEquals(0, counter.copy().get())
    }

    @Test
    fun testIncrement() {
        val counter = PNCounter(0, 1)
        counter.increment()
        Assertions.assertEquals(1, counter.get())
        counter.increment()
        Assertions.assertEquals(2, counter.get())
    }

    @Test
    fun testConstructorWithInvalidValues() {
        Assertions.assertThrows(IllegalArgumentException::class.java) { PNCounter(0, -1) }
        Assertions.assertThrows(IllegalArgumentException::class.java) { PNCounter(0, 0) }
        Assertions.assertThrows(IllegalArgumentException::class.java) { GCounter(-1, 1) }
        Assertions.assertThrows(IllegalArgumentException::class.java) { GCounter(1, 1) }
    }

    @Test
    fun testDecrement() {
        val counter = PNCounter(0, 1)
        counter.dec()
        Assertions.assertEquals(-1, counter.get())
        counter.dec()
        Assertions.assertEquals(-2, counter.get())
    }

    @Test
    fun TestIncrementAndDecrement() {
        val counter = PNCounter(0, 1)
        counter.increment()
        counter.increment()
        counter.dec()
        Assertions.assertEquals(1, counter.get())
    }

    @Test
    fun testGet() {
        val counter = PNCounter(0, 2)
        counter.increment()
        counter.increment()
        counter.increment()
        Assertions.assertEquals(3, counter.get())
    }

    @Test
    fun testMerge() {
        val counter1 = PNCounter(0, 2)
        val counter2 = PNCounter(1, 2)
        counter1.increment()
        counter1.increment()
        counter2.dec()
        counter1.merge(counter2)
        Assertions.assertEquals(1, counter1.get())
    }

    @Test
    fun testCopy() {
        val counter = PNCounter(0, 1)
        counter.increment()
        val copy = counter.copy()
        Assertions.assertEquals(1, copy.get())
        copy.increment()
        Assertions.assertEquals(2, copy.get())
        Assertions.assertEquals(1, counter.get())
    }

    @Test
    fun testConcurency() {
        val pnCounter0 = PNCounter(0, 3)
        val pnCounter1 = PNCounter(1, 3)
        val pnCounter2 = PNCounter(2, 3)
        val t1 = Thread {
            for (i in 0..99) {
                for (j in 0..2) {
                    pnCounter0.increment()
                }
                for (j in 0..2) {
                    pnCounter0.dec()
                }
                for (j in 0..2) {
                    pnCounter0.increment()
                }
                pnCounter0.merge(pnCounter1)
            }
        }
        val t2 = Thread {
            for (i in 0..99) {
                for (j in 0..2) {
                    pnCounter1.increment()
                }
                for (j in 0..2) {
                    pnCounter1.dec()
                }
                for (j in 0..2) {
                    pnCounter1.increment()
                }
                pnCounter1.merge(pnCounter2)
            }
        }
        val t3 = Thread {
            for (i in 0..99) {
                for (j in 0..2) {
                    pnCounter2.increment()
                }
                for (j in 0..2) {
                    pnCounter2.dec()
                }
                for (j in 0..2) {
                    pnCounter2.increment()
                }
                pnCounter2.merge(pnCounter0)
            }
        }
        t1.start()
        t2.start()
        t3.start()
        t1.join()
        t2.join()
        t3.join()
        pnCounter0.merge(pnCounter1)
        pnCounter0.merge(pnCounter2)
        Assertions.assertEquals(900, pnCounter0.get())
    }
}
