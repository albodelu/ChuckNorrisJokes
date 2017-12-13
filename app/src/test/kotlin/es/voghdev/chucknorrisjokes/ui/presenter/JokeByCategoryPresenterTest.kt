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
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class JokeByCategoryPresenterTest() {
    @Mock lateinit var mockResLocator: ResLocator

    @Mock lateinit var mockNavigator: JokeByCategoryPresenter.Navigator

    @Mock lateinit var mockView: JokeByCategoryPresenter.MVPView

    lateinit var presenter: JokeByCategoryPresenter

    @Mock lateinit var mockChuckNorrisRepository: ChuckNorrisRepository

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

    @Test
    fun `should request a list of categories`() {
        whenever(mockChuckNorrisRepository.getJokeCategories()).thenReturn(Pair(categories, null))

        runBlocking {
            presenter.initialize()
        }

        verify(mockChuckNorrisRepository, times(1)).getJokeCategories()
    }

    @Test
    fun `should fill category names in a spinner when the list of categories is received`() {
        givenTheApiReturns(categories)

        runBlocking {
            presenter.initialize()
        }

        verify(mockView).fillCategoriesSpinner(anyList())
    }

    private fun givenTheApiReturns(someCategories: List<JokeCategory>) {
        whenever(mockChuckNorrisRepository.getJokeCategories()).thenReturn(Pair(someCategories, null))
    }

    @Test
    fun `should perform a search with selected category when "search" button is clicked`() {
        givenTheApiReturns(categories)
        givenTheApiReturnsAJoke()

        runBlocking {
            presenter.initialize()
            presenter.onSearchButtonClicked(1)
        }

        verify(mockChuckNorrisRepository).getRandomJokeByCategory(anyCategory())
    }

    @Test
    fun `should show the joke's text when a joke is received`() {
        givenTheApiReturns(categories)
        givenTheApiReturnsAJoke()

        runBlocking {
            presenter.initialize()
            presenter.onSearchButtonClicked(1)
        }

        verify(mockView).showJokeText("We have our fears, fear has its Chuck Norris'es")
    }

    @Test
    fun `should show the joke's image when a joke by category is received`() {
        givenTheApiReturns(categories)
        givenTheApiReturnsAJoke()

        runBlocking {
            presenter.initialize()
            presenter.onSearchButtonClicked(1)
        }

        verify(mockView).showJokeImage("http://chuck.image.url")
    }

    private fun givenTheApiReturnsAJoke() {
        whenever(mockChuckNorrisRepository.getRandomJokeByCategory(anyCategory())).thenReturn(Pair(exampleJoke, null))
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = createMockedPresenter()
    }

    private fun createMockedPresenter(): JokeByCategoryPresenter {
        val presenter = JokeByCategoryPresenter(mockResLocator, mockChuckNorrisRepository)
        presenter.view = mockView
        presenter.navigator = mockNavigator
        return presenter
    }
}
