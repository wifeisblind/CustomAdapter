package com.example.democustomadapter.customviewadapter

import com.example.democustomadapter.customviewadapter.CustomViewAdapter.CustomItem
import io.mockk.MockKAnnotations
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Before

import org.junit.Test

class CustomViewAdapterHelperTest {

    lateinit var SUT: CustomViewAdapterHelper<String>

    lateinit var mockAdapter: MockAdapter<String>

    lateinit var initialList: MutableList<Any>

    lateinit var customItem: MutableList<CustomItem>

    @Before
    fun setUp() {
        mockAdapter = spyk(MockAdapter())
        initialList = mutableListOf()
        customItem = mutableListOf()
        SUT = CustomViewAdapterHelper(mockAdapter, initialList, customItem)
        mockAdapter.helper = SUT
        MockKAnnotations.init(this)
    }

    /**
     *  Given: A Footer
     *  When: Insert A Footer
     *  Then: Footer is created
     */
    @Test
    fun testInsertCustomItem() {
        val footer = Footer()

        SUT.insertCustomItem(footer)

        verify {
            mockAdapter.spyOnCustomViewCreated(FOOTER_LAYOUT_ID)
        }
    }

    /**
     * Given: 3 Data { "a", "b", "c" }
     * When: Submit normal data
     * Then: onBindNormalViewHolder in correct sequence
     */
    @Test
    fun testSubmitNormalList() {
        val data = listOf( "a", "b", "c")

        SUT.submitNormalList(data)

        verifyOrder {
            mockAdapter.spyOnBindNormalViewHolder(0, "a")
            mockAdapter.spyOnBindNormalViewHolder(1, "b")
            mockAdapter.spyOnBindNormalViewHolder(2, "c")
        }
    }

    /**
     * Given: Current data is { Footer }
     * When: Submit normal data { "a", "b", "c" }
     * Then: Sequence is { "a", "b", "c", Footer }
     */
    @Test
    fun testFooterIsInBottom() {
        givenCurrentList(Footer())

        SUT.submitNormalList(listOf("a", "b", "c"))

        verifyOrder {
            mockAdapter.spyOnBindNormalViewHolder(0, "a")
            mockAdapter.spyOnBindNormalViewHolder(1, "b")
            mockAdapter.spyOnBindNormalViewHolder(2, "c")
            mockAdapter.spyOnCustomViewCreated(FOOTER_LAYOUT_ID)
        }
    }

    /**
     * Given: Current data is { "a", "b", "c", Footer }
     * When: Submit normal data { "a" }
     * Then: Sequence is { "a", Footer }
     */
    @Test
    fun testShrinkList() {
        givenCurrentList("a", "b", "c", Footer())

        SUT.submitNormalList(listOf("a"))

        verifyOrder {
            mockAdapter.spyOnBindNormalViewHolder(0, "a")
            mockAdapter.spyOnCustomViewCreated(FOOTER_LAYOUT_ID)
        }
    }

    /**
     * Give: Current data is { Header, Footer }
     * When: Submit normal data { "a", "b", "c" }
     * Then: Sequence is { Header, "a", "b", "c", Footer }
     */
    @Test
    fun testHeaderAndFooter() {
        givenCurrentList(Header(), Footer())

        SUT.submitNormalList(listOf("a", "b", "c"))

        verifyOrder {
            mockAdapter.spyOnCustomViewCreated(HEADER_LAYOUT_ID)
            mockAdapter.spyOnBindNormalViewHolder(0, "a")
            mockAdapter.spyOnBindNormalViewHolder(1, "b")
            mockAdapter.spyOnBindNormalViewHolder(2, "c")
            mockAdapter.spyOnCustomViewCreated(FOOTER_LAYOUT_ID)
        }
    }

    /**
     * Give: Current data is { Header, "a", "b", "c", Footer }
     * When: Submit empty data
     * Then: Sequence is { Header, Footer }
     */
    @Test
    fun testSubmitEmptyData() {
        givenCurrentList(Header(), "a", "b", "c", Footer())

        SUT.submitNormalList(emptyList())

        verifyOrder {
            mockAdapter.spyOnCustomViewCreated(HEADER_LAYOUT_ID)
            mockAdapter.spyOnCustomViewCreated(FOOTER_LAYOUT_ID)
        }
    }

    /**
     * Give: Current data is { "a", "b", "c", Footer }
     * When: Insert new FooterX
     * Then: Sequence is { "a", "b", "c", FooterX }
     */
    @Test
    fun testReplaceFooter() {
        givenCurrentList("a", "b", "c", Footer())

        SUT.insertCustomItem(FooterX())

        verifyOrder {
            mockAdapter.spyOnBindNormalViewHolder(0, "a")
            mockAdapter.spyOnBindNormalViewHolder(1, "b")
            mockAdapter.spyOnBindNormalViewHolder(2, "c")
            mockAdapter.spyOnCustomViewCreated(FOOTER_X_LAYOUT_ID)
        }
    }

    companion object {
        private const val FOOTER_LAYOUT_ID = 123
        private const val HEADER_LAYOUT_ID = 789
        private const val FOOTER_X_LAYOUT_ID = 996
    }

    private fun givenCurrentList(vararg currentList: Any) {
        currentList.forEach {
            if (it is CustomItem) {
                customItem.add(it)
            }
            initialList.add(it)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class MockAdapter<T> : CustomViewAdapterHelperDelegate {

        lateinit var helper: CustomViewAdapterHelper<T>

        override fun commitList(list: MutableList<Any>) {
            println(list)
            for (i in (0 until list.size)) {
                val (pos, data) = helper.bindViewHolder(i)
                when(val itemType = helper.getItemViewType(i)) {
                    -1 -> spyOnBindNormalViewHolder(pos, data as T)
                    else -> spyOnCustomViewCreated(itemType)
                }
            }
        }

        fun spyOnCustomViewCreated(layoutRes: Int) {

        }

        fun spyOnBindNormalViewHolder(normalPos: Int, data: T) {

        }
    }

    class Footer : CustomItem {
        override val layoutId: Int get() = FOOTER_LAYOUT_ID

        override fun getInsertPosition(itemCount: Int): Int = itemCount - 1
    }

    class FooterX : CustomItem {
        override val layoutId: Int get() = FOOTER_X_LAYOUT_ID

        override fun getInsertPosition(itemCount: Int): Int = itemCount - 1
    }

    class Header : CustomItem {
        override val layoutId: Int get() = HEADER_LAYOUT_ID

        override fun getInsertPosition(itemCount: Int): Int = 0
    }
}