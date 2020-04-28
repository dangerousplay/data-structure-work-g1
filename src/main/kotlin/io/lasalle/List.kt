package io.lasalle

class List<T>(private var inner: Array<T?>, private var len: Int) : MutableList<T> {

    companion object {
        inline fun <reified T> of(): List<T?> = List(emptyArray(), 0)
    }

    private fun grow(capacity: Int) {
        this.inner = this.inner.copyOf(this.inner.size + capacity)
    }

    override fun iterator(): MutableIterator<T> {
        val innerIterator = this.inner.iterator()

        return object : MutableIterator<T> {
            var nextElement: T? = null

            override fun hasNext(): Boolean {
                return innerIterator.hasNext()
            }

            override fun next(): T {
                nextElement = innerIterator.next()
                return nextElement!!
            }

            override fun remove() {
                this@List.remove(nextElement)
            }
        }
    }

    override val size: Int
        get() = len

    override fun contains(element: T): Boolean = this.inner.contains(element)

    override fun containsAll(elements: Collection<T>): Boolean = elements.all { this.inner.contains(it) }

    override fun get(index: Int): T = this.inner[index]!!

    override fun indexOf(element: T): Int = this.inner.indexOf(element)

    override fun isEmpty(): Boolean = this.inner.isEmpty()

    override fun lastIndexOf(element: T): Int = this.inner.lastIndexOf(element)

    override fun add(element: T): Boolean {
        if (this.inner.size == this.len) {
            this.grow(1)
        }

        this.inner[len] = element
        len++

        return true
    }

    override fun add(index: Int, element: T) = TODO()

    override fun addAll(index: Int, elements: Collection<T>): Boolean = TODO()

    override fun addAll(elements: Collection<T>): Boolean = TODO()

    override fun clear() {
        this.inner.copyOf(0)
    }

    override fun remove(element: T): Boolean {
        val index = this.inner.indexOf(element = element)

        return if (index >= 0)
            this.fastRemove(index)
        else
            false
    }

    private fun fastRemove(index: Int): Boolean {
        val newSize = size - 1

        if (newSize > index) {
            System.arraycopy(this.inner, index + 1, this.inner, index, newSize - index)
        }

        this.inner = this.inner.copyOf(--len)

        return true
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        return elements.map { this.remove(it) }.all { it }
    }

    override fun removeAt(index: Int): T {
        val element = this[index]

        this.remove(element)

        return element!!
    }

    override fun retainAll(elements: Collection<T>): Boolean = TODO()

    override fun set(index: Int, element: T): T {
        val previous = this.inner[index]

        this.inner[index] = element

        return previous!!
    }

    override fun listIterator(): MutableListIterator<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun listIterator(index: Int): MutableListIterator<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
        return this.inner.sliceArray(fromIndex..toIndex).toMutableList() as MutableList<T>
    }
}
