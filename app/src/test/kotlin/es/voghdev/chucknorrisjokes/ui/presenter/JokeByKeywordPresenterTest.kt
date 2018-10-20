package es.voghdev.chucknorrisjokes.ui.presenter

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import es.voghdev.chucknorrisjokes.anyJoke
import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.app.asResult
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

            fun createMockedPresenter(): JokeByKeywordPresenter {
                val presenter = JokeByKeywordPresenter(mockResLocator, mockChuckNorrisRepository)
                presenter.view = mockView
                presenter.navigator = mockNavigator
                return presenter
            }

            val presenter = createMockedPresenter()

            "should search jokes by keyword when 'Search' Button is tapped" {
                runBlocking {
                    presenter.initialize()

                    presenter.onSearchButtonClicked("Bruce Lee")
                }

                verify(mockChuckNorrisRepository).getRandomJokeByKeyword("Bruce Lee")
            }

            "should show an error if Search text is empty" {
                runBlocking {
                    presenter.initialize()

                    presenter.onSearchButtonClicked("")
                }

                verify(mockView).showError("Search text cannot be empty")
            }

            val aJoke = Joke(
                    id = "GdEH64AkS9qEQCmqMwM2Rg",
                    iconUrl = "https://assets.chucknorris.host/img/avatar/chuck-norris.png",
                    url = "http://api.chucknorris.io/jokes/GdEH64AkS9qEQCmqMwM2Rg",
                    value = "Chuck Norris knows how to say souffle in the French language."
            )

            val anotherJoke = Joke(id = "abc",
                    iconUrl = "http://chuck.image.url",
                    url = "http://example.url",
                    value = "We have our fears, fear has its Chuck Norris'es")

                val someJokes = listOf(aJoke, anotherJoke)
            "should show two jokes if the Api returns two results" {

                whenever(mockChuckNorrisRepository.getRandomJokeByKeyword(anyString())).thenReturn(someJokes.asResult())

                runBlocking {
                    presenter.initialize()

                    presenter.onSearchButtonClicked("Bruce Lee")
                }

                verify(mockView, times(2)).addJoke(anyJoke())
            }

            "should show the empty case if search returns zero results" {
                whenever(mockChuckNorrisRepository.getRandomJokeByKeyword(anyString())).thenReturn(emptyList<Joke>().asResult())

                runBlocking {
                    presenter.initialize()

                    presenter.onSearchButtonClicked("Bruce Lee")
                }

                verify(mockView).showEmptyCase()
            }

            "should hide the empty case if search returns zero results" {
                whenever(mockChuckNorrisRepository.getRandomJokeByKeyword(anyString())).thenReturn(someJokes.asResult())

                runBlocking {
                    presenter.initialize()

                    presenter.onSearchButtonClicked("Bruce Lee")
                }

                verify(mockView).hideEmptyCase()
            }
        })