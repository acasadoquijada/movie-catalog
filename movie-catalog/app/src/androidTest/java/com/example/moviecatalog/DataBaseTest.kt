package com.example.moviecatalog

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moviecatalog.db.AppDataBase
import com.example.moviecatalog.db.ElementDAO
import com.example.moviecatalog.model.Element
import com.example.moviecatalog.util.getOrAwaitValue
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DataBaseTest {

    private lateinit var elementDao: ElementDAO
    private lateinit var db: AppDataBase

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDataBase::class.java
        ).build()
        elementDao = db.elementDAO()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetElements() {

        db.elementDAO().insert(getElement(1))
        db.elementDAO().insert(getElement(2))

        val watchList = db.elementDAO().getWatchList()

        assertThat(watchList.getOrAwaitValue()[0].name, equalTo(getElement(1).name))
    }

    @Test
    @Throws(Exception::class)
    fun getElementById() {

        val id: Long = 1

        db.elementDAO().insert(getElement(1))

        val watchListElement = db.elementDAO().getElementById(1)

        assertThat(watchListElement?.id, equalTo(id))
    }

    @Test
    @Throws(Exception::class)
    fun deleteElement() {

        db.elementDAO().insert(getElement(1))
        db.elementDAO().insert(getElement(2))

        db.elementDAO().delete(getElement(1))

        val watchList = db.elementDAO().getWatchList()

        assertEquals(1, watchList.getOrAwaitValue().size)
    }

    private fun getElement(id: Long): Element {
        val element = Element()
        element.id = id
        element.name = "fake name$id"
        element.overview = "fake overview$id"

        return element
    }
}
