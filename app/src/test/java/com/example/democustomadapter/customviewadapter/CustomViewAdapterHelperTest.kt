package com.example.democustomadapter.customviewadapter

import com.example.democustomadapter.customviewadapter.CustomViewAdapter.CustomItem
import com.example.democustomadapter.customviewadapter.CustomViewAdapterHelper.ViewHolderType.CustomType
import com.example.democustomadapter.customviewadapter.CustomViewAdapterHelper.ViewHolderType.NormalType
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Before

import org.junit.Test

class CustomViewAdapterHelperTest {

    lateinit var SUT: CustomViewAdapterHelper<String>

    lateinit var mockAdapter: MockAdapter<String>

    @Before
    fun setUp() {
        mockAdapter = spyk(MockAdapter())
        SUT = CustomViewAdapterHelper(mockAdapter)
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
     * When: Submit normal data { "a", "b", "c", Footer }
     * Then: footer is in bottom
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
            mockAdapter.spyOnBindNormalViewHolder(1,"b")
            mockAdapter.spyOnBindNormalViewHolder(2,"c")
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

    companion object {
        private const val FOOTER_LAYOUT_ID = 123
        private const val HEADER_LAYOUT_ID = 789
    }

    @Suppress("UNCHECKED_CAST")
    private fun givenCurrentList(vararg currentList: Any) {
        mockAdapter.data = mutableListOf(*currentList)
        val customItems = SUT.javaClass.getDeclaredField("customItems").let { field ->
            field.isAccessible = true
            field.get(SUT) as MutableList<Any>
        }
        customItems.addAll(mutableListOf(*currentList).filterIsInstance<CustomItem>())
    }

    @Suppress("UNCHECKED_CAST")
    class MockAdapter<T> : CustomViewAdapterHelperDelegate {

        lateinit var helper: CustomViewAdapterHelper<T>

        var data: MutableList<Any> = mutableListOf()

        override val currentList: MutableList<Any> get() = data

        override fun submitList(list: MutableList<Any>) {
            data = list
            for (i in (0 until data.size)) {
                val itemType = helper.getItemViewType(i)
                when(val type = helper.createViewHolder(itemType)) {
                    is NormalType -> {
                        helper.onBindViewHolder(i) { normalPos ->
                            spyOnBindNormalViewHolder(normalPos, helper.getNormalItem(normalPos))
                        }
                    }
                    is CustomType -> spyOnCustomViewCreated(type.customItem.layoutId)
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

    class Header : CustomItem {
        override val layoutId: Int get() = HEADER_LAYOUT_ID

        override fun getInsertPosition(itemCount: Int): Int = 0
    }
}