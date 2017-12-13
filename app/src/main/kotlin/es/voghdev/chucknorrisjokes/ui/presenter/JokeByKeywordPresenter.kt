package es.voghdev.chucknorrisjokes.ui.presenter

import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.app.coroutine
import es.voghdev.chucknorrisjokes.model.Joke
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository

class JokeByKeywordPresenter(val context: ResLocator, val repository: ChuckNorrisRepository) :
        Presenter<JokeByKeywordPresenter.MVPView, JokeByKeywordPresenter.Navigator>() {

    override suspend fun initialize() {

    }

    interface MVPView {
        fun showError(error: String)
        fun addJoke(joke: Joke)
        fun showEmptyCase()

    }

    interface Navigator {

    }

    suspend fun onSearchButtonClicked(keyword: String) {
        if (keyword.isEmpty()) {
            view?.showError("Please enter keyword")
            return
        }

        val task = coroutine {
            repository.getRandomJokeByKeyword(keyword.toLowerCase())
        }

        val result = task.await()

        if (result.first?.isEmpty() ?: false)
            view?.showEmptyCase()

        result.first?.forEach { joke ->
            view?.addJoke(joke)
        }
    }
}
