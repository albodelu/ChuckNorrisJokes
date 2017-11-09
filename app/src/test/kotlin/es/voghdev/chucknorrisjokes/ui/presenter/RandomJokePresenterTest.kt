package es.voghdev.chucknorrisjokes.ui.presenter

import com.nhaarman.mockito_kotlin.whenever
import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.model.Joke
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class RandomJokePresenterTest {
    @Mock lateinit var mockResLocator: ResLocator

    @Mock lateinit var mockNavigator: RandomJokePresenter.Navigator

    @Mock lateinit var mockView: RandomJokePresenter.MVPView

    @Mock lateinit var mockChuckNorrisRepository: ChuckNorrisRepository

    lateinit var presenter: RandomJokePresenter

    val exampleJoke = Joke(
            id = "GdEH64AkS9qEQCmqMwM2Rg",
            iconUrl = "https://assets.chucknorris.host/img/avatar/chuck-norris.png",
            url = "http://api.chucknorris.io/jokes/GdEH64AkS9qEQCmqMwM2Rg",
            value = "Chuck Norris knows how to say souffle in the French language."
    )

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = createMockedPresenter()
    }

    @Test
    fun `should request a random joke to the API on start`() {
        whenever(mockChuckNorrisRepository.getRandomJoke()).thenReturn(Pair(exampleJoke, null))

        runBlocking { presenter.initialize() }

        verify(mockChuckNorrisRepository, times(1)).getRandomJoke()
    }

    @Test
    fun `should show the joke's text when a random joke is received`() {
        whenever(mockChuckNorrisRepository.getRandomJoke()).thenReturn(Pair(exampleJoke, null))

        runBlocking { presenter.initialize() }

        verify(mockView, times(1)).showJokeText("Chuck Norris knows how to say souffle in the French language.")
    }

    @Test
    fun `should show the joke's image when a random joke is received`() {
        whenever(mockChuckNorrisRepository.getRandomJoke()).thenReturn(Pair(exampleJoke, null))

        runBlocking { presenter.initialize() }

        verify(mockView, times(1)).showJokeImage("https://assets.chucknorris.host/img/avatar/chuck-norris.png")
    }

    @Test
    fun `should not download any image if url is empty`() {
        val jokeWithEmptyImage = exampleJoke.copy(iconUrl = "")
        whenever(mockChuckNorrisRepository.getRandomJoke()).thenReturn(Pair(jokeWithEmptyImage, null))

        runBlocking { presenter.initialize() }

        verify(mockView, times(0)).showJokeImage(anyString())
    }

    private fun createMockedPresenter(): RandomJokePresenter {
        val presenter = RandomJokePresenter(mockResLocator, mockChuckNorrisRepository)
        presenter.view = mockView
        presenter.navigator = mockNavigator
        return presenter
    }
}
