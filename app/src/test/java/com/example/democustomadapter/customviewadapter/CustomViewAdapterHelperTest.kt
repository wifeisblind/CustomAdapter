package com.example.democustomadapter.customviewadapter

import com.example.democustomadapter.customviewadapter.CustomViewAdapter.CustomItem
import com.example.democustomadapter.customviewadapter.CustomViewAdapterHelper.ViewHolderType.CustomType
import com.example.democustomadapter.customviewadapter.CustomViewAdapterHelper.ViewHolderType.NormalType
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.spyk
import io.mockk.verify
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
    fun givenAFooter_whenInsertAFooter_thenFooterIsCreated() {
        val footer = Footer()

        SUT.insertCustomItem(footer)

        verify {
            mockAdapter.spyOnCustomViewCreated(FOOTER_LAYOUT_ID)
        }
    }

    companion object {
        private const val FOOTER_LAYOUT_ID = 123
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
                            spyOnBindNormalViewHolder(normalPos, helper.getNormalItem(normalPos))
                        }
                    }
                    is CustomType -> spyOnCustomViewCreated(type.customItem.layoutId)
                }
            }
        }

        fun spyOnCustomViewCreated(layoutRes: Int) {

        }

        fun spyOnBindNormalViewHolder(normalPos: Int, data: Int) {

        }
    }

    class Footer : CustomItem {
        override val layoutId: Int get() = FOOTER_LAYOUT_ID

        override fun getInsertPosition(itemCount: Int): Int = itemCount - 1
    }
}