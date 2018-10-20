package es.voghdev.chucknorrisjokes.ui.presenter

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.app.asResult
import es.voghdev.chucknorrisjokes.model.Joke
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository
import io.kotlintest.specs.StringSpec
import kotlinx.coroutines.experimental.runBlocking

class RandomJokePresenterTest : StringSpec(
        {
            val mockResLocator: ResLocator = mock()

            val mockNavigator: RandomJokePresenter.Navigator = mock()

            val mockView: RandomJokePresenter.MVPView = mock()

            val mockChuckNorrisRepository: ChuckNorrisRepository = mock()

            fun createMockedPresenter(): RandomJokePresenter {
                val presenter = RandomJokePresenter(mockResLocator, mockChuckNorrisRepository)
                presenter.view = mockView
                presenter.navigator = mockNavigator
                return presenter
            }

            val presenter = createMockedPresenter()

            val exampleJoke = Joke(
                    id = "GdEH64AkS9qEQCmqMwM2Rg",
                    iconUrl = "https://assets.chucknorris.host/img/avatar/chuck-norris.png",
                    url = "http://api.chucknorris.io/jokes/GdEH64AkS9qEQCmqMwM2Rg",
                    value = "Chuck Norris knows how to say souffle in the French language."
            )

            "this screen should request a random joke to the Api on start" {
                runBlocking {
                    presenter.initialize()
                }

                verify(mockChuckNorrisRepository).getRandomJoke()
            }

            "should show the joke's text when a Joke is received from the Api" {
                givenTheApiReturns(mockChuckNorrisRepository, exampleJoke)

                runBlocking {
                    presenter.initialize()
                }

                verify(mockView).showJokeText("Chuck Norris knows how to say souffle in the French language.")
            }

            "should show the joke's image when a Joke is received from the Api" {
                givenTheApiReturns(mockChuckNorrisRepository, exampleJoke)

                runBlocking {
                    presenter.initialize()
                }

                verify(mockView).showJokeImage("https://assets.chucknorris.host/img/avatar/chuck-norris.png")
            }
        }
)

private fun givenTheApiReturns(mockChuckNorrisRepository: ChuckNorrisRepository, joke: Joke) {
    whenever(mockChuckNorrisRepository.getRandomJoke()).thenReturn(joke.asResult())
}