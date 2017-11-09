package es.voghdev.chucknorrisjokes.ui.presenter

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.model.Joke
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class JokeByKeywordPresenterTest() {
    @Mock lateinit var mockResLocator: ResLocator

    @Mock lateinit var mockNavigator: JokeByKeywordPresenter.Navigator

    @Mock lateinit var mockView: JokeByKeywordPresenter.MVPView

    @Mock lateinit var mockChuckNorrisRepository: ChuckNorrisRepository

    lateinit var presenter: JokeByKeywordPresenter

    val exampleJoke = Joke(
            id = "GdEH64AkS9qEQCmqMwM2Rg",
            iconUrl = "https://assets.chucknorris.host/img/avatar/chuck-norris.png",
            url = "http://api.chucknorris.io/jokes/GdEH64AkS9qEQCmqMwM2Rg",
            value = "Chuck Norris knows how to say souffle in the French language."
    )

    val anotherJoke = Joke(id = "abc",
            iconUrl = "http://chuck.image.url",
            url = "http://example.url",
            value = "We have our fears, fear has its Chuck Norris'es")

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = createMockedPresenter()
    }

    @Test
    fun `should display an error if search button is clicked and text is empty`() {
        runBlocking {
            presenter.initialize()

            presenter.onSearchButtonClicked("")
        }

        val captor = argumentCaptor<String>()

        verify(mockView).showError(captor.capture())

        assertTrue(captor.firstValue.length <= 20)
    }

    @Test
    fun `should request a joke by keyword if text is not empty`() {
        whenever(mockChuckNorrisRepository.getRandomJokeByKeyword(anyString())).thenReturn(
                Pair(
                        listOf(exampleJoke, anotherJoke),
                        null)
        )

        runBlocking {
            presenter.initialize()

            presenter.onSearchButtonClicked("Bruce lee")
        }

        verify(mockChuckNorrisRepository).getRandomJokeByKeyword("bruce lee")
    }

    @Test
    fun `should show the joke's text when a joke by keyword is returned by the API`() {
        whenever(mockChuckNorrisRepository.getRandomJokeByKeyword(anyString())).thenReturn(
                Pair(
                        listOf(exampleJoke, anotherJoke),
                        null)
        )

        runBlocking {
            presenter.initialize()

            presenter.onSearchButtonClicked("Bruce lee")
        }

        verify(mockView).showJokeText("Chuck Norris knows how to say souffle in the French language.")
    }

    @Test
    fun `should show the joke's image etc`() {
        whenever(mockChuckNorrisRepository.getRandomJokeByKeyword(anyString())).thenReturn(
                Pair(
                        listOf(exampleJoke, anotherJoke),
                        null)
        )

        runBlocking {
            presenter.initialize()

            presenter.onSearchButtonClicked("Bruce lee")
        }

        verify(mockView).showJokeImage("https://assets.chucknorris.host/img/avatar/chuck-norris.png")
    }

    private fun createMockedPresenter(): JokeByKeywordPresenter {
        val presenter = JokeByKeywordPresenter(mockResLocator, mockChuckNorrisRepository)
        presenter.view = mockView
        presenter.navigator = mockNavigator
        return presenter
    }
}
