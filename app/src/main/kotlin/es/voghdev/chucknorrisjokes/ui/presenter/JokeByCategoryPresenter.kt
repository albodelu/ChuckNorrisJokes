package es.voghdev.chucknorrisjokes.ui.presenter

import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.app.coroutine
import es.voghdev.chucknorrisjokes.app.success
import es.voghdev.chucknorrisjokes.model.JokeCategory
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository

class JokeByCategoryPresenter(val context: ResLocator, val repository: ChuckNorrisRepository) :
        Presenter<JokeByCategoryPresenter.MVPView, JokeByCategoryPresenter.Navigator>() {

    var categories: List<JokeCategory> = emptyList()

    override suspend fun initialize() {
        coroutine {
            repository.getJokeCategories()
        }.await().let { result ->
            categories = result.first ?: emptyList()
            if (result.success()) {
                view?.fillCategoriesSpinner(result.first ?: emptyList())
            }
        }
    }

    suspend fun onSearchButtonClicked(position: Int) {
        val task = coroutine {
            repository.getRandomJokeByCategory(categories[position])
        }

        val result = task.await()

        if (result.success()) {
            view?.showJokeText(result.first?.value ?: "")
            view?.showJokeImage(result.first?.iconUrl ?: "")
        }
    }

    interface MVPView {
        fun fillCategoriesSpinner(list: List<JokeCategory>)
        fun showJokeText(text: String)
        fun showJokeImage(url: String)

    }

    interface Navigator {

    }

}
