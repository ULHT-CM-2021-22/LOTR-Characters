package pt.ulusofona.cm.lotrcharacters

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pt.ulusofona.cm.lotrcharacters.activities.MainActivity
import pt.ulusofona.cm.lotrcharacters.data.remote.retrofit.LOTRServiceWithRetrofit
import pt.ulusofona.cm.lotrcharacters.data.remote.retrofit.RetrofitBuilder

val mockResponse = """
{
"docs": [
    {
        "_id": "5cd99d4bde30eff6ebccfbbe",
        "race": "Human",
        "gender": "Female",
        "birth": "",
        "spouse": "Belemir",
        "death": "",
        "realm": "",
        "name": "Mock Adanel",
        "wikiUrl": "http://lotr.wikia.com//wiki/Adanel"
    },
    {
        "_id": "5cd99d4bde30eff6ebccfbbf",
        "race": "Human",
        "gender": "Male",
        "birth": "Before ,TA 1944",
        "spouse": "",
        "death": "Late ,Third Age",
        "realm": "",
        "name": "Mock Adrahil I",
        "wikiUrl": "http://lotr.wikia.com//wiki/Adrahil_I"
    }
  ]
}
""".trimIndent()

@RunWith(AndroidJUnit4::class)
class TestApplication {

    /**
     * Use [ActivityScenarioRule] to create and launch the activity under test before each test,
     * and close it after each test.
     */
    @get:Rule
    var activityScenarioRule = activityScenarioRule<MainActivity>()

    lateinit var server: MockWebServer

    @Before
    fun setupMockWebserver() {
        server = MockWebServer()
        server.start()
    }

    @Test
    fun showsInitialButton() {
        onView(withId(R.id.getCharactersBtn)).check(matches(withText("Set Characters")))
    }

    @Test
    fun showCharactersList() {

        server.enqueue(MockResponse().setBody(mockResponse))

        // inject a new (mock) model into the activity's view model
        activityScenarioRule.scenario.onActivity { activity ->
            CoroutineScope(Dispatchers.IO).launch {   // since instrumented tests run on the Main Thread...
                activity.viewModel.model =
                    LOTRServiceWithRetrofit(RetrofitBuilder.getInstance(server.url("").toString()))
            }
        }

        onView(withId(R.id.getCharactersBtn)).perform(click())

        Thread.sleep(500)  // TODO: Ugly, should look into https://developer.android.com/training/testing/espresso/idling-resource

        onView(withId(R.id.charactersListRv)).check(matches(atPosition(0, withText("Mock Adanel"))));
        onView(withId(R.id.charactersListRv)).check(matches(atPosition(1, withText("Mock Adrahil I"))));
    }

    @After
    fun shutdownMockWebserver() {
        server.shutdown();
    }

}