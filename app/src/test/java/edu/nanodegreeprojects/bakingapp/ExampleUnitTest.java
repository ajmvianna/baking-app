package edu.nanodegreeprojects.bakingapp;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
//import android.support.test.rule.ActivityTestRule;
import android.support.test.*;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class ExampleUnitTest {


    @Test
    public void addition_isCorrect() {

        // Click on decrement button
        onView((withId(R.id.cv_recipe_item)))
                .perform(click());

        onView(withText(R.string.ingredients_title)).perform(click());

        onView(withId(android.R.id.home));

        onView((withId(R.id.cv_recipe_item)))
                .perform(click());

        onView(withText(R.string.ingredients_title)).perform(click());


//
//        // Verify that the decrement button decreases the quantity by 1
//        onView(withId(R.id.quantity_text_view)).check(matches(withText("0")));
//
//        // Verify that the increment button also increases the total cost to $5.00
//        onView(withId(R.id.cost_text_view)).check(matches(withText("$0.00")));

    }
}