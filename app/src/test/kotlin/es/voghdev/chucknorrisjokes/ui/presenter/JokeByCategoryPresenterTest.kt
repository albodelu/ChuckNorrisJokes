package es.voghdev.chucknorrisjokes.ui.presenter

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.whenever
import es.voghdev.chucknorrisjokes.anyCategory
import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.model.Joke
import es.voghdev.chucknorrisjokes.model.JokeCategory
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class JokeByCategoryPresenterTest() {
    @Mock lateinit var mockResLocator: ResLocator

    @Mock lateinit var mockNavigator: JokeByCategoryPresenter.Navigator

    @Mock lateinit var mockView: JokeByCategoryPresenter.MVPView

    lateinit var presenter : JokeByCategoryPresenter

    @Mock lateinit var mockChuckNorrisRepository : ChuckNorrisRepository

    val categories = listOf(
            JokeCategory("Politics"),
            JokeCategory("Sports")
    )

    val exampleJoke = Joke(id = "abc",
            iconUrl = "http://chuck.image.url",
            url = "http://example.url",
            value = "We have our fears, fear has its Chuck Norris'es")

    val categoryCaptor = argumentCaptor<JokeCategory>()
    val strCaptor = argumentCaptor<String>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = createMockedPresenter()
    }

    @Test
    fun `should request all available categories on start`() {
        whenever(mockChuckNorrisRepository.getJokeCategories()).thenReturn(Pair(categories, null))

        runBlocking {
            presenter.initialize()
        }

        verify(mockChuckNorrisRepository).getJokeCategories()
    }

    @Test
    fun `should fill category names in a spinner when categories are received from API`() {
        whenever(mockChuckNorrisRepository.getJokeCategories()).thenReturn(Pair(categories, null))

        runBlocking {
            presenter.initialize()
        }

        verify(mockView).fillCategoriesSpinner(anyList())
    }

    @Test
    fun `should request a joke by category to the API when "search" button is clicked`() {
        whenever(mockChuckNorrisRepository.getJokeCategories()).thenReturn(Pair(categories, null))
        whenever(mockChuckNorrisRepository.getRandomJokeByCategory(anyCategory())).thenReturn(Pair(exampleJoke, null))

        runBlocking {
            presenter.initialize()

            presenter.onSearchButtonClicked(1)
        }

        verify(mockChuckNorrisRepository).getRandomJokeByCategory(anyCategory())
    }

    @Test
    fun `should show the joke's text when the API returns a joke by category`() {
        whenever(mockChuckNorrisRepository.getJokeCategories()).thenReturn(Pair(categories, null))
        whenever(mockChuckNorrisRepository.getRandomJokeByCategory(anyCategory())).thenReturn(Pair(exampleJoke, null))

        runBlocking {
            presenter.initialize()

            presenter.onSearchButtonClicked(1)
        }

        verify(mockView).showJokeText("We have our fears, fear has its Chuck Norris'es")
    }

    @Test
    fun `should show the joke's image when API returns a joke by category`() {
        whenever(mockChuckNorrisRepository.getJokeCategories()).thenReturn(Pair(categories, null))
        whenever(mockChuckNorrisRepository.getRandomJokeByCategory(anyCategory())).thenReturn(Pair(exampleJoke, null))

        runBlocking {
            presenter.initialize()

            presenter.onSearchButtonClicked(1)
        }

        verify(mockView).showJokeImage("http://chuck.image.url")
    }

    private fun createMockedPresenter(): JokeByCategoryPresenter {
        val presenter = JokeByCategoryPresenter(mockResLocator, mockChuckNorrisRepository)
        presenter.view = mockView
        presenter.navigator = mockNavigator
        return presenter
    }
}
