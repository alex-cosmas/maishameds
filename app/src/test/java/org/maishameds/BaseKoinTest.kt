package org.maishameds

import org.junit.Rule
import org.koin.test.KoinTest
import org.maishameds.util.KoinTestRule

abstract class BaseKoinTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule()
}