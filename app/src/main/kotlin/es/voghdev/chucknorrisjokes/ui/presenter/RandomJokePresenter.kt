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

import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RandomJokePresenter(val dispatcher: CoroutineDispatcher, val resLocator: ResLocator, val repository: ChuckNorrisRepository) :
    Presenter<RandomJokePresenter.MVPView, RandomJokePresenter.Navigator>() {

    override fun initialize() {
        GlobalScope.launch(dispatcher) {
            async { repository.getRandomJoke() }
                .await()
                .fold({}, {
                    view?.showJokeText(it.value)

                    if (it.iconUrl.isNotEmpty())
                        view?.loadJokeImage(it.iconUrl)
                })
        }
    }

    interface MVPView {
        fun showJokeText(text: String)
        fun loadJokeImage(url: String)
    }

    interface Navigator {

    }
}
