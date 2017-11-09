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
            if (result.success()) {
                categories = result.first ?: emptyList()

                view?.fillCategoriesSpinner(result.first ?: emptyList())
            }
        }
    }

    interface MVPView {
        fun fillCategoriesSpinner(list: List<JokeCategory>)
        fun showJokeText(text: String)
        fun showJokeImage(s: String)

    }

    interface Navigator {

    }

    suspend fun onSearchButtonClicked(position: Int) {
        val category = categories.elementAt(position)

        coroutine {
            repository.getRandomJokeByCategory(category)
        }.await().let { result ->
            if(result.success()) {
                view?.showJokeText(result.first?.value ?: "")
                view?.showJokeImage(result.first?.iconUrl ?: "")
            }
        }
    }
}
