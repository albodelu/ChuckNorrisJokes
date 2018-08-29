package es.voghdev.chucknorrisjokes.ui.presenter

import arrow.core.Either
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import es.voghdev.chucknorrisjokes.anyCategory
import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.app.asResult
import es.voghdev.chucknorrisjokes.model.Joke
import es.voghdev.chucknorrisjokes.model.JokeCategory
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository
import io.kotlintest.specs.StringSpec
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.experimental.runBlocking

class JokeByCategoryPresenterKotlinTest : StringSpec(
    {
        val POLITICS = 0

        val mockResLocator: ResLocator = mock()

        val mockNavigator: JokeByCategoryPresenter.Navigator = mock()

        val mockView: JokeByCategoryPresenter.MVPView = mock()

        val mockChuckNorrisRepository: ChuckNorrisRepository = mock()

        fun createMockedPresenter(): JokeByCategoryPresenter {
            val presenter = JokeByCategoryPresenter(mockResLocator, mockChuckNorrisRepository)
            presenter.view = mockView
            presenter.navigator = mockNavigator
            return presenter
        }

        val presenter = createMockedPresenter()

        val someCategories = listOf(
            JokeCategory("Politics"),
            JokeCategory("Sports")
        )

        val aJoke = Joke(
            id = "abc",
            iconUrl = "http://chuck.image.url",
            url = "http://example.url",
            value = "We have our fears, fear has its Chuck Norris'es"
        )

        val listCaptor = argumentCaptor<List<JokeCategory>>()
        val categoryCaptor = argumentCaptor<JokeCategory>()
        val strCaptor = argumentCaptor<String>()

        "This screen should request all available categories on start, in order to fill categories spinner" {
            givenThereAreNoCategoriesInTheRepository(mockChuckNorrisRepository)

            runBlocking {
                presenter.initialize()
            }

            verify(mockChuckNorrisRepository).getJokeCategories()
        }

        "This screen should fill category names in the Spinner when a list of categories are received" +
            "from the Api" {
                givenThereAreSomeCategories(mockChuckNorrisRepository, someCategories)

                runBlocking {
                    presenter.initialize()
                }

                verify(mockView).fillCategories(listCaptor.capture())

                assertEquals(2, listCaptor.firstValue.size)
            }

        "This screen should search jokes by category 'Politics'" +
            "When 'Search' button is clicked" +
            "And 'Politics' has been selected in the Spinner" {
                givenThereAreSomeCategories(mockChuckNorrisRepository, someCategories)
                givenTheRepositoryHasAnExampleJoke(mockChuckNorrisRepository, aJoke)

                runBlocking {
                    presenter.initialize()

                    presenter.onSearchButtonClicked(POLITICS)
                }

                verify(mockChuckNorrisRepository).getRandomJokeByCategory(categoryCaptor.capture())
            }

        "This screen should display the returned Joke's text" +
            "When a search is performed and it returns results" {
                givenThereAreSomeCategories(mockChuckNorrisRepository, someCategories)
                givenTheRepositoryHasAnExampleJoke(mockChuckNorrisRepository, aJoke)

                runBlocking {
                    presenter.initialize()

                    presenter.onSearchButtonClicked(0)
                }

                verify(mockView).showJokeText(strCaptor.capture())

                assertEquals("We have our fears, fear has its Chuck Norris'es", strCaptor.firstValue)
            }

        "Given there are some categories in the repository" +
            "And There is an example joke in the repository" +
            "When I tap on the Search button with the 'Politics' category selected" +
            "Then the App should request a random Joke by the 'Politics' Category" {
                givenThereAreSomeCategories(mockChuckNorrisRepository, someCategories)
                givenTheRepositoryHasAnExampleJoke(mockChuckNorrisRepository, aJoke)

                runBlocking {
                    presenter.initialize()

                    presenter.onSearchButtonClicked(POLITICS)
                }

                verify(mockChuckNorrisRepository).getRandomJokeByCategory(categoryCaptor.capture())

                assertEquals(JokeCategory("Politics"), categoryCaptor.firstValue)
            }
    })

fun givenThereAreSomeCategories(mockChuckNorrisRepository: ChuckNorrisRepository, someCategories: List<JokeCategory>) {
    whenever(mockChuckNorrisRepository.getJokeCategories()).thenReturn(someCategories.asResult())
}

fun givenThereAreNoCategoriesInTheRepository(mockChuckNorrisRepository: ChuckNorrisRepository) {
    whenever(mockChuckNorrisRepository.getJokeCategories()).thenReturn(emptyList<JokeCategory>().asResult())
}

fun givenTheRepositoryHasAnExampleJoke(mockChuckNorrisRepository: ChuckNorrisRepository, exampleJoke: Joke) {
    whenever(mockChuckNorrisRepository.getRandomJokeByCategory(anyCategory())).thenReturn(Either.Right(exampleJoke))
}