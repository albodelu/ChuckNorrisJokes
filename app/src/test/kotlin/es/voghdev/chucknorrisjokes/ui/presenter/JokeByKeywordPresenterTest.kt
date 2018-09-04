package es.voghdev.chucknorrisjokes.ui.presenter

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import es.voghdev.chucknorrisjokes.anyJoke
import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.model.Joke
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository
import io.kotlintest.specs.StringSpec
import kotlinx.coroutines.experimental.runBlocking
import org.mockito.ArgumentMatchers.anyString

class JokeByKeywordPresenterTest : StringSpec(
    {
        val mockResLocator: ResLocator = mock()

        val mockNavigator: JokeByKeywordPresenter.Navigator = mock()

        val mockView: JokeByKeywordPresenter.MVPView = mock()

        val mockChuckNorrisRepository: ChuckNorrisRepository = mock()

        val exampleJoke = Joke(id = "abc",
                               iconUrl = "http://chuck.image.url",
                               url = "http://example.url",
                               value = "We have our fears, fear has its Chuck Norris'es")
        val anotherJoke = Joke(id = "abc",
                               iconUrl = "http://chuck.image.url",
                               url = "http://example.url",
                               value = "Chuck Norris created a Monad that is NOT an Endofunctor")

        fun createMockedPresenter(): JokeByKeywordPresenter {
            val presenter = JokeByKeywordPresenter(mockResLocator, mockChuckNorrisRepository)
            presenter.view = mockView
            presenter.navigator = mockNavigator
            return presenter
        }

        val presenter = createMockedPresenter()

        "The Search by keyword feature should not allow Searches with empty text" {
            runBlocking {
                presenter.onSearchButtonClicked("")
            }

            verify(mockView).showError("Text must not be empty")
        }

        "This screen should search for Jokes by keyword when 'Search' button is tapped" {
            runBlocking {
                presenter.onSearchButtonClicked("Bruce")
            }

            verify(mockChuckNorrisRepository, times(1)).getRandomJokeByKeyword("bruce")
        }

        "Should show a list of two jokes if search returns two results" {
            whenever(mockChuckNorrisRepository.getRandomJokeByKeyword(anyString())).thenReturn(
                Pair(listOf(exampleJoke, anotherJoke), null))

            runBlocking {
                presenter.onSearchButtonClicked("Bruce")
            }

            verify(mockView, times(2)).addJoke(anyJoke())
        }
    })