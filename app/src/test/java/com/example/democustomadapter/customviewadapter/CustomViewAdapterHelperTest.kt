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

    lateinit var SUT: CustomViewAdapterHelper<Int>

    lateinit var mockAdapter: MockAdapter

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
     * Given: 3 Data { 1, 2, 3}
     * When: Submit normal data
     * Then: onBindNormalViewHolder in correct sequence
     */
    @Test
    fun testSubmitNormalList() {
        val data = listOf(1, 2, 3)

        SUT.submitNormalList(data)

        verifyOrder {
            mockAdapter.spyOnBindNormalViewHolder(1)
            mockAdapter.spyOnBindNormalViewHolder(2)
            mockAdapter.spyOnBindNormalViewHolder(3)
        }
    }

    /**
     * Given: Current data is { Footer }
     * When: Submit normal data { 1, 2, 3, Footer }
     * Then: footer is in bottom
     */
    @Test
    fun testFooterIsInBottom() {
        givenCurrentList(Footer())

        SUT.submitNormalList(listOf(1, 2, 3))

        verifyOrder {
            mockAdapter.spyOnBindNormalViewHolder(1)
            mockAdapter.spyOnBindNormalViewHolder(2)
            mockAdapter.spyOnBindNormalViewHolder(3)
            mockAdapter.spyOnCustomViewCreated(FOOTER_LAYOUT_ID)
        }
    }

    /**
     * Given: Current data is { 1, 2, 3, Footer }
     * When: Submit normal data { 1 }
     * Then: Sequence is { 1, Footer }
     */
    @Test
    fun testShrinkList() {
        givenCurrentList(1, 2, 3, Footer())

        SUT.submitNormalList(listOf(1))

        verifyOrder {
            mockAdapter.spyOnBindNormalViewHolder(1)
            mockAdapter.spyOnCustomViewCreated(FOOTER_LAYOUT_ID)
        }
    }

    /**
     * Give: Current data is { Header, Footer }
     * When: Submit normal data { 1, 2, 3 }
     * Then: Sequence is { Header, 1, 2, 3, Footer }
     */
    @Test
    fun testHeaderAndFooter() {
        givenCurrentList(Header(), Footer())

        SUT.submitNormalList(listOf(1, 2, 3))

        verifyOrder {
            mockAdapter.spyOnCustomViewCreated(HEADER_LAYOUT_ID)
            mockAdapter.spyOnBindNormalViewHolder(1)
            mockAdapter.spyOnBindNormalViewHolder(2)
            mockAdapter.spyOnBindNormalViewHolder(3)
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
    class MockAdapter : CustomViewAdapterHelperDelegate {

        lateinit var helper: CustomViewAdapterHelper<Int>

        var data: MutableList<Any> = mutableListOf()

        override val currentList: MutableList<Any> get() = data

        override fun submitList(list: MutableList<Any>) {
            data = list
            for (i in (0 until data.size)) {
                val itemType = helper.getItemViewType(i)
                when(val type = helper.createViewHolder(itemType)) {
                    is NormalType -> {
                        helper.onBindViewHolder(i) { normalPos ->
                            spyOnBindNormalViewHolder(helper.getNormalItem(normalPos))
                        }
                    }
                    is CustomType -> spyOnCustomViewCreated(type.customItem.layoutId)
                }
            }
        }

        fun spyOnCustomViewCreated(layoutRes: Int) {

        }

        fun spyOnBindNormalViewHolder(data: Int) {

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