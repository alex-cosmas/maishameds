package org.maishameds.ui.views

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ScrollToAction
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.screen.Screen.Companion.idle
import com.agoda.kakao.text.KSnackbar
import com.agoda.kakao.text.KTextView
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.mock.declare
import org.maishameds.R
import org.maishameds.data.repository.PostRepository
import org.maishameds.fake.fakePost
import org.maishameds.ui.viewmodel.PostViewModel

class RecyclerviewScrollActions(private val original: ScrollToAction = ScrollToAction()) :
    ViewAction by original {

    override fun getConstraints(): Matcher<View> = CoreMatchers.anyOf(
        allOf(
            withEffectiveVisibility(Visibility.VISIBLE),
            isDescendantOfA(isAssignableFrom(RecyclerView::class.java))
        ),
        original.constraints
    )
}

@LargeTest
@RunWith(AndroidJUnit4::class)
class DashboardActivityTest : KoinTest {

    private val postRepository = mockk<PostRepository>(relaxUnitFun = true)

    @After
    fun tearDown() {
        clearMocks(postRepository)
    }

    @Test
    fun testData_isDisplayed_whenPositiveResults_areReturned() = runBlocking {

        coEvery {
            postRepository.getPosts()
        } returns flowOf(fakePost)

        declare {
            PostViewModel(
                postRepository
            )
        }

        ActivityScenario.launch(DashboardActivity::class.java)

        Screen.onScreen<MaishaMedsScreen> {

            this.posts {

                act {
                    RecyclerviewScrollActions()
                }

                idle(3000)

                isDisplayed()

                firstChild<Item> {
                    isVisible()
                    title {
                        hasText(fakePost.first().postTitle)
                    }
                    body {
                        hasText(fakePost.first().postBody)
                    }
                }

            }
        }

        idle(3000)
    }

    class MaishaMedsScreen : Screen<MaishaMedsScreen>() {
        val posts: KRecyclerView = KRecyclerView(
            {
                withId(R.id.recyclerView)
            },
            itemTypeBuilder = {
                itemType(::Item)
            }
        )
        val snackbar = KSnackbar()
    }

    class Item(parent: Matcher<View>) : KRecyclerItem<Item>(parent) {
        val title = KTextView { withId(R.id.textViewPostTitle) }
        val body = KTextView { withId(R.id.textViewPostBody) }
    }
}