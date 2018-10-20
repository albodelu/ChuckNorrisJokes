/*
 * Copyright (C) 2018 Olmo Gallegos Hernández.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.voghdev.chucknorrisjokes.ui.presenter

import arrow.core.Either
import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.app.coroutine
import es.voghdev.chucknorrisjokes.model.Joke
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository

class JokeByKeywordPresenter(val context: ResLocator, val repository: ChuckNorrisRepository) :
        Presenter<JokeByKeywordPresenter.MVPView, JokeByKeywordPresenter.Navigator>() {

    override suspend fun initialize() {

    }

    suspend fun onSearchButtonClicked(searchText: String) {
        if (searchText.isEmpty()) {
            view?.showError("Search text cannot be empty")
            return
        }

        val result = coroutine {
            repository.getRandomJokeByKeyword(searchText)
        }.await()

        if(result is Either.Right) {
            result.b.forEach { joke ->
                view?.addJoke(joke)
            }

            if (result.b.isEmpty()) {
                view?.showEmptyCase()
            } else {
                view?.hideEmptyCase()
            }
        }

    }

    interface MVPView {
        fun showError(message: String)
        fun addJoke(joke: Joke)
        fun showEmptyCase()
        fun hideEmptyCase()

    }

    interface Navigator {

    }
}
