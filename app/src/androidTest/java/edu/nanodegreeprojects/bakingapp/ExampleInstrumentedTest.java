package edu.nanodegreeprojects.bakingapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivityAllRecipes> mActivityTestRule =
            new ActivityTestRule<>(MainActivityAllRecipes.class);


    @Test
    public void mainTest() {
        //open Brownie item
        onView((withText(R.string.test_item_name)))
                .perform(click());

        //click on ingredients list
        ViewInteraction ingredientListItem = onView(withText(R.string.ingredients_title));
        if (ingredientListItem != null)
            ingredientListItem.perform(click());

        ViewInteraction ingredientsBrownie = onView(withId(R.id.favorite_icon));
        if (ingredientsBrownie != null)
            ingredientsBrownie.perform(click());

        pressBack();

        assertTrue((ingredientsBrownie != null) && (ingredientListItem != null));


    }
}
