package es.voghdev.chucknorrisjokes.ui.presenter

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import es.voghdev.chucknorrisjokes.app.ResLocator
import io.kotlintest.specs.StringSpec
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class MainPresenterTest : StringSpec(
    {
        val mockResLocator: ResLocator = mock()

        val mockNavigator: MainPresenter.Navigator = mock()

        val mockView: MainPresenter.MVPView = mock()

        fun createMockedPresenter(): MainPresenter {
            val presenter = MainPresenter(mockResLocator)
            presenter.view = mockView
            presenter.navigator = mockNavigator
            return presenter
        }

        val presenter = createMockedPresenter()

        "The Three tabs should be configured when App is started" {
            runBlocking {
                presenter.initialize()
            }

            verify(mockView).configureTabs()
        }
    }
)